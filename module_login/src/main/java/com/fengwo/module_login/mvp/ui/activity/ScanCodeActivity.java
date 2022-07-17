package com.fengwo.module_login.mvp.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.base.WebViewActivity;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.QRCodeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.UserCodeDto;
import com.fengwo.module_login.utils.UserManager;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

public class ScanCodeActivity extends BaseMvpActivity implements QRCodeView.Delegate {

    private final int REQUEST_GET_GALLERY = 100;

    @BindView(R2.id.zb_view)
    ZBarView zbView;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        new ToolBarBuilder().setTitle("扫一扫")
                .setBackIcon(R.drawable.ic_back_black)
                .setTitleColor(R.color.text_33)
                .setRightText("相册", v -> openImagePicker())
                .setRightTextColor(R.color.text_33)
                .build();
        setTitleBackground(getResources().getColor(android.R.color.transparent));
        zbView.setDelegate(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_scan;
    }

    @Override
    protected void onStart() {
        super.onStart();
        zbView.startCamera();
        zbView.startSpotAndShowRect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        zbView.stopCamera();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        L.e("=-=====" + result);
        try {
            if (result.startsWith("http://") || result.startsWith("https://")) {
                if (result.contains("fengwohuyu") || result.contains("fwhuyu")) {
                    BrowserActivity.start(this, "", result + "&app_token=" + UserManager.getInstance().getToken());
                } else {
                    BrowserActivity.start(this, "", result);
                }
            } else if (result.contains("fwId")) {
                UserCodeDto userCodeDto = new Gson().fromJson(result, UserCodeDto.class);
                int fwId = Integer.parseInt(userCodeDto.fwId);
                if (fwId > 0) {
                    finish();
                    MineDetailActivity.startActivityWithUserId(this, fwId);
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort(ScanCodeActivity.this, "非蜂窝二维码,无法识别");
                    }
                });

            }
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort(ScanCodeActivity.this, "非蜂窝二维码,无法识别");
                }
            });
        }
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        finish();
    }

    @OnClick(R2.id.btn_back)
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_back) finish();
    }

    public void openImagePicker() {
        MImagePicker.openImagePicker(this, MImagePicker.TYPE_IMG, REQUEST_GET_GALLERY);
//        Matisse.from(this)
//                .choose(MimeType.ofImage(), false)
//                .maxSelectable(1)
//                .countable(false)
//                .capture(false)  //是否可以拍照
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                .thumbnailScale(0.85f)
//                .imageEngine(new MyGlideEngine())
//                .forResult(REQUEST_GET_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GET_GALLERY && resultCode == RESULT_OK) {
            String url = MImagePicker.getImagePath(this, data);
            if (!TextUtils.isEmpty(url))
                QRCodeUtils.decodeQR(url, new QRCodeUtils.OnDecodeSuccess() {
                    @Override
                    public void onSuccess(String result) {
                        onScanQRCodeSuccess(result);
                    }
                });
//                zbView.decodeQRCode(url);
        }
    }
}
