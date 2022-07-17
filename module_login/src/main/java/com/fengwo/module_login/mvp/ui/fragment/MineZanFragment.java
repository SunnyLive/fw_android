package com.fengwo.module_login.mvp.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.ui.adapter.FragmentVpAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MineZanFragment extends BaseMvpFragment {
    public final static String UID = "userid";
    @BindView(R2.id.btn_card)
    GradientTextView btnCard;
    @BindView(R2.id.btn_vedio)
    GradientTextView btnVedio;
    @BindView(R2.id.btn_movie)
    GradientTextView btnMovie;
    @BindView(R2.id.vp)
    ViewPager vp;
    private List<Fragment> fragments;
    private List<GradientTextView> btnViews;

    private FragmentVpAdapter fragmentPagerAdapter;

    public static MineZanFragment newInstance(int uid) {
        Bundle args = new Bundle();
        args.putInt(UID, uid);
        MineZanFragment fragment = new MineZanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_mine_zan;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        int uid = getArguments().getInt(UID);
        btnViews = new ArrayList<>();
        btnViews.add(btnCard);
        btnViews.add(btnVedio);
        btnViews.add(btnMovie);
        fragments = new ArrayList<>();
        fragments.add(MineZanCardFragment.newInstance(uid, true));
        fragments.add(MineZanVideoFragment.newInstance(uid, true));
        fragments.add(MineZanMovieFragment.newInstance(uid, true));
        fragmentPagerAdapter = new FragmentVpAdapter(getChildFragmentManager(), fragments);
        vp.setOffscreenPageLimit(fragments.size());
        vp.setAdapter(fragmentPagerAdapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setBtnStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setBtnStatus(int position) {
        for (int i = 0; i < btnViews.size(); i++) {
            GradientTextView tv = btnViews.get(i);
            if (i == position) {
                tv.setEnabled(false);
                tv.setColors(Color.parseColor("#9A43FF"), Color.parseColor("#BA6FFC"), Color.parseColor("#F0A4FA"));
            } else {
                tv.setEnabled(true);
                tv.setColors(Color.parseColor("#eeeeee"), Color.parseColor("#eeeeee"), Color.parseColor("#eeeeee"));
            }
        }

    }

    @OnClick({R2.id.btn_card, R2.id.btn_vedio, R2.id.btn_movie})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_card) {
            setBtnStatus(0);
            vp.setCurrentItem(0);
        } else if (id == R.id.btn_vedio) {
            setBtnStatus(1);
            vp.setCurrentItem(1);
        } else if (id == R.id.btn_movie) {
            setBtnStatus(2);
            vp.setCurrentItem(2);
        }
    }
}
