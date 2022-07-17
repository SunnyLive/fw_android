package com.fengwo.module_comment.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fengwo.module_comment.R;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.OnKeyboardListener;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

/**
 * 标题栏
 *
 * @author chenshanghui
 * @intro
 * @date 2019/7/29
 */
public class AppTitleBar extends ConstraintLayout {

    private TextView tvTitle;
    private TextView tvMore;
    private ImageView btnBack;

    public AppTitleBar(Context context) {
        this(context, null);
    }

    public AppTitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.AppTitleBar);
        initView(ta);
    }


    private void initView(TypedArray ta) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_title_bar, this);
        String title = ta.getString(R.styleable.AppTitleBar_atb_title);
        int titleColor = ta.getColor(R.styleable.AppTitleBar_atb_title_color, ContextCompat.getColor(getContext(), R.color.text_white));
        String moreText = ta.getString(R.styleable.AppTitleBar_atb_more_text);
        Drawable backIcon = ta.getDrawable(R.styleable.AppTitleBar_atb_backIcon);
        int bgColor = ta.getColor(R.styleable.AppTitleBar_atb_backgroundColor, R.color.white);
        Drawable moreIcon = ta.getDrawable(R.styleable.AppTitleBar_atb_moreIcon);
        Drawable gradientDrawble = ta.getDrawable(R.styleable.AppTitleBar_atb_backgroundGradient);
        boolean backVisibility = ta.getBoolean(R.styleable.AppTitleBar_atb_backVisibility, true);
        boolean moreVisibility = ta.getBoolean(R.styleable.AppTitleBar_atb_moreVisibility, true);
        boolean transparentStatusBar = ta.getBoolean(R.styleable.AppTitleBar_atb_alpha_status_bar, false);
        /*transparentStatusBar配合fitsSystemWindows属性使用达到全屏图片导航栏不变*/
        int moreTextColor = ta.getColor(R.styleable.AppTitleBar_atb_more_text_color, ContextCompat.getColor(getContext(), R.color.text_white));
        tvTitle = view.findViewById(R.id.tv_title);
        btnBack = view.findViewById(R.id.btn_back);
        tvMore = view.findViewById(R.id.tv_more);
        View statusBar = view.findViewById(R.id.status_bar);
        setTitle(title);
        setTitleColor(titleColor);
        setBackIcon(backIcon);
        setMoreIcon(moreIcon);
        setMoreTextColor(moreTextColor);
        setMoreText(moreText);
        setBackVisible(backVisibility);
        setMoreVisible(moreVisibility);
        if (gradientDrawble != null) {
            setBackground(gradientDrawble);//渐变属性不为空则设置
            statusBar.setBackground(gradientDrawble);
        } else {
            setBackgroundColor(bgColor);
            statusBar.setBackgroundColor(bgColor);
        }
        if (this.getContext() instanceof Activity) {
            ImmersionBar immersionBar = ImmersionBar.with((Activity) this.getContext());
            //指定状态栏
            if (!transparentStatusBar) {       /*transparentStatusBar配合fitsSystemWindows属性使用达到全屏图片导航栏不变*/
                immersionBar.statusBarView(statusBar);

                if (gradientDrawble != null)//渐变属性不为空则设置
                    statusBar.setBackground(gradientDrawble);
                else
                    immersionBar.statusBarColorInt(bgColor);
            }
            immersionBar.navigationBarColorInt(Color.WHITE)
                    .statusBarDarkFont(true, 0.2f)
                    .navigationBarDarkIcon(true, 0.2f);
//                    .keyboardEnable(true)
//                    .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调
//                        @Override
//                        public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
//                            Timber.d("" + isPopup);  //isPopup为true，软键盘弹出，为false，软键盘关闭
//                        }
//                    })
            immersionBar.init();
        }
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).onBackPressed();
                }
            }
        });
        ta.recycle();

    }

    public void setBackClickListener(OnClickListener listener) {
        btnBack.setOnClickListener(listener);
    }

    public void setBackIcon(Drawable backIcon) {
        if (backIcon != null) {
            btnBack.setImageDrawable(backIcon);
        }
    }
    public void setBackIcon(int res) {
        setBackIcon(ContextCompat.getDrawable(getContext(),res));
    }

    public void setMoreIcon(@DrawableRes int res) {
        setMoreIcon(ContextCompat.getDrawable(getContext(), res));
    }

    public void setMoreIcon(Drawable moreIcon) {
        if (moreIcon != null) {
            moreIcon.setBounds(0, 0, moreIcon.getMinimumWidth(), moreIcon.getMinimumHeight());
            tvMore.setCompoundDrawables(null, null, moreIcon, null);
        }

    }

    public void setBackVisible(boolean backVisibility) {
        btnBack.setVisibility(backVisibility ? View.VISIBLE : View.GONE);
    }


    public void setMoreVisible(boolean mv) {
        tvMore.setVisibility(mv ? View.VISIBLE : View.GONE);
    }

    public void setMoreText(String moreText) {
        tvMore.setText(moreText);
    }

    public void setMoreText(SpannableStringBuilder spb) {
        tvMore.setText(spb);
    }

    public void setMoreTextColor(int moreTextColor) {
        tvMore.setTextColor(moreTextColor);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }


    public void setMoreClickListener(OnClickListener listener) {
        tvMore.setOnClickListener(listener);
    }

    public void setTitleColor(int titleColor) {
        tvTitle.setTextColor(titleColor);
    }


    public void setMoreUnderLine(boolean b) {
        if (b) tvMore.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

}
