package com.fengwo.module_flirt.manager;

import com.fengwo.module_comment.utils.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author BLCS
 * @Time 2020/5/5 14:08
 * 我的点单列表 未读消息数 管理
 */
public class HandleMsgManager {
    public Map<String,Integer> msgCaches= new HashMap<String,Integer>();
    private static HandleMsgManager INSTANCE;
    private HandleMsgManager(){}
    public static HandleMsgManager getInstance(){
        if (INSTANCE == null){
            synchronized (HandleMsgManager.class){
                if (INSTANCE == null){
                    INSTANCE = new HandleMsgManager();
                }
            }
        }
        return INSTANCE;
    }

    public void addMsg(String roomId){
        if (msgCaches.containsKey(roomId)){
            Integer integer = msgCaches.get(roomId);
            msgCaches.put(roomId,++integer);
        }else{
            msgCaches.put(roomId,1);
        }
    }

    public void cleanMes(String roomId){
        msgCaches.put(roomId,0);
    }

    public int getSize(String roomId) {
        return  msgCaches.containsKey(roomId)?msgCaches.get(roomId):0;
    }

    public Map<String, Integer> getMsgCaches() {
        return msgCaches;
    }
}
