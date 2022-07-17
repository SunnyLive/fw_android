package com.fengwo.module_vedio.mvp.presenter;

import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_vedio.mvp.dto.ShortVedioListDto;
import com.fengwo.module_vedio.mvp.dto.SmallVedioListDto;
import com.fengwo.module_vedio.mvp.ui.iview.ISquareView;

public class SquarePresenter extends BaseVideoPresenter<ISquareView> {

    public void getGuessLike() {
        service.getShortVedioList(new ParamsBuilder()
                .put("approvalStatus", 5 + "")
                .put("isPrivacy", 0 + "")
                .put("type", 2 + "")
                .put("status", 0 + "")
                .put("size",2+"")
                .put("current", 1 + "").build())
                .compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<ShortVedioListDto>() {
                    @Override
                    public void _onNext(ShortVedioListDto data) {
                        getView().setGuessLike(data.records);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    public void getSmallVedioList() {
        service.getSmallVedioList(new ParamsBuilder()
                .put("current", 1 + "")
                .put("size", 4 + "")
                .build())
                .compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<SmallVedioListDto>() {
                    @Override
                    public void _onNext(SmallVedioListDto data) {
                        getView().setSmallVedioList(data.records);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }
}
