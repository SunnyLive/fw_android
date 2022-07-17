package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_comment.base.BaseEachAttention;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.UserMedalBean;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserMedalService;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.Common;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.EventIntervalUtil;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_comment.widget.RatioImageView;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.eventbus.AttentionChangeEvent;
import com.fengwo.module_live_vedio.eventbus.OpenGuardEvent;
import com.fengwo.module_live_vedio.eventbus.SendGiftToUserEvent;
import com.fengwo.module_live_vedio.eventbus.ShowSendMsgEditEvent;
import com.fengwo.module_live_vedio.mvp.dto.IsBlackDto;
import com.fengwo.module_comment.adapter.GuardAdapter;
import com.fengwo.module_live_vedio.mvp.ui.adapter.LiveLabelAdapter;
import com.fengwo.module_live_vedio.utils.PrivilegeEffectUtils;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import razerdp.basepopup.BasePopupWindow;

public class NewUserInfoPopwindow extends BasePopupWindow {

    @Autowired
    UserProviderService service;
    @Autowired
    UserMedalService userMedalService;
    @BindView(R2.id.tv_jubao)
    TextView tvJubao;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.iv_level1)
    ImageView ivLevel1;
    @BindView(R2.id.iv_anchor_level)
    ImageView ivAnchorLevel; //主播等级

    @BindView(R2.id.iv_anchor_level1)
    ImageView ivAnchorLevel1;//主播勋章列表


    @BindView(R2.id.tv_anchor_level)
    TextView tvAnchorLevel;
    @BindView(R2.id.tv_fengwohao)
    TextView tvFengwohao;
    @BindView(R2.id.tv_location)
    TextView tvLocation;
    @BindView(R2.id.tv_fans)
    TextView tvFans;
    @BindView(R2.id.tv_fengmizhi)
    TextView tvFengmizhi;
    @BindView(R2.id.iv_header)
    RatioImageView ivHeader;
    @BindView(R2.id.btn_sisin)
    TextView btnSisin;
    @BindView(R2.id.btn_addmanager)
    TextView btnAddManager;
    @BindView(R2.id.btn_at)
    TextView btnAt;
    @BindView(R2.id.btn_attention)
    TextView btnAttention;
    @BindView(R2.id.btn_gift)
    TextView btnGift;
    //姓名年龄
    @BindView(R2.id.tv_sex_age)
    TextView mTvSexAge;
    @BindView(R2.id.tv_height)
    TextView mTvHeight;  //身高
    @BindView(R2.id.tv_weight)
    TextView mTvWeight; //体重
    @BindView(R2.id.tv_constellation)
    TextView mTvConstellation; //星座
    @BindView(R2.id.rv_guard)
    RecyclerView mRvGuard;   //守护列表
    @BindView(R2.id.tv_ideal_type)
    TextView mTvIdealType;
    @BindView(R2.id.tv_guard_num)
    TextView mTvGuardNum;   //守护人数
    @BindView(R2.id.tv_today_receive)
    TextView mTvTodayReceive;  //今日花蜜值
    @BindView(R2.id.rl_anchor_guard)
    RelativeLayout mRlAnchorGuard;  //是否主播显示守护相关布局
    @BindView(R2.id.tv_guard_left_time)
    TextView mTvGuardLeftTime;   //守护剩余时间
    @BindView(R2.id.iv_guard)
    ImageView mIvGuard;
    @BindView(R2.id.rv_label)
    RecyclerView mRvLabel;
    @BindView(R2.id.tv_receive_name)
    TextView mTvReceiveName;
    @BindView(R2.id.rl_view)
    RelativeLayout rl_view;
    @BindView(R2.id.iv_headers)
    CircleImageView iv_headers;
    @BindView(R2.id.im_cp)
    ImageView im_cp;


    CustomerDialog customerDialog;
    private Context context;
    ChooseTimePopwindow chooseTimePopwindow;
    private String userId;  // 当前查看用户的uid
    private String userName;
    private int channelId;
    private int attention;//0:未关注，>0:已关注
    private String headImg;
    private Context mContext;

    private UserInfo userInfo;
    private UserMedalBean userMedalBean;//活动勋章信息

    private final CompositeDisposable compositeDisposable;
    private int mRoomManager;
    private Gson gson = new Gson();
    private String fwid;

    public NewUserInfoPopwindow(Context context, int uid, int channelId, int roomManager) {
        super(context);
        mContext = context;
        mRoomManager = roomManager;
        setPopupGravity(Gravity.CENTER);
        ARouter.getInstance().inject(this);
        ButterKnife.bind(this, getContentView());
        this.context = context;
        this.userId = uid + "";
        this.channelId = channelId;
        if (service.getUserInfo().id == uid) {
            findViewById(R.id.ll_bottom).setVisibility(View.GONE);
        }
        compositeDisposable = new CompositeDisposable();

        getUserInfo(userId);

//        if (uid == channelId) {//查看的这个人就是当前主播
//            btnGift.setVisibility(View.GONE);
//        }
        btnSisin.setVisibility(service.getUserInfo().privateLetter == 0 ? View.GONE : View.VISIBLE);

        mRvGuard.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        if (roomManager == 2) {//主播
            btnAddManager.setVisibility(View.VISIBLE);
            btnAt.setVisibility(View.GONE);
        } else if (roomManager == 1) {//是房管
            btnAddManager.setVisibility(View.GONE);
            btnAt.setVisibility(View.VISIBLE);
        } else {//普通用户
            btnAddManager.setVisibility(View.GONE);
            btnAt.setVisibility(View.VISIBLE);
        }
        if (Integer.parseInt(userId) == service.getUserInfo().id) {
            tvJubao.setVisibility(View.GONE);
        }

    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.live_new_pop_userinfo);
        return v;
    }

    private void showChooseTimePop(int type) {
        chooseTimePopwindow = new ChooseTimePopwindow(context, type, channelId, Integer.parseInt(userId));
        chooseTimePopwindow.showPopupWindow();
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    @Override
    public void showPopupWindow() {
        super.showPopupWindow();
    }

    @OnClick({R2.id.btn_gift, R2.id.tv_jubao, R2.id.btn_attention, R2.id.btn_sisin, R2.id.btn_at, R2.id.btn_zhuye, R2.id.btn_addmanager
            , R2.id.iv_close_pop, R2.id.ll_user_info_root, R2.id.iv_header, R2.id.iv_guard, R2.id.ll_pop, R2.id.rl_anchor_guard})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.iv_close_pop || id == R.id.ll_user_info_root) {  //关闭pop
            this.dismiss();
        }
        if (null == userInfo) {
            return;
        }
        if (id == R.id.tv_jubao) {
            showType();
        } else if (id == R.id.btn_attention) {
            btnAttention.setClickable(false);
            if (attention == 0) {
                attention(userId);
            } else {
                removeAttention(userId);
//                ToastUtils.showShort(context, "您已关注");
            }
        } else if (id == R.id.btn_sisin) {
            if (!EventIntervalUtil.canOperate()) return;
            String currentUserId = service.getUserInfo().fwId;
            if (userId.equals(currentUserId)) {
                ToastUtils.showShort(getContext(), "当前用户为您自己");
                return;
            }
            checkoutChat(currentUserId);
        } else if (id == R.id.btn_at) {
            if (PrivilegeEffectUtils.getInstance().isMsgEnable(service.getUserInfo().userLevel)) {
                RxBus.get().post(new ShowSendMsgEditEvent("@" + tvName.getText().toString() + " "));
            } else {
                ToastUtils.showShort(BaseApplication.mApp, PrivilegeEffectUtils.getInstance().msgLevelNoEnough());
            }
            dismiss();
        } else if (id == R.id.btn_zhuye || id == R.id.iv_header) {
            ARouter.getInstance().build(ArouterApi.USER_DETAIL).withInt("id", Integer.parseInt(this.userId)).navigation();
        } else if (id == R.id.btn_addmanager) {
            if (TextUtils.equals("设置房管", btnAddManager.getText().toString())) {
                new CustomerDialog.Builder(context)
                        .setMsg("添加" + tvName.getText().toString() + "为房管")
                        .setPositiveButton(new CustomerDialog.onPositiveInterface() {
                            @Override
                            public void onPositive() {
                                addRoomManager();
                            }
                        }).create().show();

            } else {
                new CustomerDialog.Builder(context)
                        .setTitle("温馨提示")
                        .setMsg("是否取消" + tvName.getText().toString() + "为房管")
                        .setNegativeBtnShow(true)
                        .setPositiveButton(new CustomerDialog.onPositiveInterface() {
                            @Override
                            public void onPositive() {
                                removeRoomManager();
                            }
                        }).create().show();
            }
        } else if (id == R.id.btn_gift) {
            RxBus.get().post(new SendGiftToUserEvent(Integer.parseInt(this.userId), userName));
        } else if (id == R.id.iv_guard) {  //马上守护
            dismiss();
            RxBus.get().post(new OpenGuardEvent(headImg, userName, Integer.parseInt(fwid)));
        } else if (id == R.id.rl_anchor_guard) {


        } else if (id == R.id.ll_pop) {
        }
    }
    private void showType(){
        //判断是否已拉黑
        new RetrofitUtils().createApi(LiveApiService.class)
                .judgeBlack(userId)
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<IsBlackDto>>() {
                    @Override
                    public void _onNext(HttpResult<IsBlackDto> data) {
                        if (data.isSuccess()) {
                            IsBlackDto bean = data.data;

                            ReportPopWindow reportPopWindow = new ReportPopWindow(mContext, mRoomManager, TextUtils.equals(channelId + "", userId),bean.isBlack);
                            reportPopWindow.setOnMineMoreListener(new ReportPopWindow.OnMineMoreListener() {
                                @Override
                                public void onReport() {
                                    ArouteUtils.toPathWithId(ArouterApi.REPORT_ACTIVITY, userId);
                                }

                                @Override
                                public void onBlock() {
                                    new CustomerDialog.Builder(context)
                                            .setMsg("确认把用户" + tvName.getText().toString() + "拉黑吗")
                                            .setPositiveButton(new CustomerDialog.onPositiveInterface() {
                                                @Override
                                                public void onPositive() {
                                                    addBlackList();
                                                }
                                            }).create().show();
                                }

                                @Override
                                public void onForbiddenWords() {
                                    showChooseTimePop(1);
                                }

                                @Override
                                public void onKickOut() {
                                    showChooseTimePop(2);
                                }

                                @Override
                                public void onDelBlock() {
                                    new CustomerDialog.Builder(context)
                                            .setMsg("确认把用户" + tvName.getText().toString() + "恢复吗")
                                            .setPositiveButton(new CustomerDialog.onPositiveInterface() {
                                                @Override
                                                public void onPositive() {
                                                    DelBlackList();
                                                }
                                            }).create().show();
                                }
                            });
                            reportPopWindow.showPopupWindow();
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    private void checkoutChat(String currentUserId) {
        new RetrofitUtils().createApi(LiveApiService.class)
                .getEachAttention(Integer.parseInt(userId))
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult<BaseEachAttention>>() {
                    @Override
                    public void _onNext(HttpResult<BaseEachAttention> data) {
                        if (data.isSuccess() && data.data != null) {
                            L.e("state " + data.data.state);
                            if (data.data.state == 2) {
                                if (service.getUserInfo().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_YES) {
                                    ArouteUtils.toChatSingleActivity(currentUserId, userId, userName, headImg);
                                } else if (service.getUserInfo().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_ING) {
                                    ToastUtils.showShort(context, "实名认证中,请您耐心等待", Gravity.CENTER);
                                } else if (service.getUserInfo().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_NO
                                        || service.getUserInfo().myIdCardWithdraw == UserInfo.MY_ID_CARD_REAL_NULL) {
                                    // 未实名认证
                                    showVerifyDialog();
                                }
                            } else {
                                if (data.data.switchStatus == 0) {
                                    ToastUtils.showShort(context, "此功能升级中");
                                } else {
                                    ToastUtils.showShort(context, "互相关注后才能私信");
                                }
                            }
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context, msg);
                    }
                });
    }

    private void showVerifyDialog() {
        CustomerDialog mcd = new CustomerDialog.Builder(getContext())
                .setTitle("温馨提示")
                .setMsg("您未进行实名认证，\n认证后可使用打招呼")
                .setPositiveButton("去认证", () -> {
                    ARouter.getInstance().build(ArouterApi.FACE_VERIFY_ACTION)
                            .withInt("status", service.getUserInfo().getMyIdCardWithdraw())
                            .withInt("type", Common.SKIP_USER)
                            .navigation();
                }).create();
        mcd.show();
    }

    private void getUserInfo(String uid) {
        int userId = Integer.parseInt(uid) << 3;
        Map map = new HashMap();
        map.put("userId", userId);
        new RetrofitUtils().createApi(LiveApiService.class)
                .getUserinfoByIdNew(createRequestBody(map))
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<UserInfo>>() {
                    @Override
                    public void _onNext(HttpResult<UserInfo> data) {
                        if (data.isSuccess()) {
                            initUserInfo(data.data);
                            userInfo = data.data;
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                });




    }

    @Override
    public void dismiss() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.dismiss();
    }

    private void addRoomManager() {
        new RetrofitUtils().createApi(LiveApiService.class)
                .AddRoomManager(Integer.parseInt(userId))
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        ToastUtils.showShort(context, data.description);
                        if (data.isSuccess())
                            btnAddManager.setText("取消房管");

                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context, msg);
                    }
                });

    }


    private void removeRoomManager() {
        new RetrofitUtils().createApi(LiveApiService.class).removeRoomManager(Integer.parseInt(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingObserver<HttpResult>() {

                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess())
                            btnAddManager.setText("设置房管");

                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }


    private void addBlackList() {
        new RetrofitUtils().createApi(LiveApiService.class)
                .AddBlackList(Integer.parseInt(userId))
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        ToastUtils.showShort(context, data.description);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context, msg);
                    }
                });

    }
    private void DelBlackList() {
        new RetrofitUtils().createApi(LiveApiService.class)
                .DelBlackList(Integer.parseInt(userId))
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        ToastUtils.showShort(context, data.description);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context, msg);
                    }
                });

    }

    //关注
    public void attention(String id) {
        new RetrofitUtils().createApi(LiveApiService.class).addAttention(id)
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {


                        if (data.isSuccess()) {
                            RxBus.get().post(new AttentionChangeEvent(true, false, 1, id));
                            attention = 1;
                            btnAttention.setText("已关注");
                            //   getUserInfo(id);
                        }
                        btnAttention.setClickable(true);
                        ToastUtils.showShort(context, data.description);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });

    }

    //取消关注
    public void removeAttention(String id) {
        new RetrofitUtils().createApi(LiveApiService.class).removeAttention(id)
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {


                        if (data.isSuccess()) {
                            RxBus.get().post(new AttentionChangeEvent(false, false, 0, id));
                            attention = 0;
                            btnAttention.setText("+关注");


                        }
                        btnAttention.setClickable(true);
                        ToastUtils.showShort(context, data.description);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });

    }

    private void initUserInfo(UserInfo data) {
        getUserMedal();
        userName = data.getNickname();
        headImg = data.getHeadImg();
        if (data.userPhotos != null && data.userPhotos.size() > 0) {
            for (int i = 0; i < data.userPhotos.size(); i++) {
                if (data.userPhotos.get(i).photoStatus == 1) {
                    ImageLoader.loadImg(ivHeader, data.userPhotos.get(i).photoUrl);
                    break;
                } else {
                    ImageLoader.loadImg(ivHeader, headImg);
                }
            }
        } else {
            ImageLoader.loadImg(ivHeader, headImg);
        }
        if (!TextUtils.isEmpty(data.cpRank)) {
            rl_view.setVisibility(View.VISIBLE);
            if (Integer.parseInt(data.cpRank) <= 0 && Integer.parseInt(data.cpRank) < 11) {
                int res = ImageLoader.getResId("pic_cp" + data.cpRank, R.drawable.class);
                im_cp.setImageResource(res);
            }
            ImageLoader.loadImg(iv_headers, data.cpHeadImg);
        } else {
            rl_view.setVisibility(View.GONE);
        }

        tvName.setText(data.getNickname());
        int levelRes = ImageLoader.getResId("login_ic_v" + data.getLevel(), R.drawable.class);
        ivLevel1.setImageResource(levelRes);
        if (data.getLiveLevel() > 0) {
            int level2Res = ImageLoader.getResId("login_ic_type3_v" + data.getLiveLevel(), R.drawable.class);
            ivAnchorLevel.setImageResource(level2Res);
        } else {
            ivAnchorLevel.setVisibility(View.GONE);
        }

        if (data.userMedalsList != null && !data.userMedalsList.isEmpty()) {
            ImageLoader.loadImg(ivAnchorLevel1, data.userMedalsList.get(data.userMedalsList.size() - 1).medalIcon);
        }


        int age = TextUtils.isEmpty(data.age) ? 0 : Integer.parseInt(data.age);
        //判断是否是主播
        if (!TextUtils.isEmpty(data.userRole) && data.userRole.contains("ROLE_ANCHOR")) {
            CommentUtils.setSexAndAge(context, 1, data.sex, age, mTvSexAge);
        } else {
            CommentUtils.setSexAndAge(context, 0, data.sex, age, mTvSexAge);
        }
        if (!TextUtils.isEmpty(data.getLocation()) && data.getLocation().contains("/")) {
            tvLocation.setText(data.getLocation().replace("/", "."));
        } else {
            tvLocation.setText(data.getLocation());
        }
        fwid = data.getFwId();
        tvFengwohao.setText("蜂窝号: " + data.getFwId());
        tvFans.setText("粉丝" + DataFormatUtils.formatNumbers(data.getFans()));
        tvFengmizhi.setText((TextUtils.isEmpty(data.todayReceive) ? "0" : (DataFormatUtils.formatNumberGift(Integer.parseInt(data.todayReceive)))) + "花蜜值");
        attention = data.getIsAttention();
        if (attention >= 1) {
            btnAttention.setText("已关注");
        } else {
            btnAttention.setText("+关注");
        }
        mTvTodayReceive.setText("花蜜值 " + DataFormatUtils.formatNumbers(data.getReceive()));
        mTvGuardNum.setText(DataFormatUtils.formatNumbers(data.userGuardNum) + "人");
        if (!TextUtils.isEmpty(data.idealTypeTag)) {
            mTvIdealType.setText("#" + data.idealTypeTag.replace(",", " #"));  //理想型
        }
        mTvConstellation.setText(data.getConstellation());
        mTvHeight.setText(TextUtils.isEmpty(data.height) ? "" : data.height + "cm");
        mTvWeight.setText(TextUtils.isEmpty(data.weight) ? "" : data.weight + "kg");


        if (data.myIsGuard) {
            mIvGuard.setVisibility(View.GONE);
            mTvGuardLeftTime.setVisibility(View.VISIBLE);
            mTvGuardLeftTime.setText("剩余: " + data.myGuardDeadline + "天");
        } else {
            mIvGuard.setVisibility(View.VISIBLE);
            mTvGuardLeftTime.setVisibility(View.GONE);
        }

        if (data.id == service.getUserInfo().getId()) {
            mIvGuard.setVisibility(View.GONE);
        }

        //判断是否是主播
        if (!TextUtils.isEmpty(data.userRole) && data.userRole.contains("ROLE_ANCHOR")) {
            mRlAnchorGuard.setVisibility(View.VISIBLE);
            ivAnchorLevel.setVisibility(View.VISIBLE);
            tvAnchorLevel.setVisibility(View.VISIBLE);
            mTvReceiveName.setText("今日花蜜值");
        } else {
            mRlAnchorGuard.setVisibility(View.GONE);
            ivAnchorLevel.setVisibility(View.GONE);
            tvAnchorLevel.setVisibility(View.GONE);
            mTvReceiveName.setText("今日贡献榜");
        }
        btnAddManager.setText(data.isManage == 0 ? "设置房管" : "取消房管");

        List<UserInfo.UserGuardList> userGuardLists;
        if (data.userGuardList != null && data.userGuardList.size() > 3) {
            userGuardLists = data.userGuardList.subList(0, 3);
        } else {
            userGuardLists = data.userGuardList;
        }
        GuardAdapter guardAdapter = new GuardAdapter(userGuardLists);
        mRvGuard.setAdapter(guardAdapter);

        //直播标签
        if (!TextUtils.isEmpty(data.liveLabel)) {
            mRvLabel.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            LiveLabelAdapter liveLabelAdapter = new LiveLabelAdapter();
            mRvLabel.setAdapter(liveLabelAdapter);
            String tagName = data.getLiveLabel();
            if (!TextUtils.isEmpty(tagName)) {
                String[] split = tagName.split(",");
                List<String> tables = Arrays.asList(split);
                liveLabelAdapter.setNewData(tables);
            }
        }
    }

    private void getUserMedal() {
        userMedalService.getUserMedal(Integer.parseInt(userId), 0, new LoadingObserver<HttpResult<UserMedalBean>>() {
            @Override
            public void _onNext(HttpResult<UserMedalBean> data) {
                userMedalBean = data.data;
//                if (userMedalBean.medalOneId>0)
//                    ivActivityTag.setImageResource(ImageLoader.getResId("ic_medal_"+userMedalBean.medalOneId,R.drawable.class));
//                if (userMedalBean.medalOneId == 12){ //蜂国蜂后
//                    ivHeadBoard.setBackgroundResource(ImageLoader.getResId("bg_head_act_"+1,R.drawable.class));
//                }else if (userMedalBean.medalOneId == 13){//蜂国储君
//                    ivHeadBoard.setBackgroundResource(ImageLoader.getResId("bg_head_act_"+2,R.drawable.class));
//                }else if (userMedalBean.medalOneId == 14){//蜂国郡主
//                    ivHeadBoard.setBackgroundResource(ImageLoader.getResId("bg_head_act_"+3,R.drawable.class));
//                }
//                ivHeadBoard.setBackgroundResource(ImageLoader.getResId("bg_head_act_"+3,R.drawable.class));
            }

            @Override
            public void _onError(String msg) {

            }
        });

    }

    public RequestBody createRequestBody(Map map) {
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        return requestBody;
    }

}
