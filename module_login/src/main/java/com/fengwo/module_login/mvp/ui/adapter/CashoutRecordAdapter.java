package com.fengwo.module_login.mvp.ui.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.WithDrawDto;

public class CashoutRecordAdapter extends BaseQuickAdapter <WithDrawDto, BaseViewHolder>{

    public CashoutRecordAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, WithDrawDto item) {
        helper.setText(R.id.tv_bank_name,item.getBankName());
        helper.setText(R.id.tv_amount,String.valueOf(Math.round(item.getMoney())));
        helper.setText(R.id.tv_time,TimeUtils.dealDateFormatToRecord(item.getCreateTime()));
        TextView view = helper.getView(R.id.tv_withdraw_result);
//        0、1 提现中 2提现成功 3、4 提现失败
        switch (item.getStatus()){
            case 2:
                view.setText("提现成功（手续费￥"+item.getFee()+"）");
                view.setTextColor(mContext.getResources().getColor(R.color.text_99));
                helper.setGone(R.id.tv_failure_reason,false);
                break;
            case 3:
                view.setText("提现失败");
                view.setTextColor(mContext.getResources().getColor(R.color.text_99));
                helper.setText(R.id.tv_failure_reason,item.getRemark()+" 已退回账户花蜜值"+Math.round(item.getMoney())*100);
                helper.setGone(R.id.tv_failure_reason,true);
                break;
            case 4:
                view.setText("提现失败");
                view.setTextColor(mContext.getResources().getColor(R.color.text_99));
                helper.setText(R.id.tv_failure_reason,item.getRemark());
                helper.setGone(R.id.tv_failure_reason,true);
                break;
            default:
                view.setText("提现中");
                view.setTextColor(mContext.getResources().getColor(R.color.red_f46060));
                helper.setGone(R.id.tv_failure_reason,false);
                break;
        }

    }
}
