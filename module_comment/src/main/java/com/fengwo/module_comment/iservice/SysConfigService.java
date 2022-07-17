package com.fengwo.module_comment.iservice;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;

import okhttp3.RequestBody;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/7/2
 */
public interface SysConfigService extends IProvider {

    void getSysConfig(String key, LoadingObserver<HttpResult> observer);
}
