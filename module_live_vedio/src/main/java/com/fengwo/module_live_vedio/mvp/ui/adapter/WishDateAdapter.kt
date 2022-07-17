package com.fengwo.module_live_vedio.mvp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.WishingWallDto
import kotlinx.android.synthetic.main.layout_wish_item_date.view.*

class WishDateAdapter(mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val mInflate: LayoutInflater by lazy { LayoutInflater.from(mContext) }

    private val mData by lazy { ArrayList<WishingWallDto.ResidueProcessesBean>() }

    private val NORMAL = 0x000001
    private val MORE = 0x000002

    fun updateData(data:List<WishingWallDto.ResidueProcessesBean>){
        if (mData.isNotEmpty()) mData.clear()
        mData.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            NORMAL -> NormalHolder(mInflate.inflate(R.layout.layout_wish_item_date, parent,false))
            MORE -> MoreHolder(mInflate.inflate(R.layout.layout_wish_item_date_more, parent,false))
            else -> NormalHolder(mInflate.inflate(R.layout.layout_wish_item_date, parent,false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mData.size > 1 && position == mData.lastIndex - 1) MORE else NORMAL
    }

    private var isAddEmpty:Boolean = true
    override fun getItemCount(): Int {
        return if (mData.size > 1) mData.apply {
            if (isAddEmpty) {
                add(lastIndex, WishingWallDto.ResidueProcessesBean())
                isAddEmpty = false
            }
        }.size else mData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val date = mData[position]
        when (getItemViewType(position)) {
            NORMAL -> {
                val nh = holder as NormalHolder
                val ls = nh.v.layoutParams as ViewGroup.MarginLayoutParams
                ls.bottomMargin = nh.v.resources.getDimension(R.dimen.dp_10).toInt()
                if (getItemViewType(position + 1) == MORE) {
                    ls.bottomMargin = 0
                }
                nh.v.layoutParams = ls
                nh.setText("${date.begin}  -  ${date.end}")
            }

        }
    }

    inner class NormalHolder(v: View) : RecyclerView.ViewHolder(v) {
        val v = v
        fun setText(date:String){
            v.tv_start_time.text = date
        }
    }

    inner class MoreHolder(v: View) : RecyclerView.ViewHolder(v)

}