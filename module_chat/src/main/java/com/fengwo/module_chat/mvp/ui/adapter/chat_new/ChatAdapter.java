package com.fengwo.module_chat.mvp.ui.adapter.chat_new;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.faceunity.FURenderer;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_chat.mvp.ui.activity.chat_new.BaseChatActivity;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.chat.ChatTimeUtils;
import com.fengwo.module_chat.widgets.chat_new.AudioLayout;
import com.fengwo.module_comment.widget.emojiTextview.GifTextView;
import com.fengwo.module_comment.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends BaseMultiItemQuickAdapter<ChatMsgEntity, BaseViewHolder> {

    private static final int TIME_DIFF = 60;
    public Handler mHandler;
    @Autowired
    UserProviderService userProviderService;

    private RecyclerView mRecyclerView;
    private boolean moreType = false;
    private List<ChatMsgEntity> selectedEntities = new ArrayList<>();

    public ChatAdapter(RecyclerView recyclerView) {
        super(null);
        ARouter.getInstance().inject(this);
        mHandler = new Handler(Looper.getMainLooper());
        this.mRecyclerView = recyclerView;
        addItemType(ChatMsgEntity.MsgType.toText, R.layout.chat_item_me_text);
        addItemType(ChatMsgEntity.MsgType.toVoice, R.layout.chat_item_me_voice);
        addItemType(ChatMsgEntity.MsgType.toImage, R.layout.chat_item_me_image);

        addItemType(ChatMsgEntity.MsgType.comeText, R.layout.chat_item_other_text);
        addItemType(ChatMsgEntity.MsgType.comeVoice, R.layout.chat_item_other_voice);
        addItemType(ChatMsgEntity.MsgType.comeImage, R.layout.chat_item_other_image);
        addItemType(ChatMsgEntity.MsgType.revocation, R.layout.chat_item_revocation);
        addItemType(ChatMsgEntity.MsgType.inviteIntoGroup, R.layout.chat_item_revocation);
        addItemType(ChatMsgEntity.MsgType.systemTxtAttention, R.layout.chat_item_system_attention);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChatMsgEntity item) {
        if (helper.getItemViewType() != ChatMsgEntity.MsgType.systemText) {//系统通知无需显示时间
            TextView tvTime = helper.getView(R.id.messageTime);
            if (ifNeedTime(item, helper.getLayoutPosition())) {
                tvTime.setVisibility(View.VISIBLE);
                tvTime.setText(ChatTimeUtils.getChatTime(item.getDate() * 1000));
            } else tvTime.setVisibility(View.GONE);
        }
        ImageView ivAvatar;
        LinearLayout layBubble;
        AudioLayout layVoice;
        GifTextView tvText;
        ImageView ivImage;
        ImageView ivItemFail;
        LinearLayout laySel;
        ImageView ivSel;
        TextView tvTips;
        ImageView ivType;
        ImageView ivPlay;
        TextView tvFileName;
        TextView tvFileSize;
        TextView tvTalkName;
        View selectView;
        switch (helper.getItemViewType()) {
            case ChatMsgEntity.MsgType.toText://发出文本消息
                ivAvatar = helper.getView(R.id.messageUserAvatar);
                tvText = helper.getView(R.id.messageText);
                ivItemFail = helper.getView(R.id.iv_item_fail);
                selectView = helper.getView(R.id.iv_chat_item_select);
                ImageLoader.loadImg(ivAvatar, item.getHeaderImg());
                tvText.setSpanText(mHandler, item.getText(), true);
                showMsgStatus(ivItemFail, item.getSendStatus());
                if (moreType) {
                    helper.addOnClickListener(R.id.view_select);
                } else {
                    helper.addOnLongClickListener(R.id.bubble_me);
                    helper.addOnClickListener(R.id.messageUserAvatar);
                    helper.addOnClickListener(R.id.iv_item_fail);
                }
                selectView.setVisibility(moreType ? View.VISIBLE : View.GONE);
                selectView.setSelected(selectedEntities.contains(item));
                break;
            case ChatMsgEntity.MsgType.toVoice://发出语音消息
                selectView = helper.getView(R.id.iv_chat_item_select);
                ivAvatar = helper.getView(R.id.messageUserAvatar);
                layVoice = helper.getView(R.id.voice_me);
                ivItemFail = helper.getView(R.id.iv_item_fail);
                ImageLoader.loadImg(ivAvatar, item.getHeaderImg());
                layVoice.initByChatMsgEntity(true, item.getText(), ((BaseChatActivity) mContext).getVoicePlayerWrapper());
                showMsgStatus(ivItemFail, item.getSendStatus());
                if (moreType) {
                    helper.addOnClickListener(R.id.view_select);
                } else {
                    helper.addOnClickListener(R.id.iv_item_fail);
                    helper.addOnClickListener(R.id.layout_text_me);
                    helper.addOnLongClickListener(R.id.voice_me);
                    helper.addOnClickListener(R.id.messageUserAvatar);
                }
                selectView.setVisibility(moreType ? View.VISIBLE : View.GONE);
                selectView.setSelected(selectedEntities.contains(item));
                break;
            case ChatMsgEntity.MsgType.toImage://发出图片
                ivAvatar = helper.getView(R.id.messageUserAvatar);
                ivImage = helper.getView(R.id.image);
                ivItemFail = helper.getView(R.id.iv_item_fail);
                selectView = helper.getView(R.id.iv_chat_item_select);
                ImageLoader.loadImg(ivAvatar, item.getHeaderImg());
                ImageLoader.loadImg(ivImage, item.getText());
                showMsgStatus(ivItemFail, item.getSendStatus());
                if (moreType) {
                    helper.addOnClickListener(R.id.view_select);
                } else {
                    helper.addOnClickListener(R.id.layout_image_me);
                    helper.addOnClickListener(R.id.image);
                    helper.addOnLongClickListener(R.id.image);
                    helper.addOnClickListener(R.id.messageUserAvatar);
                    helper.addOnClickListener(R.id.iv_item_fail);
                }
                selectView.setVisibility(moreType ? View.VISIBLE : View.GONE);
                selectView.setSelected(selectedEntities.contains(item));
                break;
            case ChatMsgEntity.MsgType.comeText://收到文本消息
                ivAvatar = helper.getView(R.id.messageUserAvatar);
                layBubble = helper.getView(R.id.bubble_other);
                tvText = helper.getView(R.id.messageText);
                ivItemFail = helper.getView(R.id.iv_item_fail);
                tvTalkName = helper.getView(R.id.tv_group_friend);
                selectView = helper.getView(R.id.iv_chat_item_select);
                tvTalkName.setVisibility(item.isGroup() ? View.VISIBLE : View.GONE);
                tvTalkName.setText(item.getName());
                ImageLoader.loadImg(ivAvatar, item.getHeaderImg());
                tvText.setSpanText(mHandler, item.getText(), true);
                if (moreType) {
                    helper.addOnClickListener(R.id.view_select);
                } else {
                    helper.addOnClickListener(R.id.layout_text_other);
                    helper.addOnLongClickListener(R.id.bubble_other);
                    helper.addOnClickListener(R.id.messageUserAvatar);
                }
                selectView.setVisibility(moreType ? View.VISIBLE : View.GONE);
                selectView.setSelected(selectedEntities.contains(item));
                break;
            case ChatMsgEntity.MsgType.comeVoice://收到语音消息
                ivAvatar = helper.getView(R.id.messageUserAvatar);
                layVoice = helper.getView(R.id.voice_other);
                tvTalkName = helper.getView(R.id.tv_group_friend);
                selectView = helper.getView(R.id.iv_chat_item_select);
                tvTalkName.setVisibility(item.isGroup() ? View.VISIBLE : View.GONE);
                tvTalkName.setText(item.getName());
                ImageLoader.loadImg(ivAvatar, item.getHeaderImg());
                layVoice.initByChatMsgEntity(false, item.getText(), ((BaseChatActivity) mContext).getVoicePlayerWrapper());
                if (moreType) {
                    helper.addOnClickListener(R.id.view_select);
                } else {
                    helper.addOnClickListener(R.id.layout_text_other);
                    helper.addOnLongClickListener(R.id.voice_other);
                    helper.addOnClickListener(R.id.messageUserAvatar);
                }
                selectView.setVisibility(moreType ? View.VISIBLE : View.GONE);
                selectView.setSelected(selectedEntities.contains(item));
                break;
            case ChatMsgEntity.MsgType.comeImage://收到图片消息
                ivAvatar = helper.getView(R.id.messageUserAvatar);
                ivImage = helper.getView(R.id.image_other);
                tvTalkName = helper.getView(R.id.tv_group_friend);
                selectView = helper.getView(R.id.iv_chat_item_select);
                tvTalkName.setVisibility(item.isGroup() ? View.VISIBLE : View.GONE);
                tvTalkName.setText(item.getName());
                ImageLoader.loadImg(ivAvatar, item.getHeaderImg());
                ImageLoader.loadImg(ivImage, item.getText());
                if (moreType) {
                    helper.addOnClickListener(R.id.view_select);
                } else {
                    helper.addOnClickListener(R.id.layout_image_other);
                    helper.addOnClickListener(R.id.image_other);
                    helper.addOnLongClickListener(R.id.image_other);
                    helper.addOnClickListener(R.id.messageUserAvatar);
                }
                selectView.setVisibility(moreType ? View.VISIBLE : View.GONE);
                selectView.setSelected(selectedEntities.contains(item));
                break;
            case ChatMsgEntity.MsgType.revocation:
                helper.setText(R.id.revocationText, String.format("\"%s\"撤回了一条消息",item.getName()));
                break;
            case ChatMsgEntity.MsgType.inviteIntoGroup://系统消息
                tvTips = helper.getView(R.id.revocationText);
                tvTips.setText(item.getText());
                break;
            case ChatMsgEntity.MsgType.systemTxtAttention:
                tvTips = helper.getView(R.id.systemTextAttention);
                final SpannableStringBuilder style = new SpannableStringBuilder();
                //设置文字
                style.append(tvTips.getText().toString());
                //设置部分文字点击事件
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        if (mAttentionClickListener != null)
                            mAttentionClickListener.onAttention();
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setUnderlineText(false);
                    }
                };
                style.setSpan(clickableSpan, 18, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                //设置部分文字颜色
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#A04BFE"));
                style.setSpan(foregroundColorSpan, 18, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                //配置给TextView
                tvTips.setMovementMethod(LinkMovementMethod.getInstance());
                tvTips.setText(style);
                break;
            default:
                //这里可能因为类型未处理而异常退出
                L.e(TAG, helper.getItemViewType() + "");
                break;
        }
    }

    /**
     * 判断是否需要显示时间
     *
     * @param item        当前消息信息
     * @param curPosition 当前消息序号
     * @return 是否需要显示时间
     */
    private boolean ifNeedTime(ChatMsgEntity item, int curPosition) {
        ChatMsgEntity preItem = null;
        if (curPosition > 0) {
            preItem = getItem(curPosition - 1);
        }
        if (preItem == null || item.getDate() - preItem.getDate() >= TIME_DIFF) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 显示消息状态
     *
     * @param ivItemFail 视觉反馈控件
     * @param status     消息状态
     */
    private void showMsgStatus(ImageView ivItemFail, int status) {
        if (status == ChatMsgEntity.SendStatus.sendFaild) {
            ivItemFail.setImageResource(R.drawable.msg_state_fail_resend);
            ivItemFail.setVisibility(View.VISIBLE);
        } else if (status == ChatMsgEntity.SendStatus.sending) {//传输中
            ivItemFail.setVisibility(View.VISIBLE);
            ImageLoader.loadGif(ivItemFail, R.drawable.common_loading_small1_0);
        } else {
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

    /**
     * 滚动到最新一行
     */
    public void showLastItem(boolean smooth) {
        if (mRecyclerView != null) {
            //平滑滚动
            try {
                if(getItemCount()!=0){
                    if (smooth) {
                        mRecyclerView.smoothScrollToPosition(getItemCount() - 1);
                    } else {
                        mRecyclerView.smoothScrollToPosition(getItemCount() - 1);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isItemSelected(ChatMsgEntity entity) {
        return selectedEntities.contains(entity);
    }

    public void addSelection(ChatMsgEntity entity) {
        if (!isItemSelected(entity)) {
            selectedEntities.add(entity);
            notifyDataSetChanged();
        }
    }

    public void removeSelection(ChatMsgEntity entity) {
        if (isItemSelected(entity)) {
            selectedEntities.remove(entity);
            notifyDataSetChanged();
        }
    }

    public void setMoreType(boolean moreType) {
        this.moreType = moreType;
        notifyDataSetChanged();
    }

    public List<ChatMsgEntity> getSelection() {
        return selectedEntities;
    }

    public interface OnItemAttentionClickListener {
        void onAttention();
    }

    private OnItemAttentionClickListener mAttentionClickListener;

    public void setOnItemAttentionClickListener(OnItemAttentionClickListener onItemAttentionClickListener) {
        this.mAttentionClickListener = onItemAttentionClickListener;
    }
}