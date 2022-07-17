package com.fengwo.module_login.mvp.ui.activity;

import android.graphics.Color;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.BuildConfig;
import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.utils.EventIntervalUtil;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.RecordDto;
import com.fengwo.module_login.mvp.dto.WithDrawDto;
import com.fengwo.module_login.mvp.presenter.AccountRecordPresenter;
import com.fengwo.module_login.mvp.ui.adapter.CashoutRecordAdapter;
import com.fengwo.module_login.mvp.ui.adapter.ChongzhiRecordAdapter;
import com.fengwo.module_login.mvp.ui.adapter.DuihuanRecordAdapter;
import com.fengwo.module_login.mvp.ui.iview.IAccountRecordView;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.Url;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;

public class AccountRecordActivity extends BaseMvpActivity<IAccountRecordView, AccountRecordPresenter> implements IAccountRecordView {

    @BindView(R2.id.tablayout)
    TabLayout tablayout;
    @BindView(R2.id.recycleview)
    RecyclerView recycleview;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    private String[] tites = {"充值", "提现"};

    private int rechargePage  = 1;
    private int cashoutPage  = 1;
    private int index = 0;

    private ChongzhiRecordAdapter chongzhiAdapter;
    private CashoutRecordAdapter cashoutAdapter;


    private LoginApiService service;

    @Override
    public AccountRecordPresenter initPresenter() {
        return new AccountRecordPresenter();
    }

    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().setTitle("我的账单").setTitleColor(R.color.text_33).setBackIcon(R.drawable.ic_back_black).showBack(true).build();
        initTab();
        SmartRefreshLayoutUtils.setTransparentBgWithWhileText(this, smartrefreshlayout);
        p.getRechargeRecords(rechargePage);
        p.getWithDrawRecords(cashoutPage);

        initRv();

        initListener();

    }

    private void initListener() {
        smartrefreshlayout.setOnRefreshListener(refreshLayout -> {
            doRefresh();

        });
        smartrefreshlayout.setOnLoadMoreListener(refreshLayout -> {
            doLoadMore();
        });
        cashoutAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (!EventIntervalUtil.canOperate()) return;
                WithDrawDto withDrawDto = (WithDrawDto) baseQuickAdapter.getData().get(i);
                BrowserActivity.start(AccountRecordActivity.this,"", BuildConfig.DEBUG? Url.TEST_BASE_PAY_URL+"withdrawalResult?id="+withDrawDto.getId()+"&token= "+ UserManager.getInstance().getToken():Url.BASE_PAY_URL+"withdrawalResult?id="+withDrawDto.getId()+"&token= "+ UserManager.getInstance().getToken());
            }
        });
    }

    private void initRv() {
        chongzhiAdapter = new ChongzhiRecordAdapter(R.layout.login_item_record);
        chongzhiAdapter.setEmptyView(getListEmptyView());
        recycleview.setAdapter(chongzhiAdapter);


        cashoutAdapter = new CashoutRecordAdapter(R.layout.login_item_withdraw);
        cashoutAdapter.setEmptyView(getListEmptyView());

//        duihuanAdapter = new DuihuanRecordAdapter(R.layout.login_item_record, d);

        recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void doLoadMore() {
        switch (index) {
            case 0:
                p.getRechargeRecords(++rechargePage);
                break;
            case 1:
                p.getWithDrawRecords(++cashoutPage);
                break;
            case 2:
                break;
        }
    }

    private void doRefresh() {
        switch (index) {
            case 0:
                rechargePage =1;
                p.getRechargeRecords(rechargePage);
                break;
            case 1:
                cashoutPage =1;
                p.getWithDrawRecords(cashoutPage);
                break;

            case 2:

                break;

        }
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_accountrecord;
    }

    private void initTab() {
        for (int i = 0; i < tites.length; i++) {
            TabLayout.Tab t = tablayout.newTab();
            t.setText(tites[i]);
            tablayout.addTab(t);
        }
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                index = position;
                switch (position) {
                    case 0:
                        recycleview.setAdapter(chongzhiAdapter);
                        break;
                    case 1:
                        recycleview.setAdapter(cashoutAdapter);
                        break;

                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void setRechargeRecordData(List<RecordDto> data, int page) {
        smartrefreshlayout.closeHeaderOrFooter();
        if (1 == page) {
            if (data!=null&&data.size()>0) {
                chongzhiAdapter.setNewData(data);
            } else {
                chongzhiAdapter.setEmptyView(getListEmptyView());
            }
        } else {
            chongzhiAdapter.addData(data);
        }
    }

    @Override
    public void setWithDrawRecordData(List<WithDrawDto> data, int page) {
        smartrefreshlayout.closeHeaderOrFooter();
        if (1 == page) {
            if (data!=null&&data.size()>0) {
                cashoutAdapter.setNewData(data);
            } else {
                cashoutAdapter.setEmptyView(getListEmptyView());
            }
        } else {
            cashoutAdapter.addData(data);
        }
    }
}
