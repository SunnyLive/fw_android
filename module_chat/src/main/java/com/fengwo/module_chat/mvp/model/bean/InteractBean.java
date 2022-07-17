package com.fengwo.module_chat.mvp.model.bean;

/**
 * 互动通知 item 实体类
 */
public class InteractBean {
    /**
     * "age": 0,
     * "cardId": 0,
     * "cardLikeTime": "string",
     * "content": "string",
     * "cover": "string",
     * "headImg": "string",
     * "id": 0,
     * "msg": "string",
     * "nickname": "string",
     * "sex": 0,
     * "userId": 0
     */

    public String age;
    public long cardId;
    public String cardLikeTime;
    public String content;
    public String cover;
    public String headImg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    public String msg;
    public String nickname;
    public int sex;
    public int userId;
}
