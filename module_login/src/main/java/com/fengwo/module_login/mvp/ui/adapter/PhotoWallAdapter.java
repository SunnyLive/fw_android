package com.fengwo.module_login.mvp.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.utils.ViewUtils;
import com.fengwo.module_login.R;

import java.util.ArrayList;
import java.util.List;

public class PhotoWallAdapter extends BaseQuickAdapter<UserInfo.UserPhotosList, BaseViewHolder> {

    public static final int MAX_COUNT_IMAGE = 6;

    private final int itemSize;
    private List<UserInfo.UserPhotosList> data = new ArrayList<>();
    private UserInfo.UserPhotosList post;
    private OnItemClickListener listener;

    public PhotoWallAdapter(Context context) {
        super(R.layout.item_photo_wall);
        itemSize = (ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, MAX_COUNT_IMAGE + 1)) / MAX_COUNT_IMAGE;
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, UserInfo.UserPhotosList item) {
        TextView tvCoverTip = helper.getView(R.id.tv_cover_tip);
        TextView tvStatus = helper.getView(R.id.tv_status);
        tvCoverTip.setVisibility(helper.getAdapterPosition() == 0 ? View.VISIBLE : View.GONE);
        if (item.photoStatus == 0) {   //审核中
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText("审核中");
            tvStatus.setTextColor(Color.parseColor("#AAFFBE"));
        } else if (item.photoStatus == 1) {   //审核通过
            tvStatus.setVisibility(View.GONE);
        } else if (item.photoStatus == 2) {   //审核失败
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText("未通过审核");
            tvStatus.setTextColor(Color.parseColor("#FFEC91"));
        }
        if (TextUtils.equals("ADD",item.photoUrl)) {
            tvStatus.setVisibility(View.GONE);
            tvCoverTip.setVisibility(View.GONE);
            helper.setGone(R.id.tv_null,true);
            ImageLoader.loadImg(helper.getView(R.id.iv_card_post), R.drawable.ic_upload_photo_wall);
        }else{
            helper.setGone(R.id.tv_null,false);
            ImageLoader.loadImg(helper.getView(R.id.iv_card_post), item.photoUrl);
        }
        ViewUtils.throttleClick(helper.getView(R.id.cl_item), v -> {
            if (listener != null) {
                if (TextUtils.equals("ADD",item.photoUrl)) {
                    listener.itemAdd();
                } else {
                    listener.itemClick(getData(), helper.getAdapterPosition());
                }

            }

        });
    }


    // 判断数据是否为空
    public boolean isDataEmpty() {
        return (data.size() == 0) && (post == null);
    }

    // 删除项目
    private void deleteDataItem(int position) {
        if (position >= data.size()) return;
        data.remove(position);
        notifyDataSetChanged();
    }



    public void setListener(OnItemClickListener l) {
        listener = l;
    }


    public interface OnItemClickListener {
        void itemAdd();

        void itemClick(List<UserInfo.UserPhotosList> data, int position);

    }
}