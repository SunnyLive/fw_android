package com.fengwo.module_flirt.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.widget.RoundImageView;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.FlirtCommentBean;
import com.fengwo.module_flirt.widget.CustomRatingBar;
import com.willy.ratingbar.ScaleRatingBar;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FlirtCommentPopAdapter extends BaseQuickAdapter<FlirtCommentBean.RecordsDTO, BaseViewHolder> {

    private List<String> colors = Arrays.asList("#99CCFF", "#99A2FF", "#FF9999", "#DD99FF", "#FFE499");
    private int[] back = {R.drawable.bg_tag_2, R.drawable.bg_tag_3, R.drawable.bg_tag_4, R.drawable.bg_tag_1 };
    private List<String> textColor = Arrays.asList("#FE3C9C", "#FF6B5B", "#ECAB24", "#54BDFF" );

    public FlirtCommentPopAdapter(int layoutResId, @Nullable List<FlirtCommentBean.RecordsDTO> data) {
        super(layoutResId, data);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FlirtCommentBean.RecordsDTO item) {
        RoundImageView ivHeader = helper.getView(R.id.header);
        ImageLoader.loadCircleImg(ivHeader, item.getUserHeadImg());
        helper.setText(R.id.nickname, item.getUserNickname());
        if (!TextUtils.isEmpty(item.getCreateTime())) {
            long time = Long.parseLong(item.getUpdateTime());
            helper.setText(R.id.time, TimeUtils.formatDateBySpace(time));
        }
        CustomRatingBar ratingBar = helper.getView(R.id.rb_grade);
        ratingBar.showImage(item.getStartLevel());
        TagFlowLayout flowLayout = helper.getView(R.id.tag_flow_layout);
        String evTypes = item.getEvTypes();
        if (!TextUtils.isEmpty(evTypes)) {
            String[] evTypeArr = evTypes.split(",");
            List<String> evTypeList = Arrays.asList(evTypeArr);
            flowLayout.setAdapter(new TagAdapter<String>(evTypeList) {
                @Override
                public View getView(FlowLayout parent, int position, String text) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.tag_flow_layout_textview, null);
                    TextView textView = view.findViewById(R.id.tv_card_label);
                    textView.setText(text);
                    textView.setTextColor(Color.parseColor(textColor.get(position % 4)));
                    view.setBackgroundResource(back[position % 4]);
                    return view;
                }
            });
        }
        helper.addOnClickListener(R.id.root);

    }

}
