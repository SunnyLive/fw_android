package com.fengwo.module_login.serviceImpl;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_login.mvp.dto.WalletDto;
import com.fengwo.module_login.utils.UserManager;

import java.math.BigDecimal;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

@Route(path = ArouterApi.USERINFO_SERVICE, name = "获取用户信息服务")
public class UserProviderServiceImpl implements UserProviderService {

    private Context mContext;


    @Override
    public void init(Context context) {
        mContext = context;
        UserManager.init(mContext);
    }

    @Override
    public void setToken(String token) {
        UserManager.getInstance().setToken(token);
    }

    @Override
    public String getToken() {
        return UserManager.getInstance().getToken();
    }

    @Override
    public UserInfo getUserInfo() {
        //  KLog.e("tag222" , "当前经验值"+UserManager.getInstance().getUser().getExperience());
        return UserManager.getInstance().getUser();
    }

    @Override
    public void updateUserInfo(LoadingObserver lo) {
        UserManager.getInstance().updateUser(new LoadingObserver<UserInfo>() {
            @Override
            public void _onNext(UserInfo data) {
                lo._onNext(data);
            }

            @Override
            public void _onError(String msg) {
                lo._onError(msg);
            }
        });
    }

    @Override
    public void setUsetInfo(UserInfo userInfo) {

        if (new BigDecimal(UserManager.getInstance().getUser().getExperience()).compareTo(new BigDecimal(userInfo.getExperience())) <= 0) {
            //  KLog.e("tag222" , "封禁时间"+userInfo.getForbiddenWords());
            UserManager.getInstance().setUserInfo(userInfo);
            //  KLog.e("tag222" , "封禁时间--------------"+userInfo.getForbiddenWords());
        }

    }

    @Override
    public void updateWallet(Consumer<Long> consumer) {
        UserManager.getInstance().updateUserWallet().compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult<WalletDto>>() {
                    @Override
                    public void _onNext(HttpResult<WalletDto> data) {
                        try {
                            UserManager.getInstance().getUser().balance = data.data.balance + data.data.preBalance;

                            consumer.accept(data.data.balance + data.data.preBalance);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        Log.e("onError", msg);
                    }
                });
    }

    @Override
    public void setClosureTime(long time) {
        UserManager.getInstance().setClosureTime(time);
    }

    public Long getClosureTime() {
        return UserManager.getInstance().getClosureTime();
    }
}
