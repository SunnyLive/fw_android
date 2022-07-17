package com.fengwo.module_comment.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 *@data 2017/4/27
 *@author min
 *@Description 承载fragment的fragment
 */
public abstract class NestFragment extends BaseFragment {
    FragmentManager mManager;
    private int fragmentViewId;
    private BaseFragment lastFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentViewId = setChildFragmentContent();
    }

    protected abstract int setChildFragmentContent();

    public void changeFragment(BaseFragment f) {
//        nowFragment = f;
        mManager = getChildFragmentManager();
        FragmentTransaction transaction = mManager.beginTransaction();
        if (lastFragment != null && !lastFragment.isHidden()) {
            transaction.hide(lastFragment);
        }
        if (!f.isAdded()) {
            transaction.add(fragmentViewId, f);
        }
        transaction.show(f);
        lastFragment = f;
        transaction.commit();
    }
}
