package com.fengwo.module_chat.mvp.presenter;

import com.fengwo.module_chat.mvp.model.bean.GroupInfoModel;
import com.fengwo.module_chat.mvp.model.bean.GroupMemberModel;
import com.fengwo.module_chat.mvp.ui.contract.IChatInfoView;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupInfoPresenter extends BaseChatPresenter<IChatInfoView> {

    public void getGroupInfo(String groupId) {
        addNet(service.getGroupInfo(groupId).compose(io_main()).compose(handleResult())
                .subscribeWith(new LoadingObserver<GroupInfoModel>() {
                    @Override
                    public void _onNext(GroupInfoModel data) {
                        getView().setGroupInfo(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                        getView().hideLoadingDialog();
                    }
                }));
    }

    public void forbiddenGroupTalk(String groupId) {
        addNet(
                service.forbiddenGroupTalk(groupId).compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (data.isSuccess()) {
                                    getView().forbiddenSuccess();
                                } else getView().toastTip(data.description);
                            }

                            @Override
                            public void _onError(String msg) {
                                getView().toastTip(msg);
                            }
                        }));
    }

    public void unforbiddenGroupTalk(String groupId) {
        addNet(
                service.unforbiddenGroupTalk(groupId).compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (data.isSuccess()) {
                                    getView().unforbiddenSuccess();
                                } else getView().toastTip(data.description);
                            }

                            @Override
                            public void _onError(String msg) {
                                getView().toastTip(msg);
                            }
                        }));
    }

    public void getGroupMemberList(String groupId, String nickName, String pageParam) {
        addNet(
                service.getGroupMemberList(groupId, nickName, pageParam).compose(io_main()).compose(handleResult())
                        .subscribeWith(new LoadingObserver<BaseListDto<GroupMemberModel>>() {
                            @Override
                            public void _onNext(BaseListDto<GroupMemberModel> data) {
                                ArrayList<GroupMemberModel> records = data.records;
                                getView().getGroupMember(data.total, records);
                            }

                            @Override
                            public void _onError(String msg) {
                                getView().toastTip(msg);
                                getView().hideLoadingDialog();
                            }
                        }));
    }

    public void quitGroup(String groupId) {
        addNet(
                service.quitGroup(groupId).compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (data.isSuccess()) {
                                    getView().quitGroupSuccess();
                                } else getView().toastTip(data.description);
                            }

                            @Override
                            public void _onError(String msg) {
                                getView().toastTip(msg);
                            }
                        }));
    }

    public void groupDisturb(int groupId, int status) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("groupId", groupId);
        map.put("state", status);
        addNet(
                service.groupDisturb(createRequestBody(map)).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) getView().disturbGroupSuccess();
                        else getView().disturbGroupFailed(data.description, status);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().disturbGroupFailed(msg, status);
                    }
                }));
    }
}