package com.fengwo.module_live_vedio.mvp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @anchor Administrator
 * @date 2020/12/26
 */
public class LoopViewPagerAdapter2 extends BaseQuickAdapter<Fragment, BaseViewHolder> {
    public LoopViewPagerAdapter2(int layoutResId, @Nullable List<Fragment> data) {
        super(layoutResId, data);
    }
    @Override
    public int getItemCount() {

        return Integer.MAX_VALUE;
    }
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Fragment item) {


    }
    //重写getItem以免出现无限滑动时当position大于data的size时获得对象为空
    @Nullable
    @Override
    public Fragment getItem(int position) {
        int newPosition = position % getData().size();
        return getData().get(newPosition);
    }
    //重写此方法，因为BaseQuickAdapter里绘制view时会调用此方法判断，position减去getHeaderLayoutCount小于data.size()时才会调用调用cover方法绘制我们自定义的view
    @Override
    public int getItemViewType(int position) {
        int count = getHeaderLayoutCount() + getData().size();
        //刚开始进入包含该类的activity时,count为0。就会出现0%0的情况，这会抛出异常，所以我们要在下面做一下判断
        if (count <= 0) {
            count = 1;
        }
        int newPosition = position % count;

        return super.getItemViewType(newPosition);
    }
}
