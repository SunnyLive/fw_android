package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.content.Intent;
import android.os.UserManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.TreeBean;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.OpenGiftDto;
import com.fengwo.module_live_vedio.mvp.ui.activity.zhubo.StartLiveActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/8/13
 */
public class GiftOpenPopwindow extends BasePopupWindow {

    @BindView(R2.id.iv_close)
    ImageView ivClose;
    @BindView(R2.id.iv_gift)
    ImageView ivGift;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_content)
    TextView tvContent;
    @BindView(R2.id.iv_commit)
    TextView ivCommit;

    private CompositeDisposable disposable;
    private Context context;
    private int boxId;
    private int channelId;
    @Autowired
    UserProviderService userProviderService;
    private int carId;
    private String carSwf;
    private String carName;
    private int giftType;

    public GiftOpenPopwindow(Context context, int boxId, int channelId, TreeBean dto) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        disposable = new CompositeDisposable();
        ARouter.getInstance().inject(this);
        this.context = context;
        this.boxId = boxId;
        this.channelId = channelId;
        openGift(dto);
    }

    private void openGift(TreeBean dto) {

        ImageLoader.loadImgFitCenter(ivGift, dto.getGoodsIcon());
        tvName.setText(dto.getGoodsName());
    }


//    private void openGift(TreeBean data) {
//
//        ImageLoader.loadImgFitCenter(ivGift, data.getBoxGiftIcon());
//        giftType = data.getGiftType();
//
//        if (data.getGiftType() == 0) {
//            tvName.setText(data.getGiftShowName() + "+" + data.getBoxGiftValue() + "");
//            tvContent.setVisibility(View.GONE);
//        } else if (data.getGiftType() == 2) {
//            // 贵族
//            tvName.setText(data.getGiftShowName() + "*" + data.getBoxGiftValue() + "天");
//            tvContent.setVisibility(View.GONE);
//        } else {
//
//            tvName.setText(data.getGiftShowName() + "*" + data.getBoxGiftValue() + "天");
////                                tvContent.setVisibility(View.VISIBLE);
////                                if (data.data.getGiftMotorLevel()<0){
////                                    tvContent.setText("获得的座驾等级低于当前座驾等级，如需更换可在“我的座驾”点击使用。");
////                                    ivCommit.setVisibility(View.GONE);
////                                }else if (data.data.getGiftMotorLevel()>0){
////                                    tvContent.setText("获得的座驾等级高于当前座驾等级，如需更换，可点击按钮使用或者在“我的座驾”点击使用。");
////                                }else {
////                                    tvContent.setText("获得的座驾在进入直播间将有特效，可点击按钮使用或者在“我的座驾”点击使用。");
////                                }
//            carId = data.getGiftMotorId();
//            carSwf = data.getGiftMotoringSwf();
//            carName = data.getGiftShowName();
//            //使用座驾  token
//        }
//        //data.data.getGiftType()==0)//礼物类型 0-经验卡 1-座驾
//        //    ivCommit.setImageResource(data.data.getGiftType()==0?R.drawable.btn_happy_accept:R.drawable.btn_use_now);
//
//
//    }

    @Override
    public View onCreateContentView() {
        View view = createPopupById(R.layout.pop_gift_open);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R2.id.iv_commit)
    public void onViewClicked() {
        dismiss();
//        if (giftType == 0 || giftType == 2) {
//            new RetrofitUtils().createApi(LiveApiService.class).getUserCenter()
//                    .compose(RxUtils.applySchedulers())
//                    .subscribe(new Consumer<HttpResult<UserInfo>>() {
//                        @Override
//                        public void accept(HttpResult<UserInfo> data) throws Exception {
//                            if (data != null && data.data != null) {
//                                userProviderService.setUsetInfo(data.data);
//                                dismiss();
//                            }
//                        }
//                    });
//
//            return;
//        }
//        disposable.add(new RetrofitUtils().createApi(LiveApiService.class).openCar(carId, 1)
//                .compose(RxUtils.applySchedulers())
//                .subscribeWith(new LoadingObserver<HttpResult>() {
//
//                    @Override
//                    public void _onNext(HttpResult data) {
////                        getView().openCarReturn(data);
//                        UserInfo userInfo = userProviderService.getUserInfo();
//                        userInfo.motoringName = carName;
//                        userInfo.motoringSwf = carSwf;
//                        userProviderService.setUsetInfo(userInfo);
//
//                        dismiss();
//                    }
//
//                    @Override
//                    public void _onError(String msg) {
//                        dismiss();
//                    }
//                }));


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @OnClick(R2.id.iv_close)
    public void onClose() {
        dismiss();
    }
}
