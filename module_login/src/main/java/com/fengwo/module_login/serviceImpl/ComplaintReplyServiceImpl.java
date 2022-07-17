package com.fengwo.module_login.serviceImpl;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.ComplaintReplyService;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_login.api.LoginApiService;

import okhttp3.RequestBody;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/8
 */
@Route(path = ArouterApi.COMPLAINT_REPLY_SERVICE, name = "投诉回复服务")
public class ComplaintReplyServiceImpl implements ComplaintReplyService {

    @Override
    public void complaintReply(RequestBody body, LoadingObserver<HttpResult> observer) {
        new RetrofitUtils().createApi(LoginApiService.class)
                .complaintReply(body)
                .compose(RxUtils.applySchedulers())
                .subscribe(observer);
    }

    @Override
    public void init(Context context) {

    }
}
