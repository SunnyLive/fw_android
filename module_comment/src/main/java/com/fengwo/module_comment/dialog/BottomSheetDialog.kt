package com.fengwo.module_comment.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.fengwo.module_comment.R
import com.fengwo.module_comment.adapter.BottomSheetAdapter
import com.fengwo.module_comment.widget.CommonItemDecoration
import kotlinx.android.synthetic.main.dialog_bottom_sheet_common.view.*
import razerdp.basepopup.BasePopupWindow

/**
 * 通用列表弹出
 *
 * @Author gukaihong
 * @Time 2020/12/31
 */
class BottomSheetDialog(val c: Context) : BasePopupWindow(c) {

    private var mOnItemClickListen: OnItemClickListen? = null

    private var mBottomSheetAdapter: BottomSheetAdapter? = null

    override fun onCreateContentView(): View = createPopupById(R.layout.dialog_bottom_sheet_common)

    override fun onViewCreated(contentView: View?) {
        super.onViewCreated(contentView)
        showAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_dialog_anim_in)
        dismissAnimation = AnimationUtils.loadAnimation(context, R.anim.bottom_dialog_anim_out)
        setBackgroundColor(context.getColor(R.color.alpha_50_black))
        popupGravity = Gravity.BOTTOM
        contentView?.apply {
            tv_cancel.setOnClickListener {
                dismiss()
            }
            layout_bg.setOnClickListener {
                dismiss()
            }
            rv_view.layoutManager = LinearLayoutManager(context)
            rv_view.addItemDecoration(CommonItemDecoration())
            mBottomSheetAdapter = BottomSheetAdapter()
            mBottomSheetAdapter?.let {
                it.bindToRecyclerView(rv_view)
                it.setOnItemChildClickListener { _, _, position ->
                    mOnItemClickListen?.onItemClick(position)
                    dismiss()
                }
            }

        }
    }

    fun show(view: View?, items: List<String>) {
        mBottomSheetAdapter?.setNewData(items)
        showPopupWindow(view)
    }


    fun setOnItemClick(onItemClick: OnItemClickListen): BottomSheetDialog {
        mOnItemClickListen = onItemClick
        return this
    }

    interface OnItemClickListen {
        fun onItemClick(position: Int)
    }


    override fun onDestroy() {
        super.onDestroy()
        mOnItemClickListen = null
        mBottomSheetAdapter = null
    }
}