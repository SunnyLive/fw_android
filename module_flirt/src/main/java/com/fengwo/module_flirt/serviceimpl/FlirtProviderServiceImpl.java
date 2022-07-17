package com.fengwo.module_flirt.serviceimpl;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.iservice.FlirtProviderService;
import com.fengwo.module_flirt.adapter.NearbyImageAdapter;

@Route(path = ArouterApi.GREET_FLIRT_SERVICE, name = "获取i撩服务")
public class FlirtProviderServiceImpl implements FlirtProviderService {
    @Override
    public BaseQuickAdapter getNearImageAdapter(int rvWidth, int gap) {
        return new NearbyImageAdapter(rvWidth, gap);
    }

    @Override
    public void init(Context context) {

    }
}
