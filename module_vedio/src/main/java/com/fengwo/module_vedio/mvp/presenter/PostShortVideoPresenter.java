package com.fengwo.module_vedio.mvp.presenter;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_vedio.mvp.ui.iview.IPostShortVideoView;

import java.util.Map;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/6
 */
public class PostShortVideoPresenter extends BaseVideoPresenter<IPostShortVideoView> {

    public void addShortVideo(Map map) {
        addNet(service.addShortVideo(createRequestBody(map))
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess())
                            getView().setAddShortVideo(data);
                        else
                            getView().toastTip(data.description);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));
    }
}
