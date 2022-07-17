package com.fengwo.module_comment.utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 倒计时 工具类
 */
public class CountBackUtilsNew {


    public Disposable disposable;
    private long time;
    private boolean isTiming = false;

    public void countBack(long time, Callback back) {
        this.time = time;
        isTiming = false;
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (CountBackUtilsNew.this.time < 0) {
                            back.countBacking(CountBackUtilsNew.this.time);
                        } else {
                            --CountBackUtilsNew.this.time;
                            isTiming = true;
                            if (CountBackUtilsNew.this.time <= 0) {
                                isTiming = false;
                                disposable.dispose();
                                disposable = null;
                                back.finish();
                                return;
                            }
                            back.countBacking(CountBackUtilsNew.this.time);
                        }
                    }
                });
    }

    public void countBack(int time,int interval, Callback back) {
        this.time = time;
        isTiming = false;
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
        disposable = Observable.interval(interval, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (CountBackUtilsNew.this.time < 0) {
                            back.countBacking(CountBackUtilsNew.this.time);
                        } else {
                            --CountBackUtilsNew.this.time;
                            isTiming = true;
                            if (CountBackUtilsNew.this.time <= 0) {
                                isTiming = false;
                                disposable.dispose();
                                disposable = null;
                                back.finish();
                                return;
                            }
                            back.countBacking(CountBackUtilsNew.this.time);
                        }
                    }
                });
    }

    public boolean isTiming() {
        return isTiming;
    }

    public void updateTime(int time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public interface Callback {
        void countBacking(long time);

        void finish();
    }

    public void destory() {
        if (null != disposable && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }
}
