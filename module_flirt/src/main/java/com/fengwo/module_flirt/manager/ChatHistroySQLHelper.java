package com.fengwo.module_flirt.manager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_chat.entity.FlirtChatEntity;
import com.fengwo.module_chat.entity.FlirtConversationEntity;
import com.fengwo.module_chat.utils.chat_new.GreenDaoManager;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_websocket.bean.MsgType;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.WenboMsgAction;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;
import com.greendao.gen.FlirtChatEntityDao;
import com.greendao.gen.FlirtConversationEntityDao;


import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * I聊历史聊天记录
 */
public class ChatHistroySQLHelper {
    private static volatile ChatHistroySQLHelper chatHistroyHelper;
    private final FlirtChatEntityDao flirtChatEntityDao;
    private final FlirtConversationEntityDao flirtConversationEntityDao;

    @Autowired
    UserProviderService userProviderService;

    public static ChatHistroySQLHelper getInstance() {
        if (chatHistroyHelper == null) {
            synchronized (ChatHistroySQLHelper.class) {
                if (chatHistroyHelper == null) {
                    chatHistroyHelper = new ChatHistroySQLHelper();
                }
            }
        }
        return chatHistroyHelper;
    }

    private ChatHistroySQLHelper() {
        ARouter.getInstance().inject(this);
        flirtChatEntityDao = GreenDaoManager.getInstance().getSession().getFlirtChatEntityDao();
        flirtConversationEntityDao = GreenDaoManager.getInstance().getSession().getFlirtConversationEntityDao();
    }

    /**
     * 获取和用户otherUserId的消息未读数
     *
     * @param otherUserId
     * @return
     */
    public Flowable<Integer> getUnReadMsgCount(String otherUserId) {
        return Flowable
                .create((FlowableOnSubscribe<Integer>) emitter -> {
                    FlirtConversationEntity conversation = flirtConversationEntityDao.queryBuilder()
                            .where(FlirtConversationEntityDao.Properties.OtherUserId.eq(otherUserId))
                            .unique();
                    if (conversation != null) {
                        emitter.onNext(conversation.getUnReadMsgCount());
                    } else {
                        emitter.onNext(0);
                    }
                }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 清除和用户otherUserId的消息未读数
     *
     * @param otherUserId
     */
    public void clearUnReadMsgCount(int otherUserId) {
        Observable
                .create((ObservableOnSubscribe<Integer>) emitter -> {
                    //将会话的未读消息数改为0；
                    FlirtConversationEntity conversation = flirtConversationEntityDao.queryBuilder()
                            .where(FlirtConversationEntityDao.Properties.OtherUserId.eq(otherUserId))
                            .unique();
                    if (conversation != null) {
                        conversation.setUnReadMsgCount(0);
                        flirtConversationEntityDao.update(conversation);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    /**
     * 获取聊天记录
     *
     * @return
     */
    public Flowable<ArrayList<SocketRequest<WenboWsChatDataBean>>> getChatHistroy(int page, int belongUserId, int talkUserId) {
        return Flowable.create((FlowableOnSubscribe<List<FlirtChatEntity>>) emitter -> {
            L.e("========1 " + flirtChatEntityDao.loadAll().size());
            QueryBuilder<FlirtChatEntity> flirtChatEntityQueryBuilder = flirtChatEntityDao.queryBuilder();
            WhereCondition WhereCondition1 = flirtChatEntityQueryBuilder.and(FlirtChatEntityDao.Properties.FromUser_userId.eq(belongUserId),
                    FlirtChatEntityDao.Properties.ToUid.eq(talkUserId));
            WhereCondition WhereCondition2 = flirtChatEntityQueryBuilder.and(FlirtChatEntityDao.Properties.FromUser_userId.eq(talkUserId),
                    FlirtChatEntityDao.Properties.ToUid.eq(belongUserId));
            List<FlirtChatEntity> entities = flirtChatEntityDao.queryBuilder()
                    .whereOr(WhereCondition1, WhereCondition2)
                    .orderDesc(FlirtChatEntityDao.Properties.Timestamp)
                    .offset(page * 20)
                    .limit(20)
                    .list();
            L.e("========2 " + entities.size());

            //清空未读消息数
            clearUnReadMsgCount(talkUserId);

            emitter.onNext(entities);
        }, BackpressureStrategy.ERROR).map((Function<List<FlirtChatEntity>, ArrayList<SocketRequest<WenboWsChatDataBean>>>) flirtChatEntitys -> {
            if (flirtChatEntitys != null) {
                ArrayList<SocketRequest<WenboWsChatDataBean>> socketRequests = new ArrayList<>();
                for (FlirtChatEntity entity : flirtChatEntitys) {
                    SocketRequest<WenboWsChatDataBean> request = new SocketRequest<>();

                    request.msgType = entity.getMsgType();
                    request.toUid = entity.getToUid();
                    request.fromUid = entity.getFromUid();
                    request.vendor = entity.getVendor();
                    request.busiEvent = entity.getBusiEvent();
                    request.msgId = entity.getMsgId();
                    request.sendStatus = entity.getSendStatus();
                    request.version = entity.getVersion();
                    request.timestamp = entity.getTimestamp();

                    WenboWsChatDataBean wenboWsChatDataBean = new WenboWsChatDataBean();
                    WenboWsChatDataBean.ContentBean contentBean = new WenboWsChatDataBean.ContentBean();
                    contentBean.setDuration(entity.getDuration());
                    contentBean.setTime(entity.getTime());
                    contentBean.setType(entity.getType());
                    contentBean.setValue(entity.getValue());
                    contentBean.setOrdinaryGift(entity.getIsOrdinaryGift());
                    wenboWsChatDataBean.setContent(contentBean);

                    WenboWsChatDataBean.FromUserBean fromUserBean = new WenboWsChatDataBean.FromUserBean();
                    fromUserBean.setHeadImg(entity.getFromUser_headImg());
                    fromUserBean.setNickname(entity.getFromUser_nickname());
                    fromUserBean.setRole(entity.getFromUser_role());
                    fromUserBean.setUserId(entity.getFromUser_userId());
                    wenboWsChatDataBean.setFromUser(fromUserBean);
                    WenboWsChatDataBean.RoomBean roomBean = new WenboWsChatDataBean.RoomBean();
                    roomBean.setAnchorId(entity.getAnchorId());
                    roomBean.setRoomId(entity.getRoomId());
                    roomBean.setRoomTitle(entity.getRoomTitle());
                    wenboWsChatDataBean.setRoom(roomBean);
                    WenboWsChatDataBean.ToUserBean toUserBean = new WenboWsChatDataBean.ToUserBean();
                    L.e("==========toUserBean " + entity.getToUser_headImg());
                    toUserBean.setHeadImg(entity.getToUser_headImg());
                    toUserBean.setNickname(entity.getToUser_nickname());
                    toUserBean.setRole(entity.getToUser_role());
                    toUserBean.setUserId(entity.getToUser_userId());
                    wenboWsChatDataBean.setToUser(toUserBean);
                    wenboWsChatDataBean.setAction(entity.getAction());
                    wenboWsChatDataBean.setGears(entity.getGears());
                    wenboWsChatDataBean.getContent().setOrdinaryGift(entity.getIsOrdinaryGift());
                    wenboWsChatDataBean.setIsGears(entity.getIsGears());
                    request.data = wenboWsChatDataBean;

                    socketRequests.add(request);
                }
                L.e("======== socketRequests " + socketRequests.size());
                return socketRequests;
            }
            return null;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 添加聊天记录，同时添加会话的未读消息数，默认添加的消息都未读，若当前正在聊天则需要在对应的业务处手动
     * 清理该会话的消息未读数
     *
     * @param chatBean
     */
    String msgid = "";

    public Observable addChatRecord(SocketRequest<WenboWsChatDataBean> chatBean) {
        return Observable
                .create((ObservableOnSubscribe<Integer>) emitter -> {
                    KLog.e("tag", "添加数据" + chatBean.msgId);
                    if (!msgid.equals(chatBean.msgId)) {
                        flirtChatEntityDao.save(converFlirtChat(chatBean));
                        msgid = chatBean.msgId;
                        //添加聊天记录后，添加会话的未读消息数，默认添加的消息都未读
                        int selfId = userProviderService.getUserInfo().getId();
                        String fromUserId = chatBean.data.getFromUser().getUserId();
                        //判断是别人发送给我的消息
                        if (!String.valueOf(selfId).equals(fromUserId)
                                && String.valueOf(selfId).equals(chatBean.toUid)) {
//                        List<FlirtConversationEntity> conversations = flirtConversationEntityDao.queryBuilder()
//                                .where(FlirtConversationEntityDao.Properties.OtherUserId.eq(fromUserId)).list();
//                        FlirtConversationEntity conversation = conversations.get(0);
                            FlirtConversationEntity conversation = flirtConversationEntityDao.queryBuilder()
                                    .where(FlirtConversationEntityDao.Properties.OtherUserId.eq(fromUserId)).unique();
                            if (conversation != null) {
                                conversation.setUnReadMsgCount(conversation.getUnReadMsgCount() + 1);
                                flirtConversationEntityDao.update(conversation);
                            } else {
                                conversation = new FlirtConversationEntity();
                                conversation.setUnReadMsgCount(1);
                                conversation.setOtherUserId(fromUserId);
                                flirtConversationEntityDao.insert(conversation);
                            }
                        }
                        emitter.onNext(0);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void addChatRecordAsync(SocketRequest<WenboWsChatDataBean> chatBean) {
        KLog.e("tag", "添加数据" + chatBean.toUid);
        flirtChatEntityDao.save(converFlirtChat(chatBean));

        //添加聊天记录后，添加会话的未读消息数，默认添加的消息都未读
        int selfId = userProviderService.getUserInfo().getId();
        String fromUserId = chatBean.data.getFromUser().getUserId();
        //判断是别人发送给我的消息
        if (!String.valueOf(selfId).equals(fromUserId)
                && String.valueOf(selfId).equals(chatBean.toUid)) {
//            List<FlirtConversationEntity> conversations = flirtConversationEntityDao.queryBuilder()
//                    .where(FlirtConversationEntityDao.Properties.OtherUserId.eq(fromUserId)).list();
//            FlirtConversationEntity conversation = conversations.get(0);
            FlirtConversationEntity conversation = flirtConversationEntityDao.queryBuilder()
                    .where(FlirtConversationEntityDao.Properties.OtherUserId.eq(fromUserId)).unique();
            if (conversation != null) {
                conversation.setUnReadMsgCount(conversation.getUnReadMsgCount() + 1);
                flirtConversationEntityDao.update(conversation);
            } else {
                conversation = new FlirtConversationEntity();
                conversation.setUnReadMsgCount(1);
                conversation.setOtherUserId(fromUserId);
                flirtConversationEntityDao.insert(conversation);
            }
        }
    }

    @NotNull
    private FlirtChatEntity converFlirtChat(SocketRequest<WenboWsChatDataBean> chatBean) {
        FlirtChatEntity entity = new FlirtChatEntity();
        WenboWsChatDataBean.FromUserBean fromUser = chatBean.data.getFromUser();
        WenboWsChatDataBean.ToUserBean toUser = chatBean.data.getToUser();
        WenboWsChatDataBean.RoomBean room = chatBean.data.getRoom();
        WenboWsChatDataBean.ContentBean content = chatBean.data.getContent();

        entity.setMsgType(chatBean.msgType).setToUid(chatBean.toUid).setFromUid(chatBean.fromUid).setVendor(chatBean.vendor).setBusiEvent(chatBean.busiEvent)
                .setMsgId(chatBean.msgId).setSendStatus(chatBean.sendStatus).setVersion(chatBean.version).setTimestamp(chatBean.timestamp).setAction(chatBean.data.getAction())
                .setFromUser_userId(fromUser.getUserId()).setFromUser_nickname(fromUser.getNickname()).setFromUser_role(fromUser.getRole()).setFromUser_headImg(fromUser.getHeadImg())
                .setToUser_userId(toUser.getUserId()).setToUser_nickname(toUser.getNickname()).setFromUser_role(toUser.getRole()).setToUser_headImg(toUser.getHeadImg())
                .setRoomId(room.getRoomId()).setRoomTitle(room.getRoomTitle()).setAnchorId(room.getAnchorId())
                .setType(content.getType()).setValue(content.getValue()).setTime(content.getTime()).setDuration(content.getDuration()).setGears(chatBean.data.getGears())
                .setIsGears(chatBean.data.getIsGears()).setIsOrdinaryGift(content.isOrdinaryGift());
        L.e("=====entity " + entity.toString());
        return entity;
    }

    /**
     * 删除指定消息
     *
     * @param msgId
     */
    public void deleteSingChatMsg(String msgId) {
        Observable
                .create(emitter -> {
                    QueryBuilder<FlirtChatEntity> queryBuilder = flirtChatEntityDao.queryBuilder();
                    FlirtChatEntity flirtChatEntity = queryBuilder.where(FlirtChatEntityDao.Properties.MsgId.eq(msgId)).unique();
                    if (flirtChatEntity != null) {
                        flirtChatEntityDao.delete(flirtChatEntity);
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    /**
     * 撤回消息
     *
     * @param msgId
     */
    public void revokeChatMsg(boolean isSend, String msgId) {
        Observable
                .create(emitter -> {
                    QueryBuilder<FlirtChatEntity> queryBuilder = flirtChatEntityDao.queryBuilder();
                    FlirtChatEntity flirtChatEntity = queryBuilder.where(FlirtChatEntityDao.Properties.MsgId.eq(msgId)).unique();
                    if (flirtChatEntity != null) {
                        flirtChatEntity.setAction(WenboMsgAction.WITHDRAW);
                        flirtChatEntity.setMsgType(isSend ? MsgType.toRevocation : MsgType.comeRevocation);
                        flirtChatEntityDao.update(flirtChatEntity);

                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    /**
     * 修改消息状态
     *
     * @param msgId
     */
    public void editTypeChatMsg(int sendStatus, String msgId) {
        Observable
                .create(emitter -> {
                    QueryBuilder<FlirtChatEntity> queryBuilder = flirtChatEntityDao.queryBuilder();
                    FlirtChatEntity flirtChatEntity = queryBuilder.where(FlirtChatEntityDao.Properties.MsgId.eq(msgId)).unique();
                    if (flirtChatEntity != null) {
                        flirtChatEntity.setSendStatus(sendStatus);
                        flirtChatEntityDao.update(flirtChatEntity);
                    } else {
                        return;
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public Flowable<ArrayList<SocketRequest<WenboWsChatDataBean>>> deleteFlirtChat(int page, int belongUserId, int talkUserId) {
        return Flowable.create((FlowableOnSubscribe<List<FlirtChatEntity>>) emitter -> {
            L.e("========1 " + flirtChatEntityDao.loadAll().size());
            QueryBuilder<FlirtChatEntity> flirtChatEntityQueryBuilder = flirtChatEntityDao.queryBuilder();
            WhereCondition WhereCondition1 = flirtChatEntityQueryBuilder.and(FlirtChatEntityDao.Properties.FromUser_userId.eq(belongUserId),
                    FlirtChatEntityDao.Properties.ToUser_userId.eq(talkUserId));
            WhereCondition WhereCondition2 = flirtChatEntityQueryBuilder.and(FlirtChatEntityDao.Properties.FromUser_userId.eq(talkUserId),
                    FlirtChatEntityDao.Properties.ToUser_userId.eq(belongUserId));
            List<FlirtChatEntity> entities = flirtChatEntityDao.queryBuilder()
                    .whereOr(WhereCondition1, WhereCondition2)
                    .orderDesc(FlirtChatEntityDao.Properties.Timestamp)

                    .list();
            L.e("========2 " + entities.size());

            //删除对应的会话记录
            FlirtConversationEntity conversation = flirtConversationEntityDao.queryBuilder()
                    .where(FlirtConversationEntityDao.Properties.OtherUserId.eq(talkUserId))
                    .unique();
            if (conversation != null) {
                flirtConversationEntityDao.delete(conversation);
            }

            emitter.onNext(entities);
        }, BackpressureStrategy.ERROR).map((Function<List<FlirtChatEntity>, ArrayList<SocketRequest<WenboWsChatDataBean>>>) flirtChatEntitys -> {
            if (flirtChatEntitys != null) {
                ArrayList<SocketRequest<WenboWsChatDataBean>> socketRequests = new ArrayList<>();
                for (FlirtChatEntity entity : flirtChatEntitys) {
                    flirtChatEntityDao.delete(entity);
                }

                L.e("======== socketRequests " + socketRequests.size());
                return socketRequests;
            }
            return null;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 更新聊天记录
     */
    public void updateChatRecord(FlirtChatEntity flirtChatEntity) {
        flirtChatEntityDao.update(flirtChatEntity);
    }


    /**
     * 清空聊天记录
     */
    public void clearChatRecord() {
        flirtChatEntityDao.deleteAll();
        flirtConversationEntityDao.deleteAll();
    }

}