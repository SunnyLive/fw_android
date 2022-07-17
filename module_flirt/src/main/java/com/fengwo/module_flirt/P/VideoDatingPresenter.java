package com.fengwo.module_flirt.P;


import android.text.TextUtils;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_flirt.Interfaces.IVideoDatingView;
import com.fengwo.module_flirt.bean.CityHost;

import okhttp3.RequestBody;

/**
 * @Author BLCS
 * @Time 2020/4/27 12:02
 */
public class VideoDatingPresenter extends BaseFlirtPresenter<IVideoDatingView>{
    /**
     * 获取视频交友列表
     * @param status  0 推荐 1 在线
     */
    public void getVideoDating(String city, int page, int status, String tagName) {
        RequestBody build = new WenboParamsBuilder()
                .put("city", city)
                .put("page", page+",10")
                .put("status", String.valueOf(status))
                .put("tagName",tagName)
                .build();

        addNet(service.getCityHost(build).compose(handleResult()).subscribeWith(new LoadingObserver<BaseListDto<CityHost>>() {
            @Override
            public void _onNext(BaseListDto<CityHost> data) {
                getView().setData(data.records);
            }

            @Override
            public void _onError(String msg) {
                if (TextUtils.isEmpty(msg)) return;
                getView().toastTip(msg);
            }
        }));
    }
}
