package com.fengwo.module_websocket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;


import com.fengwo.module_websocket.security.DataSecurityUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/17
 */
public class FWWebSocket {


    public static final FWWebSocket instance = new FWWebSocket();

    public List<OnSocketConnectListener> listenerList;

    public static FWWebSocket getInstance() {
        return instance;
    }

    private FWWebSocket() {
    }

    private static final String TAG = "FWWebSocket";
    //    public static final WebSocketConnection mConnect = new WebSocketConnection();
    private ScoketMessageCreator socketMessageCreator;

    public String url = "ws://apitest.fengwohuyu.com:88/hkim";

    private int uid;

    public void setUid(int id) {
        uid = id;
    }

    private boolean isConnect = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (isConnect()) {
                        handler.removeMessages(1);
                    } else {
                        connect();
                        handler.sendEmptyMessageDelayed(1, 3000);
//                        L.e("----------断线重连-------", "断线了，重连中");
                    }
                    break;
            }

        }
    };

    /**
     * 登入成功的时候 创建连接
     */
    public void connect() {
        if (null == socketMessageCreator) {
            socketMessageCreator = new ScoketMessageCreator();
        }
//        try {
//            mConnect.connect(url, new WebSocketConnectionHandler() {
//                @Override
//                public void onConnect(ConnectionResponse response) {
//                    L.e(TAG, "Connected to server");
//                }
//
//                @Override
//                public void onOpen() {
//                    L.e(TAG, "onOpen");
//                    socketMessageCreator.sendLoginChatMessage(uid);
//                }
//
//                @Override
//                public void onClose(int code, String reason) {
//                    L.e(TAG, "onClose");
//                    mConnect.sendClose();
//                    handler.sendEmptyMessageDelayed(1, 3000);
//                }
//
//                @Override
//                public void onMessage(String payload) {
//                    String json = desUtils.decrypt(payload);
//                    Log.d(TAG, "onTextMessage: " + json);
//                    if (null != listenerList) {
//                        for (int i = 0; i < listenerList.size(); i++) {
//                            listenerList.get(i).onMessage(json);
//                        }
//                    }
//                }
//            });
//        } catch (WebSocketException e) {
//            e.printStackTrace();
//        }
//            mConnect.connect(url, new WebSocketHandler() {
//                @Override
//                public void onOpen() {
//                    isConnect = true;
//                    Log.d(TAG, "onOpen: ");
//                    socketMessageCreator.sendLoginChatMessage(uid);
//                    handler.removeMessages(2);
//                    handler.sendEmptyMessageDelayed(2, 30000);
//                }
//
//                @Override
//                public void onClose(int code, String reason) {
//                    L.e("------------onClose----", "断线了");
//                    isConnect = false;
//                    handler.sendEmptyMessageDelayed(1, 3000);
//                }
//
//                @Override
//                public void onTextMessage(String payload) {
//                    String json = desUtils.decrypt(payload);
//                    Log.d(TAG, "onTextMessage: " + json);
//                    if (null != listenerList) {
//                        for (int i = 0; i < listenerList.size(); i++) {
//                            listenerList.get(i).onMessage(json);
//                        }
//                    }
//                }
//            });
//        } catch (WebSocketException e) {
//            e.printStackTrace();
//        }
    }

    public void disconnect() {
//        mConnect.sendClose();
    }

    public boolean isConnect() {
        return isConnect;
    }

    DataSecurityUtil desUtils = new DataSecurityUtil();

    public boolean sendTextMessage(String str) {
        Log.d(TAG, "sendTextMessage: " + str);
//        if (mConnect.isConnected()) {
//            String msg = desUtils.encrypt(str);
//            mConnect.sendMessage(msg);
//            return true;
//        } else {
//            return false;
//        }
        return false;
    }


    public void addOnConnectListener(OnSocketConnectListener l) {
        if (null == listenerList) {
            listenerList = new ArrayList<>();
        }
        listenerList.add(l);
    }

    public void removeListener(OnSocketConnectListener l) {
        listenerList.remove(l);
    }

    public interface OnSocketConnectListener {
        void onMessage(String playLoad);
    }
}
