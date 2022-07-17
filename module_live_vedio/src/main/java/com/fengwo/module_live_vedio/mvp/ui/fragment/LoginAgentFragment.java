package com.fengwo.module_live_vedio.mvp.ui.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WebViewActivity;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.BrokerRankDto;
import com.fengwo.module_live_vedio.mvp.dto.ProfitDto;
import com.fengwo.module_live_vedio.mvp.ui.activity.AgentInviteActivity;
import com.fengwo.module_live_vedio.mvp.ui.activity.AgentListActivity;
import com.fengwo.module_live_vedio.mvp.ui.pop.BrokenSharePop;
import com.fengwo.module_live_vedio.utils.AttentionUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = ArouterApi.LIVE_AGENT_FRAGMENT)
public class LoginAgentFragment extends BaseMvpFragment implements OnRefreshListener {

    LiveApiService service;
    @BindView(R2.id.broker_one_avatar)
    CircleImageView brokerOneAvatar;
    @BindView(R2.id.broker_one_name)
    TextView brokerOneName;
    @BindView(R2.id.broker_two_avatar)
    CircleImageView brokerTwoAvatar;
    @BindView(R2.id.broker_two_name)
    TextView brokerTwoName;
    @BindView(R2.id.broker_three_avatar)
    CircleImageView brokerThreeAvatar;
    @BindView(R2.id.broker_three_name)
    TextView brokerThreeName;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;

    @BindView(R2.id.tv_yaoqing)
    TextView tvYaoqing;
    @BindView(R2.id.tv_gongxiang)
    TextView tvGongxiang;
    @BindView(R2.id.tv_xiaji)
    TextView tvXiaji;
    @BindView(R2.id.tv_shuali)
    TextView tvShuali;
    @BindView(R2.id.tv_fengmizhi_two)
    TextView tvFengmizhiTwo;
    @BindView(R2.id.tv_fengmizhi_one)
    TextView tvFengmizhiOne;
    @BindView(R2.id.tv_fengmizhi_three)
    TextView tvFengmizhiThree;

    ArrayList<BrokerRankDto> rankList;

    private final DecimalFormat df = new DecimalFormat("###.##");

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int setContentView() {
        return R.layout.live_fragment_loginagent;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        service = new RetrofitUtils().createApi(LiveApiService.class);
        smartrefreshlayout.setOnRefreshListener(this);
        smartrefreshlayout.setEnableLoadMore(false);
        SmartRefreshLayoutUtils.setTransparentBlackText(getActivity(), smartrefreshlayout);
        getBrokenTop3();
        getProfitToday();

    }

    private void getBrokenTop3() {
        service.getBrokerRank("1,3")
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<BaseListDto<BrokerRankDto>>>() {
                    @Override
                    public void _onNext(HttpResult<BaseListDto<BrokerRankDto>> data) {
                        if (data.isSuccess()) {
                            setRank(data.data.records);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    private void setRank(ArrayList<BrokerRankDto> records) {
        rankList = records;
        for (int i = 0; i < records.size(); i++) {
            final int j = i;
            BrokerRankDto data = records.get(i);
            switch (i) {
                case 0:
                    brokerOneName.setText(data.getUserNickname());
                    ImageLoader.loadImg(brokerOneAvatar, data.getUserHeadImg());
                    tvFengmizhiOne.setText(DataFormatUtils.formatNumbers(Double.parseDouble(data.getTotalDivide())) + "花蜜值");
                    break;
                case 1:
                    brokerTwoName.setText(records.get(i).getUserNickname());
                    ImageLoader.loadImg(brokerTwoAvatar, data.getUserHeadImg());
                    tvFengmizhiTwo.setText(DataFormatUtils.formatNumbers(Double.parseDouble(data.getTotalDivide())) + "花蜜值");
                    break;
                case 2:
                    brokerThreeName.setText(records.get(i).getUserNickname());
                    ImageLoader.loadImg(brokerThreeAvatar, data.getUserHeadImg());
                    tvFengmizhiThree.setText(DataFormatUtils.formatNumbers(Double.parseDouble(data.getTotalDivide())) + "花蜜值");
                    break;
            }
        }
    }

    private void getProfitToday() {
        service.getProfitToday()
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult<ProfitDto>>() {
                    @Override
                    public void _onNext(HttpResult<ProfitDto> data) {
                        if (data.isSuccess()) {
                            setProfit(data.data);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    private void setProfit(ProfitDto data) {
        tvGongxiang.setText(DataFormatUtils.formatNumbers(data.getSuperiorDivide()));
        StyleSpan span1 = new StyleSpan(Typeface.BOLD);
        SpannableString spanString1 = new SpannableString(data.getInviteCount() + "\n邀请人数");
        spanString1.setSpan(span1, 0, (data.getInviteCount() + "").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvYaoqing.setText(spanString1);
        StyleSpan span2 = new StyleSpan(Typeface.BOLD);
        SpannableString spanString2 = new SpannableString(data.getInviteProfitCount() + "\n贡献人数");
        spanString2.setSpan(span2, 0, (data.getInviteCount() + "").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvXiaji.setText(spanString2);
        StyleSpan span3 = new StyleSpan(Typeface.BOLD);
        SpannableString spanString3 = new SpannableString(data.getSuperiorGiftTotal() + "\n刷礼总额");
        spanString3.setSpan(span3, 0, (data.getInviteCount() + "").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvShuali.setText(spanString3);

    }

    @OnClick({R2.id.iv_today_share, R2.id.ll_agent_list, R2.id.tv_yaoqing, R2.id.iv_login_agent_ad})
    public void onViewClicked(View view) {
        if (isFastClick()) return;
        int id = view.getId();
        if (id == R.id.iv_today_share) {
            new BrokenSharePop(getActivity(), getActivity().getSupportFragmentManager()).showPopupWindow();
//            new ShareAction(getActivity()).withText("hello").setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN)
//                    .setCallback(new UMShareListener() {
//                        @Override
//                        public void onStart(SHARE_MEDIA share_media) {
//
//                        }
//
//                        @Override
//                        public void onResult(SHARE_MEDIA share_media) {
//
//                        }
//
//                        @Override
//                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//
//                        }
//
//                        @Override
//                        public void onCancel(SHARE_MEDIA share_media) {
//
//                        }
//                    }).open();
        } else if (id == R.id.ll_agent_list) {
            startActivity(AgentListActivity.class);
        } else if (id == R.id.tv_yaoqing) {
            startActivity(AgentInviteActivity.class);
        } else if (id == R.id.iv_login_agent_ad) {
            BrowserActivity.start(getActivity(),"申请成为经纪人公会", Constants.UNION_INTRO);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getBrokenTop3();
        getProfitToday();
        smartrefreshlayout.closeHeaderOrFooter();
    }

}
