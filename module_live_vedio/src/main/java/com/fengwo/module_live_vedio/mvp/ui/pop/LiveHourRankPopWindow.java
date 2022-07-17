package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.event.AttentionRefreshEvent;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.eventbus.AttentionChangeEvent;
import com.fengwo.module_live_vedio.eventbus.ChangeRoomEvent;
import com.fengwo.module_live_vedio.mvp.dto.LiveHourRankDto;
import com.fengwo.module_live_vedio.mvp.dto.MyHourDto;
import com.fengwo.module_live_vedio.mvp.dto.RankZhuboDto;
import com.fengwo.module_live_vedio.mvp.ui.activity.RankTopActivity;
import com.fengwo.module_live_vedio.mvp.ui.adapter.ZhuboRankAdapter;
import com.fengwo.module_live_vedio.utils.AttentionUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import razerdp.basepopup.BasePopupWindow;

import static com.fengwo.module_comment.utils.CommentUtils.isFastClick;

;
;

public class LiveHourRankPopWindow extends BasePopupWindow implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R2.id.recycleview)
    RecyclerView recycleview;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R2.id.root)
    RelativeLayout root;
    private Unbinder bind;

    private ZhuboRankAdapter mAdapter;
    private CompositeDisposable allRxBus;
    private MyHourDto myHourDto = null;
    private View v;
    private int mChannelId;

    @Override
    public View onCreateContentView() {
        v = createPopupById(R.layout.layout_live_hour_rank);
        bind = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDismiss() {
        super.onDismiss();
        release();
    }

    /**
     * 释放
     */
    private void release() {
        if (bind != null) bind.unbind();
        if (allRxBus != null && allRxBus.size() > 0) {
            allRxBus.clear();
        }
    }

    public LiveHourRankPopWindow(Context context, MyHourDto myHourDto, int channelId) {
        super(context);
        allRxBus = new CompositeDisposable();
        this.myHourDto = myHourDto;
        this.mChannelId = channelId;
        //        SmartRefreshLayoutUtils.setClassicsColor(getContext(), smartrefreshlayout, R.color.white, R.color.text_33);
        setPopupGravity(Gravity.BOTTOM);
        smartrefreshlayout.setOnRefreshListener(this);
        smartrefreshlayout.setEnableLoadMore(false);
        allRxBus.add(RxBus.get().toObservable(AttentionRefreshEvent.class).subscribe(event ->
                refreshAttentionStatus(event.refreshUid, event.isAttention)));
        allRxBus.add(RxBus.get().toObservable(ChangeRoomEvent.class).subscribe(event -> dismiss()));//关闭页面
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        getRank();
//        getZhuboData();
        setMyItemView(context);

        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ZhuboRankAdapter();
        recycleview.setAdapter(mAdapter);
        mAdapter.setAttentionListener((position, isChecked) -> {
            if (isFastClick()) return null;
            if (mAdapter.getData().get(position).isAttension == 0) {
                addAttention(mAdapter.getData().get(position).value);
            } else {
                delAttention(mAdapter.getData().get(position).value);
            }
            return null;
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (isFastClick()) {
                return;
            }
            int id = view.getId();
            if (id == R.id.btn_attention) {
                if (mAdapter.getData().get(position).isAttension == 0) {
                    addAttention(mAdapter.getData().get(position).value);
                } else {
                    delAttention(mAdapter.getData().get(position).value);
                }
            } else if (id == R.id.root) {
                if (mAdapter.getItem(position).liveStatus == 2) {
//                                            ArrayList<ZhuboDto> list = new ArrayList<>();
//                                            ZhuboDto zhuboDto = new ZhuboDto();
//                                            zhuboDto.channelId = mAdapter.getItem(position).value;
//                                            list.add(zhuboDto);
//                                            LivingRoomActivity.start(getContext(), list, 0, true);

                    RxBus.get().post(new ChangeRoomEvent(mAdapter.getItem(position).value + ""));
                } else
                    ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, mAdapter.getData().get(position).value);
            }
        });
        View v = LayoutInflater.from(getContext()).inflate(R.layout.item_base_empty_view, null, false);
        mAdapter.setEmptyView(v);
    }

    private void setMyItemView(Context context) {
        TextView tv_name = v.findViewById(R.id.tv_name);
        tv_name.setText(myHourDto.getNickname().toString());
        int levelRes;
        ImageView iv_level1 = v.findViewById(R.id.iv_level1);
        if (TextUtils.isEmpty(myHourDto.getUserLevel()) || myHourDto.getUserLevel().equals("null")) {
            iv_level1.setVisibility(View.GONE);
        } else {
            levelRes = ImageLoader.getResId("login_ic_v" + myHourDto.getUserLevel(), R.drawable.class);
            iv_level1.setImageResource(levelRes);
        }
        ImageView iv_level2 = v.findViewById(R.id.iv_level2);
        if (null != myHourDto.getAnchorLevel() + "" && !myHourDto.getAnchorLevel().toString().equals("null")) {
            levelRes = ImageLoader.getResId("login_ic_type3_v" + myHourDto.getAnchorLevel(), R.drawable.class);
            iv_level2.setImageResource(levelRes);
        } else
            iv_level2.setVisibility(View.GONE);


        TextView btn_attention = v.findViewById(R.id.btn_attention);
        btn_attention.setVisibility(View.GONE);

        ImageView iv_header = v.findViewById(R.id.iv_header);
        ImageLoader.loadImg(iv_header, myHourDto.getHeadImg());

        TextView tv_context = v.findViewById(R.id.tv_context);
        tv_context.setText(DataFormatUtils.formatNumbers(myHourDto.getScore()));

        TextView tv_rank = v.findViewById(R.id.tv_rank);

        ImageView iv_rank = v.findViewById(R.id.iv_rank);

        ImageView im_ph_back = v.findViewById(R.id.im_ph_back);

        ImageView im_dw = v.findViewById(R.id.im_dw);


        if (myHourDto.getRank() > 50 || myHourDto.getRank() == 0) {
            tv_rank.setText("未上榜");
        } else {
            switch (myHourDto.getRank()) {
                case 1:
                    iv_rank.setVisibility(View.VISIBLE);
                    iv_rank.setImageResource(R.drawable.pic_hour_one);
                    tv_rank.setVisibility(View.INVISIBLE);
                    im_ph_back.setImageResource(R.drawable.pic_xsb_one);
                    im_dw.setImageResource(R.drawable.pic_dw_one);
                    break;
                case 2:
                    iv_rank.setVisibility(View.VISIBLE);
                    iv_rank.setImageResource(R.drawable.pic_hour_two);
                    tv_rank.setVisibility(View.INVISIBLE);
                    im_ph_back.setImageResource(R.drawable.pic_xsb_two);
                    im_ph_back.setImageResource(R.drawable.pic_dw_two);
                    break;
                case 3:
                    iv_rank.setVisibility(View.VISIBLE);
                    iv_rank.setImageResource(R.drawable.pic_hour_three);
                    tv_rank.setVisibility(View.INVISIBLE);
                    im_ph_back.setImageResource(R.drawable.pic_xsb_three);
                    im_ph_back.setImageResource(R.drawable.pic_dw_three);
                    break;
                default:
                    iv_rank.setVisibility(View.INVISIBLE);
                    tv_rank.setText(myHourDto.getRank() + "");
                    im_ph_back.setImageResource(0);
                    im_ph_back.setImageResource(0);

            }


        }
        TextView tv_gd = v.findViewById(R.id.tv_gd);
        tv_gd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RankTopActivity.class);
                context.startActivity(intent);
            }
        });


    }

    private void refreshAttentionStatus(int uid, int isAttention) {
        if (mAdapter != null && mAdapter.getData().size() > 0) {
            for (RankZhuboDto item : mAdapter.getData()) {
                if (item.value == uid) {
                    item.isAttension = isAttention;

                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private void getZhuboData() {
        allRxBus.add(new RetrofitUtils().createApi(LiveApiService.class).getLiveHourRank(mChannelId)
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<List<LiveHourRankDto>>>() {
                    @Override
                    public void _onNext(HttpResult<List<LiveHourRankDto>> data) {

                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }


    public void getRank() {
        allRxBus.add(new RetrofitUtils().createApi(LiveApiService.class).getZhuboRank(RankTopActivity.TYPE_HOURSE)
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<List<RankZhuboDto>>>() {
                    @Override
                    public void _onNext(HttpResult<List<RankZhuboDto>> data) {
                        if (smartrefreshlayout != null)
                            smartrefreshlayout.closeHeaderOrFooter();
                        if (recycleview == null) return;
                        if (data.isSuccess()) {
                            mAdapter.setNewData(data.data);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    private void delAttention(int id) {
        AttentionUtils.delAttention(id, new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                ToastUtils.showShort(getContext(), data.description);
                if (data.isSuccess()) {
                    RxBus.get().post(new AttentionRefreshEvent(id, 0));
                    RxBus.get().post(new AttentionChangeEvent(false, false, 0, id + ""));
                }
            }

            @Override
            public void _onError(String msg) {
                ToastUtils.showShort(getContext(), msg);
            }
        });
    }

    private void addAttention(int id) {
        AttentionUtils.addAttention(id, new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                ToastUtils.showShort(getContext(), data.description);
                if (data.isSuccess()) {
                    RxBus.get().post(new AttentionRefreshEvent(id, 1));
                    RxBus.get().post(new AttentionChangeEvent(true, false, 1, id + ""));
                }
            }

            @Override
            public void _onError(String msg) {
                ToastUtils.showShort(getContext(), msg);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //release();
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getRank();
    }
}
