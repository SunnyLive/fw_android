package com.fengwo.module_flirt.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.event.PaySuccessEvent;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.SafeHandle;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.epf.EPlayerView;
import com.fengwo.module_comment.widget.epf.PlayerScaleType;
import com.fengwo.module_comment.widget.epf.filter.AlphaFrameFilter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.adapter.GiftVpAdapter;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.GiftDto;
import com.fengwo.module_flirt.utlis.CommonUtils;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.bean.WenboGiftMsg;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import razerdp.basepopup.BasePopupWindow;

public class GiftPopWindow extends BasePopupWindow implements TabLayout.BaseOnTabSelectedListener, View.OnClickListener {

    public static final int TYPE_NORMAL_GIFT = 0;
    public static final int TYPE_TIME_GIFT = 1;

    private CompositeDisposable compositeDisposable;
    private String[] titels = {"打赏", "加时道具"};
    private List<List<GiftDto>> allGifts;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout llPoint;
    private View btnRecharge;
    private SparseArray<PagerAdapter> vpAdapters;
    private Context mContext;
    private int currentPage = 0;
    private int numCheckPosition = 0;
    private TextView btnSend;
    private TextView tvBalance;
    private boolean isFastShow = false;//是否切换快速点击
    CountBackUtils countBackUtils;
    private RetrofitUtils retrofitUtils;

    private int vpCheckPosition = 0;
    private long downTime;
    private boolean isFast;

    private Animation operatingAnim;
    private RxBus bus;
    Disposable disposable;

    private CircleImageView ivHeader;
    private TextView tvName;
    private EPlayerView ePlayerView;
    private FragmentManager fragmentManager;
    private long anchorId;
    private final FrameLayout flGiftList;
    private boolean isChatRoom;
    private boolean isFirstEnter;
    private int type;
    private FlirtApiService service;
    private String nickName;
    private boolean isCommend;

    @SuppressLint("CheckResult")
    public GiftPopWindow(Context context, FragmentManager fragmentManager, long anchorId) {
        super(context);
        this.fragmentManager = fragmentManager;
        this.anchorId = anchorId;
        ArouteUtils.inject(this);
        allGifts = new ArrayList<>();
        retrofitUtils = new RetrofitUtils();
        setPopupGravity(Gravity.BOTTOM);
        operatingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_button);
        mContext = context;
        countBackUtils = new CountBackUtils();
        vpAdapters = new SparseArray<>();
        tabLayout = findViewById(R.id.tabview);
        flGiftList = findViewById(R.id.fl_gift_list);
        viewPager = findViewById(R.id.vp);
        llPoint = findViewById(R.id.ll_point);
        btnRecharge = findViewById(R.id.btn_recharge);
        btnSend = findViewById(R.id.btn_send);
        tvBalance = findViewById(R.id.tv_balance);
        ivHeader = findViewById(R.id.iv_header);
        ePlayerView = findViewById(R.id.playview);
        tvName = findViewById(R.id.tv_name);
        btnSend.setOnClickListener(this);
        flGiftList.setOnClickListener(this);
        for (int i = 0; i < titels.length; i++) {
            String title = titels[i];
            TabLayout.Tab t = tabLayout.newTab();
            t.setText(title);
            tabLayout.addTab(t);
        }
        compositeDisposable = new CompositeDisposable();
        getGifts();
        tabLayout.addOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentPoint(position);
                setCheckZero(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bus = RxBus.get();
        btnRecharge.setOnClickListener(v -> {
            ARouter.getInstance().build(ArouterApi.CHONG_ZHI).navigation();
        });

        mHandle = new SafeHandle(getContext()) {
            @Override
            protected void mHandleMessage(Message msg) {
                super.mHandleMessage(msg);
                switch (msg.what) {
                    case WHAT_SHOW_GIFT:
                        showGift();
                        mHandle.sendEmptyMessageDelayed(WHAT_SHOW_GIFT, 500);
                        break;
                }

            }
        };
    }

    public EPlayerView getPlayerView() {
        return ePlayerView;
    }

    private void showGift() {
        getContext().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (bigGiftList) {
                    if (null == ePlayerView || ePlayerView.getVisibility() == View.VISIBLE) return;
                    //TODO
//                    if (viewGiftMsg != null && viewGiftMsg.getVisibility() == View.VISIBLE) return;
                    if (null == simpleExoPlayer) {
                        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext().getApplicationContext());
                        bigGiftListener = new Player.EventListener() {

                            @Override
                            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                                switch (playbackState) {
                                    case ExoPlayer.STATE_READY:
                                        ePlayerView.setVisibility(View.VISIBLE);
                                        L.e("========" + (ePlayerView.getVisibility() == View.VISIBLE));
                                        break;
                                    case ExoPlayer.STATE_ENDED:
                                    case ExoPlayer.STATE_IDLE:

                                        ePlayerView.setVisibility(View.GONE);
                                        if (bigGiftList == null || bigGiftList.size() == 0) {
                                            KLog.e("tag", "//////////////");
                                            dismiss();
                                        }
                                        break;
                                    default:
                                        L.e("aaaaaaaaaaaaaaaaonononon" + playbackState);
                                        break;
                                }
                            }


                            @Override
                            public void onPlayerError(ExoPlaybackException error) {
                                L.e("aaaaaaaaaaaaaaaaononoonPlayerError");
                            }
                        };
                        simpleExoPlayer.addListener(bigGiftListener);
                        ePlayerView.setSimpleExoPlayer(simpleExoPlayer);
                        ePlayerView.setGlFilter(new AlphaFrameFilter());
                    }
                    WenboGiftMsg gift;
                    if (bigGiftList.size() > 0) {
                        gift = bigGiftList.poll();
                    } else {
                        return;
                    }
                    //TODO 显示
//                    showGuardAnimation(gift);
                    String path = gift.getGift().getBigImgPath();
                    int length = path.length();
                    int startIndex = path.lastIndexOf("/") + 1;
                    String giftFileName = path.substring(startIndex, length);
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/fwhynew/" + giftFileName.split("\\.")[0]);
                    if (file.exists()) {
                        ePlayerView.setPlayerScaleType(PlayerScaleType.RESIZE_FIT_CENTER);
                        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getContext().getPackageName()));
                        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.fromFile(file));
                        simpleExoPlayer.prepare(mediaSource);
                        simpleExoPlayer.setPlayWhenReady(true);
                    }
                }
            }
        });
    }

    public void toGiftByType(int type, int anchorId) {
        this.type = type;
        this.anchorId = anchorId;
        tabLayout.getTabAt(type).select();
        if (TextUtils.isEmpty(nickName)) {
            tvName.setText(type == TYPE_TIME_GIFT ? "印象礼物" : "送给 " + nickName);
        }
        ivHeader.setVisibility(type == TYPE_TIME_GIFT ? View.GONE : View.VISIBLE);
        Flowable<HttpResult<List<GiftDto>>> getTimeGift = service.getGiftTime2(new WenboParamsBuilder().put("anchorId", String.valueOf(anchorId)).build());
        getTimeGift.compose(RxUtils.applySchedulers()).subscribeWith(new LoadingObserver<HttpResult<List<GiftDto>>>() {
            @Override
            public void _onNext(HttpResult<List<GiftDto>> httpResult) {
                L.e("=====" + allGifts.size());
                try {
                    if (allGifts.size() > 1) {
                        allGifts.remove(1);
                    }
                    allGifts.add(1, httpResult.data);
                    vpAdapters.remove(vpCheckPosition);
                    GiftVpAdapter adapter = new GiftVpAdapter(mContext, allGifts.get(vpCheckPosition));
                    vpAdapters.put(vpCheckPosition, adapter);
                    viewPager.setAdapter(vpAdapters.get(vpCheckPosition));
                    setPoint(vpCheckPosition);
                    setCheckZero(0);
                } catch (Exception e) {
                    L.e("===========礼物显示 错误");
                }
            }

            @Override
            public void _onError(String msg) {
                ToastUtils.showShort(getContext(), msg);
            }
        });
    }

    private boolean onlyGift = false;

    /**
     * 聊天室调用
     *
     * @param isChatRoom
     */
    public void setIsChatRoom(boolean isChatRoom, boolean isFirstEnter) {
        this.isChatRoom = isChatRoom;
        this.isFirstEnter = isFirstEnter;
        ivHeader.setVisibility(isFirstEnter ? View.GONE : View.VISIBLE);
    }

    public void setOnlyGift(boolean onlyGift) {
        if (onlyGift == this.onlyGift) {
            return;
        }
        this.onlyGift = onlyGift;
        tabLayout.removeAllTabs();
        if (onlyGift) {
            TabLayout.Tab t = tabLayout.newTab();
            t.setText(titels[0]);
            tabLayout.addTab(t);
        } else {
            for (int i = 0; i < titels.length; i++) {
                String title = titels[i];
                TabLayout.Tab t = tabLayout.newTab();
                t.setText(title);
                tabLayout.addTab(t);
            }
        }

    }

    private void getGifts() {
        service = retrofitUtils.createWenboApi(FlirtApiService.class);
        Flowable<HttpResult<List<GiftDto>>> getNormalGift = service.getGiftNormal(new WenboParamsBuilder().build());
        L.e("==========" + anchorId);
        Flowable<HttpResult<List<GiftDto>>> getTimeGift = service.getGiftTime2(new WenboParamsBuilder().put("anchorId", String.valueOf(anchorId)).build());
        compositeDisposable.add(Flowable.zip(getNormalGift, getTimeGift, new BiFunction<HttpResult<List<GiftDto>>, HttpResult<List<GiftDto>>, List<List<GiftDto>>>() {
                    @Override
                    public List<List<GiftDto>> apply(HttpResult<List<GiftDto>> listHttpResult, HttpResult<List<GiftDto>> listHttpResult2) throws Exception {
                        allGifts.add(0, listHttpResult.data);
                        allGifts.add(1, listHttpResult2.data);
                        return allGifts;
                    }
                }).compose(RxUtils.applySchedulers())
                        .subscribeWith(new LoadingObserver<List<List<GiftDto>>>() {
                            @Override
                            public void _onNext(List<List<GiftDto>> data) {
                                initVp();
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        })
        );
    }

    private void initVp() {
        if (null == vpAdapters.get(0)) {
            GiftVpAdapter adapter = new GiftVpAdapter(mContext, allGifts.get(0));
            vpAdapters.put(0, adapter);
            viewPager.setAdapter(vpAdapters.get(0));
            viewPager.setOffscreenPageLimit(3);
            setPoint(0);
        }
    }

    Boolean isType = false;

    @Override
    public void showPopupWindow() {
        super.showPopupWindow();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);//休眠3秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isType = true;
                /**
                 * 要执行的操作
                 */
            }
        }.start();
        RxBus.get().post(new PaySuccessEvent(""));
    }


    public void setBalance(long balance) {
        tvBalance.setText(balance + "");
    }

    public void setAnchorInfo(String headerImg, String nickName) {
        this.nickName = nickName;
        ImageLoader.loadImg(ivHeader, headerImg);
        tvName.setText(type == TYPE_TIME_GIFT ? "印象礼物" : "送给 " + nickName);
    }


    private void setPoint(int position) {
        int count = vpAdapters.get(position).getCount();
        llPoint.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView point = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = DensityUtils.dp2px(mContext, 10);
            params.leftMargin = DensityUtils.dp2px(mContext, 10);
            point.setLayoutParams(params);
            point.setBackgroundResource(com.fengwo.module_comment.R.drawable.selector_gift_point_bg);
            if (0 == i) {
                currentPage = i;
                point.setEnabled(false);
            }
            llPoint.addView(point);
        }
    }

    private void setCurrentPoint(int position) {
        llPoint.getChildAt(position).setEnabled(false);
        llPoint.getChildAt(currentPage).setEnabled(true);
        currentPage = position;

    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_flirt_gifts);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (allGifts.size() <= vpCheckPosition) return;
        vpCheckPosition = tab.getPosition();
        if (null == vpAdapters.get(vpCheckPosition)) {
            GiftVpAdapter adapter = new GiftVpAdapter(mContext, allGifts.get(vpCheckPosition));
            vpAdapters.put(vpCheckPosition, adapter);
        }
        viewPager.setAdapter(vpAdapters.get(vpCheckPosition));
        setPoint(vpCheckPosition);
        setCheckZero(0);
        btnSend.setText(vpCheckPosition == 0 ? "赠送" : "使用");
    }


    private void setCheckZero(int position) {
        if (null == vpAdapters || vpAdapters.size() == 0) {
            return;
        }
        GiftVpAdapter adapter = (GiftVpAdapter) vpAdapters.get(vpCheckPosition);
        adapter.setCheck(position, 0);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    public void destory() {
        if (null != countBackUtils && countBackUtils.isTiming()) {
            countBackUtils.destory();
            countBackUtils = null;
        }
        if (null != vpAdapters) {
            vpAdapters.clear();
            vpAdapters = null;
        }
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        mContext = null;
        bus = null;
        viewPager.setAdapter(null);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_send) {
            if (null == listener) return;
            GiftVpAdapter adapter = (GiftVpAdapter) vpAdapters.get(vpCheckPosition);
            GiftDto dto = adapter.getGift(currentPage);
            if (dto.addDurationNum > 0) {
                L.i("GiftPopWindow", "赠送加时道具");
                if (dto.gears < 3) {
                    listener.onTimeGiftSend(dto, isFirstEnter);
                } else {
                    ToastUtils.showShort(getContext(), "本场加时用尽 需重新进入");
                }
                KLog.e("tag", "//////////////");
                dismiss();
            } else {
                L.i("GiftPopWindow", "赠送普通道具");
                if (isCommend) {
                    if (CommonUtils.showTipMoney(getContext(), dto.price, fragmentManager))
                        return;
                    sendNormalGift((int) anchorId, dto);
                } else {
                    listener.onNormalGiftSend(dto);
                    KLog.e("tag", "//////////////");
                    dismiss();
                }
            }
        } else if (id == R.id.fl_gift_list) {
            KLog.e("tag", "//////////////");
            dismiss();
        }
    }

    OnGiftSendListener listener;

    public void setIsCommend(boolean isCommend) {
        this.isCommend = isCommend;
    }

    public interface OnGiftSendListener {
        void onNormalGiftSend(GiftDto id);

        void onTimeGiftSend(GiftDto id, boolean isFirstEnter);
    }

    public void setOnGiftSendListener(OnGiftSendListener l) {
        listener = l;

    }

    /**
     * 发送礼物
     *
     * @param anchorId
     * @param giftDto
     */
    public void sendNormalGift(int anchorId, GiftDto giftDto) {
        compositeDisposable.add(retrofitUtils.createWenboApi(FlirtApiService.class).sendNormalGift(new WenboParamsBuilder()
                .put("anchorId", anchorId + "")
                .put("giftId", giftDto.id + "")
                .put("quantity", "1").build())
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            showGiftIfsend(giftDto);
                            //TODO  更新余额
                            handleBalance();
                        }

                    }

                    @Override
                    public void _onError(String msg) {
                        if (TextUtils.isEmpty(msg)) return;
                        ToastUtils.showShort(getContext(), msg);
                        KLog.e("tag", "//////////////");
                        dismiss();
                    }
                }));
    }

    private void showGiftIfsend(GiftDto giftDto) {
        WenboGiftMsg msg = new WenboGiftMsg();
        WenboGiftMsg.Gift gift = new WenboGiftMsg.Gift();
        gift.setBigImgPath(giftDto.bigImgPath);
        gift.setGiftName(giftDto.giftName);
        gift.setGiftPrice(giftDto.price + "");
        gift.setSmallImgPath(giftDto.smallImgPath);
        gift.setCharmValue(giftDto.charmValue);
        msg.setGift(gift);
        WenboGiftMsg.User user = new WenboGiftMsg.User();
        UserInfo myUserInfo = UserManager.getInstance().getUser();
        user.setNickname(myUserInfo.nickname);
        user.setHeadImg(myUserInfo.headImg);
        msg.setUser(user);
        addGift(msg);
    }

    public void addGift(WenboGiftMsg gift) {
        //显示发送的礼物消息
//        p.sendText("您 送了1个"+gift.getGift().getGiftName(), MsgType.systemText);
        bigGiftList.add(gift);
        if (!mHandle.hasMessages(WHAT_SHOW_GIFT))
            mHandle.sendEmptyMessage(WHAT_SHOW_GIFT);
    }

    @Autowired
    UserProviderService userProviderService;

    /**
     * 余额处理
     */
    private void handleBalance() {
        userProviderService.updateWallet(integer -> {
            UserInfo userInfo = userProviderService.getUserInfo();
            userInfo.balance = integer;
            tvBalance.setText(String.valueOf(integer));
            userProviderService.setUsetInfo(userInfo);
        });
    }

    private SimpleExoPlayer simpleExoPlayer;
    private Player.EventListener bigGiftListener;
    private Queue<WenboGiftMsg> bigGiftList = new ConcurrentLinkedQueue<>();//礼物队列
    private SafeHandle mHandle;
    private final static int WHAT_SHOW_GIFT = 1;

    @Override
    public void dismiss() {
        if (isType) {
            super.dismiss();
            KLog.e("tag", "//////////////");
            if (mHandle != null) mHandle.removeMessages(WHAT_SHOW_GIFT);
        }
        //

    }
}
