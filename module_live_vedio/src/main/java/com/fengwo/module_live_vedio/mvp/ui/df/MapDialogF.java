package com.fengwo.module_live_vedio.mvp.ui.df;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.Transition;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.MapPoiDto;
import com.fengwo.module_live_vedio.mvp.ui.fragment.MapFragment;
import com.fengwo.module_live_vedio.widget.mapview.MapView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/19
 */
public class MapDialogF extends DialogFragment {

    FrameLayout flMap;
    MapView mapView;
    View view;

    public static DialogFragment newInstance(){
        MapDialogF mapDialogF = new MapDialogF();
        return mapDialogF;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        //去出标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(getContentLayout(), null);
        initView();
        return view;
    }

    protected void initView() {
        flMap = view.findViewById(R.id.fl_map);
        MapFragment mapFragment = (MapFragment) MapFragment.newInstance();
        FragmentTransaction transition = getChildFragmentManager().beginTransaction();
        transition.add(R.id.fl_map,mapFragment,mapFragment.getClass().getName());
        transition.commitAllowingStateLoss();
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
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ScreenUtils.getScreenWidth(getActivity());
        params.height = ScreenUtils.getScreenHeight(getActivity())*2/3;
        win.setAttributes(params);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
    }

    protected int getContentLayout() {
        return R.layout.dialog_fragment_map;
    }

    public void showDF(FragmentManager fragmentManager, String Tag){
        show(fragmentManager,Tag);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment fileFragment=(MapFragment)getFragmentManager().findFragmentByTag(MapFragment.class.getName());
        if (fileFragment!=null)
            getFragmentManager().beginTransaction().remove(fileFragment).commit();
    }
}
