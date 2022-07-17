package com.fengwo.module_comment.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.fengwo.module_comment.BuildConfig;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.utils.LubanUtils;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.MapUtil;
import com.fengwo.module_comment.utils.webview.X5WebView;
import com.fengwo.module_comment.widget.AppTitleBar;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewCallbackClient;
import com.yalantis.ucrop.util.FileUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BrowserActivity extends BaseMvpActivity {

    private final static int REQUEST_CODE_FILE_CHOOSER = 10002;
    private String mCameraFilePath;
    ValueCallback<Uri> mUploadCallBack;
    ValueCallback<Uri[]> mUploadCallBackAboveL;

    public static void start(Context context, String title, String url) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void startRuleActivity(Context context) {
        Intent i = new Intent(context, BrowserActivity.class);
        i.putExtra("url", "https://app.fwhuyu.com/agreement");
        i.putExtra("title", "用户服务协议和隐私政策");
        context.startActivity(i);
    }
    public static void startRuleActivityFor(Activity context, String title, String url) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivityForResult(intent,10);
    }
    FrameLayout container;

    private X5WebView mWebView;


    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");
//        影响提现返回  暂隐藏
//        titleBar.setBackClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mWebView.canGoBack()) {
//                    mWebView.goBack();//返回上个页面
//                    return;
//                } else {
//                    finish();
//                }
//            }
//        });
        container = findViewById(R.id.container);
        new ToolBarBuilder().showBack(true)
                .setTitle(title)
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .build();
        if (mWebView == null) mWebView = initWebView();
        container.addView(mWebView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mWebView.setWebChromeClient(new WebChromeClient() {
                                        @Override
                                        public void onProgressChanged(WebView webView, int i) {
                                            super.onProgressChanged(webView, i);
                                            if (i < 100) {
                                                showLoadingDialog();
                                            } else {
                                                hideLoadingDialog();
                                            }
                                        }

                                        @Override
                                        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
                                            mUploadCallBackAboveL = valueCallback;
                                            showFileChooser();
                                            return true;
                                        }

                                        //For Android  >= 4.1
                                        public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                                            mUploadCallBack = valueCallback;
                                            showFileChooser();
                                        }

                                        // For Android  >= 3.0
                                        public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                                            mUploadCallBack = valueCallback;
                                            showFileChooser();
                                        }

                                        // For Android < 3.0
                                        public void openFileChooser(ValueCallback<Uri> valueCallback) {
                                            mUploadCallBack = valueCallback;
                                            showFileChooser();
                                        }

                                        @Override
                                        public void onReceivedTitle(WebView webView, String s) {
                                            super.onReceivedTitle(webView, s);
                                            if (TextUtils.isEmpty(title)) ((TextView) findViewById(R.id.title_tv)).setText(s);
                                        }
                                    }
        );
//        mWebControll = new WebControll(mWebView);
//        mWebControll.setOnEventListener(new WebControll.OnEventListener() {
//            @Override
//            public void showError() {
//
//            }
//
//            @Override
//            public void loadProgress(int progress) {
//                if (progress == 100) {
//                    hideLoadingDialog();
//                }
//            }
//
//            @Override
//            public boolean handlerUrl(String url) {
//                return false;
//            }
//        });
        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_webview;
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


    private X5WebView initWebView() {
        X5WebView webView = new X5WebView(this, null);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
//        settings.setLayoutAlgorithm(IX5WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportMultipleWindows(true);
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
        webView.addJavascriptInterface(new AndroidtoJs(), "android");
//        settings.setTextZoom(150);
        return webView;
    }


    /**
     * 定义JS需要调用的方法
     */
    public class AndroidtoJs extends Object {
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void chooseMap(String longitude, String latitude, String add) {
            System.out.println("JS调用了Android的hello方法:" + longitude + "---------" + latitude);
//            if (MapUtil.isBaiduMapInstalled()) {
//                double[] latlon = MapUtil.gaoDeToBaidu(Double.parseDouble(longitude), Double.parseDouble(latitude));
//                MapUtil.toBaidu(BrowserActivity.this, latlon[1], latlon[0]);
//            }
            if (MapUtil.isGdMapInstalled()) {
                MapUtil.goToGaodeMap(BrowserActivity.this, Double.parseDouble(latitude), Double.parseDouble(longitude), add);
                return;
            }
            if (MapUtil.isBaiduMapInstalled()) {
                MapUtil.goToBaiduMap(BrowserActivity.this, Double.parseDouble(latitude), Double.parseDouble(longitude), add);
                return;
            }
            if (MapUtil.isTencentMapInstalled()) {
                MapUtil.goToTencentMap(BrowserActivity.this, Double.parseDouble(latitude), Double.parseDouble(longitude), add);
                return;
            }
            toastTip("无法找到您手机上的地图应用！！！");
        }
    }


    /**
     * 打开选择图片/相机
     */
    private void showFileChooser() {
        MImagePicker.openImagePicker(this, MImagePicker.TYPE_IMG, REQUEST_CODE_FILE_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FILE_CHOOSER) {
            if (resultCode == RESULT_OK) {
                String path = MImagePicker.getImagePath(this, data);
                File f = new File(path);
                LubanUtils.compress(this, f, new LubanUtils.OnCompress() {
                    @Override
                    public void success(File f) {
                        Uri uri = Uri.parse(f.getPath());
                        returnFileToH5(uri);
                    }

                    @Override
                    public void error(String msg) {
                        Uri uri = Uri.parse(f.getPath());
                        returnFileToH5(uri);
                    }
                });
            } else {
                clearUploadMessage();
            }
            return;
        }
    }

    private void returnFileToH5(Uri uri) {
        if (mUploadCallBackAboveL != null) {
            if (uri != null) {
                mUploadCallBackAboveL.onReceiveValue(new Uri[]{uri});
                mUploadCallBackAboveL = null;
                Log.e("BrowserActivity", "mUploadCallBackAboveL uploadSuccess");
                return;
            }
        } else if (mUploadCallBack != null) {
            if (uri != null) {
                mUploadCallBack.onReceiveValue(uri);
                mUploadCallBack = null;
                Log.e("BrowserActivity", "mUploadCallBack uploadSuccess");
                return;
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearUploadMessage();
            }
        });

    }

    /**
     * webview没有选择图片也要传null，防止下次无法执行
     */
    private void clearUploadMessage() {
        if (mUploadCallBackAboveL != null) {
            mUploadCallBackAboveL.onReceiveValue(null);
            Log.e("BrowserActivity", "mUploadCallBackAboveL failed");
            mUploadCallBackAboveL = null;
        }
        if (mUploadCallBack != null) {
            mUploadCallBack.onReceiveValue(null);
            Log.e("BrowserActivity", "mUploadCallBack failed");
            mUploadCallBack = null;
        }
    }
}
