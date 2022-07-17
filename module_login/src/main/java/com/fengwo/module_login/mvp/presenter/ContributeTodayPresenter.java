package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.SignUtils;
import com.fengwo.module_login.mvp.ui.iview.IContributeTodayView;

public class ContributeTodayPresenter extends BaseLoginPresenter<IContributeTodayView> {

    public void getLoginCode(String mobile) {
        String nonce = SignUtils.getNonce();
        long timestamp = System.currentTimeMillis();
        String sign = SignUtils.sign(nonce, mobile, timestamp);
        service.getCode(nonce, sign, new ParamsBuilder().put("mobile", mobile)
                .put("timestamp", timestamp + "")
                .put("type", "login").build())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>(getView()) {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().toastTip("短信发送成功");
                        } else {
                            getView().toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

}
