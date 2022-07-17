package com.fengwo.module_flirt.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_chat.mvp.model.bean.chat_new.VoiceInfoBean;
import com.fengwo.module_chat.mvp.ui.activity.chat_new.BaseChatActivity;
import com.fengwo.module_chat.widgets.chat_new.AudioLayout;
import com.fengwo.module_comment.utils.DataFormatUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.chat.ChatTimeUtils;
import com.fengwo.module_comment.widget.UrlImageSpan;
import com.fengwo.module_comment.widget.emojiTextview.GifTextView;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.UI.activity.BaseChatRoomActivity;

import com.fengwo.module_flirt.dialog.ChatMenuPopwindow;
import com.fengwo.module_flirt.manager.ChatHistroySQLHelper;
import com.fengwo.module_websocket.SendStatus;
import com.fengwo.module_websocket.bean.MsgType;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @Author BLCS
 * @Time 2020/4/6 10:25
 */
public class ChatRoomAdapter extends BaseMultiItemQuickAdapter<SocketRequest<WenboWsChatDataBean>, BaseViewHolder> {

    /*当前角色是否为主播角色*/
    private boolean isAnchor;
    private Handler mHandler;
    private static final int TIME_DIFF = 60 * 1000;
    private TextView tvChatType;

    public ChatRoomAdapter(IStatueListener listener) {//
        super(null);
        this.listener=listener;
        mHandler = new Handler(Looper.getMainLooper());
        addItemType(MsgType.toText, R.layout.adapter_chat_room_right_text);
        addItemType(MsgType.toVoice, R.layout.adapter_chat_room_right_voice);
        addItemType(MsgType.comeVoice, R.layout.adapter_chat_room_left_voice);
        addItemType(MsgType.comeText, R.layout.adapter_chat_room_left_text);
        addItemType(MsgType.systemText, R.layout.adapter_chat_room_stytem_text);
        //  addItemType(MsgType.systemWelcome, R.layout.adapter_chat_room_stytem_welcome);
        addItemType(MsgType.giftMeg, R.layout.adapter_chat_room_stytem_gift);
        addItemType(MsgType.toGameMsg, R.layout.adapter_chat_room_right_game);
        addItemType(MsgType.fromGameMsg, R.layout.adapter_chat_room_left_game);
        addItemType(MsgType.splashMeg, R.layout.adapter_chat_room_splash);
        addItemType(MsgType.comeRevocation, R.layout.adapter_chat_room_stytem_text);
        addItemType(MsgType.toRevocation, R.layout.adapter_chat_room_stytem_text);
        addItemType(MsgType.toGiftMsg, R.layout.adapter_chat_room_left_gift);
        addItemType(MsgType.fromGiftMsg, R.layout.adapter_chat_room_right_gift);
    }

    public void setAnchor(boolean anchor) {
        isAnchor = anchor;
    }



    @Override
    protected void convert(@NonNull BaseViewHolder mHolder, SocketRequest<WenboWsChatDataBean> item) {
        //  处理时间显示
        if (item.getItemType() != MsgType.systemText && item.getItemType() != MsgType.splashMeg && item.getItemType() != MsgType.giftMeg) {//系统通知无需显示时间
            TextView tvTime = mHolder.getView(R.id.tv_time);
            if (tvTime != null) {
                if (ifNeedTime(item, mHolder.getLayoutPosition())) {
                    tvTime.setVisibility(View.VISIBLE);
                    tvTime.setText(ChatTimeUtils.getChatTime(Long.parseLong(item.timestamp)));
                } else {
                    try {
                        tvTime.setVisibility(View.GONE);
                    } catch (NullPointerException e) {

                    }
                }
            }
        }

//        if (null != mHolder.getView(R.id.tv_content_right) && isAnchor) {
//            GifTextView tvContent = mHolder.getView(R.id.tv_content_right);
//            tvContent.setMaxWidth(R.dimen.dp_184);
//        }
//        if (null != mHolder.getView(R.id.tv_content_left) && isAnchor) {
//            GifTextView tvContent = mHolder.getView(R.id.tv_content_left);
//            tvContent.setMaxWidth(R.dimen.dp_184);
//        }

        //  显示文本
        switch (item.getItemType()) {

            case MsgType.toText://发送文本消息:
                CircleImageView ivAvatar = mHolder.getView(R.id.civ_header_right);
                ImageView ivLoading = mHolder.getView(R.id.iv_item_loading_right);
                GifTextView tvContent = mHolder.getView(R.id.tv_content_right);
                //主播用橙色，用户用蓝色
                tvContent.setBackgroundResource(isAnchor ? R.drawable.chat_left_text_bg : R.drawable.chat_rights_text_bg);
                ImageLoader.loadCircleWithBorder(ivAvatar, item.data.getFromUser().getHeadImg(), 2, 2);
//                ImageLoader.loadImg(ivAvatar, item.data.getFromUser().getHeadImg());
              String msg =  item.data.getContent().getValue().replace("<br/>","\n");
                tvContent.setSpanText(mHandler, msg, true);
                showMsgStatus(ivLoading, item.sendStatus,item);
                break;
            case MsgType.comeText://收到文本消息:
                CircleImageView ivAvatarLeft = mHolder.getView(R.id.civ_header_left);
                ImageView ivLoadingLeft = mHolder.getView(R.id.iv_item_loading_left);
                ivLoadingLeft.setVisibility(View.GONE);
                //主播用橙色，用户用蓝色
                GifTextView tvContentLeft = mHolder.getView(R.id.tv_content_left);
                tvContentLeft.setBackgroundResource(isAnchor ? R.drawable.chat_rights_text_bg : R.drawable.chat_left_text_bg);
//                ImageLoader.loadImg(ivAvatarLeft, item.data.getFromUser().getHeadImg());
                ImageLoader.loadCircleWithBorder(ivAvatarLeft, item.data.getFromUser().getHeadImg(), 2, 2);
                String msgs =  item.data.getContent().getValue().replace("<br/>","\n");
                tvContentLeft.setSpanText(mHandler, msgs, true);
              //  showMsgStatus(ivLoadingLeft, item.sendStatus,item);
                tvChatType = mHolder.getView(R.id.tv_chat_type);
                if (!TextUtils.isEmpty(item.data.getGears())&&!TextUtils.isEmpty(item.data.getIsGears())&&item.data.getIsGears().equals("1")) {
                    tvChatType.setVisibility(View.VISIBLE);
                    switch (item.data.getGears()) {
                        case "0":
                            tvChatType.setBackgroundResource(R.drawable.shape_chat_gears0);
                            tvChatType.setText("开启缘分");
                            break;
                        case "1":
                            tvChatType.setBackgroundResource(R.drawable.shape_chat_gears1);
                            tvChatType.setText("再续前缘");
                            break;
                        case "2":
                            tvChatType.setBackgroundResource(R.drawable.shape_chat_gears2);
                            tvChatType.setText("缘定三生");
                            break;
                        default:
                            tvChatType.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    tvChatType.setVisibility(View.GONE);
                }
                mHolder.setText(R.id.tv_name, item.data.getFromUser().getNickname());
                break;
            case MsgType.toVoice://发送语音
                CircleImageView ivAvatarVoiceRight = mHolder.getView(R.id.civ_header_right);
                ImageView ivLoadingVoiceRight = mHolder.getView(R.id.iv_item_loading_right);
                AudioLayout voiceMe = mHolder.getView(R.id.voice_me);
             voiceMe.setBackgroundResource(isAnchor ? R.drawable.chat_left_text_bg : R.drawable.chat_rights_text_bg);
                VoiceInfoBean infoBean = new VoiceInfoBean(new File(item.data.getContent().getValue()).getName(), item.data.getContent().getDuration()).setFileUrl(item.data.getContent().getValue());
                voiceMe.initByChatMsgEntity(false, new Gson().toJson(infoBean), ((BaseChatRoomActivity) mContext).getVoicePlayerWrapper());
//                ImageLoader.loadImg(ivAvatarVoiceRight, item.data.getFromUser().getHeadImg());
                ImageLoader.loadCircleWithBorder(ivAvatarVoiceRight, item.data.getFromUser().getHeadImg(), 2, 2);
                showMsgStatus(ivLoadingVoiceRight, item.sendStatus,item);
                break;
            case MsgType.comeVoice://收到语音
                CircleImageView ivAvatarVoiceLeft = mHolder.getView(R.id.civ_header_left);
                ImageView ivLoadingVoiceLeft = mHolder.getView(R.id.iv_item_loading_left);
                ivLoadingVoiceLeft.setVisibility(View.GONE);
                AudioLayout voiceOther = mHolder.getView(R.id.voice_other);

                voiceOther.setBackgroundResource(isAnchor ? R.drawable.chat_rights_text_bg : R.drawable.chat_left_text_bg);

                VoiceInfoBean infoBean1 = new VoiceInfoBean(new File(item.data.getContent().getValue()).getName(), item.data.getContent().getDuration()).setFileUrl(item.data.getContent().getValue());
                voiceOther.initByChatMsgEntity(false, new Gson().toJson(infoBean1), ((BaseChatRoomActivity) mContext).getVoicePlayerWrapper());
//                ImageLoader.loadImg(ivAvatarVoiceLeft, item.data.getFromUser().getHeadImg());
                ImageLoader.loadCircleWithBorder(ivAvatarVoiceLeft, item.data.getFromUser().getHeadImg(), 2, 2);
               // showMsgStatus(ivLoadingVoiceLeft, item.sendStatus,item);
                tvChatType = mHolder.getView(R.id.tv_chat_type);
                if (!TextUtils.isEmpty(item.data.getGears())&&!TextUtils.isEmpty(item.data.getIsGears())&&item.data.getIsGears().equals("1")) {
                    tvChatType.setVisibility(View.VISIBLE);
                    switch (item.data.getGears()) {
                        case "0":
                            tvChatType.setBackgroundResource(R.drawable.shape_chat_gears0);
                            tvChatType.setText("开启缘分");
                            break;
                        case "1":
                            tvChatType.setBackgroundResource(R.drawable.shape_chat_gears1);
                            tvChatType.setText("再续前缘");
                            break;
                        case "2":
                            tvChatType.setBackgroundResource(R.drawable.shape_chat_gears2);
                            tvChatType.setText("缘定三生");
                            break;
                        default:
                            tvChatType.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    tvChatType.setVisibility(View.GONE);
                }
                mHolder.setText(R.id.tv_name, item.data.getFromUser().getNickname());
                break;
            case MsgType.systemText:
                String content = item.data.getContent().getValue();
                if (!TextUtils.isEmpty(content)) {
                    mHolder.setText(R.id.tv_content_tip, content);
                }
                break;
            case MsgType.splashMeg:
                String splashContent = item.data.getRoom().getRoomTitle();
                if (!TextUtils.isEmpty(splashContent)) {
                    mHolder.setText(R.id.tv_splash, splashContent);
                }
                break;
            case MsgType.fromGiftMsg:

                CircleImageView ivGiftAvatar = mHolder.getView(R.id.civ_header_right);
                GifTextView tvGiftContent = mHolder.getView(R.id.tv_content_right);

                tvGiftContent.setBackgroundResource(isAnchor ? R.drawable.chat_left_text_bg : R.drawable.chat_rights_text_bg);
                // tvGiftContent.setText(item.data.getContent().getValue());
                ImageLoader.loadCircleWithBorder(ivGiftAvatar, item.data.getFromUser().getHeadImg(), 2, 2);
                setGiftMsgText(tvGiftContent, item);


                //   tv.setText(spanString);
//                    tvChatType = mHolder.getView(R.id.tv_chat_type);
//                    if (!TextUtils.isEmpty(item.data.getIsGears())) {
//                        tvChatType.setVisibility(View.VISIBLE);
//                        switch (item.data.getIsGears()) {
//                            case "0":
//                                tvChatType.setBackgroundResource(R.drawable.shape_chat_gears0);
//                                tvChatType.setText("开启缘分");
//                                break;
//                            case "1":
//                                tvChatType.setBackgroundResource(R.drawable.shape_chat_gears1);
//                                tvChatType.setText("再续前缘");
//                                break;
//                            case "2":
//                                tvChatType.setBackgroundResource(R.drawable.shape_chat_gears2);
//                                tvChatType.setText("缘定三生");
//                                break;
//                        }
//                    } else {
//                        tvChatType.setVisibility(View.GONE);
//                    }

                break;
            case MsgType.toGiftMsg:
                //    mHolder.setText(R.id.tv_content_left, item.data.getContent().getValue());
                GifTextView tvContentLeftgift = mHolder.getView(R.id.tv_content_left);
                tvContentLeftgift.setBackgroundResource(isAnchor ? R.drawable.chat_rights_text_bg : R.drawable.chat_left_text_bg);
                setGiftMsgText(tvContentLeftgift, item);
                mHolder.setText(R.id.tv_name, item.data.getFromUser().getNickname() + "");
                tvChatType = mHolder.getView(R.id.tv_chat_type);
                if (!TextUtils.isEmpty(item.data.getGears())&&!TextUtils.isEmpty(item.data.getIsGears())&&item.data.getIsGears().equals("1")) {
                    tvChatType.setVisibility(View.VISIBLE);
                    switch (item.data.getGears()) {
                        case "0":
                            tvChatType.setBackgroundResource(R.drawable.shape_chat_gears0);
                            tvChatType.setText("开启缘分");
                            break;
                        case "1":
                            tvChatType.setBackgroundResource(R.drawable.shape_chat_gears1);
                            tvChatType.setText("再续前缘");
                            break;
                        case "2":
                            tvChatType.setBackgroundResource(R.drawable.shape_chat_gears2);
                            tvChatType.setText("缘定三生");
                            break;
                        default:
                            tvChatType.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    tvChatType.setVisibility(View.GONE);
                }
                CircleImageView ivAvatarGiftRight = mHolder.getView(R.id.civ_header_left);
                ImageLoader.loadCircleWithBorder(ivAvatarGiftRight, item.data.getFromUser().getHeadImg(), 2, 2);
                break;
            case MsgType.giftMeg:
                TextView tvMsg = mHolder.getView(R.id.tv_msg);
                setGiftMsgText(tvMsg, item);
//                if(item.data.getContent().isOrdinaryGift()==1){//是否是普通礼物
//                    mHolder.setGone(R.id.rl_hy,false);
//                    mHolder.setGone(R.id.ll_view,true);
//                    mHolder.setText(R.id.tv_content_left, item.data.getContent().getValue());
//                    GifTextView tvContentLeftgift = mHolder.getView(R.id.tv_content_left);
//                    tvContentLeftgift.setBackgroundResource(isAnchor ? R.drawable.chat_rights_text_bg : R.drawable.chat_left_text_bg);
//                    tvChatType = mHolder.getView(R.id.tv_chat_type);
//                    if (!TextUtils.isEmpty(item.data.getGears())) {
//                        tvChatType.setVisibility(View.VISIBLE);
//                        switch (item.data.getGears()) {
//                            case "0":
//                                tvChatType.setBackgroundResource(R.drawable.shape_chat_gears0);
//                                tvChatType.setText("开启缘分");
//                                break;
//                            case "1":
//                                tvChatType.setBackgroundResource(R.drawable.shape_chat_gears1);
//                                tvChatType.setText("再续前缘");
//                                break;
//                            case "2":
//                                tvChatType.setBackgroundResource(R.drawable.shape_chat_gears2);
//                                tvChatType.setText("缘定三生");
//                                break;
//                        }
//                    } else {
//                        tvChatType.setVisibility(View.GONE);
//                    }
//                    CircleImageView ivAvatarGameright = mHolder.getView(R.id.civ_header_left);
//                    ImageLoader.loadCircleWithBorder(ivAvatarGameright, item.data.getFromUser().getHeadImg(), 2, 2);
//                }else


                break;
//            case MsgType.ordinaryMeg:
//                if (null != item.data && null != item.data.getContent() && !TextUtils.isEmpty(item.data.getContent().getValue())) {
//                    mHolder.setText(R.id.tv_content_left, item.data.getContent().getValue());
//                    tvChatType = mHolder.getView(R.id.tv_chat_type);
//                    if (!TextUtils.isEmpty(item.data.getGears())) {
//                        tvChatType.setVisibility(View.VISIBLE);
//                        switch (item.data.getGears()) {
//                            case "0":
//                                tvChatType.setBackgroundResource(R.drawable.shape_chat_gears0);
//                                tvChatType.setText("开启缘分");
//                                break;
//                            case "1":
//                                tvChatType.setBackgroundResource(R.drawable.shape_chat_gears1);
//                                tvChatType.setText("再续前缘");
//                                break;
//                            case "2":
//                                tvChatType.setBackgroundResource(R.drawable.shape_chat_gears2);
//                                tvChatType.setText("缘定三生");
//                                break;
//                        }
//                    } else {
//                        tvChatType.setVisibility(View.GONE);
//                    }
//                    CircleImageView ivAvatarGameright = mHolder.getView(R.id.civ_header_left);
//                    ImageLoader.loadCircleWithBorder(ivAvatarGameright, item.data.getFromUser().getHeadImg(), 2, 2);
//                }
//                break;
            case MsgType.toGameMsg:
                CircleImageView ivAvatarGameLeft = mHolder.getView(R.id.civ_header_right);
//                ImageLoader.loadImg(ivAvatarGameLeft, item.data.getFromUser().getHeadImg());
                ImageLoader.loadCircleWithBorder(ivAvatarGameLeft, item.data.getFromUser().getHeadImg(), 2, 2);
                if (item.data.getContent().getType().equals("finger-guessing")) {
                    if (item.data.getContent().getTime() >= System.currentTimeMillis()) {
                        ImageLoader.loadGif(mHolder.getView(R.id.iv_game_right), R.drawable.caiquan);
                    } else {
                        String value = item.data.getContent().getValue();
                        KLog.e("tag", value + "");
                        if (!TextUtils.isEmpty(value)) {
                            mHolder.setImageResource(R.id.iv_game_right, ImageLoader.getResId("ic_" + value, R.drawable.class));
                        } else {
                            mHolder.setImageResource(R.id.iv_game_right, ImageLoader.getResId("ic_scissors", R.drawable.class));
                        }

                    }
                } else {
                    if (item.data.getContent().getTime() >= System.currentTimeMillis()) {
                        ImageLoader.loadGif(mHolder.getView(R.id.iv_game_right), R.drawable.touzi);
                    } else {
                        int value = 0;
                        //判断是否是数字
                        if (DataFormatUtils.isInteger(item.data.getContent().getValue())) {
                            value = Integer.parseInt(item.data.getContent().getValue()) - 1;
                        }
                        mHolder.setImageResource(R.id.iv_game_right, ImageLoader.getResId("ic_touzi" + value, R.drawable.class));
//                        String value = item.data.getContent().getValue();
//                        value = Integer.parseInt(value) - 1 + "";
//                        if (!TextUtils.isEmpty(value)) {
//                            mHolder.setImageResource(R.id.iv_game_right, ImageLoader.getResId("ic_touzi" + value, R.drawable.class));
//                        } else {
//
//                        }
                    }
                }
                showMsgStatus(mHolder.getView(R.id.iv_item_loading_right), item.sendStatus,item);
                break;
            case MsgType.fromGameMsg://收到游戏
                CircleImageView ivAvatarGameright = mHolder.getView(R.id.civ_header_left);
//                ImageLoader.loadImg(ivAvatarGameright, item.data.getFromUser().getHeadImg());
                ImageLoader.loadCircleWithBorder(ivAvatarGameright, item.data.getFromUser().getHeadImg(), 2, 2);

                if (item.data.getContent().getType().equals("finger-guessing")) {
                    if (item.data.getContent().getTime() >= System.currentTimeMillis()) {
                        ImageLoader.loadGif(mHolder.getView(R.id.iv_game_left), R.drawable.caiquan);
                    } else {
                        String value = item.data.getContent().getValue();
                        mHolder.setImageResource(R.id.iv_game_left, ImageLoader.getResId("ic_" + value, R.drawable.class));
                    }
                } else {
                    if (item.data.getContent().getTime() >= System.currentTimeMillis()) {
                        ImageLoader.loadGif(mHolder.getView(R.id.iv_game_left), R.drawable.touzi);
                    } else {
                        int value = 0;
                        //判断是否是数字
                        if (DataFormatUtils.isInteger(item.data.getContent().getValue())) {
                            value = Integer.parseInt(item.data.getContent().getValue()) - 1;
                        }
                        mHolder.setImageResource(R.id.iv_game_left, ImageLoader.getResId(("ic_touzi" + value), R.drawable.class));
                    }
                }
            //    showMsgStatus(mHolder.getView(R.id.iv_item_loading_left), item.sendStatus,item);
                tvChatType = mHolder.getView(R.id.tv_chat_type);
                if (!TextUtils.isEmpty(item.data.getGears())&&!TextUtils.isEmpty(item.data.getIsGears())&&item.data.getIsGears().equals("1")) {
                    tvChatType.setVisibility(View.VISIBLE);
                    switch (item.data.getGears()) {
                        case "0":
                            tvChatType.setBackgroundResource(R.drawable.shape_chat_gears0);
                            tvChatType.setText("开启缘分");
                            break;
                        case "1":
                            tvChatType.setBackgroundResource(R.drawable.shape_chat_gears1);
                            tvChatType.setText("再续前缘");
                            break;
                        case "2":
                            tvChatType.setBackgroundResource(R.drawable.shape_chat_gears2);
                            tvChatType.setText("缘定三生");
                            break;
                        default:
                            tvChatType.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    tvChatType.setVisibility(View.GONE);
                }

                mHolder.setText(R.id.tv_name, item.data.getFromUser().getNickname());
                break;

            case MsgType.comeRevocation://消息撤回
                mHolder.setText(R.id.tv_content_tip, item.data.getFromUser().getNickname() + "撤回了一条消息");
                break;
            case MsgType.toRevocation://消息撤回
                mHolder.setText(R.id.tv_content_tip, "你撤回了一条消息");
                break;
        }
    }

    /**
     * 文字加礼物图片
     *
     * @param tv
     * @param item
     */
    private void setGiftMsgText(TextView tv, SocketRequest<WenboWsChatDataBean> item) {
        SpannableStringBuilder spanString = null;
        if (null != item.data && null != item.data.getContent() && !TextUtils.isEmpty(item.data.getContent().getValue())) {//加时礼物

            if (TextUtils.isEmpty(item.data.getContent().getType()) || item.data.getContent().getType().equals("text")) {
                if (item.data.getContent().getValue().indexOf("|") > 0) {
                    int iconStart = item.data.getContent().getValue().lastIndexOf("|");
                    spanString = new SpannableStringBuilder(item.data.getContent().getValue());
                    spanString.setSpan("", iconStart, iconStart + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(spanString);
                } else {
                    tv.setText(item.data.getContent().getValue());
                }

            } else {
                try {
                    // UrlImageSpan span = new UrlImageSpan(mContext, item.data.getContent().getType(), tv);
                    UrlImageSpan span = new UrlImageSpan(mContext, item.data.getContent().getType(), tv, DensityUtils.dp2px(mContext, 14), DensityUtils.dp2px(mContext, 14));
                    //     UrlImageSpan span = new UrlImageSpan(mContext, "https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/gift/1589870813000*%E5%A4%9A%E5%B7%B4%E8%83%BA%E7%82%B8%E5%BC%B9.png", mHolder.getView(R.id.tv_content_right));
                    int iconStart = item.data.getContent().getValue().lastIndexOf("|");
                    spanString = new SpannableStringBuilder(item.data.getContent().getValue());
                    spanString.setSpan(span, iconStart, iconStart + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    int toutiaoStart = item.data.getContent().getValue().indexOf("|");
//                    spanString.setSpan( toutiaoStart, toutiaoStart + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(spanString);
                } catch (Exception e) {

                }
            }
        }
    }

    /**
     * 显示消息状态
     *
     * @param status 消息状态
     */
    private void showMsgStatus(ImageView ivItemFail, int status,SocketRequest<WenboWsChatDataBean> data) {
        if (status == SendStatus.sendFaild) {
            ivItemFail.setImageResource(com.fengwo.module_chat.R.drawable.msg_state_fail_resend);
            ivItemFail.setVisibility(View.VISIBLE);

            ivItemFail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null!=listener)listener.onSendFaild();
                }
            });
        } else if (status == SendStatus.sending) {//传输中
            ivItemFail.setVisibility(View.VISIBLE);
            ImageLoader.loadGif(ivItemFail, com.fengwo.module_chat.R.drawable.common_loading_small1_0);
        } else if(status == SendStatus.comeBack){
            ivItemFail.setVisibility(View.VISIBLE);
            ivItemFail.setImageResource(com.fengwo.module_chat.R.drawable.pic_cf);
            ivItemFail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null!=listener)listener.onComeBack(data.msgId,data);
                }
            });
        }else  {
            ivItemFail.setImageDrawable(null);
            ivItemFail.setVisibility(View.GONE);
        }

        // 启动传输状态下的滚动条显示
        if (ivItemFail.getDrawable() != null
                && ivItemFail.getDrawable() instanceof AnimationDrawable) {
            AnimationDrawable ad = (AnimationDrawable) (ivItemFail.getDrawable());
            ad.start();
        }
    }
    private IStatueListener listener;

    public interface IStatueListener {
        void onSendFaild();
        void onComeBack(String msgid,SocketRequest<WenboWsChatDataBean> data);
    }
    /**
     * 判断是否需要显示时间
     */
    private boolean ifNeedTime(SocketRequest<WenboWsChatDataBean> item, int curPosition) {
        SocketRequest<WenboWsChatDataBean> preItem = null;
        if (curPosition > 0) {
            preItem = getItem(curPosition - 1);
        }
        if (preItem == null || item.getTimestamp() - preItem.getTimestamp() >= TIME_DIFF) {
            return true;
        } else {
            return false;
        }
    }

    public void updateMsg(String msgId) {
        for (int i = getItemCount() - 1; i >= 0; i--) {
            if (msgId.equals(getItem(i).msgId)) {
                getItem(i).msgSuccess();
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void updateMsgFaild(String msgId) {
        for (int i = getItemCount() - 1; i >= 0; i--) {
            if (msgId.equals(getItem(i).msgId)) {
                getItem(i).msgComeBack();
                notifyItemChanged(i);
                return;
            }
        }
    }
    public void updateMsgDing(String msgId) {
        for (int i = getItemCount() - 1; i >= 0; i--) {
            if (msgId.equals(getItem(i).msgId)) {
                getItem(i).msgDing();
                notifyItemChanged(i);
                return;
            }
        }
    }
    public void clearData() {
        getData().clear();
        notifyDataSetChanged();
    }
}
