package com.fengwo.module_chat.mvp.ui.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.CommentModel;
import com.fengwo.module_chat.mvp.ui.holder.CommentViewHolder;
import com.fengwo.module_comment.utils.chat.ChatTimeUtils;
import com.fengwo.module_comment.utils.chat.FaceConversionUtil;
import com.fengwo.module_comment.utils.ImageLoader;

public class CommentAdapter extends BaseQuickAdapter<CommentModel, CommentViewHolder> {

    private OnChildItemClickListener listener;
    private final RelativeSizeSpan sizeSpan;
    private final ForegroundColorSpan colorSpan;

    public CommentAdapter(Context context) {
        super(R.layout.layout_comment);
        sizeSpan = new RelativeSizeSpan(0.8F);
        colorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.text_99));
    }

    @Override
    protected void convert(@NonNull CommentViewHolder helper, CommentModel item) {
        ImageView imageView = helper.getView(R.id.ivAvatar);
        View likeView = helper.getView(R.id.view_like);
        RecyclerView rvReply = helper.getView(R.id.rvReply);
        View expandView = helper.getView(R.id.viewExpandStatus);

        ImageLoader.loadImg(imageView, item.headImg);
        helper.setText(R.id.tvNickName, item.nickname)
                .setText(R.id.tvLikeCount, String.valueOf(item.likes));
        String time = ChatTimeUtils.getCommentTime(item.createTime);
        Spannable spannable = FaceConversionUtil.getSmiledText(mContext, item.content + "  " + time);
        spannable.setSpan(sizeSpan, item.content.length(), spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(colorSpan, item.content.length(), spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        helper.setText(R.id.tvContent, spannable);
        likeView.setSelected(item.isLike == 1);
        // 判断展开按钮是否显示
        if (item.replys <= 0) {
            expandView.setVisibility(View.GONE);
            rvReply.setVisibility(View.GONE);
        } else {
            expandView.setVisibility(View.VISIBLE);
            // 判断展开按钮的状态
            if (item.isExpand) {
                rvReply.setVisibility(View.VISIBLE);
                helper.secondAdapter.setNewData(item.seconds);
                helper.secondAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                    int id = view.getId();
                    if (listener == null) return;
                    if (id == R.id.view_like) {
                        listener.commentLike(helper.getAdapterPosition(), position, helper.secondAdapter.getData().get(position));
                    }
                });
                helper.secondAdapter.setOnItemClickListener((adapter, view, position) -> {
                    if (listener == null) return;
                    listener.commentClick(helper.getAdapterPosition(), position, helper.secondAdapter.getData().get(position));
                });
                if (item.hasMore()) {
                    expandView.setSelected(true);
                    helper.setText(R.id.tvStatus, "展开更多评论");
                } else {
                    expandView.setSelected(true);
                    helper.setText(R.id.tvStatus, "收起");
                }
            } else { // 未展开状态
                rvReply.setVisibility(View.GONE);
                expandView.setSelected(false);
                helper.setText(R.id.tvStatus, String.format("展开%s条回复", item.replys));
            }
        }
        helper.addOnClickListener(R.id.view_like, R.id.viewExpandStatus);
    }

    public void setOnChildItemClickListener(OnChildItemClickListener l) {
        listener = l;
    }

    public interface OnChildItemClickListener {
        void commentLike(int parentPosition, int position, CommentModel commentModel);

        void commentClick(int parentPosition, int position, CommentModel commentModel);
    }
}