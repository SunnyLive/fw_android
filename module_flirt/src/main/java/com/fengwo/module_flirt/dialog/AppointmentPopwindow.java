package com.fengwo.module_flirt.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.FastClickListener;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.adapter.AppointmentTimeAdapter;
import com.fengwo.module_flirt.bean.AppointTimes;
import com.fengwo.module_flirt.bean.PeriodPrice;
import com.fengwo.module_login.mvp.ui.activity.ChongzhiActivity;
import com.fengwo.module_login.utils.UserManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * 付费预约弹窗
 */
public class AppointmentPopwindow extends BasePopupWindow  {

    private final AppointTimes records;
    public String timeIds;
    public int Allprice;
    @Autowired
    UserProviderService userProviderService;
    private TextView tvMoney;
    private String isTomorrow ="1" ;
    private PeriodPrice periodPrice;

    public AppointmentPopwindow(Context context, AppointTimes records) {
        super(context);
        this.records = records;
        ArouteUtils.inject(this);
        setPopupGravity(Gravity.BOTTOM);
        initUI();
        handleBalance();
    }
    /**
     * 余额处理
     */
    private void handleBalance() {
        userProviderService.updateWallet(integer -> {
            UserInfo userInfo = userProviderService.getUserInfo();
            userInfo.balance = integer;
            tvMoney.setText("花钻 "+ integer);
            userProviderService.setUsetInfo(userInfo);
        });
    }

    private void initUI() {
        tvMoney = findViewById(R.id.tv_pop_money);
        tvMoney.setText("花钻 "+ UserManager.getInstance().getUser().getBalance());
        TextView tvRecharge = findViewById(R.id.tv_pop_recharge);
        tvRecharge.setOnClickListener(new FastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                getContext().startActivity(new Intent(getContext(),ChongzhiActivity.class));
                dismiss();
            }
        });
        RecyclerView rvTime = findViewById(R.id.rv_pop_time);
        TextView tvCharge = findViewById(R.id.tv_pop_charge);//显示总共消费价格
        TextView tvAppointment = findViewById(R.id.tv_pop_appointment);
        tvAppointment.setOnClickListener(new FastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                isTomorrow ="1";
                if (TextUtils.isEmpty(timeIds)){
                    ToastUtils.showShort(getContext(),"选择时间");
                }else{
                    for (PeriodPrice today:records.getToday()){
                        if (today==periodPrice){
                            isTomorrow = "0";
                            break;
                        }
                    }
                    if (onSureListener!=null) onSureListener.sure(timeIds,Allprice, isTomorrow);
                }
                dismiss();
            }
        });
        rvTime.setLayoutManager(new GridLayoutManager(getContext(),2));

        AppointmentTimeAdapter mAdapter = new AppointmentTimeAdapter(initData(records));
        rvTime.setAdapter(mAdapter);
        mAdapter.addOnUpdatePrice((price, appointTimeIds,period) -> {
            Allprice = price;
            timeIds = appointTimeIds;
            periodPrice = period;
            tvCharge.setText("共"+price+"花钻");
        });
        //调用在监听器下面 防止刷新适配器时 执行不到监听器方法
    }

    /**
     * 构造列表数据
     * @param records
     * @return
     */
    private List<PeriodPrice> initData(AppointTimes records) {
        List<PeriodPrice> today = records.getToday();
        List<PeriodPrice> tomorrow = records.getTomorrow();
        ArrayList<PeriodPrice> periodPrices = new ArrayList<>();
        periodPrices.add(new PeriodPrice(1,"今日"));
        periodPrices.add(new PeriodPrice(1,""));
        periodPrices.addAll(today);
        if (today.size()%2==0){
            periodPrices.add(new PeriodPrice(1,"明日"));
            periodPrices.add(new PeriodPrice(1,""));
        }else{
            periodPrices.add(new PeriodPrice(1,""));
            periodPrices.add(new PeriodPrice(1,"明日"));
            periodPrices.add(new PeriodPrice(1,""));
        }
        periodPrices.addAll(tomorrow);
        return periodPrices;
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popwindow_appointment);
        return v;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }

    public void addOnClickListener(OnSureListener listener){
        onSureListener =listener;
    }
    public interface OnSureListener{
        void sure(String appointTimeIds,int allPrice,String isTomorrow);
    }
    public OnSureListener onSureListener;
}
