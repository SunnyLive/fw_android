package com.fengwo.module_comment.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fengwo.module_comment.R;
import com.fengwo.module_comment.widget.TagLayoutView;

import java.util.Random;

public class MessageTagListAdapter extends TagLayoutView.TagViewAdapter<String> {

    private final int[] backgrounds = new int[]{
            R.drawable.bg_greet_item_1,
            R.drawable.bg_greet_item_3,
            R.drawable.bg_greet_item_4,
    };

    private final Context context;

    public MessageTagListAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected View createView() {
        return View.inflate(context, R.layout.tag_item_list, null);
    }

    @Override
    protected void bindView(ViewHolder holder, int position) {
        String tag = getItem(position);
        TextView tvTitle = holder.itemView.findViewById(R.id.tvTitle);
        tvTitle.setText(tag);
        tvTitle.setBackgroundResource(backgrounds[new Random().nextInt(backgrounds.length)]);
    }
}
