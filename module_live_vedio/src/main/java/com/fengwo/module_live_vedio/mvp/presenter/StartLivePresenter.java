package com.fengwo.module_live_vedio.mvp.presenter;

import android.app.Activity;

import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_live_vedio.mvp.dto.ChannelShareInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.StartLivePushDto;
import com.fengwo.module_live_vedio.mvp.dto.ZhuboMenuDto;
import com.fengwo.module_live_vedio.mvp.ui.iview.IStartView;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

public class StartLivePresenter extends BaseLivePresenter<IStartView> {

    public void getZhuboMenu() {
        addNet(service.getZhuboMenu(2)
                .compose(io_main())
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<ZhuboMenuDto>>(getView()) {
                    @Override
                    public void _onNext(BaseListDto<ZhuboMenuDto> data) {
                        if (getView() == null) return;
                        getView().setMenu(data.records);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    public void startLivePush(Map map) {
        addNet(service.startLive(createRequestBody(map))
                .compose(io_main())
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<StartLivePushDto>() {
                    @Override
                    public void _onNext(StartLivePushDto data) {
                        if (getView() == null) return;
                        getView().startLivePush(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (getView() == null) return;
                        getView().toastTip(msg);
                    }
                }));
    }

    public void hostConnecLine() {
        addNet(service.hostConnLine()
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult<StartLivePushDto>>() {
                    @Override
                    public void _onNext(HttpResult<StartLivePushDto> data) {
                        if (getView() == null) return;
                        getView().connectionLine(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));
    }

    public void getRoomInfo(String channelId, SHARE_MEDIA shareMedia) {
        addNet(service.getRoomInfo(new ParamsBuilder()
                .put("channelId", channelId).build())
                .compose(io_main())
                .compose(handleResult()).subscribeWith(new LoadingObserver<ChannelShareInfoDto>() {
                    @Override
                    public void _onNext(ChannelShareInfoDto data) {
                        if (getView() == null) return;
                        getView().share(data, shareMedia);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));
    }

    public void rejectReconnect() {
        addNet(service.rejectReconnect()
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {

                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    //
    // 主播端 异常断播 请求的接口
    //
    public void requestLiveConnectionStatus(){
        addNet(service.requestLiveConnectionStatus().compose(io_main()).subscribeWith(new LoadingObserver<HttpResult<Boolean>>(getView()) {
            @Override
            public void _onNext(HttpResult<Boolean> data) {
                if (data.data) {
                    getView().onLiveConnection();
                }else {
                    getView().onLiveReStart();
                }
            }

            @Override
            public void _onError(String msg) {

            }
        }));
    }



}
