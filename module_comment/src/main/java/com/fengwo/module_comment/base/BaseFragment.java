package com.fengwo.module_comment.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.utils.DarkUtil;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.LoadingDialog;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.components.SimpleImmersionOwner;
import com.gyf.immersionbar.components.SimpleImmersionProxy;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

import static com.fengwo.module_comment.utils.Utils.runOnUiThread;


/**
 * Created by Administrator on 2016-08-17.
 */
public abstract class BaseFragment extends RxFragment implements MvpView, SimpleImmersionOwner {

    //    protected FragmentComponent component;
    private Unbinder unbinder;
    public LoadingDialog loadingDialog;
    private View baseTitle;

    protected View toolbar;
    protected View statusBarView;
    private boolean isFirst = true;
    protected CompositeDisposable compositeDisposable;

    public void hideToolBar() {
        if (null != toolbar && statusBarView != null) {
            toolbar.setVisibility(View.GONE);
            statusBarView.setVisibility(View.GONE);
        }
    }

    public void showLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog == null) {
                    loadingDialog = new LoadingDialog(getActivity());
                }
                loadingDialog.show();
                loadingDialog.setProgressPercent("");
            }
        });

    }

    public boolean isLoadingDialogShow() {
        if (loadingDialog != null && loadingDialog.isShowing())
            return true;
        return false;
    }

    public void hideLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isLoadingDialogShow()) {
                    loadingDialog.setProgressPercent("");
                    loadingDialog.dismiss();
                }
            }
        });

    }

    @Override
    public void setDialogProgressPercent(String percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != loadingDialog)
                    loadingDialog.setProgressPercent(percent);
            }
        });

    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true).init();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(setContentView(), container, false);
        return v;
    }

    public void initView(View v) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean immersionBarEnabled() {
        return true;
    }

    public void setTitleBackground(int color) {
        ColorDrawable d = new ColorDrawable(color);
        statusBarView.setBackground(d);
        toolbar.setBackground(d);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        ARouter.getInstance().inject(this);
        if (getImmersionBar()) {
            ImmersionBar.with(this)
                    .statusBarDarkFont(!DarkUtil.isDarkTheme(getActivity()), 0.2f)
                    .navigationBarDarkIcon(true, 0.2f)
                    .navigationBarColor(R.color.white)
                    .init();
        }
    }

    protected boolean getImmersionBar() {
        return true;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        unbinder = ButterKnife.bind(this, v);
        baseTitle = v.findViewById(R.id.basetitle);
        statusBarView = v.findViewById(R.id.status_bar_view);
        toolbar = v.findViewById(R.id.tool_bar);
        initView(v);
        if (isFirst)
            initUI(savedInstanceState);
        isFirst = false;
        if (null != baseTitle && getImmersionBar())
            fitsLayoutOverlap();

    }

    private static final int MIN_CLICK_DELAY_TIME = 400;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) <= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    private void fitsLayoutOverlap() {
        if (statusBarView != null) {
            ImmersionBar.setStatusBarView(this, statusBarView);
        } else {
            ImmersionBar.setTitleBar(this, toolbar);
        }
    }

    @Override
    public void netError() {
        ToastUtils.showShort(getActivity(), "网络出错，请稍后重试！");
    }

    protected abstract int setContentView();

//    public abstract void initInjector();
//
//
//    /**
//     * 得到Activity传进来的值
//     *
//     * @param bundle
//     */
//    public void getBundle(Bundle bundle) {
//    }

    /**
     * 初始化控件
     *
     * @param savedInstanceState
     */
    public abstract void initUI(Bundle savedInstanceState);


    @Override
    public void toastTip(int msgId) {
        ToastUtils.showShort(getActivity(), msgId);
    }

    @Override
    public void toastTip(CharSequence msg) {
        ToastUtils.showShort(getActivity(), msg);
    }

    public void jump() {
    }


    public void startActivity(Class<?> targetClass) {
        Intent intent = new Intent(getActivity(), targetClass);
        startActivity(intent);
    }

    public void startActivity(Class<?> targetClass, String name, String value) {
        Intent intent = new Intent(getActivity(), targetClass);
        intent.putExtra(name, value);
        startActivity(intent);
    }

    /**
     * ImmersionBar代理类
     */
    private SimpleImmersionProxy mSimpleImmersionProxy = new SimpleImmersionProxy(this);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mSimpleImmersionProxy.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSimpleImmersionProxy.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mSimpleImmersionProxy.onHiddenChanged(hidden);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mSimpleImmersionProxy.onConfigurationChanged(newConfig);
    }


    public class ToolBarBuilder {
        private ImageView backIv;
        private TextView titleTv;
        private ImageView right1Iv;
        private ImageView right2Iv;
        private TextView rightTv;

        private String title;
        private int titleColor;
        private int right1Res = -1, right2Res = -1;
        private View.OnClickListener right1Listener, right2Listener, rightTextListener;
        private String rightText;

        public ToolBarBuilder() {
            if (null == baseTitle) {
                toastTip("没有 include base title");
                throw new RuntimeException("没有 include base title");
            } else {
//                backIv = baseTitle.findViewById(R.id.left_img);
//                backIv.setOnClickListener(v->{
//
//                });
                titleTv = baseTitle.findViewById(R.id.title_tv);
                right1Iv = baseTitle.findViewById(R.id.right1_img);
                right2Iv = baseTitle.findViewById(R.id.right2_img);
                rightTv = baseTitle.findViewById(R.id.right_tv);

            }
        }

        public ToolBarBuilder showBack() {

            return this;
        }

        public ToolBarBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public ToolBarBuilder setTitleColor(int color) {
            this.titleColor = color;
            return this;
        }

        public ToolBarBuilder setRight1Img(int res, View.OnClickListener l) {
            right1Res = res;
            right1Listener = l;
            return this;
        }

        public ToolBarBuilder setRight2Img(int res, View.OnClickListener l) {
            right2Res = res;
            right2Listener = l;
            return this;
        }

        public ToolBarBuilder setRightText(String res, View.OnClickListener l) {
            rightText = res;
            rightTextListener = l;
            return this;
        }

        public void build() {
            if (!TextUtils.isEmpty(title)) {
                titleTv.setText(title);
            }
            if (titleColor > 0) {
                titleTv.setTextColor(getResources().getColor(titleColor));
            }
            if (right1Res > 0) {
                right1Iv.setImageResource(right1Res);
                right1Iv.setOnClickListener(right1Listener);
            }
            if (right2Res > 0) {
                right2Iv.setImageResource(right2Res);
                right2Iv.setOnClickListener(right2Listener);
            }
            if (!TextUtils.isEmpty(rightText)) {
                rightTv.setText(rightText);
                rightTv.setOnClickListener(rightTextListener);
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSimpleImmersionProxy.onDestroy();
        unbinder.unbind();
        if(compositeDisposable!=null){
            compositeDisposable.isDisposed();
            compositeDisposable.clear();
        }
        isFirst = true;
    }
}
