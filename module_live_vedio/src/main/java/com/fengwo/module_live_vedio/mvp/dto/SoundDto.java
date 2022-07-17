package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @anchor Administrator
 * @date 2020/9/2
 */
public class SoundDto {
    private boolean open;
    private String title;

    public SoundDto(boolean open, String title) {
        this.open = open;
        this.title = title;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
