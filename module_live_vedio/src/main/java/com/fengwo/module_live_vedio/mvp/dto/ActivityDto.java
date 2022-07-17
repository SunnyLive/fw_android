package com.fengwo.module_live_vedio.mvp.dto;

/**
 * @anchor Administrator
 * @date 2020/10/27
 */
public class ActivityDto {

    /**
     * img : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/activity/helloween/hello_ween_notice.png
     * toUid : 900501009
     * color : 7064EB
     * level : 20
     * notice : 最豪气  jgujgf来了
     */

    private String img;
    private String toUid;
    private String color;
    private String level;
    private String notice;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public ActivityDto(String img, String toUid, String color, String level, String notice) {
        this.img = img;
        this.toUid = toUid;
        this.color = color;
        this.level = level;
        this.notice = notice;
    }
}
