//package com.fengwo.module_live_vedio.mvp.ui.adapter
//
//import android.graphics.Color
//import android.text.*
//import android.text.style.ClickableSpan
//import android.text.style.ForegroundColorSpan
//import android.view.View
//import androidx.recyclerview.widget.RecyclerView
//import com.alibaba.android.arouter.facade.annotation.Autowired
//import com.chad.library.adapter.base.BaseQuickAdapter
//import com.chad.library.adapter.base.BaseViewHolder
//import com.fengwo.module_comment.iservice.UserProviderService
//import com.fengwo.module_comment.utils.DensityUtils
//import com.fengwo.module_comment.utils.ImageLoader
//import com.fengwo.module_comment.utils.L
//import com.fengwo.module_comment.widget.*
//import com.fengwo.module_live_vedio.R
//import com.fengwo.module_live_vedio.utils.PrivilegeEffectUtils
//import com.fengwo.module_websocket.bean.LivingRoomTextMsg
//
//class LivingMsgAdapter(data: List<LivingRoomTextMsg?>?,var  mIsPush: Boolean,var  mUid: Int) : BaseQuickAdapter<LivingRoomTextMsg?, BaseViewHolder>(R.layout.live_item_livingmsg, data) {
//    private var mState = 0 // 0：未关注 1:已关注 2：已互相关注 = 0
//    private var mspColor: Int = Color.parseColor("#FFEA7F");
//
//    @JvmField
//    @Autowired
//    var userProviderService: UserProviderService? = null
//    fun setState(state: Int) {
//        mState = state
//    }
//
//    override fun convert(helper: BaseViewHolder, item: LivingRoomTextMsg?) {
//        if (null == item) {
//            return
//        }
//        try {
//            var spanString: SpannableStringBuilder? = null
//            var builder: StringBuilder? = null
//            val tv = helper.getView<GradientTextView>(R.id.tv_msg)
//            helper.getView<View>(R.id.ll_msg).alpha = 1f
//            tv.setStroke(Color.parseColor("#00000000"), 0)
//            tv.setTextColor(Color.parseColor("#ffffff"))
//            tv.setLineSpacing(1.5f, 1f)
//            tv.setColors(Color.parseColor("#60000000"), Color.parseColor(
//                    "#60000000"))
//            builder = generalStringFirst(item)
//            when (item.type) {
//                LivingRoomTextMsg.TYPE_SYSTEM -> {//普通消息
//                    tv.text = item.message
//                    L.e("lgl", "item_system_color===" + item.systemColor)
//                    if (!TextUtils.isEmpty(item.systemColor)) helper.setTextColor(R.id.tv_msg, Color.parseColor(item.systemColor))
//                }
//                LivingRoomTextMsg.TYPE_PK_MVP -> {// mvp消息
//                    tv.text = item.message
//                    helper.setTextColor(R.id.tv_msg, mspColor)
//                }
//                LivingRoomTextMsg.TYPE_TOUTIAO -> {//头条消息
//                    spanString = SpannableStringBuilder(item.message)
//                    val nickStart = item.message.indexOf(item.nickname)
//                    spanString.setSpan(object : ClickableSpan() {
//                        override fun onClick(view: View) {
//                            if (onNameClickListener != null) {
//                                onNameClickListener?.onNameClick(helper.layoutPosition)
//                            }
//                        }
//
//                        override fun updateDrawState(ds: TextPaint) {
//                            super.updateDrawState(ds)
//                            ds.color = Color.parseColor("#ffffff")
//                            ds.isUnderlineText = false
//                        }
//                    }, nickStart, nickStart + item.nickname.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//                    val span = UrlImageSpan(mContext, item.giftIcon, helper.getView(R.id.tv_msg))
//                    val iconStart = item.message.lastIndexOf("|")
//                    spanString.setSpan(span, iconStart, iconStart + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//                    val toutiaoStart = item.message.indexOf("|")
//                    val toutiaoIcSpan = CenteredImageSpan(mContext, R.drawable.ic_im_toutiao)
//                    spanString.setSpan(toutiaoIcSpan, toutiaoStart, toutiaoStart + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//                    tv.setColors(Color.parseColor("#974DFA"), Color.parseColor("#8400FF"), Color.parseColor("#6600FF"))
//                    //                tv.setMovementMethod(LinkMovementMethod.getInstance());  //很重要，点击无效就是由于没有设置这个引起
//                    if (PrivilegeEffectUtils.getInstance().hasEnterHeader(item.getUserVipLevel())) {
//                        val headerStart = builder.toString().indexOf("0")
//                        val headerSpan = CircleUrlImageSpan(mContext, item.headerurl, helper.getView(R.id.tv_msg), 50, 50)
//                        spanString.setSpan(headerSpan, headerStart, headerStart + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
//                    }
//                    tv.text = spanString
//                }
//                LivingRoomTextMsg.TYPE_GIFT_BOARDCAST -> {
//
//                    val effectDto = PrivilegeEffectUtils.getInstance().getEffectsWithPrice(item.allPrice)
//                    spanString = SpannableStringBuilder(item.message)
//                    if (!TextUtils.isEmpty(item.nickname)) {
//                        val nameStart = item.message.indexOf(item.nickname)
//                        if (nameStart >= 0) {
//                            spanString.setSpan(object : ClickableSpan() {
//                                override fun onClick(view: View) {
//                                    if (onNameClickListener != null) {
//                                        onNameClickListener?.onNameClick(helper.layoutPosition)
//                                    }
//                                }
//
//                                override fun updateDrawState(ds: TextPaint) {
//                                    super.updateDrawState(ds)
//                                    ds.color = Color.parseColor("#ffffff")
//                                    ds.isUnderlineText = false
//                                }
//                            }, nameStart, nameStart + item.nickname.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//                        }
//                    }
//                    if (!TextUtils.isEmpty(item.giftIcon)) {
//                        val span = UrlImageSpan(mContext, item.giftIcon, helper.getView(R.id.tv_msg))
//                        val start = item.message.indexOf("*")
//                        spanString.setSpan(span, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                    } else {
//                        item.message.replace("*", "")
//                    }
//                    if (!TextUtils.isEmpty(effectDto.bubbleColor)) {
//                        val color = Color.parseColor(effectDto.bubbleColor)
//                        tv.setColors(color, color)
//                    }
//                    if (!TextUtils.isEmpty(effectDto.borderColor)) {
//                        val color = Color.parseColor(effectDto.borderColor)
//                        tv.setStroke(color, mContext.resources.getDimension(R.dimen.dp_1_5).toInt())
//                    }
//                    if (PrivilegeEffectUtils.getInstance().hasEnterHeader(item.getUserVipLevel())) {
//                        val headerStart = builder.toString().indexOf("0")
//                        val headerSpan = CircleUrlImageSpan(mContext, item.headerurl, helper.getView(R.id.tv_msg), 50, 50)
//                        spanString.setSpan(headerSpan, headerStart, headerStart + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
//                    }
//                    tv.text = spanString
//
//                }
//                LivingRoomTextMsg.TYPE_SOMECOMEING -> {   //用户进入
//                    builder.append(" ").append(item.nickname)
//                    if (!TextUtils.isEmpty(item.carName) && !TextUtils.isEmpty(item.guardCarName)) {
//                        builder.append(" 驾着 「")
//                                .append(item.carName)
//                                .append("」 「")
//                                .append(item.guardCarName)
//                                .append("」")
//                    } else {
//                        if (!TextUtils.isEmpty(item.carName)) {
//                            builder.append(" 驾着 「")
//                                    .append(item.carName)
//                                    .append("」 ")
//                        }
//                        if (!TextUtils.isEmpty(item.guardCarName)) {
//                            builder.append(" 驾着 「")
//                                    .append(item.guardCarName)
//                                    .append("」 ")
//                        }
//                    }
//                    if (item.getUserVipLevel() > 0) {
//                        builder.append(" 闪亮登场")
//                    } else {
//                        builder.append(" 来了")
//                    }
//                    if (!TextUtils.isEmpty(item.uid)) {
//                        if (mUid != item.uid.toInt()) {
//                            builder.append("  teaseHim")
//                        }
//                    }
//                    if (TextUtils.isEmpty(item.systemColor)) {
//                        helper.setTextColor(R.id.tv_msg, Color.parseColor("#ffffff"))
//                    } else {
//                        helper.setTextColor(R.id.tv_msg, Color.parseColor(item.systemColor))
//                    }
//                    spanString = SpannableStringBuilder(builder.toString()) //123是占位符 为了替换图片 1等级 2主播等级 3vip等级 4守护 5房管
//                    if (PrivilegeEffectUtils.getInstance().hasEnterHeader(item.getUserVipLevel())) {
//                        val headerStart = builder.toString().indexOf("0")
//                        val headerSpan = CircleUrlImageSpan(mContext, item.headerurl, helper.getView(R.id.tv_msg), 50, 50)
//                        spanString.setSpan(headerSpan, headerStart, headerStart + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
//                    }
//                    if (item.getUserVipLevel() > 0) {
//                        spanString.setSpan(ForegroundColorSpan(Color.parseColor(PrivilegeEffectUtils.getInstance().getEffectsWithVip(1).comingTxtColor)), spanString.length - 4, spanString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) //-4 闪亮登场
//                    }
//                    val start = spanString.toString().indexOf(item.nickname)
//                    spanString.setSpan(object : ClickableSpan() {
//                        override fun onClick(view: View) {
//                            if (onNameClickListener != null) {
//                                if (item.isTourist > 0) {
//                                    return
//                                }
//                                onNameClickListener?.onNameClick(helper.layoutPosition)
//                            }
//                        }
//
//                        override fun updateDrawState(ds: TextPaint) {
//                            super.updateDrawState(ds)
//                            if (null != item.systemColor) ds.color = Color.parseColor(item.systemColor)
//                            ds.isUnderlineText = false
//                        }
//                    }, start, start + item.nickname.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//                }
//                LivingRoomTextMsg.TYPE_GIFT_BOARDCAST -> {
//                    if (item.type == LivingRoomTextMsg.TYPE_BUY_GUARD) {   //购买守护
//                        spanString = SpannableStringBuilder(builder.append(" ").append(item.message))
//                    } else if (item.type == LivingRoomTextMsg.TYPE_TEASE_HIM) {   //撩他一下
//                        builder.append(item.nickname).append(" 对 ").append(item.toNickname).append(item.message)
//                        builder.append("  teaseHim")
//                        spanString = SpannableStringBuilder(builder)
//                    } else if (item.type == LivingRoomTextMsg.TYPE_ANCHOR_ATTENTION) {   //关注主播
//                        builder.append(" ")
//                                .append(item.nickname).append("：")
//                                .append(item.message)
//                        if (!TextUtils.isEmpty(item.uid)) {
//                            if (mUid != item.uid.toInt()) {
//                                if (0 == mState && !mIsPush) builder.append("  attention")
//                            }
//                        }
//                        spanString = SpannableStringBuilder(builder.toString())
//                    } else if (item.type == LivingRoomTextMsg.TYPE_ANCHOR_SHARE) {   //分享主播
//                        builder.append(" ")
//                                .append(item.nickname).append("：")
//                                .append(item.message)
//                        if (!TextUtils.isEmpty(item.uid)) {
//                            if (mUid != item.uid.toInt()) {
//                                builder.append("  share")
//                            }
//                        }
//                        spanString = SpannableStringBuilder(builder.toString())
//                    } else {
//                        builder.append(" " + item.nickname).append("：").append(item.message)
//                        spanString = SpannableStringBuilder(builder.toString()) //1是占位符 为了替换图片
//                        if (!TextUtils.isEmpty(item.userMsgColor)) spanString.setSpan(ForegroundColorSpan(Color.parseColor(item.userMsgColor)), spanString.length - item.message.length, spanString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//                        spanString.setSpan(object : ClickableSpan() {
//                            override fun onClick(widget: View) {
//                                if (onNameClickListener != null) {
//                                    onNameClickListener?.onNameClick(helper.layoutPosition)
//                                }
//                            }
//                            override fun updateDrawState(ds: TextPaint) {
//                                super.updateDrawState(ds)
//                                if (null != item.userNickColor) ds.color = Color.parseColor(item.userNickColor)
//                                ds.isUnderlineText = false
//                            }
//                        }, 0, spanString.length - item.message.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//                    }
//
//                    //添加姓名的颜色
//                    if (!TextUtils.isEmpty(item.userNickColor)) {
//                        val start = spanString.toString().indexOf(item.nickname)
//                        spanString.setSpan(ForegroundColorSpan(Color.parseColor(item.userNickColor)), start, start + item.nickname.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//                    }
//                    if (PrivilegeEffectUtils.getInstance().hasEnterHeader(item.getUserVipLevel())) {
//                        val headerStart = builder.toString().indexOf("0")
//                        val headerSpan = CircleUrlImageSpan(mContext, item.headerurl, helper.getView(R.id.tv_msg), 50, 50)
//                        spanString.setSpan(headerSpan, headerStart, headerStart + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
//                    }
//                    if (!TextUtils.isEmpty(item.level) && item.level.toInt() > 0) {
//                        val levelStart = builder.toString().indexOf("1")
//                        val levelSpan = CenteredImageSpan(mContext, ImageLoader.getUserLevel(item.level.toInt()))
//                        spanString.setSpan(levelSpan, levelStart, levelStart + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                    } else {
//                        val levelStart = builder.toString().indexOf("1")
//                        val levelSpan = CenteredImageSpan(mContext, R.drawable.ic_span_holder)
//                        spanString.setSpan(levelSpan, levelStart, levelStart + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                    }
//                    if (!TextUtils.isEmpty(item.userVipLevel) && item.userVipLevel.toInt() > 0) {
//                        val start = builder.toString().indexOf("3")
//                        val vipSpan = CenteredImageSpan(mContext, ImageLoader.getVipLevel(item.userVipLevel.toInt()))
//                        spanString.setSpan(vipSpan, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                    }
//                    if (!TextUtils.isEmpty(item.userShouHuLevelIMG)) {
//                        val start = builder.toString().indexOf("4")
//                        val span = UrlImageSpan(mContext, item.userShouHuLevelIMG, helper.getView(R.id.tv_msg), DensityUtils.dp2px(mContext, 14f), DensityUtils.dp2px(mContext, 14f))
//                        spanString.setSpan(span, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                    }
//                    if (item.isRoomManage) { //替换房管图片
//                        val start = builder.toString().indexOf("5")
//                        val roomSpan = CenteredImageSpan(mContext, R.drawable.ic_roommanager, DensityUtils.dp2px(mContext, 14f), DensityUtils.dp2px(mContext, 14f))
//                        spanString.setSpan(roomSpan, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                    }
//                    if (!TextUtils.isEmpty(item.userVipLevel)) {
//                        val vipLevel = item.userVipLevel.toInt()
//                        if (vipLevel == 3) {
//                            val color = PrivilegeEffectUtils.getInstance().vip3Color
//                            tv.setColors(color, color)
//                        }
//                    }
//                    if (item.type == LivingRoomTextMsg.TYPE_ANCHOR_ATTENTION && 0 == mState && !mIsPush) {   //关注主播
//                        if (!TextUtils.isEmpty(item.uid)) {
//                            if (mUid != item.uid.toInt()) {     //自己不加入图片
//                                val start = builder.toString().indexOf("attention")
//                                val roomSpan: ClickableImageSpan = object : ClickableImageSpan(mContext, R.drawable.ic_live_anchor_attention) {
//                                    override fun onClick(view: View) {
//                                        if (onNameClickListener != null && mState == 0) {
//                                            onNameClickListener?.onAnchorAttentionClick(helper.layoutPosition)
//                                        }
//                                    }
//                                }
//                                spanString.setSpan(roomSpan, start, start + 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                            }
//                        }
//                    }
//                    if (item.type == LivingRoomTextMsg.TYPE_ANCHOR_SHARE) {   //分享主播
//                        if (!TextUtils.isEmpty(item.uid)) {
//                            if (mUid != item.uid.toInt()) {    //自己不加入图片
//                                val start = builder.toString().indexOf("share")
//                                val roomSpan: ClickableImageSpan = object : ClickableImageSpan(mContext, R.drawable.ic_live_anchor_share) {
//                                    override fun onClick(view: View) {
//                                        if (onNameClickListener != null) {
//                                            onNameClickListener?.onShareAttentionClick(helper.layoutPosition)
//                                        }
//                                    }
//                                }
//                                spanString.setSpan(roomSpan, start, start + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                            }
//                        }
//                    }
//                    if (item.type == LivingRoomTextMsg.TYPE_TEASE_HIM) {   //撩他一下  图片
//                        //添加姓名的颜色
//                        if (!TextUtils.isEmpty(item.userNickColor)) {
//                            val start = spanString.toString().indexOf(item.nickname)
//                            spanString.setSpan(ForegroundColorSpan(Color.parseColor(item.userNickColor)), start, start + item.nickname.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//                        }
//                        //添加姓名的颜色
//                        if (!TextUtils.isEmpty(item.toUserNickColor)) {
//                            val start = spanString.toString().indexOf(item.toNickname)
//                            spanString.setSpan(ForegroundColorSpan(Color.parseColor(item.toUserNickColor)), start, start + item.toNickname.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//                        }
//                        val start = builder.toString().indexOf("teaseHim")
//                        val roomSpan: ClickableImageSpan = object : ClickableImageSpan(mContext, R.drawable.ic_live_tease_him_bg) {
//                            override fun onClick(view: View) {}
//                        }
//                        spanString.setSpan(roomSpan, start, start + 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                    }
//                    if (item.type == LivingRoomTextMsg.TYPE_SOMECOMEING) {   //新用户进入
//                        if (!TextUtils.isEmpty(item.uid)) {
//                            val replace = builder.toString().indexOf("teaseHim")
//                            if (item.isTeaseHim) {
//                                builder.replace(replace, replace + 8, "")
//                                spanString.replace(replace, replace + 8, "")
//                                spanString.setSpan(builder, 0, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                            } else {
//                                if (mUid != item.uid.toInt()) {     //自己不加入图片
//                                    val start = builder.toString().indexOf("teaseHim")
//                                    val roomSpan: ClickableImageSpan = object : ClickableImageSpan(mContext, R.drawable.ic_live_tease_him) {
//                                        override fun onClick(view: View) {
//                                            if (onNameClickListener != null) {
//                                                onNameClickListener?.onTeaseHimClick(helper.layoutPosition)
//                                            }
//                                        }
//                                    }
//                                    spanString.setSpan(roomSpan, start, start + 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                                }
//                            }
//                        }
//                    }
//                    tv.text = spanString
//                }
//                LivingRoomTextMsg.TYPE_GIFT_BOARDCAST -> {
//
//                }
//                LivingRoomTextMsg.TYPE_GIFT_BOARDCAST -> {
//                    tv.movementMethod = ClickableMovementMethod.getInstance() //很重要，点击无效就是由于没有设置这个引起
//                }
//                LivingRoomTextMsg.TYPE_LIVERS->{
//
//                }
//            }
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun generalStringFirst(item: LivingRoomTextMsg): StringBuilder {
//        val builder = StringBuilder()
//        if (!TextUtils.isEmpty(item.userVipLevel)) {
//            //item.type == LivingRoomTextMsg.TYPE_SOMECOMEING &&
//            if (PrivilegeEffectUtils.getInstance().hasEnterHeader(item.userVipLevel.toInt())) {
//                builder.append("0") //头像
//            }
//        }
//        builder.append(" 1") //1用户等级 2主播等级 3vip等级 4守护等级 5 房管
//        if (!TextUtils.isEmpty(item.anchorLevel)) {
//            builder.append(" 2")
//        }
//        if (!TextUtils.isEmpty(item.userVipLevel) && item.userVipLevel.toInt() > 0) {
//            builder.append(" 3")
//        }
//        if (!TextUtils.isEmpty(item.userShouHuLevelIMG)) {
//            builder.append(" 4")
//        }
//        if (item.isRoomManage) {
//            builder.append(" 5")
//        }
//        return builder
//    }
//
//    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
//        super.onDetachedFromRecyclerView(recyclerView)
//    }
//
//    private fun generalStringEnd(item: LivingRoomTextMsg): StringBuilder {
//        val builder = StringBuilder()
//        builder.append(" 6")
//        return builder
//    }
//
//    fun clearData() {
//        mData.clear()
//        notifyDataSetChanged()
//    }
//
//    interface onAddNameClickListener {
//        fun onNameClick(position: Int)
//        fun onTeaseHimClick(position: Int)
//        fun onAnchorAttentionClick(position: Int)
//        fun onShareAttentionClick(position: Int)
//    }
//
//    private var onNameClickListener: onAddNameClickListener? = null
//    fun setOnNameClickListener(onNameClickListener: onAddNameClickListener?) {
//        this.onNameClickListener = onNameClickListener
//    }
//
//
//
//}