package com.fengwo.module_vedio.mvp.ui.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.chat.ChatTimeUtils;
import com.fengwo.module_comment.utils.chat.FaceConversionUtil;
import com.fengwo.module_vedio.mvp.dto.ShortVideoCommentDto;

public class ShortVideoCommentSecondAdapter extends BaseQuickAdapter<ShortVideoCommentDto, BaseViewHolder> {

    private final RelativeSizeSpan sizeSpan;
    private final ForegroundColorSpan colorSpan;

    public ShortVideoCommentSecondAdapter(Context context) {
        super(R.layout.layout_comment_second);
        sizeSpan = new RelativeSizeSpan(0.8F);
        colorSpan = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.text_99));
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShortVideoCommentDto item) {
        ImageView imageView = helper.getView(R.id.ivAvatar);
        View likeView = helper.getView(R.id.view_like);

        ImageLoader.loadImg(imageView, item.userUrl);
        helper.setText(R.id.tvNickName, item.nickname)
                .setText(R.id.tvLikeCount, String.valueOf(item.likes));
        String time = ChatTimeUtils.getCommentTime(item.createTimestamp);
        Spannable spannable;
        if (item.secondType == 1) {
            spannable = FaceConversionUtil.getSmiledText(mContext, item.content + "  " + time);
        } else {
            String str = String.format("回复 @%s: %s  %s", item.secondNickname, item.content, time);
            spannable = FaceConversionUtil.getSmiledText(mContext, str);
        }
        spannable.setSpan(sizeSpan, spannable.length() - time.length() - 2, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(colorSpan, spannable.length() - time.length() - 2, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        helper.setText(R.id.tvContent, spannable);

        likeView.setSelected(item.isLike);
        helper.addOnClickListener(R.id.view_like);
    }
}