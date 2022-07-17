package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.PkTeamInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/11
 */
public class PkTeamAdapter extends BaseQuickAdapter<PkTeamInfo, BaseViewHolder> {

    private boolean isLeft;

    public PkTeamAdapter(@Nullable List<PkTeamInfo> data,boolean isLeft) {
        super(R.layout.live_item_pk_team,data);
        this.isLeft = isLeft;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(@NonNull BaseViewHolder helper, PkTeamInfo item) {
        CircleImageView circleImageView = helper.getView(R.id.iv_header);
        circleImageView.setBorderColor(isLeft?R.color.blue_7F95FF:R.color.pink_EA7FFF);
        ImageLoader.loadImg(helper.getView(R.id.iv_header),item.getHeadImg());
        circleImageView.post(new Runnable() {
            @Override
            public void run() {
                int[] imFirst = new int[2];
                circleImageView.getLocationOnScreen(imFirst);
                Log.e("tag","circleImageView="+imFirst[0]);
            }
        });
    }
}
