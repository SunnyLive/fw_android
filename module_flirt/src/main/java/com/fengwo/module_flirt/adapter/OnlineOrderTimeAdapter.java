package com.fengwo.module_flirt.adapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.OrderListBean;

import java.math.BigDecimal;


/**
 * @Author BLCS
 * @Time 2020/4/1 16:18
 */
public class OnlineOrderTimeAdapter extends BaseQuickAdapter<OrderListBean, BaseViewHolder> {
    public OnlineOrderTimeAdapter() {
        super(R.layout.adapter_online_order);
    }
    public int adapterPosition = -1;
    @Override
    protected void convert(@NonNull BaseViewHolder holder, OrderListBean item) {
        holder.setText(R.id.tv_online_order_time, item.getTimeLost() + "分钟");
        ConstraintLayout llOrder = holder.getView(R.id.ll_order);
        holder.setText(R.id.tv_tprice, item.getTeimPrice()+"");
        llOrder.setSelected(false);
        holder.setVisible(R.id.iv_check,false);
        llOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (adapterPosition >=0 && adapterPosition != position) notifyItemChanged(adapterPosition);
                llOrder.setSelected(true);
                holder.setVisible(R.id.iv_check,true);
                adapterPosition = position;
               if (onClickTimeListener!=null) onClickTimeListener.click( item.getTeimPrice()+"",item.getId());
            }
        });
        //默认选中第一个
        if (adapterPosition==-1){
            llOrder.setSelected(true);
            holder.setVisible(R.id.iv_check,true);
            adapterPosition = holder.getAdapterPosition();
            if (onClickTimeListener!=null) onClickTimeListener.click( item.getTeimPrice()+"",item.getId());
        }
    }

    public OnClickTimeListener onClickTimeListener;

    public void addOnClickTimeListener(OnClickTimeListener linstener) {
        onClickTimeListener = linstener;
    }

    public interface OnClickTimeListener {
        void click(String price, int orderId);
    }
}
