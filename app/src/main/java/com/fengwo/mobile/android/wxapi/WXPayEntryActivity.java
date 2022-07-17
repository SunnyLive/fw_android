package com.fengwo.mobile.android.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fengwo.module_comment.base.Constant;
import com.fengwo.module_comment.event.PaySuccessEvent;
import com.fengwo.module_comment.event.RechargeSuccessEvent;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.umentlib.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.WXAPP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        L.d("---------pay---", "onPayFinish, errCode = " + resp.errCode);
        if (resp.errCode == 0) {
//            RxBus.get().post(new RechargeSuccessEvent());
        }
        finish();
    }
}