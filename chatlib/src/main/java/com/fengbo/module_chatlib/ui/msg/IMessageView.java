package com.fengbo.module_chatlib.ui.msg;

import android.view.View;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fengwo.module_websocket.bean.ChatMessage;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/18
 */
public interface IMessageView extends MultiItemEntity {
    View obtainMessageView(View parent, int position);

    String getType();

    String getMsg();

    int getPosition();

    ChatMessage getChatMessage();
}
