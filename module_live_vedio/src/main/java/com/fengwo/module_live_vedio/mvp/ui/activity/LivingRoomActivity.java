package com.fengwo.module_live_vedio.mvp.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.BuildConfig;
import com.fengwo.module_comment.base.BaseActivity;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.BackpackDto;
import com.fengwo.module_comment.bean.IsAttentionDto;
import com.fengwo.module_comment.bean.JumpInvtationDataBean;
import com.fengwo.module_comment.bean.UserLevelBean;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.event.PaySuccessEvent;
import com.fengwo.module_comment.event.RechargeSuccessEvent;
import com.fengwo.module_comment.event.RedPacketCountEvent;
import com.fengwo.module_comment.ext.ViewExtKt;
import com.fengwo.module_comment.iservice.IBackpackService;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.BeanUtil;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.Common;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.DownloadHelper;
import com.fengwo.module_comment.utils.EPSoftKeyBoardListener;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.utils.SafeHandle;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_comment.widget.LivingRoomFrameLayout;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.eventbus.AttentionChangeEvent;
import com.fengwo.module_live_vedio.eventbus.BannedPostEvent;
import com.fengwo.module_live_vedio.eventbus.BuyGuardSuccessEvent;
import com.fengwo.module_live_vedio.eventbus.ChangeRoomEvent;
import com.fengwo.module_live_vedio.eventbus.OpenGuardEvent;
import com.fengwo.module_live_vedio.eventbus.SendGiftToUserEvent;
import com.fengwo.module_live_vedio.eventbus.ShowBuyGuardPopEvent;
import com.fengwo.module_live_vedio.eventbus.ShowGiftEvent;
import com.fengwo.module_live_vedio.eventbus.ShowRechargePopEvent;
import com.fengwo.module_live_vedio.eventbus.ShowSendMsgEditEvent;
import com.fengwo.module_live_vedio.helper.LiveNoticeHelper;
import com.fengwo.module_live_vedio.mvp.UserGoodBean;
import com.fengwo.module_live_vedio.mvp.dto.CustomLiveEndDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomActDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomPkActivityDto;
import com.fengwo.module_live_vedio.mvp.dto.GetActivityInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;
import com.fengwo.module_live_vedio.mvp.dto.GuardListDto;
import com.fengwo.module_live_vedio.mvp.dto.LastFrameDto;
import com.fengwo.module_live_vedio.mvp.dto.MatchTeamResult;
import com.fengwo.module_live_vedio.mvp.dto.MyHourDto;
import com.fengwo.module_live_vedio.mvp.dto.PacketCountBean;
import com.fengwo.module_live_vedio.mvp.dto.PendantDto;
import com.fengwo.module_live_vedio.mvp.dto.PendantListDto;
import com.fengwo.module_live_vedio.mvp.dto.PkResultDto;
import com.fengwo.module_live_vedio.mvp.dto.PkScoreDto;
import com.fengwo.module_live_vedio.mvp.dto.PkTimeDto;
import com.fengwo.module_live_vedio.mvp.dto.QuickTalkDto;
import com.fengwo.module_live_vedio.mvp.dto.RechargeDto;
import com.fengwo.module_live_vedio.mvp.dto.RefreshBackpack;
import com.fengwo.module_live_vedio.mvp.dto.StickersDto;
import com.fengwo.module_live_vedio.mvp.dto.WatcherDto;
import com.fengwo.module_live_vedio.mvp.presenter.LivingRoomPresenter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.ActivityInfoEndAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LiveRecAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LivingRoomInfoPageAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LivingRoomPageAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.QuickSdMsgListAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.WatchersAdapter;
import com.fengwo.module_live_vedio.mvp.ui.df.PKGroupResultDialog;
import com.fengwo.module_live_vedio.mvp.ui.dialog.game.GameDialog;
import com.fengwo.module_live_vedio.mvp.ui.dialog.redpackresult.RedPackResultDialog;
import com.fengwo.module_live_vedio.mvp.ui.fragment.LivingRoomFragment;
import com.fengwo.module_live_vedio.mvp.ui.pop.ActivityGiftPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.BuyGiftTipsPopWindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.BuyShouhuPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.GiftPopWindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.LeavePopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.LiveHourRankPopWindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.LiveRoomMorewindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.NewUserInfoPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.PeoplePopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.QuickTalkPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.RechargePopWindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.ShouhuListPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.ToutiaoListPopwindow;
import com.fengwo.module_live_vedio.mvp.ui.pop.ToutiaoRulePopwindow;
import com.fengwo.module_live_vedio.utils.AttentionUtils;
import com.fengwo.module_live_vedio.utils.PkAnimaUtil;
import com.fengwo.module_live_vedio.utils.PrivilegeEffectUtils;
import com.fengwo.module_live_vedio.widget.giftlayout.bean.NoticeBean;
import com.fengwo.module_live_vedio.widget.redpack.FallingAdapter;
import com.fengwo.module_live_vedio.widget.redpack.FallingView;
import com.fengwo.module_live_vedio.widget.redpack.RedPackIconView;
import com.fengwo.module_websocket.Url;
import com.fengwo.module_websocket.bean.LivingRoomTextMsg;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.youth.banner.transformer.AlphaPageTransformer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import razerdp.basepopup.BasePopupWindow;

/**
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　~~~   ~~~　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * 给后人造福 写逻辑（被坑了好几个月了）
 * 直播间和主播开播共同使用baseliveingroomactivity
 * 直播界面 可以从n多地方进来，首页的列表的逻辑没改 其他直播间需要做无限循环滚动 避免只有一个视频 所以进入前会掉更多直播接口 直播间较少的会走Arouter方法进入
 * 进入直播界面会调用viewpager2的滑动事件，跳转到相应的position  播放器和pk的播放器都在viewpage2的adapter的fragment里面
 * 现有飘屏消息列队功能有 公告  礼物 座驾（pk没做队列）
 * 直播间内容可以从data.get(preIndex)拿到 直播间id=channelId
 * 每次滚动会清楚一大堆内容 setLoadNextData()
 * 左右切换用的viewpager 为了实现清屏  viewpage.get(0) 是主播下播界面
 * 其他各种弹框的的popwin 切记 请勿随便删除任何判断代码和调用逻辑
 * 直播间聊天代码较多，但不难，仔细看看能改
 * 活动代码非常乱，而且原有的活动都被干掉了 可以说 每写一个活动都要从新做
 * 直播间view点击事件在activity的自定义view做的拦截 遇到想不明白的可以去打个断点看看
 * 直播间更新经验值 每个获取经验值都会调用 这后台写的有问题 经验值经常拿的不对 而且经验值是前端计算
 * 直播间软键盘是覆盖到activity上面 软键盘下面有快捷回复，为了界面不闪烁，如遇到相关软键盘问题 把使用功能的base 换成源生尝试
 * 为执行祖传代码不能删的原则 需要重构请先读懂原有逻辑和调用关系，如实在看不懂 请删除重构  弹框坑有（公告，头条，礼物，背包，活动，守护）
 * im消息收不到或者不及时 一般都是后台的问题 入pk进度条更新等 直播间已经被try（）cath（）包裹 一般崩溃不影响正常使用
 * 动画分为mp4和svga动画 mp4动画采用阿尔法和rgb双通道播放 使用exo播放器加AlphaPlayer制作 https://github.com/bytedance/AlphaPlayer
 * 谨慎代码有 活动 礼物和背包 没啥事别tm乱改
 * 2021.1.12 杨宇豪
 */

@Route(path = ArouterApi.LIVE_ROOM)
public class LivingRoomActivity extends BaseLiveingRoomActivity implements View.OnClickListener, LivingRoomFragment.OnPkBtnClickListener {

    private static final int START_PLAY_WHAT = 2;
    private static final int START_PLAY_ONE = 4;
    private static final int SHOW_PK_RESULT = 5;


    @Autowired
    UserProviderService userProviderService;

    @Autowired
    IBackpackService mBackpackService;

    private final static String GET_LIST = "getlist";
    private final static String GET_INDEX = "getindex";
    private final static String GET_ONE = "getone";
    private final static String TOTAL_SIZE = "totalSize";
    private final static String CURRENT_PAGE = "currentPage";
    private LayoutInflater mInflater;
    @BindView(R2.id.view_root)
    FrameLayout viewRoot;
    @BindView(R2.id.root)
    LivingRoomFrameLayout livingRoomFrameLayout;
    @BindView(R2.id.rv)
    ViewPager2 rv;
    @BindView(R2.id.vp)
    ViewPager vp;
    @BindView(R2.id.iv_close)
    ImageView ivClose;

    @BindView(R2.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R2.id.right_layout)
    LinearLayout right_layout;
    @BindView(R2.id.recycleviewRight)
    RecyclerView recycleviewRight;
    @BindView(R2.id.mysmartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;


    int preIndex;
    LivingRoomFragment livingRoomFragment;
    LivingRoomInfoPageAdapter livingRoomInfoPageAdapter;


    private FallingView fallingView;
    private TextView tv_hour_my;

    private View viewRight;
    private View viewBottom;
    private View viewBottomInput;
    private RecyclerView tagLayoutView;
    private TextView tvRoomId;


    private ImageView btnSend;
    private ImageView btnSixin, btnShare, btnMore;
    private LinearLayout talkRoot;
    private Switch swDanmu;
    private ImageView btnToutiao;//头条按钮
    //    private ImageView btnQipaop;
//    private ImageView ivTipSoap;

    //守护
    private ImageView btnShouhu;//守护按钮
    private FrameLayout flShouhu;
    private CircleImageView ivShouhuHeader;

    //    private FrameLayout shouhu1, shouhu2, shouhu3;//本直播间观众 刷钱前3名 带有background 都没刷钱background置空
//    private CircleImageView ivShouhu1, ivShouhu2, ivShouhu3;
    private RecyclerView rvWatchers;
    private WatchersAdapter watchersAdapter;
    private ImageView btnSendGift;
    private TextView btnAttention;
    private CircleImageView ivHeader;
    private ImageView ivHeadBoard;
    private TextView tvGuardNum;
    private TextView tvWatcherNum;
    //private TextView tv_level_now;//暂离
    private TextView tvCalorific;//热度
    private TextView mTvAnchorLevel;  //主播等级
    private ImageView mIvAnchorLevel;

    private RecyclerView rvChat;
    private TextView tvName;
    private View liveEndView;

    private LinearLayout tvHourRank;
    private LinearLayout llMoreLive;
    private ImageView ivMoreClose;

    ///////////////////////告白女神活动

    private ImageView ivActivityTag;

    ActivityGiftPopwindow activityGiftPopwindow;


    ////////////////////////////////end


    //    AttentionPopwindow attentionPopwindow;

    PeoplePopwindow watcherNumPop;
    QuickTalkPopwindow quickTalkPopwindow;
    LeavePopwindow leavePopwindow;
    ToutiaoListPopwindow toutiaoListPopwindow;
    private BuyShouhuPopwindow buyShouhuPopwindow;
    private RechargePopWindow rechargePopWindow;
    private GameDialog gameDialog;
    private RedPackResultDialog redPackResultDialog;
    private MyHourDto myHourDto = null;

    private List<View> roomInfoViews = new ArrayList<>();


    private CountBackUtils countBackUtils;


    private boolean isAttention;
    private boolean isDanmu = false;


    private SafeHandle handle;
    private int acceptGiftUserId = -1;
    private String acceptGiftUsername = "";

    private volatile boolean isSingle = true;//是否单人pk
    private volatile boolean isPk = false;//是否pk
    private EnterLivingRoomDto anchorInfo; // 主播信息

    WindowManager wm;
    private View pendantLayout;
//    private ImageView ivCloseSoap;

    private BuyGiftTipsPopWindow mTipsPopWindow;  //3级不到弹窗
    private Timer mTimer;
    private Timer mUpLevelTimer;
    private TextView tvTitle;
    private ImageView tvDragContent;
    private View noticeView;
    private ImageView btn_shop;

    private boolean keyBoardShow;  //键盘是否显示
    private boolean selfCloseKeyBord;  //是否切换快捷关闭键盘


    private ImageView im_moer;
    private LiveRoomMorewindow liveRoomMorewindow;
    private LinearLayout ll_view;
    private ImageView im_sd_lw;
    private TextView tv_sd_lw;
    private LivingRoomPageAdapter livingRoomPageAdapter;

    private int type = 1;//是否开启私信功能
    private LinearLayout llNotice;
    private RelativeLayout fl_back;
    private ConstraintLayout viewRightRoot;
    private RedPackIconView redpacket;
    private List<PacketCountBean> packetList;//红包队列
    private LiveHourRankPopWindow hourRankPopWindow;
    private ToutiaoRulePopwindow toutiaoRulePopwindow;
    //    private List<PacketCountBean> packetList;//红包队列

    public LivingRoomPresenter getPresenter() {
        return p;
    }

    @Override
    protected int getContentView() {
        return R.layout.live_activity_livingroom;
    }

    public static void start(Context c, ArrayList<ZhuboDto> dtoList, int position) {
        Intent i = new Intent(c, LivingRoomActivity.class);
        i.putExtra(GET_LIST, dtoList);
        i.putExtra(GET_INDEX, position);
        c.startActivity(i);
    }

    public static void start(Context c, ArrayList<ZhuboDto> dtoList, int position, boolean isOne) {
        Intent i = new Intent(c, LivingRoomActivity.class);
        i.putExtra(GET_LIST, dtoList);
        i.putExtra(GET_INDEX, position);

        c.startActivity(i);
    }


    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        super.initView();
        wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        handle = new SafeHandle(this) {
            @Override
            protected void mHandleMessage(Message msg) {
                super.mHandleMessage(msg);
                if (msg.what == START_PLAY_WHAT) {
//                    if (msg.arg1 == preIndex) {
//                        klog("大屏拉流 p=" + preIndex);
//                        initPlay(preIndex);
//                        startNormalPlay();
//                      /*  if (isDebuggable(LivingRoomActivity.this)) {
//                            copyText(LivingRoomActivity.this, data.get(preIndex).pullUrlFLV, "拉流地址已复制到剪切板");
//                        }*/
//                    }
                    //不需要从列表中找数据，直接重缓存中区播放
                    if (p.enterLivingRoomDto != null) {
                        initPlay(p.roomId);
                        startNormalPlay();
                    }
                } else if (msg.what == START_PLAY_ONE) {
                    changeRoom(data.get(0).channelId + "", data.get(0).headImg);
                } else if (msg.what == SHOW_PK_RESULT) {

                }
            }
        };
        EPSoftKeyBoardListener.setListener(this, new EPSoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //       tvQuickMsg.setBackgroundResource(R.drawable.shape_gray_dot);
                //      btnSend.setImageResource(R.mipmap.pic_end);
                rv.setUserInputEnabled(false);
                if (!etInput.hasFocus()) {
                    return;
                }
                keyBoardShow = true;
                selfCloseKeyBord = false;
                ConstraintLayout.MarginLayoutParams params = (ConstraintLayout.MarginLayoutParams) viewBottomInput.getLayoutParams();
//                params.bottomMargin = height;
                viewBottomInput.setVisibility(View.VISIBLE);
                viewBottom.setVisibility(View.GONE);
                tagLayoutView.setVisibility(View.VISIBLE);
                params.height = height + viewBottomInput.findViewById(R.id.ll_input).getHeight();
                viewBottomInput.setLayoutParams(params);
            }

            @Override
            public void keyBoardHide(int height) {
                rv.setUserInputEnabled(true);
                if (!etInput.hasFocus()) {
                    return;
                }
                keyBoardShow = false;
                if (!selfCloseKeyBord) {
                    viewBottom.setVisibility(View.VISIBLE);
                    viewBottomInput.setVisibility(View.GONE);
                    tagLayoutView.setVisibility(View.GONE);
                }


            }
        });
        mInflater = LayoutInflater.from(this);
        List<ZhuboDto> zhuboDtos = (List<ZhuboDto>) getIntent().getSerializableExtra(GET_LIST);
        if (zhuboDtos != null) {
            data.addAll(zhuboDtos);
        }
        preIndex = getIntent().getIntExtra(GET_INDEX, 0);

        if (preIndex >= data.size()) {
            preIndex = data.size() - 1;
        }
        preIndex = Math.max(preIndex, 0);
        channelId = data.get(preIndex).channelId;
        initVp();
        initLivingRv();
        initRxBus();
        setZhuboList(data, preIndex);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                p.getRecharge();
                p.getGift(false, 0);
                p.getTouTiaoIfExist();
                p.getQuickSdMsgList();//快捷回复
                getUserLevelFromRaw();
            }
        });

        setDrawerLeftEdgeSize(LivingRoomActivity.this, drawerLayout, 0.4f);

    }

    private void initRxBus() {
        allRxbus.add(RxBus.get().toObservable(ShowRechargePopEvent.class)
                .compose(bindToLifecycle())
                .subscribe(new Consumer<ShowRechargePopEvent>() {
                    @Override
                    public void accept(ShowRechargePopEvent showRechargePopEvent) throws Exception {
                        if (null != rechargePopWindow) {
                            updateWallet("");
                            rechargePopWindow.showPopupWindow();
                        } else {
                            p.getRecharge();
                        }
                    }
                }));
        allRxbus.add(RxBus.get().toObservable(String.class).subscribe(it -> {
            if (it.equals("yyh_GG")) {
                newYearSmokeWeb();
            }
        }));
        allRxbus.add(RxBus.get().toObservable(ShowBuyGuardPopEvent.class)//开通守护第二个界面
                .compose(bindToLifecycle())
                .subscribe(new Consumer<ShowBuyGuardPopEvent>() {
                    @Override
                    public void accept(ShowBuyGuardPopEvent showBuyGuardPopEvent) throws Exception {
                        if (!showBuyGuardPopEvent.isMine) {
                            showBuyGuardPopWin(showBuyGuardPopEvent.type, showBuyGuardPopEvent.userInfo);
                        }

                    }
                }));
        allRxbus.add(RxBus.get().toObservable(ShowSendMsgEditEvent.class)
                .compose(bindToLifecycle())
                .subscribe(new Consumer<ShowSendMsgEditEvent>() {
                    @Override
                    public void accept(ShowSendMsgEditEvent showSendMsgEditEvent) throws Exception {
                        showSendMsgEdit(showSendMsgEditEvent.text);
                    }
                }));
        allRxbus.add(RxBus.get().toObservable(ShowGiftEvent.class)
                .compose(bindToLifecycle())
                .subscribe(new Consumer<ShowGiftEvent>() {
                    @Override
                    public void accept(ShowGiftEvent showGiftEvent) throws Exception {
                        if (showGiftEvent.getType() == ShowGiftEvent.GIFT_TYPE_TOUTIAO) {
                        } else {
                            giftPopWindow.setIsWish(false);
                            giftPopWindow.setjy(userProviderService.getUserInfo());
                            giftPopWindow.showPopupWindow();
                        }
                    }
                }));
        allRxbus.add(RxBus.get().toObservable(PaySuccessEvent.class)
                .compose(bindToLifecycle())
                .subscribe(new Consumer<PaySuccessEvent>() {
                    @Override
                    public void accept(PaySuccessEvent paySuccessEvent) throws Exception {
                        updateWallet(paySuccessEvent.getExperience());
                    }
                }));
        allRxbus.add(RxBus.get().toObservable(BuyGuardSuccessEvent.class)
                .compose(bindToLifecycle())
                .subscribe(new Consumer<BuyGuardSuccessEvent>() {
                    @Override
                    public void accept(BuyGuardSuccessEvent buyGuardSuccessEvent) throws Exception {
                        getAttentionStatus();
                        //   p.getGuardList(data.get(preIndex).channelId);
                        p.sendBuyGuardMsg(buyGuardSuccessEvent.getName(), buyGuardSuccessEvent.getIcon());
                        updateWallet(buyGuardSuccessEvent.getExperience());
                        if (shouhuListPopwindow != null) shouhuListPopwindow.hasGurad();
                    }
                }));
        allRxbus.add(RxBus.get().toObservable(AttentionChangeEvent.class)
                .compose(bindToLifecycle())
                .subscribe(new Consumer<AttentionChangeEvent>() {
                    @Override
                    public void accept(AttentionChangeEvent attentionChangeEvent) {
                        if (attentionChangeEvent.isAttention && attentionChangeEvent.sendIMMsg == 1) {   //关注主播成功，且不是PK
                            getAttentionStatus();
                            if (attentionChangeEvent.id.equals(myAnchorId + "")) { //有一个看不懂的 这里是发消息 是否给主播发的im消息
                                p.sendAnchorAttentionMsg();
                            } else {
                                p.sendAnchorAttentionMsgs(attentionChangeEvent.id);
                            }
                        }

                        if (!attentionChangeEvent.isAttention)
                            getAttentionStatus();
                        if (attentionChangeEvent.isPk) {
                            livingRoomFragment.setAttention();
                        }
                        //else {
//                            isAttention = attentionChangeEvent.isAttention;
//                            if (isAttention) {
//                                btnAttention.setVisibility(View.GONE);
//                            } else {
//                                btnAttention.setVisibility(View.VISIBLE);
//                            }
//                            if (null != leavePopwindow)
//                                leavePopwindow.setAttention(isAttention);
                        // }
                    }
                }));
        allRxbus.add(RxBus.get().toObservable(SendGiftToUserEvent.class)//详情点击送礼
                .compose(bindToLifecycle())
                .subscribe(new Consumer<SendGiftToUserEvent>() {
                    @Override
                    public void accept(SendGiftToUserEvent sendGiftToUserEvent) throws Exception {
                        if (null != giftPopWindow) {
                            acceptGiftUserId = sendGiftToUserEvent.getUid();
                            acceptGiftUsername = sendGiftToUserEvent.getUserName();
                            giftPopWindow.setIsWish(false);
                            giftPopWindow.setjy(userProviderService.getUserInfo());
                            giftPopWindow.showPopupWindow();

                        }
                    }
                }));
        allRxbus.add(RxBus.get().toObservable(ChangeRoomEvent.class)
                .compose(bindToLifecycle())
                .subscribe(new Consumer<ChangeRoomEvent>() {
                    @Override
                    public void accept(ChangeRoomEvent changeRoomEvent) throws Exception {
                        changeRoom(changeRoomEvent.getChannelId(), changeRoomEvent.headImage);
                    }
                }));
        allRxbus.add(RxBus.get().toObservable(BannedPostEvent.class)
                .compose(bindToLifecycle())
                .subscribe(new Consumer<BannedPostEvent>() {
                    @Override
                    public void accept(BannedPostEvent bannedPostEvent) throws Exception {
                        type = bannedPostEvent.getType();
                        //      btnSixin.setVisibility(type == 0 ? View.GONE : View.VISIBLE);
                    }
                }));

        allRxbus.add(RxBus.get().toObservable(OpenGuardEvent.class)//守护第一个界面
                .compose(bindToLifecycle())
                .subscribe(new Consumer<OpenGuardEvent>() {
                    @Override
                    public void accept(OpenGuardEvent openGuardEvent) throws Exception {
                        if (null == shouhuListPopwindow) {//删掉会造成续费的问题
                            shouhuListPopwindow = new ShouhuListPopwindow(LivingRoomActivity.this, false, openGuardEvent.getId());
                        }
                        p.getUserInfo(false, openGuardEvent.getId());

                        //   p.getGuardList(openGuardEvent.getId());

                    }
                }));
        //跳转到i撩直播间 （sb需求 开这播 还能去看直播 平生第一次遇到这种需求）
        allRxbus.add(RxBus.get().toObservable(JumpInvtationDataBean.class)
                .compose(bindToLifecycle())
                .subscribe(dataBean -> {
                    p.leaveGroup(groupId, data.get(preIndex).channelId);//切换成功时 退出上一个房间
                    finish();
                }));
        allRxbus.add(RxBus.get().toObservable(RefreshBackpack.class)
                .compose(bindToLifecycle())
                .subscribe(data -> {
                    if (giftPopWindow != null && giftPopWindow.isShowing()) {
                        //显示背包弹出再去请求数据，防止多次请求
                        requestBackpack(data.bean);
                    }
                }));
    }


    @Override
    public void attentionEachSuccess(int state, int switchStatus, boolean istype) { //点击私信  获取互相关注状态
        if (istype) {
            if (state == 2) {
                if (userProviderService.getUserInfo().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_YES) {
                    ArouteUtils.toChatSingleActivity(userProviderService.getUserInfo().fwId, String.valueOf(channelId),
                            anchorInfo.nickname, anchorInfo.headImg);
                } else if (userProviderService.getUserInfo().myIsCardStatus == UserInfo.IDCARD_STATUS_ING) {
                    ToastUtils.showShort(this, "实名认证中,请您耐心等待", Gravity.CENTER);
                } else if (userProviderService.getUserInfo().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_NO
                        || userProviderService.getUserInfo().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_NULL) {
                    showVerifyDialog();
                }
            } else {
                if (switchStatus == 0) {
                    toastTip("此功能升级中");
                } else {
                    toastTip("互相关注后才能私信");
                }
            }
        }
//        0：未关注 1:已关注 2：已互相关注
        this.state = state;

        if (state == 0) {
            p.isAttention = false;
            isAttention = false;
            btnAttention.setVisibility(View.VISIBLE);
        } else {
            p.isAttention = true;
            isAttention = true;
            btnAttention.setVisibility(View.GONE);
        }
        if (livingMsgAdapter != null)
            livingMsgAdapter.setState(state);
    }

    private void showBuyGuardPopWin(int id, UserInfo userInfo) {
        if (buyShouhuPopwindow == null)
            buyShouhuPopwindow = new BuyShouhuPopwindow(this);
        buyShouhuPopwindow.setZhuboInfo(id, userInfo);
        buyShouhuPopwindow.setBalance(userProviderService.getUserInfo().balance);
        buyShouhuPopwindow.showPopupWindow();
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

    private int page = 1;
    //private boolean isLoadMore = false;
    private LiveRecAdapter liveRecAdapter;

    private void initDrawLayout() {
        p.getZhuboList(page, -1);

    }

    @Override
    public void onResume() {
        super.onResume();
//        if (isFirstResume) {
//            isFirstResume = false;
//        } else {
//            if (p != null)
//                p.joinGroup(groupId, data.get(preIndex).channelId, false);//加入群 会自动设置toId
//        }
        if (isOne) {
            p.UpActivity(myAnchorId);
        }
        allRxbus.add(AttentionUtils.isAttention(channelId, new LoadingObserver<HttpResult<IsAttentionDto>>() {
            @Override
            public void _onNext(HttpResult<IsAttentionDto> data) {
                RxBus.get().post(new AttentionChangeEvent(data.data.isAttention > 0));
            }

            @Override
            public void _onError(String msg) {

            }
        }));
        if (rechargePopWindow != null && rechargePopWindow.isShowing())
            RxBus.get().post(new RechargeSuccessEvent());


    }


    private void updateWallet(String experience) {
        userProviderService.updateWallet(new Consumer<Long>() {
            @Override
            public void accept(Long integer) throws Exception {
                UserInfo userInfo = userProviderService.getUserInfo();
                if (!experience.equals("")) {
                    userInfo.setExperience(experience);
                }

                userProviderService.getUserInfo().setBalance(integer);
                userInfo.balance = integer;//
                if (null != giftPopWindow) {
                    giftPopWindow.setjy(userInfo);
                    giftPopWindow.setBalance(userInfo);

                }
                if (null != rechargePopWindow) {
                    rechargePopWindow.setHuazuan(integer);
                }
                if (null != buyShouhuPopwindow) {
                    buyShouhuPopwindow.setBalance(integer);
                }

                userProviderService.setUsetInfo(userInfo);
                if (PrivilegeEffectUtils.getInstance().isMsgEnable(userProviderService.getUserInfo().userLevel)) {
                    if (mUpLevelTimer != null) {
                        mUpLevelTimer.cancel();
                        mUpLevelTimer = null;
                    }
                }
            }
        });
    }


    @Override
    public RecyclerView getChatRv() {
//        return livingRoomFragment.getRvChat(preIndex);
        return rvChat;
    }

    @Override
    public ViewPager getViewPager() {
        return vp;
    }

    @Override
    public void addSystemList() {

    }

    @Override
    public void onBackPressed() {
        if (null != watcherNumPop && watcherNumPop.isShowing() ||
                null != leavePopwindow && leavePopwindow.isShowing() ||
                null != toutiaoListPopwindow && toutiaoListPopwindow.isShowing()
                || null != buyShouhuPopwindow && buyShouhuPopwindow.isShowing()
                || null != giftPopWindow && giftPopWindow.isShowing()
                || null != rechargePopWindow && rechargePopWindow.isShowing()) {
            super.onBackPressed();
        } else
            leave();
    }


    // List<Fragment> fragmentList;
    private ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            //KLog.e("viewpager2", "onPageScrolled" + +positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            KLog.e("tage", "ViewPager2 onPageSelected " + position);
            KLog.e("tage", "ViewPager2 old_index " + old_index);
            if (fallingView != null && fallingView.isFalling() && redpacket.getGlobalPacket() == null) {//普通红包立即停止
                fallingView.stopFalling();
            }
            int index = position % data.size();
            if (old_index > index) {
                isUp = true;
            } else {
                isUp = false;
            }
            if (statusDataList == 0 && old_index == 0 && index == data.size() - 1 && data.size() >= 10) {
                //初始状态，并且由第一个向上滑动，需要请求到最后一条直播数据
                statusDataList = 1;
            }

            old_index = index;
            if (index == data.size() - 1 || index == data.size() - 2) {
                getListIndex = index;
                initDrawLayout();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
            //KLog.e("viewpager2", "onPageScrollStateChanged  state-->" + +state);
        }
    };
    private int old_index;//滑动上一个下标
    private int getListIndex;//扩容数据后的下标
    private final FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycleCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
            super.onFragmentResumed(fm, f);
            if (statusDataList == 1) {
                //数据加载中，不拉流
                return;
            }
            if (f instanceof LivingRoomFragment) {
                LivingRoomFragment fragment = (LivingRoomFragment) f;
                if (fragment == livingRoomFragment) {
                    return;
                }
                if (livingRoomFragment != null) {
                    livingRoomFragment.setJoinRoom(false);
                }
                livingRoomFragment = fragment;
                livingRoomFragment.setLiveRoomFrameLayout(livingRoomFrameLayout);
                livingRoomFragment.setPkBtnClickListener(LivingRoomActivity.this);

                int index = rv.getCurrentItem() % data.size();
                KLog.e("tage", "加载" + rv.getCurrentItem() + "size= " + data.size() + data.get(index).nickname);
                KLog.e("tage", "加载 index " + index);
                //livingRoomFragment.setInitializationView(data.get(index).headImg,data.get(index).thumb);
                releaseAnim();
                setLoadNextData(index);

            }
        }

        @Override
        public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
            super.onFragmentViewDestroyed(fm, f);
            if (f instanceof LivingRoomFragment) {
                LivingRoomFragment fragment = (LivingRoomFragment) f;
                ZhuboDto zhuboDto = fragment.getMData();
                if (zhuboDto != null && fragment.isJoinRoom()) {
                    p.leaveGroup(getIMGroupId(zhuboDto.channelId), zhuboDto.channelId);
                }
            }
        }
    };

    private void initviewpahge2() {
        rv.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        rv.setOffscreenPageLimit(1);
        rv.registerOnPageChangeCallback(onPageChangeCallback);
        rv.setPageTransformer(new AlphaPageTransformer());
        livingRoomPageAdapter = new LivingRoomPageAdapter(this, data);
        rv.setAdapter(livingRoomPageAdapter);
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks, false);
        rv.setCurrentItem(data.size() * 100 + preIndex, false);
        old_index = preIndex;
        page = (int) Math.ceil(data.size() / 10d);
        KLog.e("tage", "初始翻页 page = " + page);
        groupId = getIMGroupId(data.get(preIndex).channelId);
        myAnchorId = data.get(preIndex).channelId;
        p.getHour(channelId);

        if (BuildConfig.DEBUG) {
            for (int i = 0; i < data.size(); i++) {
                KLog.e("tage", data.get(i).nickname);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initLivingRv() {
        initviewpahge2();
        rvChat.post(new Runnable() {
            @Override
            public void run() {
                if (livingRoomFragment != null && viewBottom != null) {
                  int h =  rvChat.getLayoutParams().height//聊天高度
                            + viewBottom.getHeight()//底部功能高度
                            + DensityUtils.dp2px(rvChat.getContext(), 46);
                    livingRoomFragment.setBottomHeight(h);
                    svga_pk.getLayoutParams().height = h;
                }

            }
        });

        drawerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                try {
                    final float x = ev.getX();
                    final float y = ev.getY();

                    switch (ev.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mLastMotionX = x;
                            mLastMotionY = y;
                            break;

                        case MotionEvent.ACTION_MOVE:
                            //来到新的坐标
                            float endX = ev.getX();
                            float endY = ev.getY();
                            //计算偏移量
                            float distanceX = endX - mLastMotionX;
                            float distanceY = endY - mLastMotionX;
                            if (endY - mLastMotionY > 10 || (Math.abs(distanceX) < Math.abs(distanceY))) {
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                            } else {
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                            }
                        default:
                            break;
                    }
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });
        redpacket.attachRoom(channelId);
    }

    public void klog(String msg) {
        KLog.e("tagpk", msg);
    }

    private boolean isUp; //是否向上滑动
    private int statusDataList = 0;//0 初始状态 1 请求数据中 2 已完成全部请求
    private boolean isSwitchRoom = true;//是否是主动切换直播

    /**
     * 加载滑动下一页的数据
     *
     * @param page
     */
    private void setLoadNextData(int page) {

        //  klog("加载下一页  移除" + preIndex + "更新" + page);
        isPk = false;
        if (null != livingRoomFragment) {
            livingRoomFragment.hidePkResult();
            livingRoomFragment.startLoading(true);
        }
        if (redpacket != null && isOne) {
            //发送红包角标数量给首页
            RxBus.get().post(new RedPacketCountEvent(this.channelId, redpacket.redPacketCount(), -1));
        }
        p.stopMsgSync();
        p.startMsgSync();//消息同步
        if (p != null && preIndex < data.size() && isOne) {
            p.leaveGroup(groupId, data.get(preIndex).channelId);//切换成功时 退出上一个房间
        }
        changeRoomClearAllRes();//滚动调用....一大堆  他这里面会清空网络请求 和


        stopPlay();//停止直播

        preIndex = page;
        klog("滚动后释放资源 开始加载新界面 p=" + preIndex);
        groupId = getIMGroupId(data.get(preIndex).channelId);
        p.joinGroup(groupId, data.get(preIndex).channelId, isSwitchRoom);//加入群 会自动设置toId
        isSwitchRoom = false;
        myAnchorId = data.get(preIndex).channelId;
        redpacket.attachRoom(data.get(preIndex).channelId);
        p.getstickers(myAnchorId);

        LiveNoticeHelper.INSTANCE.getLiveNoticeConfig().setChannelId(data.get(preIndex).channelId);
        //if (tv_level_now != null) tv_level_now.setVisibility(View.GONE);
        if (liveEndView != null) liveEndView.setVisibility(View.GONE);
    }


    private float mLastMotionX;
    private float mLastMotionY;

    /**
     * 切换直播间
     *
     * @param channelId
     */
    @Override
    public void changeRoom(String channelId) {
        changeRoom(channelId, "");
    }

    public void changeRoom(String channelId, String headImage) {
        if (null == p || channelId.equals(myAnchorId + "")) return;
        isPk = false;
        if (null != livingRoomFragment) {
            livingRoomFragment.stopPk();
            //直接切换loading显示头像
            livingRoomFragment.setLoadingHead(headImage);
            livingRoomFragment.startLoading(false);
        }
        if (redpacket != null && isOne) {
            //发送红包角标数量给首页
            RxBus.get().post(new RedPacketCountEvent(this.channelId, redpacket.redPacketCount(), -1));
        }
        changeRoomClearAllRes();
        isSwitchRoom = true;
        p.leaveGroup(groupId, data.get(preIndex).channelId);//切换成功时 退出上一个房间
        if (null != countBackUtils)
            countBackUtils.destory();
        int roomId = Integer.parseInt(channelId);
        int group = GROUP_BASE + roomId;
        LiveNoticeHelper.INSTANCE.getLiveNoticeConfig().setChannelId(roomId);
        //p.joinGroup(group, roomId, true);
        myAnchorId = roomId;
        checkJoinRoom(headImage, roomId, group);
    }

    /**
     * 校验跳转直播间
     * 跳转的直播间在循环列表中，直接滚动到当前列表，如果直播间不在循环列表中，则添加新数据
     *
     * @param headImage
     * @param roomId
     * @param group
     */
    @SuppressLint("CheckResult")
    private void checkJoinRoom(String headImage, int roomId, int group) {
        ZhuboDto zhuboDto = new ZhuboDto();
        zhuboDto.channelId = roomId;
        zhuboDto.headImg = headImage;
        zhuboDto.status = 2;
        Observable.just(zhuboDto)
                .observeOn(Schedulers.io())
                .map(newDto -> {
                    int index = 0;
                    boolean hasNew = true;
                    for (ZhuboDto oldDto : data) {
                        if (newDto.channelId == oldDto.channelId) {
                            hasNew = false;
                            break;
                        }
                        index++;
                    }
                    if (hasNew) {
                        //如果有新数据添加到 data中
                        data.add(preIndex, newDto);
                    }
                    return Pair.create(hasNew, index);
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(pair -> {
                    if (pair.first) {
                        //有新数据更新跳转
                        updateListUI();
                    } else {
                        if (preIndex == pair.second) {
                            //如果是相同下标直接进入
                            p.joinGroup(group, roomId, isSwitchRoom);
                        } else {
                            //无新数据，直接滚动
                            rv.setCurrentItem(data.size() * 100 + pair.second, false);
                        }
                    }
                    isSwitchRoom = false;
                });
    }

    //进入直播间接口
    @Override
    public void changeRoomInfo(EnterLivingRoomDto data) {
        if (null != data.getActivityCpInfoBean() && !TextUtils.isEmpty(data.getActivityCpInfoBean().getCpRank()))//判断是否是cp标识
            this.cpRank = data.getActivityCpInfoBean().getCpRank();

        p.getHour(channelId);//查询小时榜
        redpacket.attachRoom(data.channelId);
        redpacket.refreshPackList();
        LiveNoticeHelper.INSTANCE.getLiveNoticeConfig().setChannelId(data.channelId);
        //飘窗去围观按钮 当前直播间不显示
        if (mGiftPiaopingDto != null) {
            if (mGiftPiaopingDto.getAnchorUserId() == channelId) {
                mTvGoWatch.setVisibility(View.GONE);
                if (mIsShowLong) {
                    toutiaoLefttimeTv.setVisibility(View.VISIBLE);
                }
            } else {
                mTvGoWatch.setVisibility(mIsShowLong ? View.VISIBLE : View.GONE);
            }
        }
        //礼物头条
        if (mGiftBoardcastDto != null) {
            if (mGiftBoardcastDto.channelId == channelId) {
                //  tvAnnouncementGo.setVisibility(View.INVISIBLE);
            } else {
                //tvAnnouncementGo.setVisibility(View.VISIBLE);
            }
        }


        p.getActvity(data.channelId);//查询挂件

    }

    //非pk中调用 大屏播放
    @Override
    public void setNewRoomInfo(EnterLivingRoomDto data) {
        if (livingRoomFragment != null)
            livingRoomFragment.setVedioType(data.type);
        if (null != toutiaoInfoPopwindow) {
            toutiaoInfoPopwindow.setChannelId(data.channelId + "");
        }
        this.data.get(preIndex).pullUrlFLV = data.pullUrlFLV;
        this.data.get(preIndex).nickname = data.nickname;
        this.data.get(preIndex).headImg = data.headImg;

        Message message = Message.obtain();
        message.what = START_PLAY_WHAT;
        message.arg1 = preIndex;
        handle.sendMessage(message);
        //      startNormalPlay();
//        initPlay(preIndex);
        if (data.type == 2) {//TV 直播 增加聊天列表的高度
            ViewGroup.LayoutParams layoutParams = rvChat.getLayoutParams();
            layoutParams.height = (int) getResources().getDimension(R.dimen.dp_300);
            rvChat.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = rvChat.getLayoutParams();
            layoutParams.height = (int) getResources().getDimension(R.dimen.dp_235);
            rvChat.setLayoutParams(layoutParams);
        }

    }

    //更多直播加载数据
    @Override
    public void setZhuboList1(List<ZhuboDto> d, int page) {
        smartRefreshLayout.closeHeaderOrFooter();
        liveRecAdapter.setNewData(d);
    }

    //调用进入直播间前吧view vp 1 隐藏  （应该是为了美观）
    @Override
    public void showNoCancleLoading() {
        super.showNoCancleLoading();
        if (null != viewRightRoot)
            viewRightRoot.setVisibility(View.GONE);
    }

    @Override
    public void hideNoCancleLoading() {
        super.hideNoCancleLoading();
        viewRightRoot.setVisibility(View.VISIBLE);
    }

    @Override
    public void getAttentionStatus() {
        p.getAttentionEach(channelId, false);
    }

    UserGoodBean userGood;

    @Override
    public void updateSdj(UserGoodBean userGood) {
        RxBus.get().post(new RefreshBackpack());
        this.userGood = userGood;

        if (null != userGood) {
            ll_view.setVisibility(View.VISIBLE);
            if (userGood.getNumb() >= userGood.getLottoSockPropCount()) {
                im_sd_lw.setImageResource(R.drawable.pic_sd_wz);
                tv_sd_lw.setVisibility(View.GONE);
                tv_sd_lw.setTextColor(Color.parseColor("#FFF2A9"));
                ll_view.setEnabled(true);

            } else {
                tv_sd_lw.setVisibility(View.VISIBLE);
                im_sd_lw.setImageResource(R.drawable.pic_sdj_cj);
                tv_sd_lw.setTextColor(Color.parseColor("#ffffff"));
                tv_sd_lw.setBackgroundResource(R.drawable.bg_sd_text);
                tv_sd_lw.setText(userGood.getNumb() + "/" + userGood.getLottoSockPropCount());
                ll_view.setEnabled(false);
            }
        } else {
            ll_view.setVisibility(View.GONE);
        }
    }

    private void newYearSmokeWeb() {
        if (!BaseActivity.isFastClick()){
            BigDecimal a = new BigDecimal(userGood.getNumb() + "").divide(new BigDecimal(userGood.getLottoSockPropCount() + ""), 0, BigDecimal.ROUND_DOWN);
            if (a.intValue() > 0) {
                BrowserActivity.startRuleActivityFor(LivingRoomActivity.this, "", userGood.getH5Url() + "?token=" + userProviderService.getToken() + "&chance=" + a.intValue());
                KLog.e("tag", userGood.getH5Url() + "?token=" + userProviderService.getToken() + "&chance=" + a.intValue());
            }
        }



    }


    void initVp() {
        //初始化消息通知
        LinearLayout noticeContainer = findViewById(R.id.v_notice_container);
        LiveNoticeHelper.INSTANCE.setNoticeContainer(this, noticeContainer);
        LiveNoticeHelper.INSTANCE.getLiveNoticeConfig().setChannelId(channelId);
        LiveNoticeHelper.INSTANCE.getLiveNoticeConfig().setLiving(false);
        LiveNoticeHelper.INSTANCE.getLiveNoticeConfig().setChangeRoom(roomId -> {
            changeRoom(roomId + "");
            return null;
        });
        View viewLeft = mInflater.inflate(R.layout.live_item_roomid, null);
        viewRight = mInflater.inflate(R.layout.live_item_room_info, null);
        redpacket = viewRight.findViewById(R.id.v_redpack_icon);
        redpacket.setOnPlayFalling(packetCountBean -> {
            playRedPackFalling(packetCountBean);
            return null;
        });
        initRedPacket();
        ViewStub VsView = viewRight.findViewById(R.id.vs_view);
        VsView.inflate();
        VsView.setVisibility(View.VISIBLE);
        tvRoomId = viewLeft.findViewById(R.id.tv_roomid);
        //tv_level_now = viewLeft.findViewById(R.id.tv_level_now);
        liveEndView = viewLeft.findViewById(R.id.include_layout_live_end);
        rlToutiaoView = viewRight.findViewById(R.id.rl_toutiao_view);
        im_cp = viewRight.findViewById(R.id.im_cp);
        fl_back = viewRight.findViewById(R.id.fl_back);
        viewRightRoot = viewRight.findViewById(R.id.root);
        btn_shop = viewRight.findViewById(R.id.btn_shop);
        ll_view = viewRight.findViewById(R.id.ll_view);

        im_sd_lw = viewRight.findViewById(R.id.im_sd_lw);
        tv_sd_lw = viewRight.findViewById(R.id.tv_sd_lw);
        llNotice = viewRight.findViewById(R.id.ll_notice);
        ll_view_new = viewRight.findViewById(R.id.ll_view_new);
        im_sd_lw_new = viewRight.findViewById(R.id.im_sd_lw_new);
        tv_sd_lw_new = viewRight.findViewById(R.id.tv_sd_lw_new);
        svga_pk = viewRight.findViewById(R.id.svga_pk);

        svga_animation = viewRight.findViewById(R.id.svga_animation);
        tv_hour_my = viewRight.findViewById(R.id.tv_hour_my);

        noticeAnnoucement = viewRight.findViewById(R.id.view_notice);
        tvNoticeAnnouncement = viewRight.findViewById(R.id.tv_notice_title);
        //弹幕
        danmakuView = viewRight.findViewById(R.id.dmk_show_danmu);
        initDanmu();
        //购买守护特效
        viewBuyGuard = viewRight.findViewById(R.id.view_buyguard);
        ivBuyGuard = viewRight.findViewById(R.id.iv_guard);
        tvBuyGuardUserName = viewRight.findViewById(R.id.tv_buy_guard_username);
        tvBuyGuardName = viewRight.findViewById(R.id.tv_buy_guardname);
        //头条
        viewToutiao = viewRight.findViewById(R.id.view_toutiao);
        toutiaoLefttimeTv = viewRight.findViewById(R.id.toutiao_lefttime_tv);
        toutiaoLefttimeView = viewRight.findViewById(R.id.toutiao_lefttime_view);
        toutiaoUseravatar = viewRight.findViewById(R.id.toutiao_useravatar);
        toutiaoGift2 = viewRight.findViewById(R.id.toutiao_gift2);
        toutiaoGiftNum2 = viewRight.findViewById(R.id.toutiao_gift_num2);
        toutiaoLeftviewShort = viewRight.findViewById(R.id.toutiao_leftview_short);
        toutiaoGift = viewRight.findViewById(R.id.toutiao_gift);
        toutiaoGiftNum = viewRight.findViewById(R.id.toutiao_gift_num);
        toutiaoGiftview = viewRight.findViewById(R.id.toutiao_giftview);
        toutiaoContent = viewRight.findViewById(R.id.toutiao_content);
        toutiaoLeftviewLong = viewRight.findViewById(R.id.toutiao_leftview_long);
        toutiao_im_tt = viewRight.findViewById(R.id.toutiao_im_tt);
        toutiaoGift = viewRight.findViewById(R.id.toutiao_gift);
        mTvGoWatch = viewRight.findViewById(R.id.tv_go_watch);  //头条去围观
        tvAnnouncementGo = viewRight.findViewById(R.id.announcement_tv_go);  //礼物去围观
        //通知弹幕背景
        viewAnnoucement = viewRight.findViewById(R.id.view_annoucement);
        tvAnnouncement = viewRight.findViewById(R.id.announcement_tv);
        im_announcement = viewRight.findViewById(R.id.im_announcement);
        giftRoot = viewRight.findViewById(R.id.gift_root);
        rlGiftAnim = viewRight.findViewById(R.id.gift_anim_rl);
        qipaoView = viewRight.findViewById(R.id.live_view);
        frameAnimationView = viewRight.findViewById(R.id.animationView);


        tvWatcherNum = viewRight.findViewById(R.id.tv_watchers_num);
        btnAttention = viewRight.findViewById(R.id.btn_attention);
        talkRoot = viewRight.findViewById(R.id.ll_talk_root);
        viewBottom = viewRight.findViewById(R.id.view_bottom);
        viewBottomInput = viewRight.findViewById(R.id.view_bottom_input);
        tvQuickMsg = viewRight.findViewById(R.id.tv_quick_msg);
//        tagLayoutView = viewBottomInput.findViewById(R.id.tagQuickSdMsg);
        tagLayoutView = viewBottomInput.findViewById(R.id.tagQuickSdMsg);
        ll_view.setTag(R.id.live_id_view_level, 2);
        View.OnTouchListener onTouchListener = ViewExtKt.customClick(ll_view, new Function1<View, Unit>() {
            @Override
            public Unit invoke(View view) {
                newYearSmokeWeb();
                return null;
            }
        });
        livingRoomFrameLayout.addOnCustomTouchListener(ll_view, onTouchListener);
        etInput = viewRight.findViewById(R.id.et_input);
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(etInput.getText().toString())) {
                    btnSend.setImageResource(R.drawable.pic_ends);
                } else {
                    btnSend.setImageResource(R.drawable.pic_end);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        im_moer = viewRight.findViewById(R.id.im_moer);
        rvWatchers = viewRight.findViewById(R.id.rv_watchers);
        btnToutiao = viewRight.findViewById(R.id.btn_toutiao);
//        btnQipaop = viewRight.findViewById(R.id.btn_qipao);
//        ivTipSoap = viewRight.findViewById(R.id.iv_soap_tip);
//        ivCloseSoap = viewRight.findViewById(R.id.iv_close_soap);
        tvCalorific = viewRight.findViewById(R.id.tv_calorific);
        mTvAnchorLevel = viewRight.findViewById(R.id.tv_anchor_level);
        mIvAnchorLevel = viewRight.findViewById(R.id.iv_anchor_level);
        btnSendGift = viewRight.findViewById(R.id.btn_sendgift);
        ivHeader = viewRight.findViewById(R.id.iv_header);
        ivHeadBoard = viewRight.findViewById(R.id.iv_head_board);
        tvGuardNum = viewRight.findViewById(R.id.tv_guard_num);
        btnSend = viewRight.findViewById(R.id.btn_send);
        rvChat = viewRight.findViewById(R.id.rv_chat);
        btnSixin = viewRight.findViewById(R.id.btn_sixin);
        btnShare = viewRight.findViewById(R.id.btn_share);
        btnMore = viewRight.findViewById(R.id.btn_more);
        tvName = viewRight.findViewById(R.id.tv_name);
        swDanmu = viewRight.findViewById(R.id.sw_danmu);
        ivClose = viewBottom.findViewById(R.id.iv_close);
        btnShouhu = viewRight.findViewById(R.id.btn_shouhu);
        flShouhu = viewRight.findViewById(R.id.fl_out_shouhu_people);
        FrameLayout bottom = viewRight.findViewById(R.id.bottom);
        bottom.bringToFront();
        ivShouhuHeader = viewRight.findViewById(R.id.iv_out_shouhu_header);
        //进入直播间飘屏 控件
        llEnter = viewRight.findViewById(R.id.ll_enter);
        tvBayMsg = viewRight.findViewById(R.id.tv_bay_msg);

        ivEnterUserLevel = viewRight.findViewById(R.id.iv_enter_user_level);
        ivEnterVipLevel = viewRight.findViewById(R.id.iv_enter_vip_level);
        tvEnterMsg = viewRight.findViewById(R.id.tv_enter_msg);
        tvEnterMsg = viewRight.findViewById(R.id.tv_enter_msg_activity);
        ivEnterGuard = viewRight.findViewById(R.id.iv_enter_guard);
        ivEnterRoommanager = viewRight.findViewById(R.id.iv_enter_roommanager);
        rlEnterBack = viewRight.findViewById(R.id.rl_enter_back);
        imEnterPic = viewRight.findViewById(R.id.im_enter_pic);
        /////////////告白女神活动
        llEnterActivity = viewRight.findViewById(R.id.ll_enter_activity);
        ivAngel = viewRight.findViewById(R.id.iv_angel);
        tvAngel = viewRight.findViewById(R.id.tv_angel);
        ivLevel = viewRight.findViewById(R.id.iv_level);
        ivActivityTag = viewRight.findViewById(R.id.iv_activity_tag);
        tvHourRank = viewRight.findViewById(R.id.tv_hour_rank);
        llMoreLive = viewRight.findViewById(R.id.ll_more_live);
        ivMoreClose = findViewById(R.id.iv_more_close);
        //红包雨view
        fallingLayout = viewRight.findViewById(R.id.falling_layout);
        //banner
        actBanner = viewRight.findViewById(R.id.act_banner);
        //////////////
        viewRight.findViewById(R.id.v_game).setOnClickListener(this);
        ll_view.setOnClickListener(this);
        im_moer.setOnClickListener(this);
//        ivTipSoap.setOnClickListener(this);
        btn_shop.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnMore.setOnClickListener(this);
        talkRoot.setOnClickListener(this);
        ivHeader.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnSendGift.setOnClickListener(this);
        btnSixin.setOnClickListener(this);
        btnShouhu.setOnClickListener(this);
        flShouhu.setOnClickListener(this);
        btnAttention.setOnClickListener(this);
        btnToutiao.setOnClickListener(this);
//        btnQipaop.setOnClickListener(this);
        viewBottomInput.setOnClickListener(this);
        tvHourRank.setOnClickListener(this);
        llMoreLive.setOnClickListener(this);
        ivMoreClose.setOnClickListener(this);
//        ivCloseSoap.setOnClickListener(this);
        tvQuickMsg.setOnClickListener(this);
        roomInfoViews.add(viewLeft);
        roomInfoViews.add(viewRight);

        swDanmu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isDanmu = b;
                if (b) {
                    tvQuickMsg.setVisibility(View.GONE);
                    etInput.setHint("已开启弹幕模式,10钻/条");
                } else {
                    tvQuickMsg.setVisibility(View.VISIBLE);
                    etInput.setHint("说点什么吧");
                }
            }
        });

        ImageLoader.loadImg(ivHeader, data.get(preIndex).headImg, R.drawable.default_head);
        tvName.setText(data.get(preIndex).nickname);
        nickname = data.get(preIndex).nickname;
        livingRoomInfoPageAdapter = new LivingRoomInfoPageAdapter(roomInfoViews);
        vp.setAdapter(livingRoomInfoPageAdapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //    KLog.e("tag", "position" + position + "positionOffset" + positionOffset + "positionOffsetPixels" + positionOffsetPixels);
                if (positionOffset == 1 || positionOffset == 0) {
                    if (position == 1) {
                        viewRight.setAlpha(1);
//                        frameAnimationView.setVisibility(View.VISIBLE);
//                        carFrameAnimationView.setVisibility(View.VISIBLE);
//                        pkAnimationView.setVisibility(View.VISIBLE);
                    } else {
                        viewRight.setAlpha(0);
                        if (null != silkyAnimation) {
                            silkyAnimation.stop();
                            frameAnimationView.setVisibility(View.GONE);
                        }

//                        carFrameAnimationView.setVisibility(View.GONE);
//                        pkAnimationView.setVisibility(View.GONE);
                    }
                } else {
                    viewRight.setAlpha(positionOffset);
//                   frameAnimationView.setAlpha(positionOffset);
//                   carFrameAnimationView.setAlpha(positionOffset);
//                   pkAnimationView.setAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setDrawerLeftEdgeSize(LivingRoomActivity.this, drawerLayout, 0);
                } else {
                    setDrawerLeftEdgeSize(LivingRoomActivity.this, drawerLayout, 0.4f);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
        //如果被禁言 隐藏私信
        //   btnSixin.setVisibility(userProviderService.getUserInfo().privateLetter == 0 ? View.GONE : View.VISIBLE);
//        handle.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ivTipSoap.setVisibility(View.GONE);
//                ivCloseSoap.setVisibility(View.GONE);
//            }
//        }, 5000);

        //大于三级
        if (!PrivilegeEffectUtils.getInstance().isMsgEnable(userProviderService.getUserInfo().userLevel)) {
            startUpLevelTimer();
        }
//        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                //重新设置聊天列表的高度
//                ViewGroup.LayoutParams layoutParams = rvChat.getLayoutParams();
//                int [] locBottom = new int[2];
//                int [] locDm = new int[2];
//                viewBottom.getLocationOnScreen(locBottom);
//                danmakuView.getLocationOnScreen(locDm);
//                layoutParams.height = locBottom[1]-danmakuView.getHeight()-locDm[1];//高度控制在弹幕 和 底部之间
//                rvChat.setLayoutParams(layoutParams);
//            }
//        });
        initExoPlay();
    }

    //初始化红包
    private void initRedPacket() {
        LinearLayout noticeContainer = findViewById(R.id.v_notice_container);
        //初始化消息通知
        LiveNoticeHelper.INSTANCE.setNoticeContainer(this, noticeContainer);
        LiveNoticeHelper.INSTANCE.getLiveNoticeConfig().setChannelId(channelId);
        LiveNoticeHelper.INSTANCE.getLiveNoticeConfig().setChangeRoom(roomId -> {
            changeRoom(roomId + "");
            return null;
        });
    }


    public void initPlay(int position) {
        initPlay(data.get(position).roomId);
    }

    public void initPlay(String roomId) {
        startGiftThead();//开始循环回调各种动画
        if (null != danmakuView && danmakuView.isPaused()) danmakuView.start(); //弹幕
        if (null != countBackUtils) countBackUtils.destory();//pk倒计时关闭
        tvRoomId.setText(roomId);//vp0 的房间号
    }

    @Override
    public void onPause() {
        super.onPause();
        actBanner.onDestroy();
        danmakuView.clear();
    }


    @Override
    public void setZhuboInfo(EnterLivingRoomDto data) {
        super.setZhuboInfo(data);
        if (liveEndView.getVisibility() != View.VISIBLE) {
            vp.setCurrentItem(1);
        }
        //loading头像设置
        if (livingRoomFragment != null) {
            livingRoomFragment.setLoadingHead(data.headImg);
        }
        this.anchorInfo = data;
        ImageLoader.loadImg(ivHeader, data.headImg);
        tvName.setText(data.nickname);
        nickname = data.nickname;
        if (null != toutiaoInfoPopwindow) {
            toutiaoInfoPopwindow.setNickname(nickname);
        }

        String level = data.hostLevel;   //主播等级
        if (!TextUtils.isEmpty(level)) {
            mTvAnchorLevel.setText(level);
            int level2 = Integer.parseInt(level);
            mIvAnchorLevel.setImageResource(ImageLoader.getAnchorLevel(level2));
            mTvAnchorLevel.setText("LV." + level);
        }
        groupId = getIMGroupId(data.channelId);
        isAttention = data.isAttention > 0;
        p.isAttention = isAttention;
        RxBus.get().post(new AttentionChangeEvent(data.isAttention > 0));
        // 刷新头条飘屏数据
        if (toutiaoInfoPopwindow != null) {
            toutiaoInfoPopwindow.setChannelId(data.channelId + "");
            //    toutiaoInfoPopwindow.resetAnchorData(data.nickname, String.valueOf(data.channelId));
            toutiaoInfoPopwindow.resetAnchorData(data.nickname, String.valueOf(data.channelId));
        }
    }


    private void showSendMsgEdit(String msg) {
        viewBottom.setVisibility(View.GONE);
        viewBottomInput.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(msg)) {
            etInput.setText(msg);
            etInput.setSelection(msg.length());
        }

        etInput.requestFocus();

        KeyBoardUtils.openKeybord(etInput, this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.btn_send) {
            InputMethodManager imm = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
        }
        int id = view.getId();
        if (id == R.id.btn_shop) {//TODO 测试用 上线记得改

            //   fallingLayout.addFallingBody(250);
            //  addActivtiyMp4ToQueue(2);
//            RxBus.get().post(new InvtationDataBean("invtationBean.getData().getAnchor().getHeadImg()",
//                    "invtationBean.getData().getRoom().getRoomId()",
//                    "invtationBean.getData().getAnchor().getNickname()",
//                    "invtationBean.getData().getContent().getValue())"));

            BrowserActivity.start(this, "", (BuildConfig.DEBUG ? Url.TEST_BASE_PAY_URL : Url.BASE_PAY_URL) + "storeHoldingPage?token=" + userProviderService.getToken());
        } else if (id == R.id.ll_talk_root) {//点击发消息
            if (PrivilegeEffectUtils.getInstance().isMsgEnable(userProviderService.getUserInfo().userLevel)) {//不够三级去弹出一个view
                showSendMsgEdit("");
            } else {
//                toastTip(PrivilegeEffectUtils.getInstance().msgLevelNoEnough());
                showUpLevelPop();

            }

        } else if (id == R.id.ll_view) {
            newYearSmokeWeb();
        }

        //点击主播头像  显示弹框展示主播信息页面
        else if (id == R.id.iv_header) {
            //    addPlaneMp4ToQueue();
//            if (!isluzhi) {
//                mLivePlayer.startRecord(TXRecordCommon.VIDEO_RESOLUTION_360_640);
//                mLivePlayer.setVideoRecordListener(new TXRecordCommon.ITXVideoRecordListener() {
//                    @Override
//                    public void onRecordEvent(int i, Bundle bundle) {
////短视频录制事件通知
//                        toastTip(i+"");
//                    }
//
//                    @Override
//                    public void onRecordProgress(long l) {
//                      //  toastTip(msg);
////短视频录制进度
//                    }
//
//                    @Override
//                    public void onRecordComplete(TXRecordCommon.TXRecordResult txRecordResult) {
//                        //短视频录制完成
//                        toastTip("录制完成");
//
//
//
//                    }
//                });
//                isluzhi = !isluzhi;
//            } else {
//                mLivePlayer.stopRecord();
//                isluzhi = !isluzhi;
//            }

            //    getAddNotice("hahahahahahaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",0,"","name");
            //  addPlaneMp4ToQueue();
            //  addNewYearMp4ToQueue(1);
            newUserInfoPopwindow = new NewUserInfoPopwindow(this, channelId, channelId, roomManager);
            newUserInfoPopwindow.showPopupWindow();
//            if (null != attentionPopwindow) {
//                attentionPopwindow.showPopupWindow();
//            }
        } else if (id == R.id.btn_send) {//发消息

            String content = etInput.getText().toString();
            if (TextUtils.isEmpty(content)) {
                return;
            }
//            if (isDanmu && userProviderService.getUserInfo().balance < 10) {
//                KeyBoardUtils.closeKeybord(etInput, this);
//                if (null == noMoneyDialog || !noMoneyDialog.isShowing()) {
//                    noMoneyDialog = DialogUtil.showNoMoneyDialog(this, new DialogUtil.AlertDialogBtnClickListener() {
//                        @Override
//                        public void clickPositive() {
//                            RxBus.get().post(new ShowRechargePopEvent());
//                        }
//
//                        @Override
//                        public void clickNegative() {
//                        }
//                    });
//                }
//                return;
//            }
            p.sendMsg(groupId, content, isDanmu, cpRank);
            etInput.setText("");
            btnSend.setImageResource(R.drawable.pic_end);
        } else if (id == R.id.tv_watchers_num) {//左上角 观看人数排行榜
           // endActivity(17);
            //  getFirstKill(0,"https://fwres.oss-cn-hangzhou.aliyuncs.com/resource/app/video/test/fbred.svga");
            watcherNumPop = new PeoplePopwindow(this, data.get(preIndex).channelId, data.get(preIndex).nickname);
            watcherNumPop.showPopupWindow();
        } else if (id == R.id.btn_sendgift) {//礼物
            acceptGiftUsername = "";
            acceptGiftUserId = -1;//清除 礼物护送参数
            if (null != giftPopWindow) {
                giftPopWindow.setIsWish(false);
                giftPopWindow.setjy(userProviderService.getUserInfo());
                giftPopWindow.showPopupWindow();
            } else {
                p.getGift(false, 0);
            }
        } else if (id == R.id.iv_close) {//关闭
            KeyBoardUtils.closeKeybord(etInput, this);
            leave();
        } else if (id == R.id.btn_attention) {//关注
            p.attention(data.get(preIndex).channelId, false);
            return;
        } else if (id == R.id.btn_shouhu || id == R.id.fl_out_shouhu_people) {//守护
            if (null != shouhuListPopwindow) {
                p.getUserInfo(false, myAnchorId);
            }

            return;
        }
//        else if (id == R.id.iv_close_soap) {   //关闭气泡 提示
//            ivTipSoap.setVisibility(View.GONE);
//            ivCloseSoap.setVisibility(View.GONE);
//        } else if (id == R.id.iv_soap_tip) {
//            ivTipSoap.setVisibility(View.GONE);
//            ivCloseSoap.setVisibility(View.GONE);
//        }
        else if (id == R.id.v_game) {//小游戏
            if (gameDialog == null)
                gameDialog = new GameDialog(this);
            if (!gameDialog.isShowing())
                gameDialog.showDialog(channelId);
        }
//        else if (id == R.id.btn_qipao) {//点击气泡送礼
//            p.sendFeizaoGift(channelId + "");
//        }
//        else if (id == R.id.btn_quicktalk) {
//            if (quickTalkPopwindow == null) {
//                quickTalkPopwindow = new QuickTalkPopwindow(this);
//                quickTalkPopwindow.setOnQuickTalkClickListener(new QuickTalkPopwindow.OnQuickTalkClickListener() {
//                    @Override
//                    public void onClick(String content) {
//                        p.sendMsg(groupId, content, false);
//
//                    }
//                });
//            }
//            if (PrivilegeEffectUtils.getInstance().isMsgEnable(userProviderService.getUserInfo().userLevel)) {
//                quickTalkPopwindow.showPopupWindow();
//            } else {
//                toastTip(PrivilegeEffectUtils.getInstance().msgLevelNoEnough());
//            }
//
//        }

        //
        //直播间 右下角 头条按钮点击事件  头条弹框
        //
        else if (id == R.id.btn_toutiao) {//头条

            toutiaoListPopwindow = new ToutiaoListPopwindow(LivingRoomActivity.this, data.get(preIndex).channelId);
            toutiaoListPopwindow.SetOnShowRuleListener(new ToutiaoListPopwindow.OnShowRuleListener() {
                @Override
                public void onShow(List<String> rules, String giftPackageName, int left, int bottom) {
                     toutiaoRulePopwindow = new ToutiaoRulePopwindow(LivingRoomActivity.this);
                    List<String> msg = new ArrayList<>();
                    for (int i = 0; i < rules.size(); i++) {
                        msg.add(rules.get(i).replaceAll("\n",""));
                    }
                    toutiaoRulePopwindow.setRule(msg, giftPackageName);
                    toutiaoRulePopwindow.showAtLocation(getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, left, bottom);
                }

                @Override
                public void onRuleShow(int left1, int bottom1) {
                     toutiaoRulePopwindow = new ToutiaoRulePopwindow(LivingRoomActivity.this);
                    toutiaoRulePopwindow.showAtLocation(getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, left1 - DensityUtils.dp2px(LivingRoomActivity.this, 50), bottom1);
                }
            });

            toutiaoListPopwindow.showPopupWindow();
        } else if (id == R.id.tv_hour_rank) {//小时榜
            isClick = true;
            p.getHour(channelId);
        } else if (id == R.id.ll_more_live) {//抽屉
            drawerLayout.openDrawer(right_layout);
        } else if (id == R.id.iv_more_close) {
            drawerLayout.closeDrawer(right_layout);
        } else if (id == R.id.tv_quick_msg) {  //快捷
            if (keyBoardShow) {
                viewBottom.setVisibility(View.VISIBLE);
                tagLayoutView.setVisibility(View.VISIBLE);
                KeyBoardUtils.closeKeybord(etInput, LivingRoomActivity.this);
                selfCloseKeyBord = true;
                swDanmu.setChecked(false);
                isDanmu = false;
                etInput.setHint("说点什么吧");
            } else {
                viewBottom.setVisibility(View.GONE);
                viewBottomInput.setVisibility(View.VISIBLE);
                tagLayoutView.setVisibility(View.VISIBLE);
                KeyBoardUtils.openKeybord(etInput, LivingRoomActivity.this);
            }

        } else if (id == R.id.im_moer) {
            showLivePopMore();
        }
//        else if (id == R.id.btn_activity) {
//            if (null == activityPopwindow) {
//                activityPopwindow = new ActivityPopwindow(this);
//            }
//            activityPopwindow.showPopupWindow();
//            showActivityGiftPop();
//        }
    }

    /**
     * 新 直播间更多功能
     */
    private void showLivePopMore() {
        if (null == liveRoomMorewindow) {
            liveRoomMorewindow = new LiveRoomMorewindow(this);
            liveRoomMorewindow.setOnItemClickListener(new LiveRoomMorewindow.OnItemClickListener() {

                @Override
                public void onItemClick(int position) {
                    switch (position) {
                        case 0://私信
                            if (state == 2) {
                                if (userProviderService.getUserInfo().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_YES) {
                                    ArouteUtils.toChatSingleActivity(userProviderService.getUserInfo().fwId, String.valueOf(channelId),
                                            anchorInfo.nickname, anchorInfo.headImg);
                                } else if (userProviderService.getUserInfo().myIdCardWithdraw == UserInfo.IDCARD_STATUS_ING) {
                                    ToastUtils.showShort(LivingRoomActivity.this, "实名认证中,请您耐心等待", Gravity.CENTER);
                                } else if (userProviderService.getUserInfo().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_NO
                                        || userProviderService.getUserInfo().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_NULL) {
                                    showVerifyDialog();
                                }
                            } else {
                                p.getAttentionEach(channelId, true);

                            }
                            break;
                        case 1://分享
                            showShareDialog();
                            break;

                    }
                }
            });
        }
        liveRoomMorewindow.setSixin(type);
        liveRoomMorewindow.showPopupWindow();
    }


    private void showVerifyDialog() {
        CustomerDialog mcd = new CustomerDialog.Builder(this)
                .setTitle("温馨提示")
                .setMsg("您未进行实名认证，\n认证后可使用私信")
                .setPositiveButton("去认证", () -> {
                    ARouter.getInstance().build(ArouterApi.FACE_VERIFY_ACTION)
                            .withInt("status", userProviderService.getUserInfo().getMyIdCardWithdraw())
                            .withInt("type", Common.SKIP_USER)
                            .navigation();
                }).create();
        mcd.show();

    }

    private void startTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    if (mTipsPopWindow != null)
                        mTipsPopWindow.dismiss();
                    if (mTimer != null) {
                        mTimer.cancel();
                    }
                } catch (ConcurrentModificationException e) {

                }
            }
        }, 10 * 1000);
    }

    private void startUpLevelTimer() {
        if (mUpLevelTimer != null) {
            mUpLevelTimer.cancel();
            mUpLevelTimer = null;
        }
        mUpLevelTimer = new Timer();
        mUpLevelTimer.schedule(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showUpLevelPop();
                    }
                });
                if (mUpLevelTimer != null) {
                    mUpLevelTimer.cancel();
                }
            }
        }, 60 * 1000);
    }


    /**
     * 添加显示pop弹窗
     */
    private void showUpLevelPop() {
        if (mTipsPopWindow == null)
            mTipsPopWindow = new BuyGiftTipsPopWindow(this);
        mTipsPopWindow.setOnGiftSendListener(new BuyGiftTipsPopWindow.OnGiftSendListener() {
            @Override
            public void onGiftSend() {
                mTipsPopWindow.dismiss();
                acceptGiftUsername = "";
                acceptGiftUserId = -1;//清除 礼物护送参数
                if (null != giftPopWindow) {
                    giftPopWindow.setIsWish(false);
                    giftPopWindow.setCheckUpLevel();
                    giftPopWindow.setjy(userProviderService.getUserInfo());
                    giftPopWindow.showPopupWindow();
                } else {
                    p.getGift(true, 0);
                }
            }

            @Override
            public void isDismiss() {
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer = null;
                }
            }
        });
        try {
            if (mTipsPopWindow != null && !mTipsPopWindow.isShowing())
                mTipsPopWindow.showPopupWindow();
            startTimer();
        } catch (NullPointerException e) {

        }

    }

    private int time = 125;

    //活动期间 的提示送礼弹框
    private void showActivityGiftPop() {
        if (null == activityGiftPopwindow) {
            activityGiftPopwindow = new ActivityGiftPopwindow(this);
            activityGiftPopwindow.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    int myTime = time;
                    activityCb.countBack(myTime, new CountBackUtils.Callback() {
                        @Override
                        public void countBacking(long time) {

                            long lastTime = (long) SPUtils1.get(LivingRoomActivity.this, "noshowtoday", 0L);
                            if (lastTime != 0 && TimeUtils.isToday(new Date(lastTime))) {
                                if (null != activityCb)
                                    activityCb.destory();
                                return;
                            }
                            if (time == 5 && myTime == time) {
                                if (!endActivity && vp.getCurrentItem() == 1) {
                                    showActivityGiftPop();
                                }


                            }
                        }

                        @Override
                        public void finish() {

                        }
                    });
                }
            });
            activityGiftPopwindow.setOnActivityGiftSendListener(new ActivityGiftPopwindow.OnActivityGiftSendListener() {
                @Override
                public void onSend(GiftDto dto) {
                    if (null != dto) {
                        if (userProviderService.getUserInfo().balance < dto.giftPrice) {

                            if (noMoneyDialog == null || !noMoneyDialog.isShowing()) {
                                noMoneyDialog = DialogUtil.showNoMoneyDialog(LivingRoomActivity.this,
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

                        } else
                            p.sendGift(dto, 1, data.get(preIndex).channelId);
                    }

                }

                @Override
                public void onActivitySend(GiftDto dto, int num) {
                    if (userProviderService.getUserInfo().balance < dto.giftPrice) {
                        if (noMoneyDialog == null || !noMoneyDialog.isShowing()) {
                            noMoneyDialog = DialogUtil.showNoMoneyDialog(LivingRoomActivity.this,
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
                    } else
                        p.sendActivityGift(dto, 1, data.get(preIndex).channelId);
                }
            });
        }
        try {
            if (!activityGiftPopwindow.isShowing())
                activityGiftPopwindow.showPopupWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void leave() {
        if (anchorInfo == null) {
            finish();
        } else {
            leavePopwindow = new LeavePopwindow(this, anchorInfo.headImg, anchorInfo.nickname, anchorInfo.isAttention);
            leavePopwindow.setAttention(isAttention);
            leavePopwindow.setLeavePopClickListener(new LeavePopwindow.LeavePopClickListener() {
                @Override
                public void leaveClick() {
                    leavePopwindow.dismiss();
                    p.leaveGroup(groupId, myAnchorId);
                    finish();
                    leavePopwindow.setLeavePopClickListener(null);
                }

                @Override
                public void attentionClick() {
                    leavePopwindow.dismiss();
                    p.attention(anchorInfo.channelId, false);
                }
            });
            if (leavePopwindow != null)
                leavePopwindow.showPopupWindow();
        }
    }


    @Override
    public void setGuardWindow(int total, ArrayList<GuardListDto.Guard> records) {
        super.setGuardWindow(total, records);

        if (!CommentUtils.isListEmpty(records)) {
            ImageLoader.loadImg(ivShouhuHeader, records.get(0).guardUserHeadImg);
            if (total == 0) {
                tvGuardNum.setVisibility(View.INVISIBLE);
            } else {
                tvGuardNum.setText(total + "");
                tvGuardNum.setVisibility(View.VISIBLE);
            }
            flShouhu.setVisibility(View.VISIBLE);
            btnShouhu.setVisibility(View.GONE);
        } else {
            flShouhu.setVisibility(View.VISIBLE);
            btnShouhu.setVisibility(View.GONE);
            tvGuardNum.setVisibility(View.INVISIBLE);
        }
    }

    //观看列比饿哦
    @Override
    public void setWatchers(List<WatcherDto> dtos) {
        tvWatcherNum.setText(dtos.size() + "");
        tvWatcherNum.setOnClickListener(LivingRoomActivity.this);
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
                    newUserInfoPopwindow = new NewUserInfoPopwindow(LivingRoomActivity.this, data.userId, channelId, roomManager);
                    newUserInfoPopwindow.showPopupWindow();
                }
            });
        } else {
            watchersAdapter.getData().clear();
            watchersAdapter.addData(dtos);
        }
        if (!watchersAdapter.getData().isEmpty())
            rvWatchers.scrollToPosition(0);
    }

    GiftPopWindow.OnGiftSendListener gift = new GiftPopWindow.OnGiftSendListener() {

        @Override
        public void onGiftSend(GiftDto gift, int num) {
            UserInfo userInfo = userProviderService.getUserInfo();
            if (gift.giftPrice * num > userInfo.balance && gift.id > 0) {
                if (noMoneyDialog == null || !noMoneyDialog.isShowing()) {
                    noMoneyDialog = DialogUtil.showNoMoneyDialog(LivingRoomActivity.this,
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
                            } else {
                                id = data.get(preIndex).channelId;
                                p.sendGift(gift, num, id);
                            }
                            LivingRoomActivity.this.hideLoadingDialog();
                        }

                        @Override
                        public void failed() {
                            LivingRoomActivity.this.toastTip("礼物下载失败，请检查您的网络状况,检查是否开启文件权限");
                            LivingRoomActivity.this.hideLoadingDialog();
                        }

                        @Override
                        public void onProgress(int progress) {
                            LivingRoomActivity.this.setDialogProgressPercent(progress + "%");
                        }
                    });
                } else {
                    int id;
                    if (acceptGiftUserId > 0) {
                        id = acceptGiftUserId;
                        p.sendGiftToUser(id, gift, num, acceptGiftUsername, channelId);
                    } else {
                        id = data.get(preIndex).channelId;
                        p.sendGift(gift, num, id);
                    }
                }
            }
            if (null != newUserInfoPopwindow && newUserInfoPopwindow.isShowing()) {
                giftPopWindow.dismiss();
                newUserInfoPopwindow.dismiss();

            }
        }

    };

    AlertDialog noMoneyDialog;


    @Override
    public void setGifts(List<List<GiftDto>> allGifts, boolean isUpLevelTip, int giftid) {
        if (null == giftPopWindow) {
            this.allGifts = allGifts;
            UserInfo userInfo = userProviderService.getUserInfo();
            giftPopWindow = new GiftPopWindow(this, allGifts, userLevelBeans, userInfo,false);
            giftPopWindow.setBalance(userProviderService.getUserInfo());
            requestBackpack(null);
            giftPopWindow.setOnGiftSendListener(gift);
            if (giftid != 0) {
                giftPopWindow.showPopupWindow();
                giftPopWindow.setGiftId(giftid);
            }
        } else {
            if (giftid != 0) {
                giftPopWindow.showPopupWindow();
                giftPopWindow.setGiftId(giftid);
            }
        }
    }
//        @Override
//        public void setGifts(List<List<GiftDto>> allGifts, boolean isUpLevelTip, int giftid) {
//
//            this.allGifts = allGifts;
//            UserInfo userInfo = userProviderService.getUserInfo();
//            giftPopWindow = new GiftPopWindow(this, allGifts, userLevelBeans, userInfo);
//            giftPopWindow.setBalance(userProviderService.getUserInfo());
//            requestBackpack(giftid);
//            giftPopWindow.setOnGiftSendListener(gift);
//            if (isUpLevelTip)
//                giftPopWindow.setCheckUpLevel();
//            if (!isFirstIn) {
//                giftPopWindow.setIsWish(false);
//                giftPopWindow.setjy(userInfo);
//                if (giftid == 0) {
//                    giftPopWindow.showPopupWindow();
//                }
//
//            }
//            isFirstIn = false;
//
//
//        }

    /**
     * 请求背包的数据信息
     */
    public void requestBackpack(GiftDto sendGift) {
        mBackpackService.getBackpack(new LoadingObserver<HttpResult<BackpackDto>>() {
            @Override
            public void _onNext(HttpResult<BackpackDto> d) {
                if (d.isSuccess()) {

                    /***
                     *
                     *
                     * 这个是背包道具数据
                     * 要兼容这个送礼卡片的数据结构
                     *
                     * 目前只有这种方式将里面的数据这样进行转换
                     * 服务端给的数据结构字段还不一样
                     *
                     */
                    List<GiftDto> gifts = new ArrayList<>();
                    List<GiftDto> motors = new ArrayList<>();
                    List<GiftDto> nobilitys = new ArrayList<>();
                    List<GiftDto> props = new ArrayList<>();
                    for (BackpackDto.GiftsBean gift : d.data.gifts) {
                        gifts.add(BeanUtil.copy(gift, GiftDto.class));
                    }

                    for (BackpackDto.GiftsBean motor : d.data.motors) {
                        motors.add(BeanUtil.copy(motor, GiftDto.class));
                    }

                    for (BackpackDto.GiftsBean nobility : d.data.nobilities) {
                        nobilitys.add(BeanUtil.copy(nobility, GiftDto.class));
                    }

                    for (BackpackDto.GiftsBean prop : d.data.props) {
                        props.add(BeanUtil.copy(prop, GiftDto.class));
                    }

                    if (!props.isEmpty()) {
                        props.get(0).giftTypeText = props.get(0).goodsName;
                        putBackpackData(props);
                    }

                    if (!nobilitys.isEmpty()) {
                        nobilitys.get(0).giftTypeText = "贵族";
                        putBackpackData(nobilitys);
                    }

                    if (!motors.isEmpty()) {
                        motors.get(0).giftTypeText = "座驾";
                        putBackpackData(motors);
                    }
                    if (!gifts.isEmpty()) {
                        for (int i = 0; i < gifts.size(); i++) {
                            gifts.get(i).giftTypeText = "礼物";
                        }
                        putBackpackData(gifts);
                    } else {
                        clearBackpackDataByType("礼物");
                    }
                    if (null != giftPopWindow) {
                        giftPopWindow.setBackpackTotal(d.data.totalCount);
                        giftPopWindow.refreshBackpack();
                        ZhuboDto zhuboDto = data.get(preIndex);
                        giftPopWindow.setData(zhuboDto);
                        if (sendGift != null)
                            giftPopWindow.setPackGiftId(sendGift);
                        else if (giftPopWindow.currentGift != null && giftPopWindow.isShowing()) {
                            giftPopWindow.setPackGiftId(giftPopWindow.currentGift);
                        } else if (giftPopWindow.selectGift != null && giftPopWindow.isShowing()) {
                            giftPopWindow.setPackGiftId(giftPopWindow.selectGift);
                            giftPopWindow.selectGift = null;
                        }
                    }

                }
            }

            @Override
            public void _onError(String msg) {

            }
        });
    }


    /**
     * 跟新数据
     *
     * @param data data
     */
    private void putBackpackData(List<GiftDto> data) {
        if (giftPopWindow != null) {
            List<List<GiftDto>> gifts = giftPopWindow.getAllGifts();
            boolean isChoose = false;
            int firstBackIndex = -1;
            for (int i = 0; i < gifts.size(); i++) {
                if (!gifts.get(i).isEmpty() && !TextUtils.isEmpty(gifts.get(i).get(0).goodsId)) {
                    if (firstBackIndex == -1)
                        firstBackIndex = i;
                }
                if (!gifts.get(i).isEmpty() && gifts.get(i).get(0).goodsType == data.get(0).goodsType) {
                    gifts.remove(i);
                    if (null != giftPopWindow.getVpAdapter().get(i)) {
                        giftPopWindow.getVpAdapter().remove(i);
                    }
                    isChoose = true;
                    gifts.add(i, data);
                    break;
                }
            }
            if (!isChoose) {
                if (firstBackIndex == -1)
                    gifts.add(data);
                else
                    gifts.add(firstBackIndex, data);
            }
        }
    }

    private void clearBackpackDataByType(String type) {
        List<List<GiftDto>> gifts = giftPopWindow.getAllGifts();
        for (int i = 0; i < gifts.size(); i++) {
            if (!gifts.get(i).isEmpty() && gifts.get(i).get(0).giftTypeText.equals(type)) {
                gifts.get(i).clear();
                if (null != giftPopWindow.getVpAdapter().get(i)) {
                    giftPopWindow.getVpAdapter().remove(i);
                }
                break;
            }
        }
    }

    public void startNormalPlay() {
        if (null != livingRoomFragment) {
            livingRoomFragment.stopPk();
//            livingRoomAdapter.setVedioType(preIndex, data.get(preIndex).type);
//            livingRoomAdapter.setVedioType(preIndex, 2);
            startPlay(p.enterLivingRoomDto.pullUrlFLV, livingRoomFragment.getTextureView());
            klog("开始啦大屏视频 p=" + preIndex + "流地址" + p.enterLivingRoomDto.pullUrlFLV);
        }
    }

    //18736885525
    private boolean checkGiftEnable(GiftDto gift) {
        if (TextUtils.isEmpty(gift.giftSwf)) {
            return true;
        }
        return DownloadHelper.isAlreadyDownload(gift.giftSwf);
    }

    @Override
    public void setRecharge(List<RechargeDto> data) {
        if (null == rechargePopWindow) {
            rechargePopWindow = new RechargePopWindow(this, data);
            rechargePopWindow.setHuazuan(userProviderService.getUserInfo().balance);
        }
    }

    @Override
    public void setPendantList(PendantListDto data) {

    }


//    @Override
//    public void sendGiftSuccess(GiftDto giftDto, int num) {
//        if (null != giftPopWindow) {
//            giftPopWindow.setBalance(userProviderService.getUserInfo().balance);
//        }
//        if (null != rechargePopWindow) {
//            rechargePopWindow.setHuazuan(userProviderService.getUserInfo().balance);
//        }
//    }

    @Override
    public void updatePkTime(PkTimeDto pkTimeDto) {
        if (pkTimeDto.getEndPkTime() <= 0 && pkTimeDto.getEndPunishTime() < 0) return;
        int time = 0;
        boolean isPk = true;
        if (pkTimeDto.getEndPkTime() > 0) {
            if (pkTimeDto.getEndPkTime() < 1000) {
                pkTimeDto.setEndPkTime(1000);
            }
            time = pkTimeDto.getEndPkTime();
            isPk = true;
        } else if (pkTimeDto.getEndPunishTime() != 0 && pkTimeDto.getEndPkTime() == 0) {
            time = pkTimeDto.getEndPunishTime() - 3000;

            isPk = false;
        }
        if (countBackUtils == null) {//pk倒计时
            countBackUtils = new CountBackUtils();
        }
        final String text = isPk ? "PK中  " : "惩罚中  ";
        countBackUtils.destory();//啥玩意 先new了一个 就清空了？
        klog("开始倒计时 p=" + preIndex + "");
        countBackUtils.countBack(time / 1000, new CountBackUtils.Callback() {
            @Override
            public void countBacking(long time) {
                KLog.e("tag", "pk倒计时" + time);
                livingRoomFragment.uploadTime(text, (int) time);
            }

            @Override
            public void finish() {
                if (text.contains("PK")) {
                    livingRoomFragment.uploadTime("计算中", -1);
                    handle.postDelayed(new MRunnable() {
                        @Override
                        public void run() {
                            if (p != null)
                                p.checkPkResult(channelId, isSingle);
                        }
                    }, 3000);
                } else {
                    endPk();
                }
            }
        });
        if (null != countBackUtils)
            countBackUtils.updateTime(time / 1000);
    }

    /**
     * 刷新pk进度条
     * 进入直播间
     * 查询pk结果
     * 团队pk结果
     * 100357 pk礼物积分
     *
     * @param scoreDto
     */
    @Override
    public void updatePkGiftScore(PkScoreDto scoreDto) {
        if (null == scoreDto) return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(String.valueOf(scoreDto.getTotalPoint()))) {
                    scoreDto.setTotalPoint(0);
                }
                if (TextUtils.isEmpty(String.valueOf(scoreDto.getOtherTotalPoint()))) {
                    scoreDto.setOtherTotalPoint(0);
                }
                if (TextUtils.isEmpty(String.valueOf(scoreDto.getGroupPoint()))) {
                    scoreDto.setGroupPoint(0);
                }
                if (TextUtils.isEmpty(String.valueOf(scoreDto.getOtherGroupPoint()))) {
                    scoreDto.setOtherGroupPoint(0);
                }
                KLog.e("pktag", "" + scoreDto.getTotalPoint());
                livingRoomFragment.uploadScore(scoreDto.getTotalPoint(), scoreDto.getOtherTotalPoint(), scoreDto.getGroupPoint(), scoreDto.getOtherGroupPoint(), isPk);
            }
        });

    }

    @Override
    public void addAttentionSuccess() {

    }

    //pk结束
    @Override
    public synchronized void endPk() {
        if (!isPk) return;
        klog("pk结束");
        stopPlay();
        Message message = Message.obtain();
        message.what = START_PLAY_WHAT;
        message.arg1 = preIndex;
        handle.sendMessage(message);//去开始啦大屏的流
//        initPlay(preIndex);
        isPk = false;
        livingRoomFragment.uploadScore(0, 0, 0, 0, isPk);//pk结束 重置值
        livingRoomFragment.hidePkResult();
        stopCheckPkEndSchedule();//停止线程池
    }

    /**
     * pk结果
     * 正在pk
     * 是否开始pk
     *
     * @param result
     */
    @Override
    public void setSingleMatchResult(MatchTeamResult result) {
//        handle.sendEmptyMessageDelayed(2,200);
        runOnUiThread(new MRunnable() {
            @Override
            public void run() {
                matchPkResultUtil.setMatchSingleResult(result);//设置单人pk 并保存pk数据
                isSingle = true;
                prePk();
            }
        });

    }

    @Override
    public void setTeamMatchResult(MatchTeamResult matchResult) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                matchPkResultUtil.setMatchTeamResult(matchResult);
                isSingle = false;
                prePk();
            }
        });

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
        livingRoomFragment.showSingleProgressView(matchPkResultUtil.matchTeamResult, matchPkResultUtil.matchTeamResult.getUserResult());
    }

    private synchronized void prePk() {//只执行一次
        if (!isActivityResume || isPk) return;
        isPk = true;
        klog("pk调用 p=" + preIndex);
        resetPkAnimView();//更新pk数据
        initPlay(preIndex);//初始化礼物 弹幕  关闭pk倒计时的操作
        livingRoomFragment.startPk(isSingle, matchPkResultUtil);//设置当前adapter的数据
        allRxbus.add(AttentionUtils.isAttention(matchPkResultUtil.getOtherInfo().getUserId(), new LoadingObserver<HttpResult<IsAttentionDto>>() {
            @Override//是否关注对方主播
            public void _onNext(HttpResult<IsAttentionDto> data) {
                if (data.isSuccess()) {
                    livingRoomFragment.isHidePkAttention(data.data.isAttention > 0);
                }
            }

            @Override
            public void _onError(String msg) {

            }
        }));
//                startMinePlayer(livingRoomFragment.getMineVedioView(preIndex), matchPkResultUtil.getUserInfo().getAudienceUrl());
//                startOtherPlayer(livingRoomFragment.getOtherVedioView(preIndex), matchPkResultUtil.getOtherInfo().getAudienceUrl());
        klog("开始拉流 p=" + preIndex + "流地址+" + matchPkResultUtil.getUserInfo().getAudienceUrl());
        //pk啦双方的流
        startPkPlay(matchPkResultUtil.getUserInfo().getAudienceUrl(), matchPkResultUtil.getOtherInfo().getAudienceUrl(), livingRoomFragment.getMineVedioView(), livingRoomFragment.getOtherVedioView());
//pk倒计时
        updatePkTime(new PkTimeDto(matchPkResultUtil.getPkTime(), matchPkResultUtil.getPunishTime()));
        startCheckPkEnd();//使用线程池去刷新pk状态
    }

    /**
     * 大屏
     */
    @Override
    public void onPlayBegin() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivClose.setVisibility(View.VISIBLE);
                livingRoomFragment.hideLoading();
                if (liveEndView.getVisibility() != View.VISIBLE) {
                    vp.setCurrentItem(1);
                }

                liveEndView.setVisibility(View.GONE);

            }
        });

    }


    @Override
    protected void onPlayEnd() {
        super.onPlayEnd();
        livingRoomFragment.showPlayend();
//        initPlay(preIndex);
        Message message = Message.obtain();
        message.what = START_PLAY_WHAT;
        message.arg1 = preIndex;
        handle.sendMessage(message);
//        vp.setCurrentItem(0);
    }

    @Override
    protected void onDestroy() {
        if (redpacket != null) {
            //发送红包角标数量给首页
            RxBus.get().post(new RedPacketCountEvent(this.channelId, redpacket.redPacketCount(), -1));
        }
        danmakuView.removeAllLiveDanmakus();
        danmakuView.setCallback(null);
        danmakuView.setOnDanmakuClickListener(null);
        danmakuView.clear();
        danmakuView.clearDanmakusOnScreen();
        danmakuView.removeAllDanmakus(true);
        mParser.release();
        mDmkContext.unregisterAllConfigChangedCallbacks();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mUpLevelTimer != null) {
            mUpLevelTimer.cancel();
            mUpLevelTimer = null;
        }
        if (null != livingRoomFragment) {
            livingRoomFragment.onDestroy();
        }
        rv.unregisterOnPageChangeCallback(onPageChangeCallback);
//        fragmentList.clear();
        getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks);

        livingRoomFrameLayout.setOnScrollListener(null);
        if (allRxbus != null && allRxbus.size() > 0) {
            allRxbus.clear();
        }
        if (null != quickTalkPopwindow) {
            quickTalkPopwindow.setOnQuickTalkClickListener(null);
            quickTalkPopwindow = null;
        }
        if (null != buyShouhuPopwindow) {
            buyShouhuPopwindow = null;
        }
        if (null != toutiaoListPopwindow) {
            toutiaoListPopwindow.SetOnShowRuleListener(null);
            toutiaoListPopwindow = null;
        }
        if (null != toutiaoInfoPopwindow) {
            toutiaoListPopwindow = null;
        }
        if (null != rechargePopWindow) {
            rechargePopWindow = null;
        }
        if (null != shouhuListPopwindow) {
            shouhuListPopwindow = null;
        }
        if (null != livingRoomFragment) {
            livingRoomFragment.setPkBtnClickListener(null);
            livingRoomFragment.onDestroy();
            livingRoomFragment = null;
            // livingRoomFragment.setOnItemChildClickListener(null);
            //   livingRoomFragment.setPkBtnClickListener(null);

        }
//        if (null != livingRoomPageAdapter) {
//            livingRoomPageAdapter.getFragmentList().clear();
//            livingRoomPageAdapter.destroy();
//        }
        if (null != livingRoomInfoPageAdapter) {
            livingRoomInfoPageAdapter = null;
        }
        if (null != giftPopWindow) {
            giftPopWindow.setOnGiftSendListener(null);
            giftPopWindow.destory();
            giftPopWindow = null;
        }
        if (null != activityGiftPopwindow) {
            activityGiftPopwindow.setOnActivityGiftSendListener(null);
            activityGiftPopwindow.destory();
            activityGiftPopwindow = null;
        }
        // TODO 暂时测试  没测试效果
        if (null != activityCb) {
            activityCb.destory();
            activityCb = null;
        }

        if (null != buyShouhuPopwindow) {
            buyShouhuPopwindow = null;
        }
        if (null != watchersAdapter) {
            watchersAdapter = null;
        }
        if (null != countBackUtils) {
            countBackUtils.destory();
            countBackUtils = null;
        }
        userProviderService = null;
        super.onDestroy();
    }

    @Override
    public void kickedOut() {
//        try {
//            new CustomerDialog.Builder(this)
//                    .setMsg("您被踢出直播间")
//                    .setOutSideCancel(false)
//                    .setNegativeBtnShow(false)
//                    .setPositiveButton(new CustomerDialog.onPositiveInterface() {
//                        @Override
//                        public void onPositive() {
        toastTip("已被踢出");//咱也不知道 这句话为什么要换那么多次 咱也不敢问
        finish();
//                        }
//                    }).create().show();
//        } catch (Exception e) {
//            KLog.e("kikout_error", e.getMessage());
//        }

    }

    private void closeDialog(BasePopupWindow popupWindow) {
        if (popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }

    @Override
    public void liveEnd(CustomLiveEndDto customLiveEndDto) {
        closeAllDialog();
        //   ivClose.setVisibility(View.GONE);
        vp.setCurrentItem(0);

        if (mUpLevelTimer != null) {
            mUpLevelTimer.cancel();
            mUpLevelTimer = null;
        }
        if (null != anchorWishPop) {
            anchorWishPop.dismiss();
        }

        liveEndView.setVisibility(View.VISIBLE);

        ZhuboDto zhuboDto = livingRoomFragment.getMData();
        ImageView ivHead = liveEndView.findViewById(R.id.iv_head);
        TextView tvName = liveEndView.findViewById(R.id.tv_name);
        TextView tvAttention = liveEndView.findViewById(R.id.tv_attention);
        TextView tvLookPeople = liveEndView.findViewById(R.id.tv_look_people);
        TextView tvLick = liveEndView.findViewById(R.id.tv_like);
        TextView tvSure = liveEndView.findViewById(R.id.tv_sure);
        ImageLoader.loadImg(ivHead, zhuboDto.headImg);
        tvName.setText(zhuboDto.nickname);
        tvLookPeople.setText(customLiveEndDto.getLookNums() + "");
        if (customLiveEndDto.getLiveTime() != null) {
            tvLick.setText(TimeUtils.fromSecond(customLiveEndDto.getLiveTime()) + "");
        } else {
            tvLick.setText(TimeUtils.fromSecond(customLiveEndDto.getLiveTimes()) + "");
        }
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvAttention.setBackground(getResources().getDrawable(isAttention ? R.drawable.shape_gray_corner : R.drawable.shape_purple_corner));
        tvAttention.setTextColor(getResources().getColor(isAttention ? R.color.text_66 : R.color.white));
        tvAttention.setText(isAttention ? "已关注" : "关注");

        if (!isAttention) {
            tvAttention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    p.attention(data.get(preIndex).channelId, false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvAttention.setTextColor(getResources().getColor(R.color.text_66));
                            tvAttention.setBackground(getResources().getDrawable(R.drawable.shape_gray_corner));
                            tvAttention.setText("已关注");
                        }
                    });
                }
            });
        }
    }

    private void closeAllDialog() {
        if (gameDialog != null && gameDialog.getH5GameDialog() != null && gameDialog.getH5GameDialog().isShowing()) {
            gameDialog.getH5GameDialog().dismiss();
        }
        if (drawerLayout != null) {
            drawerLayout.closeDrawers();
        }
        if(null!=toutiaoRulePopwindow){
            toutiaoRulePopwindow.dismiss();
        }
        closeDialog(gameDialog);
        closeDialog(hourRankPopWindow);
        closeDialog(rechargePopWindow);
        closeDialog(giftPopWindow);
        closeDialog(activityGiftPopwindow);
        closeDialog(watcherNumPop);
        closeDialog(quickTalkPopwindow);
        closeDialog(toutiaoListPopwindow);
        closeDialog(buyShouhuPopwindow);
        closeDialog(redPackResultDialog);
        closeDialog(mTipsPopWindow);
        closeDialog(liveRoomMorewindow);
        closeDialog(shouhuListPopwindow);
        closeDialog(activityPopwindow);
        closeDialog(newUserInfoPopwindow);
        closeDialog(toutiaoInfoPopwindow);
        closeDialog(pkContributePopwindow);
        closeDialog(anchorWishPop);
        closeDialog(planePopWindow);
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing())
            bottomSheetDialog.dismiss();
    }

    @Override
    public void setCalorific(int calorific) {
        try {
            tvCalorific.setText(DataFormatUtils.formatNumbersHot(calorific));
        } catch (Exception e) {

        }

    }


    @Override
    public void moveUpLoactions(String id, String url, String t, String l, String title, int isDelete, String textColor) {
        if (isDelete == 3) {
            if (pendantLayout != null)
                fl_back.removeView(pendantLayout);
            pendantLayout = null;
        } else {
            BigDecimal w = new BigDecimal(wm.getDefaultDisplay().getWidth() + "");
            BigDecimal h = new BigDecimal(wm.getDefaultDisplay().getHeight() + "");
            BigDecimal lw = w.divide(new BigDecimal("100")).multiply(new BigDecimal(l)).setScale(2, BigDecimal.ROUND_UP);
            BigDecimal lh = h.divide(new BigDecimal("100")).multiply(new BigDecimal(t)).setScale(2, BigDecimal.ROUND_UP);
            BigDecimal end_l = lw.multiply(new BigDecimal("100"));
            BigDecimal end_t = lh.multiply(new BigDecimal("100"));
            if (pendantLayout == null) {
                pendantLayout = LayoutInflater.from(this).inflate(R.layout.add_pandant_view, null, false);
                tvDragContent = pendantLayout.findViewById(R.id.tv_drag_content);
                tvTitle = pendantLayout.findViewById(R.id.tv_title);
                tvTitle.setText(title + "");
                tvTitle.setTag(url);
                ImageLoader.loadImg(tvDragContent, url);
                fl_back.addView(pendantLayout);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.topMargin = end_t.intValue();
                lp.leftMargin = end_l.intValue();
                pendantLayout.setLayoutParams(lp);
                //  layoutParams.setMargins(end_l.intValue(),end_t.intValue(),0,0);

            } else {
                if (!tvTitle.getTag().toString().equals(url)) {
                    tvTitle.setTag(url);
                    ImageLoader.loadImg(tvDragContent, url);
                }
                if (!TextUtils.isEmpty(textColor)) {
                    tvTitle.setTextColor(Color.parseColor(textColor));
                }
                tvTitle.setText(title + "");
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.topMargin = end_t.intValue();
                lp.leftMargin = end_l.intValue();
                pendantLayout.setLayoutParams(lp);
                //  layoutParams.setMargins(end_l.intValue(),end_t.intValue(),0,0);
                //    viewRightRoot.addView(pendantLayout,lp);

            }
        }

    }

    @Override
    public void hostLeave() {
        //tv_level_now.setVisibility(View.VISIBLE);
    }

    @Override
    public void hostComeBack() {
        //tv_level_now.setVisibility(View.GONE);
        LivingRoomTextMsg msg = new LivingRoomTextMsg("主播回来了", LivingRoomTextMsg.TYPE_SYSTEM);
        msg.systemColor = "#ffffff";
        addMsg(msg);
    }

    @Override
    public UserInfo getUserInfo() {
        return userProviderService.getUserInfo();
    }

    @Override
    public GiftPopWindow.OnGiftSendListener getGiftListener() {
        return gift;
    }


    @Override
    public synchronized void showPkResult(PkResultDto pkResultDto, boolean isSingle) {
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isPk) return;
                klog("接口检测或进入直播间查到在pk");
                updatePkTime(new PkTimeDto(0, matchPkResultUtil.getPunishTime()));//更新倒计时
                if (livingRoomFragment == null) return;
                pkResultDto.getSinglePkResultDto().userInfo = matchPkResultUtil.getMatchTeamResult().userInfo;
                pkResultDto.getSinglePkResultDto().objectInfo = matchPkResultUtil.getMatchTeamResult().objectInfo;
                livingRoomFragment.showPkResult(pkResultDto, isSingle, matchPkResultUtil.getPunishTime(), new PkAnimaUtil.MAnimatorListener() {
                    @Override
                    public void start() {

                    }

                    @Override
                    public void end() {
//                        if (!isPk) {
//                            livingRoomFragment.hidePkResult(preIndex);
//                        }
                    }
                });
            }
        }, 200);
//        runOnUiThread(new MRunnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }

    /**
     * pk自己
     */
    @Override
    public void onPkMinePlayBegin() {
        super.onPkMinePlayBegin();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                livingRoomFragment.startPkMineLoading(false);
                livingRoomFragment.hidePkLoading();
            }
        });

    }

    @Override
    public void onPkMinePlayEnd() {
        super.onPkMinePlayEnd();
        livingRoomFragment.startPkMineLoading(true);
        KLog.e("tag", "preIndex=" + preIndex);
        startMinePlayer(livingRoomFragment.getMineVedioView(), pkMineUrl);
    }

    /**
     * pk 对方
     */
    @Override
    public void onPkOtherPlayBegin() {
        super.onPkOtherPlayBegin();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                livingRoomFragment.hidePkLoading();
                livingRoomFragment.startPkOtherLoading(false);
            }
        });

        actBanner.post(new Runnable() {
            @Override
            public void run() {
                actBanner.setVisibilitySVGA(false);
            }
        });

    }

    @Override
    public void onPkOtherPlayEnd() {
        super.onPkOtherPlayEnd();
        startOtherPlayer(livingRoomFragment.getOtherVedioView(), pkOhterUrl);
        // startOtherPlayer(livingRoomAdapter.getOtherVedioView(preIndex), pkOhterUrl);


    }


    @Override
    public void activityNoStart() {
        super.activityNoStart();
        vp.setBackgroundResource(0);
        ivHeadBoard.setBackgroundResource(0);
    }

    /**
     * 进入直播间的系统公告（活动）
     *
     * @param notice
     */
    @Override
    public void activityIng(String notice, String noticeBack) {
        if (TextUtils.isEmpty(notice)) return;
        releaseAnim();

        String str = "                                               公告：" + notice;
        //   tvAnnouncementGo.setVisibility(View.GONE);
        SpannableStringBuilder spannableString = new SpannableStringBuilder(str);
        noticeList.add(new NoticeBean(spannableString, noticeBack, "", "", 0, null, ""));
        annotation();

//        if (activityCb == null) {
//            activityCb = new CountBackUtils();
//        }
//        activityCb.countBack(65, new CountBackUtils.Callback() {
//            @Override
//            public void countBacking(long time) {
//                long lastTime = (long) SPUtils1.get(LivingRoomActivity.this, "noshowtoday", 0L);
//                if (lastTime != 0 && TimeUtils.isToday(new Date(lastTime))) {
//                    activityCb.destory();
//                    return;
//                }
//                if (time == 5) {
//                    showActivityGiftPop();
//                    activityCb.updateTime(65);
//                }
//            }
//
//            @Override
//            public void finish() {
//
//            }
//        });
    }

    /**
     * 进入直播间的系统公告
     *
     * @param notice
     */
    @Override
    public void normalActivityIng(String notice, int id, String color, String name, String noticeBack) {
        if (!TextUtils.isEmpty(notice)) {
            String str = "" + notice;
            //    tvAnnouncementGo.setVisibility(View.GONE);
            SpannableStringBuilder spannableString = new SpannableStringBuilder(str);
            if (!TextUtils.isEmpty(color)) {
                tvAnnouncement.setWht(color);
            } else tvAnnouncement.setWht("#ffffff");

            noticeList.add(new NoticeBean(spannableString, noticeBack, "", "", id, null, ""));
            annotation();
        }
        if (activityCb == null) {
            activityCb = new CountBackUtils();
        }
        // TODO 暂时测试   65  25  提示用户送礼弹窗
//        activityCb.countBack(8, new CountBackUtils.Callback() {
//            @Override
//            public void countBacking(long time) {
//                long lastTime = (long) SPUtils1.get(LivingRoomActivity.this, "noshowtoday", 0L);
//                if (lastTime != 0 && TimeUtils.isToday(new Date(lastTime))) {
//                    if (null != activityCb)
//                        activityCb.destory();
//                    return;
//                }
//                if (time == 5) {
//                    if (!endActivity && vp.getCurrentItem() == 1) {
//                        showActivityGiftPop();
//                    }
//
//
//                }
//            }
//
//            @Override
//            public void finish() {
//
//            }
//        });
    }

    @Override
    public void toRecharge() {
        if (noMoneyDialog == null || !noMoneyDialog.isShowing()) {
            noMoneyDialog = DialogUtil.showNoMoneyDialog(LivingRoomActivity.this,
                    new DialogUtil.AlertDialogBtnClickListener() {
                        @Override
                        public void clickPositive() {

                        }

                        @Override
                        public void clickNegative() {

                        }
                    });
        }
    }

    @Override
    public void activityReward(EnterLivingRoomPkActivityDto activityDto) {
        if (activityDto == null || activityDto.getActivityId() < 3) return;
        ivActivityTag.setVisibility(!TextUtils.isEmpty(activityDto.getChannelInnerLable()) ? View.VISIBLE : View.GONE);
        ImageLoader.loadImg(ivActivityTag, activityDto.getChannelInnerLable());
        ImageLoader.loadUrl(vp, activityDto.getChannelFrame());
    }

    @Override
    public void normalActivityReward(EnterLivingRoomActDto activityDto) {
//        if (activityDto == null) return;
//        ivActivityTag.setVisibility(!TextUtils.isEmpty(activityDto.getChannelInnerLable()) ? View.VISIBLE : View.GONE);
//        ImageLoader.loadImg(ivActivityTag, activityDto.getChannelInnerLable());
//        ImageLoader.loadUrl(vp, activityDto.getChannelFrame());
    }

    @Override
    public void lastActivityReward(LastFrameDto activityDto) {//上个活动奖励阶段
//        if (activityDto.angelActivity > 0) {
//            ivActivityTag.setVisibility(View.VISIBLE);
//            if (TextUtils.isEmpty(activityDto.lastChannelInnerLable)) {
//                ivActivityTag.setVisibility(View.GONE);
//            } else {
//                ImageLoader.loadImg(ivActivityTag, activityDto.lastChannelInnerLable);
//            }
//            if (TextUtils.isEmpty(activityDto.lastChannelInnerLable)) {
//                vp.setBackgroundResource(0);
//            } else {
//                ImageLoader.loadUrl(vp, activityDto.lastChannelInnerFrame);
//            }
//            if (activityDto.angelActivity < 4 && activityDto.activityId == 2) {
//                ivHeadBoard.setBackgroundResource(ImageLoader.getResId("bg_head_act_" + activityDto.angelActivity, R.drawable.class));
//            }
//        } else {
//            ivActivityTag.setVisibility(View.GONE);
//            vp.setBackgroundResource(0);
//            ivHeadBoard.setBackgroundResource(0);
//        }
        if (!TextUtils.isEmpty(activityDto.getLastChannelInnerLable())) {
            ivActivityTag.setVisibility(View.VISIBLE);
            ImageLoader.loadImg(ivActivityTag, activityDto.getLastChannelInnerLable());
        } else {
            ivActivityTag.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(activityDto.getLastChannelInnerFrame())) {
            ImageLoader.loadUrl(vp, activityDto.getLastChannelInnerFrame());
        } else {
            vp.setBackgroundResource(0);
        }
    }


    @Override
    public void getQuickSdMsgList(ArrayList<QuickTalkDto> dtos) {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(LivingRoomActivity.this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        tagLayoutView.setLayoutManager(layoutManager);

        QuickSdMsgListAdapter quickSdMsgListAdapter = new QuickSdMsgListAdapter(dtos);
        tagLayoutView.setAdapter(quickSdMsgListAdapter);
        quickSdMsgListAdapter.setOnItemClickListener((adapter, view, position) -> {
            String tag = ((QuickTalkDto) adapter.getItem(position)).getContent();
            p.sendMsg(groupId, tag, false, cpRank);
            ConstraintLayout.MarginLayoutParams params = (ConstraintLayout.MarginLayoutParams) viewBottomInput.getLayoutParams();
            viewBottom.setVisibility(View.VISIBLE);
            viewBottomInput.setVisibility(View.GONE);
            KeyBoardUtils.closeKeybord(etInput, LivingRoomActivity.this);
            params.bottomMargin = 0;
        });
//        QuickSdMsgListAdapter quickSdMsgListAdapter = new QuickSdMsgListAdapter(LivingRoomActivity.this);
//        tagLayoutView.setAdapter(quickSdMsgListAdapter);
//
//        QuickMsgListAdapter adapter = new QuickMsgListAdapter(dtos);
//        rvQuickMsg.setAdapter(adapter);
//        quickSdMsgListAdapter.setOnTagClickListener(position -> {
//            String tag = quickSdMsgListAdapter.getItem(position);
//            p.sendMsg(groupId, tag, false);
//        });
//        List<String> dataList = StreamSupport.stream(dtos).map(QuickTalkDto::getContent).collect(Collectors.toList());
//        quickSdMsgListAdapter.setTagList(dataList.subList(0, 8));
    }

    public static List<ZhuboDto> removeDuplicate(List<ZhuboDto> list) {
        Set set = new LinkedHashSet<ZhuboDto>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    @SuppressLint("CheckResult")
    @Override
    public void setZhuboList(List<ZhuboDto> data, int page) {
        if (null == liveRecAdapter) {
            recycleviewRight.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

            liveRecAdapter = new LiveRecAdapter(this.data);
            liveRecAdapter.setOnItemClickListener((adapter, view, position) -> {
                if (isFastClick()) return;
                ArrayList<ZhuboDto> data1 = (ArrayList<ZhuboDto>) liveRecAdapter.getData();
                //这里过滤掉position 和 data 对应不上。请求已经过滤，所以不需要过滤
//                ArrayList<ZhuboDto> collect = (ArrayList<ZhuboDto>) StreamSupport.stream(data1)
//                        .filter(e -> e.status == 2)
//                        .collect(Collectors.toList());
                ZhuboDto zhuboDto = data1.get(position);
                if (zhuboDto.status == 2) {
                    if (LivingRoomActivity.this.channelId == zhuboDto.channelId) {
                        toastTip("您已在当前直播间");
                    } else {
                        changeRoom(zhuboDto.channelId + "", zhuboDto.headImg);
                    }
                    // rv.setCurrentItem(data.size() * 100 + position, false);
                } else {
                    ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, zhuboDto.channelId);
                }
            });
            recycleviewRight.setAdapter(liveRecAdapter);
            SmartRefreshLayoutUtils.setTransparentBlackText(this, smartRefreshLayout);
            smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    //p.getZhuboList(1, -1);
                    p.getZhuboList1(1, -1);
                }
            });
            smartRefreshLayout.setEnableLoadMore(false);
//            smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//                @Override
//                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                    //isLoadMore = true;
//                    //int page = LivingRoomActivity.this.page + 1;
//                    p.getZhuboList(LivingRoomActivity.this.page, -1);
//                }
//            });
            p.getZhuboList1(1, -1);
        }
        this.page++;
        if (data.size() == 0) {
            this.page = 1;
            //无更多数据
            smartRefreshLayout.setNoMoreData(true);
            if (statusDataList == 1) {
                //请求最后一条数据状态已结束，更新UI
                preIndex = this.data.size() - 1;
                updateListUI();
            }
            statusDataList = 2;
        } else {
            smartRefreshLayout.setNoMoreData(false);
        }

        smartRefreshLayout.closeHeaderOrFooter();

        drawerLayout.setScrimColor(Color.TRANSPARENT);
        //isLoadMore = false;
        if (data.size() > 0) {
//            for (int i = 0; i < this.data.size(); i++) {
//                for (int j = 0; j < data.size(); j++) {
//                    if (data.get(j).channelId == this.data.get(i).channelId) {
//                        data.remove(data.get(j));
//
//                    }
//                }
//            }
//            List<ZhuboDto> myList = new ArrayList<>();
//            myList.addAll(this.data);
//            myList.addAll(data);
//            this.data = removeDuplicate(myList);
            //  this.data.addAll(data);

            Observable.just(data)
                    .observeOn(Schedulers.io())
                    .map(zhuboDtos -> {
                        List<ZhuboDto> tempDto = new ArrayList<>();
                        synchronized (this.data) {
                            for (ZhuboDto newDto : zhuboDtos) {
                                boolean key = true;
                                for (ZhuboDto oldDto : this.data) {
                                    if ((oldDto.channelId == newDto.channelId)) {
                                        oldDto.update(newDto);
                                        key = false;
                                        break;
                                    }
                                }
                                if (key) {
                                    //没有重复
                                    tempDto.add(newDto);
                                }
                            }
                        }
                        return tempDto;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(zhuboDtos -> {
                        if (zhuboDtos.size() > 0) {
                            this.data.addAll(zhuboDtos);
                            if (BuildConfig.DEBUG) {
                                for (int i = 0; i < this.data.size(); i++) {
                                    KLog.e("tage", "去重后的数据   " + i + "  " + this.data.get(i).nickname);
                                }
                            }
                            if (statusDataList == 1) {
                                //继续加载数据
                                initDrawLayout();
                                return;
                            }
                            // updateListUI();
                            livingRoomPageAdapter.setData(this.data);
                            rv.setCurrentItem(this.data.size() * 100 + getListIndex, false);
                        }
                    });
        }
    }

    //更新列表UI
    private void updateListUI() {
        livingRoomPageAdapter.setData(this.data);
        //livingRoomPageAdapter.notifyDataSetChanged();
        rv.setCurrentItem(this.data.size() * 100 + preIndex, false);
    }

    @Override
    public void setLoadmoreEnable(boolean b) {
        smartRefreshLayout.closeHeaderOrFooter();
        smartRefreshLayout.setNoMoreData(!b);
    }

    boolean isClick = false;

    @Override
    public void getMyHour(MyHourDto data) {
        this.myHourDto = data;
        if (null != myHourDto) {
            tv_hour_my.setText((data.getRank() == 0 || data.getRank() > 50) ? "TOP榜 未上榜" : "TOP榜 第" + data.getRank() + "名");
            if (isClick) {//点击排行榜 显示弹窗   防止第一次进入的时候调取
                isClick = false;
                hourRankPopWindow = new LiveHourRankPopWindow(LivingRoomActivity.this, myHourDto, channelId);
                hourRankPopWindow.showPopupWindow();
            }
        }
    }

    @Override
    public void getPendant(HttpResult<PendantDto> b) {

    }

    @Override
    public void getStickers(HttpResult<List<StickersDto>> b) {
        if (b.data.size() > 0) {
            int sun = b.data.size() - 1;
            p.setUpsticker(b.data.get(sun).getId() + "", b.data.get(sun).getStickerId() + "", b.data.get(sun).getStickerUrl(), b.data.get(sun).getStickerText(), b.data.get(sun).getStickerLocation(), 0, b.data.get(sun).getTextColor());
        } else {
            if (pendantLayout != null)
                fl_back.removeView(pendantLayout);
            pendantLayout = null;
        }

    }


    /**
     * 直播公告窗
     *
     * @param msg
     */
    @Override
    public void getAddNotice(String msg, int id, String color, String name) {
        if (null != noticeView) {
            getEndNotice();
        }
        noticeView = LayoutInflater.from(this).inflate(R.layout.add_room_notice_view, null, false);

        TextView tv_title = noticeView.findViewById(R.id.tv_notice_title);
        TextView tv_notice_go_watch = noticeView.findViewById(R.id.tv_notice_go_watch);
        if (id == 0 || id == channelId) {
            tv_notice_go_watch.setVisibility(View.GONE);
        } else {
            tv_notice_go_watch.setVisibility(View.VISIBLE);
            tv_notice_go_watch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    noticeView.setVisibility(View.GONE);
                    RxBus.get().post(new ChangeRoomEvent(id + ""));
                }
            });
        }
        if (!TextUtils.isEmpty(color)) {
            SpannableStringBuilder spannableString = new SpannableStringBuilder(msg);
            int firstRecIndex = msg.indexOf(name);
            spannableString.setSpan(color, firstRecIndex, firstRecIndex + name.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv_title.setText(spannableString);
        } else {
            tv_title.setText(msg);
        }
        tv_title.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_title.setSingleLine(true);//设置单行显示
                tv_title.setHorizontallyScrolling(true);//横向滚动
                tv_title.setMarqueeRepeatLimit(1);
                tv_title.setSelected(true);//开始滚
                tv_title.setSingleLine(true);
            }
        }, 2500);
        llNotice.addView(noticeView);
        float width = ScreenUtils.getScreenWidth(this) - getResources().getDimension(R.dimen.dp_20);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.dp_54));
        lp.rightMargin = 32;
        lp.leftMargin = 32;
        lp.topMargin = (int) getResources().getDimension(R.dimen.dp_175);
        noticeView.setLayoutParams(lp);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(tv_title, "translationX", width, 0);
        animator1.setInterpolator(new LinearInterpolator());
        AnimatorSet annoucementAnimalSet = new AnimatorSet();
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

                    }
                }, 3000);
                annotationCb = new CountBackUtils();
                annotationCb.countBack(10, new CountBackUtils.Callback() {
                    @Override
                    public void countBacking(long time) {

                    }

                    @Override
                    public void finish() {
                        noticeAnnoucement.setVisibility(View.GONE);

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


        new Handler().postDelayed(new Runnable() {
            public void run() {
                //显示dialog
                try {
                    llNotice.removeView(noticeView);
                } catch (IllegalStateException e) {

                }
            }
        }, 10000);


    }

    @Override
    public void getEndNotice() {
        try {
            llNotice.removeView(noticeView);
            noticeView = null;
        } catch (IllegalStateException e) {

        }
    }

    @Override
    public void setUpdateExperience(Long experience) {
        KLog.e("Level", "experience=" + experience);
        userProviderService.getUserInfo().setExperience(experience.toString());
        KLog.e("Level", "UserLevel=" + userProviderService.getUserInfo().getUserLevel() + "Experience=" + userProviderService.getUserInfo().getExperience());
        userProviderService.setUsetInfo(userProviderService.getUserInfo());
        if (null != giftPopWindow) {
            giftPopWindow.setjys(experience.toString());
        }
    }

    @Override
    public void setActivityInfo(GetActivityInfoDto getActivityInfoDto) {

        if (null == getActivityInfoDto.getProcessDtVOList() || getActivityInfoDto.getProcessDtVOList().size() == 0) {
            return;
        }
        if (null == bottomSheetDialog) {
            bottomSheetDialog = new BottomSheetDialog(LivingRoomActivity.this);

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
            RecyclerView rcv_view = bottomSheetDialog.findViewById(R.id.rcv_view);
            rcv_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            ActivityInfoEndAdapter activityInfoAdapter = new ActivityInfoEndAdapter();
            activityInfoAdapter.setIDeleteListener(new ActivityInfoEndAdapter.IAddListListener() {
                @Override
                public void onClickePosition(GiftDto gift) {
                    if (isFastClick()) {
                        return;
                    }
                    //      p.sendGiftToUser(data.get(preIndex).channelId, gift, 1, acceptGiftUsername, channelId);
                    UserInfo userInfo = userProviderService.getUserInfo();
                    if (gift.giftPrice > userInfo.balance) {
                        if (noMoneyDialog == null || !noMoneyDialog.isShowing()) {
                            noMoneyDialog = DialogUtil.showNoMoneyDialog(LivingRoomActivity.this,
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
//                        GiftBean bean = new GiftBean();
//                        bean.swf = gift.giftSwf;
//                        bean.bigName = GiftDto.getSwfName(gift.giftSwf);
//                        bean.isPlay = true;
//                        bean.userid = userInfo.id + "";
//                        bean.isToutiao = true;
//                        showGiftAnimation(bean);
                        if (!checkGiftEnable(gift)) {
                            showLoadingDialog();

                            DownloadHelper.downloadGift(gift.giftSwf, new DownloadHelper.DownloadListener() {
                                @Override
                                public void completed() {
                                    int id;
                                    boolean toUser = false;
                                    if (acceptGiftUserId > 0) {
                                        id = acceptGiftUserId;
                                        p.sendGiftToUser(id, gift, 1, acceptGiftUsername, channelId);
                                    } else {
                                        id = data.get(preIndex).channelId;
                                        p.sendGift(gift, 1, id);
                                    }
                                    LivingRoomActivity.this.hideLoadingDialog();
                                }

                                @Override
                                public void failed() {
                                    LivingRoomActivity.this.toastTip("礼物下载失败，请检查您的网络状况,检查是否开启文件权限");
                                    LivingRoomActivity.this.hideLoadingDialog();
                                }

                                @Override
                                public void onProgress(int progress) {
                                    LivingRoomActivity.this.setDialogProgressPercent(progress + "%");
                                }
                            });
                        } else {
                            int id;
                            boolean toUser = false;
                            if (acceptGiftUserId > 0) {
                                id = acceptGiftUserId;
                                p.sendGiftToUser(id, gift, 1, acceptGiftUsername, channelId);
                            } else {
                                id = data.get(preIndex).channelId;
                                p.sendGift(gift, 1, id);
                            }
                        }
                        bottomSheetDialog.dismiss();
                    }

                    //    p.sendGift(data.get(preIndex).channelId, gift, 1, acceptGiftUsername, channelId);
                }
            });
            rcv_view.setAdapter(activityInfoAdapter);

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
                                activityInfoAdapter.setNumStatus(getActivityInfoDto.getProcessDtVOList().get(i).getStatus());
                                activityInfoAdapter.setNewData(getActivityInfoDto.getProcessDtVOList().get(i).getGiftItemList());
                                tv_sl.setText("任务收礼（" + getActivityInfoDto.getProcessDtVOList().get(i).getCurrentGiftNum() + "/" + getActivityInfoDto.getProcessDtVOList().get(i).getTargetGiftNum() + "）");
                                rlOne.setBackgroundResource(R.drawable.bg_activityg_gift_select);
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
                                activityInfoAdapter.setNumStatus(getActivityInfoDto.getProcessDtVOList().get(i).getStatus());
                                activityInfoAdapter.setNewData(getActivityInfoDto.getProcessDtVOList().get(i).getGiftItemList());
                                tv_sl.setText("任务收礼（" + getActivityInfoDto.getProcessDtVOList().get(i).getCurrentGiftNum() + "/" + getActivityInfoDto.getProcessDtVOList().get(i).getTargetGiftNum() + "）");
                                rlTwo.setBackgroundResource(R.drawable.bg_activityg_gift_select);
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
                                activityInfoAdapter.setNumStatus(getActivityInfoDto.getProcessDtVOList().get(i).getStatus());
                                activityInfoAdapter.setNewData(getActivityInfoDto.getProcessDtVOList().get(i).getGiftItemList());
                                tv_sl.setText("任务收礼（" + getActivityInfoDto.getProcessDtVOList().get(i).getCurrentGiftNum() + "/" + getActivityInfoDto.getProcessDtVOList().get(i).getTargetGiftNum() + "）");
                                rlThree.setBackgroundResource(R.drawable.bg_activityg_gift_select);
                                break;
                            case 3:
                                im_status_three.setBackgroundResource(R.drawable.pic_activity_wsj_ywc);
                                break;
                        }
                        break;
                }
            }
            if (activityInfoAdapter.getData().size() == 0 && getActivityInfoDto.getProcessDtVOList().size() > 0) {
                activityInfoAdapter.setNumStatus(getActivityInfoDto.getProcessDtVOList().get(getActivityInfoDto.getProcessDtVOList().size() - 1).getStatus());
                activityInfoAdapter.setNewData(getActivityInfoDto.getProcessDtVOList().get(getActivityInfoDto.getProcessDtVOList().size() - 1).getGiftItemList());
                tv_sl.setText("任务收礼（" + getActivityInfoDto.getProcessDtVOList().get(getActivityInfoDto.getProcessDtVOList().size() - 1).getCurrentGiftNum() + "/" + getActivityInfoDto.getProcessDtVOList().get(getActivityInfoDto.getProcessDtVOList().size() - 1).getTargetGiftNum() + "）");
                switch (getActivityInfoDto.getProcessDtVOList().size() - 1) {
                    case 0:
                        rlOne.setBackgroundResource(R.drawable.bg_activityg_gift_select);
                        break;
                    case 1:
                        rlTwo.setBackgroundResource(R.drawable.bg_activityg_gift_select);
                        break;
                    case 2:
                        rlThree.setBackgroundResource(R.drawable.bg_activityg_gift_select);
                        break;

                }
            }

            rlOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activityInfoAdapter.setNumStatus(getActivityInfoDto.getProcessDtVOList().get(0).getStatus());
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
                        activityInfoAdapter.setNumStatus(getActivityInfoDto.getProcessDtVOList().get(1).getStatus());
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
                        activityInfoAdapter.setNumStatus(getActivityInfoDto.getProcessDtVOList().get(2).getStatus());
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

    @Override
    public void endActivity(int activityId) {
        if (null != activityCb) {
            activityCb.destory();
            activityCb = null;
        }
        if (null != actBanner) {
            actBanner.endActivity(activityId);
        }

    }

    @Override
    public void showShouhu(Boolean isHost, int id, UserInfo data) {
        shouhuListPopwindow.setData(isHost, id, data);
        shouhuListPopwindow.showPopupWindow();
    }

    private void setDrawerLeftEdgeSize(Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (drawerLayout == null) return;
        try {
            // 找到 ViewDragHelper 并设置 Accessible 为true
            Field leftDraggerField =
                    drawerLayout.getClass().getDeclaredField("mRightDragger");//Right
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);

            // 找到 edgeSizeField 并设置 Accessible 为true
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);

            // 设置新的边缘大小
            Point displaySize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
            if (displayWidthPercentage == 0) {
                edgeSizeField.setInt(leftDragger, 0);
            } else
                edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (displaySize.x *
                        displayWidthPercentage)));
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }


    @Override
    public void fillWish(int giftId) {
        acceptGiftUsername = "";
        acceptGiftUserId = -1;//清除 礼物护送参数
        if (null != giftPopWindow) {
            giftPopWindow.setIsWish(true);
            giftPopWindow.setGiftId(giftId);
            giftPopWindow.setjy(userProviderService.getUserInfo());
            giftPopWindow.showPopupWindow();
        } else {
            p.getGift(false, 0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int[] leftTop = {0, 0};
        //获取输入框当前的location位置
        viewBottomInput.getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + viewBottomInput.getHeight();
        int right = left + viewBottomInput.getWidth();
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

        tagLayoutView.getLocationInWindow(leftTop);
        int left1 = leftTop[0];
        int top1 = leftTop[1];
        int bottom1 = top1 + tagLayoutView.getHeight();
        int right1 = left1 + tagLayoutView.getWidth();
        int x1 = (int) event.getX();
        int y1 = (int) event.getY();
        if (x1 > left1 && x1 < right1 && y1 > top1 && y1 < bottom1) {
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
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
                ConstraintLayout.MarginLayoutParams params = (ConstraintLayout.MarginLayoutParams) viewBottomInput.getLayoutParams();
                viewBottom.setVisibility(View.VISIBLE);
                viewBottomInput.setVisibility(View.GONE);
                KeyBoardUtils.closeKeybord(etInput, LivingRoomActivity.this);
                params.bottomMargin = 0;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:

                break;
        }
        if (requestCode == 10086 && resultCode == RESULT_OK) {
            if (giftPopWindow != null) {
                String[] anchor = data.getStringArrayExtra("anchor");
                giftPopWindow.mGivingGiftsPopWindow.setData(anchor[0], anchor[1], anchor[2]);

            }
        }
    }

    //开始播放红包雨
    private void playRedPackFalling(PacketCountBean packetBean) {
        if (fallingView != null && fallingView.isFalling()) return;
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
        int packetCount = 100;
        //如果是全栈红包，红包显示的数量由剩余时间来计算
        if (redpacket.isGlobalPacket(packetBean) && redpacket.getResidueTime(packetBean) < 0) {
            long residueTime = RedPackIconView.GLOBAL_COUNTDOWN_TIME + 10_000 - (packetBean.currentTimestamp - packetBean.createDatetimestamp);
            packetCount = (int) (packetCount * residueTime / 10_000f);
//            Log.d("lucas", "packetCount:" + packetCount);
            if (packetCount > 100 || packetCount <= 0) {
                packetCount = 100;
            }
        }
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < packetCount; i++) {
            list.add(i);
        }
        fallingView.setOnFallingListener(new FallingView.OnFallingListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {
                fallingView.setVisibility(View.GONE);
                if (!fallingView.isForceStop() || adapter.getClickIconCount() > 0) {
                    //红包结束查询结果
                    if (redPackResultDialog == null)
                        redPackResultDialog = new RedPackResultDialog(LivingRoomActivity.this);
                    redPackResultDialog.show(packetBean.id);
                }
                //刷新角标
                redpacket.refreshPackList();
            }
        });
        adapter.setData(list);
        fallingView.startFalling();
    }

    @Override
    public void refreshPacketCount() {
        if (redpacket != null) {
            redpacket.refreshPackList();
        }
    }

    @Override
    public void onDropLiveStreamSuccess() {

    }

    @Override
    public void onDropLiveStreamError() {

    }


    //主播下播 切换到下一个直播间
    @Override
    public void setRoomNextData() {
        rv.setUserInputEnabled(false);
    /*    new Handler().postDelayed(new Runnable() {
            public void run() {*/
        if (rv.getCurrentItem() > 0) {
            int index = rv.getCurrentItem() % data.size();
            KLog.e("tage", "滑动到下页 移除" + index + "rv=" + rv.getCurrentItem());

            //   KLog.e("tage", "adapter 移除" +livingRoomPageAdapter.getData().get(rv.getCurrentItem()).nickname) ;
            // livingRoomPageAdapter.notifyItemRemoved(rv.getCurrentItem());//移除上一个下播的直播间
            KLog.e("tage", " 从data中移除" + data.get(index).nickname);
            data.remove(preIndex);//data删除下播的直播间
            livingRoomPageAdapter.setData(data);
            //      livingRoomPageAdapter.notifyItemRemoved(isUp ? rv.getCurrentItem() + 1 : rv.getCurrentItem() - 1);

            // rv.setCurrentItem(rv.getCurrentItem());//切换下一个直播间
            //  livingRoomPageAdapter.notifyDataSetChanged();
            if (isUp) {
                rv.setCurrentItem(this.data.size() * 100 + preIndex - 1, false);
            } else {
                rv.setCurrentItem(this.data.size() * 100 + preIndex + 1, false);
            }

            if (data.size() < 3) {
                initDrawLayout();
            }
            //有主播下播并刷新更多列表
            p.getZhuboList1(1, -1);

        }
        rv.setUserInputEnabled(true);
    /*        }
        }, 1000);*/

    }


    @Override
    public void nickNameClick(@org.jetbrains.annotations.Nullable String s) {
        changeRoom(s);
    }

    @Override
    public void attentionClick(@org.jetbrains.annotations.Nullable String s) {
        p.attention(Integer.parseInt(s), true);
    }

    @Override
    public void showTeamPkResult(@org.jetbrains.annotations.Nullable PkResultDto pkResultDto) {
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                PKGroupResultDialog pkGroupResultDialog = (PKGroupResultDialog) PKGroupResultDialog.getInstance(pkResultDto);
                pkGroupResultDialog.showDF(getSupportFragmentManager(), PKGroupResultDialog.class.getName());
                KLog.e("内存 pk", getSupportFragmentManager().getFragments().size());
            }
        }, 5000);
    }

    @Override
    public void pkContributeClick(int id, boolean isWe) {
        showPkControbutePop(id, false, isWe);
    }
}
