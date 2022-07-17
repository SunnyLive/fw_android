package com.fengwo.module_login.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.event.PaySuccessEvent;
import com.fengwo.module_comment.event.RechargeSuccessEvent;
import com.fengwo.module_comment.iservice.SysConfigService;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.SafeHandle;
import com.fengwo.module_live_vedio.BuildConfig;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.OrderDto;
import com.fengwo.module_login.mvp.dto.RechargeDto;
import com.fengwo.module_login.mvp.dto.WalletDto;
import com.fengwo.module_login.mvp.ui.adapter.ChongzhiAdapter;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.umentlib.Constants;
import com.fengwo.umentlib.PayUtils;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

@Route(path = ArouterApi.CHONG_ZHI)
public class ChongzhiActivity extends BaseMvpActivity {

    private String payNo;

    private final static int SDK_PAY_FLAG = 10000011;
    private final static String LAKALA_SYS_KEY = "LKLWX_PAY";
    @BindView(R2.id.tv_recharge_tips)
    TextView tvRechargeTips;
    @BindView(R2.id.recycleview)
    RecyclerView recycleview;
    @BindView(R2.id.tv_money)
    TextView tvMoney;
//    @BindView(R2.id.ll_wx)
//    ImageView ll_wx;
//    @BindView(R2.id.ll_zfb)
//    ImageView ll_zfb;
//    @BindView(R2.id.ll_yl)
//    ImageView ll_yl;

    @BindView(R2.id.ll_wx)
    LinearLayout ll_wx;
    @BindView(R2.id.ll_zfb)
    LinearLayout ll_zfb;
    @BindView(R2.id.ll_yl)
    LinearLayout ll_yl;

    UserInfo info;
    private ChongzhiAdapter chongzhiAdapter;

    private LoginApiService service;
    private String payWay = "ali";
    //private boolean isLakala = false;

    SafeHandle handle;
    CompositeDisposable disposables;
    @Autowired
    UserProviderService userService;
    @Autowired
    SysConfigService sysConfigService;


    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    private boolean isPayConfirm = false;
    @SuppressLint("HandlerLeak")
    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().setTitleColor(R.color.text_33)
                .setTitle("充值")
                .setBackIcon(R.drawable.ic_back_black)
                .showBack(true).build();
        info = UserManager.getInstance().getUser();
        disposables = new CompositeDisposable();
        handle = new SafeHandle(this) {
            @Override
            protected void mHandleMessage(Message msg) {
                super.mHandleMessage(msg);
                if (msg.what == 1) {
//                    updateWallet();
                } else if (msg.what == SDK_PAY_FLAG) {
                    String requestStatus = (String) msg.obj;
                    switch (requestStatus) {
                        case "9000":
//                            toastTip("支付成功");
                            RxBus.get().post(new RechargeSuccessEvent(1));
                            break;
                        case "6001":
                            toastTip("订单支付取消");
                            break;
                        case "4000":
                            toastTip("订单支付失败");
                            break;
                        case "8000":
                        case "6004":
                            toastTip("订单处理中，请稍后刷新");
                            break;
                    }
                }
            }
        };
        service = new RetrofitUtils().createApi(LoginApiService.class);
        tvMoney.setText(info.balance + "");
        ll_zfb.setSelected(true);
        getRechargeList();
        getSysConfig();
        disposables.add(RxBus.get().toObservable(PaySuccessEvent.class)
                .compose(bindToLifecycle())
                .subscribe(paySuccessEvent -> updateWallet()));
        disposables.add(RxBus.get().toObservable(RechargeSuccessEvent.class)
                .compose(bindToLifecycle())
                .subscribe(rechargeSuccessEvent -> {
                    if (TextUtils.isEmpty(payNo) || (payWay.equals("ali") && rechargeSuccessEvent.code != 1)) {
                        return;
                    }
                    if (!isPayConfirm) return;
                    isPayConfirm = false;
                    CommonDialog.getInstance("","是否支付完成？","已完成支付","未支付",false,true).addOnDialogListener(new CommonDialog.OnDialogListener() {
                        @Override
                        public void cancel() {
                            showLoadingDialog();
                            getWindow().getDecorView().postDelayed(()->{
                                hideLoadingDialog();
                                requestPayOrderState();
                            },5000);
                        }

                        @Override
                        public void sure() {

                        }
                    }).show(getSupportFragmentManager(),ChongzhiActivity.class.getName());
                }));
    }

    private void requestPayOrderState() {
        com.fengwo.module_comment.utils.PayUtils.notifyPayNo(payNo, new com.fengwo.module_comment.utils.PayUtils.PayCallback() {
            @Override
            public void onSuccess() {
                payNo = "";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.showAlertDialog(ChongzhiActivity.this, "支付结果", "谢谢您的支持，本次支付成功", "确定", new DialogUtil.AlertDialogBtnClickListener() {
                            @Override
                            public void clickPositive() {
                                ChongzhiActivity.this.finish();

                            }

                            @Override
                            public void clickNegative() {

                            }
                        });
//                                    Looper.loop();
                        RxBus.get().post(new PaySuccessEvent(""));
                    }
                });
            }

            @Override
            public void onFailed() {
                payNo = "";
                runOnUiThread(() -> {
                    DialogUtil.showAlertDialog(ChongzhiActivity.this, "支付结果", "预计5分钟到账，如还没到账请联系客服", "确定", null);
                });
            }
        });
    }

    private void updateWallet() {
        UserManager.getInstance().updateUserWallet()
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<WalletDto>>() {
                    @Override
                    public void _onNext(HttpResult<WalletDto> data) {
                        if (data.isSuccess()) {
                            Long huazun = data.data.preBalance + data.data.balance;
                            info.balance = huazun;
                            UserManager.getInstance().setUserInfo(info);
                            tvMoney.setText(huazun + "");
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        RxBus.get().post(new RechargeSuccessEvent());
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_chongzhi;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handle != null) {
            handle.removeMessages(SDK_PAY_FLAG);
            handle.removeMessages(1);
            handle = null;
        }
        disposables.dispose();
    }

    private void getRechargeList() {
        service.getRechargeList(1 + "," + PAGE_SIZE)
                .compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<List<RechargeDto>>(this) {
                    @Override
                    public void _onNext(List<RechargeDto> data) {
                        chongzhiAdapter = new ChongzhiAdapter(data);
                        recycleview.setLayoutManager(new GridLayoutManager(ChongzhiActivity.this, 2));
                        recycleview.setAdapter(chongzhiAdapter);
                        chongzhiAdapter.setEmptyView(getListEmptyView());
                        chongzhiAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                chongzhiAdapter.checkPosition = position;
                                chongzhiAdapter.notifyDataSetChanged();
                                if ("0".equals(payType) && Double.parseDouble(chongzhiAdapter.getData().get(position).price) > 3000) {
                                    ll_wx.setEnabled(false);
                                    if (ll_wx.isSelected()) {
                                        setSelectAli();
                                    }
                                } else if (!"0".equals(payType) && Double.parseDouble(chongzhiAdapter.getData().get(position).price) > 5000) {
                                    ll_wx.setEnabled(false);
                                    if (ll_wx.isSelected()) {
                                        setSelectAli();
                                    }
                                } else {
                                    ll_wx.setEnabled(true);
                                }
                            }
                        });
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }
    private String payType;
    private void pay(int id, String title, String content, String payWay) {

        String thirdPayWay = "app";
        if ("yeepay".equals(payWay))
            thirdPayWay = "yeepay";
        else if ("xibwx".equals(payWay))
            thirdPayWay = "wxapp";

        Map params = new HashMap();
        params.put("title", title);
        params.put("content", content);
        params.put("thirdPayWay", thirdPayWay);
        params.put("thirdType", payWay);

        payNo = "";
        service.getOrder(id, HttpUtils.createRequestBody(params))
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult<OrderDto>>(this) {
                    @Override
                    public void _onNext(HttpResult<OrderDto> data) {
                        if (data.isSuccess()) {
                            payNo = data.data.orderNo;
                            if ("wx".equals(payWay)) {
                                wxPay(data.data.payString);
                            } else if ("ali".equals(payWay)) {
                                aliPay(data.data.payString);
                            } else if ("yeepay".equals(payWay)) {
                                yeepay(data.data.payString);
                            } else if ("lklwx".equals(payWay)) {
                                lakalaPay(data.data.payString);
                            } else if ("xibwx".equals(payWay)) {
                                xibPay(data.data.payString);
                            }
                        } else {
                            toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    private void getSysConfig() {
        sysConfigService.getSysConfig(LAKALA_SYS_KEY, new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                if (data.isSuccess()) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(data.data.toString());
//                        isLakala = (payType = jsonObject.getString(LAKALA_SYS_KEY)).equals("1");
//                        tvRechargeTips.setText(isLakala ? "充值方式(微信支付单日充值限额20万，单笔5000元)" : "充值方式(微信支付日限额3000元)");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                    try {
                        JSONObject jsonObject = new JSONObject(data.data.toString());
                        payType = jsonObject.getString(LAKALA_SYS_KEY);
                        String text = "充值方式(微信支付日限额3000元)";
                        switch (payType){
                            case "0":
                                break;
                            case "1":
                                text = "充值方式(微信支付单日充值限额20万，单笔5000元)";
                                break;
                            case "2":
                                //todo 文案还没有确认
                                text = "充值方式(微信支付单日充值限额20万，单笔3000元)";
                                break;

                        }
                        tvRechargeTips.setText(text);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void _onError(String msg) {

            }
        });
    }

    private void yeepay(String data) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(data);
        intent.setData(content_url);
        startActivity(intent);
    }

    private void aliPay(String data) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(ChongzhiActivity.this);
                Map<String, String> result = alipay.payV2(data, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result.get("resultStatus");
                if (handle != null)
                    handle.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void wxPay(String data) {
        try {
            PayUtils.wxPay(this, new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     *  厦门国际银行支付
     * @param orderNo 支付订单号
     * {@link #pay}
     *
     */
    private void xibPay(String orderNo){

        IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.WXAPP_ID);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = com.fengwo.module_comment.Constants.WX_MINI_PRO_ORIGIN_ID; // 填小程序原始id
        StringBuilder sb = new StringBuilder();
        sb.append("sqtg_sun/pages/home/pay/bosspay?")
                .append("userToken=")
                .append(UserManager.getInstance().getToken())
                .append("&appid=")
                .append(com.fengwo.module_comment.Constants.WX_MINI_PRO_APP_ID)
                .append("&orderNo=")
                .append(orderNo);

        req.path = sb.toString();                 ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        if (BuildConfig.DEBUG) req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;
        api.sendReq(req);
    }



    private void lakalaPay(String token) {
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.WXAPP_ID);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = com.fengwo.module_comment.Constants.WX_MINI_PRO_ORIGIN_ID; // 填小程序原始id
        req.path = com.fengwo.module_comment.Constants.WX_MINI_PRO_PATH + token + "&appid=" +
                com.fengwo.module_comment.Constants.WX_MINI_PRO_APP_ID;                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        KLog.e("lgl_lakala:", req.path);
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }

    @OnClick({R2.id.ll_wx, R2.id.ll_zfb, R2.id.ll_yl, R2.id.btn_submit})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_submit) {
            if (chongzhiAdapter == null) return;
            RechargeDto dto = chongzhiAdapter.getSelected();
            String title = "购买" + dto.diamondNum + "花钻";
            String content = "赠送" + dto.diamondGive + "花钻";
            if (payWay.equals("wx")) {
                if ("1".equals(payType))
                    payWay = "lklwx";
                else if ("2".equals(payType))
                    payWay = "xibwx";
            }
            pay(dto.id, title, content, payWay);
            isPayConfirm = true;
        } else if (id == R.id.ll_wx) {
            payWay = "wx";
            ll_wx.setSelected(true);
            ll_wx.setBackgroundResource(R.drawable.bg_recharge_ture);
            ll_zfb.setSelected(false);
            ll_zfb.setBackgroundResource(0);
            ll_yl.setSelected(false);
            ll_yl.setBackgroundResource(0);
        } else if (id == R.id.ll_zfb) {

            setSelectAli();
        } else if (id == R.id.ll_yl) {
            payWay = "yeepay";
            ll_wx.setSelected(false);
            ll_wx.setBackgroundResource(0);
            ll_zfb.setSelected(false);
            ll_zfb.setBackgroundResource(0);
            ll_yl.setSelected(true);
            ll_yl.setBackgroundResource(R.drawable.bg_recharge_ture);
        }
    }

    private void setSelectAli() {
        payWay = "ali";
        ll_wx.setSelected(false);
        ll_wx.setBackgroundResource(0);
        ll_zfb.setSelected(true);
        ll_zfb.setBackgroundResource(R.drawable.bg_recharge_ture);
        ll_yl.setSelected(false);
        ll_yl.setBackgroundResource(0);
    }

}
