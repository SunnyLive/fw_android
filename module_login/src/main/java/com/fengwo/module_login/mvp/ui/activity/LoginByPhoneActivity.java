/*

使用手机号码登录的页面
验证手机号
验证短信验证码


 */

package com.fengwo.module_login.mvp.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.base.Constant;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.AppUtils;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_login.BuildConfig;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.eventbus.LoginSuccessEvent;
import com.fengwo.module_login.mvp.dto.LoginDto;
import com.fengwo.module_login.mvp.presenter.LoginPresenter;
import com.fengwo.module_login.mvp.ui.activity.acc_cancel.AccCancelConfirmActivity;
import com.fengwo.module_login.mvp.ui.iview.ILoginView;
import com.fengwo.module_login.mvp.ui.pop.WriteOffTipsPopwindow;
import com.fengwo.module_login.utils.UserManager;
import com.gyf.immersionbar.ImmersionBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.OnClick;

public class LoginByPhoneActivity extends BaseMvpActivity<ILoginView, LoginPresenter> implements ILoginView {
    private final static String GET_CODE_STR = "获取验证码";
    private final static int WAITING_TIME = 60;
    //@BindView(R2.id.btn_back)
    //ImageView btnBack;
    @BindView(R2.id.btn_get_code)
    TextView btnGetCode;
    @BindView(R2.id.et_phone)
    EditText etPhone;
    @BindView(R2.id.et_code)
    EditText etCode;
    @BindView(R2.id.btn_rule)
    TextView btnRule;
    @BindView(R2.id.ll_code_parent)
    LinearLayout mCodeParent; //验证码控件的父
    @BindView(R2.id.btn_login)
    Button mBtSendLogin;

    @BindView(R2.id.view_clear)
    View editClear;

    @BindView(R2.id.logo)
    View mViewLogo;
    @BindView(R2.id.ims_yd)
    CheckBox mRbDeal;

    private CountBackUtils countBackUtils;
    private boolean isGetKeyBoardHeight = false;
    private AlertDialog alertDialog;

    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .navigationBarDarkIcon(true, 0.2f)
                .navigationBarColor(com.fengwo.module_comment.R.color.white)
                .keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                .setOnKeyboardListener(this)
                .init();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        countBackUtils = new CountBackUtils();
        btnRule.setText(Html.fromHtml(getResources().getString(R.string.login_rule)));
        String lastPhoone = UserManager.getInstance().getLastLoginnPhone();
        if (!TextUtils.isEmpty(lastPhoone)) {
            etPhone.setText(lastPhoone);
            etPhone.setSelection(lastPhoone.length());
        }
        editClear.setOnClickListener(v -> etPhone.setText(""));
    }

    @Override
    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
        super.onKeyboardChange(isPopup, keyboardHeight);
        if (!isGetKeyBoardHeight && isPopup && keyboardHeight >= DensityUtils.dp2px(this, 200)) {
            isGetKeyBoardHeight = true;
            SPUtils1.put(this, Constant.PRE_KEYBOARD_HEIGHT, keyboardHeight);
        }
        chooseLogo(isPopup);
    }

    @Override
    protected void onStop() {
        getWindow().getDecorView().requestLayout();
        super.onStop();
    }


    @Override
    protected int getContentView() {
//        return R.layout.login_activity_loginbyphone;
        return R.layout.activity_login_phone;
    }

    private void chooseLogo(boolean isHide) {
        float startValue = isHide ? 1 : 0;
        float endValue = isHide ? 0 : 1;
        AnimatorSet animatorSet = new AnimatorSet();//组合动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mViewLogo, "scaleX", startValue, endValue);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mViewLogo, "scaleY", startValue, endValue);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mViewLogo, "alpha", startValue, endValue);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(scaleX, scaleY, alpha);
        animatorSet.start();
    }


    @OnClick({R2.id.btn_back, R2.id.tv_custom_service, R2.id.et_phone, R2.id.btn_get_code, R2.id.btn_login, R2.id.btn_rule, R2.id.logo, R2.id.ims_yd})
    public void onViewClicked(View view) {
        if (isFastClick()) return;
        int id = view.getId();
        if (id == R.id.et_phone) {
            mCodeParent.clearFocus();
        } else if (id == R.id.btn_back) {
            onBackPressed();
        } else if (id == R.id.btn_get_code) {
            String phone = etPhone.getText().toString().trim();
            if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                toastTip("请输入正确的手机号码！");
                return;
            }
            etPhone.clearFocus();
            mCodeParent.setFocusable(true);
            p.getLoginCode(phone);

        } else if (id == R.id.btn_login) {
            if (!mRbDeal.isChecked()) {
                ToastUtils.showShort(this, "请先勾选用户协议");
                return;
            }
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
            etPhone.clearFocus();
            mCodeParent.clearFocus();
            p.loginByCode(phone, code, AppUtils.getChannelName(this));
        } else if (id == R.id.btn_rule) {
            BrowserActivity.startRuleActivity(this);
        } else if (id == R.id.logo) { // TODO 测试专用
            if (BuildConfig.DEBUG) {
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle("选择环境")
                        .setItems(new String[]{"正式服", "开发服", "测试服pre", "测试服sit01", "测试服sit02"}, (dialogInterface, i) -> {
                            SPUtils1.put(this, "KEY_URL", i);
                            btnRule.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                }
                            }, 1000);
                        })
                        .create();
                alertDialog.show();
            }
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
                btnGetCode.setText(time + "s");
            }

            @Override
            public void finish() {
                btnGetCode.setText(GET_CODE_STR);
                btnGetCode.setEnabled(true);
            }
        });
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
                AccCancelConfirmActivity.start(LoginByPhoneActivity.this, loginDto.userId + "", loginDto.mobile, false);
            }
        });
        writeOffTipsPopwindow.showPopupWindow();
    }

    @Override
    public void loginSuccess(UserInfo userInfo, boolean isEditInfo) {

        //判断用户是否需要修改昵称和头像
        if (isEditInfo) {
            hideLoadingDialog();
            Intent i = new Intent(this, LoginInfoActivity.class);
            i.putExtra(LoginInfoActivity.NICK_NAME, userInfo.nickname);
            i.putExtra(LoginInfoActivity.HEADER_URL, userInfo.headImg);
            i.putExtra(LoginInfoActivity.BIND_PHONE, true);
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
    protected void onDestroy() {
        super.onDestroy();
        countBackUtils.destory();
    }
}
