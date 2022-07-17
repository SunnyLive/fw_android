package com.fengwo.module_live_vedio.mvp.ui.fragment.redpackage

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fengwo.module_live_vedio.R
import kotlinx.android.synthetic.main.fragment_red_package_rule.*


/**
 * 红包规则
 *
 * @Author gukaihong
 * @Time 2020/12/29
 */
class RedPackageRuleFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_red_package_rule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val txt = StringBuilder()
        txt.append("<h5>【玩法说明】</h5>")
        txt.append("<br>")
        txt.append("<p><b>1、在直播间发红包有什么好处？</b></p>")
        txt.append("<p>有红包的直播间会展示在首页，吸引更多用户观看直播。</p>")
        txt.append("<br>")
        txt.append("<p><b>2、红包发给谁？</b></p>")
        txt.append("<p>红包发出后，直播间所有用户可抢，包括主播和自己。</p>")
        txt.append("<br>")
        txt.append("<p><b>3、未被领取的红包如何处理？</b></p>")
        txt.append("<p>红包打开后未被领取的花钻，以及主播下播后未打开的红包花钻，会退回到您的账户。</p>")
        txt.append("<br>")
        txt.append("<p><b>4、还有什么好玩的？</b></p>")
        txt.append("<p>单次红包达到一定花钻数量，可触发全服红包，获得更多曝光。</p>")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_content.text = Html.fromHtml(txt.toString(),Html.FROM_HTML_MODE_COMPACT)
        }else{
            tv_content.text = Html.fromHtml(txt.toString())
        }
    }
}