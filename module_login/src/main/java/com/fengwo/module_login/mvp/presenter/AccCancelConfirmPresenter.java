package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.SignUtils;
import com.fengwo.module_login.mvp.dto.LoginDto;
import com.fengwo.module_login.mvp.ui.iview.IAccCancelConfirmView;
import com.fengwo.module_login.mvp.ui.iview.ILoginView;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.FWWebSocket1;

import java.util.Map;

public class AccCancelConfirmPresenter extends BaseLoginPresenter<IAccCancelConfirmView> {

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
                            getView().setCode("");
                        } else {
                            getView().toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    public void accDestroy(String code,String mobile,String userId,String writeOffReason){

        service.accDestroy(new ParamsBuilder().put("code",code)
                        .put("mobile",mobile)
        .put("userId",userId)
        .put("writeOffReason",writeOffReason).build())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().destroy();
                        }else {
                            getView().toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                });

    }

    public void accDestroyCancel(String code,String mobile,String userId,String writeOffReason){

        service.accDestroyCancel(new ParamsBuilder().put("code",code)
                .put("mobile",mobile)
                .put("userId",userId)
                .put("writeOffReason",writeOffReason).build())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().cancelDestroy();
                        }else {
                            getView().toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                });

    }

    public void getMobile(int userId){
        service.getMobile(userId)
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().setMobile(data.data.toString());
                        }else {
                            getView().toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                });

    }


}
