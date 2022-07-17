package com.fengwo.module_login.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.taobao.windvane.util.CommonUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.Interfaces.IRealNameInterceptService;
import com.fengwo.module_comment.MapLocationUtil;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.Common;
import com.fengwo.module_comment.utils.CopyUtils;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.IntentRoomActivityUrils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.widget.MenuItem;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_live_vedio.mvp.ui.activity.LivingRoomActivity;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.GiftWallDto;
import com.fengwo.module_login.mvp.dto.MyCarDto;
import com.fengwo.module_login.mvp.dto.WatchHistoryDto;
import com.fengwo.module_login.mvp.ui.activity.AccountRecordActivity;
import com.fengwo.module_login.mvp.ui.activity.AgentActivity;
import com.fengwo.module_login.mvp.ui.activity.AttentionActivity;
import com.fengwo.module_login.mvp.ui.activity.ChongzhiActivity;
import com.fengwo.module_login.mvp.ui.activity.ComplaintActivity;
import com.fengwo.module_login.mvp.ui.activity.FansActivity;
import com.fengwo.module_login.mvp.ui.activity.FriendActivity;
import com.fengwo.module_login.mvp.ui.activity.HuafenActivity;
import com.fengwo.module_login.mvp.ui.activity.LivingTimeActivity;
import com.fengwo.module_login.mvp.ui.activity.MineDetailActivity;
import com.fengwo.module_login.mvp.ui.activity.MonthGiftWallActivity;
import com.fengwo.module_login.mvp.ui.activity.MyAccountActivity;
import com.fengwo.module_login.mvp.ui.activity.MyCarActivity;
import com.fengwo.module_login.mvp.ui.activity.MyFavouriteActivity;
import com.fengwo.module_login.mvp.ui.activity.MyGonghuiActivity;
import com.fengwo.module_login.mvp.ui.activity.MyGuardActivity;
import com.fengwo.module_login.mvp.ui.activity.NobilityPrivilegeActivity;
import com.fengwo.module_login.mvp.ui.activity.ProfitActivity;
import com.fengwo.module_login.mvp.ui.activity.RankDetailActivity;
import com.fengwo.module_login.mvp.ui.activity.ScanCodeActivity;
import com.fengwo.module_login.mvp.ui.activity.SettingActivity;
import com.fengwo.module_login.mvp.ui.activity.ViewsHistoryActivity;
import com.fengwo.module_login.mvp.ui.adapter.MineGiftWallAdapter;
import com.fengwo.module_login.mvp.ui.adapter.MineGuardAdapter;
import com.fengwo.module_login.mvp.ui.adapter.WatchHistoryAdapter;
import com.fengwo.module_login.mvp.ui.pop.QrCodePopwindow;
import com.fengwo.module_login.utils.UserManager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

;

@Route(path = ArouterApi.LOGIN_FRAGMENT_MINE)
public class OldMineFragment extends BaseMvpFragment {

    @BindView(R2.id.iv_header)
    ImageView ivHeader;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.btn_code)
    ImageView btnCode;
    @BindView(R2.id.tv_fans)
    TextView tvFans;
    @BindView(R2.id.tv_attention)
    TextView tvAttention;
    @BindView(R2.id.tv_friends)
    TextView tvFriends;
    @BindView(R2.id.tv_huazuan)
    TextView tvHuazuan;
    @BindView(R2.id.tv_fengmizhi)
    TextView tvFengmizhi;
    @BindView(R2.id.tv_huafen)
    TextView tvHuafen;
    @BindView(R2.id.tv_benifit)
    TextView tvBenifit;
    @BindView(R2.id.rl_guild)
    RelativeLayout mRlGuid;  //我的公会
    @BindView(R2.id.tv_guild_tip)
    TextView mTvGuidTip;
    @BindView(R2.id.iv_guild_pic)
    CircleImageView mIvGuidPic;
    @BindView(R2.id.btn_wenbo_record)
    MenuItem btnWenboRecord;

    @BindView(R2.id.btn_toguizu)
    MenuItem btnToguizu;
    @BindView(R2.id.btn_toshichang)
    MenuItem btnToshichang;
    @BindView(R2.id.ll_is_live)
    LinearLayout mLlIsLive;  //实名认证是主播
    @BindView(R2.id.rl_history)
    RelativeLayout mRlHistory;

    @BindView(R2.id.btn_toshoucang)
    MenuItem btnToshoucang;
    @BindView(R2.id.btn_torenzheng)
    MenuItem btnTorenzheng;
    @BindView(R2.id.btn_tosetting)
    MenuItem btnTosetting;
    @BindView(R2.id.btn_tofans)
    LinearLayout btnTofans;
    @BindView(R2.id.btn_toattention)
    LinearLayout btnToattention;
    @BindView(R2.id.btn_tofriend)
    LinearLayout btnTofriend;


    @BindView(R2.id.iv_level1)
    ImageView ivLevel1;
    @BindView(R2.id.iv_level2)
    ImageView ivLevel2;
    @BindView(R2.id.iv_level3)
    ImageView ivLevel3;
    @BindView(R2.id.iv_medal)
    ImageView ivMedal;

    @BindView(R2.id.iv_regimental)
    ImageView mIvPhotoFrame;   //相框

    @BindView(R2.id.btn_zhubo_level)
    MenuItem btnLevel;
    @BindView(R2.id.view1)
    View view1;
    @BindView(R2.id.view2)
    View view2;
    @BindView(R2.id.btn_date_record)
    MenuItem btnDataRecord;
    @BindView(R2.id.smartrefreshlayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R2.id.ll_tohuami)
    LinearLayout ll_tohuami;
//   @BindView(R2.id.btn_torenzheng_flirt)
//    MenuItem btnTorenzhengFlirt;
    @BindView(R2.id.iv_mine_home_bg)
    ImageView ivMineHomeBg;
    @BindView(R2.id.tv_id)
    TextView mTvId;  //蜂窝号
    @BindView(R2.id.fl_agent)
    FrameLayout flAgent;
    //姓名年龄
    @BindView(R2.id.tv_sex_age)
    TextView mTvSexAge;
    //守护布局
    @BindView(R2.id.tv_guard_tip)
    TextView mTvGuardTip;
    @BindView(R2.id.rv_guard)
    RecyclerView mRvGuard;
    @BindView(R2.id.rv_history)
    RecyclerView mRvHistory;    //历史记录列表
    @BindView(R2.id.rv_history2)
    RecyclerView mRvHistory2;
    @BindView(R2.id.tv_today_live_time)
    TextView mTvTodayLiveTime;  //今日直播时长
    @BindView(R2.id.tv_today_receive)
    TextView mTvTodayReceive;   //花蜜值
    @BindView(R2.id.iv_mine_car)  //我的座驾
            ImageView mIvMineCar;
    @BindView(R2.id.tv_mine_car_tip)
    TextView mTvMineCarTip;
    @BindView(R2.id.rv_gift)
    RecyclerView mRvGift;
    @BindView(R2.id.tv_gift_tip)
    TextView mTvGiftTip;
    @BindView(R2.id.btn_scan)
    ImageView mIvScan;
    @BindView(R2.id.iv_setting)
    ImageView mIvSetting;

    @BindView(R2.id.btn_expert_info)
    MenuItem mIvExpertInfo;       //达人认证的 columns

    @Autowired
    IRealNameInterceptService mRealNameInterceptService; //获取实名认证服务

    private boolean isFirst = true;
    private boolean isFragmentVisiable;
    private UserInfo userInfo;
    protected int page = 1;
    protected final String PAGE_SIZE = ",3";

    private RetrofitUtils mRetrofitUtils;
    private WatchHistoryAdapter mWatchHistoryAdapter;

    @Override
    protected int setContentView() {
        ARouter.getInstance().inject(this);
        return R.layout.login_fragment_mine;
    }

    private static final String TAG = "MineFragiv_settingment";

    @SuppressLint("CheckResult")
    @OnClick({R2.id.iv_header,
            R2.id.tv_name,
            R2.id.btn_code,
            R2.id.rl_guard,
            R2.id.rl_guild,
            R2.id.rl_car,
            R2.id.btn_toguizu,
            R2.id.btn_toshichang,
            R2.id.rl_history,
            R2.id.btn_history,
            R2.id.btn_toshoucang,
            R2.id.btn_torenzheng,
           /* R2.id.btn_torenzheng_flirt,*/
            R2.id.btn_tosetting,
            R2.id.btn_tofans,
            R2.id.btn_toattention,
            R2.id.btn_tofriend,
            R2.id.btn_toprofit,
            R2.id.btn_myaccount,
            R2.id.btn_tohuafen,
            R2.id.btn_scan,
            R2.id.btn_complaint,
            R2.id.btn_zhubo_level,
            R2.id.fl_agent,
            R2.id.ll_tohuami,
            R2.id.btn_torecord,
            R2.id.btn_tochongzhi,
            R2.id.btn_date_record,
            R2.id.btn_wenbo_record,
            R2.id.iv_setting,
            R2.id.tv_id_copy,
            R2.id.rl_gift,
            /*R2.id.btn_city_wide,*///同城资料设置
            R2.id.btn_expert_info //达人认证的columns
    })
    public void onViewClicked(View view) {
        if (isFastClick()) return;
        int id = view.getId();
        if (id == R.id.btn_tofriend || id == R.id.btn_toattention || id == R.id.btn_tofans) {
            MapLocationUtil.getInstance().startLocationForOnce(new MapLocationUtil.LocationListener() {
                @Override
                public void onLocationSuccess(AMapLocation location) {
                    if (id == R.id.btn_tofriend) {
                        FriendActivity.startActivity(getActivity(), location);
                    } else if (id == R.id.btn_toattention) {
                        AttentionActivity.startActivity(getActivity(), location);
                    } else if (id == R.id.btn_tofans) {
                        FansActivity.startActivity(getActivity(), location);
                    }
                }

                @Override
                public void onLocationFailure(String msg) {
                    if (id == R.id.btn_tofriend) {
                        FriendActivity.startActivity(getActivity(), null);
                    } else if (id == R.id.btn_toattention) {
                        AttentionActivity.startActivity(getActivity(), null);
                    } else if (id == R.id.btn_tofans) {
                        FansActivity.startActivity(getActivity(), null);
                    }
                }
            });
        } else if (id == R.id.iv_header) {
//            startActivity(EdittextInfoActivity.class);
//            startActivity(MineDetailActivity.class);
            if (userInfo != null)
                MineDetailActivity.startActivityWithUserId(getActivity(), userInfo.getId());
        } else if (id == R.id.tv_name) {
        } else if (id == R.id.btn_code) {
//            startActivity(MyCodeActivity.class);
            QrCodePopwindow popwindow = new QrCodePopwindow(getActivity());
            popwindow.showPopupWindow();
        } else if (id == R.id.rl_guard) {
            startActivity(MyGuardActivity.class);
        } else if (id == R.id.rl_guild) {
//            if (null != UserManager.getInstance().getUser().familyApplyStatus && UserManager.getInstance().getUser().familyApplyStatus > 0) {
//                startActivity(MyGonghuiActivity.class);
//            } else {
//                startActivity(GonghuiActivity.class);
//            }

            startActivity(MyGonghuiActivity.class);


        } else if (id == R.id.tv_id_copy) {
            if (CopyUtils.copy2Board(Objects.requireNonNull(getActivity()), mTvId.getText().toString())) {
                toastTip("复制成功");
            } else toastTip("复制失败");
        } else if (id == R.id.rl_car) {
            startActivity(MyCarActivity.class);
        } else if (id == R.id.btn_toguizu) {
            startActivity(NobilityPrivilegeActivity.class);
        } else if (id == R.id.btn_toshichang) {
            LivingTimeActivity.getInstance(getActivity(), 1);
        } else if (id == R.id.rl_history || id == R.id.btn_history) {
            startActivity(ViewsHistoryActivity.class);
        } else if (id == R.id.btn_toshoucang) {
            startActivity(MyFavouriteActivity.class);
        } else if (id == R.id.btn_torenzheng) {
            mRealNameInterceptService.showRealName(getActivity(), 1,true);
//            ARouter.getInstance().build(ArouterApi.FACE_VERIFY_ACTION)
//                    .withInt("status", UserManager.getInstance().getUser().myIsCardStatus)
//                    .withInt("type", Common.SKIP_ANCHOR)
//                    .navigation();
            //RealNameActivity.start(getActivity(), userInfo.getMyIsCardStatus());
        } else if (id == R.id.btn_tosetting || id == R.id.iv_setting) {   //设置
            startActivity(SettingActivity.class);
        } else if (id == R.id.btn_toprofit || id == R.id.ll_tohuami) {
            ProfitActivity.start(getContext());
        } else if (id == R.id.btn_myaccount) {
            startActivity(MyAccountActivity.class);
        } else if (id == R.id.btn_tohuafen) {
            startActivity(HuafenActivity.class);
        } else if (id == R.id.btn_scan) {
            startActivity(ScanCodeActivity.class);
        } else if (id == R.id.btn_complaint) {
            startActivity(ComplaintActivity.class);
        } else if (id == R.id.btn_zhubo_level) {
            startActivity(RankDetailActivity.class);
        } else if (id == R.id.fl_agent) {
            startActivity(AgentActivity.class);
        } else if (id == R.id.btn_torecord) {
            startActivity(AccountRecordActivity.class);
        } else if (id == R.id.btn_tochongzhi) {
            startActivity(ChongzhiActivity.class);
        } //else if (id == R.id.btn_torenzheng_flirt) {
        //ARouter.getInstance().build(ArouterApi.FLIRT_CERTIFICATION).navigation();
        //}
        else if (id == R.id.btn_date_record) {
            ARouter.getInstance().build(ArouterApi.FLIRT_DATERECORD).navigation();
        } else if (id == R.id.btn_wenbo_record) {
            LivingTimeActivity.getInstance(getActivity(), 2);
        } else if (id == R.id.rl_gift) {   //礼物墙
            if (userInfo != null) {
                Intent intent = new Intent(getActivity(), MonthGiftWallActivity.class);
                intent.putExtra("userId", userInfo.getId());
                startActivity(intent);
            }
        }
        //同城资料设置
/*        else if (id == R.id.btn_city_wide) {
            mRealNameInterceptService.showRealName(getActivity(), 2,true);
        }*/
        //达人认证
        else if (id == R.id.btn_expert_info){
            mRealNameInterceptService.showRealName(getActivity(), 2,true);
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        if (DarkUtil.isDarkTheme(getActivity())) {
            ivMineHomeBg.setVisibility(View.GONE);
            smartRefreshLayout.setBackgroundColor(Color.parseColor("#191326"));
            Drawable drawable = mIvScan.getDrawable();
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(wrappedDrawable, null);
            mIvScan.setImageDrawable(wrappedDrawable);
            Drawable drawable1 = mIvSetting.getDrawable();
            Drawable wrappedDrawable1 = DrawableCompat.wrap(drawable1);
            DrawableCompat.setTintList(wrappedDrawable1, null);
            mIvSetting.setImageDrawable(wrappedDrawable1);
        }
        SmartRefreshLayoutUtils.setTransparentBlackText(getActivity(), smartRefreshLayout);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                updateUser();
                smartRefreshLayout.closeHeaderOrFooter();
            }
        });
        mRvGuard.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRvHistory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRvHistory2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRvGift.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mWatchHistoryAdapter = new WatchHistoryAdapter();
        mRvHistory.setAdapter(mWatchHistoryAdapter);
        mRvHistory2.setAdapter(mWatchHistoryAdapter);
        mWatchHistoryAdapter.setOnItemChildClickListener(((adapter, view, position) -> {
            WatchHistoryDto item = (WatchHistoryDto) adapter.getItem(position);
            if (item.getStatus() == 2) {
                ArrayList<ZhuboDto> list = new ArrayList<>();
                ZhuboDto zhuboDto = new ZhuboDto();
                zhuboDto.channelId = item.getChannelId();
                list.add(zhuboDto);
                showFVExitDialog(list, position);
            }  else {
                MineDetailActivity.startActivityWithUserId(getContext(), item.getChannelId());
            }
        }));
        initUserInfo(UserManager.getInstance().getUser());

    }

    private void initUserInfo(UserInfo userInfo) {
        getWatchHistory();
        getGiftWall(page + PAGE_SIZE, UserManager.getInstance().getUser().getId());
        getMyCar();
        this.userInfo = userInfo;
        if (null == this.userInfo) return;
        ImageLoader.loadImg(ivHeader, userInfo.headImg);
        tvName.setText(userInfo.nickname);
        tvAttention.setText(DataFormatUtils.formatNumbers(userInfo.attention));
        tvFans.setText(DataFormatUtils.formatNumbers(userInfo.fans));
        tvFriends.setText(DataFormatUtils.formatNumbers(userInfo.friendNum));
        tvHuazuan.setText(DataFormatUtils.formatNumbers(userInfo.balance));
        if (!TextUtils.isEmpty(userInfo.experience))
            tvHuafen.setText(DataFormatUtils.formatNumbers(Double.parseDouble(userInfo.experience)));
//        tvFengmizhi.setText(DataFormatUtils.formatNumbers(userInfo.totalProfit));
        tvFengmizhi.setText(DataFormatUtils.formatNumbers(userInfo.profit));
        tvBenifit.setText(DataFormatUtils.formatNumbers(userInfo.profit));
        mTvId.setText(userInfo.getId() + "");
        if (userInfo.getMyVipLevel() > 0) {
            ivLevel3.setVisibility(View.VISIBLE);
            ivLevel3.setImageResource(ImageLoader.getVipLevel(userInfo.getMyVipLevel()));
        } else {
            ivLevel3.setVisibility(View.GONE);
        }
        if (userInfo.profit == 0)
            tvBenifit.setText("0");
        if (userInfo.getMyIsCardStatus() == UserInfo.IDCARD_STATUS_NO) {
            btnTorenzheng.setRightText("认证不通过");
            btnTorenzheng.setRightTextColor(getResources().getColor(R.color.red_ff3333));
        } else if (userInfo.getMyIsCardStatus() == UserInfo.IDCARD_STATUS_ING) {
            btnTorenzheng.setRightText("认证中");
            btnTorenzheng.setRightTextColor(getResources().getColor(R.color.gray_9F9F9F));
        } else if (userInfo.getMyIsCardStatus() == UserInfo.IDCARD_STATUS_YES) {
            btnTorenzheng.setRightText("已认证");
            btnTorenzheng.setRightTextColor(getResources().getColor(R.color.gray_9F9F9F));
        } else {
            btnTorenzheng.setRightText("快来成为主播");
            btnTorenzheng.setRightTextColor(getResources().getColor(R.color.gray_9F9F9F));
        }
        int age = TextUtils.isEmpty(userInfo.age) ? 0 : Integer.parseInt(userInfo.age);
        //判断是否是主播
        if (!TextUtils.isEmpty(userInfo.userRole) && userInfo.userRole.contains("ROLE_ANCHOR")) {
            CommentUtils.setSexAndAge2(getActivity(), 1, userInfo.sex, age, mTvSexAge);
        } else {
            CommentUtils.setSexAndAge2(getActivity(), 0, userInfo.sex, age, mTvSexAge);
        }

        if (userInfo.myIsLive) {
            ivLevel2.setVisibility(View.VISIBLE);
            int zhuboLevel = userInfo.getMyLiveLevel();
            ivLevel2.setImageResource(ImageLoader.getResId("login_ic_type3_v" + zhuboLevel, com.fengwo.module_comment.R.drawable.class));
        } else ivLevel2.setVisibility(View.GONE);
        int userLevel = userInfo.getUserLevel();
        ivLevel1.setImageResource(ImageLoader.getResId("login_ic_v" + userLevel, com.fengwo.module_comment.R.drawable.class));
        if (userInfo.isReanName()) {
            btnToshichang.setVisibility(View.VISIBLE);
            mLlIsLive.setVisibility(View.VISIBLE);
            mRlHistory.setVisibility(View.GONE);
            btnLevel.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            long duration = userInfo.todayLivingTime;
            mTvTodayLiveTime.setText(TimeUtils.l2String(duration));
            mTvTodayReceive.setText((TextUtils.isEmpty(userInfo.todayReceive) ? "0" : (DataFormatUtils.formatNumberGift(Integer.parseInt(userInfo.todayReceive)))));
        } else {
            btnToshichang.setVisibility(View.GONE);
            btnLevel.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            mLlIsLive.setVisibility(View.GONE);
            mRlHistory.setVisibility(View.VISIBLE);
        }
        if (userInfo.myIsCardStatus == 1 || userInfo.wenboAnchorStatus == 1) {
            mRlGuid.setVisibility(View.VISIBLE);
            mTvGuidTip.setText(userInfo.familyName);
            //在前辈的代码基础上加一块逻辑  如果没有logo就不加载
            if (!TextUtils.isEmpty(userInfo.familyLogo)) {
                ImageLoader.loadImg(mIvGuidPic, userInfo.familyLogo);
            }

        } else {
            mRlGuid.setVisibility(View.GONE);
        }
        if (userInfo.isWenboRole() && CommentUtils.isOpenFlirt) {
            //btnTorenzhengFlirt.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
            btnWenboRecord.setVisibility(View.VISIBLE);
        } else {
            //btnTorenzhengFlirt.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            btnWenboRecord.setVisibility(View.GONE);
        }

        if (userInfo.vipRemainDays == 0) {
            btnToguizu.setRightText("");
        } else {
            btnToguizu.setRightText("剩余" + userInfo.vipRemainDays + "天");
            btnToguizu.setRightTextColor(Color.parseColor("#FB584A"));
        }
        MineGuardAdapter guardAdapter = new MineGuardAdapter(userInfo.userGuardList);
        mRvGuard.setAdapter(guardAdapter);
        if (userInfo.userGuardList == null || userInfo.userGuardList.size() <= 0)
            mTvGuardTip.setVisibility(View.VISIBLE);
        else
            mTvGuardTip.setVisibility(View.GONE);
//        btnDataRecord.setVisibility(CommentUtils.isOpenFlirt ? View.VISIBLE : View.GONE);


        //用户等级 徽章
        if (userInfo.userMedalsList != null && !userInfo.userMedalsList.isEmpty()) {
            List<UserInfo.UserMedalsList> userMedalsList = userInfo.userMedalsList;
            UserInfo.UserMedalsList uml = userMedalsList.get(userMedalsList.size() - 1);

            if (!TextUtils.isEmpty(uml.medalHeadFrame)) {
                mIvPhotoFrame.setVisibility(View.VISIBLE);
                ImageLoader.loadImg(mIvPhotoFrame,uml.medalHeadFrame);
            }

            if (!TextUtils.isEmpty(uml.medalIcon)) {
                ivMedal.setVisibility(View.VISIBLE);
                ImageLoader.loadImg(ivMedal, uml.medalIcon);
            }
        }

        //判断达人认证的columns 的文案如何展示
        //如果是达人   就进入达人直播页面
        //达人申请状态：0申请中 1申请通过 2申请未通过 3未申请
        //这里的逻辑是达人已经实名成功  已经申请了达人  但是正在申请中
        if (userInfo.wenboAnchorStatus == 0) {
            mIvExpertInfo.setRightText("申请中");
            mIvExpertInfo.setRightTextColor(getResources().getColor(R.color.gray_9F9F9F));
        }else if (userInfo.wenboAnchorStatus == 1){
            mIvExpertInfo.setRightText("已认证");
            mIvExpertInfo.setRightTextColor(getResources().getColor(R.color.gray_9F9F9F));
        }else if (userInfo.wenboAnchorStatus == 2){
            mIvExpertInfo.setRightText("认证失败");
            mIvExpertInfo.setRightTextColor(getResources().getColor(R.color.red_ff3333));
        }else if (userInfo.wenboAnchorStatus == 3){
            mIvExpertInfo.setRightText("未认证");
            mIvExpertInfo.setRightTextColor(getResources().getColor(R.color.gray_9F9F9F));
        }
    }







    @Override
    public void onResume() {
        super.onResume();
        if (!isFragmentVisiable) {
            return;
        }
        updateUser();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        isFragmentVisiable = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            if (isFirst) {
//                isFirst = false;
//                return;
//            }
            updateUser();
        }
    }

    private void updateUser() {
        UserManager.getInstance().updateUser(new LoadingObserver<UserInfo>() {
            @Override
            public void _onNext(UserInfo data) {
                initUserInfo(data);
            }

            @Override
            public void _onError(String msg) {
                if (!TextUtils.isEmpty(msg) && msg.contains("重新登录")) {
                    tokenIInvalid();
                }
            }
        });
    }

    /**
     * 获取历史观看记录
     */
    private void getWatchHistory() {
        if (mRetrofitUtils == null)
            mRetrofitUtils = new RetrofitUtils();
        mRetrofitUtils.createApi(LoginApiService.class)
                .getWatchHistory(page + PAGE_SIZE)
                .compose(io_main())
                .compose(bindToLifecycle())
                .subscribe(new LoadingObserver<HttpResult<BaseListDto<WatchHistoryDto>>>() {

                    @Override
                    public void _onNext(HttpResult<BaseListDto<WatchHistoryDto>> data) {
                        if (data != null && data.data != null) {
                            mWatchHistoryAdapter.setNewData(data.data.records);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                });

    }

    /**
     * 获取我的座驾
     */
    private void getMyCar() {
        if (mRetrofitUtils == null)
            mRetrofitUtils = new RetrofitUtils();
        mRetrofitUtils.createApi(LoginApiService.class)
                .getMyCarList()
                .compose(io_main())
                .compose(bindToLifecycle())
                .subscribe(new LoadingObserver<HttpResult<MyCarDto>>() {
                    @Override
                    public void _onNext(HttpResult<MyCarDto> data) {
                        if (data.data.getRecords() != null && data.data.getRecords().size() > 0) {
                            for (int i = 0; i < data.data.getRecords().size(); i++) {
                                if (data.data.getRecords().get(i).getIsOpened() == 1) {  //启用状态
                                    ImageLoader.loadImg(mIvMineCar, data.data.getRecords().get(i).getMotoringThumb());
                                    mTvMineCarTip.setVisibility(View.GONE);
                                }
                            }
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
}


    public void getGiftWall(String pageParams, int userId) {
        if (mRetrofitUtils == null)
            mRetrofitUtils = new RetrofitUtils();
        mRetrofitUtils.createApi(LoginApiService.class)
                .getGiftWall(pageParams, userId)
                .compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<GiftWallDto>() {
                    @Override
                    public void _onNext(GiftWallDto data) {
                        MineGiftWallAdapter adapter = new MineGiftWallAdapter(data.getPageList().getRecords());
                        mRvGift.setAdapter(adapter);
                        if (data.getPageList() == null || data.getPageList().getRecords() == null || data.getPageList().getRecords().size() <= 0) {
                            mTvGiftTip.setVisibility(View.VISIBLE);
                        } else {
                            mTvGiftTip.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                });
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }


    /**
     * 关闭悬浮窗提示
     * @param list
     * @param position
     */
    private void showFVExitDialog(ArrayList<ZhuboDto> list, int position) {
        FloatingView floatingView = FloatingView.getInstance();
        if (floatingView.isShow()) {
            ExitDialog dialog = new ExitDialog();
            dialog.setNegativeButtonText("取消")
                    .setPositiveButtonText("确定退出")
                    .setTip("进入直播间会退出达人房间\n印象值将归零，是否要退出")
                    .setGear(floatingView.getGear())
                    .setNickname(floatingView.getNickname())
                    .setExpireTime(floatingView.getExpireTime())
                    .setRoomId(floatingView.getRoomId())
                    .setHeadImg(floatingView.getHeadImg())
                    .setExitType(ExitDialog.ENTER_LIVING)
                    .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                        @Override
                        public void onConfirm() {
                            IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);
                      //      LivingRoomActivity.start(getActivity(), list, 0, true);
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
            dialog.show(getChildFragmentManager(), "");
        } else {
            IntentRoomActivityUrils.setRoomActivity(list.get(position).channelId,list,0);
     //       LivingRoomActivity.start(getActivity(), list, position);
        }
    }
}
