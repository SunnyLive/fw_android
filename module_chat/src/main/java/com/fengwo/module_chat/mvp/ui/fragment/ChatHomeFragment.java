package com.fengwo.module_chat.mvp.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.model.bean.AdvertiseBean;
import com.fengwo.module_chat.mvp.ui.activity.ChatCardActivityNew;
import com.fengwo.module_chat.mvp.ui.activity.publish.PostCardActivity;
import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_chat.mvp.presenter.ChatHomePresenter;
import com.fengwo.module_chat.mvp.ui.activity.CardHomeActivity;
import com.fengwo.module_chat.mvp.ui.activity.CardSelectActivity;
import com.fengwo.module_chat.mvp.ui.activity.SocialSearchActivity;
import com.fengwo.module_chat.mvp.ui.activity.social.AddressBookActivity;
import com.fengwo.module_chat.mvp.ui.adapter.ChatHomeTopAdapter;
import com.fengwo.module_chat.mvp.ui.contract.IChatHomeView;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.DoubleClickListener;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;

import com.fengwo.module_comment.widget.BannerView.BannerView;
import com.fengwo.module_comment.widget.BannerView.OnBannerItemCliakListenr;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


@Route(path = ArouterApi.CHAT_FRAGMENT_HOME)
public class ChatHomeFragment extends BaseMvpFragment<IChatHomeView, ChatHomePresenter> implements IChatHomeView {

    @Autowired
    UserProviderService service;

    @BindView(R2.id.basetitle)
    View topView;

    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R2.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R2.id.view_top_banner)
    BannerView vpTop;
    @BindView(R2.id.rv_top_category)
    RecyclerView cateRecyclerView;
    @BindView(R2.id.rv_chat_home)
    RecyclerView rvCard;
    @BindView(R2.id.iv_chat_home_bg)
    ImageView ivChatHomeBg;
    @BindView(R2.id.iv_chat_home_search)
    ImageView ivChatHomeSearch;
    @BindView(R2.id.iv_chat_home_publish)
    ImageView iv_chat_home_publish;
    @BindView(R2.id.tv_chat_home_title)
    TextView tvChatHomeTitle;

    private final String PAGE_SIZE = ",20";
    private final int REQUEST_TAB_CHANGE = 1002;
    private int MAX_SCROLL;
    private int page = 1;
    private CircleListBean bean;
    private ChatHomeTopAdapter topAdapter;
    private BaseQuickAdapter<RecommendCircleBean, BaseViewHolder> cateAdapter;
    private BaseQuickAdapter<CircleListBean, BaseViewHolder> staggerAdapter;

    @Override
    protected int setContentView() {
        return R.layout.chat_fragment_home;
    }

    @Override
    protected ChatHomePresenter initPresenter() {
        return new ChatHomePresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initUI(Bundle savedInstanceState) {
        MAX_SCROLL = DensityUtils.dp2px(getActivity(), 100);
        initScrollView();
        initRefreshLayout();
        initCircleRecyclerView();
        initCardRecyclerView();
        // 获取推荐圈子
        p.getRecommendList();
        // 获取顶部Banner
        p.getHomeBanner("1,20", 1);
        // 获取推荐卡片列表
        p.getCardList(page + PAGE_SIZE);

        topView.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                scrollView.fullScroll(View.FOCUS_UP);
            }
        });
    }

    private static final String TAG = "ChatHomeFragment";

    @OnClick({R2.id.iv_chat_home_search, R2.id.iv_chat_home_publish, R2.id.iv_chat_home_contact})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_chat_home_contact) {
            startActivity(AddressBookActivity.class);
        } else if (id == R.id.iv_chat_home_publish) {// 新建社交
            Intent intent = new Intent(getActivity(), PostCardActivity.class);
            getActivity().startActivity(intent);
        } else if (id == R.id.iv_chat_home_search) {
            SocialSearchActivity.start(getActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAB_CHANGE && resultCode == Activity.RESULT_OK) {
            // 获取推荐圈子
            p.getRecommendList();
        }
    }

    @Override
    public void setRecommendCircleList(List<RecommendCircleBean> data) {
        if (data.size() >= 4) {
            data = data.subList(0, 4);
            RecommendCircleBean moreBean = new RecommendCircleBean();
            moreBean.name = "更多";
            moreBean.id = -1;
            data.add(moreBean);
        } else {
            RecommendCircleBean moreBean = new RecommendCircleBean();
            moreBean.name = "更多";
            moreBean.id = -1;
            data.add(moreBean);
        }
        cateAdapter.setNewData(data);
    }

    @Override
    public void setTopBanner(ArrayList<AdvertiseBean> records) {
        if (records == null || records.size() == 0) return;
        vpTop.setBanner(records);
        vpTop.setOnBannerItemCliakListenr(new OnBannerItemCliakListenr() {
            @Override
            public void jump(String action, String id, int pos) {
                if (action.equals(Constants.BANNER_JUMP_TYPE_LIVE)) {
                    try {
//                        LivingRoomActivity.start(getActivity(), Integer.parseInt(id));
                    } catch (Exception e) {

                    }
                } else if (action.equals(Constants.BANNER_JUMP_TYPE_ALBUM)) {
                    try {
                        ArouteUtils.toShortVideoActivity(Integer.parseInt(id));
                    } catch (Exception e) {

                    }

                }
            }
        });
        vpTop.startAutoScroll();
//        // 初始化循环数据
//        AdvertiseBean firstItem = records.get(0);
//        if (records.size() == 1)
//        } else {
//            AdvertiseBean lastItem = records.get(records.size() - 1);
//            records.add(0, lastItem);
//            records.add(firstItem);
//        }
//        topAdapter.setData(records);
    }

    @Override
    public void setCardList(BaseListDto<CircleListBean> data) {
        ArrayList<CircleListBean> beans = data.records;
        if (page == 1) {
            refreshLayout.finishRefresh();
            staggerAdapter.setNewData(beans);
        } else {
            staggerAdapter.addData(beans);
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void loadListFailed(String msg) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        toastTip(msg);
    }

    @Override
    public void cardLikeSuccess(String id, int position) {
        if (bean == null) return;
        if (!TextUtils.equals(bean.id, id)) return;
//        if (TextUtils.equals(bean.isLike, "1")) {
//            card.isLike = "0";
//            int i = Integer.parseInt(card.likes);
//            i -= 1;
//            card.likes = String.valueOf(i);
//            refreshLikeStatus();
//        } else {
//            card.isLike = "1";
//            int i = Integer.parseInt(card.likes);
//            i += 1;
//            card.likes = String.valueOf(i);
//            refreshLikeStatus();
//        }
    }

    // 初始化滑动监听
    private void initScrollView() {
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//            topView.setSelected(scrollY > 0);
            float alpha = Math.min(MAX_SCROLL, scrollY) / (float) MAX_SCROLL;
            int backgroundAlpha = (int) (alpha * 255);
            int backgroundColor = Color.argb(backgroundAlpha, 255, 255, 255);
            iv_chat_home_publish.setImageResource(alpha > 0.5 ? R.drawable.ic_chat_publish_white : R.drawable.ic_chat_publish);
            if (!DarkUtil.isDarkTheme(getActivity())) {
                ivChatHomeSearch.setImageResource(alpha > 0.5 ? R.drawable.chat_ic_home_search_selected : R.drawable.chat_ic_home_search);
                tvChatHomeTitle.setTextColor(alpha > 0.5 ? ContextCompat.getColor(getContext(), R.color.text_33) : ContextCompat.getColor(getContext(), R.color.text_white));
                topView.setBackgroundColor(backgroundColor);
                refreshLayout.setBackgroundColor(backgroundColor);
            }
        });
    }

    // 初始化刷新控件
    private void initRefreshLayout() {
        SmartRefreshLayoutUtils.setTransparentBgWithWhileText(getContext(), refreshLayout);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnRefreshListener(layout -> {
            L.e("=====刷新");
            page = 1;
            // 获取推荐圈子
            p.getRecommendList();
            // 获取顶部Banner
            p.getHomeBanner("1,20", 1);
            // 获取推荐卡片列表
            p.getCardList(page + PAGE_SIZE);
        });
        refreshLayout.setOnLoadMoreListener(layout -> {
            L.e("=====加载");
            ++page;
            p.getCardList(page + PAGE_SIZE);
        });
    }

    // 初始化顶部轮播图
    private void initTopViewPager() {
    }

    // 初始化分类列表
    private void initCircleRecyclerView() {
        cateRecyclerView.setNestedScrollingEnabled(false);
        cateRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));

        cateAdapter = new BaseQuickAdapter<RecommendCircleBean, BaseViewHolder>(R.layout.chat_item_home_category) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, RecommendCircleBean item) {
                ImageView imageView = helper.setText(R.id.textView, item.name).getView(R.id.iv);
                if (item.id == -1) {
                    imageView.setImageResource(R.drawable.chat_ic_home_more);
                } else ImageLoader.loadCircleImg(imageView, item.thumb);
            }
        };
        cateAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (isFastClick()) {
                return;
            }
            RecommendCircleBean bean = (RecommendCircleBean) adapter.getData().get(position);
            if (bean.id == -1) {
                Intent intent = new Intent(getContext(), CardSelectActivity.class);
                startActivityForResult(intent, REQUEST_TAB_CHANGE);
            } else {
                CardHomeActivity.start(getContext(), String.valueOf(bean.id));
            }
        });
        cateRecyclerView.setAdapter(cateAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获取推荐卡片列表 && 防止点击第二页详情返回刷新数据
        if (isVisiable && page == 1) {
            vpTop.startAutoScroll();
            page = 1;
            // 获取推荐卡片列表
            p.getCardList(page + PAGE_SIZE);
        }
        if (DarkUtil.isDarkTheme(getActivity())) {
            ivChatHomeBg.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != vpTop)
            vpTop.stopAutoScroll();
    }

    boolean isVisiable;
    private boolean isFirstGif = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisiable = isVisibleToUser;
        if (isFirstGif && isVisibleToUser) {
            isFirstGif = false;
            ImageLoader.loadGif1OneTime(iv_chat_home_publish, R.drawable.ic_chat_publish_gif);
        }
        if (isVisibleToUser) {
            if ((!vpTop.isRunning)) {
                vpTop.startAutoScroll();
            }
        }
    }

    // 初始化底部RecyclerView
    private void initCardRecyclerView() {
        staggerAdapter = new BaseQuickAdapter<CircleListBean, BaseViewHolder>(R.layout.chat_item_home_stagger) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, CircleListBean item) {
                Group group = helper.getView(R.id.groupChat);
                ImageView civHeader = helper.getView(R.id.civChat);
                ImageView ivPoster = helper.getView(R.id.ivChatHeader);
                TextView tvTitle = helper.getView(R.id.tvChat);
                View location = helper.getView(R.id.view_location);
                TextView tvLocation = helper.getView(R.id.tvLocation);

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
//                helper.addOnClickListener(R.id.ivChatAgree);
            }
        };
        //点赞
//        staggerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                //TODO
////                bean = (CircleListBean) adapter.getData().get(position);
////                p.cardLike(bean.id);
//            }
//        });
        staggerAdapter.setOnItemClickListener((adapter, view, position) -> {
            CircleListBean bean = (CircleListBean) adapter.getData().get(position);
            if (bean.isAd == 1) {
                BrowserActivity.start(getContext(), bean.excerpt, bean.adContentUrl);
            } else {
                if (isFastClick()) return;
                ChatCardActivityNew.start(getContext(), new ArrayList<>(adapter.getData()), ((CircleListBean) adapter.getData().get(position)).id, "0", 0, 0, 0, 1);
            }
        });
        rvCard.setNestedScrollingEnabled(false);
        rvCard.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvCard.setAdapter(staggerAdapter);
        View v = LayoutInflater.from(getActivity()).inflate(com.fengwo.module_comment.R.layout.item_base_empty_view, null, false);
        staggerAdapter.setEmptyView(v);
    }
}