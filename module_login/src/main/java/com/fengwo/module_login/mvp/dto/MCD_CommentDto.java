package com.fengwo.module_login.mvp.dto;

import java.util.ArrayList;
import java.util.List;

public class MCD_CommentDto {
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

    public boolean isExpand = false;
    public List<MCD_CommentDto> seconds = new ArrayList<>();

    public boolean hasMore() {
        return replys > seconds.size();
    }
}
