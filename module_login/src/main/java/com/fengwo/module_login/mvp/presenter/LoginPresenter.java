package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.SignUtils;
import com.fengwo.module_login.mvp.dto.LoginDto;
import com.fengwo.module_login.mvp.ui.iview.ILoginView;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.FWWebSocket1;

public class LoginPresenter extends BaseLoginPresenter<ILoginView> {


    public LoginPresenter() {
        super();
    }

    public void getAutoLogin(String token, String appChannel) {
        service.autoLogin(new ParamsBuilder().put("appChannel", appChannel)
                .put("token", token).build())
                .compose(handleResult())
                .subscribe(new LoadingObserver<LoginDto>() {
                    @Override
                    public void _onNext(LoginDto data) {
                        if (data.writeOffStatus == 1 || data.writeOffStatus == 2) {
                            getView().hideLoadingDialog();
                            getView().toAccDestroyCancel(data);
                        } else {
                            UserManager.getInstance().setToken(data.token);
                            UserManager.getInstance().updateUser(new LoadingObserver<UserInfo>() {
                                @Override
                                public void _onNext(UserInfo userInfo) {
                                    getView().loginSuccess(userInfo,data.isEditInfo);
                                    FWWebSocket1.getInstance().init(userInfo.id,data.token);
                                   // FWWebSocket1.getInstance().sendLoginChatMessage(userInfo.id);
                                    FWWebSocket1.getInstance().sendLoginChatMessage(userInfo.id);
                                }

                                @Override
                                public void _onError(String msg) {
                                    getView().hideLoadingDialog();
                                    getView().toastTip(msg);
                                }
                            });
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().hideLoadingDialog();
                        getView().toastTip(msg);
                    }
                });
    }

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

    public void getBindPhoneCode(String mobile) {
        String nonce = SignUtils.getNonce();
        long timestamp = System.currentTimeMillis();
        String sign = SignUtils.sign(nonce, mobile, timestamp);
        service.getCode(nonce, sign, new ParamsBuilder().put("mobile", mobile)
                .put("timestamp", timestamp + "")
                .put("type", "mobile").build())
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

    /**
     * 微信登录调用的接口
     */
    public void thirdLogin(String headImg, String name, String openId, String gender, int type, String channel, String unionid) {
        int sex = "男".equals(gender) ? 1 : 2;
        service.thirdLogin(new ParamsBuilder()
                .put("headImg", headImg)
                .put("name", name)
                .put("openid", openId)
                .put("sex", sex + "")
                .put("type", type + "")
                .put("appChannel", channel)
                .put("unionid", unionid)
                .build())
                .compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<LoginDto>() {
                    @Override
                    public void _onNext(LoginDto data) {
                        if (data.writeOffStatus==1||data.writeOffStatus == 2){
                            getView().hideLoadingDialog();
                            getView().toAccDestroyCancel(data);
                        }else if (!data.register) {
                            UserManager.getInstance().setToken(data.token);
                            UserManager.getInstance().updateUser(new LoadingObserver<UserInfo>() {
                                @Override
                                public void _onNext(UserInfo userInfo) {
                                    getView().loginSuccess(userInfo,data.isEditInfo);
                                    FWWebSocket1.getInstance().init(userInfo.id,data.token);
                                    FWWebSocket1.getInstance().sendLoginChatMessage(userInfo.id);
                                }

                                @Override
                                public void _onError(String msg) {

                                }
                            });
                        } else {
                            getView().toBindPhone(data.token);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    /**
     * 手机登录调用的接口
     */
    public void loginByCode(String phone, String code, String channel) {
        L.e("fengwohuyu", "loginchannel:-----" + channel);
        service.login(new ParamsBuilder()
                .put("code", code)
                .put("mobile", phone)
                .put("appChannel", channel)
                .build())
                .compose(handleResult())
                .subscribe(new LoadingObserver<LoginDto>() {
                    @Override
                    public void _onNext(LoginDto data) {
                        if (data.writeOffStatus==1||data.writeOffStatus == 2){
                            getView().hideLoadingDialog();
                            getView().toAccDestroyCancel(data);
                            UserManager.getInstance().setToken("");
                        }else {
                            UserManager.getInstance().saveLastLoginPhone(phone);
                            UserManager.getInstance().setToken(data.token);
                            UserManager.getInstance().updateUser(new LoadingObserver<UserInfo>() {
                                @Override
                                public void _onNext(UserInfo userInfo) {
                                    getView().loginSuccess(userInfo,data.isEditInfo);
                                    FWWebSocket1.getInstance().init(userInfo.id,data.token);
                                    FWWebSocket1.getInstance().sendLoginChatMessage(userInfo.id);
                                }

                                @Override
                                public void _onError(String msg) {
                                    getView().hideLoadingDialog();
                                    getView().toastTip(msg);
                                }
                            });
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().hideLoadingDialog();
                        getView().toastTip(msg);
                    }
                });
    }

    public void bindPhone(String mobile, String code, String token, String channel) {
        service.bindMobile(new ParamsBuilder()
                .put("code", code)
                .put("token", token)
                .put("mobile", mobile)
                .put("appChannel", channel)
                .build())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<LoginDto>>() {
                    @Override
                    public void _onNext(HttpResult<LoginDto> data) {
                        if (data.isSuccess()) {
                            if (data.data.writeOffStatus==1||data.data.writeOffStatus == 2){
                                getView().hideLoadingDialog();
                                getView().toAccDestroyCancel(data.data);
                            }else {
                                UserManager.getInstance().setToken(data.data.token);
                                UserManager.getInstance().updateUser(new LoadingObserver<UserInfo>() {
                                    @Override
                                    public void _onNext(UserInfo userInfo) {
                                        getView().loginSuccess(userInfo,data.data.isEditInfo);
                                        FWWebSocket1.getInstance().init(userInfo.id,token);
                                        FWWebSocket1.getInstance().sendLoginChatMessage(userInfo.id);
                                    }

                                    @Override
                                    public void _onError(String msg) {
                                        getView().hideLoadingDialog();
                                        getView().toastTip(msg);
                                    }
                                });
                            }
                        } else {
                            getView().toastTip(data.description);
                        }
                        getView().hideLoadingDialog();
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().hideLoadingDialog();
                    }
                });

    }
}
