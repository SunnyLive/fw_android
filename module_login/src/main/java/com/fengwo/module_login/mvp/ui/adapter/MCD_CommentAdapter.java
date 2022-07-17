package com.fengwo.module_login.mvp.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.chat.ChatTimeUtils;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.MCD_CommentDto;

public class MCD_CommentAdapter extends BaseQuickAdapter<MCD_CommentDto, BaseViewHolder> {


    public MCD_CommentAdapter() {
        super(R.layout.item_mcd_comment);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MCD_CommentDto item) {
        //头像
        ImageView imageView = helper.getView(R.id.iv_head);
        ImageLoader.loadImg(imageView, item.headImg);
        //用户名
        helper.setText(R.id.tv_name, item.nickname);
        //
        //helper.getView(R.id.tv_mine).setVisibility(item.userId == userProviderService.getUserInfo().getId() ? View.VISIBLE : View.GONE);

        //点赞数量
        helper.setText(R.id.tv_like_count, String.valueOf(item.likes));
        //自己是否点赞
        helper.getView(R.id.iv_find_like).setSelected(item.isLike == 1);
        //时间
        helper.setText(R.id.tv_time, ChatTimeUtils.getCommentTime(item.createTime));
        //内容
        helper.setText(R.id.tv_content, item.content);

        helper.addOnClickListener(R.id.iv_find_like);
    }
}
