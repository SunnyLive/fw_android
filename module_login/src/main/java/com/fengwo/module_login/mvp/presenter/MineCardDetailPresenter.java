package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.MCD_CommentDto;
import com.fengwo.module_login.mvp.dto.MineCardDetailDto;
import com.fengwo.module_login.mvp.dto.ShareUrlDto;
import com.fengwo.module_login.mvp.ui.iview.IMineCardDetailView;

public class MineCardDetailPresenter extends BasePresenter<IMineCardDetailView> {

    public LoginApiService service;

    public MineCardDetailPresenter() {
        service = new RetrofitUtils().createApi(LoginApiService.class);
    }

    /**
     * 获取动态详情信息
     *
     * @param cardId 动态id
     */
    public void getCardDetail(int cardId) {
        service.getMineCardDetail(cardId)
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<MineCardDetailDto>>() {
                    @Override
                    public void _onNext(HttpResult<MineCardDetailDto> data) {
                        if (data.isSuccess()) {
                            getView().resultDataSuccess(data.data);
                        } else {
                            getView().resultDataFail(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().requestError(msg);
                    }
                });

    }

    /**
     * 获取评论列表数据信息
     *
     * @param cardId    动态id
     * @param pageIndex 分页
     */
    public void getCommentList(int cardId, int pageIndex) {
        service.getMineCardComment(cardId, pageIndex + ",20")
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<BaseListDto<MCD_CommentDto>>>() {
                    @Override
                    public void _onNext(HttpResult<BaseListDto<MCD_CommentDto>> data) {
                        if (data.isSuccess()) {
                            getView().resultCommentsSuccess(data.data);
                        } else {
                            getView().resultCommentsFail(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().requestError(msg);
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
                        getView().requestError(msg);
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
        getView().showLoadingDialog();
        service.setMineCardDetailAuthority(new ParamsBuilder()
                .put("id", cardId + "")
                .put("powerStatus", power + "")
                .build()).subscribe(new LoadingObserver<HttpResult<Boolean>>() {
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
                getView().requestError(msg);
            }
        });
    }


    /**
     * 添加评论
     * <p>
     * cardId	integer($int32)
     * 卡片ID
     * <p>
     * content	string
     * 内容
     * <p>
     * parentId	integer($int32)
     * 评论ID,一级评论父ID为0
     * <p>
     * secondType	integer($int32)
     * 2级评论类型（0为一级评论） 1：评论 2：回复
     * <p>
     * secondUserId	integer($int32)
     * 2级评论回复对象id（2级评论为回复类型填回复对象ID，其余填默认值0）
     * <p>
     * type	integer($int32)
     * 评论类型：1 一级 2 二级
     * <p>
     * map.put("cardId", cardId+"");
     * map.put("content", content);
     * map.put("parentId", 0);
     * map.put("secondType", 0);
     * map.put("secondUserId", 0);
     * map.put("type", 1);
     *
     * @param cardId
     * @param content
     */
    public void sendComment(int cardId, String content) {
        service.sendComment(new ParamsBuilder()
                .put("cardId", cardId+"")
                .put("content", content)
                .put("parentId", "0")
                .put("secondType", "0")
                .put("secondUserId", "0")
                .put("type", "1")
                .build())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<MCD_CommentDto>>() {
                    @Override
                    public void _onNext(HttpResult<MCD_CommentDto> data) {
                        if (data.isSuccess()) {
                            getView().sendCommentResultSuccess(data.data);
                        } else {
                            getView().sendCommentResultFail(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().requestError(msg);
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
                            getView().resultShareSuccess(data.data);
                        } else {
                            getView().resultShareFail(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().requestError(msg);
                    }
                });
    }


    /**
     * 点赞请求接口
     *
     * @param cardId 动态id
     */
    public void requestCardLike(int cardId) {
        service.getLikeInfo(cardId)
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().resultCardLikeSuccess();
                        } else {
                            getView().resultCardLikeFail(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().requestError(msg);
                    }
                });
    }


    /**
     *
     * 评论点赞请求网路
     * @param parentIndex position
     * @param id id
     */
    public void requestLikeComment(int parentIndex, int id) {
        service.likeComment(id)
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().resultCommentLike(parentIndex);
                        } else {
                            getView().resultCardLikeFail(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().requestError(msg);
                    }
                });
    }

}
