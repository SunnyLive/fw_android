package com.fengwo.module_comment.base;

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

public abstract class BaseRxActivity extends BaseActivity {
    public <T> FlowableTransformer<T, T> io_main() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

    public <T> FlowableTransformer<HttpResult<T>, T> handleResult() {
        return new FlowableTransformer<HttpResult<T>, T>() {
            @Override
            public Publisher<T> apply(Flowable<HttpResult<T>> upstream) {
                return upstream.flatMap(new Function<HttpResult<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(HttpResult<T> tHttpResult) throws Exception {
                        if (tHttpResult.isSuccess()) {
                            return createData(tHttpResult.data);
                        } else if (tHttpResult.isTokenInvalid()) {
                            tokenIInvalid();
                            return Flowable.error(new Throwable(tHttpResult.description));
                        } else {
                            return Flowable.error(new Throwable(tHttpResult.description));
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
