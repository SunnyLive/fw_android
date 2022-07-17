package com.fengwo.module_live_vedio.mvp.ui.activity

import android.content.Intent
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.fengwo.module_comment.ArouterApi
import com.fengwo.module_comment.Constants
import com.fengwo.module_comment.Interfaces.ICardType
import com.fengwo.module_comment.base.BaseActivity
import com.fengwo.module_comment.base.BaseMvpActivity
import com.fengwo.module_comment.bean.VensionDto
import com.fengwo.module_comment.dialog.UpdateAppDialog
import com.fengwo.module_comment.iservice.UserInfo
import com.fengwo.module_comment.iservice.UserProviderService
import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.TeenagerVideoDto
import com.fengwo.module_live_vedio.mvp.presenter.TeenagerModeMainPresenter
import com.fengwo.module_live_vedio.mvp.ui.adapter.TeenagerVideoListAdapter
import com.fengwo.module_live_vedio.mvp.ui.iview.ITeenagerModeMainView
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_teenager_mode_main.*


/**
 * 青少年模式主页
 *
 * @Author gukaihong
 * @Time 2020/12/9
 */
class TeenagerModeMainActivity : BaseMvpActivity<ITeenagerModeMainView, TeenagerModeMainPresenter>(), ITeenagerModeMainView {

    @Autowired
    @JvmField
    var userProviderService: UserProviderService? = null

    private val dataAdapter: TeenagerVideoListAdapter by lazy { TeenagerVideoListAdapter() }

    override fun initPresenter() = TeenagerModeMainPresenter()

    override fun getContentView() = R.layout.activity_teenager_mode_main

    private val mUpdateAppDialog: UpdateAppDialog by lazy { UpdateAppDialog() }

    private var tokenIInvalidValue = false//token是否过期，因为启动关系，需要记录状态，提示重新登录

    override fun onResume() {
        super.onResume()
        p.updateUserInfo()
    }

    override fun initView() {
        statusBarView = status_bar_view
        ImmersionBar.setStatusBarView(this, statusBarView)
        SmartRefreshLayoutUtils.setWhiteBlackText(this, smart_refresh)
        smart_refresh.setEnableLoadMore(true)
        smart_refresh.setEnableRefresh(true)
        smart_refresh.setOnRefreshListener {
            p.getListData(true)
        }
        smart_refresh.setOnLoadMoreListener {
            p.getListData(false)
        }
        //刷新
        p.getListData(true, needLoading = true)

        rv_list.layoutManager = GridLayoutManager(this, 2)
        dataAdapter.bindToRecyclerView(rv_list)
//        dataAdapter.setOnItemChildClickListener { adapter, view, position ->
//            if (BaseActivity.isFastClick()) {
//                return@setOnItemChildClickListener
//            }
//            val bannerList = ArrayList<ICardType>()
//            val item = adapter.getItem(position) as TeenagerVideoDto.Records
//            bannerList.add(item)
//            //跳转视频播放页面
//            ARouter.getInstance().build(ArouterApi.FLIRT_DETAIL_CARD_ACTION)
//                    .withOptionsCompat(ActivityOptionsCompat.makeScaleUpAnimation(view,view.width/2,view.height/2,0,0))
//                    .withSerializable("bannerList", bannerList)
//                    .withInt("position", 0)
//                    .navigation()
//            p.addCount(item.id.toString())
//        }

        tv_exit.setOnClickListener {
            if (BaseActivity.isFastClick()) {
                return@setOnClickListener
            }
            //退出-校验密码
            startActivity(TeenagerModePasswordActivity::class.java)
        }

        //检测版本更新
        p.checkVersion()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            //获取window焦点后，检测是不是token过期
            if (tokenIInvalidValue) {
                tokenIInvalid()
            }
        }
    }

    private var mExitTime = 0L
    override fun onBackPressed() {
        //super.onBackPressed()
        //去掉返回事件
        if (System.currentTimeMillis() - mExitTime > 2000) {
            toastTip("再按一次就真的退出了哦~")
            mExitTime = System.currentTimeMillis()
        } else {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addCategory(Intent.CATEGORY_HOME)
            this.startActivity(intent)
        }
    }

    /**刷新数据*/
    override fun updateListData(data: List<TeenagerVideoDto.Records>?, isRefresh: Boolean) {


        if (isRefresh) {
            smart_refresh.finishRefresh()
        } else {
            if (data.isNullOrEmpty()) {
                smart_refresh.finishLoadMoreWithNoMoreData()
            } else {
                smart_refresh.finishLoadMore()
            }
        }
        data?.let {
            if (isRefresh) {
                dataAdapter.setNewData(it)
            } else {
                dataAdapter.addData(it)
            }
        }
        showEmptyView()

    }

    override fun updateAPPVersion(data: VensionDto?) {
        data?.let {
            mUpdateAppDialog.showDialogOnlyOne(this, data)
        }
    }

    override fun updateUserInfo(data: UserInfo) {
        userProviderService?.setUsetInfo(data)
    }

    /**网络异常*/
    override fun netError() {
        //super.netError()
        smart_refresh.finishRefresh()
        smart_refresh.finishLoadMore()
        showEmptyView()
    }

    private fun showEmptyView() {
        if (dataAdapter.data.isNullOrEmpty()) {
            empty_view.visibility = View.VISIBLE
        } else {
            empty_view.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE_UPDATE_APP_VERSION) {
            mUpdateAppDialog.reInstallApk(this)
        }
    }

    override fun tokenIInvalid() {
        tokenIInvalidValue = true
        super.tokenIInvalid()
    }
}