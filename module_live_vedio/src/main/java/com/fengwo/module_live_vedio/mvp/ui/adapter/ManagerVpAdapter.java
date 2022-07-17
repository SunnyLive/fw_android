package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.BannedDto;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/23
 */
public class ManagerVpAdapter extends PagerAdapter {

    private List<BannedDto> stickList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private Context mContext;
    private int position;

    public ManagerVpAdapter(Context context,List<BannedDto> mListData,List<String>titleList) {
        this.mContext = context;
        this.titleList = titleList;
        this.stickList = mListData;
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        BaseQuickAdapter adapter = null;
        View empty = View.inflate(mContext, R.layout.item_base_empty_view, null);
        View v = View.inflate(mContext, R.layout.live_item_giftvp, null);
        RecyclerView rv = v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new ManagerAdapter(stickList);
        rv.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int channelId = ((BannedDto)adapter.getData().get(position)).getChannelId();
                int userId = ((BannedDto)adapter.getData().get(position)).getUserId();
                if (onItemClickLisenter!=null){
                    onItemClickLisenter.onChildClick(channelId,userId);
                }
            }
        });
        adapter.setEmptyView(empty);
        v.setTag(position);
        container.addView(v);
        return v;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if ((Integer)((View)object).getTag() == position) {
            return POSITION_NONE;
        }else {
            return super.getItemPosition(object);
        }
    }

    public void reFreshStickData(List<BannedDto> list, int position){
        stickList = list;
        this.position = position;
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (titleList.get(position)!=null){
            return titleList.get(position);
        }else {
            return super.getPageTitle(position);
        }
    }

    public interface OnChildItemClickListener{
        void onChildClick(int channelId,int userId);
    }
    private OnChildItemClickListener onItemClickLisenter;

    public void setOnItemClickLisenter(OnChildItemClickListener onItemClickLisenter) {
        this.onItemClickLisenter = onItemClickLisenter;
    }
}
