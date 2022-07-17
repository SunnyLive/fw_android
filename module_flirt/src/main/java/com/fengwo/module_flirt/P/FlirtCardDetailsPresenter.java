package com.fengwo.module_flirt.P;


import android.text.TextUtils;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.GreetTipsBean;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_flirt.Interfaces.IFlirtCardDetailsView;
import com.fengwo.module_flirt.bean.AppointTimes;
import com.fengwo.module_comment.bean.CheckAnchorStatus;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.bean.OrderIdBean;
import com.fengwo.module_flirt.bean.OrderListBean;
import com.fengwo.module_flirt.bean.ShareInfoBean;
import com.fengwo.module_flirt.bean.SureAppointmentBean;
import com.fengwo.module_login.api.LoginApiService;

import java.util.ArrayList;
import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import okhttp3.RequestBody;

/**
 * @Author BLCS
 * @Time 2020/4/27 12:02
 */
public class FlirtCardDetailsPresenter extends BaseFlirtPresenter<IFlirtCardDetailsView> {

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

    /**
     * 获取在线列表
     */
    public void getOnlineList(int page, String city, String maxAge, String minAge, String sex) {
        RequestBody build = new WenboParamsBuilder()
                .put("page", String.valueOf(page))
                .put("city", city)
                .put("maxAge", maxAge)
                .put("minAge", minAge)
                .put("sex", sex)
                .build();
        addNet(service.getOnLineList(build).compose(handleResult()).subscribeWith(new LoadingObserver<BaseListDto<CityHost>>(getView()) {
            @Override
            public void _onNext(BaseListDto<CityHost> data) {
                getView().onReceiveFlirtData(page == 1, data.records);
            }

            @Override
            public void _onError(String msg) {
                if (TextUtils.isEmpty(msg)) return;
                getView().toastTip(msg);
            }
        }));
    }

    /**
     * 主播信息详情
     */
    public void getAnchorInfo(int position, int appointmentId) {
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", appointmentId + "")
                .build();
        addNet(service.getAnchorInfo(build).compose(handleResult()).subscribeWith(new LoadingObserver<CityHost>(false) {

            @Override
            public void _onNext(CityHost data) {
                getView().getAnchorInfoSuccess(position, data);
            }

            @Override
            public void _onError(String msg) {
                L.e("======" + msg);
                if (!TextUtils.isEmpty(msg)) {
                    getView().toastTip(msg);
                }
            }
        }));
    }

    /**
     * 检测主播直播状态
     */
    public void checkAnchorStatus(int anchorId, boolean b) {
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", anchorId + "")
                .build();
        addNet(service.checkAnchorStatus(build).compose(handleResult()).subscribeWith(new LoadingObserver<CheckAnchorStatus>(false) {
            @Override
            public void _onNext(CheckAnchorStatus data) {
                getView().checkAnchorStatus(data, b);
            }

            @Override
            public void _onError(String msg) {
                L.e("======" + msg);
                if (!TextUtils.isEmpty(msg)) {
                    getView().toastTip(msg);
                }
            }
        }));
    }
    public void checkAnchorStatusint(int anchorId, boolean b) {
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", anchorId + "")
                .build();
        addNet(service.checkAnchorStatus(build).compose(handleResult()).subscribeWith(new LoadingObserver<CheckAnchorStatus>(false) {
            @Override
            public void _onNext(CheckAnchorStatus data) {
                getView().checkAnchorStatus(data, b);
            }

            @Override
            public void _onError(String msg) {
                L.e("======" + msg);
                if (!TextUtils.isEmpty(msg)) {
                    getView().toastTip(msg);
                }
            }
        }));
    }
    /**
     * 获取视频交友列表
     * 暂时该接口调用点未知！！
     */
    @Deprecated
    public void getVideoDating(String city, int page, int status, String tagName) {
        L.e("page " + page);
        RequestBody build = new WenboParamsBuilder()
                .put("city", city)
                .put("page", page + ",10")
                .put("status", String.valueOf(status))
                .put("tagName", tagName)
                .build();

        addNet(service.getCityHost(build).compose(handleResult()).subscribeWith(new LoadingObserver<BaseListDto<CityHost>>() {
            @Override
            public void _onNext(BaseListDto<CityHost> data) {
//                getView().setVideoData(data.records);
            }

            @Override
            public void _onError(String msg) {
                if (TextUtils.isEmpty(msg)) return;
                getView().toastTip(msg);
            }
        }));
    }

//    /**
//     * 获取附近的人  第二次更改放弃
//     */
//    public void getPeopleNearby(String longitude, String latitude, String maxAge, String minAge, int page, String sex) {
//        RequestBody build = new WenboParamsBuilder()
//                .put("longitude", longitude)
//                .put("latitude", latitude)
//                .put("maxAge", maxAge)
//                .put("minAge", minAge)
//                .put("page", page + ",10")
//                .put("sex", sex)
//                .build();
//        addNet(service.getPeopleNearby(build).compose(handleResult()).subscribeWith(new LoadingObserver<BaseListDto<CityHost>>() {
//            @Override
//            public void _onNext(BaseListDto<CityHost> data) {
//                getView().setNearByData(data.records);
//            }
//
//            @Override
//            public void _onError(String msg) {
//                if (!TextUtils.isEmpty(msg)) {
//                    getView().toastTip(msg);
//                }
//            }
//        }));
//    }

    /**
     * 查询主播时间段价格
     *
     * @anchorId 主播ID
     * @current 当前页
     * @size 每页显示数量
     */
    public void getPeriodPrice(String anchorId) {
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", anchorId)
                .build();

        addNet(service.getPeriodPrice(build).compose(handleResult()).subscribeWith(new LoadingObserver<AppointTimes>() {
            @Override
            public void _onNext(AppointTimes data) {
//                getView().setPeriodPrice(data);
            }

            @Override
            public void _onError(String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    getView().toastTip(msg);
                }
            }
        }));
    }

    /**
     * 点单价格表
     */
    public void getOrderListPrice(String anchorId) {
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", anchorId)
                .build();
        addNet(service.getOrderListPrice(build).compose(handleResult()).subscribeWith(new LoadingObserver<ArrayList<OrderListBean>>() {
            @Override
            public void _onNext(ArrayList<OrderListBean> data) {
//                getView().setOrderListPrice(data);
            }

            @Override
            public void _onError(String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    getView().toastTip(msg);
                }
            }
        }));
    }

    /**
     * 确定支付
     */
    public void getSurePayInfo(int anchorId, int orderId) {
        L.e("============");
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", String.valueOf(anchorId))
                .put("targetId", String.valueOf(orderId))
                .build();
        addNet(service.getSurePayInfo(build).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult<OrderIdBean>>() {
            @Override
            public void _onNext(HttpResult<OrderIdBean> data) {
                L.e("============");
                if (data.isSuccess()) {
//                    getView().getSurePayInfo(data.data);
                } else {
//                    getView().getSurePayInfoFailure(data.description);
                }
            }

            @Override
            public void _onError(String msg) {
//                getView().getSurePayInfoFailure(msg);
                L.e("============" + msg);
            }
        }));
    }

    /**
     * 确定预约
     *
     * @isTomorrow 是否明天，0：否，默认；1：是
     */
    public void getAppointment(String anchorId, String appointTimeIds, String isTomorrow) {
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", anchorId)
                .put("appointTimeId", appointTimeIds)
                .put("isTomorrow", isTomorrow)
                .build();
        addNet(service.getAppointment(build).compose(handleResult()).subscribeWith(new LoadingObserver<SureAppointmentBean>() {
            @Override
            public void _onNext(SureAppointmentBean data) {
//                getView().setAppointmentSuccess(data);
            }

            @Override
            public void _onError(String msg) {
//                getView().setAppointmentFailure(msg);
            }
        }));
    }

    /**
     * 添加浏览记录 /api/bunko/2030013
     */
    public void addBrowingRecord(int anchorId, long times) {
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", String.valueOf(anchorId))
                .put("dwellTime", String.valueOf(times))
                .build();
        addNet(service.addBrowingRecord(build).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>(false) {
            @Override
            public void _onNext(HttpResult data) {
            }

            @Override
            public void _onError(String msg) {
                getView().toastTip(msg);
            }
        }));
    }

    /**
     * 分享
     */
    public void getShareInfo(int userId) {
        RequestBody build = new WenboParamsBuilder()
                .put("userId", String.valueOf(userId))
                .build();
        addNet(service.getShareInfo(build).compose(handleResult()).subscribeWith(new LoadingObserver<ShareInfoBean>() {
            @Override
            public void _onNext(ShareInfoBean data) {
                getView().getShareInfoSuccess(data);
            }

            @Override
            public void _onError(String msg) {
                if (TextUtils.isEmpty(msg)) return;
                getView().toastTip(msg);
            }
        }));
    }

    public void greetDiscuss(String userId, String content) {

        LoginApiService  service = new RetrofitUtils().createApi(LoginApiService.class);
        addNet(service.greetDiscuss(new ParamsBuilder()
                .put("acceptUserId", userId)
                .put("content", content).build())
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {

                    @Override
                    public void _onNext(HttpResult data) {
//                        getView().greet(content);

                        if (data.isSuccess()) {
                            getView().toastTip("打招呼成功");
                            getView().greet(content);
                        } else {
                            getView().toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    public void greetTipsList(int apage) {
        LoginApiService service = new RetrofitUtils().createWenboApi(LoginApiService.class);
        RequestBody build = new com.fengwo.module_comment.base.WenboParamsBuilder()
                .put("current", apage + "")
                .put("size", 10 + "")
                .build();
        compositeDisposable.add(service
                .greetTipsList(build)
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<GreetTipsBean>>() {
                    @Override
                    public void _onNext(BaseListDto<GreetTipsBean> data) {
                        List<String> dataList = StreamSupport.stream(data.records).map(e -> e.title).collect(Collectors.toList());
                        getView().setGreetTips(dataList, data.pages);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));
    }
}
