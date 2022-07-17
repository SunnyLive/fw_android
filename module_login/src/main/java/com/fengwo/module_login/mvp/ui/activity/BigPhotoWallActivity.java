package com.fengwo.module_login.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.FileUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.UCropUtils;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_comment.widget.TakePicPopwindow;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.eventbus.UpdatePhotoEvent;
import com.fengwo.module_login.mvp.presenter.BigPhotoWallPresenter;
import com.fengwo.module_login.mvp.ui.iview.IBigPhotoWallView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author dqm
 */
public class BigPhotoWallActivity extends BaseMvpActivity<IBigPhotoWallView, BigPhotoWallPresenter> implements  IBigPhotoWallView {
    private final static int REQUEST_CODE = 10001;
    private final static int REQUEST_TAKE_PHOTO = 10007;
    private Uri photoUri;
    private int mId;
    private String mUrl;
    private String mUpdateUrl;
    @BindView(R2.id.iv_photo)
    ImageView mIvPhoto;
    @BindView(R2.id.right_img)
    ImageView mIvRight;
    @BindView(R2.id.tv_status)
    TextView mTvStatus;
    private int mPosition;
    private int mStatus;
    private TakePicPopwindow takePicPopwindow;

    public static void start(Activity context, int id, String url,int position,int status) {
        Intent intent = new Intent(context, BigPhotoWallActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("url", url);
        intent.putExtra("position",position);
        intent.putExtra("status",status);
        context.startActivityForResult(intent, EdittextInfoActivity.REQUEST_DELETE_PHOTO);
    }

    @Override
    public BigPhotoWallPresenter initPresenter() {
        return new BigPhotoWallPresenter();
    }



    @Override
    protected int getContentView() {
        return R.layout.activity_bigi_photo_wall;
    }

    @Override
    protected void initView() {
        mId = getIntent().getIntExtra("id", 0);
        mUrl = getIntent().getStringExtra("url");
        mPosition = getIntent().getIntExtra("position",0);
        mStatus = getIntent().getIntExtra("status",0);
        ImageLoader.loadImg(mIvPhoto, mUrl);
        mIvRight.setVisibility(mStatus == 0 ? View.GONE : View.VISIBLE );      //0审核中 1审核通过  2:审核失败
        mTvStatus.setVisibility(mStatus == 2 ? View.VISIBLE : View.GONE);


    }

    @OnClick({R2.id.tv_live_photo, R2.id.btn_back, R2.id.right_img})
    void onClick(View v) {
        if (v.getId() == R.id.tv_live_photo) {


        } else if (v.getId() == R.id.btn_back) {
            finish();
        } else if (v.getId() == R.id.right_img) {
            if (takePicPopwindow == null)
                takePicPopwindow = new TakePicPopwindow(this,1);
            takePicPopwindow.setOnTakePopwindowClickListener(new TakePicPopwindow.OnTakePopwindowClickListener() {
                @Override
                public void onTakeClick() {
                    takePhoto();
                    takePicPopwindow.dismiss();
                }

                @Override
                public void onChooseClick() {
                    MImagePicker.openImagePicker(BigPhotoWallActivity.this, MImagePicker.TYPE_IMG, REQUEST_CODE);
                    takePicPopwindow.dismiss();
                }

                @Override
                public void onDeleteClick() {

                    CustomerDialog dialog = new CustomerDialog.Builder(BigPhotoWallActivity.this).setTitle("温馨提示").setMsg("是否删除图片")
                            .setNegativeButton("取消", () -> {

                            })
                            .setPositiveButton("确定", () -> {
                                p.userDeletePhotos(mId);
                            }).create();
                    dialog.show();


                }
            });
            takePicPopwindow.showPopupWindow();
        }

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
                                photoUri = FileProvider.getUriForFile(BigPhotoWallActivity.this, "fengwoImg", file);
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
                            mUrl = url;
                            hideLoadingDialog();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    p.userUpdatePhotos(mId,url);
                                }
                            });
                        }
                        @Override
                        public void onError() {

                        }
                    });

        }
    }


    @Override
    public void deleteSuccess() {
        Intent intent = new Intent();
        intent.putExtra("position",mPosition);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void updateSuccess() {
        ImageLoader.loadImg(mIvPhoto, mUrl);
        RxBus.get().post(new UpdatePhotoEvent(mPosition,mUrl,0));
    }
}
