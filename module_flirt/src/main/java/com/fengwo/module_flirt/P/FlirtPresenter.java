package com.fengwo.module_flirt.P;

import android.text.TextUtils;

import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_chat.mvp.model.bean.AdvertiseBean;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_flirt.Interfaces.IFlirtView;
import com.fengwo.module_flirt.bean.CerTagBean;
import com.fengwo.module_login.api.LoginApiService;

import java.util.List;

import okhttp3.RequestBody;

/**
 * @Author BLCS
 * @Time 2020/4/24 12:19
 */
public class FlirtPresenter extends BaseFlirtPresenter<IFlirtView> {
    /**
     * 获取广告栏
     */
    public void getBanner(String pageParam, int position) {
        ChatService service = new RetrofitUtils().createApi(ChatService.class);
        addNet(service.getBanner(4, 1, pageParam, 1, 0 + "", position)
                .compose(io_main()).compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<AdvertiseBean>>() {
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
                }));
    }

    /**
     * 上传经纬度
     */
    public void upLoading(String latitude, String longitude, String city) {
////      {"params":{"longitude ":"118.100552","latitude ":"24.519782"}}
//        L.e("====== latitude "+latitude);
//        L.e("====== longitude "+longitude);
//        RequestBody build = new WenboParamsBuilder()
//                .put("latitude ", latitude)
//                .put("longitude ", longitude )
//                .build();
//        LoginApiService service = new RetrofitUtils().createApi(LoginApiService.class);
//        addNet(service.putLnglat(build).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
//            @Override
//            public void _onNext(HttpResult data) {
//                L.e("=====上传经纬度 " + data.description);
//            }
//
//            @Override
//            public void _onError(String msg) {
//                L.e("=====上传经纬度 " + msg);
//            }
//        }));
        new RetrofitUtils().createApi(LoginApiService.class)
                .updateUserinfo(new HttpUtils.ParamsBuilder()
//                        .put("signature", userInfo.signature)
//                        .put("sex", userInfo.sex + "")
                                .put("location", city)
                                .put("longitude", longitude)
                                .put("latitude", latitude)
//                        .put("headImg", userInfo.headImg)
//                        .put("birthday", userInfo.birthday)
                                .build()
                )
                .compose(io_main())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        L.e("更新地址成功");
                    }

                    @Override
                    public void _onError(String msg) {
                        L.e("更新地址失败 " + msg);
                    }
                });
    }

    /**
     * 标签 0-推荐，1-在线
     */
    public void getLabel(int status,String city) {
        RequestBody build = new WenboParamsBuilder()
                .put("status", status)
                .put("city", city)
                .build();
        KLog.e("okh==city",city);
        addNet(service.getHomeTagList(build).compose(handleResult())
                .subscribeWith(new LoadingObserver<List<CerTagBean>>() {
                    @Override
                    public void _onNext(List<CerTagBean> data) {
                        getView().getLabelSuccess(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }

    /**
     * 我的点单列表
     */
    public void getMyOrder() {
        addNet(service.getMyOrder().compose(handleResult())
                        .subscribeWith(new LoadingObserver<List<MyOrderDto>>(getView()) {
                            @Override
                            public void _onNext(List<MyOrderDto> data) {
                                getView().setMyOrderList(data);
                            }

                            @Override
                            public void _onError(String msg) {
                                if (TextUtils.isEmpty(msg)) return;
                                getView().toastTip(msg);
                            }
                        }));
    }

}
