package com.fengwo.module_live_vedio.serviceimpl;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.IsAttentionDto;
import com.fengwo.module_comment.iservice.AttentionService;
import com.fengwo.module_live_vedio.utils.AttentionUtils;

import io.reactivex.disposables.Disposable;

@Route(path = ArouterApi.ATTENTION_SERVICE, name = "关注服务")
public class AttentionServiceImpl implements AttentionService {
    @Override
    public void addAttention(int id, LoadingObserver<HttpResult> resultLoadingObserver) {
        AttentionUtils.addAttention(id, resultLoadingObserver);
    }

    @Override
    public void delAttention(int id, LoadingObserver<HttpResult> observer) {
        AttentionUtils.delAttention(id, observer);
    }

    @Override
    public Disposable isAttention(int id, LoadingObserver<HttpResult<IsAttentionDto>> observer) {
        return AttentionUtils.isAttention(id, observer);
    }

    @Override
    public void init(Context context) {

    }
}
