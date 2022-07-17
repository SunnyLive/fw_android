package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.BuyShouhuDto;

import java.util.List;

public class PrivilegeAdapter extends BaseQuickAdapter<BuyShouhuDto.GuardListBean.PrivilegeVOListBean, BaseViewHolder> {
    public PrivilegeAdapter(@Nullable List<BuyShouhuDto.GuardListBean.PrivilegeVOListBean> data) {
        super(R.layout.live_item_shouhu_privilege, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BuyShouhuDto.GuardListBean.PrivilegeVOListBean item) {
        ImageView iv = helper.getView(R.id.iv);
        String imgUrl = "";
        if (item.getIsHave() == 1) {
            imgUrl = item.getPrivilegeIcon();
        } else {
            imgUrl = item.getPrivilegeIconGray();
        }
        ImageLoader.loadImg(iv, imgUrl);
        helper.setText(R.id.tv_name, item.getPrivilegeName());

    }
}
