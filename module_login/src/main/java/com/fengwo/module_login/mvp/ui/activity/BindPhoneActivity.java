/*
 *
 * 手机号码绑定
 * 微信登录入口后进行手机绑定操作
 *
 * */

package com.fengwo.module_login.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.AppUtils;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.DrawableUtil;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.eventbus.LoginSuccessEvent;
import com.fengwo.module_login.mvp.dto.LoginDto;
import com.fengwo.module_login.mvp.presenter.LoginPresenter;
import com.fengwo.module_login.mvp.ui.activity.acc_cancel.AccCancelConfirmActivity;
import com.fengwo.module_login.mvp.ui.iview.ILoginView;
import com.fengwo.module_login.mvp.ui.pop.WriteOffTipsPopwindow;
import com.fengwo.module_comment.utils.DrawableUtilHtml;

import butterknife.BindView;
import butterknife.OnClick;

public class BindPhoneActivity extends BaseMvpActivity<ILoginView, LoginPresenter> implements ILoginView {
    private final static String GET_CODE_STR = "获取验证码";
    private final static int WAITING_TIME = 60;
    @BindView(R2.id.btn_back)
    ImageView btnBack;
    @BindView(R2.id.btn_get_code)
    TextView btnGetCode;
    //    @BindView(R2.id.view_bottom)
//    LinearLayout viewBottom;
    @BindView(R2.id.et_phone)
    EditText etPhone;
    @BindView(R2.id.et_code)
    EditText etCode;
    @BindView(R2.id.btn_rule)
    TextView btnRule;

    @BindView(R2.id.ll_code_parent)
    LinearLayout mCodeParent; //验证码控件的父


    private CountBackUtils countBackUtils;
    String token;

    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    public static void startBindPhoneActivity(Context context, String token) {
        Intent i = new Intent(context, BindPhoneActivity.class);
        i.putExtra("token", token);
        context.startActivity(i);
    }

    @Override
    protected void initView() {
        countBackUtils = new CountBackUtils();
        btnRule.setText(Html.fromHtml(getResources().getString(R.string.login_rule)));
        token = getIntent().getStringExtra("token");
        etPhone.setFilters( new InputFilter[]{new InputFilter.LengthFilter(11)});
        new DrawableUtil(etPhone, new DrawableUtil.OnDrawableListener() {
            @Override
            public void onLeft(View v, Drawable left) {

            }

            @Override
            public void onRight(View v, Drawable right) {
                etPhone.setText("");
            }
        });
    }

    @Override
    protected int getContentView() {
        //return R.layout.login_activity_bindphone;
        return R.layout.activity_login_bindphoe;
    }


    @OnClick({R2.id.tv_custom_service, R2.id.et_phone, R2.id.btn_back, R2.id.btn_get_code, R2.id.btn_login, R2.id.btn_rule})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_back) {
            onBackPressed();
        } else if (id == R.id.et_phone) {
            mCodeParent.clearFocus();
        } else if (id == R.id.btn_get_code) {
            String phone = etPhone.getText().toString().trim();
            if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                toastTip("请输入正确的手机号码！");
                return;
            }
            etPhone.clearFocus();
            mCodeParent.setFocusable(true);
            p.getBindPhoneCode(phone);

        } else if (id == R.id.btn_login) {
            String phone = etPhone.getText().toString().trim();
            String code = etCode.getText().toString().trim();
            if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                toastTip("请输入正确的手机号码！");
                return;
            }
            if (TextUtils.isEmpty(code) || code.length() != 6) {
                toastTip("请输入正确的验证码！");
                return;
            }
            showLoadingDialog();
            p.bindPhone(phone, code, token, AppUtils.getChannelName(this));
//            ARouter.getInstance().build(ArouterApi.MAIN_MAIN_ACTIVITY).navigation();
        } else if (id == R.id.btn_rule) {
            BrowserActivity.startRuleActivity(this);
        } else if (id == R.id.tv_custom_service) {
            DialogUtil.showAlertDialog(this, "拨打客服电话?", "是否拨打客服电话：400-005-1118",
                    "确定", "取消", false, new DialogUtil.AlertDialogBtnClickListener() {
                        @Override
                        public void clickPositive() {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:400-005-1118");
                            intent.setData(data);
                            startActivity(intent);
                        }

                        @Override
                        public void clickNegative() {

                        }
                    });
        }
    }

    @Override
    public void setCode(String code) {
        btnGetCode.setEnabled(false);
        countBackUtils.countBack(WAITING_TIME, new CountBackUtils.Callback() {
            @Override
            public void countBacking(long time) {
                btnGetCode.setText(time + "");
            }

            @Override
            public void finish() {
                btnGetCode.setText(GET_CODE_STR);
                btnGetCode.setEnabled(true);
            }
        });
    }

    @Override
    public void loginSuccess(UserInfo userInfo,boolean isEditInfo) {
        //判断用户是否需要修改昵称和头像
        if (isEditInfo) {
            hideLoadingDialog();
            Intent i = new Intent(this, LoginInfoActivity.class);
            i.putExtra(LoginInfoActivity.NICK_NAME, userInfo.nickname);
            i.putExtra(LoginInfoActivity.HEADER_URL, userInfo.headImg);
            i.putExtra(LoginInfoActivity.BIND_PHONE,true);
            startActivity(i);
        } else {
            ARouter.getInstance().build(ArouterApi.MAIN_MAIN_ACTIVITY).navigation();
            RxBus.get().post(new LoginSuccessEvent());
            runOnUiThread(() -> {
                hideLoadingDialog();
                finish();
            });
        }
    }

    @Override
    public void toBindPhone(String token) {

    }

    @Override
    public void toAccDestroyCancel(LoginDto loginDto) {
        WriteOffTipsPopwindow writeOffTipsPopwindow = new WriteOffTipsPopwindow(this, loginDto.fwId);
        writeOffTipsPopwindow.setOnConfirmListener(new WriteOffTipsPopwindow.OnConfirmListener() {
            @Override
            public void onConfirm() {
                AccCancelConfirmActivity.start(BindPhoneActivity.this, loginDto.userId + "", loginDto.mobile, false);
            }
        });
        writeOffTipsPopwindow.showPopupWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countBackUtils.destory();
    }
}
