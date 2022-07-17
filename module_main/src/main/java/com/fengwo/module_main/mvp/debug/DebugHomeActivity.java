package com.fengwo.module_main.mvp.debug;

import com.fengwo.module_comment.base.BaseActivity;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_main.R;

public class DebugHomeActivity extends BaseMvpActivity {

    @Override
    protected void initView() {
//        ChatHomeFragment fragment = new ChatHomeFragment();
//        FragmentManager m = getSupportFragmentManager();
//        FragmentTransaction t = m.beginTransaction();
//        t.replace(R.id.vedio,fragment);
//        t.commit();
    }

    @Override
    protected int getContentView() {
        return R.layout.main_activity_home;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }
}
