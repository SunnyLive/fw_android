package com.fengwo.module_chat.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_chat.mvp.presenter.CardSelectPresenter;
import com.fengwo.module_chat.mvp.ui.adapter.CardSelectAdapter;
import com.fengwo.module_chat.mvp.ui.contract.ICardSelectView;
import com.fengwo.module_comment.widget.GridItemDecoration;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;

import com.fengwo.module_comment.utils.ToastUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;

/**
 * 卡片选择类别页面
 */
public class CardSelectActivity extends BaseMvpActivity<ICardSelectView, CardSelectPresenter> implements ICardSelectView {

    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R2.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R2.id.btnConfirm)
    TextView btnConfirm;

    private CardSelectAdapter adapter;
    private final String PAGE_SIZE = ",20";
    private int page = 1;

    @Override
    public CardSelectPresenter initPresenter() {
        return new CardSelectPresenter();
    }

    @Override
    protected void initView() {
//        setTitleBackground(R.color.black_000000);
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
        adapter = new CardSelectAdapter(4);
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
        if (adapter.getSelectedCount() != 4) {
            toastTip("请选择展示到首页的四个圈子");
            return;
        }
        p.setUserLoveCircle(adapter.getSelectedCircleIds());
    }

    @SuppressLint("CheckResult")
    @Override
    public void setCircles(ArrayList<RecommendCircleBean> records) {
        if (page == 1) {
            smartRefreshLayout.finishRefresh();
            adapter.clearSelectedCount();
            for (RecommendCircleBean bean : records) {
                bean.selected = bean.isLike == 1;
                if (bean.selected) adapter.addSelectedCount();
            }
            Flowable.fromArray(records)
                    .map(recommendCircleBeans -> { //过滤被后台禁掉的圈子

                        ArrayList<RecommendCircleBean> beans = new ArrayList<>(recommendCircleBeans);
                        for (RecommendCircleBean bean : recommendCircleBeans) {
                            if (bean.status == 0) beans.remove(bean);
                        }
                        return beans;
                    }).compose(RxUtils.applySchedulers2())
                    .subscribe(recommendCircleBeans -> {
                        adapter.setNewData(recommendCircleBeans);
                    });

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
        ToastUtils.showShort(this, "设置成功");
        setResult(RESULT_OK);
        finish();
    }
}