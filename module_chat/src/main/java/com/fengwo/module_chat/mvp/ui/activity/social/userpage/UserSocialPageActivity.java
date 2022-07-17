package com.fengwo.module_chat.mvp.ui.activity.social.userpage;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.ui.adapter.UserSocialPagerAdapter;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.widget.AppTitleBar;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.gyf.immersionbar.ImmersionBar;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;


/**
 * 用户详情
 */
public class UserSocialPageActivity extends BaseMvpActivity {

    @BindView(R2.id.view_fix_height)
    View viewFixHeight;
    @BindView(R2.id.parallax)
    ImageView parallax;
    @BindView(R2.id.textView4)
    TextView textView4;
    @BindView(R2.id.textView2)
    TextView textView2;
    @BindView(R2.id.tv_nickname)
    TextView tvNickname;
    @BindView(R2.id.tv_intro)
    TextView tvIntro;
    @BindView(R2.id.iv_gender)
    ImageView ivGender;
    @BindView(R2.id.tv_age)
    TextView tvAge;
    @BindView(R2.id.tv_location)
    TextView tvLocation;
    @BindView(R2.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R2.id.titleBar)
    AppTitleBar titleBar;
    @BindView(R2.id.collapse)
    CollapsingToolbarLayout collapse;
    @BindView(R2.id.toolBar)
    Toolbar toolBar;
    @BindView(R2.id.viewPager)
    ViewPager viewPager;

    private UserSocialPagerAdapter mPagerAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, UserSocialPageActivity.class);
        context.startActivity(intent);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        int fixHeight = ImmersionBar.getStatusBarHeight(this);
        //布局标题栏是写在coordinator里面，所以沉浸式标题栏下移一个状态栏的高度  toolbar换个图标
        CollapsingToolbarLayout.LayoutParams flp  = (CollapsingToolbarLayout.LayoutParams) toolBar.getLayoutParams();
        flp.setMargins(0,fixHeight,0,0); //
        mPagerAdapter = new UserSocialPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_user_social_page;
    }



}
