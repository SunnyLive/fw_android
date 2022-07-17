package com.fengwo.module_flirt.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;


import androidx.appcompat.widget.LinearLayoutCompat;

import com.fengwo.module_flirt.R;

public class CustomRatingBar extends LinearLayoutCompat {

    private ImageView[] mImageViews = new ImageView[5];

    public CustomRatingBar(Context context) {
        super(context);
        init();
    }

    public CustomRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

   private void init() {
       View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_imageview, null);
       this.addView(view);
       mImageViews[0] = view.findViewById(R.id.image1);
       mImageViews[1] = view.findViewById(R.id.image2);
       mImageViews[2] = view.findViewById(R.id.image3);
       mImageViews[3] = view.findViewById(R.id.image4);
       mImageViews[4] = view.findViewById(R.id.image5);
   }

   public void showImage(int number) {
        hideView();

        for (int i = 0; i < number; i++) {
            mImageViews[i].setImageResource(R.mipmap.ic_gradle_small);
            mImageViews[i].setVisibility(View.VISIBLE);
        }
   }

   private void hideView() {
        for (ImageView imageView : mImageViews) {
            imageView.setVisibility(GONE);
        }
   }


}
