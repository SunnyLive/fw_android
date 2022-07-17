package com.fengwo.module_comment.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fengwo.module_comment.Interfaces.ICardType;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.SuperPlayerGlobalConfig;
import com.fengwo.module_comment.widget.scaleview.ScaleView;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author min
 * @data 2017/4/26
 * @Description 首页banner 实现无限轮播 BannerBean需实现 Ibanner 接口
 */
public class DetailCardPopView<T extends ICardType> extends RelativeLayout implements ITXVodPlayListener, ViewPager.OnPageChangeListener {
    private Context mContext;
    private View mView;
    private DisposeViewPager vp;
    private LinearLayout pointLl;
    private List<View> views;

    private int viewLength;

    private boolean mIsChanged = false;
    private static final long AD_CHANGE_TIME = 4000;
    public int mCurrentPagePosition = FIRST_ITEM_INDEX;
    public int mCurrentIndex;
    private static final int FIRST_ITEM_INDEX = 0;
    public boolean isRunning;
    private List<T> mBannerList;
    private TXVodPlayer mVodPlayer;
    private TXVodPlayConfig mVodPlayConfig;

    public DetailCardPopView(Context context) {
        this(context, null);
    }

    public DetailCardPopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailCardPopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.layout_detail_card_view_pop, this, true);
        //文件列表 viewpager
        vp = mView.findViewById(R.id.main_viewpager);
        //指示器
        pointLl = mView.findViewById(R.id.main_bannerview_pointgroup);
        initPlay();
    }

    /**
     * 初始化播放器
     */
    public void initPlay() {
        if (mVodPlayer != null) {
            return;
        }
        mVodPlayer = new TXVodPlayer(mContext);
        SuperPlayerGlobalConfig config = SuperPlayerGlobalConfig.getInstance();
        mVodPlayConfig = new TXVodPlayConfig();
        mVodPlayConfig.setCacheFolderPath(Environment.getExternalStorageDirectory().getPath() + "/.nomedia");
        mVodPlayConfig.setMaxCacheItems(config.maxCacheItem);
        mVodPlayer.setConfig(mVodPlayConfig);
        mVodPlayer.setLoop(true);
        mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mVodPlayer.setVodListener(this);
        mVodPlayer.enableHardwareDecode(config.enableHWAcceleration);
    }

    /**
     * 设置数据
     */
    public void setBanner(ArrayList<T> bannerList, int position) {
        if (bannerList == null || bannerList.size() == 0) return;
        mCurrentPagePosition = position;
        mBannerList = bannerList;
        pointLl.removeAllViews();
        views = new ArrayList<>();
//        //创建背景View
//        if (bannerList.size() > 1) {
//            CityHost.ResBean bean = bannerList.get(bannerList.size() - 1);//取列表最后一个值
//            if (bean.getRPath().endsWith("mp4")) {
//                views.add(addTXCloudVideoView(bean));
//            } else {
//                views.add(addImageView(bean));
//            }
//        }
        // 创建banner 点
        for (int i = 0; i < bannerList.size(); i++) {
            ImageView point = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = DensityUtils.dp2px(mContext, 20);
            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.selector_bannerview_point_bg);
            if (mCurrentPagePosition == i) {
                point.setEnabled(false);
            } else {
                point.setEnabled(true);
            }
            pointLl.addView(point);
            ICardType bean = bannerList.get(i);
            if (bean.getSourceType() == 2) {
                views.add(addTXCloudVideoView(bean));
            } else {
                views.add(addImageView(bean));
            }
        }
//        // 取第一个值
//        if (bannerList.size() > 1) {
//            CityHost.ResBean bean = bannerList.get(0);
//            if (bean.getRPath().endsWith("mp4")) {
//                views.add(addTXCloudVideoView(bean));
//            } else {
//                views.add(addImageView(bean));
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
        vp.addOnPageChangeListener(this);

        vp.setCurrentItem(mCurrentPagePosition, false);
        if (mCurrentPagePosition == 1 || mCurrentPagePosition == 0)
            refreshView();

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
        if (mBannerList != null && mBannerList.size() > mCurrentIndex && mBannerList.get(mCurrentIndex).getSourceType() == 1)
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

    //刷新view
    public void refreshView() {
        if (mBannerList == null || mBannerList.size() == 0 || mCurrentIndex > mBannerList.size() - 1)
            return;
        //如果是视频 就播放
        if (mBannerList.get(mCurrentIndex).getSourceType() == 2 && views.get(mCurrentPagePosition) instanceof FrameLayout) {
            startPlay(mBannerList.get(mCurrentIndex).getUrl(), (TXCloudVideoView) ((ViewGroup) views.get(mCurrentPagePosition)).getChildAt(0));
            //隐藏播放按钮
            ((FrameLayout) views.get(mCurrentPagePosition)).getChildAt(2).setVisibility(GONE);
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
        if (vp != null)
            vp.removeOnPageChangeListener(this);
        pointLl.removeAllViews();
    }

    /**
     * 添加图片
     */
    private View addImageView(final ICardType banner) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_big_scale_view, null);
        final ScaleView imageView = view.findViewById(R.id.sv_image);
        imageView.setOnClickListener(v -> {
            if (clickPlayListener != null) {
                clickPlayListener.onClick(-1,0);
            }
        });

        //如果有海报  就显示海报 没有海报就显示url
        String imageUrl = TextUtils.isEmpty(banner.getPoster()) ? banner.getUrl() : banner.getPoster();
        ImageLoader.loadRouteImg(imageView, imageUrl);
        return view;
    }

    /**
     * 添加视频View
     */
    @SuppressLint("CheckResult")
    private View addTXCloudVideoView(final ICardType banner) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_video_view, null);
        ImageView ivCover = view.findViewById(R.id.iv_fengmian);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVodPlayer.isPlaying()) {
                    mVodPlayer.pause();
                    ((FrameLayout) views.get(mCurrentPagePosition)).getChildAt(2).setVisibility(VISIBLE);//播放按钮
                    if (clickPlayListener != null) {
                        clickPlayListener.onClick(mVodPlayer.getPlayableDuration(), -1);
                    }
                } else {
                    mVodPlayer.resume();
                    ((FrameLayout) views.get(mCurrentPagePosition)).getChildAt(2).setVisibility(GONE);//播放按钮
                }
            }
        });

        //设置第一帧

        //设置封面
        if (!TextUtils.isEmpty(banner.getPoster())) {
            ImageLoader.loadImg(ivCover, banner.getPoster());
            return view;
        }
        Flowable.create((FlowableOnSubscribe<Bitmap>) emitter -> {
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(banner.getUrl(), new HashMap<>());
                    Bitmap bitmap = mmr.getFrameAtTime();
                    emitter.onNext(bitmap);
                },
                BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        ivCover.setImageBitmap(bitmap);
                    }
                });
        return view;
    }

    /**
     * 显示 指示器的点
     */
    private void setCurrentDot(int positon, LinearLayout mPointViewGroup) {
        // 界面实际显示的序号是第1, 2, 3。而点的序号应该是0, 1, 2.所以减1.
//        positon = positon - 1;
        if (positon < 0 || positon > viewLength - 1 || mCurrentIndex == positon) {
            return;
        }
        try {
            mPointViewGroup.getChildAt(positon).setEnabled(false);
            mPointViewGroup.getChildAt(mCurrentIndex).setEnabled(true);
            mCurrentIndex = positon;
        } catch (Exception e) {
            L.e("指示器的点 ===========");
        }
    }

    @Override
    public void onPlayEvent(TXVodPlayer txVodPlayer, int event, Bundle bundle) {
        try {
            if (event == TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED) { //视频播放开始
            } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
                if (views.get(mCurrentPagePosition) instanceof FrameLayout) {
                    ((FrameLayout) views.get(mCurrentPagePosition)).getChildAt(1).setVisibility(GONE);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int pPosition) {
        System.out.println("-=--=--onPageSelected");
        mIsChanged = true;
//        if (pPosition > viewLength - 2) {
//            mCurrentPagePosition = FIRST_ITEM_INDEX;
//        } else if (pPosition < FIRST_ITEM_INDEX) {
//            mCurrentPagePosition = viewLength - 2;
//        } else {
        mCurrentPagePosition = pPosition;
//        }
        setCurrentDot(mCurrentPagePosition, pointLl);
        if (mBannerList.size() > mCurrentIndex && mBannerList.get(mCurrentIndex).getSourceType() == 2) {
            stopPlay();
        } else if (mVodPlayer != null && mVodPlayer.isPlaying()) {
//                    mVodPlayer.setVodListener(null);
            mVodPlayer.stopPlay(false);
        }
        refreshView();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        if (ViewPager.SCROLL_STATE_IDLE == state) {
//            if (mIsChanged) {
//                mIsChanged = false;
//                vp.setCurrentItem(mCurrentPagePosition, false);
//            }
//        }
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
        void onClick(float videoTime, int current);
    }

    public ClickPlayListener clickPlayListener;

    public void addClickPlayListener(ClickPlayListener clickPlayListener) {
        this.clickPlayListener = clickPlayListener;
    }
}
