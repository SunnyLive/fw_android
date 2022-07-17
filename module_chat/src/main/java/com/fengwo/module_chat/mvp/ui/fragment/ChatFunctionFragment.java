package com.fengwo.module_chat.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_comment.utils.chat.CommonFragmentPageAdapter;
import com.fengwo.module_comment.utils.chat.IndicatorView;
import com.fengwo.module_comment.base.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * ================================================
 * 作    者：Fuzp
 * 版    本：1.0
 * 创建日期：2018/11/15.
 * 描    述：聊天其他功能片段
 * 修订历史：
 * ================================================
 */
@SuppressLint("ValidFragment")
public class ChatFunctionFragment extends BaseFragment {

    private View rootView;
    @BindView(R2.id.vp_function)
    ViewPager vpFunction;
    @BindView(R2.id.idv_function)
    IndicatorView idvFunction;
    private CommonFragmentPageAdapter functionPagerAdapter;
    private ArrayList<Fragment> fragments;
    private FunctionFirstFragment.FunctionFirstListener mFirstListener;

    public ChatFunctionFragment(){
    }

    public void setListener(FunctionFirstFragment.FunctionFirstListener firstListener){
        this.mFirstListener = firstListener;
    }
    private void initWidget() {
        vpFunction.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int oldPagerPos = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //更新指示器
                idvFunction.playByStartPointToNext(oldPagerPos, position);
                oldPagerPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        fragments = new ArrayList<>();
        FunctionFirstFragment firstFragment = (FunctionFirstFragment) FunctionFirstFragment.FunctionFirstFragment();
        firstFragment.setLitener(mFirstListener);
        fragments.add(firstFragment);
        functionPagerAdapter = new CommonFragmentPageAdapter(getActivity().getSupportFragmentManager(), fragments);
        vpFunction.setAdapter(functionPagerAdapter);
        //初始化指示器
        idvFunction.initIndicator(fragments.size());
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_chat_function;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        initWidget();
    }

    @Override
    public void tokenIInvalid() {
    }
}
