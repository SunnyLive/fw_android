package com.fengwo.module_login.mvp.ui.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.BitmapUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.QRCodeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.UserCodeDto;
import com.fengwo.module_login.mvp.ui.pop.ShareCodeDialog;
import com.fengwo.module_login.utils.UserManager;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyCodeActivity extends BaseMvpActivity implements ShareCodeDialog.OnItemClickListener {

    @BindView(R2.id.iv_code)
    ImageView ivCode;
    @BindView(R2.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R2.id.iv_header_code)
    ImageView ivHeaderCode;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_id)
    TextView tvId;
    @BindView(R2.id.clClipView)
    View clipView;

    private UserInfo userInfo;
    private Bitmap headerBp;
    private ShareCodeDialog dialog;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        setWhiteTitle("我的二维码");
        setUserInfo();
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                UserCodeDto bean = new UserCodeDto();
                bean.fwId = userInfo.fwId;
                QRCodeUtils.generQRCode(new Gson().toJson(bean), ivCode.getWidth(), getResources().getColor(R.color.tab_primary_Dark), new QRCodeUtils.OnGenerQRCodeListener() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        ivCode.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    private void setUserInfo() {
        userInfo = UserManager.getInstance().getUser();
        ImageLoader.loadImg(ivHeader, userInfo.headImg);
        ImageLoader.loadImg(ivHeaderCode, userInfo.headImg);
        tvName.setText(userInfo.nickname);
        tvId.setText("蜂窝ID: " + userInfo.fwId);

    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_mycode;
    }


    @OnClick({R2.id.btn_saoyisao, R2.id.btn_savecode, R2.id.btn_share})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_saoyisao) {
            startActivity(ScanCodeActivity.class);
        } else if (id == R.id.btn_savecode) {
            Bitmap m = BitmapUtils.createViewBitmap(clipView);
            BitmapUtils.saveBitmap(m, this);
        } else if (id == R.id.btn_share) {
            showShareDialog();
        }
    }

    private void showShareDialog() {
        if (dialog == null) {
            dialog = new ShareCodeDialog();
            dialog.setOnItemClickListener(this);
        }
        dialog.show(getSupportFragmentManager(), "share");
    }

    @Override
    public void WeiXinShare() {
        share(SHARE_MEDIA.WEIXIN);
    }

    @Override
    public void WeiXinCircleShare() {
        share(SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    private void share(SHARE_MEDIA shareMedia) {
        Bitmap bitmap = BitmapUtils.createViewBitmap(clipView);
        UMImage image = new UMImage(this, bitmap);
        new ShareAction(this)
                .setPlatform(shareMedia)
                .withMedia(image)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        ToastUtils.showShort(MyCodeActivity.this, "分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                    }
                }).share();
    }
}
