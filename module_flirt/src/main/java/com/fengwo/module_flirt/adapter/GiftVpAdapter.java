package com.fengwo.module_flirt.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;


import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.bean.GiftDto;

import java.util.ArrayList;
import java.util.List;

public class GiftVpAdapter extends PagerAdapter {

    private final static int DEAFULT_PAGE_SIZE = 8;

    private List<List<GiftDto>> mGifts;
    private Context mContext;
    private SparseArray<GiftAdapter> adapters;
    private boolean isFirst = true;

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
                if (i != 0 && (i + 1) % DEAFULT_PAGE_SIZE == 0 || i == gifts.size() - 1) {
                    mGifts.add(temp);
                    temp = new ArrayList<>();
                }

            }
        }
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
        View v = View.inflate(mContext, R.layout.flirt_item_giftvp, null);
        RecyclerView rv = v.findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(mContext, 4));
        if (null == adapters.get(position, null)) {
            GiftAdapter adapter = new GiftAdapter(mGifts.get(position));
            adapters.put(position, adapter);
        }
        if (isFirst) {
            adapters.get(position).setCheckPosition(0);
            isFirst = false;
        }
        rv.setAdapter(adapters.get(position));
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public GiftDto getGift(int position) {
        return adapters.get(position).getGift();
    }

    public void setCheck(int page, int position) {
        if (null == adapters || null == adapters.get(page))
            return;
        adapters.get(page).setCheckPosition(position);
    }
}
