package com.fengwo.module_flirt.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.SuperPlayerGlobalConfig;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.UI.activity.DetailCardActivity;
import com.fengwo.module_flirt.bean.CityHost;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author min
 * @data 2017/4/26
 * @Description 首页banner 实现无限轮播 BannerBean需实现 Ibanner 接口
 */
public class DetailCardView extends RelativeLayout implements ITXVodPlayListener {
    private Context mContext;
    private View mView;
    private ViewPager vp;
    private LinearLayout pointLl;
    private List<View> views;

    private int viewLength;

    private boolean mIsChanged = false;
    private static final long AD_CHANGE_TIME = 4000;
    public int mCurrentPagePosition = FIRST_ITEM_INDEX;
    public int mCurrentIndex;
    private static final int FIRST_ITEM_INDEX = 0;
    public boolean isRunning;
    private List<CityHost.ResBean> mBannerList;
    private TXVodPlayer mVodPlayer;
    private TXVodPlayConfig mVodPlayConfig;

    public DetailCardView(Context context) {
        this(context, null);
    }

    public DetailCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.detail_card_view_layout, this, true);
        vp = mView.findViewById(R.id.main_viewpager);
        pointLl = mView.findViewById(R.id.main_bannerview_pointgroup);
        initPlay();
    }

    /**
     * 初始化播放器
     */
    public void initPlay() {
        if (mVodPlayer != null)
            return;
        mVodPlayer = new TXVodPlayer(mContext);
        SuperPlayerGlobalConfig config = SuperPlayerGlobalConfig.getInstance();
        mVodPlayConfig = new TXVodPlayConfig();
        mVodPlayConfig.setCacheFolderPath(Environment.getExternalStorageDirectory().getPath() + "/.nomedia");
        mVodPlayConfig.setMaxCacheItems(config.maxCacheItem);
        mVodPlayer.setConfig(mVodPlayConfig);
        mVodPlayer.setLoop(false);
        mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mVodPlayer.setVodListener(this);
        mVodPlayer.enableHardwareDecode(config.enableHWAcceleration);
    }

    /**
     * 设置数据
     */
    public void setBanner(ArrayList<CityHost.ResBean> bannerList) {
        if (bannerList == null || bannerList.size() == 0) return;
        //初始化reset
        mBannerList = bannerList;
        pointLl.removeAllViews();
        views = new ArrayList<>();
        mCurrentPagePosition = FIRST_ITEM_INDEX;
        mCurrentIndex = 0;
        vp.clearOnPageChangeListeners();
        //创建背景View
//        if (bannerList.size() > 1) {
//            CityHost.ResBean bean = bannerList.get(bannerList.size() - 1);//取列表最后一个值
//            if (bean.getRPath().endsWith("mp4")) {
//                views.add(addTXCloudVideoView(bean, bannerList.size() - 1));
//            } else {
//                views.add(addImageView(bean, bannerList.size() - 1));
//            }
//        }
        // 创建banner 点
        for (int i = 0; i < bannerList.size(); i++) {
            ImageView point = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = DensityUtils.dp2px(mContext, 10);
            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.selector_banner_lin_point_bg);
            if (0 == i) {
                point.setEnabled(false);
            } else {
                point.setEnabled(true);
            }
            pointLl.addView(point);
            CityHost.ResBean bean = bannerList.get(i);
            if (bean.getRPath().endsWith("mp4")) {
                views.add(addTXCloudVideoView(bean, i));
            } else {
                views.add(addImageView(bean, i));
            }
        }
        // 取第一个值
//        if (bannerList.size() > 1) {
//            CityHost.ResBean bean = bannerList.get(0);
//            if (bean.getRPath().endsWith("mp4")) {
//                views.add(addTXCloudVideoView(bean, 0));
//            } else {
//                views.add(addImageView(bean, 0));
//            }
//        }

        viewLength = views.size();
        initVp();
    }

    /**
     * ViewPager
     */
    private void initVp() {
        vp.setAdapter(new MainAdPageradapter(views));
        // 实现轮播效果
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("tag", "onPageScrolled" + position + "***" + positionOffset + "/////" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int pPosition) {
                System.out.println(pPosition + "-=-==----" + mCurrentPagePosition);
                Log.e("tag", "onPageSelected" + pPosition);
                if (null != clickPlayListener) {
                    clickPlayListener.setPsition(pPosition);
                }
                mIsChanged = true;
//                if (pPosition > viewLength - 2) {
//                    mCurrentPagePosition = FIRST_ITEM_INDEX;
//                } else if (pPosition < FIRST_ITEM_INDEX) {
//                    mCurrentPagePosition = viewLength - 2;
//                } else {
                mCurrentPagePosition = pPosition;
//                }

                setCurrentDot(mCurrentPagePosition, pointLl);
                if (mBannerList.size() > mCurrentIndex && mBannerList.get(mCurrentIndex).getRPath().endsWith("mp4")) {
                    stopPlay();
                } else if (mVodPlayer != null && mVodPlayer.isPlaying()) {
//                    mVodPlayer.setVodListener(null);
                    mVodPlayer.stopPlay(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("tag", "onPageScrollStateChanged" + state);
                if (ViewPager.SCROLL_STATE_IDLE == state) {
                    if (mIsChanged) {
                        mIsChanged = false;
//                        vp.setCurrentItem(mCurrentPagePosition, false);
                    }
                }
            }
        });

        vp.setCurrentItem(mCurrentPagePosition, false);
        if (mBannerList.size() > 1) {
            isRunning = true;
            pointLl.setVisibility(View.VISIBLE);
        } else {
            isRunning = false;
            pointLl.setVisibility(View.INVISIBLE);
        }
    }

    //  菜单滚动
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

    /**
     * 播放视频
     */
    public void startPlay(String url, TXCloudVideoView txCloudVideoView) {
        if (mVodPlayer == null) {
            initPlay();
        }
//        txCloudVideoView.updateVideoViewSize(ScreenUtils.getScreenWidth(mContext), ScreenUtils.getScreenHeight(mContext));
        mVodPlayer.setPlayerView(txCloudVideoView);
        mVodPlayer.setAutoPlay(true);
        mVodPlayer.setVodListener(this);
        mVodPlayer.startPlay(url);
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        if (mBannerList != null && mBannerList.size() > mCurrentIndex && !mBannerList.get(mCurrentIndex).getRPath().endsWith("mp4"))
            return;
        if (mVodPlayer != null && views != null) {
            mVodPlayer.setVodListener(null);
            mVodPlayer.stopPlay(false);
            mVodPlayer = null;
            //显示第一帧 与 播放按钮
            if (views.size() > mCurrentPagePosition && views.get(mCurrentPagePosition) instanceof FrameLayout) {
                ((FrameLayout) views.get(mCurrentPagePosition)).getChildAt(1).setVisibility(VISIBLE);//第一帧
                ((FrameLayout) views.get(mCurrentPagePosition)).getChildAt(2).setVisibility(VISIBLE);//播放按钮
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopPlay();
    }

    //刷新view
    public void refreshView() {
        if (mBannerList == null || mBannerList.size() == 0 || mCurrentIndex > mBannerList.size() - 1)
            return;
        if (mBannerList.get(mCurrentIndex).getRPath().endsWith("mp4") && views.get(mCurrentPagePosition) instanceof FrameLayout) {
//            startPlay(mBannerList.get(mCurrentIndex).getRPath(), (TXCloudVideoView) ((ViewGroup) views.get(mCurrentPagePosition)).getChildAt(0));
            //隐藏播放按钮
//            ((FrameLayout) views.get(mCurrentPagePosition)).getChildAt(2).setVisibility(GONE);
        }
    }

    /**
     * 销毁
     */
    public void destroy() {
        if (mVodPlayer != null) {
            mVodPlayer.setVodListener(null);
            mVodPlayer.stopPlay(false);
            mVodPlayer = null;
        }
    }

    /**
     * 添加图片
     */
    private ImageView addImageView(final CityHost.ResBean banner, int position) {
        final ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ImageLoader.loadRouteImg(imageView, banner.getRPath());
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailCardActivity.start(mContext, (ArrayList<CityHost.ResBean>) mBannerList, position);
            }
        });
        return imageView;
    }

    /**
     * 添加视频View
     */
    @SuppressLint("CheckResult")
    private View addTXCloudVideoView(final CityHost.ResBean banner, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_video_view, null);
        TXCloudVideoView videoView = view.findViewById(R.id.card_txvideo_view);
        ImageView imageView = view.findViewById(R.id.iv_play);
        ImageView ivCover = view.findViewById(R.id.iv_fengmian);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickPlayListener != null) clickPlayListener.click();
                if (null == mVodPlayer) {
                    mVodPlayer = new TXVodPlayer(mContext);
                }
                if (!mVodPlayer.isPlaying()) {
                    TXVodPlayConfig txVodPlayConfig = new TXVodPlayConfig();
                    txVodPlayConfig.setMaxCacheItems(15);
                    txVodPlayConfig.setProgressInterval(200);
                    mVodPlayer.setConfig(txVodPlayConfig);
                    mVodPlayer.setAutoPlay(true);
                    mVodPlayer.setLoop(true);
                    mVodPlayer.setVodListener(new ITXVodPlayListener() {
                        @Override
                        public void onPlayEvent(TXVodPlayer txVodPlayer, int event, Bundle bundle) {
                            L.e("tag", event + "");
                            if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
                                imageView.setVisibility(GONE);
                                ivCover.setVisibility(GONE);
                            }
                        }

                        @Override
                        public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

                        }
                    });
                    mVodPlayer.setPlayerView(videoView);
                } else {
                    stopPlay();
                }
                if (mVodPlayer != null) {
                    mVodPlayer.startPlay(banner.getRPath());
                }
//                //播放视频
                //     refreshView();
                //        DetailCardActivity.start(mContext, (ArrayList<CityHost.ResBean>) mBannerList, position);
            }
        });

        ImageLoader.loadVideoFirstFrame(getContext(), banner.getRPath(), ivCover);

        //设置第一帧
//        Flowable.create((FlowableOnSubscribe<Bitmap>) emitter -> {
//                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//                    mmr.setDataSource(banner.getRPath(), new HashMap<>());
//                    Bitmap bitmap = mmr.getFrameAtTime();
//                    emitter.onNext(bitmap);
//                },
//                BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Bitmap>() {
//                    @Override
//                    public void accept(Bitmap bitmap) throws Exception {
//                        ivCover.setImageBitmap(bitmap);
//                    }
//                });
        return view;
    }

    /**
     * 显示 指示器的点
     */
    private void setCurrentDot(int position, LinearLayout mPointViewGroup) {
        // 界面实际显示的序号是第1, 2, 3。而点的序号应该是0, 1, 2.所以减1.
        if (position < 0 || position > viewLength - 1 || mCurrentIndex == position) {
            return;
        }
        try {
            for (int i = 0; i < mBannerList.size(); i++) {
                mPointViewGroup.getChildAt(i).setEnabled(true);
            }
            mPointViewGroup.getChildAt(position).setEnabled(false);
            mCurrentIndex = position;
        } catch (Exception e) {
            L.e("指示器的点 ===========");
        }
    }

    @Override
    public void onPlayEvent(TXVodPlayer txVodPlayer, int event, Bundle bundle) {
        if (event == TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED) { //视频播放开始
        } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
            try {
                ((FrameLayout) views.get(mCurrentPagePosition)).getChildAt(1).setVisibility(GONE);
            } catch (Exception e) {
            }
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            if (views.get(mCurrentPagePosition) instanceof FrameLayout) {
                ((FrameLayout) views.get(mCurrentPagePosition)).getChildAt(1).setVisibility(VISIBLE);
                ((FrameLayout) views.get(mCurrentPagePosition)).getChildAt(2).setVisibility(VISIBLE);
            }
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
        } else if (event == TXLiveConstants.PLAY_ERR_HLS_KEY
//                || event == TXLiveConstants.PLAY_ERR_VOD_LOAD_LICENSE_FAIL
//                || event == TXLiveConstants.PLAY_ERR_VOD_UNSUPPORT_DRM
                || event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {// 播放点播文件失败
//            Toast.makeText(this, bundle.getString(TXLiveConstants.EVT_DESCRIPTION) + ",尝试其他链接播放", Toast.LENGTH_SHORT).show();
        }

        if (event < 0
//                && event != TXLiveConstants.PLAY_ERR_VOD_LOAD_LICENSE_FAIL
                && event != TXLiveConstants.PLAY_ERR_HLS_KEY
//                && event != TXLiveConstants.PLAY_ERR_VOD_UNSUPPORT_DRM
                && event != TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
            mVodPlayer.stopPlay(true);
        }
    }

    @Override
    public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

    }

    private class MainAdPageradapter extends PagerAdapter {

        private List<View> mViewList;

        public MainAdPageradapter(List<View> pArrayList) {
            this.mViewList = pArrayList;
        }

        @Override
        public int getCount() {
            return mViewList != null ? mViewList.size() : 0;
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


    public void stopAutoScroll() {
        isRunning = false;
        mHandler.removeMessages(1);
    }

    public void startAutoScroll() {
        if (isRunning) return;
        if (mBannerList != null && mBannerList.size() > 1) {
            isRunning = true;
            mHandler.sendEmptyMessageDelayed(1, AD_CHANGE_TIME);
        }
    }

    public void setPoitGravity(int gravity) {
        if (pointLl == null) return;
        pointLl.setGravity(gravity);
    }

    public interface ClickPlayListener {
        void click();

        void setPsition(int psition);
    }

    public ClickPlayListener clickPlayListener;

    public void addClickPlayListener(ClickPlayListener clickPlayListener) {
        this.clickPlayListener = clickPlayListener;
    }
}
