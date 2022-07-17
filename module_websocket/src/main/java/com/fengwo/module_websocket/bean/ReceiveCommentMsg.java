package com.fengwo.module_websocket.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 评价消息
 */
public class ReceiveCommentMsg {

    /**
     * action : evaluationAnchorMsg
     * duration : 372
     * startLevel : 5
     * evTypes : 本人超美,聊得很开心,想成为她男朋友,治愈我心,善解人意,超绝可爱,重回初恋,文艺女青年
     * giftList : [{"giftId":39,"giftName":"多巴胺炸弹","giftNum":1,"giftBigImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/gift/1589879284000*duobaanzhadan.mp4","giftSmallImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1589870813000*%E5%A4%9A%E5%B7%B4%E8%83%BA%E7%82%B8%E5%BC%B9.png"},{"giftId":41,"giftName":"迷情香水","giftNum":1,"giftBigImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/gift/1589879318000*miqingxiangshui.mp4","giftSmallImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1589870872000*%E8%BF%B7%E6%83%85%E9%A6%99%E6%B0%B4.png"},{"giftId":42,"giftName":"专属天使","giftNum":1,"giftBigImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/gift/1589879334000*zhuanshutianshi.mp4","giftSmallImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1589870897000*%E4%B8%93%E5%B1%9E%E5%A4%A9%E4%BD%BF.png"},{"giftId":40,"giftName":"极乐鸟","giftNum":1,"giftBigImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/gift/1589879298000*jileniao.mp4","giftSmallImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1589870844000*%E6%9E%81%E4%B9%90%E9%B8%9F.png"},{"giftId":43,"giftName":"为爱启航","giftNum":1,"giftBigImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/gift/1596449820000*gift7293579052.mp4","giftSmallImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1596449807000*gift4799713105.png"},{"giftId":44,"giftName":"情定终身","giftNum":1,"giftBigImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/gift/1596449853000*gift9948053922.mp4","giftSmallImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1596449844000*gift4490198127.png"},{"giftId":45,"giftName":"守护降临","giftNum":1,"giftBigImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/gift/1596449865000*gift4354022102.mp4","giftSmallImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1596449862000*gift5440105973.png"},{"giftId":46,"giftName":"梦幻之城","giftNum":1,"giftBigImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/gift/1596449906000*gift695974677.mp4","giftSmallImgPath":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1596449887000*gift376085898.png"}]
     * user : {"userId":"500895","nickname":"乖乖～","role":null,"headImg":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/500895/images_1581601991_QyU2SVgT95.jpeg?x-oss-process=style/500x500_watermark"}
     * anchor : {"userId":"821894","nickname":"草莓味女孩","role":null,"headImg":"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/1595480183255.jpg"}
     * content : {"type":"text","value":"[评价]"}
     */

    @SerializedName("action")
    private String action;
    @SerializedName("duration")
    private Integer duration;
    @SerializedName("startLevel")
    private Integer startLevel;
    @SerializedName("evTypes")
    private String evTypes;
    @SerializedName("user")
    private UserDTO user;
    @SerializedName("anchor")
    private AnchorDTO anchor;
    @SerializedName("content")
    private ContentDTO content;
    @SerializedName("giftList")
    private List<GiftListDTO> giftList;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getStartLevel() {
        return startLevel;
    }

    public void setStartLevel(Integer startLevel) {
        this.startLevel = startLevel;
    }

    public String getEvTypes() {
        return evTypes;
    }

    public void setEvTypes(String evTypes) {
        this.evTypes = evTypes;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AnchorDTO getAnchor() {
        return anchor;
    }

    public void setAnchor(AnchorDTO anchor) {
        this.anchor = anchor;
    }

    public ContentDTO getContent() {
        return content;
    }

    public void setContent(ContentDTO content) {
        this.content = content;
    }

    public List<GiftListDTO> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<GiftListDTO> giftList) {
        this.giftList = giftList;
    }

    public static class UserDTO {
        /**
         * userId : 500895
         * nickname : 乖乖～
         * role : null
         * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/500895/images_1581601991_QyU2SVgT95.jpeg?x-oss-process=style/500x500_watermark
         */

        @SerializedName("userId")
        private String userId;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("role")
        private Object role;
        @SerializedName("headImg")
        private String headImg;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Object getRole() {
            return role;
        }

        public void setRole(Object role) {
            this.role = role;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }
    }

    public static class AnchorDTO {
        /**
         * userId : 821894
         * nickname : 草莓味女孩
         * role : null
         * headImg : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/1595480183255.jpg
         */

        @SerializedName("userId")
        private String userId;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("role")
        private Object role;
        @SerializedName("headImg")
        private String headImg;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Object getRole() {
            return role;
        }

        public void setRole(Object role) {
            this.role = role;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }
    }

    public static class ContentDTO {
        /**
         * type : text
         * value : [评价]
         */

        @SerializedName("type")
        private String type;
        @SerializedName("value")
        private String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class GiftListDTO {
        /**
         * giftId : 39
         * giftName : 多巴胺炸弹
         * giftNum : 1
         * giftBigImgPath : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/gift/1589879284000*duobaanzhadan.mp4
         * giftSmallImgPath : https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1589870813000*%E5%A4%9A%E5%B7%B4%E8%83%BA%E7%82%B8%E5%BC%B9.png
         */

        @SerializedName("giftId")
        private Integer giftId;
        @SerializedName("giftName")
        private String giftName;
        @SerializedName("giftNum")
        private Integer giftNum;
        @SerializedName("giftBigImgPath")
        private String giftBigImgPath;
        @SerializedName("giftSmallImgPath")
        private String giftSmallImgPath;

        public Integer getGiftId() {
            return giftId;
        }

        public void setGiftId(Integer giftId) {
            this.giftId = giftId;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public Integer getGiftNum() {
            return giftNum;
        }

        public void setGiftNum(Integer giftNum) {
            this.giftNum = giftNum;
        }

        public String getGiftBigImgPath() {
            return giftBigImgPath;
        }

        public void setGiftBigImgPath(String giftBigImgPath) {
            this.giftBigImgPath = giftBigImgPath;
        }

        public String getGiftSmallImgPath() {
            return giftSmallImgPath;
        }

        public void setGiftSmallImgPath(String giftSmallImgPath) {
            this.giftSmallImgPath = giftSmallImgPath;
        }
    }
}
