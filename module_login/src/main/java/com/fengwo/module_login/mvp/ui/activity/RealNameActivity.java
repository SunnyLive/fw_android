/*
 *  为后辈做贡献
 *  在此打下注释
 *
 *  此页面是实名认证页面
 *
 *  实名认证成功后 弹框提醒用户是否要进行主播认证
 *
 *  ---------------------------------------
 *
 *  ---------------------------------------
 *
 * */

package com.fengwo.module_login.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.RegexUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_comment.widget.LoadingProgressDialog;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.MineCardDto;
import com.fengwo.module_login.utils.UserManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ArouterApi.REALNAME_ACTIVITY)
public class RealNameActivity extends BaseMvpActivity {

    private final static String KEY_STATUS = "status";
    private final static String KEY_TYPE = "type";

    public final static int SKIP_ANCHOR = 0x0001;             //跳转到主播认证
    public final static int SKIP_ANCHOR_STATUS_NO = 0x0002;   //跳转到主播认证_实名认证失败
    public final static int SKIP_EXPERT = 0x0003;             //跳转到达人认证
    public final static int SKIP_EXPERT_STATUS_NO = 0x0004;   //跳转到达人认证_实名认证失败

    private static final int IDCARD_HEAD = 10002;
    private static final int IDCARD_BACK = 10003;

    private String imgHead, imgBack;

    @BindView(R2.id.et_name)
    EditText etName;
    @BindView(R2.id.et_id_card)
    EditText etIdCard;
    @BindView(R2.id.iv_id_card_head)
    ImageView ivIdCardHead; //身份证正面照片
    @BindView(R2.id.iv_id_card_back)
    ImageView ivIdCardBack; //身份证背面照片
    @BindView(R2.id.btn_submit)
    Button mbtSubmit;
    @BindView(R2.id.tv_rule)
    TextView tvRule;

    private LoginApiService service;

    private int type;//记录实名认证从那个入口而来

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    LoadingProgressDialog dialog;

    public static void start(Context context, int status) {
        Intent i = new Intent(context, RealNameActivity.class);
        i.putExtra(KEY_STATUS, status);
        context.startActivity(i);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        type = getIntent().getIntExtra(KEY_TYPE, 0);
        new ToolBarBuilder().setTitle(type == 1 ? "主播认证" : "达人认证").setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black).showBack(true).build();
//        setTitleBackground(getResources().getColor(R.color.white));
        service = new RetrofitUtils().createApi(LoginApiService.class);
        new RxPermissions(this).request(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
            } else {
                toastTip("您关闭了权限，请去设置页面开启");
            }
        });
        dialog = new LoadingProgressDialog(this);
    }


    /**
     * 实名认证
     * <p>
     * cardImgBack	string  身份证反面照
     * <p>
     * cardImgHead	string  身份证正面照
     * <p>
     * idCardNum	string  身份证号码
     * <p>
     * realName*	string   名字
     *
     * @param cardNum
     * @param realName
     */
    private void realName(String cardNum, String realName) {
        netManager.add(
                service.realIdCard(new HttpUtils.ParamsBuilder()
                        .put("cardImgBack", imgBack)
                        .put("cardImgHead", imgHead)
                        .put("idCardNum", cardNum)
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

                                    CustomerDialog.Builder builder = new CustomerDialog.Builder(RealNameActivity.this);
                                    builder.setTitle("");
                                    builder.setMsg("实名认证成功");
                                    //type == 1 是主播  2 是达人
                                    if (type == SKIP_EXPERT) {
                                        //开通达人
                                        CustomerDialog customerDialog = builder.setPositiveButton("开通达人", () -> {
                                            ARouter.getInstance().build(ArouterApi.FLIRT_CERTIFICATION)
                                                    .navigation();
                                        }).create();
                                        customerDialog.show();
                                        customerDialog.hideCancle();
                                    } else if (type == SKIP_ANCHOR) {
                                        //进入直播申请


                                    }

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

    @Override
    protected int getContentView() {
        return R.layout.login_activity_realname;
    }


    @OnClick({
            R2.id.btn_id_card_head, //身份证正面
            R2.id.iv_btn_id_card_head, //身份证正面
            //R2.id.btn_idcard_head,
            R2.id.btn_id_card_back, //身份证背面
            R2.id.iv_btn_id_card_back, //身份证背面
            //R2.id.view_bottom,  //
            //R2.id.btn_getcode,    //验证码
            R2.id.btn_submit,     //提交
            R2.id.tv_rule         //协议
    })
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_id_card_head) {
            MImagePicker.openImagePicker(RealNameActivity.this, MImagePicker.TYPE_IMG, IDCARD_HEAD);
        }
        //查看身份证正面
        else if (id == R.id.iv_btn_id_card_head) {
            if (TextUtils.isEmpty(imgHead)) return;
            List<MineCardDto.CoverBean> cover = new ArrayList<>();
            MineCardDto.CoverBean coverBean = new MineCardDto.CoverBean();
            coverBean.setImageUrl(imgHead);
            cover.add(coverBean);
            ARouter.getInstance().build(ArouterApi.FLIRT_DETAIL_CARD_ACTION)
                    .withSerializable("bannerList", (Serializable) cover)
                    .withInt("position", 0)
                    .navigation();
        } else if (id == R.id.btn_id_card_back) {
            MImagePicker.openImagePicker(RealNameActivity.this, MImagePicker.TYPE_IMG, IDCARD_BACK);
        }
        //查看身份证背面
        else if (id == R.id.iv_btn_id_card_back) {
            if (TextUtils.isEmpty(imgBack)) return;
            List<MineCardDto.CoverBean> cover = new ArrayList<>();
            MineCardDto.CoverBean coverBean = new MineCardDto.CoverBean();
            coverBean.setImageUrl(imgBack);
            cover.add(coverBean);
            ARouter.getInstance().build(ArouterApi.FLIRT_DETAIL_CARD_ACTION)
                    .withSerializable("bannerList", (Serializable) cover)
                    .withInt("position", 0)
                    .navigation();
        } else if (id == R.id.tv_rule) {
            BrowserActivity.startRuleActivity(this);
        } else if (id == R.id.btn_submit) {
            String name, cardNum;
            if (checkInput(etName) && RegexUtils.isChinese(etName.getText())) {
                name = etName.getText().toString();
            } else {
                toastTip("请输入正确的姓名！");
                return;
            }
            if (checkInput(etIdCard) && RegexUtils.isIDCard18Exact(etIdCard.getText())) {
                cardNum = etIdCard.getText().toString();
            } else {
                toastTip("请输入正确的身份证号码");
                return;
            }
            if (TextUtils.isEmpty(imgHead) || TextUtils.isEmpty(imgBack)) {
                toastTip("请根据图示，上传完整照片");
                return;
            }
            //realName(mobile, name, code);
            realName(cardNum, name);

        }
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

    private boolean checkInput(EditText et) {
        String str = et.getText().toString().trim();
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String path = MImagePicker.getImagePath(this, data);
            File f = new File(path);
            showLoadingDialog();
            switch (requestCode) {
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
                                    ImageLoader.loadImg(ivIdCardHead, url);
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
                                    ImageLoader.loadImg(ivIdCardBack, url);
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
}
