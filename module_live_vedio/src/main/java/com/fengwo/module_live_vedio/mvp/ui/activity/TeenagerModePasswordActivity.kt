package com.fengwo.module_live_vedio.mvp.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.fengwo.module_comment.Constants
import com.fengwo.module_comment.base.BaseMvpActivity
import com.fengwo.module_comment.iservice.UserInfo
import com.fengwo.module_comment.iservice.UserProviderService
import com.fengwo.module_comment.utils.ActivitysManager
import com.fengwo.module_comment.utils.ToastUtils
import com.fengwo.module_comment.widget.VerificationCodeView
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.presenter.TeenagerModePasswordPresenter
import com.fengwo.module_live_vedio.mvp.ui.iview.ITeenagerModePasswordView
import kotlinx.android.synthetic.main.activity_teenager_mode_password.*


/**
 * 青少年模式-密码校验activity
 *
 * @Author gukaihong
 * @Time 2020/12/8
 */
class TeenagerModePasswordActivity : BaseMvpActivity<ITeenagerModePasswordView, TeenagerModePasswordPresenter>(), ITeenagerModePasswordView {

    @Autowired
    @JvmField
    var userProviderService: UserProviderService? = null

    private var tokenIInvalidValue = false//token是否过期，因为启动关系，需要记录状态，提示重新登录

    var userInfo: UserInfo? = null

    override fun initPresenter() = TeenagerModePasswordPresenter()

    override fun getContentView() = R.layout.activity_teenager_mode_password

    override fun initView() {

        setWhiteTitle("青少年模式")
        userInfo = userProviderService?.userInfo
        //更新UI
        p.updateUI(userInfo)

        tv_forget_password_tips.text = Html.fromHtml(resources.getString(R.string.teenager_password_tips))

        tv_code.onCodeFinishListener = object : VerificationCodeView.OnCodeFinishListener {
            override fun onComplete(view: View?, content: String?) {
                //输入完成
                userProviderService?.let {
                    p.checkPassword(it.userInfo, content ?: "")
                }
            }

            override fun onTextChange(view: View?, content: String?) {
                //输入内容
            }

        }

        tv_forget_password_tips.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            val data = Uri.parse("tel:4000051118")
            intent.data = data
            startActivity(intent)
        }

        userInfo?.let {
            val fw = "蜂窝号：${it.fwId}"
            tv_server_tips.text = fw
        }

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


    /**退出青少年模式*/
    override fun exitTeenagerMode() {
        //跳转设置页面确认退出
        startActivity(TeenagerModeSettingActivity::class.java)
        finish()
    }


    /**进入青少年模式*/
    override fun joinTeenagerMode(password: String) {
        ActivitysManager.getInstance().popAll(Constants.MAIN_CLASS_NAME)
        updateUserInfo(Constants.TEENAGER_MODE_ENABLE, password)
        // 进入青少年模式
        startActivity(TeenagerModeMainActivity::class.java)


    }

    private fun updateUserInfo(mode: String, password: String? = null) {
        userInfo?.let {
            it.teenagerMode = mode
            if (!password.isNullOrEmpty() && it.teenagerPassword.isNullOrEmpty()) {
                //如果初始密码为空，设置密码。更新本地数据
                it.teenagerPassword = password
            }
            userProviderService?.setUsetInfo(it)
        }
    }


    /**错误提示*/
    override fun errorTips(msg: String) {
        ToastUtils.showShort(applicationContext, msg, Gravity.CENTER)
        if (TextUtils.equals("密码错误，请重新输入", msg)) {
            tv_code.setEmpty()
        }
    }

    /**更新UI*/
    override fun updateUI(title: String, showTips: Boolean) {
        tv_type_tips.text = title
        val visibility = if (showTips) {
            View.VISIBLE
        } else {
            View.GONE
        }
        tv_forget_password_tips.visibility = visibility
        tv_server_tips.visibility = visibility
    }

    /**重新输入*/
    override fun reTypePassword() {
        tv_code.setEmpty()
    }

    /**
     * 隐藏键盘
     */
    override fun hideInput() {
        // KeyBoardUtils.closeKeybord(tv_code.getChildAt(tv_code.childCount - 1) as EditText?, this)
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v = window.peekDecorView()
        if (null != v) {
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
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

}