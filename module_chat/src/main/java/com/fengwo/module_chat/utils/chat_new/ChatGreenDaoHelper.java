package com.fengwo.module_chat.utils.chat_new;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_chat.base.RefreshMessageListEvent;
import com.fengwo.module_chat.base.RefreshUnReadMessageEvent;
import com.fengwo.module_chat.entity.ChatItemEntity;
import com.fengwo.module_chat.entity.ChatListItemEntity;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_chat.mvp.model.bean.chat_new.VoiceInfoBean;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_websocket.EventConstant;
import com.google.gson.Gson;
import com.greendao.gen.ChatItemEntityDao;
import com.greendao.gen.ChatListItemEntityDao;

import org.greenrobot.greendao.query.WhereCondition;

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

public class ChatGreenDaoHelper {

    private static volatile ChatGreenDaoHelper sInstance;

    private final String TAG = getClass().getSimpleName();
    @Autowired
    UserProviderService userProviderService;
    private final ChatItemEntityDao itemDao;
    private final ChatListItemEntityDao listDao;
    private final Gson gson;
    private final ArrayList<String> taskList;
    /**
     * 定义全局字段
     * 作用：私信放送消息后，由于成功回调没有指定是哪条消息成功。故在发送的时候保存
     *
     * @fingerPrint 消息唯一标识
     */
    public ChatMsgEntity recentNews;
    /**
     * 对方id
     */
    public String toId;

    public static ChatGreenDaoHelper getInstance() {
        if (sInstance == null) {
            synchronized (ChatGreenDaoHelper.class) {
                if (sInstance == null) {
                    sInstance = new ChatGreenDaoHelper();
                }
            }
        }
        return sInstance;
    }

    private ChatGreenDaoHelper() {
        ARouter.getInstance().inject(this);
        itemDao = GreenDaoManager.getInstance().getSession().getChatItemEntityDao();
        listDao = GreenDaoManager.getInstance().getSession().getChatListItemEntityDao();
        gson = new Gson();
        taskList = new ArrayList<>();
    }

    /**
     * 获取用户与对应用户的历史聊天记录
     * 1.读取聊天记录
     * 2.清空未读消息数
     * 3.转换数据
     */
    public Flowable<List<ChatMsgEntity>> getChatHistoryList(String belongUserId, String talkUserId, boolean isGroup, int offset) {
        Log.d(TAG, String.format("读取聊天记录:belongUserId = %s,talkUserId = %s", belongUserId, talkUserId));
        return Flowable.create((FlowableOnSubscribe<List<ChatItemEntity>>) emitter -> {
            List<ChatItemEntity> entities = itemDao.queryBuilder()
                    .where(ChatItemEntityDao.Properties.BelongUserId.eq(Integer.parseInt(belongUserId)),
                            ChatItemEntityDao.Properties.TalkUserId.eq(Integer.parseInt(talkUserId)))
                    .orderDesc(ChatItemEntityDao.Properties.Timestamp)
                    .offset(offset * 50)
                    .limit(50)
                    .list();
            Log.d(TAG, String.format("读取聊天记录:共查询到数据%d条", entities.size()));
            cleanUnreadCount(belongUserId, talkUserId);
            emitter.onNext(entities);
        }, BackpressureStrategy.ERROR).map((Function<List<ChatItemEntity>, List<ChatMsgEntity>>) chatItemEntities -> {
            if (chatItemEntities != null) {
                ArrayList<ChatMsgEntity> list = new ArrayList<>(chatItemEntities.size());
                for (ChatItemEntity entity : chatItemEntities) {
                    ChatMsgEntity item = null;
                    int msgType = entity.getMsgType();
                    if (entity.getSendStatus() == ChatMsgEntity.SendStatus.sending) {//如果消息处于加载状态 则设置为失败 // 加载中的消息处于发送成功没有收到回调
                        entity.setSendStatus(ChatMsgEntity.SendStatus.sendFaild);
                    }
                    switch (msgType) {
                        case ChatMsgEntity.MsgType.comeText:
                            item = ChatMsgEntityFactory.buildComeText(entity.getSendUserName(),
                                    entity.getSendUserHeader(), entity.getMessage(), entity.getTimestamp(),
                                    entity.getFingerPrint(), isGroup, entity.getSendStatus());
                            break;
                        case ChatMsgEntity.MsgType.comeImage:
                            item = ChatMsgEntityFactory.buildComeImage(entity.getSendUserName(),
                                    entity.getSendUserHeader(), entity.getMessage(), entity.getTimestamp()
                                    , entity.getFingerPrint(), isGroup, entity.getSendStatus());
                            break;
                        case ChatMsgEntity.MsgType.comeVoice:
                            VoiceInfoBean infoBean = new VoiceInfoBean(entity.getFileName(), entity.getDuration())
                                    .setFileUrl(entity.getFileUrl()).setSendId(String.valueOf(entity.getUserId()));
                            item = ChatMsgEntityFactory.buildComeVoice(entity.getSendUserName(),
                                    entity.getSendUserHeader(), gson.toJson(infoBean), entity.getTimestamp(),
                                    entity.getFingerPrint(), isGroup, entity.getSendStatus());
                            break;
                        case ChatMsgEntity.MsgType.toText:
                            item = ChatMsgEntityFactory.buildText(entity.getMessage(), entity.getSendUserHeader(), entity.getFingerPrint(), entity.getTimestamp(), isGroup, entity.getSendStatus());
                            break;
                        case ChatMsgEntity.MsgType.toImage:
                            item = ChatMsgEntityFactory.buildImage(entity.getSendUserHeader(), entity.getMessage(), entity.getFingerPrint(), entity.getTimestamp(), isGroup, entity.getSendStatus());
                            break;
                        case ChatMsgEntity.MsgType.toVoice:
                            VoiceInfoBean bean = new VoiceInfoBean(entity.getFileName(), entity.getDuration())
                                    .setFileUrl(entity.getFileUrl()).setSendId(String.valueOf(entity.getUserId()));
                            item = ChatMsgEntityFactory.buildVoice(entity.getSendUserHeader(),
                                    gson.toJson(bean), entity.getFingerPrint(), entity.getTimestamp(), isGroup, entity.getSendStatus());
                            break;
                        case ChatMsgEntity.MsgType.revocation:
                            item = ChatMsgEntityFactory.createRecovationEntity(entity.getSendUserName(),
                                    entity.getSendUserHeader(), entity.getTimestamp(), isGroup);
                            break;
                        case ChatMsgEntity.MsgType.inviteIntoGroup:
                            item = ChatMsgEntityFactory.createInviteIntoGroupMsgEntity(entity.getMessage(), entity.getTimestamp(), isGroup);
                            break;
                        case ChatMsgEntity.MsgType.systemTxtAttention:
                            item = ChatMsgEntityFactory.createSystemTxtAttentionMsgEntity(entity.getMessage(), entity.getTimestamp(), isGroup);
                            break;
                    }
                    if (item != null) {
                        item.userId = entity.getUserId();
                        list.add(item);
                    }
                }
                return list;
            }
            return null;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 将聊天记录插入数据库中
     *
     * @param uid  发送聊天消息的用户id
     * @param toId 接受聊天消息的用户id
     */
    @SuppressLint("CheckResult")
    public Observable<ChatItemEntity> addChatHistoryItem(String belongUserId, String talkUserId, String uid,
                                                         String toId, ChatMsgEntity item) {
        return Observable.create((ObservableOnSubscribe<ChatItemEntity>) emitter -> {
            Log.d(TAG, String.format("插入一条聊天记录:belongUserId = %s,talkUserId = %s", belongUserId, talkUserId));
            int msgType = item.getMsgType();
            ChatItemEntity entity = new ChatItemEntity();
            entity.setBelongUserId(Integer.parseInt(belongUserId));
            entity.setTalkUserId(Integer.parseInt(talkUserId));
            entity.setUserId(Integer.parseInt(uid));
            entity.setToId(Integer.parseInt(toId));
            entity.setSendUserName(item.getName());
            entity.setSendUserHeader(item.getHeaderImg());
            entity.setMsgType(msgType);
            entity.setSendStatus(item.getSendStatus());
            entity.setFingerPrint(item.getFingerPrintOfProtocal());
            L.e("=======getDate " + item.getDate());
            entity.setTimestamp(item.getDate());
            switch (msgType) {
                case ChatMsgEntity.MsgType.comeText:
                case ChatMsgEntity.MsgType.toText:
                case ChatMsgEntity.MsgType.comeImage:
                case ChatMsgEntity.MsgType.toImage:
                case ChatMsgEntity.MsgType.systemTxtAttention:
                case ChatMsgEntity.MsgType.inviteIntoGroup:
                    entity.setMessage(item.getText());
                    break;
                case ChatMsgEntity.MsgType.comeVoice:
                case ChatMsgEntity.MsgType.toVoice:
                    VoiceInfoBean bean = gson.fromJson(item.getText(), VoiceInfoBean.class);
                    entity.setFileName(bean.getFileName());
                    entity.setFileUrl(bean.getFileUrl());
                    entity.setDuration(bean.getDuration());
                    break;
            }
            itemDao.insert(entity);
            emitter.onNext(entity);
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 将消息保存到消息列表页面
     *
     * @return
     */
    public Observable<Object> insertOrReplaceList(String belongUserId, String talkUserId, String talkUserName,
                                                  String talkUserAvatar, String groupName, String groupAvatar,
                                                  ChatMsgEntity item, boolean isRead, boolean isGroup) {
        Log.d(TAG, String.format("插入消息列表:belongUserId = %s,talkUserId = %s", belongUserId, talkUserId));
        return Observable.create(emitter -> {
            ChatListItemEntity itemEntity = listDao.queryBuilder().where(
                    ChatListItemEntityDao.Properties.BelongUserId.eq(Integer.parseInt(belongUserId)),
                    ChatListItemEntityDao.Properties.TalkUserId.eq(Integer.parseInt(talkUserId))).unique();
            int msgType = item.getMsgType();
            if (itemEntity == null) {
                ChatListItemEntity entity = new ChatListItemEntity();
                entity.setBelongUserId(Integer.parseInt(belongUserId));
                entity.setTalkUserId(Integer.parseInt(talkUserId));
                entity.setUserAvatar(talkUserAvatar);
                entity.setUserName(talkUserName);
                entity.setSendStatus(item.getSendStatus());
                entity.setFingerPrint(item.getFingerPrintOfProtocal());
                entity.setMsgType(msgType);
                entity.setTimestamp(item.getDate());
                if (isGroup) {
                    entity.setGroupAvatar(groupAvatar);
                    entity.setGroupName(groupName);
                    entity.setGroupId(talkUserId);
                }
                switch (msgType) {
                    case ChatMsgEntity.MsgType.comeText:
                    case ChatMsgEntity.MsgType.toText:
                    case ChatMsgEntity.MsgType.comeImage:
                    case ChatMsgEntity.MsgType.toImage:
                    case ChatMsgEntity.MsgType.revocation:
                    case ChatMsgEntity.MsgType.inviteIntoGroup:
                        entity.setMessage(item.getText());
                        break;
                    case ChatMsgEntity.MsgType.comeVoice:
                    case ChatMsgEntity.MsgType.toVoice:
                        VoiceInfoBean bean = gson.fromJson(item.getText(), VoiceInfoBean.class);
                        entity.setFileName(bean.getFileName());
                        entity.setFileUrl(bean.getFileUrl());
                        entity.setDuration(bean.getDuration());
                        break;
                }
                if (!isRead) entity.setUnreadCount(1);
                listDao.insert(entity);
                emitter.onNext(entity);
            } else {
                itemEntity.setMsgType(msgType);
                itemEntity.setUserName(talkUserName);
                itemEntity.setUserAvatar(talkUserAvatar);
                itemEntity.setTimestamp(item.getDate());
                if (itemEntity.getTopTime() > 0)
                    itemEntity.setTopTime(System.currentTimeMillis());
                itemEntity.setFingerPrint(item.getFingerPrintOfProtocal());
                if (isGroup) {
                    itemEntity.setGroupAvatar(groupAvatar);
                    itemEntity.setGroupName(groupName);
                    itemEntity.setGroupId(talkUserId);
                }
                switch (msgType) {
                    case ChatMsgEntity.MsgType.comeText:
                    case ChatMsgEntity.MsgType.toText:
                    case ChatMsgEntity.MsgType.comeImage:
                    case ChatMsgEntity.MsgType.toImage:
                    case ChatMsgEntity.MsgType.revocation:
                    case ChatMsgEntity.MsgType.inviteIntoGroup:
                        itemEntity.setMessage(item.getText());
                        break;
                    case ChatMsgEntity.MsgType.comeVoice:
                    case ChatMsgEntity.MsgType.toVoice:
                        VoiceInfoBean bean = gson.fromJson(item.getText(), VoiceInfoBean.class);
                        itemEntity.setFileName(bean.getFileName());
                        itemEntity.setFileUrl(bean.getFileUrl());
                        itemEntity.setDuration(bean.getDuration());
                        break;
                }
                if (!isRead) itemEntity.setUnreadCount(itemEntity.getUnreadCount() + 1);
                listDao.update(itemEntity);
                emitter.onNext(itemEntity);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 获取数据库中的消息列表
     */
    public Flowable<List<ChatListItemEntity>> getMessageList(String belongUserId) {
        Log.d(TAG, "读取消息列表");
        return Flowable.create((FlowableOnSubscribe<List<ChatListItemEntity>>) emitter -> {
            List<ChatListItemEntity> list = listDao.queryBuilder()
                    .where(ChatListItemEntityDao.Properties.BelongUserId.eq(Integer.parseInt(belongUserId)))
                    .orderDesc(ChatListItemEntityDao.Properties.Id).list();
            emitter.onNext(list);
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 置空数据库中的消息列表的某条消息（用于清空消息后 列表消息置空）
     */
    public Flowable<Boolean> setChatListItemEmpty(String belongUserId, int talkId) {
        Log.d(TAG, "读取消息列表");
        return Flowable.create((FlowableOnSubscribe<Boolean>) emitter -> {
            List<ChatListItemEntity> list = listDao.queryBuilder()
                    .where(ChatListItemEntityDao.Properties.BelongUserId.eq(Integer.parseInt(belongUserId)))
                    .orderDesc(ChatListItemEntityDao.Properties.Id).list();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTalkUserId() == talkId) {
                    ChatListItemEntity itemEntity = list.get(i);
                    itemEntity.setMessage("");
                    listDao.update(itemEntity);
                    break;
                }
            }
            emitter.onNext(true);
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 获取数据库中未读消息数
     */
    public Flowable<Integer> getUnReadMessageCount(int belongUserId, String noMsgGroundIds) {
        Log.d(TAG, "获取未读消息数");
        return Flowable.create((FlowableOnSubscribe<Integer>) emitter -> {
            //免打扰消息处理

//            Log.d(TAG, "免打扰通知: "+groundIds );
           // List<String> noDistrubLists = GsonUtils.json2ObjList(noMsgGroundIds, String.class);
            List<ChatListItemEntity> list = listDao.queryBuilder()
                    .where(ChatListItemEntityDao.Properties.BelongUserId.eq(belongUserId)).list();
            int sum = 0;
            for (ChatListItemEntity entity : list) {
                int unreadCount = 0;
                if (TextUtils.isEmpty(entity.getGroupId())) {//单聊计数
                    unreadCount = entity.getUnreadCount();
                } else {//群聊
//                    if (noDistrubLists != null && noDistrubLists.contains(entity.getGroupId())) {//免打扰模式
//                    } else {
//                        unreadCount = entity.getUnreadCount();
//                    }
                }
                sum += unreadCount;
            }
            emitter.onNext(sum);
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 清空当前用户与某个用户对话的未读消息数
     */
    public void cleanUnreadCount(String belongUserId, String talkUserId) {
        Observable.create(emitter -> {
            ChatListItemEntity itemEntity = listDao.queryBuilder()
                    .where(ChatListItemEntityDao.Properties.BelongUserId.eq(Integer.parseInt(belongUserId)),
                            ChatListItemEntityDao.Properties.TalkUserId.eq(Integer.parseInt(talkUserId))).unique();
            if (itemEntity != null) {
                itemEntity.setUnreadCount(0);
                listDao.update(itemEntity);
            }
            emitter.onNext(0);
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    /**
     * 清空当前用户与某个用户对话的未读消息数（观察者)
     * //todo 其他场景刷新是在onResume方法 此方法适用于清空操作在onResume之后的场景（私聊/群聊等）
     */
    @Deprecated
    public void observeCleanUnreadCount(String belongUserId, String talkUserId) {
        Observable.create(emitter -> {
            ChatListItemEntity itemEntity = listDao.queryBuilder()
                    .where(ChatListItemEntityDao.Properties.BelongUserId.eq(Integer.parseInt(belongUserId)),
                            ChatListItemEntityDao.Properties.TalkUserId.eq(Integer.parseInt(talkUserId))).unique();
            if (itemEntity != null) {
                itemEntity.setUnreadCount(0);
                listDao.update(itemEntity);
            }
            RxBus.get().post(new RefreshMessageListEvent());
            RxBus.get().post(new RefreshUnReadMessageEvent());
            emitter.onNext(0);
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    /**
     * 清除数据库中未读消息数
     */
    public Observable<Boolean> clearAllUnReadMessage(String belongUserId) {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            List<ChatListItemEntity> list = listDao.queryBuilder()
                    .where(ChatListItemEntityDao.Properties.BelongUserId.eq(belongUserId)).list();
            for (ChatListItemEntity entity : list) {
                entity.setUnreadCount(0);
            }
            emitter.onNext(true);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 置顶消息
     */
    public Observable<Boolean> topMessage(String belongUserId, String talkUserId) {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            ChatListItemEntity chatListItemEntity = listDao.queryBuilder()
                    .where(ChatListItemEntityDao.Properties.BelongUserId.eq(belongUserId))
                    .where(ChatListItemEntityDao.Properties.TalkUserId.eq(talkUserId)).unique();
            if (chatListItemEntity.getTopTime() == 0)
                chatListItemEntity.setTopTime(System.currentTimeMillis());
            else
                chatListItemEntity.setTopTime(0);
            emitter.onNext(true);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 删除会话列表的消息
     */
    public Observable<Boolean> deleteListItem(String belongUserId, String talkUserId) {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            ChatListItemEntity entity = listDao.queryBuilder()
                    .where(ChatListItemEntityDao.Properties.BelongUserId.eq(belongUserId))
                    .where(ChatListItemEntityDao.Properties.TalkUserId.eq(talkUserId)).unique();
            if (entity != null) listDao.delete(entity);
            //同时删除聊天记录
            List<ChatItemEntity> list = itemDao.queryBuilder()
                    .where(ChatItemEntityDao.Properties.BelongUserId.eq(Integer.parseInt(belongUserId)),
                            ChatItemEntityDao.Properties.TalkUserId.eq(Integer.parseInt(talkUserId))).list();
            itemDao.deleteInTx(list);
            emitter.onNext(true);
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 首次安装 构建本地消息（约会助手/互赞通知/最近访客）
     */
    public Observable<Boolean> buildMessageListFirstInstall(String toId) {
        return Observable.combineLatest(buildAppointFirstInstall(toId),
                buildInteractFirstInstall(toId),
                buildRecentVisitorFirstInstall(toId),
//                buildSystemMessageFirstInstall(toId),
                (o, o2, o3) -> true)
                .subscribeOn(Schedulers.io());
    }

    private Observable<Object> buildAppointFirstInstall(String toId) {
        ChatMsgEntity systemEntity = new ChatMsgEntity("", null, System.currentTimeMillis() / 1000, "您的约会通告小秘书", ChatMsgEntity.MsgType.comeText, false);
        return insertOrReplaceList(toId, EventConstant.appoint_event + "",
                null, null,
                null, null, systemEntity, true, false);
    }

    private Observable<Object> buildInteractFirstInstall(String toId) {
        ChatMsgEntity systemEntity = new ChatMsgEntity("", null, (System.currentTimeMillis() / 1000) - 1, "多发布动态，让更多的人关注你~", ChatMsgEntity.MsgType.comeText, false);
        return insertOrReplaceList(toId, EventConstant.interact_event + "",
                null, null,
                null, null, systemEntity, true, false);
    }

    private Observable<Object> buildRecentVisitorFirstInstall(String toId) {
        ChatMsgEntity systemEntity = new ChatMsgEntity("", null, (System.currentTimeMillis() / 1000) - 2, "关心你的人偷偷来看你了~", ChatMsgEntity.MsgType.comeText, false);
        return insertOrReplaceList(toId, EventConstant.recent_visitor_event + "",
                null, null,
                null, null, systemEntity, true, false);
    }

    private Observable<Object> buildSystemMessageFirstInstall(String toId) {
        ChatMsgEntity systemEntity = new ChatMsgEntity("", null, (System.currentTimeMillis() / 1000) - 3, "系统通知", ChatMsgEntity.MsgType.comeText, false);
        return insertOrReplaceList(toId, EventConstant.system_event + "",
                null, null,
                null, null, systemEntity, true, false);
    }

    private Observable<Object> buildGreetMessageFirstInstall(String toId) {
        ChatMsgEntity systemEntity = new ChatMsgEntity("", null, (System.currentTimeMillis() / 1000) - 4, "收到的招呼", ChatMsgEntity.MsgType.comeText, false);
        return insertOrReplaceList(toId, EventConstant.greet_event + "",
                null, null,
                null, null, systemEntity, true, false);
    }

    /**
     * 消息撤回 刷新聊天界面
     * 1.查询数据库消息
     * 2.刷新消息状态
     * 3.更新数据库
     */
    @SuppressLint("CheckResult")
    public Observable<ChatMsgEntity> revocationChatMsg(String fingerPrint, boolean isGroup,String name) {
        Log.d(TAG, "将状态修改为撤回");
        return Observable.create((ObservableOnSubscribe<ChatMsgEntity>) emitter -> {
            // 刷新聊天数据 状态
            ChatItemEntity entity = itemDao.queryBuilder()
                    .where(ChatItemEntityDao.Properties.FingerPrint.eq(fingerPrint)).unique();
            if (entity == null) return;
            entity.setMsgType(ChatMsgEntity.MsgType.revocation);
            entity.setIsRevocation(true);
            entity.setMessage(userProviderService.getUserInfo().getNickname().equals(name)?"“您”撤回了一条消息":"对方撤回了一条消息");
            itemDao.update(entity);
            ChatMsgEntity chatMsgEntity = new ChatMsgEntity(entity.getSendUserName(), "", System.currentTimeMillis() / 1000, "", entity.getMsgType(), fingerPrint, isGroup, entity.getSendStatus(), ChatMsgEntity.SendStatusSecondary.none);
            emitter.onNext(chatMsgEntity);
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 撤回时：刷新聊天列表
     */
    public Observable<Boolean> refreshMessageList(String fingerPrint,String name) {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            // 刷新列表数据
            ChatListItemEntity itemEntity = listDao.queryBuilder().where(
                    ChatListItemEntityDao.Properties.FingerPrint.eq(fingerPrint)).unique();
            if (itemEntity == null) return;
            itemEntity.setIsRevocation(true);
            itemEntity.setMsgType(ChatMsgEntity.MsgType.revocation);
            itemEntity.setMessage(userProviderService.getUserInfo().getNickname().equals(name)?"“您”撤回了一条消息":"对方撤回了一条消息");
            listDao.update(itemEntity);
            emitter.onNext(true);
        }).subscribeOn(Schedulers.io());

    }

    /**
     * 删除消息
     */
    public Observable<ChatItemEntity> deleteItem(String belongUserId, String talkUserId, String fingerPrint) {
        Log.d(TAG, "删除消息");
        return Observable.create((ObservableOnSubscribe<ChatItemEntity>) emitter -> {
            // 查询数据库，获取数据
            ChatItemEntity entity = itemDao.queryBuilder()
                    .where(ChatItemEntityDao.Properties.BelongUserId.eq(Integer.parseInt(belongUserId)),
                            ChatItemEntityDao.Properties.TalkUserId.eq(Integer.parseInt(talkUserId)),
                            ChatItemEntityDao.Properties.FingerPrint.eq(fingerPrint)).unique();
            if (entity != null) itemDao.delete(entity);
            emitter.onNext(entity);
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 删除本地系统消息
     */
    public Observable<ChatItemEntity> deleteSystemItem(String belongUserId, String talkUserId,int msgType) {
        Log.d(TAG, "删除消息");
        return Observable.create((ObservableOnSubscribe<ChatItemEntity>) emitter -> {
            // 查询数据库，获取数据
            ChatItemEntity entity = itemDao.queryBuilder()
                    .where(ChatItemEntityDao.Properties.BelongUserId.eq(Integer.parseInt(belongUserId)),
                            ChatItemEntityDao.Properties.TalkUserId.eq(Integer.parseInt(talkUserId)),
                            ChatItemEntityDao.Properties.MsgType.eq(msgType)).unique();
            if (entity != null) itemDao.delete(entity);
            emitter.onNext(entity);
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 删除聊天记录
     */
    public Observable<ChatItemEntity> deleteChatRecords(String belongUserId, String talkUserId) {
        Log.d(TAG, "删除聊天记录");
        return Observable.create((ObservableOnSubscribe<ChatItemEntity>) emitter -> {
            // 查询数据库，获取数据
            List<ChatItemEntity> list = itemDao.queryBuilder()
                    .where(ChatItemEntityDao.Properties.BelongUserId.eq(Integer.parseInt(belongUserId)),
                            ChatItemEntityDao.Properties.TalkUserId.eq(Integer.parseInt(talkUserId))).list();
            itemDao.deleteInTx(list);
            emitter.onNext(new ChatItemEntity());
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 删除群聊
     */
    @SuppressLint("CheckResult")
    public void deleteGroup(String belongUserId, String groupId) {
        Log.d(TAG, "删除群聊");
        Observable.create(emitter -> {
            ChatListItemEntity listEntity = listDao.queryBuilder().where(
                    ChatListItemEntityDao.Properties.BelongUserId.eq(Integer.parseInt(belongUserId)),
                    ChatListItemEntityDao.Properties.TalkUserId.eq(Integer.parseInt(groupId))).unique();
            if (listEntity != null) {
                listDao.delete(listEntity);
                emitter.onNext(listEntity);
            } else emitter.onError(new NullPointerException("查无此数据"));
        }).map(o -> {
            List<ChatItemEntity> list = itemDao.queryBuilder().where(
                    ChatItemEntityDao.Properties.BelongUserId.eq(Integer.parseInt(belongUserId)),
                    ChatItemEntityDao.Properties.TalkUserId.eq(Integer.parseInt(groupId))).list();
            if (list != null && list.size() > 0) {
                for (ChatItemEntity item : list) {
                    itemDao.delete(item);
                }
            }
            return true;
        }).subscribeOn(Schedulers.io()).subscribe(aBoolean -> {
        }, Throwable::printStackTrace);
    }

    /**
     * 修改用户头像
     *
     * @return
     */
    @SuppressLint("CheckResult")
    public Observable<Boolean> changeUserAvatar(String userId, String userAvatar) {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            if (TextUtils.isEmpty(userId)) emitter.onError(new NullPointerException("userId为空"));
            if (taskList.contains(userId)) {
                emitter.onNext(false);
                return;
            }
            taskList.add(userId);
            List<ChatItemEntity> historyList = itemDao.queryBuilder().where(
                    ChatItemEntityDao.Properties.UserId.eq(Integer.parseInt(userId))
            ).orderDesc(ChatItemEntityDao.Properties.Timestamp).list();
            if (historyList != null && historyList.size() > 0) {
                if (!TextUtils.equals(historyList.get(0).getSendUserHeader(), userAvatar)) {
                    Log.d(TAG, String.format("修改用户头像： userId = %s, userAvatar = %s", userId, userAvatar));
                    for (ChatItemEntity item : historyList) {
                        item.setSendUserHeader(userAvatar);
                        itemDao.update(item);
                    }
                    emitter.onNext(true);
                } else emitter.onNext(false);
            } else emitter.onNext(false);
            taskList.remove(userId);
        }).map(s -> {
            if (s) {
                ChatListItemEntity historyListEntity = listDao.queryBuilder().where(
                        ChatListItemEntityDao.Properties.TalkUserId.eq(userId)
                ).unique();
                if (historyListEntity != null) {
                    historyListEntity.setUserAvatar(userAvatar);
                    listDao.update(historyListEntity);
                    return true;
                }
            }
            return false;
        }).subscribeOn(Schedulers.io());
    }


    /**
     * 群聊 发送消息，发送成功刷新消息状态
     */
    public Observable<ChatItemEntity> getChatHistory(ChatMsgEntity groupItem) {
        return Observable.create((ObservableOnSubscribe<ChatItemEntity>) emitter -> {
            ChatItemEntity unique = itemDao.queryBuilder()
                    .where(ChatItemEntityDao.Properties.FingerPrint.eq(groupItem.getFingerPrintOfProtocal())).unique();
            if (unique == null) return;
            unique.setSendStatus(groupItem.getSendStatus());
            itemDao.update(unique);
            emitter.onNext(unique);
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 更新单聊发送消息状态
     *
     * @return
     */
    public Observable<ChatMsgEntity> updateMessageStatus() {
        return Observable.create((ObservableOnSubscribe<ChatMsgEntity>) emitter -> {
            if (recentNews == null) return;
            ChatItemEntity chatItemEntity = itemDao.queryBuilder().where(ChatItemEntityDao.Properties.FingerPrint.eq(recentNews.getFingerPrintOfProtocal())).unique();
            if (chatItemEntity == null) return;
            chatItemEntity.setSendStatus(ChatMsgEntity.SendStatus.beReceived);
            itemDao.update(chatItemEntity);
            recentNews.setSendStatus(ChatMsgEntity.SendStatus.beReceived);
            emitter.onNext(recentNews);
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 更新群聊发送消息状态
     *
     * @return
     */
    public Observable<ChatItemEntity> updateMessageStatus(String fingerPrint) {
        return Observable.create((ObservableOnSubscribe<ChatItemEntity>) emitter -> {
            ChatItemEntity chatItemEntity = itemDao.queryBuilder().where(ChatItemEntityDao.Properties.FingerPrint.eq(fingerPrint)).unique();
            if (chatItemEntity == null) return;
            chatItemEntity.setSendStatus(ChatMsgEntity.SendStatus.beReceived);
            itemDao.update(chatItemEntity);
            emitter.onNext(chatItemEntity);
        }).subscribeOn(Schedulers.io());
    }

    public Flowable<List<ChatListItemEntity>> getSearchMessageList(String belongUserId,String content) {
        WhereCondition where1 = ChatListItemEntityDao.Properties.BelongUserId.eq(Integer.parseInt(belongUserId));
        L.e("======"+content);
        WhereCondition where2 = ChatListItemEntityDao.Properties.UserName.like("%"+content+"%");
        return Flowable.create((FlowableOnSubscribe<List<ChatListItemEntity>>) emitter -> {
            List<ChatListItemEntity> list = listDao.queryBuilder()
                    .where(where1,where2)
                    .list();
            L.e("======"+list.size());
            emitter.onNext(list);
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}