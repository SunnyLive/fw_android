package com.fengwo.module_flirt.UI.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.AnimDotBean;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MarqueView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private ExecutorService executorService;

    public MarqueView(Context context) {
        this(context, null);
    }

    private float mRingRadius;

    public MarqueView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mRectFCache = new SparseArray<>();
        flagIsRunning = new AtomicBoolean(false);
        animDotBeans = new ArrayList<>();

        mDotR = getResources().getDimensionPixelOffset(R.dimen.dp_4) / 2;
        mRingRadius = getResources().getDimensionPixelOffset(R.dimen.dp_4)*2;
        mItemSpace = getResources().getDimensionPixelOffset(R.dimen.dp_4)*5;

        initPaint();
        initSurface();
        initWorker();
    }


    private void initWorker() {
        executorService = Executors.newFixedThreadPool(1);
    }

    private void initPaint() {
        mClipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mClipPaint.setStyle(Paint.Style.FILL);
        mClipPaint.setColor(Color.TRANSPARENT);
        mClipPaint.setAlpha(0);
        mClipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(R.color.c9014C5));
    }

    private void initSurface() {
        setZOrderOnTop(true);
      setZOrderMediaOverlay(false);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        getHolder().addCallback(this);
    }


    private SparseArray<RectF> mRectFCache;
    private Paint mPaint, mClipPaint;

    private RectF mClipRectF, mCircleRingRectF;

    private SurfaceHolder mHolder;

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        mHolder = holder;
        startDraw();
    }

    private AtomicBoolean flagIsRunning;

    private void startDraw() {
        if (flagIsRunning.compareAndSet(false, true)) {
            executorService.submit(this);
        }
    }


    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        mClipRectF = mRectFCache.get(0);
        if (mClipRectF == null) {
            mClipRectF = new RectF(0, 0, getWidth(), getHeight());
            mCircleRingRectF = new RectF(mRingRadius, mRingRadius, getWidth() - mRingRadius, getHeight() - mRingRadius);
            mRectFCache.put(1, mCircleRingRectF);
        }
        mCircleRingRectF = mRectFCache.get(1);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void run() {
        while (flagIsRunning.get()) {
            Canvas canvas = mHolder.lockCanvas();
            synchronized (MarqueView.class) {
                if (canvas != null) {
                    try {
                        drawClipRoundRectFBg(canvas);
                        drawDot(canvas);
                    } finally {
                        mHolder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private List<AnimDotBean> animDotBeans;

    private int mItemSpace;
    private int mDotR;

    private void drawDot(Canvas canvas) {
        canvas.save();
        int crow = (int) Math.floor((getWidth()-2*mDotR) * 1.0f / (mItemSpace + mDotR)) + 1;
        int span = (int) Math.floor((getHeight()-2*mDotR) * 1.0f / (mItemSpace + mDotR)) + 1;
        if (animDotBeans.isEmpty()) {
            fillDotDataOnce(2 * (crow + span));
        }
        for (int index = 0; index < animDotBeans.size(); index++) {
            AnimDotBean animDotBean = animDotBeans.get(index);
            translateCanvasIfNeeded(index, crow, span, canvas);
            animDotBean.update(canvas);
        }
        canvas.restore();
    }

    private void fillDotDataOnce(int count) {
        for (int i = 0; i < count; i++) {
            animDotBeans.add(new AnimDotBean(getContext(), R.dimen.dp_4, i % 2 == 0 ? R.array.dot_colors : R.array.dot_colors2));
        }
    }


    private boolean isConnerPoint(int index, int crow, int span) {
        return index == 0 || index == crow || index == crow + span || index == crow + crow + span;
    }

    private void translateCanvasIfNeeded(int index, int crow, int span, Canvas canvas) {
        if (index <= crow) {
            if(index == 0){
                canvas.translate(mRingRadius+mDotR,mRingRadius/2f);
            }else if(index == crow){
                float off = (getWidth() - (crow-1)*mItemSpace);
                canvas.translate(off/2f,mRingRadius);
            }else{
                canvas.translate(mItemSpace, 0);
            }
        } else if (index <= crow + span) {
            if(index == crow+span){
                float off = getHeight() - mRingRadius/2f - (span-1)*mItemSpace - (mRingRadius+mItemSpace)/2f;
                canvas.translate(-(mRingRadius+mDotR/2f), (off+mRingRadius)/2f);
            }else{
                canvas.translate(0, mItemSpace);
            }
        } else if (index <= crow + span + crow) {
            if(index == 2*crow+span){
//                float offX= (getWidth() - (crow-1)*mItemSpace);
                float offX= (getWidth() - (crow-1)*mItemSpace) -(mRingRadius+mDotR/2f);
                float offY = getHeight() - mRingRadius/2f - (span-1)*mItemSpace - (mRingRadius+mItemSpace)/2f;
                canvas.translate(-offX/2f, -offY/2f -mRingRadius/2f - mDotR);
//                canvas.translate(-offX/2f, -offY/2f);
            }else{
                canvas.translate(-mItemSpace, 0);
            }
        } else if (index <= crow + span + crow + span) {
            canvas.translate(0, -mItemSpace);
        }
//        if (index <= crow) {
//            if(index == 0){
//                canvas.translate(mRingRadius,mRingRadius/2f);
//            }else if(index == crow){
//                canvas.translate(mItemSpace-mDotR/2f,(mRingRadius+mItemSpace)/2f);
//            }else{
//                canvas.translate(mItemSpace, 0);
//            }
//        } else if (index <= crow + span) {
//            if(index == crow+span){
//                canvas.translate(-(mRingRadius+mDotR/2f), mDotR+mRingRadius+mItemSpace/2f);
//            }else{
//                canvas.translate(0, mItemSpace);
//            }
//        } else if (index <= crow + span + crow) {
//            if(index == 2*crow+span){
//                canvas.translate(-(mRingRadius+mItemSpace)/2f, -(mRingRadius+mItemSpace/2f));
//            }else{
//                canvas.translate(-mItemSpace, 0);
//            }
//        } else if (index <= crow + span + crow + span) {
//            canvas.translate(0, -mItemSpace);
//        }
    }

    /**
     * 绘制矩形裁剪背景
     *
     * @param canvas Canvas
     */
    private void drawClipRoundRectFBg(Canvas canvas) {
        canvas.drawRoundRect(mClipRectF, 24, 24, mPaint);
        canvas.drawRoundRect(mCircleRingRectF, 24, 24, mClipPaint);
    }
//    private ExecutorService executorService;
//
//    public MarqueView(Context context) {
//        this(context, null);
//    }
//
//    private float mRingRadius;
//
//    public MarqueView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        mRectFCache = new SparseArray<>();
//        flagIsRunning = new AtomicBoolean(false);
//        animDotBeans = new ArrayList<>();
//
//        mDotR = getResources().getDimensionPixelOffset(R.dimen.x12) / 2;
//        mRingRadius = getResources().getDimensionPixelOffset(R.dimen.x16);
//        mItemSpace = mDotR * 9;
//
//        initPaint();
//        initSurface();
//        initWorker();
//    }
//
//
//    private void initWorker() {
//        executorService = Executors.newFixedThreadPool(1);
//    }
//
//    private void initPaint() {
//        mClipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mClipPaint.setStyle(Paint.Style.FILL);
//        mClipPaint.setColor(Color.TRANSPARENT);
//        mClipPaint.setAlpha(0);
//        mClipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setColor(getResources().getColor(R.color.c9014C5));
//    }
//
//    private void initSurface() {
//        setZOrderOnTop(true);
//        getHolder().setFormat(PixelFormat.TRANSPARENT);
//        getHolder().addCallback(this);
//    }
//
//
//    private SparseArray<RectF> mRectFCache;
//    private Paint mPaint, mClipPaint;
//
//    private RectF mClipRectF, mCircleRingRectF;
//
//    private SurfaceHolder mHolder;
//
//    @Override
//    public void surfaceCreated(@NonNull SurfaceHolder holder) {
//        mHolder = holder;
//        startDraw();
//    }
//
//    private AtomicBoolean flagIsRunning;
//
//    private void startDraw() {
//        if (flagIsRunning.compareAndSet(false, true)) {
//            executorService.submit(this);
//        }
//    }
//
//
//    @Override
//    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
//        mClipRectF = mRectFCache.get(0);
//        if (mClipRectF == null) {
//            mClipRectF = new RectF(0, 0, getWidth(), getHeight());
//            mCircleRingRectF = new RectF(mRingRadius, mRingRadius, getWidth() - mRingRadius, getHeight() - mRingRadius);
//            mRectFCache.put(1, mCircleRingRectF);
//        }
//        mCircleRingRectF = mRectFCache.get(1);
//    }
//
//    @Override
//    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
//
//    }
//
//    @Override
//    public void run() {
//        while (flagIsRunning.get()) {
//            Canvas canvas = mHolder.lockCanvas();
//            synchronized (mHolder) {
//                if (canvas != null) {
//                    try {
//                        drawClipRoundRectFBg(canvas);
//                        drawDot(canvas);
//                    } finally {
//                        mHolder.unlockCanvasAndPost(canvas);
//                    }
//                }
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }
//
//
//    private List<AnimDotBean> animDotBeans;
//
//    private int mItemSpace;
//    private int mDotR;
//
//    private void drawDot(Canvas canvas) {
//        canvas.save();
//        int crow = (int) Math.floor(getWidth() * 1.0f / (mItemSpace + mDotR)) + 1;
//        int span = (int) Math.floor((getHeight()-mItemSpace - mDotR) * 1.0f / (mItemSpace + mDotR)) + 1;
//        if (animDotBeans.isEmpty()) {
//            fillDotDataOnce(2 * (crow + span));
//        }
//        for (int index = 0; index < animDotBeans.size(); index++) {
//            AnimDotBean animDotBean = animDotBeans.get(index);
//            translateCanvasIfNeeded(index, crow, span, canvas);
//            animDotBean.update(canvas,isConnerPoint(index,crow,span));
//        }
//        canvas.restore();
//    }
//
//    private void fillDotDataOnce(int count) {
//        for (int i = 0; i < count; i++) {
//            animDotBeans.add(new AnimDotBean(getContext(), R.dimen.dp_4, i % 2 == 0 ? R.array.dot_colors : R.array.dot_colors2));
//        }
//    }
//
//
//    private boolean isConnerPoint(int index, int crow, int span) {
//        return index == 0 || index == crow || index == crow + span || index == crow + crow + span;
//    }
//
//    private void translateCanvasIfNeeded(int index, int crow, int span, Canvas canvas) {
//        if (index <= crow) {
//            if(index == 0){
//                canvas.translate(1.5F*mItemSpace,mItemSpace/2f);
//            }else if(index == crow){
//                canvas.translate(0,mItemSpace);
//            }else{
//                canvas.translate(mItemSpace, 0);
//            }
//        } else if (index <= crow + span) {
//            if(index == crow+span){
//                canvas.translate(-mItemSpace, mItemSpace/4f);
//            }else{
//                canvas.translate(0, mItemSpace);
//            }
//        } else if (index <= crow + span + crow) {
//            if(index == 2*crow+span){
//                canvas.translate(0, -mItemSpace);
//            }else{
//                canvas.translate(-mItemSpace, 0);
//            }
//        } else if (index <= crow + span + crow + span) {
//            canvas.translate(0, -mItemSpace);
//        }
//    }
//
//    /**
//     * 绘制矩形裁剪背景
//     *
//     * @param canvas Canvas
//     */
//    private void drawClipRoundRectFBg(Canvas canvas) {
//        canvas.drawRoundRect(mClipRectF, 24, 24, mPaint);
//        canvas.drawRoundRect(mCircleRingRectF, 24, 24, mClipPaint);
//    }
}
