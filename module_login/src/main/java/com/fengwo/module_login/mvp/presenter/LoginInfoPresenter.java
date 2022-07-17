package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_login.mvp.ui.iview.ILoginInfView;

public class LoginInfoPresenter extends BaseLoginPresenter<ILoginInfView> {

    public void saveUserInfo(String headerUrl, String nickName) {
        service.saveUserInfo(new ParamsBuilder()
                        .put("headImg",headerUrl)
                        .put("nickname",nickName).build())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>(getView()) {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess())
                            getView().success();
                         else
                             getView().fail(data.description);

                    }

                    @Override
                    public void _onError(String msg) {
                        getView().error(msg);
                    }
                });
    }


    public void skipUserInfo() {

        service.skipUserInfo()
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>(getView()) {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess())
                            getView().success();
                        else getView().fail(data.description);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().error(msg);
                    }
                });


    }

}
