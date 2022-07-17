package com.fengwo.module_login.mvp.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.presenter.FavouritePresenter;
import com.fengwo.module_login.mvp.ui.adapter.FavouriteShopAdapter;
import com.fengwo.module_login.mvp.ui.adapter.FavouriteVideoAdapter;
import com.fengwo.module_login.mvp.ui.iview.IFavouriteView;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyFavouriteActivity extends BaseMvpActivity<IFavouriteView, FavouritePresenter> implements IFavouriteView {

    private static final String TITLE_SHORT = "短片";
    private static final String TITLE_SHOP = "商家";
    private static final int INDEX_VIDEO = 0;
    private static final int INDEX_SHOP = 1;
    @BindView(R2.id.tablayout)
    TabLayout tablayout;
    @BindView(R2.id.recycleview)
    RecyclerView recycleview;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;

    private List<String> titles = new ArrayList<>();
    private int videoPage = 1;
    private int shopPage = 1;
    private int choosePosition = 0;

    private FavouriteVideoAdapter favouriteVideoAdapter;
    private FavouriteShopAdapter favouriteShopAdapter;
    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    public FavouritePresenter initPresenter() {
        return new FavouritePresenter();
    }

    @Override
    protected void initView() {
        new ToolBarBuilder().showBack(true)
                .setTitle("我的收藏")
                .build();
        initTab();
        DefaultRefreshHeaderCreator creator = (context, layout) -> {
            layout.setPrimaryColorsId(R.color.white, R.color.text_99);//全局设置主题颜色
            return new ClassicsHeader(context);
        };
        smartrefreshlayout.setRefreshHeader(creator.createRefreshHeader(this, smartrefreshlayout));
        DefaultRefreshFooterCreator footerCreator = (context, layout) -> {
            layout.setPrimaryColorsId(R.color.white, R.color.text_99);//全局设置主题颜色
            return new ClassicsFooter(context);
        };
        smartrefreshlayout.setRefreshFooter(footerCreator.createRefreshFooter(this, smartrefreshlayout));
        p.getVedioData(1);
        p.getShopData(1);

    }

    void initTab() {
        titles.add(TITLE_SHORT);
        titles.add(TITLE_SHOP);
        for (int i = 0; i < titles.size(); i++) {
            TabLayout.Tab t = tablayout.newTab();
            t.setText(titles.get(i));
            tablayout.addTab(t);
        }
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                changeAdapter(position);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void changeAdapter(int choosePosition) {
        switch (choosePosition) {
            case INDEX_SHOP:
                recycleview.setLayoutManager(linearLayoutManager);
                recycleview.setAdapter(favouriteShopAdapter);
                break;
            case INDEX_VIDEO:
                recycleview.setLayoutManager(staggeredGridLayoutManager);
                recycleview.setAdapter(favouriteVideoAdapter);
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_myfavourite;
    }

    @Override
    public void setVideoData(List<String> data, int page) {
        data = new ArrayList<>();
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        if (null == favouriteVideoAdapter) {
            favouriteVideoAdapter = new FavouriteVideoAdapter(R.layout.login_item_favourite_video, data);
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
            recycleview.setLayoutManager(staggeredGridLayoutManager);
            recycleview.setAdapter(favouriteVideoAdapter);
        }

    }

    @Override
    public void setShopData(List<String> data, int page) {
        data = new ArrayList<>();
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        data.add("1");
        if (null == favouriteShopAdapter) {
            favouriteShopAdapter = new FavouriteShopAdapter(data);
            linearLayoutManager =new LinearLayoutManager(MyFavouriteActivity.this, LinearLayoutManager.VERTICAL, false);
        } else {
            recycleview.setLayoutManager(linearLayoutManager);
            recycleview.setAdapter(favouriteShopAdapter);
        }
    }
}
