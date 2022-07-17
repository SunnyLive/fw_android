package com.fengwo.module_login.mvp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseViewHolder;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_login.R;
import com.fengwo.module_login.mvp.dto.BlackDto;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class BlackListAdapter extends RecyclerSwipeAdapter<BaseViewHolder> {

    List<BlackDto> data;
    public BlackListAdapter(List<BlackDto> d) {
        data = d;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.login_item_blacklist, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder baseViewHolder, int i) {
        SwipeLayout swipeLayout = baseViewHolder.getView(R.id.swipe);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        TextView view = baseViewHolder.getView(R.id.tv_name);
        CircleImageView ivHeader = baseViewHolder.getView(R.id.iv_header);
        ImageLoader.loadImg(ivHeader, data.get(i).headImg);
        view.setText(data.get(i).nickname);
        baseViewHolder.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null) listener.delete(data.get(baseViewHolder.getAdapterPosition()).blackUserId,baseViewHolder.getAdapterPosition());
                mItemManger.closeAllItems();
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

    public  DeleteBlackListener listener;
    public interface DeleteBlackListener{
        void delete(int userId,int pos);
    }
    public void setDeleteBlack( DeleteBlackListener deleteBlackListener){
        listener = deleteBlackListener;
    }
}
