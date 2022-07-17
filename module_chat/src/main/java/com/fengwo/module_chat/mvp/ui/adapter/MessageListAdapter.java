package com.fengwo.module_chat.mvp.ui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.entity.ChatListItemEntity;
import com.fengwo.module_chat.enums.MessageHeaderEnum;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_websocket.EventConstant;

import java.util.List;

public class MessageListAdapter extends BaseQuickAdapter<ChatListItemEntity, BaseViewHolder> {

    private List<String> groupLists;

    public MessageListAdapter() {
        super(R.layout.chat_item_message);
    }

    public void setNoDistrubData(List<String> groupLists) {
        this.groupLists = groupLists;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChatListItemEntity item) {
        //置顶
        helper.itemView.setBackgroundColor(ContextCompat.getColor(helper.itemView.getContext(),
                item.getTopTime() == 0 ? R.color.white : R.color.grayF8_to_black));

        ImageView ivHeader = helper.getView(R.id.ivHeader);
        TextView tvBadge = helper.getView(R.id.tvBadge); //未读消息数
        helper.setText(R.id.tvDate, TimeUtils.formatDateByMessage(item.getTimestamp() * 1000));
        tvBadge.setText(item.getUnreadCount() > 99 ? "99+" : String.valueOf(item.getUnreadCount()));
        tvBadge.setVisibility(item.getUnreadCount() > 0 ? View.VISIBLE : View.INVISIBLE);
        //判断是否是系统通知（约会助手 / 互动通知 / 最近访客）
//        KLog.v("tag", "" + item.getTalkUserId());
        if (item.getTalkUserId() == EventConstant.recent_visitor_event ||
                item.getTalkUserId() == EventConstant.interact_event ||
                item.getTalkUserId() == EventConstant.appoint_event ||
                item.getTalkUserId() == EventConstant.system_event ||
                item.getTalkUserId() == EventConstant.greet_event ||
                item.getTalkUserId() == EventConstant.comment_event ||
                item.getTalkUserId() == EventConstant.official_news) {
            switch (item.getTalkUserId()) {
                case EventConstant.appoint_event:
                    helper.setText(R.id.tvTitle, MessageHeaderEnum.getName(Constants.NOTICE_TYPE_APPOINT));
                    ImageLoader.loadImg(ivHeader, MessageHeaderEnum.getIcon(Constants.NOTICE_TYPE_APPOINT));
                    break;
                case EventConstant.recent_visitor_event:
                    helper.setText(R.id.tvTitle, MessageHeaderEnum.getName(Constants.NOTICE_TYPE_RECENT_VISITORS));
                    ImageLoader.loadImg(ivHeader, MessageHeaderEnum.getIcon(Constants.NOTICE_TYPE_RECENT_VISITORS));
                    break;
                case EventConstant.interact_event:
                    helper.setText(R.id.tvTitle, MessageHeaderEnum.getName(Constants.NOTICE_TYPE_INTERACT));
                    ImageLoader.loadImg(ivHeader, MessageHeaderEnum.getIcon(Constants.NOTICE_TYPE_INTERACT));
                    break;
                case EventConstant.comment_event:
                case EventConstant.system_event:
                    helper.setText(R.id.tvTitle, MessageHeaderEnum.getName(Constants.NOTICE_TYPE_SYSTEM));
                    ImageLoader.loadImg(ivHeader, MessageHeaderEnum.getIcon(Constants.NOTICE_TYPE_SYSTEM));
                    break;
                case EventConstant.greet_event:
                    helper.setText(R.id.tvTitle, MessageHeaderEnum.getName(Constants.NOTICE_TYPE_GREET));
                    ImageLoader.loadImg(ivHeader, MessageHeaderEnum.getIcon(Constants.NOTICE_TYPE_GREET));
                    break;
                case EventConstant.official_news:
                    helper.setText(R.id.tvTitle, MessageHeaderEnum.getName(Constants.NOTICE_TYPE_OFFICIAL));
                    ImageLoader.loadImg(ivHeader, MessageHeaderEnum.getIcon(Constants.NOTICE_TYPE_OFFICIAL));
                    break;
            }
        } else {
            //判断单聊还是群聊
            if (TextUtils.isEmpty(item.getGroupId())) {
                helper.setText(R.id.tvTitle, item.getUserName());
                ImageLoader.loadImg(ivHeader, item.getUserAvatar());
            } else {
                helper.setText(R.id.tvTitle, item.getGroupName());
                ImageLoader.loadImg(ivHeader, item.getGroupAvatar());
                if (groupLists != null) {
                    //消息免打扰
                    tvBadge.setVisibility(groupLists.contains(item.getGroupId()) || item.getUnreadCount() == 0 ? View.GONE : View.VISIBLE);
                    helper.setVisible(R.id.iv_no_disturb, groupLists.contains(item.getGroupId()));
                } else {
                    helper.setVisible(R.id.iv_no_disturb, false);
                }
            }
        }

        helper.addOnClickListener(R.id.root);
        //显示消息内容
        switch (item.getMsgType()) {
            case ChatMsgEntity.MsgType.comeText:
                helper.setText(R.id.tvSubtitle, item.getMessage());
//                        if (isGroup(item.getGroupId())) {
//                            helper.setText(R.id.tvSubtitle, String.format("%s：%s", item.getUserName(), item.getMessage()));
//                        } else {
//                            helper.setText(R.id.tvSubtitle, item.getMessage());
//                        }
                break;
            case ChatMsgEntity.MsgType.toText:
                helper.setText(R.id.tvSubtitle, item.getMessage());
                break;
            case ChatMsgEntity.MsgType.comeImage:
                helper.setText(R.id.tvSubtitle, "[图片]");
//                        if (isGroup(item.getGroupId())) {
//                            helper.setText(R.id.tvSubtitle, String.format("%s：[图片]", item.getUserName()));
//                        } else {
//                            helper.setText(R.id.tvSubtitle, "[图片]");
//                        }
                break;
            case ChatMsgEntity.MsgType.toImage:
                helper.setText(R.id.tvSubtitle, "[图片]");
                break;
            case ChatMsgEntity.MsgType.comeVoice:
                helper.setText(R.id.tvSubtitle, "[语音]");
//                        if (isGroup(item.getGroupId())) {
//                            helper.setText(R.id.tvSubtitle, String.format("%s：[语音]", item.getUserName()));
//                        } else {
//                            helper.setText(R.id.tvSubtitle, "[语音]");
//                        }
                break;
            case ChatMsgEntity.MsgType.toVoice:
                helper.setText(R.id.tvSubtitle, "[语音]");
                break;
            case ChatMsgEntity.MsgType.revocation:
//                helper.setText(R.id.tvSubtitle, String.format("%s撤回了一条消息", item.getUserName()));
                helper.setText(R.id.tvSubtitle, item.getMessage());
                break;
            case ChatMsgEntity.MsgType.inviteIntoGroup:
                helper.setText(R.id.tvSubtitle, item.getMessage());
                break;
//            case ChatMsgEntity.MsgType.inviteIntoGroup:
//                break;
        }
    }
}
