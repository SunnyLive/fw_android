package com.fengwo.module_login.mvp.ui.pop;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.BitmapUtil;
import com.fengwo.module_comment.utils.CommonUtil;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.QRCodeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.mvp.ui.df.ShareCodeDialog;
import com.fengwo.module_live_vedio.mvp.ui.pop.ShareCommonPopwindow;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.UserCodeDto;
import com.fengwo.module_comment.utils.ShareHelper;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_login.widget.RatioImageView;
import com.google.gson.Gson;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import razerdp.basepopup.BasePopupWindow;

public class QrCodePopwindow extends BasePopupWindow {

    private Unbinder bind;
    @BindView(R2.id.ivQrCode)
    ImageView ivQrCode;
    @BindView(R2.id.tvName)
    TextView tvName;
    @BindView(R2.id.cv_avator)
    ImageView cvAvator;
    @BindView(R2.id.tvFwId)
    TextView tvFwId;
    @BindView(R2.id.rootContent)
    ConstraintLayout rootContent;

    public QrCodePopwindow(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        initUI();
    }

    private void initUI() {
        UserInfo userInfo = UserManager.getInstance().getUser();
        ImageLoader.loadImg(cvAvator, userInfo.headImg);
        tvName.setText(userInfo.nickname);
        tvFwId.setText(userInfo.fwId);

        getContext().getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                UserCodeDto bean = new UserCodeDto();
                bean.fwId = userInfo.fwId;
                QRCodeUtils.generQRCode(new Gson().toJson(bean), ivQrCode.getWidth(), getContext().getResources().getColor(R.color.black_000000), new QRCodeUtils.OnGenerQRCodeListener() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        ivQrCode.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popwindow_qrcode);
        bind = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bind != null) bind.unbind();
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultAlphaAnimation();
    }

    protected void showShareDialog() {
        ShareCommonPopwindow shareCommonPopwindow = new ShareCommonPopwindow(getContext());
        shareCommonPopwindow.addClickListener(new ShareCommonPopwindow.OnShareClickListener() {
            @Override
            public void onWx() {
                ShareHelper.get().shareImage(getContext(), BitmapUtil.createViewBitmap(rootContent), SHARE_MEDIA.WEIXIN);
            }

            @Override
            public void onWxCircle() {
                ShareHelper.get().shareImage(getContext(), BitmapUtil.createViewBitmap(rootContent), SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        });
        shareCommonPopwindow.showPopupWindow();
    }

    @OnClick({R2.id.tvShare, R2.id.tvSave, R2.id.root, R2.id.iv_close_qr})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.tvShare) {
            showShareDialog();
        } else if (id == R.id.tvSave) {
            BitmapUtil.saveImageFromBitmap(getContext().getApplicationContext(), CommonUtil.getCameraPath(),
                    BitmapUtil.createViewBitmap(rootContent), Bitmap.CompressFormat.PNG, null, 100);
            ToastUtils.showShort(getContext(), "保存成功");
        }else if (id == R.id.iv_close_qr) {
           dismiss();
        }
    }
}
