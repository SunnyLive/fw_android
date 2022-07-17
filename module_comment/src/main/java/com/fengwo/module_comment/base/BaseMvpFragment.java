package com.fengwo.module_comment.base;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.utils.ActivitysManager;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.SPUtils1;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseMvpFragment<V extends MvpView, P extends BasePresenter<V>> extends BaseFragment {

    protected P p;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        p = initPresenter();
        if (null != p)
            p.attachView((V) this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected abstract P initPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Fragment的销毁动作放到onDestroyView
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != p)
            p.detachView();
    }

    @Override
    public void tokenIInvalid() {
        ((BaseMvpActivity) getActivity()).tokenIInvalid();
    }

    public <T> FlowableTransformer<T, T> io_main() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

    public <T> FlowableTransformer<HttpResult<T>, T> handleResult() {
        return new FlowableTransformer<HttpResult<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<HttpResult<T>> upstream) {
                return upstream.flatMap(new Function<HttpResult<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(HttpResult<T> tHttpResult) throws Exception {
                        if (tHttpResult.isSuccess()) {
                            return createData(tHttpResult.data);
                        } else {
                            if (tHttpResult.isTokenInvalid()) {
                                tokenIInvalid();
                                return Flowable.error(new Throwable(tHttpResult.description));
                            } else {
                                return Flowable.error(new Throwable(tHttpResult.description));
                            }
                        }
                    }
                }).compose(io_main());
            }
        };
    }

    private <T> Flowable<T> createData(final T t) {
        return Flowable.create(subscriber -> {
            try {
                subscriber.onNext(t);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }, BackpressureStrategy.ERROR);
    }
}
