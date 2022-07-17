package com.fengwo.module_websocket;


import com.fengwo.module_websocket.bean.LivingRoomTextMsg;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.SocketResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class ScoketMessageCreator {
    FWWebSocket fwWebSocket = FWWebSocket.getInstance();

    public ScoketMessageCreator() {
    }


    public void sendLoginChatMessage(int fromUid) {
        try {
            JSONObject clientpara = new JSONObject();
            clientpara.put("eventId", EventConstant.chatLogin);
            clientpara.put("fromUid", "" + fromUid);
            if (fwWebSocket.isConnect()) {
                fwWebSocket.sendTextMessage(clientpara.toString());
            } else {
                fwWebSocket.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loopMessage(int uid, int toId, int allAccount) {
        try {
            JSONObject clientpara = new JSONObject();
            clientpara.put("eventId", EventConstant.loopMessage);
            clientpara.put("fromUid", "" + uid);
            clientpara.put("toUid", "" + toId);
            clientpara.put("indexNum", allAccount + "");
            if (FWWebSocket.getInstance().isConnect()) {
                FWWebSocket.getInstance().sendTextMessage(clientpara.toString());
            } else {
                fwWebSocket.connect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendLivingTextMsg(int uid, int groupId, String text, boolean isDanmu, String nickname, String headerurl, String level) {


        try {
            JSONObject clientpara = new JSONObject();
            clientpara.put("eventId", EventConstant.saveAMessage);
            clientpara.put("fromUid", "" + uid);
            clientpara.put("toUid", "" + groupId);
            clientpara.put("forward", "0");
            clientpara.put("message", text);
            clientpara.put("messageType", "0");
            clientpara.put("headerurl", headerurl);
            clientpara.put("level", level);
            clientpara.put("isDanmu", isDanmu);
            clientpara.put("nickname", nickname);
            if (fwWebSocket.isConnect()) {
                fwWebSocket.sendTextMessage(clientpara.toString());
            } else {
                fwWebSocket.connect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendGiftMsg(int fromId, int toId, String giftImg, String giftName, String userNickname, String userHeaderUrl, String swf, int sendNum) {
        try {
            JSONObject clientpara = new JSONObject();
            clientpara.put("eventId", EventConstant.broadcastMessage);
            clientpara.put("fromUid", "" + fromId);
            clientpara.put("groupId", "" + toId);
            clientpara.put("messageType", "0");
            clientpara.put("headerurl", userHeaderUrl);
            clientpara.put("nickname", userNickname);
            clientpara.put("giftImg", giftImg);
            clientpara.put("giftName", giftName);
            clientpara.put("sendNum", sendNum);
            clientpara.put("giftSwf", swf);
            clientpara.put(EventConstant.ACTIONID, EventConstant.SEND_GIFT);
            if (fwWebSocket.isConnect()) {
                fwWebSocket.sendTextMessage(clientpara.toString());
            } else {
                fwWebSocket.connect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void joinGroup(int uid, int groupId, String imgUrl, String nickname, String level, String consumNums) {
        try {
            JSONObject clientpara = new JSONObject();
            clientpara.put("eventId", EventConstant.joinGroup + "");
            clientpara.put("fromUid", "" + uid);
            clientpara.put("uid", "" + uid);
            clientpara.put("groupId", groupId);
            clientpara.put("headerurl", imgUrl);
            clientpara.put("nickname", nickname);
            clientpara.put("level", level);
            clientpara.put("consumNums", consumNums);//在本直播间 消费值 用来排序用户
            if (fwWebSocket.isConnect()) {
                fwWebSocket.sendTextMessage(clientpara.toString());
            } else {
                fwWebSocket.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void leaveGroup(int uid, int groupId) {
        try {
            JSONObject clientpara = new JSONObject();
            clientpara.put("eventId", EventConstant.leaveGroup + "");
            clientpara.put("uid", "" + uid);
            clientpara.put("fromUid", "" + uid);
            clientpara.put("groupId", groupId);
            if (fwWebSocket.isConnect()) {
                fwWebSocket.sendTextMessage(clientpara.toString());
            } else {
                fwWebSocket.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第一次进入 获取20条聊天数据
     *
     * @param nowNum 传入当前总条数，即可向上获取20条历史消息 传0获取最新20条数据  vIWxXimQqv4BVqEQKQ5Pc43IBR+H9RCV0YnK1RIP1FQrn/7WWbXgiL/rZ++wklUXoZVGTPV42nskh0vSTLVW6A==
     */
    public void getFisrt20(int uid, int toId, int nowNum) {
        try {
            JSONObject clientpara = new JSONObject();
            clientpara.put("eventId", EventConstant.firstComeinMessage);
            clientpara.put("fromUid", uid);
            clientpara.put("toUid", toId);
            clientpara.put("indexNum", nowNum + "");//
            if (fwWebSocket.isConnect()) {
                fwWebSocket.sendTextMessage(clientpara.toString());
            } else {
                fwWebSocket.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAudio(int uid, int toId, String url, File f, long sound_length) {
        int index = url.indexOf(".com");
        String str = url.substring(0, index + ".com".length());
        String str2 = url.substring(index + ".com".length(), url.length());
        String message = f.getName() + "&" + str + "&" + str2 + "&" + sound_length;
        JSONObject clientpara = new JSONObject();
        try {
            clientpara.put("eventId", EventConstant.saveAMessage);
            clientpara.put("fromUid", "" + uid);
            clientpara.put("toUid", "" + toId);
            clientpara.put("forward", "0");
            clientpara.put("message", message);
            clientpara.put("messageType", "2");
            if (fwWebSocket.isConnect()) {
                fwWebSocket.sendTextMessage(clientpara.toString());
            } else {
                fwWebSocket.connect();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
