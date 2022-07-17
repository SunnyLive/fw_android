package com.fengwo.module_chat.mvp.model.bean.uimsg;

import com.fengwo.module_websocket.bean.ChatMessage;
import com.fengwo.module_websocket.bean.MessageContentType;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/18
 */
public class MessageViewFractory {

    public static BaseMessageView createMessageView(ChatMessage chatMessage){
        BaseMessageView iMessageView = null;
        if (chatMessage.getMessageType().equals(MessageContentType.TEXT.getType() + "")){
            iMessageView = new TextMessage(chatMessage);
        }else if (chatMessage.getMessageType().equals(MessageContentType.IMAGE.getType() + "")){
            iMessageView = new ImageMessage(chatMessage);
        }else if (chatMessage.getMessageType().equals(MessageContentType.AUDIO.getType() + "")){
            iMessageView = new AudioMessage(chatMessage);
        }
        return iMessageView;
    }

}
