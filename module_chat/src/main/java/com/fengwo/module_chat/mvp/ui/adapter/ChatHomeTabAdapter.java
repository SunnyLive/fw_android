package com.fengwo.module_chat.mvp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fengwo.module_chat.mvp.ui.fragment.ChatHomeChildFragment;
import com.fengwo.module_chat.mvp.ui.fragment.EmptyFragment;

import java.util.List;

public class ChatHomeTabAdapter extends FragmentPagerAdapter {

    private List<ChatHomeChildFragment> fragments;
    private boolean isEmpty = true;
    private EmptyFragment emptyView = new EmptyFragment(); // 解决没有tabItem时闪退的bug

    public ChatHomeTabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (isEmpty) return emptyView;
        else return fragments != null ? fragments.get(position) : null;
    }

    @Override
    public int getCount() {
        if (isEmpty) return 1;
        else return fragments != null ? fragments.size() : 0;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void setData(List<ChatHomeChildFragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
        notifyDataSetChanged();
    }

    public EmptyFragment getEmptyView() {
        return emptyView;
    }

    public List<ChatHomeChildFragment> getData() {
        return fragments;
    }
}
