package com.fengwo.module_login.mvp.ui.activity

import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.fengwo.module_comment.ArouterApi
import com.fengwo.module_comment.base.BaseMvpActivity
import com.fengwo.module_login.R
import com.fengwo.module_comment.bean.BackpackDto
import com.fengwo.module_login.mvp.presenter.BackpackPresenter
import com.fengwo.module_login.mvp.ui.adapter.BackpackAdapter
import com.fengwo.module_login.mvp.ui.iview.IBackpackView
import kotlinx.android.synthetic.main.activity_my_backpack.*
import kotlinx.android.synthetic.main.layout_backpack.view.*

@Route(path = ArouterApi.BACKPACK_ACTION)
class MyBackpackActivity : BaseMvpActivity<IBackpackView,BackpackPresenter>(),IBackpackView{



    override fun initPresenter(): BackpackPresenter {
        return BackpackPresenter()
    }

    override fun getContentView(): Int {
        return R.layout.activity_my_backpack
    }

    override fun initView() {
        ToolBarBuilder().showBack(true)
                .setTitle("背包")
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .build()
        p.getBackpack()
    }

    override fun onBackpack(bp: BackpackDto) {
        val inflater:LayoutInflater = LayoutInflater.from(this@MyBackpackActivity)
        //礼物
        bp.gifts?.apply {
            if (isEmpty()) return@apply
            val giftView = inflater.inflate(R.layout.layout_backpack,ll_my_backpack_parent,false)
            giftView.tv_backpack_title.text = "礼物"
            setChildViewData(giftView)
            ll_my_backpack_parent.addView(giftView)
        }
        //座驾
        bp.motors?.apply {
            if (isEmpty()) return@apply
            val carView = inflater.inflate(R.layout.layout_backpack,ll_my_backpack_parent,false)
            carView.tv_backpack_title.text = "座驾"
            setChildViewData(carView)
            ll_my_backpack_parent.addView(carView)
        }
        //贵族
        bp.nobilities?.apply {
            if (isEmpty()) return@apply
            val vipView = inflater.inflate(R.layout.layout_backpack,ll_my_backpack_parent,false)
            vipView.tv_backpack_title.text = "贵族"
            setChildViewData(vipView)
            ll_my_backpack_parent.addView(vipView)
        }

    }


    private fun List<BackpackDto.GiftsBean>.setChildViewData(view: View) {
        ll_my_backpack_parent.findViewById<View>(R.id.backpack_empty)?.apply {
            ll_my_backpack_parent.removeView(this)
        }
        val giftAdapter = BackpackAdapter()
        view.findViewById<RecyclerView>(R.id.rv_backpack_list).apply {
            layoutManager = GridLayoutManager(this@MyBackpackActivity, 4)
            adapter = giftAdapter
            giftAdapter.bindToRecyclerView(this)
        }
        giftAdapter.setNewData(this)
        view.findViewById<CheckBox>(R.id.cb_backpack_more).apply {
            visibility = if(size < 8) View.GONE else View.VISIBLE
            setOnCheckedChangeListener { buttonView, isChecked ->
                giftAdapter.isMore = isChecked
                buttonView.text = if (isChecked) "收起" else "更多"
                giftAdapter.notifyDataSetChanged()
            }
        }
    }


}