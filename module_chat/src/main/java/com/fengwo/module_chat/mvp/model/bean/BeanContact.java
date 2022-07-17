package com.fengwo.module_chat.mvp.model.bean;

import com.fengwo.module_chat.widgets.indexbar.bean.BaseIndexPinyinBean;

/**
 * 联系人
 * @author chenshanghui
 * @intro
 * @date 2019/9/16
 */
public class BeanContact extends BaseIndexPinyinBean {

    private String name;

    private String avatar;

    private boolean isTop;//是否是最上面的 不需要被转化成拼音的
    @Override
    public String getTarget() {
        return name;
    }

    public String getName() {
        return name;
    }

    public BeanContact setName(String name) {
        this.name = name;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public BeanContact setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public boolean isTop() {
        return isTop;
    }

    public BeanContact setTop(boolean top) {
        isTop = top;
        return this;
    }

    @Override
    public boolean isNeedToPinyin() {
        return !isTop;
    }


    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }
}
