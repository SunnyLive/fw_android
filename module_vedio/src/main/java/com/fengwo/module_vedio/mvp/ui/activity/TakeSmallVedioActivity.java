package com.fengwo.module_vedio.mvp.ui.activity;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TakeSmallVedioActivity extends BaseMvpActivity implements Camera.PreviewCallback {

    @BindView(R2.id.surface_view)
    SurfaceView surfaceView;
    @BindView(R2.id.layout_surface)
    LinearLayout layoutSurface;
    @BindView(R2.id.seek_pink)
    SeekBar seekPink;
    @BindView(R2.id.pink_value)
    TextView pinkValue;
    @BindView(R2.id.seek_whiten)
    SeekBar seekWhiten;
    @BindView(R2.id.whiten_value)
    TextView whitenValue;
    @BindView(R2.id.seek_redden)
    SeekBar seekRedden;
    @BindView(R2.id.redden_value)
    TextView reddenValue;
    @BindView(R2.id.seek_soften)
    SeekBar seekSoften;
    @BindView(R2.id.soften_value)
    TextView softenValue;
    @BindView(R2.id.listview)
    RecyclerView listview;
    @BindView(R2.id.filter_redden)
    SeekBar filterRedden;
    @BindView(R2.id.filter_value)
    TextView filterValue;
    @BindView(R2.id.tv_sticker)
    TextView tvSticker;
//    private PreviewUtils mPreviewUtils;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
//        mPreviewUtils = new PreviewUtils(getApplicationContext(), this);
    }

    @Override
    protected int getContentView() {
        return R.layout.vedio_activity_takesmallvedio;
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

    }

}
