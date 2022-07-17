package com.fengwo.module_vedio.mvp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.MapLocationUtil;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.TCVideoFileInfo;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_comment.widget.AppTitleBar;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.R2;
import com.fengwo.module_vedio.mvp.presenter.PostSmallVideoPresenter;
import com.fengwo.module_vedio.mvp.ui.iview.IPostSmallVideoView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Zachary
 * @date 2020/1/2
 */
@Route(path = ArouterApi.POST_SMALL_VIDEO)
public class PostSmallVideoActivity extends BaseMvpActivity<IPostSmallVideoView, PostSmallVideoPresenter> implements IPostSmallVideoView {

    private static final int REQUEST_CODE = 10011;
    private static final int IMG_REQUESTCODE = 10001;

    @BindView(R2.id.title)
    AppTitleBar titleBar;
    @BindView(R2.id.viewCategory)
    View categoryView;
    @BindView(R2.id.tvCategory)
    TextView tvCategory;
    @BindView(R2.id.etDesc)
    EditText etDesc;
    @BindView(R2.id.viewPost)
    View postView;
    @BindView(R2.id.ivPost)
    ImageView ivPost;
    @BindView(R2.id.view_chat_card_location)
    View locationView;
    @BindView(R2.id.tv_chat_card_location)
    TextView tvLocation;
    @BindView(R2.id.switch_location)
    Switch locationSwitch;

    @Autowired(name = "TCVideoFileInfo")
    TCVideoFileInfo tcVideoFileInfo;
    private String coverUrl;
    private String videoUrl;
    private int menuId;
    private double latitude;
    private double longitude;

    @Override
    public PostSmallVideoPresenter initPresenter() {
        return new PostSmallVideoPresenter();
    }

    @Override
    protected void initView() {
        locationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                MapLocationUtil.getInstance().startLocationForOnce(new MapLocationUtil.LocationListener() {
                    @Override
                    public void onLocationSuccess(AMapLocation location) {
                        tvLocation.setText(location.getCity());
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }

                    @Override
                    public void onLocationFailure(String msg) {
                        locationSwitch.setChecked(false);
                    }
                });
            }
        });

        tcVideoFileInfo = (TCVideoFileInfo) getIntent().getExtras().getSerializable("TCVideoFileInfo");
        if (tcVideoFileInfo.getThumbPath() != null) {
            uploadPic(tcVideoFileInfo.getThumbPath());
        }

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                    } else {
                        toastTip("您关闭了权限，请去设置页面开启");
                    }
                });
    }

    @Override
    protected int getContentView() {
        return R.layout.video_activity_post_video;
    }

    @OnClick({R2.id.tv_chat_post, R2.id.ivPost, R2.id.viewCategory})
    public void onViewClick(View view) {
        if (view.getId() == R.id.tv_chat_post) {
            postShortVideo();
        } else if (view.getId() == R.id.ivPost) {
            MImagePicker.openImagePicker(this, MImagePicker.TYPE_IMG, IMG_REQUESTCODE);
        } else if (view.getId() == R.id.viewCategory) {
            Intent intent = new Intent(this, SelectSmallVideoMenuActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    private void postShortVideo() {
        if (TextUtils.isEmpty(tvCategory.getText().toString().trim())) {
            toastTip("请选择内容分类");
            return;
        }
        if (TextUtils.isEmpty(etDesc.getText().toString().trim())) {
            toastTip("请输入小视频简介");
            return;
        }
        if (TextUtils.isEmpty(coverUrl)) {
            toastTip("请选择封面");
            return;
        }
//        p.addShortVideo(map);
        uploadVideo(tcVideoFileInfo.getFilePath());
        showLoadingDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                tvCategory.setText(data.getStringExtra("title"));
                menuId = data.getIntExtra("menuId", 0);
            }
            if (requestCode == IMG_REQUESTCODE) {
                if (!TextUtils.isEmpty(MImagePicker.getImagePath(this, data))) {
                    uploadPic(MImagePicker.getImagePath(this, data));
                    showLoadingDialog();
                }
            }
        }
    }

    private void uploadPic(String oldUrl) {
        UploadHelper.getInstance(this).doUpload(UploadHelper.TYPE_IMAGE, new File(oldUrl), new UploadHelper.OnUploadListener() {
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
                if (isLoadingDialogShow()) hideLoadingDialog();
                coverUrl = url;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageLoader.loadImgNoCache(ivPost, url);
                    }
                });
                File file = new File(oldUrl);
                if (file.exists()) {
                    boolean isDelete = file.delete();
                    L.e("isDelete" + isDelete);
                }
            }

            @Override
            public void onError() {
                if (isLoadingDialogShow()) hideLoadingDialog();
                toastTip("图片上传失败，请重新上传");
            }
        });
    }

    private void uploadVideo(String url) {
        UploadHelper.getInstance(this).doUpload(UploadHelper.TYPE_S_VIDEOS, new File(url), new UploadHelper.OnUploadListener() {
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
                if (isLoadingDialogShow()) hideLoadingDialog();
                videoUrl = url;
                Map map = new HashMap();
                map.put("menuId", menuId);
                map.put("cover", coverUrl);
                map.put("latitude", latitude);
                map.put("longitude", longitude);
                map.put("duration", tcVideoFileInfo.getDuration() / 1000);
                map.put("isPrivacy", 0);//私密：1是，0否
                map.put("videoTitle", etDesc.getText().toString().trim());
                map.put("des", etDesc.getText().toString().trim());
                map.put("url", videoUrl);
                p.addSmallVideo(map);

            }

            @Override
            public void onError() {
                if (isLoadingDialogShow()) hideLoadingDialog();
                toastTip("视频上传失败，请重新上传");
            }
        });
    }

    @Override
    public void setAddSmallVideo(HttpResult httpResult) {
//        toastTip(httpResult.description);
        if (httpResult.isSuccess()) {
            toastTip("视频上传成功，正在审核");
            ARouter.getInstance().build(ArouterApi.MAIN_MAIN_ACTIVITY).navigation();
            finish();
        }
    }
}
