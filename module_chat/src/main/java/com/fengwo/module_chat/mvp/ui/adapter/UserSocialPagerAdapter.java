package com.fengwo.module_chat.mvp.ui.adapter;

import android.util.SparseArray;

import com.fengwo.module_chat.mvp.ui.activity.social.userpage.SmallVideoFragment;
import com.fengwo.module_comment.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * 用户社交页面适配器
 * @author chenshanghui
 * @intro
 * @date 2019/9/23
 */
public class UserSocialPagerAdapter extends FragmentPagerAdapter {

    private SparseArray<BaseFragment> sparseArray = new SparseArray<>();

    public UserSocialPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = sparseArray.get(position);
        if (fragment == null){
            if (position == 0){//小视频
                fragment = SmallVideoFragment.newInstance();
            }else if (position == 1){//短片
                fragment = SmallVideoFragment.newInstance();
            }else if(position == 2){//赞
                fragment = SmallVideoFragment.newInstance();
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
