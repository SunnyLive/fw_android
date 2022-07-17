package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.event.PaySuccessEvent;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.eventbus.ChangeRoomEvent;
import com.fengwo.module_live_vedio.eventbus.ShowRechargePopEvent;
import com.fengwo.module_live_vedio.mvp.dto.GiftPiaopingDto;
import com.fengwo.module_live_vedio.mvp.dto.PopoDto;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.shinichi.library.tool.ui.ToastUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import razerdp.basepopup.BasePopupWindow;

public class ToutiaoInfoPopwindow extends BasePopupWindow {

    @Autowired
    UserProviderService service;

    @BindView(R2.id.tv_time)
    TextView tvTime;
    @BindView(R2.id.iv_close)
    ImageView ivClose;
    @BindView(R2.id.iv_header_send)
    CircleImageView ivHeaderSend;
    @BindView(R2.id.iv_header_receive)
    CircleImageView ivHeaderReceive;
    @BindView(R2.id.tv_content)
    TextView tvContent;
    @BindView(R2.id.btn_ding)
    TextView btnDing;
    @BindView(R2.id.tv_send_name)
    TextView tvSend;
    @BindView(R2.id.tv_receive_name)
    TextView tvReceiveName;
    @BindView(R2.id.tv_price)
    TextView tvPrice;
    @BindView(R2.id.btn_submit)
    View btnSubmit;
    @BindView(R2.id.tv_rule)
    TextView tvRule;

    private  String nickname ;

    private  GiftPiaopingDto giftModel;
    private String channelId;
    private boolean isClick = false;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setChannelId(String channelId) {
        if (TextUtils.equals(channelId, String.valueOf(giftModel.getAnchorUserId()))) {
            btnSubmit.setVisibility(View.GONE);
        } else {
            btnSubmit.setVisibility(View.VISIBLE);
        }
    }

    public ToutiaoInfoPopwindow(Context context, GiftPiaopingDto model, String anchorId,String nickname) {
        super(context);
        setPopupGravity(Gravity.CENTER);

        ButterKnife.bind(this, getContentView());
        ARouter.getInstance().inject(this);
        initView(model, anchorId,nickname);

    }

    public void initView(GiftPiaopingDto model, String anchorId,String nickname) {
        this.giftModel = model;
        channelId = anchorId;
        this.nickname = nickname;

        String content;
        if (model.getHeadTimes() > 0) {
            content = String.format("送出%sx%d个", model.getGiftName(), model.getSendNum()) + "+100花钻x" + model.getHeadTimes();
        } else {
            content = String.format("送出%sx%d个", model.getGiftName(), model.getSendNum());
        }
        tvContent.setText(content);
        tvSend.setText(model.getNickname());
        tvReceiveName.setText(model.getAnchorNickname());
        tvPrice.setText(String.format("价值%d花钻", model.getGiftTotal()));
        StringBuilder builder = new StringBuilder("送给 ").append(nickname)
                .append(" ").append(model.getGiftName()).append("+100花钻x").append(model.getHeadTimes() + 1);
        tvRule.setText(builder.toString());

        ImageLoader.loadImg(ivHeaderSend, model.getHeaderurl());
        ImageLoader.loadImg(ivHeaderReceive, model.getAnchorHeaderImg());
        if (TextUtils.equals(anchorId, String.valueOf(model.getAnchorUserId()))) {
            btnSubmit.setVisibility(View.GONE);
        }
    }

    public void setTime(int time) {
        tvTime.setText(time + "s");
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_toutiao_info);
    }

    @OnClick({R2.id.iv_close, R2.id.btn_submit, R2.id.iv_header_receive, R2.id.btn_ding})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.iv_close) {
            dismiss();
        } else if (id == R.id.btn_submit) {
            RxBus.get().post(new ChangeRoomEvent(giftModel.getAnchorUserId() + ""));
            dismiss();
//            LivingRoomActivity.start(getContext(), getData(), 0);
        } else if (id == R.id.iv_header_receive) {
        } else if (id == R.id.btn_ding) {

            if (!isClick) {
                isClick = true;
            } else {
                isClick = false;
                return;
            }
            if (service.getUserInfo().balance < giftModel.getGiftTotal() + 100) {
                dismiss();
                DialogUtil.showNoMoneyDialog(getContext(),
                        new DialogUtil.AlertDialogBtnClickListener() {
                            @Override
                            public void clickPositive() {
                                dismiss();
                                RxBus.get().post(new ShowRechargePopEvent());
                            }

                            @Override
                            public void clickNegative() {
                            }
                        });
            } else sendTouTiao();
        }
    }

    private void sendTouTiao() {
        new RetrofitUtils().createApi(LiveApiService.class).sendToutiaoGift(new HttpUtils.ParamsBuilder()
                .put("id", String.valueOf(giftModel.getGiftId()))
                .put("isAddition", "1")
                .put("targetId", channelId + "")
                .build())
                .compose(RxUtils.applySchedulers2())
                .subscribe(new LoadingObserver<HttpResult<PopoDto>>() {
                    @Override
                    public void _onNext(HttpResult<PopoDto> data) {
                        isClick = false;
                        if (data.isSuccess()) {
                            service.getUserInfo().setExperience(data.data.getExperience().toString());
                            service.setUsetInfo(service.getUserInfo());
                            dismiss();
                            RxBus.get().post(new PaySuccessEvent(data.data.getExperience()+""));
                        } else {
                            ToastUtils.showShort(getContext(), data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(getContext(), msg);
                    }
                });
    }

    public void resetAnchorData(String anchorName, String anchorId) {
        this.channelId = anchorId;
        StringBuilder builder = new StringBuilder("送给 ").append(nickname)
                .append(" ").append(giftModel.getGiftName()).append("+100花钻x").append(giftModel.getHeadTimes() + 1);
        tvRule.setText(builder.toString());
    }
}
