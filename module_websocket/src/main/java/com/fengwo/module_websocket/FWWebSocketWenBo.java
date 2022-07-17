package com.fengwo.module_websocket;

import android.os.Build;
import android.util.Log;

import com.fengwo.module_websocket.bean.ReqSyncBean;
import com.fengwo.module_websocket.security.DataSecurityUtil;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;


/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/17
 */
public class FWWebSocketWenBo {
    private final String TAG = getClass().getSimpleName();
    private static FWWebSocketWenBo INSTANCE;
    ExecutorService singleThreadExecutor;
    private OnSocketConnectListener mainListener;
    private DataSecurityUtil desUtils = new DataSecurityUtil();
    private final int NORMAL_CLOSURE_STATUS = 1000;
    private boolean isReconnent = false;
    public static final int MAX_PING_PONG = 15;//最大心跳时长
    public static final int MIN_PING_PONG = 3;//最小心跳时长

    public int getUid() {
        return uid;
    }

    private int uid;
    private OkHttpClient sClient;
    private WebSocket sWebSocket;
    private String loginMsgId;
    private Disposable disposable;

    private FWWebSocketWenBo() {
        singleThreadExecutor = Executors.newSingleThreadExecutor();
        startRequest();
    }

    public void init(int uid) {
        this.uid = uid;
    }

    public static FWWebSocketWenBo getInstance() {
        if (INSTANCE == null) {
            synchronized (FWWebSocketWenBo.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FWWebSocketWenBo();
                }
            }
        }
        return INSTANCE;
    }

    public synchronized void startRequest() {
        Log.d(TAG, "startRequest");
        if (sClient == null) {
            sClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .pingInterval(5, TimeUnit.SECONDS).build();
        }
        if (sWebSocket == null) {
            Request request = new Request.Builder().url(BuildConfig.DEBUG ? Url.TEST_BASE_IM_URL_WENBO : Url.BASE_IM_URL_WENBO).build();
            EchoWebSocketListener listener = new EchoWebSocketListener();
            if(null==sClient){
                sClient = new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        .pingInterval(5, TimeUnit.SECONDS).build();
            }else
            sWebSocket = sClient.newWebSocket(request, listener);
        }
    }

    private void sendMessage(WebSocket webSocket, String msg) {
        String entryMsg = desUtils.encrypt(msg);
        Log.i(TAG, "sending: " + msg);
        webSocket.send(entryMsg);
    }

    public void sendTextMessage(String msg) {
        WebSocket webSocket;
        synchronized (FWWebSocketWenBo.class) {
            webSocket = sWebSocket;
        }
//        Log.d(TAG, "webSocket:" + webSocket + ",sWebSocket:" + sWebSocket);
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
        Log.i(TAG, "----Receiving: ");
        closeWebSocket();
        if (disposable != null && !disposable.isDisposed()){
            Log.d(TAG,"停止心跳");
            disposable.dispose();
            myTime = 0;
        }
        if (sClient != null) {
            sClient.dispatcher().executorService().shutdown();
            sClient = null;
        }
    }

    private void resetWebSocket() {
        synchronized (FWWebSocketWenBo.class) {
            sWebSocket = null;
            sClient = null;
        }
    }

    long myTime = 0;

    public void endHeartBeat(final long time) {
        if (myTime == time) {
            return;
        }
        this.myTime = time;
        if (null != disposable) {
            disposable.dispose();
        }
        disposable = Observable.interval(0, time, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                //    .observeOn(AndroidSchedulers.mainThread())
                .takeWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(Long statusInfo) throws Exception {
                        return INSTANCE != null;
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.v(TAG, "心跳？ 笑死个人了，第" + aLong + "次消心跳  间隔时间=" + myTime);
                        if (null != INSTANCE) {
                            if (time < 5) {
                                sendTextMessage(ReqSyncBean.build(uid, createMsgId(uid + ""), getModel()));
                            } else {
                                //sendTextMessage(ReqSyncBean.build(uid, createMsgId(uid + ""), getModel()));
                                sendTextMessage(ReqSyncBean.buildPing(uid, createMsgId(uid + ""), getModel()));
                            }
                        } else {
                            disposable.dispose();
                        }
                    }
                });
    }

    public class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Log.i(TAG, "WebSocket已连接");
            //init(uid);
            endHeartBeat(MAX_PING_PONG);
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
            Log.i(TAG, "----Receiving: " + json);
            if (null != listenerList) {
                for (int i = 0; i < listenerList.size(); i++) {
                    listenerList.get(i).onMessage(json);
                }
            }
            if (mainListener != null) mainListener.onMessage(json);
            try {
                if (json.contains("msgId")) {
                    JSONObject jsonObject = new JSONObject(json);
                    //服务器返回的ack消息不回复
                    if (jsonObject.has("busiEvent") && "ack".equals(jsonObject.getString("busiEvent"))) {
                        //同步请求
                        String oriMsgId = jsonObject.getString("oriMsgId");
                        if (loginMsgId.equals(oriMsgId)) {
                            //   sendTextMessage(ReqSyncBean.build(uid, createMsgId(uid + ""), getModel()));
                            endHeartBeat(MAX_PING_PONG);
                        }
                        return;
                    }
                    String msgId = jsonObject.getString("msgId");
                    String myId = Utils.createMsgId(uid + "");
                    JSONObject jsonObjectResopnse = new JSONObject();
                    jsonObjectResopnse.put("msgId", myId);
                    jsonObjectResopnse.put("oriMsgId", msgId);
                    jsonObjectResopnse.put("fromUid", uid);
                    jsonObjectResopnse.put("toUid", "-1");
                    jsonObjectResopnse.put("busiEvent", "ack");
                    jsonObjectResopnse.put("version", "2.0");
                    jsonObjectResopnse.put("vendor", Utils.getModel());
                    jsonObjectResopnse.put("timestamp", System.currentTimeMillis() + "");
                    jsonObjectResopnse.put("description", "消息回执");
                    sendMessage(sWebSocket, jsonObjectResopnse.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

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
            Log.e(TAG, "onClosed: 断开IM");
//            INSTANCE = null;
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Log.i(TAG, "onFailure: WebSocket连接失败" + t.getMessage());
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

    public void reconnectWebSocket() {
        Log.d(TAG, "reconnectWebSocket");
        isReconnent = true;
        startRequest();
//        sendLoginChatMessage(uid);
    }

    /**
     * 登录
     *
     * @param fromUid
     */
    public void sendLoginChatMessage(int fromUid) {

        try {
            loginMsgId = createMsgId(fromUid + "");
            JSONObject clientpara = new JSONObject();
            clientpara.put("fromUid", "" + fromUid);
            clientpara.put("toUid", "-1");
            clientpara.put("msgId", loginMsgId);
            clientpara.put("version", "2.0");
            clientpara.put("vendor", getModel());
            clientpara.put("busiEvent", "connect");
            clientpara.put("description", "客户端连接");
            clientpara.put("timestamp", System.currentTimeMillis() + "");
            sendTextMessage(clientpara.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static CopyOnWriteArrayList<OnSocketConnectListener> listenerList;

    public void addOnConnectListener(OnSocketConnectListener l) {
        if (null == listenerList) {
            listenerList = new CopyOnWriteArrayList<>();//保证遍历时，避免remove数据造成闪退
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

        void onFaildMsg(String msgid);

    }

    public void faildMsg(String msgid) {

        if (null != listenerList) {
            for (int i = 0; i < listenerList.size(); i++) {
                listenerList.get(i).onFaildMsg(msgid);
            }
        }

    }

    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return Build.BRAND + "-" + model;
    }
}
