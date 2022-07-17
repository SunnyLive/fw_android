package com.fengwo.module_flirt.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.fengwo.module_flirt.R;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.Indicator;

public class BannerIndicator extends View implements Indicator {
    public BannerIndicator(Context context) {
        this(context,null);
    }

    public BannerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mIndicatorW = context.getResources().getDimension(R.dimen.x20);
        mIndicatorH = context.getResources().getDimension(R.dimen.y6);
        mDistance = context.getResources().getDimension(R.dimen.x12);
        mSelectedItemW = mIndicatorW;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(R.color.alpha20_black));

        mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setColor(getResources().getColor(R.color.color_FF529A));

    }

    private String TAG = getClass().getSimpleName();

    private float mIndicatorW;
    private float mIndicatorH;

    private float mDistance;

    private Paint mPaint;

    private Paint mSelectedPaint;



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        translateCanvas2Center(canvas);
        drawIndicator(canvas);
        canvas.restore();
    }

    private void drawIndicator(Canvas canvas) {
        for (int i =0; i< mItemCount; i++){
            if(i == mCurrentIndex){
                drawSelectedItem(canvas,mSelectedPaint);
            }else{
                drawUnSelectedItem(canvas,mPaint);
            }
        }
    }

    private void translateCanvas2Center(Canvas canvas) {
        canvas.translate((getWidth() - ((mItemCount-1)*(mIndicatorH+mDistance) + mSelectedItemW))/2f,(getHeight() - getPaddingBottom() -mIndicatorH)/2f);
    }

    private void drawUnSelectedItem(Canvas canvas, Paint paint) {
        canvas.drawCircle(0f,mIndicatorH/2f,mIndicatorH/2f,paint);
        canvas.translate((mDistance+mIndicatorH),0f);
    }

    private float mSelectedItemW;

    private void drawSelectedItem(Canvas canvas, Paint paint) {
        canvas.drawRoundRect(new RectF(0f,0f,mSelectedItemW,mIndicatorH),mIndicatorH/2f,mIndicatorH/2f,paint);
        canvas.translate((mDistance+mSelectedItemW),0f);
    }


    private int mItemCount = 0;

    private int mCurrentIndex = 0;

    public void attachViewPager(ViewPager vpg) {
        vpg.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentIndex = position;
                postInvalidate();
            }
        });
    }

    public void attachViewPager2(ViewPager2 vpg) {
        vpg.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentIndex = position-1;
                postInvalidate();
            }
        });
    }

    public void updatePageCount(int pageSize){
        mItemCount = pageSize;
        postInvalidate();
    }

    public void selectPage(int position) {
        if (mCurrentIndex == position) return;
        mCurrentIndex = position;
        postInvalidate();
    }


    @NonNull
    @Override
    public View getIndicatorView() {
        return null;
    }

    @Override
    public IndicatorConfig getIndicatorConfig() {
        IndicatorConfig indicatorConfig = new IndicatorConfig();
        indicatorConfig.setAttachToBanner(false);
        return indicatorConfig;
    }

    @Override
    public void onPageChanged(int count, int currentPosition) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentIndex = position;
        postInvalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
