/*
 *
 *   设置昵称,头像
 *   用户首次注册，手机登录或者微信登录时
 *
 * */

package com.fengwo.module_login.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.FileUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.UCropUtils;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_comment.widget.TakePicPopwindow;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.eventbus.LoginSuccessEvent;
import com.fengwo.module_login.mvp.presenter.LoginInfoPresenter;
import com.fengwo.module_login.mvp.ui.iview.ILoginInfView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginInfoActivity extends BaseMvpActivity<ILoginInfView, LoginInfoPresenter> implements ILoginInfView {


    private final static int REQUEST_CODE = 10001;
    private final static int REQUEST_TAKE_PHOTO = 10007;


    private RxPermissions rxPermissions;//申请权限
    private TakePicPopwindow takePicPopwindow;//图片裁剪
    private Uri photoUri; //拍照的url

    public static String HEADER_URL = "header_url";
    public static String NICK_NAME = "nick_name";
    public static String BIND_PHONE = "bind_phone";

    private String mNickName;
    private String mHeaderUrl;
    private boolean isBindPhone;//判断是否从微信进入绑定页面过来的


    @BindView(R2.id.iv_login_info_header)
    ImageView mIvHeader;//头像
    @BindView(R2.id.et_login_info_nickname)
    EditText mEtNickName;//昵称
    @BindView(R2.id.tv_login_skip)
    TextView mTvSkip;


    @Override
    public LoginInfoPresenter initPresenter() {
        return new LoginInfoPresenter();
    }

    @Override
    protected void initView() {
        rxPermissions = new RxPermissions(this);
        mHeaderUrl = this.getIntent().getStringExtra(HEADER_URL);
        mNickName = this.getIntent().getStringExtra(NICK_NAME);
        isBindPhone = this.getIntent().getBooleanExtra(BIND_PHONE,false);
        mEtNickName.setText(mNickName);
        mEtNickName.setSelection(mEtNickName.length());
        mTvSkip.setText(getString(R.string.text_skip));
        ImageLoader.loadImg(mIvHeader, mHeaderUrl, R.drawable.icon_login_info_header);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login_info;
    }

    @SuppressLint("CheckResult")
    @OnClick({
            R2.id.iv_login_info_header,
            R2.id.btn_login_info_save,
            R2.id.tv_login_skip,
            R2.id.btn_login_info_last})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.iv_login_info_header) {
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            //图片选择器
                            showTakePhotoPoo();
                        } else {
                            toastTip("您关闭了权限，请去设置页面开启");
                        }
                    });
        }
        //保存头像url 用户昵称提交到服务器
        else if (view.getId() == R.id.btn_login_info_save) {
            if (isNikeNameEmpty()) {
                toastTip("昵称不能为空");
                return;
            }
            showLoadingDialog();
            p.saveUserInfo(mHeaderUrl, mNickName);
        }
        //返回上一层页面
        else if (view.getId() == R.id.btn_login_info_last) {
            onBackPressed();
        }
        //跳过此步骤，下次再次进入
        else if (view.getId() == R.id.tv_login_skip) {
            showLoadingDialog();
            p.skipUserInfo();
        }
    }

    private boolean isNikeNameEmpty() {
        mNickName = mEtNickName.getText().toString();
        return TextUtils.isEmpty(mNickName);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取相册图片
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String url = MImagePicker.getImagePath(this, data);
            if (TextUtils.isEmpty(url)) return;
            File file = new File(url);
            UCropUtils.startCrop(this, Uri.fromFile(file));
        }
        //拉起系统相机拍照后的回调
        else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {//拍照处理
            if (photoUri != null) {
                UCropUtils.startCrop(this, photoUri);
            }
        }
        //拉起裁剪，裁剪完成后的回调
        //得到图片上传到cdn
        else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            if (null == data) return;
            showLoadingDialog();
            Uri outputUri = UCrop.getOutput(data);
            File outputFile = new File(outputUri.getPath());
            UploadHelper.getInstance(getApplicationContext()).doUpload(UploadHelper.TYPE_IMAGE,
                    outputFile, new UploadHelper.OnUploadListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onLoading(long cur, long total) {
                        }

                        @Override
                        public void onSuccess(String url) {
                            hideLoadingDialog();
                            runOnUiThread(() -> {
                                mHeaderUrl = url;
                                ImageLoader.loadImg(mIvHeader, mHeaderUrl, R.drawable.icon_login_info_header);
                            });
                        }

                        @Override
                        public void onError() {
                            hideLoadingDialog();
                            toastTip("上传失败");
                        }
                    });
        }
    }

    /**
     * 显示弹窗window
     */
    private void showTakePhotoPoo() {
        if (null == takePicPopwindow) {
            takePicPopwindow = new TakePicPopwindow(LoginInfoActivity.this, 0);
        }
        takePicPopwindow.setOnTakePopwindowClickListener(new TakePicPopwindow.OnTakePopwindowClickListener() {
            @Override
            public void onTakeClick() {
                takePhoto();
                takePicPopwindow.dismiss();
            }

            @Override
            public void onChooseClick() {
                MImagePicker.openImagePicker(LoginInfoActivity.this, MImagePicker.TYPE_IMG, REQUEST_CODE);
                takePicPopwindow.dismiss();
            }

            @Override
            public void onDeleteClick() {

            }
        });
        takePicPopwindow.showPopupWindow();
    }


    @SuppressLint("CheckResult")
    private void takePhoto() {
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    try {
                        if (granted) {
                            String take_cam_fileName = TimeUtils.getCurrentTimeInLong() + ".jpg";
                            // 步骤一：创建存储照片的文件
                            String path = FileUtils.SD_PATH;
                            File file = new File(path, take_cam_fileName);
                            if (!file.getParentFile().exists())
                                file.getParentFile().mkdirs();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                //步骤二：Android 7.0及以上获取文件 Uri
                                photoUri = FileProvider.getUriForFile(LoginInfoActivity.this, "fengwoImg", file);
                            } else {
                                //步骤三：获取文件Uri
                                photoUri = Uri.fromFile(file);
                            }
                            //步骤四：调取系统拍照
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                        } else {
                            Toast.makeText(this, "您关闭了权限，请去设置页面开启", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        L.e(LoginInfoActivity.class.getName(), e.getMessage());
                    }

                });
    }

    @Override
    public void success() {
        hideLoadingDialog();
        ARouter.getInstance().build(ArouterApi.MAIN_MAIN_ACTIVITY).navigation();
        if (isBindPhone) RxBus.get().post(new LoginSuccessEvent());
        onBackPressed();
    }

    @Override
    public void fail(String message) {
        hideLoadingDialog();
        Toast t = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }

    @Override
    public void error(String e) {
        hideLoadingDialog();
        L.e(LoginInfoActivity.class.getName(), e);
    }
}
