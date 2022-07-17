package com.fengwo.module_vedio.mvp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zachary
 * @date 2019/12/30
 */
public class ShortVideoCommentDto {
    public String content;
    public long createTimestamp;
    public int id;
    public boolean isLike = false;
    public int likes;
    public int movieId;
    public String nickname;
    public int parentId;
    public int replys;
    public String secondNickname;
    public int secondType;
    public int secondUserId;
    public int status;
    public int type;
    public int userId;
    public String userUrl;
    public String videoId;

    public boolean isExpand = false;
    public List<ShortVideoCommentDto> seconds = new ArrayList<>();

    public boolean hasMore() {
        return replys > seconds.size();
    }
}
