package com.fengwo.module_chat.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_chat.mvp.model.bean.MerchantListBoean;
import com.fengwo.module_chat.mvp.ui.adapter.BubbleRecommendAdapter;
import com.fengwo.module_chat.mvp.ui.adapter.MerchantTagAdapter;
import com.fengwo.module_chat.mvp.ui.adapter.RateAdapter;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * @author GuiLianL
 * @intro 气泡推荐
 * @date 2019/11/25
 */
public class BubbleRecommendActivity extends BaseListActivity<MerchantListBoean> {

    @Autowired
    UserProviderService userProviderService;

    private String tagId;

    public static void start(Context context, String tagId, String name) {
        Intent intent = new Intent(context, BubbleRecommendActivity.class);
        intent.putExtra("tagId", tagId);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }

    private List<String> mListData = new ArrayList<>();
    private BubbleRecommendAdapter mAdapter;

    @Override
    protected void initView() {
        tagId = getIntent().getStringExtra("tagId");
        super.initView();
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().setTitleColor(R.color.text_33)
                .setTitle(getIntent().getStringExtra("name"))
                .showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .build();
        SmartRefreshLayoutUtils.setTransparentBlackText(this, smartRefreshLayout);
    }

    @Override
    public Flowable setNetObservable() {
        return new RetrofitUtils().createApi(ChatService.class).getMerchantList(page + PAGE_SIZE, tagId);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_recommend_acticle;
    }

    @Override
    public void setData(List<MerchantListBoean> datas, int page) {
        super.setData(datas, page);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            MerchantListBoean model = (MerchantListBoean) adapter.getData().get(position);
            String realUrl = String.format("%s&token=%s", model.url, userProviderService.getToken());
//            WebActivity.startWithTitleUrl(this, model.merchantName, realUrl);
            BrowserActivity.start(this, model.merchantName, realUrl);
        });
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, MerchantListBoean item, int position) {
        ImageView imageView = helper.getView(R.id.iv_recommend);
        RecyclerView rvTag = helper.getView(R.id.rv_tag);
        RecyclerView rvRate = helper.getView(R.id.rv_rate);
        ImageLoader.loadImg(imageView, item.cover);
        helper.setText(R.id.tv_recommend_title, item.merchantName)
                .setText(R.id.tv_recommend_des, item.merchantPosition);
        rvRate.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRate.setNestedScrollingEnabled(false);
        rvRate.setAdapter(new RateAdapter(item.compreScore));
        if (item.merchantTagList != null) {
            rvTag.setVisibility(View.VISIBLE);
            rvTag.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            if (item.merchantTagList.size() > 3) {
                rvTag.setAdapter(new MerchantTagAdapter(item.merchantTagList.subList(0, 3)));
            } else {
                rvTag.setAdapter(new MerchantTagAdapter(item.merchantTagList));
            }
        } else rvTag.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_bubble_recommend;
    }
}
