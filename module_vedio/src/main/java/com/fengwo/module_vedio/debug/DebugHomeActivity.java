//package com.fengwo.module_vedio.debug;
//
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.fengwo.module_comment.base.BaseMvpActivity;
//import com.fengwo.module_comment.base.BasePresenter;
//import com.fengwo.module_vedio.mvp.ui.fragment.VedioFragment;
//import com.fengwo.module_vedio.R;
//
//public class DebugHomeActivity extends BaseMvpActivity {
//
//    @Override
//    protected void initView() {
//        VedioFragment fragment = new VedioFragment();
//        FragmentManager m = getSupportFragmentManager();
//        FragmentTransaction t = m.beginTransaction();
//        t.replace(R.id.vedio,fragment);
//        t.commit();
//    }
//
//    @Override
//    protected int getContentView() {
//        return R.layout.vedio_activity_debug_home;
//    }
//
//    @Override
//    public BasePresenter initPresenter() {
//        return null;
//    }
//}
