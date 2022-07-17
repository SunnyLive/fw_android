package com.fengwo.module_comment.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fengwo.module_comment.R;


public class MenuItem extends LinearLayout {

    private ImageView ivIcons;
    private TextView tvName;
    private TextView tvRight;


    public MenuItem(Context context) {
        this(context, null);
    }

    public MenuItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        View v = LayoutInflater.from(context).inflate(R.layout.item_menuitem, this);
        ivIcons = v.findViewById(R.id.iv_icon);
        tvName = v.findViewById(R.id.tv_name);
        tvRight = v.findViewById(R.id.tv_right);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.menu_item);
        int img = ta.getResourceId(R.styleable.menu_item_menu_icon, -1);
        if (-1 == img) {
            ivIcons.setVisibility(GONE);
        } else {
            ivIcons.setImageResource(img);
        }
        int visibility = ta.getInteger(R.styleable.menu_item_menu_more_visibility, 1);
        if (visibility == 0){
            v.findViewById(R.id.iv_more).setVisibility(INVISIBLE);
        }
        String text = ta.getString(R.styleable.menu_item_menu_title);
        tvName.setText(text);
        String textRight = ta.getString(R.styleable.menu_item_menu_right_text);
        tvRight.setText(textRight);
        int rightColor = ta.getColor(R.styleable.menu_item_menu_right_text_color, getResources().getColor(R.color.text_99));
        tvRight.setTextColor(rightColor);
        ta.recycle();
    }

    public void setRightText(String msg) {
        tvRight.setText(msg);
    }
    public String getRightText() {
        return tvRight.getText().toString();
    }

    public void setRightTextColor(int color) {
        tvRight.setTextColor(color);
    }
}
