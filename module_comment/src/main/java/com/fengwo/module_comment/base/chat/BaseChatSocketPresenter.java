//package com.fengwo.module_comment.base.chat;
//
//import android.widget.Toast;
//
//import com.fengwo.module_comment.base.BasePresenter;
//import com.fengwo.module_comment.base.MvpView;
//import com.fengwo.module_websocket.EventConstant;
//import com.fengwo.module_websocket.FWWebSocket;
//import com.fengwo.module_websocket.bean.ChatContent;
//import com.fengwo.module_websocket.security.DataSecurityUtil;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.lang.reflect.Array;
//import java.util.ArrayList;
//import java.util.List;
//
//public class BaseChatSocketPresenter<V extends IBaseChatView> extends BasePresenter<V> {
//    DataSecurityUtil desUtils = new DataSecurityUtil();
//
//    private String uid, toId;// uid>800000000 时 表示在 群 里聊天
//    FWWebSocket webSocket;
//
//
//    public void setFromId(String id) {
//        uid = id;
//    }
//
//    public void setToId(String id) {
//        toId = id;
//    }
//
//    @Override
//    public void attachView(V mvpView) {
//        super.attachView(mvpView);
//    }
//
//    @Override
//    public void detachView() {
//        super.detachView();
//    }
//
//    /**
//     * 必须设置 uid toid；由上层获取 传入
//     */
//    public BaseChatSocketPresenter() {
//        webSocket = new FWWebSocket();
//    }
//
//    /**
//     * 进入聊天室调用 自动获取20条聊天数据
//     */
//    public void connnect() {
//        webSocket.setOnConnectListener(new FWWebSocket.OnSocketConnectListener() {
//            @Override
//            public void onOpen() {
//                loginChat(uid);
//            }
//
//            @Override
//            public void onClose(int code, String reason) {
//
//            }
//
//            @Override
//            public void onMessage(String playLoad) {
//                socketMessage(playLoad);
//            }
//        });
//        webSocket.connect();
//    }
//
//    /**
//     * 登入聊天室
//     *
//     * @param fromUid
//     */
//    private void loginChat(String fromUid) {
//        try {
//            JSONObject clientpara = new JSONObject();
//            clientpara.put("eventId", EventConstant.chatLogin);
//            clientpara.put("fromUid", fromUid);
//            String msg = desUtils.encrypt(clientpara.toString());
//            if (webSocket.isConnect()) {
//                webSocket.sendTextMessage(msg);
//            } else {
//                getView().toastTip("链接服务器失败");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 第一次进入 获取20条聊天数据
//     *
//     * @param nowNum 传入当前总条数，即可向上获取20条历史消息 传0获取最新20条数据  vIWxXimQqv4BVqEQKQ5Pc43IBR+H9RCV0YnK1RIP1FQrn/7WWbXgiL/rZ++wklUXoZVGTPV42nskh0vSTLVW6A==
//     */
//    public void getFisrt20(int nowNum) {
//        try {
//            JSONObject clientpara = new JSONObject();
//            clientpara.put("eventId", EventConstant.firstComeinMessage);
//            clientpara.put("fromUid", uid);
//            clientpara.put("toUid", toId);
//            clientpara.put("index_num", nowNum + "");//
//            if (webSocket.isConnect()) {
//                webSocket.sendTextMessage(clientpara.toString());
//            } else {
//                getView().toastTip("链接服务器失败");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    //处理socket的消息
//
//    /**
//     * 处理socket返回的数据
//     *
//     * @param json
//     */
//    private void socketMessage(String json) {
//        try {
//            JSONObject jsonObject = new JSONObject(json);
//            String status = jsonObject.getString("status");
//            if ("OK".equals(status)) {
//                String eventId = jsonObject.getString("eventId");
//                int eventId_int = Integer.parseInt(eventId);
//                switch (eventId_int) {
//                    case EventConstant.chatLogin://登录
//                        getFisrt20(0);
//                        break;
//                    case EventConstant.firstComeinMessage:// 消息进入时调用（包括新消息 以及 第一次进来抓取最近20条）
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//                        processMessage(jsonArray);//处理消息
//                        break;
//                    case EventConstant.loopMessage://向上翻页
//                        jsonArray = jsonObject.getJSONArray("data");
////                        processLoopMessage(jsonArray);//处理消息
//                        break;
//                    case EventConstant.deleteAChatMessage://删除一条消息
//                        String guidList = jsonObject.getString("description");
////                        deleteItem(guidList);//处理消息
//                        break;
//                    case EventConstant.rebackAChatMessage://撤回一条消息
//                        guidList = jsonObject.getString("guid");
//                        String rebackUid = jsonObject.getString("rebackUid");
////                        rebackItem(guidList, rebackUid);//处理消息
//                        break;
//                    default:
//                        break;
//                }
//            } else {
////                String description = jsonObject.getString("description");
////                Alerter.create(ChatViewActivity.this)
////                        .setTitle("错误提示")
////                        .setText(description)
////                        .setBackgroundColorRes(R.color.errorColor)
////                        .show();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    //处理消息包(包括获取历史消息 和新消息)
//    private void processMessage(JSONArray jsonArray) {
//        try {
//            if (jsonArray.length() > 0) {
//                List<ChatContent> chatContents = new ArrayList<>();
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject abody = jsonArray.getJSONObject(i);
//                    String guid = abody.getString("guid");
//                    String fromUid = abody.getString("fromUid");
//                    String toUid = abody.getString("toUid");
//                    String message_type = abody.getString("message_type");
//                    String message = abody.getString("message");
//                    String add_time = abody.getString("add_time");
//                    ChatContent chatContent = new ChatContent();
//                    chatContent.setGuid(guid);
//                    chatContent.setFromUid(fromUid);
//                    chatContent.setToUid(toUid);
//                    chatContent.setMessage(message);
//                    chatContent.setMessage_type(message_type);
//                    chatContent.setAddTime(add_time);
//                    chatContents.add(chatContent);
//                }
//                getView().setMessage(chatContents);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//}
