package com.fengwo.module_live_vedio.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_live_vedio.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class SbQipaoView extends View {

    protected final int LEFT = 1;
    protected final int RIGHT = 2;
    protected final int TOP = 3;
    protected final int BOTTOM = 4;
    private final long ALIVETIME = 5 * 1000;//存活时间

    private static final float[][] STAR_LOCATION = new float[][]{
            {0.5f, 0.2f}, {0.68f, 0.35f}, {0.5f, 0.05f},
            {0.15f, 0.15f}, {0.5f, 0.5f}, {0.15f, 0.8f},
            {0.2f, 0.3f}, {0.77f, 0.4f}, {0.75f, 0.5f},
            {0.8f, 0.55f}, {0.9f, 0.6f}, {0.1f, 0.7f},
            {0.1f, 0.1f}, {0.7f, 0.8f}, {0.5f, 0.6f}
    };

    private Resources mResources;
    private Bitmap p1, p2, p3, p4, p5, p6, p7;
    private Rect r1, r2, r3, r4, r5, r6;
    private int mFloatTransLowSpeed, mFloatTransMidSpeed, mFloatTransFastSpeed;
    private int mStartCount = 15;
    private int oneTimeAddCount = 10;
    private List<StarInfo> mStarInfos;
    private Paint mPaint;
    private int screenHeight, screenWidth;
    private Random random;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                checkTime();
                if (mStarInfos.size() == 0) {
                    invalidate();
                    handler.removeMessages(1);
                }
                for (int i = 0; i < mStarInfos.size(); i++) {
                    checkOut(mStarInfos.get(i));
                    resetStarFloat(mStarInfos.get(i));
                }
                handler.sendEmptyMessageDelayed(1, 10);
            }
        }
    };

    public SbQipaoView(Context context) {
        this(context, null);
    }

    public SbQipaoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SbQipaoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        random = new Random();
        mResources = context.getResources();
        screenHeight = ScreenUtils.getScreenHeight(context);
        screenWidth = ScreenUtils.getScreenWidth(context);
        initBitmapInfo();
        initSpeed();
        mStarInfos = new ArrayList<>();
    }

    public void addQipao() {
        initStarInfo();
        if (!handler.hasMessages(1))
            handler.sendEmptyMessage(1);
    }


    /**
     * init bitmap info
     */
    private void initBitmapInfo() {
        p1 = ((BitmapDrawable) mResources.getDrawable(R.drawable.live_paopao01))
                .getBitmap();
        p2 = ((BitmapDrawable) mResources.getDrawable(R.drawable.live_paopao02))
                .getBitmap();
        p3 = ((BitmapDrawable) mResources.getDrawable(R.drawable.live_paopao03))
                .getBitmap();
        p4 = ((BitmapDrawable) mResources.getDrawable(R.drawable.live_paopao04))
                .getBitmap();
        p5 = ((BitmapDrawable) mResources.getDrawable(R.drawable.live_paopao05))
                .getBitmap();
        p6 = ((BitmapDrawable) mResources.getDrawable(R.drawable.live_paopao06))
                .getBitmap();
        p7 = ((BitmapDrawable) mResources.getDrawable(R.drawable.live_paopao07))
                .getBitmap();
    }

    private void initSpeed() {
        mFloatTransLowSpeed = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.8f,
                mResources.getDisplayMetrics());
        mFloatTransMidSpeed = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.2f,
                mResources.getDisplayMetrics());
        mFloatTransFastSpeed = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.6f,
                mResources.getDisplayMetrics());
    }

    /**
     * 获取星球大小
     */
    private float getStarSize(float start, float end) {
        int next = new Random().nextInt(3);
        switch (next) {
            case 0:
                return 0.7f;
            case 1:
                return 0.8f;
            case 2:
                return 0.9f;
        }
        return 1f;
    }

    /**
     * 初始化星球运行方向
     */
    private int[] getStarDirection() {
        Random random = new Random();
        int randomInt = random.nextInt(2);
        int directionX = 0;
        int directionY = 0;
        switch (randomInt) {
            case 0:
                directionX = LEFT;
                break;
            case 1:
                directionX = RIGHT;
                break;
        }
        randomInt = random.nextInt(2);
        switch (randomInt) {
            case 0:
                directionY = TOP;
                break;
            case 1:
                directionY = BOTTOM;
                break;
        }
        int[] direction = {directionX, directionY};
        return direction;
    }

    /**
     * 初始化星球信息
     */
    private void initStarInfo() {

        StarInfo starInfo = null;
        Random random = new Random();
        for (int i = 0; i < oneTimeAddCount; i++) {
            starInfo = new StarInfo();
            int mod = i % 7;
            L.e("modmodmod", i + "");
            switch (mod) {
                case 0:
                    starInfo.bp = p1;
                    break;
                case 1:
                    starInfo.bp = p2;
                    break;
                case 2:
                    starInfo.bp = p3;
                    break;
                case 3:
                    starInfo.bp = p4;
                    break;
                case 4:
                    starInfo.bp = p5;
                    break;
                case 5:
                    starInfo.bp = p6;
                    break;
                default:
                    starInfo.bp = p7;
                    break;
            }
            // 获取星球大小比例
            float starSize = getStarSize(1f, 2f);
            // 初始化星球大小
            float[] starLocation = STAR_LOCATION[i];

            starInfo.sizePercent = starSize;
            // 初始化漂浮速度
            int randomSpeed = random.nextInt(3);
            switch (randomSpeed) {
                case 0:
                    starInfo.speed = mFloatTransLowSpeed;
                    break;
                case 1:
                    starInfo.speed = mFloatTransMidSpeed;
                    break;
                case 2:
                    starInfo.speed = mFloatTransFastSpeed;
                    break;
                default:
                    starInfo.speed = mFloatTransMidSpeed;
                    break;
            }
            // 初始化星球透明度
            starInfo.alpha = getStarSize(1f, 2f);
            // 初始化星球位置
            int x = (int) (starLocation[0] * screenWidth);
            if (x + starInfo.bp.getWidth() * starInfo.sizePercent > screenWidth) {
                x = (int) (screenWidth - starInfo.bp.getWidth() * starInfo.sizePercent);
            }
            starInfo.xLocation = (int) (random.nextFloat() * screenWidth);
            starInfo.yLocation = (int) (random.nextFloat() * screenHeight);
            L.e("xLocation = " + starInfo.xLocation + "--yLocation = "
                    + starInfo.yLocation);
            L.e("stoneSize = " + starSize + "---stoneAlpha = "
                    + starInfo.alpha);
            // 初始化星球位置
            starInfo.directionX = getStarDirection()[0];
            starInfo.directionY = getStarDirection()[1];
            starInfo.endTime = System.currentTimeMillis() + ALIVETIME;//只存在10秒
            mStarInfos.add(starInfo);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mStarInfos.size(); i++) {
            drawStarDynamic(i, mStarInfos.get(i), canvas, mPaint);
        }
    }

    private void drawStarDynamic(int count, StarInfo starInfo,
                                 Canvas canvas, Paint paint) {

        float starAlpha = starInfo.alpha;
        int xLocation = starInfo.xLocation;
        int yLocation = starInfo.yLocation;
        float sizePercent = starInfo.sizePercent;

        xLocation = (int) (xLocation / sizePercent);
        yLocation = (int) (yLocation / sizePercent);

        Bitmap bitmap = null;
        Rect destRect = new Rect();
        bitmap = starInfo.bp;
        destRect.set(xLocation, yLocation,
                xLocation + bitmap.getWidth(), yLocation
                        + bitmap.getHeight());
//        paint.setAlpha((int) (starAlpha * 255));
        canvas.save();
        canvas.scale(sizePercent, sizePercent);
        canvas.drawBitmap(bitmap, null, destRect, paint);
        canvas.restore();
    }


    /**
     * 星球
     *
     * @author AJian
     */
    private class StarInfo {
        Bitmap bp;
        // 缩放比例
        float sizePercent;
        // x位置
        int xLocation;
        // y位置
        int yLocation;
        // 透明度
        float alpha;
        // 漂浮方向
        int directionX;
        int directionY;
        // 漂浮速度
        int speed;
        long endTime;
    }

    private void checkOut(StarInfo starInfo) {
        if (starInfo.directionX == RIGHT) {
            if (starInfo.xLocation + (starInfo.bp.getWidth() * starInfo.sizePercent) >= screenWidth) {
                starInfo.directionX = LEFT;
            }
        }
        if (starInfo.directionX == LEFT) {
            if (starInfo.xLocation <= 0) {
                starInfo.directionX = RIGHT;
            }
        }
        if (starInfo.directionY == TOP) {
            if (starInfo.yLocation <= 0) {
                starInfo.directionY = BOTTOM;
            }
        }
        if (starInfo.directionY == BOTTOM) {
            if (starInfo.yLocation + (starInfo.sizePercent * starInfo.bp.getHeight()) >= screenHeight) {
                starInfo.directionY = TOP;
            }
        }
    }

    private void checkTime() {
        Iterator<StarInfo> it = mStarInfos.iterator();
        if (it.hasNext()) {
            StarInfo item = it.next();
            if (System.currentTimeMillis() - item.endTime >= ALIVETIME) {
                it.remove();
            }
        }
    }

    private void resetStarFloat(StarInfo starInfo) {
        switch (starInfo.directionX) {
            case LEFT:
                starInfo.xLocation -= starInfo.speed;
                break;
            case RIGHT:
                starInfo.xLocation += starInfo.speed;
                break;
            default:
                break;
        }
        switch (starInfo.directionY) {
            case TOP:
                starInfo.yLocation -= starInfo.speed;
                break;
            case BOTTOM:
                starInfo.yLocation += starInfo.speed;
                break;
            default:
                break;
        }
        invalidate();
    }
}
