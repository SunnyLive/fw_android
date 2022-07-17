package com.fengwo.module_chat.mvp.ui.contract;

import com.fengwo.module_chat.mvp.model.bean.GroupInfoModel;
import com.fengwo.module_chat.mvp.model.bean.GroupMemberModel;
import com.fengwo.module_comment.base.MvpView;

import java.util.ArrayList;

public interface IChatInfoView extends MvpView {

    void setGroupInfo(GroupInfoModel data);

    void forbiddenSuccess();

    void unforbiddenSuccess();

    void getGroupMember(String memberCount, ArrayList<GroupMemberModel> records);

    void quitGroupSuccess();

    void disturbGroupSuccess();

    void disturbGroupFailed(String msg,int status);
}
