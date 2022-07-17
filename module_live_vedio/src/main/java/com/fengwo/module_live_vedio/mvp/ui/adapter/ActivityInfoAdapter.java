package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseVH;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.GetActivityInfoDto;

/**
 * @anchor Administrator
 * @date 2020/10/23
 */
public class ActivityInfoAdapter extends BaseQuickAdapter<GetActivityInfoDto.ProcessDtVOListBean.GiftItemListBean, BaseVH> {
    public ActivityInfoAdapter() {
        super(R.layout.adapter_activity_wsj);
    }

    @Override
    protected void convert(@NonNull BaseVH helper, GetActivityInfoDto.ProcessDtVOListBean.GiftItemListBean item) {
        ImageView im_pic = helper.getView(R.id.im_pic);
        ImageLoader.loadRouteImg(im_pic,item.getPic(),4);
        helper.setText(R.id.tv_name,item.getName()+"");
        helper.setText(R.id.tv_num,"（"+item.getCurrentGiftNum()+"/"+item.getTargetGiftNum()+"）");
        LinearLayout ll_view = helper.getView(R.id.ll_view);
        if(item.getStatus()==1){
            helper.setText(R.id.tv_status,"未完成");
            ll_view.setBackgroundResource(0);
        }else {
            helper.setText(R.id.tv_status,"已完成");
            ll_view.setBackgroundResource(R.drawable.bg_activityg_gift_select);
        }

    }
}
