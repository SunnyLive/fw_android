package com.fengwo.module_chat.mvp.ui.holder;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.ui.adapter.CommentSecondAdapter;
import com.fengwo.module_comment.base.BaseApplication;

public class CommentViewHolder extends BaseViewHolder {

    public final CommentSecondAdapter secondAdapter;

    public CommentViewHolder(View view) {
        super(view);
        RecyclerView recyclerView = view.findViewById(R.id.rvReply);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        secondAdapter = new CommentSecondAdapter(view.getContext());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(secondAdapter);
    }
}
