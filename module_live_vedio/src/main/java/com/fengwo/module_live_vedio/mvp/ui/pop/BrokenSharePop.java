package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import androidx.fragment.app.FragmentManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.ShareInfoDto;
import com.fengwo.module_live_vedio.mvp.ui.fragment.ShareCodeDialogFragment;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import razerdp.basepopup.BasePopupWindow;

public class BrokenSharePop extends BasePopupWindow implements View.OnClickListener, UMShareListener {

    private View btnCircle, btnWx;
    private View tvShare;
    private View btnClose;
    private Context mContext;
    private ShareInfoDto shareInfoDto;
    private FragmentManager fragmentManager;

    public BrokenSharePop(Context context, FragmentManager supportFragmentManager) {
        super(context);
        this.fragmentManager = supportFragmentManager;
        btnWx = findViewById(R.id.btn_wx);
        btnCircle = findViewById(R.id.btn_circle);
        tvShare = findViewById(R.id.tv_share_code);
        btnClose = findViewById(R.id.iv_share_cancel);
        setPopupGravity(Gravity.CENTER);
        btnWx.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        btnCircle.setOnClickListener(this);
        mContext = context;
        getShareInfo();
        ARouter.getInstance().inject(this);

    }


    private void getShareInfo() {
        new RetrofitUtils().createApi(LiveApiService.class)
                .getShareInfo()
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult<ShareInfoDto>>() {
                    @Override
                    public void _onNext(HttpResult<ShareInfoDto> data) {
                        shareInfoDto = data.data;
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_share_broken);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }

    @Override
    public void onClick(View view) {
        if (null == shareInfoDto) {
            ToastUtils.showShort(mContext, "获取信息失败，请重试");
            getShareInfo();
            return;
        }
        UMWeb web = new UMWeb(shareInfoDto.getLink());
        web.setTitle(shareInfoDto.getTitle());
        web.setDescription(shareInfoDto.getDecribe());
        if (!TextUtils.isEmpty(shareInfoDto.getIcon()))
            web.setThumb(new UMImage(mContext, shareInfoDto.getIcon()));
        int id = view.getId();
        if (id == R.id.btn_wx) {
            new ShareAction((Activity) mContext)
                    .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                    .withMedia(web)
                    .setCallback(this)//回调监听器
                    .share();
        } else if (id == R.id.btn_circle) {
            new ShareAction((Activity) mContext)
                    .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                    .withMedia(web)
                    .setCallback(this)//回调监听器
                    .share();
        } else if (id == R.id.tv_share_code) {//生成二维码
            ShareCodeDialogFragment.getInstance(shareInfoDto).show(fragmentManager);
            dismiss();
        } else if (id == R.id.iv_share_cancel) {
            dismiss();
        }
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        ToastUtils.showShort(mContext, "分享成功");
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        ToastUtils.showShort(mContext, "分享失败");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }
}
