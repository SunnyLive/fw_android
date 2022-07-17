package com.fengwo.module_chat.mvp.ui.adapter;

import android.graphics.Paint;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.widgets.TagTextView;
import com.fengwo.module_comment.widget.MyFlowLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * 热点圈适配器
 *
 * @author chenshanghui
 * @intro
 * @date 2019/9/6
 */
public class ChatHotCircleAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ChatHotCircleAdapter(@Nullable List<String> data) {
        super(R.layout.chat_item_hot_circle_article, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        TextView tvArticleName = helper.getView(R.id.tv_article_name);
        TextView tvViews = helper.getView(R.id.tv_view_num);
        TextView tvComments = helper.getView(R.id.tv_comment_num);
        ImageView ivImg = helper.getView(R.id.iv_article_img);
        MyFlowLayout llTag = helper.getView(R.id.fl_label);
        llTag.setMaxLines(2);
        tvArticleName.setText(helper.getAdapterPosition() + "");
        ArrayList list = new ArrayList<String>();
        list.add("#爷爷乃纳");
        list.add("#按时框");
        list.add("#撒打算");
        addLabel(llTag, list);
    }


    private void addLabel(MyFlowLayout linearLayout, List<String> list) {
        linearLayout.removeAllViews();
        int dp7 = (int) mContext.getResources().getDimension(R.dimen.dp_7);
        int dp10 = (int) mContext.getResources().getDimension(R.dimen.dp_10);
        int dp8 = (int) mContext.getResources().getDimension(R.dimen.dp_8);
        int dp2 = (int) mContext.getResources().getDimension(R.dimen.dp_2);
        for (String s : list) {
            TagTextView tvLabel = new TagTextView(mContext);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llp.setMargins(dp10, dp2, 0, 0);
            tvLabel.setTextColor(ContextCompat.getColor(mContext, R.color.blue_0085FF));
            tvLabel.setFullColor(R.color.blue_E5F2FF);
            tvLabel.setBackGroudStyle(Paint.Style.FILL);
            tvLabel.setPadding(dp8, dp7, dp8, dp7);
            tvLabel.setText(s);
            tvLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.sp_11));
            linearLayout.addView(tvLabel, llp);
        }
    }
}
