package com.fengwo.module_login.mvp.dto;

public class UserDetailDto {
    public String account; // 账号
    public long attention; // 用户关注数
    public String birthday; // 生日
    public String constellation; // 星座
    public long fans; // 粉丝数
    public String fwId; // 蜂窝号
    public String headImg; // 头像
    public long id; // 用户ID
    public int isAttention; // 用户是否已关注
    public int isBlack; // 用户是否被拉黑
    public int isManage; // 用户是否是房管 0不是 1是，只有填写房间ID时才有可能为1
    public long likeVideoCount; // 用户点赞小视频数量
    public String liveLabel; // 用户主播开播标签
    public int liveLevel; // 用户主播等級
    public long liveLevelExperience; // 用户主播等级经验值
    public long liveLevelHighest; // 用户主播等级最高经验值
    public long liveLevelLowest; // 用户主播等级初始经验值
    public String location; // 所在地区
    public String mobile; // 手机号
    public long movieCount; // 用户短片数量
    public String nickname; // 昵称
    public int sex; // 性别 0：保密，1：男；2：女
    public String signature; // 个性签名
    public long videoCount; // 用户小视频数量
    public int vipLevel; // 用户vip等级
}
