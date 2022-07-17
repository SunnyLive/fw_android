package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_login.mvp.dto.VerifyResultDto;
import com.fengwo.module_login.mvp.dto.VerifyTokenDto;
import com.fengwo.module_login.mvp.ui.iview.IFaceVerifyView;

public class FaceVerifyPresenter extends BaseLoginPresenter<IFaceVerifyView> {
    /**
     *
     * @param type
     */
    public void getVerifyToken(String type) {
        service.getVerifyToken(type)
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<VerifyTokenDto>>() {
                    @Override
                    public void _onNext(HttpResult<VerifyTokenDto> data) {
                        if(getView() == null) return;
                        if (data.data != null) {
                            getView().onGetVerifyTokenSuccess(data.data);
                        } else {
                           getView().onGetVerifyTokenError(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        if (getView() == null) return;
                        getView().onGetVerifyTokenError(msg);
                    }
                });
    }

    /**
     *
     * @param bizId
     * @param type
     */
    public void getVerifyResult(String bizId, String type) {
        service.getVerifyResult(bizId, type)
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<VerifyResultDto>>() {
                    @Override
                    public void _onNext(HttpResult<VerifyResultDto> data) {
                        if (getView() == null) return;
                        if (data.data != null) {
                            getView().onGetVerifyResultSuccess(data.data);
                        } else {
                            getView().onGetVerifyResultError(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        if (getView() == null) return;
                        getView().onGetVerifyResultError(msg);
                    }
                });
    }


    /**
     * 请求人工审核的接口 并返回结果
     * @param bizId
     */
    public void getVerifyReview(String bizId){
        service.getVerifyReview(bizId).compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().onFaceReViewSuccess();
                        }else {
                            getView().toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

}
