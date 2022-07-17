package com.fengwo.module_flirt.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.FastClickListener;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.adapter.OnlineOrderTimeAdapter;
import com.fengwo.module_flirt.bean.OrderListBean;
import com.fengwo.module_login.mvp.ui.activity.ChongzhiActivity;
import com.fengwo.module_login.utils.UserManager;

import java.text.DecimalFormat;
import java.util.ArrayList;

import razerdp.basepopup.BasePopupWindow;

/**
 * 在线下单弹窗
 */
public class OnlineOrderPopwindow extends BasePopupWindow {

    private final ArrayList<OrderListBean> records;
    private int orderId=-1;
    private String Allprice;
    @Autowired
    UserProviderService userProviderService;
    private TextView tvMoney;

    public OnlineOrderPopwindow(Context context, ArrayList<OrderListBean> records) {
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
        RecyclerView rvTime = findViewById(R.id.rv_pop_time);
        TextView tvCharge = findViewById(R.id.tv_pop_charge);
        TextView tvAppointment = findViewById(R.id.tv_pop_appointment);
        tvAppointment.setText("确定支付");
        rvTime.setLayoutManager(new GridLayoutManager(getContext(),2));
        OnlineOrderTimeAdapter mAdapter = new OnlineOrderTimeAdapter();
        rvTime.setAdapter(mAdapter);
        mAdapter.addOnClickTimeListener((price, orderId) -> {
            this.orderId=orderId;
            Allprice=price;
            tvCharge.setText("共" + price + "花钻");
        });
        mAdapter.setNewData(records);
        //"确定支付
        tvAppointment.setOnClickListener(new FastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                if (orderId!=-1){
                    if (onSureListener!=null) onSureListener.sure(orderId,Allprice);
                }else{
                    ToastUtils.showShort(getContext(),"选择时间");
                }
                dismiss();
            }
        });
        tvRecharge.setOnClickListener(new FastClickListener() {
            @Override
            public void onNoFastClick(View v) {
                getContext().startActivity(new Intent(getContext(),ChongzhiActivity.class));
                dismiss();
            }
        });
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popwindow_online_order);
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
        void sure(int orderId,String allPrice);
    }
    public OnSureListener onSureListener;

}
