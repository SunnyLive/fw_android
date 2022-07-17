package com.fengwo.module_login.mvp.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.security.realidentity.RPEventListener;
import com.alibaba.security.realidentity.RPResult;
import com.alibaba.security.realidentity.RPVerify;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.RegexUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.SignUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_comment.widget.LoadingProgressDialog;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.RealNameFailedDto;
import com.fengwo.module_login.utils.UserManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ArouterApi.REALIDCARD_ACTIVITY)
public class RealIdCardActivity extends BaseMvpActivity {

    private static final String TAG = "RealIdCardActivity";

    private final static String KEY_STATUS = "status";

    private static final int IDCARD_TAKE = 10001;
    private static final int IDCARD_HEAD = 10002;
    private static final int IDCARD_BACK = 10003;
    @BindView(R2.id.sv)
    ScrollView sv;
    @BindView(R2.id.ll_success)
    LinearLayout llSuccess;

    private int chooseType = -1;

    private String imgTake, imgHead, imgBack;

    @BindView(R2.id.et_name)
    EditText etName;
    @BindView(R2.id.et_phone)
    EditText etPhone;
    @BindView(R2.id.et_code)
    EditText etCode;
    @BindView(R2.id.btn_getcode)
    GradientTextView btnGetcode;
    @BindView(R2.id.iv_takeidcard)
    ImageView ivTakeidcard;
    @BindView(R2.id.iv_idcard_head)
    ImageView ivIdcardHead;
    @BindView(R2.id.iv_idcard_back)
    ImageView ivIdcardBack;
    @BindView(R2.id.cb)
    CheckBox cb;
    @BindView(R2.id.btn_submit)
    View v;
    @BindView(R2.id.tv_rule)
    TextView tvRule;
    @BindView(R2.id.tv_failed_reason)
    TextView tvFailedReason;
    @BindView(R2.id.iv_real_status)
    ImageView ivRealStatus;
    @BindView(R2.id.tv_real_status)
    TextView tvRealStatus;
    @BindView(R2.id.btn_recommit)
    GradientTextView btnRecommit;

    boolean isAgree = false;
    private LoginApiService service;
    private CountBackUtils countBackUtils;
    private String code;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    private int time = 60;

    LoadingProgressDialog dialog;

    public static void start(Context context, int status) {
        Intent i = new Intent(context, RealIdCardActivity.class);
        i.putExtra(KEY_STATUS, status);
        context.startActivity(i);
    }

    @Override
    protected void initView() {
        new ToolBarBuilder().setTitle("实名认证").setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black).showBack(true).build();
//        setTitleBackground(getResources().getColor(R.color.white));
        service = new RetrofitUtils().createApi(LoginApiService.class);
        new RxPermissions(this).request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                    } else {
                        toastTip("您关闭了权限，请去设置页面开启");
                    }
                });
        int status = getIntent().getIntExtra(KEY_STATUS, 0);
        refreshRealnameStatus(status);
//        if (UserManager.getInstance().getUser().isReanName()) {
//            sv.setVisibility(View.GONE);
//            llSuccess.setVisibility(View.VISIBLE);
//        }
        dialog = new LoadingProgressDialog(this);
    }


    private void getCode(String mobile) {
        String nonce = SignUtils.getNonce();
        long timestamp = System.currentTimeMillis();
        String sign = SignUtils.sign(nonce, mobile, timestamp);
        service.getCode(nonce, sign, new HttpUtils.ParamsBuilder().put("mobile", mobile)
                .put("timestamp", timestamp + "")
                .put("type", "name").build())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>(this) {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            toastTip("验证码发送成功");
                            btnGetcode.setEnabled(false);
                            countBackUtils = new CountBackUtils();
                            countBackUtils.countBack(60, new CountBackUtils.Callback() {
                                @Override
                                public void countBacking(long time) {
                                    btnGetcode.setText(time + "s");
                                }

                                @Override
                                public void finish() {
                                    btnGetcode.setEnabled(true);
                                    btnGetcode.setText("获取验证码");
                                    time = 60;
                                }
                            });
                        } else {
                            toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    private void getFailedReason() {
        netManager.add(service.realNameFailedReason()
                .compose(io_main())
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<RealNameFailedDto>() {
                    @Override
                    public void _onNext(RealNameFailedDto data) {
                        tvFailedReason.setVisibility(View.VISIBLE);
                        tvFailedReason.setText(data.remark);
                        etName.setText(data.realName);
                        etPhone.setText(data.mobile);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));

    }

    private void realName(String mobile, String realName) {
        netManager.add(
                service.realIdCard(new HttpUtils.ParamsBuilder()
                        .put("cardImgBack", imgBack)
                        .put("cardImgHead", imgHead)
                        .put("idCardNum", mobile)
                        .put("realName", realName)
                        .build())
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>(this) {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (data.isSuccess()) {
                                    toastTip("实名认证完成");
                                    UserInfo userInfo = UserManager.getInstance().getUser();
                                    userInfo.myIdCardWithdraw = 1;
                                    UserManager.getInstance().setUserInfo(userInfo);
//                                    refreshRealnameStatus(userInfo.myIdCardWithdraw);
                                    FaceVerifyActivity.launchActivity(RealIdCardActivity.this);
                                    finish();
                                } else {
                                    toastTip(data.description);
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                                toastTip("认证失败，" + msg);
                            }
                        }));
    }


    private void refreshRealnameStatus(int status) {
        btnRecommit.setVisibility(View.GONE);
        tvFailedReason.setVisibility(View.GONE);
        switch (status) {
            case UserInfo.IDCARD_STATUS_NULL:
                sv.setVisibility(View.VISIBLE);
                llSuccess.setVisibility(View.GONE);
                break;
            case UserInfo.IDCARD_STATUS_NO:
                sv.setVisibility(View.GONE);
                llSuccess.setVisibility(View.VISIBLE);
                tvRealStatus.setText("认证不通过");
                ivRealStatus.setImageResource(R.drawable.ic_real_name_faile);
                btnRecommit.setVisibility(View.VISIBLE);
                btnRecommit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sv.setVisibility(View.VISIBLE);
                        llSuccess.setVisibility(View.GONE);
                    }
                });
                getFailedReason();
                break;
            case UserInfo.IDCARD_STATUS_YES:
                sv.setVisibility(View.GONE);
                llSuccess.setVisibility(View.VISIBLE);
                ivRealStatus.setImageResource(R.drawable.ic_real_name_success);
                break;
            case UserInfo.IDCARD_STATUS_ING:
                sv.setVisibility(View.GONE);
                llSuccess.setVisibility(View.VISIBLE);
                tvRealStatus.setText("认证中");
                ivRealStatus.setImageResource(R.drawable.ic_real_name_ing);
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_real_idcard;
    }


    @OnClick({R2.id.btn_takeidcard, R2.id.btn_idcard_head, R2.id.btn_idcard_back, R2.id.view_bottom, R2.id.btn_getcode, R2.id.btn_submit, R2.id.tv_rule})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_takeidcard) {
            MImagePicker.openImagePicker(RealIdCardActivity.this, MImagePicker.TYPE_IMG, IDCARD_TAKE);
        } else if (id == R.id.btn_idcard_head) {
            MImagePicker.openImagePicker(RealIdCardActivity.this, MImagePicker.TYPE_IMG, IDCARD_HEAD);
        } else if (id == R.id.btn_idcard_back) {
            MImagePicker.openImagePicker(RealIdCardActivity.this, MImagePicker.TYPE_IMG, IDCARD_BACK);
        } else if (id == R.id.tv_rule) {
            BrowserActivity.startRuleActivity(this);
        } else if (id == R.id.view_bottom) {
            isAgree = !isAgree;
            cb.setChecked(isAgree);
        } else if (id == R.id.btn_getcode) {

            String mobile = etPhone.getText().toString();
            if (TextUtils.isEmpty(mobile) || mobile.length() != 11) {
                toastTip("请输入手机号！");
                return;
            }

            getCode(mobile);
        } else if (id == R.id.btn_submit) {
            String name, mobile, code;
            if (checkInput(etName) && RegexUtils.isChinese(etName.getText())) {
                name = etName.getText().toString();
            } else {
                toastTip("请输入正确的姓名！");
                return;
            }
            if (checkInput(etPhone) && RegexUtils.isIDCard18(etPhone.getText())) {
                mobile = etPhone.getText().toString();
            } else {
                toastTip("请输入正确的身份证号码");
                return;
            }
            if (TextUtils.isEmpty(imgHead) || TextUtils.isEmpty(imgBack)) {
                toastTip("请根据图示，上传完整照片");
                return;
            }
            realName(mobile, name);
        }
    }

    private boolean checkInput(EditText et) {
        String str = et.getText().toString().trim();
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String path = MImagePicker.getImagePath(this, data);
            File f = new File(path);
            showLoadingDialog();
            switch (requestCode) {
                case IDCARD_TAKE:
                    UploadHelper.getInstance(this).doUpload(UploadHelper.TYPE_IMAGE, f, new UploadHelper.OnUploadListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onLoading(long cur, long total) {
                            int p = (int) (1f * cur / total * 100);
                            setDialogProgressPercent(p + "%");
                        }

                        @Override
                        public void onSuccess(String url) {
                            hideLoadingDialog();
                            imgTake = url;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ImageLoader.loadImg(ivTakeidcard, url);
                                }
                            });
                        }

                        @Override
                        public void onError() {
                            hideLoadingDialog();
                        }
                    });
                    break;
                case IDCARD_HEAD:

                    UploadHelper.getInstance(this).doUpload(UploadHelper.TYPE_IMAGE, f, new UploadHelper.OnUploadListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onLoading(long cur, long total) {
                            int p = (int) (1f * cur / total * 100);
                            setDialogProgressPercent(p + "%");
                        }

                        @Override
                        public void onSuccess(String url) {
                            hideLoadingDialog();
                            imgHead = url;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ImageLoader.loadImg(ivIdcardHead, url);
                                }
                            });
                        }

                        @Override
                        public void onError() {
                            hideLoadingDialog();
                        }
                    });
                    break;
                case IDCARD_BACK:
                    UploadHelper.getInstance(this).doUpload(UploadHelper.TYPE_IMAGE, f, new UploadHelper.OnUploadListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onLoading(long cur, long total) {
                            int p = (int) (1f * cur / total * 100);
                            setDialogProgressPercent(p + "%");
                        }

                        @Override
                        public void onSuccess(String url) {
                            hideLoadingDialog();
                            imgBack = url;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ImageLoader.loadImg(ivIdcardBack, url);
                                }
                            });

                        }

                        @Override
                        public void onError() {
                            hideLoadingDialog();
                        }
                    });
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
