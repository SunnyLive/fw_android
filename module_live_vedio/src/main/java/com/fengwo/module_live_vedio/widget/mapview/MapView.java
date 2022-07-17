package com.fengwo.module_live_vedio.widget.mapview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.MapPoiDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 地图控件测试
 */

public class MapView extends RelativeLayout {
    //这是一个40*22的地图w*h
    private static final int MAX_POINT_WIDTH = 40;
    private static final int MAX_POINT_HEIGHT = 20;
    private static final float DRAWPADDING = 5;


    private int[][] points = new int[MAX_POINT_HEIGHT][MAX_POINT_WIDTH];


    private Context mContext;
    //画笔
    Paint mPaint;
    //控件宽度
    private int mViewWidth;
    //控件高度
    private int mViewHeight;
    //控制画板的矩阵
    private Matrix mMapMatrix;
    //地图初始化需要位移
    private float mInitTranslateX;
    private float mInitTranslateY;
    //地图Bitmap
    private Bitmap mMapBitmap;
    //初始化绘制开关
    private boolean mIsFirstDraw = true;
    //此处手指情况只考虑单指移动和双指缩放
    //上次手指停留位置（单手指）
    private float mLastSinglePointX;
    private float mLastSinglePointY;
    //用于双指缩放
    private float mLastDistancce;
    //最小缩放倍数
    private float mMinScale = 2f;
    //最大缩放倍数
    private float mMaxScale = 4.0f;


    //上次手机离开时缩放倍数
    private float mLastScale = mMinScale;

    //是否能够移动的标志
    private boolean mCouldMove = true;
    //矩阵对应的值
    float[] mNowMatrixvalues;

    //x位移最大值
    private float mMaxTranslateX;
    //Y位移最大值
    private float mMaxTranslateY;

    /**
     * 边界回弹状态  边界起头：1   例：11
     *
     * @param context
     */
    private int mNowBoundStates = 0;
    //只向上恢复
    private static final int BOUND_ONLY_TOP = 11;
    //只向左恢复
    private static final int BOUND_ONLY_LEFT = 12;
    //同时向左和上恢复
    private static final int BOUND_TOPANDLEFT = 13;
    //只向右恢复
    private static final int BOUND_ONLY_RIGHT = 14;
    //同时向右上恢复
    private static final int BOUND_RIGHTANDTOP = 15;
    //只向下恢复
    private static final int BOUND_ONLY_BOTTOM = 16;
    //同时向右下恢复
    private static final int BOUND_RIGHTANDBOTTOM = 17;
    //同时向左下恢复
    private static final int BOUND_LEFTANDBOTTOM = 18;
    //属性动画起始和结束值
    private static final int REBOUND_ANIMATION_START_VALUE = 0;
    private static final int REBOUND_ANIMATION_END_VALUE = 100;
    private static final int REBOUND_ANIMATION_TIME = 200;

    //poi实体集合
    List<MapPoiDto> mMapPoiEntityList;
    //poi点的默认图标
    private Bitmap mPoiBitmapDefault1;
    private Bitmap mPoiBitmapDefault2;
    private Bitmap mPoiBitmapDefault3;
    private Bitmap mPoiBitmapDefault4;
    private Bitmap mPoiBitmapDefault5;
    private Bitmap mPoiBitmapDefault6;
    private Bitmap mPoiBitmapDefault7;
    //poi点的默认图标
    private Bitmap mTxtBitmapDefault;
    //测量文字宽高的rect
    private Rect mTextRect;
    //poi圆角矩形的rectf
    private RectF poiConersRectf;
    //三角形的Path
    private Path trianglePath;
    //存储poi边界
    private List<PoiBoundEntity> mPoiBoundEntities;


    float imgHeight;
    float imgWidth;
    float onePointWidth;//一个网格的宽
    float onePointHeight;//一个网格的高

    public MapView(Context context) {
        super(context);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mMapMatrix = new Matrix();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mNowMatrixvalues = new float[9];
        mPoiBoundEntities = new ArrayList<>();
//加载城池资源
        mPoiBitmapDefault1 = BitmapFactory.decodeResource(getResources(), R.drawable.txtbg);
        mPoiBitmapDefault2 = BitmapFactory.decodeResource(getResources(), R.drawable.txtbg);
        mPoiBitmapDefault3 = BitmapFactory.decodeResource(getResources(), R.drawable.txtbg);
        mPoiBitmapDefault4 = BitmapFactory.decodeResource(getResources(), R.drawable.txtbg);
        mPoiBitmapDefault5 = BitmapFactory.decodeResource(getResources(), R.drawable.txtbg);
        mPoiBitmapDefault6 = BitmapFactory.decodeResource(getResources(), R.drawable.txtbg);
        mPoiBitmapDefault7 = BitmapFactory.decodeResource(getResources(), R.drawable.txtbg);

        mTxtBitmapDefault = BitmapFactory.decodeResource(getResources(), R.drawable.txtbg);
        mTextRect = new Rect();
        poiConersRectf = new RectF();
        trianglePath = new Path();

        mPoiBitmapDefault1 = scaleBitmap(mPoiBitmapDefault1, DensityUtils.dp2px(mContext, 50), DensityUtils.dp2px(mContext, 38));
        mPoiBitmapDefault2 = scaleBitmap(mPoiBitmapDefault2, DensityUtils.dp2px(mContext, 55), DensityUtils.dp2px(mContext, 40));
        mPoiBitmapDefault3 = scaleBitmap(mPoiBitmapDefault3, DensityUtils.dp2px(mContext, 60), DensityUtils.dp2px(mContext, 43));
        mPoiBitmapDefault4 = scaleBitmap(mPoiBitmapDefault4, DensityUtils.dp2px(mContext, 65), DensityUtils.dp2px(mContext, 45));
        mPoiBitmapDefault5 = scaleBitmap(mPoiBitmapDefault5, DensityUtils.dp2px(mContext, 70), DensityUtils.dp2px(mContext, 50));
        mPoiBitmapDefault6 = scaleBitmap(mPoiBitmapDefault6, DensityUtils.dp2px(mContext, 70), DensityUtils.dp2px(mContext, 50));
        mPoiBitmapDefault7 = scaleBitmap(mPoiBitmapDefault7, DensityUtils.dp2px(mContext, 70), DensityUtils.dp2px(mContext, 50));
        mTxtBitmapDefault = scaleBitmap(mTxtBitmapDefault, DensityUtils.dp2px(mContext, 80), DensityUtils.dp2px(mContext, 20));
    }

    /**
     * 对bitmap进行缩放
     */
    public Bitmap scaleBitmap(Bitmap bitmap, int scaleX, int scaleY) {
        float imgHeight = bitmap.getHeight();
        float imgWidth = bitmap.getWidth();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX / imgWidth, scaleY / imgHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, (int) imgWidth, (int) imgHeight, matrix, true);
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    /**
     * 初始化图片的宽高
     */
    public void initImageWH(Bitmap mapImg, OnPointClickLisenter onPointClickLisenter) {
        this.onPointClickLisenter = onPointClickLisenter;
        imgHeight = mapImg.getHeight();
        imgWidth = mapImg.getWidth();
        onePointWidth = imgWidth / MAX_POINT_WIDTH;
        onePointHeight = imgHeight / MAX_POINT_HEIGHT;

        float changeWidth = 0.0f;
        float changeHeight = 0.0f;
        float scaleWidth = mViewWidth / imgWidth;
        float scaleHeight = mViewHeight / imgHeight;
        //对图片宽高进行缩放
        if (scaleHeight > scaleWidth) {
            changeHeight = mViewHeight;
            changeWidth = mViewHeight * imgWidth / imgHeight;
            mInitTranslateY = 0;
            mInitTranslateX = -Math.abs((changeWidth - mViewWidth) / 2);
        } else {
            changeWidth = mViewWidth;
            changeHeight = mViewWidth * imgHeight / imgWidth;
            mInitTranslateY = -Math.abs((changeHeight - mViewHeight) / 2);
            mInitTranslateX = 0;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(changeWidth / imgWidth, changeHeight / imgHeight);//先按比例缩放
        mMapBitmap = Bitmap.createBitmap(mapImg, 0, 0, (int) imgWidth, (int) imgHeight, matrix, true);
        if (mapImg != null && mMapBitmap != null && !mapImg.equals(mMapBitmap) && !mapImg.isRecycled()) {
            mapImg = null;
        }
        //初次加载时，将Matrix移动到正确位置
        mMapMatrix.postScale(mMinScale, mMinScale);//比例缩放之后 在按给定的最小缩放值缩放
        mMapMatrix.postTranslate(mInitTranslateX, mInitTranslateY);
        invalidate();
    }

    /**
     * 设置poi内容
     */
    public void setMapPoiEntityList(List<MapPoiDto> mapPoiEntityList) {
        mMapPoiEntityList = mapPoiEntityList;
        for (int i = 0; i < mMapPoiEntityList.size(); i++) {
            View v = View.inflate(getContext(), R.layout.item_poi, null);
            TextView tvName = v.findViewById(R.id.tv2);
            TextView tvStatus = v.findViewById(R.id.tv1);
            tvName.setText(mapPoiEntityList.get(i).getCityName());
            if (mapPoiEntityList.get(i).getState() == 0) {//0：未报名 1：已报名
                tvStatus.setText("未报名");
            } else {
                tvStatus.setText("已报名");
            }
            addView(v);
        }
        invalidate();
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!CommentUtils.isListEmpty(mMapPoiEntityList))
            for (int i = 0; i < mMapPoiEntityList.size(); i++) {
                MapPoiDto dto = mMapPoiEntityList.get(i);
                View v = getChildAt(i);
                v.setScaleX(mLastScale / mMaxScale);
                v.setScaleY(mLastScale / mMaxScale);
                v.findViewById(R.id.iv).setScaleY(mLastScale / mMaxScale);
                v.findViewById(R.id.iv).setScaleX(mLastScale / mMaxScale);
                v.findViewById(R.id.tv1).setScaleY(mLastScale / mMaxScale);
                v.findViewById(R.id.tv1).setScaleX(mLastScale / mMaxScale);
                v.findViewById(R.id.tv2).setScaleY(mLastScale / mMaxScale);
                v.findViewById(R.id.tv2).setScaleX(mLastScale / mMaxScale);
                L.e("xxxxxxxxxxxxx" + v.getMeasuredHeight() + "xxxxxxxx" + v.getMeasuredWidth());
                int width = (int) (onePointWidth * mLastScale);
                int height = (int) (onePointHeight * mLastScale);
                int left = (int) (getMatrixRectF().left + ((dto.getX() - 1) * width));
                int top = (int) (getMatrixRectF().top + ((dto.getY() - 1) * height));
                v.layout(left, top, left + width, top + height);
            }
    }

    /**
     * 测量控件宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        }
        mMaxTranslateX = mViewWidth / 6;
        mMaxTranslateY = mViewHeight / 8;
        setMeasuredDimension(mViewWidth, mViewHeight);
    }


    /**
     * 用户触控事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mMapMatrix.getValues(mNowMatrixvalues);
        //缩放
        scaleCanvas(event);
        //位移
        translateCanvas(event);
        //边界控制
        return true;
    }

    /**
     * 用户双指缩放操作
     *
     * @param event
     */
    private void scaleCanvas(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            mCouldMove = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    float lastlengthOFY = Math.abs(event.getY(1) - event.getY(0));
                    float lastlengthOFX = Math.abs(event.getX(1) - event.getX(0));
                    mLastDistancce = (float) Math.sqrt(lastlengthOFX * lastlengthOFX + lastlengthOFY * lastlengthOFY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float lengthOFY = Math.abs(event.getY(1) - event.getY(0));
                    float lengthOFX = Math.abs(event.getX(1) - event.getX(0));
                    float distance = (float) Math.sqrt(lengthOFX * lengthOFX + lengthOFY * lengthOFY);
                    float scale = distance / mLastDistancce;
                    if (mLastDistancce != 0) {
                        //缩放大小控制
                        float nowScale = mNowMatrixvalues[Matrix.MSCALE_X];
                        if ((nowScale > mMaxScale && scale > 1.0f) || (nowScale < mMinScale && scale < 1.0f)) {
                            return;
                        }
                        mMapMatrix.postScale(scale, scale,
                                event.getX(0) + (event.getX(1) - event.getX(0)) / 2,
                                event.getY(0) + (event.getY(1) - event.getY(0)) / 2);
                        mLastScale = nowScale;
                    }
                    mLastDistancce = distance;
                    invalidate();
                    requestLayout();
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mLastDistancce = 0;
                    break;
                case MotionEvent.ACTION_POINTER_2_UP:
                    mLastDistancce = 0;
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 获取poi点击事件
     */
    private void getWhichPoiClick(float xTouch, float yTouch) {
        RectF matrixRectF = getMatrixRectF();
        for (int i = 0; i < mMapPoiEntityList.size(); i++) {
            MapPoiDto dto = mMapPoiEntityList.get(i);
            int width = (int) (onePointWidth * mLastScale);
            int height = (int) (onePointHeight * mLastScale);
            int left = (int) ((dto.getX() - 1) * width);
            int top = (int) ((dto.getY() - 1) * height);
            Rect rect = new Rect(left, top, left + width, top + height);
            if (rect.contains((int) (Math.abs(matrixRectF.left) + xTouch), (int) (Math.abs(matrixRectF.top) + yTouch))) {
                Toast.makeText(mContext, "被点击了" + dto.getCityName() + "", Toast.LENGTH_SHORT).show();
                if (onPointClickLisenter != null) {
                    onPointClickLisenter.onPointClick(dto);
                }
                break;
            }
        }
    }


    float translateX;
    float translateY;

    /**
     * 用户手指的位移操作
     *
     * @param event
     */
    private void translateCanvas(MotionEvent event) {
        if (event.getPointerCount() == 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //获取到上一次手指位置
                    mLastSinglePointX = event.getX();
                    mLastSinglePointY = event.getY();
                    //寻找点击位置
                    getWhichPoiClick(event.getRawX(), event.getRawY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mCouldMove) {
                        translateX = event.getX() - mLastSinglePointX;
                        translateY = event.getY() - mLastSinglePointY;
                        RectF matrixRectF = getMatrixRectF();
                        //边界控制
                        //left不能大于mMaxTranslateX,right值不能小于mViewwidth-mMaxTranslateX
                        if ((matrixRectF.left >= mMaxTranslateX && translateX > 0) || ((matrixRectF.right <= mViewWidth - mMaxTranslateX) && translateX < 0)) {
                            translateX = 0;
                        }
                        //top不能大于mMaxTranslateY,bottom值不能小于mViewHeight-mMaxTranslateY
                        if ((matrixRectF.top >= mMaxTranslateY && translateY > 0) || ((matrixRectF.bottom <= mViewHeight - mMaxTranslateY) && translateY < 0)) {
                            translateY = 0;
                        }
                        //对本次移动造成的超过范围做调整
                        if (translateX > 0 && ((matrixRectF.left + translateX) > mMaxTranslateX)) {
                            translateX = mMaxTranslateX - matrixRectF.left;
                        }
                        if (translateX < 0 && ((matrixRectF.right + translateX) < mViewWidth - mMaxTranslateX)) {
                            translateX = -(mMaxTranslateX - (mViewWidth - matrixRectF.right));
                        }
                        if (translateY > 0 && ((matrixRectF.top + translateY) > mMaxTranslateY)) {
                            translateY = mMaxTranslateY - matrixRectF.top;
                        }
                        if (translateY < 0 && ((matrixRectF.bottom + translateY) < mViewHeight - mMaxTranslateY)) {
                            translateY = -(mMaxTranslateY - (mViewHeight - matrixRectF.bottom));
                        }
                        mMapMatrix.postTranslate(translateX, translateY);
                        mLastSinglePointX = event.getX();
                        mLastSinglePointY = event.getY();
                        invalidate();
                        requestLayout();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mLastSinglePointX = 0;
                    mLastSinglePointY = 0;
                    mLastDistancce = 0;
                    mCouldMove = true;
                    controlBound();
                    break;
            }
        }
    }


    /**
     * 用于控制用户的手指抬起时，对留边的情况进行控制
     */
    private void controlBound() {
        RectF matrixRectF = getMatrixRectF();
        float nowScale = mNowMatrixvalues[Matrix.MSCALE_X];
        if (mNowMatrixvalues[Matrix.MSCALE_X] < 1 && (mNowMatrixvalues[Matrix.MSCALE_X] + 0.001f) > 1) {
            //消除缩放误差
            nowScale = 1;
        }
        if (nowScale < 1) {
            //缩小的情况，缩小以四个顶角为基准,并且向位移多的地方恢复
            scaleBoundAnimation(matrixRectF);
        } else {
            //情况判断,放大或者一倍的情况
            if (matrixRectF.top > 0) {
                //头在边界下
                if (matrixRectF.left > 0) {
                    //向左上恢复的情况
                    mNowBoundStates = BOUND_TOPANDLEFT;
                } else if (matrixRectF.right < mViewWidth) {
                    //向右上恢复的情况
                    mNowBoundStates = BOUND_RIGHTANDTOP;
                } else {
                    //只向上恢复的情况
                    mNowBoundStates = BOUND_ONLY_TOP;
                }
            } else if (matrixRectF.top < 0 && matrixRectF.bottom > mViewHeight) {
                //头在边界上，底在边界下
                if (matrixRectF.left > 0) {
                    //只向左恢复的情况
                    mNowBoundStates = BOUND_ONLY_LEFT;
                } else if (matrixRectF.right < mViewWidth) {
                    //只向右恢复的情况
                    mNowBoundStates = BOUND_ONLY_RIGHT;
                }
            } else if (matrixRectF.top < 0 && matrixRectF.bottom < mViewHeight) {
                //底在边界上
                if (matrixRectF.left > 0) {
                    //向左下恢复的情况
                    mNowBoundStates = BOUND_LEFTANDBOTTOM;
                } else if (matrixRectF.right < mViewWidth) {
                    //向右下恢复的情况
                    mNowBoundStates = BOUND_RIGHTANDBOTTOM;
                } else {
                    //只向下恢复的情况
                    mNowBoundStates = BOUND_ONLY_BOTTOM;
                }
            }
            translateBoundAnimation(matrixRectF);
        }
    }

    /**
     * 边界回弹动画（缩放动画）
     */
    public void scaleBoundAnimation(final RectF martrixRectF) {
        final float startScale = mNowMatrixvalues[Matrix.MSCALE_X];
        //位移属性动画
        //上一次的缩放倍数
        final float[] lastScale = {0.0f};
        ValueAnimator valueAnimator = ValueAnimator.ofInt(REBOUND_ANIMATION_START_VALUE, REBOUND_ANIMATION_END_VALUE);
        valueAnimator.setDuration(REBOUND_ANIMATION_TIME);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //重新获取一下值
                mMapMatrix.getValues(mNowMatrixvalues);
                //缩放结束再查看边界进行位移缩放
                controlBound();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer numerator = (Integer) valueAnimator.getAnimatedValue();
                float total = REBOUND_ANIMATION_END_VALUE - REBOUND_ANIMATION_START_VALUE;
                if (lastScale[0] != 0) {
                    float shouldScale = (((float) numerator / total) * (1 - startScale) + startScale) / lastScale[0];
                    mMapMatrix.postScale(shouldScale, shouldScale, martrixRectF.left + martrixRectF.width() / 2, martrixRectF.top + martrixRectF.height() / 2);
                    invalidate();
                }
                lastScale[0] = ((float) numerator / total) * (1 - startScale) + startScale;
            }
        });
        valueAnimator.start();
    }

    /**
     * 边界回弹动画(位移动画)
     *
     * @param matrixRectF
     */
    public void translateBoundAnimation(final RectF matrixRectF) {
        //应该位移的x值与y值
        float boundTranslateX = 0.0f;
        float boundTranslateY = 0.0f;
        //右边恢复需要的位移值
        float translateRight = mViewWidth - matrixRectF.right;
        //下边恢复需要的位移值
        float translateBottom = mViewHeight - matrixRectF.bottom;
        //放大或者原图状态，直接进行位移操作
        switch (mNowBoundStates) {
            case BOUND_ONLY_TOP:
                boundTranslateY = -matrixRectF.top;
                break;
            case BOUND_ONLY_LEFT:
                boundTranslateX = -matrixRectF.left;
                break;
            case BOUND_ONLY_RIGHT:
                boundTranslateX = translateRight;
                break;
            case BOUND_ONLY_BOTTOM:
                boundTranslateY = translateBottom;
                break;
            case BOUND_TOPANDLEFT:
                boundTranslateY = -matrixRectF.top;
                boundTranslateX = -matrixRectF.left;
                break;
            case BOUND_RIGHTANDTOP:
                boundTranslateY = -matrixRectF.top;
                boundTranslateX = translateRight;
                break;
            case BOUND_RIGHTANDBOTTOM:
                boundTranslateX = translateRight;
                boundTranslateY = translateBottom;
                break;
            case BOUND_LEFTANDBOTTOM:
                boundTranslateX = -matrixRectF.left;
                boundTranslateY = translateBottom;
                break;
            default:
                break;

        }
        //位移属性动画
        ValueAnimator valueAnimator = ValueAnimator.ofInt(REBOUND_ANIMATION_START_VALUE, REBOUND_ANIMATION_END_VALUE);
        valueAnimator.setDuration(REBOUND_ANIMATION_TIME);
        valueAnimator.setInterpolator(new LinearInterpolator());
        final float finalBoundTranslateX = boundTranslateX;
        final float finalBoundTranslateY = boundTranslateY;
        //上一次valueAnimation移动的位置[x,y]
        final float[] lastTranslate = new float[2];
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer numerator = (Integer) valueAnimator.getAnimatedValue();
                float total = REBOUND_ANIMATION_END_VALUE - REBOUND_ANIMATION_START_VALUE;
                float shouldTranslateX = finalBoundTranslateX * ((float) numerator / total) - lastTranslate[0];
                lastTranslate[0] = finalBoundTranslateX * ((float) numerator / total);
                float shouldTranslateY = finalBoundTranslateY * ((float) numerator / total) - lastTranslate[1];
                lastTranslate[1] = finalBoundTranslateY * ((float) numerator / total);
                mMapMatrix.postTranslate(shouldTranslateX, shouldTranslateY);
                invalidate();
                requestLayout();
            }
        });
        valueAnimator.start();
        mNowBoundStates = 0;
    }


    /**
     * 获取当前bitmap矩阵的RectF,以获取宽高与margin
     *
     * @return
     */
    private RectF getMatrixRectF() {
        RectF rectF = new RectF();
        if (mMapBitmap != null) {
            rectF.set(0, 0, mMapBitmap.getWidth(), mMapBitmap.getHeight());
            mMapMatrix.mapRect(rectF);
        }
        return rectF;
    }

    /**
     * 地图移动到中间
     */
    private void moveToCenter() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMapBitmap == null)
            return;
        //绘制地图
        canvas.drawBitmap(mMapBitmap, mMapMatrix, mPaint);
        //绘制poi
//        drawPoi(canvas);
    }

    /**
     * 画poi点，并确定位置
     */
    private void drawPoi(Canvas canvas) {
        if (mMapPoiEntityList != null && mMapPoiEntityList.size() > 0) {
            for (int i = 0; i < mMapPoiEntityList.size(); i++) {
                //加粗文字
                mPaint.setFakeBoldText(true);
                //获取文字宽高
                mPaint.setTextSize(DensityUtils.dp2px(mContext, 10));
                mPaint.setStrokeWidth(8);
                //获取地图图片对应矩阵
                RectF matrixRectF = getMatrixRectF();


                //画poi图片
                int level = mMapPoiEntityList.get(i).getCityLevel();
                MapPoiDto data = mMapPoiEntityList.get(i);
                //城池 bp位置信息
                Bitmap tempBp;
                float bpLeft;
                float bpTop;
                //城池底部 文字背景 bp 位置信息
                float textBgLeft;
                float textBgTop;
                float drawablePadding = DRAWPADDING;
                float bgWidth = mTxtBitmapDefault.getWidth();
                float bgHeight = mTxtBitmapDefault.getHeight();
                float onePointWidth = MapView.this.onePointWidth * mLastScale;
                float onePointHeight = MapView.this.onePointHeight * mLastScale;
                switch (level) {
                    case 2:
                        tempBp = mPoiBitmapDefault2;
                        bpLeft = (data.getX() - 1) * onePointWidth + onePointWidth / 2 - tempBp.getWidth() / 2;
                        bpTop = (data.getY() - 1) * onePointHeight + onePointHeight / 2 + (tempBp.getHeight() + bgHeight + DRAWPADDING) / 2;
                        textBgTop = bpTop + tempBp.getHeight() + DRAWPADDING;
                        break;
                    case 3:
                        tempBp = mPoiBitmapDefault3;
                        bpLeft = (data.getX() - 1) * onePointWidth + onePointWidth / 2 - tempBp.getWidth() / 2;
                        bpTop = (data.getY() - 1) * onePointHeight + onePointHeight / 2 + (tempBp.getHeight() + bgHeight + DRAWPADDING) / 2;
                        textBgTop = bpTop + tempBp.getHeight() + DRAWPADDING;
                        break;
                    case 4:
                        tempBp = mPoiBitmapDefault4;
                        bpLeft = (data.getX() - 1) * onePointWidth + onePointWidth / 2 - tempBp.getWidth() / 2;
                        bpTop = (data.getY() - 1) * onePointHeight + onePointHeight / 2 - (tempBp.getHeight() + bgHeight + DRAWPADDING) / 2;
                        textBgTop = bpTop + tempBp.getHeight() + DRAWPADDING;
                        break;
                    case 5:
                        tempBp = mPoiBitmapDefault5;
                        bpLeft = (data.getX() - 1) * onePointWidth + onePointWidth / 2 - tempBp.getWidth() / 2;
                        bpTop = (data.getY() - 1) * onePointHeight + onePointHeight / 2 - (tempBp.getHeight() + bgHeight + DRAWPADDING) / 2;
                        textBgTop = bpTop + tempBp.getHeight() + DRAWPADDING;
                        break;
                    case 6:
                        tempBp = mPoiBitmapDefault6;
                        bpLeft = (data.getX() - 1) * onePointWidth + onePointWidth / 2 - tempBp.getWidth() / 2;
                        bpTop = (data.getY() - 1) * onePointHeight + onePointHeight / 2 - (tempBp.getHeight() + bgHeight + DRAWPADDING) / 2;
                        textBgTop = bpTop + tempBp.getHeight() + DRAWPADDING;
                        break;
                    case 7:
                        tempBp = mPoiBitmapDefault7;
                        bpLeft = (data.getX() - 1) * onePointWidth + onePointWidth / 2 - tempBp.getWidth() / 2;
                        bpTop = (data.getY() - 1) * onePointHeight + onePointHeight / 2 - (tempBp.getHeight() + bgHeight + DRAWPADDING) / 2;
                        textBgTop = bpTop + tempBp.getHeight() + DRAWPADDING;
                        break;
                    default:
                        tempBp = mPoiBitmapDefault1;
                        bpLeft = (data.getX() - 1) * onePointWidth + onePointWidth / 2 - tempBp.getWidth() / 2;
                        bpTop = (data.getY() - 1) * onePointHeight + onePointHeight / 2 - (tempBp.getHeight() + bgHeight + DRAWPADDING) / 2;
                        textBgTop = bpTop + tempBp.getHeight() + DRAWPADDING;
                        break;

                }
                canvas.drawBitmap(tempBp, matrixRectF.left + bpLeft, matrixRectF.top + bpTop, mPaint);
                textBgLeft = (data.getX() - 1) * onePointWidth + onePointWidth / 2 - bgWidth / 2;
                canvas.drawBitmap(mTxtBitmapDefault, matrixRectF.left + textBgLeft, matrixRectF.top + textBgTop, mPaint);
                //画文字
                mPaint.setColor(getResources().getColor(R.color.black_000000));
                Rect textR = new Rect();
                String cityName = mMapPoiEntityList.get(i).getCityName();
                mPaint.getTextBounds(cityName, 0, cityName.length(), textR);
                float textL = (data.getX() - 1) * onePointWidth + onePointWidth / 2 - textR.width() / 2;
                canvas.drawText(mMapPoiEntityList.get(i).getCityName(), matrixRectF.left + textL, matrixRectF.top + bpTop + tempBp.getHeight() + DRAWPADDING + mTxtBitmapDefault.getHeight() / 2 + textR.height() / 2, mPaint);
            }
            mIsFirstDraw = false;
        }
    }

    //回收同步生命周期
    public void onDestroy() {
        if (mMapBitmap != null) {
            mMapBitmap = null;
        }
        if (mPoiBitmapDefault1 != null) {
            mPoiBitmapDefault1 = null;
            mPoiBitmapDefault2 = null;
            mPoiBitmapDefault3 = null;
            mPoiBitmapDefault4 = null;
            mPoiBitmapDefault5 = null;
            mPoiBitmapDefault6 = null;
            mPoiBitmapDefault7 = null;
            mTxtBitmapDefault = null;
        }
    }

    //城池点击回调
    public interface OnPointClickLisenter {
        void onPointClick(MapPoiDto mapPoiDto);
    }

    private OnPointClickLisenter onPointClickLisenter;

    public void setOnPointClickLisenter(OnPointClickLisenter onPointClickLisenter) {
        this.onPointClickLisenter = onPointClickLisenter;
    }
}
