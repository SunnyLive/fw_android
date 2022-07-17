package com.fengwo.module_chat.mvp.ui.contract;

import com.fengwo.module_chat.mvp.model.bean.GroupMemberModel;
import com.fengwo.module_comment.base.MvpView;

import java.util.List;

public interface IChatGroupMemberView extends MvpView {

    void setGroupMember(List<GroupMemberModel> records);

    void createGroupSuccess(String groupId,String groupName);

    void deleteGroupMemberSuccess();
}
