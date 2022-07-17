package com.fengwo.module_comment.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.webkit.WebView;

import com.fengwo.module_comment.R;


/**
 * Created by Administrator on 2016-11-11.
 */

public class WebActivity extends BaseMvpActivity {


    public static void startRuleActivity(Context context) {
        Intent i = new Intent(context, WebActivity.class);
        i.putExtra("url", "https://app.fwhuyu.com/agreement");
        i.putExtra("title", "用户服务协议和隐私政策");
        context.startActivity(i);
    }

    public static void startWithTitleUrl(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    WebView mWebview;


    private WebControll mWebControll;


    @Override
    protected void initView() {

        initView(getIntent());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_web;
    }


    public void initView(Intent intent) {
//        setTitleBackground(Color.WHITE);
        mWebview = (WebView) findViewById(R.id.webview);
        String title = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");
        new ToolBarBuilder().showBack(true).setBackIcon(R.drawable.ic_back_white).setTitle(title).setTitleColor(R.color.text_33).build();
        mWebControll = new WebControll(mWebview);
        mWebControll.setOnEventListener(new WebControll.OnEventListener() {
            @Override
            public void showError() {

            }

            @Override
            public void loadProgress(int progress) {
                if (progress == 100) {
                    hideLoadingDialog();
                }
            }

            @Override
            public boolean handlerUrl(String url) {
                return false;
            }
        });
        showLoadingDialog();
        mWebControll.loadUrl(url);

    }

    @Override
    public void onResume() {
        super.onResume();
        mWebControll.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebControll.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebControll.onDestroy();
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void tokenIInvalid() {

    }

    @Override
    public void onBackPressed() {
        if (mWebview.canGoBack()) {
            mWebControll.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
