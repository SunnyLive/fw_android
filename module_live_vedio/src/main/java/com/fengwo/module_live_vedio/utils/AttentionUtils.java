package com.fengwo.module_live_vedio.utils;

import com.fengwo.module_comment.base.BaseHttpData;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.IsAttentionDto;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_live_vedio.api.LiveApiService;

import io.reactivex.disposables.Disposable;

public class AttentionUtils {

    public static void addAttention(int uid, LoadingObserver<HttpResult> observer) {
        new RetrofitUtils().createApi(LiveApiService.class)
                .addAttention(uid + "")
                .compose(RxUtils.applySchedulers())
                .subscribe(observer);
    }

    public static void delAttention(int uid, LoadingObserver<HttpResult> observer) {
        new RetrofitUtils().createApi(LiveApiService.class)
                .removeAttention(uid + "")
                .compose(RxUtils.applySchedulers())
                .subscribe(observer);
    }

    public static Disposable isAttention(int uid, LoadingObserver<HttpResult<IsAttentionDto>> observer) {
        return new RetrofitUtils().createApi(LiveApiService.class)
                .isAttention(uid)
                .compose(RxUtils.applySchedulers())
                .subscribeWith(observer);
    }
}
