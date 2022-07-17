package com.fengwo.module_websocket;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fengwo.module_websocket.security.DataSecurityUtil;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/17
 */
public class FWWebSocket1 {

    //private LruCache<String, String> cacheMsgId;

    private final String TAG = getClass().getSimpleName();
    private boolean isTheadRuning = false;

    private static FWWebSocket1 INSTANCE;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                resetWebSocket();
                reconnectWebSocket();
            }
        }
    };
    ExecutorService singleThreadExecutor;
    private OnSocketConnectListener mainListener;

    private FWWebSocket1() {
        //cacheMsgId = new LruCache<>(100);
        singleThreadExecutor = Executors.newSingleThreadExecutor();
        startRequest();
    }

    public void init(int uid,String token) {
        this.uid = uid;
        this.token = token;
    }

    public static FWWebSocket1 getInstance() {
        if (INSTANCE == null) {
            synchronized (FWWebSocket1.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FWWebSocket1();
                }
            }
        }
        return INSTANCE;
    }

    private DataSecurityUtil desUtils = new DataSecurityUtil();
    private final int NORMAL_CLOSURE_STATUS = 1000;

    private boolean isReconnent = false;
    private OkHttpClient sClient;
    private WebSocket sWebSocket;

    private int uid;
    private String token;

    public synchronized void startRequest() {
//        L.e(TAG, "webSocket connecting");
        if (sClient == null) {
            sClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .pingInterval(5, TimeUnit.SECONDS).build();
        }
        if (sWebSocket == null) {
            try {
                Request request = new Request.Builder().url(BuildConfig.DEBUG ? Url.TEST_BASE_IM_URL : Url.BASE_IM_URL).build();
//            Request request = new Request.Builder().url("wss://ws.fwhuyu.com/hkim").build();//上线正式地址
                EchoWebSocketListener listener = new EchoWebSocketListener();
                sWebSocket = sClient.newWebSocket(request, listener);
            } catch (NullPointerException e) {

            }

        }
    }

    private void sendMessage(WebSocket webSocket, String msg) {
        Log.i(TAG, "sending: " + msg);
        String entryMsg = desUtils.encrypt(msg);
        if(null!=webSocket){
            webSocket.send(entryMsg);
        }

    }

    public void sendSyncMessage(String uid) {
        try {
            String myId = createMsgId(uid);
            JSONObject jsonObjectResopnse = new JSONObject();
            jsonObjectResopnse.put("fromUid", uid);
            jsonObjectResopnse.put("toUid", "-1");
            jsonObjectResopnse.put("msgId", myId);
            jsonObjectResopnse.put("version", "2.0");
            jsonObjectResopnse.put("vendor", getModel());
            jsonObjectResopnse.put("description", "消息同步");
            jsonObjectResopnse.put("timestamp", System.currentTimeMillis() + "");
            jsonObjectResopnse.put("busiEvent", "req");
            JSONObject jsonObjectData = new JSONObject();
            jsonObjectData.put("userId", uid);
            jsonObjectData.put("action", "sync");
            jsonObjectResopnse.put("data", jsonObjectData);
            sendMessage(sWebSocket, jsonObjectResopnse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTextMessage(String msg) {
        WebSocket webSocket;
        synchronized (FWWebSocket1.class) {
            webSocket = sWebSocket;
        }
        if (webSocket != null) {
            sendMessage(webSocket, msg);
        } else {
            startRequest();
        }
    }

    public synchronized void closeWebSocket() {
        if (sWebSocket != null) {
            sWebSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye!");
            sWebSocket = null;
            Log.e(TAG, "closeWebSocket");
        }
    }

    public synchronized void destroy() {
        Log.e(TAG, "destroy");
        closeWebSocket();
        if (sClient != null) {
            sClient.dispatcher().executorService().shutdown();
            sClient = null;
        }
    }

    private void resetWebSocket() {
        synchronized (FWWebSocket1.class) {
            sWebSocket = null;
            sClient = null;
        }
    }

    public class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Log.e(TAG, "WebSocket已连接");
            if (isReconnent) {
                if (null != listenerList && listenerList.size() > 0) {
                    for (int i = 0; i < listenerList.size(); i++) {
                        listenerList.get(i).onReconnect();
                    }
                }
                isReconnent = false;
            }
            sendLoginChatMessage(uid);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            String json = desUtils.decrypt(text);
            String msgId = "";
            Log.i(TAG, "Receiving------: " + json);
            try {
                if (json.contains("msgId")) {
                    JSONObject jsonObject = new JSONObject(json);
                    //服务器返回的ack消息不回复
                    if (jsonObject.has("busiEvent") && "ack".equals(jsonObject.getString("busiEvent"))) {
                        return;
                    }
                    msgId = jsonObject.getString("msgId");

                    String myId = createMsgId(uid + "");
                    JSONObject jsonObjectResopnse = new JSONObject();
                    jsonObjectResopnse.put("msgId", myId);
                    jsonObjectResopnse.put("oriMsgId", msgId);
                    jsonObjectResopnse.put("fromUid", uid);
                    jsonObjectResopnse.put("toUid", "-1");
                    jsonObjectResopnse.put("busiEvent", "ack");
                    jsonObjectResopnse.put("version", "2.0");
                    jsonObjectResopnse.put("vendor", getModel());
                    jsonObjectResopnse.put("timestamp", System.currentTimeMillis() + "");
                    jsonObjectResopnse.put("description", "消息回执");
                    sendMessage(sWebSocket, jsonObjectResopnse.toString());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
           /* if (!TextUtils.isEmpty(msgId)) {
                if (!TextUtils.isEmpty(cacheMsgId.get(msgId)) && cacheMsgId.get(msgId).equals(msgId))
                    return;
                cacheMsgId.put(msgId, msgId);
            }*/
            if (null != listenerList) {
                for (int i = 0; i < listenerList.size(); i++) {
                    listenerList.get(i).onMessage(json);
                }
            }
            if (mainListener != null) mainListener.onMessage(json);
        }


        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
//            Log.i(TAG, "Receiving------: " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            Log.i(TAG, "Closing: " + code + " " + reason);
            resetWebSocket();
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            Log.i(TAG, "Closed: " + code + " " + reason);
            //断开连接后 释放FWWebSocket 重置Uid 预防接收2条信息 
//            INSTANCE = null;
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Log.i(TAG, "onFailure: WebSocket连接失败");
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    resetWebSocket();
                    reconnectWebSocket();
                    try {
                        Thread.sleep(3000);//重连操作
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    private String createMsgId(String uid) {
        DateFormat df = new SimpleDateFormat("yyyyMMDDHHmmssSSS");
        Date date = new Date();
        int i = new Random().nextInt(99999);
        String id = String.format(Locale.CHINA, "%010d", Integer.parseInt(uid));
        String random = String.format(Locale.CHINA, "%05d", i);
        return df.format(date) + id + random;
    }

    public String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return Build.BRAND + "-" + model;
    }

    private void reconnectWebSocket() {
        isReconnent = true;
        startRequest();
        sendLoginChatMessage(uid);
    }


    public void sendLoginChatMessage(int fromUid) {
        try {
            JSONObject clientpara = new JSONObject();
            clientpara.put("eventId", EventConstant.chatLogin);
            clientpara.put("fromUid", "" + fromUid);
            clientpara.put("token", "" + token);
            sendTextMessage(clientpara.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<OnSocketConnectListener> listenerList;

    public void addOnConnectListener(OnSocketConnectListener l) {
        if (null == listenerList) {
            listenerList = new ArrayList<>();
        }
        listenerList.add(l);
    }

    public void setOnConnectListener(OnSocketConnectListener l) {
        this.mainListener = l;
    }

    public void removeListener(OnSocketConnectListener l) {
        if (listenerList != null) listenerList.remove(l);
    }

    public interface OnSocketConnectListener {
        void onMessage(String playLoad);

        void onReconnect();
    }
}
