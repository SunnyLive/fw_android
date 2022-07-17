package com.fengwo.module_main.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

import com.fengwo.module_chat.base.RefreshMessageListEvent;
import com.fengwo.module_chat.base.RefreshUnReadMessageEvent;
import com.fengwo.module_chat.mvp.ui.activity.ChatGroupInfoActivity;
import com.fengwo.module_chat.utils.chat_new.ChatGreenDaoHelper;
import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.DictsBean;
import com.fengwo.module_comment.event.UndateUnReadMessageEvent;
import com.fengwo.module_comment.event.VisitorRecordEvent;
import com.fengwo.module_comment.utils.AppUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.DeviceUtils;
import com.fengwo.module_comment.utils.DownloadHelper;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.utils.SafeHandle;
import com.fengwo.module_flirt.UI.activity.ChatRoomActivity;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.ClientReconnentDto;
import com.fengwo.module_flirt.bean.ReconWbBean;
import com.fengwo.module_flirt.dialog.HeaderPopWindow;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_main.mvp.IView.IMainView;
import com.fengwo.module_main.mvp.activity.MainActivity;
import com.fengwo.module_comment.bean.VensionDto;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;

public class MainPresenter extends BaseMainPresenter<IMainView> {

    private final ChatGreenDaoHelper daoHelper;

    private int giftCount;
    private List<String> giftData;

    private SafeHandle handle;

    public MainPresenter() {
        daoHelper = ChatGreenDaoHelper.getInstance();
    }

    @Override
    public void attachView(IMainView mvpView) {
        super.attachView(mvpView);
        handle = new SafeHandle((Activity) mvpView) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    if (giftCount > 0 && giftCount == DownloadHelper.downCount()) {
                        handle.removeMessages(1);
                    } else {
                        if (CommentUtils.isListEmpty(giftData)) {
                            getGiftList();
                        } else {
                            DownloadHelper.download(giftData);
                        }
                    }
                }
            }
        };
    }


    public void getUnReadMessageCount(int uid) {
        if (TextUtils.isEmpty(String.valueOf(uid))) {
            getView().toastTip("用户id为空，请重试");
            return;
        }
        String groundIds = (String) SPUtils1.get(BaseApplication.mApp, ChatGroupInfoActivity.SP_NO_DISTURB, "");
        addNet(daoHelper.getUnReadMessageCount(uid, groundIds).subscribe(chatListItemEntities -> {
            UndateUnReadMessageEvent.unReadMessageCount = chatListItemEntities;
            RxBus.get().post(new UndateUnReadMessageEvent(chatListItemEntities));
        }));
    }

    public void getAppVension() {

        addNet(service.getAppVension()
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<VensionDto>() {
                    @Override
                    public void _onNext(VensionDto data) {
                        getView().setVension(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    public void loginlog() {
        String appVersion = AppUtils.getVersionName((MainActivity) getView());
        addNet(service.loginlog(
                new ParamsBuilder()
                        .put("appVersion", appVersion)
                        .put("loginIp", AppUtils.getIPAddress((MainActivity) getView()))
                        .put("mobileType", DeviceUtils.getModel())
                        .put("mobileVersion", DeviceUtils.getSDKVersionName()).build()).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {

            }

            @Override
            public void _onError(String msg) {

            }
        }));
    }

    public void userOfflineLog() {
        addNet(service.userOfflineLog().compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {

            }

            @Override
            public void _onError(String msg) {

            }
        }));
    }

    public void addVisitorRecord(VisitorRecordEvent recordEvent) {
        addNet(service.addVisitorRecord(new ParamsBuilder()
                .put("beUserId", recordEvent.beUserId + "")
                .put("stayTime", recordEvent.stayTime + "")
                .build())
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
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
     * 主播断线重连
     */
    public void wenboAnchor() {
        addNet(new RetrofitUtils().createWenboApi(FlirtApiService.class).reconnec().compose(handleResult())
                .subscribeWith(new LoadingObserver<ReconWbBean>() {
                    @Override
                    public void _onNext(ReconWbBean data) {
                        getView().reconnec(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }

    /**
     * 主播拒绝重连
     */
    public void rejectReconnect() {
        addNet(new RetrofitUtils().createWenboApi(FlirtApiService.class).rejectReconnec().compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }

    /**
     * 用户断线重连
     */
    public void wenboIsReconnent() {

        addNet(
                new RetrofitUtils().createWenboApi(FlirtApiService.class).clientReconnent().compose(handleResult())
                        .subscribeWith(new LoadingObserver<ClientReconnentDto>() {
                            @Override
                            public void _onNext(ClientReconnentDto data) {
                                L.e("====断线重连" + data.getAnchorId());
                                L.e("====断线重连" + data.getContent());
                                if (data.getAnchorId() <= 0)
                                    return;
                                HeaderPopWindow headerPopWindow = new HeaderPopWindow((Activity) getView(), "", "断线重连", data.getContent(), "重新连接", "放弃");
                                headerPopWindow.setOutSideDismiss(true);
                                headerPopWindow.setOnBtnClickListener(new HeaderPopWindow.OnBtnClickListener() {
                                    @Override
                                    public void sure() {
                                    }

                                    @Override
                                    public void cancle() {
                                        ChatRoomActivity.startWait((Activity) getView(), data.getAnchorId(), "", 0 );
                                    }
                                });
                                headerPopWindow.showPopupWindow();
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        })
        );
    }

    public void getWenboTip() {
        getSystemConfiguration("WenBoLivingRoomTip");
        //getSystemConfiguration("WENBO_ANCHOR_GUIDE");
    }

    /**
     * 获取系统文案配置信息
     * @param key
     */
    public void getSystemConfiguration(String key) {
        addNet(new RetrofitUtils().createApi(FlirtApiService.class).getDicts(key)
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<List<DictsBean>>() {
                    @Override
                    public void _onNext(List<DictsBean> data) {
                        StringBuilder builder = new StringBuilder();
                        List<String> values = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            builder.append(data.get(i).getValue());
                            if (i < data.size() - 1) {
                                builder.append("\n");
                            }
                            values.add(data.get(i).getValue());

                        }
                        switch (key) {
                            case "WenBoLivingRoomTip":
                                SPUtils1.put((Context) getView(), "wenbotip", builder.toString());
                                break;
                            case "WENBO_ANCHOR_GUIDE": //i撩 运营提示语句
                                SPUtils1.put((Context) getView(), "wenboopt", builder.toString());
                                break;
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                })
        );
    }

    public void getGiftList() {
        addNet(Flowable.zip(service.getGiftList(), new RetrofitUtils().createWenboApi(FlirtApiService.class).getGiftList(), new BiFunction<HttpResult<List<String>>, HttpResult<List<String>>, List<String>>() {
            @Override
            public List<String> apply(HttpResult<List<String>> listHttpResult, HttpResult<List<String>> listHttpResult2) throws Exception {
                List<String> gifts = new ArrayList<>();
                gifts.addAll(listHttpResult.data);
                gifts.addAll(listHttpResult2.data);
                return gifts;
            }
        }).compose(io_main())
                .subscribeWith(new LoadingObserver<List<String>>() {
                    @Override
                    public void _onNext(List<String> data) {
                        DownloadHelper.download(data);
                        giftCount = data.size();
                        giftData = data;
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
        if (!handle.hasMessages(1))
            handle.sendEmptyMessageDelayed(1, 10000);
    }

    /**
     * 上传经纬度
     */
    public void upLoading(String latitude, String longitude, String city) {
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
                        getView().hideLoadingDialog();
                    }

                    @Override
                    public void _onError(String msg) {
                        L.e("更新地址失败 " + msg);
                        getView().hideLoadingDialog();
                    }
                });
//        RequestBody build = new WenboParamsBuilder()
//                .put("latitude ", latitude)
//                .put("longitude ", longitude)
//                .build();
//
//        LoginApiService service = new RetrofitUtils().createApi(LoginApiService.class);
//        addNet(service.putLnglat(build).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
//            @Override
//            public void _onNext(HttpResult data) {
//            }
//
//            @Override
//            public void _onError(String msg) {
//                L.e("=====msg " + msg);
//            }
//        }));
    }

    public void buildMessageListFirstInstall(int uid) {
        addNet(daoHelper.buildMessageListFirstInstall(uid + "").subscribe(aBoolean -> {
            SPUtils1.put((Context) getView(), "buildMessageListFirstInstall" + uid, false);
            //刷新消息列表
            RxBus.get().post(new RefreshMessageListEvent());
            //刷新未读消息
            RxBus.get().post(new RefreshUnReadMessageEvent());
        }));
    }
}
