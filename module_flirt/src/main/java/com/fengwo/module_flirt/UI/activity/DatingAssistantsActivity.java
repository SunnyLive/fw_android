package com.fengwo.module_flirt.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_chat.base.RefreshAfterClearEvent;
import com.fengwo.module_chat.utils.chat_new.ChatGreenDaoHelper;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.widget.AppTitleBar;
import com.fengwo.module_flirt.Interfaces.IDatingAssistantsView;
import com.fengwo.module_flirt.P.DatingAssistantsPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.adapter.DatingAssistantAdapter;
import com.fengwo.module_flirt.bean.AppointmentListBean;
import com.fengwo.module_login.mvp.ui.activity.MineDetailActivity;
import com.fengwo.module_websocket.EventConstant;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;

/**
 * 约会助手
 *
 * @Author BLCS
 * @Time 2020/4/1 16:47
 */
@Route(path = ArouterApi.MESSAGE_DATINGASSISTANTS)
public class DatingAssistantsActivity extends BaseMvpActivity<IDatingAssistantsView, DatingAssistantsPresenter> implements OnRefreshListener, OnLoadMoreListener, IDatingAssistantsView {
    @BindView(R2.id.sr_refresh)
    SmartRefreshLayout srRefresh;
    @BindView(R2.id.rv_include)
    RecyclerView rvInclude;
    private int page = 1;
    private DatingAssistantAdapter mAdapter;

    @Autowired
    UserProviderService userProviderService;

    @Override
    public DatingAssistantsPresenter initPresenter() {
        return new DatingAssistantsPresenter();
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, DatingAssistantsActivity.class));
    }

    @Override
    protected void initView() {
        //清空已读消息
        ChatGreenDaoHelper daoHelper = ChatGreenDaoHelper.getInstance();
        daoHelper.cleanUnreadCount(userProviderService.getUserInfo().id + "", EventConstant.appoint_event + "");

        srRefresh.setOnRefreshListener(this);
        srRefresh.setOnLoadMoreListener(this);
        initRv();
        //清空数据
        setTitleBackground(getResources().getColor(R.color.white));

        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("约会助手")
                .setTitleColor(R.color.text_33)
                .setRight1Img(R.drawable.ic_clear_message,view -> {
                    if (mAdapter.getData().size() > 0) {
                        CommonDialog.getInstance("", "确定清空当前所有约会消息？", "取消", "确定", false).addOnDialogListener(new CommonDialog.OnDialogListener() {
                            @Override
                            public void cancel() {

                            }

                            @Override
                            public void sure() {
                                p.clearList();
                            }
                        }).show(getSupportFragmentManager(), "清空数据");
                    } else {
                        toastTip("暂无消息");
                    }
                })
                .setRightTextColor(R.color.text_33)
                .build();
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        p.getList(1);
    }

    private void initRv() {
        rvInclude.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DatingAssistantAdapter();
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_base_empty_view, null);
        TextView tv = inflate.findViewById(R.id.tv_empty);
        TextView btn = inflate.findViewById(R.id.tv_button);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//去同城交友
                ARouter.getInstance().build(ArouterApi.MAIN_MAIN_ACTIVITY)
                        .withInt("pos", 1)
                        .navigation();
            }
        });
        tv.setText("您的日程目前空挡 快向心动的TA发出约会邀请吧~");
        mAdapter.setEmptyView(inflate);
        rvInclude.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                AppointmentListBean bean = (AppointmentListBean) baseQuickAdapter.getData().get(i);
                int id = view.getId();
                ConstraintLayout cl = (ConstraintLayout) view.getParent();
                TextView tvRefuse = cl.findViewById(R.id.tv_refuse);
                TextView tvOrder = cl.findViewById(R.id.tv_order);
                if (id == R.id.tv_order) {
                    tvOrder.setSelected(true);
                    tvOrder.setClickable(false);

                    if (tvOrder.getText().equals("接单")) {
                        tvRefuse.setSelected(true);
                        tvRefuse.setTextColor(Color.WHITE);
                        tvRefuse.setClickable(false);
                        tvRefuse.setVisibility(View.GONE);
                        tvOrder.setText("已接单");
                        p.acceptAppointment(bean.getItemId(), bean.getId(), 1);
                    } else if (tvOrder.getText().equals("提醒他")) {
                        tvOrder.setText("已接单");
                        p.remindUser(bean.getItemId(), bean.getId());
                    } else if (tvOrder.getText().equals("去看看")) {
                        FlirtCardDetailsActivity.start(DatingAssistantsActivity.this, bean.getSenderUid());
                    }
                } else if (id == R.id.tv_refuse) {
                    p.acceptAppointment(bean.getItemId(), bean.getId(), 0);
                    tvOrder.setSelected(true);
                    tvOrder.setClickable(false);
                    tvRefuse.setSelected(true);
                    tvRefuse.setTextColor(Color.WHITE);
                    tvRefuse.setClickable(false);
                    tvOrder.setText("已拒绝");
                    tvRefuse.setVisibility(View.GONE);
                } else if(id == R.id.civ_header){
                    MineDetailActivity.startActivityWithUserId(DatingAssistantsActivity.this,bean.getSenderUid());
                }
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_dating_assistants;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        /*TODO 刷新*/
        page = 1;
        p.getList(1);
        refreshLayout.finishRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        /*TODO 加载*/
        p.getList(++page);
        refreshLayout.finishLoadMore();
    }

    @Override
    public void setAppointList(ArrayList<AppointmentListBean> records) {
        if (page == 1) {
            mAdapter.setNewData(records);
        } else {
            mAdapter.addData(records);
        }
    }

    @Override
    public void acceptAppointment(HttpResult data) {
        if (data.status.equals("OK")) {

        } else {
            toastTip(data.description);
        }
    }

    @Override
    public void remindUserSuccess(HttpResult data) {
        if (data.status.equals("OK")) {

        } else {
            toastTip(data.description);
        }
    }

    @Override
    public void setClearList(HttpResult data) {
        //Todo 清空消息列表
        RxBus.get().post(new RefreshAfterClearEvent(EventConstant.appoint_event + ""));
        mAdapter.setNewData(null);
    }
}
