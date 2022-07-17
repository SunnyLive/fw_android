package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.graphics.Color;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.LivePushMoreDto;
import com.fengwo.module_live_vedio.mvp.dto.SoundDto;

import java.util.List;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/7
 */
public class LiveSoundMoreAdapter extends BaseQuickAdapter<SoundDto, BaseViewHolder> {


    public LiveSoundMoreAdapter(int layoutResId, @Nullable List<SoundDto> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SoundDto item) {
        TextView view = helper.getView(R.id.tv_title);
        if(item.getTitle().endsWith("0")){
            if(item.isOpen()){
                view.setBackgroundResource(R.drawable.sound_off);

            }else {
                view.setBackgroundResource(R.drawable.sound_on);
            }
            view.setText("");
        }else {
            if(item.isOpen()){
                view.setTextColor(Color.parseColor("#ffffff"));
                view.setBackgroundResource(R.drawable.bg_live_sound_yuans);
            }else {
                view.setTextColor(Color.parseColor("#151923"));
                view.setBackgroundResource(R.drawable.bg_live_sound_yuan);
            }
            view.setText(item.getTitle());
        }


      //  view.setCompoundDrawablesWithIntrinsicBounds(null,mContext.getResources().getDrawable(item.getIvImg()),null,null);
    }
}
