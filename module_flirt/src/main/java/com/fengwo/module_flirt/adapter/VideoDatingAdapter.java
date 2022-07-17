package com.fengwo.module_flirt.adapter;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.CityHost;


import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 视频交友
 *
 * @Author BLCS
 * @Time 2020/3/27 10:28
 */
public class VideoDatingAdapter extends BaseQuickAdapter<CityHost, BaseViewHolder> {
    private String tabelName;

    public VideoDatingAdapter() {
        super(R.layout.adapter_video_dating);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, CityHost item) {
        holder.setText(R.id.tv_adapter_video_describe, item.getRoomTitle());
        holder.setText(R.id.tv_adapter_video_name, item.getNickname());
//        holder.setText(R.id.tv_adapter_card_location, handleLocation(item.getLocation()));
        holder.setGone(R.id.tv_adapter_card_age, item.getAge() <= 0 ? false : true);
        holder.setText(R.id.tv_adapter_card_age, item.getAge() + "岁");
        if (!TextUtils.isEmpty(item.getTagName())) {
            holder.setGone(R.id.tv_adapter_video_label, true);
            String[] split = item.getTagName().split(",");
            if (TextUtils.isEmpty(tabelName)){
                holder.setText(R.id.tv_adapter_video_label, split[0]);
            }else{
                holder.setText(R.id.tv_adapter_video_label,item.getTagName().contains(tabelName)?tabelName:split[0]);
            }
        } else {
            holder.setGone(R.id.tv_adapter_video_label, false);
        }
        ImageView imageView = holder.getView(R.id.iv_adapter_card);
        ImageLoader.loadRouteImg(imageView, item.getHeadImg());

        ImageView view = holder.getView(R.id.iv_adapter_video_tab);
        ImageLoader.loadGif(view, R.drawable.gif_liao);
        holder.setGone(R.id.iv_adapter_video_tab, item.getBstatus() == 1);
        holder.setText(R.id.tv_adapter_video_distance, item.getDistance());
    }

    /**
     * 处理地址
     * @param location
     */
    private String handleLocation(String location) {
        String lc;
        if (TextUtils.isEmpty(location)){
           lc = "";
        }else{
            if (location.contains("/")){
                String[] split = location.split("/");
                lc = split.length>1? split[1]:split[0];
            }else if(location.contains("-")){
                String[] split = location.split("-");
                lc = split.length>1? split[1]:split[0];
            }else{
                lc = location;
            }
        }
//        if (!TextUtils.isEmpty(lc)&&lc.endsWith("市")){
//            KLog.e("lgl--length",lc.length()+"");
//            lc = lc.substring(0,lc.length()-1);
//        }
        return lc;
    }

    public void setTableName(String tabelName) {
        this.tabelName = tabelName;
    }
}
