package com.fengwo.module_live_vedio.mvp.ui.df;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.trello.rxlifecycle2.components.support.RxDialogFragment;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/5
 */
public abstract class BaseDialogFragment extends RxDialogFragment {

    View view;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        //设置背景为透明
        window.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = getContextRect(getActivity());
        //设置弹窗大小为会屏
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        //去除阴影
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.0f;
        window.setAttributes(layoutParams);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.ThemeLivePush);
////        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MyDialog);
//        getDialog().getWindow().getAttributes().windowAnimations = R.style.Animation_Design_BottomSheetDialog;
//        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        //去出标题
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        applyCompat();
        view = inflater.inflate(getContentLayout(), null);
        initView();
        return view;
    }


    //获取内容区域
    private int getContextRect(Activity activity){
        //应用区域
        Rect outRect1 = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        return outRect1.height();
    }


    @Override
    public void onStart() {
        super.onStart();
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = getOrientation();
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = getWidth();
        params.height = getHeight();
        win.setAttributes(params);
//        win.setLayout(getWidth(), getHeight());

        getDialog().setCancelable(cancelable());
        getDialog().setCanceledOnTouchOutside(cancelable());
    }

    protected int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    protected int getWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    protected boolean cancelable() {
        return true;
    }

    protected abstract void initView();

    protected abstract int getContentLayout();

    public int getOrientation() {
        return Gravity.BOTTOM;
    }

    /**
     * 重写 findViewById
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T findViewById(int id) {
        if (view != null && id != 0) {
            return view.findViewById(id);
        }
        return null;
    }

    public void showDF(FragmentManager fragmentManager, String tag){
        show(fragmentManager,tag);
        fragmentManager.executePendingTransactions();
    }
}
