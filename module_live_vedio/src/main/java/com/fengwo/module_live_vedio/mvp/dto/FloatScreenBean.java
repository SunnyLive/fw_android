package com.fengwo.module_live_vedio.mvp.dto;

import com.fengwo.module_comment.bean.PkFloatingScreenBean;

public class FloatScreenBean {
    public WatcherDto watcherDto;
    public PkFloatingScreenBean pkFloatingScreenBean;
    public ActivityDto activityDto;

    public FloatScreenBean(WatcherDto watcherDto, PkFloatingScreenBean pkFloatingScreenBean,ActivityDto activityDto) {
        this.watcherDto = watcherDto;
        this.pkFloatingScreenBean = pkFloatingScreenBean;
        this.activityDto = activityDto;
    }
}
