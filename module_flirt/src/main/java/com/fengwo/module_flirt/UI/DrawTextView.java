package com.fengwo.module_flirt.UI;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.DimenRes;
import androidx.annotation.IntRange;
import androidx.appcompat.widget.AppCompatTextView;

import com.fengwo.module_flirt.R;

public class DrawTextView extends AppCompatTextView {
    public DrawTextView(Context context) {
        super(context);
    }

    public DrawTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable[] drawables = getCompoundDrawables();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawTextView);
        int drawDir = typedArray.getInt(R.styleable.DrawTextView_drawDir, 0);
        Drawable drawable = drawables[drawDir];
        if(drawable != null){
            int dw = typedArray.getDimensionPixelOffset(R.styleable.DrawTextView_drawW, -1);
            int dh = typedArray.getDimensionPixelOffset(R.styleable.DrawTextView_drawH, -1);
            if(dw > 0 && dh > 0){
                drawable.setBounds(0,0,dw,dh);
            }
            drawables[drawDir] = drawable;
        }
        setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
        typedArray.recycle();
    }



    public void setStartDrawAble(int dwId, @DimenRes int w, @DimenRes int h){
        setDrawAble(0,dwId,w,h);
    }

    public void setTopDrawAble(int dwId, @DimenRes int w, @DimenRes int h){
        setDrawAble(1,dwId,w,h);
    }

    public void setEndDrawAble(int dwId, @DimenRes int w, @DimenRes int h){
        setDrawAble(2,dwId,w,h);
    }

    public void setBottomDrawAble(int dwId, @DimenRes int w, @DimenRes int h){
        setDrawAble(3,dwId,w,h);
    }

    private void setDrawAble(@IntRange(from = 0,to = 3) int dwDir, int dwId, @DimenRes int w, @DimenRes int h){
        try {
            Drawable drawable = getResources().getDrawable(dwId);
            drawable.setBounds(0,0,getResources().getDimensionPixelOffset(w),getResources().getDimensionPixelOffset(h));
            Drawable[] drawables = getCompoundDrawables();
            drawables[dwDir] = drawable;
            setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
        }catch (Resources.NotFoundException exp){
            Log.e("","your drawable id:["+dwId+"] was not found drawable");
        }
    }






}
