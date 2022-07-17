package com.fengwo.module_vedio.mvp.presenter;

import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_vedio.mvp.dto.ShortVedioListDto;
import com.fengwo.module_vedio.mvp.ui.iview.IThemeListMenuView;

public class ThemeListMenuPresenter extends BaseVideoMenuPresenter<IThemeListMenuView> {


    public void getThemeByType(int type, int page) {
        service.getShortVedioList(new ParamsBuilder()
                .put("approvalStatus", 5 + "")
                .put("isPrivacy", 0 + "")
                .put("type", 0 + "")
                .put("status", 0 + "")
                .put("current", page + "").build())
//                .put("themeId", type + "").build())
                .compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<ShortVedioListDto>() {
                    @Override
                    public void _onNext(ShortVedioListDto data) {
                        getView().setThemeList(data.records, page);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });

    }

}
