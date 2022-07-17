package com.fengwo.module_vedio.mvp.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.widget.autoLayout.AutoTabLayout;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.R2;
import com.fengwo.module_vedio.mvp.dto.FilterLabelDto;
import com.fengwo.module_vedio.mvp.presenter.FilterPresenter;
import com.fengwo.module_vedio.mvp.ui.iview.IFilterView;
import com.google.android.material.tabs.TabLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;

public class FilterActivity extends BaseMvpActivity<IFilterView, FilterPresenter> implements IFilterView {

    String[] tabTitleStrs = {"最近热播", "最近上架", "大众好评"};
    String[] tabTypeStrs = {"最近热播", "最近上架", "大众好评", "最近热播", "最近上架", "大众好评", "最近热播", "最近上架", "大众好评"};
    String[] tabQualityStrs = {"最近热播", "最近上架", "大众好评", "最近热播", "最近上架", "大众好评", "最近热播", "最近上架", "大众好评"};
    String[] tabTimeStrs = {"最近热播", "最近上架", "大众好评", "最近热播", "最近上架", "大众好评", "最近热播", "最近上架", "大众好评"};
    @BindView(R2.id.tab_title)
    AutoTabLayout tabTitle;
    @BindView(R2.id.tab_type)
    AutoTabLayout tabType;
    @BindView(R2.id.tab_short)
    AutoTabLayout tabShort;
    @BindView(R2.id.tab_small)
    AutoTabLayout tabSmall;

    @Override
    public FilterPresenter initPresenter() {
        return new FilterPresenter();
    }

    @Override
    protected void initView() {
        ToolBarBuilder toolBarBuilder = new ToolBarBuilder();
        toolBarBuilder.showBack(true)
                .setTitle(getResources().getString(R.string.vedio_serach_filter)).build();
        p.getLabs();

    }


//    public void initTabs() {
//        for (int i = 0; i < tabTitleStrs.length; i++) {
//            TabLayout.Tab tab = tabTitle.newTab();
//            View v = getTitleTab(tabTitleStrs[i]);
//            if (i == 0) {
//                v.findViewById(R.id.tv_title).setEnabled(false);
//            }
//            tab.setCustomView(v);
//            tabTitle.addTab(tab);
//        }
//        for (int i = 0; i < tabTypeStrs.length; i++) {
//            TabLayout.Tab tab = tabType.newTab();
//            tab.setText(tabTypeStrs[i]);
//            tabType.addTab(tab);
//        }
//        for (int i = 0; i < tabQualityStrs.length; i++) {
//            TabLayout.Tab tab = tabQuality.newTab();
//            tab.setText(tabQualityStrs[i]);
//            tabQuality.addTab(tab);
//        }
//        for (int i = 0; i < tabTimeStrs.length; i++) {
//            TabLayout.Tab tab = tabTime.newTab();
//            tab.setText(tabTimeStrs[i]);
//            tabTime.addTab(tab);
//        }
//    }

    public View getTitleTab(String title) {
        View v = LayoutInflater.from(this).inflate(R.layout.vedio_item_filter_tab_title, null, false);
        AutoUtils.auto(v);
        TextView tv = v.findViewById(R.id.tv_title);
        tv.setText(title);
        return v;
    }

    @Override
    protected int getContentView() {
        return R.layout.vedio_activity_filter;
    }

    @Override
    public void setLabs(FilterLabelDto data) {
        intTab(tabTitle, data.video);
        intTab(tabType, data.label);
        intTab(tabShort, data.movie);
        intTab(tabSmall, data.shortVideo);
    }

    private void intTab(TabLayout tabLayout, List<FilterLabelDto.VideoLab> data) {
        for (int i = 0; i < data.size(); i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View v = getTitleTab(data.get(i).videoDetail);
            if (i == 0) {
                v.findViewById(R.id.tv_title).setEnabled(false);
            }
            tab.setCustomView(v);
            tabLayout.addTab(tab);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tv_title).setEnabled(false);
                int position = tab.getPosition();
                int id = tabLayout.getId();
                if (id == R.id.tab_title) {
                    if (data.get(position).id == 10) {//10 短片
                        tabShort.setVisibility(View.VISIBLE);
                        tabSmall.setVisibility(View.GONE);
                    } else if (data.get(position).id == 11) {//11 小视频
                        tabShort.setVisibility(View.GONE);
                        tabSmall.setVisibility(View.VISIBLE);
                    }
                } else if (id == R.id.tab_type) {
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tv_title).setEnabled(true);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
