package com.fengwo.module_login.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_login.R;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

public class FaceVerifyAgreementActivity extends BaseMvpActivity {


    public static void launchActivity(Context context) {
        context.startActivity(new Intent(context, FaceVerifyAgreementActivity.class));
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_back).setOnClickListener(view -> finish());
        WebView webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSavePassword(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setAllowFileAccessFromFileURLs(false);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(false);
        webView.loadUrl("https://app.fwhuyu.com/static/privacyAgreement.html");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_face_verify_agreement;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

}