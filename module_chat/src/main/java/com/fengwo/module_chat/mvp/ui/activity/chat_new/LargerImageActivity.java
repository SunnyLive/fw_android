package com.fengwo.module_chat.mvp.ui.activity.chat_new;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.widget.LargerImage.ImageSource;
import com.fengwo.module_comment.widget.LargerImage.RxScaleImageView;
import com.scwang.smart.refresh.header.material.CircleImageView;


import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

public class LargerImageActivity extends BaseMvpActivity {


    @BindView(R2.id.iv_larger_icon)
    RxScaleImageView ivLargerIcon;
    private static final String IMAGE_URL = "IMAGE_URL";

    public static void start(Context context, String imgUrl) {
        Intent intent = new Intent(context, LargerImageActivity.class);
        intent.putExtra(IMAGE_URL, imgUrl);
        context.startActivity(intent);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
       Flowable.create((FlowableOnSubscribe<Bitmap>) emitter -> {
           String imgUrl = getIntent().getStringExtra(IMAGE_URL);
           Bitmap bitmap = ImageLoader.getBitmap(LargerImageActivity.this, imgUrl);
           emitter.onNext(bitmap);
       }, BackpressureStrategy.ERROR).compose(RxUtils.applySchedulers2()).subscribeWith(new LoadingObserver<Bitmap>() {
            @Override
            public void _onNext(Bitmap data) {
                ivLargerIcon.setImage(ImageSource.bitmap(data));
            }
            @Override
            public void _onError(String msg) {
                toastTip("图片解析错误");
                finish();
            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_larger_image;
    }

    @OnClick(R2.id.iv_close)
    public void onClick() {
        finish();
    }
}
