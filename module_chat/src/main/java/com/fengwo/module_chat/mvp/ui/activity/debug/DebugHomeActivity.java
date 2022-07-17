package com.fengwo.module_chat.mvp.ui.activity.debug;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.ui.fragment.ChatHomeFragment;
import com.fengwo.module_comment.base.BaseActivity;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;

public class DebugHomeActivity extends BaseMvpActivity {


    @Override
    protected void initView() {
        ChatHomeFragment fragment = new ChatHomeFragment();
        FragmentManager m = getSupportFragmentManager();
        FragmentTransaction t = m.beginTransaction();
        t.replace(R.id.vedio,fragment);
        t.commit();
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_debug_home;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }
}
