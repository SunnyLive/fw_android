package com.fengwo.module_comment.utils.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseActivity;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.IntentRoomActivityUrils;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;

public class X5WebView extends WebView {
    TextView title;
    private WebViewClient client = new WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //action://live?id=513051    action://user?id=513051
            if (url.contains("action://live")) {
                //直播
                ArrayList<ZhuboDto> list = new ArrayList<>();
                ZhuboDto zhuboDto = new ZhuboDto();
                zhuboDto.channelId = Integer.parseInt(url.split("=")[1]);
                list.add(zhuboDto);
                if (FloatingView.getInstance().isShow()) {
                    showExitDialog(list);
                } else {
                    IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);
                  //  ArouteUtils.toLive(list);
                }
                return true;
            } else if (url.contains("action://user")) {
                ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, Integer.parseInt(url.split("=")[1]));
                return true;
            }
            view.loadUrl(url);
            return true;
        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    public X5WebView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        this.setWebViewClient(client);
        // this.setWebChromeClient(chromeClient);
        // WebStorage webStorage = WebStorage.getInstance();
        initWebViewSettings();
        this.getView().setClickable(true);
    }
    /**
     * 关闭悬浮窗弹框提示
     */
    public void showExitDialog(ArrayList<ZhuboDto> list) {
        FloatingView floatingView = FloatingView.getInstance();
        ExitDialog dialog = new ExitDialog();
        dialog.setNegativeButtonText("取消")
                .setPositiveButtonText("确定退出")
                .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);
                  //      ArouteUtils.toLive(list);
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .setGear(floatingView.getGear())
                .setNickname(floatingView.getNickname())
                .setExpireTime(floatingView.getExpireTime())
                .setHeadImg(floatingView.getHeadImg())
                .setRoomId(floatingView.getRoomId())
                .setTip("退出达人房间，印象值将归零\n是否要退出")
                .show(((BaseActivity) getContext()).getSupportFragmentManager(), "");
    }
    private void initWebViewSettings() {
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean ret = super.drawChild(canvas, child, drawingTime);
//		canvas.save();
//		Paint paint = new Paint();
//		paint.setColor(0x7fff0000);
//		paint.setTextSize(24.f);
//		paint.setAntiAlias(true);
//		if (getX5WebViewExtension() != null) {
//			canvas.drawText(this.getContext().getPackageName() + "-pid:"
//					+ android.os.Process.myPid(), 10, 50, paint);
//			canvas.drawText(
//					"X5  Core:" + QbSdk.getTbsVersion(this.getContext()), 10,
//					100, paint);
//		} else {
//			canvas.drawText(this.getContext().getPackageName() + "-pid:"
//					+ android.os.Process.myPid(), 10, 50, paint);
//			canvas.drawText("Sys Core", 10, 100, paint);
//		}
//		canvas.drawText(Build.MANUFACTURER, 10, 150, paint);
//		canvas.drawText(Build.MODEL, 10, 200, paint);
//		canvas.restore();
        return ret;
    }

    public X5WebView(Context arg0) {
        super(arg0);
        setBackgroundColor(85621);
    }

}
