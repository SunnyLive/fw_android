package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.AnchorWishBean;
import com.fengwo.module_comment.bean.UserLevelBean;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.event.PaySuccessEvent;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.eventbus.ShowRechargePopEvent;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;
import com.fengwo.module_live_vedio.mvp.dto.RefreshBackpack;
import com.fengwo.module_live_vedio.mvp.ui.activity.LivingRoomActivity;
import com.fengwo.module_live_vedio.mvp.ui.adapter.GiftVpAdapter;
import com.fengwo.module_live_vedio.mvp.ui.event.ChangeGiftEvent;
import com.fengwo.module_live_vedio.utils.WishCacheMr;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import razerdp.basepopup.BasePopupWindow;

/**
 * 能重构重构 需求能不接不接 谁改谁骂街
 */
public class GiftPopWindow extends BasePopupWindow implements TabLayout.BaseOnTabSelectedListener, View.OnClickListener {

    private GiftVpAdapter myadapter;
    public GiftDto dto;
    private int my_number = 1;
    @Autowired
    UserProviderService userProviderService;
    private List<List<GiftDto>> allGifts;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout llPoint;
    private View btnRecharge;
    private SparseArray<PagerAdapter> vpAdapters;
    private Context mContext;
    private int currentPage = 0;
    private int numCheckPosition = 0;
    private TextView btnNum1, btnNum10, btnNum66, btnNum520;
    private TextView btnSend;
    private TextView tvBalance;
    private TextView tvClickLeftTime;
    private ImageView buttonOut;//连点外圈 动画img
    private View btnFastClick;
    private boolean isFastShow = false;//是否切换快速点击
    CountBackUtils countBackUtils;
    private ProgressBar progressBar;
    private TextView tvCurLevel;
    private TextView tvNextLevel;
    private TextView tv_releft_exo;
    private TextView tv_gift_tips;
    /////////////////////////////////////
    private RadioButton mRbBackpack;
    private RadioGroup mRgBackpackNav;
    private TextView mTvBackpackTotal;
    private TextView mTvRemark;
    /////////////////////////////////////
    private View mProgressParent;

    private int vpCheckPosition = 0;
    private long downTime;
    private boolean isFast;

    private Animation operatingAnim;
    private RxBus bus;
    Disposable disposable;
    private boolean isWish;
    private List<UserLevelBean> userLevelBeans = new ArrayList<>();
    private UserInfo userInfo;
    private LinearLayout llNumber;
    private EditText tvNumber;
    private String sendNum;//送礼数量
    private ImageView mIvUp;
    private int mSourceSize;
    public GivingGiftsPopWindow mGivingGiftsPopWindow;
    private ZhuboDto mZhuboDto;
    private boolean isEnabled = true;
    public GiftDto currentGift;
    public GiftDto selectGift;

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_gifts);
    }

    public List<List<GiftDto>> getAllGifts() {
        return allGifts;
    }

    public SparseArray<PagerAdapter> getVpAdapter() {
        return vpAdapters;
    }


    public void setBackpackTotal(int totalCount) {
        if (totalCount > 0) {
            String count = totalCount > 99 ? "99+" : totalCount + "";
            mTvBackpackTotal.setText(count);
            mTvBackpackTotal.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("CheckResult")
    public GiftPopWindow(Context context, List<List<GiftDto>> data, List<UserLevelBean> userLevelBeans, UserInfo userInfo, boolean ispush) {
        super(context);

        setBackgroundColor(Color.TRANSPARENT);
        mGivingGiftsPopWindow = new GivingGiftsPopWindow(context);
        setPopupGravity(Gravity.BOTTOM);
        ARouter.getInstance().inject(this);
        operatingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_button);
        mContext = context;
        this.allGifts = data;
        mSourceSize = data.size();
        this.userLevelBeans = userLevelBeans;
        this.userInfo = userInfo;
        countBackUtils = new CountBackUtils();
        vpAdapters = new SparseArray<>();
        tabLayout = findViewById(R.id.tabview);
        llNumber = findViewById(R.id.ll_number);
        viewPager = findViewById(R.id.vp);
        llPoint = findViewById(R.id.ll_point);
        btnRecharge = findViewById(R.id.btn_recharge);
        btnNum1 = findViewById(R.id.btn_num1);
        btnNum10 = findViewById(R.id.btn_num10);
        btnNum66 = findViewById(R.id.btn_num66);
        btnNum520 = findViewById(R.id.btn_num520);
        btnSend = findViewById(R.id.btn_send);
        btnFastClick = findViewById(R.id.fastclick_view);
        buttonOut = findViewById(R.id.button_out);
        tvNumber = findViewById(R.id.tv_number);
        tvBalance = findViewById(R.id.tv_balance);
        tvClickLeftTime = findViewById(R.id.click_left_time);
        progressBar = findViewById(R.id.progress_bar);
        tvCurLevel = findViewById(R.id.tv_current_level);
        tvNextLevel = findViewById(R.id.tv_next_level);
        tv_releft_exo = findViewById(R.id.tv_releft_exo);
        tv_gift_tips = findViewById(R.id.tv_gift_tips);
        mTvBackpackTotal = findViewById(R.id.tv_backpack_label);
        mTvRemark = findViewById(R.id.tv_remark);
        mIvUp = findViewById(R.id.iv_up);
        mIvUp.setOnClickListener(this);
        llNumber.setOnClickListener(this);
        btnNum1.setOnClickListener(this);
        btnNum10.setOnClickListener(this);
        btnNum66.setOnClickListener(this);
        btnNum520.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnFastClick.setOnClickListener(this);
        //背包按钮的view
        mRbBackpack = findViewById(R.id.rb_backpack);
        View gift_line = findViewById(R.id.gift_line);
        if (ispush) {
            mRbBackpack.setVisibility(View.GONE);
            gift_line.setVisibility(View.GONE);
        }
        mRgBackpackNav = findViewById(R.id.rg_backpack_nav);
        mProgressParent = findViewById(R.id.ll_progress);
        for (int i = 0; i < allGifts.size(); i++) {
            String title = allGifts.get(i).get(0).giftTypeText;
            TabLayout.Tab t = tabLayout.newTab();
            t.setText(title);
            tabLayout.addTab(t);
        }
        tabLayout.addOnTabSelectedListener(this);
        tvNumber.setOnFocusChangeListener((v, hasFocus) -> {
                    if (hasFocus) {
                        tvNumber.postDelayed(() ->
                                tvNumber.setSelection(tvNumber.getText().length()), 300);
                    } else {
                        KeyBoardUtils.closeKeybord(tvNumber, tvNumber.getContext());
                    }
                }
        );
        tvNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PagerAdapter pagerAdapter = vpAdapters.get(vpCheckPosition);
                if (pagerAdapter instanceof GiftVpAdapter) {
                    GiftVpAdapter giftVpAdapter = (GiftVpAdapter) pagerAdapter;
                    if (giftVpAdapter == null) return;
                    GiftDto gift = giftVpAdapter.getGift(giftVpAdapter.getCheckPosition());
                    if (gift != null && TextUtils.isEmpty(gift.giftName) && !TextUtils.isEmpty(s)) {
                        int num = Integer.parseInt(s.toString());
                        if (num > gift.goodsCount) {
                            tvNumber.setText(gift.goodsCount + "");
                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (null == vpAdapters.get(0)) {
            GiftVpAdapter adapter = new GiftVpAdapter(context, allGifts.get(0)) {
                @Override
                public void onItemClickListener(BaseQuickAdapter adapter1, View view, int position) {
                    super.onItemClickListener(adapter1, view, position);
                    onAdapterClickEvent(adapter1, view, position);
                }
            };
            vpAdapters.put(0, adapter);
        }
        viewPager.setAdapter(vpAdapters.get(0));
        viewPager.setOffscreenPageLimit(3);
        setPoint(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setCurrentPoint(position);
                setCheckZero(position);
                onClick(btnNum1);
                if (myadapter != null)
                    myadapter.setChecks(currentPage, 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bus = RxBus.get();
        btnRecharge.setOnClickListener(v -> {
            bus.post(new ShowRechargePopEvent());
        });
        disposable = bus.toObservable(ChangeGiftEvent.class).subscribe(event -> {
//            if(event.istype){
//                actGiftDt = null;
//            }
            L.e("tags", event.position + "currentPage=" + currentPage);
            if (vpCheckPosition == -1) return;
            if (vpAdapters.get(vpCheckPosition) instanceof GiftVpAdapter) {
                if (!type) {
                    my_number = 1;
                    tvNumber.setText(my_number + "");

                }

                myadapter = (GiftVpAdapter) vpAdapters.get(vpCheckPosition);
                dto = myadapter.getGift(currentPage);
                //    handleUserLevel(dto, my_number);
                if (dto == null) return;
                setUpUserLevel(dto);
                if (!TextUtils.isEmpty(event.dto.giftSwf)) {
                    btnFastClick.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                }
                if (vpCheckPosition == 0 && currentPage == 0 && dto.giftName.equals("赤焰红唇")) {
                    tv_gift_tips.setVisibility(View.GONE);
                    String num = ((GiftVpAdapter) vpAdapters.get(0)).getGift(0).quantityGrad.replace(",", " ");
                    String num1 = " 66 520 1314 ";
                    SpannableStringBuilder spanString = new SpannableStringBuilder("赤焰红唇连续单击赠送" + num + "会触发隐藏特效哦！");
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#FE3C9C"));
                    spanString.setSpan(foregroundColorSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ForegroundColorSpan foregroundColorSpan1 = new ForegroundColorSpan(Color.parseColor("#FAD58E"));
                    spanString.setSpan(foregroundColorSpan1, 10, 10 + num1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_gift_tips.setText(spanString);
                } else {
                    tv_gift_tips.setVisibility(View.GONE);
                }
                if (event.dto != null && !TextUtils.isEmpty(event.dto.quantityGrad)) {
                    sendNum = event.dto.quantityGrad;
                    isEnabled = true;
                } else {
                    isEnabled = false;
                }
            }
        });


        //背包nav的事件处理方式
        mRbBackpack.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isEnabled = false;
                tvNumber.setEnabled(true);
                mRgBackpackNav.setVisibility(View.VISIBLE);
                llNumber.setBackground(ContextCompat.getDrawable(mContext, R.color.transparent));
                llPoint.setVisibility(View.GONE);
                tvNumber.setVisibility(View.GONE);
                mIvUp.setVisibility(View.GONE);
                btnSend.setVisibility(View.GONE);
                mProgressParent.setVisibility(View.GONE);
                Class<? extends TabLayout> clazz = tabLayout.getClass();
                try {
                    Field selectedTab = clazz.getDeclaredField("selectedTab");
                    selectedTab.setAccessible(true);
                    selectedTab.set(tabLayout, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tabLayout.setSelected(true);
                tabLayout.setSelected(false);
                if (mRgBackpackNav.getChildCount() > 0)
                    mRgBackpackNav.getChildAt(0).performClick();
            } else {
                isEnabled = true;
                tvNumber.setEnabled(false);
                tvNumber.setText("1");
                llNumber.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_gifts_back));
                mRgBackpackNav.clearCheck();
                mRgBackpackNav.setVisibility(View.GONE);
                llPoint.setVisibility(View.VISIBLE);
                tvNumber.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.VISIBLE);
                btnSend.setTag(null);
                btnSend.setText("发送");
                mIvUp.setVisibility(View.VISIBLE);
                mProgressParent.setVisibility(View.VISIBLE);
            }
        });


        mRgBackpackNav.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == -1) return;
            View checkedView = findViewById(checkedId);
            if (checkedView != null && checkedView instanceof RadioButton) {
                if (!((RadioButton) checkedView).isChecked()) {
                    //此处是某个选中按钮被取消的回调，在调用check方法修改选中的时候会触发
                    return;
                }
            }
            vpCheckPosition = checkedId;
            chooseBottomBtnState();
            if (null == vpAdapters.get(vpCheckPosition)) {
                GiftVpAdapter adapter = new GiftVpAdapter(mContext, allGifts.get(vpCheckPosition)) {
                    @Override
                    public void onItemClickListener(BaseQuickAdapter adapter1, View view, int position) {
                        super.onItemClickListener(adapter1, view, position);
                        onAdapterClickEvent(adapter1, view, position);
                    }
                };
                vpAdapters.put(vpCheckPosition, adapter);
            }
            PagerAdapter adapter = vpAdapters.get(vpCheckPosition);
            viewPager.setAdapter(adapter);
            llPoint.setVisibility(adapter.getCount() <= 1 ? View.GONE : View.VISIBLE);
            setPoint(vpCheckPosition);
            setCheckZero(0);
            onClick(btnNum1);
        });
    }

    /**
     * 购买座驾或者
     */
    public void refreshBackpack() {
        if (mRgBackpackNav.getChildCount() > 0) {
            mRgBackpackNav.clearCheck();
            mRgBackpackNav.removeAllViews();
        }
        initChildNav();
    }

    /**
     * 初始化子选项
     */
    private void initChildNav() {
        if (allGifts.size() == mSourceSize) return;

        for (int i = mSourceSize; i < allGifts.size(); i++) {
            if (!allGifts.get(i).isEmpty()) {
                String title = allGifts.get(i).get(0).giftTypeText;
                RadioButton childNav = (RadioButton) LayoutInflater.from(mContext).inflate(R.layout.layout_gift_child_nav, mRgBackpackNav, false);
                childNav.setText(title);
                childNav.setId(i);
                mRgBackpackNav.addView(childNav);
            }
        }

        if (mRbBackpack.isChecked()) {
            if (vpAdapters.get(vpCheckPosition) == null) {
                if (mRgBackpackNav.getChildCount() > 0) {
                    mRgBackpackNav.getChildAt(0).performClick();
                }
            } else {
                for (int i = 0; i < mRgBackpackNav.getChildCount(); i++) {
                    if (mRgBackpackNav.getChildAt(i).getId() == vpCheckPosition) {
                        mRgBackpackNav.getChildAt(i).performClick();
                    }
                }
            }
        }

    }

    /**
     * 更改底部按钮的状态
     */
    private void chooseBottomBtnState() {
        List<GiftDto> gifts = getAllGifts().get(vpCheckPosition);
        GiftDto gift = gifts.get(0);
        llNumber.setBackground(ContextCompat.getDrawable(mContext, R.color.transparent));
        //物品类型：1礼物 2座驾 3贵族 4道具
        switch (gift.goodsType) {
            case 1:
                tvNumber.setEnabled(true);
                llPoint.setVisibility(View.VISIBLE);
                btnSend.setTag(null);
                btnSend.setText("发送");
                mTvRemark.setVisibility(View.GONE);
                showSendBt(true, false);
                break;
            case 2:
                llPoint.setVisibility(View.GONE);
                mTvRemark.setVisibility(View.GONE);
                showSendBt(false, false);
                break;
            case 3:
                showSendBt(false, false);
                mTvRemark.setVisibility(View.VISIBLE);
                mTvRemark.setText("注：默认最高贵族权限，有效期结束后自动更换成有效的其他贵族权限");
                break;
            case 4:
                mTvRemark.setVisibility(View.VISIBLE);
                mTvRemark.setText(gift.remark);
                btnSend.setTag(gift);
                if ("8".equals(gift.activityId) || Integer.parseInt(gift.activityId) > 9 && Integer.parseInt(gift.activityId) < 18) {
                    showSendBt(gift.remainCount >= 18, true);
                    btnSend.setText("抽奖");
                } else if ("9".equals(gift.activityId)) {
                    showSendBt(gift.remainCount >= 1, true);
                    btnSend.setText("赠送");
                } else {
                    showSendBt(false, false);
                }
                break;

        }

    }

    private void showSendBt(boolean isShowView, boolean isShowOnlySend) {
        if (isShowView) {
            llNumber.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_gifts_back));
            llNumber.setVisibility(View.VISIBLE);
        } else {
            llNumber.setVisibility(View.GONE);
        }
        if (isShowOnlySend) {
            llNumber.setBackground(ContextCompat.getDrawable(mContext, R.color.transparent));
            btnSend.setVisibility(View.VISIBLE);
            tvNumber.setVisibility(View.GONE);
            mIvUp.setVisibility(View.GONE);
        } else {
            if (btnFastClick.getVisibility() == View.VISIBLE)
                btnSend.setVisibility(View.GONE);
            else
                btnSend.setVisibility(View.VISIBLE);
            tvNumber.setVisibility(View.VISIBLE);
            mIvUp.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 这个是item里面带有按钮的事件回调
     *
     * @param adapter  adapter
     * @param view     view
     * @param position position
     */
    private void onAdapterClickEvent(BaseQuickAdapter adapter, View view, int position) {
        if (view instanceof TextView) {
            selectGift = (GiftDto) adapter.getData().get(position);
            switch (String.valueOf(view.getTag())) {
                //物品状态：1未使用 2使用中 3已到期
                case "1":
                    //物品类型：1礼物 2座驾 3贵族 4道具
                    requestUse(adapter, position);
                    break;
                case "2":
                    if (selectGift.goodsType == 2 && !selectGift.isOpened) {
                        new RetrofitUtils().createApi(LiveApiService.class)
                                .openCar(selectGift.goodsId, 1)
                                .compose(RxUtils.applySchedulers())
                                .subscribe(new LoadingObserver<HttpResult>() {
                                    @Override
                                    public void _onNext(HttpResult data) {
                                        if (data.isSuccess())
                                            currentGift = null;
                                        RxBus.get().post(new RefreshBackpack());
                                        userProviderService.updateUserInfo(new LoadingObserver() {
                                            @Override
                                            public void _onNext(Object data) {
                                                if (mContext instanceof LivingRoomActivity) {
                                                    ((LivingRoomActivity) mContext).getPresenter().userInfo = userProviderService.getUserInfo();
                                                }
                                            }

                                            @Override
                                            public void _onError(String msg) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void _onError(String msg) {

                                    }
                                });
                    } else if (selectGift.goodsType == 2) {
                        ARouter.getInstance().build(ArouterApi.CAR_ACTION)
                                .navigation();
                        dismiss();
                    }

                    if (selectGift.goodsType == 3) {
                        ARouter.getInstance().build(ArouterApi.VIP_ACTION)
                                .navigation();
                        dismiss();
                    }

                    break;
                case "3":
                    //物品类型：1礼物 2座驾 3贵族 4道具
                    if (selectGift.goodsType == 2) {
                        ARouter.getInstance().build(ArouterApi.CAR_ACTION)
                                .navigation();
                    }
                    if (selectGift.goodsType == 3) {
                        ARouter.getInstance().build(ArouterApi.VIP_ACTION)
                                .navigation();
                    }
                    dismiss();
                    break;

            }
        }
    }

    /**
     * 两个接口请求的切换
     *
     * @param adapter  adapter
     * @param position position
     * @return
     */
    private Flowable<HttpResult> requestUseChoose(BaseQuickAdapter adapter, int position) {
        GiftDto gift = (GiftDto) adapter.getData().get(position);
        if (gift.goodsType == 2) {
            return new RetrofitUtils().createApi(LiveApiService.class)
                    .requestUseCar(new BasePresenter().new ParamsBuilder()
                            .put("goodsId", gift.goodsId)
                            .put("goodsDuration", gift.goodsDuration).build());
        }
        if (gift.goodsType == 3) {
            return new RetrofitUtils().createApi(LiveApiService.class)
                    .requestUseVip(new BasePresenter().new ParamsBuilder()
                            .put("goodsId", gift.goodsId)
                            .put("goodsDuration", gift.goodsDuration).build());
        }

        return null;
    }


    /**
     * 使用座驾
     */
    private void requestUse(BaseQuickAdapter adapter, int position) {

        Flowable<HttpResult> http = requestUseChoose(adapter, position);
        if (http == null) return;
        GiftDto gift = (GiftDto) adapter.getData().get(position);
        http.compose(RxUtils.applySchedulers()).subscribe(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                if (data.isSuccess()) {
                    RxBus.get().post(new RefreshBackpack(gift));
                    userProviderService.updateUserInfo(new LoadingObserver() {
                        @Override
                        public void _onNext(Object data) {
                            if (mContext instanceof LivingRoomActivity) {
                                ((LivingRoomActivity) mContext).getPresenter().userInfo = userProviderService.getUserInfo();
                            }
                        }

                        @Override
                        public void _onError(String msg) {

                        }
                    });
                }
                ToastUtils.show(mContext, data.description, 300);
            }

            @Override
            public void _onError(String msg) {

            }
        });

    }

    int levelHighest = 0;

    //计算经验值
    @SuppressLint("SetTextI18n")
    private void handleUserLevel(GiftDto dto, int number) {
        if (userInfo == null || userLevelBeans == null) return;
        UserLevelBean levelBean;
        if (userInfo.userLevel > 0) {
            levelBean = userLevelBeans.get(userInfo.userLevel - 1);
        } else {
            levelBean = userLevelBeans.get(0);
        }

        int levelLowest = levelBean.levelLowest;
        levelHighest = levelBean.levelHighest;
        int futureExp;
        progressBar.setMax(100);
        if (userInfo.userLevel >= 46) {
            tvCurLevel.setText("LV.46");
            tvNextLevel.setText("已满级");
            progressBar.setProgress(100);
            progressBar.setSecondaryProgress(100);
            return;
        }
        if (!TextUtils.isEmpty(userInfo.experience) && Integer.parseInt(userInfo.experience) >= levelLowest) {
            BigDecimal myl = new BigDecimal(userInfo.experience).subtract(new BigDecimal(levelLowest + ""));
            BigDecimal next = new BigDecimal(levelHighest + "").subtract(new BigDecimal(levelLowest + ""));
            BigDecimal progress = myl.divide(next, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100"));

            progressBar.setProgress(progress.intValue());
            futureExp = (int) (Integer.parseInt(userInfo.experience) + dto.giftPrice * number);
        } else {
            progressBar.setProgress(0);
            futureExp = (int) (0 + dto.giftPrice * number);
        }

        //可获得经验值减当前最低经验值
        BigDecimal myls = new BigDecimal(futureExp + "").subtract(new BigDecimal(levelLowest + ""));
        for (int i = 0; i < userLevelBeans.size() - userInfo.userLevel - 1; i++) {
            if (myls.compareTo(new BigDecimal(userLevelBeans.get(i).levelLowest)) == 1) {
                BigDecimal nexts = new BigDecimal(levelHighest + "").subtract(new BigDecimal(levelLowest + ""));
                BigDecimal progresss = myls.divide(nexts, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_UP);
                progressBar.setSecondaryProgress(progresss.intValue());
                break;
            }
        }
        if (futureExp >= userLevelBeans.get(userInfo.userLevel).levelLowest) {
            for (int i = 0; i < userLevelBeans.size(); i++) {
                if (futureExp > userLevelBeans.get(i).levelLowest) {
                    if (userInfo.userLevel == userLevelBeans.get(i).levelId) {
                        tvNextLevel.setText("升至LV." + (userLevelBeans.get(i).levelId + 1));
                    } else {
                        if (userLevelBeans.get(i).levelId == userInfo.userLevel) {
                            tvNextLevel.setText("LV." + userLevelBeans.get(i).levelId);
                        } else
                            tvNextLevel.setText("升至LV." + userLevelBeans.get(i).levelId);
                    }
                } else {
                    break;
                }
            }

        } else {
            tvNextLevel.setText("LV." + (userInfo.userLevel + 1));
        }

        tvCurLevel.setText("LV." + userInfo.userLevel);
        BigDecimal jy = new BigDecimal(dto.giftPrice + "").multiply(new BigDecimal(number + "")).setScale(0, BigDecimal.ROUND_UP);
        tv_releft_exo.setText("+" + jy);

    }

    public void setBalance(UserInfo balance) {
        tvBalance.setText(balance.getBalance() + "");
        if (null != dto) {
            userInfo = balance;
            Log.e("tag", "等级" + userProviderService.getUserInfo().getExperience());
        }
        for (int i = 0; i < userLevelBeans.size(); i++) {
            if (Integer.parseInt(userInfo.experience) >= userLevelBeans.get(i).levelLowest && Integer.parseInt(userInfo.experience) < userLevelBeans.get(i).levelHighest) {
                userInfo.userLevel = userLevelBeans.get(i).levelId;
                Log.e("tag", "等级" + userLevelBeans.get(i).levelId);
                break;
            }
        }
        if (null != dto) {
            setUpUserLevel(dto);
        }
    }

    //设置经验条
    public void setjy(UserInfo nowuserInfo) {
        userInfo.userLevel = nowuserInfo.getUserLevel();
        userInfo.experience = nowuserInfo.getExperience();
    }

    public void setjys(String experience) {
        userInfo.experience = experience;
    }

    public void selectGiftById() {
        if (null != tabLayout) {
            tabLayout.getTabAt(allGifts.size() - 1).select();
        }
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
        Log.e("tag", "position=" + currentPage + "");
        currentPage = position;

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        currentGift = null;
        showSendBt(true, false);
        mRbBackpack.setChecked(false);
        mTvRemark.setVisibility(View.GONE);
        vpCheckPosition = tab.getPosition();
        currentPage = 0;
        if (null == vpAdapters.get(vpCheckPosition)) {
            GiftVpAdapter adapter = new GiftVpAdapter(mContext, allGifts.get(vpCheckPosition)) {
                @Override
                public void onItemClickListener(BaseQuickAdapter adapter1, View view, int position) {
                    super.onItemClickListener(adapter1, view, position);
                    onAdapterClickEvent(adapter1, view, position);
                }
            };
            vpAdapters.put(vpCheckPosition, adapter);
        }
        PagerAdapter adapter = vpAdapters.get(vpCheckPosition);
        viewPager.setAdapter(adapter);
        llPoint.setVisibility(adapter.getCount() <= 1 ? View.GONE : View.VISIBLE);
        setPoint(vpCheckPosition);
        setCheckZero(0);
        onClick(btnNum1);
    }

    //   private GiftDto actGiftDt;

    /**
     * @param giftId
     */
    public void setGiftId(int giftId) {
        selectGift = null;
        currentGift = null;
        for (int i = 0; i < allGifts.size(); i++) {
            List<GiftDto> giftDtos = allGifts.get(i);
            for (int j = 0; j < giftDtos.size(); j++) {
                GiftDto giftDto = giftDtos.get(j);
                if (giftDto.id == giftId) {
                    tabLayout.getTabAt(i).select();
                    //    actGiftDt = giftDto;

                    if (null == vpAdapters.get(i)) {
                        GiftVpAdapter adapter = new GiftVpAdapter(mContext, allGifts.get(i)) {
                            @Override
                            public void onItemClickListener(BaseQuickAdapter adapter1, View view, int position) {
                                super.onItemClickListener(adapter1, view, position);
                                onAdapterClickEvent(adapter1, view, position);
                            }
                        };
                        vpAdapters.put(i, adapter);

                    }

                    viewPager.setAdapter(vpAdapters.get(i));
                    GiftVpAdapter pagerAdapter = (GiftVpAdapter) vpAdapters.get(i);
                    pagerAdapter.setCheckUpLevel(false);
                    viewPager.setCurrentItem(j / 8);

                    currentPage = j / 8;

                    pagerAdapter.setInitPos(true, j / 8, j % 8 == 0 ? 0 : j % 8);
                }

            }
        }

    }

    //选中背包礼物
    public void setPackGiftId(GiftDto oldGift) {
        int packGiftIndex = 0;
        for (int i = 0; i < allGifts.size(); i++) {
            List<GiftDto> giftDtos = allGifts.get(i);
            for (int j = 0; j < giftDtos.size(); j++) {
                GiftDto giftDto = giftDtos.get(j);
//                Log.d("lucas", "giftDto.id:" + giftDto.id + ",oldGift.goodsId:" + oldGift.goodsId +
//                        ",giftDto.goodsId:" + giftDto.goodsId + ",giftDto.remainValidDays:" + giftDto.remainValidDays +
//                        ",oldGift.remainValidDays:" + oldGift.remainValidDays);
                if (giftDto.id == 0 && !TextUtils.isEmpty(oldGift.goodsId) && !TextUtils.isEmpty(giftDto.goodsId) && oldGift.goodsId.equals(giftDto.goodsId) && giftDto.goodsType == oldGift.goodsType) {
                    if ("座驾".equals(giftDto.giftTypeText)) {

                    } else if ("礼物".equals(giftDto.giftTypeText)) {
                        if (giftDto.remainValidDays != oldGift.remainValidDays) {
                            continue;
                        }
                    }
                    if (packGiftIndex < mRgBackpackNav.getChildCount()) {
                        mRgBackpackNav.getChildAt(packGiftIndex).performClick();
                    }
                    PagerAdapter adapter1 = vpAdapters.get(i);
                    if (null == adapter1) {
                        GiftVpAdapter adapter = new GiftVpAdapter(mContext, allGifts.get(i)) {
                            @Override
                            public void onItemClickListener(BaseQuickAdapter adapter1, View view, int position) {
                                super.onItemClickListener(adapter1, view, position);
                                onAdapterClickEvent(adapter1, view, position);
                            }
                        };
                        vpAdapters.put(i, adapter);
                    }
                    GiftVpAdapter pagerAdapter = (GiftVpAdapter) adapter1;
                    if (pagerAdapter.getCount() == 0) {
                        if (mRgBackpackNav.getChildCount() > 0) {
                            mRgBackpackNav.getChildAt(0).performClick();
                        } else {
                            mRbBackpack.setChecked(false);
                        }
                    }
                    pagerAdapter.setInitPos(true, j / 8, j % 8);
                    pagerAdapter.setCheckUpLevel(false);
                    viewPager.setAdapter(adapter1);
                    viewPager.setCurrentItem(j / 8);
                    Log.d("lucas", "j:" + j);
                }
            }
            if (!giftDtos.isEmpty() && giftDtos.get(0).id == 0) {
                packGiftIndex++;
            }
        }
    }

    private void setCheckZero(int position) {
        if (null == vpAdapters || vpAdapters.size() == 0) {
            return;
        }
        GiftVpAdapter adapter = (GiftVpAdapter) vpAdapters.get(vpCheckPosition);
        adapter.setCheck(position, 0);
    }

    private boolean type = false;

    public void setCheckUpLevel() {
        if (null == vpAdapters || vpAdapters.size() == 0) {
            return;
        }
        vpCheckPosition = 0;
        if (null != tabLayout) {
            tabLayout.getTabAt(0).select();
        }

        type = true;
        GiftVpAdapter adapter = (GiftVpAdapter) vpAdapters.get(vpCheckPosition);
        adapter.setCheckUpLevel(true);
        viewPager.setAdapter(vpAdapters.get(vpCheckPosition));
        adapter.setCheck(0, 1);
        tvNumber.setText("2");
        tvNumber.setTag("2");
        my_number = 2;

        tvNumber.postDelayed(new Runnable() {
            @Override
            public void run() {
                type = false;
            }
        }, 500);
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
        allGifts.clear();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_up) {
            if (tvNumber.isFocused()) {
                View parent = (View) tvNumber.getParent();
                parent.requestFocus();
            }
            if (!isEnabled) return;
            int[] location = new int[2];
            llNumber.getLocationOnScreen(location);
            mIvUp.setImageResource(R.drawable.ic_gift_number_up);
            FiftNumberPopwindow fiftNumberPopwindow = new FiftNumberPopwindow(getContext(), sendNum, my_number);
            fiftNumberPopwindow.setOnItemClickListener(new FiftNumberPopwindow.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, String number) {
                    tvNumber.setText(number);
                    my_number = Integer.parseInt(number);
                    handleUserLevel(dto, my_number);
                }

                @Override
                public void isDismiss() {
                    mIvUp.setImageResource(R.drawable.ic_gift_number_down);
                }
            });
            fiftNumberPopwindow.showPopupWindow();

        } else if (id == R.id.btn_num1) {
            getCheckTv().setEnabled(true);
            numCheckPosition = 0;
            btnNum1.setEnabled(false);
        } else if (id == R.id.btn_num10) {
            getCheckTv().setEnabled(true);
            numCheckPosition = 1;
            btnNum10.setEnabled(false);
        } else if (id == R.id.btn_num66) {
            getCheckTv().setEnabled(true);
            numCheckPosition = 2;
            btnNum66.setEnabled(false);
        } else if (id == R.id.btn_num520) {
            getCheckTv().setEnabled(true);
            numCheckPosition = 3;
            btnNum520.setEnabled(false);
        } else if (id == R.id.btn_send) {
            sendGift(view);
        } else if (id == R.id.fastclick_view) {
            if (countBackUtils.isTiming()) {
                countBackUtils.updateTime(3);
            }
            if (null != listener) {
                GiftVpAdapter adapter = (GiftVpAdapter) vpAdapters.get(vpCheckPosition);
                dto = adapter.getGift(currentPage);
                if (null == dto) {
                    return;
                }
                listener.onGiftSend(dto, Integer.parseInt(tvNumber.getText().toString()));
                setUpUserLevel(dto);
            }
        }
    }

    private void sendGift(View view) {
        String numStr = tvNumber.getText().toString();
        if (TextUtils.isEmpty(numStr)) {
            ToastUtils.show(mContext, "请输入礼物数量", Toast.LENGTH_SHORT);
            return;
        }
        int num = Integer.parseInt(numStr);
        if (num <= 0) {
            ToastUtils.show(mContext, "请输入礼物数量", Toast.LENGTH_SHORT);
            return;
        }
        if (view.getTag() != null) {
            currentGift = (GiftDto) view.getTag();
            if (currentGift.goodsCount <= 0) return;
            //这个是抽奖
            if (currentGift.goodsType == 4) {
                int activeId = Integer.parseInt(currentGift.activityId);
                if ("8".equals(currentGift.activityId) || (activeId > 9 && activeId < 18)) {
                    RxBus.get().post("yyh_GG");
                    RxBus.get().post(new RefreshBackpack());
                    dismiss();
                }
                //这个是赠送
                else if ("9".equals(currentGift.activityId)) {
                    mGivingGiftsPopWindow.setData(
                            mZhuboDto.channelId + "",
                            mZhuboDto.headImg,
                            mZhuboDto.nickname,
                            currentGift.goodsIcon,
                            currentGift.remainCount + "",
                            currentGift.goodsId);
                    mGivingGiftsPopWindow.showPopupWindow();
                    dismiss();
                }
                return;
            }
        }

        GiftVpAdapter adapter = (GiftVpAdapter) vpAdapters.get(vpCheckPosition);
        dto = adapter.getGift(currentPage);
        currentGift = dto;
        List<AnchorWishBean> existWish = WishCacheMr.getInstance().getExistWish();
        for (AnchorWishBean anchorWishBean : existWish) {
            if (anchorWishBean.giftId == dto.id) {
                dto.isWishGift = 1;
            }
        }

        if (null != listener) {
            if (null == dto) {
                return;
            }
            listener.onGiftSend(dto, num);
            setUpUserLevel(dto);
        }
        if (downTime == 0) {
            downTime = System.currentTimeMillis();
        } else {
            long now = System.currentTimeMillis();
            if (now - downTime <= 3500) {
                isFast = true;
                isShowFastClick(true);
            } else {
                isFast = false;
                isShowFastClick(false);
            }
            downTime = System.currentTimeMillis();
        }
    }

    private void setUpUserLevel(GiftDto dto) {
        KLog.e("tag", "送礼////////////////////////");
        handleUserLevel(dto, my_number);
    }

    @Override
    public void showPopupWindow() {
        super.showPopupWindow();
        RxBus.get().post(new RefreshBackpack());
        RxBus.get().post(new PaySuccessEvent(""));
    }

    private void isShowFastClick(boolean b) {
        if (b) {
            btnSend.setVisibility(View.GONE);
            buttonOut.startAnimation(operatingAnim);
            btnFastClick.setVisibility(View.VISIBLE);
            countBackUtils.countBack(4, new CountBackUtils.Callback() {
                @Override
                public void countBacking(long time) {
                    downTime = System.currentTimeMillis();
                    tvClickLeftTime.setText(time + "");
                }

                @Override
                public void finish() {
                    btnFastClick.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                    downTime = 0;
                    tvClickLeftTime.setText("3");
                }
            });
        } else {
            buttonOut.clearAnimation();
            btnSend.setVisibility(View.VISIBLE);
            btnFastClick.setVisibility(View.GONE);
        }
    }

    private TextView getCheckTv() {
        switch (numCheckPosition) {
            case 0:
                return btnNum1;
            case 1:
                return btnNum10;
            case 2:
                return btnNum66;
            case 3:
                return btnNum520;
        }
        return null;
    }

    OnGiftSendListener listener;

    public void setIsWish(boolean isWish) {
        this.isWish = isWish;
    }

    public void setData(ZhuboDto zhuboDto) {
        this.mZhuboDto = zhuboDto;
    }

    public interface OnGiftSendListener {
        void onGiftSend(GiftDto gift, int num);
    }

    public void setOnGiftSendListener(OnGiftSendListener l) {
        listener = l;

    }
}
