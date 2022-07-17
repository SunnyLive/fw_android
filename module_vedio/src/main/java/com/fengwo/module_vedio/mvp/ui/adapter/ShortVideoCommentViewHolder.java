package com.fengwo.module_vedio.mvp.ui.adapter;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_vedio.R;

public class ShortVideoCommentViewHolder extends BaseViewHolder {

    public final ShortVideoCommentSecondAdapter secondAdapter;

    public ShortVideoCommentViewHolder(View view) {
        super(view);
        RecyclerView recyclerView = view.findViewById(R.id.rvReply);
        recyclerView.setLayoutManager(new LinearLayoutManager(BaseApplication.mApp));
        secondAdapter = new ShortVideoCommentSecondAdapter(BaseApplication.mApp);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(secondAdapter);
    }
}
