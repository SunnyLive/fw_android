package com.fengwo.module_chat.mvp.presenter;

import com.fengwo.module_chat.mvp.model.bean.SearchCardBean;
import com.fengwo.module_chat.mvp.ui.contract.SocialSearchView;
import com.fengwo.module_comment.base.LoadingObserver;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/26
 */
public class SocialSearchPresenter extends BaseChatPresenter<SocialSearchView> {

    public void getSearchHot() {
        addNet(service.getSearchHot()
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<List<String>>() {
                    @Override
                    public void _onNext(List<String> data) {
                        getView().returnSearchHot(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    public void getSearchCard(String pageParams, String content) {
        addNet(service.getSearchCard(pageParams, content)
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<SearchCardBean>() {
                    @Override
                    public void _onNext(SearchCardBean data) {
                        getView().returnSearchCard(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }
}
