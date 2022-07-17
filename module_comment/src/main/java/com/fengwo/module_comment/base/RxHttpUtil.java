package com.fengwo.module_comment.base;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.reactivestreams.Publisher;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RxHttpUtil {

    protected final static int PAGE_SIZE = 20;

    static Gson gson = new Gson();
    protected  static CompositeDisposable compositeDisposable = new CompositeDisposable();


    public static void addNet(Disposable d) {
        compositeDisposable.add(d);
    }


    public static void clearHttpRequest() {
        if (null != compositeDisposable && compositeDisposable.size() > 0) {
            compositeDisposable.clear();
        }
    }

    public static <T> FlowableTransformer<T, T> io_main() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<HttpResult<T>, T> handleResult() {
        return new FlowableTransformer<HttpResult<T>, T>() {
            @Override
            public Publisher<T> apply(Flowable<HttpResult<T>> upstream) {
                return upstream.flatMap(new Function<HttpResult<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(HttpResult<T> tHttpResult) throws Exception {
                        if (tHttpResult.isSuccess()) {
                            return createData(tHttpResult.data);
                        } else if (tHttpResult.isTokenInvalid()) {
//                            getView().tokenIInvalid();
                            return Flowable.error(new Throwable(tHttpResult.description));
                        } else if (tHttpResult.isServiceFailed() || !TextUtils.isEmpty(tHttpResult.description)) {
                            return Flowable.error(new Throwable(tHttpResult.description));
                        } else {
                            String des = "网络出错，请稍后重试！";
                            return Flowable.error(new Throwable(des));
                        }
                    }
                }).compose(io_main());
            }

        };
    }


    private static <T> Flowable<T> createData(final T t) {
        return Flowable.create(subscriber -> {
            try {
                subscriber.onNext(t);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }, BackpressureStrategy.ERROR);
    }

    public RequestBody createRequestBody(Map map) {
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        return requestBody;
    }

    public class ParamsBuilder {
        private Map<String, String> params = new HashMap<>();

        public ParamsBuilder put(String key, String value) {
            params.put(key, value);
            return this;
        }

        public RequestBody build() {
            return createRequestBody(params);
        }
    }

    public RequestBody createWenboRequestBody(Map map) {
        Map params = new HashMap();
        params.put("params", map);
        RequestBody requestBody = RequestBody.create(gson.toJson(params), MediaType.parse("application/json"));
        return requestBody;
    }

    public class WenboParamsBuilder {
        private Map<String, Object> params = new HashMap<>();

        public WenboParamsBuilder put(String key, Object value) {
            params.put(key, value);
            return this;
        }

        public RequestBody build() {
            return createWenboRequestBody(params);
        }
    }

}