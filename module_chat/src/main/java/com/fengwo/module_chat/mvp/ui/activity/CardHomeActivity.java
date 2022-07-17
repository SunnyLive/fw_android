package com.fengwo.module_chat.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.base.ChatHomeChildRefreshEvent;
import com.fengwo.module_chat.mvp.model.bean.AdvertiseBean;
import com.fengwo.module_chat.mvp.model.bean.CardMemberBean;
import com.fengwo.module_chat.mvp.model.bean.CardMemberModel;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_chat.mvp.presenter.CardHomePresenter;
import com.fengwo.module_chat.mvp.ui.adapter.ChatHomeTopAdapter;
import com.fengwo.module_chat.mvp.ui.contract.ICardHomeView;
import com.fengwo.module_chat.mvp.ui.fragment.ChatHomeChildFragment;
import com.fengwo.module_chat.widgets.header.HeaderViewPager;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;

import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 圈子首页
 */

public class CardHomeActivity extends BaseMvpActivity<ICardHomeView, CardHomePresenter>
        implements ICardHomeView, OnRefreshListener {

    @BindView(R2.id.header)
    HeaderViewPager headerView;
    @BindView(R2.id.ivCircle_avatar)
    ImageView ivAvatar;
    @BindView(R2.id.ivCircle_avatar_litter)
    ImageView ivAvatarLitter;
    @BindView(R2.id.tvTitle)
    TextView tvTitle;
    @BindView(R2.id.tvSubtitle)
    TextView tvSubtitle;
    @BindView(R2.id.vp_center)
    ViewPager vpBanner;
    @BindView(R2.id.indicator)
    ScrollIndicatorView indicatorView;
    @BindView(R2.id.vp)
    ViewPager viewPager;
    @BindView(R2.id.rvMember)
    RecyclerView rvMember;
    @BindView(R2.id.tv_circle_num)
    TextView tvTotalCount;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartRefreshLayout;

    @Autowired
    UserProviderService service;

    private String circleId;
    private BaseQuickAdapter<CardMemberBean, BaseViewHolder> memberAdapter;
    private int groupId;
    private RecommendCircleBean circleData;

    public static void start(Context context, String id) {
        Intent intent = new Intent(context, CardHomeActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    private ChatHomeTopAdapter bannerAdapter;
    private final String[] titles = new String[]{"推荐动态", "最新"};
    private final List<ChatHomeChildFragment> fragments = new ArrayList<>(2);

    @Override
    public CardHomePresenter initPresenter() {
        return new CardHomePresenter();
    }

    @Override
    protected void initView() {
        circleId = getIntent().getStringExtra("id");

//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true)
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("圈子首页")
                .build();
        initBanner();
        initIndicator();
        initHeader();
        initMemberRv();
        SmartRefreshLayoutUtils.setWhiteBlackText(this, smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setEnableLoadMore(false);
        // 获取Banner数据
        p.getHomeBanner("1,50", 1, circleId);
        // 获取圈子信息
        p.getCircleInfo(circleId);
        // 获取会员
        p.getCircleMember(circleId);
    }

    @OnClick(R2.id.tv_enter_circle)
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_enter_circle) {
            if (groupId > 0 && circleData != null)
                ArouteUtils.toChatGroupActivity(String.valueOf(service.getUserInfo().id), String.valueOf(groupId), circleData.name, "");
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_card_home;
    }

    // 初始化头部
    private void initHeader() {
        headerView.setCurrentScrollableContainer(fragments.get(0));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                headerView.setCurrentScrollableContainer(fragments.get(position));
            }
        });
    }

    public void initIndicator() {
        fragments.add(ChatHomeChildFragment.newInstance(circleId, "0"));
        fragments.add(ChatHomeChildFragment.newInstance(circleId, "1"));

        int selectColor = getResources().getColor(R.color.text_33);
        int unSelectColor = getResources().getColor(R.color.text_33);
        indicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(18, 14));
        indicatorView.setSplitAuto(false);
        IndicatorViewPager pager = new IndicatorViewPager(indicatorView, viewPager);
        pager.setClickIndicatorAnim(false);
        pager.setAdapter(new IndicatorViewPager.IndicatorFragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public View getViewForTab(int position, View convertView, ViewGroup container) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(CardHomeActivity.this).inflate(R.layout.chat_item_home_tab, container, false);
                }
                TextView textView = (TextView) convertView;
                textView.setText(titles[position]);
                int width = getTextWidth(textView);
                int padding = DensityUtils.dp2px(CardHomeActivity.this, 20);
                //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
                textView.setWidth((int) (width * 1.2f) + padding);
                return convertView;
            }

            @Override
            public Fragment getFragmentForPage(int position) {
                return fragments.get(position);
            }

            private int getTextWidth(TextView textView) {
                if (textView == null) {
                    return 0;
                }
                Rect bounds = new Rect();
                String text = textView.getText().toString();
                Paint paint = textView.getPaint();
                paint.getTextBounds(text, 0, text.length(), bounds);
                return bounds.left + bounds.width();
            }
        });
    }

    // 初始化Banner
    private void initBanner() {
        bannerAdapter = new ChatHomeTopAdapter();
        vpBanner.setAdapter(bannerAdapter);
    }

    private void initMemberRv() {
        rvMember.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        memberAdapter = new BaseQuickAdapter<CardMemberBean, BaseViewHolder>(R.layout.chat_item_card_member) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, CardMemberBean item) {
                if (helper.getAdapterPosition() == 0) {
                    RecyclerView.MarginLayoutParams lp =
                            (RecyclerView.MarginLayoutParams) helper.itemView.getLayoutParams();
                    lp.leftMargin = 0;
                    helper.itemView.setLayoutParams(lp);
                }
                ImageView imageView = helper.getView(R.id.ivMember);
                if (helper.getAdapterPosition() >= 3) {
                    imageView.setImageResource(R.drawable.ic_card_home_more);
                } else {
                    ImageLoader.loadImg(imageView, item.headImg);
                }
            }
        };
        rvMember.setAdapter(memberAdapter);
    }

    @Override
    public void setBanner(ArrayList<AdvertiseBean> records) {
        smartRefreshLayout.closeHeaderOrFooter();
        if (CommentUtils.isListEmpty(records))
            records.add(new AdvertiseBean());
        bannerAdapter.setData(records);
    }

    @Override
    public void setCircleInfo(RecommendCircleBean data) {
        smartRefreshLayout.closeHeaderOrFooter();
        this.circleData = data;
        ImageLoader.loadImg(ivAvatar, data.thumb);
        ImageLoader.loadImg(ivAvatarLitter, data.thumb);
        tvTitle.setText(data.name);
        tvSubtitle.setText(data.introduce);
    }

    @Override
    public void setMemberList(CardMemberModel data) {
        smartRefreshLayout.closeHeaderOrFooter();
        this.groupId = data.id;
        int memberCount = Integer.parseInt(data.usersNum);
        if (memberCount <= 0) return;
        if (memberCount > 3) {
            data.members = data.members.subList(0, 3);
            data.members.add(new CardMemberBean());
        }
        memberAdapter.setNewData(data.members);
        tvTotalCount.setText(String.format("%s人", data.usersNum));
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        p.getHomeBanner("1,50", 1, circleId);
        // 获取圈子信息
        p.getCircleInfo(circleId);
        // 获取会员
        p.getCircleMember(circleId);

        RxBus.get().post(new ChatHomeChildRefreshEvent());
    }
}