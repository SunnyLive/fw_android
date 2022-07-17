package com.fengwo.module_login.debug;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.ui.fragment.MineFragment;


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DebugHomeActivity extends BaseMvpActivity {

    @Override
    protected void initView() {
        MineFragment fragment = new MineFragment();
        FragmentManager m = getSupportFragmentManager();
        FragmentTransaction t = m.beginTransaction();
        t.replace(R.id.vedio,fragment);
        t.commit();
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_debug_home;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }
}
