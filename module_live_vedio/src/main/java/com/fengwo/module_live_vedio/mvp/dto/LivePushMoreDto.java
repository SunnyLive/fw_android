package com.fengwo.module_live_vedio.mvp.dto;

import com.fengwo.module_live_vedio.R;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/7
 */
public enum  LivePushMoreDto {
    反转(R.drawable.ic_live_revere_creame,R.drawable.ic_live_revere_creame_select,"反转",false),
    闪光灯(R.drawable.ic_live_shanguangdeng,R.drawable.ic_live_shanguangdeng_select,"闪光灯",false),
    镜像开(R.drawable.ic_live_mirrir,R.drawable.ic_live_mirrir_select,"镜像开",false),
    清屏(R.drawable.ic_live_clear_screen,0,"清屏",false),
    声音(R.drawable.ic_live_mute,R.drawable.ic_live_mute_select,"静音",false),
    音效(R.drawable.ic_live_music,R.drawable.ic_live_music_select,"音效",false),
   // 大字幕(R.drawable.ic_live_dzm,"大字幕",false),
    直播间公告(R.drawable.ic_live_notice,0,"直播间公告",false),
    发消息(R.drawable.ic_live_send_message,0,"发消息",false),
   流量监控(R.drawable.ic_live_liuliangjiankong,R.drawable.ic_live_liuliangjiankong_select,"流量监控关",false),
    分享(R.drawable.ic_live_share,0,"分享",false),
    动画(R.drawable.ic_live_anim,R.drawable.ic_live_anim_select,"关闭动画",false);



    private int ivImg;
    private int F_ivImg;
    private String title;
    private boolean isOpen;

     LivePushMoreDto(int ivImg,int F_ivImg, String title, boolean isOpen) {
        this.ivImg = ivImg;
         this.F_ivImg = F_ivImg;
        this.title = title;
        this.isOpen=isOpen;
    }

    public int getF_ivImg() {
        return F_ivImg;
    }

    public void setF_ivImg(int f_ivImg) {
        F_ivImg = f_ivImg;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIvImg() {
        return ivImg;
    }

    public void setIvImg(int ivImg) {
        this.ivImg = ivImg;
    }
}
