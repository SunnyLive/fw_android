package com.fengwo.module_login.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.Common;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.presenter.RealAnchorPresenter;
import com.fengwo.module_login.mvp.ui.iview.IRealAnchorView;
import com.fengwo.module_login.utils.UserManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

@Route(path = ArouterApi.REAL_ANCHOR_ACTION)
public class RealAnchorActivity extends BaseMvpActivity<IRealAnchorView, RealAnchorPresenter> implements IRealAnchorView{


    @BindView(R2.id.iv_take_id_card)
    ImageView mIvIdCard; //手持身份证照片

    @BindView(R2.id.iv_heard)
    ImageView mIvHeard; //个人形象照片

    @BindView(R2.id.scroll_view)
    ScrollView scrollView;

    @BindView(R2.id.empty_view)
    LinearLayout llEmptyView;

    @BindView(R2.id.tv_empty)
    AppCompatTextView tvEmpty;


    @BindView(R2.id.iv_take_id_card_del)
    View mViewDelIdCard;
    @BindView(R2.id.btn_id_card)
    View mBtnIdCard;
    @BindView(R2.id.iv_heard_del)
    View mViewDelHeader;
    @BindView(R2.id.btn_header)
    View mBtnHeader;



    @Autowired
    UserProviderService userService;

    private static final int IDCARD_TAKE = 10001;
    private static final int IDCARD_HEAD = 10002;

    private String mIdCardUrl, mHeardUrl;
    private int mAnchorStatus;
    private String mBizId;

    @Override
    public RealAnchorPresenter initPresenter() {
        return new RealAnchorPresenter();
    }

    @Override
    protected void initView() {
        new ToolBarBuilder().setTitleColor(R.color.text_33)
                .setTitle("主播认证")
                .showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .build();

        Intent intent = getIntent();
        if (intent != null) {
            mAnchorStatus = intent.getIntExtra("anchor_status", UserInfo.IDCARD_STATUS_NULL);
            mBizId = intent.getStringExtra("bizId");
            // 主播认证已经通过，显示已认证提示
            if (mAnchorStatus == UserInfo.IDCARD_STATUS_YES) {
                scrollView.setVisibility(View.GONE);
                llEmptyView.setVisibility(View.VISIBLE);
                tvEmpty.setText("已经完成主播认证");
            }
        }
    }

    @SuppressLint("CheckResult")
    @OnClick({
            R2.id.btn_id_card,//上传身份证
            R2.id.btn_header,  //上传形象
            R2.id.tv_rule,   //协议
            R2.id.btn_submit, //提交
            R2.id.iv_take_id_card_del,
            R2.id.iv_heard_del
    })
    void onViewClick(View v) {

        if (v.getId() == R.id.btn_id_card) {
            new RxPermissions(this).request(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            MImagePicker.openImagePicker(RealAnchorActivity.this, MImagePicker.TYPE_IMG, IDCARD_TAKE);
                        } else {
                            toastTip("您关闭了权限，请去设置页面开启");
                        }
                    });
        } else if (v.getId() == R.id.btn_header) {
            new RxPermissions(this).request(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            MImagePicker.openImagePicker(RealAnchorActivity.this, MImagePicker.TYPE_IMG, IDCARD_HEAD);
                        } else {
                            toastTip("您关闭了权限，请去设置页面开启");
                        }
                    });

        } else if (v.getId() == R.id.tv_rule) {
            BrowserActivity.startRuleActivity(this);
        } else if (v.getId() == R.id.btn_submit) {

            if (TextUtils.isEmpty(mIdCardUrl)) {
                ToastUtils.showShort(this, "请上传手持身份证照片");
                return;
            }
            if (TextUtils.isEmpty(mHeardUrl)) {
                ToastUtils.showShort(this, "请上传个人形象照片");
                return;
            }

            // 需要先进行人脸识别认证
            p.postAnchorIDCard(mBizId, mIdCardUrl, mHeardUrl);

        }else if (v.getId() == R.id.iv_take_id_card_del){
            mViewDelIdCard.setVisibility(View.GONE);
            mBtnIdCard.setVisibility(View.VISIBLE);
            mIvIdCard.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.icon_fw_id_card));
            mIdCardUrl = "";
        }else if (v.getId() == R.id.iv_heard_del){
            mViewDelHeader.setVisibility(View.GONE);
            mBtnHeader.setVisibility(View.VISIBLE);
            mIvHeard.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.icon_fw_id_card));
            mHeardUrl = "";
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_real_anchor;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == Common.REQUEST_FACE_VERIFY_ACTIVITY) {
                p.postAnchorIDCard(mBizId, mIdCardUrl, mHeardUrl);
            } else if (requestCode == IDCARD_HEAD || requestCode == IDCARD_TAKE){
                String path = MImagePicker.getImagePath(this, data);
                File f = new File(path);
                p.requestUpLoad(this, f, requestCode);
                ImageLoader.loadLocalImg(f, requestCode == IDCARD_HEAD ? mIvHeard : mIvIdCard);
                if (requestCode == IDCARD_HEAD) {
                    mViewDelHeader.setVisibility(View.VISIBLE);
                    mBtnHeader.setVisibility(View.GONE);
                }
                if (requestCode == IDCARD_TAKE) {
                    mViewDelIdCard.setVisibility(View.VISIBLE);
                    mBtnIdCard.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onPostAnchorIDCardSuccess(String msg) {
        ToastUtils.showShort(getApplicationContext(), "上传成功");
        UserInfo userInfo = UserManager.getInstance().getUser();
        userInfo.myIsCardStatus = UserInfo.IDCARD_STATUS_ING;
        UserManager.getInstance().setUserInfo(userInfo);
        finish();
    }

    @Override
    public void onPostAnchorIDCardError(String msg) {
        ToastUtils.showShort(this, msg);
    }

    @Override
    public void onUpLoadSuccess(String url, int type) {
        if (type == IDCARD_HEAD) {
            mHeardUrl = url;
        } else if (type == IDCARD_TAKE) {
            mIdCardUrl = url;
        }
    }
}
