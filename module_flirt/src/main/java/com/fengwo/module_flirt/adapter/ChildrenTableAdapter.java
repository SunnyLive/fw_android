package com.fengwo.module_flirt.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.CerTagBean;

/**
 * @Author BLCS
 * @Time 2020/8/13 17:24
 */
public class ChildrenTableAdapter extends BaseQuickAdapter<CerTagBean.ChildrenBean,BaseViewHolder> {

    public ChildrenTableAdapter() {
        super(R.layout.adapter_childre_table);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, CerTagBean.ChildrenBean childrenBean) {
        viewHolder.setText(R.id.tv_childre_table,childrenBean.getTagNameX());
        TextView view = viewHolder.getView(R.id.tv_childre_table);
        view.setSelected(childrenBean.isChecked());
        view.setTextColor(childrenBean.isChecked()?ContextCompat.getColor(mContext,R.color.text_white):ContextCompat.getColor(mContext,R.color.color_9a9a));
    }


}
