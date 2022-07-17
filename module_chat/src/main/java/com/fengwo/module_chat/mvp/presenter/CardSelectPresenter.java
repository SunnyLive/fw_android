package com.fengwo.module_chat.mvp.presenter;

import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_chat.mvp.ui.contract.ICardSelectView;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;

public class CardSelectPresenter extends BaseChatPresenter<ICardSelectView> {

    public void getCircles(String pageParam) {
        addNet(service.getCircleList(pageParam).compose(io_main()).compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<RecommendCircleBean>>() {
                    @Override
                    public void _onNext(BaseListDto<RecommendCircleBean> data) {
                        if (data.records != null) {
                            getView().setCircles(data.records);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));
    }

    public void setUserLoveCircle(String ids) {
        addNet(service.setUserLoveCircle(new ParamsBuilder().put("objectIds", ids)
                .put("type", "1").build())
                .compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().setLoveCircleSuccess();
                        } else getView().toastTip(data.description);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));
    }
}
