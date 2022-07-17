package com.fengwo.module_vedio.mvp.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.base.BaseActivity;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_vedio.R;
import com.fengwo.module_vedio.R2;
import com.fengwo.module_vedio.mvp.ui.adapter.RankAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@Route(path = "/test/rankactivity")
public class RankActivity extends BaseMvpActivity implements TabLayout.OnTabSelectedListener {

    private String[] tabTitles = {"新人榜", "经典榜", "飙升榜"};

    @BindView(R2.id.tablayout)
    TabLayout tabLayout;
    @BindView(R2.id.recycleview)
    RecyclerView recyclerView;


    @Override
    protected void initView() {
        ToolBarBuilder barBuilder = new ToolBarBuilder();
        barBuilder.setTitle(getResources().getString(R.string.vedio_rank)).showBack(true).build();
        for (int i = 0; i < tabTitles.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(tabTitles[i]);
            tabLayout.addTab(tab);
        }
        tabLayout.addOnTabSelectedListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<String> l = new ArrayList<>();
        l.add("1");
        l.add("2");
        l.add("3");
        l.add("4");
        l.add("5");
        l.add("6");
        l.add("7");
        recyclerView.setAdapter(new RankAdapter(R.layout.vedio_item_rank, l));


    }

    @Override
    protected int getContentView() {
        return R.layout.vedio_activity_rank;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }
}
