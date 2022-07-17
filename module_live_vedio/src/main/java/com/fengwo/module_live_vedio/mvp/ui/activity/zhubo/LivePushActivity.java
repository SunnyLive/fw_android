package com.fengwo.module_live_vedio.mvp.ui.activity.zhubo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.faceunity.entity.Effect;
import com.faceunity.ui.adapter.EffectRecyclerAdapter;
import com.faceunity.ui.control.BeautyControlView;
import com.faceunity.ui.entity.BeautyParameterModel;
import com.faceunity.ui.entity.EffectEnum;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.JumpInvtationDataBean;
import com.fengwo.module_comment.bean.UserLevelBean;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.event.BannedEvent;
import com.fengwo.module_comment.event.PaySuccessEvent;
import com.fengwo.module_comment.event.RedPacketCountEvent;
import com.fengwo.module_comment.iservice.AttentionService;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.DownloadHelper;
import com.fengwo.module_comment.utils.EPSoftKeyBoardListener;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.KeyboardStateObserver;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.utils.SafeHandle;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_comment.widget.NetWorkState;
import com.fengwo.module_comment.widget.NetWorkTextView;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.Constants;
import com.fengwo.module_live_vedio.eventbus.ChangeRoomEvent;
import com.fengwo.module_live_vedio.eventbus.EndPkEvent;
import com.fengwo.module_live_vedio.eventbus.SendGiftToUserEvent;
import com.fengwo.module_live_vedio.eventbus.ShowRechargePopEvent;
import com.fengwo.module_live_vedio.helper.LiveNoticeHelper;
import com.fengwo.module_live_vedio.mvp.UserGoodBean;
import com.fengwo.module_live_vedio.mvp.dto.CloseLiveDto;
import com.fengwo.module_live_vedio.mvp.dto.FloatScreenBean;
import com.fengwo.module_live_vedio.mvp.dto.GetActivityInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;
import com.fengwo.module_live_vedio.mvp.dto.GuardListDto;
import com.fengwo.module_live_vedio.mvp.dto.LivePushMoreDto;
import com.fengwo.module_live_vedio.mvp.dto.MatchTeamResult;
import com.fengwo.module_live_vedio.mvp.dto.MyHourDto;
import com.fengwo.module_live_vedio.mvp.dto.PacketCountBean;
import com.fengwo.module_live_vedio.mvp.dto.PendantDto;
import com.fengwo.module_live_vedio.mvp.dto.PendantListDto;
import com.fengwo.module_live_vedio.mvp.dto.PkRankDto;
import com.fengwo.module_live_vedio.mvp.dto.PkRankMember;
import com.fengwo.module_live_vedio.mvp.dto.PkResultDto;
import com.fengwo.module_live_vedio.mvp.dto.PkScoreDto;
import com.fengwo.module_live_vedio.mvp.dto.PkTimeDto;
import com.fengwo.module_live_vedio.mvp.dto.PunishTimeDto;
import com.fengwo.module_live_vedio.mvp.dto.QuickTalkDto;
import com.fengwo.module_live_vedio.mvp.dto.RechargeDto;
import com.fengwo.module_live_vedio.mvp.dto.RoomMemberChangeMsg;
import com.fengwo.module_live_vedio.mvp.dto.StartLivePushDto;
import com.fengwo.module_live_vedio.mvp.dto.StickersDto;
import com.fengwo.module_live_vedio.mvp.dto.WatcherDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.ActivityInfoAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.PkSingleAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.PkTeamAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.WatchersAdapter;
import com.fengwo.module_live_vedio.mvp.ui.df.InviteTeamPKDialogFragment;
import com.fengwo.module_live_vedio.mvp.ui.df.LiveProfitDF;
import com.fengwo.module_live_vedio.mvp.ui.df.MapDialogF;
import com.fengwo.module_live_vedio.mvp.ui.df.PKGroupMatchingDialog;
import com.fengwo.module_live_vedio.mvp.ui.df.PKGroupResultDialog;
import com.fengwo.module_live_vedio.mvp.ui.df.ReceivePkInviteDF;
import com.fengwo.module_live_vedio.mvp.ui.dialog.RedPackageDialog;
import com.fengwo.module_live_vedio.mvp.ui.dialog.redpackresult.RedPackResultDialog;
import com.fengwo.module_live_vedio.mvp.ui.event.ChangeAnimateEvent;
import com.fengwo.module_live_vedio.mvp.ui.iview.ILivingRoom;
import com.fengwo.module_live_vedio.mvp.ui.iview.SoftKeyboardFixerForFullscreen;
import com.fengwo.module_live_vedio.mvp.ui.pop.ActivityPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.CloseLivePushPop;
import com.fengwo.module_live_vedio.mvp.ui.pop.GiftPopWindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.InviteHostPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.LiveHourRankPopWindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.LiveManagerPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.LivePushMorePopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.MakeExpansionPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.NewUserInfoPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.PendantListPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.PeoplePopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.PkSurrenderPop;
import com.fengwo.module_live_vedio.mvp.ui.pop.RechargePopWindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.ShouhuListPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.SingleMatchPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.SoundEffectPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.StartPkPopwindow;
import com.fengwo.module_live_vedio.utils.PkAnimaUtil;
import com.fengwo.module_live_vedio.utils.ViewGosn;
import com.fengwo.module_live_vedio.widget.DragView;
import com.fengwo.module_live_vedio.widget.PendantLayout;
import com.fengwo.module_live_vedio.widget.giftlayout.bean.NoticeBean;
import com.fengwo.module_live_vedio.widget.redpack.FallingAdapter;
import com.fengwo.module_live_vedio.widget.redpack.FallingView;
import com.fengwo.module_live_vedio.widget.redpack.RedPackIconView;
import com.fengwo.module_websocket.bean.InvitePkMsg;
import com.fengwo.module_websocket.bean.LivingRoomTextMsg;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

/**
 * @author chenshanghui
 * @intro 主播端
 * @date 2019/10/4
 */
public class LivePushActivity extends BaseLivePushActivity implements ILivingRoom, View.OnClickListener, PendantLayout.IAddListListener {
    private final static int[] resShouhuBg = {R.drawable.live_ic_shouhu1, R.drawable.live_ic_shouhu2, R.drawable.live_ic_shouhu3};
    public static final String LIVE_PUSH_DATA = "live_push_data";
    public static final String LIVE_PUSH_PK_DATA = "live_push_pk_data";
    public static final String BEAUTY_DATA = "beauty_data";

    @Autowired
    UserProviderService service;
    @Autowired
    AttentionService attentionService;

    @BindView(R2.id.pusher_tx_cloud_view)
    TXCloudVideoView pushView;
    @BindView(R2.id.iv_live_push_bottom_pk)
    ImageView ivLivePushBottomPk;
    @BindView(R2.id.iv_live_push_bottom_mssage)
    ImageView ivLivePushBottomMssage;
    @BindView(R2.id.iv_live_push_bottom_manager)
    ImageView ivLivePushBottomManager;
    @BindView(R2.id.iv_live_push_bottom_more)
    ImageView ivLivePushBottomMore;
    @BindView(R2.id.topview)
    View topView;
    @BindView(R2.id.view_bottom)
    View view_bottom;
    @BindView(R2.id.view_bottom_input)
    View view_bottom_input;

    @BindView(R2.id.view_bottom_notice)
    View view_bottom_notice;
    @BindView(R2.id.et_input_notice)
    EditText et_input_notice;
    @BindView(R2.id.btn_send_notice)
    TextView btn_send_notice;


    @BindView(R2.id.btn_send)
    ImageView btn_send;

    @BindView(R2.id.im_shousha)
    ImageView im_shousha;


    @BindView(R2.id.et_input)
    EditText etInput;
    @BindView(R2.id.tv_quick_msg)
    ImageView tvQuickMsg;

    @BindView(R2.id.sw_danmu)
    Switch swDanmu;


    @BindView(R2.id.iv_close)
    ImageView ivClose;
    @BindView(R2.id.pk_other_view)
    TXCloudVideoView txCloudPullView;
    @BindView(R2.id.rv_chat)
    RecyclerView rvChat;
    @BindView(R2.id.layout_pk_progress)
    View layoutPkProgress;
    //主播信息
    @BindView(R2.id.iv_header)
    ImageView ivHead;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.btn_attention)
    TextView btnAttention;
    //美颜view
    @BindView(R2.id.fl_faceunity)
    FrameLayout flFaceUnity;


    //展示pk结果
    @BindView(R2.id.layout_pk_result)
    protected Group layoutPkResult;

    @BindView(R2.id.tv_calorific)
    TextView tvCalorific;
    @BindView(R2.id.tv_anchor_level)
    TextView mTvAnchorLevel;  //主播等级
    @BindView(R2.id.iv_anchor_level)
    ImageView mIvAnchorLevel;

    @BindView(R2.id.ll_other_attention)
    LinearLayout ll_other_attention;
    @BindView(R2.id.iv_pk_result_left)
    ImageView ivPkResultLeft;
    @BindView(R2.id.iv_pk_result_right)
    ImageView ivPkResultRight;
    @BindView(R2.id.iv_live_push_bottom_daoju)
    ImageView iv_live_push_bottom_daoju;

    @BindView(R2.id.tv_hour_rank)
    LinearLayout tvHourRank;
    @BindView(R2.id.tv_hour_my)
    TextView tv_hour_my;
    @BindView(R2.id.rl_view)
    RelativeLayout rl_view;

    @BindView(R2.id.tv_network)
    NetWorkTextView mTvNetWork;


    private SafeHandle handle;//线程调度 pk结果
    private ImageView btnShouhu;//守护按钮
    private TextView tvGuardNum;
    private FrameLayout flShouhu;
    private CircleImageView ivShouhuHeader;


    private RecyclerView rvWatchers;
    private WatchersAdapter watchersAdapter;
    private TextView tvWatcherNum;
    PeoplePopwindow watcherNumPop;

    private List<FrameLayout> shouhuBgs;
    private List<CircleImageView> shouhuHeaders;

    private StartLivePushDto startLivePushDto;

    private LivePushMorePopwindow morePopwindow; //更多
    private MakeExpansionPopwindow makeExpansionPopwindow; //主播的更多
    private SoundEffectPopwindow soundEffectPopwindow; //音效
    private StartPkPopwindow startPkPopwindow;//pk入口弹窗
    private SingleMatchPopwindow singleMatchPopwindow;//单人开始匹配
    private InviteTeamPKDialogFragment teamPKDialogFragment;//邀请团队pk
    private InviteHostPopwindow inviteHostPopwindow;//可邀请pk列表弹窗
    private PKGroupMatchingDialog pkGroupMatchingDialog;//团队开始匹配弹窗
    private RechargePopWindow rechargePopWindow;//充值弹窗
    private PendantListPopwindow pendantListPopwindow;//贴纸弹框


    //        private MatchSingleResult matchSingleResult; //单人匹配成功回调数据
    private volatile boolean isSingle = true;//是否单人pk
    private boolean isSticker = false;//是否使用贴纸
    private boolean isFirstIn = true;
    private boolean isDanmu = false;
    protected BeautyControlView mBeautyControlView; //美颜view
    protected ViewStub mBottomViewStub;//美颜 占位viewupdatePkRank
    protected ViewStub mEffectViewStub;//道具\手势 占位view
    private EffectRecyclerAdapter mEffectRecyclerAdapter; //道具adapter
    WindowManager wm;
    //创建及执行
    ScheduledExecutorService startPkScheduleService;//查询是否开始pk的轮询

    //活动h5 pop
    ActivityPopwindow activityPopwindow;
    private ViewGosn viewGosn;
    private float downX;
    private PendantLayout pendantLayout = null;
    private int[] myFirstOne;
    private int[] youFirstOne;
    private RedPackIconView redpacket;
    private FallingView fallingView;
    private RedPackResultDialog redPackResultDialog;


    @Override
    protected TXCloudVideoView getPushView() {
        return pushView;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_live_push;
    }

    @Override
    protected boolean getImmersionBar() {
        return false;
    }

    @SuppressLint({"CheckResult", "HandlerLeak"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        startLivePushDto = getIntent().getParcelableExtra(LivePushActivity.LIVE_PUSH_DATA);

        MatchTeamResult matchTeamResult = (MatchTeamResult) getIntent().getSerializableExtra(LIVE_PUSH_PK_DATA);
        channelId = startLivePushDto.getChannelId();
        if (startLivePushDto.getColorInfo() != null)
            p.setRoomId(String.valueOf(channelId), startLivePushDto.getColorInfo().getNickname_color(), startLivePushDto.getColorInfo().getUser_color(), startLivePushDto.getColorInfo().getNickname_bg_color(),
                    startLivePushDto.getColorInfo().getSystem_notice_color(), startLivePushDto.getColorInfo().getBullet_screen());
        roomManager = 2;
        isPush = true;
        p.joinGroupIM(getIMGroupId(startLivePushDto.getChannelId()), 0, 0, 0, true, "");
        startRtmpPush(startLivePushDto.getPushUrl());
        danmakuView = findViewById(R.id.dmk_show_danmu);
        rlToutiaoView = findViewById(R.id.rl_toutiao_view);
        im_cp = findViewById(R.id.im_cp);
        if (null != startLivePushDto.getActivityCpInfoBean() && null != startLivePushDto.getActivityCpInfoBean().getCpRank()) {
            cpRank = startLivePushDto.getActivityCpInfoBean().getCpRank();
            activityCpIMG(cpRank);
        }

        llEnterActivity = findViewById(R.id.ll_enter_activity);
        actBanner = findViewById(R.id.act_banner);
        ivAngel = findViewById(R.id.iv_angel);
        tvAngel = findViewById(R.id.tv_angel);
        ivLevel = findViewById(R.id.iv_level);
        tv_djs = findViewById(R.id.tv_djs);
        fl_back = findViewById(R.id.fl_back);
        tv_monitor = findViewById(R.id.tv_monitor);
        tvQuickMsg.setVisibility(View.GONE);
        initDanmu();
        initShouhu();
        initPopwindow();
        initPKAnimView();
        initAnimationView();
        initEnterAnimation();
        initGiftView();
        initToutiao();
        initActivity();
        initEvent();
        setWatchers(new ArrayList<>());
        setTopView();
        set();
        p.getGuardList(startLivePushDto.getChannelId());
        p.getRecharge();
        p.getZbStickers();
        p.getTouTiaoIfExist();
        p.getGift(false, 0);
        p.getActivityMsg(startLivePushDto.getChannelId());
        ivClose.setVisibility(View.VISIBLE);
        //开启弹幕监听
        swDanmu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isDanmu = b;
                if (b) etInput.setHint("已开启弹幕模式,10钻/条");
                else etInput.setHint("说点什么吧");
            }
        });
        RxBus.get().toObservable(ShowRechargePopEvent.class)
                .compose(bindToLifecycle())
                .subscribe(showRechargePopEvent -> {
                    if (null != rechargePopWindow) {
                        rechargePopWindow.showPopupWindow();
                    }
                });
        RxBus.get().toObservable(PaySuccessEvent.class)
                .compose(bindToLifecycle())
                .subscribe(paySuccess -> {
                    updateWallet();
                });
//跳转到i撩直播间 （sb需求 开这播 还能去看直播 平生第一次遇到这种需求）
        RxBus.get().toObservable(JumpInvtationDataBean.class)
                .compose(bindToLifecycle())
                .subscribe(dataBean -> {
                    p.closeLivePush(dataBean);
                });
        handle = new SafeHandle(this) {
            @Override
            protected void mHandleMessage(Message msg) {
                super.mHandleMessage(msg);
                if (msg.what == 1) {
                    updateWallet();
                }
            }
        };
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (startLivePushDto.getPkStatus() > 0 && matchTeamResult != null) {
                    reConnection(matchTeamResult);
                    getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
//                    //重新设置聊天列表的高度
//                    ViewGroup.LayoutParams layoutParams = rvChat.getLayoutParams();
//                    int [] locBottom = new int[2];
//                    int [] locDm = new int[2];
//                    view_bottom.getLocationOnScreen(locBottom);
//                    danmakuView.getLocationOnScreen(locDm);
//                    layoutParams.height = locBottom[1]-danmakuView.getHeight()-locDm[1];//高度控制在弹幕 和 底部之间
//                    rvChat.setLayoutParams(layoutParams);
            }
        });
        initBeautiView();
        initRedPacket();
        allRxbus.add(RxBus.get().toObservable(ChangeRoomEvent.class)
                .compose(bindToLifecycle())
                .subscribe(new Consumer<ChangeRoomEvent>() {
                    @Override
                    public void accept(ChangeRoomEvent changeRoomEvent) throws Exception {
                        toastTip("当前你正直播中，无法去TA的直播间。");
                    }
                }));
        allRxbus.add(RxBus.get().toObservable(SendGiftToUserEvent.class)
                .compose(bindToLifecycle())
                .subscribe(new Consumer<SendGiftToUserEvent>() {
                    @Override
                    public void accept(SendGiftToUserEvent sendGiftToUserEvent) throws Exception {
                        if (null != giftPopWindow) {
                            acceptGiftUserId = sendGiftToUserEvent.getUid();
                            acceptGiftUsername = sendGiftToUserEvent.getUserName();
                            giftPopWindow.setIsWish(false);
                            giftPopWindow.setOnGiftSendListener(new GiftPopWindow.OnGiftSendListener() {
                                @Override
                                public void onGiftSend(GiftDto gift, int num) {
                                    UserInfo userInfo = service.getUserInfo();
                                    if (gift.giftPrice * num > userInfo.balance) {
                                        if (noMoneyDialog == null || !noMoneyDialog.isShowing()) {
                                            noMoneyDialog = DialogUtil.showNoMoneyDialog(LivePushActivity.this,
                                                    new DialogUtil.AlertDialogBtnClickListener() {
                                                        @Override
                                                        public void clickPositive() {
                                                            if (null != rechargePopWindow) {
                                                                rechargePopWindow.showPopupWindow();
                                                            }
                                                        }

                                                        @Override
                                                        public void clickNegative() {

                                                        }
                                                    });
                                        }
                                    } else {
                                        if (!checkGiftEnable(gift)) {
                                            showLoadingDialog();
                                            DownloadHelper.downloadGift(gift.giftSwf, new DownloadHelper.DownloadListener() {
                                                @Override
                                                public void completed() {
                                                    int id;
                                                    boolean toUser = false;
                                                    if (acceptGiftUserId > 0) {
                                                        id = acceptGiftUserId;
                                                        p.sendGiftToUser(id, gift, num, acceptGiftUsername, channelId);
                                                    }
                                                    LivePushActivity.this.hideLoadingDialog();
                                                }

                                                @Override
                                                public void failed() {
                                                    LivePushActivity.this.toastTip("礼物下载失败，请检查您的网络状况！！！");
                                                    LivePushActivity.this.hideLoadingDialog();
                                                }

                                                @Override
                                                public void onProgress(int progress) {
                                                    LivePushActivity.this.setDialogProgressPercent(progress + "%");
                                                }
                                            });
                                        } else {
                                            int id;
                                            boolean toUser = false;
                                            if (acceptGiftUserId > 0) {
                                                id = acceptGiftUserId;
                                                p.sendGiftToUser(id, gift, num, acceptGiftUsername, channelId);
                                            }
                                        }
                                    }
                                    if (null != newUserInfoPopwindow && newUserInfoPopwindow.isShowing()) {
                                        giftPopWindow.dismiss();
                                        newUserInfoPopwindow.dismiss();

                                    }
                                }
                            });
                            giftPopWindow.showPopupWindow();
                        }
                    }
                }));
        KeyboardStateObserver.getKeyboardStateObserver(this).
                setKeyboardVisibilityListener(new KeyboardStateObserver.OnKeyboardVisibilityListener() {
                    @Override
                    public void onKeyboardShow() {

                    }

                    @Override
                    public void onKeyboardHide() {
                        et_input_notice.clearFocus();
                    }
                });
        SoftKeyboardFixerForFullscreen.assistActivity(this);
        initExoPlay();
    }

    //初始化红包
    private void initRedPacket() {
        redpacket = findViewById(R.id.v_redpack_icon);
        redpacket.attachRoom(startLivePushDto.getChannelId());
        redpacket.setOnPlayFalling(packetCountBean -> {
            playRedPackFalling(packetCountBean);
            return null;
        });
        LinearLayout noticeContainer = findViewById(R.id.v_notice_container);
        //初始化消息通知
        LiveNoticeHelper.INSTANCE.setNoticeContainer(this, noticeContainer);
        LiveNoticeHelper.INSTANCE.getLiveNoticeConfig().setChannelId(channelId);
        LiveNoticeHelper.INSTANCE.getLiveNoticeConfig().setLiving(true);
    }

    private void initBeautiView() {
        BeautyParameterModel.init(this);
        mBottomViewStub = (ViewStub) findViewById(R.id.fu_base_bottom);
        mBottomViewStub.setInflatedId(R.id.fu_base_bottom);
        mEffectViewStub = (ViewStub) findViewById(R.id.fu_effect_bottom);
        mEffectViewStub.setInflatedId(R.id.fu_effect_bottom);
        //美颜
        mBottomViewStub.setLayoutResource(R.layout.layout_fu_beauty);
        mBeautyControlView = (BeautyControlView) mBottomViewStub.inflate();
        mBeautyControlView.setOnFUControlListener(mFURenderer);
        //道具 、手势
        mEffectViewStub.setLayoutResource(R.layout.layout_fu_effect);
        mEffectViewStub.inflate();
    }

    private void initGiftView() {
        giftRoot = findViewById(R.id.gift_root);
        rlGiftAnim = findViewById(R.id.gift_anim_rl);
        qipaoView = findViewById(R.id.live_view);
        viewAnnoucement = findViewById(R.id.view_annoucement);
        tvAnnouncement = findViewById(R.id.announcement_tv);
        im_announcement = findViewById(R.id.im_announcement);
        tvAnnouncementGo = findViewById(R.id.announcement_tv_go);   //礼物去围观
        noticeAnnoucement = findViewById(R.id.view_notice);
        tvNoticeAnnouncement = findViewById(R.id.tv_notice_title);

    }

    AlertDialog noMoneyDialog;
    private int acceptGiftUserId = -1;
    private String acceptGiftUsername = "";

    private boolean checkGiftEnable(GiftDto gift) {
        if (TextUtils.isEmpty(gift.giftSwf)) {
            return true;
        }
        return DownloadHelper.isAlreadyDownload(gift.giftSwf);
    }


    private void initEvent() {

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btn_send.setImageResource(R.drawable.pic_ends);
                } else {
                    btn_send.setImageResource(R.drawable.pic_end);
                }
            }
        });


    }
    private ArrayList<UserLevelBean> userLevelBeans = new ArrayList<>();

    private void getUserLevelFromRaw() {
        try {
            InputStream is = getResources().openRawResource(R.raw.gift_level);
            userLevelBeans = new Gson().fromJson(readTextFromSDcard(is), new TypeToken<List<UserLevelBean>>() {
            }.getType());
            System.out.println("-=-=---" + new Gson().toJson(userLevelBeans));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();
    }

    @Override
    public void setGifts(List<List<GiftDto>> allGifts, boolean isUpLevelTip, int id) {
        if (null == giftPopWindow) {
            getUserLevelFromRaw();
            UserInfo userInfo = service.getUserInfo();
            giftPopWindow = new GiftPopWindow(this, allGifts, userLevelBeans, userInfo,true);
            giftPopWindow.setBalance(service.getUserInfo());
            if (isUpLevelTip)
                giftPopWindow.setCheckUpLevel();
            giftPopWindow.setOnGiftSendListener(new GiftPopWindow.OnGiftSendListener() {
                @Override
                public void onGiftSend(GiftDto gift, int num) {
                    UserInfo userInfo = service.getUserInfo();
                    if (gift.giftPrice * num > userInfo.balance) {
                        if (noMoneyDialog == null || !noMoneyDialog.isShowing()) {
                            noMoneyDialog = DialogUtil.showNoMoneyDialog(LivePushActivity.this,
                                    new DialogUtil.AlertDialogBtnClickListener() {
                                        @Override
                                        public void clickPositive() {
                                            if (null != rechargePopWindow) {
                                                rechargePopWindow.showPopupWindow();
                                            }
                                        }

                                        @Override
                                        public void clickNegative() {

                                        }
                                    });
                        }
                    } else {
                        if (!checkGiftEnable(gift)) {
                            showLoadingDialog();
                            DownloadHelper.downloadGift(gift.giftSwf, new DownloadHelper.DownloadListener() {
                                @Override
                                public void completed() {
                                    int id;
                                    boolean toUser = false;
                                    if (acceptGiftUserId > 0) {
                                        id = acceptGiftUserId;
                                        p.sendGiftToUser(id, gift, num, acceptGiftUsername, channelId);
                                    }
                                    LivePushActivity.this.hideLoadingDialog();
                                }

                                @Override
                                public void failed() {
                                    LivePushActivity.this.toastTip("礼物下载失败，请检查您的网络状况！！！");
                                    LivePushActivity.this.hideLoadingDialog();
                                }

                                @Override
                                public void onProgress(int progress) {
                                    LivePushActivity.this.setDialogProgressPercent(progress + "%");
                                }
                            });
                        } else {
                            int id;
                            boolean toUser = false;
                            if (acceptGiftUserId > 0) {
                                id = acceptGiftUserId;
                                p.sendGiftToUser(id, gift, num, acceptGiftUsername, channelId);
                            }
                        }
                    }
                }
            });
            if (!isFirstIn) {
                giftPopWindow.setIsWish(false);
                giftPopWindow.showPopupWindow();
            }
            isFirstIn = false;
        }
    }

    private void initEnterAnimation() {
        //进入直播间飘屏 控件
        llEnter = findViewById(R.id.ll_enter);
        tvBayMsg = findViewById(R.id.tv_bay_msg);

        ivEnterUserLevel = findViewById(R.id.iv_enter_user_level);
        ivEnterVipLevel = findViewById(R.id.iv_enter_vip_level);
        tvEnterMsg = findViewById(R.id.tv_enter_msg);
        ivEnterGuard = findViewById(R.id.iv_enter_guard);
        ivEnterRoommanager = findViewById(R.id.iv_enter_roommanager);
        rlEnterBack = findViewById(R.id.rl_enter_back);
        imEnterPic = findViewById(R.id.im_enter_pic);
    }

    private void initToutiao() {
        viewToutiao = findViewById(R.id.view_toutiao);
        toutiaoLefttimeTv = findViewById(R.id.toutiao_lefttime_tv);
        toutiaoLefttimeView = findViewById(R.id.toutiao_lefttime_view);
        toutiaoUseravatar = findViewById(R.id.toutiao_useravatar);
        toutiaoGift2 = findViewById(R.id.toutiao_gift2);
        toutiaoGiftNum2 = findViewById(R.id.toutiao_gift_num2);
        toutiaoLeftviewShort = findViewById(R.id.toutiao_leftview_short);
        toutiaoGift = findViewById(R.id.toutiao_gift);
        toutiaoGiftNum = findViewById(R.id.toutiao_gift_num);
        toutiaoGiftview = findViewById(R.id.toutiao_giftview);
        toutiaoContent = findViewById(R.id.toutiao_content);
        toutiaoLeftviewLong = findViewById(R.id.toutiao_leftview_long);

        toutiao_im_tt = findViewById(R.id.toutiao_im_tt);
        toutiaoGift = findViewById(R.id.toutiao_gift);
        mTvGoWatch = findViewById(R.id.tv_go_watch);   //去围观


    }

    private void initActivity() {
        ll_view_new = findViewById(R.id.ll_view_new);
        im_sd_lw_new = findViewById(R.id.im_sd_lw_new);
        tv_sd_lw_new = findViewById(R.id.tv_sd_lw_new);
    }

    private void reConnection(MatchTeamResult matchTeamResult) {
        //断线重连
        boolean isPkTime;
        if (null != matchTeamResult) {
            if (matchTeamResult.getMembers() != null && matchTeamResult.getMembers().size() > 0) {
                setTeamMatchResult(matchTeamResult);
            } else {
                setSingleMatchResult(matchTeamResult);
            }
            if (matchTeamResult.getPkPoint() != null)
                updatePkGiftScore(matchTeamResult.getPkPoint());
            if (matchTeamResult.getPkTime() == 0) {//惩罚时间进入直播间
                PkResultDto pkResultDto = new PkResultDto();
                pkResultDto.setResult(matchTeamResult.getUserResult());
                try {
                    pkResultDto.getSinglePkResultDto().userInfo = matchTeamResult.userInfo;
                    pkResultDto.getSinglePkResultDto().objectInfo = matchTeamResult.objectInfo;
                } catch (NullPointerException e) {

                }

                showPkResult(pkResultDto, true);//如果是惩罚时间进入直播间，暂时客户端只展示当前直播间胜负，不展示团队胜负
                isPkTime = false;
            } else {
                isPkTime = true;
            }
            p.setPkId(matchTeamResult.getPkId(), isPkTime);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        RxBus.get().toObservable(EndPkEvent.class).subscribe(new Consumer<EndPkEvent>() {
            @Override
            public void accept(EndPkEvent endPkEvent) throws Exception {
                endPk();
            }
        });
        EPSoftKeyBoardListener.setListener(this, new EPSoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
//                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_bottom_input.getLayoutParams();
//                params.bottomMargin = height;
//                view_bottom_input.setLayoutParams(params);
            }

            @Override
            public void keyBoardHide(int height) {
//                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_bottom_input.getLayoutParams();
//                params.bottomMargin = 0;
//                view_bottom_input.setLayoutParams(params);
                view_bottom.setVisibility(View.VISIBLE);
                view_bottom_input.setVisibility(View.GONE);
                view_bottom_notice.setVisibility(View.GONE);
                rvChat.scrollToPosition((rvChat.getAdapter()).getItemCount() - 1);

            }
        });

    }


    private void initAnimationView() {
        frameAnimationView = findViewById(R.id.animationView);
        svga_pk = findViewById(R.id.svga_pk);

        svga_animation = findViewById(R.id.svga_animation);
        //  tvCarMsg = findViewById(R.id.tv_car_msg);
    }

    private void initShouhu() {

        tvWatcherNum = findViewById(R.id.tv_watchers_num);
        rvWatchers = findViewById(R.id.rv_watchers);
        btnShouhu = findViewById(R.id.btn_shouhu);
        tvGuardNum = findViewById(R.id.tv_guard_num);
        flShouhu = findViewById(R.id.fl_out_shouhu_people);
        ivShouhuHeader = findViewById(R.id.iv_out_shouhu_header);
        //购买守护特效
        viewBuyGuard = findViewById(R.id.view_buyguard);
        ivBuyGuard = findViewById(R.id.iv_guard);
        tvBuyGuardUserName = findViewById(R.id.tv_buy_guard_username);
        tvBuyGuardName = findViewById(R.id.tv_buy_guardname);
        //
    }

    @Override
    public void setGuardWindow(int total, ArrayList<GuardListDto.Guard> records) {
        super.setGuardWindow(total, records);
        if (CommentUtils.isListEmpty(records)) {
            tvGuardNum.setVisibility(View.INVISIBLE);
            btnShouhu.setVisibility(View.GONE);
            flShouhu.setVisibility(View.VISIBLE);
        } else {
            btnShouhu.setVisibility(View.GONE);
            flShouhu.setVisibility(View.VISIBLE);
            ImageLoader.loadImg(ivShouhuHeader, records.get(0).guardUserHeadImg);
            if (total == 0) {
                tvGuardNum.setVisibility(View.INVISIBLE);
            } else {
                tvGuardNum.setText(total + "");
                tvGuardNum.setVisibility(View.VISIBLE);
            }

        }
    }

    private void setTopView() {
        ImageLoader.loadCircleImg(ivHead, startLivePushDto.getHeadImg());
        tvName.setText(startLivePushDto.getNickname());
//        tvAttention.setText(startLivePushDto.getLookNums() + "");
        tvHourRank.setOnClickListener(this);
        int level = service.getUserInfo().liveLevel;   //主播等级
        mIvAnchorLevel.setImageResource(ImageLoader.getAnchorLevel(level));
        mTvAnchorLevel.setText("LV." + level);
        btnAttention.setVisibility(View.GONE);
    }

    private void initPopwindow() {
        setShouhuListPop();//守护列表
//        setMeiyanPopwindowLisenter();
        setMorePopwindow();  //更多
        setMakeExpansionPopwindow();  //主播更多
        setStartPkPopwindow();  //pk
        setSoundPop();//音效


        setSingleMatchPop();
//        setTeamInvitePkPop();
    }

    private void setPendantListPopwindow() {
        if (null != pendantListDtoList && null != pendantListPopwindow) {
            pendantListPopwindow.showPopupWindow();
        } else
            p.getPendantList();
//        if(null==pendantListPopwindow){
//            pendantListPopwindow = new PendantListPopwindow(this);
//        }
    }

    PendantListDto pendantListDtoList;
    boolean is_one;

    @Override
    public void setPendantList(PendantListDto data) {
        pendantListDtoList = data;
        pendantListPopwindow = new PendantListPopwindow(this, pendantListDtoList);
        pendantListPopwindow.showPopupWindow();
        pendantListPopwindow.setOnItemClickListener(new PendantListPopwindow.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, String pendantListDto, int id, boolean type, int length, String textcolor) {
                //   mPendantLayout.setVisibility(View.VISIBLE);
                if (null == pendantLayout) {
                    pendantLayout = new PendantLayout(LivePushActivity.this);
                    pendantLayout.setImgView(pendantListDto, id, length);
                    pendantLayout.setEdittext(type, textcolor);
                    is_one = false;
                    fl_back.addView(pendantLayout);
                    DragView rl_drag_container = pendantLayout.findViewById(R.id.rl_drag_container);
                    rl_drag_container.post(new Runnable() {
                        @Override
                        public void run() {
                            int[] position = new int[2];
                            rl_drag_container.getLocationOnScreen(position);
                            moveUpLoaction(id, position[0], position[1], "");
                        }
                    });

                    pendantLayout.setIDeleteListener(LivePushActivity.this);
                } else {
                    pendantLayout.setImgView(pendantListDto, id, length);
                    pendantLayout.setEdittext(type, textcolor);
                    DragView rl_drag_container = pendantLayout.findViewById(R.id.rl_drag_container);
                    rl_drag_container.post(new Runnable() {
                        @Override
                        public void run() {
                            int[] position = new int[2];
                            rl_drag_container.getLocationOnScreen(position);
                            moveUpLoactionEdAdd(id, position[0], position[1], "");
                        }
                    });
                }


            }
        });
    }


    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    int pendantId = -1;

    @Override
    public void getPendant(HttpResult<PendantDto> b) {
        pendantId = b.data.getId();
    }

    @Override
    public void getStickers(HttpResult<List<StickersDto>> b) {
        if (b.data.size() > 0) {
            int sun = b.data.size() - 1;
            Log.e("tag", "/////////////////////////");
            p.setUpsticker(b.data.get(sun).getId() + "", b.data.get(sun).getStickerId() + "", b.data.get(sun).getStickerUrl(), b.data.get(sun).getStickerText(), b.data.get(sun).getStickerLocation(), b.data.get(sun).getStickerType(), b.data.get(sun).getTextColor());
        }
    }


    private void setSoundPop() {
        soundEffectPopwindow = new SoundEffectPopwindow(this);
        soundEffectPopwindow.setOnItemClickListener(new SoundEffectPopwindow.OnItemClickListener() {

            @Override
            public void onItemClick(String livePushMoreDto) {
                switch (livePushMoreDto) {
                    case "0":

                        mLivePusher.setVoiceChangerType(TXLiveConstants.REVERB_TYPE_0);
                        break;
                    case "KTV":

                        mLivePusher.setVoiceChangerType(TXLiveConstants.REVERB_TYPE_1);
                        break;
                    case "小房间":

                        mLivePusher.setVoiceChangerType(TXLiveConstants.REVERB_TYPE_2);
                        break;
                    case "大会堂":

                        mLivePusher.setVoiceChangerType(TXLiveConstants.REVERB_TYPE_3);
                        break;
                    case "低沉":

                        mLivePusher.setVoiceChangerType(TXLiveConstants.REVERB_TYPE_4);
                        break;
                    case "洪亮":

                        mLivePusher.setVoiceChangerType(TXLiveConstants.REVERB_TYPE_5);
                        break;
                    case "磁性":

                        mLivePusher.setVoiceChangerType(TXLiveConstants.REVERB_TYPE_6);
                        break;
                }
                if (livePushMoreDto.equals("0")) {
                    morePopwindow.setSelete(false);
                } else {
                    morePopwindow.setSelete(true);
                }
            }
        });
    }

    private void setMakeExpansionPopwindow() {
        iv_live_push_bottom_daoju.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                iv_live_push_bottom_daoju.getLocationOnScreen(location);
                makeExpansionPopwindow = new MakeExpansionPopwindow(LivePushActivity.this, isMirror, location[0]);
                makeExpansionPopwindow.setOnItemClickListener(new MakeExpansionPopwindow.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                        if (position >= 0 && position <= 2) {
                            flFaceUnity.setVisibility(View.VISIBLE);
                        } else {
                            flFaceUnity.setVisibility(View.GONE);
                        }
                        if (position == 0) {
                            mEffectViewStub.setVisibility(View.GONE);
                            mBottomViewStub.setVisibility(View.VISIBLE);
                        } else if (position == 1) {
                            mBottomViewStub.setVisibility(View.GONE);
                            mEffectViewStub.setVisibility(View.VISIBLE);
                            setEffectData(6);
                        } else if (position == 2) {
                            mBottomViewStub.setVisibility(View.GONE);
                            mEffectViewStub.setVisibility(View.VISIBLE);
                            setEffectData(1);
                        } else if (position == 3) {
                            setPendantListPopwindow();
//                            morePopwindow.dismiss();
//                            showShareDialog();
                        } else if (position == 4) {
                            //是否开启观众端镜像观看
                            mLivePusher.setMirror(!isMirror);
                            SPUtils1.put(LivePushActivity.this, "isMirror", !isMirror);
                            isMirror = !isMirror;
                        }
                    }
                });
            }
        });

    }

    //房间dialogF
    private void setTeamInvitePkPop(List<RoomMemberChangeMsg> roomMemberChangeMsgs) {
        teamPKDialogFragment = (InviteTeamPKDialogFragment) InviteTeamPKDialogFragment.getInstance(service.getUserInfo().id, roomMemberChangeMsgs);
        teamPKDialogFragment.setOnAddRandomTeamPkListener(new InviteTeamPKDialogFragment.OnAddRandomTeamPkListener() {
            @Override
            public void onAddRandomTeamPk(String teamId) {
                p.teamRandomPk(teamId, 1);
            }
        });
        teamPKDialogFragment.setOnInviteTeamListener(new InviteTeamPKDialogFragment.OnInviteTeamListener() {
            @Override
            public void onInviteTeam(String teamId) {
                if (inviteHostPopwindow == null) {
                    inviteHostPopwindow = new InviteHostPopwindow(LivePushActivity.this, p, false, teamId);
                } else {
                    inviteHostPopwindow.setIsSingle(false);
                }
                if (!inviteHostPopwindow.isShowing())
                    inviteHostPopwindow.show(LivePushActivity.this, teamId);
            }
        });
        if (!teamPKDialogFragment.isVisible() && !teamPKDialogFragment.isAdded() && !teamPKDialogFragment.isRemoving())
            teamPKDialogFragment.showDF(getSupportFragmentManager(), InviteTeamPKDialogFragment.class.getName());
    }

    //刷新房间成员数据
    private void refreshTeamInvitePkPop(List<RoomMemberChangeMsg> roomMemberChangeMsgs) {
        if (teamPKDialogFragment == null) {
            setTeamInvitePkPop(roomMemberChangeMsgs);
        } else {
            teamPKDialogFragment.addData(roomMemberChangeMsgs);
            if (!teamPKDialogFragment.isVisible() && !teamPKDialogFragment.isAdded() && !teamPKDialogFragment.isRemoving())
                teamPKDialogFragment.showDF(getSupportFragmentManager(), InviteTeamPKDialogFragment.class.getName());
        }
    }

    private void setShouhuListPop() {
        if (null == shouhuListPopwindow) {
            shouhuListPopwindow = new ShouhuListPopwindow(this, true, 0);
        }
    }

    private void setSingleMatchPop() {
        singleMatchPopwindow = new SingleMatchPopwindow((Activity) this, startLivePushDto.getHeadImg());
        singleMatchPopwindow.setOnSingleMatchListener(new SingleMatchPopwindow.OnSingleMatchListener() {
            @Override
            public void onCancelMatch(boolean isRandom) {
                if (isRandom) { //单人随机
                    p.cancleMatchSinglePk();
                } else {
                    p.cancelInviteFriend();
                }
            }
        });
    }

    private void setStartPkPopwindow() {
        startPkPopwindow = new StartPkPopwindow(this);
        startPkPopwindow.setOnStartPkListener(new StartPkPopwindow.OnStartPkListener() {
            @Override
            public void onSingleRandom() {
                isSingle = true;
                p.startSingleRandomPk();
            }

            @Override
            public void onSingleInvite() {
                isSingle = true;
                if (inviteHostPopwindow == null) {
                    inviteHostPopwindow = new InviteHostPopwindow(LivePushActivity.this, p, true, null);
                } else {
                    inviteHostPopwindow.setIsSingle(true);
                }
                inviteHostPopwindow.show(LivePushActivity.this);
            }

            @Override
            public void onTeamRandom() {
                isSingle = false;
                p.groupRandomSingleAdd(1);
            }

            @Override
            public void onTeamInvite() {
                isSingle = false;
                p.createRoom(1);
            }
        });

    }

    private boolean open_lamp = false; //是否打开闪光灯

    private boolean open_silence = false; //是否静音
    private boolean open_clearactivity = false; //是否清屏

    private boolean open_animate = false; //是否开启动画
    private boolean open_flip = false; //是否开启动画

    private void setMorePopwindow() {
        List<LivePushMoreDto> livePushMore = Arrays.asList(LivePushMoreDto.values());
        morePopwindow = new LivePushMorePopwindow(this, livePushMore);
        morePopwindow.setOnItemClickListener(new LivePushMorePopwindow.OnItemClickListener() {
            @Override
            public void onItemClick(LivePushMoreDto live, int position) {

                if (live == LivePushMoreDto.动画) {
                    open_animate = !open_animate;
                    ToastUtils.showShort(LivePushActivity.this, open_animate ? "开启动画" : "不使用动画");
                    RxBus.get().post(new ChangeAnimateEvent(open_animate));
                    livePushMore.get(position).setOpen(open_animate);
                } else if (live == LivePushMoreDto.发消息) {
                    showSendMsgEdit("");
                    morePopwindow.dismiss();
                } else if (live == LivePushMoreDto.分享) {
                    showShareDialog();
                    morePopwindow.dismiss();
                } else if (live == LivePushMoreDto.反转) {
                    open_flip = !open_flip;
                    mLivePusher.switchCamera();
                    livePushMore.get(position).setOpen(open_flip);
                } else if (live == LivePushMoreDto.清屏) {
                    livingMsgAdapter.clearData();
                    morePopwindow.dismiss();
                } else if (live == LivePushMoreDto.镜像开) {
                    mLivePusher.setMirror(!isMirror);
                    SPUtils1.put(LivePushActivity.this, "isMirror", !isMirror);
                    isMirror = !isMirror;
                    livePushMore.get(position).setOpen(isMirror);
                    ToastUtils.showShort(LivePushActivity.this, isMirror ? "镜像模式开启，观众看到的画面和你是一致的了~" : "镜像模式关闭，观众看到的画面和你是相反的了~");
                } else if (live == LivePushMoreDto.闪光灯) {
                    open_lamp = !open_lamp;
                    mLivePusher.turnOnFlashLight(open_lamp);
                    ToastUtils.showShort(LivePushActivity.this, open_lamp ? "打开闪光灯" : "关闭闪光灯");
                    livePushMore.get(position).setOpen(open_lamp);
                } else if (live == LivePushMoreDto.音效) {
                    soundEffectPopwindow.showPopupWindow();
                    morePopwindow.dismiss();

                } else if (live == LivePushMoreDto.声音) {
                    open_silence = !open_silence;
                    mLivePusher.setMute(open_silence);
                    ToastUtils.showShort(LivePushActivity.this, open_silence ? "声音关闭" : "声音打开");
                    livePushMore.get(position).setOpen(open_silence);
                } else if (live == LivePushMoreDto.直播间公告) {
                    showNoticeEdit(noticeMsg);
                    morePopwindow.dismiss();
                } else if (live == LivePushMoreDto.流量监控) {
                    if (livePushMore.get(position).isOpen()) {
                        tv_monitor.setVisibility(View.GONE);
                        livePushMore.get(position).setOpen(false);
                    } else {
                        tv_monitor.setVisibility(View.VISIBLE);
                        livePushMore.get(position).setOpen(true);
                    }
                    morePopwindow.dismiss();
                }
//                if (position >= 0 && position <= 2) {
//                    flFaceUnity.setVisibility(View.VISIBLE);
//                } else {
//                    flFaceUnity.setVisibility(View.GONE);
//                }
//                if (position == 0) {
//                    mEffectViewStub.setVisibility(View.GONE);
//                    mBottomViewStub.setVisibility(View.VISIBLE);
//                } else if (position == 1) {
//                    mBottomViewStub.setVisibility(View.GONE);
//                    mEffectViewStub.setVisibility(View.VISIBLE);
//                    setEffectData(1);
//                } else if (position == 2) {
//                    mBottomViewStub.setVisibility(View.GONE);
//                    mEffectViewStub.setVisibility(View.VISIBLE);
//                    setEffectData(6);
//                } else if (position == 3) {
//                    morePopwindow.dismiss();
//                    showShareDialog();
//                } else if (position == 4) {
//                    //是否开启观众端镜像观看
//                    mLivePusher.setMirror(!isMirror);
//                    SPUtils1.put(LivePushActivity.this, "isMirror", !isMirror);
//                    isMirror = !isMirror;
//                }
            }
        });
    }

    public void set() {
        viewGosn = new ViewGosn();

        fl_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //将按下时的坐标存储
                        downX = x;
                        break;
                    case MotionEvent.ACTION_MOVE:

                        float dx = x;

                        if (dx > downX) {
                            //   KLog.e("view","隐藏"+mBtnSwitchMic.getVisibility() );
                            if (topView.getVisibility() == View.VISIBLE) {
                                viewGosn.Gosn(open_clearactivity, topView);
                                viewGosn.Gosn(open_clearactivity, view_bottom);
                                viewGosn.Gosn(open_clearactivity, rvChat);
                                viewGosn.Gosn(open_clearactivity, rl_view);
                                viewGosn.Gosn(open_clearactivity, tv_djs);
                                viewGosn.Gosn(open_clearactivity, tvHourRank);
                                viewGosn.Gosn(open_clearactivity, mTvNetWork);

                                open_clearactivity = !open_clearactivity;
                            }
                        }
                        if (dx < downX) {
                            if (topView.getVisibility() == View.INVISIBLE) {
                                viewGosn.Gosn(open_clearactivity, topView);
                                viewGosn.Gosn(open_clearactivity, view_bottom);
                                viewGosn.Gosn(open_clearactivity, rvChat);
                                viewGosn.Gosn(open_clearactivity, rl_view);
                                viewGosn.Gosn(open_clearactivity, tv_djs);
                                viewGosn.Gosn(open_clearactivity, tvHourRank);
                                viewGosn.Gosn(open_clearactivity, mTvNetWork);
                                open_clearactivity = !open_clearactivity;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        //获取到距离差
                }
                return true;
            }
        });
    }

    private List<Effect> mSSEffects;
    private List<Effect> mDJEffects;
    int mSSselice = 0;
    int mDJselice = 0;

    private void setEffectData(int effectType) {
        if (effectType == 6) {
            if (null == mSSEffects) {
                mSSEffects = EffectEnum.getEffectsByEffectType(6);
            }
            RecyclerView recyclerView = findViewById(R.id.fu_effect_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(LivePushActivity.this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(mEffectRecyclerAdapter = new EffectRecyclerAdapter(
                    LivePushActivity.this, mSSEffects, mFURenderer, mSSselice, new EffectRecyclerAdapter.OnDescriptionChangeListener() {

                @Override
                public void onDescriptionChangeListener(int description) {
                    mSSselice = description;
                }
            }));
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        }
        if (effectType == 1) {
            if (null == mDJEffects) {
                mDJEffects = EffectEnum.getEffectsByEffectType(1);
            }
            RecyclerView recyclerView = findViewById(R.id.fu_effect_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(LivePushActivity.this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(mEffectRecyclerAdapter = new EffectRecyclerAdapter(LivePushActivity.this, mDJEffects, mFURenderer, mDJselice, new EffectRecyclerAdapter.OnDescriptionChangeListener() {

                @Override
                public void onDescriptionChangeListener(int description) {
                    mDJselice = description;
                }
            }));

            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        }

    }


//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//        lp.topMargin = wm.getDefaultDisplay().getWidth() / 2;
//        lp.leftMargin = (int) getResources().getDimension(R.dimen.dp_150);
//        im_shousha.setLayoutParams(lp);
//        im_shousha.setVisibility(View.VISIBLE);
//        if (isOurSide == 1) {
//            new PkAnimaUtil().start(im_shousha, myFirstOne[0], myFirstOne[1], new PkAnimaUtil.MAnimatorListener() {
//                @Override
//                public void start() {
//
//                }
//
//                @Override
//                public void end() {
//                    im_shousha.setVisibility(View.GONE);
//                }
//            });
//        } else {
//            new PkAnimaUtil().start(im_shousha, youFirstOne[0], youFirstOne[1], new PkAnimaUtil.MAnimatorListener() {
//                @Override
//                public void start() {
//
//                }
//
//                @Override
//                public void end() {
//                    im_shousha.setVisibility(View.GONE);
//                }
//            });
//        }

//        new Thread(new Runnable() {
//            public void run() {
//                //sleep设置的是时长
//                try {
//                    Thread.sleep(2000);
//                    if(isOurSide == 1){
//                        setbtnDissm(im_shousha, myFirstOne);
//                    }else {
//                        setbtnDissm(im_shousha, youFirstOne);
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();


    @Override
    public void getAddNotice(String msg, int id, String color, String name) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        TextView tv_notice_go_watch = findViewById(R.id.tv_notice_go_watch);
        tv_notice_go_watch.setVisibility(View.GONE);
        KeyBoardUtils.closeKeybord(et_input_notice, this);
        noticeMsg = msg;
        annotationNotice(msg);
//        if(null!=noticeView){
//            getEndNotice();
//        }
//        if(null==noticeView){
//            noticeView = LayoutInflater.from(this).inflate(R.layout.add_notice_view, null, false);
//            TextView tv_title = noticeView.findViewById(R.id.tv_title);
//            tv_title.setText(msg);
//            tv_title.setSingleLine(true);//设置单行显示
//            tv_title.setHorizontallyScrolling(true);//横向滚动
//            tv_title.setMarqueeRepeatLimit(1);
//            tv_title.setSelected(true);//开始滚
//            tv_title.setSingleLine(true);
//            fl_back.addView(noticeView);
//
//            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.dp_30));
//            lp.rightMargin = 32;
//            lp.leftMargin = 32;
//            lp.topMargin = 286;
//            noticeView.setLayoutParams(lp);
//            btn_send_notice.setText("删除");
//            noticeMsg = msg;
//            noticeTime = 0;
//            calculationTime = 0;
//        }

    }

    @Override
    public void getEndNotice() {
        noticeMsg = "";
    }

    @Override
    public void setUpdateExperience(Long experience) {

    }

    @Override
    public void setActivityInfo(GetActivityInfoDto getActivityInfoDto) {
        if (null == getActivityInfoDto.getProcessDtVOList() || getActivityInfoDto.getProcessDtVOList().size() == 0) {
            return;
        }
        if (null == bottomSheetDialog) {
            bottomSheetDialog = new BottomSheetDialog(LivePushActivity.this);

        }
        if (!bottomSheetDialog.isShowing()) {
            bottomSheetDialog.setContentView(R.layout.bottom_activity_wsj);
            bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet)
                    .setBackgroundResource(android.R.color.transparent);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.setCanceledOnTouchOutside(true);
            bottomSheetDialog.show();
            ImageView im_close = bottomSheetDialog.findViewById(R.id.im_close);
            im_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                }
            });
            ImageView im_gift_one = bottomSheetDialog.findViewById(R.id.im_gift_one);
            ImageView im_gift_two = bottomSheetDialog.findViewById(R.id.im_gift_two);
            ImageView im_gift_three = bottomSheetDialog.findViewById(R.id.im_gift_three);
            RelativeLayout rlOne = bottomSheetDialog.findViewById(R.id.rl_one);
            RelativeLayout rlTwo = bottomSheetDialog.findViewById(R.id.rl_two);
            RelativeLayout rlThree = bottomSheetDialog.findViewById(R.id.rl_three);
            TextView tv_gift_one = bottomSheetDialog.findViewById(R.id.tv_gift_one);
            TextView tv_gift_two = bottomSheetDialog.findViewById(R.id.tv_gift_two);
            TextView tv_gift_three = bottomSheetDialog.findViewById(R.id.tv_gift_three);
            TextView tv_sl = bottomSheetDialog.findViewById(R.id.tv_sl);


            ImageView im_status_one = bottomSheetDialog.findViewById(R.id.im_status_one);
            ImageView im_status_two = bottomSheetDialog.findViewById(R.id.im_status_two);
            ImageView im_status_three = bottomSheetDialog.findViewById(R.id.im_status_three);
            for (int i = 0; i < getActivityInfoDto.getProcessDtVOList().size(); i++) {
                switch (i) {
                    case 0:
                        ImageLoader.loadRouteImg(im_gift_one, getActivityInfoDto.getProcessDtVOList().get(i).getPic(), 4);
                        tv_gift_one.setText(getActivityInfoDto.getProcessDtVOList().get(i).getName());
                        switch (getActivityInfoDto.getProcessDtVOList().get(i).getStatus()) {//1-待开启;2-进行中;3-已完成
                            case 1:
                                im_status_one.setBackgroundResource(R.drawable.pic_activity_wsj_dkq);
                                break;
                            case 2:
                                im_status_one.setBackgroundResource(R.drawable.pic_activity_wsj_jxz);
                                break;
                            case 3:
                                im_status_one.setBackgroundResource(R.drawable.pic_activity_wsj_ywc);
                                break;
                        }

                        break;
                    case 1:
                        ImageLoader.loadRouteImg(im_gift_two, getActivityInfoDto.getProcessDtVOList().get(i).getPic(), 4);
                        tv_gift_two.setText(getActivityInfoDto.getProcessDtVOList().get(i).getName());
                        switch (getActivityInfoDto.getProcessDtVOList().get(i).getStatus()) {//1-待开启;2-进行中;3-已完成
                            case 1:
                                im_status_two.setBackgroundResource(R.drawable.pic_activity_wsj_dkq);
                                break;
                            case 2:
                                im_status_two.setBackgroundResource(R.drawable.pic_activity_wsj_jxz);
                                break;
                            case 3:
                                im_status_two.setBackgroundResource(R.drawable.pic_activity_wsj_ywc);
                                break;
                        }
                        break;
                    case 2:
                        ImageLoader.loadRouteImg(im_gift_three, getActivityInfoDto.getProcessDtVOList().get(i).getPic(), 4);
                        tv_gift_three.setText(getActivityInfoDto.getProcessDtVOList().get(i).getName());
                        switch (getActivityInfoDto.getProcessDtVOList().get(i).getStatus()) {//1-待开启;2-进行中;3-已完成
                            case 1:
                                im_status_three.setBackgroundResource(R.drawable.pic_activity_wsj_dkq);
                                break;
                            case 2:
                                im_status_three.setBackgroundResource(R.drawable.pic_activity_wsj_jxz);
                                break;
                            case 3:
                                im_status_three.setBackgroundResource(R.drawable.pic_activity_wsj_ywc);
                                break;
                        }
                        break;
                }
            }
            RecyclerView rcv_view = bottomSheetDialog.findViewById(R.id.rcv_view);
            rcv_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            ActivityInfoAdapter activityInfoAdapter = new ActivityInfoAdapter();
            rcv_view.setAdapter(activityInfoAdapter);
            activityInfoAdapter.setNewData(getActivityInfoDto.getProcessDtVOList().get(0).getGiftItemList());
            tv_sl.setText("任务收礼（" + getActivityInfoDto.getProcessDtVOList().get(0).getCurrentGiftNum() + "/" + getActivityInfoDto.getProcessDtVOList().get(0).getTargetGiftNum() + "）");
            rlOne.setBackgroundResource(R.drawable.bg_activityg_gift_select);
            rlOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activityInfoAdapter.setNewData(getActivityInfoDto.getProcessDtVOList().get(0).getGiftItemList());
                    rlOne.setBackgroundResource(R.drawable.bg_activityg_gift_select);
                    rlTwo.setBackgroundResource(0);
                    rlThree.setBackgroundResource(0);
                    tv_sl.setText("任务收礼（" + getActivityInfoDto.getProcessDtVOList().get(0).getCurrentGiftNum() + "/" + getActivityInfoDto.getProcessDtVOList().get(0).getTargetGiftNum() + "）");
                }
            });
            rlTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivityInfoDto.getProcessDtVOList().size() >= 2) {
                        activityInfoAdapter.setNewData(getActivityInfoDto.getProcessDtVOList().get(1).getGiftItemList());
                        rlOne.setBackgroundResource(0);
                        rlTwo.setBackgroundResource(R.drawable.bg_activityg_gift_select);
                        rlThree.setBackgroundResource(0);
                        tv_sl.setText("任务收礼（" + getActivityInfoDto.getProcessDtVOList().get(1).getCurrentGiftNum() + "/" + getActivityInfoDto.getProcessDtVOList().get(1).getTargetGiftNum() + "）");
                    }

                }
            });
            rlThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivityInfoDto.getProcessDtVOList().size() >= 3) {
                        activityInfoAdapter.setNewData(getActivityInfoDto.getProcessDtVOList().get(2).getGiftItemList());
                        rlOne.setBackgroundResource(0);
                        rlTwo.setBackgroundResource(0);
                        rlThree.setBackgroundResource(R.drawable.bg_activityg_gift_select);
                        tv_sl.setText("任务收礼（" + getActivityInfoDto.getProcessDtVOList().get(2).getCurrentGiftNum() + "/" + getActivityInfoDto.getProcessDtVOList().get(2).getTargetGiftNum() + "）");
                    }

                }
            });


        }
    }


    @OnClick({R2.id.iv_live_push_bottom_pk, R2.id.iv_live_push_bottom_mssage, R2.id.iv_live_push_bottom_manager, R2.id.iv_live_push_bottom_more, R2.id.iv_close,
            R2.id.btn_shouhu, R2.id.iv_live_push_bottom_gift, R2.id.iv_live_push_bottom_camera, R2.id.iv_live_push_bottom_guild_pk, R2.id.tv_send_msg, R2.id.btn_send,
            R2.id.iv_header, R2.id.tv_other_attention, R2.id.fl_faceunity, R2.id.tv_time, R2.id.fl_out_shouhu_people, R2.id.iv_live_push_bottom_daoju,
            R2.id.iv_live_push_bottom_private_chat, R2.id.btn_send_notice, R2.id.v_live_anchor_game})
    public void onClick(View v) {
        if (v.getId() == R.id.v_live_anchor_game) {//发红包
            new RedPackageDialog().show(getSupportFragmentManager(), "redPacket");
        } else if (v.getId() == R.id.iv_live_push_bottom_private_chat) {
            showSendMsgEdit("");
        } else if (v.getId() == R.id.btn_send_notice) {
            //    ToastUtils.showShort(LivePushActivity.this,"点到哦了");
            if (!TextUtils.isEmpty(et_input_notice.getText())) {
                if (btn_send_notice.getText().toString().equals("发送")) {

                    p.getNoticeAdd(et_input_notice.getText().toString());
                    KeyBoardUtils.closeKeybord(et_input_notice, this);

                } else {
                    p.getNoticeEdl();
                    getEndNotice();
                    noticeMsg = "";
                    noticeAnnoucement.setVisibility(View.GONE);
                    KeyBoardUtils.closeKeybord(et_input_notice, this);

                }

            } else {
                toastTip("请输入内容");
            }

        } else if (v.getId() == R.id.iv_live_push_bottom_daoju) {
            makeExpansionPopwindow.showPopupWindow();
        } else if (v.getId() == R.id.iv_live_push_bottom_pk) {

            startPkPopwindow.showPopupWindow();
        } else if (v.getId() == R.id.iv_live_push_bottom_mssage) {
            toastTip("功能开发中...");
        } else if (v.getId() == R.id.iv_live_push_bottom_manager) {
            LiveManagerPopwindow liveManagerPopwindow = new LiveManagerPopwindow(this, startLivePushDto.getChannelId());
            liveManagerPopwindow.showPopupWindow();
        } else if (v.getId() == R.id.iv_live_push_bottom_more) {
            morePopwindow.showPopupWindow();
        } else if (v.getId() == R.id.iv_close) {
            requestLeaveRoom();
        } else if (v.getId() == R.id.tv_watchers_num) {
            watcherNumPop = new PeoplePopwindow(this, service.getUserInfo().id, service.getUserInfo().nickname);
            watcherNumPop.showPopupWindow();
        } else if (v.getId() == R.id.fl_out_shouhu_people || v.getId() == R.id.btn_shouhu) {
            if (null == shouhuListPopwindow) {
                shouhuListPopwindow = new ShouhuListPopwindow(this, true, channelId);
            }
            p.getUserInfo(true, channelId);

            p.getGuardList(startLivePushDto.getChannelId());
        } else if (v.getId() == R.id.iv_live_push_bottom_gift) {
            LiveProfitDF liveProfitDF = (LiveProfitDF) LiveProfitDF.getInstance(startLivePushDto.getChannelId());
            liveProfitDF.show(getSupportFragmentManager(), LiveProfitDF.class.getName());
        } else if (v.getId() == R.id.iv_live_push_bottom_camera) {
            switchCamera(beautyDto.isFront);
        } else if (v.getId() == R.id.iv_live_push_bottom_guild_pk) {
            MapDialogF mapDialogF = (MapDialogF) MapDialogF.newInstance();
            mapDialogF.showDF(getSupportFragmentManager(), MapDialogF.class.getName());
        } else if (v.getId() == R.id.tv_send_msg) {
            showSendMsgEdit("");
        } else if (v.getId() == R.id.btn_send) {
            preSendMsg();
        } else if (v.getId() == R.id.iv_header) {
            //        addNewYearMp4ToQueue(1);
            //  new NewUserInfoPopwindow(this, channelId, channelId, 2).showPopupWindow();
            newUserInfoPopwindow = new NewUserInfoPopwindow(this, channelId, channelId, 2);
            newUserInfoPopwindow.showPopupWindow();
        } else if (v.getId() == R.id.tv_other_attention) {
            attentionService.addAttention(matchPkResultUtil.getOtherInfo().getUserId(), new LoadingObserver<HttpResult>() {
                @Override
                public void _onNext(HttpResult data) {
                    if (data.isSuccess()) {
                        findViewById(R.id.tv_other_attention).setVisibility(View.GONE);
                    }
                }

                @Override
                public void _onError(String msg) {

                }
            });
        } else if (v.getId() == R.id.fl_faceunity) {
            if (flFaceUnity.getVisibility() == View.VISIBLE) {
                flFaceUnity.setVisibility(View.GONE);
                BeautyParameterModel.save();
            }
        } else if (v.getId() == R.id.tv_time) {
            PkSurrenderPop pkSurrenderPop = new PkSurrenderPop(this, ((TextView) v).getText().toString());
            pkSurrenderPop.setPopupGravity(Gravity.CENTER);
            pkSurrenderPop.showPopupWindow();
        } else if (v.getId() == R.id.tv_hour_rank) {
            isClick = true;
            p.getHour(channelId);
        }
    }

    private void preSendMsg() {
        String content = etInput.getText().toString();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (isDanmu && service.getUserInfo().balance < 10) {
            KeyBoardUtils.closeKeybord(etInput, this);
            DialogUtil.showNoMoneyDialog(this, new DialogUtil.AlertDialogBtnClickListener() {
                @Override
                public void clickPositive() {
                    RxBus.get().post(new ShowRechargePopEvent());
                }

                @Override
                public void clickNegative() {
                }
            });
            return;
        }
        p.sendMsg(getIMGroupId(startLivePushDto.getChannelId()), content.trim(), isDanmu, cpRank);
        etInput.setText("");
    }

    private void showSendMsgEdit(String msg) {
        view_bottom.setVisibility(View.GONE);
        view_bottom_input.setVisibility(View.VISIBLE);
        etInput.setText(msg);
        etInput.requestFocus();
        etInput.setSelection(msg.length());
        KeyBoardUtils.openKeybord(etInput, this);
    }


    private void showNoticeEdit(String msg) {
        view_bottom.setVisibility(View.GONE);
        view_bottom_notice.setVisibility(View.VISIBLE);
        if (!noticeMsg.equals("")) {
            btn_send_notice.setText("删除");
        }
        et_input_notice.setText(msg);
        et_input_notice.requestFocus();
        et_input_notice.setSelection(msg.length());
        et_input_notice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (!TextUtils.isEmpty(et_input_notice.getText())) {
                    if (btn_send_notice.getText().toString().equals("发送")) {
                        p.getNoticeAdd(et_input_notice.getText().toString());
                    } else {
                        p.getNoticeEdl();
                        getEndNotice();
                        noticeMsg = "";
                        KeyBoardUtils.closeKeybord(et_input_notice, LivePushActivity.this);

                    }

                }
                et_input_notice.clearFocus();

                return false;
            }
        });
        et_input_notice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(et_input_notice.getText())) {
                    if (et_input_notice.getText().toString().equals(noticeMsg)) {
                        btn_send_notice.setText("删除");
                    } else {
                        btn_send_notice.setText("发送");
                    }
                }
            }
        });
        KeyBoardUtils.openKeybord(et_input_notice, this);
    }

    @Override
    public void setRecharge(List<RechargeDto> data) {
        if (null == rechargePopWindow) {
            rechargePopWindow = new RechargePopWindow(this, data);
            rechargePopWindow.setHuazuan(service.getUserInfo().balance);
        }
    }


    private void updateWallet() {
        service.updateWallet(new Consumer<Long>() {
            @Override
            public void accept(Long integer) throws Exception {
                UserInfo userInfo = service.getUserInfo();
                if (null != rechargePopWindow) {
                    rechargePopWindow.setHuazuan(integer);
                }
                if (null != giftPopWindow) {
                    giftPopWindow.setBalance(userInfo);
                }
                userInfo.balance = integer;
                service.setUsetInfo(userInfo);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (flFaceUnity.getVisibility() == View.VISIBLE) {
            flFaceUnity.setVisibility(View.GONE);
            BeautyParameterModel.save();
            return;
        }
        requestLeaveRoom();
    }

    public void requestLeaveRoom() {
        if (isPk) {
            toastTip("PK中不能退出直播");
            return;
        }
        CloseLivePushPop closeLivePushPop = new CloseLivePushPop(this, p);
        closeLivePushPop.setPopupGravity(Gravity.CENTER);
        closeLivePushPop.showPopupWindow();
    }

    @Override
    public void setWatchers(final List<WatcherDto> dtos) {
        tvWatcherNum.setText("" + dtos.size());
        tvWatcherNum.setOnClickListener(LivePushActivity.this);
        if (watchersAdapter == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            rvWatchers.setLayoutManager(linearLayoutManager);
            watchersAdapter = new WatchersAdapter(dtos);
            rvWatchers.setAdapter(watchersAdapter);
            watchersAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    WatcherDto data = watchersAdapter.getItem(position);
                    newUserInfoPopwindow = new NewUserInfoPopwindow(LivePushActivity.this, data.userId, channelId, roomManager);
                    newUserInfoPopwindow.showPopupWindow();
                }
            });
        }else {
            watchersAdapter.getData().clear();
            watchersAdapter.addData(dtos);
        }
        if (!watchersAdapter.getData().isEmpty())
            rvWatchers.scrollToPosition(0);


    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBeautyControlView != null) {
            mBeautyControlView.onResume();
        }
        if (mEffectRecyclerAdapter != null) {
            mEffectRecyclerAdapter.onResume();
        }
        if (null != p) {
            p.startMsgSync();
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mEffectRecyclerAdapter != null) {
            mEffectRecyclerAdapter.onPause();
        }
        if (null != p) {
            p.stopMsgSync();
        }

    }

    @Override
    public RecyclerView getChatRv() {
        return rvChat;
    }

    @Override
    public void addSystemList() {
        runOnUiThread(() -> {
            if (CommentUtils.isListEmpty(startLivePushDto.getNotices())) {
                return;
            }
            for (int i = 0; i < startLivePushDto.getNotices().size(); i++) {
                LivingRoomTextMsg msg = new LivingRoomTextMsg(startLivePushDto.getNotices().get(i), LivingRoomTextMsg.TYPE_SYSTEM);
                msg.systemColor = startLivePushDto.getColorInfo().getSystem_notice_color();
                L.e("lgl", "system_notice_color" + msg.systemColor);
                addMsg(msg);
            }
        });
    }

    @Override
    public void changeRoom(String id) {

    }

    @Override
    public ViewPager getViewPager() {
        return null;
    }

    @Override
    public void setSingleMatch(HttpResult<PunishTimeDto> result, boolean b) throws JSONException {
        if (result.isSuccess()) {
            singleMatchPopwindow.showPopupWindow(b);
            startCheckPkStart();
        } else if (Constants.PUNISH.equals(result.status)) {
            long time = result.data.getTime() - System.currentTimeMillis();
            L.e("lgl", "punishtime:" + time);
            new CustomerDialog.Builder(this)
                    .setTitle(result.description)
                    .setCountDownTimer(time)
                    .setNegativeBtnShow(false)
                    .create().show();
        } else {
            toastTip(result.description);
        }
    }


    @Override
    public void teamRandomMatch(List<RoomMemberChangeMsg> result) {
        if (teamPKDialogFragment != null && teamPKDialogFragment.isVisible()) {
            teamPKDialogFragment.dismiss();//加入匹配队列成功，关闭房间弹窗
        }
        pkGroupMatchingDialog = (PKGroupMatchingDialog) PKGroupMatchingDialog.getInstance(p);
        if (!pkGroupMatchingDialog.isVisible())
            pkGroupMatchingDialog.showDF(getSupportFragmentManager(), PKGroupMatchingDialog.class.getName(), startLivePushDto.getHeadImg());
        startCheckPkStart();
    }

    @Override
    public void cancleMatchSinglePK() {
        if (singleMatchPopwindow != null && singleMatchPopwindow.isShowing()) {
            singleMatchPopwindow.cancelCountDown();
            singleMatchPopwindow.dismiss();
        }
        if (pkGroupMatchingDialog != null && pkGroupMatchingDialog.isVisible()) {
            pkGroupMatchingDialog.dismiss();
        }
        endStartPkSchedule();
    }

    @Override
    public void closeLivePush(CloseLiveDto closeLiveDto, JumpInvtationDataBean jumpInvtationDataBean) {
        stopRTMPPush();
        if (p != null) {
            // 甘波 让我删的  说是因为主播不需要掉关闭直播间接口     p.leaveGroup(getIMGroupId(startLivePushDto.getChannelId()), startLivePushDto.getChannelId());
            p.sendEndLiveMsg(startLivePushDto.getChannelId());
        }
        Intent intent = new Intent(this, LiveEndActivity.class);
        intent.putExtra("isHost", true);
        intent.putExtra("userId", startLivePushDto.getChannelId());
        intent.putExtra("data", closeLiveDto);
        if (null != jumpInvtationDataBean) {
            intent.putExtra("jumpdata", jumpInvtationDataBean);
        }
        startActivity(intent);
        finish();
    }


    //封禁主播退出直播
    @Override
    public void bannedHost(String title) {
/*        if(!TextUtils.isEmpty(title)){
            Toast t = Toast.makeText(getApplication(), title, Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        }*/

        stopRTMPPush();
        BannedEvent bannedEvent = new BannedEvent();
        bannedEvent.content = title;
        RxBus.get().post(bannedEvent);
        finish();
    }

    @Override
    public void setSingleMatchResult(MatchTeamResult result) {
//        singleMatchPopwindow.changeMatchSuccessView();
        isSingle = true;
        if (singleMatchPopwindow != null && singleMatchPopwindow.isShowing()) {
            singleMatchPopwindow.dismiss();
        }
        matchPkResultUtil.setMatchSingleResult(result);
        startPk();
    }

    @Override
    public void setTeamMatchResult(MatchTeamResult result) {
        if (pkGroupMatchingDialog != null && pkGroupMatchingDialog.isVisible())
            pkGroupMatchingDialog.dismiss();
        isSingle = false;
        matchPkResultUtil.setMatchTeamResult(result);
        startPk();
    }

    @Override
    public synchronized void startPk() {
        if (isPk || null == matchPkResultUtil.getOtherInfo()) return;
        isPk = true;
        endStartPkSchedule();
        startCheckPkEnd();
//        reStartEngine(isPk);
        ivLivePushBottomPk.setVisibility(View.INVISIBLE);//隐藏pk按钮
        if (startPkPopwindow != null && startPkPopwindow.isShowing())
            startPkPopwindow.dismiss();
        RelativeLayout.LayoutParams pushLayoutP = (RelativeLayout.LayoutParams) pushView.getLayoutParams();
        pushLayoutP.width = ScreenUtils.getScreenWidth(this) / 2;
        pushLayoutP.height = ScreenUtils.getScreenWidth(this) * 2 / 3;
        svga_pk.getLayoutParams().height = ScreenUtils.getScreenWidth(this) * 2 / 3;
        pushLayoutP.addRule(RelativeLayout.ALIGN_TOP, R.id.pk_constraint_layout);
//        pushLayoutP.topMargin = DensityUtils.dp2px(this, 39);
        pushView.setLayoutParams(pushLayoutP);

        startPlay(matchPkResultUtil.getOtherInfo().getAnchorUrl(), txCloudPullView);

        resetPkAnimView();
        setPkStartScroeView();
        updatePkTime(new PkTimeDto(matchPkResultUtil.getPkTime(), matchPkResultUtil.getPunishTime()));
        ll_other_attention.setVisibility(View.VISIBLE);
        p.isAttention(matchPkResultUtil.getOtherInfo().getUserId());
        ((TextView) findViewById(R.id.tv_other_name)).setText(matchPkResultUtil.getOtherInfo().getNickname());
        actBanner.post(new Runnable() {
            @Override
            public void run() {
                actBanner.setVisibilitySVGA(false);
            }
        });
    }


    private void setPkStartScroeView() {
        layoutPkProgress.setVisibility(View.VISIBLE);
        if (!isSingle) {
            TextView tvWeScore = findViewById(R.id.tv_we_team_score);
            TextView tvOtherScore = findViewById(R.id.tv_other_team_score);
            tvWeScore.setText("团队：0");
            tvOtherScore.setText("0：团队");
            RecyclerView rvWeTeam = findViewById(R.id.rv_we_team);
            RecyclerView rvOtherTeam = findViewById(R.id.rv_other_team);
            RelativeLayout rvTeamBottom = findViewById(R.id.rl_pk_team_bottom);
            rvWeTeam.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            rvOtherTeam.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            rvWeTeam.setAdapter(new PkTeamAdapter(matchPkResultUtil.getUserTeamInfo(), true));
            rvOtherTeam.setAdapter(new PkTeamAdapter(matchPkResultUtil.getOtherTeamInfo(), false));
            rvTeamBottom.setVisibility(View.VISIBLE);
            tvWeScore.setVisibility(View.VISIBLE);
            tvOtherScore.setVisibility(View.VISIBLE);
        } else {
            showSingleProgressView(matchPkResultUtil.matchTeamResult, matchPkResultUtil.matchTeamResult.getUserResult());
        }
    }

    public void showSingleProgressView(PkRankDto pkRankDto, int result) {
        if (pkRankDto == null || pkRankDto.contributionRank == null && pkRankDto.objectContributionRank == null)
            return;
        int weSize = pkRankDto.contributionRank.size();
        int otherSize = pkRankDto.objectContributionRank.size();
        if (weSize < 3) {
            for (int i = 0; i < 3 - weSize; ++i) {
                pkRankDto.contributionRank.add(new PkRankMember());
            }
        }
        Collections.reverse(pkRankDto.contributionRank);
        if (otherSize < 3) {
            for (int i = 0; i < 3 - otherSize; ++i) {
                pkRankDto.objectContributionRank.add(new PkRankMember());
            }
        }
        RelativeLayout rvSingleBottom = (RelativeLayout) findViewById(R.id.rl_pk_single_bottom);
        LinearLayout llHotWe = (LinearLayout) findViewById(R.id.ll_hot_we);
        TextView tvHotWe = (TextView) findViewById(R.id.tv_hot_we);
        TextView tvHotOther = (TextView) findViewById(R.id.tv_hot_other);
        ImageView ivWipeWe = (ImageView) findViewById(R.id.iv_wipe_we);
        ImageView ivWipeOther = (ImageView) findViewById(R.id.iv_wipe_other);
        tvHotWe.setText(pkRankDto.memberPopularity + "");
        tvHotOther.setText(pkRankDto.objectMemberPopularity + "");
        llHotWe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPkControbutePop(pkRankDto.getUserInfo().getUserId(), true, true);
            }
        });
        LinearLayout llHotOther = (LinearLayout) findViewById(R.id.ll_hot_other);
        llHotOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPkControbutePop(pkRankDto.getObjectInfo().getUserId(), true, false);
            }
        });
        RecyclerView rvWeSingle = (RecyclerView) findViewById(R.id.rv_we_single);
        ImageView im_view = findViewById(R.id.im_view);
        RecyclerView rvOtherSingle = (RecyclerView) findViewById(R.id.rv_other_single);
        rvWeSingle.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvOtherSingle.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvWeSingle.setAdapter(new PkSingleAdapter(pkRankDto.contributionRank, true, result, new PkSingleAdapter.ILocationistener() {
            @Override
            public void getLocation(int position, int[] location, View view) {
                myFirstOne = location;
                Log.e("tag", "myFirstOne=" + myFirstOne[0]);


            }
        }));
        rvOtherSingle.setAdapter(new PkSingleAdapter(pkRankDto.objectContributionRank, false, result, new PkSingleAdapter.ILocationistener() {
            @Override
            public void getLocation(int position, int[] location, View view) {
                youFirstOne = location;
                Log.e("tag", "youFirstOne=" + youFirstOne[0]);

            }
        }));


        rvWeSingle.setLayoutFrozen(true);
        rvOtherSingle.setLayoutFrozen(true);
        if (result == 1) {
            ivWipeWe.setVisibility(View.GONE);
            ivWipeOther.setVisibility(View.VISIBLE);
        } else if (result == -1) {
            ivWipeWe.setVisibility(View.VISIBLE);
            ivWipeOther.setVisibility(View.GONE);
        } else {
            ivWipeWe.setVisibility(View.GONE);
            ivWipeOther.setVisibility(View.GONE);
        }
        rvSingleBottom.setVisibility(View.VISIBLE);
    }

    private void cleanPkResultAnim() {
        if (ivPkResultLeft == null) {
            ivPkResultLeft = findViewById(R.id.iv_pk_result_left);
        }
        if (ivPkResultRight == null) {
            ivPkResultRight = findViewById(R.id.iv_pk_result_right);
        }
        ivPkResultLeft.clearAnimation();
        ivPkResultRight.clearAnimation();
        layoutPkResult.setVisibility(View.GONE);
    }

    @Override
    public void updatePkRank(MatchTeamResult matchTeamResult) {
        //进行转化，除了排行版信息其他信息跟原来MatchTeamResult的字段代表含义不一样，丢弃
        MatchTeamResult newRank = matchPkResultUtil.getMatchTeamResult();
        newRank.contributionRank = matchTeamResult.contributionRank;
        newRank.objectContributionRank = matchTeamResult.objectContributionRank;
        newRank.memberPopularity = matchTeamResult.memberPopularity;
        newRank.objectMemberPopularity = matchTeamResult.objectMemberPopularity;
        matchPkResultUtil.setMatchSingleResult(newRank);
        showSingleProgressView(matchPkResultUtil.matchTeamResult, matchPkResultUtil.matchTeamResult.getUserResult());
    }

    @Override
    public void getQuickSdMsgList(ArrayList<QuickTalkDto> dtos) {

    }

    @Override
    public void setZhuboList(List<ZhuboDto> data, int page) {

    }

    @Override
    public void setZhuboList1(List<ZhuboDto> data, int page) {

    }

    @Override
    public void setLoadmoreEnable(boolean b) {

    }

    boolean isClick = false;

    @Override
    public void getMyHour(MyHourDto data) {
        if (null != data) {
            tv_hour_my.setText((data.getRank() == 0 || data.getRank() > 50) ? "TOP榜 未上榜" : "TOP榜 第" + data.getRank() + "名");
            if (isClick) {//点击排行榜 显示弹窗   防止第一次进入的时候调取
                isClick = false;
                LiveHourRankPopWindow hourRankPopWindow = new LiveHourRankPopWindow(LivePushActivity.this, data, channelId);
                hourRankPopWindow.showPopupWindow();
            }
        }
    }


    @Override
    public synchronized void endPk() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isPk) return;
                stopCheckPkEndSchedule();
                cleanPkResultAnim();
                isShowPkResulting = false;
                isPk = false;
                ivLivePushBottomPk.setVisibility(View.VISIBLE);//显示pk按钮
                if (pushView == null) {
                    pushView = findViewById(R.id.pusher_tx_cloud_view);
                }
                RelativeLayout.LayoutParams pushLayoutP = (RelativeLayout.LayoutParams) pushView.getLayoutParams();
                pushLayoutP.width = ScreenUtils.getScreenWidth(LivePushActivity.this);
                //   pushLayoutP.height = ScreenUtils.getScreenHeight(LivePushActivity.this);
                pushLayoutP.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                pushLayoutP.topMargin = 0;
                pushLayoutP.removeRule(RelativeLayout.ALIGN_TOP);
                pushView.setLayoutParams(pushLayoutP);
                stopPlay();
                if (txCloudPullView != null) txCloudPullView.onDestroy();
                layoutPkProgress.setVisibility(View.GONE);
                ll_other_attention.setVisibility(View.GONE);
                RelativeLayout rvTeamBottom = findViewById(R.id.rl_pk_team_bottom);
                TextView tvWeTeamScore = findViewById(R.id.tv_we_team_score);
                TextView tvOtherTeamScore = findViewById(R.id.tv_other_team_score);
                tvWeTeamScore.setVisibility(View.GONE);
                tvOtherTeamScore.setVisibility(View.GONE);
                rvTeamBottom.setVisibility(View.GONE);
                oldLeft = 0;
                oldRight = 0;
                uploadScore(0, 0);
            }
        });

    }

    @Override
    public void setPkerror(String error) {
        new CustomerDialog.Builder(this).setMsg(error)
                .setNegativeBtnShow(false)
                .create().show();
    }

    @Override
    public void updatePkGiftScore(PkScoreDto pkScoreDto) {
        uploadScore(pkScoreDto.getTotalPoint(), pkScoreDto.getOtherTotalPoint());
        if (!isSingle) {
            TextView tvWeScore = findViewById(R.id.tv_we_team_score);
            TextView tvOtherScore = findViewById(R.id.tv_other_team_score);
            tvWeScore.setText("团队：" + DataFormatUtils.formatNumbers(pkScoreDto.getGroupPoint()));
            tvOtherScore.setText(DataFormatUtils.formatNumbers(pkScoreDto.getOtherGroupPoint()) + "：团队");
        }
    }


    @Override
    public void updatePkTime(PkTimeDto timeDto) {
        if (timeDto.getEndPkTime() <= 0 && timeDto.getEndPunishTime() <= 0) return;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        TextView tv = (TextView) findViewById(R.id.tv_time);
        if (timeDto.getEndPkTime() > 0) {
            if (timeDto.getEndPkTime() < 1000) {
                timeDto.setEndPkTime(1000);
            }
            startPkCountDown(timeDto.getEndPkTime(), tv, "PK中  ", "  |  投降");
        } else if (timeDto.getEndPunishTime() > 0 && timeDto.getEndPkTime() == 0) {
            startPkCountDown(timeDto.getEndPunishTime(), tv, "惩罚中  ", "  |  关闭");
        }
    }

    CountDownTimer countDownTimer;

    //开始pk计时
    private void startPkCountDown(long timeMilli, TextView textView, String begin, String end) {
        countDownTimer = new CountDownTimer(timeMilli, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText(begin + TimeUtils.fromSecond((int) (millisUntilFinished / 1000)) + end);
            }

            @Override
            public void onFinish() {
                if (begin.contains("PK")) {
                    updatePkTime(new PkTimeDto(0, matchPkResultUtil.getPunishTime()));
                    handle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (p != null)
                                p.checkPkResult(channelId, isSingle);
                        }
                    }, 3000);
                } else {
                    textView.setText("00:00");
                    endPk();//倒计时结束 自动结束pk
                }
            }
        };
        countDownTimer.start();
    }

    int oldLeft = 0, oldRight = 0;

    /**
     * 更新pk礼物值
     *
     * @param left
     * @param right 当前按照 礼物最少值 20计算 ， 当一方没收到礼物一方收到任何礼物的时候，没收到礼物的一方保留1.5权值 另一方20权值
     */
    public void uploadScore(int left, int right) {
        if (isPk) {
            if (left < oldLeft || right < oldRight) {//pk过程中 避免数值回退
                return;
            }
        } else { //pk 结束 初始化这两个值
            oldLeft = 0;
            oldRight = 0;
        }
        View sLeft = findViewById(R.id.view_score_left);
        View sRight = findViewById(R.id.view_score_right);
        LinearLayout.LayoutParams leftParams = (LinearLayout.LayoutParams) sLeft.getLayoutParams();
        LinearLayout.LayoutParams rightParams = (LinearLayout.LayoutParams) sRight.getLayoutParams();
        float weightLeft = 1, weightRight = 1;
        if (left == 0 && right == 0) {

        } else if (1f * left / right < 0.2 || 1f * right / left < 0.2) {
            if (left > right) {
                weightLeft = 8;
                weightRight = 2;
            } else {
                weightLeft = 2;
                weightRight = 8;
            }
        } else {
            weightLeft = left;
            weightRight = right;
        }
        leftParams.weight = weightLeft;
        rightParams.weight = weightRight;
        sLeft.setLayoutParams(leftParams);
        sRight.setLayoutParams(rightParams);
        TextView tvSLeft = (TextView) findViewById(R.id.tv_score_mine);
        TextView tvSRight = (TextView) findViewById(R.id.tv_score_other);

        tvSLeft.setText("我方" + left);
        tvSRight.setText(right + "对方");
        oldLeft = left;
        oldRight = right;
    }

    @Override
    public void someoneComing(FloatScreenBean floatScreenBean) {
//        if (!isFirstIn) {
        super.someoneComing(floatScreenBean);
//            isFirstIn = false;
//        }
        p.getWatchers(service.getUserInfo().id + "");
    }

    @Override
    public void showReceivePkMsg(InvitePkMsg invitePkMsg, boolean isSingle) {
        ReceivePkInviteDF receivePkInviteDF = (ReceivePkInviteDF) ReceivePkInviteDF.getInstance(invitePkMsg, isSingle);
        receivePkInviteDF.setOnResponseListener(isAccept -> {
            if (invitePkMsg != null) {
                if (isSingle) {
                    if (isAccept) {//接受{
                        p.responseFriendSingleAgree(invitePkMsg.userId);
                    } else {
                        p.responseFriendSingleRefulse(invitePkMsg.userId);
                    }
                } else {
                    if (isAccept) {
                        p.intoGroupRoom(invitePkMsg.teamId);
                    }
                }
            }

        });
        receivePkInviteDF.showDF(getSupportFragmentManager(), ReceivePkInviteDF.class.getName());
    }

    @Override
    public void createRoomSuccess(String roomId) {
        List<RoomMemberChangeMsg> list = new ArrayList<>();
        RoomMemberChangeMsg roomMemberChangeMsg = new RoomMemberChangeMsg();
        roomMemberChangeMsg.userId = service.getUserInfo().id;
        roomMemberChangeMsg.nickname = service.getUserInfo().nickname;
        roomMemberChangeMsg.headImg = service.getUserInfo().headImg;
        roomMemberChangeMsg.teamId = roomId;
        roomMemberChangeMsg.leader = 1;
        list.add(roomMemberChangeMsg);
        refreshTeamInvitePkPop(list);
    }

    @Override
    public void roomMemberChange(List<RoomMemberChangeMsg> list) {
        refreshTeamInvitePkPop(list);
        if (teamPKDialogFragment.isFull() && inviteHostPopwindow != null && inviteHostPopwindow.isShowing())
            inviteHostPopwindow.dismiss();
    }

    @Override
    public UserInfo getUserInfo() {
        return service.getUserInfo();
    }

    @Override
    public GiftPopWindow.OnGiftSendListener getGiftListener() {
        return null;
    }

    @Override
    public void isAttention(String attention) {
        // isAttention  0：未关注 1:已关注
        findViewById(R.id.tv_other_attention).setVisibility(attention.contains("1") ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setCalorific(int calorific) {
        tvCalorific.setText(DataFormatUtils.formatNumbers(calorific));
    }

    @Override
    public void moveUpLoactions(String id, String url, String t, String l, String title,
                                int isDelete, String textcolor) {
        pendantId = Integer.parseInt(id);
        if (!is_one && null == pendantLayout) {

            pendantLayout = new PendantLayout(LivePushActivity.this);
            pendantLayout.setImgView(url, Integer.parseInt(id), -1);
            is_one = true;
            fl_back.addView(pendantLayout);

            BigDecimal w = new BigDecimal(wm.getDefaultDisplay().getWidth() + "");
            BigDecimal h = new BigDecimal(wm.getDefaultDisplay().getHeight() + "");
            BigDecimal lw = w.divide(new BigDecimal("100")).multiply(new BigDecimal(l)).setScale(2, BigDecimal.ROUND_UP);
            BigDecimal lh = h.divide(new BigDecimal("100")).multiply(new BigDecimal(t)).setScale(2, BigDecimal.ROUND_UP);
            BigDecimal end_l = lw.multiply(new BigDecimal("100"));
            BigDecimal end_t = lh.multiply(new BigDecimal("100"));
            RelativeLayout vie = pendantLayout.findViewById(R.id.rl_drag_container);
            ImageView tv_drag_content = vie.findViewById(R.id.tv_drag_content);


            pendantLayout.setEdittext(isDelete == 1, textcolor);
            pendantLayout.settext(title);
            ImageLoader.loadImg(tv_drag_content, url);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.topMargin = end_t.intValue();
            lp.leftMargin = end_l.intValue();
            vie.setLayoutParams(lp);
            pendantLayout.setIDeleteListener(LivePushActivity.this);
        }

        //  layoutParams.setMargins(end_l.intValue(),end_t.intValue(),0,0);


    }

    @Override
    public synchronized void showPkResult(PkResultDto pkResultDto, boolean isSingle) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isShowPkResulting || !isPk || null == matchPkResultUtil.getOtherInfo())
                        return;
                    TextView tv = (TextView) findViewById(R.id.tv_time);
                    if (null != tv && tv.getText().toString().contains("PK")) {//后台推送结果，如果还没到惩罚时间，pk倒计时清零，进入惩罚时间
                        updatePkTime(new PkTimeDto(0, matchPkResultUtil.getPunishTime()));
                    }
                    if (TextUtils.isEmpty(String.valueOf(pkResultDto.getResult())))
                        pkResultDto.setResult(0);//如果为null 默认设为0
                    layoutPkResult.setVisibility(View.VISIBLE);
                    ImageView weResult = findViewById(R.id.iv_pk_result_left);
                    ImageView otherResult = findViewById(R.id.iv_pk_result_right);
                    ImageView weResultEnd = findViewById(R.id.iv_pk_result_left_end);
                    ImageView otherResultEnd = findViewById(R.id.iv_pk_result_right_end);

                    if (pkResultDto.getResult() == 0) {//平
                        weResult.setImageResource(R.drawable.ic_pk_draw);
                        otherResult.setImageResource(R.drawable.ic_pk_draw);
                    } else if (pkResultDto.getResult() > 0) {//胜
                        weResult.setImageResource(R.drawable.ic_pk_success);
                        otherResult.setImageResource(R.drawable.ic_pk_fail);
//                    getFirstKill(1);
                    } else {
                        weResult.setImageResource(R.drawable.ic_pk_fail);
                        otherResult.setImageResource(R.drawable.ic_pk_success);
//                    getFirstKill(0);
                    }
                    new PkAnimaUtil().start(weResult, weResultEnd, otherResult, otherResultEnd, new PkAnimaUtil.MAnimatorListener() {
                        @Override
                        public void start() {

                        }

                        @Override
                        public void end() {
                            if (!isPk) {
                                cleanPkResultAnim();
                            }
                        }
                    });
                    if (!isSingle && pkResultDto.getTeamPkResultDto().getNormal() != 0 && isActivityResume) {
                        handle.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PKGroupResultDialog pkGroupResultDialog = (PKGroupResultDialog) PKGroupResultDialog.getInstance(pkResultDto);
                                pkGroupResultDialog.showDF(getSupportFragmentManager(), PKGroupResultDialog.class.getName());
                            }
                        }, 5000);
                    }
                    if (isSingle) {
                        pkResultDto.getSinglePkResultDto().userInfo = matchPkResultUtil.getMatchTeamResult().userInfo;
                        pkResultDto.getSinglePkResultDto().objectInfo = matchPkResultUtil.getMatchTeamResult().objectInfo;
                        showSingleProgressView(pkResultDto.getSinglePkResultDto(), pkResultDto.getResult());
                    }
                    isShowPkResulting = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void startCheckPkStart() {
        endStartPkSchedule();
        startPkScheduleService = Executors.newScheduledThreadPool(1);
        startPkScheduleService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    p.checkPkStart(startLivePushDto.getChannelId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

    }

    private void endStartPkSchedule() {
        if (startPkScheduleService != null) {
            startPkScheduleService.shutdownNow();
        }
        startPkScheduleService = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (redpacket != null) {
            //发送红包角标数量给首页
            RxBus.get().post(new RedPacketCountEvent(channelId, redpacket.redPacketCount(), -1));
        }
        isPk = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (handle != null) {
            handle.removeCallbacksAndMessages(null);
            handle = null;
        }


        endStartPkSchedule();
    }

    @Override
    protected void onPlayEnd() {
        super.onPlayEnd();
        startPlay(matchPkResultUtil.getOtherInfo().getAnchorUrl(), txCloudPullView);


    }

    @Override
    public void warnHost(String msg) {
        new CustomerDialog.Builder(this)
                .setTitle("警告")
                .setMsg(msg)
                .setNegativeBtnShow(false)
                .setPositiveButton("确定", new CustomerDialog.onPositiveInterface() {
                    @Override
                    public void onPositive() {

                    }
                })
                .create().show();
    }

    @Override
    public void getAttentionStatus() {
        p.getAttentionEach(channelId, false);
    }

    @Override
    public void updateSdj(UserGoodBean enterLivingRoomActWsJDto) {

    }

    @Override
    public void normalActivityIng(String notice, int id, String color, String name, String noticeBack) {
        if (!TextUtils.isEmpty(notice)) {
            String str = "公告：" + notice;
            //    tvAnnouncementGo.setVisibility(View.GONE);
            SpannableStringBuilder spannableString = new SpannableStringBuilder(str);
            if (!TextUtils.isEmpty(color)) {
                tvAnnouncement.setWht(color);
            } else tvAnnouncement.setWht("#ffffff");

            noticeList.add(new NoticeBean(spannableString, noticeBack, "", "", 0,null,""));
            annotation();
        }
    }

    @Override
    public void toRecharge() {

    }


    @Override
    public void deleteBank(int id, View v) {
        fl_back.removeView(v);
        pendantLayout = null;
        p.getDelPendant(pendantId);
    }

    @Override
    public void moveUpLoaction(int id, int l, int r, String title) {
        BigDecimal w = new BigDecimal(wm.getDefaultDisplay().getWidth() + "");
        BigDecimal h = new BigDecimal(wm.getDefaultDisplay().getHeight() + "");
        BigDecimal endl = new BigDecimal(l + "").divide(w, 2, BigDecimal.ROUND_DOWN);
        BigDecimal endr = new BigDecimal(r + "").divide(h, 2, BigDecimal.ROUND_DOWN);
        if (is_one) {
            if (pendantId != -1) {
                p.getAddPendant(pendantId, endr + "," + endl, title);
            }

        } else {
            p.getAddPendantpost(id, endr + "," + endl, title);
            is_one = true;
        }
    }


    public void moveUpLoactionEdAdd(int id, int l, int r, String title) {
        BigDecimal w = new BigDecimal(wm.getDefaultDisplay().getWidth() + "");
        BigDecimal h = new BigDecimal(wm.getDefaultDisplay().getHeight() + "");
        BigDecimal endl = new BigDecimal(l + "").divide(w, 2, BigDecimal.ROUND_DOWN);
        BigDecimal endr = new BigDecimal(r + "").divide(h, 2, BigDecimal.ROUND_DOWN);

        p.getAddPendantpost(id, endr + "," + endl, title);
        is_one = true;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int[] leftTop = {0, 0};
        //获取输入框当前的location位置
        view_bottom_input.getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + view_bottom_input.getHeight();
        int right = left + view_bottom_input.getWidth();
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (x > left && x < right && y > top && y < bottom) {
            // 点击的是发送按钮，保留点击EditText的事件
            // 必不可少，否则所有的组件都不会有TouchEvent了
            if (getWindow().superDispatchTouchEvent(event)) {
                return true;
            }
            return onTouchEvent(event);
        }
        handlerOtherTypeDispatchTouchEvent(event);
        return false;
    }

    private boolean handlerOtherTypeDispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, event)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(event);
        } else if (action == MotionEvent.ACTION_MOVE) {

        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(event)) {
            return true;
        }
        return onTouchEvent(event);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (isTouchPointInView(btn_send_notice, x, y)) {
                return false;
            }
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                //使EditText触发一次失去焦点事件
                v.setFocusable(false);
                v.setFocusableInTouchMode(true);
                return true;
            }
        }
        return false;
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            if (view instanceof RecyclerView) {
                return true;
            }
            if (view.hasOnClickListeners())
                return true;
            return false;
        }
        return false;
    }

    @Override
    public void endActivity(int activityId) {
        if (null != actBanner) {
            actBanner.endActivity(activityId);
        }

    }

    @Override
    public void showShouhu(Boolean isHost, int id, UserInfo data) {
        shouhuListPopwindow.setData(isHost, id, data);
        shouhuListPopwindow.showPopupWindow();
    }

    @Override
    public void setRoomNextData() {

    }

    @Override
    public void refreshPacketCount() {
        if (redpacket != null) {
            redpacket.refreshPackList();
        }
    }

    //开始播放红包雨
    private void playRedPackFalling(PacketCountBean packetBean) {
        fallingView = findViewById(R.id.v_redpack_view);
        fallingView.setVisibility(View.VISIBLE);
        //防止事件击穿
        fallingView.setOnClickListener(v -> {
        });
        FallingAdapter adapter = new FallingAdapter();
        adapter.setPacketBean(packetBean);
        adapter.setOnClickBuried(packetCountBean -> {
            //领取红包
            redpacket.givingPacket(packetCountBean.serialNo);
            return null;
        });
        fallingView.setAdapter(adapter);
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        fallingView.setOnFallingListener(new FallingView.OnFallingListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {
                fallingView.setVisibility(View.GONE);
                //红包结束查询结果
                if (redPackResultDialog == null)
                    redPackResultDialog = new RedPackResultDialog(LivePushActivity.this);
                redPackResultDialog.show(packetBean.id);
                //刷新角标
                redpacket.refreshPackList();
            }
        });
        adapter.setData(list);
        fallingView.startFalling();
    }

    @Override
    public void requestBackpack(GiftDto sendGift) {

    }

    @Override
    public String getPushRoomId() {
        if (startLivePushDto != null)
            return startLivePushDto.getChannelId() + "";
        return "";
    }

    @Override
    public void requestReStartLive() {
        requestLeaveRoom();
    }




    /*  实现流量监控实时显示 ****************************************************/
    /*  实现流量监控实时显示 ****************************************************/
    /*  实现流量监控实时显示 ****************************************************/
    /*  实现流量监控实时显示 ****************************************************/

    @Override
    public void onNetStatus(Bundle status) {
        super.onNetStatus(status);
        int netWork = status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED);
        if (netWork > 300)
            mTvNetWork.setNetWorkState(NetWorkState.HIGH);
         else if (netWork >= 150)
            mTvNetWork.setNetWorkState(NetWorkState.MIDDLE);
         else
            mTvNetWork.setNetWorkState(NetWorkState.LOW);
        mTvNetWork.setNetWorkText(netWork);
    }
}
