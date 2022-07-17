package com.fengwo.module_chat.mvp.ui.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.ChatCardBean;
import com.fengwo.module_comment.utils.ImageLoader;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.List;

public class ChatCardAdapter extends BaseQuickAdapter<ChatCardBean, BaseViewHolder> {

    private SparseArray<ChatCardThumbAdapter> thumbAdapters;
    private SparseArray<ChatCardPicAdapter> picAdapters;
    PagerSnapHelper pagerSnapHelper;
    private int myId;

    public ChatCardAdapter(@Nullable List<ChatCardBean> data, int myId) {
        super(R.layout.chat_fragment_card, data);
        this.myId = myId;
        thumbAdapters = new SparseArray<>();
        picAdapters = new SparseArray<>();
        pagerSnapHelper = new PagerSnapHelper();

    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChatCardBean item) {
        int position = helper.getAdapterPosition();
        helper.setText(R.id.tv_card_name, "@" + item.nickname);
        helper.setText(R.id.tv_card_content, item.excerpt);
        ImageLoader.loadImg(helper.getView(R.id.iv_header), item.headImg);
        helper.setText(R.id.tv_chat_like, item.likes);
        helper.setText(R.id.tv_chat_comment, item.comments);
        ImageView view = helper.getView(R.id.iv_add_attention);
        helper.addOnClickListener(R.id.iv_add_attention, R.id.view_like, R.id.iv_chat_comment, R.id.iv_header, R.id.tvShare, R.id.tvCardSetting);
        if (item.isAttention.equals("1")) {
            helper.setVisible(R.id.iv_add_attention, false);
            view.setSelected(true);
        } else {
            if (item.userId.equals(myId + "")) {
                helper.setVisible(R.id.iv_add_attention, false);
            } else {
                helper.setVisible(R.id.iv_add_attention, true);
            }
            view.setSelected(false);
        }

        ImageView ivLike = helper.getView(R.id.view_like);
        if (item.isLike.equals("1")) {
            ivLike.setSelected(true);
        } else {
            ivLike.setSelected(false);
        }
        if (item.type.equals("1")) {//图片资源
            helper.setVisible(R.id.view_picture, true);
            helper.setVisible(R.id.view_video, false);
            helper.setVisible(R.id.iv_default, false);
            RecyclerView rvThumb = helper.getView(R.id.rvPictureThumb);
            rvThumb.setLayoutManager(new LinearLayoutManager(mContext));
            ChatCardThumbAdapter adapter;
//            if (thumbAdapters.get(helper.getAdapterPosition()) == null) {
//
//                thumbAdapters.put(position, adapter);
//            } else {
//                adapter = thumbAdapters.get(position);
//            }
            adapter = new ChatCardThumbAdapter(item.imgContent);
            rvThumb.setAdapter(adapter);
            RecyclerView rvPic = helper.getView(R.id.rvPicture);
            pagerSnapHelper.attachToRecyclerView(rvPic);
            rvPic.setLayoutManager(new LinearLayoutManager(mContext));
            ChatCardPicAdapter picAdapter = new ChatCardPicAdapter(item.imgContent);
//            if (picAdapters.get(position) == null) {
//                picAdapter = new ChatCardPicAdapter(item.imgContent);
//                picAdapters.put(position, picAdapter);
//
//            } else {
//                picAdapter = picAdapters.get(position);
//            }
            rvPic.setAdapter(picAdapter);
            rvPic.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                        int p = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                        adapter.setSelectedPosition(p);
                    }
                }
            });
        } else {
            ImageView iv = helper.getView(R.id.iv_default);
            ImageLoader.loadImg(iv, item.covers);
            helper.setVisible(R.id.view_picture, false);
            helper.setVisible(R.id.view_video, true);
            if (iv.getVisibility() == View.VISIBLE)//todo 默认是显示的 如果变成了隐藏的状态 那就不需要再显示出来 防止刷新又重新覆盖了
                helper.setVisible(R.id.iv_default, true);

        }

        helper.setVisible(R.id.tvCardSetting, item.userId.equals(myId + ""));
    }

    public void hideDefaultImg(int position) {
        ImageView iv = (ImageView) getViewByPosition(position, R.id.iv_default);
        if (null != iv) {
            iv.setVisibility(View.GONE);
        }
    }

    public TXCloudVideoView getVideo(int preIndex) {
        TXCloudVideoView view = (TXCloudVideoView) getViewByPosition(preIndex, R.id.cloud_video_view);
        return view;
    }

    public void setAttentionOk(int position) {
        View v = getViewByPosition(position, R.id.iv_add_attention);
        if (null != v) {
            v.setSelected(true);
        }
    }

    public void setLike(boolean isLike, int position) {
        ImageView ivLike = (ImageView) getViewByPosition(position, R.id.view_like);
        TextView tvNum = (TextView) getViewByPosition(position, R.id.tv_chat_like);
        if (ivLike == null || tvNum == null) return;
        ivLike.setSelected(isLike);
        int likes = Integer.parseInt(getItem(position).likes);
        tvNum.setText(likes + "");
    }

    public void setAttention(boolean isAttention, int position) {
        View v = getViewByPosition(position, R.id.iv_add_attention);
        if (null != v) {
            v.setVisibility(isAttention ? View.GONE : View.VISIBLE);
        }
    }

    public void showVedio(int position) {
        View pic = getViewByPosition(position, R.id.view_picture);
        View vedio = getViewByPosition(position, R.id.view_video);
        if (null == pic || vedio == null) return;
        pic.setVisibility(View.GONE);
        vedio.setVisibility(View.VISIBLE);
    }

    public void showDefaultImg(int preIndex) {
        ImageView iv = (ImageView) getViewByPosition(preIndex, R.id.iv_default);
        if (null != iv) {
            iv.setVisibility(View.VISIBLE);
        }
    }
}
