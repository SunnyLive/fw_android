package com.fengwo.module_live_vedio.mvp.ui.df;

import android.os.Bundle;

import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.ui.adapter.FragmentVpAdapter;
import com.fengwo.module_live_vedio.mvp.ui.fragment.LiveProfitFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/5
 */
public class LiveProfitDF extends BaseDialogFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentVpAdapter fragmentVpAdapter;
    public int channelId;

    public static DialogFragment getInstance(int channelId){
        LiveProfitDF liveProfitDF = new LiveProfitDF();
        Bundle bundle = new Bundle();
        bundle.putInt("channelId",channelId);
        liveProfitDF.setArguments(bundle);
        return liveProfitDF;
    }

    @Override
    protected void initView() {
        channelId = getArguments().getInt("channelId");
        tabLayout = view.findViewById(R.id.tabview);
        viewPager = view.findViewById(R.id.vp);
//        titleList.add("许愿棒");
        titleList.add("本场贡献");
        titleList.add("收到礼物");
        fragmentList.add(LiveProfitFragment.getInstanse(0, channelId));
        fragmentList.add(LiveProfitFragment.getInstanse(1, channelId));
        fragmentVpAdapter = new FragmentVpAdapter(getChildFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(fragmentVpAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public int getContentLayout() {
        return R.layout.pop_live_profit;
    }

}
