package com.fengwo.module_comment.iservice;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.BackpackDto;

public interface IBackpackService extends IProvider {

    void getBackpack(LoadingObserver<HttpResult<BackpackDto>> l);

}
