package com.fengwo.module_login.mvp.ui.activity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.ui.adapter.ChongzhiAdapter;
import com.fengwo.module_login.mvp.ui.adapter.DuihuanAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DuihuanActivity extends BaseMvpActivity {
    @BindView(R2.id.recycleview)
    RecyclerView recycleview;

    private DuihuanAdapter duihuanAdapter;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        new ToolBarBuilder().setTitle("兑换").showBack(true).build();
        List<String> d = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            d.add("");
        }
        duihuanAdapter = new DuihuanAdapter(R.layout.login_item_duihuan, d);
        recycleview.setLayoutManager(new GridLayoutManager(this, 3));
        recycleview.setAdapter(duihuanAdapter);

    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_duihuan;
    }

}
