package com.fengwo.module_live_vedio.mvp.ui.event;

import com.fengwo.module_live_vedio.eventbus.ChangeRoomEvent;

public class ChangeAnimateEvent {

    private boolean isAnimate;

    public ChangeAnimateEvent(boolean isAnimate) {
        this.isAnimate = isAnimate;
    }

    public boolean isAnimate() {
        return isAnimate;
    }
}
