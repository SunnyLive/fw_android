package com.fengwo.module_chat.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_comment.base.BaseFragment;

import androidx.fragment.app.Fragment;
import butterknife.OnClick;


/**
 * ================================================
 * 作    者：Fuzp
 * 版    本：1.0
 * 创建日期：2018/11/16.
 * 描    述：聊天其他功能片段 - 第一页
 * 修订历史：
 * ================================================
 */
public class FunctionFirstFragment extends BaseFragment {
    private View rootView;
    private FunctionFirstListener mFirstListener = null;


    public static Fragment FunctionFirstFragment() {
        FunctionFirstFragment functionFirstFragment = new FunctionFirstFragment();
        return functionFirstFragment;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_function_first;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
    }

    @Override
    public void tokenIInvalid() {
    }

    @OnClick({R2.id.layout_fun_choose, R2.id.layout_fun_shoot})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.layout_fun_choose) {
            if (mFirstListener != null)
                mFirstListener.onChoosePress();
        } else if (id == R.id.layout_fun_shoot) {
            if (mFirstListener != null)
                mFirstListener.onShootPress();
        }
    }

    public void setLitener(FunctionFirstListener mFirstListener) {
        this.mFirstListener = mFirstListener;
    }

    public interface FunctionFirstListener {

        /**
         * 点击相册按钮时调用
         */
        void onChoosePress();

        /**
         * 点击拍摄按钮时调用
         */
        void onShootPress();
    }
}
