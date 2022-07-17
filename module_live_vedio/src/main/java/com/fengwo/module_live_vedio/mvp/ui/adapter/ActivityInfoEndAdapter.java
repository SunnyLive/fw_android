package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.base.BaseVH;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.GetActivityInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;

/**
 * @anchor Administrator
 * @date 2020/10/23
 */
public class ActivityInfoEndAdapter extends BaseQuickAdapter<GetActivityInfoDto.ProcessDtVOListBean.GiftItemListBean, BaseVH> {
    public ActivityInfoEndAdapter() {
        super(R.layout.adapter_activity_wsj_end);
    }

    private IAddListListener listener;

    public interface IAddListListener {
        void onClickePosition(GiftDto dto);
    }
    int numStatus ;
    public void setNumStatus ( int numStatus){
        this.numStatus = numStatus;
    }
    public void setIDeleteListener(IAddListListener listener) {
        this.listener = listener;
    }
    @Override
    protected void convert(@NonNull BaseVH helper, GetActivityInfoDto.ProcessDtVOListBean.GiftItemListBean item) {
        ImageView im_pic = helper.getView(R.id.im_pic);
        ImageLoader.loadRouteImg(im_pic,item.getPic(),4);
        helper.setText(R.id.tv_name,item.getName()+"");
        helper.setText(R.id.tv_num,"（"+item.getCurrentGiftNum()+"/"+item.getTargetGiftNum()+"）");
      //  LinearLayout ll_view = helper.getView(R.id.ll_view);
        TextView tv_status = helper.getView(R.id.tv_status);
        if(numStatus == 1){
            tv_status.setBackgroundResource(0);
            helper.setText(R.id.tv_status,"未完成");
        }else {
            switch (item.getStatus()){
                case 1:
                    tv_status.setBackgroundResource(R.drawable.pic_end_view);
                    helper.setText(R.id.tv_status,"立即赠送");
                    tv_status.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(null!=listener){

                                listener.onClickePosition(item.giftInfo);
                            }
                        }
                    });

                    break;
                case 2:
                    tv_status.setBackgroundResource(R.drawable.pic_end_view);
                    helper.setText(R.id.tv_status,"已完成");
                    break;
//                case 3:
//                    tv_status.setBackgroundResource(0);
//                    helper.setText(R.id.tv_status,"待开启");
//                    break;
            }
        }

//        if(item.getStatus()==1){//1-待开启;2-进行中;3-已完成
//
//        }else {
//
//
//        }

    }
}
