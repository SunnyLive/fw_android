package com.fengwo.module_vedio.mvp.presenter;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_vedio.mvp.dto.AdvertiseBean;
import com.fengwo.module_vedio.mvp.dto.VideoHomeCategoryDto;
import com.fengwo.module_vedio.mvp.ui.iview.IVideoHomeView;

/**
 * @author Zachary
 * @date 2019/12/24
 */
public class VideoHomePresenter extends BaseVideoPresenter<IVideoHomeView> {

    public void getCategory() {
        service.getCategory("1,100").compose(io_main()).compose(handleResult())
                .subscribe(new LoadingObserver<BaseListDto<VideoHomeCategoryDto>>() {
                    @Override
                    public void _onNext(BaseListDto<VideoHomeCategoryDto> data) {
                        if (null == getView()) return;
                        getView().setCategory(data.records);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (null == getView()) return;
                        getView().toastTip(msg);
                    }
                });
    }

    public void getBannerAd() {
        service.getBannerAd(3, 1, "1,20", 1, 0, 1)
                .compose(io_main()).compose(handleResult())
                .subscribe(new LoadingObserver<BaseListDto<AdvertiseBean>>() {
                    @Override
                    public void _onNext(BaseListDto<AdvertiseBean> data) {
                        if (null == getView()) return;
                        getView().setBannerData(data.records);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (null == getView()) return;
                        getView().toastTip(msg);
                    }
                });
    }
}
