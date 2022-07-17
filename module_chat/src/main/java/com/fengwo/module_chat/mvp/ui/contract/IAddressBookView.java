package com.fengwo.module_chat.mvp.ui.contract;

import com.fengwo.module_chat.mvp.model.bean.ContactBean;
import com.fengwo.module_comment.base.MvpView;

import java.util.List;

public interface IAddressBookView extends MvpView {

    void setContactListData(List<ContactBean> list);
}
