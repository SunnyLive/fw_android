package com.fengwo.module_comment.base;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

public class BaseVH extends BaseViewHolder {
    public BaseVH(View view) {
        super(view);
        AutoUtils.autoSize(itemView);
    }
}
