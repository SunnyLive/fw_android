package com.fengwo.module_chat.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.BubbleBean;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class QiqiuView extends View {

    protected final int LEFT = 1;
    protected final int RIGHT = 2;
    protected final int TOP = 3;
    protected final int BOTTOM = 4;
    private final long ALIVETIME = 5 * 1000;//存活时间

    private Resources mResources;
    private int mFloatTransLowSpeed, mFloatTransMidSpeed, mFloatTransFastSpeed;
    private int oneTimeAddCount = 1;
    private List<StarInfo> mStarInfos;
    private Paint mPaint;
    private int screenHeight, screenWidth;
    private Paint textPaint;
    private Random random;
    private OnItemClickListener listener;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<Integer> textColors = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (mStarInfos.size() == 0) {
                    invalidate();
                    handler.removeMessages(5);
                }
                for (int i = 0; i < mStarInfos.size(); i++) {
                    checkOut(mStarInfos.get(i));
                }
                for (int i = 0; i < mStarInfos.size(); i++) {
                    resetStarFloat(mStarInfos.get(i));
                }
                handler.sendEmptyMessage(1);
            }
        }
    };

    public QiqiuView(Context context) {
        this(context, null);
    }

    public QiqiuView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QiqiuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        textPaint = new Paint();
        textPaint.setTextSize(DensityUtils.sp2px(context, 12));
        textPaint.setTextAlign(Paint.Align.CENTER);
        random = new Random();
        mResources = context.getResources();
        screenHeight = ScreenUtils.getScreenHeight(context);
        screenWidth = ScreenUtils.getScreenWidth(context);
        initBitmapInfo();
        initSpeed();
        mStarInfos = new ArrayList<>();
    }

    public void addQipao(BubbleBean bubbleBean) {
        initStarInfo(bubbleBean);
        if (!handler.hasMessages(1))
            handler.sendEmptyMessage(1);
    }


    /**
     * init bitmap info
     */
    private void initBitmapInfo() {
        Bitmap p1 = ((BitmapDrawable) mResources.getDrawable(R.drawable.live_paopao01))
                .getBitmap();
        Bitmap p2 = ((BitmapDrawable) mResources.getDrawable(R.drawable.live_paopao02))
                .getBitmap();
        Bitmap p3 = ((BitmapDrawable) mResources.getDrawable(R.drawable.live_paopao03))
                .getBitmap();
        Bitmap p4 = ((BitmapDrawable) mResources.getDrawable(R.drawable.live_paopao04))
                .getBitmap();
        Bitmap p5 = ((BitmapDrawable) mResources.getDrawable(R.drawable.live_paopao05))
                .getBitmap();
        bitmaps.add(p1);
        bitmaps.add(p2);
        bitmaps.add(p3);
        bitmaps.add(p4);
        bitmaps.add(p5);
        textColors.add(Color.parseColor("#9867FF"));
        textColors.add(Color.parseColor("#FFA92D"));
        textColors.add(Color.parseColor("#8F81FF"));
        textColors.add(Color.parseColor("#F19786"));
        textColors.add(Color.parseColor("#EE7EF2"));
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
        int directionX = LEFT;
        int directionY = 0;
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
     *
     * @param bubbleBean
     */
    private void initStarInfo(BubbleBean bubbleBean) {
        StarInfo starInfo = null;
        Random random = new Random();
        for (int i = 0; i < oneTimeAddCount; i++) {
            starInfo = new StarInfo();
            starInfo.id = bubbleBean.id;
            int mod = i % 5;
            starInfo.bp = bitmaps.get(mod);
            starInfo.textColor = textColors.get(mod);
            // 获取星球大小比例
            float starSize = getStarSize(1f, 2f);
            // 初始化星球大小
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
//            starInfo.xLocation = (int) (screenWidth);
//            starInfo.yLocation = (int) (random.nextFloat() * screenHeight);
            starInfo.xLocation = (int) ((screenWidth - DensityUtils.dp2px(getContext(), 80)) * random.nextFloat());
            starInfo.yLocation = (int) (screenHeight);
            L.e("xLocation = " + starInfo.xLocation + "--yLocation = "
                    + starInfo.yLocation);
            L.e("stoneSize = " + starSize + "---stoneAlpha = "
                    + starInfo.alpha);
            // 初始化星球位置
//            starInfo.directionX = getStarDirection()[0];
//            starInfo.directionY = getStarDirection()[1];
            starInfo.directionX = LEFT;
            starInfo.directionY = BOTTOM;
            starInfo.endTime = System.currentTimeMillis() + ALIVETIME;//只存在10秒
            starInfo.name = bubbleBean.bubbleName;
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
            drawText(mStarInfos.get(i), canvas, textPaint);
        }
    }

    private void drawText(StarInfo starInfo, Canvas canvas, Paint textPaint) {
        if (starInfo == null || TextUtils.isEmpty(starInfo.name)) return;
        Bitmap bp = starInfo.bp;
        int startX = starInfo.xLocation + bp.getWidth() / 2;
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int startY = (int) (starInfo.yLocation + bp.getHeight() / 2 + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
        textPaint.setColor(starInfo.textColor);
        canvas.drawText(starInfo.name, startX, startY, textPaint);
    }

    private void drawStarDynamic(int count, StarInfo starInfo,
                                 Canvas canvas, Paint paint) {
        int xLocation = starInfo.xLocation;
        int yLocation = starInfo.yLocation;
        Bitmap bitmap = null;
        bitmap = starInfo.bp;
        Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect destRect = new Rect();
        destRect.set(xLocation, yLocation,
                xLocation + bitmap.getWidth(), yLocation
                        + bitmap.getHeight());

        canvas.drawBitmap(bitmap, srcRect, destRect, paint);
        starInfo.nowLocation = destRect;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionId = event.getAction();
        switch (actionId) {
            case MotionEvent.ACTION_DOWN:
                L.e("ACTION_DOWN");
                int clickX = (int) event.getX();
                int clickY = (int) event.getY();
                for (int i = 0; i < mStarInfos.size(); i++) {
                    if (mStarInfos.get(i).nowLocation.contains(clickX, clickY)) {
                        if (listener != null)
                            listener.onItemClick(mStarInfos.get(i).id, mStarInfos.get(i).name);
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                L.e("ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                L.e("ACTION_UP");
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 星球
     *
     * @author AJian
     */
    private class StarInfo {
        String id;
        String name;
        Bitmap bp;
        int textColor;
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
        Rect nowLocation;
    }

    private void checkOut(StarInfo starInfo) {
        if (starInfo.directionX == LEFT) {
            if (starInfo.xLocation <= 0) {//超出范围 销毁
                destoryItem(starInfo.endTime);
            }
        }
        if (starInfo.directionY == TOP) {
            if (starInfo.yLocation + starInfo.bp.getWidth() <= 0) {
                destoryItem(starInfo.endTime);
            }
        }
        if (starInfo.directionY == BOTTOM) {
            if (starInfo.yLocation + starInfo.bp.getHeight() >= screenHeight) {
                starInfo.directionY = TOP;
            }
        }
    }

    /**
     * 懒得改 通过endtime匹配 并销毁
     *
     * @param endTime
     */
    private void destoryItem(long endTime) {
        Iterator<StarInfo> it = mStarInfos.iterator();
        if (it.hasNext()) {
            StarInfo item = it.next();
            if (item.endTime == endTime) {
                it.remove();
            }
        }
    }

    private void resetStarFloat(StarInfo starInfo) {
//        switch (starInfo.directionX) {
//            case LEFT:
//                starInfo.xLocation -= starInfo.speed;
//                break;
//            case RIGHT:
//                starInfo.xLocation += starInfo.speed;
//                break;
//            default:
//                break;
//        }
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

    public void setOnItemClickListener(OnItemClickListener l) {
        listener = l;
    }

    public interface OnItemClickListener {
        void onItemClick(String bubbleId, String name);
    }
}