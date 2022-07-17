package com.fengwo.module_vedio.mvp.dto;

import java.io.Serializable;
import java.util.List;

public class SmallCommentDto {
    public int current;
    public int pages;
    //    public Order orders;
    public List<Comment> records;
    public boolean searchCount;
    public int size;
    public int total;

    public class Comment implements Serializable {
        public String userHead;
        public int userId;
        public String userName;
        public int commentId;
        public String content;
        public String createTime;
        public int id;
        public boolean isFavorite;
        public boolean isLike;
        public int likes;
        public int replyUserId;
        public String replyUserName;
        public int secondNum;
        public int status;
        public int type;
        public String updateTime;
        public int videoId;
        public List<Comment> commentDTOS;
    }

    public class Order {
        public boolean asc;
        public String column;
    }
}
