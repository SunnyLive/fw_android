package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.GiftWallDto;
import com.fengwo.module_login.mvp.ui.iview.IGiftWallView;
import com.fengwo.module_login.mvp.ui.iview.IMineInfoView;
import com.fengwo.module_login.utils.UserManager;

import okhttp3.RequestBody;

public class GiftWallPresenter extends BaseLoginPresenter<IGiftWallView> {

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


    /**
     * 获取达人礼物
     *
     * @param pageIndex
     */
    public void getExpertGiftWall(int pageIndex, int userId) {

        LoginApiService wenboApi = new RetrofitUtils().createWenboApi(LoginApiService.class);
        RequestBody params = new com.fengwo.module_comment.base.WenboParamsBuilder()
                .put("current", pageIndex + "")
                .put("userId", userId + "")
                .put("size", "20")
                .build();
        addNet(wenboApi.getExpertGiftWall(params).compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult<GiftWallDto>>() {
                    @Override
                    public void _onNext(HttpResult<GiftWallDto> data) {
                        if (data.isSuccess()) {
                            getView().getGiftWall(data.data);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }


}
