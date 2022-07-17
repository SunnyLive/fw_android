package com.fengwo.module_flirt.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.DoubleClickListener;
import com.fengwo.module_comment.utils.FastClickListener;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.AppointTimes;
import com.fengwo.module_flirt.bean.PeriodPrice;

import java.util.List;

/**
 * @Author BLCS
 * @Time 2020/4/1 15:46
 */
public class AppointmentTimeAdapter extends BaseMultiItemQuickAdapter<PeriodPrice, BaseViewHolder> {
    int pos = -1;

    public AppointmentTimeAdapter(List<PeriodPrice> data) {
        super(data);
        addItemType(1,R.layout.adapter_appointment_text);
        addItemType(0,R.layout.adapter_appointment_time);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, PeriodPrice item) {
        if(item.getItemType()==1){
            holder.setText(R.id.tv_appointment_text,item.getDay());
        }else{
            holder.setText(R.id.tv_periodTime, item.getPeriodTime());
            holder.setText(R.id.tv_tprice, String.valueOf(item.getTprice()));
            ConstraintLayout cl = holder.getView(R.id.cl_layout);
            //处理复用刷新问题
            cl.setSelected(pos == holder.getAdapterPosition() ? true : false);
            holder.setGone(R.id.iv_check, pos == holder.getAdapterPosition() ? true : false);
            cl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView check = holder.getView(R.id.iv_check);
                    if (check.getVisibility() == View.GONE) {
                        cl.setSelected(true);
                        holder.setGone(R.id.iv_check, true);
                        pos = holder.getAdapterPosition();
                    }

//                else {
//                    cl.setSelected(false);
//                    holder.setGone(R.id.iv_check, false);
//                    pos = -1;
//                }
                    notifyDataSetChanged();
                }
            });
            //判断是否可预约
            cl.setClickable(item.getIsAppointment() == 0 ? true : false);
            cl.setBackgroundResource(item.getIsAppointment() == 0 ? R.drawable.shape_appointment_time : R.drawable.shape_gray_radius);
            //添加默认选中 可选第一个
            if (pos==-1&&item.getIsAppointment() == 0){//若无选中时间段 则默认指定第一个
                pos = holder.getAdapterPosition();
                cl.setSelected(true);
                holder.setGone(R.id.iv_check, true);
            }
            if (pos == holder.getAdapterPosition()&&onUpdatePriceLinstener != null){
                onUpdatePriceLinstener.update(item.getTprice(), String.valueOf(item.getId()),item);
            }
        }
    }

    public OnUpdatePriceLinstener onUpdatePriceLinstener;

    public void addOnUpdatePrice(OnUpdatePriceLinstener linstener) {
        onUpdatePriceLinstener = linstener;
    }

    public interface OnUpdatePriceLinstener {
        void update(int price, String appointTimeIds,PeriodPrice period);
    }
}
