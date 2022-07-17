package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.PendantBean;

import java.util.List;

/**
 * @anchor Administrator
 * @date 2020/8/31
 */
public class PendantAdapter extends BaseQuickAdapter<PendantBean, BaseViewHolder> {

    public PendantAdapter(@Nullable List<PendantBean> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<PendantBean>() {
            @Override
            protected int getItemType(PendantBean s) {
                return s.getOutType();
            }
        });

        getMultiTypeDelegate().registerItemType(PendantBean.TITLE_TYPE, R.layout.pendant_title_layout)
                             .registerItemType(PendantBean.PENDANT_TYPE,R.layout.pendant_content_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PendantBean item) {
        switch (helper.getItemViewType()){
            case PendantBean.TITLE_TYPE:
                helper.setText(R.id.tv_title,item.getTitle());
                break;
            case PendantBean.PENDANT_TYPE:
                ImageView view = helper.getView(R.id.image_view);
                ImageLoader.loadImg(view,item.getStickerUrl(),R.drawable.ic_logo);
                break;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = (GridLayoutManager)manager;
            gridManager.setSpanSizeLookup(new androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup() {
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    if (type==PendantBean.TITLE_TYPE) {
                        return 1;
                    } else if (type==PendantBean.PENDANT_TYPE) {
                        return gridManager.getSpanCount();
                    }else {
                        return 1;
                    }
                }
            });
        }

    }
}
