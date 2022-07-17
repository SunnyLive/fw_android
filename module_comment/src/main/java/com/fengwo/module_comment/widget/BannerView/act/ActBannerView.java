package com.fengwo.module_comment.widget.BannerView.act;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.bean.ActBannerBean;
import com.fengwo.module_comment.bean.PkMatchScoreDto;
import com.fengwo.module_comment.bean.RefreshProcessesBean;
import com.fengwo.module_comment.ext.ViewExtKt;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CountBackUtilsNew;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.SvgaUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.widget.BannerView.FixedSpeedScroller;
import com.fengwo.module_comment.widget.DrawView;
import com.opensource.svgaplayer.SVGAImageView;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * @author min
 * @data 2017/4/26
 * @Description 直播间活动banner 实现无限轮播 BannerBean需实现 ActBannerBean 接口
 */
public class ActBannerView<T> extends RelativeLayout {

    private final static String LIVE = "live";
    private final static String MOVIE = "movie";

    private Context mContext;
    private View mView;
    private ViewPager vp;
//    private LinearLayout pointLl;

    private int viewLength;
    private boolean mIsChanged = false;
    private static final long AD_CHANGE_TIME = 5000;
    private int mCurrentPagePosition = FIRST_ITEM_INDEX;
    private int mCurrentIndex;
    private static final int FIRST_ITEM_INDEX = 1;
    public boolean isRunning;
    private ArrayList<ActBannerBean> mBannerList = new ArrayList<>();
    @Autowired
    UserProviderService userProviderService;

    private View integralView, pkActivityView, normalActivityView;
    private View wishView;
    private View signView;
    private View panishView, boxView;
    private CountDownTimer planeCountDown;
    private CountDownTimer boxCountDown;

    private int default_img = R.drawable.live_home_default;
    private View halloeen;
    private CountBackUtilsNew activityCb;
    private boolean ispk = true;// 是否正常直播
    private CountDownTimer timer;

    //
    // 如果是pk 就隐藏SVGA的这个view
    //
    public void setVisibilitySVGA(boolean isVisibility) {
        ispk = isVisibility;
        if (vp.getAdapter() instanceof ActBannerView.MainAdPageradapter) {
            List<View> viewList = ((MainAdPageradapter) vp.getAdapter()).getViewList();
            for (View view : viewList) {
                if (view.findViewById(R.id.im_sds) != null) {
                    if (ispk) {
                        view.findViewById(R.id.im_sds).setVisibility(VISIBLE);
                    } else {
                        view.findViewById(R.id.im_sds).setVisibility(GONE);
                    }
                    //  view.findViewById(R.id.im_sds).setAlpha(isVisibility ? 1 : 0);
                }
            }
            if (null != vp && vp.getAdapter() != null && vp.getAdapter().getCount() > 0 && views.size() > 0) {
                vp.getAdapter().notifyDataSetChanged();
            }
        }
    }


    public void setDefaultImg(int img) {
        default_img = img;
    }

    OnActBannerItemCliakListenr OnActBannerItemCliakListenr;

    public void setOnActBannerItemCliakListenr(OnActBannerItemCliakListenr l) {
        OnActBannerItemCliakListenr = l;
    }


    //    // 菜单滚动
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1 && isRunning) {
                if (++mCurrentPagePosition < viewLength) {
                } else {
                    mCurrentPagePosition = 0;
                }
                vp.setCurrentItem(mCurrentPagePosition);
                sendEmptyMessageDelayed(1, AD_CHANGE_TIME);
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void stopAutoScroll() {
        isRunning = false;
        //    views.clear();
        mHandler.removeMessages(1);

//stop调的比较多 不知道是不是有别的特殊原因 先这么写
        if (mBannerList.size() > 1) {
            isRunning = true;
            mHandler.sendEmptyMessageDelayed(1, AD_CHANGE_TIME);

        } else {
            isRunning = false;
            mHandler.removeMessages(1);
        }
    }

    public void startAutoScroll() {
        if (isRunning) return;
        if (mBannerList != null && mBannerList.size() > 1) {
            isRunning = true;
            mHandler.sendEmptyMessageDelayed(1, AD_CHANGE_TIME);
        }
    }


    public ActBannerView(Context context) {
        this(context, null);
    }

    public ActBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ActBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ArouteUtils.inject(this);
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.banner_view_layout, this, true);
        vp = (ViewPager) mView.findViewById(R.id.main_viewpager);

   //     vp.setPageTransformer(true,new ZoomOutPageTransformer());
//        pointLl = (LinearLayout) mView.findViewById(R.id.main_bannerview_pointgroup);
        setViewPagerScrollSpeed();
    }
    private void setViewPagerScrollSpeed() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(vp.getContext());
            mScroller.set(vp, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        } catch (Exception e){

        }
    }
    List<View> views = new ArrayList<View>();

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int pPosition) {
            mIsChanged = true;
            if (pPosition > viewLength - 2) {
                mCurrentPagePosition = FIRST_ITEM_INDEX;
            } else if (pPosition < FIRST_ITEM_INDEX) {
                mCurrentPagePosition = viewLength - 2;
            } else {
                mCurrentPagePosition = pPosition;
            }

            if (null != views.get(pPosition).getTag() && !TextUtils.isEmpty(views.get(pPosition).getTag() + "") && views.get(pPosition).getTag().toString().equals("sdj")) {
                SVGAImageView im_sds = views.get(pPosition).findViewById(R.id.im_sds);
                im_sds.stopAnimation();
                if (ispk) {
                    im_sds.setVisibility(VISIBLE);
                } else {
                    im_sds.setVisibility(GONE);
                }
                svgaUtils = new SvgaUtils(mContext, im_sds, 0);
                svgaUtils.initAnimator();
                svgaUtils.startAnimator(sdSvgaName);
            }

//           KLog.e("tag","type= "+mBannerList.get(pPosition%mBannerList.size()).type);
//            if(mBannerList.get(pPosition%mBannerList.size()).type==8){
//                SvgaUtils svgaUtils = new SvgaUtils(mContext, im_sds);
//                svgaUtils.initAnimator();
//                svgaUtils.repeat();
//                svgaUtils.startAnimator(sdSvgaName);
//            }else {
//                if(null!=im_sds){
//                    im_sds.stopAnimation();
//                }
//
//            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            //   KLog.e("tag","0="+arg0+"1="+arg1+"2="+arg2);

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (ViewPager.SCROLL_STATE_IDLE == arg0) {
                if (mIsChanged) {
                    mIsChanged = false;
                    vp.setCurrentItem(mCurrentPagePosition, false);
                }
            }
        }
    };

    public void setBanner(ArrayList<ActBannerBean> bannerList) {
        if (bannerList == null || bannerList.size() == 0) {
            setVisibility(GONE);
            return;
        }
        //    setVisibility(VISIBLE);
        mBannerList = bannerList;
        stopAutoScroll();
        views = new ArrayList<View>();
        setVisibility(VISIBLE);
        if (mBannerList.size() > 1)
            views.add(addView(mBannerList.get(mBannerList.size() - 1)));
        for (int i = 0; i < mBannerList.size(); i++) {
            views.add(addView(mBannerList.get(i)));
        }
        if (mBannerList.size() > 1) {
            views.add(addView(mBannerList.get(0)));
        }
        viewLength = views.size();
        vp.setAdapter(new MainAdPageradapter(views));
        vp.removeOnPageChangeListener(mOnPageChangeListener);
        vp.addOnPageChangeListener(mOnPageChangeListener);
        vp.setCurrentItem(mCurrentPagePosition, false);
        if (mBannerList.size() > 1) {
            isRunning = true;
            mHandler.sendEmptyMessageDelayed(1, AD_CHANGE_TIME);

        } else {
            isRunning = false;
            mHandler.removeMessages(1);
        }

    }


    private View addView(final ActBannerBean banner) {
        switch (banner.type) {
            case 1://积分
                return getIntegralView(banner);
            case 2://心愿
                return getWishView(banner);
            case 3://签到
                return getSignView(banner);
            case 4://惩罚
                return getPanishView(banner);
            case 5://pk排位赛活动
                return getPkActivity(banner);
            case 6://普通活动
                return getNormalActivity(banner);
            case 7://万圣节 宝箱被我删了  万圣节的视频我也删了 之前活动复用请重新开发 或者去旧分支找代码
                return getHalloween(banner);
            case 8://圣诞节
                return getChristmas(banner);
            //  return getHalloween(banner);
            case 9://元旦
                return getNewYear(banner);
            case 10://新年
                return getChristmas(banner);
            case 11://新年
                return getChristmas(banner);
            case 12://新年
                return getChristmas(banner);
            case 13://新年
                return getChristmas(banner);
            case 14://新年
                return getChristmas(banner);
            case 15://新年
                return getChristmas(banner);
            case 16://新年
                return getChristmas(banner);
            case 17://女神节
                return getChristmas(banner);
            default:
                return null;
        }
    }


    private View getNormalActivity(ActBannerBean banner) {
        normalActivityView = inflate(mContext, R.layout.layout_act_normal, null);
        RelativeLayout clActBg = normalActivityView.findViewById(R.id.cl_act_bg);
        ImageView ivActBg = normalActivityView.findViewById(R.id.iv_act_bg);
        ImageView ivLevel = normalActivityView.findViewById(R.id.iv_act_level);
        ImageLoader.loadImg(ivLevel, banner.levelIcon);
        ImageLoader.loadImg(ivActBg, banner.backgroundImg);
        TextView tvGrade = normalActivityView.findViewById(R.id.tv_act_grade);
        TextView tvAchievement = normalActivityView.findViewById(R.id.tv_act_achievement);
//        tvAchievement.setText(banner.sort <= 50 ? "排名：" + banner.sort + "" : "排名：50+");
        //此处判断中秋活动返回值是否为空，空显示SK2活动
        if (TextUtils.isEmpty(banner.integralInfo)) {
            tvGrade.setText("缘分值：" + DataFormatUtils.formatNumbersHot(banner.integral));
            tvAchievement.setText(banner.sort <= 50 ? "排名：" + banner.sort + "" : "排名：50+");
        } else {
            tvGrade.setText(banner.sortInfo);
            tvAchievement.setText(banner.integralInfo);
        }

//        clActBg.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (OnActBannerItemCliakListenr != null)
//                    OnActBannerItemCliakListenr.normalActJump(banner.address);
//            }
//        });
        ViewExtKt.setCustomClick(clActBg, new Function1<View, Unit>() {
            @Override
            public Unit invoke(View view) {
                if (OnActBannerItemCliakListenr != null)
                    OnActBannerItemCliakListenr.normalActJump(banner.address);
                return null;
            }
        });
        return normalActivityView;
    }

    @SuppressLint("SetTextI18n")
    public void updateActivityScore(PkMatchScoreDto pkMatchScoreDto) {
        if (pkActivityView == null || mBannerList == null) return;
        stopAutoScroll();
        for (int i = 0; i < mBannerList.size(); i++) {
            if (mBannerList.get(i).type == 5) {
                mBannerList.get(i).combatScore = Integer.parseInt(pkMatchScoreDto.combatScore);
                mBannerList.get(i).levelIcon = pkMatchScoreDto.levelIcon;
                mBannerList.get(i).winningStreakNum = Integer.parseInt(pkMatchScoreDto.winningStreakNum);
                mBannerList.get(i).losingStreakNum = Integer.parseInt(pkMatchScoreDto.losingStreakNum);
            }

        }
        setBanner(mBannerList);
    }

    public void endActivity(int activityId) {
        for (int i = 0; i < mBannerList.size(); i++) {
            if (mBannerList.get(i).type == activityId) {
                mBannerList.remove(i);
            }
        }
        setBanner(mBannerList);
    }


    public void updateBannerData(ActBannerBean bannerBean) {
        stopAutoScroll();
        if (bannerBean == null || bannerBean.type == 4) {    //宝箱活动不开启了和飞机的活动，前台写死
            return;
        }
//        if(bannerBean.type>9&&bannerBean.type<17){
//            for (int i = 0; i < mBannerList.size(); i++) {
//                if(mBannerList.get(i).type>9&&mBannerList.get(i).type<17){
//                    mBannerList.remove(i);
//                }
//            }
//        }
        if (!hasData(bannerBean)) {
            mBannerList.add(bannerBean);
        }
        for (int i = 0; i < mBannerList.size(); i++) {
            if (mBannerList.get(i).type == mBannerList.get(i).type) {
                mBannerList.get(i).sort = bannerBean.sort;
                mBannerList.get(i).castleSort = bannerBean.castleSort;
                mBannerList.get(i).refreshProcesses = bannerBean.refreshProcesses;
                mBannerList.get(i).planeOpenSecond = bannerBean.planeOpenSecond;
                mBannerList.get(i).address = bannerBean.address;
                if (bannerBean.type == 9) {
                    mBannerList.get(i).isChance = bannerBean.isChance;

                }
            }
        }

        if (bannerBean.type > 9 && bannerBean.type < 18) {
            for (int i = 0; i < views.size(); i++) {
                if (null != views.get(i).getTag() && !TextUtils.isEmpty(views.get(i).getTag() + "") && views.get(i).getTag().toString().equals("sdj")) {
                    getChristmasUp(bannerBean, views.get(i));
                }
            }
        } else
            setBanner(mBannerList);
    }

    private boolean hasData(ActBannerBean bannerBean) {
        boolean istype = false;
        for (int i = 0; i < mBannerList.size(); i++) {
            if (mBannerList.get(i).type == bannerBean.type) {
                mBannerList.get(i).combatScore = bannerBean.combatScore;
                mBannerList.get(i).levelIcon = bannerBean.levelIcon;
                mBannerList.get(i).winningStreakNum = bannerBean.winningStreakNum;
                mBannerList.get(i).losingStreakNum = bannerBean.losingStreakNum;
                mBannerList.get(i).integral = bannerBean.integral;
                mBannerList.get(i).sort = bannerBean.sort;
                mBannerList.get(i).integralInfo = bannerBean.integralInfo;
                mBannerList.get(i).backgroundImg = bannerBean.backgroundImg;
                mBannerList.get(i).sortInfo = bannerBean.sortInfo;
                mBannerList.get(i).address = bannerBean.address;
                mBannerList.get(i).planeOpenSecond = bannerBean.planeOpenSecond;
                istype = true;
                //    return true;
            }
        }
        return istype;
    }

    private View getPkActivity(ActBannerBean banner) {
        pkActivityView = inflate(mContext, R.layout.layout_act_pk, null);
        ConstraintLayout clActBg = pkActivityView.findViewById(R.id.cl_act_bg);
        ImageView ivActBg = pkActivityView.findViewById(R.id.iv_act_bg);
        ImageView ivLevel = pkActivityView.findViewById(R.id.iv_act_level);
        ImageLoader.loadImg(ivLevel, banner.levelIcon);
        TextView tvGrade = pkActivityView.findViewById(R.id.tv_act_grade);
        tvGrade.setText("战力值：" + DataFormatUtils.formatNumbersHot(banner.combatScore));
        TextView tvAchievement = pkActivityView.findViewById(R.id.tv_act_achievement);
        if (banner.losingStreakNum == banner.winningStreakNum) {
            tvAchievement.setText(banner.winningStreakNum + "连胜");
        } else if (banner.winningStreakNum > 0) {
            tvAchievement.setText(banner.winningStreakNum + "连胜");
        } else {
            tvAchievement.setText(banner.losingStreakNum + "连败");
        }
        ImageLoader.loadImg(ivActBg, banner.backgroundImg);
//        clActBg.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (OnActBannerItemCliakListenr != null)
//                    OnActBannerItemCliakListenr.integralJump(banner.address);
//            }
//        });
        ViewExtKt.setCustomClick(clActBg, new Function1<View, Unit>() {
            @Override
            public Unit invoke(View view) {
                if (OnActBannerItemCliakListenr != null)
                    OnActBannerItemCliakListenr.integralJump(banner.address);
                return null;
            }
        });
        return pkActivityView;
    }

    private long time = System.currentTimeMillis();
    private long remainTime;

    //仙女惩罚
    private View getPanishView(ActBannerBean banner) {
        if (panishView == null) {
            panishView = inflate(mContext, R.layout.layout_act_panish, null);
        }
        ImageView ivPanishGift = panishView.findViewById(R.id.iv_panish_gift);
        TextView tvPanishSend = panishView.findViewById(R.id.tv_panish_send);
//        tvPanishPrice.setText(banner.giftPrice + "");
        ImageLoader.loadImg(ivPanishGift, banner.icon, R.drawable.bg_plane);
//        panishView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (OnActBannerItemCliakListenr != null)
//                    OnActBannerItemCliakListenr.panishJump(banner.boxId);
//            }
//        });
        ViewExtKt.setCustomClick(panishView, new Function1<View, Unit>() {
            @Override
            public Unit invoke(View view) {
                if (OnActBannerItemCliakListenr != null)
                    OnActBannerItemCliakListenr.panishJump(banner.boxId);
                return null;
            }
        });
        if (banner.planeOpenSecond > 0 && !banner.isPush) {
            if (planeCountDown != null) {
                planeCountDown.cancel();
                planeCountDown = null;
            }
            planeCountDown = new CountDownTimer(banner.planeOpenSecond * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvPanishSend.setVisibility(VISIBLE);
                    tvPanishSend.setText(TimeUtils.calT((int) (millisUntilFinished / 1000)));
                    remainTime = millisUntilFinished;
                    banner.planeOpenSecond = (int) (millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    if (System.currentTimeMillis() - time > 1000 && remainTime < 1000 && OnActBannerItemCliakListenr != null) {
                        OnActBannerItemCliakListenr.showPlaneMp4(banner.boxId);
                        time = System.currentTimeMillis();
                    }
                    banner.planeOpenSecond = (0);
                    tvPanishSend.setVisibility(GONE);

                }
            }.start();
//            planeCountDown = new CountBackUtils();
//            planeCountDown.countBack(banner.planeOpenSecond, new CountBackUtils.Callback() {
//                @Override
//                public void countBacking(long time) {
//                    tvPanishSend.setVisibility(VISIBLE);
//                    tvPanishSend.setText(TimeUtils.cal((int) time));
////                    remainTime = time;
//                }
//
//                @Override
//                public void finish() {
//                    if (OnActBannerItemCliakListenr!=null){
//                        OnActBannerItemCliakListenr.showPlaneMp4(banner.boxId);
//                    }
//                    tvPanishSend.setVisibility(GONE);
//                }
//            });
        }
        return panishView;
    }

    //宝箱view
    private View getBoxView(ActBannerBean banner) {
//        if (boxView==null){
        boxView = inflate(mContext, R.layout.layout_act_box, null);
//        }
        ImageView ivPanishGift = boxView.findViewById(R.id.iv_panish_gift);
        TextView tvPanishSend = boxView.findViewById(R.id.tv_panish_send);
        ImageLoader.loadImg(ivPanishGift, banner.icon, R.drawable.ic_box);
//        boxView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (OnActBannerItemCliakListenr != null)
//                    OnActBannerItemCliakListenr.openBox(banner.address);
//            }
//        });
        ViewExtKt.setCustomClick(boxView, new Function1<View, Unit>() {
            @Override
            public Unit invoke(View view) {
                if (OnActBannerItemCliakListenr != null)
                    OnActBannerItemCliakListenr.openBox(banner.address);
                return null;
            }
        });
        if (!banner.userIsOpen) {
            tvPanishSend.setVisibility(VISIBLE);
            tvPanishSend.setText("待开启");
            if (banner.planeOpenSecond > 0) {
                if (boxCountDown != null) {
                    boxCountDown.cancel();
                    boxCountDown = null;
                }
                boxCountDown = new CountDownTimer(banner.planeOpenSecond * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvPanishSend.setText(TimeUtils.fromSecond((int) (millisUntilFinished / 1000)));
                        banner.planeOpenSecond = (int) (millisUntilFinished / 1000);
                    }

                    @Override
                    public void onFinish() {
                        tvPanishSend.setText("待开启");
                        banner.planeOpenSecond = 0;

                    }
                }.start();
            }
        } else {
            tvPanishSend.setVisibility(GONE);
        }
        return boxView;
    }

    private int castleSort = -1;
    boolean istrue = true;

    //万圣节
    private View getHalloween(ActBannerBean banner) {

        halloeen = inflate(mContext, R.layout.layout_act_halloween, null);
        LinearLayout iv_panish_gift = halloeen.findViewById(R.id.iv_panish_gift);
        TextView tvTopGrade = halloeen.findViewById(R.id.tv_top_grade);
        LinearLayout ll_gift = halloeen.findViewById(R.id.ll_gift);
        LinearLayout ll_box = halloeen.findViewById(R.id.ll_box);
        ImageView im_views = halloeen.findViewById(R.id.im_views);
        LinearLayout tvPanishSend = halloeen.findViewById(R.id.tv_panish_send);
        switch (banner.castleSort) {
            case 0:
                iv_panish_gift.setBackgroundResource(R.mipmap.pic_halloween1);
                break;
            case 1:
                iv_panish_gift.setBackgroundResource(R.mipmap.pic_halloween2);
                break;
            case 2:
                iv_panish_gift.setBackgroundResource(R.mipmap.pic_halloween3);
                break;
        }
        if (banner.sort > 30 || banner.sort == 0) {
            tvTopGrade.setText("未上榜");
        } else {
            tvTopGrade.setText("日榜TOP " + banner.sort + "");
        }
        if (banner.planeOpenSecond > 0) {//如果时间大于0  就显示红包倒计时

            ll_gift.setVisibility(GONE);
            ll_box.setVisibility(VISIBLE);
            TextView tv_djs = halloeen.findViewById(R.id.tv_djs);
//            activityCb = new CountBackUtilsNew();
//            activityCb.countBack(banner.planeOpenSecond + 1, new CountBackUtilsNew.Callback() {
//                @Override
//                public void countBacking(long time) {
//                    tv_djs.setText(TimeUtils.fromSecond((int) (time)));
//                }
//
//                @Override
//                public void finish() {
//
//                    if (OnActBannerItemCliakListenr != null && ActBannerView.this.castleSort != banner.castleSort) {
//                        OnActBannerItemCliakListenr.actRedBoxRain(banner.castleSort);
//                        ActBannerView.this.castleSort = banner.castleSort;
//                    }
//
//                }
//            });
            return halloeen;
        }
        if (null != activityCb && activityCb.isTiming()) {
            return halloeen;
        }
        if (banner.isFinish) {//如果结束 显示结束对话框
            iv_panish_gift.setBackgroundResource(R.mipmap.pic_halloween4);
            im_views.setVisibility(VISIBLE);
            ll_gift.setVisibility(GONE);
            ll_box.setVisibility(GONE);

            if (OnActBannerItemCliakListenr != null)
                OnActBannerItemCliakListenr.endActivitiy();

//            iv_panish_gift.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (OnActBannerItemCliakListenr != null)
//                        OnActBannerItemCliakListenr.actWSJ();
//                }
//            });
            ViewExtKt.setCustomClick(iv_panish_gift, new Function1<View, Unit>() {
                @Override
                public Unit invoke(View view) {
                    if (OnActBannerItemCliakListenr != null)
                        OnActBannerItemCliakListenr.actWSJ();
                    return null;
                }
            });
//            tvPanishSend.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (OnActBannerItemCliakListenr != null)
//                        OnActBannerItemCliakListenr.openBox(banner.address);
//                }
//            });
            ViewExtKt.setCustomClick(tvPanishSend, new Function1<View, Unit>() {
                @Override
                public Unit invoke(View view) {
                    if (OnActBannerItemCliakListenr != null)
                        OnActBannerItemCliakListenr.openBox(banner.address);
                    return null;
                }
            });
            return halloeen;
        }
        im_views.setVisibility(GONE);
        if (banner.planeOpenSecond <= 0) {//如果时间小于0 就显示进度条
            if (banner.refreshProcesses.size() == 0) {
                if (istrue) {
                    istrue = false;
                    if (OnActBannerItemCliakListenr != null)
                        OnActBannerItemCliakListenr.actWSJ();
                }

            }
            ll_gift.setVisibility(VISIBLE);
            ll_box.setVisibility(GONE);
            TextView tv_name1 = halloeen.findViewById(R.id.tv_name1);
            TextView tv_name2 = halloeen.findViewById(R.id.tv_name2);
            TextView tv_name3 = halloeen.findViewById(R.id.tv_name3);
            DrawView tv_draw1 = halloeen.findViewById(R.id.tv_draw1);
            DrawView tv_draw2 = halloeen.findViewById(R.id.tv_draw2);
            DrawView tv_draw3 = halloeen.findViewById(R.id.tv_draw3);
            for (int i = 0; i < banner.refreshProcesses.size(); i++) {
                switch (i) {
                    case 0:
                        tv_name1.setText(banner.refreshProcesses.get(i).getGiftName());
                        int a = banner.refreshProcesses.get(i).getCustomsValue().divide(banner.refreshProcesses.get(i).getCustomsTotalValue(), 2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).intValueExact();
                        if (a == 0 || a > 100) {
                            if (a == 0) {
                                tv_draw1.setYIYOU(0, banner.refreshProcesses.get(i).getCustomsValue().toString(), banner.refreshProcesses.get(i).getCustomsTotalValue().toString());
                            } else {
                                tv_draw1.setYIYOU(100, banner.refreshProcesses.get(i).getCustomsValue().toString(), banner.refreshProcesses.get(i).getCustomsTotalValue().toString());
                            }
                        } else {
                            tv_draw1.setYIYOU(a, banner.refreshProcesses.get(i).getCustomsValue().toString(), banner.refreshProcesses.get(i).getCustomsTotalValue().toString());
                        }


                        break;
                    case 1:
                        tv_name2.setText(banner.refreshProcesses.get(i).getGiftName());
                        int b = banner.refreshProcesses.get(i).getCustomsValue().divide(banner.refreshProcesses.get(i).getCustomsTotalValue(), 2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).intValueExact();
                        if (b == 0 || b > 100) {
                            if (b == 0) {
                                tv_draw2.setYIYOU(0, banner.refreshProcesses.get(i).getCustomsValue().toString(), banner.refreshProcesses.get(i).getCustomsTotalValue().toString());
                            } else {
                                tv_draw2.setYIYOU(100, banner.refreshProcesses.get(i).getCustomsValue().toString(), banner.refreshProcesses.get(i).getCustomsTotalValue().toString());
                            }
                        } else {
                            tv_draw2.setYIYOU(b, banner.refreshProcesses.get(i).getCustomsValue().toString(), banner.refreshProcesses.get(i).getCustomsTotalValue().toString());
                        }
                        break;
                    case 2:
                        tv_name3.setText(banner.refreshProcesses.get(i).getGiftName());
                        int c = banner.refreshProcesses.get(i).getCustomsValue().divide(banner.refreshProcesses.get(i).getCustomsTotalValue(), 2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal("100")).intValueExact();
                        if (c == 0 || c > 100) {
                            if (c == 0) {
                                tv_draw3.setYIYOU(0, banner.refreshProcesses.get(i).getCustomsValue().toString(), banner.refreshProcesses.get(i).getCustomsTotalValue().toString());
                            } else {
                                tv_draw3.setYIYOU(100, banner.refreshProcesses.get(i).getCustomsValue().toString(), banner.refreshProcesses.get(i).getCustomsTotalValue().toString());
                            }
                        } else {
                            tv_draw3.setYIYOU(c, banner.refreshProcesses.get(i).getCustomsValue().toString(), banner.refreshProcesses.get(i).getCustomsTotalValue().toString());
                        }
                        break;
                }
            }


//            iv_panish_gift.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (OnActBannerItemCliakListenr != null)
//                        OnActBannerItemCliakListenr.actWSJ();
//                }
//            });
            ViewExtKt.setCustomClick(iv_panish_gift, new Function1<View, Unit>() {
                @Override
                public Unit invoke(View view) {
                    if (OnActBannerItemCliakListenr != null)
                        OnActBannerItemCliakListenr.actWSJ();
                    return null;
                }
            });
//            tvPanishSend.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (OnActBannerItemCliakListenr != null)
//                        OnActBannerItemCliakListenr.openBox(banner.address);
//                }
//            });
            ViewExtKt.setCustomClick(tvPanishSend, new Function1<View, Unit>() {
                @Override
                public Unit invoke(View view) {
                    if (OnActBannerItemCliakListenr != null)
                        OnActBannerItemCliakListenr.openBox(banner.address);
                    return null;
                }
            });
        }


        return halloeen;
    }

    private View getNewYear(ActBannerBean banner) {
        View newyearView = inflate(mContext, R.layout.layout_act_newyear, null);
        TextView tv_no = newyearView.findViewById(R.id.tv_no);
        TextView tv_jf = newyearView.findViewById(R.id.tv_jf);

        RelativeLayout rv_2 = newyearView.findViewById(R.id.rv_2);
        RelativeLayout rl_cp = newyearView.findViewById(R.id.rl_cp);

        if (banner.isChance) {
            rl_cp.setBackgroundResource(R.drawable.pic_newyear_msxy);
        } else {
            rl_cp.setBackgroundResource(R.drawable.pic_newyear_yyq);
        }

        if (banner.sort == -1) {
            tv_no.setText("未上榜");
        } else {
            tv_no.setText("No." + banner.sort + "");
        }
        tv_jf.setText(banner.integral + "");
//        rl_cp.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (OnActBannerItemCliakListenr != null)
//                    OnActBannerItemCliakListenr.actNewYear(banner);
//            }
//        });
        ViewExtKt.setCustomClick(rl_cp, new Function1<View, Unit>() {
            @Override
            public Unit invoke(View view) {
                if (OnActBannerItemCliakListenr != null)
                    OnActBannerItemCliakListenr.actNewYear(banner);
                return null;
            }
        });
//        rv_2.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (OnActBannerItemCliakListenr != null)
//                    OnActBannerItemCliakListenr.integralJump(banner.address);
//            }
//        });
        ViewExtKt.setCustomClick(rv_2, new Function1<View, Unit>() {
            @Override
            public Unit invoke(View view) {
                if (OnActBannerItemCliakListenr != null)
                    OnActBannerItemCliakListenr.integralJump(banner.address);
                return null;
            }
        });
        return newyearView;
    }

    SvgaUtils svgaUtils;//Todo 圣诞节动画
    private String sdSvgaName = "";

    //圣诞节 + 新年活动  + 女神节
    private View getChristmas(ActBannerBean banner) {
        if(null!=timer){
            timer.cancel();
       //     timer.onFinish();
        }

        View christmasView = inflate(mContext, R.layout.layout_act_christmas, null);
        christmasView.setTag("sdj");
        TextView tv_pm = christmasView.findViewById(R.id.tv_pm);
        ImageView im_gift = christmasView.findViewById(R.id.im_gift);
        TextView tv_sum = christmasView.findViewById(R.id.tv_sum);
        RelativeLayout rl_view = christmasView.findViewById(R.id.rl_view);
        if (banner.sort == -1) {
            tv_pm.setText("未上榜");
        } else {
            tv_pm.setText("No." + banner.sort + "");
        }

        TextView tv_jf = christmasView.findViewById(R.id.tv_jf);
        tv_jf.setText(banner.integral + "");
        SVGAImageView im_sds = christmasView.findViewById(R.id.im_sds);
        if (ispk) {
            im_sds.setVisibility(VISIBLE);

        } else {
            im_sds.setVisibility(GONE);
        }

        svgaUtils = new SvgaUtils(mContext, im_sds, 0);
        svgaUtils.initAnimator();
        svgaUtils.repeat();
        if (banner.isFinish) {
            if (null == im_sds.getTag()) {
                svgaUtils.startAnimator("lv_3");
                im_sds.setTag("lv_3");
            } else if (!im_sds.getTag().toString().equals("lv_3")) {
                svgaUtils.startAnimator("lv_3");
                im_sds.setTag("lv_3");
            }
            sdSvgaName = "lv_3";
        } else {
            switch (banner.castleSort){
                case 0:
                    sdSvgaName = "lv_" + banner.castleSort;
                    break;
                case 1:

                    sdSvgaName = "lv_" + banner.castleSort;
                    break;
                case 2:

                    sdSvgaName = "lv_" + banner.castleSort;
                    break;
                case 3:
                    sdSvgaName = "lv_" + banner.castleSort;
                    break;
            }
            svgaUtils.startAnimator(sdSvgaName);
            im_sds.setTag(sdSvgaName);
        }

        if (banner.planeOpenSecond > 0) {//如果时间大于0  就显示红包倒计时
            im_gift.setVisibility(GONE);

             timer = new CountDownTimer((banner.planeOpenSecond + 1)*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    tv_sum.setText(TimeUtils.fromSecond((int) (millisUntilFinished / 1000)));

                }

                @Override
                public void onFinish() {
                    if (OnActBannerItemCliakListenr != null && ActBannerView.this.castleSort != banner.castleSort) {
                        OnActBannerItemCliakListenr.actSDJRedBoxRain();
                        ActBannerView.this.castleSort = banner.castleSort;
                    }

                }
            }.start();

//
//            activityCb = new CountBackUtilsNew();
//            activityCb.countBack(banner.planeOpenSecond + 1, new CountBackUtilsNew.Callback() {
//                @Override
//                public void countBacking(long time) {
//                    tv_sum.setText(TimeUtils.fromSecond((int) (time)));
//                }
//
//                @Override
//                public void finish() {
//
//
//
//                }
//            });

        } else {
            im_gift.setVisibility(VISIBLE);
            if (null != banner.refreshProcesses) {
                for (int i = 0; i < banner.refreshProcesses.size(); i++) {
                    RefreshProcessesBean refreshProcessesBean = banner.refreshProcesses.get(i);

                    if (banner.castleSort == i) {
                        tv_sum.setText(refreshProcessesBean.getCustomsValue() + "/" + refreshProcessesBean.getCustomsTotalValue());
                        ImageLoader.loadImg(im_gift, refreshProcessesBean.getIcon());
                    }
                }
            }

        }
//        christmasView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (OnActBannerItemCliakListenr != null) OnActBannerItemCliakListenr.actSDJ(banner);
//            }
//        });
        ViewExtKt.setCustomClick(christmasView, new Function1<View, Unit>() {
            @Override
            public Unit invoke(View view) {
                if (OnActBannerItemCliakListenr != null) OnActBannerItemCliakListenr.actSDJ(banner);
                return null;
            }
        });
//        rl_view.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (OnActBannerItemCliakListenr != null)
//                    OnActBannerItemCliakListenr.integralJump(banner.address);
//            }
//        });
        ViewExtKt.setCustomClick(rl_view, new Function1<View, Unit>() {
            @Override
            public Unit invoke(View view) {
                if (OnActBannerItemCliakListenr != null)
                    OnActBannerItemCliakListenr.integralJump(banner.address);
                return null;
            }
        });
        return christmasView;
    }


    //签到view
    private View getSignView(ActBannerBean banner) {
//        if (signView == null) {
        signView = inflate(mContext, R.layout.layout_act_sign, null);
//        }
        TextView tvSignDay = signView.findViewById(R.id.tv_sign_day);
        ImageView ivSign = signView.findViewById(R.id.iv_sign_gift);
        ImageView ivSigned = signView.findViewById(R.id.iv_signed);
//        tvSignDay.setText("签到 Day" + banner.signDays);
        ImageLoader.loadImgFitCenter(ivSign, banner.icon);
//        ivSigned.setVisibility(banner.todaySignStatus == 1 ? GONE : VISIBLE);
//        tvSignDay.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (OnActBannerItemCliakListenr != null)
//                    OnActBannerItemCliakListenr.actTips("签到说明" +
//                            "连续7天使用维蜜花打\n" +
//                            "卡，将获得座驾礼包\n" +
//                            "【龙吟冰雪30天】");
//            }
//        });
        ViewExtKt.setCustomClick(tvSignDay, new Function1<View, Unit>() {
            @Override
            public Unit invoke(View view) {
                if (OnActBannerItemCliakListenr != null)
                    OnActBannerItemCliakListenr.actTips("签到说明" +
                            "连续7天使用维蜜花打\n" +
                            "卡，将获得座驾礼包\n" +
                            "【龙吟冰雪30天】");
                return null;
            }
        });
        ivSign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (OnActBannerItemCliakListenr != null)
//                OnActBannerItemCliakListenr.signJump(banner.giftId);
            }
        });
        return signView;
    }


    //心愿view
    private View getWishView(ActBannerBean banner) {
        wishView = inflate(mContext, R.layout.layout_act_wish, null);
        TextView tvAddWish = wishView.findViewById(R.id.tv_add_wish);
        tvAddWish.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        TextView tvWishSpeed = wishView.findViewById(R.id.tv_wish_speed);
        ImageView ivWish = wishView.findViewById(R.id.iv_wish_gift);
        if (banner.wishStatus == 1) {//已许愿
            tvAddWish.setVisibility(GONE);
            tvWishSpeed.setVisibility(VISIBLE);
            ivWish.setVisibility(VISIBLE);
        } else {
            tvAddWish.setVisibility(VISIBLE);
            tvWishSpeed.setVisibility(GONE);
            ivWish.setVisibility(GONE);
        }
        tvWishSpeed.setText(banner.factSpeed + "/" + banner.wishSpeed);
        ImageLoader.loadImgFitCenter(ivWish, banner.icon);
//        wishView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (OnActBannerItemCliakListenr != null)
//                    OnActBannerItemCliakListenr.wishJump(banner.wishStatus);
//            }
//        });
        ViewExtKt.setCustomClick(wishView, new Function1<View, Unit>() {
            @Override
            public Unit invoke(View view) {
                if (OnActBannerItemCliakListenr != null)
                    OnActBannerItemCliakListenr.wishJump(banner.wishStatus);
                return null;
            }
        });
        return wishView;
    }

    //积分view
    private View getIntegralView(ActBannerBean banner) {
//        if (integralView == null) {
        integralView = inflate(mContext, R.layout.layout_act_integral, null);
//        }
        TextView tvIntegral = integralView.findViewById(R.id.tv_integral);
        TextView tvIntegralRank = integralView.findViewById(R.id.tv_integral_rank);
//        tvIntegral.setText("积分：" + banner.integral);
//        tvIntegralRank.setText(banner.sort > 50 ? "排名：50+" : "排名：" + banner.sort);
        integralView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (OnActBannerItemCliakListenr != null)
//                OnActBannerItemCliakListenr.integralJump(banner.address);
            }
        });
        return integralView;
    }

    private void setCurrentDot(int positon, LinearLayout mPointViewGroup) {
        // 界面实际显示的序号是第1, 2, 3。而点的序号应该是0, 1, 2.所以减1.
        positon = positon - 1;
        if (positon < 0 || positon > viewLength - 1 || mCurrentIndex == positon || mPointViewGroup.getChildCount() <= mCurrentIndex) {
            return;
        }
        mPointViewGroup.getChildAt(positon).setEnabled(false);
        mPointViewGroup.getChildAt(mCurrentIndex).setEnabled(true);
        mCurrentIndex = positon;
    }

    private class MainAdPageradapter extends PagerAdapter {

        private List<View> mViewList;

        public List<View> getViewList() {
            return mViewList;
        }

        public MainAdPageradapter(List<View> pArrayList) {
            this.mViewList = pArrayList;
        }

        @Override
        public int getCount() {
            if (mViewList != null) {
                return mViewList.size();
            }
            return 0;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            KLog.e("tag", "destroyItem=" + position);
//            if(null!=container.getTag()&&container.getTag().equals("wsj")){
//              //  KLog.e("tag","pObject="+pObject);
//                SVGAImageView im_av = container.findViewById(R.id.im_sds);
//                im_av.stopAnimation();
//            }
            container.removeView(mViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int pPosition) {
            /*if (mViewList.get(pPosition).getParent() != null) {
                container.removeView(mViewList.get(pPosition));
            }*/
            //   KLog.e("tag","position="+pPosition);
            //IllegalArgumentException: Cannot add a null child view to a ViewGroup
            if(null!=mViewList.get(pPosition)){
                container.addView(mViewList.get(pPosition));
            }

//            if(null!=mViewList.get(pPosition).getTag()&&mViewList.get(pPosition).getTag().equals("sdj")){
//                SVGAImageView   im_sds =    mViewList.get(pPosition).findViewById(R.id.im_sds);
//                SvgaUtils svgaUtils = new SvgaUtils(mContext, im_sds);
//                svgaUtils.initAnimator();
//                svgaUtils.repeat();
//                svgaUtils.startAnimator(sdSvgaName);
//            }

           /* if(mBannerList.get(pPosition%mBannerList.size()).type==8){

            }*/
            return mViewList.get(pPosition);
        }


        @Override
        public boolean isViewFromObject(View pView, Object pObject) {
//            if(null!=pView.getTag()&&pView.getTag().equals("wsj")){
//                KLog.e("tag","pObject="+pObject);
//                SVGAImageView   im_sds =    pView.findViewById(R.id.im_sds);
//                 svgaUtils = new SvgaUtils(mContext, im_sds,0);
//                svgaUtils.initAnimator();
//            //    svgaUtils.repeat();
//                svgaUtils.startAnimator(sdSvgaName);
//            }
            return pView == pObject;
        }


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //父层ViewGroup不要拦截点击事件
        getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    }

    public void onDestroy() {
        castleSort = -1;
        if (planeCountDown != null) {
            planeCountDown.cancel();
            planeCountDown = null;
        }
        if (boxCountDown != null) {
            boxCountDown.cancel();
            boxCountDown = null;
        }
        if (panishView != null) {
            panishView = null;
        }
        if (boxView != null) {
            boxView = null;
        }
        if(timer!=null){
            timer.cancel();
    //        timer.onFinish();
        }
        if (null != activityCb) {
            activityCb.destory();
            activityCb = null;

        }
    }

    //圣诞节
    private View getChristmasUp(ActBannerBean banner, View christmasView) {
        if(null!=timer){
            timer.cancel();
           // timer.onFinish();
        }

        TextView tv_pm = christmasView.findViewById(R.id.tv_pm);
        ImageView im_gift = christmasView.findViewById(R.id.im_gift);
        TextView tv_sum = christmasView.findViewById(R.id.tv_sum);
        RelativeLayout rl_view = christmasView.findViewById(R.id.rl_view);
        if (banner.sort == -1) {
            tv_pm.setText("未上榜");
        } else {
            tv_pm.setText("No." + banner.sort + "");
        }

        TextView tv_jf = christmasView.findViewById(R.id.tv_jf);
        tv_jf.setText(banner.integral + "");
        SVGAImageView im_sds = christmasView.findViewById(R.id.im_sds);
        if (ispk) {
            im_sds.setVisibility(VISIBLE);

        } else {
            im_sds.setVisibility(GONE);
        }
        im_sds.stopAnimation();

        svgaUtils = new SvgaUtils(mContext, im_sds, 0);
        svgaUtils.initAnimator();
        svgaUtils.repeat();
        if (banner.isFinish) {

            svgaUtils.startAnimator("lv_3");
            im_sds.setTag("lv_3");

            sdSvgaName = "lv_3";
        } else {
            switch (banner.castleSort){
                case 0:
                    sdSvgaName = "lv_" + banner.castleSort;
                    break;
                case 1:

                    sdSvgaName = "lv_" + banner.castleSort;
                    break;
                case 2:

                    sdSvgaName = "lv_" + banner.castleSort;
                    break;
                case 3:
                    sdSvgaName = "lv_" + banner.castleSort;
                    break;
            }
            svgaUtils.startAnimator(sdSvgaName);
            im_sds.setTag(sdSvgaName);
        }


        if (banner.planeOpenSecond > 0) {//如果时间大于0  就显示红包倒计时
            im_gift.setVisibility(GONE);


            timer = new CountDownTimer((banner.planeOpenSecond + 1)*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    tv_sum.setText(TimeUtils.fromSecond((int) (millisUntilFinished / 1000)));

                }

                @Override
                public void onFinish() {
                    if (OnActBannerItemCliakListenr != null && ActBannerView.this.castleSort != banner.castleSort) {
                        OnActBannerItemCliakListenr.actSDJRedBoxRain();
                        ActBannerView.this.castleSort = banner.castleSort;
                    }

                }
            }.start();


//            activityCb = new CountBackUtilsNew();
//            activityCb.countBack(banner.planeOpenSecond + 1, new CountBackUtilsNew.Callback() {
//                @Override
//                public void countBacking(long time) {
//                    tv_sum.setText(TimeUtils.fromSecond((int) (time)));
//                }
//
//                @Override
//                public void finish() {
//
//                    if (OnActBannerItemCliakListenr != null && ActBannerView.this.castleSort != banner.castleSort) {
//                        OnActBannerItemCliakListenr.actSDJRedBoxRain();
//                        ActBannerView.this.castleSort = banner.castleSort;
//                    }
//
//                }
//            });

        } else {
            im_gift.setVisibility(VISIBLE);
            if (null != banner.refreshProcesses) {
                for (int i = 0; i < banner.refreshProcesses.size(); i++) {
                    RefreshProcessesBean refreshProcessesBean = banner.refreshProcesses.get(i);

                    if (banner.castleSort == i) {
                        tv_sum.setText(refreshProcessesBean.getCustomsValue() + "/" + refreshProcessesBean.getCustomsTotalValue());
                        ImageLoader.loadImg(im_gift, refreshProcessesBean.getIcon());
                    }
                }
            }

        }
//        christmasView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (OnActBannerItemCliakListenr != null) OnActBannerItemCliakListenr.actSDJ(banner);
//            }
//        });
        ViewExtKt.setCustomClick(christmasView, new Function1<View, Unit>() {
            @Override
            public Unit invoke(View view) {
                if (OnActBannerItemCliakListenr != null) OnActBannerItemCliakListenr.actSDJ(banner);
                return null;
            }
        });
//        rl_view.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (OnActBannerItemCliakListenr != null)
//                    OnActBannerItemCliakListenr.integralJump(banner.address);
//            }
//        });
        ViewExtKt.setCustomClick(rl_view, new Function1<View, Unit>() {
            @Override
            public Unit invoke(View view) {
                if (OnActBannerItemCliakListenr != null)
                    OnActBannerItemCliakListenr.integralJump(banner.address);
                return null;
            }
        });
        return christmasView;
    }

}
