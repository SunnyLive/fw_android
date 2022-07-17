package com.fengwo.module_vedio.mvp.ui.activity.shortvideo;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.TCVideoFileInfo;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_comment.widget.AppTitleBar;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.R2;
import com.fengwo.module_vedio.mvp.presenter.PostShortVideoPresenter;
import com.fengwo.module_vedio.mvp.ui.iview.IPostShortVideoView;
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
public class PostShortVideoActivity extends BaseMvpActivity<IPostShortVideoView, PostShortVideoPresenter> implements IPostShortVideoView {

    private static final int REQUEST_CODE = 10011;
    private static final int IMG_REQUESTCODE = 10001;

    @BindView(R2.id.title)
    AppTitleBar titleBar;
    @BindView(R2.id.viewCategory)
    View categoryView;
    @BindView(R2.id.etTitle)
    EditText etTitle;
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

    private String coverUrl;
    private String videoUrl;
    private int albumId = -1;
    private TCVideoFileInfo tcVideoFileInfo;

    @Override
    public PostShortVideoPresenter initPresenter() {
        return new PostShortVideoPresenter();
    }

    @Override
    protected void initView() {

        tcVideoFileInfo = (TCVideoFileInfo) getIntent().getSerializableExtra("TCVideoFileInfo");
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
        return R.layout.video_activity_post_short_video;
    }

    @OnClick({R2.id.tv_chat_post, R2.id.view_chat_card_location, R2.id.ivPost})
    public void onViewClick(View view) {
        if (view.getId() == R.id.tv_chat_post) {
            postShortVideo();
        } else if (view.getId() == R.id.view_chat_card_location) {
            Intent intent = new Intent(this, SelectSubjectActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        } else if (view.getId() == R.id.ivPost) {
            MImagePicker.openImagePicker(this, MImagePicker.TYPE_IMG, IMG_REQUESTCODE);
        }

    }

    private void postShortVideo() {
        if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            toastTip("请输入短片标题");
            return;
        }
        if (TextUtils.isEmpty(etDesc.getText().toString().trim())) {
            toastTip("请输入短片简介");
            return;
        }
        if (TextUtils.isEmpty(coverUrl)) {
            toastTip("请选择封面");
            return;
        }
//        if (albumId == -1){
//            toastTip("请选择所属专题");
//            return;
//        }
//        p.addShortVideo(map);
        uploadVideo(tcVideoFileInfo.getFilePath());
        showLoadingDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                albumId = data.getIntExtra("albumId", -1);
                tvLocation.setText(data.getStringExtra("title"));
            }
            if (requestCode == IMG_REQUESTCODE) {
//                showLoadingDialog();
//                ImageLoader.loadImg(ivCover,MImagePicker.getImagePathList(data).get(0));
                String url = MImagePicker.getImagePath(this, data);
                if (!TextUtils.isEmpty(url)) {
                    uploadPic(url);
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
        UploadHelper.getInstance(this).doUpload(UploadHelper.TYPE_M_VIDEOS, new File(url), new UploadHelper.OnUploadListener() {
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
                map.put("albumId", albumId);
                map.put("cover", coverUrl);
                map.put("duration", tcVideoFileInfo.getDuration() / 1000);
                map.put("intro", etDesc.getText().toString().trim());
                map.put("isPrivacy", 0);//私密：1是，0否
                map.put("movieTitle", etTitle.getText().toString().trim());
                map.put("url", videoUrl);
                p.addShortVideo(map);
            }

            @Override
            public void onError() {
                if (isLoadingDialogShow()) hideLoadingDialog();
                toastTip("视频上传失败，请重新上传");
            }
        });
    }

    @Override
    public void setAddShortVideo(HttpResult httpResult) {
//        toastTip(httpResult.description);
        if (httpResult.isSuccess()) {
            toastTip("视频上传成功，正在审核");
            finish();
        }
    }
}
