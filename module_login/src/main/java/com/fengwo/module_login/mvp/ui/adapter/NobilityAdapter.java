package com.fengwo.module_login.mvp.ui.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.NobilityDTO;

public class NobilityAdapter extends BaseQuickAdapter<NobilityDTO, BaseViewHolder> {

    private int selectPosition = 0;

    public NobilityAdapter() {
        super(R.layout.item_nobility);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NobilityDTO item) {
        helper.setText(R.id.tvTitle, String.format("%d天", item.dayNum))
                .setText(R.id.tvSubtitle, String.format("%d花钻", item.itemPrice));
        helper.itemView.setSelected(selectPosition == helper.getLayoutPosition());
        helper.addOnClickListener(R.id.rootView);
    }

    public void setSelectItem(int position) {
        selectPosition = position;
        notifyDataSetChanged();
    }

    public int getSelectPosition() {
        return selectPosition;
    }
}