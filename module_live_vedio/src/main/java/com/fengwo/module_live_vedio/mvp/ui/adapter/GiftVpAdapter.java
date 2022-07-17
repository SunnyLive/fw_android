package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;

import java.util.ArrayList;
import java.util.List;

public class GiftVpAdapter extends PagerAdapter {

    private final static int DEAFULT_PAGE_SIZE = 8;

    private List<List<GiftDto>> mGifts;
    private Context mContext;
    private SparseArray<GiftAdapter> adapters;//rv
    private boolean isFirst = true;
    private boolean hasInit =false;
    private int page;
    private int pos;
    private boolean mIsCheckUpLevel = false;   //设置是否选择升级礼物
    public GiftVpAdapter(Context c, List<GiftDto> gifts) {
        mGifts = new ArrayList<>();
        mContext = c;
        adapters = new SparseArray<>();
        if (gifts.size() <= DEAFULT_PAGE_SIZE) {
            mGifts.add(gifts);
        } else {
            List<GiftDto> temp = new ArrayList<>();
            for (int i = 0; i < gifts.size(); i++) {
                temp.add(gifts.get(i));
                if (i != 0 && (i + 1) % 8 == 0 || i == gifts.size() - 1) {
                    mGifts.add(temp);
                    temp = new ArrayList<>();
                }

            }
        }
    }


    public void onItemClickListener(BaseQuickAdapter adapter1, View view, int position){

    }


    @Override
    public int getCount() {
        return mGifts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = View.inflate(mContext, R.layout.live_item_giftvp, null);
        RecyclerView rv = v.findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(mContext, 4));
        if (null == adapters.get(position, null)) {
            GiftAdapter adapter = new GiftAdapter(container.getContext(),mGifts.get(position));
            adapter.setOnItemChildClickListener(this::onItemClickListener);
            adapters.put(position, adapter);
        }

        if (isFirst) {
            adapters.get(position).setCheckPosition(0);
            isFirst = false;
        }

        rv.setAdapter(adapters.get(position));
        //指定位置
        if (hasInit&&null != adapters.get(page, null)) {
            L.e("=== page "+page);
            L.e("=== pos "+pos);
            Log.d("lucas","page:"+page+",position:"+pos);
            adapters.get(page).setCheckPosition1(pos);
            rv.setAdapter(adapters.get(page));
            hasInit= false;
        }
        if(mIsCheckUpLevel){
            Log.d("lucas","mIsCheckUpLevel");
            setCheck(0,1);
            notifyDataSetChanged();
        }
        container.addView(v);
        return v;
    }




    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public GiftDto getGift(int position) {
        Log.e("tag",adapters.size()+"");
        if (adapters.get(position)==null)return null;
        return adapters.get(position).getGift();
    }

    public void setInitPos(boolean hasInit,int page, int pos) {
        this.page = page;
        this.pos = pos;
        this.isFirst = false;
        this.hasInit = hasInit;
    }


    public int getCheckPosition(){
        return pos;
    }

    /**
     * 设置选中第二个升级礼物
     */
    public void setCheckUpLevel(boolean isCheckUpLevel){
        mIsCheckUpLevel = isCheckUpLevel;
        notifyDataSetChanged();
    }

    public void setCheck(int page, int position) {
        if (null == adapters || null == adapters.get(page))
            return;
        adapters.get(page).setCheckPosition(position);
        notifyDataSetChanged();
    }
    public void setChecks(int page, int position) {
        if (null == adapters || null == adapters.get(page))
            return;
        adapters.get(page).setCheckPositions(position);

    }

}
