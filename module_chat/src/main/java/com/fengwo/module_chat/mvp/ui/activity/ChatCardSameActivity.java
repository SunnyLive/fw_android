package com.fengwo.module_chat.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.presenter.ChatCardSamePresenter;
import com.fengwo.module_chat.mvp.ui.activity.publish.PostCardTagActivity;
import com.fengwo.module_chat.mvp.ui.adapter.SameLableAdapter;
import com.fengwo.module_chat.mvp.ui.contract.IChatCardSameView;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.bean.CardTagModel;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.widget.AppTitleBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ChatCardSameActivity extends BaseMvpActivity<IChatCardSameView, ChatCardSamePresenter>
        implements IChatCardSameView {
    private final int REQUEST_LABLE_TAG = 101;
    @BindView(R2.id.rv_card_lable)
    RecyclerView rvCardLable;
    @BindView(R2.id.rv_card_list)
    RecyclerView rvCardList;
    @BindView(R2.id.sr_card_list)
    SmartRefreshLayout srCardList;
    @BindView(R2.id.title)
    AppTitleBar titleBar;
    private SameLableAdapter sameLableAdapter;
    private String circleId;
    private String cardId;
    private BaseQuickAdapter<CircleListBean, BaseViewHolder> staggerAdapter;
    private int page = 1;
    private String lableIds;
    private List<CardTagModel> lables = new ArrayList<>();
    private ArrayList<String> lableNames = new ArrayList<>();

    public static void start(Context context, String cardId, String circleId) {
        Intent intent = new Intent(context, ChatCardSameActivity.class);
        intent.putExtra("cardId", cardId);
        intent.putExtra("circleId", circleId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_chat_card_same;
    }

    @Override
    public ChatCardSamePresenter initPresenter() {
        return new ChatCardSamePresenter();
    }

    @Override
    protected void initView() {
        titleBar.setTitle("Same");
        cardId = getIntent().getStringExtra("cardId");
        circleId = getIntent().getStringExtra("circleId");
        p.getLableList(cardId);
        //add lable
        titleBar.setMoreClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostCardTagActivity.start(ChatCardSameActivity.this, Integer.parseInt(circleId), lableIds, REQUEST_LABLE_TAG);
            }
        });
        lableUI();
        initCardRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (data != null && requestCode == REQUEST_LABLE_TAG) { // 添加卡片数据
            List<CardTagModel> tagList = (List<CardTagModel>) data.getSerializableExtra("data");
            if (tagList == null) return;
            handleLable(tagList);
        }
    }

    private static final String TAG = "ChatCardSameActivity";

    /**
     * handle add lable callback
     */
    private void handleLable(List<CardTagModel> tagList) {
        Observable.fromArray(tagList).map((Function<List<CardTagModel>, List<CardTagModel>>) cardTagModels -> {
            StringBuffer sb = new StringBuffer(lableIds);
            lableNames.clear();
            for (CardTagModel model : lables) {
                lableNames.add(model.name);
            }
            for (CardTagModel tagModel : cardTagModels) {
                if (!lableNames.contains(tagModel.name)) {
                    lables.add(tagModel);
                    sb.append(tagModel.id).append(",");
                }
            }
            lableIds = sb.toString();
            return lables;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(r -> {
            sameLableAdapter.setNewData(r);
            page = 1;
            p.getCardList(page + ",20", lableIds);
        });
    }

    private void lableUI() {
        rvCardLable.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        sameLableAdapter = new SameLableAdapter();
        rvCardLable.setAdapter(sameLableAdapter);
        sameLableAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (isFastClick()) return;
                if (lables.size() < 2) {
                    toastTip("不能少于1个");
                    return;
                }
                CardTagModel bean = (CardTagModel) adapter.getData().get(position);
                lables.remove(bean);
                sameLableAdapter.setNewData(lables);
                lableIds = lableIds.replace(bean.id + ",", "");
                page = 1;
                p.getCardList(page + ",20", lableIds);
            }
        });
    }

    @Override
    public void setLableList(List<CardTagModel> datas) {
        lables.clear();
        lables.addAll(datas);
        sameLableAdapter.setNewData(datas);
        StringBuilder sb = new StringBuilder();
        for (CardTagModel bean : datas) {
            int id = bean.id;
            sb.append(id).append(",");
        }
        lableIds = sb.toString();
        page = 1;
        p.getCardList(page + ",20", lableIds);
    }

    @Override
    public void setCardList(List<CircleListBean> datas) {
        if (page > 1) {
            staggerAdapter.addData(datas);
            srCardList.finishLoadMore();
        } else {
            staggerAdapter.setNewData(datas);
            srCardList.finishRefresh();
        }
    }

    @Override
    public void cardLikeSuccess(String id, int positoin) {

    }

    private void initCardRecyclerView() {
        srCardList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                p.getCardList(page + ",20", lableIds.toString());
            }
        });
        srCardList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                p.getCardList((++page) + ",20", lableIds.toString());
            }
        });
        staggerAdapter = new BaseQuickAdapter<CircleListBean, BaseViewHolder>(R.layout.chat_item_home_stagger) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, CircleListBean item) {
                Group group = helper.getView(R.id.groupChat);
                ImageView civHeader = helper.getView(R.id.civChat);
                ImageView ivPoster = helper.getView(R.id.ivChatHeader);
                TextView tvTitle = helper.getView(R.id.tvChat);
                View location = helper.getView(R.id.view_location);
                TextView tvLocation = helper.getView(R.id.tvLocation);
//                ImageView ivChatAgree = helper.getView(R.id.ivChatAgree);
//                ivChatAgree.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        p.cardLike(item.id, helper.getAdapterPosition());
//                    }
//                });
                if (item.isAd == 1) {
                    ViewGroup.LayoutParams lp = ivPoster.getLayoutParams();
                    lp.height = DensityUtils.dp2px(mContext, 129);
                    ImageLoader.loadImg(ivPoster, item.adImage);
                    ivPoster.setLayoutParams(lp);
                } else {
                    ViewGroup.LayoutParams lp = ivPoster.getLayoutParams();
                    lp.height = DensityUtils.dp2px(mContext, 206);
                    ivPoster.setLayoutParams(lp);
                    ImageLoader.loadImg(ivPoster, item.cover);
                }
                location.setVisibility(TextUtils.isEmpty(item.position) ? View.INVISIBLE : View.VISIBLE);
                tvLocation.setText(item.position);
                tvTitle.setText(item.excerpt);
                tvTitle.setVisibility(TextUtils.isEmpty(item.excerpt) ? View.GONE : View.VISIBLE);
                group.setVisibility(TextUtils.isEmpty(item.nickname) ? View.GONE : View.VISIBLE);
                helper.setText(R.id.tvChatName, item.nickname).setText(R.id.tvChatNum, String.valueOf(item.likes));
                ImageLoader.loadImg(civHeader, item.headImg);
            }
        };
        staggerAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<CircleListBean> data = adapter.getData();
            CircleListBean bean = (CircleListBean) data.get(position);
            if (bean.isAd == 1) {
                BrowserActivity.start(this, bean.excerpt, bean.adContentUrl);
            } else
                ChatCardActivityNew.start(this, new ArrayList<>(data), staggerAdapter.getData().get(position).id, "0", 1, 0, 0,0);
        });
        rvCardList.setNestedScrollingEnabled(false);
        rvCardList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvCardList.setAdapter(staggerAdapter);
    }
}
