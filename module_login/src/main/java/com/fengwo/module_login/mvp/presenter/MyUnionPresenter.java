package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_login.mvp.dto.UnionInfo;
import com.fengwo.module_login.mvp.ui.iview.IMyUnionView;

public class MyUnionPresenter extends BaseLoginPresenter<IMyUnionView> {

    /*
     *
     * 获取公会
     * 和我的公会信息
     *
     * */
    public void getUnionInfo() {

        service.getUnionInfo().compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<UnionInfo>>() {
                    @Override
                    public void _onNext(HttpResult<UnionInfo> data) {
                        if (data.isSuccess()) {
                            getView().onUnionInfoSuccess(data.data);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });


    }


}
