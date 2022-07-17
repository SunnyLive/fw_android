package com.fengwo.module_flirt.UI.activity


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.webkit.WebSettings
import com.fengwo.module_comment.base.BaseMvpActivity
import com.fengwo.module_comment.base.BasePresenter
import com.fengwo.module_comment.base.MvpView
import com.fengwo.module_comment.utils.DarkUtil
import com.fengwo.module_flirt.R
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.activity_html.*


/**
 * @anchor Administrator
 * @date 2020/12/4
 */
class HtmlActivity : BaseMvpActivity<MvpView, BasePresenter<MvpView>>() {

    companion object {
        fun forward(context: Context, msgHtml: String, title: String) {
            val intent = Intent(context, HtmlActivity::class.java)
            intent.putExtra("msgHtml", msgHtml)
            intent.putExtra("title", title)
            context.startActivity(intent)
        }
    }

    //val url ="<p>【直播】升级让你有<a href=\"http://www.baidu.com\"rel=\"noopener noreferrer\"target=\"_blank\">更好</a>的体验，<a href=\"http://期待\"rel=\"noopener noreferrer\"target=\"_blank\">期待</a>你在蜂窝互娱的生活越来越致润</p><img width=\"100%\"src=\"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/editorNews/1607653805000*editorNews6692343181.jpg\"><p>【直播】<a href=\"http://fengwohuyu.com\"rel=\"noopener noreferrer\"target=\"_blank\">升级让你有更好的体验，期待你在蜂窝互娱的生活越来越致润</a></p><video class=\"ql-video\"controls=\"controls\"type=\"video/mp4\"width=\"100%\"poster=\"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/editorNews/1607653843000*editorNews8028867572.mp4?x-oss-process=video/snapshot,t_1000,f_jpg\"src=\"https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/editorNews/1607653843000*editorNews8028867572.mp4\"preload=\"auto\"></video><p><a href=\"http://www.baidu.com\"rel=\"noopener noreferrer\"target=\"_blank\">绿色直播</a></p>"
    var html: String = ""
    override fun initView() {
        ToolBarBuilder().setTitle(intent.extras?.getString("title"))
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .showBack(true).build()

        html = intent.extras?.getString("msgHtml").toString()

        val webSetting = tv_html.getSettings()
        webSetting.javaScriptEnabled = true
        webSetting.savePassword = true
        webSetting.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webSetting.setAllowFileAccessFromFileURLs(false)
        webSetting.setAllowUniversalAccessFromFileURLs(false)
        tv_html.loadUrl("file:///android_asset/htmlStyle.html?type=${if (DarkUtil.isDarkTheme(this)) 1 else 0}")
        tv_html.webViewClient = object : WebViewClient() {
            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                tv_html.loadUrl("javascript:createTable('$html')")
            }
        }
        tv_html.webChromeClient = object : WebChromeClient() {
            override fun getDefaultVideoPoster(): Bitmap {
                return super.getDefaultVideoPoster()
            }
        }

    }


    override fun getContentView(): Int {
        return R.layout.activity_html
    }

    /**返回事件-返回上页还是退出页面*/
    override fun onBackPressed() {
        //super.onBackPressed()
        if (tv_html.canGoBack()) {
            tv_html.goBack()
        } else {
            finish()
        }
    }

    override fun initPresenter(): BasePresenter<MvpView> {
        return BasePresenter();
    }


}