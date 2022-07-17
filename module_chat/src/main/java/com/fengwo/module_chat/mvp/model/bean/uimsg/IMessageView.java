package com.fengwo.module_chat.mvp.model.bean.uimsg;

import android.view.View;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/18
 */
public interface IMessageView extends MultiItemEntity {

    View obtainMessageView(View parent);

}
