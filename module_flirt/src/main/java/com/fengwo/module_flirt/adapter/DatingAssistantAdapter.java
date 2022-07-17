package com.fengwo.module_flirt.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.AppointmentListBean;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * @Author BLCS
 * @Time 2020/4/1 16:56
 */
public class DatingAssistantAdapter extends BaseQuickAdapter<AppointmentListBean, BaseViewHolder> {
    public DatingAssistantAdapter() {
        super(R.layout.adapter_dating_assistant);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder mHolder, AppointmentListBean bean) {
        TextView reason = mHolder.getView(R.id.tv_reason);
        TextView tvOrder = mHolder.getView(R.id.tv_order);
        TextView tvRefuse = mHolder.getView(R.id.tv_refuse);
        TextView tvDatingTime = mHolder.getView(R.id.tv_dating_time);
        if (TextUtils.isEmpty(bean.getAppointmentTime())) {
            tvDatingTime.setVisibility(View.GONE);
        } else {
            tvDatingTime.setText( bean.getAppointmentTime());
        }
        CircleImageView civHeader = mHolder.getView(R.id.civ_header);
        ImageLoader.loadCircleImg(civHeader, bean.getSenderHeadImg());
        mHolder.setText(R.id.tv_name, bean.getSenderNickname());
        mHolder.setText(R.id.tv_time, TimeUtils.longToString(bean.getCreateTime()));
        mHolder.setText(R.id.tv_content, bean.getContent());
        reason.setVisibility(View.GONE);
        tvRefuse.setVisibility(View.GONE);
        tvOrder.setVisibility(View.GONE);
        tvDatingTime.setVisibility(View.VISIBLE);
        mHolder.addOnClickListener(R.id.tv_order, R.id.tv_refuse, R.id.civ_header);
        L.e("====="+bean.getStatus());
        tvOrder.setSelected(bean.getStatus() == 0 ?  false: true);
        tvOrder.setClickable(bean.getStatus() == 0 ?  true: false);
        tvRefuse.setSelected(bean.getStatus() == 0 ? false : true);
        tvRefuse.setTextColor(bean.getStatus() == 0 ? ContextCompat.getColor(mContext, R.color.purple_9966ff):Color.WHITE);
        tvRefuse.setClickable(bean.getStatus() == 0 ? true : false);

        switch (bean.getType()) {
            case 1://预约通知主播 接单
                tvRefuse.setVisibility(View.VISIBLE);
                tvOrder.setVisibility(View.VISIBLE);
                tvOrder.setText(bean.getStatus()==1?"已接单":"接单");
                tvRefuse.setText(bean.getStatus()==2?"已拒绝":"拒绝");
                if (bean.getStatus()==0){
                    tvOrder.setText("接单");
                    tvRefuse.setText("拒绝");
                }else if(bean.getStatus()==1){
                    tvOrder.setText("已接单");
                    tvRefuse.setVisibility(View.GONE);
                }else if(bean.getStatus()==2){
                    tvOrder.setText("已拒绝");
                    tvRefuse.setVisibility(View.GONE);
                }else if(bean.getStatus()==3){
                    tvRefuse.setVisibility(View.GONE);
                    tvOrder.setText("已撤销");
                }else if(bean.getStatus()==4){
                    tvRefuse.setVisibility(View.GONE);
                    tvOrder.setText("已过期");
                }
                break;
            case 2://预约 提前通知主播
                break;
            case 3://用户预约迟到通知
                tvOrder.setVisibility(View.VISIBLE);
                tvOrder.setText("提醒他");
                tvOrder.setText(bean.getStatus()==1?"已提醒":"提醒他");
                break;
            case 5://预约 提前通知用户
                tvOrder.setVisibility(View.VISIBLE);
                tvOrder.setText("去看看");
                break;
            case 4://预约到时间通知用户
            case 8://主播邀约用户
            case 7://主播提醒客户
                tvDatingTime.setVisibility(View.GONE);
                tvOrder.setVisibility(View.VISIBLE);
                tvOrder.setText("去看看");
                break;
            case 6://预约超时退款通知用户
            case 9://主播拒接预约
            case 10://主播提前结束 退款
                reason.setVisibility(View.VISIBLE);
                reason.setText(bean.getReason());
                reason.setTextColor(mContext.getResources().getColor(R.color.red_ff3333));
                break;

        }
    }
}
