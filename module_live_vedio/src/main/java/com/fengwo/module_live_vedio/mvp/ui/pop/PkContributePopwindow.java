package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.PkContributeInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;
import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/7/14
 */
public class PkContributePopwindow extends BasePopupWindow {

    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.iv_guard_two)
    ImageView ivGuardTwo;
    @BindView(R2.id.iv_header_two)
    CircleImageView ivHeaderTwo;
    @BindView(R2.id.tv_name_two)
    TextView tvNameTwo;
    @BindView(R2.id.iv_guard_one)
    ImageView ivGuardOne;
    @BindView(R2.id.iv_header_one)
    CircleImageView ivHeaderOne;
    @BindView(R2.id.tv_name_one)
    TextView tvNameOne;
    @BindView(R2.id.ll_one)
    LinearLayout llOne;
    @BindView(R2.id.iv_guard_three)
    ImageView ivGuardThree;
    @BindView(R2.id.iv_header_three)
    CircleImageView ivHeaderThree;
    @BindView(R2.id.tv_name_three)
    TextView tvNameThree;
    @BindView(R2.id.rv_hot_gift)
    RecyclerView rvHotGift;
    @BindView(R2.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R2.id.fl_content)
    FrameLayout flContent;
    @BindView(R2.id.iv_header_mine)
    CircleImageView ivHeaderMine;
    @BindView(R2.id.tv_contribute_mine)
    TextView tvContributeMine;
    @BindView(R2.id.ll_header_bottom)
    LinearLayout llHeaderBottom;
    @BindView(R2.id.tv_empty_bottom)
    TextView tvEmptyBottom;
    @BindView(R2.id.tv_send)
    GradientTextView tvSend;
    @BindView(R2.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R2.id.rl_content)
    RelativeLayout rl_content;

    private int hostId;
    private CompositeDisposable disposable;
    private Context context;
    private boolean isHost;
    private boolean isWe;

    public PkContributePopwindow(Context context, int hostId,boolean isHost,boolean isWe) {
        super(context);
        this.context = context;
        this.isHost = isHost;
        this.isWe = isWe;
        disposable = new CompositeDisposable();
        setPopupGravity(Gravity.BOTTOM);
        getData(hostId);
    }

    private void getData(int hostId) {
        disposable.add(new RetrofitUtils().createApi(LiveApiService.class).getPkContributeInfo(hostId)
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<PkContributeInfo>>() {
                    @Override
                    public void _onNext(HttpResult<PkContributeInfo> data) {

                        refreshView(data.data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    @SuppressLint("ResourceAsColor")
    private void refreshView(PkContributeInfo data) {
        int rankSize = data.getContributionRank().size();
        if (data.getContributionRank()!=null&&rankSize>0){
            ivEmpty.setVisibility(View.GONE);
            rl_content.setVisibility(View.VISIBLE);
            llHeaderBottom.setVisibility(View.VISIBLE);
            tvEmptyBottom.setVisibility(View.GONE);
            ImageLoader.loadImg(ivHeaderMine,data.getHeadImg(),R.drawable.ic_default_deep);
            tvContributeMine.setText("我的贡献："+data.getUserContribution()+"花钻");
            tvNameOne.setText(data.getContributionRank().get(0).getNickname());
            tvNameOne.setTextColor(context.getResources().getColor(R.color.text_white));
            ImageLoader.loadImg(ivHeaderOne,data.getContributionRank().get(0).getHeadImg(),R.drawable.ic_default_deep);
            ivGuardOne.setVisibility(data.getContributionRank().get(0).isIsGuard()? View.VISIBLE:View.GONE);
            if (rankSize>1) {
                tvNameTwo.setText(data.getContributionRank().get(1).getNickname());
                tvNameTwo.setTextColor(context.getResources().getColor(R.color.text_white));
                ImageLoader.loadImg(ivHeaderTwo,data.getContributionRank().get(1).getHeadImg(),R.drawable.ic_default_deep);
                ivGuardTwo.setVisibility(data.getContributionRank().get(1).isIsGuard()? View.VISIBLE:View.GONE);
            }
            if (rankSize>2){
                tvNameThree.setText(data.getContributionRank().get(2).getNickname());
                tvNameThree.setTextColor(context.getResources().getColor(R.color.text_white));
                ImageLoader.loadImg(ivHeaderThree,data.getContributionRank().get(2).getHeadImg(),R.drawable.ic_default_deep);
                ivGuardThree.setVisibility(data.getContributionRank().get(2).isIsGuard()? View.VISIBLE:View.GONE);
            }
            rvHotGift.setLayoutManager(new GridLayoutManager(context,4));
            ContributeGiftAdapter adapter = new ContributeGiftAdapter(data.getContributionGift());
            rvHotGift.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (onPkContributeListener!=null){
                        dismiss();
                        onPkContributeListener.onGiftClick(((PkContributeInfo.ContributionGiftBean)adapter.getData().get(position)).getGiftId());
                    }
                }
            });
        }else {
            rl_content.setVisibility(View.GONE);
            ivEmpty.setVisibility(View.VISIBLE);
            llHeaderBottom.setVisibility(View.GONE);
            tvEmptyBottom.setVisibility(View.VISIBLE);
        }
        if (isHost||!isWe){
            llBottom.setVisibility(View.GONE);
        }
        tvTitle.setText(isWe?"我方本场PK贡献榜":"对方本场PK贡献榜");
    }

    @OnClick(R2.id.tv_send)
    void onClick(){
        if (onPkContributeListener!=null){
            dismiss();
            onPkContributeListener.onSend();
        }
    }

    @Override
    public View onCreateContentView() {
        View view = createPopupById(R.layout.pop_pk_contribute);
        ButterKnife.bind(this, view);
        return view;
    }

    OnPkContributeListener onPkContributeListener;

    public void setOnPkContributeListener(OnPkContributeListener onPkContributeListener) {
        this.onPkContributeListener = onPkContributeListener;
    }

    public interface OnPkContributeListener {
        void onSend();
        void onGiftClick(int giftId);
    }

    public static  class ContributeGiftAdapter extends BaseQuickAdapter<PkContributeInfo.ContributionGiftBean, BaseViewHolder> {

        public ContributeGiftAdapter(@Nullable List<PkContributeInfo.ContributionGiftBean> data) {
            super(R.layout.item_pk_contribute_gift, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, PkContributeInfo.ContributionGiftBean item) {
            ImageLoader.loadImg(helper.getView(R.id.iv_gift),item.getGiftIcon());
            helper.setText(R.id.tv_gift_name,item.getGiftName());
            helper.setText(R.id.tv_gift_num,"*"+item.getGiftNumber());
        }
    }
}
