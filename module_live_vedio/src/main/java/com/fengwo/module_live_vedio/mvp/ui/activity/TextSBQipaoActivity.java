package com.fengwo.module_live_vedio.mvp.ui.activity;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.widget.SbQipaoView;

import butterknife.BindView;

public class TextSBQipaoActivity extends BaseMvpActivity {

    @BindView(R2.id.sbqipao)
    SbQipaoView sbQipaoView;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_testsbqipao;
    }
}
