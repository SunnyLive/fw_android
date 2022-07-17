package com.fengwo.module_vedio.mvp.presenter;

import android.annotation.SuppressLint;

import com.alibaba.android.arouter.facade.annotation.Param;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_vedio.mvp.dto.SmallCommentDto;
import com.fengwo.module_vedio.mvp.ui.iview.ISmallAuthorDetailView;
import com.fengwo.module_vedio.mvp.ui.iview.ISmallVedioDetailView;

import java.util.List;
import java.util.Map;

public class SmallVedioDetailPresenter extends BaseVideoPresenter<ISmallVedioDetailView> {

    public void setLike(int vedioId, int position) {
        service.setSmallLike(new ParamsBuilder()
                .put("videoId", vedioId + "")
                .put("type", "0")
                .build()
        )
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().setLike(position);
                        }

                    }

                    @Override
                    public void _onError(String msg) {
                    }
                });
    }


//    public void getComment(int vedioId) {
//        service.getComment().compose(io_main())
//                .compose(handleResult())
//                .subscribe(new LoadingObserver<SmallCommentDto>(getView()) {
//                    @Override
//                    public void _onNext(SmallCommentDto data) {
//                        getView().setComment(vedioId, data.records);
//                    }
//
//                    @Override
//                    public void _onError(String msg) {
//
//                    }
//                });
//
//    }

    public void addPlayNum(int vedioId) {
        addNet(service.addSmallVideoPlayNem(vedioId).compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));

    }
    public void delSmallVideo(Map map) {
        addNet(service.delSmallVideo(createRequestBody(map)).compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        getView().setDelete(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));

    }
}
