package com.fengwo.module_comment.iservice;

import java.util.List;

public class UserInfo {

    public final static int IDCARD_STATUS_NULL = 0; // 主播未认证
    public final static int IDCARD_STATUS_YES = 1;  // 主播已认证
    public final static int IDCARD_STATUS_NO = 2;   // 主播认证失败
    public final static int IDCARD_STATUS_ING = 3;  // 主播认证中

    public final static int WENBO_STATUS_ING = 0;       // 达人审核中
    public final static int WENBO_STATUS_YES = 1;       // 达人通过
    public final static int WENBO_STATUS_NO = 2;        // 达人未通过
    public final static int WENBO_STATUS_NULL = 3;      // 达人未申请

    public static int MY_ID_CARD_REAL_NULL = 0; // 未实名认证
    public static int MY_ID_CARD_REAL_YES = 1;      // 已经实名认证
    public static int MY_ID_CARD_REAL_NO = 2;      // 已经实名认证
    public static int MY_ID_CARD_REAL_ING = 3;      // 已经实名认证

    public static final int BOY = 1;
    public static final int GILR = 2;

    public String account;
    public String birthday;
    public String age;
    public String experience;//经验值
    public String fwId;
    public String headImg;
    public int id = 0;
    public String location;
    public String mobile;
    public String nickname;
    public int sex;  //0：保密，1：男；2：女
    public String signature;
    public int userLevel;
    public String motoringSwf = "";
    public String motoringName = "";
    public String motoringThumb;
    public String words;
    public int frameRate;
    public int charge;//用户总充值
    public String todayReceive;
    public String height;  //身高
    public String weight;
    public String constellation; //用户星座
    public String idealTypeTag;  //理想型
    public int userGuardNum;   //守护数量
    public int duration;
    public int myLiveLevel;
    public int onlineStatus;   //在线状态 0：离线 1：在线
    public int isOfficialUser;
    public double distance;  //距离
    public boolean myIsGuard;    //是否守护
    public String myGuardDeadline;   //守护剩余时间
    public Integer familyId; //公会id
    public String familyLogo;//公会logo
    public String familyName;//公会名称
    public String familyIntroduction;
    public Integer familyApplyStatus;//公会申请状态 -1：未申请，0：拒绝，1：申请中，2：通过
    public int isManage;   //是否是房管  0：否 1：是
    public String realName; //用户真实姓名
    public List<UserMedalsList> userMedalsList;//用户勋章
    public List<UserGuardList> userGuardList;  //用户守护列表
    public List<UserPhotosList> userPhotos;   //照片墙

    public boolean myIsCard;//是否实名认证
    public int myIsCardStatus;//主播认证状态  0未认证 1通过 2失败 3认证中
    public boolean myIsLive;//我的是否是主播
    public boolean myIsQq;//我的是否 qq
    public boolean myIsWx;//我的 是否 wx
    public boolean isWeboSet;//是否设置同城资料
    public int myVipLevel;//我的贵族等级
    public long myLiveDuration;  //直播时长
    public long todayLivingTime;
    public int isBank;
    public String userRole; //ROLE_USER,ROLE_ANCHOR_WENBO,ROLE_ANCHOR,ROLE_USER_WENBO
    public int myIdCardWithdraw; //1:实名认证 （提现认证，聊天双方必须实名认证）
    public int wenboAnchorStatus; //达人申请状态：0申请中 1申请通过 2申请未通过 3未申请
    public int liveStatus;//主播的直播状态
    public int wenboLiveStatus;// 达人的直播状态
    public String cpRank;
    public String cpHeadImg;


    public double receive;
    public long balance;
    public double profit;


    public int fans;
    public int attention;
    public int friendNum;
    public String thumb;    //默认上一次直播封面


   // public long forbiddenWords;//被禁言截止时间戳
    public int privateLetter;//私信功能 1：允许  0：禁止


    public int isAttention;//1:已关注 2:互相关注 其他:关注
    public String liveLabel;
    public int liveLevel;
    public int liveLevelExperience;
    public int liveLevelHighest;
    public int liveLevelLowest;
    public int movieCount;
    public int videoCount;
    public int vipLevel;
    public long sendGiftTotal;
    public int level;
    public double totalProfit;

    public int joinCircleNum;
    public int cardNum;


    public int likeCardCount;
    public int likeMovieCount;
    public int likeVideoCount;

    public int beLikeCardCount;
    public int beLikeMovieCount;
    public int beLikeVideoCount;

    public boolean isFamily;  //是否加入公会
    public int vipRemainDays; //贵族剩余天数
    private boolean isFace;   // true 已进行人脸识别， false 未进行人脸识别

    public long todayTotalTime;//今日印象值
    private List<TodayUserTime> todayUserTimes;

    public String teenagerDuration;//青少年模式-时长
    public String teenagerMode;//青少年模式-开关-1:启用0:未启用
    public String teenagerPassword;//青少年模式-密码

    public List<TodayUserTime> getTodayUserTimes() {
        return todayUserTimes;
    }

    public int getMyIsCardStatus() {
        return myIsCardStatus;
    }

    public void setMyIsCardStatus(int myIsCardStatus) {
        this.myIsCardStatus = myIsCardStatus;
    }

    public int getMyIdCardWithdraw() {
        return myIdCardWithdraw;
    }

    public void setMyIdCardWithdraw(int myIdCardWithdraw) {
        this.myIdCardWithdraw = myIdCardWithdraw;
    }

    public boolean isReanName() {
        return myIsCardStatus == IDCARD_STATUS_YES;
    }

    public int getMyLiveLevel() {
        if (myLiveLevel <= 0) {
            myLiveLevel = 1;
        } else if (myLiveLevel >= 50) {
            myLiveLevel = 50;
        }
        return myLiveLevel;
    }

    public int getUserLevel() {
        if (userLevel <= 0) {
            userLevel = 1;
        } else if (userLevel >= 50) {
            userLevel = 50;
        }
        return userLevel;
    }

    public boolean isWenboRole() {//
        return userRole != null && userRole.contains("ROLE_ANCHOR_WENBO");
    }

    public boolean isWenboUser() {//
        return userRole != null && userRole.contains("ROLE_USER_WENBO");
    }

    //是否可文播开播
    public boolean isWenboPush() {
        return userRole != null && userRole.contains("ROLE_ANCHOR_WENBO") && isWeboSet;
    }

    public boolean isFace() {
        return isFace;
    }

    public void setFace(boolean face) {
        isFace = face;
    }

    /**
     *
     *
     * {
     *         "id": null,
     *         "userId": null,
     *         "medalId": 30,
     *         "createTime": "2020-10-25T09:56:46.000+0000",
     *         "updateTime": null,
     *         "nickname": null,
     *         "medalName": null,
     *         "medalType": null,
     *         "effective": "365",
     *         "effectiveType": 2,
     *         "status": 1,
     *         "expireTime": "2020-10-27T09:56:49.000+0000",
     *         "medalIcon": "https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/activityList/1603437314000*activityList1237578630.png",
     *         "medalHeadFrame": "https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/activity/helloween/user_award_head_img.png"
     *       }
     *
     *
     *
     */
    public static class UserMedalsList {
        public int id;
        public int medalId;
        public int userId;
        public String createTime;
        public String expireTime;
        public String medalIcon;     // 徽章
        public String medalHeadFrame; //相框
    }


    public static class UserGuardList {
        public int guardUserId;
        public String guardUserNickname;
        public String guardUserHeadImg;
        public int level;
        public int quantity;
        public String levelName;
        public String levelIcon;

    }


    public static class UserPhotosList {
        public int id;
        public int photoStatus;   //0审核中 1审核通过  2:审核失败
        public String photoUrl;
        public String videoPath;
        public String fileUrl;
        public int anchorId;
        public String coverImg;
        public String rPath = "";

        public UserPhotosList(String photoUrl) {
            this.photoUrl = photoUrl;
        }

    }

    public static int getBOY() {
        return BOY;
    }

    public static int getGILR() {
        return GILR;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getFwId() {
        return fwId;
    }

    public void setFwId(String fwId) {
        this.fwId = fwId;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public String getMotoringSwf() {
        return motoringSwf;
    }

    public void setMotoringSwf(String motoringSwf) {
        this.motoringSwf = motoringSwf;
    }

    public String getMotoringName() {
        return motoringName;
    }

    public void setMotoringName(String motoringName) {
        this.motoringName = motoringName;
    }

    public String getMotoringThumb() {
        return motoringThumb;
    }

    public void setMotoringThumb(String motoringThumb) {
        this.motoringThumb = motoringThumb;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setMyLiveLevel(int myLiveLevel) {
        this.myLiveLevel = myLiveLevel;
    }

    public boolean isMyIsCard() {
        return myIsCard;
    }

    public void setMyIsCard(boolean myIsCard) {
        this.myIsCard = myIsCard;
    }

    public boolean isMyIsLive() {
        return myIsLive;
    }

    public void setMyIsLive(boolean myIsLive) {
        this.myIsLive = myIsLive;
    }

    public boolean isMyIsQq() {
        return myIsQq;
    }

    public void setMyIsQq(boolean myIsQq) {
        this.myIsQq = myIsQq;
    }

    public boolean isMyIsWx() {
        return myIsWx;
    }

    public void setMyIsWx(boolean myIsWx) {
        this.myIsWx = myIsWx;
    }

    public int getMyVipLevel() {
        return myVipLevel;
    }

    public void setMyVipLevel(int myVipLevel) {
        this.myVipLevel = myVipLevel;
    }

    public long getMyLiveDuration() {
        return myLiveDuration;
    }

    public void setMyLiveDuration(long myLiveDuration) {
        this.myLiveDuration = myLiveDuration;
    }

    public double getReceive() {
        return receive;
    }

    public void setReceive(int receive) {
        this.receive = receive;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getFriendNum() {
        return friendNum;
    }

    public void setFriendNum(int friendNum) {
        this.friendNum = friendNum;
    }




    public int getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(int isAttention) {
        this.isAttention = isAttention;
    }

    public int getLikeVideoCount() {
        return likeVideoCount;
    }

    public void setLikeVideoCount(int likeVideoCount) {
        this.likeVideoCount = likeVideoCount;
    }

    public String getLiveLabel() {
        return liveLabel;
    }

    public void setLiveLabel(String liveLabel) {
        this.liveLabel = liveLabel;
    }

    public int getLiveLevel() {
        return liveLevel;
    }

    public void setLiveLevel(int liveLevel) {
        this.liveLevel = liveLevel;
    }

    public int getLiveLevelExperience() {
        return liveLevelExperience;
    }

    public void setLiveLevelExperience(int liveLevelExperience) {
        this.liveLevelExperience = liveLevelExperience;
    }

    public int getLiveLevelHighest() {
        return liveLevelHighest;
    }

    public void setLiveLevelHighest(int liveLevelHighest) {
        this.liveLevelHighest = liveLevelHighest;
    }

    public int getLiveLevelLowest() {
        return liveLevelLowest;
    }

    public void setLiveLevelLowest(int liveLevelLowest) {
        this.liveLevelLowest = liveLevelLowest;
    }

    public int getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(int movieCount) {
        this.movieCount = movieCount;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public long getSendGiftTotal() {
        return sendGiftTotal;
    }

    public void setSendGiftTotal(long sendGiftTotal) {
        this.sendGiftTotal = sendGiftTotal;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public static class TodayUserTime {


        /**
         *
         *
         *
         * todayTotalTime	integer($int32)
         * 今日印象值
         *
         * todayUserTimes	[
         * 今日用户印象值集合
         *
         * 用户印象值{
         * headImg	string
         * 用户头像
         *
         * orderTime	integer($int32)
         * 用户印象值
         *
         * userId	integer($int32)
         * 用户id
         *
         *
         *
         * */

        private String headImg;
        private long orderTime;
        private int userId;

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public long getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(long orderTime) {
            this.orderTime = orderTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }


}
