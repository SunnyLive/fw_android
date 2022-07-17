/*
 *
 * 前辈的代码，这个页面应该就是i撩主播室
 *
 * 当用户点击右下角的礼物记录按钮时
 *
 * 会打开这个弹框  展示用户的礼物礼物记录信息
 *
 * 不用感谢我  我是雷锋 2020-10-28
 *
 * */
package com.fengwo.module_flirt.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.RxHttpUtil;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.adapter.ChatRoomPopListAdapter;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.GiftRecordPriceBean;
import com.fengwo.module_flirt.bean.HostOrderBean;
import com.fengwo.module_login.utils.UserManager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.RequestBody;
import razerdp.basepopup.BasePopupWindow;

import static com.fengwo.module_flirt.adapter.ChatRoomPopListAdapter.IMPRESSION;
import static com.fengwo.module_flirt.adapter.ChatRoomPopListAdapter.REWARD;
import static com.fengwo.module_flirt.adapter.ChatRoomPopListAdapter.TOTAL;

/**
 * 聊天室 底部弹窗
 */


public class ChatRoomBottomPopwindow extends BasePopupWindow implements OnRefreshListener, OnLoadMoreListener {

    private Unbinder bind;

    @BindView(R2.id.tv_price)
    TextView mTvPrice;      //总计
    @BindView(R2.id.rg_choose_type)
    RadioGroup mRgChooseType;   //记录类型选择

    @BindView(R2.id.sr_refresh)
    SmartRefreshLayout srRefresh;
    @BindView(R2.id.rv_include)
    RecyclerView rvInclude;

    @BindView(R2.id.rg_child_choose)
    RadioGroup mRgChildChoose; //   子选项  今日 本月的容器

    @BindView(R2.id.ll_show_price)
    View mLinePrice;          //line

    private ChatRoomPopListAdapter mAdapter;

    private int page = 1;
    private Context context;
    private int mAnchorId;

    private FlirtApiService mFlirtApiService;

    private final String [] mEmptyText = {
            "本日还没有收到打赏哦～",
            "本月还没有收到打赏哦～",
            "本日还没用户与你开启缘分哦～",
            "本月还没用户与你开启缘分哦～"};

    @Override
    public View onCreateContentView() { //布局
        View v = createPopupById(R.layout.popwindow_chatroom_bottom);
        setHeight(ScreenUtils.getScreenHeight(getContext()) / 2);
        bind = ButterKnife.bind(this, v);
        mFlirtApiService = new RetrofitUtils().createWenboApi(FlirtApiService.class);
        mAnchorId = UserManager.getInstance().getUser().id;
        return v;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }

    public ChatRoomBottomPopwindow(Context context) {
        super(context);
        this.context = context;
        setPopupGravity(Gravity.BOTTOM);
        initUI();
    }

    private void initUI() {
        SmartRefreshLayoutUtils.setTransparentBlackText(getContext(), srRefresh);
        srRefresh.setOnRefreshListener(this);
        srRefresh.setOnLoadMoreListener(this);
        rvInclude.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ChatRoomPopListAdapter();
        setEmptyView();
        rvInclude.setAdapter(mAdapter);
        //
        // 记录类型切换
        //
        mRgChooseType.setOnCheckedChangeListener((group, checkedId) -> {
            mRgChildChoose.clearCheck();
            mRgChildChoose.getChildAt(0).performClick();
        });

        //
        // 今日 本月的选择事件
        //
        mRgChildChoose.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == -1) return;
            View checkedView = findViewById(checkedId);
            if (checkedView != null && checkedView instanceof RadioButton) {
                if (!((RadioButton) checkedView).isChecked()) {
                    //此处是某个选中按钮被取消的回调，在调用check方法修改选中的时候会触发
                    return;
                }
            }
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            page = 1;
            requestServerData();
        });
        mRgChooseType.getChildAt(0).performClick();

    }


    /**
     * 按钮切换需要请求的接口顺序
     */
    private void requestServerData() {
        int parentSearchId = mRgChooseType.getCheckedRadioButtonId();
        int childSearchId = mRgChildChoose.getCheckedRadioButtonId();

        Drawable mDrawableRecord = ContextCompat.getDrawable(getContext(), R.drawable.icon_record);
        Drawable mDrawableImpression = ContextCompat.getDrawable(getContext(), R.drawable.icon_move_house_heart);

        mRgChildChoose.setVisibility(View.VISIBLE);
        mLinePrice.setVisibility(View.VISIBLE);
        mTvPrice.setVisibility(View.VISIBLE);
        if (parentSearchId == R.id.rb_choose_type1) {   //打赏榜单
            mAdapter.setChooseType(REWARD);
            ((RadioButton) mRgChildChoose.getChildAt(0)).setText("今日打赏榜");
            ((RadioButton) mRgChildChoose.getChildAt(1)).setText("本月打赏榜");
            if (childSearchId == R.id.rb_child_choose1) {
                getListReward(1);
                getRewardTotal(1);
                setEmptyText(mEmptyText[0]);
            } else if (childSearchId == R.id.rb_child_choose2) {
                getListReward(2);
                getRewardTotal(2);
                setEmptyText(mEmptyText[1]);
            }
            mTvPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, mDrawableRecord, null);
        } else if (parentSearchId == R.id.rb_choose_type2) {   //印象榜单
            mAdapter.setChooseType(IMPRESSION);
            ((RadioButton) mRgChildChoose.getChildAt(0)).setText("今日印象榜");
            ((RadioButton) mRgChildChoose.getChildAt(1)).setText("本月印象榜");
            if (childSearchId == R.id.rb_child_choose1) {
                getListImpression(1);
                getImpressionTotal(1);
                setEmptyText(mEmptyText[2]);
            } else if (childSearchId == R.id.rb_child_choose2) {
                getListImpression(2);
                getImpressionTotal(2);
                setEmptyText(mEmptyText[3]);
            }
            mTvPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, mDrawableImpression, null);
        } else if (parentSearchId == R.id.rb_choose_type3) {   //收礼明细
            mAdapter.setChooseType(TOTAL);
            mRgChildChoose.setVisibility(View.GONE);
            mLinePrice.setVisibility(View.GONE);
            mTvPrice.setVisibility(View.GONE);
            //getAllPrice();
            setEmptyText(mEmptyText[0]);
            getRecord();
        }
    }


    public void addOnClickListener(OnClickListener listener) {
        onClickListener = listener;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        requestServerData();
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        requestServerData();
    }


    public interface OnClickListener {
        void sure();
    }

    public OnClickListener onClickListener;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bind != null) bind.unbind();
        RxHttpUtil.clearHttpRequest();
    }


    /**
     * 获取礼物总收益价格
     * <p>
     * 收礼明细的总计接口不变 还是当前这个接口
     */
    public void getAllPrice() {
        RxHttpUtil.addNet(mFlirtApiService.getGiftRecordPrice()
                .compose(RxHttpUtil.handleResult())
                .subscribeWith(new LoadingObserver<GiftRecordPriceBean>() {
                    @Override
                    public void _onNext(GiftRecordPriceBean data) {
                        mTvPrice.setText("本场总计：" + data.total);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context, msg);
                    }
                })
        );
    }


    /**
     * 以前的打赏 印象 都是这个接口
     * <p>
     * 现在只有收礼明细用这个接口
     */
    private void getRecord() {
        RequestBody build = new WenboParamsBuilder()
                .put("current", page + "")
                .put("size", 20 + "")
                .put("anchorId", mAnchorId + "")
                .put("type", "")
                .build();
        RxHttpUtil.addNet(mFlirtApiService.getGiftDetail(build)
                .compose(RxHttpUtil.handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<HostOrderBean>>() {
                    @Override
                    public void _onNext(BaseListDto<HostOrderBean> data) {
                        setData(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context, msg);
                    }
                })
        );
    }


    /**
     * 现在的 打赏榜单
     * <p>
     * /api/bunko/v2/reward
     * <p>
     * type  1 是今日 2 是本期
     */
    private void getListReward(int type) {
        RxHttpUtil.addNet(mFlirtApiService.getListReward(getRequestBody(type))
                .compose(RxHttpUtil.handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<HostOrderBean>>() {
                    @Override
                    public void _onNext(BaseListDto<HostOrderBean> data) {
                        setData(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context, msg);
                    }
                }));
    }


    /**
     * 打赏榜单总计
     *
     * @param type
     */
    private void getRewardTotal(int type) {
        RxHttpUtil.addNet(mFlirtApiService.getRewardTotal(getRequestBody(type))
                .compose(RxHttpUtil.handleResult())
                .subscribeWith(new LoadingObserver<GiftRecordPriceBean>() {
                    @Override
                    public void _onNext(GiftRecordPriceBean data) {
                        mTvPrice.setText((type == 1 ? "今日" : "本月") + "总计：" + data.total);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context, msg);
                    }
                })
        );

    }


    /**
     * 印象榜单
     * <p>
     * /api/bunko/v2/impression
     * <p>
     * type  1 是今日 2 是本期
     */
    private void getListImpression(int type) {
        RxHttpUtil.addNet(mFlirtApiService.getListImpression(getRequestBody(type))
                .compose(RxHttpUtil.handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<HostOrderBean>>() {
                    @Override
                    public void _onNext(BaseListDto<HostOrderBean> data) {
                        setData(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context, msg);
                    }
                }));
    }


    /**
     * 印象榜单的总计
     *
     * @param type
     */
    private void getImpressionTotal(int type) {
        RxHttpUtil.addNet(mFlirtApiService.getImpressionTotal(getRequestBody(type))
                .compose(RxHttpUtil.handleResult())
                .subscribeWith(new LoadingObserver<GiftRecordPriceBean>() {
                    @Override
                    public void _onNext(GiftRecordPriceBean data) {
                        mTvPrice.setText((type == 1 ? "今日" : "本月") + "总计：" + data.total);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context, msg);
                    }
                })
        );
    }


    /**
     * 简单封装一下 打赏 印象接口的请求头
     *
     * @param type 自选项 1 今日 2 本期
     * @return
     */
    private RequestBody getRequestBody(int type) {
        RequestBody build = new WenboParamsBuilder()
                .put("current", page + "")
                .put("anchorId", mAnchorId + "")
                .put("size", "20")
                .put("type", type + "")
                .build();
        return build;
    }


    private void setData(BaseListDto<HostOrderBean> data) {
        srRefresh.closeHeaderOrFooter();
        if (page == 1) {
            mAdapter.setNewData(data.records);
        } else {
            mAdapter.addData(data.records);
        }

        boolean isEmpty = mAdapter.getData().isEmpty();
        srRefresh.setEnableLoadMore(!isEmpty);
        srRefresh.setEnableRefresh(!isEmpty);
    }

    private void setEmptyView() {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_chat_record_empty, null, false);
        mAdapter.setEmptyView(v);
    }



    private void setEmptyText(String msg){
        TextView emptyText = mAdapter.getEmptyView()
                .findViewById(R.id.iv_chat_room_empty);
        emptyText.setText(msg);
    }

}
