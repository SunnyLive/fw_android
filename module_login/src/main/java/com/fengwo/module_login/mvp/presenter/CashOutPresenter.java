package com.fengwo.module_login.mvp.presenter;

import android.app.Activity;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_login.mvp.dto.CashOutDto;
import com.fengwo.module_login.mvp.ui.iview.ICashOutView;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/29
 */
public class CashOutPresenter extends BaseLoginPresenter<ICashOutView> {

    public void getPageData(){
        service.getCashOutPageData()
                .compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<CashOutDto>() {
                    @Override
                    public void _onNext(CashOutDto data) {
                        getView().returnPageDate(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort((Activity) getView(),msg);
                    }
                });
    }

    public void cashOutCommit(int money){
        service.cashOutCommit(money)
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        getView().returnCommitResult(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort((Activity) getView(),msg);
                    }
                });
    }
}
