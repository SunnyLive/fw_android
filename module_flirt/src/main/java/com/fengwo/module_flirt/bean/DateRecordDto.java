package com.fengwo.module_flirt.bean;

public class DateRecordDto {

    /**
     * anchorId : 0
     * apponitStatus : 0
     * apponitTime : string
     * createTime : 2020-05-04T03:58:11.371Z
     * headImg : string
     * id : 0
     * nickname : string
     */

    private int anchorId;
    private int apponitStatus;//0-待接单 998-未开始 1-已连接 3-已完成 5-已接受（未连接）999-过时
    private String apponitTime;
    private FwTime createTime;
    private String headImg;
    private int id;
    private String nickname;



    public static class ApponitTime{

    }

    public static class FwTime{
        private long epochSecond;
        private int nano;

        public long getEpochSecond() {
            return epochSecond;
        }

        public void setEpochSecond(long epochSecond) {
            this.epochSecond = epochSecond;
        }

        public int getNano() {
            return nano;
        }

        public void setNano(int nano) {
            this.nano = nano;
        }
    }

    public boolean isCanCancle() {
        if (apponitStatus == 999||apponitStatus == 998||apponitStatus == 3) {
            return false;
        }
        return true;
    }

    public String getButtonText() {
        switch (apponitStatus) {
            case 0:
                return "撤销申请";//未开始
            case 1:
            case 5:
                return "去看看";//已连接
            default:
                return "去看看";
        }
    }
    public String getStatusTxt() {
        switch (apponitStatus) {
            case 0:
                return "待接单";//未开始
            case 998:
                return "未开始";//未开始
            case 1:
            case 5:
                return "已接入";//已连接
            case 3:
                return "已完成";//已完成
            case 999:
                return "已过期";//已过期
            default:
                return "待接单";
        }
    }

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public int getApponitStatus() {
        return apponitStatus;
    }

    public void setApponitStatus(int apponitStatus) {
        this.apponitStatus = apponitStatus;
    }

    public String getApponitTime() {
        return apponitTime;
    }

    public void setApponitTime(String apponitTime) {
        this.apponitTime = apponitTime;
    }

    public FwTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(FwTime createTime) {
        this.createTime = createTime;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
