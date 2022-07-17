package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.LiveLengthDto;
import com.fengwo.module_login.mvp.ui.iview.ILiveTimeView;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/30
 */
public class LiveTimePresenter extends BaseLoginPresenter<ILiveTimeView> {

    public void getLiveTime(String pageParam, Map map){
        service.getLiveLength(pageParam,createRequestBody(map))
                .compose(handleResult())
                .subscribe(new LoadingObserver<LiveLengthDto>() {
                    @Override
                    public void _onNext(LiveLengthDto data) {
                        getView().returnLiveTimeDate(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                });
    }
    public void getVideoTime(String startTime,String endTime,String page){
        RequestBody build = new WenboParamsBuilder()
                .put("endTime", endTime)
                .put("page", page)
                .put("startTime", startTime)
                .build();
        new RetrofitUtils().createWenboApi(LoginApiService.class)
                .getVideoTime(build)
                .compose(handleResult())
                .subscribe(new LoadingObserver<LiveLengthDto>() {
                    @Override
                    public void _onNext(LiveLengthDto data) {
                        getView().returnLiveTimeDate(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                });
    }
    //获取视频总时长
    public void getVideoLength(){
        new RetrofitUtils().createWenboApi(LoginApiService.class)
                .getVideoLength()
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        getView().returnAllTime(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                });
    }
}
