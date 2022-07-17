package com.fengwo.module_comment.event;

public class GameEvent {
    public final static int TYPE_CAIQUAN = 0;
    public final static int TYPE_TOUZI = 1;
    public final static int RECEIVE = 2;
    private int type;

    public GameEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
