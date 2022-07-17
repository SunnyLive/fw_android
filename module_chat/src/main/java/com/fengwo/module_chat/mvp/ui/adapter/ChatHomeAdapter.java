package com.fengwo.module_chat.mvp.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.widgets.TagTextView;
import com.fengwo.module_comment.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 聊天首页适配器
 *
 * @author chenshanghui
 * @intro
 * @date 2019/9/6
 */
public class ChatHomeAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private int gridItemWidth;
    private int gridItemHeight;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatHomeAdapter(Context context, List<MultiItemEntity> data) {
        super(data);
        addItemType(1, R.layout.chat_item_home_article);
        addItemType(2, R.layout.chat_item_home_video_article);
        gridItemWidth = (int) ((ScreenUtils.getScreenWidth(context) - context.getResources().getDimension(R.dimen.dp_10)*2)) / 2;
        gridItemHeight = (int) (gridItemWidth / 0.865f)/*设计稿上宽除以高得到的比例*/;
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiItemEntity item) {
        Log.d(TAG, "convert: position " + helper.getAdapterPosition() + " type " + item.getItemType());
        switch (item.getItemType()) {
            case 1: {
                FrameLayout flMedia = helper.getView(R.id.fl_media);
                RecyclerView rvImg = helper.getView(R.id.rv_img);
                if (helper.getAdapterPosition() % 2 == 0) {
                    flMedia.setVisibility(View.VISIBLE);
                    rvImg.setVisibility(View.GONE);
                    helper.setVisible(R.id.fl_media, true);
                    helper.setVisible(R.id.rv_img, false);
                    LinearLayout linearLayout = helper.getView(R.id.ll_tag);
                    ArrayList<String> list = new ArrayList<>();
                    list.add("蛋炒饭");
                    list.add("煮冬瓜");
                    list.add("爆炒西蓝花");
                    addLabel(linearLayout, list);
                } else {
                    flMedia.setVisibility(View.GONE);
                    rvImg.setVisibility(View.VISIBLE);
                    rvImg = helper.getView(R.id.rv_img);
                    rvImg.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
                    ArrayList list = new ArrayList<String>();
                    list.add("1");
                    list.add("2");
                    list.add("3");
                    rvImg.setAdapter(new ChildImageAdapter(mContext, list));
                }
                break;
            }
            case 2: {
                FrameLayout flVideoArticle = helper.getView(R.id.fl_video_article);
                FrameLayout flMedia = helper.getView(R.id.fl_media);
                RecyclerView rvImg = helper.getView(R.id.rv_img);
                ViewGroup.LayoutParams lp = flVideoArticle.getLayoutParams();
                lp.width = gridItemWidth;
                lp.height = gridItemHeight;
                break;
            }
        }
    }

    private void addLabel(LinearLayout linearLayout, List<String> list) {
        int dp4 = (int) mContext.getResources().getDimension(R.dimen.dp_4);
        int dp9 = (int) mContext.getResources().getDimension(R.dimen.dp_9);
        int dp5 = (int) mContext.getResources().getDimension(R.dimen.dp_5);
        for (String s : list) {
            TagTextView tvLabel = new TagTextView(mContext);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llp.setMargins(dp5, 0, 0, 0);
            tvLabel.setGradientColorRes(R.color.purple_9B7CF1, R.color.pink_E65BFF);
            tvLabel.setBackGroudStyle(Paint.Style.STROKE);
            tvLabel.setPadding(dp9, dp4, dp9, dp4);
            tvLabel.setText(s);
            tvLabel.setTextColor(ContextCompat.getColor(mContext,R.color.purple_9966ff));
            tvLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen.sp_12));
            linearLayout.addView(tvLabel, llp);
        }
    }


    public static class ChildImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        private int imgWidth;
        private int imgHeight;

        public ChildImageAdapter(Context context, @Nullable List<String> data) {
            super(R.layout.chat_item_article_child_img, data);
            imgWidth = (int) ((ScreenUtils.getScreenWidth(context)
                    - context.getResources().getDimension(R.dimen.dp_10) * 2
                    - context.getResources().getDimension(R.dimen.dp_5) * 2)) / 3;
            imgHeight = imgWidth * 3 / 4;
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, String item) {
            ImageView ivImg = helper.getView(R.id.iv_article_child_img);
            ViewGroup.LayoutParams lp = ivImg.getLayoutParams();
            lp.width = imgWidth;
            lp.height = imgHeight;
        }
    }


}
