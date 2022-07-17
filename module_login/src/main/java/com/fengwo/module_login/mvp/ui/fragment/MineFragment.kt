/*
 *
 * 如果你看到这里 那么恭喜你 这个页面的代码已经被我给重构了，他们不敢干的事情我敢干
 *
 * 祖传代码不敢删？开玩笑，佛说，我不如地狱谁入地狱
 *
 * 这个是我的个人中心的页面
 *
 * 主要展示用户的信息相关
 *
 * 祖传代码在old同名
 *
 * */
package com.fengwo.module_login.mvp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.location.AMapLocation
import com.faceunity.ui.dialog.BaseDialogFragment
import com.fengwo.module_comment.ArouterApi
import com.fengwo.module_comment.Interfaces.IRealNameInterceptService
import com.fengwo.module_comment.MapLocationUtil
import com.fengwo.module_comment.base.BaseActivity
import com.fengwo.module_comment.base.BaseListDto
import com.fengwo.module_comment.base.BaseMvpFragment
import com.fengwo.module_comment.bean.ZhuboDto
import com.fengwo.module_comment.dialog.ExitDialog
import com.fengwo.module_comment.ext.isShow
import com.fengwo.module_comment.iservice.UserInfo
import com.fengwo.module_comment.utils.*
import com.fengwo.module_comment.widget.floatingview.FloatingView
import com.fengwo.module_login.R
import com.fengwo.module_login.mvp.dto.*
import com.fengwo.module_login.mvp.presenter.MineIndexPresenter
import com.fengwo.module_login.mvp.ui.activity.*
import com.fengwo.module_login.mvp.ui.adapter.MineImageAdapter
import com.fengwo.module_login.mvp.ui.adapter.MineServerAdapter
import com.fengwo.module_login.mvp.ui.iview.IMineIndexView
import com.fengwo.module_login.mvp.ui.pop.QrCodePopwindow
import com.fengwo.module_login.utils.UserManager
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.layout_certification.*
import kotlinx.android.synthetic.main.layout_mine_list.*
import kotlinx.android.synthetic.main.layout_user_card.*
import kotlinx.android.synthetic.main.mine_index_fragment.*
import java.util.*
import kotlin.collections.ArrayList


@Route(path = ArouterApi.LOGIN_FRAGMENT_MINE)
class MineFragment : BaseMvpFragment<IMineIndexView, MineIndexPresenter>(), IMineIndexView, AppBarLayout.OnOffsetChangedListener {


    //private val mAnchorAdapter: MineImageAdapter by lazy { MineImageAdapter(context!!) }
    //private val mExpertAdapter: MineImageAdapter by lazy { MineImageAdapter(context!!) }
    private val mGuardAdapter:  MineImageAdapter by lazy { MineImageAdapter(context!!) }
    private val mCarAdapter:    MineImageAdapter by lazy { MineImageAdapter(context!!) }
    private val mGiftAdapter:   MineImageAdapter by lazy { MineImageAdapter(context!!) }


    @Autowired
    @JvmField
    var mRealNameService //获取实名认证服务
            : IRealNameInterceptService? = null

    override fun setContentView(): Int {
        return R.layout.mine_index_fragment
    }


    override fun initUI(savedInstanceState: Bundle?) {
        initEvent()
        initData()
    }


    override fun onResume() {
        super.onResume()
        userVisibleHint
    }

    override fun getUserVisibleHint(): Boolean {
        initData()
        return super.getUserVisibleHint()
    }

    override fun initView(v: View?) {
        super.initView(v)
        val params = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as AppBarLayout.Behavior?
        behavior?.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return false
            }
        })
        SmartRefreshLayoutUtils.setTransparentBgWithWhileText(activity, mine_refresh)
        appBarLayout.addOnOffsetChangedListener(this)
        collapsingToolbarLayout.title = "我的"

        mine_refresh.setOnRefreshListener { initData() }
        val screenWidth = ScreenUtils.getScreenWidth(context)
        val margin = resources.getDimension(R.dimen.dp_30)
        val lineWidth = resources.getDimension(R.dimen.dp_1)
        val childWidth = (screenWidth - margin - lineWidth) / 2
        cl_guard_info.layoutParams.width = childWidth.toInt()
        cl_union_info.layoutParams.width = childWidth.toInt()
        cl_car_info.layoutParams.width = childWidth.toInt()
        cl_gift_info.layoutParams.width = childWidth.toInt()

        //我的守护
        rv_guard_info_images.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_guard_info_images.adapter = mGuardAdapter
        mGuardAdapter.emptyView.findViewById<TextView>(R.id.tv_empty).text = "成为守护神？"
        mGuardAdapter.bindToRecyclerView(rv_guard_info_images)

        //我的座驾
        rv_car_info_images.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_car_info_images.adapter = mCarAdapter
        mCarAdapter.emptyView.findViewById<TextView>(R.id.tv_empty).text = "就差一辆座驾？"
        mCarAdapter.bindToRecyclerView(rv_car_info_images)

        //我的礼物
        rv_gift_info_images.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_gift_info_images.adapter = mGiftAdapter
        mGiftAdapter.emptyView.findViewById<TextView>(R.id.tv_empty).text = "我的礼物呢？"
        mGiftAdapter.bindToRecyclerView(rv_gift_info_images)

        //这个是主播的历史
        //rv_anchor_history.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        //rv_anchor_history.adapter = mAnchorAdapter
        //mAnchorAdapter.emptyView.findViewById<TextView>(R.id.tv_empty).text = "暂无直播历史哦"
        //mAnchorAdapter.bindToRecyclerView(rv_anchor_history)
        //这个是i撩的历史
        //rv_expert_history.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        //rv_expert_history.adapter = mExpertAdapter
        //mExpertAdapter.emptyView.findViewById<TextView>(R.id.tv_empty).text = "暂无i撩历史哦"
        //mExpertAdapter.bindToRecyclerView(rv_expert_history)

    }

    override fun initPresenter(): MineIndexPresenter {
        return MineIndexPresenter()
    }


    private fun initEvent() {

        //个人信息
        iv_user_header.setOnClickListener {
            if (isFastClick()) return@setOnClickListener
            UserManager.getInstance().user?.apply {
                if (!BaseActivity.isFastClick())
                MineDetailActivity.startActivityWithUserId(activity, id)
            }
        }
        ll_tohuami.setOnClickListener{
            if (!BaseActivity.isFastClick())
            ProfitActivity.start(context)
        }
        //设置
        iv_setting.setOnClickListener {
            if (!BaseActivity.isFastClick())
            startActivity(SettingActivity::class.java)
        }

        //扫码
        iv_scan.setOnClickListener {
            if (!BaseActivity.isFastClick())
            startActivity(ScanCodeActivity::class.java)
        }

        //复制id
        tv_user_id_copy.setOnClickListener {
            if (!BaseActivity.isFastClick())
            if (CopyUtils.copy2Board(Objects.requireNonNull(activity), tv_user_id.text.toString()))
                toastTip("复制成功")
            else toastTip("复制失败")
        }

        //分享自己信息给好友
        fl_share_user.setOnClickListener {
            if (!BaseActivity.isFastClick())
            QrCodePopwindow(activity).showPopupWindow()
        }

        //广告图
        iv_mine_banner.setOnClickListener {
            if (!BaseActivity.isFastClick())
            startActivity(AgentActivity::class.java)
        }

        //达人认证
        iv_expert_certification.setOnClickListener {
            if (!BaseActivity.isFastClick())
            mRealNameService?.showRealName(activity, 2, false)
        }
        tv_expert_certification.setOnClickListener {
            if (!BaseActivity.isFastClick())
            mRealNameService?.showRealName(activity, 2, false)
        }

        //修改达人资料
        iv_expert_modify.setOnClickListener {
            if (!BaseActivity.isFastClick())
            ARouter.getInstance().build(ArouterApi.FLIRT_CERTIFICATION).navigation()
        }

        //主播认证
        iv_anchor_certification.setOnClickListener {
            if (!BaseActivity.isFastClick())
            mRealNameService?.showRealName(activity, 1, false)
        }
        tv_anchor_certification.setOnClickListener {
            if (!BaseActivity.isFastClick())
            mRealNameService?.showRealName(activity, 1, false)
        }


        //好友的
        requestLocationEvent(tv_friend_label, tv_friend_value, success = {
            FriendActivity.startActivity(activity, it)
        }, fail = {
            FriendActivity.startActivity(activity, null)
        })

        //关注的
        requestLocationEvent(tv_attention_label, tv_attention_value, success = {
            AttentionActivity.startActivity(activity, it)
        }, fail = {
            AttentionActivity.startActivity(activity, null)
        })

        //粉丝的
        requestLocationEvent(tv_fans_label, tv_fans_value, success = {
            FansActivity.startActivity(activity, it)
        }, fail = {
            FansActivity.startActivity(activity, null)
        })

        //我的守护
        cl_guard_info.setOnClickListener {
            if (!BaseActivity.isFastClick())
            startActivity(MyGuardActivity::class.java)
        }

        //我的公会
        cl_union_info.setOnClickListener {
            if (!BaseActivity.isFastClick())
            startActivity(MyGonghuiActivity::class.java)
        }

        //我的座驾
        cl_car_info.setOnClickListener {
            if (!BaseActivity.isFastClick())
            startActivity(MyCarActivity::class.java)
        }
        //我的礼物墙
        cl_gift_info.setOnClickListener {
            if (!BaseActivity.isFastClick())
            UserManager.getInstance().user?.apply {
                val intent = Intent(activity, MonthGiftWallActivity::class.java)
                intent.putExtra("userId", id)
                startActivity(intent)
            }
        }

        //直播记录
        //mAnchorAdapter.setOnItemClickListener { adapter, _, position ->
        //    val item = adapter.getItem(position) as MineImageDto
        //    item.mAnchorHistory?.apply {
        //        if (status == 2) {
        //            val list = ArrayList<ZhuboDto>()
        //            val zhuboDto = ZhuboDto()
        //            zhuboDto.channelId = channelId
        //            list.add(zhuboDto)
        //            showFVExitDialog(list, position)
        //        } else {
        //            MineDetailActivity.startActivityWithUserId(context, channelId)
        //        }
        //    }
        //
        //}

        // 主播 历史观看记录
        //rv_anchor_history.setOnClickListener {
        //    startActivity(ViewsHistoryActivity::class.java)
        //}

        // i撩 历史观看记录
        //rv_expert_history.setOnClickListener {

        //}

        // 直播时长
        ll_live_time.setOnClickListener {
            if (!BaseActivity.isFastClick())
            LivingTimeActivity.getInstance(activity, 1)
        }

    }

    private fun initData() {
        p.updateUser()
        UserManager.getInstance().user?.apply {
            updateUser(this)
        }
    }

/*    private fun updateViewAnimator(vararg view: View, value: Float) {
        val mValue = if (value > 1) 1f else value
        view.forEach {
            it.apply {
                alpha = mValue
                scaleX = mValue
                scaleY = mValue
            }
        }
    }*/

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
//        if (i == 0) {
//            updateViewAnimator(cl_user_card, iv_mine_banner, tv_back_title, value = 1f)
//            updateViewAnimator(tv_user_title, value = 0f)
//            return
//        }
//        val a = appBarLayout.totalScrollRange / 100f
//        var parcent = abs(i) / a
//        parcent = 1f - parcent / 100f
//        cl_user_card.alpha = parcent
//        updateViewAnimator(cl_user_card, iv_mine_banner, tv_back_title, value = parcent)
//        updateViewAnimator(tv_user_title, value = 1f - parcent)
    }

    //
    // 我的服务
    //
    private fun setServerData(user: UserInfo) {
        val mServer: ArrayList<MineServerDto> = ArrayList()
        mServer.apply {
/*            add(MineServerDto(R.drawable.icon_package, "我的背包") {
                ARouter.getInstance().build(ArouterApi.BACKPACK_ACTION).navigation()
            })*/

            add(MineServerDto(R.drawable.icon_privilege, "贵族特权") {
                if (!BaseActivity.isFastClick())
                startActivity(NobilityPrivilegeActivity::class.java)
            })

            add(MineServerDto(R.drawable.icon_top_up, "充值") {
                if (!BaseActivity.isFastClick())
                startActivity(ChongzhiActivity::class.java)
            })

            add(MineServerDto(R.drawable.icon_bill, "我的账单") {
                if (!BaseActivity.isFastClick())
                startActivity(AccountRecordActivity::class.java)
            })

            add(MineServerDto(R.drawable.icon_anchor_time, "观看历史") {
                if (!BaseActivity.isFastClick())
                startActivity(ViewsHistoryActivity::class.java)
            })

//            if (user.wenboAnchorStatus == UserInfo.WENBO_STATUS_YES) {
//                add(MineServerDto(R.drawable.icon_video_time, "i撩记录") {
//                    LivingTimeActivity.getInstance(activity, 2)
//                })
//            }

            add(MineServerDto(R.drawable.icon_cs, "投诉/建议") {
                if (!BaseActivity.isFastClick())
                startActivity(ComplaintActivity::class.java)
            })

        }
        val mServerAdapter = MineServerAdapter()
        rv_mine_server.adapter = mServerAdapter
        rv_mine_server.layoutManager = GridLayoutManager(context, 4)
        mServerAdapter.bindToRecyclerView(rv_mine_server)
        mServerAdapter.setNewData(mServer)
    }


    /**
     * 关闭悬浮窗提示
     * @param list
     * @param position
     */
    private fun showFVExitDialog(list: ArrayList<ZhuboDto>, position: Int) {
        val floatingView = FloatingView.getInstance()
        if (floatingView.isShow) {
            val dialog = ExitDialog()
            dialog.setNegativeButtonText("取消")
                    .setPositiveButtonText("确定退出")
                    .setTip("进入直播间会退出达人房间\n印象值将归零，是否要退出")
                    .setGear(floatingView.gear)
                    .setNickname(floatingView.nickname)
                    .setExpireTime(floatingView.expireTime)
                    .setHeadImg(floatingView.headImg)
                    .setExitType(ExitDialog.ENTER_LIVING)
                    .addDialogClickListener(object : BaseDialogFragment.OnClickListener {
                        override fun onConfirm() {
                            IntentRoomActivityUrils.setRoomActivity(list.get(0).channelId,list,0)
                         //   LivingRoomActivity.start(activity, list, 0, true)
                        }

                        override fun onCancel() {}
                    })
            dialog.show(childFragmentManager, "")
        } else {
            IntentRoomActivityUrils.setRoomActivity(list.get(position).channelId,list,position)
          //  LivingRoomActivity.start(activity, list, position)
        }
    }


    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    ///////////     这里是网络请求的接口回调     /////////////
    ///////////     这里是网络请求的接口回调     /////////////
    ///////////     这里是网络请求的接口回调     /////////////
    ///////////     这里是网络请求的接口回调     /////////////
    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////

    override fun updateUser(user: UserInfo) {
        p.apply {
            getGiftWall()
            getExpertGiftWall()
            //getWatchHistory()
            getCar()
        }
        setServerData(user)
        ImageLoader.loadImg(iv_user_header, user.headImg)
        tv_user_name.text = user.nickname
     //   tv_user_title.text = user.nickname
        tv_user_id.text = "${user.id}"

        // sex 0 保密 1 男 2 女
        // age 18
        tv_user_age.apply {
            val age = (if (null == user.age) "0" else user.age).toInt()
            if (age == 0 && user.sex == 0) {
                tv_user_age_bg.visibility = View.GONE
                return@apply
            }
            text = if (null == user.age) "" else user.age
            isEnabled = user.sex == 2
            tv_user_age_bg.isEnabled = isEnabled
            if (user.sex == 0) {
                setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
        }

        //用户等级的徽章
        iv_user_level_badge.apply {
            if (user.userLevel > 0) {
                visibility = View.VISIBLE
                setImageResource(ImageLoader.getResId("login_ic_v${user.userLevel}",
                        com.fengwo.module_comment.R.drawable::class.java))
            }
        }

        //主播等级的徽章
        iv_anchor_level_badge.apply {
            if (user.myLiveLevel > 0) {
                visibility = View.VISIBLE
                setImageResource(ImageLoader.getResId("login_ic_type3_v${user.myLiveLevel}",
                        com.fengwo.module_comment.R.drawable::class.java))
            }
        }

        //vip 等级的徽章
        iv_vip_level_badge.apply {
            if (user.getMyVipLevel() > 0) {
                visibility = View.VISIBLE
                setImageResource(ImageLoader.getVipLevel(user.getMyVipLevel()))
            }
        }

        //活动的徽章
        user.userMedalsList?.apply {
            if (isNotEmpty()) {
                iv_active_level_badge.apply {
                    val url = last().medalIcon
                    url?.let {
                        visibility = View.VISIBLE
                        ImageLoader.loadImg(this, url)
                    }
                }
                //这个是头像的相框
                iv_photo_frame.apply {
                    val url = last().medalHeadFrame
                    isShow(!url.isNullOrEmpty())
                    ImageLoader.loadImg(this, url)
                }

            }
        }

        //好友数量
        tv_friend_value.text = DataFormatUtils.formatNumbers(user.friendNum)
        //关注数量
        tv_attention_value.text = DataFormatUtils.formatNumbers(user.attention)
        //粉丝数量
        tv_fans_value.text = DataFormatUtils.formatNumbers(user.fans)
        //花钻值
        tv_flowers_drill_value.text = DataFormatUtils.formatNumbers(user.balance.toDouble())
        //花蜜值
        tv_honey_value.text = DataFormatUtils.formatNumbers(user.profit)
        // 主播认证 达人认证
        setCertification(user)
        //今日直播时长
        tv_live_time.text= TimeUtils.l2String(user.todayLivingTime)
        //今日花蜜值
        tv_nectar_value.text = if (TextUtils.isEmpty(user.todayReceive)) "0" else DataFormatUtils.formatNumberGift(user.todayReceive.toDouble())

        //我的守护
        val mGuards = ArrayList<MineImageDto>()
        user.userGuardList?.forEach {
            val imageDto = MineImageDto()
            imageDto.imageUrl = it.levelIcon
            imageDto.mGuard = it
            mGuards.add(imageDto)
        }
        mGuardAdapter.setNewData(mGuards)

        //公会信息
        user.familyLogo?.apply {
            iv_union_image.visibility = View.VISIBLE
            ImageLoader.loadImg(iv_union_image, this)
            tv_union_info_empty.visibility = View.GONE
        }
        user.familyName?.apply {
            tv_union_name.text = this
            tv_union_name.visibility = View.VISIBLE
            tv_union_info_empty.visibility = View.GONE
        }

        //根据用户类型显示隐藏view
        chooseViewVisible(cl_union_info,user = user)
    }

    //
    // 根据用户类型 显示view
    // 列入 我的公会，我的礼物墙  用户不是文播 或者秀播 就隐藏掉
    //
    private fun chooseViewVisible(vararg view:View,user: UserInfo) {
        view.forEach {
            if (user.myIsCardStatus == 1 || user.wenboAnchorStatus == 1) {
                (it.parent as ViewGroup).getChildAt(0)?.apply {
                    it.layoutParams.width = width
                    it.layoutParams.height = height
                    val dp14 = context.resources.getDimension(R.dimen.dp_14).toInt()
                    it.setPadding(dp14,dp14,dp14,dp14)
                }
            }else{
                it.layoutParams.width = 0
                it.layoutParams.height = 0
                it.setPadding(0,0,0,0)
            }
            it.requestLayout()
        }
    }

    //
    //主播认证 达人认证
    //
    private fun setCertification(user: UserInfo) {
        val gap = resources.getDimension(R.dimen.dp_10).toInt()
        val gapEnd = resources.getDimension(R.dimen.dp_5).toInt()
        //主播认证
        tv_anchor_certification.apply {
            isSelected = false
            setPadding(0, 0, 0, 0)
            when (user.myIsCardStatus) {
                UserInfo.IDCARD_STATUS_NO -> {
                    text = "认证失败，请重新认证"
                    setTextColor(ContextCompat.getColor(context!!, R.color.red_ff3333))
                }
                UserInfo.IDCARD_STATUS_ING -> {
                    text = "认证审核中..."
                    setTextColor(ContextCompat.getColor(context, R.color.green_17CE5D))
                }
                UserInfo.IDCARD_STATUS_YES -> {
                    iv_anchor_certification.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.icon_anchor_certification))
                    ll_live_time.visibility = View.VISIBLE
                    text = "一键开播"
                    isSelected = true
                    setTextColor(ContextCompat.getColor(context!!, R.color.text_white))
                    setPadding(gap, 0, gapEnd, 0)
                }
                else -> {
                    text = "快来成为主播"
                    setTextColor(ContextCompat.getColor(context!!, R.color.text_99))
                }
            }

        }

        //达人认证
        //达人申请状态：0申请中 1申请通过 2申请未通过 3未申请
        tv_expert_certification.apply {
            isSelected = false
            setPadding(0, 0, 0, 0)
            when (user.wenboAnchorStatus) {
                UserInfo.WENBO_STATUS_NULL -> {
                    text = "快来成为达人"
                    setTextColor(ContextCompat.getColor(context!!, R.color.text_99))
                }
                UserInfo.WENBO_STATUS_YES -> {
                    iv_expert_certification.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.icon_expert_certification))
                    iv_expert_modify.visibility = View.VISIBLE
                    text = "开始视频"
                    isSelected = true
                    setTextColor(ContextCompat.getColor(context, R.color.text_white))
                    setPadding(gap, 0, gapEnd, 0)
                }
                UserInfo.WENBO_STATUS_NO -> {
                    text = "认证失败，请重试"
                    setTextColor(ContextCompat.getColor(context, R.color.red_ff3333))
                }
                UserInfo.WENBO_STATUS_ING  -> {
                    text = "认证审核中..."
                    setTextColor(ContextCompat.getColor(context, R.color.green_17CE5D))
                }
            }
        }

    }


    //
    // 获取位置信息 再操作接口回调操作
    //
    private inline fun requestLocationEvent(vararg view: View, crossinline success: (location: AMapLocation) -> Unit, crossinline fail: () -> Unit) {
        view.forEach {
            it.setOnClickListener {
                if (isFastClick()) return@setOnClickListener
                MapLocationUtil.getInstance().startLocationForOnce(object : MapLocationUtil.LocationListener {
                    override fun onLocationSuccess(location: AMapLocation) {
                        success(location)
                    }

                    override fun onLocationFailure(msg: String) {
                        fail()
                    }
                })
            }
        }
    }


    override fun onWatchHistory(ws: BaseListDto<WatchHistoryDto>) {
        //val mAnchor = ArrayList<MineImageDto>()
        //mAnchorAdapter.isHeader = true
        //ws.records?.forEach {
        //    val imageDto = MineImageDto()
        //    imageDto.imageUrl = it.headImg
        //    imageDto.mAnchorHistory = it
        //    mAnchor.add(imageDto)
        //}
        //mAnchorAdapter.setNewData(mAnchor)
    }

    override fun onCar(car: MyCarDto) {
        val mCars = ArrayList<MineImageDto>()
        car.records?.filter { it.isOpened == 1 }?.forEach {
            val imageDto = MineImageDto()
            imageDto.imageUrl = it.motoringThumb
            imageDto.mCar = it
            mCars.add(imageDto)
        }
        mCarAdapter.setNewData(mCars)

    }

    override fun onGiftWall(gift: GiftWallDto) {
        val mGifts = ArrayList<MineImageDto>()
        gift.pageList?.records?.forEach {
            val imageDto = MineImageDto()
            imageDto.imageUrl = it.giftIcon
            imageDto.mGift = it
            mGifts.add(imageDto)
        }
        if (mGifts.isNotEmpty()) mGiftAdapter.setNewData(mGifts)
    }

    override fun reSetRefresh() {
        mine_refresh.finishRefresh()
    }

}