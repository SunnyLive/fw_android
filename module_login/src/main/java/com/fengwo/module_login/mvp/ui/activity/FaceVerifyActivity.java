package com.fengwo.module_login.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.security.realidentity.RPEventListener;
import com.alibaba.security.realidentity.RPResult;
import com.alibaba.security.realidentity.RPVerify;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ClickUtil;
import com.fengwo.module_comment.utils.ClickUtils;
import com.fengwo.module_comment.utils.Common;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.eventbus.BackToMainEvent;
import com.fengwo.module_login.mvp.dto.VerifyResultDto;
import com.fengwo.module_login.mvp.dto.VerifyTokenDto;
import com.fengwo.module_login.mvp.presenter.FaceVerifyPresenter;
import com.fengwo.module_login.mvp.ui.iview.IFaceVerifyView;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_login.widget.URLSpanNoUnderLine;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

@Route(path = ArouterApi.FACE_VERIFY_ACTION)
public class FaceVerifyActivity extends BaseMvpActivity<IFaceVerifyView, FaceVerifyPresenter> implements IFaceVerifyView {
    private static final String TAG = "FaceVerifyActivity";

    private final static String KEY_STATUS = "status";
    private final static String KEY_REVIEW = "isReview";
    private final static String KEY_TYPE = "type";
    private final static String RP_BASIC = "RPBasic";
    private final static String RP_BIO_ONLY = "RPBioOnly";

    @BindView(R2.id.logo)
    AppCompatImageView ivLogo;
    @BindView(R2.id.tv_logo_tip)
    AppCompatTextView tvLogoTip;
    @BindView(R2.id.tv_add_nickname)
    AppCompatTextView tvAddNickName;
    @BindView(R2.id.tv_agree_tip)
    AppCompatTextView tvAgreeTip;
    @BindView(R2.id.next)
    GradientTextView tvNext;
    @BindView(R2.id.cb_agree)
    AppCompatCheckBox cbAgree;

    @BindView(R2.id.tv_verify_tip)
    AppCompatTextView tvVerifyTip;
    @BindView(R2.id.iv_image_tip)
    AppCompatImageView ivImageTip;
    @BindView(R2.id.tv_retry)
    AppCompatTextView tvRetry;
    @BindView(R2.id.tv_review)
    AppCompatTextView tvReview;

    @BindView(R2.id.tv_back_main)
    AppCompatTextView tvBackMain;

    @BindView(R2.id.tv_verify)
    AppCompatTextView tvVerify;

    @Autowired
    UserProviderService userService;

    private int status;     // 实名认证状态
    private int type;       // 跳转类型
    private boolean isReview = true; //是否显示 review
    private String mFaceVerifyType;      // RP_BASIC RP_BIO_ONLY
    private boolean isAgree = true;

    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, FaceVerifyActivity.class);
        context.startActivity(intent);
    }

    public static void launchActivityForResult(Activity context) {
        Intent intent = new Intent(context, FaceVerifyActivity.class);
        context.startActivityForResult(intent, Common.REQUEST_FACE_VERIFY_ACTIVITY);
    }

    @Override
    public FaceVerifyPresenter initPresenter() {
        return new FaceVerifyPresenter();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_face_verify;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra(KEY_TYPE, 0);
            status = intent.getIntExtra(KEY_STATUS, 0);
            isReview = intent.getBooleanExtra(KEY_REVIEW, true);
        }
        new ToolBarBuilder()
                .showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitleColor(R.color.text_33)
                .setTitle("实人认证")
                .build();

        SpannableString spannableString = new SpannableString(getString(R.string.agree_tip));
        // "查看协议详情"设置颜色和点击事件
        URLSpanNoUnderLine clickableSpan = new URLSpanNoUnderLine("");
        clickableSpan.setTextColor(Color.parseColor("#63a5ff"));
        clickableSpan.setOnClickListener(v -> {
            FaceVerifyAgreementActivity.launchActivity(FaceVerifyActivity.this);
        });
        spannableString.setSpan(clickableSpan, spannableString.length() - 6, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvAgreeTip.setText(spannableString);
        tvAgreeTip.setMovementMethod(LinkMovementMethod.getInstance());

        tvAddNickName.setText(String.format(getString(R.string.add_nickname_tip), TextUtils.isEmpty(userService.getUserInfo().realName) ? "" : userService.getUserInfo().realName));
        cbAgree.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            isAgree = isChecked;
        }));

    }

    @OnClick({R2.id.next, R2.id.tv_retry})
    public void next(View v) {
        if (ClickUtil.canClick()) {
            if (!isAgree) {
                ToastUtils.showShort(this, "需用户同意才能操作");
                return;
            }
            ivImageTip.setVisibility(View.GONE);
            tvVerifyTip.setVisibility(View.GONE);
            // 已经通过实名认证，只需做人脸识别
            // 未通过实名认证的，需要先做人脸识别后上传身份证
            mFaceVerifyType = status == UserInfo.MY_ID_CARD_REAL_YES ? RP_BIO_ONLY : RP_BASIC;
            p.getVerifyToken(mFaceVerifyType);
            showLoadingDialog();
        }
    }

    @OnClick(R2.id.tv_verify)
    public void toVerify(View v) {
        String bizId = String.valueOf(tvReview.getTag());
        if (type == Common.SKIP_ANCHOR || type == Common.SKIP_LIVE) {
            ARouter.getInstance().build(ArouterApi.REAL_ANCHOR_ACTION)
                    .withString("bizId",bizId)
                    .navigation();
        } else if (type == Common.SKIP_EXPERT || type == Common.SKIP_ILIAO){
            ARouter.getInstance().build(ArouterApi.FLIRT_CERTIFICATION)
                    .withString("bizId",bizId)
                    .navigation();
        }
        finish();
    }

    @OnClick(R2.id.tv_back_main)
    public void toBackMain(View v) {
        BackToMainEvent event = new BackToMainEvent();
        if (type == Common.SKIP_ANCHOR || type == Common.SKIP_LIVE) {
            event.setBackEvent(BackToMainEvent.BACK_TO_MAIN);
        } else {
            event.setBackEvent(BackToMainEvent.BACK_TO_ILIAO);
        }
        RxBus.get().post(event);
        finish();
    }


    @OnClick(R2.id.tv_review)
    public void onReview(View v){
        p.getVerifyReview(String.valueOf(v.getTag()));
    }



    @Override
    public void onGetVerifyTokenSuccess(VerifyTokenDto verifyTokenDto) {
        hideAgreementUI();
        tvReview.setTag(verifyTokenDto.getBizId());
        if (status == UserInfo.MY_ID_CARD_REAL_YES) {
            // 已实名认证
            rpVerifyOnly(this, verifyTokenDto);
        } else {
            // 未实名认证
            rpVerifyBasic(this, verifyTokenDto);
        }
    }

    @Override
    public void onGetVerifyTokenError(String msg) {
        hideLoadingDialog();
        KLog.d(TAG, msg);
        ToastUtils.showShort(getApplicationContext(), "获取身份信息失败");
    }

    private void rpVerifyBasic(Context context, VerifyTokenDto verifyTokenDto) {

        RPVerify.start(context, verifyTokenDto.getToken(), new RPEventListener() {
            @Override
            public void onFinish(RPResult rpResult, String s, String s1) {
                hideLoadingDialog();
                KLog.d(TAG, "message= " + rpResult.message + " s= " + s + " s1" + s1);
                switch (rpResult) {
                    case AUDIT_PASS:
                        getVerifyResult(verifyTokenDto);
                        break;
                    case AUDIT_IN_AUDIT:
                    case AUDIT_NOT:
                    case AUDIT_FAIL:
                    case AUDIT_EXCEPTION:
                    default:
                        showVerifyResult(false);
                        break;
                }
            }
        });
    }

    private void rpVerifyOnly(Context context, VerifyTokenDto verifyTokenDto) {
        RPVerify.startByNative(context, verifyTokenDto.getToken(), new RPEventListener() {
            @Override
            public void onFinish(RPResult rpResult, String s, String s1) {
                hideLoadingDialog();
                KLog.d(TAG, "message= " + rpResult.message + " s= " + s + " s1" + s1);
                switch (rpResult) {
                    case AUDIT_PASS:
                        getVerifyResult(verifyTokenDto);
                        break;
                    case AUDIT_IN_AUDIT:
                    case AUDIT_NOT:
                    case AUDIT_FAIL:
                    case AUDIT_EXCEPTION:
                    default:
                        showVerifyResult(false);
                        break;
                }
            }
        });
    }

    private void getVerifyResult(VerifyTokenDto verifyTokenDto) {
        if (verifyTokenDto != null) {
            p.getVerifyResult(verifyTokenDto.getBizId(), mFaceVerifyType);
            showLoadingDialog();
        } else {
            showVerifyResult(false);
        }
    }

    @Override
    public void onGetVerifyResultSuccess(VerifyResultDto verifyResultDto) {
        hideLoadingDialog();
        UserInfo userInfo = UserManager.getInstance().getUser();
        userInfo.myIdCardWithdraw = UserInfo.MY_ID_CARD_REAL_YES;
        userInfo.setFace(true);
        UserManager.getInstance().setUserInfo(userInfo);
        // code == 1成功，其他都是认证失败
        if (verifyResultDto.getCode() == 1) {
            showVerifyResult(true);
        } else {
            showVerifyResult(false);
        }
        KLog.d(TAG, verifyResultDto.getMsg());
    }

    @Override
    public void onGetVerifyResultError(String msg) {
        showVerifyResult(false);
        hideLoadingDialog();
        KLog.d(TAG, msg);
    }

    @Override
    public void onFaceReViewSuccess() {
        TipDialog td = new TipDialog(this);
        td.showPopupWindow();
        tvReview.postDelayed(()->{
            td.dismiss();
            onBackPressed();
        },3000);
    }


    /**
     * 隐藏协议页面UI
     */
    private void hideAgreementUI() {
        ivLogo.setVisibility(View.GONE);
        tvLogoTip.setVisibility(View.GONE);
        tvAddNickName.setVisibility(View.GONE);
        tvAgreeTip.setVisibility(View.GONE);
        tvNext.setVisibility(View.GONE);
        cbAgree.setVisibility(View.GONE);
    }

    /**
     * 显示认证结果
     *
     * @param pass
     */
    private void showVerifyResult(boolean pass) {
        tvVerifyTip.setVisibility(View.VISIBLE);
        ivImageTip.setVisibility(View.VISIBLE);

        if (pass) {
            tvVerifyTip.setText(R.string.face_verify_pass);
            ivImageTip.setImageResource(R.drawable.ic_face_verify_pass);
            tvRetry.setVisibility(View.GONE);
            tvReview.setVisibility(View.GONE);
            if (type == Common.SKIP_ANCHOR || type == Common.SKIP_LIVE) {
                tvBackMain.setText("返回直播");
                tvVerify.setText("主播认证");
                tvBackMain.setVisibility(View.VISIBLE);
                tvVerify.setVisibility(View.VISIBLE);
            } else if (type == Common.SKIP_EXPERT || type == Common.SKIP_ILIAO) {
                tvBackMain.setText("返回i撩");
                tvVerify.setText("达人认证");
                tvBackMain.setVisibility(View.VISIBLE);
                tvVerify.setVisibility(View.VISIBLE);
            } else {
                tvBackMain.setVisibility(View.GONE);
                tvVerify.setVisibility(View.GONE);
                ivImageTip.postDelayed(() -> {
                    finish();
                }, 3000);
            }
        } else {
            tvVerifyTip.setText(R.string.face_verify_error);
            ivImageTip.setImageResource(R.drawable.ic_face_verify_error);
            tvRetry.setVisibility(View.VISIBLE);
            tvReview.setVisibility(isReview ? View.VISIBLE : View.GONE);
            if (status == UserInfo.MY_ID_CARD_REAL_YES) {
                if (type == Common.SKIP_ANCHOR || type == Common.SKIP_LIVE &&
                        (UserManager.getInstance().getUser().myIsCardStatus == UserInfo.IDCARD_STATUS_NULL
                                || UserManager.getInstance().getUser().myIsCardStatus == UserInfo.IDCARD_STATUS_NO)) {
                    ivImageTip.setVisibility(View.GONE);
                    tvVerifyTip.setVisibility(View.GONE);
                    toVerify(null);
                } else if ((type == Common.SKIP_EXPERT || type == Common.SKIP_ILIAO) &&
                        (UserManager.getInstance().getUser().wenboAnchorStatus == UserInfo.WENBO_STATUS_NO
                                || UserManager.getInstance().getUser().wenboAnchorStatus == UserInfo.WENBO_STATUS_NULL)) {
                    ivImageTip.setVisibility(View.GONE);
                    tvVerifyTip.setVisibility(View.GONE);
                    toVerify(null);
                }
            }
        }
    }

    @OnClick(R2.id.btn_back)
    public void back() {
        finish();
    }





    private static class TipDialog extends BasePopupWindow {

        public TipDialog(Context context) {
            super(context);
            setPopupGravity(Gravity.CENTER);
        }

        @Override
        public View onCreateContentView() {
            return createPopupById(R.layout.layout_tip_dialog);
        }
    }
}
