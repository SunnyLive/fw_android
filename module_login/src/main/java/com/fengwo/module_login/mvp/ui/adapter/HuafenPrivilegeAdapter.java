package com.fengwo.module_login.mvp.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.TaskDto;

import java.util.List;


public class HuafenPrivilegeAdapter extends BaseQuickAdapter<TaskDto, BaseViewHolder> {

    public HuafenPrivilegeAdapter(@Nullable List<TaskDto> data) {
        super(R.layout.login_item_huafen_privilege, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TaskDto item) {
        ImageView iv = helper.getView(R.id.iv);
        ImageLoader.loadImg(iv, item.taskIcon);
        helper.setText(R.id.tv_name, item.taskName);
        helper.setText(R.id.tv_des, item.taskRemark);

    }
}
