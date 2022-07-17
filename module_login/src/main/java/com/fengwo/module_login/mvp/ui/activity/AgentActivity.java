package com.fengwo.module_login.mvp.ui.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseFragment;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_login.R;

public class AgentActivity extends BaseMvpActivity {

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        setTitle("");
        BaseFragment fragment = (BaseFragment) ARouter.getInstance().build(ArouterApi.LIVE_AGENT_FRAGMENT).navigation();
        if (null != fragment) {
            FragmentManager m = getSupportFragmentManager();
            FragmentTransaction t = m.beginTransaction();
            t.replace(R.id.fragment, fragment);
            t.commit();
            fragment.hideToolBar();
        }

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_agent;
    }

}
