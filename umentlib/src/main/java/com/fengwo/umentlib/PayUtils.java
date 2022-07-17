package com.fengwo.umentlib;

import android.content.Context;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class PayUtils {
    public static void wxPay(Context context, JSONObject json) throws JSONException {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, Constants.WXAPP_ID);
        PayReq req = new PayReq();
        req.appId = json.getString("appid");
        req.partnerId = json.getString("partnerid");
        req.prepayId = json.getString("prepayid");
        req.nonceStr = json.getString("noncestr");
        req.timeStamp = json.getString("timestamp");
        req.packageValue = "Sign=WXPay";
        req.sign = json.getString("sign");
        iwxapi.sendReq(req);
    }
}
