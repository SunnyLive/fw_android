package com.fengwo.module_flirt.UI.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_chat.utils.chat_new.VoicePlayHelper;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.live.BaseTXLiveActivity;
import com.fengwo.module_comment.bean.JumpInvtationDataBean;
import com.fengwo.module_comment.bean.ReceiveSocketBean;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.utils.SafeHandle;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.utils.SvgaUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.epf.EPlayerView;
import com.fengwo.module_comment.widget.epf.PlayerScaleType;
import com.fengwo.module_comment.widget.epf.filter.AlphaFrameFilter;
import com.fengwo.module_flirt.Interfaces.IChatRoomView;
import com.fengwo.module_flirt.P.ChatRoomPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.bean.CommentWordDto;
import com.fengwo.module_flirt.bean.EnterRoomBean;
import com.fengwo.module_flirt.bean.GiftDto;
import com.fengwo.module_flirt.bean.ImpressDTO;
import com.fengwo.module_flirt.bean.MessageListVO;
import com.fengwo.module_flirt.bean.StartWBBean;
import com.fengwo.module_flirt.dialog.CommentDialogWindow;
import com.fengwo.module_flirt.dialog.GiftCenterPopWindow;
import com.fengwo.module_flirt.dialog.GiftPopWindow;
import com.fengwo.module_flirt.dialog.QuickTalkFlirtPopwindow;
import com.fengwo.module_flirt.manager.WenboMsgManager;
import com.fengwo.module_flirt.utlis.CommonUtils;
import com.fengwo.module_flirt.widget.ChatRoomButtonView;
import com.fengwo.module_flirt.widget.ChatRoomInputView;
import com.fengwo.module_flirt.widget.ChatRoomVideoView;
import com.fengwo.module_flirt.widget.HorizontalBarrageView;
import com.fengwo.module_flirt.widget.ShortcutView;
import com.fengwo.module_flirt.widget.SmallWindowView;
import com.fengwo.module_live_vedio.eventbus.ChatEvaluateEvent;
import com.fengwo.module_live_vedio.eventbus.ShowGiftEvent;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.FWWebSocketWenBo;
import com.fengwo.module_websocket.bean.MsgType;
import com.fengwo.module_websocket.bean.ReceiveCommentMsg;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.StatFateMsg;
import com.fengwo.module_websocket.bean.WebboBulletin;
import com.fengwo.module_websocket.bean.WenboGiftMsg;
import com.fengwo.module_websocket.bean.WenboMsgAction;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 处理 聊天室  聊天信息与/直播信息处理
 *
 * @Author BLCS
 * @Time 2020/4/2 17:59
 */
public abstract class BaseChatRoomActivity extends BaseTXLiveActivity<IChatRoomView, ChatRoomPresenter> implements ChatRoomInputView.OnInputClickListener, ChatRoomVideoView.OnClickListener, SmallWindowView.OnSmallWindowClickListener, IChatRoomView, ChatRoomInputView.RecodeListener {
    @BindView(R2.id.crb_user)
    ChatRoomButtonView crbUser;
    @BindView(R2.id.crv_room)
    protected ChatRoomVideoView crvRoom;
    @BindView(R2.id.sw_window)
    SmallWindowView swWindow;
    @BindView(R2.id.view_gift_msg)
    View viewGiftMsg;
    @BindView(R2.id.fl_chatroom)
    FrameLayout fl_chatroom;

    //礼物漂屏
    @BindView(R2.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R2.id.tv_msg1)
    TextView tvMsg1;
    @BindView(R2.id.iv_gift)
    ImageView ivGift;

    @BindView(R2.id.tv_msg2)
    TextView tvMsg2;
    @BindView(R2.id.playview)
    EPlayerView playview;
    @BindView(R2.id.ll_tips)
    LinearLayout llTips;
    @BindView(R2.id.view_bulletin_msg)
    View mViewBulletinMsg;//告示区飘窗
    @BindView(R2.id.tv_bulletin_name)
    TextView mTvBulletinUserName;

    @BindView(R2.id.iv_bg)
    ImageView ivBg;
    @BindView(R2.id.hbv)
    HorizontalBarrageView hbv;
    @BindView(R2.id.svga_gift)
    SVGAImageView svga_gift;


    public String bgUrl;
    private SimpleExoPlayer simpleExoPlayer;
    private Player.EventListener bigGiftListener;
    public Queue<WenboGiftMsg> bigGiftList = new ConcurrentLinkedQueue<>();//礼物队列
    public long expireTime;
    public int status;
    public int orderNum;
    GiftPopWindow giftPopWindow;

    GiftCenterPopWindow giftCenterPopWindow;

    protected int anchorId;
    private int page = 0;
    protected StartWBBean startWBBean;
    protected String pullStreamUrl;

    AudioManager manager;

    public SafeHandle mHandle;
    public final static int WHAT_SHOW_GIFT = 1;
    private CommonDialog timeTip;
    private CommentDialogWindow forbid;
    private MediaPlayer mRingPlayer;
    private MediaPlayer mRingTipPlayer;
    private QuickTalkFlirtPopwindow rePlayPopWindow;

    public ChatRoomButtonView getCrbUser() {
        return crbUser;
    }

    static SHARE_MEDIA SHARE_TYPE = SHARE_MEDIA.WEIXIN;

    /**
     * 当前角色是否为主播
     *
     * @return
     */
    protected abstract boolean isHost();

    public boolean isEnterFirstGift = false;//用于判断是否第一次进入   第一次进入开启缘分走 点单流程    再续缘分与缘定三生走加时流程

    private VoicePlayHelper mVoicePlayerWrapper;

    protected boolean isPay = false;              // 是否处于缘分状态

    @Override
    protected void initView() {
        super.initView();

        anchorId = getIntent().getIntExtra("anchorId", 0);
        bgUrl = getIntent().getStringExtra("bgUrl");

//        p.setTalk(id,name,);
        crvRoom.addInputClickListener(this);
        crvRoom.addOnClickListener(this);
        swWindow.addClickSwith(this);
        ImageLoader.loadImgs(ivBg, bgUrl);
//        ViewGroup.LayoutParams layoutParams = playview.getLayoutParams();
//        layoutParams.width = ScreenUtils.getScreenWidth(this);
//        layoutParams.height = ScreenUtils.getScreenWidth(this);
//        playview.setLayoutParams(layoutParams);
        initChatList();
        initReceive();

        //语音播放器
        mVoicePlayerWrapper = new VoicePlayHelper(this, String.valueOf(anchorId));
        crvRoom.getEditInput().setVoicePlayerWrapper(mVoicePlayerWrapper);

        giftPopWindow = new GiftPopWindow(this, getSupportFragmentManager(), anchorId);
        giftPopWindow.setIsChatRoom(true, false);
        giftPopWindow.setOnGiftSendListener(new GiftPopWindow.OnGiftSendListener() {
            @Override
            public void onNormalGiftSend(GiftDto gift) {
                if (CommonUtils.showTipMoney(getBaseContext(), gift.price, getSupportFragmentManager()))
                    return;
                L.e("========" + anchorId);
                p.sendNormalGift(anchorId, gift);
            }

            @Override
            public void onTimeGiftSend(GiftDto gift, boolean isFirst) {
                if (CommonUtils.showTipMoney(getBaseContext(), gift.price, getSupportFragmentManager()))
                    return;
                L.e("tag" + anchorId + "onTimeGiftSend=" + isFirst);
                if (crbUser.getVideoTime() == 0) {
                    p.sendOrderTimeGift(anchorId, gift);  //加单
                    if (giftPopWindow != null && giftPopWindow.isShowing())
                        giftPopWindow.dismiss();
                } else {
                    p.sendTimeGift(anchorId, gift);
                }
            }
        });
        giftCenterPopWindow = new GiftCenterPopWindow(this, getSupportFragmentManager(), anchorId);
        giftCenterPopWindow.setOnGiftSendListener((gift, isFirst) -> {
            if (CommonUtils.showTipMoney(getBaseContext(), gift.price, getSupportFragmentManager()))
                return;
            L.e("tag" + anchorId + "onTimeGiftSend=" + isFirst);
            if (crbUser.getVideoTime() == 0) {
                p.sendOrderTimeGift(anchorId, gift);  //加单
                if (giftPopWindow != null && giftPopWindow.isShowing())
                    giftPopWindow.dismiss();
            } else {
                p.sendTimeGift(anchorId, gift);
            }
        });

        mHandle = new SafeHandle(this) {
            @Override
            protected void mHandleMessage(Message msg) {
                super.mHandleMessage(msg);
                switch (msg.what) {
                    case WHAT_SHOW_GIFT:
                        showGift();
                        mHandle.sendEmptyMessageDelayed(WHAT_SHOW_GIFT, 500);
                        break;
                }

            }
        };
        crvRoom.getEditInput().setRecodeListener(this);
    }

    public VoicePlayHelper getVoicePlayerWrapper() {
        return mVoicePlayerWrapper;
    }

    protected boolean isFirstHistory = true;

    @Override
    public void addHistoryMsg(boolean isRefresh, ArrayList<SocketRequest<WenboWsChatDataBean>> records) {
        Collections.reverse(records);
        for (int i = 0; i < records.size(); i++) {//消息状态变成 已接收
            //   records.get(i).sendStatus = SendStatus.beReceived;
            try {
                L.e("======data " + records.get(i).sendStatus + "");
//                L.e("======data " + records.get(i).data.getToUser().getHeadImg());
//                L.e("======data " + records.get(i).data.getFromUser().getHeadImg());
            } catch (NullPointerException e) {

            }

        }
        if (isRefresh) {
            crvRoom.getChatAdapter().setNewData(records); //刷新列表
            //crvRoom.scrollLast(0);//此次判断放到最后一行
        } else {
            crvRoom.getChatAdapter().addData(0, records); //刷新列表
        }

        if (isFirstHistory) {//如果是刚进入聊天室 不是通过下拉刷新获取  显示提示语
            //if (!isHost()) {//主播也要显示
                SocketRequest<WenboWsChatDataBean> tipBean = new SocketRequest<WenboWsChatDataBean>();
                WenboWsChatDataBean data = new WenboWsChatDataBean();
                WenboWsChatDataBean.ContentBean contentBean = new WenboWsChatDataBean.ContentBean();
                contentBean.setValue((String) SPUtils1.get(BaseChatRoomActivity.this, "wenbotip", ""));
                data.setContent(contentBean);
                tipBean.data = data;
                tipBean.msgType = ChatMsgEntity.MsgType.systemText;
                crvRoom.getChatAdapter().addData(tipBean);
                isFirstHistory = false;
            //}
            //开始读取socket message消息
            p.startReadMessage();
        }
        if (isRefresh) {
            //如果刷新，默认滑动到最低
            crvRoom.scrollLast(0);
        }
    }

    @Override
    protected void onDestroy() {
        if (p != null) p.clean();
        super.onDestroy();
        mHandle.removeMessages(WHAT_SHOW_GIFT);
        WenboMsgManager.getInstant().setSocket3(FWWebSocketWenBo.MAX_PING_PONG);
        if(simpleExoPlayer!=null){
            simpleExoPlayer.removeListener(bigGiftListener);
        }
    }


    /**
     * 获取聊天数据
     */
    protected void initChatList() {
    }

    @Override
    public void updateMsgStatus(String msgId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                crvRoom.getChatAdapter().updateMsg(msgId);
            }
        });

    }

    @Override
    public void updateMsgFaild(String msgId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                crvRoom.getChatAdapter().updateMsgFaild(msgId);
            }
        });
    }

    @Override
    protected TXCloudVideoView getPushView() {
        return (TXCloudVideoView) crvRoom.getVideoView();
    }

    /**
     * 接收聊天信息
     *
     * @param chatBean
     */
    @Override
    public void receiveMsg(SocketRequest<WenboWsChatDataBean> chatBean) {
        receiveChatMsg(chatBean);
    }

    protected abstract void receiveChatMsg(SocketRequest<WenboWsChatDataBean> chatBean);

    /**
     * 聊天信息处理
     */
    protected void initReceive() {
        //初始化聊天信息
        p.refreshList.observe(this, chatMsgEntities -> {
            crvRoom.showLast(chatMsgEntities.size() > 20 ? true : false);
            if (page > 0) {
                crvRoom.getChatAdapter().addData(0, chatMsgEntities);
            } else {
                crvRoom.getChatAdapter().setNewData(chatMsgEntities);
                crvRoom.scrollLast(0);
            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_chat_room;
    }

    @Override
    public ChatRoomPresenter initPresenter() {
        return new ChatRoomPresenter();
    }

    @Override
    public void clickPresent() {//点击礼物
        showGiftPop(0);
    }

    public void showGiftPop(int isFirst) {//显示礼物
        if (null != giftPopWindow) {
            giftPopWindow.setBalance(UserManager.getInstance().getUser().balance);
            giftPopWindow.setOnlyGift(false);
            giftPopWindow.toGiftByType(isFirst == 1 ? GiftPopWindow.TYPE_TIME_GIFT : GiftPopWindow.TYPE_NORMAL_GIFT, anchorId);
            giftPopWindow.setIsChatRoom(true, isFirst == 1);
            giftPopWindow.setIsCommend(false);
            giftPopWindow.showPopupWindow();
        }
    }


    public void showCenterGiftPop() {//显示印象礼物
        if (null != giftCenterPopWindow) {
            giftCenterPopWindow.setIsFisrt(orderNum);
            giftCenterPopWindow.showPopupWindow();
        }
    }

    /**
     * 首次进入心动小屋通知
     *
     * @param webboBulletin skt
     *                      (ArrayList<SocketRequest<WenboWsChatDataBean>> records) {
     *                      Collections.reverse(records);
     *                      for (int i = 0; i < records.size(); i++) {//消息状态变成 已接收
     *                      records.get(i).sendStatus = SendStatus.beReceived;
     *                      L.e("======data " + records.get(i).data.getContent().toString());
     *                      L.e("======data " + records.get(i).data.getToUser().getHeadImg());
     *                      L.e("======data " + records.get(i).data.getFromUser().getHeadImg());
     *                      }
     *                      crvRoom.getChatAdapter().addData(0, records);
     */
    @Override
    public void notifyBulletinMsg(WebboBulletin webboBulletin) {
        if (webboBulletin.getAction().equals(WenboMsgAction.SPLASH)) {
            SocketRequest<WenboWsChatDataBean> chatBean = WenboMsgManager.getInstant().sendChatMsgTemporary(p.userId, webboBulletin.getUser().getNickname(),
                    webboBulletin.getUser().getHeadImg(), p.talkId, p.talkName, p.talkAvatar, webboBulletin.getContent().getValue(), webboBulletin.getRoom().getRoomId(),
                    webboBulletin.getContent().getValue(), MsgType.splashMeg, 0);
            crvRoom.getChatAdapter().addData(chatBean);
            crvRoom.scrollLast(0);
            //   showBulletinMsg(webboBulletin.getAnchor().getNickname(), webboBulletin.getAction());
//            p.addSqMsg(wenboWsChatDataBeanSocketRequest);
//
//
////            SocketRequest<WenboWsChatDataBean> wenboWsChatDataBeanSocketRequest = new SocketRequest<>();
////            wenboWsChatDataBeanSocketRequest.msgType = MsgType.splashMeg;
////            wenboWsChatDataBeanSocketRequest.version = webboBulletin.getContent().getValue();

        } else {
            int uid = UserManager.getInstance().getUser().getId();
            String pre = "";
            String end = "";
            String nickName = "";       // 公告的昵称
            String bulletUid = "";      // 公告的UID
            if (webboBulletin.getAction().equals(WenboMsgAction.ENTER_LIVING_ROOM) && UserManager.getInstance().getUser().isWenboRole()) {
                // 收到进入房间消息，并且自己是用户，则不显示该公告
                if (webboBulletin.getUser().getUserId().equals(uid + "")) {
                    return;
                }
                pre = "欢迎";
                end = "来到达人圈";
                nickName = webboBulletin.getUser().getNickname();
                bulletUid = webboBulletin.getUser().getUserId();
            } else {
                // 收到心动小屋消息，并且自己是主播，则不显示该公告
                if (webboBulletin.getAnchor().getUserId().equals(uid + "")) {
                    return;
                }
                pre = "欢迎进入";
                end = "心动小屋";
                nickName = webboBulletin.getAnchor().getNickname();
                bulletUid = webboBulletin.getAnchor().getUserId();
            }
            showBulletinMsg(bulletUid, nickName, pre, end);
        }

    }

    @Override
    public void onItemMsgFor(String msgId, SocketRequest<WenboWsChatDataBean> data) {
        //  WenboMsgManager.getInstant().setItemMsgFor(msgId);
        WenboMsgManager.getInstant().setItemMsgFor(msgId, data);

    }

    /**
     * 首次进入心动小屋提示
     *
     * @param nickName   nickName
     * @param chatUserId 公告的用户ID
     */
    public void showBulletinMsg(String chatUserId, String nickName, String preText, String endText) {
        if (mViewBulletinMsg.getVisibility() == View.VISIBLE) return;
        AssetManager am = getAssets();
        MediaPlayer MP = new MediaPlayer();
        AssetFileDescriptor mAssetFile;
        try {
            mAssetFile = am.openFd("bulletin_msg.mp3");
            MP.setDataSource(mAssetFile.getFileDescriptor(), mAssetFile.getStartOffset(), mAssetFile.getLength());
            MP.setOnPreparedListener(mp -> {
                mViewBulletinMsg.setVisibility(View.VISIBLE);
                mTvBulletinUserName.setTag(chatUserId);
                StringBuilder sb = new StringBuilder();
                sb.append(preText).append("<font color='#FFF282'>&nbsp;&nbsp;").append(nickName).append("&nbsp;&nbsp;</font>").append(endText);
                mTvBulletinUserName.setText(Html.fromHtml(sb.toString()));
                mTvBulletinUserName.setSingleLine(true);//设置单行显示
                mTvBulletinUserName.setHorizontallyScrolling(true);//横向滚动
                mTvBulletinUserName.setMarqueeRepeatLimit(1);
                mTvBulletinUserName.setSelected(true);//开始滚
                mViewBulletinMsg.postDelayed(() -> {
                    mViewBulletinMsg.setVisibility(View.GONE);
                }, 5000);
            });
            MP.prepare();
            MP.start();
        } catch (IOException e) {
            L.e("yang", e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 聊天室中消息的弹幕
     *
     * @param chatData data
     */
    protected void showChatAnimation(SocketRequest<WenboWsChatDataBean> chatData, WenboGiftMsg gift) {
//        if (isHost) {
//            return;
//        }
        hbv.addBarrage(chatData, gift);

        //文本弹幕的点击事件
        hbv.setOnTextBarrageClickListener(new HorizontalBarrageView.OnTextBarrageClickListener() {
            @Override
            public void onTextBarrageClick(SocketRequest<WenboWsChatDataBean> chatData) {
                ShortcutView mShortcutView = new ShortcutView(BaseChatRoomActivity.this, chatData);
                mShortcutView.setOnItemClickListener(new ShortcutView.ShortcutItemClickListener() {
                    @Override
                    public void onItemClick(CommentWordDto data) {
                        if (null != crvRoom.getCurrentChatUser()) {
                            WenboWsChatDataBean.RoomBean mRoom = chatData.data.getRoom();
                            WenboWsChatDataBean.FromUserBean u = chatData.data.getFromUser();
                            p.setRoomInfo(mRoom.getRoomId(), mRoom.getRoomTitle());
                            //    p.setToUserInfo(u.getUserId(), u.getNickname(), u.getHeadImg());
                            boolean isReceiveMsg = u.getUserId().equals(String.valueOf(crvRoom.getCurrentChatUser().getAudienceUid()));
                            p.sendText(data.getTitle(), MsgType.toText, chatData.data.getGears(), isReceiveMsg, String.valueOf(anchorId), chatData.data.getIsGears().equals("1") ? true : false, u);
                        } else {
                            //    p.sendText(data.getTitle(), MsgType.toText, getGears(), true);

                            UserInfo userInfo = UserManager.getInstance().getUser();
                            SocketRequest<WenboWsChatDataBean> chatBean = WenboMsgManager.getInstant().sendChatMsg(String.valueOf(userInfo.id), userInfo.getNickname(), userInfo.getHeadImg(),
                                    chatData.fromUid, chatData.data.getFromUser().getNickname(), chatData.data.getFromUser().getHeadImg(),
                                    data.getTitle(), chatData.data.getRoom().getRoomId(), chatData.data.getRoom().getRoomTitle(),
                                    MsgType.toText, 0, chatData.data.getGears(), "", false, WenboMsgManager.TYPE_TEXT, chatData.data.getRoom().getAnchorId(), chatData.data.getIsGears().equals("1") ? true : false);
                        }
                        //    crvRoom.setVider();
                        Toast t = Toast.makeText(getApplication(), "回复成功", Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    }

                    @Override
                    public boolean onChangeChatUser() {
                        if (isHost()) {
                            crvRoom.changeCurrentChatUser(chatData.data.getFromUser().getUserId());
                            return true;
                        }
                        return false;
                    }
                });
                mShortcutView.showPopupWindow();
            }

            @Override
            public boolean onSwitchChatUser(SocketRequest<WenboWsChatDataBean> chatData) {
                if (isHost()) {
                    crvRoom.changeCurrentChatUser(chatData.data.getFromUser().getUserId());
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 获取用户角色当前的缘分等级
     *
     * @return
     */
    protected String getGears() {
        long expireTime = crbUser.getVideoTime();
        if (isHost()) {
            return String.valueOf(orderNum);
        } else
            return (!isHost() && expireTime > 0) ? String.valueOf(orderNum - 1) : null;
    }


    private void showGuardAnimation(WenboGiftMsg gift) {
        if (viewGiftMsg.getVisibility() == View.VISIBLE) return;
        ImageLoader.loadImg(ivHeader, gift.getUser().getHeadImg());
        ImageLoader.loadImg(ivGift, gift.getGift().getSmallImgPath());
        tvMsg1.setText(gift.getUser().getNickname() + "送出[" + gift.getGift().getGiftName() + "] x1");
        L.e("====礼物 " + gift.getGift().getCharmValue());
        tvMsg2.setText(",魅力值+" + gift.getGift().getCharmValue());
        viewGiftMsg.setVisibility(View.VISIBLE);
        viewGiftMsg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewGiftMsg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float width = ScreenUtils.getScreenWidth(BaseChatRoomActivity.this) - getResources().getDimension(com.fengwo.module_live_vedio.R.dimen.dp_20);
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(viewGiftMsg, "translationX", width, 0 - viewGiftMsg.getWidth());
                animator1.setInterpolator(new LinearInterpolator());
                AnimatorSet annoucementAnimalSet = new AnimatorSet();
                annoucementAnimalSet.setDuration(6000);
                annoucementAnimalSet.addListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        viewGiftMsg.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                annoucementAnimalSet.playSequentially(animator1);
                annoucementAnimalSet.start();
            }
        });
    }


    @Override
    public boolean clickInput() {
        // 点击输入
        if (isHost() && crvRoom.getCurrentChatUser() == null) {
            ToastUtils.showShort(this, "当前没有聊天对象，暂时无法使用聊天功能");
            return true;
        }
        return false;
    }

    @Override
    public void clickEmoticon() {
        // 点击表情
    }

    @Override
    public void clickSend(String content) {
        //点击发送
        p.sendText(content, MsgType.toText, getGears(), String.valueOf(anchorId), isPay);
    }

    @Override
    public void onRecordSuccess(String path, int duration) {//发送语音
        File file = new File(path);
        if (!file.exists()) {
            ToastUtils.showLong(this, "存储出错，请检测存储环境是否正常");
            return;
        }
        p.sendAudioMessage(file, duration, getGears(), String.valueOf(anchorId), isPay);
    }

    @Override
    public void addLoadHistory() {//下拉刷新

    }

    @Override
    public void showAddGift() {//显示加时礼物
        if (crvRoom.getGiftBtn().equals("去评价")) {
            RxBus.get().post(new ChatEvaluateEvent(ShowGiftEvent.GIFT_TYPE_TOUTIAO));
            //
        } else {
            showCenterGiftPop();
        }
    }

    @Override
    public void onRevoke(String msgId) {
        p.sendText("您撤回了一条消息", MsgType.toRevocation, getGears(), msgId, false, String.valueOf(anchorId), isPay);
    }

    @Override
    public void onResume() {
        super.onResume();
        WenboMsgManager.getInstant().setSocket3(FWWebSocketWenBo.MIN_PING_PONG);
    }

    //    /**
//     * 显示结束评价弹窗
//     */
//    public void showFinishChatPopwindow() {
//
//        if (popwindow != null && popwindow.isShowing()) return;
//        popwindow = new FinishChatPopwindow(this, anchorId, roomId, livingRoomUserId, userProviderService);
//        popwindow.addOnClickListener(new FinishChatPopwindow.OnSureListener() {
//            @Override
//            public void sure() {
//                if (Integer.parseInt(crbUser.getVideoTime()) < 1) {
//                    finish();
//                }else {
//                    popwindow.dismiss();
//                }
//
//            }
//
//            @Override
//            public void onSendGift() {
//                giftPopWindow.setOnlyGift(true);
//                giftPopWindow.setIsCommend(true);
//                giftPopWindow.setIsChatRoom(false, false);
//                giftPopWindow.showPopupWindow();
//            }
//
//            @Override
//            public void onFail() {
//                if (Integer.parseInt(crbUser.getVideoTime()) < 1) {
//                    finish();
//                }
//            }
//        });
//        popwindow.showPopupWindow();
//        //      releaseCountTime();
//    }
    @Override
    public void clickDown() {
        crvRoom.hideChatView(true);
        if (swWindow.getVisibility() == View.VISIBLE) {
            enlarge();
            swWindow.setVisibility(View.GONE);
        }

        //提示上滑显示
        boolean aBoolean = SPUtils.getInstance().getBoolean("TIP_ROOM", false);
        if (!aBoolean) {
            SPUtils.getInstance().put("TIP_ROOM", true);
            llTips.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void clickAddTime() {
        showCenterGiftPop();
    }

    @Override
    public void clickSwitch() {
        enlarge();
        //显示接收数据
        crvRoom.scrollLast(0);
    }

    /*放大*/
    private void enlarge() {
        crvRoom.setSpaceweight(10);
        crvRoom.showVideoView();
        crbUser.setVideoScaleVisibility(true);
        KLog.e("tag", "enlarge == 开始播放视频");
        startPlay(pullStreamUrl, crvRoom.getVideoView(), null);
    }

    @Override
    public void clickHide() {
        crbUser.setBtnShowSmallVisibility(true);
    }

    @Override
    public void hostEnd(HttpResult result, JumpInvtationDataBean jumpInvtationDataBean) {
    }

    @Override
    public void enterRoomSuccess(EnterRoomBean data) {
    }

    @Override
    public void enterRoomFailure(String meg) {
    }

    @Override
    public void quitRoomSuccess(HttpResult data, boolean isShow) {
    }


    @Override
    public void quitRoomFailure(String msg) {
    }

    @Override
    public void toastBanned(ReceiveSocketBean bean) {
        stopRTMPPush();
        //主播被封  收到通知 到这边处理
        if (forbid != null && forbid.isShowing()) forbid.dismiss();
        forbid = new CommentDialogWindow(this, false, "知道了", bean.getContent().getValue());
        forbid.addOnClickListener(new CommentDialogWindow.OnSureClickListener() {
            @Override
            public void click() {
                p.rejectReconnect();
                finish();
            }
        });
        forbid.showPopupWindow();
    }

    @Override
    public void addTimeTip(String msg) {
        if (timeTip != null && timeTip.isAdded()) timeTip.dismiss();
        timeTip = CommonDialog.getInstance(msg, true, "知道了", "").addOnDialogListener(new CommonDialog.OnDialogListener() {
            @Override
            public void cancel() {
            }

            @Override
            public void sure() {
            }
        });
        timeTip.show(getSupportFragmentManager(), "addTimeTip");
    }

    //       addChatMsgGift("你赠送"+crbUser.getName()+"一个"+gift.getGift().getGiftName()+"，开启缘分~");


    private void showGift() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (bigGiftList) {
                    if (null == playview || playview.getVisibility() == View.VISIBLE)
                        return;
                    if (viewGiftMsg != null && viewGiftMsg.getVisibility() == View.VISIBLE) return;
                    if (null == simpleExoPlayer) {
                        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext());
                        bigGiftListener = new Player.EventListener() {

                            @Override
                            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                                L.e("=======" + playbackState);
                                switch (playbackState) {
                                    case ExoPlayer.STATE_READY:
                                        playview.setVisibility(View.VISIBLE);
                                        break;
                                    case ExoPlayer.STATE_ENDED:
                                    case ExoPlayer.STATE_IDLE:
                                        playview.setVisibility(View.GONE);
                                        break;
                                    default:
                                        L.e("aaaaaaaaaaaaaaaaonononon" + playbackState);
                                        break;
                                }
                            }


                            @Override
                            public void onPlayerError(ExoPlaybackException error) {
                                L.e("aaaaaaaaaaaaaaaaononoonPlayerError");
                            }
                        };
                        simpleExoPlayer.addListener(bigGiftListener);
                        playview.setSimpleExoPlayer(simpleExoPlayer);
                        playview.setGlFilter(new AlphaFrameFilter());
                    }
                    WenboGiftMsg gift;
                    if (bigGiftList.size() > 0) {
                        gift = bigGiftList.poll();
                    } else {
                        return;
                    }
                    if (playview.getVisibility() == View.VISIBLE || svga_gift.getVisibility() == View.VISIBLE && svga_gift.isAnimating()) {
                        return;
                    }
                    String msg = gift.getGift().getBigImgPath().substring(gift.getGift().getBigImgPath().length() - 4, gift.getGift().getBigImgPath().length());
                    if (msg.equals("svga")) {
                        SvgaUtils svgaUtils1 = new SvgaUtils(BaseChatRoomActivity.this, svga_gift);
                        svgaUtils1.initAnimator(new SVGACallback() {
                            @Override
                            public void onPause() {

                            }

                            @Override
                            public void onFinished() {
                                if (svga_gift != null) {
                                    playview.setVisibility(View.GONE);
                                }

                            }

                            @Override
                            public void onRepeat() {

                            }

                            @Override
                            public void onStep(int i, double v) {

                            }
                        });
                        int start = gift.getGift().getBigImgPath().lastIndexOf("/");
                        int end = gift.getGift().getBigImgPath().lastIndexOf(".svga");
                        String name = gift.getGift().getBigImgPath().substring(start + 1, end);
                        svgaUtils1.getFile(name, gift.getGift().getBigImgPath());
                        svga_gift.setVisibility(View.VISIBLE);
                        return;
                    }
                    //   showGuardAnimation(gift); //礼物飘窗
                    String path = gift.getGift().getBigImgPath();
                    int length = path.length();
                    int startIndex = path.lastIndexOf("/") + 1;
                    String giftFileName = path.substring(startIndex, length);
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/fwhynew/" + giftFileName.split("\\.")[0]);
                    if (file.exists()) {
                        playview.setPlayerScaleType(PlayerScaleType.RESIZE_FIT_CENTER);
                        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(BaseChatRoomActivity.this, Util.getUserAgent(BaseChatRoomActivity.this, getPackageName()));
                        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.fromFile(file));
                        simpleExoPlayer.prepare(mediaSource);
                        simpleExoPlayer.setPlayWhenReady(true);
                    }
                }
            }
        });

    }

    /**
     * 播放收到订单铃声
     */
    public void startRingTip() {
        if (mRingTipPlayer != null) {
            mRingTipPlayer.stop();
            mRingTipPlayer.release();
            mRingTipPlayer = null;
        }
        mRingTipPlayer = MediaPlayer.create(this, R.raw.voice_order);
        mRingTipPlayer.setLooping(false);
        mRingTipPlayer.start();
    }

    /**
     * 停止铃声
     */
    public void stopRingTip() {
        if (mRingTipPlayer != null) {
            mRingTipPlayer.stop();
            mRingTipPlayer.release();
            mRingTipPlayer = null;
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public boolean clickVoice(boolean istype) {
        if (isHost() && crvRoom.getCurrentChatUser() == null) {
            ToastUtils.showShort(this, "当前没有聊天对象，暂时无法使用聊天功能");
            return true;
        }

        if (crbUser.getVideoTime() <= 1 && !isHost()) {
            //   crvRoom.getEditInput().setChoice();
            return false;
        }
        if (istype) isEnterFirstGift = false;
        if (isEnterFirstGift) {//显示  TODO 快捷回复
//            if (rePlayPopWindow == null) {
//                rePlayPopWindow = new QuickTalkFlirtPopwindow(this);
//                rePlayPopWindow.setOnQuickTalkClickListener(new QuickTalkFlirtPopwindow.OnQuickTalkClickListener() {
//                    @Override
//                    public void onClick(String content) {
//                        p.sendText(content, MsgType.toText);
//                    }
//                });
//            }
//            rePlayPopWindow.showPopupWindow();
            crvRoom.getEditInput().setVoice();
        } else {
            new RxPermissions(this)
                    .request(Manifest.permission.RECORD_AUDIO)
                    //.compose(bindToLifecycle()) RxPermissions的bug，如果加上这句，就不会提示了
                    .subscribe(grant -> {
                        if (grant) {
                            crvRoom.getEditInput().setVoice();
                        } else {
                            Toast.makeText(this, "您关闭了权限，请去设置页面开启", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        return false;
    }

//    /**
//     * 播放等待铃声
//     */
//    public void startRing() {
//        if (mRingPlayer != null) {
//            mRingPlayer.stop();
//            mRingPlayer.release();
//            mRingPlayer = null;
//        }
//        mRingPlayer = MediaPlayer.create(this, R.raw.voice_waiting);
//        mRingPlayer.setLooping(true);
//        mRingPlayer.start();
//    }

    /**
     * 停止铃声
     */
    public void stopRing() {
        if (mRingPlayer != null) {
            mRingPlayer.stop();
            mRingPlayer.release();
            mRingPlayer = null;
        }
    }

    @Override
    public void showKeyboard(boolean show, int we) {
        crvRoom.scrollLast(we);
    }

    @Override
    public void onGetChatUsers(List<MessageListVO> data) {

    }

    /**
     * 有用户进入直播间
     *
     * @param bulletin
     */
    @Override
    public void onUserEnterRoom(WebboBulletin bulletin) {

    }

    /**
     * 有用户推出直播间
     *
     * @param bulletin
     */
    @Override
    public void onUserExitRoom(WebboBulletin bulletin) {

    }

    /**
     * 接受到缘分礼物
     *
     * @param statFate
     */
    @Override
    public void onStatFate(StatFateMsg statFate) {

    }

    @Override
    public void onGetImpress(ImpressDTO impressDTO) {

    }

    @Override
    public void onReceiveComment(ReceiveCommentMsg receiveCommentMsg) {

    }

    @Override
    public void onVoiceFailed(String msg) {
        ToastUtils.showShort(getApplicationContext(), "语音内容涉嫌违规，请文明发言");
    }
}
