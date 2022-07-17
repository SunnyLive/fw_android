package com.fengwo.module_live_vedio.mvp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;

import java.util.List;

public class LiveRecAdapter extends BaseQuickAdapter<ZhuboDto, BaseViewHolder> {

    public LiveRecAdapter(@Nullable List<ZhuboDto> data) {
        super(R.layout.item_liveroom_rec_live, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ZhuboDto item) {
//        ViewGroup.LayoutParams parm = helper.itemView.getLayoutParams();
//        parm.height = ScreenUtils.getScreenHeight(mContext) / 5;
        ImageLoader.loadRouteImg(helper.getView(R.id.iv_thumb), item.thumb);
        helper.setText(R.id.tv_title, item.title);
        //是否开播
        if (item.status == 2) {
            helper.setVisible(R.id.iv_gif,true);
            ImageLoader.loadGif(helper.getView(R.id.iv_gif), R.drawable.live_cell_gif);
            helper.setText(R.id.tv_looknum, DataFormatUtils.formatNumbers(item.lookNums));//观看人数
            helper.setGone(R.id.im_hmz,false);
        } else {

            helper.setGone(R.id.iv_gif,false);
            helper.setVisible(R.id.im_hmz,true);
            helper.setText(R.id.tv_looknum, DataFormatUtils.formatNumbers(item.weekProfit));//上周花蜜值

        }
    }
}
