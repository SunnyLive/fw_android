package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.widget.ContentLoadingProgressBar;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.IntentRoomActivityUrils;
import com.fengwo.module_comment.utils.webview.X5WebView;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.mvp.ui.activity.BaseLiveingRoomActivity;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

public class ActivityPopwindow extends BasePopupWindow {
    @BindView(R2.id.fl_wv)
    FrameLayout fl_wv;
    @BindView(R2.id.pb)
    ContentLoadingProgressBar pb;
    @BindView(R2.id.btn_back)
    ImageView btnBack;
    @BindView(R2.id.btn_share)
    ImageView btnShare;
    private String url;
    @Autowired
    UserProviderService userProviderService;

    private X5WebView mWebView;
    private Context context;
    private int anchorId;
    private boolean isPkActivity;//是否pk排位赛活动
    private boolean isShare = true;//是否有分享

    public ActivityPopwindow(Context context, String url,int anchorId, boolean isPkActivity) {
        this(context,url,anchorId,isPkActivity,true);
    }

    public ActivityPopwindow(Context context, String url, int anchorId, boolean isPkActivity, boolean isShare) {
        super(context);
        this.context = context;
        this.url = url;
        this.anchorId = anchorId;
        this.isPkActivity = isPkActivity;
        this.isShare = isShare;
        ARouter.getInstance().inject(this);
        ButterKnife.bind(this, getContentView());
        setPopupGravity(Gravity.BOTTOM);
        if (mWebView == null) mWebView = initWebView();
        fl_wv.addView(mWebView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mWebView.setWebChromeClient(new WebChromeClient() {
                                        @Override
                                        public void onProgressChanged(WebView webView, int i) {
                                            pb.setVisibility(i < 100 ? View.VISIBLE : View.GONE);
                                        }
                                    }
        );
        btnShare.setVisibility(isShare?View.VISIBLE:View.GONE);
        if (!TextUtils.isEmpty(url)) {
            try {
               // KLog.e("webview==url",url);
                mWebView.loadUrl(url);
            }catch (Exception e){
              //  KLog.e("webview",e.getMessage());
            }
        }
    }

    private X5WebView initWebView() {
        X5WebView webView = new X5WebView(context, null);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
//        settings.setLayoutAlgorithm(IX5WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setSupportMultipleWindows(true);
// webSetting.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setAppCacheMaxSize(Long.MAX_VALUE);
// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//设置默认的JS Object
//        webView.addJavascriptInterface(new AndroidtoJs(), "android");
        webView.setWebViewClient(client);



        settings.setDisplayZoomControls(false);
        settings.setLoadsImagesAutomatically(true);

//        settings.setDomStorageEnabled(true);//设置支持DomStorage
//        settings.setDatabaseEnabled(true);//设置支持本地存储
//        settings.setJavaScriptEnabled(true);
//
//        settings.setBuiltInZoomControls(true);
//        settings.setDomStorageEnabled(true);
//        settings.setSupportMultipleWindows(true);
        return webView;
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_activity);
        return v;
    }

    @OnClick({R2.id.btn_back, R2.id.btn_share})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_back) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                dismiss();
            }
        } else if (id == R.id.btn_share) {
            new SharePopwindow(getContext(),anchorId,isPkActivity).showPopupWindow();
        }
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    @Override
    public void showPopupWindow() {
        super.showPopupWindow();
        if (mWebView!=null)
            mWebView.setBackgroundColor(Color.argb(1, 0, 0, 0));
        try {
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.loadUrl(url);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView!=null) {
            mWebView.clearCache(true);
            mWebView.removeAllViews();
            mWebView = null;
            fl_wv.removeAllViews();
        }
    }

    private WebViewClient client = new WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //action://live?id=513051    action://user?id=513051
            if(anchorId== userProviderService.getUserInfo().getId()) {
                return true;
            }
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
                   // ArouteUtils.toLive(list);
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
             //           ArouteUtils.toLive(list);
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
                .show(((BaseLiveingRoomActivity) getContext()).getSupportFragmentManager(), "");
    }
}
