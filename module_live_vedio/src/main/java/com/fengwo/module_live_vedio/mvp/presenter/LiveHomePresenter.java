package com.fengwo.module_live_vedio.mvp.presenter;

import com.fengwo.module_comment.base.BannerBean;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_live_vedio.mvp.ui.iview.ILivingHome;

import java.util.List;

public class LiveHomePresenter extends BasePresenter<ILivingHome> {

    LiveApiService service;

    public LiveHomePresenter() {
        service = new RetrofitUtils().createApi(LiveApiService.class);
    }

    public void getRecomment() {
        addNet(service.getRecomment(1)
                .compose(handleResult())
                .subscribeWith(
                        new LoadingObserver<BaseListDto<ZhuboDto>>() {

                            @Override
                            public void _onNext(BaseListDto<ZhuboDto> data) {
                                if (getView() == null) return;
                                getView().initHeaderRv(data.records);
                            }

                            @Override
                            public void _onError(String msg) {
                                KLog.d("getRecomment error", msg);
//                                if ("请重新登录".equals(msg)) {
//                                    getView().tokenIInvalid();
//                                }
                            }
                        }
                ));
    }

    public void getBanner() {
        addNet(service.getBanner(1)
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<List<BannerBean>>() {
                    @Override
                    public void _onNext(List<BannerBean> result) {
                        if (getView() == null) return;
                        if (CommentUtils.isListEmpty(result)) {
                            return;
                        }
                        getView().setBanner(result);
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                }));
    }

    public void getZhuboList(int page, int menuId) {
        String p = page + "," + PAGE_SIZE;
        addNet(service.getZhuboList(p, menuId)
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<ZhuboDto>>() {
                    @Override
                    public void _onNext(BaseListDto<ZhuboDto> data) {
                        if (getView() == null) return;
                        getView().setLoadmoreEnable(page < data.pages);
                        getView().setZhuboList(data.records, page);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (getView() == null) return;
                        getView().netError();
                    }
                }));
    }
}
