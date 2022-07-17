package com.fengwo.module_flirt.bean;

import com.fengwo.module_flirt.R;

/**
 * @Author BLCS
 * @Time 2020/9/23 16:28
 */
public enum GiftLevel {
    GIFT_FIRST("开启缘分", R.drawable.ic_gift_first),
    GIFT_SECOND("再续前缘", R.drawable.ic_gift_second),
    GIFT_three("缘定三生", R.drawable.ic_gift_three);

    private  String title;
    private  int drawable;

    GiftLevel(String title, int drawable) {
        this.title = title;
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
