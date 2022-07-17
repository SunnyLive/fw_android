package com.fengwo.module_live_vedio.mvp.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.ActBannerBean;
import com.fengwo.module_comment.bean.JumpInvtationDataBean;
import com.fengwo.module_comment.bean.PkFloatingScreenBean;
import com.fengwo.module_comment.bean.PkMatchScoreDto;
import com.fengwo.module_comment.bean.RefreshProcessesBean;
import com.fengwo.module_comment.bean.UserLevelBean;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.GiftAnimationUtil;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.utils.SafeHandle;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.utils.SvgaUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.BannerView.act.ActBannerView;
import com.fengwo.module_comment.widget.BannerView.act.OnActBannerItemCliakListenr;
import com.fengwo.module_comment.widget.CenteredImageSpan;
import com.fengwo.module_comment.widget.CircleUrlImageSpan;
import com.fengwo.module_comment.widget.DanmakuView;
import com.fengwo.module_comment.widget.EPlayTextureView;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_comment.widget.UrlImageSpan;
import com.fengwo.module_comment.widget.epf.PlayerScaleType;
import com.fengwo.module_comment.widget.epf.filter.AlphaFrameFilter;
import com.fengwo.module_comment.widget.likeview.KsgLikeView;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.eventbus.ChangeRoomEvent;
import com.fengwo.module_live_vedio.mvp.dto.ActivityDto;
import com.fengwo.module_live_vedio.mvp.dto.ChannelShareInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.CloseLiveDto;
import com.fengwo.module_live_vedio.mvp.dto.CustomLiveEndDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomActDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomPkActivityDto;
import com.fengwo.module_live_vedio.mvp.dto.FloatScreenBean;
import com.fengwo.module_live_vedio.mvp.dto.GiftBoardcastDto;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;
import com.fengwo.module_live_vedio.mvp.dto.GiftPiaopingDto;
import com.fengwo.module_live_vedio.mvp.dto.GuardListDto;
import com.fengwo.module_live_vedio.mvp.dto.LastFrameDto;
import com.fengwo.module_live_vedio.mvp.dto.LivingRoomBannerDto;
import com.fengwo.module_live_vedio.mvp.dto.MatchTeamResult;
import com.fengwo.module_live_vedio.mvp.dto.PkResultDto;
import com.fengwo.module_live_vedio.mvp.dto.PkScoreDto;
import com.fengwo.module_live_vedio.mvp.dto.PkTeamInfo;
import com.fengwo.module_live_vedio.mvp.dto.PkTimeDto;
import com.fengwo.module_live_vedio.mvp.dto.PunishTimeDto;
import com.fengwo.module_live_vedio.mvp.dto.RechargeDto;
import com.fengwo.module_live_vedio.mvp.dto.RoomMemberChangeMsg;
import com.fengwo.module_live_vedio.mvp.dto.WatcherDto;
import com.fengwo.module_live_vedio.mvp.presenter.LivingRoomPresenter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LivingMsgAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.StartPkAnimAdapter;
import com.fengwo.module_live_vedio.mvp.ui.df.ShareCodeDialog;
import com.fengwo.module_live_vedio.mvp.ui.event.ChangeAnimateEvent;
import com.fengwo.module_live_vedio.mvp.ui.event.RefrshWishEvent;
import com.fengwo.module_live_vedio.mvp.ui.iview.ILivingRoom;
import com.fengwo.module_live_vedio.mvp.ui.pop.ActivityPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.AnchorWishPop;
import com.fengwo.module_live_vedio.mvp.ui.pop.GiftPopWindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.NewUserInfoPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.PkContributePopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.PlanePopWindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.ShouhuListPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.ToutiaoInfoPopwindow;
import com.fengwo.module_live_vedio.utils.DanmakuConfigUtil;
import com.fengwo.module_live_vedio.utils.MatchPkResultUtil;
import com.fengwo.module_live_vedio.utils.PrivilegeEffectUtils;
import com.fengwo.module_live_vedio.utils.WishCacheMr;
import com.fengwo.module_live_vedio.widget.AutoScrollTextView;
import com.fengwo.module_live_vedio.widget.falllayout.FallingLayout;
import com.fengwo.module_live_vedio.widget.giftlayout.GiftRootLayout;
import com.fengwo.module_live_vedio.widget.giftlayout.bean.GiftBean;
import com.fengwo.module_live_vedio.widget.giftlayout.bean.NoticeBean;
import com.fengwo.module_websocket.EventConstant;
import com.fengwo.module_websocket.bean.InvitePkMsg;
import com.fengwo.module_websocket.bean.LivingRoomGuardMsg;
import com.fengwo.module_websocket.bean.LivingRoomTextMsg;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public abstract class BaseLiveingRoomActivity extends BaseMvpActivity<ILivingRoom, LivingRoomPresenter> implements ILivingRoom, UMShareListener, AnchorWishPop.OnWishClickListener {


    public boolean endActivity = false;
    protected AnimatorSet bulletAnimatorSet;


    private SvgaUtils pkSvga;
    protected SVGAImageView svga_pk;



    private SvgaUtils animationSvga;
    protected SVGAImageView svga_animation;

    protected EPlayTextureView frameAnimationView;//大礼物
    protected SimpleExoPlayer silkyAnimation;

    private Queue<GiftBean> bigGiftList = new ConcurrentLinkedQueue<>();
    private Queue<GiftBean> myGiftList = new ConcurrentLinkedQueue<>();




//    protected EPlayTextureView pkAnimationView;//pk
//    protected SimpleExoPlayer pkAnimation;



    public ArrayList<UserLevelBean> userLevelBeans = new ArrayList<>();
    public PlanePopWindow planePopWindow;

    protected static class MRunnable implements Runnable {
        @Override
        public void run() {

        }
    }

    public int state;
    protected boolean isActivityResume = false;
    public static String ISANIMATION = "isAnimation";
    CountBackUtils somecomingCb, somecomingAct;//进入直播间 飘屏 倒数
    CountBackUtils utils;
    CountBackUtils buyGuardCb;
    CountBackUtils annotationCb;
    CountBackUtils startPkCb;
    private CountBackUtils toutiaoCountback;


    public Queue<NoticeBean> noticeList = new ConcurrentLinkedQueue<>();

    public List<List<GiftDto>> allGifts;
    protected static final int GROUP_BASE = 900000000;
    // public static final int ACTIVITY_TYPE_PUBLISH = 1;
    public static final int ACTIVITY_TYPE_LIVE_PLAY = 2;
    //    public static final int ACTIVITY_TYPE_VOD_PLAY = 3;
//    public static final int ACTIVITY_TYPE_LINK_MIC = 4;
    public static final int ACTIVITY_TYPE_REALTIME_PLAY = 5;
    public ImageView tvQuickMsg;
    protected final List<ZhuboDto> data = new ArrayList<>();
    protected int groupId;//群id
    public int channelId;//主播id(观众端 正在观看的主播)
    protected int roomManager;//0：不是房管,1:是房管 2:主播
    protected String nickname;
    protected int mActivityType;
    protected int mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;

    public TXLivePlayer mLivePlayer = null;
    private TXLivePlayer pkLivePlayer = null;
    //    private Surface mSurface;
    private boolean mHWDecode = false;
    private int mCurrentRenderMode;
    private int mCurrentRenderRotation;

    private long mStartPlayTS = 0;

    protected boolean isPush = false;//是不是主播端 默认false

    private ITXLivePlayListener mainListener, pkMineListener, pkOtherListener;
    protected String pkMineUrl, pkOhterUrl;





    protected LinearLayout ll_view_new;
    protected ImageView im_sd_lw_new;
    protected TextSwitcher tv_sd_lw_new;


    protected GiftRootLayout giftRoot;
    protected RelativeLayout rlGiftAnim;
    protected KsgLikeView qipaoView;

    //进入直播间 飘屏/////////////
    protected RelativeLayout llEnter;

    protected GradientTextView tvBayMsg;
    protected ImageView ivEnterUserLevel;
    protected ImageView ivEnterVipLevel;
    protected TextView tvEnterMsg;
    protected ImageView ivEnterGuard;
    protected ImageView ivEnterRoommanager;


    protected RelativeLayout rlEnterBack;
    protected ImageView imEnterPic;

    //活动进入飘屏
    protected LinearLayout llEnterActivity;
    protected ImageView ivAngel;
    protected TextView tvAngel;
    protected ImageView ivLevel;

    protected LinearLayout llMedalEnter;//活动勋章入场飘屏
    protected TextView btnAttention;   //主播关注
    ///活动banner
    protected ActBannerView<ActBannerBean> actBanner;
    ActivityPopwindow activityPopwindow;
    protected CountBackUtils activityCb;
    //////////////////////
    //红包雨view
    protected FallingLayout fallingLayout;
    //BuyGuardPiaoping
    protected View viewBuyGuard;
    protected ImageView ivBuyGuard;
    protected TextView tvBuyGuardUserName;
    protected TextView tvBuyGuardName;
    protected EditText etInput;
    //弹幕
    DanmakuContext mDmkContext;
    protected DanmakuView danmakuView;
    BaseDanmakuParser mParser;
    View decorView;
    ViewTreeObserver.OnGlobalLayoutListener listener;
    public LivingMsgAdapter livingMsgAdapter; //左下角 聊天 适配器
    private List<LivingRoomTextMsg> chatList;
    protected GiftPopWindow giftPopWindow;//礼物弹窗
    public String cpRank = "";//cp标签


    protected RelativeLayout fl_back;
    //------------------pk-----------------------
    protected List<PkTeamInfo> weListData = new ArrayList<>();//pk我方集合
    protected List<PkTeamInfo> otherListData = new ArrayList<>();//pk对方
    protected StartPkAnimAdapter weAdapter;//pk开始动画我方适配器
    protected StartPkAnimAdapter otherAdapter;//pk开始动画对方适配器
    protected MatchPkResultUtil matchPkResultUtil = new MatchPkResultUtil(); //开始pk回调数据
    //开始pk 动画
    @BindView(R2.id.layout_pk_start)
    protected View layoutPkStartAnim;
    @BindView(R2.id.rv_start_pk_anim_left)
    protected RecyclerView rvStartPkAnimLeft;
    @BindView(R2.id.rv_start_pk_anim_right)
    protected RecyclerView rvStartPkAnimRight;
    @BindView(R2.id.iv_start_pk_anim_vs)
    protected ImageView ivStartPkAnimVS;
    @BindView(R2.id.iv_start_pk_anim_light)
    protected ImageView ivStartPkAnimLight;
    public NewUserInfoPopwindow newUserInfoPopwindow;
    //去围观


    public TextView tv_djs;
    public View viewAnnoucement;
    public AutoScrollTextView tvAnnouncement;
    public ImageView im_announcement;
    public View noticeAnnoucement;
    public TextView tvNoticeAnnouncement;
    private Queue<GiftBoardcastDto> announcementList = new ArrayDeque<>();
    private ConcurrentLinkedQueue<FloatScreenBean> someoneComingList = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<FloatScreenBean> enTerList = new ConcurrentLinkedQueue<>();
    private Queue<LivingRoomGuardMsg> buyGuardList = new ArrayDeque<>();

    //头条
    protected TextView toutiaoLefttimeTv;
    protected LinearLayout toutiaoLefttimeView;
    protected ImageView toutiaoUseravatar;
    protected ImageView toutiaoGift2;
    protected RelativeLayout rlToutiaoView;
    protected ImageView im_cp;
    protected TextView toutiaoGiftNum2;
    protected LinearLayout toutiaoLeftviewShort;
    protected ImageView toutiaoGift;
    protected TextView toutiaoGiftNum;
    protected LinearLayout toutiaoGiftview;
    protected TextView toutiaoContent;
    protected LinearLayout toutiaoLeftviewLong;
    protected ImageView toutiao_im_tt;
    protected View viewToutiao;
    protected TextView mTvGoWatch;  //去围观
    private boolean isShowAnimate = true;//是否显示动画
    protected volatile boolean isShowPkResulting = false;//是否正在显示pk结果

    private boolean isBigGiftStop = true;
    private boolean isShowPlane = false;//是否正在播放红包雨之前的飞机动画，播放完之后下红包雨
    protected CompositeDisposable allRxbus;
    private SafeHandle giftHandle;
    public View pendantLayout;

    public TextView tvAnnouncementGo;   //送金额大的礼物会发公告 去围观按钮区别显示
    private ScheduledExecutorService endPkScheduleService;//查询是否需要结束pk的轮询
    public BottomSheetDialog bottomSheetDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        isShowAnimate = (boolean) SPUtils1.get(this, ISANIMATION, true);
//TODO 测试代码
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                addPlaneMp4ToQueue();
//            }
//        }, 4000);
    }


    @Override
    protected void initView() {
        allRxbus = new CompositeDisposable();
        startGiftThead();
        mCurrentRenderMode = TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;
        mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;
        mActivityType = 2;
        decorView = getWindow().getDecorView();
        listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initMsgRv();
                decorView.getViewTreeObserver().removeOnGlobalLayoutListener(listener);

                listener = null;
            }
        };
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        initPKAnimView();
        allRxbus.add(RxBus.get().toObservable(ChangeAnimateEvent.class)
                .compose(bindToLifecycle())
                .subscribe(new Consumer<ChangeAnimateEvent>() {
                    @Override
                    public void accept(ChangeAnimateEvent changeAnimateEvent) throws Exception {
                        isShowAnimate = changeAnimateEvent.isAnimate();
                        SPUtils1.put(BaseLiveingRoomActivity.this, ISANIMATION, isShowAnimate);
                        runOnUiThread(new MRunnable() {
                            @Override
                            public void run() {
                                if (silkyAnimation != null) {
                                    silkyAnimation.stop();
                                }
                                bigGiftList.clear();
                                myGiftList.clear();
                            }
                        });
                        if (isShowAnimate) {
                        } else {
                        }
                    }
                }));
        //主播生成心愿单后刷新数据
        allRxbus.add(RxBus.get().toObservable(RefrshWishEvent.class)
                .compose(bindToLifecycle())
                .subscribe(new io.reactivex.functions.Consumer<RefrshWishEvent>() {
                    @Override
                    public void accept(RefrshWishEvent refrshWishEvent) throws Exception {
                        L.e("========生成心愿成功 刷新数据");
                        p.getActivityMsg(channelId);
                    }
                }));
        mainListener = new ITXLivePlayListener() {
            @Override
            public void onPlayEvent(int event, Bundle param) {
                String playEventLog = "score event: " + event + ", " + param.getString(TXLiveConstants.EVT_DESCRIPTION);
                if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                    onPlayBegin();
                    L.d("AutoMonitor", "PlayFirstRender,cost=" + (System.currentTimeMillis() - mStartPlayTS));
                } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_EVT_PLAY_END) {
                    L.d("txplayevent", "播放断开连接+PLAY_ERR_NET_DISCONNECT");
                    onPlayEnd();
                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
                    L.d("txplayevent", "播放器加载PLAY_EVT_PLAY_LOADING");
                } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
                    L.d("txplayevent", "播放第一针+PLAY_EVT_RCV_FIRST_I_FRAME");
                } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
                    L.d("txplayevent", "播放改变分辨率+PLAY_EVT_CHANGE_RESOLUTION");
                } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_ROTATION) {
                    L.d("txplayevent", "播放“更改”旋转+PLAY_EVT_CHANGE_ROTATION");
                    return;
                }
            }

            @Override
            public void onNetStatus(Bundle bundle) {

            }
        };
        pkMineListener = new ITXLivePlayListener() {
            @Override
            public void onPlayEvent(int event, Bundle param) {
                String playEventLog = "score event: " + event + ", " + param.getString(TXLiveConstants.EVT_DESCRIPTION);
                if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                    onPkMinePlayBegin();
                    L.d("AutoMonitor", "mine PlayFirstRender,cost=" + (System.currentTimeMillis() - mStartPlayTS));
                } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_EVT_PLAY_END) {
                    L.d("txplayevent", "播放断开连接+mine PLAY_ERR_NET_DISCONNECT");
                    onPkMinePlayEnd();
                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
                    L.d("txplayevent", "播放器加载PLAY_EVT_PLAY_LOADING");
                } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
                    L.d("txplayevent", "播放第一针+PLAY_EVT_RCV_FIRST_I_FRAME");
                } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
                    L.d("txplayevent", "播放改变分辨率+PLAY_EVT_CHANGE_RESOLUTION");
                } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_ROTATION) {
                    L.d("txplayevent", "播放“更改”旋转+PLAY_EVT_CHANGE_ROTATION");
                    return;
                }
            }

            @Override
            public void onNetStatus(Bundle bundle) {

            }
        };
        pkOtherListener = new ITXLivePlayListener() {
            @Override
            public void onPlayEvent(int event, Bundle param) {
                String playEventLog = "score event: " + event + ", " + param.getString(TXLiveConstants.EVT_DESCRIPTION);
                if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                    onPkOtherPlayBegin();
                    L.d("txplayevent", "others PlayFirstRender,cost=" + (System.currentTimeMillis() - mStartPlayTS));
                } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_EVT_PLAY_END) {
                    onPkOtherPlayEnd();
                    L.d("txplayevent", "断开+others PLAY_ERR_NET_DISCONNECT");
                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
                    L.d("txplayevent", "播放器加载PLAY_EVT_PLAY_LOADING");
                } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
                    L.d("txplayevent", "播放第一针+PLAY_EVT_RCV_FIRST_I_FRAME");
                } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
                    L.d("txplayevent", "播放改变分辨率+PLAY_EVT_CHANGE_RESOLUTION");
                } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_ROTATION) {
                    L.d("txplayevent", "播放“更改”旋转+PLAY_EVT_CHANGE_ROTATION");
                    return;
                }
            }

            @Override
            public void onNetStatus(Bundle bundle) {

            }
        };


    }

    public void initExoPlay() {
        if (silkyAnimation == null) {
            frameAnimationView.post(new Runnable() {
                @Override
                public void run() {
                    silkyAnimation = ExoPlayerFactory.newSimpleInstance(getApplicationContext());
                    silkyAnimation.addListener(bigGiftListener);
                    frameAnimationView.setSimpleExoPlayer(silkyAnimation);
                    frameAnimationView.setGlFilter(new AlphaFrameFilter());
                }
            });

        }

    }

    public long djsTime = 0;
    public String noticeMsg = "";
    public long calculationTime = 0;
    public long noticeTime;

    /**
     * 各种礼物动画循环回调的handle
     */
    protected void startGiftThead() {
        if (null == giftHandle) {
            giftHandle = new SafeHandle(this) {
                @Override
                protected void mHandleMessage(Message msg) {
                    super.mHandleMessage(msg);
                    if (msg.what == 1) {
                        if (null != frameAnimationView && frameAnimationView.getVisibility() != View.VISIBLE && isBigGiftStop) {
                            try {
                                showBigGift();//礼物动画
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        djsTime++;
                        if (null != tv_djs) {
                            tv_djs.setText(TimeUtils.l2String(djsTime));
                        }

                        if (!noticeMsg.equals("")) {
                            calculationTime++;
                            noticeTime++;
                            if (calculationTime >= 10) {
                                //隐藏挂件

                            }
                            if (noticeTime >= 600) {
                                noticeTime = 0;
                                calculationTime = 0;
                                getAddNotice(noticeMsg, 0, "", "");
                                if (null != p) {
                                    p.getNoticeInfo();
                                }

                            }
                        }

                        giftHandle.sendEmptyMessageDelayed(1, 1000);
                    }
                }
            };
        }
        if (giftHandle.hasMessages(1)) return;
        giftHandle.sendEmptyMessage(1);
    }


    protected void stopGiftThead() {
        giftHandle.removeMessages(1);
//        if (singleThreadExecutor == null) {
//            return;
//        }
//        try {
//            singleThreadExecutor.shutdown();
//            singleThreadExecutor.shutdownNow();
//        } catch (Exception e) {
//
//        }
//        singleThreadExecutor = null;
    }

    protected void initPKAnimView() {
        rvStartPkAnimLeft.setLayoutManager(new LinearLayoutManager(this));
        rvStartPkAnimRight.setLayoutManager(new LinearLayoutManager(this));
        weAdapter = new StartPkAnimAdapter(weListData, true);
        rvStartPkAnimLeft.setAdapter(weAdapter);
        otherAdapter = new StartPkAnimAdapter(otherListData, false);
        rvStartPkAnimRight.setAdapter(otherAdapter);

    }

    //更新pk数据
    protected void resetPkAnimView() {
        weListData.clear();//我方pk数据
        otherListData.clear();
        weListData.addAll(matchPkResultUtil.getUserTeamInfo());
        otherListData.addAll(matchPkResultUtil.getOtherTeamInfo());
        weAdapter.notifyDataSetChanged();//pk排行榜 数据
        otherAdapter.notifyDataSetChanged();
        if (weListData.size() > 1) {
            rvStartPkAnimLeft.setBackgroundResource(R.drawable.bg_pk_team_we);
            rvStartPkAnimRight.setBackgroundResource(R.drawable.bg_pk_team_other);
        } else {
            rvStartPkAnimLeft.setBackgroundResource(R.drawable.bg_pk_single_we);
            rvStartPkAnimRight.setBackgroundResource(R.drawable.bg_single_pk_other);
        }
        statPkAnim();


    }

    AnimatorSet animatorSet;

    private void clearPkAnim() {
        canclePkAnim();
        rvStartPkAnimLeft.setVisibility(View.GONE);
        rvStartPkAnimRight.setVisibility(View.GONE);
        ivStartPkAnimVS.setVisibility(View.GONE);
        ivStartPkAnimLight.setVisibility(View.GONE);
        //  actBanner.setAlpha(0);
        //  KLog.e("tagpk", "clearPkAnim");
        //     actBanner.setAlpha(0);
//        rvStartPkAnimLeft.setTranslationX(0);
//        rvStartPkAnimRight.setTranslationX(0);
//        ivStartPkAnimVS.setScaleX(0);
//        ivStartPkAnimVS.setScaleY(0);
//        ivStartPkAnimLight.setScaleY(0);
//        ivStartPkAnimLight.setScaleX(0);
//        ivStartPkAnimVS.setAlpha(0f);
//        ivStartPkAnimLight.setAlpha(0f);
//        rvStartPkAnimLeft.setAlpha(0f);
//        rvStartPkAnimRight.setAlpha(0f);
    }

    /**
     * 开始pk动画  写备注是因为遇到滑动不播放的bug  尝试修改的准备
     * 这里大致pk是 播放一个源生动画 没啥可看的
     */
    private void statPkAnim() {
        clearPkAnim();//关闭礼物动画
        rvStartPkAnimLeft.setVisibility(View.VISIBLE);//pk两侧的列表
        rvStartPkAnimRight.setVisibility(View.VISIBLE);
        ivStartPkAnimVS.setVisibility(View.VISIBLE);//pk中的 vs 图片
        ivStartPkAnimLight.setVisibility(View.VISIBLE);//vp开始中间拿到闪电
        if (null == animatorSet) {
            animatorSet = new AnimatorSet();
            ObjectAnimator animatorWe = new ObjectAnimator().ofFloat(rvStartPkAnimLeft, "translationX", -500, rvStartPkAnimLeft.getTranslationX());
            ObjectAnimator animatorOther = new ObjectAnimator().ofFloat(rvStartPkAnimRight, "translationX", rvStartPkAnimRight.getTranslationX() + 500, rvStartPkAnimRight.getTranslationX());
            animatorOther.setDuration(100);
            animatorWe.setDuration(100);
            ObjectAnimator scaleVsX = new ObjectAnimator().ofFloat(ivStartPkAnimVS, "scaleX", 0, 1f);
            ObjectAnimator scaleVsY = new ObjectAnimator().ofFloat(ivStartPkAnimVS, "scaleY", 0, 1f);
            ObjectAnimator scaleLightX = new ObjectAnimator().ofFloat(ivStartPkAnimLight, "scaleX", 0, 1f);
            ObjectAnimator scaleLightY = new ObjectAnimator().ofFloat(ivStartPkAnimLight, "scaleY", 0, 1f);
            ObjectAnimator alphaVs = ObjectAnimator.ofFloat(ivStartPkAnimVS, "alpha", 0f, 1.5f, 1f);
            ObjectAnimator alphaLight = ObjectAnimator.ofFloat(ivStartPkAnimLight, "alpha", 0f, 1.5f, 1f);
            ObjectAnimator alphaLeft = ObjectAnimator.ofFloat(rvStartPkAnimLeft, "alpha", 0f, 1.5f, 1f);
            alphaLeft.setDuration(50);
            ObjectAnimator alphaRight = ObjectAnimator.ofFloat(rvStartPkAnimRight, "alpha", 0f, 1.5f, 1f);
            alphaRight.setDuration(50);
            animatorSet.playTogether(animatorWe, animatorOther, scaleVsX, scaleVsY, scaleLightX, scaleLightY, alphaVs, alphaLight, alphaLeft, alphaRight);
            animatorSet.setDuration(300);
            animatorSet.setStartDelay(500);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (null == startPkCb) {
                        startPkCb = new CountBackUtils();
                    }
                    startPkCb.countBack(2, new CountBackUtils.Callback() {
                        @Override
                        public void countBacking(long time) {

                        }

                        @Override
                        public void finish() {
                            rvStartPkAnimLeft.setAlpha(0);
                            rvStartPkAnimRight.setAlpha(0);
                            layoutPkStartAnim.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    layoutPkStartAnim.setVisibility(View.VISIBLE);
                }
            });
        }
        animatorSet.start();
    }

    private void canclePkAnim() {
        try {
            rvStartPkAnimRight.clearAnimation();//隐藏pkview
            rvStartPkAnimLeft.clearAnimation();
            ivStartPkAnimVS.clearAnimation(); // vs图片取消动画
            ivStartPkAnimLight.clearAnimation();
            if (animatorSet != null) animatorSet.cancel();//pk动画
            if (startPkCb != null) startPkCb.destory();
            startPkCb = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public LivingRoomPresenter initPresenter() {
        return new LivingRoomPresenter();
    }

    protected int getIMGroupId(int channelId) {
        KLog.e("tag", channelId + "");
        return GROUP_BASE + channelId;
    }

    protected boolean checkPlayUrl(final String playUrl) {
        if (TextUtils.isEmpty(playUrl) || (!playUrl.startsWith("http://") && !playUrl.startsWith("https://") && !playUrl.startsWith("rtmp://") && !playUrl.startsWith("/"))) {
//            Toast.makeText(getApplicationContext(), "播放地址不合法，直播目前仅支持rtmp,flv播放方式!", Toast.LENGTH_SHORT).show();
            return false;
        }
        switch (mActivityType) {
            case ACTIVITY_TYPE_LIVE_PLAY: {
                if (playUrl.startsWith("rtmp://")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
                } else if ((playUrl.startsWith("http://") || playUrl.startsWith("https://")) && playUrl.contains(".flv")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;
                } else {
//                    Toast.makeText(getApplicationContext(), "播放地址不合法，直播目前仅支持rtmp,flv播放方式!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            break;
            case ACTIVITY_TYPE_REALTIME_PLAY:
                mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP_ACC;
                break;
            default:
//                Toast.makeText(getApplicationContext(), "播放地址不合法，目前仅支持rtmp,flv播放方式!",Toast.LENGTH_SHORT).show();
                return false;
        }
        return true;
    }

    public int startOtherPlayer(TXCloudVideoView videoView, String url) {
        if (!checkPlayUrl(url)) {
            return -1;
        }
        pkLivePlayer = new TXLivePlayer(this);
        pkLivePlayer.setPlayListener(pkOtherListener);
        pkLivePlayer.setRenderRotation(mCurrentRenderRotation);//设置渲染角度
        pkLivePlayer.setRenderMode(mCurrentRenderMode);//图像平铺
        pkLivePlayer.enableHardwareDecode(mHWDecode);//启用或禁用视频硬解码.
        pkLivePlayer.setPlayerView(videoView);
        return pkLivePlayer.startPlay(url, mPlayType);
    }

    public int startMinePlayer(TXCloudVideoView videoView, String url) {
        if (!checkPlayUrl(url)) {
            return -1;
        }
        mLivePlayer = new TXLivePlayer(this);
        mLivePlayer.setPlayListener(pkMineListener);
        mLivePlayer.setRenderRotation(mCurrentRenderRotation);
        mLivePlayer.setRenderMode(mCurrentRenderMode);
        mLivePlayer.enableHardwareDecode(mHWDecode);
        mLivePlayer.setPlayerView(videoView);
        KLog.e("tag", "url " + url);
        return mLivePlayer.startPlay(url, mPlayType);
    }

    public boolean startPlay(String url, TXCloudVideoView txCloudVideoView) {
        stopPlay();

//        if (null != mSurface) {
//            mSurface.release();
//            mSurface = null;
//        }
        if (!checkPlayUrl(url)) {
            return false;
        }
        mLivePlayer = new TXLivePlayer(this);
        setLivePlayConfig(mLivePlayer);

        mLivePlayer.setPlayListener(mainListener);
        // 硬件加速在1080p解码场景下效果显著，但细节之处并不如想象的那么美好：
        // (1) 只有 4.3 以上android系统才支持
        // (2) 兼容性我们目前还仅过了小米华为等常见机型，故这里的返回值您先不要太当真
        mLivePlayer.enableHardwareDecode(mHWDecode);
        mLivePlayer.setRenderRotation(mCurrentRenderRotation);

        mLivePlayer.setRenderMode(mCurrentRenderMode);
        //设置播放器缓存策略
        //这里将播放器的策略设置为自动调整，调整的范围设定为1到4s，您也可以通过setCacheTime将播放器策略设置为采用
        //固定缓存时间。如果您什么都不调用，播放器将采用默认的策略（默认策略为自动调整，调整范围为1到4s）

        mLivePlayer.setPlayerView(txCloudVideoView);

        int result = mLivePlayer.startPlay(url, mPlayType); // result返回值：0 code;  -1 empty url; -2 invalid url; -3 invalid playType;
        mStartPlayTS = System.currentTimeMillis();
        if (result != 0) {
            return false;
        }
        if (null != actBanner) {
            actBanner.post(new Runnable() {
                @Override
                public void run() {
                    actBanner.setVisibilitySVGA(true);
                }
            });

        }
        return true;
    }

    /**
     * 设置播放器缓存
     *
     * @param mLivePlayer
     */
    private void setLivePlayConfig(TXLivePlayer mLivePlayer) {
        TXLivePlayConfig mLivePlayConfig = new TXLivePlayConfig();
        mLivePlayConfig.setAutoAdjustCacheTime(true);

        mLivePlayConfig.setMaxAutoAdjustCacheTime(5);
        mLivePlayConfig.setMinAutoAdjustCacheTime(5);
        mLivePlayer.setConfig(mLivePlayConfig);
    }

    public void startPkPlay(String mineUrl, String otherUrl, TXCloudVideoView mineVedioview, TXCloudVideoView otherVedioview) {
        stopPlay();
        pkMineUrl = mineUrl;
        pkOhterUrl = otherUrl;


        startMinePlayer(mineVedioview, mineUrl);
        startOtherPlayer(otherVedioview, otherUrl);
    }


    protected void stopPlay() {
        if (mLivePlayer != null) {
            mLivePlayer.stopRecord();
            mLivePlayer.setPlayListener(null);
            mLivePlayer.stopPlay(true);
            mLivePlayer = null;
        }
        stopOther();
    }

    protected void stopOther() {
        if (pkLivePlayer != null) {
            pkLivePlayer.stopRecord();
            pkLivePlayer.setPlayListener(null);
            pkLivePlayer.stopPlay(true);
            pkLivePlayer = null;
        }
        KLog.e("tagpk", "开始pk  停止大屏功能");
        if (null != actBanner) {
            actBanner.post(new Runnable() {
                @Override
                public void run() {
                    actBanner.setVisibilitySVGA(true);
                }
            });
        }

        //  actBanner.setAlpha(1);

    }

    @Override
    protected void onDestroy() {
        isCf = false;
        if (null != mDmkContext) {
            mDmkContext.unregisterAllConfigChangedCallbacks();
        }
        if (null != mParser) {
            mParser.release();
        }
        if (null != danmakuView) {
            danmakuView.hide();
            danmakuView.stop();
            danmakuView.clearDanmakusOnScreen();
            danmakuView.removeAllLiveDanmakus();
            danmakuView.release();
        }


        WishCacheMr.getInstance().cleanCache();
        changeRoomClearAllRes();
        stopPlay();
        stopGiftThead();
        destoryDanmuView();
        stopCheckPkEndSchedule();
        if (null != utils)
            utils.destory();
        if (null != somecomingCb) {
            somecomingCb.destory();
            somecomingCb = null;
        }
        if (null != somecomingAct) {
            somecomingAct.destory();
            somecomingAct = null;
        }
        if (null != buyGuardCb) {
            buyGuardCb.destory();
            buyGuardCb = null;
        }
        if (null != annotationCb) {
            annotationCb.destory();
            annotationCb = null;
        }
        if (null != toutiaoCountback) {
            toutiaoCountback.destory();
            toutiaoCountback = null;
        }
        if (null != startPkCb) {
            startPkCb.destory();
            startPkCb = null;
        }
        if (silkyAnimation != null) {
            silkyAnimation.removeListener(bigGiftListener);
            bigGiftListener = null;
            silkyAnimation.stop();
            silkyAnimation.release();
            silkyAnimation = null;
            frameAnimationView.onPause();
            frameAnimationView = null;

        }

        if (null != svga_pk) {
            svga_pk.stopAnimation();

        }

        if (null != svga_animation) {
            svga_animation.stopAnimation();
        }



        if (null != startPkCb) {
            startPkCb.destory();
        }
        if (livingMsgAdapter != null) {
            livingMsgAdapter.setOnNameClickListener(null);
            livingMsgAdapter = null;
        }
        mainListener = null;
        pkOtherListener = null;
        pkMineListener = null;

        if (annoucementAnimalSet != null) {
            annoucementAnimalSet.cancel();
            annoucementAnimalSet.cancel();
        }
        super.onDestroy();
    }

    private void destoryDanmuView() {
        if (danmakuView != null) {
//            try {
//                Field handler = danmakuView.getClass().getDeclaredField("handler");
//                handler.setAccessible(true);
//                DrawHandler hand = (DrawHandler) handler.get(danmakuView);
//                Field danmakuViewField = hand.getClass().getDeclaredField("mDanmakuView");
//                danmakuViewField.setAccessible(true);
//                danmakuViewField.set(hand,null);
//                handler.set(danmakuView, null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            danmakuView.release();
            danmakuView = null;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        isActivityResume = false;
        stopGiftThead();
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }
        if (null != mLivePlayer)
            mLivePlayer.pause();
        if (null != pkLivePlayer) {
            pkLivePlayer.pause();
        }
//        if (null != fallingLayout) {
//            fallingLayout.clean();
//        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        startGiftThead();
        isActivityResume = true;
        if (danmakuView != null && danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }
        if (null != mLivePlayer)
            mLivePlayer.resume();
        if (null != pkLivePlayer) {
            pkLivePlayer.resume();
        }
    }

    @Override
    public void attentionEachSuccess(int state, int switchStatus, boolean istype) { //点击私信  获取互相关注状态
//        0：未关注 1:已关注 2：已互相关注
        L.e("state " + state);
        this.state = state;
        if (state == 0) {
            btnAttention.setVisibility(View.VISIBLE);
        } else {
            btnAttention.setVisibility(View.GONE);
        }
    }

    protected void onPlayEnd() {

    }

    public void onPlayBegin() {
    }

    public void onPkMinePlayBegin() {
    }

    public void onPkOtherPlayBegin() {
    }

    public void onPkMinePlayEnd() {
    }

    public void onPkOtherPlayEnd() {
    }

    @Override
    public void setRoomManager(int roomManager) {
        this.roomManager = roomManager;
    }

    public void setZhuboInfo(EnterLivingRoomDto data) {
        channelId = data.channelId;
        roomManager = data.roomManage;
        nickname = data.nickname;
        shouhuListPopwindow = new ShouhuListPopwindow(this, false, data.channelId);
        if (isPush) {

        } else {
            if (data.isAttention > 0) {
            } else {

            }
        }
        //获取互相关注状态 用于判断是否可以私信
        if (data != null) p.getAttentionEach(channelId, false);
    }


    public void setRecharge(List<RechargeDto> data) {
    }

    /**
     * 为后人造福
     * 大哥、房管、座驾 （新增活动）进入直播间 im消息上方的飘屏
     */
    public void someoneComing(FloatScreenBean floatScreenBean) {
        if (floatScreenBean.watcherDto != null) {//暂时只看到非pk
            WatcherDto watcherDto = floatScreenBean.watcherDto;
            //跟礼物动画有关
            if (!TextUtils.isEmpty(watcherDto.carSwf) || !TextUtils.isEmpty(watcherDto.guardSwf)) {//有座驾播放座驾 守护进场座驾
                if (!TextUtils.isEmpty(watcherDto.guardSwf)) {//守护进场座驾
                    int index = watcherDto.guardSwf.lastIndexOf("/");
                    int length = watcherDto.guardSwf.length();
                    String name = watcherDto.guardSwf.substring(index + 1, length);
                    GiftBean b = new GiftBean();
                    b.bigName = name.split("\\.")[0];
                    b.isCar = true;
                    b.frameRate = watcherDto.guarFrameRate;
                    if(watcherDto.carSwf.equals("")){
                        b.swf = watcherDto.guardSwf;
                    }else {
                        b.swf = watcherDto.carSwf;
                    }

                    b.carDes = watcherDto.nickname + "乘坐着" + watcherDto.guarName + "来到直播间";
                    bigGiftList.offer(b);
                }
                if (!TextUtils.isEmpty(watcherDto.carSwf) && floatScreenBean.activityDto == null) {//座驾
                    int index = watcherDto.carSwf.lastIndexOf("/");
                    int length = watcherDto.carSwf.length();
                    String name = watcherDto.carSwf.substring(index + 1, length);
                    //普通座驾
                    GiftBean b = new GiftBean();
                    b.bigName = name.split("\\.")[0];
                    b.isCar = true;
                    b.frameRate = watcherDto.carFrameRate;
                    if(watcherDto.carSwf.equals("")){
                        b.swf = watcherDto.guardSwf;
                    }else {
                        b.swf = watcherDto.carSwf;
                    }
                    b.carDes = watcherDto.nickname + "乘坐着" + watcherDto.carName + "来到直播间";
                    bigGiftList.offer(b);
                }

            }
            if (!TextUtils.isEmpty(watcherDto.specialEffectUrl)) { //七夕活动大哥进入直播间
                GiftBean bean = new GiftBean();
                bean.swf = watcherDto.specialEffectUrl;
                bean.bigName = GiftDto.getSwfName(watcherDto.specialEffectUrl);
                bean.isPlay = true;
                bean.userid = String.valueOf(getUserInfo().getId());
                bean.isToutiao = true;
                showGiftAnimation(bean);
            }
            //跟守护有关（字面理解）  如果是守护优先守护 然后根据座驾？
            if (watcherDto.isActivityGuardType > 0 || watcherDto.isShareTalent > 0 || watcherDto.isCombatCheer > 0) {
                if (watcherDto.isActivityGuardType > 0) {
                    WatcherDto clone = (WatcherDto) watcherDto.clone();
                    someoneComingList.offer(new FloatScreenBean(clone, null, null));
                    watcherDto.isActivityGuardType = 0;
                }
                if (watcherDto.isShareTalent == 1) {
                    someoneComingList.offer(floatScreenBean);
                }
                if (watcherDto.isCombatCheer == 1) {
                    someoneComingList.offer(floatScreenBean);
                }
                if (someoneComingList.size() > 0)
                    activitySomeoneComing();
            } else if (watcherDto.userLevel >= 10 || watcherDto.userVipLevel > 0 || !TextUtils.isEmpty(watcherDto.guardSwf) || !TextUtils.isEmpty(watcherDto.carSwf)) {
                enTerList.offer(new FloatScreenBean(watcherDto, null, null));
                setEnterData();
            }

        } else if (floatScreenBean.pkFloatingScreenBean != null) {//pk相关
            someoneComingList.offer(floatScreenBean);
            activitySomeoneComing();
        } else if (floatScreenBean.activityDto != null) {//最豪气
            ActivityDto activityDto = floatScreenBean.activityDto;
            enTerList.offer(new FloatScreenBean(null, null, activityDto));
            setEnterData();
        }
    }

    boolean isActivitySomeOneComing = false;//保证同时进来多个 排队播放
    boolean isActivityOne = false;

    @SuppressLint("SetTextI18n")
    private synchronized void activitySomeoneComing() {
        if (somecomingAct != null && somecomingAct.isTiming() || isActivitySomeOneComing) {
            return;
        }
        if (someoneComingList == null || someoneComingList.size() <= 0)
            return;
        FloatScreenBean floatScreenBean = someoneComingList.poll();
        KLog.e("somethingCome===", someoneComingList.size() + "");
        isActivitySomeOneComing = true;
//        assert floatScreenBean != null;
        WatcherDto watcherDto = floatScreenBean.watcherDto;
        PkFloatingScreenBean pkFloatingScreenBean = floatScreenBean.pkFloatingScreenBean;
        if (watcherDto != null) {
            if (watcherDto.isActivityGuardType >= 12 && watcherDto.isActivityGuardType <= 15) { //主播勋章进入其他人直播间不显示
                return;
            }
            llEnterActivity.setVisibility(View.VISIBLE);
            ivAngel.setVisibility(View.VISIBLE);
            ivLevel.setVisibility(View.GONE);
            if (watcherDto.isActivityGuardType > 0) {//todo 守护活动
                if (watcherDto.isActivityGuardType < 9) { //告白天使活动守护
                    ivAngel.setImageResource(ImageLoader.getResId("ic_angel_" + (watcherDto.isActivityGuardType - 1), R.drawable.class));
                } else if (watcherDto.isActivityGuardType < 16) { //唤醒蜂后 勋章
                    ivAngel.setImageResource(ImageLoader.getResId("ic_medal_" + watcherDto.isActivityGuardType, R.drawable.class));
                } else {//唤醒蜂后 守护
                    ivAngel.setImageResource(ImageLoader.getResId("ic_guard_" + watcherDto.isActivityGuardType, R.drawable.class));
                }
                tvAngel.setText("欢迎" + getNameByActivityGuardType(watcherDto.isActivityGuardType) + watcherDto.nickname + " 登场");
                llEnterActivity.setBackgroundColor(getBgColorByActivityGuardType(watcherDto.isActivityGuardType));
            } else {
                //todo 助力达人 分享达人
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius(getResources().getDimension(R.dimen.dp_2));
                if (watcherDto.isCombatCheer == 1) {
                    ivAngel.setImageResource(R.drawable.ic_cheer_talent);
                    gradientDrawable.setColor(Color.parseColor("#4DFCEC97"));
                    llEnterActivity.setBackground(gradientDrawable);
                }
                if (watcherDto.isShareTalent == 1) {
                    ivAngel.setImageResource(R.drawable.ic_share_talent);
                    gradientDrawable.setColor(Color.parseColor("#4DFF89AC"));
                    llEnterActivity.setBackground(gradientDrawable);
                }
                if (!TextUtils.isEmpty(watcherDto.carName))
                    tvAngel.setText(watcherDto.nickname + "驾着 「" + watcherDto.carName + "」 " + "來了");
                else
                    tvAngel.setText(watcherDto.nickname + "來了");
            }
        } else if (pkFloatingScreenBean != null) {
            llEnterActivity.setVisibility(View.VISIBLE);
            ivAngel.setVisibility(View.GONE);
            //巅峰排位赛飘屏
            llEnterActivity.setBackgroundResource(R.drawable.ic_pk_float_screen_bg);
            tvAngel.setText(pkFloatingScreenBean.content);
            if (!TextUtils.isEmpty(pkFloatingScreenBean.levelIcon)) {
                ImageLoader.loadImg(ivLevel, pkFloatingScreenBean.levelIcon);
                ivLevel.setVisibility(View.VISIBLE);
            } else {
                ivLevel.setVisibility(View.GONE);
            }
        }
        startActivityEnterAnimation();
    }

    private String getNameByActivityGuardType(int activityGuardType) {
        switch (activityGuardType) {
            case 1:
                return "告白天使实力守护 ";
            case 2:
                return "魅力天使实力守护 ";
            case 3:
                return "活力天使实力守护 ";
            case 4:
                return "宠爱天使实力守护 ";
            case 9:
                return "唤醒蜂后“最皮工蜂” ";
            case 10:
                return "唤醒蜂后“捣蛋工蜂”";
            case 11:
                return "唤醒蜂后“搞怪工蜂” ";
            case 16:
                return "蜂国蜂后实力守护 ";
            case 17:
                return "蜂国储君实力守护 ";
            case 18:
                return "蜂国郡主实力守护";
            case 19:
                return "蜂国精灵实力守护";
        }
        return " ";
    }

    private int getBgColorByActivityGuardType(int activityGuardType) {
        switch (activityGuardType) {
            case 1:
                return Color.parseColor("#80D073FF");
            case 2:
                return Color.parseColor("#808852F2");
            case 3:
                return Color.parseColor("#80F2B452");
            case 4:
                return Color.parseColor("#80FF6666");
            case 9:
                return Color.parseColor("#FF4E4E");
            case 10:
                return Color.parseColor("#AF4EFF");
            case 11:
                return Color.parseColor("#4F98FF");
            case 16:
                return Color.parseColor("#AF4EFF");
            case 17:
                return Color.parseColor("#FF4F4F");
            case 18:
                return Color.parseColor("#4F5DFF");
            case 19:
                return Color.parseColor("#37C78C");
        }
        return Color.TRANSPARENT;
    }

    private void addActivityMsg(FloatScreenBean floatScreenBean) {
        tvAngel.setVisibility(View.GONE);
        llEnter.setVisibility(View.VISIBLE);
        ActivityDto activityDto = floatScreenBean.activityDto;
        imEnterPic.setVisibility(View.VISIBLE);
        ImageLoader.loadImg(imEnterPic, activityDto.getImg());
        if (Integer.parseInt(activityDto.getLevel()) > 0) {   //用户等级
            ivEnterUserLevel.setVisibility(View.VISIBLE);
            ivEnterUserLevel.setImageResource(ImageLoader.getUserLevel(Integer.parseInt(activityDto.getLevel())));

        } else {
            ivEnterUserLevel.setVisibility(View.GONE);
        }

        tvBayMsg.setVisibility(View.GONE);
        tvEnterMsg.setVisibility(View.VISIBLE);
        StringBuilder builder = new StringBuilder();
        builder.append(activityDto.getNotice().toString());
        tvEnterMsg.setText(builder);
        //   tvAngel.setText(builder.toString());
        imEnterPic.setVisibility(View.VISIBLE);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor("#" + activityDto.getColor()));
        gradientDrawable.setCornerRadius(getResources().getDimension(R.dimen.dp_12));
        rlEnterBack.setBackground(gradientDrawable);
        startEnterAnimation();
    }

    /**
     * 用户有座驾进入直播间
     */
    private void setEnterData() {
        SpannableStringBuilder spanString = null;
        if (somecomingCb != null && somecomingCb.isTiming()) {

        }
        if (isActivityOne) {
            return;
        }
        if (enTerList == null || enTerList.size() <= 0)
            return;
        isActivityOne = true;
        llEnter.setVisibility(View.VISIBLE);
        tvAngel.setVisibility(View.VISIBLE);
        tvBayMsg.setVisibility(View.VISIBLE);
        imEnterPic.setVisibility(View.GONE);
        FloatScreenBean floatScreenBean = enTerList.poll();
        if (floatScreenBean.watcherDto == null && floatScreenBean.activityDto != null) {
            addActivityMsg(floatScreenBean);
            return;
        }
        assert floatScreenBean != null;
        WatcherDto watcherDto = floatScreenBean.watcherDto;
        StringBuilder builder = new StringBuilder();
        Spannable spannable = null;
        if (watcherDto.userVipLevel == 3) {

            builder.append(" 0");
        }


        if (watcherDto.userLevel > 0) {   //用户等级
            ivEnterUserLevel.setVisibility(View.GONE);
            ivEnterUserLevel.setImageResource(ImageLoader.getUserLevel(watcherDto.userLevel));
            builder.append(" 1");
        } else {
            ivEnterUserLevel.setVisibility(View.GONE);
        }

        if (watcherDto.userVipLevel > 0) {   //vip等级
            ivEnterVipLevel.setVisibility(View.GONE);
            ivEnterVipLevel.setImageResource(ImageLoader.getVipLevel(watcherDto.userVipLevel));
            builder.append(" 2");//头像
        } else {
            ivEnterVipLevel.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(watcherDto.guardSwf)) {  //座驾
            ivEnterGuard.setVisibility(View.GONE);
            ImageLoader.loadImg(ivEnterGuard, watcherDto.userShouHuLevelIMG);
            builder.append(" 3");
        } else {
            ivEnterGuard.setVisibility(View.GONE);
        }


        if (watcherDto.isRoomManage) {   //是否房管
            ivEnterRoommanager.setVisibility(View.GONE);
            builder.append(" 4");
        } else {
            ivEnterRoommanager.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(watcherDto.cpRank)) {
            builder.append(" 5");
        }


        builder.append(" ").append(watcherDto.nickname);

        if (!TextUtils.isEmpty(watcherDto.carSwf) && !TextUtils.isEmpty(watcherDto.guardSwf)) {
            builder.append(" 驾着 「")
                    .append(watcherDto.carName)
                    .append("」「")
                    .append(watcherDto.guarName)
                    .append("」");
        } else {
            if (!TextUtils.isEmpty(watcherDto.carSwf)) {
                builder.append(" 驾着 「");
                builder.append(watcherDto.carName).append("」");

            }
            if (!TextUtils.isEmpty(watcherDto.guardSwf)) {
                builder.append(" 驾着 「");
                builder.append(watcherDto.guarName).append("」");
            }
        }
        if (watcherDto.userVipLevel <= 0) {
            builder.append(" 来了");
        } else {
            builder.append(" 闪亮登场   ");
            spannable = new SpannableString(builder.toString());
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(PrivilegeEffectUtils.getInstance().getEffectsWithVip(0).getComingTxtColor()));
            spannable.setSpan(colorSpan, builder.toString().length() - 4, builder.toString().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        }
        spanString = new SpannableStringBuilder(builder.toString());

        if (watcherDto.userVipLevel == 3) {   //VIP等级3

            int start = builder.toString().indexOf("0");
            CircleUrlImageSpan roomSpan = new CircleUrlImageSpan(BaseLiveingRoomActivity.this, watcherDto.headImg, tvBayMsg, DensityUtils.dp2px(this, 16), DensityUtils.dp2px(this, 16));
            spanString.setSpan(roomSpan, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (watcherDto.userLevel > 0) {
            int levelStart = builder.toString().indexOf("1");
            CenteredImageSpan levelSpan = new CenteredImageSpan(this, ImageLoader.getUserLevel(watcherDto.userLevel));
            spanString.setSpan(levelSpan, levelStart, levelStart + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (watcherDto.userVipLevel > 0) {   //vip等级
            int start = builder.toString().indexOf("2");
            CenteredImageSpan vipSpan = new CenteredImageSpan(this, ImageLoader.getVipLevel(watcherDto.userVipLevel));
            spanString.setSpan(vipSpan, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (!TextUtils.isEmpty(watcherDto.guardSwf)) {
            int levelStart = builder.toString().indexOf("3");
            UrlImageSpan levelSpan = new UrlImageSpan(BaseLiveingRoomActivity.this, watcherDto.userShouHuLevelIMG, tvBayMsg, DensityUtils.dp2px(this, 14), DensityUtils.dp2px(this, 14));
            spanString.setSpan(levelSpan, levelStart, levelStart + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        if (watcherDto.isRoomManage) {//替换房管图片
            int start = builder.toString().indexOf("4");
            CenteredImageSpan roomSpan = new CenteredImageSpan(this, R.drawable.ic_roommanager, DensityUtils.dp2px(this, 14), DensityUtils.dp2px(this, 14));
            spanString.setSpan(roomSpan, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (!TextUtils.isEmpty(watcherDto.cpRank) && Integer.parseInt(watcherDto.cpRank) < 11) {
            int start = builder.toString().indexOf("5");
            int res = ImageLoader.getResId("pic_cp" + watcherDto.cpRank, R.drawable.class);
            CenteredImageSpan toutiaoIcSpan = new CenteredImageSpan(this, res);
            spanString.setSpan(toutiaoIcSpan, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (null != spannable) {
            tvEnterMsg.setText(spannable);
        } else {
            tvEnterMsg.setText(builder.toString());
        }
        tvEnterMsg.setVisibility(View.GONE);
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (watcherDto.userVipLevel == 3) {

            gradientDrawable.setColor(Color.parseColor(PrivilegeEffectUtils.getInstance().getEffectsWithVip(2).getBubbleColor()));
            rlEnterBack.setBackground(gradientDrawable);
        } else {
            if (watcherDto.userVipLevel == 2) {
                gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(Color.parseColor(PrivilegeEffectUtils.getInstance().getEffectsWithVip(1).getBubbleColor()));
                rlEnterBack.setBackground(gradientDrawable);
            } else {
                gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(Color.parseColor(PrivilegeEffectUtils.getInstance().getEffectsWithVip(0).getBubbleColor()));

            }

        }


        tvBayMsg.setText(spanString);

        gradientDrawable.setCornerRadius(getResources().getDimension(R.dimen.dp_12));
        rlEnterBack.setBackground(gradientDrawable);
        startEnterAnimation();
    }

    private void startEnterAnimation() {
        ViewTreeObserver.OnGlobalLayoutListener l = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llEnter.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = llEnter.getWidth() + BaseLiveingRoomActivity.this.getResources().getDimensionPixelOffset(R.dimen.dp_7);
                ObjectAnimator animation = GiftAnimationUtil.createFlyFromLtoR(llEnter, 0, width, 1000, new LinearInterpolator());
                animation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (null == somecomingCb) {
                            somecomingCb = new CountBackUtils();
                        }
                        somecomingCb.countBack(2, new CountBackUtils.Callback() {
                            @Override
                            public void countBacking(long time) {
                                isActivityOne = false;
                            }

                            @Override
                            public void finish() {
                                llEnter.setVisibility(View.INVISIBLE);
                                setEnterData();
                            }
                        });
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                animation.start();
            }
        };
        llEnter.getViewTreeObserver().addOnGlobalLayoutListener(l);
    }

    private void startActivityEnterAnimation() {
        ViewTreeObserver.OnGlobalLayoutListener l = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llEnterActivity.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = llEnterActivity.getWidth() + BaseLiveingRoomActivity.this.getResources().getDimensionPixelOffset(R.dimen.dp_7);
                ObjectAnimator animation = GiftAnimationUtil.createFlyFromLtoR(llEnterActivity, 0, width, 2000, new LinearInterpolator());
                animation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (null == somecomingAct) {
                            somecomingAct = new CountBackUtils();
                        }
                        somecomingAct.countBack(3, new CountBackUtils.Callback() {
                            @Override
                            public void countBacking(long time) {

                            }

                            @Override
                            public void finish() {
                                llEnterActivity.setVisibility(View.INVISIBLE);
                                isActivitySomeOneComing = false;
                                activitySomeoneComing();
                            }
                        });
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                animation.start();

            }
        };
        llEnterActivity.getViewTreeObserver().addOnGlobalLayoutListener(l);
    }

    public void buyGuardMsg(LivingRoomGuardMsg buyGuardMsg) {
        buyGuardList.offer(buyGuardMsg);
        if (viewBuyGuard.getVisibility() != View.VISIBLE) {
            showGuardAnimation();
        }

    }


    private void showGuardAnimation() {
        if (viewBuyGuard.getVisibility() == View.VISIBLE) return;
        LivingRoomGuardMsg buyGuardMsg = buyGuardList.poll();
        ImageLoader.loadImg(ivBuyGuard, buyGuardMsg.userShouHuLevelIMG);
        tvBuyGuardUserName.setText(buyGuardMsg.nickname);
        tvBuyGuardName.setText("开通了 " + buyGuardMsg.userShouHuLevel);
        viewBuyGuard.setVisibility(View.VISIBLE);
        viewBuyGuard.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewBuyGuard.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float width = ScreenUtils.getScreenWidth(BaseLiveingRoomActivity.this) - getResources().getDimension(R.dimen.dp_20);
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(viewBuyGuard, "translationX", width, 0 - viewBuyGuard.getWidth());
                animator1.setInterpolator(new LinearInterpolator());
                AnimatorSet annoucementAnimalSet = new AnimatorSet();
                annoucementAnimalSet.setDuration(8000);
                annoucementAnimalSet.addListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (buyGuardCb == null)
                            buyGuardCb = new CountBackUtils();
                        buyGuardCb.countBack(3, new CountBackUtils.Callback() {
                            @Override
                            public void countBacking(long time) {

                            }

                            @Override
                            public void finish() {
                                viewBuyGuard.setVisibility(View.GONE);
                                if (buyGuardList.size() > 0) {
                                    showGuardAnimation();
                                }
                            }
                        });
//                viewAnnoucement.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                annoucementAnimalSet.playSequentially(animator1);
                annoucementAnimalSet.start();
            }
        });
    }

    /**
     * 刷新心愿信息
     */
    public void refreshWish() {
        L.e("======刷新心愿数据");
        L.e("=====" + channelId);
        L.e("=====" + getUserInfo().getId());
        if (isPush) {
            p.getActivityMsg(channelId);
        } else {
            p.loadRoomInfo(groupId, channelId, true);
        }
    }

    public void addMsg(LivingRoomTextMsg msgDto) {
        try {
            if (livingMsgAdapter == null) initMsgRv();
            livingMsgAdapter.addData(msgDto);
            if (livingMsgAdapter == null || livingMsgAdapter.getItemCount() <= 0) return;
            ((LinearLayoutManager)         // 必须异步调用
                    getChatRv().getLayoutManager()).scrollToPositionWithOffset(livingMsgAdapter.getItemCount() - 1, Integer.MIN_VALUE);
//            giftHandle.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            }, 50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearMsg() {
        if (livingMsgAdapter != null)
            livingMsgAdapter.clearData();
    }

    public void sendDanmu(String content, String color, int userLevel, String nickname, String headUrl, String nickColor, String msgColor) {
        DanmakuConfigUtil.getOneDanmu2(this, mDmkContext, danmakuView.getCurrentTime(), content, color, userLevel, nickname, headUrl, nickColor, msgColor, new DanmakuConfigUtil.OnImgDanmuCreatedListener() {
            @Override
            public void onSuccess(BaseDanmaku danmaku) {
                if (danmakuView.getVisibility() != View.VISIBLE) {
                    danmakuView.setVisibility(View.VISIBLE);
                }
                danmakuView.addDanmaku(danmaku);
            }
        });
    }

    public void showGiftBroadcast(GiftBoardcastDto content) {
        announcementList.offer(content);
        giftBroadcast(announcementList.poll());

    }

    String noticeBgImg = "";

    public static Bitmap imageScale(Bitmap bitmap, int dst_w, int dst_h) {

        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        float scale_w = ((float) dst_w) / src_w;
        float scale_h = ((float) dst_h) / src_h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,
                true);
        return dstbmp;
    }

    /**
     * 活动公告
     * 价值大的礼物会有头条公告 参加活动开启公告
     */
    protected void annotation() {
        if (isFastClickLong())return;

        if (noticeList.size() == 0) {
            return;
        }
        if (viewAnnoucement.getVisibility() == View.VISIBLE) {
            return;
        }
        NoticeBean noticeBack = noticeList.poll();
        if (noticeBack.getChannelId() != 0) {
            viewAnnoucement.setClickable(true);
            viewAnnoucement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isPush) {
                        if (groupId != getIMGroupId(noticeBack.getChannelId()))
                            changeRoom(noticeBack.getChannelId() + "");
                    }
                }
            });

        } else {
            viewAnnoucement.setClickable(false);
        }
        if (noticeBack.getChannelId() == 0) {
            tvAnnouncementGo.setVisibility(View.GONE);
        } else {
            if (noticeBack.getChannelId() == channelId || isPush) {
                tvAnnouncementGo.setVisibility(View.GONE);
            } else {
                tvAnnouncementGo.setVisibility(View.VISIBLE);
            }
        }


        float pad = getResources().getDimension(R.dimen.dp_15);
        if (noticeBack.getSendUserName().equals("")) {//如果有昵称 不显示活动
            viewAnnoucement.setVisibility(View.VISIBLE);
            im_announcement.setVisibility(View.INVISIBLE);
            if (!TextUtils.isEmpty(noticeBack.getNoticeBack()) || !TextUtils.isEmpty(noticeBgImg)) {//图片不为空
                if (!TextUtils.isEmpty(noticeBack.getNoticeBack())) {//存背景图片
                    noticeBgImg = noticeBack.getNoticeBack();
                }
                ImageLoader.setBackground(this, noticeBack.getNoticeBack(), viewAnnoucement);
                viewAnnoucement.setPadding(0, 0, 0, 0);
            } else {
                viewAnnoucement.setBackgroundResource(R.drawable.pic_notice_backs);
                viewAnnoucement.setPadding((int) pad, 0, (int) pad, 0);
            }
            tvAnnouncement.setText(noticeBack.getContext().toString());

            tvAnnouncement.init(getWindowManager(), "", "", null, "");
        } else {
            viewAnnoucement.setBackgroundResource(R.drawable.pic_notice_backs);
            viewAnnoucement.setPadding((int) pad, 0, (int) pad, 0);
            im_announcement.setVisibility(View.VISIBLE);
            tvAnnouncement.setText(noticeBack.getContext().toString());
            //   UrlImageSpan span = new UrlImageSpan(this, noticeBack.getGiftIcon(), tvAnnouncement, DensityUtils.dp2px(this, 16), DensityUtils.dp2px(this, 16));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    URL bmp = null;
                    try {
                        bmp = new URL(noticeBack.getGiftIcon());
                        //    bmp = new URL("https://www.baidu.com");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        Bitmap pngBM = BitmapFactory.decodeStream(bmp.openStream());
                        if (null != pngBM) {
                            tvAnnouncement.init(getWindowManager(), noticeBack.getSendUserName(), noticeBack.getReceiveUserName(),
                                    imageScale(pngBM, DensityUtils.dp2px(BaseLiveingRoomActivity.this, 18), DensityUtils.dp2px(BaseLiveingRoomActivity.this, 18)), noticeBack.getSendNum());
                            viewAnnoucement.post(new Runnable() {
                                @Override
                                public void run() {
                                    viewAnnoucement.setVisibility(View.VISIBLE);
                                }
                            });
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

            //     Bitmap btm = ImageLoader.getBitmap(BaseLiveingRoomActivity.this,noticeBack.getGiftIcon());

        }
        viewAnnoucement.setAlpha(0f);
        tvAnnouncement.setIDeleteListener(new AutoScrollTextView.IAddListListener() {
            @Override
            public void deleteBank() {
                viewAnnoucement.setAlpha(0f);
                viewAnnoucement.setVisibility(View.GONE);
                if (noticeList.size() > 0) {
                    annotation();

                }

            }
        });
//        tvAnnouncement.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                tvAnnouncement.startScroll();
//            }
//        }, 1000);


        //    tvAnnouncement.stopScroll();
//        float width = ScreenUtils.getScreenWidth(BaseLiveingRoomActivity.this) - getResources().getDimension(R.dimen.dp_10);
//        viewAnnoucement.setVisibility(View.VISIBLE);
//        viewAnnoucement.setTranslationX(width);
//
//        bulletAnimatorSet = new AnimatorSet();
        viewAnnoucement.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animatorBg = ObjectAnimator.ofFloat(viewAnnoucement, "alpha", 0f, 1f);
                animatorBg.setDuration(1000);
                animatorBg.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        tvAnnouncement.startScroll();
                    }
                });
                animatorBg.start();
            }
        }, 100);


//
//
//        if (animaAnnouncement == null)
//            animaAnnouncement = new AnimatorSet();
//        animaAnnouncement.setDuration(2000);
//        animaAnnouncement.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                int widthss = tvAnnouncement.getWidth();
//                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tvAnnouncement, "x", 0,
//                        -(ScreenUtils.getScreenWidth(BaseLiveingRoomActivity.this) + widthss) / 2).setDuration(6000);
//                objectAnimator.setRepeatCount(3);
//                objectAnimator.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        viewAnnoucement.setVisibility(View.GONE);
//                    }
//                });
//                objectAnimator.start();
////                if (null == annotationCb)
////                    annotationCb = new CountBackUtils();
////                annotationCb.countBack(6, new CountBackUtils.Callback() {
////                    @Override
////                    public void countBacking(long time) {
////
////                    }
////
////                    @Override
////                    public void finish() {
////                        tvAnnouncementGo.setVisibility(View.VISIBLE);
////                        viewAnnoucement.setVisibility(View.GONE);
////
////                        if (announcementList.size() > 0) {
//////                            showGiftBroadcast(announcementList.poll());
////                            giftBroadcast(announcementList.poll());
////                        }
////                    }
////                });
////                viewAnnoucement.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });
//        animaAnnouncement.playSequentially(animator1);
//        animaAnnouncement.start();
    }

    /**
     * 翻页时释放动画
     */
    protected void releaseAnim() {
        if (bulletAnimatorSet != null && bulletAnimatorSet.isRunning()) {
            bulletAnimatorSet.end();
        }
    }

    AnimatorSet annoucementAnimalSet;

    protected void annotationNotice(String content) {
        tvNoticeAnnouncement.setText(content);
        noticeAnnoucement.setVisibility(View.VISIBLE);
//        //设置走马灯 每次赋值 都要在代码中动态设置

        tvNoticeAnnouncement.setSingleLine(true);//设置单行显示
        tvNoticeAnnouncement.setHorizontallyScrolling(true);//横向滚动
        tvNoticeAnnouncement.setMarqueeRepeatLimit(1);
        tvNoticeAnnouncement.setSelected(true);//开始滚
        tvNoticeAnnouncement.setSingleLine(true);
        float width = ScreenUtils.getScreenWidth(this) - getResources().getDimension(R.dimen.dp_20);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(noticeAnnoucement, "translationX", width, 0);
        animator1.setInterpolator(new LinearInterpolator());
        annoucementAnimalSet = new AnimatorSet();
        annoucementAnimalSet.setDuration(2000);
        annoucementAnimalSet.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                noticeAnnoucement.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        noticeAnnoucement.setVisibility(View.GONE);
                    }
                }, 3000);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        annoucementAnimalSet.playSequentially(animator1);
        annoucementAnimalSet.start();
    }

    /**
     * 公告
     */
    public GiftBoardcastDto mGiftBoardcastDto;

    private void giftBroadcast(GiftBoardcastDto content) {
        if (content == null) return;
        if (annoucementAnimalSet != null)
            annoucementAnimalSet.cancel();
        if (null != annotationCb) {
            annotationCb.destory();
            annotationCb = null;
        }
        mGiftBoardcastDto = content;


//        Map<String, String> map =  new HashMap<>();
//        map.put("公告：","#ffffff");
//        map
//        StringBuilder builder = new StringBuilder();
//        builder.append("")
//                .append("公告：")
//                .append(content.msg);

//        SpannableStringBuilder spannableString = new SpannableStringBuilder(builder.toString());
//        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FFEA7F"));
//        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#FFEA7F"));
//        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#FFEA7F"));
//        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.parseColor("#FFEA7F"));
//        ForegroundColorSpan colorSpan4 = new ForegroundColorSpan(Color.parseColor("#F38EFF"));
//
//        if (content.msg.contains("告白航班")) {
//            int firstSendIndex = builder.toString().trim().indexOf(content.sendUserName);
//            spannableString.setSpan(colorSpan4, firstSendIndex, firstSendIndex + content.sendUserName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//            ImageSpan centeredImageSpan = new ImageSpan(BaseLiveingRoomActivity.this, R.drawable.ic_rob);
////            CenteredImageSpan centeredImageSpan = new CenteredImageSpan(BaseLiveingRoomActivity.this, R.drawable.ic_im_toutiao);
//            spannableString.setSpan(centeredImageSpan, spannableString.toString().trim().length() - 1, spannableString.toString().trim().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//        } else {
//            int firstSendIndex = builder.toString().indexOf(content.sendUserName);
//            int lastSendIndex = builder.toString().lastIndexOf(content.sendUserName);
//            spannableString.setSpan(colorSpan, firstSendIndex, firstSendIndex + content.sendUserName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//            spannableString.setSpan(colorSpan1, lastSendIndex, lastSendIndex + content.sendUserName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//            int firstRecIndex = builder.toString().indexOf(content.receiveUserName);
//            int lastRecIndex = builder.toString().lastIndexOf(content.receiveUserName);
//            spannableString.setSpan(colorSpan2, firstRecIndex, firstRecIndex + content.receiveUserName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//            spannableString.setSpan(colorSpan3, lastRecIndex, lastRecIndex + content.receiveUserName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//        }
//        spannableString = spannableString.append(" ");
//        UrlImageSpan span = new UrlImageSpan(this, content.giftIcon, tvAnnouncement, DensityUtils.dp2px(this, 16), DensityUtils.dp2px(this, 16));
//
//        spannableString.setSpan(span, spannableString.toString().length() - 1, spannableString.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder spannableString = new SpannableStringBuilder(content.msg);
        noticeList.add(new NoticeBean(spannableString, "", content.sendUserName, content.receiveUserName, content.channelId, content.giftIcon, content.sendNum));

        annotation();
    }

    private void initMsgRv() {
        if (null != livingMsgAdapter) return;
        RecyclerView rv = getChatRv();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rv.setLayoutManager(linearLayoutManager);
        OverScrollDecoratorHelper.setUpOverScroll(rv, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        chatList = new ArrayList<>();
        livingMsgAdapter = new LivingMsgAdapter(chatList, isPush, getUserInfo().getId());
        livingMsgAdapter.setState(state);
        livingMsgAdapter.setHasStableIds(true);
        rv.setAdapter(livingMsgAdapter);
        rv.getItemAnimator().setChangeDuration(0);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                try {
                    if (manager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) manager;
                        if (livingMsgAdapter.getData().size() > 3) {
                            int firstVisiblePos = linearManager.findFirstVisibleItemPosition();
                            setViewAplha(Objects.requireNonNull(linearManager.findViewByPosition(firstVisiblePos)));
                            setViewAplha(Objects.requireNonNull(linearManager.findViewByPosition(firstVisiblePos + 1)));
                            int firstCompletelyVisible = linearManager.findFirstCompletelyVisibleItemPosition();
                            View view1 = linearManager.findViewByPosition(firstCompletelyVisible);
                            View view2 = linearManager.findViewByPosition(firstCompletelyVisible + 1);
                            if (view1 != null) {
                                view1.setAlpha(1);
                            }
                            if (view2 != null) {
                                view2.setAlpha(1);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


//        livingMsgAdapter.setOnItemChildClickListener((adapter, view, position) -> new UserInfoPopwindow(this, livingMsgAdapter.getItem(position).uid, channelId, roomManager).showPopupWindow());
        livingMsgAdapter.setOnNameClickListener(new LivingMsgAdapter.onNameClickListener() {
            @Override
            public void onNameClick(int positon) {
                if (isFastClick()) return;
                if (livingMsgAdapter.getItem(positon) == null) return;
                newUserInfoPopwindow = new NewUserInfoPopwindow(BaseLiveingRoomActivity.this, Integer.parseInt(livingMsgAdapter.getItem(positon).uid), channelId, roomManager);
                newUserInfoPopwindow.showPopupWindow();

            }

            //撩他一下
            @Override
            public void onTeaseHimClick(int position) {
                p.sendTeaseHimMsg(livingMsgAdapter.getItem(position));
                LivingRoomTextMsg msg = livingMsgAdapter.getItem(position);
                if (msg != null) {   //撩用户
                    msg.isTeaseHim = true;
                    livingMsgAdapter.setData(position, msg);
                    livingMsgAdapter.notifyItemChanged(position);
                }
            }

            //我也关注
            @Override
            public void onAnchorAttentionClick(int position) {
                p.attention(channelId, false);
            }

            //我也分享
            @Override
            public void onShareAttentionClick(int position) {
                showShareDialog();
            }
        });
        decorView.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        addSystemList();
    }


//    float beginPercent = 0.2f;
//    float endValue = 2;

    public void setViewAplha(View view) {
        if (null == view) {
            return;
        }
//        float p = DensityUtils.px2dp(this, Math.abs((int) view.getY())) * 1.0f / DensityUtils.px2dp(this, view.getHeight()) * 1.0f;
//        float curPercent = Float.compare(p - beginPercent, 0.0f) < 0 ? 0.0f : p - beginPercent;
//        curPercent = Float.compare(1, curPercent * endValue) < 0 ? 1 : curPercent * endValue;
        view.setAlpha(0.5f);
    }


    public abstract RecyclerView getChatRv();

    public abstract void addSystemList();

    public abstract void changeRoom(String id);

    public abstract ViewPager getViewPager();

    /**
     * 初始化弹幕
     */
    protected void initDanmu() {

        mDmkContext = DanmakuConfigUtil.getDanmakuContext();
        danmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                new Handler(Looper.getMainLooper()).postDelayed(()->{
                    if (null != danmakuView)
                        danmakuView.start();
                },500);
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
        mParser = DanmakuConfigUtil.getDefaultDanmakuParser();
        danmakuView.prepare(mParser, mDmkContext);
    }

//    GiftBean giftBean;

    //大礼物动画回调
    private Player.EventListener bigGiftListener = new Player.EventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case ExoPlayer.STATE_READY:
                    //用户端在隐藏界面或者结束界面不展示动画
                    if (!isPush) {
                        if (null != getViewPager() && getViewPager().getCurrentItem() != 0) {
                            frameAnimationView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        frameAnimationView.setVisibility(View.VISIBLE);
                    }
                    break;
                case ExoPlayer.STATE_ENDED:
                case ExoPlayer.STATE_IDLE:
                    frameAnimationView.setVisibility(View.GONE);
                    if (isShowPlane) {
                        if (null != fallingLayout) {
                            if (!isFastClick()) {
                                fallingLayout.addFallingBody(250);
                            }
                        }
                        isShowPlane = false;
                        try {
                            showBigGift();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
        }
    };






    public void showBigGift() throws Exception {
        runOnUiThread(new MRunnable() {
            @SuppressLint("WrongConstant")
            @Override
            public void run() {
                synchronized (frameAnimationView) {
                    if (frameAnimationView == null) return;
                    if (silkyAnimation == null) {
                        silkyAnimation = ExoPlayerFactory.newSimpleInstance(getApplicationContext());
                        silkyAnimation.addListener(bigGiftListener);
                        frameAnimationView.setSimpleExoPlayer(silkyAnimation);
                        frameAnimationView.setGlFilter(new AlphaFrameFilter());
                    }
                    //加载大礼物
                    if (frameAnimationView == null || frameAnimationView.getVisibility() == View.VISIBLE) {
                        return;
                    }
                    if (frameAnimationView.getVisibility() == View.VISIBLE || svga_animation.getVisibility() == View.VISIBLE && svga_animation.isAnimating()) {
                        return;
                    }

                    GiftBean bean;
                    if (null != myGiftList && myGiftList.size() > 0) {
                        bean = myGiftList.poll();

                        L.e("biggift,remove mygift");
                    } else if (null != bigGiftList && bigGiftList.size() > 0) {
                        bean = bigGiftList.poll();
                        L.e("biggift,remove othergift");
                    } else {
                        return;
                    }
                    if (null != bean.swf) {
                        String msg = bean.swf.substring(bean.swf.length() - 4, bean.swf.length());
                        if (msg.equals("svga")) {
                            animationSvga = new SvgaUtils(BaseLiveingRoomActivity.this, svga_animation);
                            animationSvga.initAnimator(new SVGACallback() {
                                @Override
                                public void onPause() {
                                }

                                @Override
                                public void onFinished() {
                                    if (null != svga_animation) {
                                        svga_animation.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onRepeat() {
                                }

                                @Override
                                public void onStep(int i, double v) {
                                }
                            });
                            int start = bean.swf.lastIndexOf("/");
                            int end = bean.swf.lastIndexOf(".svga");
                            String name = bean.swf.substring(start + 1, end);
                            animationSvga.getFile(name, bean.swf);
                            svga_animation.setVisibility(View.VISIBLE);
                            return;
                        }
                    }
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/fwhynew/" + bean.bigName);
                    if (file.exists()) {
                        frameAnimationView.setPlayerScaleType(PlayerScaleType.RESIZE_FIT_WIDTH);
                        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(BaseLiveingRoomActivity.this, Util.getUserAgent(BaseLiveingRoomActivity.this, getPackageName()));
                        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.fromFile(file));
                        silkyAnimation.prepare(mediaSource);
                        silkyAnimation.setPlayWhenReady(true);
//                        silkyAnimation.setPlayWhenReady(true);
                    } else {
//                        frameAnimationView.setPlayerScaleType(PlayerScaleType.RESIZE_NONE);
//                        Uri uri = Uri.parse(bean.swf);
//                        MediaSource mediaSource = new ProgressiveMediaSource.Factory( new DefaultDataSourceFactory(BaseLiveingRoomActivity.this), MatroskaExtractor.FACTORY).createMediaSource(MediaItem.fromUri(uri));
//                        //player.addListener(this);
//                        silkyAnimation.setMediaSource(mediaSource);
//                        silkyAnimation.prepare();
//                        silkyAnimation.play();


                        frameAnimationView.setPlayerScaleType(PlayerScaleType.RESIZE_NONE);
                        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(BaseLiveingRoomActivity.this,
                                Util.getUserAgent(BaseLiveingRoomActivity.this, getPackageName()));
                        Uri uri = Uri.parse(bean.swf);

                        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                                .createMediaSource(uri);

                        silkyAnimation.prepare(videoSource);
                        silkyAnimation.setPlayWhenReady(true);
                    //    silkyAnimation.setPlayWhenReady(true);
                    }
                }
            }
        });
    }



    /**
     * @param isOurSide 1自己 0 对方
     */
    @Override
    public void getFirstKill(final int isOurSide, String url) {
        try {
            if (TextUtils.isEmpty(url)) {
                return;
            }

            String msg = url.substring(url.length() - 4, url.length());
            if (msg.equals("svga")) {
                pkSvga = new SvgaUtils(this, svga_pk);
                pkSvga.initAnimator(new SVGACallback() {
                    @Override
                    public void onPause() {
                    }

                    @Override
                    public void onFinished() {
                        if (null != svga_pk) {
                            svga_pk.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onRepeat() {
                    }

                    @Override
                    public void onStep(int i, double v) {
                    }
                });
                pkSvga.initAnimator();
                svga_pk.setVisibility(View.VISIBLE);
                int start = url.lastIndexOf("/");
                int end = url.lastIndexOf(".svga");
                String name = url.substring(start + 1, end);
                pkSvga.getFile(name, url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOne = false;

    /**
     * 滑动清除不用的view  fuck   我tm只是加的备注 改的时候别骂我
     * 是否是第一次加载 true  只用用户会调用
     */
    protected void changeRoomClearAllRes() {
        try {
            L.e("tag", "滚动调用");
            if (isOne) {
                p.clearHttpRequest();
            } else
                isOne = true;
            stopGiftThead();//礼物定时回调清除
            clearPkAnim();//pk动画关闭
            im_cp.setVisibility(View.GONE);//头像下面的cp表示隐藏
            activityNoStart();//啥也没干 活动....不...开始？
            endActivity = false;//万圣节时 临时判断是否需要刷新的
            isActivityOne = false;//判断座驾进入直播间 不知道为啥要写这个
            isActivitySomeOneComing = false;//进场发消息做的队列
            cloneAnimation();//关闭送礼动画类
            if (viewBuyGuard.getVisibility() == View.VISIBLE)
                viewBuyGuard.setVisibility(View.GONE);//守护公告
            if (null != buyGuardCb && buyGuardCb.isTiming()) {
                buyGuardCb.destory();//定时器 （开通守护）
            }
            if (fallingLayout != null) {//红包雨
                fallingLayout.release();
                fallingLayout.clean();
            }
            noticeList.clear();//公告
            tvAnnouncement.clearAnimation();//活动和大礼物公告
            tvAnnouncement.stopScroll();
//            danmakuView.pause();//弹幕
//            danmakuView.clear();
//            danmakuView.removeAllDanmakus(true);//弹幕
            someoneComingList.clear();//守护？座驾？ 进场通知
            enTerList.clear();//同上
            buyGuardList.clear();//同上

            if (null != rlGiftAnim)//没看明白
                rlGiftAnim.removeAllViews();
            if (null != giftRoot) {//也没看明白
                giftRoot.resetItemLayoutState();
                giftRoot.clearAnimation();
                giftRoot.setVisibility(View.GONE);
            }
            isShowPkResulting = false;//初始化不显示pk结果
            if (null != somecomingCb) {//进入直播间飘屏倒计时
                somecomingCb.destory();
                somecomingCb = null;
            }
            if (null != somecomingAct) {//同上
                somecomingAct.destory();
                somecomingAct = null;
            }
            llEnter.setVisibility(View.INVISIBLE);//公告类
            llEnterActivity.setVisibility(View.INVISIBLE);//公告类
//            llMedalEnter.setVisibility(View.INVISIBLE);
            actBanner.stopAutoScroll();//活动
            actBanner.setVisibility(View.GONE);
            actBanner.onDestroy();
            viewAnnoucement.setVisibility(View.GONE);//活动大礼物公告
            initMsgRv();
            if (pendantLayout != null) {
                fl_back.removeView(pendantLayout);//隐藏挂件
                pendantLayout = null;
            }
        } catch (Exception e) {
        }
    }

    //做了个分类 这里全部都是视频动画或者svga动画关闭
    private void cloneAnimation() {
        bigGiftList.clear();//大礼物数据

        myGiftList.clear();
        if (null != pkSvga) pkSvga.clearList();//pk为展示的动画数据清空

        if (null != silkyAnimation) silkyAnimation.stop();//停止动画
        if (null != animationSvga) animationSvga.clearList();
        frameAnimationView.setVisibility(View.GONE);//动画隐藏

        if (null != pkSvga) svga_pk.stopAnimation();//pk动画关闭

        if (null != svga_animation) svga_animation.stopAnimation();


    }

    public ShouhuListPopwindow shouhuListPopwindow;


    public void setGuardWindow(int total, ArrayList<GuardListDto.Guard> records) {

        if (null != shouhuListPopwindow)
            shouhuListPopwindow.setData(total, records);
    }

    public synchronized void showPkResult(PkResultDto pkResultDto, boolean isSingle) {
    }

    /**
     * 头条飘屏
     *
     * @param toutiaoBean
     */

    protected ToutiaoInfoPopwindow toutiaoInfoPopwindow;
    protected GiftPiaopingDto mGiftPiaopingDto;
    protected boolean mIsShowLong;

    public void showToutiao(GiftPiaopingDto toutiaoBean) {
        //   toutiao_im_tt.setVisibility(View.VISIBLE);
        mGiftPiaopingDto = toutiaoBean;
        if (toutiaoInfoPopwindow != null) {
            toutiaoInfoPopwindow.initView(toutiaoBean, String.valueOf(channelId), nickname);
        } else {
            toutiaoInfoPopwindow = new ToutiaoInfoPopwindow(BaseLiveingRoomActivity.this, toutiaoBean, String.valueOf(channelId), nickname);
        }
        String content = toutiaoBean.getNickname() + "  送给  " + toutiaoBean.getAnchorNickname()+toutiaoBean.getGiftName();
        toutiaoContent.setText(content);
        ImageLoader.loadImg(toutiaoGift, toutiaoBean.getGiftIcon());
        ImageLoader.loadImg(toutiaoGift2, toutiaoBean.getGiftIcon());
        ImageLoader.loadImg(toutiaoUseravatar, toutiaoBean.getHeaderurl());
        toutiaoGiftNum.setText("x " + toutiaoBean.getSendNum());
        toutiaoGiftNum2.setText("x " + toutiaoBean.getSendNum());
        toutiaoLeftviewLong.setVisibility(View.VISIBLE);
        rlToutiaoView.setBackgroundResource(R.drawable.pic_tt_back);
        toutiaoLeftviewShort.setVisibility(View.GONE);
        viewToutiao.setVisibility(View.VISIBLE);
        toutiaoLefttimeTv.setVisibility(View.GONE);
        mIsShowLong = true;
        if (toutiaoBean.getAnchorUserId() == channelId || isPush) {
            mTvGoWatch.setVisibility(View.INVISIBLE);
        } else {
            mTvGoWatch.setVisibility(View.VISIBLE);
        }

        if (null != toutiaoInfoPopwindow) {
            toutiaoInfoPopwindow.setChannelId(channelId + "");
        }

        //toutiaoContent.setMarqueeRepeatLimit(Integer.MAX_VALUE);
        //toutiaoContent.setFocusable(true);
        //toutiaoContent.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        //toutiaoContent.setSingleLine();
        //toutiaoContent.setFocusableInTouchMode(true);
        //toutiaoContent.setHorizontallyScrolling(true);
        //toutiaoContent.setMarqueeRepeatLimit(1);
        //toutiaoContent.setSelected(true);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.toutian_in_left);
        Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.toutian_out_right);
        animationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                toutiaoLeftviewLong.setVisibility(View.GONE);
                rlToutiaoView.setBackgroundResource(0);
                toutiaoLeftviewShort.setVisibility(View.VISIBLE);
                mTvGoWatch.setVisibility(View.GONE);
                toutiaoLefttimeTv.setVisibility(View.VISIBLE);
                mIsShowLong = false;


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        viewToutiao.startAnimation(animation);     //从左侧进入直播间
        if (toutiaoCountback != null) {
            toutiaoCountback.destory();
            toutiaoCountback = null;
        }
        if (null == toutiaoCountback)
            toutiaoCountback = new CountBackUtils();
        toutiaoCountback.countBack(toutiaoBean.getTimeLeft(), new CountBackUtils.Callback() {
            @Override
            public void countBacking(long time) {
                toutiaoInfoPopwindow.setTime((int) time);
                toutiaoLefttimeTv.setText(time + "s");
                if (time == toutiaoBean.getTimeLeft() - 10) {
                    viewToutiao.startAnimation(animationOut);
                }
            }

            @Override
            public void finish() {
                viewToutiao.setVisibility(View.GONE);
                if (toutiaoInfoPopwindow.isShowing()) {
                    toutiaoInfoPopwindow.dismiss();
                }

            }
        });
        if (!isPush) {
            mTvGoWatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RxBus.get().post(new ChangeRoomEvent(toutiaoBean.getAnchorUserId() + ""));
                }
            });

            toutiaoLeftviewLong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    toutiaoInfoPopwindow.setChannelId(channelId + "");
                    toutiaoInfoPopwindow.setPopupGravity(Gravity.CENTER);
                    toutiaoInfoPopwindow.showPopupWindow();
                }
            });
            toutiaoLeftviewShort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    toutiaoInfoPopwindow.setChannelId(channelId + "");
                    toutiaoInfoPopwindow.setPopupGravity(Gravity.CENTER);
                    toutiaoInfoPopwindow.showPopupWindow();
                }
            });
            toutiaoLefttimeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    toutiaoInfoPopwindow.setChannelId(channelId + "");
                    toutiaoInfoPopwindow.setPopupGravity(Gravity.CENTER);
                    toutiaoInfoPopwindow.showPopupWindow();
                }
            });
        }
    }


    public void showWSJ(String toutiaoBean, int id) {
        toutiaoContent.setText(toutiaoBean);
        toutiaoLeftviewLong.setVisibility(View.VISIBLE);
        toutiaoLeftviewShort.setVisibility(View.GONE);
        viewToutiao.setVisibility(View.VISIBLE);
        toutiaoLefttimeTv.setVisibility(View.GONE);
        toutiao_im_tt.setVisibility(View.GONE);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.toutian_in_left);
        Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.toutian_out_right);
        animationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                toutiaoLeftviewLong.setVisibility(View.GONE);
                toutiaoLeftviewShort.setVisibility(View.VISIBLE);
                mTvGoWatch.setVisibility(View.GONE);
                toutiaoLefttimeTv.setVisibility(View.VISIBLE);
                mIsShowLong = false;


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        viewToutiao.startAnimation(animation);     //从左侧进入直播间
        if (toutiaoCountback != null) {
            toutiaoCountback.destory();
            toutiaoCountback = null;
        }
        if (null == toutiaoCountback)
            toutiaoCountback = new CountBackUtils();
        toutiaoCountback.countBack(3, new CountBackUtils.Callback() {
            @Override
            public void countBacking(long time) {
                toutiaoInfoPopwindow.setTime((int) time);
                toutiaoLefttimeTv.setText(time + "s");
                if (time == 20 - 10) {
                    viewToutiao.startAnimation(animationOut);
                }
            }

            @Override
            public void finish() {
                viewToutiao.setVisibility(View.GONE);
                if (toutiaoInfoPopwindow.isShowing()) {
                    toutiaoInfoPopwindow.dismiss();
                }

            }
        });
        if (!isPush) {
            mTvGoWatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RxBus.get().post(new ChangeRoomEvent(id + ""));
                }
            });

        }
    }

    GiftRootLayout.GiftRootListener giftViewListener;

    public void showGiftAnimation(GiftBean bean) {
//        if (bean.isToutiao) {
//            return;
//        }
        giftRoot.setVisibility(View.VISIBLE);
        if (null == giftViewListener) {
            giftViewListener = new GiftRootLayout.GiftRootListener() {
                @Override
                public void showGiftInfo(GiftBean giftBean) {
                }

                @Override
                public void showGiftAmin(GiftBean giftBean, int index) {
                    if (giftBean == null)
                        return;
                    if (!TextUtils.isEmpty(giftBean.bigName)) {
                        return;
                    }
                    Random random = new Random();
                    int giftImgWidth = (int) getResources().getDimension(R.dimen.dp_55);
                    int giftLeftMargin = (int) getResources().getDimension(R.dimen.dp_125);
                    int length = giftBean.group;
                    if (length == 1) {

                    } else if (length <= 100) {
                        length = 5;
                    } else if (length < 1000) {
                        length = 10;
                    } else {
                        length = 15;
                    }
                    for (int i = 0; i < length; i++) {
                        if (rlGiftAnim.getChildCount() > 60) {
                            return;
                        }
                        ImageView view = new ImageView(BaseLiveingRoomActivity.this);
                        view.setId(i);
                        int dp250 = (int) getResources().getDimension(R.dimen.dp_180);
                        int dp58 = (int) getResources().getDimension(R.dimen.dp_58);
                        int topMargin = dp250 + dp58 * (index - 1);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(giftImgWidth, giftImgWidth);
                        params.topMargin = topMargin;
                        params.leftMargin = giftLeftMargin;
                        view.setLayoutParams(params);
                        ImageLoader.loadImg(view, giftBean.getGiftImage(), R.drawable.trans_bg);
                        int xDelta = random.nextInt(ScreenUtils.getScreenWidth(BaseLiveingRoomActivity.this) - giftImgWidth) - giftLeftMargin;
                        int yDelta = random.nextInt(topMargin - giftImgWidth) - topMargin;
                        AnimatorSet animatorSet = new AnimatorSet();
                        ObjectAnimator translationAnimalX = ObjectAnimator.ofFloat(view, "translationX", 0, xDelta);
                        translationAnimalX.setInterpolator(new AccelerateInterpolator());
                        translationAnimalX.setDuration(2000);
                        ObjectAnimator translationAnimalY = ObjectAnimator.ofFloat(view, "translationY", 0, yDelta);
                        translationAnimalY.setInterpolator(new AccelerateInterpolator());
                        translationAnimalY.setDuration(2000);
                        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 1, 0.5f, 0.2f, 0);
                        alphaAnimator.setDuration(2000);
                        animatorSet.play(translationAnimalX).with(translationAnimalY).before(alphaAnimator);
                        animatorSet.start();
                        animatorSet.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                rlGiftAnim.removeView(view);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        rlGiftAnim.addView(view);
                    }
                }

                @Override
                public void hideGiftAmin(int index) {
                    rlGiftAnim.removeAllViews();
                }
            };
        }
        giftRoot.setPlayGiftEndListener(giftViewListener);
        if (!bean.isToutiao) {
            giftRoot.loadGift(bean);
//            return;
        }

        if (!isShowAnimate) {
            return;
        }
        L.e("gifttttttttttt", bean.group + "--------" + bean.price);
//        if (PrivilegeEffectUtils.getInstance().isShowBigGift(bean.group * bean.price)) {
//
//        }
        if (!TextUtils.isEmpty(bean.bigName) && bean.isPlay) {
            if (bean.userid.equals(getUserInfo().id + "")) {
                myGiftList.offer(bean);
            } else {
                bigGiftList.offer(bean);
            }
            //     giftHandle.sendEmptyMessage(1);
        }

    }

    public void teamPkError(int userId, int msgId) {
        if (msgId == EventConstant.KAFKA_MSG_ID_GROUP_PK_ERROR_TO_OBJECT) {
            toastTip("由于您的对手掉线，PK结束");
            return;
        }
        for (int i = 0; i < matchPkResultUtil.getUserTeamInfo().size(); i++) {
            if (userId == matchPkResultUtil.getUserTeamInfo().get(i).getUserId()) {
                if (msgId == EventConstant.KAFKA_MSG_ID_GROUP_PK_ING_ERROR) {
                    toastTip(matchPkResultUtil.getUserTeamInfo().get(i).getNickname() + "掉线！");
                } else {
                    toastTip(matchPkResultUtil.getUserTeamInfo().get(i).getNickname() + "离开房间！");
                }
                return;
            }
        }
        for (int i = 0; i < matchPkResultUtil.getOtherTeamInfo().size(); i++) {
            if (userId == matchPkResultUtil.getOtherTeamInfo().get(i).getUserId()) {
                if (msgId == EventConstant.KAFKA_MSG_ID_GROUP_PK_ING_ERROR) {
                    toastTip(matchPkResultUtil.getUserTeamInfo().get(i).getNickname() + "掉线！");
                } else {
                    toastTip(matchPkResultUtil.getUserTeamInfo().get(i).getNickname() + "离开房间！");
                }
                return;
            }
        }
    }

    public PkContributePopwindow pkContributePopwindow;

    public void showPkControbutePop(int id, boolean isHost, boolean isWe) {
        pkContributePopwindow = new PkContributePopwindow(this, id, isHost, isWe);
        pkContributePopwindow.setOnPkContributeListener(new PkContributePopwindow.OnPkContributeListener() {
            @Override
            public void onSend() {
                if(null==giftPopWindow){
                    p.getGift(false, 0);
                }else {
                    giftPopWindow.showPopupWindow();
                }



            }

            @Override
            public void onGiftClick(int giftId) {

                if(null==giftPopWindow){
                    p.getGift(false, giftId);
                }else {
                    giftPopWindow.showPopupWindow();
                    giftPopWindow.setGiftId(giftId);
                }


            }
        });
        pkContributePopwindow.showPopupWindow();
    }

    protected void startCheckPkEnd() {
        stopCheckPkEndSchedule();//关闭pk线程池
        endPkScheduleService = Executors.newScheduledThreadPool(1);
        endPkScheduleService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {//10秒刷新一次pk状态
                try {
                    p.checkPkType(channelId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10, TimeUnit.SECONDS);

    }

    //关闭线程池 好像是刷新pk状态用的
    protected void stopCheckPkEndSchedule() {
        if (endPkScheduleService != null) {
            endPkScheduleService.shutdownNow();
        }
        endPkScheduleService = null;


    }

    public void setSingleMatch(HttpResult<PunishTimeDto> result, boolean b) throws
            JSONException {
    }

    public void sendGiftSuccess(GiftDto giftDto, int num) {
    }

    public void closeLivePush(CloseLiveDto closeLiveDto, JumpInvtationDataBean jumpInvtationDataBean) {
    }

    public void setSingleMatchResult(MatchTeamResult result) {
    }

    public void startPk() {

    }

    public void endPk() {

    }

    public void updatePkTime(PkTimeDto dto) {
    }

    public void updatePkGiftScore(PkScoreDto dto) {
    }

    public void setPkerror(String error) {
    }

    public void cancleMatchSinglePK() {
    }

    public void addAttentionSuccess() {
    }

    public void kickedOut() {

    }

    public void liveEnd(CustomLiveEndDto customLiveEndDto) {
    }

    public void bannedHost(String title) {

    }

    public void setCalorific(int calorific) {

    }

    public void showReceivePkMsg(InvitePkMsg invitePkMsg, boolean isSingle) {
    }

    public void teamRandomMatch(List<RoomMemberChangeMsg> result) {
    }

    public void setTeamMatchResult(MatchTeamResult matchResult) {
    }

    public void teamInvitePkAccept(InvitePkMsg invitePkMsg) {
    }

    public void createRoomSuccess(String roomId) {
    }

    public void roomMemberChange(List<RoomMemberChangeMsg> list) {
    }

    public void setNewRoomInfo(EnterLivingRoomDto dto) {
    }

    public void isAttention(String attention) {

    }

    public void changeRoomInfo(EnterLivingRoomDto data) {

    }

    public void addQipao() {
        if (!isShowAnimate) return;
        qipaoView.addFavor();
        qipaoView.addFavor();
    }

    public void hostLeave() {
    }

    public void hostComeBack() {
    }

    protected void showShareDialog() {
        ShareCodeDialog dialog = new ShareCodeDialog();
        dialog.setOnItemClickListener(new ShareCodeDialog.OnItemClickListener() {
            @Override
            public void WeiXinShare() {
                p.getRoomInfo(String.valueOf(channelId), SHARE_MEDIA.WEIXIN);
            }

            @Override
            public void WeiXinCircleShare() {
                p.getRoomInfo(String.valueOf(channelId), SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        });
        dialog.show(getSupportFragmentManager(), "share");
    }

    @Override
    public void share(ChannelShareInfoDto data, SHARE_MEDIA shareMedia) {
        if (!CommentUtils.isWeixinAvilible(this)) {
            ToastUtils.showShort(this, "您没有安装微信！！！");
            return;
        }
        UMWeb web = new UMWeb(data.shareUrl);
        web.setTitle(data.shareTitle);
        web.setDescription(data.shareContent);
        if (!TextUtils.isEmpty(data.shareImg))
            web.setThumb(new UMImage(this, data.shareImg));
        else web.setThumb(new UMImage(this, getUserInfo().headImg));
        new ShareAction(this)
                .setPlatform(shareMedia)//传入平台
                .withMedia(web)
                .setCallback(this)//回调监听器
                .share();
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }


    @Override
    public void onResult(SHARE_MEDIA share_media) {
        ToastUtils.showShort(this, "分享成功");
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        ToastUtils.showShort(this, "分享失败");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }

    public abstract UserInfo getUserInfo();
    public abstract GiftPopWindow.OnGiftSendListener getGiftListener();

    @Override
    public void pkSurrender() {
        toastTip("对方认输，我方获得胜利");
    }

    //
//    LoadingNoCancleDialog dialog;
//
    @Override
    public void showNoCancleLoading() {
    }

    //
    @Override
    public void hideNoCancleLoading() {
    }

    @Override
    public void warnHost(String msg) {

    }

    public AnchorWishPop anchorWishPop;

    @Override
    public synchronized void updateActivityScore(LivingRoomBannerDto dto, int msgId) {
        L.e("=====刷新Banner");
        actBanner.setBanner(dto.getActBanner(isPush));
        actBanner.setOnActBannerItemCliakListenr(new OnActBannerItemCliakListenr() {
            @Override
            public void integralJump(String url) {
                if (activityPopwindow != null) {
                    activityPopwindow.onDestroy();
                    activityPopwindow = null;
                }
                activityPopwindow = new ActivityPopwindow(BaseLiveingRoomActivity.this, url, channelId, false);
                activityPopwindow.showPopupWindow();
            }

            @Override
            public void wishJump(int wishStatus) {

                anchorWishPop = new AnchorWishPop(BaseLiveingRoomActivity.this, isPush);

                if (wishStatus == 1) {//已许愿
                    if (dto.wishInfo == null || dto.wishInfo.size() == 0) return;
                    anchorWishPop.setWishData(dto.wishInfo);
                    if (isPush) {//主播查看心愿单
                        anchorWishPop.setType(2);
                        anchorWishPop.showPopupWindow();
                    } else {//用户查看主播心愿单
                        anchorWishPop.setType(3);
                        anchorWishPop.addClickListener(BaseLiveingRoomActivity.this);
                        anchorWishPop.showPopupWindow();
                    }
                } else {//未许愿
                    anchorWishPop.setType(0);
                    anchorWishPop.showPopupWindow();
                }
            }

            @Override
            public void signJump(int id) {
//                    giftPopWindow.showPopupWindow();
//                    giftPopWindow.selectGiftById();
            }

            @Override
            public void normalActJump(String url) {
                if (activityPopwindow != null) {
                    activityPopwindow.onDestroy();
                    activityPopwindow = null;
                }
                activityPopwindow = new ActivityPopwindow(BaseLiveingRoomActivity.this, url, channelId, false);
                activityPopwindow.showPopupWindow();
            }

            @Override
            public void actTips(String content) {

            }

            @Override
            public void panishJump(int boxid) {
                planePopWindow = new PlanePopWindow(BaseLiveingRoomActivity.this, channelId);
                planePopWindow.setOnSendGiftListener(new PlanePopWindow.OnSendGiftListener() {
                    @Override
                    public void onSendGift(int giftId) {
                        if(null==giftPopWindow){
                            p.getGift(false, giftId);
                        }else {
                            giftPopWindow.showPopupWindow();
                            giftPopWindow.setGiftId(giftId);
                        }

                    }
                });
                planePopWindow.showPopupWindow();
            }

            @Override
            public void showPlaneMp4(int boxid) {
                //  addPlaneMp4ToQueue(boxid);
            }

            @Override
            public void openBox(String url) {
                if (activityPopwindow != null) {
                    activityPopwindow.onDestroy();
                    activityPopwindow = null;
                }
                activityPopwindow = new ActivityPopwindow(BaseLiveingRoomActivity.this, url, channelId, false, true);
                activityPopwindow.showPopupWindow();
            }

            @Override
            public void actWSJ() {
                p.getActivityInfo(channelId);

            }

            @Override
            public void actRedBoxRain(int type) {
                addActivtiyMp4ToQueue(type);
                p.UpActivity(channelId);
            }

            @Override
            public void endActivitiy() {
                endActivity = true;
                if (null != activityCb) {
                    activityCb.destory();
                    activityCb = null;
                }
                //    p.UpActivity(channelId);
            }

            @Override
            public void actSDJ(ActBannerBean bannerBean) {
                setActivitySdj(bannerBean);
            }

            @Override
            public void actSDJRedBoxRain() {
                addPlaneMp4ToQueue();
                p.UpActivity(channelId);
            }

            @Override
            public void actNewYear(ActBannerBean bannerBean) {
                ARouter.getInstance().build(ArouterApi.WISHING_WALL_ACTION).navigation();
            }


        });

    }

    public void setActivitySdj(ActBannerBean bannerBean) {
        if (null == bottomSheetDialog) {
            bottomSheetDialog = new BottomSheetDialog(BaseLiveingRoomActivity.this);
        }
        if (!bottomSheetDialog.isShowing()) {
            bottomSheetDialog.setContentView(R.layout.bottom_activity_sdj);
            bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet)
                    .setBackgroundResource(android.R.color.transparent);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.setCanceledOnTouchOutside(true);
            bottomSheetDialog.show();
            TextView tv_zs = bottomSheetDialog.findViewById(R.id.tv_zs);
            if (isPush) {
                tv_zs.setVisibility(View.INVISIBLE);
            }
            ImageView im_gift = bottomSheetDialog.findViewById(R.id.im_gift);

            TextView tv_gift_num = bottomSheetDialog.findViewById(R.id.tv_gift_num);

            TextView tv_activity_one_name = bottomSheetDialog.findViewById(R.id.tv_activity_one_name);
            TextView tv_activity_one_num = bottomSheetDialog.findViewById(R.id.tv_activity_one_num);

            ImageView im_one = bottomSheetDialog.findViewById(R.id.im_one);
            ImageView im_two = bottomSheetDialog.findViewById(R.id.im_two);
            ImageView im_three = bottomSheetDialog.findViewById(R.id.im_three);

            TextView tv_activity_two_name = bottomSheetDialog.findViewById(R.id.tv_activity_two_name);
            TextView tv_activity_two_num = bottomSheetDialog.findViewById(R.id.tv_activity_two_num);

            TextView tv_activity_three_name = bottomSheetDialog.findViewById(R.id.tv_activity_three_name);
            TextView tv_activity_three_num = bottomSheetDialog.findViewById(R.id.tv_activity_three_num);
            if (null != bannerBean.refreshProcesses) {


                for (int i = 0; i < bannerBean.refreshProcesses.size(); i++) {
                    RefreshProcessesBean refreshProcessesBean = bannerBean.refreshProcesses.get(i);
                    if (bannerBean.castleSort == i) {
                        ImageLoader.loadImg(im_gift, refreshProcessesBean.getIcon());
                        tv_gift_num.setText(refreshProcessesBean.getCustomsValue().toString() + "/" + refreshProcessesBean.getCustomsTotalValue());
                    }
                    switch (i) {
                        case 0:
                            ImageLoader.loadImg(im_one, refreshProcessesBean.getIcon());
                            tv_activity_one_name.setText(refreshProcessesBean.getGiftName());
                            tv_activity_one_num.setText((refreshProcessesBean.getCustomsValue().toString() + "/" + refreshProcessesBean.getCustomsTotalValue()));
                            break;
                        case 1:
                            ImageLoader.loadImg(im_two, refreshProcessesBean.getIcon());
                            tv_activity_two_name.setText(refreshProcessesBean.getGiftName());
                            tv_activity_two_num.setText((refreshProcessesBean.getCustomsValue().toString() + "/" + refreshProcessesBean.getCustomsTotalValue()));
                            break;
                        case 2:
                            ImageLoader.loadImg(im_three, refreshProcessesBean.getIcon());
                            tv_activity_three_name.setText(refreshProcessesBean.getGiftName());
                            tv_activity_three_num.setText((refreshProcessesBean.getCustomsValue().toString() + "/" + refreshProcessesBean.getCustomsTotalValue()));
                            break;
                    }
                }
            }

            SVGAImageView im_sds = bottomSheetDialog.findViewById(R.id.im_sds_big);
            SvgaUtils svgaUtils = new SvgaUtils(this, im_sds, 0);
            svgaUtils.initAnimator();
            svgaUtils.repeat();
            if (bannerBean.isFinish) {
                svgaUtils.startAnimator("lv_3");
            } else {
                if (bannerBean.castleSort > 2) {
                    svgaUtils.startAnimator("lv_3");
                } else
                    switch (bannerBean.castleSort) {
                        case 0:
                            svgaUtils.startAnimator("lv_0");
                            im_sds.setTag("lv_" + bannerBean.castleSort);
                            break;
                        case 1:
                            svgaUtils.startAnimator("lv_1");
                            im_sds.setTag("lv_" + bannerBean.castleSort);
                            break;
                        case 2:
                            svgaUtils.startAnimator("lv_2");
                            im_sds.setTag("lv_" + bannerBean.castleSort);
                            break;
                        case 3:
                            svgaUtils.startAnimator("lv_3");
                            im_sds.setTag("lv_" + bannerBean.castleSort);
                            break;
                    }
            }


            SVGAImageView im_sds_back = bottomSheetDialog.findViewById(R.id.im_sds_back);
            SvgaUtils svgaUtil = new SvgaUtils(this, im_sds_back, 0);
            svgaUtil.initAnimator();
            svgaUtil.repeat();
            svgaUtil.startAnimator("down");

            tv_zs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();

                    // giftPopWindow.selectGiftById();
                    if (bannerBean.refreshProcesses.size() > 0) {
                        // initGift(bannerBean.refreshProcesses.get(bannerBean.castleSort).getGiftId());
                        if(null==giftPopWindow){
                                p.getGift(false, bannerBean.refreshProcesses.get(bannerBean.castleSort).getGiftId());
                        }else {
                            giftPopWindow.showPopupWindow();
                            giftPopWindow.setGiftId(bannerBean.refreshProcesses.get(bannerBean.castleSort).getGiftId());
                        }


                    }

                }
            });
        }
    }

    public void addActivtiyMp4ToQueue(int index) {
//        GiftBean giftBean = new GiftBean();
//        switch (index) {
//            case 0:
//                giftBean.bigName = "halloween1.mp4";
//                break;
//            case 1:
//                giftBean.bigName = "halloween2.mp4";
//                break;
//            case 2:
//                giftBean.bigName = "halloween3.mp4";
//                break;
//        }
//
//        giftBean.id = index;
//        giftBean.channelId = channelId;
//        bigGiftList.offer(giftBean);
    }

    public void addPlaneMp4ToQueue() {
//        GiftBean giftBean = new GiftBean();
//        giftBean.bigName = "ChristmasTrees_Max.mp4";
//        giftBean.id = boxid;
//        giftBean.channelId = channelId;
//        bigGiftList.offer(giftBean);


        SvgaUtils svgaUtils = new SvgaUtils(this, svga_animation);
        svga_animation.setVisibility(View.VISIBLE);
        svgaUtils.initAnimator(new SVGACallback() {
            @Override
            public void onStep(int i, double v) {

            }

            @Override
            public void onRepeat() {

            }

            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                svga_animation.setVisibility(View.GONE);
                if (null != fallingLayout) {
                    fallingLayout.setUserInfo(channelId, 0);//红包雨
                    fallingLayout.addFallingBody(280);
                }

            }
        });
        //   svgaUtils.repeat();
        svgaUtils.startAnimator("lv_max");


//        if (null != fallingLayout) {
//            fallingLayout.setUserInfo(channelId, 0);//红包雨
//            fallingLayout.addFallingBody(280);
//        }

    }
//
//    public void addFirstKillMp4Queue(int boxid) {
//        GiftBean giftBean = new GiftBean();
//        giftBean.bigName = "First_kill.mp4";
//        giftBean.channelId = channelId;
//        bigGiftList.offer(giftBean);
//    }

    @Override
    public void updateActivityScore(PkMatchScoreDto pkMatchScoreDto) {
        if (actBanner == null) return;
        actBanner.updateActivityScore(pkMatchScoreDto);
    }

    @Override
    public void updateBannerData(ActBannerBean actBannerBean) {
        if (actBannerBean.type == 4 && actBannerBean.planeOpenSecond > 0 && actBannerBean.planeOpenSecond <= 30 && !actBannerBean.userIsOpen) {
            //   addPlaneMp4ToQueue(actBannerBean.boxId);
            actBannerBean.planeOpenSecond = 0;
        } else if (actBannerBean.type == 4 && actBannerBean.planeOpenSecond > 30 && !isPush) {
            actBannerBean.planeOpenSecond -= 30;
        }
        actBannerBean.isPush = isPush;
        if (actBanner == null) return;
        actBanner.updateBannerData(actBannerBean);


    }

    @Override
    public void fillWish(int giftId) {
        //用户端重写改方法实现赠送礼物效果
    }


    @Override
    public void activityNoStart() {

    }

    @Override
    public void activityReward(EnterLivingRoomPkActivityDto activityDto) {

    }

    @Override
    public void lastActivityReward(LastFrameDto activityDto) {

    }

    @Override
    public void activityIng(String notice, String back) {
    }

    @Override
    public void updatePkRank(MatchTeamResult matchTeamResult) {

    }

    @Override
    public void normalActivityIng(String notice, int id, String color, String name, String noticeBack) {

    }

    @Override
    public void normalActivityReward(EnterLivingRoomActDto normalActDto) {

    }

    @Override
    public void updateNewYear(Long userGoodBean) {
        if (userGoodBean > 0) {//如果大于0 就显示view
            if (userGoodBean / 1000 > 10) {//如果大于10 显示倒计时
                ll_view_new.setVisibility(View.VISIBLE);
                //      tv_sd_lw_new.setText("");
                tv_sd_lw_new.setFactory(new ViewSwitcher.ViewFactory() {
                    @Override
                    public View makeView() {
                        final TextView tv = new TextView(BaseLiveingRoomActivity.this);
                        tv.setTextSize(8);
                        //     tv.setBackgroundResource(R.drawable.pic_newyear_zw);
                        tv.setTextColor(Color.parseColor("#ffffff"));
                        tv.setGravity(Gravity.CENTER);
                        return tv;
                    }
                });
                setTimeNewYear(userGoodBean / 1000);

            } else {//小于10播放动画
                addNewYearMp4ToQueue(0);
            }


        } else {
            ll_view_new.setVisibility(View.GONE);
        }

    }

    boolean istype = true;
    boolean isCf = true;

    private void setTimeNewYear(final long time) {
        tv_sd_lw_new.setCurrentText("零点全网烟花盛宴");
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeWhile(statusInfo -> isCf)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //     KLog.e("tag", aLong + "" + (aLong % 4 == 0));
                        if (aLong % 4 == 0) {
                            istype = !istype;
                            if (istype) {
                                //      KLog.i("yang", "---------------------------");
                                tv_sd_lw_new.setText("零点全网烟花盛宴");
                            } else {
                                //        KLog.i("yang", "+++++++++++++++++++++++++++");
                                tv_sd_lw_new.setText(TimeUtils.l2String(time - aLong));

                            }
                        } else {
                            if (istype) {

                            } else {
                                tv_sd_lw_new.setCurrentText(TimeUtils.l2String(time - aLong));
                                //    tv_sd_lw_new.setCurrentText("零点全网烟花盛宴");
                            }
                        }
                        if (time - aLong == 11) {
                            isCf = false;
                            ll_view_new.setVisibility(View.GONE);
                            addNewYearMp4ToQueue(0);
                        }

                    }
                });
    }

    public void addNewYearMp4ToQueue(int index) {


//        GiftBean giftBean = new GiftBean();
//        giftBean.bigName = "Countdown_RGBA.mp4";
//        giftBean.id = index;
//        giftBean.channelId = channelId;
//        bigGiftList.offer(giftBean);
//
//        GiftBean giftBeans = new GiftBean();
//        giftBeans.bigName = "Happy_New_Year_RGBA.mp4";
//        giftBeans.id = index;
//        giftBeans.channelId = channelId;
//        bigGiftList.offer(giftBeans);


    }

    @Override
    public void activityCpIMG(String cpRank) {
        im_cp.setVisibility(View.VISIBLE);
        if (Integer.parseInt(cpRank) <= 0 && Integer.parseInt(cpRank) < 11) {
            int res = ImageLoader.getResId("pic_cp" + cpRank, R.drawable.class);
            im_cp.setImageResource(res);
        }


    }

    @Override
    public void startActivity() {
        p.UpActivity(channelId);
    }
}
