package com.fengwo.module_live_vedio.mvp.ui.activity;

import android.os.Bundle;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.widget.QiqiuView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestQiqiuActivity extends BaseMvpActivity {
    @BindView(R2.id.qiqiu)
    QiqiuView qiqiu;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        qiqiu.addQipao();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_testqiqiu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
