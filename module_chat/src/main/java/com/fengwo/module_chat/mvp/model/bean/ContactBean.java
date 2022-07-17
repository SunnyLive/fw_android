package com.fengwo.module_chat.mvp.model.bean;

import com.fengwo.module_chat.widgets.indexbar.bean.BaseIndexPinyinBean;

public class ContactBean extends BaseIndexPinyinBean {
    public String createTime;
    public String fromId;
    public String remark;
    public String userId;
    public String userName;
    public String userUrl;

    @Override
    public String getTarget() {
        return userName;
    }
}
