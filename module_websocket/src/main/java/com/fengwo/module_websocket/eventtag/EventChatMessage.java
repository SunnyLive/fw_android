package com.fengwo.module_websocket.eventtag;

import com.fengwo.module_websocket.bean.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/18
 */
public class EventChatMessage {

    public List<ChatMessage> chatMessage;

    public EventChatMessage(List<ChatMessage> chatMessage) {
        this.chatMessage = chatMessage;
    }
}
