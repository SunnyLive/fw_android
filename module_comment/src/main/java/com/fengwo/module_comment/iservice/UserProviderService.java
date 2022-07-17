package com.fengwo.module_comment.iservice;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.fengwo.module_comment.base.LoadingObserver;

import io.reactivex.functions.Consumer;

public interface UserProviderService extends IProvider {

    void setToken(String token);

    String getToken();

    UserInfo getUserInfo();

    void updateUserInfo(LoadingObserver lo);

    void setUsetInfo(UserInfo userInfo);

    void updateWallet(Consumer<Long> consumer );

    void setClosureTime(long time);

    Long getClosureTime();
}
