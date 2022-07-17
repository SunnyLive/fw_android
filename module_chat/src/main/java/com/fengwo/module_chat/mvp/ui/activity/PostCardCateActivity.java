package com.fengwo.module_chat.mvp.ui.activity;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_chat.mvp.presenter.CardSelectPresenter;
import com.fengwo.module_chat.mvp.ui.adapter.PostCardCateAdapter;
import com.fengwo.module_chat.mvp.ui.contract.ICardSelectView;
import com.fengwo.module_comment.widget.GridItemDecoration;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;

import com.fengwo.module_comment.utils.ToastUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class PostCardCateActivity extends BaseMvpActivity<ICardSelectView, CardSelectPresenter> implements ICardSelectView {

    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R2.id.recyclerview)
    RecyclerView recyclerView;

    private final String PAGE_SIZE = ",20";
    private int page = 1;
    private PostCardCateAdapter adapter;

    @Override
    public CardSelectPresenter initPresenter() {
        return new CardSelectPresenter();
    }

    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true).setTitle("选择卡片类别")
                .setTitleColor(com.fengwo.module_comment.R.color.text_33)
                .setBackIcon(com.fengwo.module_comment.R.drawable.ic_back_black).build();
        SmartRefreshLayoutUtils.setTransparentBlackText(this, smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(layout -> {
            page = 1;
            p.getCircles(page + PAGE_SIZE);
        });
        smartRefreshLayout.setOnLoadMoreListener(layout -> {
            page++;
            p.getCircles(page + PAGE_SIZE);
        });
        adapter = new PostCardCateAdapter();
        GridItemDecoration decoration = new GridItemDecoration(this, DensityUtils.dp2px(this, 30));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(decoration);

        p.getCircles(page + PAGE_SIZE);
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_card_select;
    }

    @OnClick(R2.id.btnConfirm)
    public void onViewClick(View view) {
        if (adapter.getSelectedItem() == null) {
            ToastUtils.showShort(this, "请选择圈子");
        } else {
            Intent intent = new Intent();
            intent.putExtra("data", adapter.getSelectedItem());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void setCircles(ArrayList<RecommendCircleBean> records) {
        if (page == 1) {
            smartRefreshLayout.finishRefresh();
            adapter.setNewData(records);
        } else if (records == null || records.size() < 20) {
            smartRefreshLayout.finishLoadMoreWithNoMoreData();
            if (records != null) adapter.addData(records);
        } else {
            smartRefreshLayout.finishLoadMore();
            adapter.addData(records);
        }
    }

    @Override
    public void setLoveCircleSuccess() {
    }
}
