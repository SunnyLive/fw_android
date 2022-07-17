package com.fengwo.module_comment.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.fengwo.module_comment.R;

import java.math.BigDecimal;

/**
 * @anchor Administrator
 * @date 2020/10/22
 */
public class DrawView extends View {

    private  int startColor,endColor;


    public DrawView(Context context) {
        super(context);

    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public int YIYOU = 100;
    public String msg1,msg2;

    public void setYIYOU(int YIYOU,String msg1,String msg2) {
        this.YIYOU = YIYOU;
        this.msg1 = msg1;
        this.msg2 = msg2;
        invalidate();
    }

//    public void iniview(int context){
//        YIYOU=context;
//
//        invalidate();
//    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        startColor = this.getContext().getColor(R.color.purple_EB985B);
        endColor = this.getContext().getColor(R.color.purple_FFC199);
        Double mDensity = Double.valueOf(getWidth());
        Double a =mDensity / 100;
        int jindu = (int) (a * YIYOU);

//        //设置画笔
       Paint paint = new Paint();
        LinearGradient linearGradient = new LinearGradient(0,getHeight(),0,0,startColor, endColor, Shader.TileMode.MIRROR);

        paint.setShader(linearGradient);
        canvas.drawRoundRect(new RectF(0, 0, jindu, getHeight()), 25, 25, paint);      //参数2,3 为圆角的xy的弧度半100322径




//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic_jdt);
//        BigDecimal aaaa = new BigDecimal(getWidth()+"").divide(new BigDecimal("100"));
//        BigDecimal bbb = aaaa.multiply(new BigDecimal(jindu+""));
//        int mBitWidth = bbb.intValue();
//        Rect mSrcRect = new Rect(0, 0, 0, getHeight());
//        Rect mDestRect = new Rect(0, 0, mBitWidth, getHeight());
//
//        canvas.drawBitmap(bitmap,mSrcRect,mDestRect,paint);




        Paint mTextPaint = new Paint();
        mTextPaint.setTextSize(getContext().getResources().getDimension(R.dimen.sp_10));
        mTextPaint.setColor(this.getContext().getColor(R.color.text_white_arr));
        int circleXY = (int) (getWidth() / 2);
        int circleRadius = (int) ((getWidth() * 0.5) / 2) + 20;
        String mText = msg1 +"/"+msg2;
        //计算文本宽度
        int textWidth = getTextWidth(mTextPaint,mText);
        int textX = (circleXY-textWidth)/2+circleRadius-20;

        canvas.drawText(mText, textX, 30, mTextPaint);
    }
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }
}