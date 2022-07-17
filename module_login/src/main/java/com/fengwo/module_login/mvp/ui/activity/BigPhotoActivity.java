package com.fengwo.module_login.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.FileUtils;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.utils.UCropUtils;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_comment.widget.TakePicPopwindow;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.eventbus.UpdateUserinfo;
import com.fengwo.module_login.utils.UserManager;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/6/28
 */
public class BigPhotoActivity extends BaseMvpActivity {
    private final static int REQUEST_CODE = 10001;
    private final static int REQUEST_NAME_CODE = 10002;
    private final static int REQUEST_DES_CODE = 10003;
    private final static int REQUEST_SEX_CODE = 10004;
    private final static int REQUEST_ADDRESS_CODE = 10005;
    private final static int REQUEST_BIRTHDAY_CODE = 10006;
    private final static int REQUEST_TAKE_PHOTO = 10007;

    @Autowired
    UserProviderService userProviderService;

    @BindView(R2.id.status_bar_view)
    View statusBarView;
    @BindView(R2.id.btn_back)
    ImageView btnBack;
    @BindView(R2.id.iv_left2)
    ImageView ivLeft2;
    @BindView(R2.id.title_tv)
    TextView titleTv;
    @BindView(R2.id.right1_img)
    ImageView right1Img;
    @BindView(R2.id.right2_img)
    ImageView right2Img;
    @BindView(R2.id.right_tv)
    TextView rightTv;
    @BindView(R2.id.tool_bar)
    RelativeLayout toolBar;
    @BindView(R2.id.basetitle)
    LinearLayout basetitle;
    @BindView(R2.id.iv_big_photo)
    ImageView ivBigPhoto;

    private String url;
    private String nick;
    private int uid;
    private Uri photoUri;

    private TakePicPopwindow takePicPopwindow;
    private RxPermissions rxPermissions;
    UserInfo userInfo;

    public static void start(Activity context, int uid, String url,String nick){
        Intent intent = new Intent( context,BigPhotoActivity.class);
        intent.putExtra("uid",uid);
        intent.putExtra("url",url);
        intent.putExtra("nick",nick);
        context.startActivityForResult(intent,-1);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    @Override
    protected void initView() {
        setTitleBackground(Color.parseColor("#333333"));
        uid = getIntent().getIntExtra("uid",-1);
        url = getIntent().getStringExtra("url");
        nick = getIntent().getStringExtra("nick");
        rxPermissions = new RxPermissions(this);
        userInfo = userProviderService.getUserInfo();
        ImageLoader.loadImgFitCenter(ivBigPhoto,url);
        if (uid == userInfo.getId()){
            right1Img.setVisibility(View.VISIBLE);
            titleTv.setText("更换头像");
        }else {
            titleTv.setText(nick+"的头像");
            right1Img.setVisibility(View.GONE);
        }
        right1Img.setImageResource(R.drawable.ic_mine_more);
        right1Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == takePicPopwindow) {
                    takePicPopwindow = new TakePicPopwindow(BigPhotoActivity.this,0);
                    takePicPopwindow.setOnTakePopwindowClickListener(new TakePicPopwindow.OnTakePopwindowClickListener() {
                        @Override
                        public void onTakeClick() {
                            takePhoto();
                            takePicPopwindow.dismiss();
                        }

                        @Override
                        public void onChooseClick() {
                            MImagePicker.openImagePicker(BigPhotoActivity.this, MImagePicker.TYPE_IMG, REQUEST_CODE);
//                        MImagePicker.startPick(EdittextInfoActivity.this, 1);
                            takePicPopwindow.dismiss();
                        }

                        @Override
                        public void onDeleteClick() {

                        }
                    });
                }
                rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {
                                takePicPopwindow.showPopupWindow();
                            } else {
                                toastTip("您关闭了权限，请去设置页面开启");
                            }
                        });
            }
        });
    }

    @OnClick(R2.id.btn_back)
    void onClick(View v){
        RxBus.get().post(new UpdateUserinfo());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_big_photo;
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
                                photoUri = FileProvider.getUriForFile(BigPhotoActivity.this, "fengwoImg", file);
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
                    }catch (Exception e){

                    }

                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String url = MImagePicker.getImagePath(this, data);
            if (TextUtils.isEmpty(url)) return;
            File file = new File(url);
            UCropUtils.startCrop(this, Uri.fromFile(file));
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {//拍照处理
            if (photoUri != null) {
                UCropUtils.startCrop(this, photoUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
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
                            userInfo.headImg = url;
                            hideLoadingDialog();
                            changeUserInfo();
                        }

                        @Override
                        public void onError() {

                        }
                    });

        } else if (resultCode == RESULT_OK) {
            ImageLoader.loadImgFitCenter(ivBigPhoto,userInfo.headImg);
        }
    }

    private void changeUserInfo() {
        new RetrofitUtils().createApi(LoginApiService.class)
                .updateUserinfo(new HttpUtils.ParamsBuilder()
                        .put("sex", userInfo.sex + "")
                        .put("location", userInfo.location)
                        .put("headImg", userInfo.headImg)
                        .put("birthday", userInfo.birthday)
                        .build()
                )
                .compose(io_main())
                .compose(bindToLifecycle())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
//                            userInfo.constellation = astro;
                            UserManager.getInstance().setUserInfo(userInfo);
//                            RxBus.get().post(new UpdateUserinfo());
                            ImageLoader.loadImgFitCenter(ivBigPhoto,userInfo.headImg);
                        } else if (data.isServiceErr()) {
                            ToastUtils.showShort(BigPhotoActivity.this, data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(RESULT_OK);
    }
}
