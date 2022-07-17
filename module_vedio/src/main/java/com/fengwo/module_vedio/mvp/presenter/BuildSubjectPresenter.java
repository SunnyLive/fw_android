package com.fengwo.module_vedio.mvp.presenter;

import android.annotation.SuppressLint;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_vedio.mvp.ui.iview.IBuildSubjectView;

import java.util.Map;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/6
 */
public class BuildSubjectPresenter extends BaseVideoPresenter<IBuildSubjectView> {

    @SuppressLint("CheckResult")
    public void addAlbum(Map map){
        service.addAlbum(createRequestBody(map))
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        getView().setAddAlbum(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });

    }
}
