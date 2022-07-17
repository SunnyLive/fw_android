package com.fengwo.module_live_vedio.widget.giftlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Created by SEELE on 2016/10/6.
 */
@SuppressLint("AppCompatCustomView")
public class GiftCountTextView extends TextView {

    public GiftCountTextView(Context context) {
        super(context);
        init();
    }

    public GiftCountTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GiftCountTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/customfontstyle.otf"));
    }
}