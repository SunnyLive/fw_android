package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.ShareInfoDto;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.UmengTool;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.weixin.handler.UmengWXHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import razerdp.basepopup.BasePopupWindow;

public class SharePopwindow extends BasePopupWindow implements UMShareListener {
    @BindView(R2.id.ll_wx)
    LinearLayout llWx;
    @BindView(R2.id.ll_circle)
    LinearLayout llCircle;

    private CompositeDisposable compositeDisposable;
    private int anchorId;
    private boolean isPkActivity;//是否pk排位赛活动


    public SharePopwindow(Context context,int anchorId,boolean isPkActivity) {
        super(context);
        this.anchorId = anchorId;
        this.isPkActivity = isPkActivity;
        setPopupGravity(Gravity.BOTTOM);
        ButterKnife.bind(this, getContentView());
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_activity_share);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    private void share(SHARE_MEDIA type) {
        Flowable<HttpResult<ShareInfoDto>> flowable;
        if (isPkActivity){
           flowable = new RetrofitUtils().createApi(LiveApiService.class).getPkRankShareInfo(anchorId);
        }else {
            flowable = new RetrofitUtils().createApi(LiveApiService.class).getActivityShareInfo();
        }
        compositeDisposable.add(flowable
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<ShareInfoDto>>() {
                    @Override
                    public void _onNext(HttpResult<ShareInfoDto> result) {
                        ShareInfoDto shareInfoDto = result.data;
                        UMWeb web = new UMWeb(shareInfoDto.getLink());
                        web.setThumb(new UMImage(getContext(), shareInfoDto.getPoster()));
                        web.setTitle(shareInfoDto.getTitle());
                        web.setDescription(shareInfoDto.getDecribe());
                        new ShareAction(getContext())
                                .setPlatform(type)//传入平台
                                .withMedia(web)
                                .setCallback(SharePopwindow.this)//回调监听器
                                .share();
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {

    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }

    @OnClick({R2.id.ll_wx, R2.id.ll_circle})
    public void onViewClicked(View view) {
        if (!CommentUtils.isWeixinAvilible(getContext())) {
            ToastUtils.showShort(getContext(), "您没有安装微信！！！");
            return;
        }
        int id = view.getId();
        if (id == R.id.ll_wx) {
            share(SHARE_MEDIA.WEIXIN);
        } else if (id == R.id.ll_circle) {
            share(SHARE_MEDIA.WEIXIN_CIRCLE);
        }
    }
}
