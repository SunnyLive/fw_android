package com.fengwo.module_chat.widgets.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_chat.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EmojiVpAdapter extends PagerAdapter {

    private Context mContext;
    private int[] res;
    private List<List<Integer>> listRes;
    private List<List<String>> listStr;

    public EmojiVpAdapter(Context c, int[] res, String[] resStr) {
        this.res = res;
        mContext = c;
        listRes = new ArrayList<>();
        listStr = new ArrayList<>();
        List<Integer> l = new ArrayList<>();
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < res.length; i++) {//一页4行 一行7个 一页28个
            l.add(res[i]);
            strList.add(resStr[i]);
            if (l.size() == 28 || i == res.length - 1) {
                listRes.add(l);
                listStr.add(strList);
                l = new ArrayList<>();
                strList = new ArrayList<>();
            }

        }
    }

    @Override
    public int getCount() {
        return listRes.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_emoji, container, false);
        RecyclerView rv = view.findViewById(R.id.rv_emoji);
        rv.setLayoutManager(new GridLayoutManager(mContext, 7));
        EmojiAdapter a = new EmojiAdapter(R.layout.chat_emoji_item, listRes.get(position));
        a.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int p) {
                String str = listStr.get(position).get(p);
                if (null != l) {
                    l.onEmojiClick(str);
                }
            }
        });
        rv.setAdapter(a);
        container.addView(view);
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public interface onEmojiClickListener {
        void onEmojiClick(String str);
    }

    public onEmojiClickListener l;

    public void setOnEmojiClickListener(onEmojiClickListener l) {
        this.l = l;
    }
}
