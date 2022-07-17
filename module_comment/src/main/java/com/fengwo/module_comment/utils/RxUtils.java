package com.fengwo.module_comment.utils;

import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.HttpResult;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunhee
 * @intro
 * @date 2019/7/25
 */
public class RxUtils {

    public static <T> FlowableTransformer<T, T> applySchedulers2() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> FlowableTransformer<T, T> applySchedulers() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> FlowableTransformer<HttpResult<T>, T> handleResult2() {
        return new FlowableTransformer<HttpResult<T>, T>() {
            @Override
            public Publisher<T> apply(Flowable<HttpResult<T>> upstream) {
                return upstream.flatMap(new Function<HttpResult<T>, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(HttpResult<T> tHttpResult) throws Exception {
                        return new Publisher<T>() {
                            @Override
                            public void subscribe(Subscriber<? super T> s) {
                                if (tHttpResult.isSuccess()) {
                                    s.onNext(tHttpResult.data);
                                    s.onComplete();
                                } else {
                                    if (tHttpResult.isTokenInvalid()) {
                                        tokenIInvalid();
                                        s.onError(new Throwable(tHttpResult.description));
                                    }
                                }
                            }
                        };
                    }
                });
            }
        };
    }

    public static <T> ObservableTransformer<HttpResult<T>, T> handleResult() {
        return new ObservableTransformer<HttpResult<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<HttpResult<T>> upstream) {
                return upstream.flatMap(new Function<HttpResult<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(HttpResult<T> tHttpResult) throws Exception {
                        if (tHttpResult.isSuccess()) {
                            return createData(tHttpResult.data);
                        } else {
                            if (tHttpResult.isTokenInvalid()) {
                                tokenIInvalid();
                                return Observable.error(new Exception(tHttpResult.description));
                            } else {
                                return null;
                            }
                        }
                    }
                });
            }
        };
    }

    private static <T> Observable<T> createData(final T t) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(t);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    public static void tokenIInvalid() {

    }


}
