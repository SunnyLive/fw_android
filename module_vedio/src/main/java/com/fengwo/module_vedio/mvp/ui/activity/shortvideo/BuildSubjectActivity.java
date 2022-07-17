package com.fengwo.module_vedio.mvp.ui.activity.shortvideo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.MImagePicker;

import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.R2;
import com.fengwo.module_vedio.mvp.presenter.BuildSubjectPresenter;
import com.fengwo.module_vedio.mvp.ui.iview.IBuildSubjectView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/6
 */
@Route(path = ArouterApi.BUILD_SUBJECT)
public class BuildSubjectActivity extends BaseMvpActivity<IBuildSubjectView, BuildSubjectPresenter> implements IBuildSubjectView {
    private static final int IMG_REQUESTCODE = 10001;

    @BindView(R2.id.iv_cover)
    ImageView ivCover;
    @BindView(R2.id.tv_set_cover)
    TextView tvSetCover;
    @BindView(R2.id.fl_cover)
    FrameLayout flCover;
    @BindView(R2.id.et_subject_name)
    EditText etSubjectName;
    @BindView(R2.id.tv_sure)
    GradientTextView tvSure;

    private String netUrl;

    @Override
    public BuildSubjectPresenter initPresenter() {
        return new BuildSubjectPresenter();
    }

    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("创建专题")
                .setTitleColor(R.color.text_33)
                .build();

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
        return R.layout.activity_build_subject;
    }

    @OnClick({R2.id.fl_cover, R2.id.tv_sure})
    void onClick(View v) {
        if (v.getId() == R.id.fl_cover) {
            MImagePicker.openImagePicker(this, MImagePicker.TYPE_IMG, IMG_REQUESTCODE);
        } else if (v.getId() == R.id.tv_sure) {
            if (isFastClick()) {
                return;
            }
            if (TextUtils.isEmpty(etSubjectName.getText().toString().trim())) {
                toastTip("请输入专辑名称");
                return;
            }
            if (TextUtils.isEmpty(netUrl)) {
                toastTip("请上传封面");
                return;
            }
            addAlbum();
        }
    }

    private void addAlbum() {
        Map map = new HashMap();
        map.put("cover", netUrl);
        map.put("name", etSubjectName.getText().toString().trim());
        p.addAlbum(map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMG_REQUESTCODE) {
//                showLoadingDialog();
//                ImageLoader.loadImg(ivCover,MImagePicker.getImagePathList(data).get(0));
                String url = MImagePicker.getImagePath(this, data);
                if (!TextUtils.isEmpty(url)) {
                    upLoad(url);
                    showLoadingDialog();
                }
            }
        }
    }

    private void upLoad(String url) {
        UploadHelper.getInstance(this).doUpload(UploadHelper.TYPE_IMAGE, new File(url), new UploadHelper.OnUploadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onLoading(long cur, long total) {

            }

            @Override
            public void onSuccess(String url) {
                if (isLoadingDialogShow()) hideLoadingDialog();
                netUrl = url;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageLoader.loadImg(ivCover, url);
                    }
                });
//                tvSetCover.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                if (isLoadingDialogShow()) hideLoadingDialog();
                toastTip("图片上传失败，请重新上传");
            }
        });
    }

    @Override
    public void setAddAlbum(HttpResult httpResult) {
        if (httpResult.isSuccess()) {
            toastTip("创建专题成功");
            finish();
        }
    }
}
