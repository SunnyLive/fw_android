package com.fengwo.module_chat.mvp.model.bean;

import androidx.annotation.Nullable;

import com.fengwo.module_comment.utils.L;

import java.util.List;

public class EnterGroupModel {
    public int forbidden; // 禁言:0：群全员禁言 1：正常
    public String groupName;
    public String groupHeadImg;
    public int isFirstJoin; // 是否首次进入：0不是，1是
    public int isGroupMaster; // 是否是群主
    public List<GroupInfoModel> resourceInfo;

    public static class GroupInfoModel {
        public String id;
        public String cover;
        public String merchantName;
        public String merchantUrl;

        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            GroupInfoModel other = (GroupInfoModel) obj;
            if (id.equals(other.id)) {
                return true;
            } else {
                return false;
            }
        }

    }
}
