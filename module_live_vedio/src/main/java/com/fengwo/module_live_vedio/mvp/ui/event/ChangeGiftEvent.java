package com.fengwo.module_live_vedio.mvp.ui.event;

import com.fengwo.module_live_vedio.mvp.dto.GiftDto;

public class ChangeGiftEvent {
    public GiftDto dto;
    public int position;
public  boolean istype ;
    public ChangeGiftEvent(GiftDto dto,int position,boolean istype) {
        this.dto = dto;
        this.position = position;
        this.istype = istype;
    }
}
