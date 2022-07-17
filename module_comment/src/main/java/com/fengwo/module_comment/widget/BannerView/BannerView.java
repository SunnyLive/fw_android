package com.fengwo.module_comment.widget.BannerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.base.BaseActivity;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.IntentRoomActivityUrils;
import com.fengwo.module_comment.widget.floatingview.FloatingView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author min
 * @data 2017/4/26
 * @Description 首页banner 实现无限轮播 BannerBean需实现 Ibanner 接口
 */
public class BannerView<T> extends RelativeLayout {

    private final static String LIVE = "live";
    private final static String MOVIE = "movie";

    private Context mContext;
    private View mView;
    private ViewPager vp;
    private LinearLayout pointLl;

    private int viewLength;
    private boolean mIsChanged = false;
    private static final long AD_CHANGE_TIME = 4000;
    private int mCurrentPagePosition = FIRST_ITEM_INDEX;
    private int mCurrentIndex;
    private static final int FIRST_ITEM_INDEX = 1;
    public boolean isRunning;
    private List<IBanner> mBannerList;
    @Autowired
    UserProviderService userProviderService;

    private int default_img = R.drawable.live_home_default;

    public void setDefaultImg(int img) {
        default_img = img;
    }

    OnBannerItemCliakListenr onBannerItemCliakListenr;

    public void setOnBannerItemCliakListenr(OnBannerItemCliakListenr l) {
        onBannerItemCliakListenr = l;
    }

    //   菜单滚动
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

    public void stopAutoScroll() {
        isRunning = false;
        mHandler.removeMessages(1);
    }

    public void startAutoScroll() {
        stopAutoScroll();
        if (isRunning) return;
        if (mBannerList != null && mBannerList.size() > 1) {
            isRunning = true;
            mHandler.sendEmptyMessageDelayed(1, AD_CHANGE_TIME);
        }
    }


    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ArouteUtils.inject(this);
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.banner_home_layout, this, true);
        vp = (ViewPager) mView.findViewById(R.id.main_viewpager);
        pointLl = (LinearLayout) mView.findViewById(R.id.main_bannerview_pointgroup);
    }

    public void setPoitGravity(int gravity) {
        if (pointLl == null) return;
        pointLl.setGravity(gravity);
    }

    public void setBanner(ArrayList<IBanner> bannerList) {
        mBannerList = bannerList;
        pointLl.removeAllViews();
        List<View> views = new ArrayList<View>();
        if (bannerList == null || bannerList.size() == 0) {
            return;
        }
        if (bannerList.size() > 1) {
            views.add(addImageView(bannerList.get(bannerList.size() - 1)));
        }
        for (int i = 0; i < bannerList.size(); i++) {
            ImageView point = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = DensityUtils.dp2px(mContext, 5);
            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.selector_bannerview_point_bg);
            if (0 == i) {
                point.setEnabled(false);
            }
            pointLl.addView(point);
            views.add(addImageView(bannerList.get(i)));
        }
        if (bannerList.size() > 1)
            views.add(addImageView(bannerList.get(0)));

        viewLength = views.size();
        vp.setAdapter(new MainAdPageradapter(views));
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
                setCurrentDot(mCurrentPagePosition, pointLl);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

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
        });

        vp.setCurrentItem(mCurrentPagePosition, false);
        stopAutoScroll();
        if (mBannerList.size() > 1) {
            isRunning = true;
            mHandler.sendEmptyMessageDelayed(1, AD_CHANGE_TIME);
            pointLl.setVisibility(View.VISIBLE);
        } else {
            isRunning = false;
            mHandler.removeMessages(1);
            pointLl.setVisibility(View.INVISIBLE);
        }

    }

    private ImageView addImageView(final IBanner banner) {
        final ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ImageLoader.loadImg(imageView, banner.getImgUrl());
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = banner.getJumpUrl();
                boolean isH5 = url.startsWith("http");
                if (isH5) {
                    if (banner.isOut()) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(banner.getJumpUrl() + "?token=" + userProviderService.getToken());
                        intent.setData(content_url);
                        mContext.startActivity(intent);
                    } else {
                        BrowserActivity.start(mContext, banner.getTitle(), banner.getJumpUrl());
                    }
                } else {

                    try {
                        String action = url.split("//")[1].split("\\?")[0];
                        String id = url.split("//")[1].split("\\?")[1].split("=")[1];
                        if (action.equals(Constants.BANNER_JUMP_TYPE_LIVE)) {
                            ArrayList<ZhuboDto> list = new ArrayList<>();
                            ZhuboDto zhuboDto = new ZhuboDto();
                            zhuboDto.channelId = Integer.parseInt(id);
                            list.add(zhuboDto);
                            if (FloatingView.getInstance().isShow()) {
                                showExitDialog(list);
                            } else {
                                IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);
                              //  ArouteUtils.toLive(list);
                            }
                        } else if (action.equals(Constants.BANNER_JUMP_TYPE_ALBUM)) {
                            ArouteUtils.toShortVideoActivity(Integer.parseInt(id));
                        }
                        if (null != onBannerItemCliakListenr) {
                            onBannerItemCliakListenr.jump(action, id,mCurrentIndex);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (null != onBannerItemCliakListenr) {
                            onBannerItemCliakListenr.jump("", "",mCurrentIndex);
                        }
                    }
                }
//                if (null != onBannerItemCliakListenr) {
//                    onBannerItemCliakListenr.onClick(banner.getTitle(), banner.getJumpUrl());
//                }
            }
        });
        return imageView;
    }
    /**
     * 关闭悬浮窗弹框提示
     */
    public void showExitDialog(ArrayList<ZhuboDto> list) {
        FloatingView floatingView = FloatingView.getInstance();
        ExitDialog dialog = new ExitDialog();
        dialog.setNegativeButtonText("取消")
                .setPositiveButtonText("确定退出")
                .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0);
                //        ArouteUtils.toLive(list);
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .setGear(floatingView.getGear())
                .setNickname(floatingView.getNickname())
                .setExpireTime(floatingView.getExpireTime())
                .setHeadImg(floatingView.getHeadImg())
                .setRoomId(floatingView.getRoomId())
                .setTip("退出达人房间，印象值将归零\n是否要退出")
                .show(((BaseActivity) getContext()).getSupportFragmentManager(), "");
    }
    private void setCurrentDot(int positon, LinearLayout mPointViewGroup) {
        // 界面实际显示的序号是第1, 2, 3。而点的序号应该是0, 1, 2.所以减1.
        positon = positon - 1;
        if (positon < 0 || positon > viewLength - 1 || mCurrentIndex == positon) {
            return;
        }
        if(null!=mPointViewGroup&&null!=mPointViewGroup.getChildAt(positon)){
            mPointViewGroup.getChildAt(positon).setEnabled(false);
            mPointViewGroup.getChildAt(mCurrentIndex).setEnabled(true);
            mCurrentIndex = positon;
        }

    }

    private class MainAdPageradapter extends PagerAdapter {

        private List<View> mViewList;

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
            container.removeView(mViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int pPosition) {
            container.addView(mViewList.get(pPosition), 0);
            return mViewList.get(pPosition);
        }

        @Override
        public boolean isViewFromObject(View pView, Object pObject) {
            return (pView == pObject);
        }
    }
}
