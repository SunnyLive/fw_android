package com.fengwo.module_chat.mvp.model.bean;

import java.io.Serializable;

public class RecommendCircleBean implements Serializable {

    private static final long serialVersionUID = -1802311659614625256L;

    public String createTime;
    public String groupMasterNickname;
    public String groupMasterUid;
    public int id;
    public String introduce;
    public String name;
    public String nickname;
    public int status;
    public String thumb;
    public int times;
    public String updateTime;
    public String userId;
    public int isLike;

    // 自定义属性
    public boolean selected = false;
}
