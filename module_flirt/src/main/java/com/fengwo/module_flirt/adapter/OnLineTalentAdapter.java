package com.fengwo.module_flirt.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.CityHost;

import java.util.List;


public class OnLineTalentAdapter extends BaseQuickAdapter<CityHost, BaseViewHolder> {
    public OnLineTalentAdapter() {
        super(R.layout.adapter_online_talent);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CityHost item) {
        if (item.getType()==2){/*广告*/
            List<CityHost.AdvertListBean> advertList = item.getAdvertList();
        }else{
            helper.setText(R.id.tv_title,item.getRoomTitle());
            ImageView view = helper.getView(R.id.iv_bg);
            ImageLoader.loadRouteImg(view,item.getCover());
        }

    }
}
