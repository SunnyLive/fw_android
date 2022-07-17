package com.fengwo.module_login.mvp.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.LiveLengthDto;

import java.util.List;

public class LivingTimeAdapter extends BaseQuickAdapter<LiveLengthDto.RecordsBean, BaseViewHolder> {
    private Context mContext;
    private int type;

    public LivingTimeAdapter(Context c, int layoutResId, @Nullable List<LiveLengthDto.RecordsBean> data, int type) {
        super(layoutResId, data);
        mContext = c;
        this.type = type;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LiveLengthDto.RecordsBean item) {
        TextView tvStart = helper.getView(R.id.item_tv_starttime);
        TextView tvEnd = helper.getView(R.id.item_tv_endtime);
        TextView tvTime = helper.getView(R.id.item_tv_alltime);
        TextView tvDevice = helper.getView(R.id.item_tv_devices);
        ImageView ivHead = helper.getView(R.id.iv_head);
        if (type == 1){
            String startTime = TimeUtils.dealDateFormatToRecord(item.getStartTime());
            String endTime = TimeUtils.dealDateFormatToRecord(item.getEndTime());
            ivHead.setVisibility(View.GONE);
            tvStart.setVisibility(View.VISIBLE);
            tvStart.setTextColor(mContext.getResources().getColor(R.color.text_99));
            tvStart.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp_11));
            tvEnd.setTextColor(mContext.getResources().getColor(R.color.text_99));
            tvEnd.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp_11));
            tvStart.setText(formatTime(startTime));
            tvEnd.setText(formatTime(endTime));
            tvTime.setText(item.getLiveTimes()+"");
            if (TextUtils.isEmpty(item.getDeviceInfo())) {
                helper.setText(R.id.item_tv_devices, "iphone x");
            }else {
                helper.setText(R.id.item_tv_devices, item.getDeviceInfo() + "");
            }
            tvDevice.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp_11));
        }else {
            String startTime = TimeUtils.longToString(Long.parseLong(item.getCreateTime()));
            String endTime = TimeUtils.longToString(Long.parseLong(item.getEndTime()));
            ivHead.setVisibility(View.VISIBLE);
            tvStart.setVisibility(View.GONE);
            ImageLoader.loadImg(ivHead,item.getThumb());
            tvEnd.setTextColor(mContext.getResources().getColor(R.color.text_99));
            tvEnd.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp_11));
            tvTime.setTextColor(mContext.getResources().getColor(R.color.text_99));
            tvTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp_11));
            tvEnd.setText(formatTime(startTime));
            tvTime.setText(formatTime(endTime));
            tvDevice.setText(item.getLiveTimes()+"");
        }


    }

    private String formatTime(String time){
        String[] result = time.split("\\s+");
        return result[0]+"\n"+result[1];
    }
}
