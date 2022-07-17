package com.fengwo.module_live_vedio.mvp.ui.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import com.fengwo.module_comment.base.BaseActivity
import com.fengwo.module_comment.base.HttpResult
import com.fengwo.module_comment.base.LoadingObserver
import com.fengwo.module_comment.event.PaySuccessEvent
import com.fengwo.module_comment.iservice.UserInfo
import com.fengwo.module_comment.utils.*
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.api.LiveApiService
import com.fengwo.module_live_vedio.eventbus.ShowBuyGuardPopEvent
import com.fengwo.module_live_vedio.mvp.dto.GuardListDto
import com.fengwo.module_live_vedio.mvp.ui.activity.zhubo.LivePushActivity
import com.fengwo.module_live_vedio.mvp.ui.adapter.ShouhuListAdapter
import com.fengwo.module_live_vedio.utils.MaxHeightLayoutmanager
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.live_pop_list_shouhu.view.*
import razerdp.basepopup.BasePopupWindow

class ShouhuListPopwindow(var context: Context, isHost: Boolean, from: Int) : BasePopupWindow(context) {
    private var adapter: ShouhuListAdapter? = null
    var from = 0
    var isHost = false
    private var mCompositeDisposable: CompositeDisposable? = null
    override fun showPopupWindow() {
        super.showPopupWindow()
        RxBus.get().post(PaySuccessEvent(""))
    }

    init {
        popupGravity = Gravity.BOTTOM
        //setData(isHost, from)
        mCompositeDisposable = CompositeDisposable()
    }

    fun setData(isHost: Boolean, from: Int, data: UserInfo) {

        this.isHost = isHost
        this.from = from

        getContentView().rv.layoutManager = MaxHeightLayoutmanager(context)
        if (from != 0) {
            //用户等级的徽章
            data?.let {

                getContentView().btn_submit.setOnClickListener { view: View? ->

                    if (BaseActivity.isForeground(context, "LivingRoomActivity")||BaseActivity.isForeground(context, "LivePushActivity")) {
                        RxBus.get().post(ShowBuyGuardPopEvent(from,data,false))
                    }else{
                        RxBus.get().post(ShowBuyGuardPopEvent(from,data,true))
                    }

                    dismiss()
                }
                ImageLoader.loadImg(getContentView().ivHeader, data?.headImg)
                if (it.myIsGuard) getContentView().btn_submit!!.text = "续费" else getContentView().btn_submit!!.text = "立即开通守护"
                getContentView().btn_submit.visibility = if (isHost) View.GONE else View.VISIBLE
                //  getContentView().tvEmpty.visibility = if (isHost) View.VISIBLE else View.GONE
                getContentView().tv_tile_name.text = data?.nickname
                if (isHost) {
                    getContentView().tvName.text = "守护军团:"
                    getContentView().tvTips.visibility = View.GONE
                    getContentView().tvSubTitle.visibility = View.INVISIBLE
                    getContentView().tvContent.visibility = View.INVISIBLE
                } else {
                    getContentView().tvName.text = String.format("%s的守护军团: ", data?.nickname)
                }

                //主播等级的徽章
                getContentView().iv_anchor_level_badge.apply {
                    if (it.liveLevel > 0) {
                        visibility = View.VISIBLE
                        setImageResource(ImageLoader.getResId("login_ic_type3_v${it.liveLevel}", R.drawable::class.java))
                    }
                }

                //vip 等级的徽章
                getContentView().iv_vip_level_badge.apply {
                    if (it.getMyVipLevel() > 0) {
                        visibility = View.VISIBLE
                        setImageResource(ImageLoader.getVipLevel(it.getMyVipLevel()))
                    }
                }


            }

            mCompositeDisposable?.add(RetrofitUtils().createApi(LiveApiService::class.java)
                    .getGuardList("1,50", from)
                    .compose(RxUtils.applySchedulers())
                    .subscribeWith(object : LoadingObserver<HttpResult<GuardListDto>>() {
                        override fun _onError(msg: String?) {}

                        override fun _onNext(data: HttpResult<GuardListDto>) {
                            setData(data.data.total, data.data.records)
                        }
                    }))


        }
    }


    fun setData(total: Int, datas: List<GuardListDto.Guard?>) {
        if (CommentUtils.isListEmpty(datas)) {
            getContentView().rv.visibility = View.GONE
            getContentView().ll_view.visibility = View.VISIBLE
            getContentView().tvEmpty.visibility = View.VISIBLE
        } else {
            getContentView().rv.visibility = View.VISIBLE
            getContentView().tvEmpty.visibility = View.GONE
            getContentView().ll_view.visibility = View.VISIBLE
            getContentView().ll_shjy.visibility = View.GONE
        }
        if (null == adapter) {
            adapter = ShouhuListAdapter(datas)
            getContentView().rv.adapter = adapter
        } else {
            adapter?.setNewData(datas)
        }
        getContentView().tv_num.text = total.toString() + ""
    }

    fun hasGurad() {
        if (getContentView().btn_submit != null) getContentView().btn_submit.text = "续费"
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.live_pop_list_shouhu)
    }

    override fun onCreateShowAnimation(): Animation {
        return getTranslateVerticalAnimation(1f, 0f, 300)
    }

    override fun onCreateDismissAnimation(): Animation {
        return getTranslateVerticalAnimation(0f, 1f, 300)
    }


}