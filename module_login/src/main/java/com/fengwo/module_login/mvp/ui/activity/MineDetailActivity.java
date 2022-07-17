package com.fengwo.module_login.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.UserMedalBean;
import com.fengwo.module_comment.event.AttentionRefreshEvent;
import com.fengwo.module_comment.event.RefreshCardNum;
import com.fengwo.module_comment.event.RefreshEvent;
import com.fengwo.module_comment.event.VisitorRecordEvent;
import com.fengwo.module_comment.iservice.GetUserInfoByIdService;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserMedalService;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.pop.MessageGreetPopwindow;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.CommonUtil;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.eventbus.BuyGuardSuccessEvent;
import com.fengwo.module_live_vedio.mvp.dto.IsBlackDto;
import com.fengwo.module_live_vedio.mvp.ui.pop.ReportPopWindow;
import com.fengwo.module_live_vedio.utils.AttentionUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.eventbus.UpdateUserinfo;
import com.fengwo.module_login.mvp.dto.LiveStatusDto;
import com.fengwo.module_login.mvp.presenter.MineDetailPresenter;
import com.fengwo.module_login.mvp.ui.adapter.FragmentVpAdapter;
import com.fengwo.module_login.mvp.ui.fragment.MineCardFragment;
import com.fengwo.module_login.mvp.ui.fragment.MineInfoFragment;
import com.fengwo.module_login.mvp.ui.iview.IMineDetailView;
import com.fengwo.module_login.mvp.ui.pop.MineMorePopwindow;
import com.fengwo.module_login.utils.UserManager;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.listener.OnPageChangeListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = ArouterApi.USER_DETAIL)
public class MineDetailActivity extends BaseMvpActivity<IMineDetailView, MineDetailPresenter> implements IMineDetailView {

    private final static String TYPE_UID = "id";
    @Autowired
    UserProviderService userProviderService;
    //    private final static String[] tabsTitle = {"动态", "小视频", "短片", "赞"};
    private final static String[] tabsTitle = {"基本信息", "动态"};
    private List<Fragment> fragments;
    private FragmentVpAdapter fragmentPagerAdapter;
    @BindView(R2.id.tv_belike)
    TextView tvBelike;
    @BindView(R2.id.tv_circle_num)
    TextView tvCircleNum;
    private int[] tabsTitleNum = new int[2];
    private final static int CHOOSE_CARD = 0;
    private final static int CHOOSE_SMALL = 1;
    private final static int CHOOSE_SHORT = 2;
    private final static int CHOOSE_ZAN = 3;

    @Autowired
    GetUserInfoByIdService getUserInfoByIdService;
    @Autowired
    UserMedalService userMedalService;


    @BindView(R2.id.vp)
    ViewPager vp;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_des)
    TextView tvDes;

    @BindView(R2.id.tv_age)
    TextView tvAge;
    @BindView(R2.id.tv_location)
    TextView tvLocation;
    @BindView(R2.id.tv_id)
    TextView tvId;
    @BindView(R2.id.iv_real_name_authentication)
    ImageView ivIsRealName;
    @BindView(R2.id.iv_vip_level)
    ImageView ivVipLevel;
    @BindView(R2.id.tv_fans)
    TextView tvFans;
    @BindView(R2.id.tv_attention)
    TextView tvAttention;
    @BindView(R2.id.tv_cer_mine)
    TextView tvCerMine;
    @BindView(R2.id.ll_mine_attention)
    LinearLayout llMineAttention;
    @BindView(R2.id.ll_mine_other)
    LinearLayout llMineOther;
    @BindView(R2.id.ll_self)
    LinearLayout mLlSelf;
    @BindView(R2.id.iv_attention)
    ImageView ivAttention;
    @BindView(R2.id.right1_img)
    ImageView right1Img;
    @BindView(R2.id.rl_mine_bg)
    RelativeLayout rl_mine_bg;
    @BindView(R2.id.tool_bar)
    RelativeLayout tool_bar;

    @BindView(R2.id.btn_back)
    ImageView btn_back;
    @BindView(R2.id.title_tv)
    TextView title_tv;
    @BindView(R2.id.tob_title)
    TextView tob_title;

    @BindView(R2.id.tv_mine_attention)
    TextView tvMineAttention;
    @BindView(R2.id.appbarlayout)
    AppBarLayout appbarlayout;

    @BindView(R2.id.iv_activity_tag_2)
    ImageView ivTag2;
    @BindView(R2.id.iv_activity_tag_3)
    ImageView ivTag3;
    @BindView(R2.id.tv_flirt_video)
    TextView tvFlirtVideo;
    @BindView(R2.id.tab)
    SlidingTabLayout mTabLayout;
    @BindView(R2.id.tv_attention_user)
    TextView mTvAttentionUser;
    @BindView(R2.id.banner)
    Banner mBanner;
    @BindView(R2.id.tv_current_num)
    TextView mTvCurrentNum;
    @BindView(R2.id.iv_live_status)
    ImageView mTvStatus;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.tob_back)
    ImageView tob_back;

    @BindView(R2.id.iv_more)
    ImageView mIvMore;

    //姓名年龄
    @BindView(R2.id.tv_sex_age)
    TextView mTvSexAge;
    @BindView(R2.id.iv_sex)
    ImageView ivSex;
    private int MAX_SCROLL;

    private UserInfo userInfo;
    private UserMedalBean userMedalBean;//活动勋章信息
    int uid = 0;
    private long startTime;
    private MessageGreetPopwindow greetPopwindow;
    private MineInfoFragment mMineInfoFragment;
    private MineCardFragment mMineCardFragment;

    public static void startActivityWithUserId(Context context, int uid) {
        Intent i = new Intent(context, MineDetailActivity.class);
        i.putExtra(TYPE_UID, uid);
        context.startActivity(i);

    }

    public static void startActivityWithUserIdForR(Activity context, int uid) {
        Intent i = new Intent(context, MineDetailActivity.class);
        i.putExtra(TYPE_UID, uid);
        context.startActivityForResult(i, 10);

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {


        setSupportActionBar(toolbar);
//        tvFlirtVideo.setVisibility(CommentUtils.isOpenFlirt ? View.VISIBLE : View.GONE);
        startTime = System.currentTimeMillis();
//        if (DarkUtil.isDarkTheme(this)) {
//            rl_mine_bg.setBackgroundColor(getResources().getColor(R.color.white));
//        }
        fullScreen(this);
        MAX_SCROLL = DensityUtils.dp2px(this, 250);
        setTitleBackground(android.R.color.transparent);
        ToolBarBuilder barBuilder = new ToolBarBuilder();
        barBuilder.showBack(true)
                .build();
        if (getIntent() != null && getIntent().getExtras() != null) {
            uid = getIntent().getExtras().getInt(TYPE_UID, -1);
        }
        if (uid == -1) {
            uid = (int) getIntent().getExtras().getLong(TYPE_UID, -1);
        }
        setNumbers();
        L.e("=======uid " + uid);
        if (uid > 0) {
            if (uid != UserManager.getInstance().getUser().getId()) {
                llMineOther.setVisibility(View.VISIBLE);

                mIvMore.setVisibility(View.VISIBLE);
                mLlSelf.setVisibility(View.GONE);
            } else {
                mLlSelf.setVisibility(View.VISIBLE);
                llMineOther.setVisibility(View.GONE);
                mIvMore.setVisibility(View.GONE);
            }
        } else {    //自己
            mIvMore.setVisibility(View.GONE);
            mLlSelf.setVisibility(View.VISIBLE);
            llMineOther.setVisibility(View.GONE);
            llMineAttention.setVisibility(View.GONE);
            userInfo = UserManager.getInstance().getUser();
            uid = userInfo.id;
            tvMineAttention.setText("编辑资料");
            ivAttention.setVisibility(View.GONE);
            //未认证 1通过 2失败 3认证中
            L.e("=======myIsCardStatus " + userInfo.myIsCardStatus);
            showAnChorId();
            RxBus.get().toObservable(UpdateUserinfo.class).compose(bindToLifecycle()).subscribe(event -> {
                userInfo = UserManager.getInstance().getUser();
                initUserInfo();
            });
        }
        right1Img.setBackgroundResource(R.drawable.ic_mine_more);
        right1Img.setOnClickListener(l -> {
            MineMorePopwindow mineMorePopwindow = new MineMorePopwindow(this);
            mineMorePopwindow.setOnMineMoreListener(new MineMorePopwindow.OnMineMoreListener() {
                @Override
                public void onReport() {
                    ArouteUtils.toPathWithId(ArouterApi.REPORT_ACTIVITY, uid);
                }

                @Override
                public void onBlock() {
                    new CustomerDialog.Builder(MineDetailActivity.this)
                            .setMsg("确定拉黑该用户？")
                            .setNegativeBtnShow(true)
                            .setPositiveButton("确定", new CustomerDialog.onPositiveInterface() {
                                @Override
                                public void onPositive() {
                                    p.addBlack(uid);
                                }
                            })
                            .create().show();
                }
            });
            mineMorePopwindow.showPopupWindow();
        });

        appbarlayout.addOnOffsetChangedListener((appBarLayout, i) -> {
            int dy = Math.abs(i);
            tool_bar.setSelected(dy > 10);
            float alpha = Math.min(MAX_SCROLL, dy) / (float) MAX_SCROLL;
            if (alpha > 0.5 && userInfo != null) {
                tob_title.setText(userInfo.getNickname());
            }
            int backgroundAlpha = (int) (alpha * 255);
            int backgroundColor = Color.argb(backgroundAlpha, 255, 255, 255);
            if (DarkUtil.isDarkTheme(MineDetailActivity.this)) {
                int backgroundBlack = Color.argb(backgroundAlpha, 255, 255, 255);
                tob_title.setTextColor(backgroundBlack);
                tob_back.setColorFilter(backgroundBlack);
            } else {
                int backgroundBlack = Color.argb(backgroundAlpha, 0, 0, 0);
                tob_title.setTextColor(backgroundBlack);
                btn_back.setColorFilter(backgroundBlack);
                tob_back.setColorFilter(backgroundBlack);
                right1Img.setColorFilter(backgroundBlack);
                baseTitle.setBackgroundColor(backgroundColor);
            }
        });
        netManager.add(RxBus.get().toObservable(RefreshCardNum.class).compose(bindToLifecycle())
                .subscribe(refreshCardNum -> refreshUserInfo()));
        netManager.add(RxBus.get().toObservable(RefreshEvent.class)
                .compose(bindToLifecycle())
                .subscribe(refreshEvent -> reFreshData()));

        netManager.add(RxBus.get().toObservable(BuyGuardSuccessEvent.class)
                .compose(bindToLifecycle())
                .subscribe(buyGuardSuccessEvent -> refreshUserInfo()));

    }

    private void showAnChorId() {
        if (userInfo.id == UserManager.getInstance().getUser().getId()) {
            if (userInfo.myIsCardStatus == 0 || userInfo.myIsCardStatus == 2) {
//                tvCerMine.setVisibility(View.VISIBLE);
                tvCerMine.setText("开通主播");
                Drawable drawableLeft = getResources().getDrawable(
                        R.drawable.ic_flirt_cer_mine);
                tvCerMine.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                        null, null, null);
            } else if (userInfo.myIsCardStatus == 3) {
//                tvCerMine.setVisibility(View.VISIBLE);
                tvCerMine.setText("认证中");
                Drawable drawableLeft = getResources().getDrawable(
                        R.drawable.ic_cer_ing);
                tvCerMine.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                        null, null, null);
            } else {
                tvCerMine.setVisibility(View.GONE);
                if (userInfo.liveLevel > 50) {
                    userInfo.liveLevel = 50;
                } else if (userInfo.liveLevel == 0) {
                    userInfo.liveLevel = 1;
                }
                L.e("=======liveLevel " + userInfo.liveLevel);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshUserInfo();

    }

    private void refreshUserInfo() {
        getUserInfoByIdService.getUserInfoById(uid + "", new LoadingObserver<HttpResult<UserInfo>>(this) {
            @Override
            public void _onNext(HttpResult<UserInfo> data) {
                userInfo = data.data;
                initUserInfo();

                //达人不在线  主播也不在线的状态会进这里面
                mTvStatus.setVisibility(View.VISIBLE);
                if (userInfo.liveStatus == 0 && userInfo.wenboLiveStatus == 0) {
                    mTvStatus.setVisibility(View.GONE);
                }
                //主播在线会进这里面
                else if (userInfo.liveStatus == 1) {
                    mTvStatus.setBackground(ContextCompat.getDrawable(MineDetailActivity.this, R.drawable.ic_mine_is_live));
                }
                //达人在线会进这里面
                else if (userInfo.wenboLiveStatus == 1) {
                    mTvStatus.setBackground(ContextCompat.getDrawable(MineDetailActivity.this, R.drawable.icon_expert_flag));
                }

            }

            @Override
            public void _onError(String msg) {

            }
        });
//        userMedalService.getUserMedal(uid, 0, new LoadingObserver<HttpResult<UserMedalBean>>() {
//            @Override
//            public void _onNext(HttpResult<UserMedalBean> data) {
//                userMedalBean = data.data;
////                ivRegimental.setVisibility(userMedalBean.isLeader == 1 ? View.VISIBLE : View.INVISIBLE);
//                if (userMedalBean.medalOneId > 0 && !TextUtils.isEmpty(data.data.medalOneIcon))
////                    ivTag.setImageResource(ImageLoader.getResId("ic_medal_" + userMedalBean.medalOneId, R.drawable.class));
//                    ImageLoader.loadImg(ivTag, data.data.medalOneIcon);
//                if (userMedalBean.medalTwoId > 0 && !TextUtils.isEmpty(data.data.medalTwoIcon))
////                    ivTag2.setImageResource(ImageLoader.getResId("ic_medal_" + userMedalBean.medalTwoId, R.drawable.class));
//                    ImageLoader.loadImg(ivTag, data.data.medalTwoIcon);
//                if (userMedalBean.medalThreeId > 0)
////                    ivTag3.setImageResource(ImageLoader.getResId("ic_medal_" + userMedalBean.medalThreeId, R.drawable.class));
//                    ImageLoader.loadImg(ivTag, data.data.medalThreeIcon);
////                if (userMedalBean.medalOneId > 11 && userMedalBean.medalOneId < 20)
////                    ivHeaderBg.setBackgroundResource(ImageLoader.getResId("bg_mine_head_" + userMedalBean.medalOneId, R.drawable.class));
////                else
////                    ivHeaderBg.setBackgroundResource(ImageLoader.getResId("bg_mine_head_0", R.drawable.class));
////                ImageLoader.loadImg(ivHeaderBg,data.data.medalOneHeadFrame);
//
//            }
//
//            @Override
//            public void _onError(String msg) {
//
//            }
//        });
    }


    private void initUserInfo() {
//        if (!CommentUtils.isListEmpty(userInfo.userMedalsList)) {
//            ivTag.setVisibility(View.VISIBLE);
//            tvTime.setText("有效期至 " + userInfo.userMedalsList.get(0).expireTime.split("T")[0]);
//            int medalId = userInfo.userMedalsList.get(0).medalId - 1;
//            int res = ImageLoader.getResId("ic_angel_" + (medalId < 4 ? medalId : medalId % 4), com.fengwo.module_live_vedio.R.drawable.class);
//            ivTag.setImageResource(res);
//        } else {
//            tvTime.setText("");
//            ivTag.setVisibility(View.GONE);
//        }
//        ImageLoader.loadImg(ivHeader, userInfo.headImg);
        if (mMineInfoFragment != null)
            mMineInfoFragment.initUserInfo(userInfo);

        tvName.setText(userInfo.nickname);
        tvDes.setText(userInfo.signature);


        int age = TextUtils.isEmpty(userInfo.age) ? 0 : Integer.parseInt(userInfo.age);

        //判断是否是主播
        if (!TextUtils.isEmpty(userInfo.userRole) && userInfo.userRole.contains("ROLE_ANCHOR")) {
            CommentUtils.setSexAndAge(this, 1, userInfo.sex, age, mTvSexAge);
        } else {
            CommentUtils.setSexAndAge(this, 0, userInfo.sex, age, mTvSexAge);
        }
        tvLocation.setText(userInfo.location);
        if (userInfo.liveLevel > 0) {
            if (userInfo.liveLevel > 50) {
                userInfo.liveLevel = 50;
            }
        }
        ivIsRealName.setVisibility(userInfo.myIsCard ? View.VISIBLE : View.GONE);

        if (TextUtils.isEmpty(userInfo.age)) {
            tvAge.setVisibility(View.GONE);
        } else {
            tvAge.setText(userInfo.age);
        }
        tvId.setText("蜂窝号：" + userInfo.fwId);
        tvDes.setText(userInfo.signature);

        attentionUI();
        if (userInfo.vipLevel > 0) {
            ivVipLevel.setVisibility(View.VISIBLE);
            ivVipLevel.setImageResource(ImageLoader.getVipLevel(userInfo.vipLevel));
        } else {
            ivVipLevel.setVisibility(View.GONE);
        }
        tvFans.setText(userInfo.fans + "");
        tvAttention.setText(userInfo.attention + "");
        int like = userInfo.beLikeCardCount + userInfo.beLikeMovieCount + userInfo.beLikeVideoCount;
        tvBelike.setText(like + "");
        tvCircleNum.setText(userInfo.joinCircleNum + "");
//        llMineAttention.setVisibility(userInfo.id == UserManager.getInstance().getUser().id ? View.GONE : View.VISIBLE);
        right1Img.setVisibility(userInfo.id == UserManager.getInstance().
                getUser().id ? View.GONE : View.VISIBLE);


        List<UserInfo.UserPhotosList> userPhotosLists = new ArrayList<>();
//        UserInfo.UserPhotosList userPhotosList = new UserInfo.UserPhotosList(userInfo.headImg);
//        userPhotosLists.add(userPhotosList);
        if (userInfo.userPhotos != null && userInfo.userPhotos.size() > 0) {
            for (int i = 0; i < userInfo.userPhotos.size(); i++) {
                if (userInfo.userPhotos.get(i).photoStatus == 1) {
                    userPhotosLists.add(userInfo.userPhotos.get(i));
                }
            }

        }
        if (userPhotosLists.size() != 0) {
            mTvCurrentNum.setText("1/" + userPhotosLists.size());
        } else {
            mTvCurrentNum.setText("0/0");
        }

        mBanner.setAdapter(new BannerImageAdapter<UserInfo.UserPhotosList>(userPhotosLists) {
            @Override
            public void onBindView(BannerImageHolder holder, UserInfo.UserPhotosList data, int position, int size) {
                ImageLoader.loadImg(holder.imageView, data.photoUrl);
            }
        });
        mBanner.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (userPhotosLists.size() <= 0) {
                    mTvCurrentNum.setVisibility(View.GONE);
                } else {
                    mTvCurrentNum.setVisibility(View.VISIBLE);
                    mTvCurrentNum.setText((position + 1) + "/" + userPhotosLists.size());
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        UserInfo mUser = UserManager.getInstance().getUser();
        if (userInfo.id == UserManager.getInstance().getUser().getId()) {
            userInfo.userLevel = mUser.userLevel;
            userInfo.experience = mUser.experience;
            userProviderService.setUsetInfo(userInfo);
            return;//如果是自己的页面   就不调用查询主播状态的接口
        }

        //if (userInfo != null)
        //查询主播状态，跟姐姐用户id查询
        //  p.getLiveStatus(userInfo.getId());

    }

    private void attentionUI() {
        llMineAttention.setSelected(userInfo.isAttention != 0);
        switch (userInfo.isAttention) {
            case 1:
                mTvAttentionUser.setText("已关注");
                ivAttention.setImageResource(R.drawable.ic_attention);
                mTvAttentionUser.setBackgroundResource(R.drawable.bg_not_attention);
                break;
            case 2:
                mTvAttentionUser.setText("互相关注");
                ivAttention.setImageResource(R.drawable.ic_inter_attention);
                mTvAttentionUser.setBackgroundResource(R.drawable.bg_not_attention);
                break;
            default:
                mTvAttentionUser.setText("+关注");
                ivAttention.setImageResource(R.drawable.bg_attention);
                mTvAttentionUser.setBackgroundResource(R.drawable.bg_attention);

                break;
        }
    }


    private void setRvData(int position) {
        List<String> l = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            l.add("1");
        }
    }

    @Override
    protected int getContentView() {
        ARouter.getInstance().inject(this);
        return R.layout.login_activity_mine_detail;
    }

    public void reFreshData() {
        refreshUserInfo();
    }


    @Override
    public MineDetailPresenter initPresenter() {
        return new MineDetailPresenter();
    }

    /**
     * 请求发布页面得到回调
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            mTabLayout.setCurrentTab(1);
            mMineCardFragment.refreshData();
        }
    }

    @OnClick({R2.id.ll_mine_attention, R2.id.tv_flirt_video, R2.id.tv_cer_mine, R2.id.tv_chat_call, R2.id.tv_attention_user,
            R2.id.tv_edit_info, R2.id.tv_send_trend, R2.id.tob_back, R2.id.iv_more})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.tv_edit_info) {   //编辑个人资料
            if (userInfo == null) return;
            if (uid != UserManager.getInstance().getUser().id) {
                BigPhotoActivity.start(this, uid, userInfo.headImg, userInfo.nickname);
                return;
            }
            startActivity(EdittextInfoActivity.class);
        } else if (view.getId() == R.id.tv_flirt_video) {//视频
            if (userInfo == null) return;
            if (userInfo.getId() == UserManager.getInstance().getUser().id) {
                toastTip("不能和自己视频聊天哦~");
                return;
            }
            if (!TextUtils.isEmpty(userInfo.userRole) && userInfo.userRole.contains("ROLE_ANCHOR_WENBO")) {
                ArouteUtils.toFlirtCardDetailsActivity(uid);
            } else {
                toastTip("对方认证通过后你才可以跟TA视频哦！");
            }
        } else if (view.getId() == R.id.tv_chat_call) {//打招呼
            if (CommonUtil.call(this, userInfo, UserManager.getInstance().getUser())) return;
            if (greetPopwindow == null) {
                greetPopwindow = new MessageGreetPopwindow(MineDetailActivity.this);
                greetPopwindow.setOnSelectListener(new MessageGreetPopwindow.OnSelectListener() {
                    @Override
                    public void onSelect(String content) {
                        p.greetDiscuss(userInfo.getId() + "", content);


                    }

                    @Override
                    public void onRequest() {
                        observeMessageTagList();
                    }
                });
            } else {
                if (greetPopwindow.isShowing())
                    return;
            }
            observeMessageTagList();
            greetPopwindow.showPopupWindow();
        } else if (view.getId() == R.id.tv_cer_mine) {//主播认证
            ARouter.getInstance().build(ArouterApi.OLD_REAL_ANCHOR_ACTION)
                    .withInt("status", UserManager.getInstance().getUser().myIsCardStatus)
                    .navigation();
            //RealNameActivity.start(this, UserManager.getInstance().getUser().myIsCardStatus);
        } else if (view.getId() == R.id.tob_back) {//
            finish();
        } else if (view.getId() == R.id.tv_send_trend) {
            ArouteUtils.toPostTrend(MineDetailActivity.this);
        } else if (view.getId() == R.id.iv_more) {
            showType();
        } else if (view.getId() == R.id.ll_mine_attention || view.getId() == R.id.tv_attention_user) {
            if (tvMineAttention.getText().toString().equals("编辑资料")) {
                if (uid != UserManager.getInstance().getUser().id) {
                    return;
                }
                startActivity(EdittextInfoActivity.class);
            } else {
                if (userInfo == null) return;
                if (userInfo.isAttention == 0) {
                    AttentionUtils.addAttention(userInfo.id, new LoadingObserver<HttpResult>() {
                        @Override
                        public void _onNext(HttpResult data) {
                            toastTip(data.description);
                            if (data.isSuccess()) {

                                userInfo.isAttention = 1;
                                attentionUI();
                                refreshUserInfo();
                                RxBus.get().post(new AttentionRefreshEvent(userInfo.id, 1));
                            }
                        }

                        @Override
                        public void _onError(String msg) {
                            toastTip(msg);
                        }
                    });
                } else {
                    AttentionUtils.delAttention(userInfo.id, new LoadingObserver<HttpResult>() {
                        @Override
                        public void _onNext(HttpResult data) {
                            toastTip(data.description);
                            if (data.isSuccess()) {
                                userInfo.isAttention = 0;
                                attentionUI();
                                refreshUserInfo();
                                RxBus.get().post(new AttentionRefreshEvent(userInfo.id, 0));
                            }
                        }

                        @Override
                        public void _onError(String msg) {
                            toastTip(msg);
                        }
                    });
                }
            }
        }
    }
    private void showType(){
        //判断是否已拉黑
        new RetrofitUtils().createApi(LiveApiService.class)
                .judgeBlack(uid+"")
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<IsBlackDto>>() {
                    @Override
                    public void _onNext(HttpResult<IsBlackDto> data) {
                        if (data.isSuccess()) {
                            IsBlackDto bean = data.data;

                            ReportPopWindow reportPopWindow = new ReportPopWindow(MineDetailActivity.this, 0, false,bean.isBlack);
                            reportPopWindow.setOnMineMoreListener(new ReportPopWindow.OnMineMoreListener() {
                                @Override
                                public void onReport() {
                                    ArouteUtils.toPathWithId(ArouterApi.REPORT_ACTIVITY, userInfo.getId());
                                }

                                @Override
                                public void onBlock() {
                                    new CustomerDialog.Builder(MineDetailActivity.this)
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
                                }

                                @Override
                                public void onKickOut() {
                                }

                                @Override
                                public void onDelBlock() {
                                    new CustomerDialog.Builder(MineDetailActivity.this)
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
    public void setNumbers() {
        mMineInfoFragment = MineInfoFragment.newInstance();
        mMineCardFragment = MineCardFragment.newInstance(uid, false);
        fragments = new ArrayList<>();
        fragments.add(mMineInfoFragment);
        fragments.add(mMineCardFragment);
//        fragments.add(MineMovieFragment.newInstance(uid, false));
//        fragments.add(MineZanFragment.newInstance(uid));
        fragmentPagerAdapter = new FragmentVpAdapter(getSupportFragmentManager(), fragments);
        vp.setOffscreenPageLimit(fragments.size());
        vp.setAdapter(fragmentPagerAdapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setViewPager(vp, tabsTitle);//tab和ViewPager进行关联
        LayoutInflater inflater = LayoutInflater.from(this);
        setRvData(CHOOSE_SMALL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (UserManager.getInstance().getUser() != null && uid != UserManager.getInstance().getUser().id)
            //分发最近访客记录
            RxBus.get().post(new VisitorRecordEvent(uid, (System.currentTimeMillis() - startTime) / 1000));
    }

    @Override
    public void greet(String content) {
        //todo 打招呼成功
        if (greetPopwindow.isShowing())
            greetPopwindow.dismiss();
        if (userInfo.isAttention == 0) {
            AttentionUtils.addAttention(userInfo.id, new LoadingObserver<HttpResult>() {
                @Override
                public void _onNext(HttpResult data) {
                    if (data.isSuccess()) {
                        userInfo.isAttention = 1;
                        attentionUI();
                        refreshUserInfo();
                        RxBus.get().post(new AttentionRefreshEvent(userInfo.id, 1));
                    }
                }

                @Override
                public void _onError(String msg) {
                    toastTip(msg);
                }
            });
        }
        UserInfo mine = UserManager.getInstance().getUser();
        if (userInfo == null || mine == null) return;
        //   RxBus.get().post(new GreetMessageEvent(mine.id + "", userInfo.id + "", userInfo.nickname, userInfo.headImg, content, mine.headImg, System.currentTimeMillis() / 1000));


        UserInfo mUser = UserManager.getInstance().getUser();
        ArouteUtils.toGreetChatSingleMsgActivity(mUser.getId() + "", uid + "", userInfo.getNickname(), content, userInfo.getHeadImg());
        //加入消息列表
//        ArouteUtils.toGreetChatSingleActivity(UserManager.getInstance().getUser().id + "",
//                userInfo.id + "", userInfo.nickname, content, userInfo.headImg);
    }

    private void addBlackList() {
        new RetrofitUtils().createApi(LiveApiService.class)
                .AddBlackList(userInfo.getId())
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        ToastUtils.showShort(MineDetailActivity.this, data.description);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(MineDetailActivity.this, msg);
                    }
                });

    }
    private void DelBlackList() {
        new RetrofitUtils().createApi(LiveApiService.class)
                .DelBlackList(userInfo.getId())
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        ToastUtils.showShort(MineDetailActivity.this, data.description);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(MineDetailActivity.this, msg);
                    }
                });

    }
    @Override
    public void setGreetTips(List<String> dataList, int page) {
        pages = page;
        greetPopwindow.setAdapter(dataList);
    }

    @Override
    public void getLiveStatus(LiveStatusDto d) {
        //达人不在线  主播也不在线的状态会进这里面
        mTvStatus.setVisibility(View.VISIBLE);
        if (d.liveStatus == 0 && d.wenboLiveStatus == 0) {
            mTvStatus.setVisibility(View.GONE);
        }
        //主播在线会进这里面
        else if (d.liveStatus == 1) {
            mTvStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_mine_is_live));
        }
        //达人在线会进这里面
        else if (d.wenboLiveStatus == 1) {
            mTvStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.icon_expert_flag));
        }
    }

    private int page = 0, pages = 1;

    private void observeMessageTagList() {
        if (page < pages)
            page++;
        else {
            page = 1;
        }
        p.greetTipsList(page);
    }
}
