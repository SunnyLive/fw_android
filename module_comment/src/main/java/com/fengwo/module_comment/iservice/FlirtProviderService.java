package com.fengwo.module_comment.iservice;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.chad.library.adapter.base.BaseQuickAdapter;

public interface FlirtProviderService extends IProvider {
    BaseQuickAdapter getNearImageAdapter(int rvWidth, int gap);
}
