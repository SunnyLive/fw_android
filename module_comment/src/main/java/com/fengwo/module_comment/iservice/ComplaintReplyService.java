package com.fengwo.module_comment.iservice;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;

import okhttp3.RequestBody;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/8
 */
public interface ComplaintReplyService extends IProvider {

    void complaintReply(RequestBody body, LoadingObserver<HttpResult> observer);
}
