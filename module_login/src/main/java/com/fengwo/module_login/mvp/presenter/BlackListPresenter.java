package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_login.mvp.dto.BankDto;
import com.fengwo.module_login.mvp.dto.BlackDto;
import com.fengwo.module_login.mvp.ui.iview.IBlackListView;

import java.util.List;

public class BlackListPresenter extends BaseLoginPresenter<IBlackListView> {

    public void getBlackList(String pageParam){
        service.getBlackList(pageParam)
                .compose(handleResult())
                .subscribe(new LoadingObserver<BaseListDto<BlackDto>>() {
                    @Override
                    public void _onNext(BaseListDto<BlackDto> data) {
                        getView().getBalckList(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    public void deleteBlack(int userId,int pos){
        service.deleteBlack(userId)
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult>(getView()) {
                    @Override
                    public void _onNext(HttpResult data) {
                        getView().deleteSuccess(data.description,pos);
                    }
                    @Override
                    public void _onError(String msg) {
                    }
                });
    }
}
