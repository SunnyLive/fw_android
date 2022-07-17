package com.fengwo.module_vedio.mvp.ui.pop;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.utils.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import cc.shinichi.library.tool.ui.ToastUtil;
import razerdp.basepopup.BasePopupWindow;

public class SharePop extends BasePopupWindow implements View.OnClickListener, UMShareListener {

    private View btnCircle, btnWx, btnClose;
    private View tvShare;
    private Context mContext;

    private String title, url, icUrl, des;

    public SharePop(Context context, String title, String url) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        this.title = title;
        this.url = url;
        btnWx = findViewById(R.id.btn_wx);
        btnCircle = findViewById(R.id.btn_circle);
        tvShare = findViewById(R.id.tv_share_code);
        btnClose = findViewById(R.id.iv_share_cancel);
        tvShare.setVisibility(View.GONE);
        setPopupGravity(Gravity.CENTER);
        btnWx.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnCircle.setOnClickListener(this);
        mContext = context;
        ARouter.getInstance().inject(this);

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
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
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
        } else if (id == R.id.tv_share_code) {
//            AgentShareCodeActivity.startAgentShareCodeActivity(mContext, url);
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
