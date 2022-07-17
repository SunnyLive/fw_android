package com.fengwo.module_login.mvp.ui.activity.acc_cancel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.presenter.AccCancelConfirmPresenter;
import com.fengwo.module_login.mvp.presenter.AccountRecordPresenter;
import com.fengwo.module_login.mvp.ui.activity.LoginActivity;
import com.fengwo.module_login.mvp.ui.iview.IAccCancelConfirmView;
import com.fengwo.module_login.mvp.ui.pop.TipsPopwindow;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.FWWebSocket1;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/7/30
 */
public class AccCancelConfirmActivity extends BaseMvpActivity<IAccCancelConfirmView,AccCancelConfirmPresenter> implements IAccCancelConfirmView {

    @BindView(R2.id.tv_area_code)
    TextView tvAreaCode;
    @BindView(R2.id.et_phone)
    EditText etPhone;
    @BindView(R2.id.et_yzm_code)
    EditText etYzmCode;
    @BindView(R2.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R2.id.tv_sure)
    GradientTextView tvSure;
    @BindView(R2.id.tv_tips)
    TextView tvTips;

    private String reason;
    private String mobile;
    private boolean isCancel;

    private CountBackUtils countBackUtils;
    @Autowired
    UserProviderService userProviderService;

    public static void start(Context context,String reason,String mobile,boolean isCancel){
        Intent intent = new Intent(context,AccCancelConfirmActivity.class);
        intent.putExtra("reason",reason);//在撤销 注销账号 过来，reason是userId;
        intent.putExtra("isCancel",isCancel);//是否注销用户 true:注销账号;false:取消注销
        intent.putExtra("mobile",mobile);//是否注销用户 true:注销账号;false:取消注销
        context.startActivity(intent);
    }

    @Override
    public AccCancelConfirmPresenter initPresenter() {
        return new AccCancelConfirmPresenter();
    }

    @Override
    protected void initView() {
        mobile = getIntent().getStringExtra("mobile");
        reason = getIntent().getStringExtra("reason");
        isCancel = getIntent().getBooleanExtra("isCancel",true);

        countBackUtils = new CountBackUtils();

        new ToolBarBuilder().showBack(true)
                .setTitle(isCancel?"注销账号":"撤销账号注销申请")
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .build();
        if (isCancel) {
            tvTips.setVisibility(View.GONE);
            p.getMobile(UserManager.getInstance().getUser().id);
        }else {
            etPhone.setText(mobile);
        }

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_acc_cancel_confirm;
    }


    @OnClick({R2.id.tv_area_code, R2.id.tv_get_code, R2.id.tv_sure})
    public void onViewClicked(View view) {
        int id = view.getId();
       if (id == R.id.tv_area_code){

       }else if (id == R.id.tv_get_code){
           String phone = etPhone.getText().toString().trim();
           if (TextUtils.isEmpty(phone) || phone.length() != 11) {
               toastTip("请输入正确的手机号码！");
               return;
           }
           p.getLoginCode(phone);
       }else if (id == R.id.tv_sure){
           accDestroy();
       }
    }

    private void accDestroy() {
        String code = etYzmCode.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        if(TextUtils.isEmpty(code)){
            toastTip("请输入验证码");
            return;
        }
        if (isCancel) {
            TipsPopwindow tipsPopwindow = new TipsPopwindow(this);
            tipsPopwindow.setOnConfirmListener(new TipsPopwindow.OnConfirmListener() {
                @Override
                public void onConfirm() {
                    if(null!=userProviderService&&null!=userProviderService.getUserInfo()){
                        p.accDestroy(code, phone, userProviderService.getUserInfo().id + "", reason);
                    }else {
                        toastTip("为获取到您的个人信息，请从新尝试");
                    }

                }
            });
            tipsPopwindow.showPopupWindow();
        }else {
            p.accDestroyCancel(code,phone,reason,"");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countBackUtils.destory();
    }

    @Override
    public void setCode(String code) {
        tvGetCode.setEnabled(false);
        countBackUtils.countBack(60, new CountBackUtils.Callback() {

            @Override
            public void countBacking(long time) {
                tvGetCode.setText(time + "");
            }

            @Override
            public void finish() {
                tvGetCode.setText("获取验证码");
                tvGetCode.setEnabled(true);
            }
        });
    }

    @Override
    public void destroy() {
        try {
            UserManager.getInstance().clearUser();
            FWWebSocket1.getInstance().destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        startActivity(LoginActivity.class);
        finish();
    }

    @Override
    public void cancelDestroy() {
        startActivity(LoginActivity.class);
        finish();
    }

    @Override
    public void setMobile(String mobile) {
        etPhone.setText(mobile);
    }
}
