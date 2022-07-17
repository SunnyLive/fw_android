package com.fengwo.module_vedio.mvp.presenter;

import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_vedio.mvp.dto.SmallVedioListDto;
import com.fengwo.module_vedio.mvp.ui.iview.ISmallVedioMenuView;


public class SmallVedioMenuPresenter extends BaseVideoMenuPresenter<ISmallVedioMenuView> {

    public void getSmallVedioList(int page, int menuId) {
        service.getSmallVedioList(new ParamsBuilder()
                .put("current", page + "")
                .put("menuId", menuId + "")
                .build())
                .compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<SmallVedioListDto>() {
                    @Override
                    public void _onNext(SmallVedioListDto data) {
                        getView().setSmallVedioList(data.records, page);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }
}
