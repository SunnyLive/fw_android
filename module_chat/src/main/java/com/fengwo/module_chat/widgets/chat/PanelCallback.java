package com.fengwo.module_chat.widgets.chat;

/**
 * @author sunhee
 * @intro
 * @date 2019/9/17
 */
public interface PanelCallback {

    void sendText(String str);

    void sendImage();

    void sendCamera();

    void sendAudio(String filePath, int sound_length);

    void bottomShow();
}
