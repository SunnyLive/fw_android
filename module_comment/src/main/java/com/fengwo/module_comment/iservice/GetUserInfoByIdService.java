package com.fengwo.module_comment.iservice;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.fengwo.module_comment.base.BaseEachAttention;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;

public interface GetUserInfoByIdService extends IProvider {
    void getUserInfoById(String uid, LoadingObserver<HttpResult<UserInfo>> loadingObserver);

    void getEachAttention(String uid, LoadingObserver<HttpResult<BaseEachAttention>> loadingObserver);
}
