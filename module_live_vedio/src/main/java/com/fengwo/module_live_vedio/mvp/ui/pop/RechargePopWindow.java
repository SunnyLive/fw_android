package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.event.PaySuccessEvent;
import com.fengwo.module_comment.event.RechargeSuccessEvent;
import com.fengwo.module_comment.iservice.SysConfigService;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.SafeHandle;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.BuildConfig;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.OrderDto;
import com.fengwo.module_live_vedio.mvp.dto.RechargeDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.RechargeAdapter;
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

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;
import razerdp.basepopup.BasePopupWindow;

public class RechargePopWindow extends BasePopupWindow {

    private final static int SDK_PAY_FLAG = 10000111;
    private final static String LAKALA_SYS_KEY = "LKLWX_PAY";

    private RecyclerView rv;
    private TextView tvHuazun;
    private TextView btnSubmit;
    private TextView tvRechargeTips;
    private Context mContext;
    //    private ImageView ivClose;
    RechargeAdapter mAdapter;
    private ImageView iv_recharge_wx, iv_recharge_ali, iv_recharge_yeepay;
    private String payWay = "ali";
    SafeHandle handle;
    private String payNo;
    CompositeDisposable disposables;

    @Autowired
    SysConfigService sysConfigService;
    private String payType;

    @Autowired
    UserProviderService mUserInfoService;

    public RechargePopWindow(Context context, List<RechargeDto> data) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        ARouter.getInstance().inject(this);
        disposables = new CompositeDisposable();
        mContext = context;
        rv = findViewById(R.id.rv);
        getSysConfig();
        tvHuazun = findViewById(R.id.tv_huazuan);
        tvRechargeTips =findViewById(R.id.tv_recharge_tips);
//        ivClose = findViewById(R.id.iv_close);
        rv.setLayoutManager(new GridLayoutManager(context, 3));
        mAdapter = new RechargeAdapter(data);
        rv.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.checkPosition = position;
                mAdapter.notifyDataSetChanged();
                if ("0".equals(payType) && mAdapter.getData().get(position).price > 3000) {
                    iv_recharge_wx.setEnabled(false);
                    if (iv_recharge_wx.isSelected()) {
                        setSelectAli();
                    }
                } else if (!"0".equals(payType) && mAdapter.getData().get(position).price > 5000) {
                    iv_recharge_wx.setEnabled(false);
                    if (iv_recharge_wx.isSelected()) {
                        setSelectAli();
                    }
                }else {
                    iv_recharge_wx.setEnabled(true);
                }
            }
        });

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(v -> {
            RechargeDto dto = mAdapter.getSelected();
            String title = "购买" + dto.diamondNum + "花钻";
            String content = "赠送" + dto.diamondGive + "花钻";
            if (payWay.equals("wx")){
                if ("1".equals(payType))
                    payWay = "lklwx";
                else if ("2".equals(payType))
                    payWay = "xibwx";
            }
            pay(dto.id, title, content, payWay);
        });
        findViewById(R.id.iv_cloase).setOnClickListener(v -> dismiss());
        iv_recharge_wx = findViewById(R.id.iv_recharge_wx);
        iv_recharge_ali = findViewById(R.id.iv_recharge_ali);
        iv_recharge_yeepay = findViewById(R.id.iv_recharge_yeepay);
        iv_recharge_ali.setSelected(true);
        iv_recharge_wx.setOnClickListener(v -> {
            payWay = "wx";
            iv_recharge_wx.setSelected(true);
            iv_recharge_ali.setSelected(false);
            iv_recharge_yeepay.setSelected(false);
        });
        iv_recharge_ali.setOnClickListener(v -> {
            setSelectAli();
        });
        iv_recharge_yeepay.setOnClickListener(v -> {
            payWay = "yeepay";
            iv_recharge_wx.setSelected(false);
            iv_recharge_ali.setSelected(false);
            iv_recharge_yeepay.setSelected(true);
        });

        handle = new SafeHandle((Activity) mContext) {
            @Override
            protected void mHandleMessage(Message msg) {
                super.mHandleMessage(msg);
                if (msg.what == 1) {

                } else if (msg.what == SDK_PAY_FLAG) {
                    String requestStatus = (String) msg.obj;
                    switch (requestStatus) {
                        case "9000":
//                            ToastUtils.showShort(mContext, "支付成功");
                            RxBus.get().post(new RechargeSuccessEvent(1));
                            break;
                        case "4000":
                            ToastUtils.showShort(mContext, "订单支付失败");
                            break;
                        case "6001":
                            ToastUtils.showShort(mContext, "订单支付取消");
                            break;
                        case "8000":
                        case "6004":
                            ToastUtils.showShort(mContext, "订单处理中，请稍后刷新");
                            break;
                    }
                }
            }
        };
        disposables.add(RxBus.get().toObservable(RechargeSuccessEvent.class)
                .subscribe(rechargeSuccessEvent -> {
                    if (TextUtils.isEmpty(payNo)||(payWay.equals("ali")&&rechargeSuccessEvent.code !=1)){
                        return;
                    }
                    CommonDialog.getInstance("","是否支付完成？","已完成支付","未支付",false,true).addOnDialogListener(new CommonDialog.OnDialogListener() {
                        @Override
                        public void cancel() {
                            ((BaseActivity)mContext).showLoadingDialog();
                            getContentView().postDelayed(() -> {
                                ((BaseActivity)mContext).hideLoadingDialog();
                                com.fengwo.module_comment.utils.PayUtils.notifyPayNo(payNo, new com.fengwo.module_comment.utils.PayUtils.PayCallback() {
                                    @Override
                                    public void onSuccess() {
                                        RxBus.get().post(new PaySuccessEvent(""));
                                        ((Activity) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                DialogUtil.showAlertDialog((Activity) context, "支付结果", "谢谢您的支持，本次支付成功", "确定", null);
                                                payNo = "";
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailed() {
                                        ((Activity) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                DialogUtil.showAlertDialog((Activity) context, "支付结果", "预计5分钟到账，如还没到账请联系客服", "确定", null);
                                                payNo = "";
                                            }
                                        });
                                    }
                                });

                            },5000);

                        }

                        @Override
                        public void sure() {

                        }
                    }).show(((FragmentActivity) context).getSupportFragmentManager(),
                            RechargePopWindow.class.getName());


                }));

    }
    private void getSysConfig() {
        sysConfigService.getSysConfig(LAKALA_SYS_KEY, new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                if (data.isSuccess()){
//                    try {
//                        JSONObject jsonObject = new JSONObject(data.data.toString());
//                        isLakala = jsonObject.getString(LAKALA_SYS_KEY).equals("1");
//                        tvRechargeTips.setText(isLakala?"充值方式(微信支付单日充值限额20万，单笔5000元)":"充值方式(微信支付日限额3000元)");
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
    private void setSelectAli() {
        payWay = "ali";
        iv_recharge_wx.setSelected(false);
        iv_recharge_ali.setSelected(true);
        iv_recharge_yeepay.setSelected(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handle != null) {
            handle.removeMessages(SDK_PAY_FLAG);
            handle = null;
        }
        if (disposables!=null){
            disposables.dispose();
        }
    }


    public void setHuazuan(long huazuan) {
        tvHuazun.setText(huazuan + "");
    }

    @Override
    public void dismiss() {
        super.dismiss();
        payNo = "";
    }

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
        new RetrofitUtils().createApi(LiveApiService.class).getOrder(id, HttpUtils.createRequestBody(params))
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult<OrderDto>>() {
                    @Override
                    public void _onNext(HttpResult<OrderDto> data) {
                        try {
                            if (data.isSuccess()) {
                                payNo = data.data.orderNo;
                                if ("wx".equals(payWay)) {
                                    wxPay(data.data.payString);
                                } else if ("ali".equals(payWay)) {
                                    aliPay(data.data.payString);
                                } else if ("yeepay".equals(payWay)){
                                    yeepay(data.data.payString);
                                }else if ("lklwx".equals(payWay)){
                                    lakalaPay(data.data.payString);
                                } else if ("xibwx".equals(payWay)) {
                                    xibPay(data.data.payString);
                                }
                            } else {
                                ToastUtils.showShort(mContext, data.description);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
//        Map map = new HashMap();
//        map.put("title", title);
//        map.put("content", content);
//        map.put("thirdPayWay", thirdPayWay);
//        map.put("tradeType", "WECHATAPP");
//        switch (payType){
//            case "0":
//                map.put("thirdType", payWay);
//                break;
//            case "1":
//                map.put("thirdType", payWay);
//                break;
//            case "2":
//                map.put("thirdType", "xibwx");
//                map.put("thirdPayWay", "wxapp");
//                break;
//        }
//        new RetrofitUtils().createApi(LiveApiService.class).getOrder(id, createRequestBody(map))
//                .compose(RxUtils.applySchedulers())
//                .subscribe(new LoadingObserver<HttpResult<OrderDto>>() {
//                    @Override
//                    public void _onNext(HttpResult<OrderDto> data) {
//                        try {
//                            if (data.isSuccess()) {
//                                payNo = data.data.orderNo;
//                                if ("wx".equals(payWay)) {
//                                    wxPay(data.data.payString);
//                                } else if ("ali".equals(payWay)) {
//                                    aliPay(data.data.payString);
//                                } else if ("yeepay".equals(payWay)){
//                                    yeepay(data.data.payString);
//                                }else if ("lklwx".equals(payWay)){
//                                    lakalaPay(data.data.payString);
//                                }
//                            } else {
//                                ToastUtils.showShort(mContext, data.description);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void _onError(String msg) {
//
//                    }
//                });
    }

    /**
     *  厦门国际银行支付
     * @param orderNo 支付的订单id
     * {@link #pay}
     *
     */
    private void xibPay(String orderNo){

        IWXAPI api = WXAPIFactory.createWXAPI(mContext, Constants.WXAPP_ID);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = com.fengwo.module_comment.Constants.WX_MINI_PRO_ORIGIN_ID; // 填小程序原始id
        StringBuilder sb = new StringBuilder();
        sb.append("sqtg_sun/pages/home/pay/bosspay?")
                .append("userToken=")
                .append(mUserInfoService.getToken())
                .append("&appid=")
                .append(com.fengwo.module_comment.Constants.WX_MINI_PRO_APP_ID)
                .append("&orderNo=")
                .append(orderNo);

        req.path = sb.toString();                 ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        if (BuildConfig.DEBUG) req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;
        api.sendReq(req);
    }


    private void lakalaPay(String token){
        IWXAPI api = WXAPIFactory.createWXAPI(mContext, Constants.WXAPP_ID);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = com.fengwo.module_comment.Constants.WX_MINI_PRO_ORIGIN_ID; // 填小程序原始id
        req.path = com.fengwo.module_comment.Constants.WX_MINI_PRO_PATH+token+"&appid="+
                com.fengwo.module_comment.Constants.WX_MINI_PRO_APP_ID;                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
//        KLog.e("lgl_lakala:",req.path);
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }

    private void yeepay(String data) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(data);
        intent.setData(content_url);
        mContext.startActivity(intent);
    }

    private void aliPay(String data) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask((Activity) mContext);
                Map<String, String> result = alipay.payV2(data, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result.get("resultStatus");
                if (handle == null) return;
                handle.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void wxPay(String data) {
        try {
            PayUtils.wxPay(mContext, new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_recharge);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }


}
