package com.fengwo.module_live_vedio.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BannerBean;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.event.RedPacketCountEvent;
import com.fengwo.module_comment.event.ToTopEvent;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.IntentRoomActivityUrils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.widget.BannerView.BannerView;
import com.fengwo.module_comment.widget.BannerView.OnBannerItemCliakListenr;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.mvp.presenter.LiveHomePresenter;
import com.fengwo.module_live_vedio.mvp.ui.activity.RankPkActivity;
import com.fengwo.module_live_vedio.mvp.ui.activity.RankTopActivity;
import com.fengwo.module_live_vedio.mvp.ui.activity.RankTuhaoActivity;
import com.fengwo.module_live_vedio.mvp.ui.activity.ReCommendMoreActivity;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LiveHomeAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LiveHomeRecommendAdapter;
import com.fengwo.module_live_vedio.mvp.ui.iview.ILivingHome;
import com.fengwo.module_live_vedio.utils.GridItemDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

;
;


public class LiveHomeRecommentFragment1 extends BaseMvpFragment<ILivingHome, LiveHomePresenter> implements ILivingHome, View.OnClickListener {

    @Autowired
    UserProviderService userProviderService;

    @BindView(R2.id.recycleview)
    RecyclerView recycleview;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R2.id.empty_view)
    View viewEmpty;

    private LiveHomeAdapter liveHomeAdapter;
    private boolean isLoadMore = false;


    private int page = 1;
    private int totalDy = 0;

    private int menuId;
    private RecyclerView rvRecommend;
    private LiveHomeRecommendAdapter recommendAdapter;
    private GridLayoutManager rvLayoutManager;
    private PagerAdapter bannerAdapter;
    private final int MAX_SCROLL = 200;

    private LinearLayout flRecomment;

    private View headerView;
    BannerView vpBanner;
//    private boolean isResume;

    public static LiveHomeRecommentFragment1 newInstance(int menuId) {
        Bundle args = new Bundle();
        args.putInt("menuid", menuId);
        LiveHomeRecommentFragment1 fragment = new LiveHomeRecommentFragment1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setContentView() {
        return R.layout.live_fragment_homerecomment1;
    }

    @SuppressLint("CheckResult")
    @Override
    public void initUI(Bundle savedInstanceState) {
        if (!DarkUtil.isDarkTheme(getActivity())) {
            RxBus.get().toObservable(ToTopEvent.class).compose(bindToLifecycle()).subscribe(new Consumer<ToTopEvent>() {
                @Override
                public void accept(ToTopEvent toTopEvent) throws Exception {
                    if (!isVisiable) return;
                    recycleview.smoothScrollToPosition(0);
                    recycleview.setBackgroundColor(Color.argb(0, 255, 255, 255));
                }
            });
        }
        RxBus.get().toObservable(RedPacketCountEvent.class).compose(bindToLifecycle())
                .subscribe(event -> {
                    initRedPacketEventBus(liveHomeAdapter, event);
                    initRedPacketEventBus(recommendAdapter, event);
                });
    }

    /**
     * 刷新红包角标
     */
    @SuppressLint("CheckResult")
    private void initRedPacketEventBus(BaseQuickAdapter<ZhuboDto, BaseViewHolder> adapter, RedPacketCountEvent event) {
        Observable.just(Pair.create(event, adapter))
                .compose(bindToLifecycle())
                .map(pair -> {
                    KLog.i("RedPacketRefreshEventBus", "收到消息 : event : " + pair.first.getChannelId());
                    int position = -1;
                    if (pair.second != null && pair.second.getData() != null && !pair.second.getData().isEmpty()) {
                        int i = 0;
                        for (ZhuboDto zhuboDto : pair.second.getData()) {
                            if (zhuboDto.channelId == pair.first.getChannelId()) {
                                int isHaveRed = pair.first.isHasRedPacket() ? 1 : 0;
                                if (zhuboDto.isHaveRed != isHaveRed) {
                                    zhuboDto.isHaveRed = isHaveRed;
                                    position = i;
                                    return Pair.create(position, pair.second);
                                }
                                break;
                            }
                            i++;
                        }
                    }
                    return Pair.create(position, pair.second);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pair -> {
                    if (pair.second == null || pair.first == -1) {
                        return;
                    }
                    pair.second.notifyDataSetChanged();
                    KLog.i("RedPacketRefreshEventBus", "收到消息 " + pair.second.getClass().getSimpleName() + " 刷新UI");
                });

    }


    @Override
    public void initView(View v) {
        super.initView(v);
        SmartRefreshLayoutUtils.setTransparentBgWithWhileText(getActivity(), smartrefreshlayout);
        smartrefreshlayout.setEnableOverScrollDrag(true);
        menuId = getArguments().getInt("menuid");
        Log.e("tag", "initView");
        initRecycleView();
        initHeaderView();
        p.getRecomment();
        p.getBanner();
        p.getZhuboList(1, menuId);

        smartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                totalDy = 0;
                p.getZhuboList(1, menuId);
                p.getBanner();
                Log.e("tag", "onRefresh");
                p.getRecomment();
//                    p.getBanner();
            }
        });
        smartrefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isLoadMore = true;
                int page = LiveHomeRecommentFragment1.this.page + 1;
                p.getZhuboList(page, menuId);
            }
        });
    }

    private void initHeaderView() {
        headerView = View.inflate(getActivity(), R.layout.live_item_home_header, null);
        vpBanner = headerView.findViewById(R.id.banner);
        flRecomment = headerView.findViewById(R.id.fl_recomment);
        rvRecommend = headerView.findViewById(R.id.rv_recommend);
        headerView.findViewById(R.id.btn_totuhao).setOnClickListener(this);
        headerView.findViewById(R.id.btn_totop).setOnClickListener(this);
        headerView.findViewById(R.id.btn_topk).setOnClickListener(this);
        headerView.findViewById(R.id.tv_more).setOnClickListener(this);
        liveHomeAdapter.addHeaderView(headerView);
    }


    @Override
    public void onClick(View view) {
        if (isFastClick()) return;
        int id = view.getId();
        if (id == R.id.btn_totuhao) {
            startActivity(RankTuhaoActivity.class);
        } else if (id == R.id.btn_totop) {
            startActivity(RankTopActivity.class);
        } else if (id == R.id.btn_topk) {
            startActivity(RankPkActivity.class);
        } else if (id == R.id.tv_more) {
            startActivity(ReCommendMoreActivity.class);
        }
    }

    @Override
    protected LiveHomePresenter initPresenter() {
        return new LiveHomePresenter();
    }

    @Override
    public void initHeaderRv(List<ZhuboDto> records) {
        if (CommentUtils.isListEmpty(records)) {
            flRecomment.setVisibility(View.GONE);
            rvRecommend.setVisibility(View.GONE);
        } else {
            rvRecommend.setVisibility(View.VISIBLE);
            flRecomment.setVisibility(View.VISIBLE);
        }
        if (null == recommendAdapter) {
            rvLayoutManager = new GridLayoutManager(getActivity(), 2);
            rvLayoutManager.setSmoothScrollbarEnabled(true);
            rvLayoutManager.setAutoMeasureEnabled(true);
            rvRecommend.setHasFixedSize(true);
            rvRecommend.setNestedScrollingEnabled(false);
            rvRecommend.setLayoutManager(rvLayoutManager);
            GridItemDecoration decoration = new GridItemDecoration(DensityUtils.dp2px(getContext(), 10));
            decoration.setNeedPadding(true);
            recommendAdapter = new LiveHomeRecommendAdapter(getLifecycle(), records);
            rvRecommend.setAdapter(recommendAdapter);
            rvRecommend.addItemDecoration(decoration);
            rvRecommend.setNestedScrollingEnabled(false);
            recommendAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                ArrayList<ZhuboDto> data = (ArrayList<ZhuboDto>) recommendAdapter.getData();
                showFVExitDialog(data, position);
            });
        } else {
            recommendAdapter.setNewData(records);
        }

    }

    @Override
    public void setBanner(List<BannerBean> result) {
        if (CommentUtils.isListEmpty(result)) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(result);
        vpBanner.setBanner(arrayList);
        vpBanner.setOnBannerItemCliakListenr(new OnBannerItemCliakListenr() {
            @Override
            public void jump(String action, String id, int pos) {


            }
        });
        //mCircleList = result;
        //mCircleList.add(0, result.get(result.size() - 1));
        //mCircleList.add(result.get(0));
        //initHeaderBanner();
    }

    private void initRecycleView() {
        GridLayoutManager listManager = new GridLayoutManager(getActivity(), 2);
        recycleview.setLayoutManager(listManager);
        if (!DarkUtil.isDarkTheme(getActivity())) {
            recycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                        totalDy -= dy;
//                        int alpha = Math.min(Math.abs(totalDy), MAX_SCROLL) * 255 / MAX_SCROLL;
//                        recyclerView.setBackgroundColor(Color.argb(alpha, 255, 255, 255));
                }
            });
        }
        liveHomeAdapter = new LiveHomeAdapter(getLifecycle());
        liveHomeAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (isFastClick()) return;
            ArrayList<ZhuboDto> data1 = (ArrayList<ZhuboDto>) liveHomeAdapter.getData();
            //这里过滤掉position 和 data 对应不上。所以不需要过滤
//            ArrayList<ZhuboDto> collect = (ArrayList<ZhuboDto>) StreamSupport.stream(data1)
//                    .filter(e -> e.status == 2)
//                    .collect(Collectors.toList());
            ZhuboDto zhuboDto = data1.get(position);
            if (zhuboDto.status == 2) {
                showFVExitDialog(data1, position);
            } else {
                ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, zhuboDto.channelId);
            }
        });
        recycleview.setAdapter(liveHomeAdapter);
        GridItemDecoration decoration = new GridItemDecoration(DensityUtils.dp2px(getContext(), 5));
        decoration.setNeedPadding(true);
        recycleview.addItemDecoration(decoration);
    }


    @Override
    public void setZhuboList(List<ZhuboDto> data, int page) {
        this.page = page;
        if ((CommentUtils.isListEmpty(data) && page == 1) || (null != liveHomeAdapter && CommentUtils.isListEmpty(liveHomeAdapter.getData()))) {
            viewEmpty.setVisibility(View.VISIBLE);
        } else {
            viewEmpty.setVisibility(View.GONE);
        }
//        if (isResume) {
//            //     recycleview.setBackgroundColor(Color.argb(0, 255, 255, 255));
//            isResume = false;
//        }

        if (isLoadMore) {
            liveHomeAdapter.addData(data);
            if (page == 2 && liveHomeAdapter.getItemCount() <= 4) {
                //   recycleview.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        } else {
            totalDy = 0;
            if (CommentUtils.isListEmpty(data) && page == 1) {
                viewEmpty.setVisibility(View.VISIBLE);
            } else {
                viewEmpty.setVisibility(View.GONE);
            }
            liveHomeAdapter.setNewData(data);
            if (page == 1 && data.size() <= 4) {
                //    recycleview.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        }
        isLoadMore = false;
    }

    /**
     * 关闭悬浮窗提示
     *
     * @param collect
     * @param position
     */
    private void showFVExitDialog(ArrayList<ZhuboDto> collect, int position) {
        FloatingView floatingView = FloatingView.getInstance();
        if (floatingView.isShow()) {
            ExitDialog dialog = new ExitDialog();
            dialog.setNegativeButtonText("取消")
                    .setPositiveButtonText("确定退出")
                    .setTip("进入直播间会退出达人房间\n印象值将归零，是否要退出")
                    .setGear(floatingView.getGear())
                    .setNickname(floatingView.getNickname())
                    .setRoomId(floatingView.getRoomId())
                    .setExpireTime(floatingView.getExpireTime())
                    .setHeadImg(floatingView.getHeadImg())
                    .setRoomId(floatingView.getRoomId())
                    .setExitType(ExitDialog.ENTER_LIVING)
                    .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                        @Override
                        public void onConfirm() {
                            IntentRoomActivityUrils.setRoomActivity(collect.get(position).channelId, collect, position);
                            // LivingRoomActivity.start(getActivity(), collect, position);
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
            dialog.show(getChildFragmentManager(), "");
        } else {
            IntentRoomActivityUrils.setRoomActivity(collect.get(position).channelId, collect, position);
            //     LivingRoomActivity.start(getActivity(), collect, position);
        }
    }

    @Override
    public void setLoadmoreEnable(boolean b) {
        smartrefreshlayout.closeHeaderOrFooter();
        smartrefreshlayout.setNoMoreData(!b);
    }

    private List<BannerBean> mCircleList;

    private Disposable intervalDisposable;

  /*  private void initHeaderBanner() {
        vpBanner = headerView.findViewById(R.id.vp_banner);
        ViewPagerIndicator viewPagerIndicator = headerView.findViewById(R.id.indicator_circle_line);
        bannerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return mCircleList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.live_item_banner_img, container, false);
                ImageView iv = view.findViewById(R.id.iv_circle_icon);
                view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ImageLoader.loadRouteImg(iv, mCircleList.get(position).url, iv.getWidth(), iv.getHeight());
                    }
                });
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(mCircleList.get(position).enterUrl)) return;
                        BrowserActivity.start(getActivity(), mCircleList.get(position).title, mCircleList.get(position).enterUrl + "?token=" + userProviderService.getToken());
                    }
                });
                container.addView(view);
                return view;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };
        vpBanner.setAdapter(bannerAdapter);
        vpBanner.setPageMargin(DensityUtils.dp2px(getContext(), 10F));
        vpBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (vpBanner.getCurrentItem() == 0)
                        vpBanner.setCurrentItem(mCircleList.size() - 2, false);
                    else if (vpBanner.getCurrentItem() == mCircleList.size() - 1)
                        vpBanner.setCurrentItem(1, false);
                    startBannerInterval();
                }
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    stopBannerInterval();
                }
            }
        });
        vpBanner.setCurrentItem(1);
        viewPagerIndicator.setViewPager(vpBanner, true);
        startBannerInterval();//初始化开始轮播
    }

    */


    private void startBannerInterval() {
//        if (null != mCircleList && mCircleList.size() > 0 && intervalDisposable == null)
//            intervalDisposable = Flowable.interval(3, TimeUnit.SECONDS)
//                    .compose(RxUtils.applySchedulers())
//                    .subscribe(i -> {
//                        vpBanner.setCurrentItem(vpBanner.getCurrentItem() + 1);
//
//                    });//轮播的
    }

    private void stopBannerInterval() {
//        if (intervalDisposable != null && !intervalDisposable.isDisposed()) {
//            intervalDisposable.dispose();
//            intervalDisposable = null;
//        }//轮播的
    }

    @Override
    public void onPause() {
        super.onPause();
        stopBannerInterval();
        vpBanner.stopAutoScroll();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("tag", "onResume");
        vpBanner.startAutoScroll();
//        if (CommentUtils.isListEmpty(mCircleList))
//            startBannerInterval();
//        p.getRecomment();
        //不刷新列表不置顶  解决点击进入直播间 返回没有回到指定位置问题
//        recycleview.scrollToPosition(0);
//        isResume = true;
//        p.getZhuboList(1, menuId);
    }

    @Override
    public void netError() {
        smartrefreshlayout.finishRefresh();
        smartrefreshlayout.finishLoadMore();
//        smartrefreshlayout.closeHeaderOrFooter();
    }

    private boolean isVisiable;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisiable = isVisibleToUser;
    }
}
