package com.fengwo.module_chat.utils.chat_new;

import android.annotation.SuppressLint;

import com.fengwo.module_chat.base.ChatSocketRequest;
import com.fengwo.module_chat.base.RefreshMessageListEvent;
import com.fengwo.module_chat.base.RefreshUnReadMessageEvent;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_websocket.EventConstant;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 互动通知/最近访客/系统消息
 * //todo 直接将事件id作为发送者id 这个id用于数据库 区分消息事件 目前这么做的有（互动通知/最近消息）
 */
public class NoticeWebSocketDelegate {
    @SuppressLint("CheckResult")
    public static void handleChatMessage(String msg, String belongUserId, ChatGreenDaoHelper daoHelper) {
        ChatSocketRequest request = new Gson().fromJson(msg, ChatSocketRequest.class);
        if (request == null) return;
        int eventId = request.data.eventId;
        switch (eventId) {
            case EventConstant.interact_event:// 互动通知
                receiveNotice(belongUserId, EventConstant.interact_event + "", daoHelper, ChatReceiveMessageUtils.createNoticeEntity(request));
                break;
            case EventConstant.recent_visitor_event:// 最近访客
                receiveNotice(belongUserId, EventConstant.recent_visitor_event + "", daoHelper, ChatReceiveMessageUtils.createNoticeEntity(request));
                break;
            case EventConstant.comment_event://评论封禁
            case EventConstant.system_event:// 系统消息
                if (eventId == EventConstant.comment_event) {
                    request.data.message = "你的评论涉及违规，未评论成功，请查看";
                } else {
                    request.data.message = "你发布动态涉及违规，未发布成功，请查看";
                }
                receiveNotice(belongUserId, EventConstant.system_event + "", daoHelper, ChatReceiveMessageUtils.createNoticeEntity(request));
                break;
//            case EventConstant.comment_event://评论封禁
//                request.data.message = "你的评论涉及违规，未评论成功，请查看";
//                receiveNotice(belongUserId, EventConstant.comment_event + "", daoHelper, ChatReceiveMessageUtils.createNoticeEntity(request));
//                break;
            case EventConstant.greet_event:// 打招呼
                receiveNotice(belongUserId, EventConstant.greet_event + "", daoHelper, ChatReceiveMessageUtils.createNoticeEntity(request));
                break;
            case EventConstant.official_news:
                request.data.message = request.data.title;
                receiveNotice(belongUserId, EventConstant.official_news + "", daoHelper, ChatReceiveMessageUtils.createNoticeEntity(request));
                break;
        }
    }

    @SuppressLint("CheckResult")
    private static void receiveNotice(String belongUserId, String fromId, ChatGreenDaoHelper daoHelper, ChatMsgEntity systemEntity) {
        daoHelper.insertOrReplaceList(belongUserId, fromId,
                null, null,
                null, null, systemEntity, false, false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    //刷新消息列表
                    RxBus.get().post(new RefreshMessageListEvent());
                    //刷新未读消息
                    RxBus.get().post(new RefreshUnReadMessageEvent());
                }, Throwable::printStackTrace);
    }
}
