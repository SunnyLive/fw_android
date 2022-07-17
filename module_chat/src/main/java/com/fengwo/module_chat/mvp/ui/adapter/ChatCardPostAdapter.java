package com.fengwo.module_chat.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_chat.R;
import com.fengwo.module_comment.bean.PostCardItem;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.utils.ViewUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatCardPostAdapter extends RecyclerView.Adapter {

    public static final int MAX_COUNT_IMAGE = 3;

    private final int TYPE_POST = 0;
    private final int TYPE_CARD = 1;
    private final int TYPE_ADD = 2;

    private final int itemSize;

    private boolean isVideo = false; // 所选项目是否是视频
    private List<PostCardItem> data = new ArrayList<>();
    private PostCardItem post;
    private OnItemClickListener listener;

    public ChatCardPostAdapter(Context context) {
        itemSize = (ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, MAX_COUNT_IMAGE + 1)) / MAX_COUNT_IMAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_POST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_post, parent, false);
                return new PostViewHolder(view);
            case TYPE_CARD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_post, parent, false);
                return new ContentViewHolder(view);
            case TYPE_ADD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_post, parent, false);
                return new AddViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        L.e(TAG, "onBindViewHolder: " + isVideo);
        L.e(TAG, "onBindViewHolder: " + data.size());
        L.e(TAG, "onBindViewHolder: " + position);
        switch (getItemViewType(position)) {
            case TYPE_CARD:
                ContentViewHolder cardHolder = (ContentViewHolder) holder;
                File itemFile = new File(data.get(position).filePath);
                ImageLoader.loadLocalImg(itemFile, cardHolder.imageView);
                break;
            case TYPE_POST:
                L.e(TAG, "TYPE_POST: " + post.filePath);
                PostViewHolder postHolder = (PostViewHolder) holder;
                if (post == null) return;
                File file = new File(post.filePath);
                ImageLoader.loadLocalImg(file, postHolder.imageView);
                break;
        }
    }

    private static final String TAG = "ChatCardPostAdapter";

    @Override
    public int getItemCount() {
        int count = data.size();
        if (isDataEmpty()) {// 未选择项目，只显示添加按钮
            return 1;
        } else if (isVideo) {// 选择的是视频，显示封面
            return 1;
        } else {// 选择的是图片，显示封面、图片，或者添加按钮
            return Math.min(count + 1, MAX_COUNT_IMAGE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int count = data.size();
        if (isDataEmpty()) return TYPE_ADD;
        if (isVideo) return TYPE_POST;
        if (position < count) return TYPE_CARD;
        else return TYPE_ADD;


//        else {
//            if (position == 0) return TYPE_POST;
//            else if (position <= count) return TYPE_CARD;
//            else return TYPE_ADD;
//        }
    }

    // 判断数据是否为空
    public boolean isDataEmpty() {
        return (data.size() == 0) && (post == null);
    }

    // 删除项目
    private void deleteDataItem(int position) {
        if (position >= data.size()) return;
        if (!isVideo) {
            data.remove(position);
            notifyDataSetChanged();
        } else {
            post = null;
            data.clear();
            notifyDataSetChanged();
        }
    }

    // 设置封面
    public void setPost(PostCardItem item) {
        post = item;
        if (data != null && data.size() > 0)
            data.get(0).filePath = item.filePath;
        notifyDataSetChanged();
    }

    // 设置项目
    public void setData(List<PostCardItem> list, boolean isVideo) {

        data.clear();
        data.addAll(list);
        this.isVideo = isVideo;
        L.e(TAG, "setData: " + isVideo + data.size());
        notifyDataSetChanged();
    }

    // 添加项目
    public void addData(List<PostCardItem> item, boolean isVideo) {
        this.isVideo = isVideo;
        data.addAll(item);
        notifyDataSetChanged();
    }

    // 添加项目
    public void addData(PostCardItem item, boolean isVideo) {
        this.isVideo = isVideo;
        data.add(item);
        notifyDataSetChanged();
    }

    public List<PostCardItem> getData() {
        return data;
    }

    public PostCardItem getPost() {
        return post;
    }

    public void setListener(OnItemClickListener l) {
        listener = l;
    }

    private class ContentViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            CardView card = itemView.findViewById(R.id.root);
            ImageView ivDelete = itemView.findViewById(R.id.iv_delete);
            imageView = itemView.findViewById(R.id.iv_card_post);
            itemView.findViewById(R.id.iv_card_video).setVisibility(View.GONE);
            ivDelete.setVisibility(View.VISIBLE);
            ivDelete.setOnClickListener(v -> deleteDataItem(getLayoutPosition()));
        }
    }

    private class AddViewHolder extends RecyclerView.ViewHolder {

        AddViewHolder(@NonNull View itemView) {
            super(itemView);
            CardView card = itemView.findViewById(R.id.root);
            itemView.findViewById(R.id.iv_card_video).setVisibility(View.GONE);
            ViewUtils.throttleClick(card, v -> {
                if (listener != null) listener.itemAdd(!isDataEmpty());
            });
        }
    }

    private class PostViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final View ivDelete;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            CardView card = itemView.findViewById(R.id.root);
            imageView = itemView.findViewById(R.id.iv_card_post);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivDelete.setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.iv_card_video_tip).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.iv_card_video).setVisibility(View.VISIBLE);
            ViewUtils.throttleClick(card, v -> {
                if (listener != null) listener.itemChangePost(isVideo);
            });
            //delete video
            ViewUtils.throttleClick(ivDelete, v -> {
                if (isVideo) {
                    post = null;
                    data.clear();
                } else if (data.size() > 0) {
                    post = data.get(0);
                    data = data.subList(1, data.size());
                } else {
                    post = null;
                }
                notifyDataSetChanged();
            });
        }
    }

    public interface OnItemClickListener {
        void itemAdd(boolean isAdd);

        void itemChangePost(boolean isVideo);
    }
}