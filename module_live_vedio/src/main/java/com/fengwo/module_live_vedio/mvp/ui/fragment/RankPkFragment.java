package com.fengwo.module_live_vedio.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.utils.ArouteUtils;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.mvp.dto.RankSinglePkDto;
import com.fengwo.module_live_vedio.mvp.presenter.RankPkPresenter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.RankPkAdapter;
import com.fengwo.module_live_vedio.mvp.ui.iview.IRankPkView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.shinichi.library.tool.ui.ToastUtil;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/31
 */
public class RankPkFragment extends BaseMvpFragment<IRankPkView, RankPkPresenter> implements IRankPkView {

    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R2.id.recycleview)
    RecyclerView recycleview;

    private int type = 1;
    private int notifyP;//刷新的item

    public static RankPkFragment newInstance(int type) {
        Bundle args = new Bundle();
        RankPkFragment fragment = new RankPkFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    RankPkAdapter rankPkAdapter;
    private List<RankSinglePkDto> mListData = new ArrayList<>();

    @Override
    public void initView(View v) {
        super.initView(v);
        type = getArguments().getInt("type");
        SmartRefreshLayoutUtils.setClassicsColor(getActivity(), smartRefreshLayout, R.color.white, R.color.text_33);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            getData();
        });
        rankPkAdapter = new RankPkAdapter(mListData);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recycleview.setAdapter(rankPkAdapter);
        rankPkAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (isFastClick()) {
                    return;
                }
//                notifyP = position;
                RankSinglePkDto rankSinglePkDto = rankPkAdapter.getData().get(position);
                if (view.getId() == R.id.tv_attention) {

                    if (rankSinglePkDto.getIsAttention() == 1) {
                        rankSinglePkDto.setIsAttention(0);
                        p.removeAttention(rankSinglePkDto.getUserId());
                    } else {
                        rankSinglePkDto.setIsAttention(1);
                        p.attention(rankSinglePkDto.getUserId());
                    }
                } else if (view.getId() == R.id.iv_header) {
                    ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, rankSinglePkDto.getUserId());
                }

            }
        });
        View empty = LayoutInflater.from(getContext()).inflate(com.fengwo.module_comment.R.layout.item_base_empty_view, null, false);
        rankPkAdapter.setEmptyView(empty);
        getData();
    }

    private void getData() {
        if (type == 1) {
            p.getSinglePk();
        } else if (type == 2) {
            p.getTeamPk();
        } else {
            p.getGuildPk();
        }
    }


    @Override
    protected int setContentView() {
        return R.layout.activity_baselist_notitle;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {

    }

    @Override
    protected RankPkPresenter initPresenter() {
        return new RankPkPresenter();
    }

    @Override
    public void singlePk(List<RankSinglePkDto> singlePkDto) {
        reSetData(singlePkDto);
    }

    @Override
    public void teamPk(List<RankSinglePkDto> teamPkDtp) {
        reSetData(teamPkDtp);
    }

    @Override
    public void guildPk(List<RankSinglePkDto> guildPkDto) {
        reSetData(guildPkDto);
    }

    @Override
    public void addAttention(HttpResult httpResult) {
        ToastUtils.showShort(getActivity(), httpResult.description);
        if (httpResult.isSuccess())
            rankPkAdapter.notifyItemChanged(notifyP);
        rankPkAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeAttention(HttpResult httpResult) {
        ToastUtils.showShort(getActivity(), httpResult.description);
        if (httpResult.isSuccess())
            rankPkAdapter.notifyItemChanged(notifyP);
        rankPkAdapter.notifyDataSetChanged();
    }

    @Override
    public void finishRefresh() {
        if (null != smartRefreshLayout)
            smartRefreshLayout.closeHeaderOrFooter();
    }

    private void reSetData(List<RankSinglePkDto> list) {
        if (null != smartRefreshLayout)
            smartRefreshLayout.closeHeaderOrFooter();
        mListData.clear();
        mListData.addAll(list);
        rankPkAdapter.notifyDataSetChanged();
    }

}
