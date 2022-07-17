package com.fengwo.module_live_vedio.mvp.ui.pop

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import com.alibaba.android.arouter.launcher.ARouter
import com.fengwo.module_comment.ArouterApi
import com.fengwo.module_comment.base.RxHttpUtil
import com.fengwo.module_comment.utils.*
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.api.LiveApiService
import com.fengwo.module_live_vedio.mvp.dto.RefreshBackpack
import kotlinx.android.synthetic.main.pop_newyear_activity.view.*
import razerdp.basepopup.BasePopupWindow

/** 背包赠送给主播礼物
 * @author chenshanghui
 * @intro
 * @date 2019/10/7
 */
class GivingGiftsPopWindow(private val context: Context) : BasePopupWindow(context) {


    private var mAnchorId:String = ""
    private var mAnchorName:String = ""
    private var mAnchorIcon:String = ""
    private var mGiftId:String = ""
    private var mGiftIcon:String = ""

    private fun initData() {
        contentView?.apply {
            im_clone.setOnClickListener {
                dismiss()
            }
            tv2.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (!TextUtils.isEmpty(s)) {
                        val number = Integer.valueOf(s.toString())
                        val maxNumber = if (tv4.tag != null) Integer.valueOf(tv4.tag.toString()) else 0
                        if(number > maxNumber) tv2.setText("$maxNumber")
                        tv2.setSelection(tv2.text.length)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })
            tvSearch.setOnClickListener {
                ARouter.getInstance().build(ArouterApi.SEARCH_LIVE_ACTION)
                        .withBoolean("activeChoose",true)
                        .navigation(context as Activity,10086)
            }
            tv_send_gift.setOnClickListener {

                if (TextUtils.isEmpty(tv2.text)) {
                    ToastUtils.show(context,"请输入赠送数量",300)
                    return@setOnClickListener
                }
                RetrofitUtils().createApi(LiveApiService::class.java).requestGiving(RxHttpUtil().ParamsBuilder()
                        .put("id",mGiftId)
                        .put("timestamp","${System.currentTimeMillis()}")
                        .put("quantity",tv2.text.toString())
                        .put("targetId",mAnchorId)
                        .build()).compose(RxUtils.applySchedulers())
                        .subscribe {
                            if (it.isSuccess) {
                                GivingGiftsSuccessPopWindow(context,mAnchorIcon,mAnchorName,mGiftIcon,tv2.text.toString())
                                        .showPopupWindow()
                                dismiss()
                                RxBus.get().post(RefreshBackpack())
                            }
                        }
            }
        }

    }


    fun setData(anchorId: String,anchorName:String,anchorIcon:String){
        this.mAnchorId = anchorId
        this.mAnchorName = anchorName
        this.mAnchorIcon = anchorIcon
        contentView?.apply {
            ImageLoader.loadImg(iv_user_header, anchorIcon)
            tvName.text = anchorName
        }
    }

    @SuppressLint("SetTextI18n")
    fun setData(anchorId:String,img: String, name: String, icon: String, number: String,giftId:String){
        this.mAnchorId = anchorId
        this.mGiftId = giftId
        this.mGiftIcon = icon
        this.mAnchorIcon = img
        this.mAnchorName = name
        contentView?.apply {
            ImageLoader.loadImg(iv_user_header, img)
            tvName.text = name
            ImageLoader.loadImg(im3,icon)
            tv4.text = "x$number"
            tv4.tag = number
        }
    }

    override fun onCreateShowAnimation(): Animation {
        return getTranslateVerticalAnimation(1f, 0f, 300)
    }

    override fun onCreateDismissAnimation(): Animation {
        return getTranslateVerticalAnimation(0f, 1f, 300)
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.pop_newyear_activity)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    init {
        popupGravity = Gravity.CENTER
        initData()
    }


}