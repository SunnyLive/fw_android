package com.fengwo.module_live_vedio.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_live_vedio.R;

public class BevelView extends AppCompatImageView {
    private static final int DIRECTION_UP_LEFT = 1;
    private static final int DIRECTION_DOWN_RIGHT = 2;
    private static final int DIRECTION_UP_RIGHT = 3;
    private static final int DIRECTION_DOWN_LEFT = 4;

    private float bevelPercent;
    private int roundRadius;
    private int direction;

    private final int defaultSize;
    private final Path path = new Path();
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BevelView(Context context) {
        this(context, null);
    }

    public BevelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BevelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultSize = DensityUtils.dp2px(getContext(), 200);
        borderPaint.setStyle(Paint.Style.STROKE);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BevelView);
        int borderWidth = ta.getDimensionPixelSize(R.styleable.BevelView_bevel_border_width, 0);
        bevelPercent = ta.getFloat(R.styleable.BevelView_bevel_bevel_percent, 0F);
        roundRadius = ta.getDimensionPixelSize(R.styleable.BevelView_bevel_round_radius, 0);
        int borderColor = ta.getColor(
                R.styleable.BevelView_bevel_border_color,
                getResources().getColor(android.R.color.transparent)
        );
        direction = ta.getInt(R.styleable.BevelView_bevel_direction, DIRECTION_UP_LEFT);
        ta.recycle();

        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if ((widthMode & heightMode) != MeasureSpec.EXACTLY) {
            if (widthMode == MeasureSpec.EXACTLY) { // 只有宽度是精确值
                heightSize = (int) (widthSize * (1 - bevelPercent / 2));
            } else if (heightMode == MeasureSpec.EXACTLY) { // 只有高度是精确值
                widthSize = (int) (heightSize * (1 + bevelPercent / 2));
            } else {// 长度高度都没有精确值
                heightSize = defaultSize;
                widthSize = (int) (defaultSize + (1 + bevelPercent / 2));
            }
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        path.reset();
        switch (direction) {
            case DIRECTION_UP_LEFT:
            case DIRECTION_DOWN_LEFT:
                path.moveTo(roundRadius, 0F);
                path.lineTo(
                        direction == DIRECTION_UP_LEFT ? getWidth() * (1 - bevelPercent) : getWidth(),
                        0F
                );
                path.lineTo(
                        direction == DIRECTION_UP_LEFT ? getWidth() : getWidth() * (1 - bevelPercent),
                        getHeight()
                );
                path.lineTo(roundRadius, getHeight());
                path.quadTo(0F, getHeight(), 0F, getHeight() - roundRadius);
                path.lineTo(0F, roundRadius);
                path.quadTo(0F, 0F, roundRadius, 0F);
                break;
            case DIRECTION_UP_RIGHT:
            case DIRECTION_DOWN_RIGHT:
                path.moveTo(getWidth(), roundRadius);
                path.lineTo(getWidth(), getHeight() - roundRadius);
                path.quadTo(getWidth(), getHeight(), getWidth() - roundRadius, getHeight());
                path.lineTo(direction == DIRECTION_UP_RIGHT ? 0 : getWidth() * bevelPercent, getHeight());
                path.lineTo(direction == DIRECTION_UP_RIGHT ? getWidth() * bevelPercent : 0, 0F);
                path.lineTo(getWidth() - roundRadius, 0F);
                path.quadTo(getWidth(), 0F, getWidth(), roundRadius);
                break;
        }
        canvas.clipPath(path);
        super.onDraw(canvas);
        canvas.drawPath(path, borderPaint);
    }
}
