package com.fengwo.module_chat.mvp.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import com.fengwo.module_chat.entity.ChatListItemEntity;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_chat.mvp.ui.contract.IMessageListView;
import com.fengwo.module_chat.utils.chat_new.ChatGreenDaoHelper;
import com.fengwo.module_chat.utils.chat_new.ChatMsgEntityFactory;
import com.fengwo.module_chat.utils.chat_new.FingerprintUtils;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.event.GreetMessageEvent;
import com.fengwo.module_comment.event.UndateUnReadMessageEvent;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_websocket.EventConstant;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MessageListPresenter extends BasePresenter<IMessageListView> {

    private final ChatGreenDaoHelper daoHelper;

    public MessageListPresenter() {
        daoHelper = ChatGreenDaoHelper.getInstance();
    }

    public void getMessageList(String uid) {
        if (TextUtils.isEmpty(uid)) {
            getView().toastTip("用户id为空，请重试");
            return;
        }
        addNet(daoHelper.getMessageList(uid).subscribe(new Consumer<List<ChatListItemEntity>>() {
            @Override
            public void accept(List<ChatListItemEntity> chatListItemEntities) throws Exception {
                List<ChatListItemEntity> result = new ArrayList<>();
                for (int i = 0; i < chatListItemEntities.size(); i++) {
                    ChatListItemEntity item = chatListItemEntities.get(i);
                    if (TextUtils.isEmpty(item.getGroupId())) {
                        result.add(item);
                    }
                }
                getView().setMessageList(result);
            }
        }));
    }

    public void clearAllUnReadList(String uid) {
        addNet(daoHelper.clearAllUnReadMessage(uid).subscribe(aBoolean -> {
            if (aBoolean) {
                RxBus.get().post(new UndateUnReadMessageEvent(0));
                getView().refreshMessageList();
            }
        }));
    }

    public void topMessage(String uid, String talkUserId) {
        addNet(daoHelper.topMessage(uid, talkUserId).subscribe(aBoolean -> {
            if (aBoolean)
                getView().refreshMessageList();
        }));
    }

    public void deleteListItem(String uid, String talkUserId) {
        addNet(daoHelper.deleteListItem(uid, talkUserId).subscribe(aBoolean -> {
            if (aBoolean)
                getView().refreshMessageList();
        }));
    }

    public void buildLocalInsertOrReplaceList(GreetMessageEvent item) {
        String fingerPrint = FingerprintUtils.getRandomFingerprint(item.talkUserId, item.belongUserId);
        // 构造本地数据
        ChatMsgEntity msgEntity = ChatMsgEntityFactory.buildText(item.text, item.headerImg,
                fingerPrint, item.time, false, ChatMsgEntity.SendStatus.beReceived);
        addNet(daoHelper.changeUserAvatar(item.belongUserId, item.headerImg)
                .flatMap((Function<Boolean, ObservableSource<?>>)
                        aBoolean -> daoHelper.addChatHistoryItem(item.belongUserId, item.talkUserId, item.belongUserId, item.talkUserId, msgEntity))
                .flatMap(o -> daoHelper.insertOrReplaceList(item.belongUserId, item.talkUserId, item.talkUserName, item.talkUserAvatar,
                        null, null, msgEntity, false, false))
                .observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
                    getView().refreshMessageList();
                }, Throwable::printStackTrace));
//        addNet(daoHelper.insertOrReplaceList(item.belongUserId, item.talkUserId,
//                item.talkUserName, item.talkUserAvatar, null, null, msgEntity,
//                false, false).subscribe(aBoolean -> {
//            getView().refreshMessageList();
//        }));
    }

    @SuppressLint("CheckResult")
    public void buildAppointMessage(String toId) {
        ChatMsgEntity systemEntity = new ChatMsgEntity("", null, System.currentTimeMillis() / 1000, "您的约会通告小秘书", ChatMsgEntity.MsgType.comeText, false);
        addNet(daoHelper.insertOrReplaceList(toId, EventConstant.appoint_event + "",
                null, null,
                null, null, systemEntity, true, false).subscribe(aBoolean -> {
            getView().refreshMessageList();
        }));
    }

    public void setChatListItemEmpty(String uid, int talkUserId) {
        addNet(daoHelper.setChatListItemEmpty(uid, talkUserId).subscribe(aBoolean -> {
            if (aBoolean)
                getView().refreshMessageList();
        }));
    }

    public void getSearchMessageList(String uid, String Content) {
        if (TextUtils.isEmpty(Content)) {
            getView().toastTip("请输入搜索内容");
            return;
        }
        addNet(daoHelper.getSearchMessageList(uid, Content).subscribe(new Consumer<List<ChatListItemEntity>>() {
            @Override
            public void accept(List<ChatListItemEntity> chatListItemEntities) throws Exception {
                List<ChatListItemEntity> result = new ArrayList<>();
                for (int i = 0; i < chatListItemEntities.size(); i++) {
                    ChatListItemEntity item = chatListItemEntities.get(i);
                    if (TextUtils.isEmpty(item.getGroupId())) {
                        result.add(item);
                    }
                }
                getView().setMessageList(result);
            }
        }));
    }
}
