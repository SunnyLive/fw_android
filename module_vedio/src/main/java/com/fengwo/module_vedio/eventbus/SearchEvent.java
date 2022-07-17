package com.fengwo.module_vedio.eventbus;

public class SearchEvent {
    public boolean isRefresh;

    public SearchEvent(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }
}
