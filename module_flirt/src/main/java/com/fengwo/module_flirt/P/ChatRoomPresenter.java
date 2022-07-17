package com.fengwo.module_flirt.P;

import android.text.TextUtils;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.bean.JumpInvtationDataBean;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_comment.bean.ReceiveSocketBean;
import com.fengwo.module_comment.event.PaySuccessEvent;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_flirt.bean.EnterRoomBean;
import com.fengwo.module_flirt.bean.GetAnchorRoomInfo;
import com.fengwo.module_flirt.bean.GiftDto;
import com.fengwo.module_flirt.bean.ImpressDTO;
import com.fengwo.module_flirt.bean.MessageListVO;
import com.fengwo.module_flirt.bean.OrderNumDto;
import com.fengwo.module_flirt.bean.ShareInfoBean;
import com.fengwo.module_flirt.bean.TimeGiftResponse;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.bean.WenboGiftMsg;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.RequestBody;

/**
 * 放 聊天室 接口
 *
 * @Author BLCS
 * @Time 2020/4/6 11:51
 */
public class ChatRoomPresenter extends ChatRoomSocketPresenter {

    /**
     * 获取加时次数
     */
    public void getAddTimeNum(int anchorId, boolean clickAdd) {
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", String.valueOf(anchorId))
                .build();
        addNet(service.getAddTimeNum(build).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult<OrderNumDto>>(getView()) {
            @Override
            public void _onNext(HttpResult<OrderNumDto> data) {
                if (data.isSuccess()) {
                    getView().getOrderNum(data.data.orderNum, clickAdd);
                } else {
                    getView().toastTip(data.description);
                }
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
     * 获取主播房间信息  （/api/bunko/2030023）
     */
    public void getAnchorRoomInfo(long anchorId) {
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", String.valueOf(anchorId))
                .build();
        addNet(service.getAnchorRoomInfo(build).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult<GetAnchorRoomInfo>>(getView()) {
            @Override
            public void _onNext(HttpResult<GetAnchorRoomInfo> data) {
                if (data.isSuccess()) {
                    getView().getAnchorRoomInfoSuccess(data.data);
                } else {
                    getView().getAnchorRoomInfoFailure(data.description);
                }
            }

            @Override
            public void _onError(String msg) {
                getView().getAnchorRoomInfoFailure(msg);
                if (!TextUtils.isEmpty(msg)) {
                    getView().toastTip(msg);
                }
            }
        }));

    }

    /**
     * 进入直播间
     */
    public void enterRoom(long anchorId) {
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", String.valueOf(anchorId))
//                .put("orderType", String.valueOf(orderType))//1.点单 2.预约单
//                .put("orderId", String.valueOf(orderId))
                .build();
        addNet(service.getEnterRoom(build).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult<EnterRoomBean>>(getView()) {
            @Override
            public void _onNext(HttpResult<EnterRoomBean> data) {
                if (data.isSuccess()) {
                    getView().enterRoomSuccess(data.data);
                } else {
                    getView().enterRoomFailure(data.description);
                }
            }

            @Override
            public void _onError(String msg) {
                getView().enterRoomFailure(msg);
//                if (!TextUtils.isEmpty(msg)) {
//                    getView().toastTip(msg);
//                }
            }
        }));
    }


    /**
     * 退出直播间
     * @param anchorId
     * @param gotoLive  退出直播间时 0 默认不缘分值 1 清除
     */
    public void quitRoom(String anchorId, int gotoLive) {
        RequestBody build = new WenboParamsBuilder()
                .put("roomId", anchorId)
                .put("goToLive", gotoLive + "")
                .build();
        addNet(service.quitRoom(build).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                getView().quitRoomSuccess(data, false);
            }

            @Override
            public void _onError(String msg) {
                getView().quitRoomFailure(msg);
                if (!TextUtils.isEmpty(msg)) {
                    getView().toastTip(msg);
                }
            }
        }));
    }
    public void quitRoomOut(String anchorId, int gotoLive) {
        RequestBody build = new WenboParamsBuilder()
                .put("roomId", anchorId)
                .put("goToLive", gotoLive + "")
                .build();
        addNet(service.quitRoom(build).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                getView().quitRoomSuccessOut();
            }

            @Override
            public void _onError(String msg) {
                getView().quitRoomFailure(msg);
                if (!TextUtils.isEmpty(msg)) {
                    getView().toastTip(msg);
                }
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

    /**
     * 关播
     */
    public void pushEnd(JumpInvtationDataBean jumpInvtationDataBean) {
        addNet(service.pushEnd().compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                getView().hostEnd(data,jumpInvtationDataBean);
            }

            @Override
            public void _onError(String msg) {
                getView().toastTip(msg);
            }
        }));
    }

    /**
     * 主播接单/拒接
     *
     * @param type 0-拒接,1-接单
     */
    public void anchorAddTimeOrderReceive(String consNo, int type) {
        RequestBody build = new WenboParamsBuilder()
                .put("consNo", String.valueOf(consNo))
                .put("type", String.valueOf(type))
                .build();
        addNet(service.anchorAddTimeOrderReceive(build).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                getView().orderReceiveSuccess(data);
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
     * 主播接单/拒接
     *
     * @param type 0-拒接,1-接单
     */
    public void anchorOrderReceive(String consNo, int type) {
        RequestBody build = new WenboParamsBuilder()
                .put("consNo", String.valueOf(consNo))
                .put("type", String.valueOf(type))
                .build();
        addNet(service.anchorOrderReceive(build).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                getView().orderReceiveSuccess(data);
            }

            @Override
            public void _onError(String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    getView().toastTip(msg);
                }
            }
        }));
    }

    /*发送普通礼物*/
    public void sendNormalGift(int anchorId, GiftDto giftDto) {
        addNet(service.sendNormalGift(new WenboParamsBuilder()
                .put("anchorId", anchorId + "")
                .put("giftId", giftDto.id + "")
                .put("quantity", 1).build())
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>(getView()) {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            showGiftIfsend(giftDto, false);
                            RxBus.get().post(new PaySuccessEvent(""));
                        }

                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    /*发送加时礼物*/
    public void sendTimeGift(int anchorId, GiftDto gift) {
        addNet(service.sendTimeGift(new WenboParamsBuilder()
                .put("anchorId", anchorId + "")
                .put("targetId", gift.id + "").build())
                .compose(io_main())
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<TimeGiftResponse>(getView()) {
                    @Override
                    public void _onNext(TimeGiftResponse data) {
                        if (data.getType() == 2) {
                            getView().updatetime(data.getExpireTime());
                            getView().isWaitStatus(gift.giftName);
                        } else {
                            getView().addTimeTip(data.getMsg());
                        }
                        RxBus.get().post(new PaySuccessEvent(""));
                        showGiftIfsend(gift, true);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (TextUtils.isEmpty(msg)) return;
                        getView().addTimeTip(msg);
                    }
                }));
    }

    /*显示礼物信息*/
    private void showGiftIfsend(GiftDto giftDto, boolean istype) {
        WenboGiftMsg msg = new WenboGiftMsg();
        WenboGiftMsg.Gift gift = new WenboGiftMsg.Gift();
        gift.setBigImgPath(giftDto.bigImgPath);
        gift.setGiftName(giftDto.giftName);
        gift.setGiftPrice(giftDto.price + "");
        gift.setSmallImgPath(giftDto.smallImgPath);
        gift.setCharmValue(giftDto.charmValue);
        gift.setGears(istype);
        msg.setGift(gift);
        WenboGiftMsg.User user = new WenboGiftMsg.User();
        UserInfo myUserInfo = UserManager.getInstance().getUser();
        user.setNickname(myUserInfo.nickname);
        user.setHeadImg(myUserInfo.headImg);
        msg.setUser(user);
        getView().addGift(msg, istype);
    }

    /**
     * 查是否关注
     */
    public void isAttention(int uid) {
        LiveApiService service = new RetrofitUtils().createApi(LiveApiService.class);
        addNet(service.isAttention(uid)
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (null == getView()) return;
                        if (data.isSuccess()) {
                            String stringData = new Gson().toJson(data.data);
                            getView().isAttention(stringData);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                }));
    }

    /*断开连接*/
    public void rejectReconnect() {
        addNet(service.rejectReconnec().compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
//                        getView().reconnec(data);
                            }

                            @Override
                            public void _onError(String msg) {
                                getView().toastTip(msg);
                            }
                        })
        );
    }

    /**
     * 取消订单
     *
     * @param anchorId
     * @param orderId
     */
    public void cancelOrder(int anchorId, long orderId) {
        addNet(service.cancleOrder(new WenboParamsBuilder()
                .put("anchorId", anchorId + "")
                .put("conId", orderId + "").build())
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>(getView()) {
                    @Override
                    public void _onNext(HttpResult data) {

                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));
    }

    @Override
    public void acceptOrder(ReceiveSocketBean bean) {
        int accept = bean.getResult().getAccept();
        L.e("=====" + bean.getAnchor().getUserId());
        if (accept == 1) {//接受
            getView().acceptOrder(bean);
            enterRoom(Integer.parseInt(bean.getAnchor().getUserId()));//进入直播间
            isAttention(Integer.parseInt(bean.getAnchor().getUserId()));
        } else {//拒绝
            getView().refuseOrder(bean.getContent().getValue());
        }
    }

    /**
     * 我的点单列表 心动小屋列表
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
     * 点单加时 加单
     *
     * @param anchorId
     * @param gift
     */
    public void sendOrderTimeGift(int anchorId, GiftDto gift) {
        addNet(service.orderGiftTime(new WenboParamsBuilder()
                .put("anchorId", anchorId + "")
                .put("targetId", gift.id + "").build())
                .compose(io_main())
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<TimeGiftResponse>(getView()) {
                    @Override
                    public void _onNext(TimeGiftResponse data) {
                        getView().isWaitStatus(data.getMsg());
                        RxBus.get().post(new PaySuccessEvent(""));
                        showGiftIfsend(gift, true);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (TextUtils.isEmpty(msg)) return;
                        getView().addTimeTip(msg);
                    }
                }));
    }

    /**
     * 获取观看的用户列表
     */
    public void getChatUsers() {
        addNet(service.getChatUsers(new WenboParamsBuilder().build())
                .compose(io_main())
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<List<MessageListVO>>(false) {
                    @Override
                    public void _onNext(List<MessageListVO> data) {
                        getView().onGetChatUsers(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (TextUtils.isEmpty(msg)) return;
                    }
                }));
    }

    public void getImpressionValue(int userId) {
        addNet(service.getImpress(new WenboParamsBuilder()
                .put("userId", userId)
                .build())
                .compose(io_main())
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<ImpressDTO>(false) {
                    @Override
                    public void _onNext(ImpressDTO data) {
                        getView().onGetImpress(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (TextUtils.isEmpty(msg)) return;
                        getView().addTimeTip(msg);
                    }
                })
        );
    }

}
