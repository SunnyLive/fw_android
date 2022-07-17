package com.fengwo.module_flirt.UI.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_flirt.Interfaces.IFlirtRankView;
import com.fengwo.module_flirt.P.FlirtRankPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.adapter.RankAdapter;
import com.fengwo.module_flirt.bean.FlirtRankBean;
import com.fengwo.module_live_vedio.utils.AttentionUtils;
import com.fengwo.module_login.utils.UserManager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RankFragment extends BaseMvpFragment<IFlirtRankView, FlirtRankPresenter> implements IFlirtRankView {
    
    @BindView(R2.id.recycleview)
    RecyclerView recycleview;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R2.id.tv_nick_second)
    TextView tvNickSecond;
    @BindView(R2.id.tv_value_second)
    TextView tvValueSecond;
    @BindView(R2.id.cb_attention_second)
    CheckBox cbAttentionSecond;
    @BindView(R2.id.iv_header_first)
    CircleImageView ivHeaderFirst;
    @BindView(R2.id.tv_nick_first)
    TextView tvNickFirst;
    @BindView(R2.id.tv_value_first)
    TextView tvValueFirst;
    @BindView(R2.id.cb_attention_first)
    CheckBox cbAttentionFirst;
    @BindView(R2.id.iv_header_third)
    ImageView ivHeaderThird;
    @BindView(R2.id.tv_nick_third)
    TextView tvNickThird;
    @BindView(R2.id.tv_value_third)
    TextView tvValueThird;
    @BindView(R2.id.cb_attention_third)
    CheckBox cbAttentionThird;
    @BindView(R2.id.ll_top1)
    LinearLayout llTop1;
    @BindView(R2.id.ll_top2)
    LinearLayout llTop2;
    @BindView(R2.id.ll_top3)
    LinearLayout llTop3;
    @BindView(R2.id.iv_header_second)
    CircleImageView ivHeaderSecond;
    @BindView(R2.id.cl_show_anchor)
    ConstraintLayout clShowAnchor;
    @BindView(R2.id.tv_rank)
    TextView tvRank;
    @BindView(R2.id.iv_rank_anchor)
    CircleImageView ivRankAnchor;
    @BindView(R2.id.tv_rank_anchor_name)
    TextView tvRankAnchorName;
    @BindView(R2.id.tv_rank_value)
    TextView tvRankValue;
    private int page = 1;
    private int type;
    private int charmType;//0-日榜，1-周榜，2-月榜
    private boolean showAnchor = false;
    private RankAdapter rankAdapter;


    public static RankFragment newInstance(int type) {
        Bundle args = new Bundle();
        RankFragment fragment = new RankFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FlirtRankPresenter initPresenter() {
        return new FlirtRankPresenter();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_rank;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {


    }

    @Override
    public void initView(View v) {
        ArouteUtils.inject(v);
        type = getArguments().getInt("type");
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        rankAdapter = new RankAdapter();
        recycleview.setAdapter(rankAdapter);
        rankAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    attention(rankAdapter.getData().get(position).anchorId,rankAdapter.getData().get(position).isAttention ==1);
            }
        });
        smartRefreshLayout.setOnRefreshListener(l -> {
            page = 1;
            getData();
        });
        smartRefreshLayout.setOnLoadMoreListener(l -> {
            page++;
            getData();
        });
//        rankAdapter.setNewData(Arrays.asList(1, 2, 3, 4, 5, 6));
        if (type == 2) {
            charmType = 0;//
        } else if (type == 3) {
            charmType = 1;
        } else if (type == 4) {
            charmType = 2;
        }
        getData();
    }

    private void getData() {
//        clShowAnchor.setVisibility(View.GONE);
        p.getRankData(page, charmType, "");
    }

    private void refreshTopView(int position, FlirtRankBean bean) {
        switch (position) {
            case 0:
                llTop1.setVisibility(bean==null?View.INVISIBLE:View.VISIBLE);
                ImageLoader.loadImg(ivHeaderFirst, bean.headImg);
                tvNickFirst.setText(bean.nickname);
                tvValueFirst.setText("" + (int)bean.charm);
                cbAttentionFirst.setText(bean.isAttention == 1?"已关注":"关注");
                cbAttentionFirst.setChecked(bean.isAttention == 1);
                cbAttentionFirst.setOnClickListener(l->{
                    attention(bean.anchorId,bean.isAttention ==1);

                });
                break;
            case 1:
                llTop2.setVisibility(bean==null?View.INVISIBLE:View.VISIBLE);
                ImageLoader.loadImg(ivHeaderSecond, bean.headImg);
                tvNickSecond.setText(bean.nickname);
                tvValueSecond.setText("" + (int)bean.charm);
                cbAttentionSecond.setChecked(bean.isAttention == 1);
                cbAttentionSecond.setText(bean.isAttention == 1?"已关注":"关注");
                cbAttentionSecond.setOnClickListener(l->{
                    attention(bean.anchorId,bean.isAttention ==1);
                });
                break;
            case 2:
                llTop3.setVisibility(bean==null?View.INVISIBLE:View.VISIBLE);
                ImageLoader.loadImg(ivHeaderThird, bean.headImg);
                tvNickThird.setText(bean.nickname);
                tvValueThird.setText("" + (int)bean.charm);
                cbAttentionThird.setChecked(bean.isAttention == 1);
                cbAttentionThird.setText(bean.isAttention == 1?"已关注":"关注");
                cbAttentionThird.setOnClickListener(l->{
                    attention(bean.anchorId,bean.isAttention ==1);
                });
                break;
        }

    }

    private void attention(int anchorId,boolean isAttention) {
        if (isAttention){
            AttentionUtils.delAttention(anchorId, new LoadingObserver<HttpResult>() {
                @Override
                public void _onNext(HttpResult data) {
                    if (data.isSuccess()) {
                        page =1;
                        getData();
                    }
                    toastTip(data.description);
                }

                @Override
                public void _onError(String msg) {
                }
            });
        }else {
            AttentionUtils.addAttention(anchorId, new LoadingObserver<HttpResult>() {
                @Override
                public void _onNext(HttpResult data) {
                    if (data.isSuccess()) {
                        page =1;
                        getData();
                    }
                    toastTip(data.description);
                }

                @Override
                public void _onError(String msg) {

                }
            });
        }
    }

    @Override
    public void setFlirtRankList(BaseListDto<FlirtRankBean> list) {
        smartRefreshLayout.closeHeaderOrFooter();
        ArrayList<FlirtRankBean> bean = list.records;
        if (bean==null)  bean = new ArrayList<>();
        //突出自己排名问题
        if (page == 1){//防止上拉加载时执行该方法
            for (int i = 0; i < bean.size(); i++){
                FlirtRankBean rankBean = bean.get(i);
                showAnchor = false;
                if (rankBean.anchorId == UserManager.getInstance().getUser().getId()){
                    tvRank.setText(""+(i+1));
                    ImageLoader.loadCircleImg(ivRankAnchor,rankBean.headImg);
                    tvRankAnchorName.setText(rankBean.nickname);
                    tvRankValue.setText(""+(int)rankBean.charm);
                    showAnchor = true;
                    break;
                }
            }
        }
        clShowAnchor.setVisibility(showAnchor?View.VISIBLE:View.GONE);
        int size = bean.size();
        if (page == 1) {
                for (int i = 0; i < (size>2 ? 3:size); i++) {
                    refreshTopView(i,bean.remove(0));
                }
                if (bean.size()>0){
                    recycleview.setBackgroundResource(R.drawable.bg_rank_list);
                    rankAdapter.setNewData(bean);
                }else{
                    if (size==0){
                        View empty = LayoutInflater.from(getContext()).inflate(com.fengwo.module_comment.R.layout.item_base_empty_view, null, false);
                        rankAdapter.setEmptyView(empty);
                    }
                    recycleview.setBackgroundColor(Color.TRANSPARENT);
                }
        } else {
            rankAdapter.addData(list.records);
        }
    }
}
