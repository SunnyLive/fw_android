package com.fengwo.module_flirt.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.alibaba.security.realidentity.build.Ia;
import com.fengwo.module_comment.base.BeautyDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.JumpInvtationDataBean;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_comment.event.GameEvent;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.Common;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.adapter.ChatUserAdapter;
import com.fengwo.module_flirt.bean.FlirtCommentBean;
import com.fengwo.module_flirt.bean.GetAnchorRoomInfo;
import com.fengwo.module_flirt.bean.ImpressDTO;
import com.fengwo.module_flirt.bean.MessageListVO;
import com.fengwo.module_flirt.bean.ShareInfoBean;
import com.fengwo.module_flirt.bean.StartWBBean;
import com.fengwo.module_flirt.dialog.CloseFlirtPushPop;
import com.fengwo.module_flirt.dialog.FlirtCommentPopWindow;
import com.fengwo.module_flirt.dialog.ILiaoShareCodeDialog;
import com.fengwo.module_flirt.dialog.MoerPopwindow;
import com.fengwo.module_flirt.dialog.ReceiveCommentPopWindow;
import com.fengwo.module_flirt.manager.ChatHistroySQLHelper;
import com.fengwo.module_flirt.manager.WenboMsgManager;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.bean.MsgType;
import com.fengwo.module_websocket.bean.ReceiveCommentMsg;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.StatFateMsg;
import com.fengwo.module_websocket.bean.WebboBulletin;
import com.fengwo.module_websocket.bean.WenboGiftMsg;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/29
 */
public class HostChatRoomActivity extends BaseChatRoomButtonActivity implements ChatUserAdapter.OnChatUserChangeListener {


    private CountBackUtils gameCountBackUtils;
    private CountBackUtils countBackUtils;
    private int gears;      // 缘分等级 0 开启缘分 1 再续前缘 2 缘定三生
    private boolean hasCommented;     // 是否已经评论

    /*当前是否有聊天对象*/
    private boolean hasChatUser;
    private FlirtCommentPopWindow mCommentPopWindow;        // 评论对话框
    @Autowired
    UserProviderService userProviderService;

    public static void start(Context context, StartWBBean startWBBean) {
        Intent intent = new Intent(context, HostChatRoomActivity.class);
        intent.putExtra("data", startWBBean);
        if (null != UserManager.getInstance().getUser()) {
            intent.putExtra("anchorId", UserManager.getInstance().getUser().getId());
            context.startActivity(intent);
        }else {
            intent.putExtra("anchorId", startWBBean.anchorId);
            context.startActivity(intent);
        }

    }


    @Override
    protected BeautyDto getBeautyDto() {
        return gson.fromJson((String) SPUtils1.get(this, "beauty", gson.toJson(new BeautyDto())), BeautyDto.class);
    }

    @Override
    protected boolean getMirrir() {
        return (boolean) SPUtils1.get(this, "isMirror", false);
    }


    @Override
    protected boolean isHost() {
        return true;
    }

    @Override
    protected void initView() {
        super.initView();
        ARouter.getInstance().inject(this);
        /*记录游戏*/
        gameCountBackUtils = new CountBackUtils();
        receiveNotice();

//        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
//        audioManager.setMicrophoneMute(true);//关闭麦克风
        startWBBean = (StartWBBean) getIntent().getSerializableExtra("data");
        startRtmpPush(startWBBean.streamPush);
        crbUser.setName(startWBBean.nickname);
        crbUser.setHeader(startWBBean.headImg);
        crbUser.setAttentionVisibility(false);
        crbUser.setVideoScaleVisibility(false);
        crbUser.setVideoTimeVisibility(false);
        crbUser.setGiftValueVisible(true);
        crbUser.setVideoTime(0);
        // crbUser.setCommentViewVisible(false);
        //显示聊天会话
        crvRoom.setShowChat(true);
        // 显示收到评论

        crvRoom.setOnChatUserChangeListener(this);
        //进入直播间获取当前正在观看的用户
        p.getChatUsers();
        p.setRoomInfo(startWBBean.roomId, startWBBean.roomTile);

        //默认先隐藏右侧的聊天用户列表
        crvRoom.showChatUsers(null);

        mTvBulletinUserName.setOnClickListener((v) -> {
            String chatUserId = (String) v.getTag();
            if (!TextUtils.isEmpty(chatUserId)) {
                crvRoom.changeCurrentChatUser(chatUserId);
            }
        });

        crvRoom.setOnNotificationListener((messageListVO, time) -> {
            if (time == Common.BULLETIN_180) {
                showBulletinMsg(messageListVO.getAudienceUid() + "",
                        messageListVO.getAudienceNickname(), "你与", "的缘分即将结束");
            } else if (time == Common.BULLETIN_360) {
                showBulletinMsg(messageListVO.getAudienceUid() + "",
                        messageListVO.getAudienceNickname(), "", "即将进行结束评价");
            }
        });

        //开始读取socket message消息
        p.startReadMessage();
    }

    /**
     * 接收到通知
     */
    private void receiveNotice() {
        /*收到 游戏通知  筛子  猜拳 */
        RxBus.get().toObservable(GameEvent.class).compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (event.getType() != GameEvent.RECEIVE) {
                        p.sendGameMsg(event, null);
                    }
                    if (!gameCountBackUtils.isTiming()) {
                        gameCountBackUtils.countBack(6, new CountBackUtils.Callback() {
                            @Override
                            public void countBacking(long time) {
                                crvRoom.getChatAdapter().notifyDataSetChanged();
                            }

                            @Override
                            public void finish() {
                            }
                        });
                    } else {
                        gameCountBackUtils.updateTime(6);
                    }
                });


        //跳转到i撩直播间 （sb需求 开这播 还能去看直播 平生第一次遇到这种需求）
        RxBus.get().toObservable(JumpInvtationDataBean.class)
                .compose(bindToLifecycle())
                .subscribe(dataBean -> {
                    p.pushEnd(dataBean);
                });
    }

    @Override
    protected void receiveChatMsg(SocketRequest<WenboWsChatDataBean> chatBean) {
        //如果当前没有聊天对象
        if (crvRoom.getCurrentChatUser() == null) return;

        WenboWsChatDataBean.FromUserBean fromUser = chatBean.data.getFromUser();
        //在主播间给主播发消息
        //anchorId 点前主播id
        //mUserId  当前用户id
        //From 表示发送方
        //to   表示接收方
        if (chatBean.msgType == MsgType.comeRevocation) {//消息撤回
            crvRoom.comeRevoke(chatBean.data.getContent().getWithdrawId());
        } else {
            String mUserId = String.valueOf(UserManager.getInstance().getUser().getId());
            int currentAudienceUid = crvRoom.getCurrentChatUser().getAudienceUid();
            //如果消息发给我，且发送消息的用户不是当前聊天对象--《弹幕》
            if (null != chatBean.toUid && chatBean.toUid.equals(mUserId)
                    && !fromUser.getUserId().equals(String.valueOf(currentAudienceUid))) {
                showChatAnimation(chatBean, null);
                //更新消息未读数
                crvRoom.refreshMsgUnReadCount(fromUser.getUserId());
            } else {
                //当前正在和该用户聊天，直接清除消息未读数
                ChatHistroySQLHelper.getInstance().clearUnReadMsgCount(currentAudienceUid);
                crvRoom.getChatAdapter().addData(chatBean);
                //显示接收数据
                crvRoom.scrollLast(0);
            }
        }
    }

    /**
     * 接收到缘分礼物
     *
     * @param statFate
     */
    @Override
    public void onStatFate(StatFateMsg statFate) {
        crvRoom.onStatFate(statFate);

        //如果赠送礼物的用户不是当前聊天对象则飘出弹幕
        MessageListVO currentChatUser = crvRoom.getCurrentChatUser();
        if (currentChatUser == null || statFate.getUser().getUserId().equals(String.valueOf(currentChatUser.getAudienceUid()))) {
            // todo
        }

        // 如果是当前真正聊天的用户收到开启缘分通知，需要更新缘分信息
        if (currentChatUser != null && statFate.getUser().getUserId()
                .equals(currentChatUser.getAudienceUid() + "")) {
            this.gears = statFate.getGears();
            p.getImpressionValue(currentChatUser.getAudienceUid());
        }
    }

    @Override
    public void clickHeader() {

    }

    @Override
    public void clickfinish() {
        CloseFlirtPushPop closeLivePushPop = new CloseFlirtPushPop(this, p);
        closeLivePushPop.showPopupWindow();

    }

    @Override
    public void showMoer() {
        MoerPopwindow moerPopwindow = new MoerPopwindow(this);
        moerPopwindow.setOnItemClickListener(new MoerPopwindow.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {//分享
                    ILiaoShareCodeDialog dialog = new ILiaoShareCodeDialog();
                    dialog.setOnItemClickListener(new ILiaoShareCodeDialog.OnItemClickListener() {
                        @Override
                        public void WeiXinShare() {
                            SHARE_TYPE = SHARE_MEDIA.WEIXIN;
                            p.getShareInfo(anchorId);
                        }

                        @Override
                        public void WeiXinCircleShare() {
                            SHARE_TYPE = SHARE_MEDIA.WEIXIN_CIRCLE;
                            p.getShareInfo(anchorId);
                        }
                    });
                    getSupportFragmentManager().beginTransaction().add(dialog, "iliaoshare").commitAllowingStateLoss();
                } else {//清屏
                    // 点击输入
                    if (crvRoom.getCurrentChatUser() == null) {
//                        ToastUtils.showShort(HostChatRoomActivity.this, "当前没有聊天对象，暂时无法使用聊天功能");
                    } else {
                        if (null != UserManager.getInstance().getUser()) {
                            p.delData(0, UserManager.getInstance().getUser().getId(), anchorId);
                        }
                        crvRoom.setScreen();
                    }
                }
                moerPopwindow.dismiss();
            }

            @Override
            public void isDismiss() {

            }
        });
        moerPopwindow.showPopupWindow();
    }

    @Override
    public void onShowOpenBtn(boolean isShow) {

    }

    @Override
    public void getShareInfoSuccess(ShareInfoBean data) {
        UMWeb web = new UMWeb(data.getShareUrl());
        web.setTitle(data.getShareTitle());
        web.setDescription(data.getShareContent());
        if (!TextUtils.isEmpty(data.getShareImg()))
            web.setThumb(new UMImage(this, data.getShareImg()));
        else web.setThumb(new UMImage(this, userProviderService.getUserInfo().headImg));
        L.e("=====]SHARE_TYPE " + SHARE_TYPE);
        new ShareAction(this)
                .setPlatform(SHARE_TYPE)//传入平台
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        toastTip("分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        toastTip("分享失败");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                })//回调监听器
                .share();
    }

    @Override
    public void quitRoomSuccessOut() {

    }

    @Override
    public void hostEnd(HttpResult result, JumpInvtationDataBean jumpInvtationDataBean) {
        if (null != jumpInvtationDataBean) {
            ARouter.getInstance().build(ArouterApi.CHAT_ROOM_ACTION)
                    .withInt("anchorId", Integer.valueOf(jumpInvtationDataBean.getRoomId()))
                    .withInt("status", 1)
                    .withString("bgUrl", jumpInvtationDataBean.getPic())
                    .navigation();
        }

        finish();
    }

    @Override
    public void setMyOrderList(List<MyOrderDto> data) {

    }

    @Override
    public void getAnchorRoomInfoSuccess(GetAnchorRoomInfo data) {

    }

    @Override
    public void getAnchorRoomInfoFailure(String description) {

    }


    @Override
    public void isWaitStatus(String msg) {

    }


    /**
     * 设置右侧聊天的用户列表
     *
     * @param users
     */
    @Override
    public void onGetChatUsers(List<MessageListVO> users) {
        crvRoom.showChatUsers(users);
        if (null != users && users.size() == 0) {
            crbUser.setLastVideoTimeVisibility(false);
        }

    }

    /**
     * 有用户进入直播间
     *
     * @param bulletin
     */
    @Override
    public void onUserEnterRoom(WebboBulletin bulletin) {
        if (p != null) {
            p.getChatUsers();
        }
        notifyBulletinMsg(bulletin);
    }

    /**
     * 有用户退出直播间
     *
     * @param bulletin
     */
    @Override
    public void onUserExitRoom(WebboBulletin bulletin) {
        if (p != null) {
            p.getChatUsers();
        }
//         如果退出房间的用户是当前正在聊天的用户，清除印象值或倒计时
        MessageListVO mvo = crvRoom.getCurrentChatUser();
        if (mvo != null && bulletin.getUser().getUserId().equals(mvo.getAudienceUid() + "")) {
            crbUser.resetVideoTime(0);
            if (countBackUtils != null) {
                countBackUtils.destory();
                countBackUtils = null;
            }
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void addLoadHistory() {
        if (p != null) {
            p.getHistoryList(false, UserManager.getInstance().getUser().getId(), crvRoom.getCurrentChatUser().getAudienceUid());
        }
    }

    @Override
    public void onBackPressed() {
        clickfinish();
    }

    /**
     * 切换当前聊天对象
     *
     * @param user
     */
    @Override
    public void onChatUserChange(MessageListVO user) {
        if (user == null) {
            hasChatUser = false;
            hasCommented = false;
//            crvRoom.setShowChat(false);
            crbUser.resetVideoTime(0);
            isPay = false;
        } else {
            orderNum = user.getGears();
            //和用户聊天时清除消息未读数
            ChatHistroySQLHelper.getInstance().clearUnReadMsgCount(user.getAudienceUid());
            hasChatUser = true;
            isPay = user.isIsPay();
//            crvRoom.setShowChat(true);
//            isFirstHistory = true;
            //设置聊天对象
            p.setToUserInfo(String.valueOf(user.getAudienceUid()), user.getAudienceNickname(), user.getAudienceHeadImg());
            //拉取数据库数据
            p.getHistoryList(true, anchorId, user.getAudienceUid());
            // 获取用户印象值
            p.getImpressionValue(user.getAudienceUid());
            crbUser.setGiftValue(0);    // 用户变化，清空礼物总价
            hasCommented = false;
        }
    }

    @Override
    public void onGetImpress(ImpressDTO impressDTO) {
        if (impressDTO == null) return;
        // 是否开启缘分, 已开启。显示当前缘分，未开启，显示上次开启缘分
        if (impressDTO.isIsOrder()) {
            crbUser.setLastVideoTimeVisibility(false);
            crbUser.setVideoTimeVisibility(true);
            int sec = impressDTO.getExpireTime() / 1000;
            crbUser.resetVideoTime(sec);
            showImpressionTime(sec, impressDTO.getOrderTime() / 1000);
            crbUser.setGiftValue(impressDTO.getGiftPrice());
        } else {
            crbUser.setVideoTimeVisibility(false);
            ImpressDTO.LastOrderDTO lastOrderDTO = impressDTO.getLastOrder();
            if (lastOrderDTO != null && lastOrderDTO.isHasLastOrder()) {
                crbUser.setLastVideoTimeVisibility(true);
                crbUser.setLastDuration(lastOrderDTO.getLastDuration());
                crbUser.setLastTime(lastOrderDTO.getLastCreateTime());
                crbUser.setLastGiftPrice(String.valueOf(lastOrderDTO.getLastGiftPrice()));
            } else {
                crbUser.setLastVideoTimeVisibility(false);
            }
        }
    }

    /**
     * 显示印象值、缘分值
     *
     * @param expireTime 剩余时间
     * @param orderTime  订单总时间
     */
    private void showImpressionTime(int expireTime, int orderTime) {
        //倒计时
        if (countBackUtils == null) {
            countBackUtils = new CountBackUtils();
        }
        if (countBackUtils.isTiming()) {
            countBackUtils.destory();
        }
        countBackUtils.countBack(expireTime, new CountBackUtils.Callback() {
            @Override
            public void countBacking(long time) {
                if (expireTime == 3) {//设置印象值为1
                    crbUser.setVideoTime(1);
                } else {
                    crbUser.setVideoTime(time); //Todo 设置印象值
                    crbUser.setExpireTime(TimeUtils.long2String(orderTime - (int) time));
                }

                if (gears == Common.VIDEO_CHANCE_THIRD) {
                    // 缘定三生, 360s倒计时提示
                    if (time <= Common.BULLETIN_360 && !hasCommented) {
                        if (time == Common.BULLETIN_360) {
                            showBulletin360Msg();
                        }
                        crbUser.beginCountDown(R.drawable.radius_green_alpha_bg, R.string.comment_count_down_tip);
                        crbUser.setCountDownTime(time);
                    }

                } else {
                    // 开启缘分、再续前缘180s倒计时提示
                    if (time <= Common.BULLETIN_180) {
                        if (time == Common.BULLETIN_180) {
                            showBulletin180Msg();
                        }
                        crbUser.beginCountDown(gears == Common.VIDEO_CHANCE_SECOND ? R.drawable.radius_yellow_alpha_bg
                                : R.drawable.radius_blue_alpha_bg, R.string.count_down_tip);
                        crbUser.setCountDownTime(time);
                    }
                }
            }

            @Override
            public void finish() {
                /*倒计时结束*/
                crbUser.endCountDown();
                isPay = false;
            }
        });
    }

    /**
     * 显示180s公告
     */
    private void showBulletin180Msg() {
        showBulletinMsg("你与", "的缘分即将结束");
    }

    /**
     * 显示360s公告
     */
    private void showBulletin360Msg() {
        showBulletinMsg("", "即将进行结束评价");
    }

    /**
     * 显示公告信息
     *
     * @param pre 显示在昵称前面的文字
     * @param end 显示在昵称后面的文字
     */
    private void showBulletinMsg(String pre, String end) {
        MessageListVO messageListVO = crvRoom.getCurrentChatUser();
        if (messageListVO != null) {
            showBulletinMsg(messageListVO.getAudienceUid() + "", messageListVO.getAudienceNickname(), pre, end);
        }
    }

    @Override
    public void addGift(WenboGiftMsg gift, boolean istype) {

        MessageListVO curChatUser = crvRoom.getCurrentChatUser();
        if (curChatUser == null) return;
        WenboGiftMsg.User giftUser = gift.getUser();
        if (giftUser == null || TextUtils.isEmpty(giftUser.getUserId())) return;

        //接受到礼物后，右侧头像下方展示礼物图片5s
        crvRoom.onReceivedGift(gift);
        SocketRequest<WenboWsChatDataBean> chatMsg = null;
        if (istype) {//是否是加时礼物

            if (!TextUtils.isEmpty(gift.getGears() + "")) {

                switch (gift.getGift().getGears()) {
                    case 0:
                        chatMsg = addChatMsgGift("TA赠送达人一个|" + gift.getGift().getGiftName() + "，开启缘分~  ", istype, gift);

                        break;
                    case 1:
                        chatMsg = addChatMsgGift("TA赠送达人一个|" + gift.getGift().getGiftName() + "，再续前缘~  ", istype, gift);

                        break;
                    case 2:
                        chatMsg = addChatMsgGift("TA赠送达人一个|" + gift.getGift().getGiftName() + "，缘定三生~  ", istype, gift);

                        break;
                }
            } else {
                chatMsg = addChatMsgGift("TA赠送达人一个|" + gift.getGift().getGiftName() + "，开启缘分~  ", istype, gift);
            }


        } else {
            chatMsg = addChatMsgGift("TA赠送了你|" + gift.getGift().getGiftName(), istype, gift);
        }
        // 送礼者和当前聊天的是同一个人，赠送礼物总价累加
        if (giftUser.getUserId().equals(curChatUser.getAudienceUid() + "")) {
            if (istype) {
                isPay = true;
            }
            int giftPrice = Integer.parseInt(gift.getGift().getGiftPrice());
            //当前正在和该用户聊天，直接清除消息未读数
            ChatHistroySQLHelper.getInstance().clearUnReadMsgCount(crvRoom.getCurrentChatUser().getAudienceUid());
//            crbUser.setGiftValue(giftPrice);
            crvRoom.getChatAdapter().addData(chatMsg);
            crvRoom.scrollLast(0);
            bigGiftList.add(gift);
            orderNum = gift.getGift().getGears();

        } else {
            showChatAnimation(chatMsg, gift);
        }

        if (!mHandle.hasMessages(WHAT_SHOW_GIFT))
            mHandle.sendEmptyMessage(WHAT_SHOW_GIFT);
    }

    public SocketRequest<WenboWsChatDataBean> addChatMsgGift(String giftName, boolean istype, WenboGiftMsg gift) {
        SocketRequest<WenboWsChatDataBean> chatMsg;
        if (istype) {
            chatMsg = WenboMsgManager.getInstant().sendChatMsg(gift.getUser().getUserId(), gift.getUser().getNickname(),
                    gift.getUser().getHeadImg(), userProviderService.getUserInfo().id + "", userProviderService.getUserInfo().nickname, userProviderService.getUserInfo().headImg,
                    giftName, anchorId + "",
                    giftName, MsgType.giftMeg, 0, getGears(), istype, gift.getGift().getSmallImgPath(), String.valueOf(anchorId), isPay);
        } else {
            chatMsg = WenboMsgManager.getInstant().sendChatMsg(gift.getUser().getUserId(), gift.getUser().getNickname(),
                    gift.getUser().getHeadImg(), userProviderService.getUserInfo().id + "", userProviderService.getUserInfo().nickname, userProviderService.getUserInfo().headImg,
                    giftName, anchorId + "",
                    giftName, MsgType.toGiftMsg, 0, getGears(), istype, gift.getGift().getSmallImgPath(), String.valueOf(anchorId), isPay);
        }
        return chatMsg;
    }

//    @Override
//    public void addGift(WenboGiftMsg gift, boolean istype) {
//        //显示发送的礼物消息
////        p.sendText("您 送了1个" + gift.getGift().getGiftName(), MsgType.systemText);
//        //  toastTip("您 送了1个" + gift.getGift().getGiftName()); isHost() ? "TA赠送" :
//        bigGiftList.add(gift);
//        String name = "你赠送";
//        if (istype) {
//            switch (orderNum) {
//                case 0:
//                    addChatMsgGift(name + "达人一个" + gift.getGift().getGiftName() + "，开启缘分~", istype, gift);
//                    break;
//                case 1:
//                    addChatMsgGift(name + "达人一个" + gift.getGift().getGiftName() + "，再续前缘~", istype, gift);
//                    break;
//                case 2:
//                    addChatMsgGift(name + "达人一个" + gift.getGift().getGiftName() + "，情牵一线，缘定三生~", istype, gift);
//                    break;
//            }
//
//        } else {
//
//            addChatMsgGift(name + "达人一个" + gift.getGift().getGiftName(), istype, gift);
//        }
//
//        if (!mHandle.hasMessages(WHAT_SHOW_GIFT))
//            mHandle.sendEmptyMessage(WHAT_SHOW_GIFT);
//    }

    @Override
    public void showCommentListDialog() {

        mCommentPopWindow = new FlirtCommentPopWindow(this, new FlirtCommentPopWindow.IAddListListener() {
            @Override
            public void clickBank(FlirtCommentBean.RecordsDTO data) {
                mCommentPopWindow.dismiss();
                ReceiveCommentPopWindow rcPopWindow = new ReceiveCommentPopWindow(HostChatRoomActivity.this, data.getId());
                rcPopWindow.setOnDismissListener(new BasePopupWindow.OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        mCommentPopWindow.showPopupWindow();
                    }
                });
                rcPopWindow.showPopupWindow();
            }

        });

        if (!mCommentPopWindow.isShowing()) {
            mCommentPopWindow.showPopupWindow();
        }
    }

    @Override
    public void onReceiveComment(ReceiveCommentMsg receiveCommentMsg) {
        if (receiveCommentMsg == null) return;
        crbUser.setCommentViewVisible(true);
//        showReceiveCommentDialog(receiveCommentMsg);
        MessageListVO mvo = crvRoom.getCurrentChatUser();
        if (mvo != null) {
            // 如果正在聊天的用户已评价，并且此时正处于"缘定三生"的倒计时阶段，则取消该倒计时, 恢复到原来状态
            if (receiveCommentMsg.getUser().getUserId().equals(mvo.getAudienceUid() + "")
                    && gears == Common.VIDEO_CHANCE_THIRD && countBackUtils != null) {
                crbUser.resetVideoTime(countBackUtils.getTime());
                hasCommented = true;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameCountBackUtils != null) {
            gameCountBackUtils.destory();
        }

        if (countBackUtils != null) {
            countBackUtils.destory();
            countBackUtils = null;
        }
        //释放定时器
        crvRoom.release();
    }

}
