package com.fengwo.module_login.mvp.dto;

import android.view.View;

public class MineServerDto {

    public int image;
    public String title;
    public View.OnClickListener ol;

    public MineServerDto(int image, String title, View.OnClickListener ol) {
        this.image = image;
        this.title = title;
        this.ol = ol;
    }
}
