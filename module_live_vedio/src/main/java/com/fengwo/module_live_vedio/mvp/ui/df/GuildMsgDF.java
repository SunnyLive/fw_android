package com.fengwo.module_live_vedio.mvp.ui.df;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.MapPoiDto;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/19
 */

public class GuildMsgDF extends BaseDialogFragment {

    private MapPoiDto mapPoiDto;
    TextView tvName;
    ImageView ivGuildHead;

    public static DialogFragment getInstance(MapPoiDto mapPoiDto){
        GuildMsgDF guildMsgDF = new GuildMsgDF();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mapPoiDto",mapPoiDto);
        guildMsgDF.setArguments(bundle);
        return guildMsgDF;
    }

    @Override
    protected void initView() {
        mapPoiDto = (MapPoiDto) getArguments().getSerializable("mapPoiDto");
        tvName = findViewById(R.id.tv_guild_name);
        ivGuildHead = findViewById(R.id.iv_guild_header);
        TextView tvCommit = findViewById(R.id.tv_commit);
        tvName.setText(mapPoiDto.getCityName());
        ImageLoader.loadImg(ivGuildHead,mapPoiDto.getCityIcon());
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CitySelectDF citySelectDF = (CitySelectDF) CitySelectDF.getInstance(mapPoiDto.getId(),mapPoiDto.getGuildId());
                citySelectDF.showDF(getChildFragmentManager(),CitySelectDF.class.getName());
                dismiss();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.dialog_guild_msg;
    }


    public void showDF(MapPoiDto mapPoiDto,FragmentManager fragmentManager, String tag) {
        super.showDF(fragmentManager, tag);
        this.mapPoiDto =mapPoiDto;
//        refreshView();
    }


    private void refreshView() {
        tvName.setText(mapPoiDto.getCityName());
        ImageLoader.loadImg(ivGuildHead,mapPoiDto.getCityIcon());
    }

    @Override
    public int getOrientation() {
        return Gravity.CENTER;
    }
}
