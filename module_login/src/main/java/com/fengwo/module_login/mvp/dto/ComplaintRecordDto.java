package com.fengwo.module_login.mvp.dto;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/7
 */
public class ComplaintRecordDto {


    /**
     * id : 172
     * complaintId : 138
     * complaintReplyId : 0
     * userId : 512084
     * type : 0
     * content : 测试
     * createTime : 2020-04-07 14:31:47
     * nickname : 刚你阿尔
     * headImg : null
     * time : 1586241107000
     */

    private int id;
    private int complaintId;
    private int complaintReplyId;
    private int userId;
    private int type;
    private String replyContent;
    private String content;
    private String createTime;
    private String nickname;
    private String replyNickname;
    private String headImg;
    private long time;


    public String getReplyNickname() {
        return replyNickname;
    }

    public void setReplyNickname(String replyNickname) {
        this.replyNickname = replyNickname;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public int getComplaintReplyId() {
        return complaintReplyId;
    }

    public void setComplaintReplyId(int complaintReplyId) {
        this.complaintReplyId = complaintReplyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
