package com.fengwo.module_chat.mvp.presenter;

import com.fengwo.module_chat.mvp.model.bean.AdvertiseBean;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_chat.mvp.ui.contract.IChatHomeView;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;

import java.util.List;

public class ChatHomePresenter extends BaseChatPresenter<IChatHomeView> {
    public void getRecommendList() {
        addNet(service.getRecommendCircleList().compose(io_main())
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<List<RecommendCircleBean>>() {
                    @Override
                    public void _onNext(List<RecommendCircleBean> data) {
                        if (null == getView()) return;
                        getView().setRecommendCircleList(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (null == getView()) return;
                        getView().toastTip(msg);
                    }
                }));
    }

    public void getHomeBanner(String pageParam, int position) {
        addNet(service.getBanner(2, 1, pageParam, 1, 0 + "", position)
                .compose(io_main()).compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<AdvertiseBean>>() {
                    @Override
                    public void _onNext(BaseListDto<AdvertiseBean> data) {
                        if (null == getView()) return;
                        getView().setTopBanner(data.records);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (null == getView()) return;
                        getView().toastTip(msg);
                    }
                }));
    }

    public void getCardList(String pageParam) {
        addNet(service.getCirclesList("0", pageParam, "1").compose(io_main()).compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<CircleListBean>>() {
                    @Override
                    public void _onNext(BaseListDto<CircleListBean> data) {
                        if (null == getView()) return;
                        getView().setCardList(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (null == getView()) return;
                        getView().loadListFailed(msg);
                    }
                }));
    }


    public void cardLike(String id, int position) {
        addNet(service.cardLike(id).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                if (data.isSuccess()) getView().cardLikeSuccess(id, position);
                else getView().toastTip(data.description);
            }

            @Override
            public void _onError(String msg) {
                getView().toastTip(msg);
            }
        }));
    }
}