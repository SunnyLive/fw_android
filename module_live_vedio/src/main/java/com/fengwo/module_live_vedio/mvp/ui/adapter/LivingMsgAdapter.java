package com.fengwo.module_live_vedio.mvp.ui.adapter;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.widget.CenteredImageSpan;
import com.fengwo.module_comment.widget.CircleUrlImageSpan;
import com.fengwo.module_comment.widget.ClickableImageSpan;
import com.fengwo.module_comment.widget.ClickableMovementMethod;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_comment.widget.UrlImageSpan;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.GiftEffectDto;
import com.fengwo.module_live_vedio.utils.PrivilegeEffectUtils;
import com.fengwo.module_live_vedio.widget.LiversMsgTextView;
import com.fengwo.module_websocket.bean.LivingRoomTextMsg;

import java.util.List;


public class LivingMsgAdapter extends BaseQuickAdapter<LivingRoomTextMsg, BaseViewHolder> {
    private int mState;   // 0：未关注 1:已关注 2：已互相关注
    protected boolean mIsPush = false;//是不是主播端 默认false
    private int mUid;
    @Autowired
    UserProviderService userProviderService;

    //    private HashMap<String, String> mMap;
//    private HashMap<String, String> mAttentionMap;
    public LivingMsgAdapter(@Nullable List<LivingRoomTextMsg> data, boolean isPush, int uid) {
        super(R.layout.live_item_livingmsg, data);
        mIsPush = isPush;
        mUid = uid;
//        mMap = new HashMap<>();
//        mAttentionMap = new HashMap<>();

    }

    public void setState(int state) {
        this.mState = state;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LivingRoomTextMsg item) {
        if (null == item) {
            return;
        }
        try {
//            if (item.nickname.contains("ە")){
//                item.nickname = item.nickname.replace("ە",".");
//            }
            SpannableStringBuilder spanString = null;
            StringBuilder builder = null;
            GradientTextView tv = helper.getView(R.id.tv_msg);
            LiversMsgTextView liversMsgTextView = helper.getView(R.id.tv_msg_livers);
            liversMsgTextView.setVisibility(View.GONE);
            helper.getView(R.id.ll_msg).setAlpha(1);
            tv.setStroke(Color.parseColor("#00000000"), 0);
            tv.setTextColor(Color.parseColor("#ffffff"));
            tv.setLineSpacing(1.5f, 1f);
            tv.setColors(Color.parseColor("#60000000"), Color.parseColor(
                    "#60000000"));

            builder = generalStringFirst(item);


            if (item.type == LivingRoomTextMsg.TYPE_SYSTEM) {
                tv.setText(item.message);
                L.e("lgl", "item_system_color===" + item.systemColor);
                if (!TextUtils.isEmpty(item.systemColor))
                    helper.setTextColor(R.id.tv_msg, Color.parseColor(item.systemColor));
            } else if (item.type == LivingRoomTextMsg.TYPE_PK_MVP) {
                tv.setText(item.message);
                helper.setTextColor(R.id.tv_msg, Color.parseColor("#FFEA7F"));
            } else if (item.type == LivingRoomTextMsg.TYPE_TOUTIAO) {
                spanString = new SpannableStringBuilder(item.message);
                int nickStart = item.message.indexOf(item.nickname);
                spanString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        if (onNameClickListener != null) {
                            onNameClickListener.onNameClick(helper.getLayoutPosition());
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.parseColor("#ffffff"));
                        ds.setUnderlineText(false);
                    }
                }, nickStart, nickStart + item.nickname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                UrlImageSpan span = new UrlImageSpan(mContext, item.giftIcon, helper.getView(R.id.tv_msg));
                int iconStart = item.message.lastIndexOf("|");
                spanString.setSpan(span, iconStart, iconStart + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                int toutiaoStart = item.message.indexOf("|");
                CenteredImageSpan toutiaoIcSpan = new CenteredImageSpan(mContext, R.drawable.ic_im_toutiao);
                spanString.setSpan(toutiaoIcSpan, toutiaoStart, toutiaoStart + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setColors(Color.parseColor("#974DFA"), Color.parseColor("#8400FF"), Color.parseColor("#6600FF"));
//                tv.setMovementMethod(LinkMovementMethod.getInstance());  //很重要，点击无效就是由于没有设置这个引起
                if (PrivilegeEffectUtils.getInstance().hasEnterHeader(item.getUserVipLevel())) {
                    int headerStart = builder.toString().indexOf("0");
                    CircleUrlImageSpan headerSpan = new CircleUrlImageSpan(mContext, item.headerurl, helper.getView(R.id.tv_msg), 50, 50);
                    spanString.setSpan(headerSpan, headerStart, headerStart + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                tv.setText(spanString);
            } else if (item.type == LivingRoomTextMsg.TYPE_GIFT_BOARDCAST) {
                GiftEffectDto effectDto = PrivilegeEffectUtils.getInstance().getEffectsWithPrice(item.allPrice);
                spanString = new SpannableStringBuilder(item.message);
                if (!TextUtils.isEmpty(item.nickname)) {
                    int nameStart = item.message.indexOf(item.nickname);
                    if (nameStart >= 0) {
                        spanString.setSpan(new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View view) {
                                if (onNameClickListener != null) {
                                    onNameClickListener.onNameClick(helper.getLayoutPosition());
                                }
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setColor(Color.parseColor("#ffffff"));
                                ds.setUnderlineText(false);
                            }
                        }, nameStart, nameStart + item.nickname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                if (!TextUtils.isEmpty(item.giftIcon)) {
                    UrlImageSpan span = new UrlImageSpan(mContext, item.giftIcon, helper.getView(R.id.tv_msg));
                    int start = item.message.indexOf("*");
                    spanString.setSpan(span, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    item.message.replace("*", "");
                }
                if (!TextUtils.isEmpty(effectDto.getBubbleColor())) {
                    int color = Color.parseColor(effectDto.getBubbleColor());
                    tv.setColors(color, color);
                }
                if (!TextUtils.isEmpty((effectDto.getBorderColor()))) {
                    int color = Color.parseColor(effectDto.getBorderColor());
                    tv.setStroke(color, (int) mContext.getResources().getDimension(R.dimen.dp_1_5));
                }
                if (PrivilegeEffectUtils.getInstance().hasEnterHeader(item.getUserVipLevel())) {
                    int headerStart = builder.toString().indexOf("0");
                    CircleUrlImageSpan headerSpan = new CircleUrlImageSpan(mContext, item.headerurl, helper.getView(R.id.tv_msg), 50, 50);
                    spanString.setSpan(headerSpan, headerStart, headerStart + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                tv.setText(spanString);
            } else {
                if (item.type == LivingRoomTextMsg.TYPE_SOMECOMEING) {   //用户进入
                    builder.append(" ").append(item.nickname);
                    if (!TextUtils.isEmpty(item.carName) && !TextUtils.isEmpty(item.guardCarName)) {
                        builder.append(" 驾着 「")
                                .append(item.carName)
                                .append("」 「")
                                .append(item.guardCarName)
                                .append("」");
                    } else {
                        if (!TextUtils.isEmpty(item.carName)) {
                            builder.append(" 驾着 「")
                                    .append(item.carName)
                                    .append("」 ");
                        }
                        if (!TextUtils.isEmpty(item.guardCarName)) {
                            builder.append(" 驾着 「")
                                    .append(item.guardCarName)
                                    .append("」 ");
                        }
                    }
                    if (item.getUserVipLevel() > 0) {
                        builder.append(" 闪亮登场");
                    } else {
                        builder.append(" 来了");
                    }
                    if (!TextUtils.isEmpty(item.uid)) {
                        if (mUid != Integer.parseInt(item.uid)) {
                            builder.append("  teaseHim");
                        }
                    }
                    if (TextUtils.isEmpty(item.systemColor)) {
                        helper.setTextColor(R.id.tv_msg, Color.parseColor("#ffffff"));
                    } else {
                        helper.setTextColor(R.id.tv_msg, Color.parseColor(item.systemColor));
                    }
                    spanString = new SpannableStringBuilder(builder.toString());//123是占位符 为了替换图片 1等级 2主播等级 3vip等级 4守护 5房管
                    if (PrivilegeEffectUtils.getInstance().hasEnterHeader(item.getUserVipLevel())) {
                        int headerStart = builder.toString().indexOf("0");
                        CircleUrlImageSpan headerSpan = new CircleUrlImageSpan(mContext, item.headerurl, helper.getView(R.id.tv_msg), 50, 50);
                        spanString.setSpan(headerSpan, headerStart, headerStart + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                    if (item.getUserVipLevel() > 0) {
                        spanString.setSpan(new ForegroundColorSpan(Color.parseColor(PrivilegeEffectUtils.getInstance().getEffectsWithVip(1).getComingTxtColor())), spanString.length() - 4, spanString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//-4 闪亮登场
                    }
                    int start = spanString.toString().indexOf(item.nickname);
                    spanString.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View view) {
                            if (onNameClickListener != null) {
                                if (item.isTourist > 0) {
                                    return;
                                }
                                onNameClickListener.onNameClick(helper.getLayoutPosition());
                            }
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            if (null != item.systemColor)
                                ds.setColor(Color.parseColor(item.systemColor));
                            ds.setUnderlineText(false);
                        }
                    }, start, start + item.nickname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                } else if (item.type == LivingRoomTextMsg.TYPE_BUY_GUARD) {   //购买守护
                    spanString = new SpannableStringBuilder(builder.append(" ").append(item.message));
                } else if (item.type == LivingRoomTextMsg.TYPE_TEASE_HIM) {   //撩他一下
                    builder.append(item.nickname).append(" 对 ").append(item.toNickname).append(item.message);
                    builder.append("  teaseHim");
                    spanString = new SpannableStringBuilder(builder);
                } else if (item.type == LivingRoomTextMsg.TYPE_ANCHOR_ATTENTION) {   //关注主播
                    builder.append(" ")
                            .append(item.nickname).append("：")
                            .append(item.message);
                    if (!TextUtils.isEmpty(item.uid)) {
                        if (mUid != Integer.parseInt(item.uid)) {
                            if (0 == mState && !mIsPush)
                                builder.append("  attention");
                        }
                    }
                    spanString = new SpannableStringBuilder(builder.toString());
                } else if (item.type == LivingRoomTextMsg.TYPE_ANCHOR_SHARE) {   //分享主播
                    builder.append(" ")
                            .append(item.nickname).append("：")
                            .append(item.message);
                    if (!TextUtils.isEmpty(item.uid)) {
                        if (mUid != Integer.parseInt(item.uid)) {
                            builder.append("  share");
                        }
                    }
                    spanString = new SpannableStringBuilder(builder.toString());
                } else {
                    builder.append(" " + item.nickname).append("：").append(item.message);
                    spanString = new SpannableStringBuilder(builder.toString());//1是占位符 为了替换图片
                    if (!TextUtils.isEmpty(item.userMsgColor))
                        spanString.setSpan(new ForegroundColorSpan(Color.parseColor(item.userMsgColor)), spanString.length() - item.message.length(), spanString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanString.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            if (onNameClickListener != null) {
                                onNameClickListener.onNameClick(helper.getLayoutPosition());
                            }
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            if (null != item.userNickColor)
                                ds.setColor(Color.parseColor(item.userNickColor));
                            ds.setUnderlineText(false);
                        }
                    }, 0, spanString.length() - item.message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                //添加姓名的颜色
                if (!TextUtils.isEmpty(item.userNickColor)) {
                    int start = spanString.toString().indexOf(item.nickname);
                    spanString.setSpan(new ForegroundColorSpan(Color.parseColor(item.userNickColor)), start, start + item.nickname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (PrivilegeEffectUtils.getInstance().hasEnterHeader(item.getUserVipLevel())) {
                    int headerStart = builder.toString().indexOf("0");
                    CircleUrlImageSpan headerSpan = new CircleUrlImageSpan(mContext, item.headerurl, helper.getView(R.id.tv_msg), 50, 50);
                    spanString.setSpan(headerSpan, headerStart, headerStart + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }


                if (item.type == LivingRoomTextMsg.TYPE_TEASE_HIM) {   //撩他一下  图片
                    //添加姓名的颜色
                    if (!TextUtils.isEmpty(item.userNickColor)) {
                        int start = spanString.toString().indexOf(item.nickname);
                        spanString.setSpan(new ForegroundColorSpan(Color.parseColor(item.userNickColor)), start, start + item.nickname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    //添加姓名的颜色
                    if (!TextUtils.isEmpty(item.toUserNickColor)) {
                        int start = spanString.toString().indexOf(item.toNickname);
                        spanString.setSpan(new ForegroundColorSpan(Color.parseColor(item.toUserNickColor)), start, start + item.toNickname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    int start = builder.toString().indexOf("teaseHim");
                    ClickableImageSpan roomSpan = new ClickableImageSpan(mContext, R.drawable.ic_live_tease_him_bg) {
                        @Override
                        public void onClick(View view) {
                        }
                    };
                    spanString.setSpan(roomSpan, start, start + 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (item.type == LivingRoomTextMsg.TYPE_ANCHOR_SHARE) {   //分享主播
                    if (!TextUtils.isEmpty(item.uid)) {
                        if (mUid != Integer.parseInt(item.uid)) {    //自己不加入图片
                            int start = builder.toString().indexOf("share");
                            ClickableImageSpan roomSpan = new ClickableImageSpan(mContext, R.drawable.ic_live_anchor_share) {
                                @Override
                                public void onClick(View view) {
                                    if (onNameClickListener != null) {
                                        onNameClickListener.onShareAttentionClick(helper.getLayoutPosition());
                                    }
                                }
                            };
                            spanString.setSpan(roomSpan, start, start + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
                if (item.type != LivingRoomTextMsg.TYPE_TEASE_HIM&&item.type!=LivingRoomTextMsg.TYPE_ANCHOR_ATTENTION) {
                    if (!TextUtils.isEmpty(item.level) && Integer.parseInt(item.level) > 0) {
                        int levelStart = builder.toString().indexOf("1");
                        CenteredImageSpan levelSpan = new CenteredImageSpan(mContext, ImageLoader.getUserLevel(Integer.parseInt(item.level)));
                        spanString.setSpan(levelSpan, levelStart, levelStart + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        int levelStart = builder.toString().indexOf("1");
                        CenteredImageSpan levelSpan = new CenteredImageSpan(mContext, R.drawable.ic_span_holder);
                        spanString.setSpan(levelSpan, levelStart, levelStart + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (!TextUtils.isEmpty(item.userVipLevel) && Integer.parseInt(item.userVipLevel) > 0) {
                        int start = builder.toString().indexOf("3");
                        CenteredImageSpan vipSpan = new CenteredImageSpan(mContext, ImageLoader.getVipLevel(Integer.parseInt(item.userVipLevel)));
                        spanString.setSpan(vipSpan, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (!TextUtils.isEmpty(item.userShouHuLevelIMG)) {
                        int start = builder.toString().indexOf("4");
                        UrlImageSpan span = new UrlImageSpan(mContext, item.userShouHuLevelIMG, helper.getView(R.id.tv_msg), DensityUtils.dp2px(mContext, 14), DensityUtils.dp2px(mContext, 14));
                        spanString.setSpan(span, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (item.isRoomManage) {//替换房管图片
                        int start = builder.toString().indexOf("5");
                        CenteredImageSpan roomSpan = new CenteredImageSpan(mContext, R.drawable.ic_roommanager, DensityUtils.dp2px(mContext, 14), DensityUtils.dp2px(mContext, 14));
                        spanString.setSpan(roomSpan, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (item.isOfficialUser==1) {//替换房管图片
                        int start = builder.toString().indexOf("7");
                        CenteredImageSpan roomSpan = new CenteredImageSpan(mContext, R.drawable.pic_gm, DensityUtils.dp2px(mContext, 14), DensityUtils.dp2px(mContext, 14));
                        spanString.setSpan(roomSpan, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (!TextUtils.isEmpty(item.userVipLevel)) {
                        int vipLevel = Integer.parseInt(item.userVipLevel);
                        if (vipLevel == 3) {
                            int color = PrivilegeEffectUtils.getInstance().getVip3Color();
                            tv.setColors(color, color);
                        }
                    }


                }
                if (item.type == LivingRoomTextMsg.TYPE_ANCHOR_ATTENTION && 0 == mState && !mIsPush) {   //关注主播
                    if (!TextUtils.isEmpty(item.uid)) {
                        if (mUid != Integer.parseInt(item.uid)) {     //自己不加入图片
                            int start = builder.toString().indexOf("attention");
                            ClickableImageSpan roomSpan = new ClickableImageSpan(mContext, R.drawable.ic_live_anchor_attention) {
                                @Override
                                public void onClick(View view) {
                                    if (onNameClickListener != null && mState == 0) {
//                                        if (mAttentionMap != null && mAttentionMap.size() == 0) {
//                                            onNameClickListener.onAnchorAttentionClick(helper.getLayoutPosition());
//                                            mAttentionMap.put(item.uid, item.uid);
//                                        } else {
//                                            if (mAttentionMap != null && TextUtils.isEmpty(mAttentionMap.get(item.uid))) {
//                                                onNameClickListener.onAnchorAttentionClick(helper.getLayoutPosition());
//                                                mAttentionMap.put(item.uid, item.uid);
//                                            }
//                                        }
                                        onNameClickListener.onAnchorAttentionClick(helper.getLayoutPosition());
                                    }
                                }
                            };
                            spanString.setSpan(roomSpan, start, start + 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }


                if (item.type == LivingRoomTextMsg.TYPE_SOMECOMEING) {   //新用户进入
                    if (!TextUtils.isEmpty(item.uid)) {
                        int replace = builder.toString().indexOf("teaseHim");
                        if (item.isTeaseHim) {
                            builder.replace(replace, replace + 8, "");
                            spanString.replace(replace, replace + 8, "");
                            spanString.setSpan(builder, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else {
                            if (mUid != Integer.parseInt(item.uid)) {     //自己不加入图片
                                int start = builder.toString().indexOf("teaseHim");
                                ClickableImageSpan roomSpan = new ClickableImageSpan(mContext, R.drawable.ic_live_tease_him) {
                                    @Override
                                    public void onClick(View view) {
                                        if (onNameClickListener != null) {
//                                        if (mMap != null && mMap.size() == 0) {
//                                            onNameClickListener.onTeaseHimClick(helper.getLayoutPosition());
//                                            mMap.put(item.uid, item.uid);
//                                        } else {
//                                            if (mMap != null && TextUtils.isEmpty(mMap.get(item.uid))) {
//                                                onNameClickListener.onTeaseHimClick(helper.getLayoutPosition());
//                                                mMap.put(item.uid, item.uid);
//                                            }
//                                        }
                                            onNameClickListener.onTeaseHimClick(helper.getLayoutPosition());
                                        }
                                    }
                                };
                                spanString.setSpan(roomSpan, start, start + 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }

                    }
                }
                if (!TextUtils.isEmpty(item.cpRank)) {
                    tv.setVisibility(View.GONE);
                    liversMsgTextView.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(item.cpRank) <= 0 && Integer.parseInt(item.cpRank) < 11) {
                        int start = builder.toString().indexOf("6");
                        int res = ImageLoader.getResId("pic_cp" + item.cpRank, R.drawable.class);
                        CenteredImageSpan toutiaoIcSpan = new CenteredImageSpan(mContext, res);
                        spanString.setSpan(toutiaoIcSpan, start, start + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }

                    liversMsgTextView.setMovementMethod(ClickableMovementMethod.getInstance());
                    liversMsgTextView.setText(spanString);
                } else {
                    tv.setText(spanString);
                }

            }
            tv.setMovementMethod(ClickableMovementMethod.getInstance());  //很重要，点击无效就是由于没有设置这个引起
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StringBuilder generalStringFirst(LivingRoomTextMsg item) {
        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(item.userVipLevel)) {
            //item.type == LivingRoomTextMsg.TYPE_SOMECOMEING &&
            if (PrivilegeEffectUtils.getInstance().hasEnterHeader(Integer.parseInt(item.userVipLevel))) {
                builder.append("0");//头像
            }
        }
        if (item.type != LivingRoomTextMsg.TYPE_TEASE_HIM&&item.type!=LivingRoomTextMsg.TYPE_ANCHOR_ATTENTION) {//分享撩一下 不显示徽章
            builder.append(" 1");//1用户等级 2主播等级 3vip等级 4守护等级 5 房管

            if (!TextUtils.isEmpty(item.anchorLevel)) {
                builder.append(" 2");
            }


            if (!TextUtils.isEmpty(item.userVipLevel) && Integer.parseInt(item.userVipLevel) > 0) {
                builder.append(" 3");
            }

            if (!TextUtils.isEmpty(item.userShouHuLevelIMG)) {
                builder.append(" 4");
            }
            if (item.isRoomManage) {//房管
                builder.append(" 5");
            }
        }

        if (!TextUtils.isEmpty(item.cpRank)) {//cp标识
            builder.append(" 6");
        }
        if (item.isOfficialUser==1) {
            builder.append(" 7");
        }
        return builder;
    }


    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

    }

    private StringBuilder generalStringEnd(LivingRoomTextMsg item) {
        StringBuilder builder = new StringBuilder();
        builder.append(" 6");

        return builder;
    }


    public void clearData() {
//        if(mMap!=null){
//            mMap.clear();
//            mMap = null;
//        }
//        if(mAttentionMap!=null){
//            mAttentionMap.clear();
//            mAttentionMap = null;
//        }
        mData.clear();
        notifyDataSetChanged();
    }

    public interface onNameClickListener {
        void onNameClick(int position);

        void onTeaseHimClick(int position);

        void onAnchorAttentionClick(int position);

        void onShareAttentionClick(int position);
    }

    private onNameClickListener onNameClickListener;

    public void setOnNameClickListener(LivingMsgAdapter.onNameClickListener onNameClickListener) {
        this.onNameClickListener = onNameClickListener;
    }

}
