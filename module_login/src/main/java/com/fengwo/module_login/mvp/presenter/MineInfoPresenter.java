package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.GuardListDto;
import com.fengwo.module_live_vedio.mvp.dto.LiveProfitDto;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.GiftWallDto;
import com.fengwo.module_login.mvp.ui.iview.IMineInfoView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MineInfoPresenter extends BaseLoginPresenter<IMineInfoView> {

    public void getGiftWall(String pageParams, int userId) {
        addNet(
                service.getGiftWall(pageParams, userId)
                        .compose(io_main())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<GiftWallDto>() {
                            @Override
                            public void _onNext(GiftWallDto data) {
                                getView().getGiftWall(data);
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        }));
    }

    public void getGuardList(int uid) {
        addNet(
                service.getGuardList("1,50", uid)
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<GuardListDto>() {
                            @Override
                            public void _onNext(GuardListDto data) {
                                if (null == getView()) return;
                                getView().setGuardWindow(data.total, data.records);
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        })
        );
    }

    public void getUserInfo(boolean isHost , int userId) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        int id = userId << 3;
        Map map = new HashMap();
        map.put("userId", id);
        compositeDisposable.add(new RetrofitUtils().createApi(LiveApiService.class)
                .getUserinfoByIdNew(createRequestBody(map))
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<UserInfo>>() {
                    @Override
                    public void _onNext(HttpResult<UserInfo> data) {
                        if (data.isSuccess()) {
                            if (getView() == null) return;
                            getView().showShouhu(isHost,userId,data.data);

                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                }));

    }

    public void getReceiveList(String pageParams, int userId){
        new RetrofitUtils().createApi(LoginApiService.class).getReceiveList(pageParams, 4, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingObserver<HttpResult<LiveProfitDto>>() {
                    @Override
                    public void _onNext(HttpResult<LiveProfitDto> data) {
                        if (data.isSuccess()) {
                            if (null == getView()) return;
                            getView().setTodayReceive(data.data.getTotal(), data.data.getRecords());
                        }
                    }
                    @Override
                    public void _onError(String msg) {
                    }
                });
    }




}
