package com.fengwo.module_chat.mvp.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class GroupMemberModel implements MultiItemEntity {
    public String headImg;
    public String nickname;
    public int userId;

    // 只有在创建私群时IM返回字段
    public String uid;

    // 自定义字段
    public int type = 0;
    public boolean isSelected = false;

    @Override
    public int getItemType() {
        return type;
    }
}
