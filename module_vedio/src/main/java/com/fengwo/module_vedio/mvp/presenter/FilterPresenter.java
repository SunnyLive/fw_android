package com.fengwo.module_vedio.mvp.presenter;

import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_vedio.mvp.dto.FilterLabelDto;
import com.fengwo.module_vedio.mvp.ui.iview.IFilterView;

public class FilterPresenter extends BaseVideoPresenter<IFilterView> {

    public void getLabs() {
        addNet(service.getFilterLabs()
                .compose(io_main())
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<FilterLabelDto>(getView()) {
                    @Override
                    public void _onNext(FilterLabelDto data) {
                        getView().setLabs(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }
}
