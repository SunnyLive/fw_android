package com.fengbo.module_chatlib.ui.msg;

import com.fengwo.module_websocket.bean.ChatMessage;
import com.fengwo.module_websocket.bean.MessageContentType;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/18
 */
public class MessageViewFractory {

    public static IMessageView createMessageView(ChatMessage chatMessage, int type) {
        IMessageView iMessageView = null;
        if (chatMessage.getMessageType().equals(MessageContentType.TEXT.getType() + "")) {
            iMessageView = new TextMessage(chatMessage, type);
        } else if (chatMessage.getMessageType().equals(MessageContentType.IMAGE.getType() + "")) {
            iMessageView = new ImageMessage(chatMessage, type);
        } else if (chatMessage.getMessageType().equals(MessageContentType.AUDIO.getType() + "")) {
            iMessageView = new AudioMessage(chatMessage, type);
        }
        return iMessageView;
    }

    public static IMessageView createRebackMessage(ChatMessage chatMessage, int type) {
        chatMessage.setMessageType(MessageContentType.TEXT.getType() + "");
        chatMessage.setMessage("消息已撤回");
        IMessageView messageView = new TextMessage(chatMessage, type);
        return messageView;
    }

}
