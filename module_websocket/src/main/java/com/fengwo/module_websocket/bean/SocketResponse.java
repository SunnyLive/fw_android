package com.fengwo.module_websocket.bean;

public class SocketResponse<T> {
    public int eventId;
    public int fromUid;
    public int toUid;
    public T data;
}
