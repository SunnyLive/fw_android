package com.fengwo.module_login.mvp.ui.adapter;

import android.widget.CheckBox;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.ReportLabelDto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/1
 */
public class ReportLabelAdapter extends BaseQuickAdapter<ReportLabelDto, BaseViewHolder> {

    private int checkPosition = -1;

    public ReportLabelAdapter(@Nullable List<ReportLabelDto> data) {
        super(R.layout.item_report_label_list, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ReportLabelDto item) {
        helper.setText(R.id.tv_name, item.getName());
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ImageView imageView = holder.getView(R.id.cb_label);
        imageView.setSelected(checkPosition == position);
    }

    public void setCheck(int position) {
        checkPosition = position;
        notifyDataSetChanged();
    }

    public ReportLabelDto getCheckItem() {
        return getData().get(checkPosition);
    }
}
