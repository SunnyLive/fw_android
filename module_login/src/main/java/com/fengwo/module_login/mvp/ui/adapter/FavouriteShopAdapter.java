package com.fengwo.module_login.mvp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.fengwo.module_login.R;

import java.util.List;

public class FavouriteShopAdapter extends RecyclerSwipeAdapter<BaseViewHolder> {

    List<String> data;

    public FavouriteShopAdapter(List<String> d) {
        data = d;

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.login_item_favourite_shop, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int i) {
        SwipeLayout swipeLayout = baseViewHolder.getView(R.id.swipe);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        baseViewHolder.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mItemManger.removeShownLayouts(swipeLayout);
//                mDataset.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, mDataset.size());
                mItemManger.closeAllItems();
//                Toast.makeText(view.getContext(), "Deleted " + viewHolder.textViewData.getText().toString() + "!", Toast.LENGTH_SHORT).show();
            }
        });
        mItemManger.bindView(baseViewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
}
