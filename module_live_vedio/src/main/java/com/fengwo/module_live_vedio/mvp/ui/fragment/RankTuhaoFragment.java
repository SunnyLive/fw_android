package com.fengwo.module_live_vedio.mvp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.mvp.dto.RankTuhaoDto;
import com.fengwo.module_live_vedio.mvp.presenter.RankTuhaoPresenter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.RankTuhaoAdapter;
import com.fengwo.module_live_vedio.mvp.ui.iview.IRankTuhaoView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/31
 */
public class RankTuhaoFragment extends BaseMvpFragment<IRankTuhaoView, RankTuhaoPresenter> implements IRankTuhaoView {
    @BindView(R2.id.recycleview)
    RecyclerView recycleview;
    @BindView(R2.id.iv_header_second)
    ImageView ivHeaderSecond;
    @BindView(R2.id.tv_nick_second)
    TextView tvNickSecond;
    @BindView(R2.id.tv_honey_second)
    TextView tvHoneySecond;
    @BindView(R2.id.iv_header_first)
    ImageView ivHeaderFirst;
    @BindView(R2.id.tv_nick_first)
    TextView tvNickFirst;
    @BindView(R2.id.tv_honey_first)
    TextView tvHoneyFirst;
    @BindView(R2.id.iv_header_third)
    ImageView ivHeaderThird;
    @BindView(R2.id.tv_nick_third)
    TextView tvNickThird;
    @BindView(R2.id.tv_honey_third)
    TextView tvHoneyThird;

    private int type;

    private RankTuhaoAdapter rankTuhaoAdapter;
    private List<RankTuhaoDto> mListData = new ArrayList<>();

    public static RankTuhaoFragment newInstance(int type) {
        Bundle args = new Bundle();
        RankTuhaoFragment fragment = new RankTuhaoFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RankTuhaoPresenter initPresenter() {
        return new RankTuhaoPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.live_fragment_rank_tuhao;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        rankTuhaoAdapter = new RankTuhaoAdapter(R.layout.live_item_rank_tuhao, mListData);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recycleview.setAdapter(rankTuhaoAdapter);
        rankTuhaoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (isFastClick()) {
                    return;
                }
                int id = view.getId();
                if (id == R.id.tv_header) {
                    ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, rankTuhaoAdapter.getData().get(position).getValue());
                }
            }
        });
        View v = LayoutInflater.from(getActivity()).inflate(com.fengwo.module_comment.R.layout.item_base_empty_view, null, false);
        rankTuhaoAdapter.setEmptyView(v);

        p.getRankTuhaoData(type);//1：小时榜 2：日榜，3：周榜，4：月榜 默认2
    }


    @Override
    public void rankTuhaoDate(List<RankTuhaoDto> rankTuhaoDtos) {
        List list = setRankData(rankTuhaoDtos);
        mListData.addAll(list);
        rankTuhaoAdapter.setNewData(mListData);
    }

    private List setRankData(List<RankTuhaoDto> rankTuhaoDtos) {
        if (rankTuhaoDtos != null) {
            if (rankTuhaoDtos.size() > 0) {
                RankTuhaoDto first = rankTuhaoDtos.remove(0);
                ImageLoader.loadImg(ivHeaderFirst, first.getHeadImg());
                tvNickFirst.setText(first.getNickname());
                tvHoneyFirst.setText("花钻" + DataFormatUtils.formatNumbers(first.getScore()));
                ivHeaderFirst.setOnClickListener(view -> {
                    ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, first.getValue());
                });
            }
            if (rankTuhaoDtos.size() > 0) {
                RankTuhaoDto second = rankTuhaoDtos.remove(0);
                ImageLoader.loadImg(ivHeaderSecond, second.getHeadImg());
                tvNickSecond.setText(second.getNickname());
                tvHoneySecond.setText("花钻" + DataFormatUtils.formatNumbers(second.getScore()));
                ivHeaderSecond.setOnClickListener(view -> {
                    ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, second.getValue());
                });
            }
            if (rankTuhaoDtos.size() > 0) {
                RankTuhaoDto third = rankTuhaoDtos.remove(0);
                ImageLoader.loadImg(ivHeaderThird, third.getHeadImg());
                tvNickThird.setText(third.getNickname());
                tvHoneyThird.setText("花钻" + DataFormatUtils.formatNumbers(third.getScore()));
                ivHeaderThird.setOnClickListener(view -> {
                    ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, third.getValue());
                });
            }
        }
        return rankTuhaoDtos;
    }

    private String bigDecimalD(double num) {
        return new BigDecimal(num).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

}
