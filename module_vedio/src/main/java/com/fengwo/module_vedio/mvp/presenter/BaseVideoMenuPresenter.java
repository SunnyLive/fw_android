package com.fengwo.module_vedio.mvp.presenter;

import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_vedio.api.VedioApiService;
import com.fengwo.module_vedio.mvp.dto.MenuDto;
import com.fengwo.module_vedio.mvp.ui.iview.IBaseVideoMenuView;

import java.util.List;

import okhttp3.RequestBody;

public class BaseVideoMenuPresenter<V extends IBaseVideoMenuView> extends BasePresenter<V> {
    public final static String MENU_TYPE_VIDEO = "video";
    public final static String MENU_TYPE_MOVIE = "movie";
    public final static String MENU_TYPE_SHORT = "short";
    public final static String MENU_TYPE_LABEL = "label";

    public VedioApiService service;
    private boolean isFirst;

    public BaseVideoMenuPresenter() {
        service = new RetrofitUtils().createApi(VedioApiService.class);
    }

    public void getMenu(String type) {
        ParamsBuilder b = new ParamsBuilder();
        RequestBody body = b.put("videoType", type).build();
        isFirst = true;
        service.getMenu(body).compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<List<MenuDto>>() {
                    @Override
                    public void _onNext(List<MenuDto> data) {
                        getView().setTypeMenu(data);

                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

}
