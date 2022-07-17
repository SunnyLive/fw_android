package com.fengwo.module_comment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.fengwo.module_comment.utils.L;

import androidx.core.app.NotificationCompat;
import cn.jpush.android.api.JPushInterface;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 自定义接收器
 * 这里我们只处理我们关心的自定义消息：ACTION_MESSAGE_RECEIVED
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "jpush";
    private MediaPlayer mRingTipPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            L.e(TAG, "--[MyReceiver] 接收Registration Id :" + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            L.e(TAG, "--[MyReceiver] 接收到推送下来的自定义消息：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            L.e(TAG, "--[MyReceiver] 接收到推送下来的自定义消息：" + bundle.getString(JPushInterface.EXTRA_EXTRA));
            if ( bundle.getString(JPushInterface.EXTRA_EXTRA).contains("pushSound")){
                startRingTip(context);
            }
        }else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())){
            L.e(TAG,"----接收到推送下来的通知");
            //todo
            int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            String extraMessage = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            L.e(TAG,"----接收到推送下来的通知附加信息："+extraMessage);
        }else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){
            L.e(TAG,"----用户点击了通知");
            //点击通知后，打开自定义页面
//            Intent intent1 = new Intent(context,TestActivity.class);
//            intent1.putExtras(bundle);
//            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(intent1);
        }else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            L.e(TAG, "--[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        }else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "--[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
            Log.d(TAG, "--[MyReceiver] Unhandled intent - " + intent.getAction());
        }

//        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
//            processCustomMessage(context, bundle);
    }


    private void processCustomMessage(Context context, Bundle bundle) {
        //1.这里我们拿到了极光推送过来的自定义消息
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        L.e(TAG,"title==" + title + "  message==" + message);
        //2.我们把这条消息拿到后，直接通知显示
        showNotice(context,message);
    }
    private void showNotice(Context context,String msg){
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("渠道ID", "优惠券商品", NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, "渠道ID");
        }else {
            builder = new NotificationCompat.Builder(context);
        }
        Notification notification = builder
                .setSmallIcon(R.drawable.ic_launch_logo)
                .setContentTitle("我是一个标题")
                .setContentText(msg)
                .setAutoCancel(true)
                .build();
        manager.notify(0,notification);
    }
    /**
     * 播放收到订单铃声
     */
    public void startRingTip(Context context){
        if (mRingTipPlayer != null){
            mRingTipPlayer.stop();
            mRingTipPlayer.release();
            mRingTipPlayer = null;
        }
        mRingTipPlayer = MediaPlayer.create(context, R.raw.voice_order);
        mRingTipPlayer.setLooping(false);
        mRingTipPlayer.start();
    }

    /**
     * 停止铃声
     */
    public void stopRingTip(){
        if (mRingTipPlayer != null){
            mRingTipPlayer.stop();
            mRingTipPlayer.release();
            mRingTipPlayer = null;
        }
    }

}