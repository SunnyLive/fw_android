package com.fengwo.module_comment.base;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.ToastUtils;

import java.lang.ref.WeakReference;

import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.HttpException;

public abstract class LoadingObserver<T> extends ResourceSubscriber<T> {

    private boolean needLoading = true;//是否需要显示loading
    private boolean needShowAllError = false;//是否需要捕获所有异常
    private MvpView mvpView;
    private Context mContext;

    public LoadingObserver() {
    }

    public LoadingObserver(boolean needLoading) {
        this(needLoading, false, null);
    }

    public LoadingObserver(MvpView view) {
        this(true, false, view);
    }

    public LoadingObserver(boolean needLoading, MvpView view) {
        this(needLoading, false, view);
    }

    public LoadingObserver(boolean needLoading,boolean needShowAllError, MvpView view) {
        this.needLoading = needLoading;
        this.mvpView = view;
        this.needShowAllError = needShowAllError;
    }

    public abstract void _onNext(T data);

    public abstract void _onError(String msg);

    public void realError(){}

    //
    @Override
    public void onStart() {
        Context c;
        if (null != mvpView) {
            if (mvpView instanceof Fragment) {
                c = ((Fragment) mvpView).getActivity();
            } else {
                c = (Context) mvpView;
            }
            if (!CommentUtils.isNetworkConnected(c)) {
                mvpView.toastTip("暂无网络，请连接网络后重试！");
                mvpView.netError();
                this.dispose();
                return;
            }
            if (needLoading) {
                mvpView.showLoadingDialog();
            }
        }
        super.onStart();
    }


    @Override
    public void onNext(T data) {
        if (mvpView != null) {
            mvpView.hideLoadingDialog();
        }
        _onNext(data);
    }


    @Override
    public void onError(Throwable e) {
        realError();
        if (mvpView != null) {
            mvpView.hideLoadingDialog();
        }

        if (!CommentUtils.isNetworkConnected(BaseApplication.mApp)) {
            ToastUtils.showShort(BaseApplication.mApp, "请检查您的网络连接是否正常!");
            this.dispose();
            return;
        }
        if (e instanceof HttpException) {
            _onError("服务器繁忙，请稍后再试！");
            return;
        }
        if(!needShowAllError){
            if (e instanceof NullPointerException) {
                return;
            }

            if ("请重新登录".equals(e.getMessage())) {
          //      ToastUtils.showShort(BaseApplication.mApp, "请重新登录!");
                return;
            }
        }
        _onError(e.getMessage());
    }

    @Override
    public void onComplete() {
        if (mvpView != null && mvpView.isLoadingDialogShow()) {
            mvpView.hideLoadingDialog();
        }
    }
}
