package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.MineCardDto;
import com.fengwo.module_login.mvp.dto.ShareUrlDto;
import com.fengwo.module_login.mvp.ui.iview.IMineCardView;

public class MineCardPresenter extends BasePresenter<IMineCardView> {

    public LoginApiService service;

    public MineCardPresenter() {
        service = new RetrofitUtils().createApi(LoginApiService.class);
    }

    /**
     * 获取动态信息列表数据
     *
     * @param pageIndex 当前页
     * @param userId    用户id
     */
    public void getMineCardData(int pageIndex, int userId) {
        String p = pageIndex + ",10";
        service.getMineCardList(p, userId + "").
                compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<MineCardDto>>() {
                    @Override
                    public void _onNext(HttpResult<MineCardDto> data) {
                        if (data.isSuccess()) {
                            getView().success(data.data);
                        } else {
                            getView().fail(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().error(msg);
                    }
                });
    }


    /**
     * 分享
     *
     * @param cardId 动态id
     */
    public void requestCardShare(int cardId) {
        service.getShareInfo(cardId).compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<ShareUrlDto>>() {
                    @Override
                    public void _onNext(HttpResult<ShareUrlDto> data) {
                        if (data.isSuccess()) {
                            getView().resultShareInfo(cardId,data.data);
                        } else {
                            getView().failShareInfo(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().errorShareInfo(msg);
                    }
                });
    }

    /**
     * 点赞请求接口
     * @param cardId 动态id
     */
    public void requestCardLike(int cardId){
        service.getLikeInfo(cardId)
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().resultCardLike(cardId);
                        }else {
                            getView().failCardLike(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().errorCardLike(msg);
                    }
                });
    }
    /**
     * 动态置顶操作
     *
     * @param cardId 动态id
     * @param state  是否置顶
     */
    public void setCardStick(int cardId, int state) {
        service.setMineCardDetailStick(
                new ParamsBuilder()
                        .put("cardId", cardId + "")
                        .put("state", state + "").build())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<Boolean>>() {
                    @Override
                    public void _onNext(HttpResult<Boolean> data) {
                        if (data.isSuccess()) {
                            getView().resultStickSuccess(state == 1);
                        } else {
                            getView().resultStickFail(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    /**
     * 动态权限操作
     *
     * @param cardId 动态id
     * @param power  权限
     */
    public void setCardAuthority(int cardId, int power) {
        service.setMineCardDetailAuthority(new ParamsBuilder()
                .put("id", cardId + "")
                .put("powerStatus", power + "")
                .build())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<Boolean>>() {
            @Override
            public void _onNext(HttpResult<Boolean> data) {
                if (data.isSuccess()) {
                    getView().resultAuthoritySuccess(data.data);
                } else {
                    getView().resultAuthorityFail(data.description);
                }
            }

            @Override
            public void _onError(String msg) {

            }
        });
    }
}
