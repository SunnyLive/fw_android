package com.fengwo.module_live_vedio.mvp.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.MapPoiDto;
import com.fengwo.module_live_vedio.widget.mapview.MapView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MapActivity extends BaseMvpActivity {

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_map;
    }

}
