package com.fengwo.module_live_vedio.mvp.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.BrokerRankDto;
import com.fengwo.module_live_vedio.mvp.dto.ProfitDto;
import com.fengwo.module_live_vedio.mvp.ui.pop.BrokenSharePop;
import com.fengwo.module_live_vedio.utils.AttentionUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AgentFragment extends BaseMvpFragment implements OnRefreshListener {

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
    @BindView(R2.id.broker_one_follow)
    TextView brokerOneFollow;
    @BindView(R2.id.broker_two_follow)
    TextView brokerTwoFollow;
    @BindView(R2.id.broker_three_follow)
    TextView brokerThreeFollow;

    ArrayList<BrokerRankDto> rankList;

    private final DecimalFormat df = new DecimalFormat("###.##");

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int setContentView() {
        return R.layout.live_fragment_agent;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().setTitle("全民经纪人").setTitleColor(R.color.text_33).build();
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
                    tvFengmizhiOne.setText("花蜜值：" + data.getTotalDivide());
                    brokerOneFollow.setText(data.getIsAttension() > 0 ? "已关注" : "关注");
                    brokerOneFollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (data.getIsAttension() > 0) {
                                AttentionUtils.delAttention(data.getId(), new LoadingObserver<HttpResult>() {
                                    @Override
                                    public void _onNext(HttpResult data) {
                                        if (data.isSuccess()) {
                                            brokerOneFollow.setText("关注");
                                            brokerOneFollow.setTextColor(getResources().getColor(R.color.purple_9966ff));
                                            brokerOneFollow.setBackground(getResources().getDrawable(R.drawable.shape_white_cornerbig));
                                            rankList.get(j).setIsAttension(0);
                                        }
                                    }

                                    @Override
                                    public void _onError(String msg) {

                                    }
                                });
                            } else {
                                AttentionUtils.addAttention(data.getId(), new LoadingObserver<HttpResult>() {
                                    @Override
                                    public void _onNext(HttpResult data) {
                                        if (data.isSuccess()) {
                                            brokerOneFollow.setText("已关注");
                                            brokerOneFollow.setTextColor(getResources().getColor(R.color.white));
                                            brokerOneFollow.setBackground(getResources().getDrawable(R.drawable.shape_white_cornerbig_selected));
                                            rankList.get(j).setIsAttension(1);
                                        }
                                    }

                                    @Override
                                    public void _onError(String msg) {

                                    }
                                });
                            }
                        }
                    });
                    break;
                case 1:
                    brokerTwoName.setText(records.get(i).getUserNickname());
                    ImageLoader.loadImg(brokerTwoAvatar, data.getUserHeadImg());
                    tvFengmizhiTwo.setText("花蜜值：" + data.getTotalDivide());
                    brokerTwoFollow.setText(data.getIsAttension() > 0 ? "已关注" : "关注");
                    brokerTwoFollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (data.getIsAttension() > 0) {
                                AttentionUtils.delAttention(data.getId(), new LoadingObserver<HttpResult>() {
                                    @Override
                                    public void _onNext(HttpResult data) {
                                        if (data.isSuccess()) {
                                            brokerTwoFollow.setText("关注");
                                            brokerTwoFollow.setTextColor(getResources().getColor(R.color.purple_9966ff));
                                            brokerTwoFollow.setBackground(getResources().getDrawable(R.drawable.shape_white_cornerbig));
                                            rankList.get(j).setIsAttension(0);
                                        }
                                    }

                                    @Override
                                    public void _onError(String msg) {

                                    }
                                });
                            } else {
                                AttentionUtils.addAttention(data.getId(), new LoadingObserver<HttpResult>() {
                                    @Override
                                    public void _onNext(HttpResult data) {
                                        if (data.isSuccess()) {
                                            brokerTwoFollow.setText("已关注");
                                            brokerTwoFollow.setTextColor(getResources().getColor(R.color.white));
                                            brokerTwoFollow.setBackground(getResources().getDrawable(R.drawable.shape_white_cornerbig_selected));
                                            rankList.get(j).setIsAttension(1);
                                        }
                                    }

                                    @Override
                                    public void _onError(String msg) {

                                    }
                                });
                            }
                        }
                    });
                    break;
                case 2:
                    brokerThreeName.setText(records.get(i).getUserNickname());
                    ImageLoader.loadImg(brokerThreeAvatar, data.getUserHeadImg());
                    tvFengmizhiThree.setText("花蜜值：" + data.getTotalDivide());
                    brokerThreeFollow.setText(data.getIsAttension() > 0 ? "已关注" : "关注");
                    brokerThreeFollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (data.getIsAttension() > 0) {
                                AttentionUtils.delAttention(data.getId(), new LoadingObserver<HttpResult>() {
                                    @Override
                                    public void _onNext(HttpResult data) {
                                        if (data.isSuccess()) {
                                            brokerThreeFollow.setText("关注");
                                            brokerThreeFollow.setTextColor(getResources().getColor(R.color.purple_9966ff));
                                            brokerThreeFollow.setBackground(getResources().getDrawable(R.drawable.shape_white_cornerbig));
                                            rankList.get(j).setIsAttension(0);
                                        }
                                    }

                                    @Override
                                    public void _onError(String msg) {

                                    }
                                });
                            } else {
                                AttentionUtils.addAttention(data.getId(), new LoadingObserver<HttpResult>() {
                                    @Override
                                    public void _onNext(HttpResult data) {
                                        if (data.isSuccess()) {
                                            brokerThreeFollow.setText("已关注");
                                            brokerThreeFollow.setTextColor(getResources().getColor(R.color.white));
                                            brokerThreeFollow.setBackground(getResources().getDrawable(R.drawable.shape_white_cornerbig_selected));
                                            rankList.get(j).setIsAttension(0);
                                            rankList.get(j).setIsAttension(1);
                                        }
                                    }

                                    @Override
                                    public void _onError(String msg) {

                                    }
                                });
                            }
                        }
                    });
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
        tvYaoqing.setText(data.getInviteCount() + "");
        tvGongxiang.setText(df.format(data.getSuperiorDivide()));
        tvXiaji.setText(data.getInviteProfitCount() + "");
        tvShuali.setText(data.getSuperiorGiftTotal() + "");
    }

    @OnClick({R2.id.iv_today_share})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.iv_today_share) {
            new BrokenSharePop(getActivity(),getFragmentManager()).showPopupWindow();

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
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getBrokenTop3();
        getProfitToday();
        smartrefreshlayout.closeHeaderOrFooter();
    }

}
