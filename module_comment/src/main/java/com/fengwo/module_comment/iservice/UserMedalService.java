package com.fengwo.module_comment.iservice;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.UserMedalBean;

import java.security.Provider;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/6/11
 */
public interface UserMedalService extends IProvider {

    void getUserMedal(int uid,int channelId, LoadingObserver<HttpResult<UserMedalBean>> loadingObserver);
}
