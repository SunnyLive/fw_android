package com.fengwo.module_login.mvp.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.PrivilegeDto;
import com.fengwo.module_login.mvp.dto.TaskDto;

import java.util.List;


public class PrivilegeAdapter extends BaseQuickAdapter<PrivilegeDto, BaseViewHolder> {

    public PrivilegeAdapter(@Nullable List<PrivilegeDto> data) {
        super(R.layout.login_item_huafen_privilege, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PrivilegeDto item) {
        ImageView iv = helper.getView(R.id.iv);
        String url = "";
        if (item.isHas || item.isHave == 1) {
            url = item.privilegeIcon;
        } else {
            url = item.privilegeIconGray;
        }
        ImageLoader.loadImg(iv, url);
        helper.setText(R.id.tv_name, item.privilegeName);
        helper.setText(R.id.tv_level, item.remark);
        helper.setText(R.id.tv_level, item.privilegeUnlock);
    }
}
