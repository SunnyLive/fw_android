package com.fengwo.module_flirt.adapter;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_flirt.IM.bean.OrderMessageBean;
import com.fengwo.module_flirt.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @Author BLCS
 * @Time 2020/4/2 18:09
 */
public class DatingOrderAdapter extends BaseQuickAdapter<OrderMessageBean, BaseViewHolder> {

    private final CountBackUtils countBackUtils;


    public DatingOrderAdapter() {
        super(R.layout.adapter_dating_order);
        countBackUtils = new CountBackUtils();
        countBackUtils.countBack(-1, new CountBackUtils.Callback() {
            @Override
            public void countBacking(long time) {
                ArrayList<OrderMessageBean> orderMessageBeans = new ArrayList<>(getData());
                for (OrderMessageBean bean:orderMessageBeans){
                    if (bean.getShowTime()<-1){
                        getData().remove(bean);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void finish() {

            }
        });
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, OrderMessageBean bean) {
            holder.addOnClickListener(R.id.tv_accept_order);
            CircleImageView header = holder.getView(R.id.civ_header);
            if (bean.getUser() != null && !TextUtils.isEmpty(bean.getUser().getHeadImg()))
                ImageLoader.loadCircleImg(header, bean.getUser().getHeadImg());
            holder.setText(R.id.tv_content, bean.getContent().getValue());
            String consNo = bean.getOrder().getConsNo();
            holder.setText(R.id.tv_refuse, "拒绝");
            if ( bean.getShowTime() < 0){
                if (onTimeOutListener != null)
                    onTimeOutListener.over(consNo, holder.getAdapterPosition());
            }else{
                holder.setText(R.id.tv_accept_order, "接单(" +  bean.getShowTime()  + "s)");
            }
            holder.addOnClickListener(R.id.tv_accept_order);
            holder.addOnClickListener(R.id.tv_refuse);
    }


    /**
     * 清空资源
     */
    public void cancelAllTimers() {
        countBackUtils.destory();
    }

    /**
     * 倒计时 时间到 监听
     */
    public void addTimeOut(OnTimeOutListener listener) {
        onTimeOutListener = listener;
    }


    public interface OnTimeOutListener {
        void over(String consNo, int pos);
    }

    public OnTimeOutListener onTimeOutListener;

}
