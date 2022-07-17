package com.fengwo.module_live_vedio.mvp.ui.dialog.game

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.fengwo.module_comment.base.BaseMvpActivity
import com.fengwo.module_comment.base.HttpResult
import com.fengwo.module_comment.base.LoadingObserver
import com.fengwo.module_comment.ext.gone
import com.fengwo.module_comment.ext.onAllEvent
import com.fengwo.module_comment.ext.show
import com.fengwo.module_comment.iservice.UserProviderService
import com.fengwo.module_comment.utils.*
import com.fengwo.module_live_vedio.BuildConfig
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.api.LiveApiService
import com.fengwo.module_live_vedio.mvp.dto.GameBean
import com.fengwo.module_live_vedio.mvp.presenter.LivingRoomPresenter
import com.fengwo.module_live_vedio.mvp.ui.dialog.RedPackageDialog
import com.fengwo.module_live_vedio.mvp.ui.dialog.h5game.H5GameDialog
import com.fengwo.module_live_vedio.mvp.ui.iview.ILivingRoom
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_game.view.*
import razerdp.basepopup.BasePopupWindow

/**
 * File GameDialog.kt
 * Date 12/29/20
 * Author lucas
 * Introduction 游戏列表
 */
class GameDialog(val mvpActivity: BaseMvpActivity<ILivingRoom, LivingRoomPresenter>) : BasePopupWindow(mvpActivity) {
    lateinit var compositeDisposable: CompositeDisposable
    lateinit var gameAdapter: GameListAdapter

    @Autowired
    lateinit var userProviderService: UserProviderService
    private var channelId: Int = 0//主播ID
    private val env = listOf("prod", "dev", "pre", "sit01", "sit02")
    var h5GameDialog: H5GameDialog? = null

    init {
        ArouteUtils.inject(this)
    }

    init {
        mvpActivity.lifecycle.onAllEvent {
            when (it) {
                Lifecycle.Event.ON_CREATE -> {
                    initData()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    compositeDisposable.dispose()
                    return@onAllEvent true
                }
            }
            return@onAllEvent false
        }
    }

    override fun onCreateContentView(): View = createPopupById(R.layout.dialog_game)

    private fun initData() {
        val disposable = RetrofitUtils().createApi(LiveApiService::class.java).gameList
                .compose(RxUtils.applySchedulers())
                .subscribe(object : LoadingObserver<HttpResult<List<GameBean>>>() {
                    override fun _onNext(data: HttpResult<List<GameBean>>) {
                        if (data.isSuccess) {
                            if (data.data.isNullOrEmpty()) {
                                gameAdapter.data.clear()
                                gameAdapter.notifyDataSetChanged()
                                contentView.v_hint_text.text = "小游戏暂未开通，敬请期待…"
                                contentView.v_empty_view.show()
                            } else {
                                contentView.v_empty_view.gone()
                                gameAdapter.setNewData(data.data)
                            }
                        } else {
                            gameAdapter.data.clear()
                            gameAdapter.notifyDataSetChanged()
                            contentView.v_hint_text.text = "小游戏暂未开通，敬请期待…"
                            contentView.v_empty_view.show()
                        }
                    }

                    override fun realError() {
                        gameAdapter.data.clear()
                        gameAdapter.notifyDataSetChanged()
                        contentView.v_empty_view.show()
                        contentView.v_hint_text.text = "网络环境差，请稍后重启进入小游戏~"
                    }

                    override fun _onError(msg: String?) {
                    }
                })
//        compositeDisposable.add(disposable)
    }

    override fun onViewCreated(contentView: View) {
        showAnimation = AnimationUtils.loadAnimation(context,R.anim.anim_in)
        dismissAnimation = AnimationUtils.loadAnimation(context,R.anim.anim_pop_out)
        gameAdapter = GameListAdapter()
        compositeDisposable = CompositeDisposable()
//        setPopupAnimationStyle(R.style.livePopWindowStyle)
        popupGravity = Gravity.BOTTOM
        setBackgroundColor(context.getColor(R.color.alpha_50_black))
        contentView.v_list.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        contentView.v_list.adapter = gameAdapter
        gameAdapter.setOnItemClickListener { adapter, view, position ->
            gameAdapter.data[position].run {
                when (gameCode) {
                    "DIG_TREASURE",
                    "PRIZE_WHEEL" -> {//全民挖宝,幸运大转盘
                        if (h5GameDialog == null)
                            h5GameDialog = H5GameDialog(mvpActivity)
                        h5GameDialog?.showByUrl(getH5Url())
                    }
                    "RED_PACKET" -> {//红包
                        if (context is AppCompatActivity) {
                            RedPackageDialog().show(mvpActivity.supportFragmentManager, "redPacket")
                        }
                    }
                }
                dismiss()
            }
        }
    }

    private fun GameBean.getH5Url(): String {
        var param = "?token=${userProviderService.token}&gameCode=${gameCode}&anchorId=${channelId}"
        if (BuildConfig.DEBUG) {
            val index = SPUtils1.get(context, "KEY_URL", 0) as Int
            param = param.plus("&env=${env[index]}")
        } else {
            param = param.plus("&env=prod")
        }
        return gameUrl.plus(param)
    }

    fun showDialog(channelId: Int) {
        this.channelId = channelId
        initData()
        showPopupWindow()
    }
}