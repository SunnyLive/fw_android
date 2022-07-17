package com.fengwo.module_flirt.bean;

/**
 * @Author BLCS
 * @Time 2020/4/29 14:20
 */
public class SureAppointmentBean {

    /**
     * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/501229/images_1585627243_bwx1PolGqi.jpeg
     * description : 心动小屋搭建中，预计还有-33秒搭建完成，记得准时入住哦
     * anchorId : 501229
     * appointmentTime : 2020年05月05日16:00-17:00
     * nickname : 安娜哟这么
     */

    private String headImg;
    private String description;
    private String anchorId;
    private String appointmentTime;
    private String nickname;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
