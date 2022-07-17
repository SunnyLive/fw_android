package com.fengwo.module_live_vedio.mvp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fengwo.module_comment.bean.ZhuboDto
import com.fengwo.module_comment.utils.KLog
import com.fengwo.module_live_vedio.mvp.ui.fragment.LivingRoomFragment.Companion.newInstance

class LivingRoomPageAdapter(fm: FragmentActivity, var data: List<ZhuboDto>) : FragmentStateAdapter(fm) {

    //override fun getItemCount(): Int = data.size

    //    override fun createFragment(position: Int): Fragment = fragmentList[position]

    //
    override fun getItemCount(): Int = Int.MAX_VALUE

    //
//
    override fun createFragment(position: Int): Fragment {
//        fragmentList?.let {
//            return it[position % it.size]
//        }
        KLog.e("tage","position= "+ position +"size="+data.size)
        if(position==0||data.size==0){
            return Fragment()
        }
        return newInstance(data[position%data.size])

    }

//    override fun getItemId(position: Int): Long {
//        var pos = 0L
//        fragmentList?.let {
//            pos = (position % it.size).toLong()
//        }
//        return pos
//    }
//
//    fun removeFragment(position: Int) {
//        fragmentList?.removeAt(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, fragmentList?.size ?: 0)
//        notifyDataSetChanged()
//    }
//
//    fun destroy() {
//        fragmentList?.clear()
//        fragmentList = null;
//    }
}