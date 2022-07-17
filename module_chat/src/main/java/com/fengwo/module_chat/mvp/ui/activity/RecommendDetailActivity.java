package com.fengwo.module_chat.mvp.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.ui.adapter.RecommendDetailAdapter;
import com.fengwo.module_chat.mvp.ui.adapter.RecommendTopVpAdapter;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.widget.AppTitleBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/25
 */
public class RecommendDetailActivity extends BaseMvpActivity {

    @BindView(R2.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R2.id.title)
    AppTitleBar titleBar;

    RecommendDetailAdapter mAdapter;
    private List<String> mListData = new ArrayList<>();
    private View headView;
    private ViewPager viewPager;

    private int distance = 0;
    private int maxDistance = 300;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        titleBar.getBackground().setAlpha(0);
        initHeadView();
        mListData.add("jj");
        mListData.add("jj");
        mListData.add("jj");
        mListData.add("jj");
        mListData.add("jj");
        mListData.add("jj");
        mListData.add("jj");
        mListData.add("jj");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecommendDetailAdapter(mListData);
        recyclerView.setAdapter(mAdapter);
        mAdapter.addHeaderView(headView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                distance += dy;
                int percent = (int) (distance*1.0f/maxDistance * 255);
                if (percent>255) {
                    percent = 255;
                    titleBar.setBackIcon(R.drawable.ic_back_white);
                    titleBar.setMoreIcon(R.drawable.ic_share_black);
                }else {
                    titleBar.setBackIcon(R.drawable.icon_back);
                    titleBar.setMoreIcon(R.drawable.ic_share_white);
                }
                titleBar.getBackground().setAlpha((int) percent);
            }
        });
    }

    private void initHeadView() {
        List<String> imageList = new ArrayList<>();
        imageList.add("http://pic1.win4000.com/pic/8/3c/141dbcb24c_250_350.jpg");
        imageList.add("http://pic1.win4000.com/pic/8/3c/141dbcb24c_250_350.jpg");
        imageList.add("http://pic1.win4000.com/pic/8/3c/141dbcb24c_250_350.jpg");
        imageList.add("http://pic1.win4000.com/pic/8/3c/141dbcb24c_250_350.jpg");
        imageList.add("http://pic1.win4000.com/pic/8/3c/141dbcb24c_250_350.jpg");
        imageList.add("http://pic1.win4000.com/pic/8/3c/141dbcb24c_250_350.jpg");
        headView = LayoutInflater.from(this).inflate(R.layout.recommend_detail_headview,null);
        viewPager = headView.findViewById(R.id.vp_top);
        viewPager.setAdapter(new RecommendTopVpAdapter(this,imageList));
    }

    @Override
    protected int getContentView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_recommend_detail;
    }
}
