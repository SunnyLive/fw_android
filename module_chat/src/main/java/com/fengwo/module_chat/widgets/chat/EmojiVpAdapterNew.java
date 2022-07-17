package com.fengwo.module_chat.widgets.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.fengwo.module_chat.R;

import java.util.Arrays;
import java.util.List;

public class EmojiVpAdapterNew extends PagerAdapter {

    private final List<String> strings = Arrays.asList(
            "[招呼]", "[害羞]", "[调皮]", "[委屈]", "[流汗]", "[敲你]", "[微笑]", "[坏笑]",
            "[抠鼻]", "[发火]", "[疑问]", "[惊讶]", "[低级]", "[耍酷]", "[尴尬]", "[流泪]",
            "[流汗]", "[晕倒]", "[偷笑]", "[鼓掌]", "[鼻滴]", "[好色]", "[傻笑]", "[亲亲]");
    private final List<Integer> emojis = Arrays.asList(
            R.drawable.e00, R.drawable.e01, R.drawable.e02, R.drawable.e03, R.drawable.e04, R.drawable.e05,
            R.drawable.e06, R.drawable.e07, R.drawable.e08, R.drawable.e09, R.drawable.e10, R.drawable.e11,
            R.drawable.e12, R.drawable.e13, R.drawable.e14, R.drawable.e15, R.drawable.e16, R.drawable.e17,
            R.drawable.e18, R.drawable.e19, R.drawable.e20, R.drawable.e21, R.drawable.e22, R.drawable.e23);

    @Override
    public int getCount() {
        return strings.size();
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
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.chat_emoji, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_emoji);
        EmojiAdapter emojiAdapter = new EmojiAdapter(R.layout.chat_emoji_item, emojis);
        emojiAdapter.setOnItemChildClickListener((adapter, view1, p) -> {
            String str = strings.get(p);
            if (null != l) l.onEmojiClick(str);
        });
        recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 7));
        recyclerView.setAdapter(emojiAdapter);
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
