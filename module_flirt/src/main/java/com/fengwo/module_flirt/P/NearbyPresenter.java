package com.fengwo.module_flirt.P;

import android.text.TextUtils;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_flirt.Interfaces.INearbyView;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.bean.FindHeaderDto;

import java.util.List;

import okhttp3.RequestBody;

/**
 * @Author BLCS
 * @Time 2020/4/24 18:16
 */
public class NearbyPresenter extends BaseFlirtPresenter<INearbyView> {
    /**
     * 获取附近的人
     */
    public void getPeopleNearby(String longitude, String latitude, String maxAge, String minAge, int page, String sex) {
        RequestBody build = new WenboParamsBuilder()
                .put("longitude", longitude)
                .put("latitude", latitude)
                .put("maxAge", maxAge)
                .put("minAge", minAge)
                .put("page", page+",10")
                .put("sex", sex)
                .build();
        addNet(service.getPeopleNearby(build).compose(handleResult()).subscribeWith(new LoadingObserver<BaseListDto<CityHost>>() {
            @Override
            public void _onNext(BaseListDto<CityHost> data) {
                getView().setNearByData(data.records);
            }

            @Override
            public void _onError(String msg) {
                if (!TextUtils.isEmpty(msg)){
                    getView().toastTip(msg);
                }
            }
        }));
    }


}
