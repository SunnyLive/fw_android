package com.fengwo.module_comment.base;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 *@data 2017/4/27
 *@author min
 *@Description 承载fragment的activity
 */
public abstract class BaseFragmentActivity extends BaseActivity {
    FragmentManager manager;
    protected int fragmentViewId;

    private BaseFragment lastFragment;
//    private BaseFragment nowFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fragmentViewId = setFragmentView();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);

    }


    public abstract int setFragmentView();

    public void changeFragment(BaseFragment f) {
//        nowFragment = f;
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (lastFragment != null && !lastFragment.isHidden()) {
            transaction.hide(lastFragment);
        }
        if (!f.isAdded()) {
            transaction.add(fragmentViewId, f);
        }
        transaction.show(f);
        lastFragment = f;
        transaction.commitAllowingStateLoss();
    }
}
