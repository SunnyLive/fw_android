package com.fengwo.module_chat.mvp.model.bean;

import java.util.List;

public class GroupInfoModel {
    public int adminId;
    public String content;
    public int disturb; // 免打扰状态0：消息免打扰 1：正常
    public int forbidden; // 群禁言0：群全员禁言 1：正常
    public String groupName;
    public int id;
    public int userId; // 群主ID
    public int usersNum; // 群人数
    public List<String> tags; // 标签
}
