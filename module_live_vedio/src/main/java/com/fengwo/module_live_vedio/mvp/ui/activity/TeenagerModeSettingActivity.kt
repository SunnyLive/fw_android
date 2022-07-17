package com.fengwo.module_live_vedio.mvp.ui.activity

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.fengwo.module_comment.Constants
import com.fengwo.module_comment.base.BaseActivity
import com.fengwo.module_comment.base.BaseMvpActivity
import com.fengwo.module_comment.iservice.UserInfo
import com.fengwo.module_comment.iservice.UserProviderService
import com.fengwo.module_comment.utils.ActivitysManager
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.presenter.TeenagerModeSettingPresenter
import com.fengwo.module_live_vedio.mvp.ui.iview.ITeenagerModeSettingView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_teenager_mode_setting.*

/**
 * 青少年模式设置
 *
 * @Author gukaihong
 * @Time 2020/12/8
 */
class TeenagerModeSettingActivity : BaseMvpActivity<ITeenagerModeSettingView, TeenagerModeSettingPresenter>(), ITeenagerModeSettingView {

    @Autowired
    @JvmField
    var userProviderService: UserProviderService? = null

    private var tokenIInvalidValue = false//token是否过期，因为启动关系，需要记录状态，提示重新登录

    override fun initPresenter() = TeenagerModeSettingPresenter()

    override fun getContentView() = R.layout.activity_teenager_mode_setting

    var rxBus: Disposable? = null

    var userInfo: UserInfo? = null;

    override fun initView() {

        userInfo = userProviderService?.userInfo

        setWhiteTitle("青少年模式")

        tv_setting.setOnClickListener {
            if (BaseActivity.isFastClick()) {
                return@setOnClickListener
            }
            if (enableTeenager(userInfo)) {
                //去关闭
                p.exitTeenagerMode(userInfo)
            } else {
                //去打开
                startActivity(TeenagerModePasswordActivity::class.java)
            }

        }
        //获取数据
        p.updateView(userInfo)
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


    /**是否开启青少年模式*/
    private fun enableTeenager(userInfo: UserInfo?): Boolean {
        return if (userInfo == null) {
            false
        } else {
            TextUtils.equals(userInfo.teenagerMode, Constants.TEENAGER_MODE_ENABLE)
        }

    }


    override fun updateUI(enable: Boolean) {
        if (enable) {
            //已开启
            img_tips.setImageResource(R.drawable.icon_teenager_tips)
            tv_tips.text = "青少年模式已开启"
            tv_setting.text = "关闭青少年模式"
        } else {
            //已关闭
            img_tips.setImageResource(R.drawable.icon_teenager_close)
            tv_tips.text = "青少年模式未开启"
            tv_setting.text = "开启青少年模式"
        }
    }

    /**退出青少年模式*/
    override fun exitTeenagerMode() {
        //更新用户信息
        userInfo?.let {
            it.teenagerMode = Constants.TEENAGER_MODE_UNENABLE
            userProviderService?.setUsetInfo(it)
        }

        //退出青少年模式-除了主页
        ActivitysManager.getInstance().popAll(Constants.MAIN_CLASS_NAME)
    }

    override fun tokenIInvalid() {
        tokenIInvalidValue = true
        runOnUiThread {
            if (!this.hasWindowFocus()) {
                //获取window焦点，否则无法弹出重新登录框
                val view = this.window.decorView
                view.isFocusable = true
                view.isFocusableInTouchMode = true
                view.requestFocus()
            }
            super.tokenIInvalid()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        rxBus?.dispose()
    }
}