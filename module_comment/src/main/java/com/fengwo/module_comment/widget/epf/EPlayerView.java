package com.fengwo.module_comment.widget.epf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.fengwo.module_comment.R;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.widget.epf.chooser.EConfigChooser;
import com.fengwo.module_comment.widget.epf.contextfactory.EContextFactory;
import com.fengwo.module_comment.widget.epf.filter.AlphaFrameFilter;
import com.fengwo.module_comment.widget.epf.filter.GlFilter;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.video.VideoListener;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.math.BigDecimal;

/**
 * Created by sudamasayuki on 2017/05/16.
 */
public class EPlayerView extends GLSurfaceView implements VideoListener {

    private final static String TAG = EPlayerView.class.getSimpleName();

    private final EPlayerRenderer renderer;
    private SimpleExoPlayer player;

    private float videoAspect = 1f;
    private PlayerScaleType playerScaleType = PlayerScaleType.RESIZE_FIT_CENTER;

    private SurfaceHolder holder;
    int mScreenHeight;
    int mScreenWidth;
    String type = "";//1是主播
    public EPlayerView(Context context) {
        this(context, null);
    }

    public EPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EPlayerView);
         type = ta.getString(R.styleable.EPlayerView_atb_type);
        mScreenHeight = ScreenUtils.getScreenHeight(context);
        mScreenWidth = ScreenUtils.getScreenWidth(context);
        setEGLContextFactory(new EContextFactory());
        setEGLConfigChooser(new EConfigChooser());

        setZOrderOnTop(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        holder = getHolder();
        holder.setFormat(PixelFormat.RGBA_8888);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });

        renderer = new EPlayerRenderer(this);
        setRenderer(renderer);
    }

    @SuppressLint("WrongConstant")
    public EPlayerView setSimpleExoPlayer(SimpleExoPlayer player) {
        if (this.player != null) {
            this.player.release();
            this.player = null;
        }
        this.player = player;
        this.player.addVideoListener(this);
        this.player.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        this.renderer.setSimpleExoPlayer(player);
        return this;
    }

    private GlFilter filter = null;

    public void setGlFilter(GlFilter glFilter) {
        renderer.setGlFilter(glFilter);
        if (glFilter != null) {
            if (glFilter instanceof AlphaFrameFilter) {
                videoAspect = videoAspect * 2;
                requestLayout();
            } else {
                if (filter != null) {
                    if (filter instanceof AlphaFrameFilter) {
                        videoAspect = videoAspect / 2;
                        requestLayout();
                    }
                }
            }
        }
        filter = glFilter;
    }

    public void setPlayerScaleType(PlayerScaleType playerScaleType) {
        this.playerScaleType = playerScaleType;
        requestLayout();

    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        int measuredWidth = getMeasuredWidth();
//        int measuredHeight = getMeasuredHeight();
//
//        int viewWidth = measuredWidth;
//        int viewHeight = measuredHeight;
//
//        switch (playerScaleType) {
//            case RESIZE_FIT_WIDTH:
//                viewHeight = measuredWidth;
//                break;
//            case RESIZE_FIT_HEIGHT:
//                viewWidth = (int) (measuredHeight * videoAspect);
//                break;
//            case RESIZE_FIT_CENTER:
//                viewHeight = measuredHeight;
//                viewWidth = measuredWidth;
//                break;
//        }
//
//         KLog.d(TAG, "onMeasure viewWidth = " + viewWidth + " viewHeight = " + viewHeight);
//
//        setMeasuredDimension(viewWidth, viewHeight);
//
//    }

    @Override
    public void onPause() {
        super.onPause();
        renderer.release();
    }

    //////////////////////////////////////////////////////////////////////////
    // SimpleExoPlayer.VideoListener

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        try {
            resizeVideo(width, height);
        } catch (ClassCastException e) {

        }

        //    changeVideoSize(width,height);

        KLog.e("TAG", "width = " + width + " height = " + height + " unappliedRotationDegrees = " + unappliedRotationDegrees + " pixelWidthHeightRatio = " + pixelWidthHeightRatio);
//       videoAspect = ((float) width / height) * pixelWidthHeightRatio;
////        KLog.d(TAG, "videoAspect = " + videoAspect);
////        requestLayout();
//
//        //setMeasuredDimension(5000, height);
//
//        int viewWidth = width;
//        int viewHeight = height;
//                switch (playerScaleType) {
//            case RESIZE_FIT_WIDTH:
//                viewHeight = height;
//                break;
//            case RESIZE_FIT_HEIGHT:
//                viewWidth = (int) (height * videoAspect);
//                break;
//            case RESIZE_FIT_CENTER:
//                viewHeight = height;
//                viewWidth = width;
//                break;
//        }
//        android.widget.FrameLayout.LayoutParams PARAMS =new android.widget.FrameLayout.LayoutParams(viewWidth,viewHeight);
//        this.setLayoutParams(PARAMS);
//
////
////         KLog.d(TAG, "onMeasure viewWidth = " + viewWidth + " viewHeight = " + viewHeight);
////
////        setMeasuredDimension(viewWidth, viewHeight);
    }


    public void resizeVideo(int w, int h) throws ClassCastException {//放大后给视频加背景
        if (w == 0 || h == 0) {
            //      ToastUtil.show("视频无法放大");
            return;
        }
        BigDecimal mVideoWidth = new BigDecimal(w + "").divide(new BigDecimal("2")).setScale(0,BigDecimal.ROUND_DOWN);
        BigDecimal mVideoHeight = new BigDecimal(h + "");
        KLog.e("tag", "屏幕的宽高" + mScreenWidth + "/" + mScreenHeight + "视频的宽高" + w + "/" + h);
        boolean istransverse;
        if (mVideoWidth.compareTo(mVideoHeight) == 1) {
            istransverse = true;
        } else {
            istransverse = false;
        }
        ViewGroup.LayoutParams params =  this.getLayoutParams();
        //  FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.getLayoutParams();
        if(null!=type&&type.equals("1")){
            if (mVideoWidth.compareTo(new BigDecimal(mScreenWidth + "")) == 1){
                BigDecimal a = mVideoWidth.subtract(new BigDecimal(mScreenWidth + ""));
                BigDecimal b = a.divide(new BigDecimal(mVideoWidth + ""), 2, BigDecimal.ROUND_UP);
                BigDecimal c = b.multiply(new BigDecimal(mVideoHeight + ""));
                BigDecimal d = new BigDecimal(mVideoHeight + "").subtract(c);
                params.width = mScreenWidth;
                params.height = d.intValue();
            } else {
                BigDecimal a = new BigDecimal(mScreenWidth + "").subtract(mVideoWidth);
                BigDecimal b = a.divide(new BigDecimal(mVideoWidth + ""), 2, BigDecimal.ROUND_DOWN);
                BigDecimal c = b.multiply(new BigDecimal(mVideoHeight + ""));
                BigDecimal d = new BigDecimal(mVideoHeight + "").add(c);
                params.width = mScreenWidth;
                params.height = d.intValue();
            }
        }else
        if (istransverse) {
            if (mVideoWidth.compareTo(new BigDecimal(mScreenWidth + "")) == 1) {  //如果视频的宽度大于屏幕的快读
                BigDecimal a = mVideoWidth.subtract(new BigDecimal(mScreenWidth + ""));
                BigDecimal b = a.divide(new BigDecimal(mVideoWidth + ""), 2, BigDecimal.ROUND_UP);
                BigDecimal c = b.multiply(new BigDecimal(mVideoHeight + ""));
                BigDecimal d = new BigDecimal(mVideoHeight + "").subtract(c);
                params.width = mScreenWidth;
                params.height = d.intValue();
            } else {
                BigDecimal a = new BigDecimal(mScreenWidth + "").subtract(mVideoWidth);
                BigDecimal b = a.divide(new BigDecimal(mVideoWidth + ""), 2, BigDecimal.ROUND_DOWN);
                BigDecimal c = b.multiply(new BigDecimal(mVideoHeight + ""));
                BigDecimal d = new BigDecimal(mVideoHeight + "").add(c);
                params.width = mScreenWidth;
                params.height = d.intValue();
            }

        } else {
            if (mVideoHeight.compareTo(new BigDecimal(mScreenHeight + "")) == 1) {
                BigDecimal a = mVideoHeight.subtract(new BigDecimal(mScreenHeight + ""));
                BigDecimal b = a.divide(new BigDecimal(mVideoHeight + ""), 2, BigDecimal.ROUND_UP);
                BigDecimal c = b.multiply(new BigDecimal(mVideoWidth + ""));
                BigDecimal d = new BigDecimal(mVideoWidth + "").subtract(c);
                params.width = d.intValue();
                params.height = mScreenHeight;
            } else {
                BigDecimal a = new BigDecimal(mScreenHeight + "").subtract(mVideoHeight);
                BigDecimal b = a.divide(new BigDecimal(mVideoHeight + ""), 2, BigDecimal.ROUND_UP);
                BigDecimal c = b.multiply(new BigDecimal(mVideoWidth + ""));
                BigDecimal d = new BigDecimal(mVideoWidth + "").add(c);
                params.width = d.intValue();
                params.height = mScreenHeight;
            }
        }

//        params.width = 600;
//        params.height = 800;
        getHolder().setFixedSize(0, 0);


//        params.height = mScreenHeight;
//        params.width = mScreenWidth+500;
        //     this.onSurfaceSizeChanged(params.width ,params.height );
//        params.gravity = Gravity.CENTER;
        this.setLayoutParams(params);
    }

    @Override
    public void onRenderedFirstFrame() {
        // do nothing
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.player != null) {
            player.removeVideoListener(this);
            this.player.release();
            this.player = null;
        }
    }
}
