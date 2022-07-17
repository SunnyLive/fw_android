package com.fengwo.module_chat.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.BeanContact;
import com.fengwo.module_chat.mvp.model.bean.ContactBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/16
 */
public class ChatAddressBookAdapter extends BaseQuickAdapter<ContactBean, BaseViewHolder> {


    public ChatAddressBookAdapter(@Nullable List<ContactBean> data) {
        super(R.layout.chat_item_address_book,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ContactBean item) {
        helper.setText(R.id.tv_name,item.userName);
    }
}
