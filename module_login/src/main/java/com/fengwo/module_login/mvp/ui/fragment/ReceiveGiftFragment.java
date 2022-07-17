package com.fengwo.module_login.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.ReceiveGiftDto;
import com.fengwo.module_login.mvp.presenter.ReceiveGiftPresenter;
import com.fengwo.module_login.mvp.ui.iview.IReceiveGiftView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import butterknife.BindView;

public class ReceiveGiftFragment extends BaseMvpFragment<IReceiveGiftView, ReceiveGiftPresenter>
        implements IReceiveGiftView {

    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R2.id.recyclerview)
    RecyclerView recyclerView;

    private TextView tvCount; // 头部数值
    private BaseQuickAdapter adapter;

    private final String PAGE_SIZE = ",20";
    private int type = 1;
    private int page = 1;
    private boolean isRefresh = true;
    private String[] titles = new String[]{"今日收到总花蜜值", "本周收到总花蜜值", "本月收到总花蜜值"};

    public static ReceiveGiftFragment newInstance(@IntRange(from = 1, to = 3) int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        ReceiveGiftFragment fragment = new ReceiveGiftFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ReceiveGiftPresenter initPresenter() {
        return new ReceiveGiftPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.login_fragment_receive_gift;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type");
        }
        initRefresh();
        initRv();
        addHeader();
        p.getReceiveGiftData(page + PAGE_SIZE, type + 1);
    }

    /**
     * 获取数据
     *
     * @param data
     */
    @Override
    public void receiveGift(ReceiveGiftDto data) {
        if (data == null || data.getList() == null) return;
        // 控件恢复
        smartRefreshLayout.setEnableLoadMore(data.getList().getRecords().size() >= 20);
        smartRefreshLayout.closeHeaderOrFooter();
        // 填充数据
        tvCount.setText(String.valueOf(data.getTotalMoney()));
        if (isRefresh) adapter.setNewData(data.getList().getRecords());
        else adapter.addData(data.getList().getRecords());
    }

    private void initRefresh() {
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            isRefresh = true;
            page = 1;
            p.getReceiveGiftData(page + PAGE_SIZE, type + 1);
        });
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            isRefresh = false;
            page += 1;
            p.getReceiveGiftData(page + PAGE_SIZE, type + 1);
        });
    }

    private void initRv() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BaseQuickAdapter<ReceiveGiftDto.ListBean.RecordsBean, BaseViewHolder>(R.layout.login_item_receive_gift) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, ReceiveGiftDto.ListBean.RecordsBean item) {
                ImageView header = helper.setText(R.id.tv_title, item.getUserNickname())
                        .setText(R.id.tv_gift_num, item.getGiftName())
                        .setText(R.id.tv_gift_total, String.format("%d花蜜值", item.getMoney()))
                        .getView(R.id.iv_header);
                ImageLoader.loadImg(header, item.getUserHeadImg());
            }
        };
        recyclerView.setAdapter(adapter);
        View v = LayoutInflater.from(getActivity())
                .inflate(com.fengwo.module_comment.R.layout.item_base_empty_view, null, false);
        adapter.setEmptyView(v);
    }

    private void addHeader() {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.login_item_header_receivegift,
                null, false);
        tvCount = header.findViewById(R.id.tv_fengmizhi);
        TextView tvTitle = header.findViewById(R.id.tv_title);
        tvTitle.setText(titles[type]);
        adapter.addHeaderView(header);
    }
}