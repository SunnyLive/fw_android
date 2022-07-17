package com.fengwo.module_comment;

import android.content.Context;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/27
 */
public class JpushReceiver extends JPushMessageReceiver {

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onTagOperatorResult(context, jPushMessage);
    }
    @Override
    public void onCheckTagOperatorResult(Context context,JPushMessage jPushMessage){
        super.onCheckTagOperatorResult(context, jPushMessage);
    }
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
    }
    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }

}
