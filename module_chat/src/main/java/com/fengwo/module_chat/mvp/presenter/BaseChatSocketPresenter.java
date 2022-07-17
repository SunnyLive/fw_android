package com.fengwo.module_chat.mvp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_chat.base.RefreshAfterClearEvent;
import com.fengwo.module_chat.entity.ChatListItemEntity;
import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_chat.mvp.model.bean.EnterGroupModel;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_chat.mvp.model.bean.chat_new.VoiceInfoBean;
import com.fengwo.module_chat.mvp.ui.activity.chat_new.IBaseChatView;
import com.fengwo.module_chat.utils.chat_new.ChatGreenDaoHelper;
import com.fengwo.module_chat.utils.chat_new.ChatMsgEntityFactory;
import com.fengwo.module_chat.utils.chat_new.ChatSendMessageUtils;
import com.fengwo.module_chat.utils.chat_new.FingerprintUtils;
import com.fengwo.module_chat.utils.manager.ChatFunManager;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.RandomContentModel;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_websocket.EventConstant;
import com.fengwo.module_websocket.FWWebSocket1;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.Collections;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java8.util.stream.StreamSupport;

public class BaseChatSocketPresenter<V extends IBaseChatView> extends BaseChatPresenter<V> {

    @Autowired
    UserProviderService userService;
    private FWWebSocket1 webSocket;
    private ChatGreenDaoHelper daoHelper;
    private String talkName;    // 单聊或群聊对象的昵称
    private String talkAvatar;  // 单聊或群聊对象的头像
    // toId>800000000 时 表示在 群 里聊天
    private String uid, toId;
    public boolean isGroup = false;
    public int switchStatus = 1;//是否允许私信 1 允许私信 0 不允许
    public MutableLiveData<List<ChatMsgEntity>> messages = new MutableLiveData<>(); // 显示数据列表

    public MutableLiveData<ChatMsgEntity> myMessage = new MutableLiveData<>(); // 显示自己发的数据

    public BaseChatSocketPresenter() {
        ARouter.getInstance().inject(this);
        webSocket = FWWebSocket1.getInstance();
        daoHelper = ChatGreenDaoHelper.getInstance();

    }

    /**
     * 加入群聊(暂不需要)
     */
    public void joinGroup(int groupId) {
        toId = String.valueOf(groupId);
        try {
            JSONObject clientpara = new JSONObject();
            clientpara.put("eventId", EventConstant.joinGroup + "");
            clientpara.put("uid", "" + uid);
            clientpara.put("groupId", toId);
            webSocket.sendTextMessage(clientpara.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加入群聊
     */
    public void enterGroup(String groupId) {
        addNet(service.enterGroup(groupId).compose(io_main()).compose(handleResult())
                .subscribeWith(new LoadingObserver<EnterGroupModel>() {
                    @Override
                    public void _onNext(EnterGroupModel data) {
                        getView().enterGroupSuccess(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        L.e("msg: " + msg);
                        getView().toastTip(msg);
                        getView().enterGroupFail();
                    }
                }));
    }

    /**
     * 离开群聊(待修改)
     */
    public void leaveGroup(int groupId) {
        try {
            JSONObject clientpara = new JSONObject();
            clientpara.put("eventId", EventConstant.joinGroup + "");
            clientpara.put("uid", "");
            clientpara.put("groupId", groupId);
            webSocket.sendTextMessage(clientpara.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除消息(待修改)
     */
    public void delMsg(String gid) {
        try {

            JSONObject clientpara = new JSONObject();
            clientpara.put("eventId", EventConstant.deleteAChatMessage);
            clientpara.put("fromUid", "" + uid);
            clientpara.put("toUid", "" + toId);
            clientpara.put("guidList", gid);
            webSocket.sendTextMessage(clientpara.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 撤回消息
     */
    @SuppressLint("CheckResult")
    public void revocationMsg(ChatMsgEntity entity) {
        // 构造撤回消息数据
        String message = ChatSendMessageUtils.createRevocationMessage(entity.getFingerPrintOfProtocal(), uid, toId, isGroup, getUserType());
        webSocket.sendTextMessage(message);
        if (!isGroup) {//TODO 私聊撤回 没有回调 / 移动端 数据库自行操作。
            ChatFunManager.withdrawMessage(daoHelper, entity.getFingerPrintOfProtocal(), isGroup, userService.getUserInfo().getNickname());
        }
    }

    private int getUserType() {
        UserInfo userInfo = userService.getUserInfo();
        int userType = 1;
        if (userInfo.myIsCardStatus == 1 || userInfo.wenboAnchorStatus == 1) {
            //如果是主播或者达人等于0
            userType = 0;
        }

        return userType;
    }

    /**
     * 禁言处理
     */
    private boolean Banned() {
        //UserInfo 不能放外面 可能导致信息不同步
        //UserInfo userInfo = userService.getUserInfo();
        if (!isGroup && switchStatus == 0) {
            getView().toastTip("此功能升级中");
            return true;
        }
        if (isBanned(userService.getClosureTime())) {
            getView().toastTip("您已被禁言");
            return true;
        }
        return false;
    }

    /**
     * 发送文本消息
     */
    @SuppressLint("CheckResult")
    public void sendTxtMessage(String message) {
        if (Banned()) return;
        UserInfo userInfo = userService.getUserInfo();
        String fingerPrint = FingerprintUtils.getRandomFingerprint(uid, toId);
        long currentTime = System.currentTimeMillis();
        // 构造服务数据
        String textMessage = ChatSendMessageUtils.createTextMessage(uid, toId, message, currentTime / 1000
                , userInfo.nickname, userInfo.headImg, fingerPrint, isGroup, talkName, talkAvatar, getUserType());
        daoHelper.toId = toId;
        // 构造本地数据
        ChatMsgEntity chatMsgEntity = ChatMsgEntityFactory.buildText(message, userInfo.headImg, fingerPrint
                , currentTime / 1000, isGroup, ChatMsgEntity.SendStatus.sending);
        daoHelper.recentNews = chatMsgEntity;
        // 加入数据库
        addToDatabase(chatMsgEntity, textMessage);
    }

    /**
     * 打招呼 接受方存本地消息
     *
     * @param nickName
     * @param message
     * @param headImg
     */
    public void sendGreetTxtMessage(String nickName, String message, String headImg) {
        if (Banned()) return;
        String fingerPrint = FingerprintUtils.getRandomFingerprint(uid, toId);
        long currentTime = System.currentTimeMillis();
        daoHelper.toId = uid;
        // 构造本地数据
        ChatMsgEntity chatMsgEntity = ChatMsgEntityFactory.buildComeText(nickName, headImg, message,
                currentTime / 1000, fingerPrint, isGroup, ChatMsgEntity.SendStatus.beReceived);
        daoHelper.recentNews = chatMsgEntity;
        // 本地构造系统消息
        ChatMsgEntity chatMsgEntity2 = ChatMsgEntityFactory.createSystemTxtAttentionMsgEntity("你们还不是好友，相互关注后才能聊天。关注对方", System.currentTimeMillis() / 1000 + 1, isGroup);
        // 加入数据库
        addGreetToDatabase(chatMsgEntity, chatMsgEntity2);
    }

    public void buildAttentionTxtMessage() {
        // 本地构造系统消息
        ChatMsgEntity chatMsgEntity2 = ChatMsgEntityFactory.createSystemTxtAttentionMsgEntity("你们还不是好友，相互关注后才能聊天。关注对方", System.currentTimeMillis() / 1000 + 1, isGroup);
        // 加入数据库
        //addToDatabaseNotInsertSession(chatMsgEntity2, null);
        //不存入数据库，存入数据库会照成历史遗留问题。修改为实时显示 2021/01/05
        updateList(chatMsgEntity2);
    }

    public void hasItemInSession(String uid, String talkUserId) {
        if (TextUtils.isEmpty(uid)) {
            getView().toastTip("用户id为空，请重试");
            return;
        }
        addNet(daoHelper.getMessageList(uid).subscribe(new Consumer<List<ChatListItemEntity>>() {
            @Override
            public void accept(List<ChatListItemEntity> chatListItemEntities) throws Exception {
                long count = StreamSupport.stream(chatListItemEntities).filter(e -> e.getTalkUserId() == Integer.parseInt(talkUserId)).count();
                if (count > 0)
                    getView().hasItemInSession(true);
                else
                    getView().hasItemInSession(false);

            }
        }));
    }

    /**
     * 发送语音消息
     */
    public void sendAudio(final String path, final int sound_length) {
        if (Banned()) return;
        File file = new File(path);
        if (!file.exists()) return;
        UserInfo userInfo = userService.getUserInfo();
        // 构造本地数据
        long currentTime = System.currentTimeMillis();
        String fingerPrint = FingerprintUtils.getRandomFingerprint(uid, toId);
        VoiceInfoBean infoBean = new VoiceInfoBean(file.getName(), sound_length).setFileUrl(path);
        ChatMsgEntity chatMsgEntity = ChatMsgEntityFactory.buildVoice(userInfo.headImg, new Gson().toJson(infoBean), fingerPrint
                , currentTime / 1000, isGroup, ChatMsgEntity.SendStatus.sending);
        UploadHelper.getInstance((Context) getView()).doUpload(UploadHelper.TYPE_AUDIOS, file, new UploadHelper.OnUploadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onLoading(long cur, long total) {

            }

            @SuppressLint("CheckResult")
            @Override
            public void onSuccess(String url) {
                try {
                    // 构造服务数据
                    String message = ChatSendMessageUtils.createVoiceMessage(uid, toId, currentTime / 1000, userInfo.nickname, userInfo.headImg, file.getName(), url, sound_length, fingerPrint, isGroup, talkName, talkAvatar, getUserType());
                    daoHelper.recentNews = chatMsgEntity;
                    daoHelper.toId = toId;
                    VoiceInfoBean infoBean = new VoiceInfoBean(file.getName(), sound_length).setFileUrl(url);
                    chatMsgEntity.setText(new Gson().toJson(infoBean));
                    // 加入数据库
                    addToDatabase(chatMsgEntity, message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                errorTip();
                // 加入数据库
                addToDatabase(chatMsgEntity, null);
            }
        });
    }

    /**
     * 发送图片消息
     */
    public void sendImageMessage(final String url, final int imgWidth, final int imgHeight) {
        if (Banned()) return;
        String fingerPrint = FingerprintUtils.getRandomFingerprint(uid, toId);
        long currentTime = System.currentTimeMillis();
        UserInfo userInfo = userService.getUserInfo();
        // 构造本地数据
        ChatMsgEntity chatMsgEntity = ChatMsgEntityFactory.buildImage(userInfo.headImg, url, fingerPrint, currentTime / 1000, isGroup, ChatMsgEntity.SendStatus.sending);
        UploadHelper.getInstance((Context) getView()).doUpload(UploadHelper.TYPE_IMAGE, new File(url), new UploadHelper.OnUploadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onLoading(long cur, long total) {
            }

            @SuppressLint("CheckResult")
            @Override
            public void onSuccess(String url) {
                try {
                    // 构造服务数据
                    String message = ChatSendMessageUtils.createImageMessage(uid, toId, url, currentTime / 1000
                            , userInfo.nickname, userInfo.headImg, fingerPrint, isGroup, talkName, talkAvatar, getUserType());
                    daoHelper.recentNews = chatMsgEntity;
                    daoHelper.toId = toId;
                    chatMsgEntity.setText(url);
                    // 加入数据库
                    addToDatabase(chatMsgEntity, message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                errorTip();
                // 加入数据库
                addToDatabase(chatMsgEntity, null);
            }
        });

    }

    /**
     * 获取聊天列表
     */
    public void getHistoryList(int page) {
        L.e("===page " + page);
        addNet(daoHelper.getChatHistoryList(uid, toId, isGroup, page).subscribeWith(new LoadingObserver<List<ChatMsgEntity>>() {
            @Override
            public void _onNext(List<ChatMsgEntity> data) {
                Collections.reverse(data);
                messages.postValue(data);
            }

            @Override
            public void _onError(String msg) {
                L.e("查询数据库", msg);
            }
        }));
    }

    /**
     * 随机土味情话
     */
    public void sendRandomContent() {
        ChatService wenboApi = new RetrofitUtils().createWenboApi(ChatService.class);
        addNet(wenboApi.getRandomContent().compose(io_main()).subscribeWith(new LoadingObserver<HttpResult<List<RandomContentModel>>>() {
            @Override
            public void _onNext(HttpResult<List<RandomContentModel>> data) {
                if (data.isSuccess()) {
                    try {
                        int pos = (int) (Math.random() * data.data.size());
                        RandomContentModel randomContentModel = data.data.get(pos);
                        getView().sendRandomContent(randomContentModel.getTitle());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    getView().toastTip(data.description);
                }
            }

            @Override
            public void _onError(String msg) {
                getView().toastTip(msg);
            }
        }));
    }

    public void setFromId(String id) {
        uid = id;
    }

    public void attentionSuccess() {
        getView().onAttentionSuccess();
    }

    public void setToId(String id) {
        toId = id;
    }

    public void setTalkName(String name) {
        talkName = name;
    }

    public void setTalkAvatar(String avatar) {
        talkAvatar = avatar;
    }

    public void deleteItems(ChatMsgEntity entity) {
        daoHelper.deleteItem(uid, toId, entity.getFingerPrintOfProtocal()).subscribe(entity1 -> {
            getHistoryList(0);
        });
    }

    public void deleteSystemItem(ChatMsgEntity entity) {
        daoHelper.deleteSystemItem(uid, toId, entity.getMsgType()).subscribe(entity1 -> {
            getHistoryList(0);
        });
    }

    /**
     * 删除聊天记录
     */
    public void deleteChatRecords() {
        daoHelper.deleteChatRecords(uid, toId).subscribe(entity1 -> {
            RxBus.get().post(new RefreshAfterClearEvent(toId));
            getHistoryList(0);
        });
    }

    /**
     * 转发消息
     *
     * @param messageInfo
     * @param talkInfo
     */
    @SuppressLint("CheckResult")
    public void transponds(ChatMsgEntity messageInfo, ChatListItemEntity talkInfo) {
        UserInfo userInfo = userService.getUserInfo();
        String talkUserId = String.valueOf(talkInfo.getTalkUserId());
        ChatMsgEntity chatEntity;
        String fingerPrint = FingerprintUtils.getRandomFingerprint(uid, talkUserId);
        long currentTime = System.currentTimeMillis();
        switch (messageInfo.getMsgType()) {
            case ChatMsgEntity.MsgType.comeText:
            case ChatMsgEntity.MsgType.toText:
                // WebSocket发送消息
                String textMessage = ChatSendMessageUtils.createTextMessage(uid, talkUserId, messageInfo.getText(),
                        currentTime / 1000, userInfo.nickname, userInfo.headImg, fingerPrint, isGroup, talkInfo.getGroupName(), talkInfo.getGroupAvatar(), getUserType());
                // 构造本地数据
                chatEntity = ChatMsgEntityFactory.buildText(messageInfo.getText(), userInfo.headImg, fingerPrint, currentTime / 1000, isGroup, ChatMsgEntity.SendStatus.sending);
                // 加入数据库
                daoHelper.addChatHistoryItem(uid, talkUserId, uid, talkUserId, chatEntity)
                        .flatMap(entity -> daoHelper.insertOrReplaceList(uid, talkUserId,
                                isGroup ? talkInfo.getGroupName() : talkInfo.getUserName(),
                                isGroup ? talkInfo.getGroupAvatar() : talkInfo.getUserAvatar(),
                                isGroup ? talkInfo.getGroupName() : talkInfo.getUserName(),
                                isGroup ? talkInfo.getGroupAvatar() : talkInfo.getUserAvatar(),
                                chatEntity, true, isGroup)).subscribe(o -> {
                    daoHelper.recentNews = chatEntity;
                    webSocket.sendTextMessage(textMessage);
                }, Throwable::printStackTrace);

                break;
            case ChatMsgEntity.MsgType.comeImage:
            case ChatMsgEntity.MsgType.toImage:
                // WebSocket发送消息
                String imageMessage = ChatSendMessageUtils.createImageMessage(uid, talkUserId, messageInfo.getText(), currentTime / 1000
                        , userInfo.nickname, userInfo.headImg, fingerPrint, isGroup, talkInfo.getGroupName(), talkInfo.getGroupAvatar(), getUserType());
                // 构造数据
                chatEntity = ChatMsgEntityFactory.buildImage(userInfo.headImg, messageInfo.getText(), fingerPrint
                        , currentTime / 1000, isGroup, ChatMsgEntity.SendStatus.sendFaild);
                // 加入数据库
                daoHelper.addChatHistoryItem(uid, talkUserId, uid, talkUserId, chatEntity)
                        .flatMap(entity -> daoHelper.insertOrReplaceList(uid, talkUserId,
                                isGroup ? talkInfo.getGroupName() : talkInfo.getUserName(),
                                isGroup ? talkInfo.getGroupAvatar() : talkInfo.getUserAvatar(),
                                isGroup ? talkInfo.getGroupName() : talkInfo.getUserName(),
                                isGroup ? talkInfo.getGroupAvatar() : talkInfo.getUserAvatar(),
                                chatEntity, true, isGroup)).subscribe(o -> {
                    daoHelper.recentNews = chatEntity;
                    webSocket.sendTextMessage(imageMessage);
                }, Throwable::printStackTrace);

                break;
            case ChatMsgEntity.MsgType.comeVoice:
            case ChatMsgEntity.MsgType.toVoice: //todo 语音暂时不能转发
                /**
                 String voiceModel = ChatSendMessageUtils.createTextMessage(uid, talkUserId, "[语音]",
                 currentTime/1000, userInfo.nickname, userInfo.headImg, fingerPrint, isGroup, talkInfo.getGroupName(), talkInfo.getGroupAvatar());
                 // 构造本地数据
                 chatEntity =  ChatMsgEntityFactory.buildText("[语音]", userInfo.headImg, fingerPrint
                 , currentTime / 1000,isGroup,ChatMsgEntity.SendStatus.sendFaild);
                 // 加入数据库
                 daoHelper.addChatHistoryItem(uid, talkUserId, uid, talkUserId, chatEntity)
                 .flatMap(entity -> daoHelper.insertOrReplaceList(uid, talkUserId,
                 isGroup ? talkInfo.getGroupName() : talkInfo.getUserName(),
                 isGroup ? talkInfo.getGroupAvatar() : talkInfo.getUserAvatar(),
                 isGroup ? talkInfo.getGroupName() : talkInfo.getUserName(),
                 isGroup ? talkInfo.getGroupAvatar() : talkInfo.getUserAvatar(),
                 chatEntity, true, isGroup)).subscribe(o -> {
                 }, Throwable::printStackTrace);
                 webSocket.sendTextMessage(voiceModel);
                 */
                break;
            default:
                break;
        }
    }

    /**
     * 添加到数据库
     *
     * @param chatMsgEntity
     */
    @SuppressLint("CheckResult")
    private void addToDatabase(ChatMsgEntity chatMsgEntity, String message) {
        UserInfo userInfo = userService.getUserInfo();
        daoHelper.changeUserAvatar(uid, userInfo.headImg)
                .flatMap((Function<Boolean, ObservableSource<?>>)
                        aBoolean -> daoHelper.addChatHistoryItem(uid, toId, uid, toId, chatMsgEntity))
                .flatMap(o -> daoHelper.insertOrReplaceList(uid, toId, talkName, talkAvatar,
                        talkName, talkAvatar, chatMsgEntity, true, isGroup))
                .observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
            updateList(chatMsgEntity);
            if (message != null) webSocket.sendTextMessage(message);
        }, Throwable::printStackTrace);
    }

    /**
     * 添加到数据库(不插入会话)
     *
     * @param chatMsgEntity
     */
    @SuppressLint("CheckResult")
    private void addToDatabaseNotInsertSession(ChatMsgEntity chatMsgEntity, String message) {
        UserInfo userInfo = userService.getUserInfo();
        daoHelper.changeUserAvatar(uid, userInfo.headImg)
                .flatMap((Function<Boolean, ObservableSource<?>>)
                        aBoolean -> daoHelper.addChatHistoryItem(uid, toId, uid, toId, chatMsgEntity))
                .observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
            updateList(chatMsgEntity);
            if (message != null) webSocket.sendTextMessage(message);
        }, Throwable::printStackTrace);
    }

    @SuppressLint("CheckResult")
    private void addGreetToDatabase(ChatMsgEntity chatMsgEntity, ChatMsgEntity systemMsgEntity) {
        UserInfo userInfo = userService.getUserInfo();
        daoHelper.changeUserAvatar(uid, userInfo.headImg)
                .flatMap((Function<Boolean, ObservableSource<?>>)
                        aBoolean -> daoHelper.addChatHistoryItem(uid, toId, toId, uid, chatMsgEntity))
                .flatMap(o -> daoHelper.insertOrReplaceList(uid, toId, talkName, talkAvatar,
                        talkName, talkAvatar, chatMsgEntity, true, isGroup))
                .flatMap(o -> daoHelper.addChatHistoryItem(uid, toId, toId, uid, systemMsgEntity))
                .observeOn(AndroidSchedulers.mainThread()).subscribe(chatItemEntity -> {
            chatMsgEntity.userId = Integer.parseInt(toId);
            updateList(chatMsgEntity);
            updateList(systemMsgEntity);
        });
    }

    /**
     * 更新UI
     *
     * @param chatMsgEntity
     */
    private void updateList(ChatMsgEntity chatMsgEntity) {
        //postValue 发送一个任务到主线程，可以在子线程中更新数据。多个任务被发送时，只有最后一个会被执行。
        myMessage.setValue(chatMsgEntity);
    }

    /**
     * 是否禁言
     */
    public boolean isBanned(long forbiddenWords) {
        return forbiddenWords > System.currentTimeMillis() ? true : false;
    }

    /**
     * 错误提示
     */
    private void errorTip() {
        if (!CommentUtils.isNetworkConnected((Context) getView())) {
            getView().toastTip("暂无网络，请连接网络后重试！");
        } else {
            getView().toastTip("当前网络环境较差");
        }
    }
}