package com.fengwo.module_main.mvp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocation;
import com.fengwo.module_chat.base.RefreshUnReadMessageEvent;
import com.fengwo.module_chat.mvp.ui.fragment.ChatHomeFragment;
import com.fengwo.module_chat.mvp.ui.fragment.MessageHomeFragment;
import com.fengwo.module_chat.utils.chat_new.ChatGreenDaoHelper;
import com.fengwo.module_chat.utils.chat_new.ChatWebSocketDelegate;
import com.fengwo.module_chat.utils.chat_new.NoticeWebSocketDelegate;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.MapLocationUtil;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.bean.VensionDto;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.dialog.UpdateAppDialog;
import com.fengwo.module_comment.event.BannedEvent;
import com.fengwo.module_comment.event.LogoutEvent;
import com.fengwo.module_comment.event.UndateUnReadMessageEvent;
import com.fengwo.module_comment.event.UserOnlineEvent;
import com.fengwo.module_comment.event.VisitorRecordEvent;
import com.fengwo.module_comment.helper.GlobalMsgHelper;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.ActivitysManager;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RedPacketConfigUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_flirt.UI.BaseIliaoFragment;
import com.fengwo.module_flirt.UI.activity.HostChatRoomActivity;
import com.fengwo.module_flirt.bean.ReconWbBean;
import com.fengwo.module_flirt.manager.FlirtSocketManager;
import com.fengwo.module_live_vedio.eventbus.BannedPostEvent;
import com.fengwo.module_live_vedio.helper.LiveNoticeHelper;
import com.fengwo.module_live_vedio.mvp.ui.fragment.LiveHomeFragment;
import com.fengwo.module_login.eventbus.BackToMainEvent;
import com.fengwo.module_login.mvp.ui.fragment.MineFragment;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_main.R;
import com.fengwo.module_main.R2;
import com.fengwo.module_main.mvp.IView.IMainView;
import com.fengwo.module_main.mvp.activity.adapter.FragmentVpAdapter;
import com.fengwo.module_main.mvp.presenter.MainPresenter;
import com.fengwo.module_websocket.EventConstant;
import com.fengwo.module_websocket.FWWebSocket1;
import com.fengwo.module_websocket.FWWebSocketWenBo;
import com.fengwo.module_websocket.Url;
import com.taobao.sophix.SophixManager;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;


@Route(path = ArouterApi.MAIN_MAIN_ACTIVITY)
public class MainActivity extends BaseMvpActivity<IMainView, MainPresenter> implements IMainView {
    @BindView(R2.id.vp)
    ViewPager vp;
    @BindView(R2.id.view_live)
    View viewLive;
    @BindView(R2.id.view_vedio)
    View viewVedio;
    @BindView(R2.id.view_message)
    View viewMessage;
    @BindView(R2.id.view_social)
    View viewSocial;
    @BindView(R2.id.view_mine)
    View viewMine;
    @BindView(R2.id.tv_live)
    TextView tvLive;
    @BindView(R2.id.tv_vedio)
    TextView tvVedio;
    @BindView(R2.id.tv_message)
    TextView tvMessage;
    @BindView(R2.id.tv_social)
    TextView tvSocial;
    @BindView(R2.id.tv_mine)
    TextView tvMine;
    @BindView(R2.id.tv_badge)
    TextView tvBadge;
    @BindView(R2.id.btn_devio)
    LinearLayout btnDevio;

    private Fragment vedioFragment, chatFragemnt, liveFragment, mineFragment, agentFragment, messageHomeFragment;

    @Autowired
    String id;

    private FragmentVpAdapter fragmentPagerAdapter;
    //  private MainPopupWindow popupWindow;

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    private List<View> views;
    private List<TextView> TextViews;

    private int prePosition = 0;

    FWWebSocket1.OnSocketConnectListener onSocketConnectListener;
    private ChatGreenDaoHelper daoHelper;
    private CompositeDisposable allRxBus;
    private FWWebSocketWenBo.OnSocketConnectListener onWenBoSocket;
    //private FlirtSocketManager flirtSocketManager;
    private BaseIliaoFragment flirtHomeFragment;
    private Disposable mDisposable;
    private UpdateAppDialog mUpdateAppDialog;

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        daoHelper = ChatGreenDaoHelper.getInstance();
        checkHotFix();
        fullScreen(this);
        new RxPermissions(this).request(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECORD_AUDIO
        )
                .compose(bindToLifecycle())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            initLocal();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        btnDevio.setVisibility(CommentUtils.isOpenFlirt ? View.VISIBLE : View.GONE);
        liveFragment = new LiveHomeFragment();//(Fragment) ARouter.getInstance().build(ArouterApi.LIVE_FRAGMENT_HOME).navigation();
//        vedioFragment = new VideoHomeFragment();//(Fragment) ARouter.getInstance().build(ArouterApi.VEDIO_FRAGMENT_HOME).navigation();
        //vedioFragment = new FlirtFragment();//(Fragment) ARouter.getInstance().build(ArouterApi.VEDIO_FRAGMENT_HOME).navigation(); 旧i撩
        //
        flirtHomeFragment = new BaseIliaoFragment();
        messageHomeFragment = new MessageHomeFragment();
//        agentFragment = new AgentFragment();
//        vedioFragment = new NewVideoFragment();//(Fragment) ARouter.getInstance().build(ArouterApi.VEDIO_FRAGMENT_HOME).navigation();
        chatFragemnt = new ChatHomeFragment();//(Fragment) ARouter.getInstance().build(ArouterApi.CHAT_FRAGMENT_HOME).navigation();
        mineFragment = new MineFragment();//(Fragment) ARouter.getInstance().build(ArouterApi.LOGIN_FRAGMENT_MINE).navigation();

        fragmentList.add(liveFragment);
        if (CommentUtils.isOpenFlirt) fragmentList.add(flirtHomeFragment);
        //       fragmentList.add(vedioFragment);
        fragmentList.add(messageHomeFragment);
        fragmentList.add(chatFragemnt);
        fragmentList.add(mineFragment);
        fragmentPagerAdapter = new FragmentVpAdapter(getSupportFragmentManager(), fragmentList);
        vp.setAdapter(fragmentPagerAdapter);
        vp.setOffscreenPageLimit(fragmentList.size() - 1);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onPageSelected(int position) {
                views.get(prePosition).setEnabled(true);
                views.get(position).setEnabled(false);
                TextViews.get(prePosition).setEnabled(true);
                TextViews.get(position).setEnabled(false);
                prePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        views = new ArrayList<>();
        TextViews = new ArrayList<>();
        viewLive.setEnabled(false);
        views.add(viewLive);
        if (CommentUtils.isOpenFlirt) views.add(viewVedio);
        views.add(viewMessage);
        views.add(viewSocial);
        views.add(viewMine);
        TextViews.add(tvLive);
        if (CommentUtils.isOpenFlirt) TextViews.add(tvVedio);
        TextViews.add(tvMessage);
        TextViews.add(tvSocial);
        TextViews.add(tvMine);
        tvLive.setEnabled(false);


        startWebSocektService();

        //获取未读消息数
        getUnReadCount();
        L.e("jpush----", JPushInterface.getRegistrationID(this) + "\n" + JPushInterface.getUdid(this));
        UserInfo user = UserManager.getInstance().getUser();
        if (user != null) {
            if (Url.BASE_URL.contains("test")) {
                JPushInterface.setAlias(this, 1, "test" + user.id);
            } else if (Url.BASE_URL.contains("dev")) {
                JPushInterface.setAlias(this, 1, "dev" + user.id);
            } else {
                JPushInterface.setAlias(this, 1, "prod" + user.id);
            }
        }

        RxBus.get().toObservable(BannedEvent.class).compose(bindToLifecycle()).subscribe(bannedEvent ->
        {
            String content = TextUtils.isEmpty(bannedEvent.content) ? "您已被禁播" : bannedEvent.content;
            CommonDialog.getInstance(content, true, "知道了", "")
                    .show(getSupportFragmentManager(), "您已被禁播");
        });
        RxBus.get().toObservable(LogoutEvent.class).compose(bindToLifecycle()).subscribe(new Consumer<LogoutEvent>() {
            @Override
            public void accept(LogoutEvent logoutEvent) throws Exception {
                finish();
            }
        });

        //最近访客记录 所有地方在这里提交
        RxBus.get().toObservable(VisitorRecordEvent.class).compose(bindToLifecycle()).subscribe(visitorRecordEvent -> p.addVisitorRecord(visitorRecordEvent));
        p.loginlog();//启动时调用在线统计接口
        //用户在线状态 所有地方在这里提交
        RxBus.get().toObservable(UserOnlineEvent.class).compose(bindToLifecycle()).subscribe(userOnlineEvent -> {
            if (userOnlineEvent.onLine) {
                p.loginlog();
            } else {
                p.userOfflineLog();
            }
        });

        // 未读消息数
        RxBus.get().toObservable(UndateUnReadMessageEvent.class)
                .compose(bindToLifecycle()).subscribe(object -> {
            if (tvBadge == null) return;
            tvBadge.setVisibility(object.num > 0 ? View.VISIBLE : View.GONE);
            tvBadge.setText(object.num > 99 ? "99+" : String.valueOf(object.num));

        }, Throwable::printStackTrace);

//        Intent i = new Intent(this, DownGiftService.class);
//        startService(i);
        p.getGiftList();
        //是否开启青少年模式
        if (!TextUtils.equals(Constants.TEENAGER_MODE_ENABLE, user.teenagerMode)) {
            p.getAppVension();//检查版本更新
        }
//        p.loginlog();
//        p.wenboIsReconnent();
        p.wenboAnchor();
        p.getWenboTip();

        Boolean firstBuild = (Boolean) SPUtils1.get(this, "buildMessageListFirstInstall" + UserManager.getInstance().getUser().id, true);
        if (firstBuild)
            p.buildMessageListFirstInstall(UserManager.getInstance().getUser().id);//首次安装 构建本地消息默认列表

        initEvent();
        initDialog();
        //更新红包配置
        RedPacketConfigUtils.Companion.getInstance().initConfigData();
    }

    //请求热修复
    private void checkHotFix() {
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    private void initEvent() {

        mDisposable = RxBus.get().toObservable(BackToMainEvent.class)
                .compose(bindToLifecycle())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((event -> {
                    if (event.getBackEvent() == BackToMainEvent.BACK_TO_MAIN) {
                        showLiveFragment();
                    } else if (event.getBackEvent() == BackToMainEvent.BACK_TO_ILIAO) {
                        showFlirtHomeFragment();
                    }
                }));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //约会助手传过来数据
        int pos = intent.getIntExtra("pos", 0);
        if (prePosition == pos) {
            return;
        }
        vp.setCurrentItem(pos, false);
    }

    /**
     * 获取地址
     */
    private void initLocal() {
        //确保地图回调成功才能操作 否则会有问题
//        showLoadingDialog();
        MapLocationUtil.getInstance().startLocationForOnce(new MapLocationUtil.LocationListener() {
            @Override
            public void onLocationSuccess(AMapLocation location) {
                p.upLoading(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), location.getCity());
            }

            @Override
            public void onLocationFailure(String msg) {
                p.upLoading("0", "0", "");
            }
        });
    }

    /**
     * get unRead message count
     */
    private void getUnReadCount() {
        //收到通知请求消息列表
        allRxBus = new CompositeDisposable();
        allRxBus.add(RxBus.get().toObservable(RefreshUnReadMessageEvent.class)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindToLifecycle()).subscribe(object -> p.getUnReadMessageCount(UserManager.getInstance().getUser().id),
                        Throwable::printStackTrace));


    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserManager.getInstance().getUser() != null) {
            p.getUnReadMessageCount(UserManager.getInstance().getUser().id);
        }
    }

    private void startWebSocektService() {
        if (UserManager.getInstance().getUser() == null) return;
        FWWebSocket1.getInstance().init(UserManager.getInstance().getUser().id,UserManager.getInstance().getToken());
        GlobalMsgHelper.INSTANCE.bindMsg();
        onSocketConnectListener = new FWWebSocket1.OnSocketConnectListener() {
            @Override
            public void onMessage(String playLoad) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        L.e("OnSocketConnectListener", "run: " + this.hashCode());
                        handSocketMsg(playLoad);
                    }
                });
            }

            @Override
            public void onReconnect() {

            }
        };
        FWWebSocket1.getInstance().setOnConnectListener(onSocketConnectListener);
//        FWWebSocket1.getInstance().sendLoginChatMessage(UserManager.getInstance().getUser().id);
        //flirtSocketManager = FlirtSocketManager.init();
        FlirtSocketManager.getInstance().connect(UserManager.getInstance().getUser().id);
    }

    private void handSocketMsg(String msg) {

        try {
            JSONObject object = new JSONObject(msg);
            UserInfo userInfo = UserManager.getInstance().getUser();
            if (object.has("data") && object.getJSONObject("data").has("busiType")
                    && object.getJSONObject("data").getInt("busiType") == 1) {// 是聊天WebSocket
                try {
                    ChatWebSocketDelegate.handleChatMessage(msg, String.valueOf(UserManager.getInstance().getUser().id),
                            UserManager.getInstance().getUser().privateLetter, daoHelper);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            }
            int eventId = Integer.parseInt(object.getString("eventId"));
            switch (eventId) {
                case EventConstant.CMD_WISH_MSG:
                    LiveNoticeHelper.INSTANCE.sendNotice(msg);
                    break;
                case EventConstant.platform_forbiden:
                    JSONObject jsObj = object.getJSONObject("data");
                    long time = jsObj.getLong("time");

                    UserManager.getInstance().setClosureTime(time);
                    break;
                case EventConstant.un_platform_forbiden:
                    UserManager.getInstance().setClosureTime(0);
                    break;
                case EventConstant.banned_login:
                    doLogoout();
                    break;
                case EventConstant.private_letter:
                    JSONObject letterObj = object.getJSONObject("data");
                    int type = letterObj.getInt("type");
                    int userId = letterObj.getInt("userId");
                    if (userId != userInfo.getId()) return;
                    userInfo.privateLetter = type;
                    L.e("-------- " + userInfo.privateLetter);
                    UserManager.getInstance().setUserInfo(userInfo);
                    RxBus.get().post(new BannedPostEvent(type));
                    break;
                case EventConstant.interact_event:// 互动通知
                case EventConstant.recent_visitor_event:// 最近访客通知
                case EventConstant.system_event://系统消息
                case EventConstant.greet_event://打招呼
                case EventConstant.comment_event:
                case EventConstant.official_news:
                    NoticeWebSocketDelegate.handleChatMessage(msg, String.valueOf(UserManager.getInstance().getUser().id), daoHelper);
                    break;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (null != UserManager.getInstance().getUser()) {
            boolean isNightMode = (boolean) SPUtils1.get(this, "IS_MODE_NIGHT" + UserManager.getInstance().getUser().id, false);
            DarkUtil.setDarkTheme(isNightMode ? MODE_NIGHT_YES : MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        FloatingView.getInstance().onDestroy();
        super.onDestroy();
        if (FWWebSocket1.getInstance() != null) {
            FWWebSocket1.getInstance().setOnConnectListener(null);
            FWWebSocket1.getInstance().removeListener(onSocketConnectListener);
            FWWebSocket1.getInstance().closeWebSocket();
            FWWebSocket1.getInstance().destroy();
        }
        flirtHomeFragment.onDestroy();
        FlirtSocketManager.getInstance().onDestroy();
        if (allRxBus != null && allRxBus.size() > 0) {
            allRxBus.clear();
        }
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.main_activity_home;
    }

    @Override
    public MainPresenter initPresenter() {
        return new MainPresenter();
    }


    @OnClick({R2.id.btn_live, R2.id.btn_social, R2.id.btn_message, R2.id.btn_devio, R2.id.btn_mine})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_live) {
            if (prePosition == 0) {
                return;
            }
            showLiveFragment();
        } else if (id == R.id.btn_devio) {
            if (prePosition == 1) {
                return;
            }
            showFlirtHomeFragment();
        } else if (id == R.id.btn_message) {
            if (prePosition == fragmentList.size() - 3) {
                return;
            }
            showMessageFragment();
        } else if (id == R.id.btn_social) {
            if (prePosition == fragmentList.size() - 2) {
                return;
            }
            showChatFragment();
        } else if (id == R.id.btn_mine) {

            if (prePosition == fragmentList.size() - 1) {
                return;
            }
            showMineFragment();
        }
//        else if (id == R.id.btn_center_menu) {
//            MainMenuPopup popup = new MainMenuPopup(this);
//            popup.showPopupWindow();
//        }
    }

    private void showLiveFragment() {
        vp.setCurrentItem(0, false);
    }

    private void showFlirtHomeFragment() {
        vp.setCurrentItem(1, false);
    }

    private void showMessageFragment() {
        vp.setCurrentItem(fragmentList.size() - 3, false);

    }

    private void showChatFragment() {
        vp.setCurrentItem(fragmentList.size() - 2, false);
    }

    private void showMineFragment() {
        vp.setCurrentItem(fragmentList.size() - 1, false);
    }

    private long mExitTime;

    //双击退出app
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                toastTip("再按一次就真的退出了哦~");
                mExitTime = System.currentTimeMillis();
            } else {
                ActivitysManager.getInstance().popAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void doLogoout() {
        UserManager.getInstance().clear();
//        RxBus.get().post(new LogoutEvent());
        FWWebSocket1.getInstance().destroy();
        FWWebSocketWenBo.getInstance().destroy();
        ArouteUtils.toPathWithId(ArouterApi.LOGIN_ACTIVITY, "banned");
        finish();
    }

    @Override
    public void setVension(VensionDto vension) {
        if (mUpdateAppDialog == null) {
            mUpdateAppDialog = new UpdateAppDialog().showDialog(this, vension);
        }
    }


    @Override
    public void reconnec(ReconWbBean reconWbBean) {
        if (reconWbBean.isLive) {
            new CustomerDialog.Builder(this).setTitle("提示")
                    .setMsg("由于您上次异常断开直播，是否需要继续上次直播")
                    .setPositiveButton("需要", new CustomerDialog.onPositiveInterface() {
                        @Override
                        public void onPositive() {
                            HostChatRoomActivity.start(MainActivity.this, reconWbBean.startLiveVO);
                        }
                    }).setNegativeButton("不需要", new CustomerDialog.onNegetiveInterface() {
                @Override
                public void onNegetive() {
                    p.rejectReconnect();
                }
            }).setOutSideCancel(false)
                    .create().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_UPDATE_APP_VERSION) {
            if (mUpdateAppDialog != null) {
                mUpdateAppDialog.reInstallApk(this);
            }
        }
    }
}
