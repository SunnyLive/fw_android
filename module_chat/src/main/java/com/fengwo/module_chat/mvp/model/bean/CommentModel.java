package com.fengwo.module_chat.mvp.model.bean;

import java.util.ArrayList;
import java.util.List;

public class CommentModel {
    public int commentId;
    public String content;
    public long createTime;
    public String headImg;
    public int isLike;
    public int likes;
    public String nickname;
    public int replys;
    public String secondNickname;
    public int secondType;
    public int secondUserId;
    public int userId;
    public int status;//1：正常   2：涉嫌违规

    public boolean isExpand = false;
    public List<CommentModel> seconds = new ArrayList<>();

    public boolean hasMore() {
        return replys > seconds.size();
    }
}
