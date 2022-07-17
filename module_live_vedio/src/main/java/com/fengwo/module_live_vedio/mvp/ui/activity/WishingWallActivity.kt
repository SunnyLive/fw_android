/*
* 这个页面是许愿的结果处理页面
* 当然这个页面也不用做什么花里胡哨的东西
* 无非就是一个结果集的展示页面
* 但是这个页面要提供一个外部调用的接口
* 方便别的模块调用到这个页面
*
* 许愿结果  许愿成功 许愿结束
* */

package com.fengwo.module_live_vedio.mvp.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fengwo.module_comment.ArouterApi
import com.fengwo.module_comment.base.BaseMvpActivity
import com.fengwo.module_comment.utils.ImageLoader
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.BlessingDto
import com.fengwo.module_live_vedio.mvp.dto.WishingWallDto
import com.fengwo.module_live_vedio.mvp.presenter.WishResultPresenter
import com.fengwo.module_live_vedio.mvp.ui.adapter.WishingWallAdapter
import com.fengwo.module_live_vedio.mvp.ui.iview.IWishResultView
import com.fengwo.module_live_vedio.mvp.ui.pop.SharePopwindow
import kotlinx.android.synthetic.main.activity_wishing_wall.*
import kotlinx.android.synthetic.main.layout_wish_blessing_dlalog.view.*
import kotlinx.android.synthetic.main.layout_wish_blessing_item.view.*
import kotlinx.android.synthetic.main.layout_wish_result_dialog.view.*
import kotlinx.android.synthetic.main.layout_wish_vows_dialog.view.*
import kotlinx.android.synthetic.main.layout_wishing_wall_item.view.*
import razerdp.basepopup.BasePopupWindow


@Route(path = ArouterApi.WISHING_WALL_ACTION)
class WishingWallActivity : BaseMvpActivity<IWishResultView, WishResultPresenter>(), IWishResultView {


    private val mAdapter by lazy { WishingWallAdapter() }
    private val mWishBlessingDialog by lazy { WishBlessingDialog(this) }
    private val mWishVowsDialog by lazy { WishVowsDialog(this) }

    override fun initPresenter(): WishResultPresenter {
        return WishResultPresenter()
    }

    override fun initView() {
        initData()
        iv_wish_result_back.setOnClickListener {
            onBackPressed()
        }
        rv_wish_result_content.adapter = mAdapter
        rv_wish_result_content.layoutManager = LinearLayoutManager(this)
        mAdapter.bindToRecyclerView(rv_wish_result_content)
        //马上许愿
        mAdapter.setWishOnClickListener { d, view, i ->
            mWishVowsDialog.showPopupWindow()
            mWishVowsDialog.contentView?.apply {
                tv_blessing_anchor_name.text = d.anchorName
                tv_blessing_anchor_name.tag = d.anchorId
                iv_wish_btn.tag = d.anchorHeadImg
                tag = arrayOf(d,view,i)
            }

         }
        //分享
        iv_wish_result_share.setOnClickListener {
            showShareDialog()
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_wishing_wall
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //获取主播的信息
        if (requestCode == 10086 && resultCode == Activity.RESULT_OK) {
            data?.let {
                mWishVowsDialog.contentView?.apply {
                    val anchor = it.getStringArrayExtra("anchor")
                    anchor?.apply {
                        tv_blessing_anchor_name.text = get(1) //名字
                        tv_blessing_anchor_name.tag = get(0) //id
                        iv_wish_btn.tag = get(2) //头像
                    }
                }
            }
        }
    }


    fun initData(){
        p.getWishList()
        p.getBlessing(1)
    }



    /**
     * 微信分享
     */
    private fun showShareDialog() {
        SharePopwindow(this,0,false).showPopupWindow()
    }


    ////////////////////////////////////
    // 点击许愿按钮 弹出要许愿的主播
    // 并选择祝福语言
    ////////////////////////////////////
    inner class WishVowsDialog(mContext: Context) : BasePopupWindow(mContext) {

        init {
            popupGravity = Gravity.BOTTOM
            contentView.apply {
                tv_blessing_content.setOnClickListener {
                    mWishBlessingDialog.showPopupWindow()
                }
                tv_blessing_anchor_name.setOnClickListener {
                    ARouter.getInstance().build(ArouterApi.SEARCH_LIVE_ACTION)
                            .withBoolean("activeChoose",true)
                            .navigation(this@WishingWallActivity,10086)
                }
                //马上许愿的操作处理 请求接口等操作
                iv_wish_btn.setOnClickListener {
                    if (TextUtils.isEmpty(tv_blessing_anchor_name.text)) {
                        toastTip("请选择主播")
                        return@setOnClickListener
                    }

                    if (TextUtils.isEmpty(tv_blessing_content.text)) {
                        toastTip("请选择祝福语")
                        return@setOnClickListener
                    }
                    val objs =  mWishVowsDialog.contentView.tag as Array<*>
                    val data = objs[0] as  WishingWallDto.HourProcessesBean
                    val anchorId = tv_blessing_anchor_name.tag as String
                    p.requestWishing(anchorId,data.dateHour,tv_blessing_content.text.toString())
                    dismiss()
                }
            }

        }

        override fun showPopupWindow() {
            super.showPopupWindow()
            contentView.tv_blessing_content?.text = ""
        }

        override fun onCreateContentView(): View {
            return createPopupById(R.layout.layout_wish_vows_dialog)
        }


        override fun onCreateShowAnimation(): Animation? {
            return getTranslateVerticalAnimation(1f, 0f, 300)
        }

        override fun onCreateDismissAnimation(): Animation? {
            return getTranslateVerticalAnimation(0f, 1f, 300)
        }
    }


    ////////////////////////////////////
    // 点击许愿结果后弹出结果集页面的弹框
    ////////////////////////////////////
    inner class WishResultDialog(mContext: Context) : BasePopupWindow(mContext) {

        init {
            mWishVowsDialog.contentView?.let {it->
                contentView?.apply {
                    val objs =  it.tag as Array<*>
                    val data = objs[0] as  WishingWallDto.HourProcessesBean
                    val position = objs[2] as Int
                    val view = objs[1] as View
                    val anchorIcon = it.iv_wish_btn.tag as String
                    ImageLoader.loadImg(civ_wish_anchor_header,anchorIcon)
                    ImageLoader.loadImg(civ_wish_user_header,data.userHeadImg)
                    tv_wish_result_ctx.text = it.tv_blessing_content.text.toString()
                    iv_wish_result_btn.setOnClickListener {_->
                        view.tv_wish_fans_blessing.text = tv_wish_result_ctx.text.toString()
                        @SuppressLint("SetTextI18n")
                        view.tv_wish_fans_blessing_date.text = "★ ${data.begin} - ${data.end} ★"
                        mAdapter.data[position].content = tv_wish_result_ctx.text.toString()
                        mAdapter.data[position].anchorHeadImg = anchorIcon
                        mAdapter.data[position].anchorName = it.tv_blessing_anchor_name.text.toString()
                        mAdapter.data[position].anchorId = it.tv_blessing_anchor_name.tag as String
                        dismiss()
                        mAdapter.runAnimatorMatching(view,position)
                    }
                }
            }
        }

        override fun onCreateContentView(): View {
            return createPopupById(R.layout.layout_wish_result_dialog)
        }

    }


    ///////////////////////////////////
    // 这是祝福语的弹框
    ///////////////////////////////////
    inner class WishBlessingDialog(mContext: Context) : BasePopupWindow(mContext) {

        val mAdapter by lazy {
            object : BaseQuickAdapter<BlessingDto.RecordsBean?, BaseViewHolder>(R.layout.layout_wish_blessing_item) {
                override fun convert(helper: BaseViewHolder, item: BlessingDto.RecordsBean?) {
                    item?.apply {
                        val position = helper.adapterPosition
                        helper.itemView.isSelected = position % 2 == 0
                        helper.itemView.tv_wish_blessing_content.text = content
                    }
                }
            }
        }

        init{
            popupGravity = Gravity.BOTTOM
            contentView.rv_wish_blessing.layoutManager = LinearLayoutManager(mContext)
            contentView.rv_wish_blessing.adapter = mAdapter
            mAdapter.setOnItemClickListener { adapter, _, position ->
                val data = adapter.data[position] as BlessingDto.RecordsBean
                mWishVowsDialog.contentView.tv_blessing_content?.apply {
                    text = data.content
                    tag = data.id
                    dismiss()
                }
            }
        }

        override fun onCreateContentView(): View {
            return createPopupById(R.layout.layout_wish_blessing_dlalog)
        }

        override fun onCreateShowAnimation(): Animation? {
            return getTranslateVerticalAnimation(1f, 0f, 300)
        }

        override fun onCreateDismissAnimation(): Animation? {
            return getTranslateVerticalAnimation(0f, 1f, 300)
        }

    }




    /////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
    //////////////////////             这里是接口的回调           /////////////////////////
    //////////////////////             这里是接口的回调           /////////////////////////
    //////////////////////             这里是接口的回调           /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    override fun onWishingSuccess() {
        WishResultDialog(this).showPopupWindow()
    }

    override fun onWishList(data: WishingWallDto) {
        data.hourProcesses[0].dateList?.apply {
            addAll(data.residueProcesses)
        }
        mAdapter.setNewData(data.hourProcesses)
    }

    override fun onBlessing(data: BlessingDto) {
        if (mWishBlessingDialog.mAdapter.data.isEmpty()) {
            mWishBlessingDialog.mAdapter.setNewData(data.records)
        }
    }


}