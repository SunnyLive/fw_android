package com.fengwo.module_comment.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.fengwo.module_comment.R;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.widget.AppTitleBar;


public class WebViewActivity extends BaseMvpActivity {

    public static void start(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    private WebView mWebView;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");
        L.e("=url= "+url);
        AppTitleBar titleBar = findViewById(R.id.title);
        FrameLayout container = findViewById(R.id.container);
        titleBar.setTitle(title);

        if (mWebView == null) mWebView = initWebView();
        if (mWebView.getParent() == null) {
            container.addView(mWebView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        if (!TextUtils.isEmpty(url)) {
            if (!url.startsWith("http")) url = "http://" + url;
            mWebView.loadUrl(url);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_web_view;
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }

            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    private WebView initWebView() {
        WebView webView = new WebView(getApplicationContext());
//        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        mWebSettings.setJavaScriptEnabled(true);//是否允许JavaScript脚本运行，默认为false。设置true时，会提醒可能造成XSS漏洞
        mWebSettings.setSupportZoom(true);//是否可以缩放，默认true
        mWebSettings.setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        mWebSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        mWebSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mWebSettings.setAppCacheEnabled(true);//是否使用缓存
        mWebSettings.setDomStorageEnabled(true);//开启本地DOM存储
        mWebSettings.setLoadsImagesAutomatically(true); // 加载图片
        mWebSettings.setMediaPlaybackRequiresUserGesture(false);//播放音频，多媒体需要用户手动？设置为false为可自动播放
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0以上允许加载http和https混合的页面(5.0以下默认允许，5.0+默认禁止)
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    showLoadingDialog();
                } else {
                    hideLoadingDialog();
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        return webView;
    }

    public class MWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // 加载完成
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
            // 加载开始
        }
    }
}