package com.fengwo.module_comment.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.fengwo.module_comment.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;

public class ShareHelper {

    private static ShareHelper INSTANCE;

    private ShareHelper() {
    }

    public static synchronized ShareHelper get() {
        if (INSTANCE == null) {
            INSTANCE = new ShareHelper();
        }
        return INSTANCE;
    }

    public void shareMin(Activity activity, String minPath, String url, String title, String content) {
        if (TextUtils.isEmpty(minPath)) {
            return;
        }

        //兼容低版本的网页链接
        UMMin umMin = new UMMin("www.baidu.com");
        // 小程序消息封面图片
        UMImage thumbImage = TextUtils.isEmpty(url) ? new UMImage(activity, R.drawable.ic_launcher) : new UMImage(activity, url);
        umMin.setThumb(thumbImage);
        // 小程序消息title
        umMin.setTitle(title);
        // 小程序消息描述
        umMin.setDescription(content);
        //小程序页面路径
        umMin.setPath(minPath);
        // 小程序原始id,在微信平台查询
//        umMin.setUserName(Const.WX_MIN_ID);
        new ShareAction(activity)
                .withMedia(umMin)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener).share();
    }

    public void shareImage(Activity activity, Bitmap bitmap, SHARE_MEDIA share_media) {
        UMImage image = new UMImage(activity, bitmap);
        new ShareAction(activity)
                .withMedia(image)
                .setPlatform(share_media)
                .setCallback(shareListener).share();
    }

    /**
     * 分享动态
     */
    public void shareDynamic(Activity activity, String url, String title, String imgurl, String content,int type) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        UMWeb  web = new UMWeb(url);
        web.setTitle(title);//标题
        if (!TextUtils.isEmpty(imgurl))  web.setThumb(new UMImage(activity, imgurl));  //缩略图
        web.setDescription(content);//描述
        new ShareAction(activity)
                .withMedia(web)
                .setPlatform(type == 0? SHARE_MEDIA.WEIXIN:SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(shareListener).share();
    }
    /**
     * 分享返回监听
     */
    private UMShareListener shareListener = new UMShareListener() {
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
    };
}
