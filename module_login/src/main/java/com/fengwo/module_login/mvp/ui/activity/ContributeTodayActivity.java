package com.fengwo.module_login.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.mvp.dto.GuardListDto;
import com.fengwo.module_live_vedio.mvp.dto.LiveProfitDto;
import com.fengwo.module_login.R;

import com.fengwo.module_login.mvp.dto.GiftWallDto;
import com.fengwo.module_login.mvp.presenter.MineInfoPresenter;
import com.fengwo.module_login.mvp.ui.iview.IMineInfoView;
import com.fengwo.module_login.utils.UserManager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 今日花蜜值贡献榜
 */
public class ContributeTodayActivity extends BaseMvpActivity<IMineInfoView, MineInfoPresenter> implements IMineInfoView {

    @BindView(R2.id.recyclerview)
    RecyclerView recyclerview;






    protected final static String PAGE_SIZE = ",10";
    protected int page = 1;
    private int userId;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;

    public boolean isRefresh = true;
    ContributeAdapter adapter;



    @Override
    protected void initView() {
        new ToolBarBuilder().showBack(true)
                .setTitle("今日花蜜值贡献榜")
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .build();
        userId = getIntent().getIntExtra("userId",0);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        smartrefreshlayout.setOnRefreshListener(refreshLayout -> {
            refresh();
        });
        SmartRefreshLayoutUtils.setTransparentBgWithWhileText(this, smartrefreshlayout);
        smartrefreshlayout.setOnLoadMoreListener(refreshLayout -> {
            isRefresh = false;
            page++;
            p.getReceiveList(page + PAGE_SIZE, userId);
        });
        p.getReceiveList(page + PAGE_SIZE, userId);
    }

    protected void refresh() {
        isRefresh = true;
        page = 1;
        p.getReceiveList(page + PAGE_SIZE, userId);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_contribute_today;
    }

    @Override
    public MineInfoPresenter initPresenter() {
        return new MineInfoPresenter();
    }

    @Override
    public void getGiftWall(GiftWallDto data) {

    }

    @Override
    public void setGuardWindow(int total, ArrayList<GuardListDto.Guard> records) {

    }

    @Override
    public void setTodayReceive(int total, List<LiveProfitDto.RecordsBean> records) {
        smartrefreshlayout.closeHeaderOrFooter();
        smartrefreshlayout.setEnableLoadMore(records.size() == 10);
        if (isRefresh){
            if (adapter == null){
                adapter = new ContributeAdapter(records);
                recyclerview.setAdapter(adapter);
                View v = LayoutInflater.from(this).inflate(R.layout.item_base_empty_view, null, false);
                adapter.setEmptyView(v);
            }else {
                adapter.setNewData(records);
            }
        }else {
            adapter.addData(records);
        }
    }

    @Override
    public void showShouhu(Boolean isHost, int id, UserInfo data) {

    }


    public static class ContributeAdapter extends BaseQuickAdapter<LiveProfitDto.RecordsBean, BaseViewHolder> {

        public ContributeAdapter(@Nullable List<LiveProfitDto.RecordsBean> data) {
            super(R.layout.item_contribute_today, data);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        protected void convert(@NonNull BaseViewHolder helper, LiveProfitDto.RecordsBean item) {
            TextView tvPositon = helper.getView(R.id.tv_position);
            TextView tv_lin = helper.getView(R.id.tv_lin);
            tv_lin.setVisibility(View.GONE);
            if (helper.getLayoutPosition() == 0) {
                tvPositon.setBackgroundResource(R.drawable.ic_contribute_today_first);
            } else if (helper.getLayoutPosition() == 1) {
                tvPositon.setBackgroundResource(R.drawable.ic_contribute_today_second);
            } else if (helper.getLayoutPosition() == 2) {
                tvPositon.setBackgroundResource(R.drawable.ic_contribute_today_third);
                tv_lin.setVisibility(View.VISIBLE);
            } else {
                tvPositon.setText(helper.getLayoutPosition() + 1 + ".");
            }
            ImageLoader.loadImg(helper.getView(R.id.iv_header), item.getHeadImg());
            helper.setText(R.id.tv_nick_name, item.getNickname());
            helper.setText(R.id.tv_item_honey_value, DataFormatUtils.formatNumbers(item.getReceive()));
            if (helper.getLayoutPosition() > 2) {
                helper.setTextColor(R.id.tv_item_honey_value, mContext.getResources().getColor(R.color.black));
            } else {
                helper.setTextColor(R.id.tv_item_honey_value, Color.parseColor("#FE3C9C"));
            }
            helper.setVisible(R.id.tv_mine,UserManager.getInstance().getUser().getId() == item.getId());
        }
    }

}
