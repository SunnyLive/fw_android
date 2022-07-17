package com.fengwo.module_vedio.mvp.presenter;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_vedio.mvp.dto.ShortVedioListDto;
import com.fengwo.module_vedio.mvp.dto.SmallCommentDto;
import com.fengwo.module_vedio.mvp.ui.iview.IShortVedioDetailView;

public class ShortVedioDetailPresenter extends BaseVideoPresenter<IShortVedioDetailView> {

    public void addFavourite(int id) {
        service.setShortFavourite(new ParamsBuilder()
                .put("id", id + "")
                .build())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        getView().toggleFavourite();
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });

    }

    public void getDetail(int id) {
        service.getShortDetail(id)
                .compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<ShortVedioListDto.ShortVedio>() {
                    @Override
                    public void _onNext(ShortVedioListDto.ShortVedio data) {
                        getView().setDetailData(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    public void getComments(int id, int page) {
        service.getShortComment(new ParamsBuilder()
                .put("movieId", id + "")
                .put("current", page + "")
                .build())
                .compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<SmallCommentDto>() {
                    @Override
                    public void _onNext(SmallCommentDto data) {
                        getView().setComments(data.records, page);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });

    }


}
