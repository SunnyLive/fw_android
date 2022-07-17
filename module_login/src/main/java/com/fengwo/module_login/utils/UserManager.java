package com.fengwo.module_login.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.SPUtils;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.AttentionNumberDto;
import com.fengwo.module_login.mvp.dto.FansNumberDto;
import com.fengwo.module_login.mvp.dto.FriendsNumberDto;
import com.fengwo.module_login.mvp.dto.IsRealNameDto;
import com.fengwo.module_login.mvp.dto.MyUsingCarDto;
import com.fengwo.module_login.mvp.dto.UserCenterDto;
import com.fengwo.module_login.mvp.dto.WalletDto;
import com.google.gson.Gson;

import org.reactivestreams.Publisher;

import java.math.BigDecimal;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function7;

public class UserManager {

    private final static String TOKEN_KEY = "token";
    private final static String USER_INFO_KEY = "userinfo";
    private final static String LAST_LOGIN_PHONE = "lastloginphone";
    private final static String CLOSURE_TIMEl = "closure_time";

    private static UserManager manager;
    private Context mContext;
    private String token;
    private String userInfo;
    private Gson gson;
    private Long closureTime = Long.valueOf(0);


    private UserManager(Context c) {
        mContext = c;
        gson = new Gson();
    }

    public static void init(Context c) {
        if (null == manager)
            manager = new UserManager(c);

    }

    public static UserManager getInstance() throws RuntimeException {
        if (null == manager) {
            init(BaseApplication.mApp);
        }
        return manager;

    }

    public void setToken(String token) {
        this.token = token;
        SPUtils1.put(mContext, TOKEN_KEY, token);
    }




    public void setWenboCer(boolean isCer) {
        SPUtils1.put(mContext, String.valueOf(manager.getUser().getId()), isCer);
    }

    public boolean getWenboCer() {
        boolean cer = (boolean) SPUtils1.get(mContext, String.valueOf(manager.getUser().getId()), false);
        return cer;
    }

    public String getToken() throws RuntimeException {
        if (TextUtils.isEmpty(token)) {
            token = (String) SPUtils1.get(mContext, TOKEN_KEY, "");
        }
        return token;
    }

    public UserInfo getUser() {
        if (TextUtils.isEmpty(userInfo)) {
            userInfo = (String) SPUtils1.get(mContext, USER_INFO_KEY, "");
        }
        UserInfo user = gson.fromJson(userInfo, UserInfo.class);
        return user;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = new Gson().toJson(userInfo);
        SPUtils1.put(mContext, USER_INFO_KEY, this.userInfo);
    }

    public void clearUser() {
        this.userInfo = "";
        SPUtils1.remove(mContext, USER_INFO_KEY);
    }

    public void clear() {
        SPUtils1.clear(mContext);
    }



    public void setClosureTime(long closureTime) {
        this.closureTime = closureTime;
        SPUtils1.put(mContext, CLOSURE_TIMEl, closureTime+"");
    }

    public Long getClosureTime() throws ClassCastException {
        closureTime =new BigDecimal((String)SPUtils1.get(mContext, CLOSURE_TIMEl, "0")).longValue() ;
        return closureTime;
    }

    public Flowable<HttpResult<WalletDto>> updateUserWallet() {
        LoginApiService service = new RetrofitUtils().createApi(LoginApiService.class);
        return service.getWalletInfo();
    }

    @SuppressLint("CheckResult")
    public void updateUser(LoadingObserver<UserInfo> observer) {
        LoginApiService service = new RetrofitUtils().createApi(LoginApiService.class);
        service.getUserCenter()
                .compose(RxUtils.applySchedulers())
                .subscribe(userInfoHttpResult -> {
                    try {
                        if (userInfoHttpResult.isSuccess()) {
                            setUserInfo(userInfoHttpResult.data);
                            if (observer != null) {
                                observer._onNext(userInfoHttpResult.data);
                            }
                        } else {
                            if (observer != null) {
                                observer._onError(userInfoHttpResult.description);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
//                .subscribe(new Observer<HttpResult<UserInfo>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(HttpResult<UserInfo> userInfoHttpResult) {
//                        try {
//                            if (userInfoHttpResult.isSuccess()) {
//                                observer._onNext(userInfoHttpResult.data);
//                                setUserInfo(userInfoHttpResult.data);
//                            } else {
//                                observer._onError(userInfoHttpResult.description);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        observer._onError(e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

    public void saveLastLoginPhone(String phone) {
        SPUtils1.put(mContext, LAST_LOGIN_PHONE, phone);
    }

    public String getLastLoginnPhone() {
        return (String) SPUtils1.get(mContext, LAST_LOGIN_PHONE, "");
    }

//    public void updateUser(LoadingObserver<UserInfo> observer) {
//        LoginApiService service = new RetrofitUtils().createApi(LoginApiService.class);
//        Observable<HttpResult<UserInfo>> user = service.getUserInfo();
//        Observable<HttpResult<FansNumberDto>> fans = service.getUserFansNumber();
//        Observable<HttpResult<AttentionNumberDto>> attention = service.getUserAttentionNumber();
//        Observable<HttpResult<FriendsNumberDto>> friend = service.getUserFriendNumber();
//        Observable<HttpResult<WalletDto>> wallet = service.getWalletInfo();
//        Observable<HttpResult<IsRealNameDto>> realName = service.getIsRealName();
//        Observable<HttpResult<MyUsingCarDto>> mUsingCar = service.getMyUsingCar();
//        service.getUserCenter()
//                .compose(RxUtils.applySchedulers())
//                .subscribe(new LoadingObserver<HttpResult<UserCenterDto>>() {
//                    @Override
//                    public void _onNext(HttpResult<UserCenterDto> data) {
//
//                    }
//
//                    @Override
//                    public void _onError(String msg) {
//
//                    }
//                });
//
//        Observable.zip(user, fans, attention, friend, wallet, realName, mUsingCar, new Function7<HttpResult<UserInfo>, HttpResult<FansNumberDto>, HttpResult<AttentionNumberDto>, HttpResult<FriendsNumberDto>, HttpResult<WalletDto>, HttpResult<IsRealNameDto>, HttpResult<MyUsingCarDto>, UserInfo>() {
//            @Override
//            public UserInfo apply(HttpResult<UserInfo> userInfoHttpResult, HttpResult<FansNumberDto> fansNumberDtoHttpResult, HttpResult<AttentionNumberDto> attentionNumberDtoHttpResult, HttpResult<FriendsNumberDto> friendsNumberDtoHttpResult, HttpResult<WalletDto> walletDtoHttpResult, HttpResult<IsRealNameDto> isRealNameDtoHttpResult, HttpResult<MyUsingCarDto> myUsingCarDtoHttpResult) throws Exception {
//                UserInfo userInfo;
//                userInfo = userInfoHttpResult.data;
//                userInfo.fans = fansNumberDtoHttpResult.data.fans;
//                userInfo.attention = attentionNumberDtoHttpResult.data.attention;
//                userInfo.receive = walletDtoHttpResult.data.receive;
//                userInfo.balance = walletDtoHttpResult.data.balance + walletDtoHttpResult.data.preBalance;
//                userInfo.profit = walletDtoHttpResult.data.profit;
//                userInfo.myIsCard = isRealNameDtoHttpResult.data.isIdCard>0;
//                userInfo.friendNum = friendsNumberDtoHttpResult.data.friendNum;
//                if (myUsingCarDtoHttpResult.isSuccess()) {
//                    userInfo.motoringSwf = myUsingCarDtoHttpResult.data.motoringSwf;
//                    userInfo.motoringName = myUsingCarDtoHttpResult.data.motoringName;
//                    userInfo.frameRate = myUsingCarDtoHttpResult.data.frameRate;
//                }
//                setUserInfo(userInfo);
//                return userInfo;
//            }
//        }).compose(RxUtils.applySchedulers())
//                .subscribe(new Observer<UserInfo>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(UserInfo userInfo) {
//                        L.e(user.toString());
//                        try {
//                            observer._onNext(userInfo);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        L.e(e.getMessage());
//                        observer._onError(e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }
}
