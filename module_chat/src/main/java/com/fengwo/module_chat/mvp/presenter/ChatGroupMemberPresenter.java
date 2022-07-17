package com.fengwo.module_chat.mvp.presenter;

import com.fengwo.module_chat.mvp.model.bean.GroupMemberModel;
import com.fengwo.module_chat.mvp.ui.contract.IChatGroupMemberView;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;

public class ChatGroupMemberPresenter extends BaseChatPresenter<IChatGroupMemberView> {

    public void getMemberList(String groupId, String nickName, String pageParam) {
        addNet(service.getGroupMemberList(groupId, nickName, pageParam).compose(io_main()).compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<GroupMemberModel>>() {
                    @Override
                    public void _onNext(BaseListDto<GroupMemberModel> data) {
                        ArrayList<GroupMemberModel> records = data.records;
                        getView().setGroupMember(records);
                        getView().hideLoadingDialog();
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                        getView().hideLoadingDialog();
                    }
                }));
    }

    public void createGroup(String groupName, int oldGroupId, String memberIds) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("groupName", groupName);
        map.put("oldGroupId", oldGroupId);
        map.put("userIds", memberIds);
        RequestBody body = createRequestBody(map);
        addNet(
                service.createGroup(body).compose(io_main()).compose(handleResult()).subscribeWith(new LoadingObserver<String>(getView()) {
                    @Override
                    public void _onNext(String data) {
                        getView().createGroupSuccess(data, groupName);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));
    }

    public void deleteGroupMember(String groupId, String memberIds) {
        addNet(
                service.deleteGroupMember(groupId, memberIds).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().deleteGroupMemberSuccess();
                        } else {
                            getView().toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));
    }
}
