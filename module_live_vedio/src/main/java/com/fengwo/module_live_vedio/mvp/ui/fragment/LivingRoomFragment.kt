package com.fengwo.module_live_vedio.mvp.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fengwo.module_comment.bean.ZhuboDto
import com.fengwo.module_comment.ext.customClick
import com.fengwo.module_comment.utils.*
import com.fengwo.module_comment.widget.LivingRoomFrameLayout
import com.fengwo.module_live_vedio.R
import com.fengwo.module_live_vedio.mvp.dto.PkRankDto
import com.fengwo.module_live_vedio.mvp.dto.PkRankMember
import com.fengwo.module_live_vedio.mvp.dto.PkResultDto
import com.fengwo.module_live_vedio.mvp.ui.adapter.PkSingleAdapter
import com.fengwo.module_live_vedio.mvp.ui.adapter.PkSingleAdapter.ILocationistener
import com.fengwo.module_live_vedio.mvp.ui.adapter.PkTeamAdapter
import com.fengwo.module_live_vedio.utils.MatchPkResultUtil
import com.fengwo.module_live_vedio.utils.PkAnimaUtil
import com.fengwo.module_live_vedio.utils.PkAnimaUtil.MAnimatorListener
import com.tencent.rtmp.ui.TXCloudVideoView
import kotlinx.android.synthetic.main.live_item_livingroom.*
import java.util.*

/**
 * @anchor Administrator
 * @date 2020/12/24
 */
class LivingRoomFragment : Fragment() {
    var pkResultAnima: PkAnimaUtil? = null
    var mData: ZhuboDto? = null
    var mView: View? = null
    var isJoinRoom = false//是否加入直播间

    companion object {
        fun newInstance(data: ZhuboDto): LivingRoomFragment {
            val args = Bundle()
            args.putSerializable("data", data);
            val fragment = LivingRoomFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var liveRoomFrameLayout: LivingRoomFrameLayout? = null
    fun setLiveRoomFrameLayout(liveRoomFrameLayout: LivingRoomFrameLayout?) {
        this.liveRoomFrameLayout = liveRoomFrameLayout
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.live_item_livingroom, container, false)
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//
//        if (container != null&&null!=container.parent) {
//            ( container.parent as ViewGroup)?.removeView(container)
//            return container
//        }
//
//        if(null==mView){
//            mView = inflater.inflate(R.layout.live_item_livingroom, container, false);
//        }
//
//        return mView
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mData = arguments?.getSerializable("data") as ZhuboDto?
        pkResultAnima = PkAnimaUtil()

        fl_loading.visibility = View.VISIBLE
        ImageLoader.loadImgBlur(iv_cover, mData?.thumb, R.drawable.live_bg_livingroom)
        ImageLoader.loadImg(iv_header_loading, mData?.headImg, R.drawable.default_head)
        pk_fl.visibility = INVISIBLE
        radar_view.startRippleAnimation()

    }

    /**
     * 设置loading页头像
     *
     * @param headImage
     */
    fun setLoadingHead(headImage: String?) {
        iv_header_loading?.run {
            if (headImage.isNullOrEmpty()) {
                iv_header_loading.setImageResource(R.drawable.default_head)
            } else {
                ImageLoader.loadImg(iv_header_loading, headImage, R.drawable.default_head)
            }
        }
    }

    fun setBottomHeight(bottomHeight: Int) {

        ll_pk_video_view?.post {
            ll_pk_video_view.run {
                    ll_pk_video_view.layoutParams.height = bottomHeight
            }

        }
    }

    fun getMineVedioView(): TXCloudVideoView? {
        return pk_main_view
    }

    fun getOtherVedioView(): TXCloudVideoView? {
        return pk_other_view
    }

    /**
     * 显示loading
     *
     * @param isUpdate 是否更新UI
     */
    fun startLoading(isUpdate: Boolean) {
        fl_loading?.run {
            if (isUpdate) {
                ImageLoader.loadImg(iv_header_loading, mData?.headImg, R.drawable.default_head)
                ImageLoader.loadImgBlur(iv_cover, mData?.thumb, R.drawable.live_bg_livingroom)
            } else {
                iv_cover.setImageResource(R.drawable.live_bg_livingroom)
            }
            fl_loading.visibility = View.VISIBLE
            tv_no_online.visibility = View.GONE
            radar_view.visibility = View.VISIBLE
            radar_view.startRippleAnimation()
        }

    }

    fun showPlayend() {
        fl_loading.visibility = View.VISIBLE
        tv_no_online.visibility = View.VISIBLE
        radar_view.stopRippleAnimation()
        radar_view.visibility = View.GONE
    }

    fun hideLoading() {
        pk_fl?.run {
            pk_fl.visibility = View.GONE
            fl_loading.visibility = View.GONE
            radar_view.stopRippleAnimation()
        }
    }

    fun hidePkLoading() {
        pk_fl?.run {
            pk_fl.visibility = View.VISIBLE
            fl_loading.visibility = View.GONE
            radar_view.stopRippleAnimation()
        }
    }

    fun startPkMineLoading(iview_score_leftShow: Boolean) {
        pb_mine.visibility = if (iview_score_leftShow) View.VISIBLE else View.GONE
    }

    fun startPkOtherLoading(iview_score_rightShow: Boolean) {
        pb_other.visibility = if (iview_score_rightShow) View.VISIBLE else View.GONE
    }

    fun getTextureView(): TXCloudVideoView? {
        //  TXCloudVideoView videoView = textureViewSparseArray.get(position, null);
        return video_view
    }


    fun showPkResult(pkResultDto: PkResultDto, isSingle: Boolean, punishTime: Int, listener: MAnimatorListener?) {
        KLog.e("pk", "pk结束 动画调用")
        pkResultAnima?.cancle()

        if (tv_time.text.toString().contains("PK")) { //后台推送结果，如果还没到惩罚时间，pk倒计时清零，进入惩罚时间
            uploadTime("惩罚中  ", punishTime / 1000)
        }
        if (TextUtils.isEmpty(pkResultDto.result.toString())) pkResultDto.result = 0 //如果为null 默认设为0

        if (null == fl_pk_left || null == fl_pk_right) return
        val leftStart = fl_pk_left.findViewById<ImageView>(R.id.iv_pk_result_left)
        val leftEnd = fl_pk_left.findViewById<ImageView>(R.id.iv_pk_result_left_end)
        val rightStart = fl_pk_right.findViewById<ImageView>(R.id.iv_pk_result_right)
        val rightEnd = fl_pk_right.findViewById<ImageView>(R.id.iv_pk_result_right_end)
        //销毁未完成动画
        leftStart.clearAnimation()
        leftEnd.clearAnimation()
        rightStart.clearAnimation()
        rightEnd.clearAnimation()
        leftStart.visibility = View.VISIBLE
        rightStart.visibility = View.VISIBLE
        if (pkResultDto.result == 0) { //平
            leftStart.setImageResource(R.drawable.ic_pk_draw)
            rightStart.setImageResource(R.drawable.ic_pk_draw)
        } else if (pkResultDto.result > 0) { //胜
            leftStart.setImageResource(R.drawable.ic_pk_success)
            rightStart.setImageResource(R.drawable.ic_pk_fail)
        } else {
            leftStart.setImageResource(R.drawable.ic_pk_fail)
            rightStart.setImageResource(R.drawable.ic_pk_success)
        }
        leftStart.translationX = 0f
        leftStart.translationY = 0f
        rightStart.translationX = 0f
        rightStart.translationY = 0f
        pkResultAnima?.start(leftStart, leftEnd, rightStart, rightEnd, listener)
        if (!isSingle && pkBtnClickListener != null && pkResultDto.teamPkResultDto.normal != 0) {
            pkBtnClickListener?.showTeamPkResult(pkResultDto)
        }
        if (isSingle) {
            showSingleProgressView(pkResultDto.singlePkResultDto, pkResultDto.result)
        }
    }

    fun isHidePkAttention(b: Boolean) {
        if (b) {
            tv_pk_attention.visibility = View.GONE
        } else {
            tv_pk_attention.visibility = View.VISIBLE
        }
    }

    fun startPk(isSingle: Boolean, matchPkResultUtil: MatchPkResultUtil) {

        pk_fl.visibility = View.VISIBLE

        tv_pk_nickname.text = matchPkResultUtil.otherInfo.nickname

        //    tv_pk_attention.parent.requestDisallowInterceptTouchEvent(true)
        tv_pk_attention.setOnClickListener({
            pkBtnClickListener?.attentionClick(matchPkResultUtil.getOtherInfo().getUserId().toString() + "")

        })
        tv_pk_nickname.setOnClickListener({
            if (pkBtnClickListener != null) {
                pkBtnClickListener?.nickNameClick(matchPkResultUtil.otherInfo.userId.toString() + "")
            }
        })
        val onTouchListener = customClick(tv_pk_attention) { view: View? ->
            if (pkBtnClickListener != null) {
                pkBtnClickListener!!.attentionClick(matchPkResultUtil.otherInfo.userId.toString() + "")
            }
            null
        }
        liveRoomFrameLayout?.addOnCustomTouchListener(tv_pk_attention, onTouchListener)
        val onTouchListener2 = customClick(tv_pk_nickname) { view: View? ->
            if (pkBtnClickListener != null) {
                pkBtnClickListener!!.nickNameClick(matchPkResultUtil.otherInfo.userId.toString() + "")
            }
            null
        }
        liveRoomFrameLayout?.addOnCustomTouchListener(tv_pk_nickname, onTouchListener2)
        if (!isSingle) {
            showTeamProgressView(matchPkResultUtil)
        } else {
            showSingleProgressView(matchPkResultUtil.matchTeamResult, matchPkResultUtil.matchTeamResult.userResult)
        }
    }

    fun showSingleProgressView(pkRankDto: PkRankDto?, result: Int) {
        if (pkRankDto == null || pkRankDto.contributionRank == null && pkRankDto.objectContributionRank == null) return
        val weSize = pkRankDto.contributionRank.size
        val otherSize = pkRankDto.objectContributionRank.size
        if (weSize < 3) {
            for (i in 0 until 3 - weSize) {
                pkRankDto.contributionRank.add(PkRankMember())
            }
        }
        Collections.reverse(pkRankDto.contributionRank) //倒序
        if (otherSize < 3) {
            for (i in 0 until 3 - otherSize) {
                pkRankDto.objectContributionRank.add(PkRankMember())
            }
        }
        tv_hot_we.text = pkRankDto.memberPopularity.toString() + ""
        tv_hot_other.text = pkRankDto.objectMemberPopularity.toString() + ""
        rv_we_single.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rv_other_single.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rv_we_single.adapter = PkSingleAdapter(pkRankDto.contributionRank, true, result, ILocationistener { position, x, view ->
            //                myFirstOne[0] = x;
//                myFirstOne[1] = y;
//                FirstView = view;
        })

        ll_hot_we.setOnClickListener({

            pkBtnClickListener?.pkContributeClick(pkRankDto.getUserInfo().getUserId(), true);

        })
        ll_hot_other.setOnClickListener({

            pkBtnClickListener?.pkContributeClick(pkRankDto.getObjectInfo().getUserId(), false);

        })
        rv_other_single.adapter = PkSingleAdapter(pkRankDto.objectContributionRank, false, result, ILocationistener { position, x, view ->
            //                myFirstOne[0] = x;
//                myFirstOne[1] = y;
//                FirstView = view;
        })
        if (liveRoomFrameLayout != null) {
            ll_hot_we.setOnClickListener(View.OnClickListener { })
            liveRoomFrameLayout?.addOnCustomTouchListener(ll_hot_we, customClick(ll_hot_we) { view: View? ->
                if (pkBtnClickListener != null && rl_pk_single_bottom != null && rl_pk_single_bottom.getVisibility() == View.VISIBLE) {
                    pkBtnClickListener!!.pkContributeClick(pkRankDto.getUserInfo().userId, true)
                }
                null
            })
            ll_hot_other.setOnClickListener(View.OnClickListener { })
            liveRoomFrameLayout?.addOnCustomTouchListener(ll_hot_other, customClick(ll_hot_other) { view: View? ->
                if (pkBtnClickListener != null && rl_pk_single_bottom != null && rl_pk_single_bottom.getVisibility() == View.VISIBLE) {
                    pkBtnClickListener!!.pkContributeClick(pkRankDto.getObjectInfo().userId, false)
                }
                null
            })
        }
        rv_we_single.isLayoutFrozen = true
        rv_other_single.isLayoutFrozen = true
        if (result == 1) {
            iv_wipe_we.setVisibility(View.GONE)
            iv_wipe_other.setVisibility(View.VISIBLE)
        } else if (result == -1) {
            iv_wipe_we.setVisibility(View.VISIBLE)
            iv_wipe_other.setVisibility(View.GONE)
        } else {
            iv_wipe_we.setVisibility(View.GONE)
            iv_wipe_other.setVisibility(View.GONE)
        }
        rl_pk_single_bottom.setVisibility(View.VISIBLE)
    }


    private fun hideSingleProgressView() {
        rl_pk_single_bottom.visibility = View.GONE
    }

    private fun showTeamProgressView(matchPkResultUtil: MatchPkResultUtil) {

        if (tv_we_team_score == null || tv_other_team_score == null) return
        tv_we_team_score.text = "团队：0"
        tv_other_team_score.text = "0：团队"


        rv_we_team.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rv_other_team.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rv_we_team.adapter = PkTeamAdapter(matchPkResultUtil.userTeamInfo, true)
        rv_other_team.adapter = PkTeamAdapter(matchPkResultUtil.otherTeamInfo, false)
        rl_pk_team_bottom.visibility = View.VISIBLE
        tv_we_team_score.visibility = View.VISIBLE
        tv_other_team_score.visibility = View.VISIBLE
    }

    private fun hideTeamProgressView() {

        if (tv_we_team_score != null) {
            tv_we_team_score.text = "团队：0"
            tv_we_team_score.visibility = View.GONE
        }
        if (tv_other_team_score != null) {
            tv_other_team_score.text = "0：团队"
            tv_other_team_score.visibility = View.GONE
        }
        if (rl_pk_team_bottom != null) rl_pk_team_bottom.visibility = View.GONE
    }

    fun stopPk() {

        pk_fl.visibility = View.GONE

        hidePkResult()
        hideTeamProgressView()
        hideSingleProgressView()
        uploadScore(0, 0, 0, 0, false) //pk结束 重置值

    }

    fun hidePkResult() {
        iv_pk_result_left?.run {
            iv_pk_result_left.clearAnimation()
            iv_pk_result_right.clearAnimation()
            iv_pk_result_left.visibility = View.GONE
            iv_pk_result_right.visibility = View.GONE
        }

        //      pkResultShowing = false
    }

    private val normalParams: FrameLayout.LayoutParams? = null

    fun setVedioType(type: Int) {

        val layoutParams = video_view.layoutParams as FrameLayout.LayoutParams
        if (type == 2) {
            layoutParams.height = (ScreenUtils.getStatusHeight(context) + ScreenUtils.getScreenHeight(context)) * 1 / 3
            context?.let {
                layoutParams.topMargin = it.getResources().getDimension(R.dimen.dp_80).toInt()
            }

        } else {
//            layoutParams.height = ScreenUtils.getStatusHeight(mContext) + ScreenUtils.getScreenHeight(mContext);
            layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
            layoutParams.topMargin = 0
        }
        video_view.layoutParams = layoutParams
    }

    /**
     * 更新pk礼物值
     *
     * @param position
     * @param left
     * @param right    当前按照 礼物最少值 20计算 ， 当一方没收到礼物一方收到任何礼物的时候，没收到礼物的一方保留1.5权值 另一方20权值
     */
    fun uploadScore(left: Int, right: Int, groupPoint: Int, otherGroupPoint: Int, isPk: Boolean) {


        if (view_score_left == null || view_score_right == null || tv_we_team_score == null || tv_other_team_score == null) return  //控件未初始化则返回
        if (tv_we_team_score != null) {
            tv_we_team_score.text = "团队：" + DataFormatUtils.formatNumbers(groupPoint)
        }
        if (tv_other_team_score != null) {
            tv_other_team_score.text = DataFormatUtils.formatNumbers(otherGroupPoint) + "：团队"
        }
        val leftParams = view_score_left.layoutParams as LinearLayout.LayoutParams
        val rightParams = view_score_right.layoutParams as LinearLayout.LayoutParams
        var weightLeft = 1f
        var weightRight = 1f
        if (left == 0 && right == 0) {
        } else if (1f * left / right < 0.2 || 1f * right / left < 0.2) {
            if (left > right) {
                weightLeft = 8f
                weightRight = 2f
            } else {
                weightLeft = 2f
                weightRight = 8f
            }
        } else {
            weightLeft = left.toFloat()
            weightRight = right.toFloat()
        }
        leftParams.weight = weightLeft
        rightParams.weight = weightRight
        view_score_left.layoutParams = leftParams
        view_score_right.layoutParams = rightParams
        tv_score_mine.text = "我方 $left"
        tv_score_other.text = "$right 对方"

    }

    fun uploadTime(text: String, time: Int) {

        if (time > 0) {
            tv_time.text = text + TimeUtils.fromSecond(time)
        } else {
            tv_time.text = text
        }

    }

    fun setAttention() {
        tv_pk_attention.visibility = View.GONE
    }


    private var pkBtnClickListener: OnPkBtnClickListener? = null

    interface OnPkBtnClickListener {
        fun nickNameClick(s: String?)
        fun attentionClick(s: String?)
        fun showTeamPkResult(pkResultDto: PkResultDto?)
        fun pkContributeClick(id: Int, isWe: Boolean)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            KLog.e("tag", "" + hidden)
        } else {
            KLog.e("tag", "" + hidden)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //     mView.getParent().remove(view);
        if (null != mView?.getParent()) {
            (mView?.getParent() as ViewGroup).removeView(mView)
            KLog.e("tag", "removeView" + this)
        }

        // mView.parent.clearChildFocus(view)
        KLog.e("tag", "onDestroyView" + this)
        ImageLoader.clearGlide(context);

        video_view?.removeVideoView()
        pk_main_view?.removeVideoView()
        pk_other_view?.removeVideoView()
    }

    override fun onResume() {
        super.onResume()
        KLog.e("tag", "onResume" + this)
        fl_loading?.visibility = View.VISIBLE
        isJoinRoom = true
    }


    override fun onPause() {
        fl_loading?.visibility = View.VISIBLE

        super.onPause()
        KLog.e("tag", "onPause" + this)
    }

    override fun onStop() {
        super.onStop()
        pkResultAnima?.cancle()
    }

    override fun onDestroy() {
        super.onDestroy()
        pkBtnClickListener = null

    }

    fun setPkBtnClickListener(l: OnPkBtnClickListener?) {
        pkBtnClickListener = l
    }

}