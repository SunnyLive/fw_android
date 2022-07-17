package com.fengwo.module_comment.base;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fengwo.module_comment.utils.L;


/**
 * WebView常用封装
 * <p/>
 * 关于js调用 参考张兴业http://blog.csdn.net/xyz_lmn/article/details/47857633
 */
public class WebControll {
    private final String TAG = "WebControll";
    private WebView mWebView;
    private WebViewClient mWebViewClient;
    private WebChromeClient mWebChromeClient;
    private OnEventListener mListener;

    public WebControll(WebView webView) {
        mWebView = webView;
        initConfig();
    }

    private void initConfig() {
        initWebSettings();
        initWebViewClient();
        initWebChromeClient();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebSettings() {
        // 支持缩放
        mWebView.getSettings().setSupportZoom(true);
        // 启用内置缩放装置
        mWebView.getSettings().setBuiltInZoomControls(true);
        // 支持JS脚本
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 提高渲染的优先级
        mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        // 设置页面自适应手机屏幕
        mWebView.getSettings().setUseWideViewPort(true);
        // WebView自适应屏幕大小
        mWebView.getSettings().setLoadWithOverviewMode(true);
        // 设置网页编码格式为utf-8
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");

        // WebView先不要自动加载图片，等页面finish后再发起图片加载
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            mWebView.getSettings().setLoadsImagesAutomatically(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0以上允许加载http和https混合的页面(5.0以下默认允许，5.0+默认禁止)
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    private void initWebViewClient() {
        mWebViewClient = new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 当点击链接时,希望覆盖而不是打开新窗口
                if (mListener != null) {
                    if (!mListener.handlerUrl(url)) {
                        view.loadUrl(url);
                    } else {
                        return false;
                    }
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                L.i(TAG, "onReceivedSslError--->" + "加载数据失败");
                if (mListener != null) {
                    mListener.showError();
                }
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                L.i(TAG, "onReceivedSslError--->" + "加载数据失败");
                if (mListener != null) {
                    mListener.showError();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                L.i(TAG, "onPageStarted--->url=" + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                L.i(TAG, "onPageFinished--->url=" + url);
                if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
                    mWebView.getSettings().setLoadsImagesAutomatically(true);
                }
            }
        };
        mWebView.setWebViewClient(mWebViewClient);
    }

    private void initWebChromeClient() {
        mWebChromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (mListener != null) {
                    mListener.loadProgress(newProgress);
                }
            }
        };
        mWebView.setWebChromeClient(mWebChromeClient);
    }

    public void onResume() {
        if (mWebView != null) {
            if (isVersaionHONEYCOMB()) {
                mWebView.onResume();
            }
            mWebView.resumeTimers();
        }
    }

    public void onPause() {
        if (mWebView != null) {
            if (isVersaionHONEYCOMB()) {
                mWebView.onPause();
            }
            mWebView.pauseTimers();
        }
    }

    public void onDestroy() {
        if (mWebView != null) {
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.setVisibility(View.GONE);
            long timeout = ViewConfiguration.getZoomControlsTimeout();
            new Handler().postAtTime(new Runnable() {
                @Override
                public void run() {
                    mWebView.destroy();
                }
            }, timeout);

        }
    }

    public WebView getWebView() {
        return mWebView;
    }

    public void loadUrl(String url) {
        if (mWebView != null) {
            mWebView.loadUrl(url);
        }
    }

    public void loadData(String htmlData) {
        if (mWebView != null) {
            mWebView.loadData(htmlData, "text/html", "utf-8");
        }
    }

    public void loadData(String baseUrl, String htmlData) {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(baseUrl, htmlData, "text/html",
                    "utf-8", null);
        }
    }

    public boolean goBack() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    private boolean isVersaionHONEYCOMB() {
        return Build.VERSION.SDK_INT >= 11;
    }

    public void setOnEventListener(OnEventListener l) {
        mListener = l;
    }

    public interface OnEventListener {
        // 显示加载失败界面
        public void showError();

        // 加载进度条
        public void loadProgress(int progress);

        // 对跳转的url做进一步处理
        public boolean handlerUrl(String url);
    }

}
