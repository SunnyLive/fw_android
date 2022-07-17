package com.fengwo.module_chat.utils.manager;

import com.fengwo.module_chat.base.ChatSocketRequest;
import com.fengwo.module_chat.base.RefreshChatMessageEvent;
import com.fengwo.module_chat.base.RefreshMessageListEvent;
import com.fengwo.module_chat.utils.chat_new.ChatGreenDaoHelper;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;

/**
 * 聊天记录功能
 * @Author BLCS
 * @Time 2020/3/13 18:08
 */
public class ChatFunManager {
    public static void withdrawMessage(ChatGreenDaoHelper daoHelper, String fingerPring,boolean isGroup,String name){
        daoHelper.revocationChatMsg(fingerPring,isGroup,name)
                .subscribe(chatMsgEntity -> { //刷新聊天界面
                    L.e("========"+chatMsgEntity);
                    RxBus.get().post(new RefreshChatMessageEvent(chatMsgEntity, RefreshChatMessageEvent.TYPE_SEND));
                });
        daoHelper.refreshMessageList(fingerPring,name)
                .subscribe(chatMsgEntity -> { //刷新聊天列表
                    L.e("========"+chatMsgEntity);
                    RxBus.get().post(new RefreshMessageListEvent());
                }, Throwable::printStackTrace);
    }
}
