package com.fengwo.module_flirt.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 评价详情
 */
public class CommentDetailsDTO {

    /**
     * anchorHeadImg : string
     * anchorId : 0
     * anchorNickname : string
     * createTime : 2020-11-03T14:28:22.066Z
     * duration : 0
     * durationFormat : string
     * evTypes : string
     * giftList : [{"giftCount":0,"giftName":"string","id":0,"smallImgPath":"string"}]
     * id : 0
     * startLevel : 0
     * userHeadImg : string
     * userId : 0
     * userNickname : string
     */

    @SerializedName("anchorHeadImg")
    private String anchorHeadImg;
    @SerializedName("anchorId")
    private Integer anchorId;
    @SerializedName("anchorNickname")
    private String anchorNickname;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("duration")
    private Integer duration;
    @SerializedName("durationFormat")
    private String durationFormat;
    @SerializedName("evTypes")
    private String evTypes;
    @SerializedName("id")
    private Integer id;
    @SerializedName("startLevel")
    private Integer startLevel;
    @SerializedName("userHeadImg")
    private String userHeadImg;
    @SerializedName("userId")
    private Integer userId;
    @SerializedName("userNickname")
    private String userNickname;
    @SerializedName("giftList")
    private List<GiftListDTO> giftList;

    public String getAnchorHeadImg() {
        return anchorHeadImg;
    }

    public void setAnchorHeadImg(String anchorHeadImg) {
        this.anchorHeadImg = anchorHeadImg;
    }

    public Integer getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(Integer anchorId) {
        this.anchorId = anchorId;
    }

    public String getAnchorNickname() {
        return anchorNickname;
    }

    public void setAnchorNickname(String anchorNickname) {
        this.anchorNickname = anchorNickname;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDurationFormat() {
        return durationFormat;
    }

    public void setDurationFormat(String durationFormat) {
        this.durationFormat = durationFormat;
    }

    public String getEvTypes() {
        return evTypes;
    }

    public void setEvTypes(String evTypes) {
        this.evTypes = evTypes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStartLevel() {
        return startLevel;
    }

    public void setStartLevel(Integer startLevel) {
        this.startLevel = startLevel;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public List<GiftListDTO> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<GiftListDTO> giftList) {
        this.giftList = giftList;
    }

    public static class GiftListDTO {
        /**
         * giftCount : 0
         * giftName : string
         * id : 0
         * smallImgPath : string
         */

        @SerializedName("giftCount")
        private Integer giftCount;
        @SerializedName("giftName")
        private String giftName;
        @SerializedName("id")
        private Integer id;
        @SerializedName("smallImgPath")
        private String smallImgPath;

        public Integer getGiftCount() {
            return giftCount;
        }

        public void setGiftCount(Integer giftCount) {
            this.giftCount = giftCount;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSmallImgPath() {
            return smallImgPath;
        }

        public void setSmallImgPath(String smallImgPath) {
            this.smallImgPath = smallImgPath;
        }
    }
}
