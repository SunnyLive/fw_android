package com.fengwo.module_comment.iservice;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.IsAttentionDto;

import io.reactivex.disposables.Disposable;

public interface AttentionService extends IProvider {
    public void addAttention(int id, LoadingObserver<HttpResult> observer);

    public void delAttention(int id, LoadingObserver<HttpResult> observer);

    public Disposable isAttention(int id, LoadingObserver<HttpResult<IsAttentionDto>> observer);
}
