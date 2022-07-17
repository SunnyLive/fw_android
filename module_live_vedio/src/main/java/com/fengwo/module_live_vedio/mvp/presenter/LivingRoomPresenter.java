package com.fengwo.module_live_vedio.mvp.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseEachAttention;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.bean.ActBannerBean;
import com.fengwo.module_comment.bean.AnchorWishBean;
import com.fengwo.module_comment.bean.JumpInvtationDataBean;
import com.fengwo.module_comment.bean.PkFloatingScreenBean;
import com.fengwo.module_comment.bean.PkMatchScoreDto;
import com.fengwo.module_comment.bean.UserMedalBean;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.event.ActivityPropMessage;
import com.fengwo.module_comment.event.PaySuccessEvent;
import com.fengwo.module_comment.ext.CharSequenceExtKt;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.SafeHandle;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.api.Constants;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.eventbus.AttentionChangeEvent;
import com.fengwo.module_live_vedio.eventbus.ShowRechargePopEvent;
import com.fengwo.module_live_vedio.helper.LiveNoticeHelper;
import com.fengwo.module_live_vedio.helper.MsgHelper;
import com.fengwo.module_live_vedio.mvp.dto.ActivityDto;
import com.fengwo.module_live_vedio.mvp.dto.ChannelShareInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.CloseLiveDto;
import com.fengwo.module_live_vedio.mvp.dto.CustomLiveEndDto;
import com.fengwo.module_live_vedio.mvp.dto.EndGiftDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomActWsJDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomPkActivityDto;
import com.fengwo.module_live_vedio.mvp.dto.FloatScreenBean;
import com.fengwo.module_live_vedio.mvp.dto.GetActivityInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.GiftBoardcastDto;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;
import com.fengwo.module_live_vedio.mvp.dto.GiftPiaopingDto;
import com.fengwo.module_live_vedio.mvp.dto.GuardListDto;
import com.fengwo.module_live_vedio.mvp.dto.IsTimeToAnimateDto;
import com.fengwo.module_live_vedio.mvp.dto.LastFrameDto;
import com.fengwo.module_live_vedio.mvp.dto.LiveEndMsg;
import com.fengwo.module_live_vedio.mvp.dto.LivingMsgDto;
import com.fengwo.module_live_vedio.mvp.dto.LivingRoomBannerDto;
import com.fengwo.module_live_vedio.mvp.dto.MatchTeamResult;
import com.fengwo.module_live_vedio.mvp.dto.MsgPlaneCompleteDto;
import com.fengwo.module_live_vedio.mvp.dto.MyHourDto;
import com.fengwo.module_live_vedio.mvp.dto.NewActivityDto;
import com.fengwo.module_live_vedio.mvp.dto.PendantDto;
import com.fengwo.module_live_vedio.mvp.dto.PendantListDto;
import com.fengwo.module_live_vedio.mvp.dto.PkResultDto;
import com.fengwo.module_live_vedio.mvp.dto.PkScoreDto;
import com.fengwo.module_live_vedio.mvp.dto.PkTimeDto;
import com.fengwo.module_live_vedio.mvp.dto.PkTypeDto;
import com.fengwo.module_live_vedio.mvp.dto.PlaneAllDto;
import com.fengwo.module_live_vedio.mvp.dto.PopoDto;
import com.fengwo.module_live_vedio.mvp.dto.PunishTimeDto;
import com.fengwo.module_live_vedio.mvp.dto.QuickTalkDto;
import com.fengwo.module_live_vedio.mvp.dto.RechargeDto;
import com.fengwo.module_live_vedio.mvp.dto.RoomMemberChangeMsg;
import com.fengwo.module_live_vedio.mvp.dto.SinglePkResultDto;
import com.fengwo.module_live_vedio.mvp.dto.StickersDto;
import com.fengwo.module_live_vedio.mvp.dto.TeamPkItemResultDto;
import com.fengwo.module_live_vedio.mvp.dto.TeamPkResultDto;
import com.fengwo.module_live_vedio.mvp.dto.UserBoxDto;
import com.fengwo.module_live_vedio.mvp.dto.WatcherDto;
import com.fengwo.module_live_vedio.mvp.ui.iview.ILivingRoom;
import com.fengwo.module_live_vedio.widget.giftlayout.bean.GiftBean;
import com.fengwo.module_websocket.EventConstant;
import com.fengwo.module_websocket.FWWebSocket1;
import com.fengwo.module_websocket.ScoketMessageCreator;
import com.fengwo.module_websocket.bean.InvitePkMsg;
import com.fengwo.module_websocket.bean.LivePendantMsg;
import com.fengwo.module_websocket.bean.LivingRoomGiftBoardcastMsg;
import com.fengwo.module_websocket.bean.LivingRoomGiftMsg;
import com.fengwo.module_websocket.bean.LivingRoomGuardMsg;
import com.fengwo.module_websocket.bean.LivingRoomJoinMsg;
import com.fengwo.module_websocket.bean.LivingRoomLeaveMsg;
import com.fengwo.module_websocket.bean.LivingRoomTeaseHimMsg;
import com.fengwo.module_websocket.bean.LivingRoomTextMsg;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import static com.umeng.socialize.utils.ContextUtil.getContext;

public class LivingRoomPresenter extends BaseLivePresenter<ILivingRoom> {

    @Autowired
    UserProviderService userProviderService;

    public UserInfo userInfo;
    private ScoketMessageCreator creator;
    private int uid;
    FWWebSocket1.OnSocketConnectListener onSocketConnectListener;
    Gson gson = new Gson();

    private int groupId = -1;//直播间 im 房间号
    public String roomId;//房间ID == 主播ID
    public EnterLivingRoomDto enterLivingRoomDto; //进入直播间获取的信息
    private String authNicenameColor = "#ffffff", authContentColor = "#ffffff", authBgColor = "#ffffff", systemColor = "#ffffff", bulletScreenColor = "#ffffff", sysColor = "#ffffff";
    private boolean isRoomManage = false;
    private String userShouHuLevelIMG = "";
    private boolean isPkTime = false; //在PK中的PK时间段（用来惩罚时间时过滤分数消息）
    private String mPkId = "";// 每场prk的pkId(用来过滤不是本场pk的消息)

    private SafeHandle mHandle;//重拉消息
    public boolean isAttention;


    public LivingRoomPresenter() {
        ARouter.getInstance().inject(this);
        userInfo = userProviderService.getUserInfo();
        uid = userInfo.id;
        creator = new ScoketMessageCreator();
        onSocketConnectListener = new FWWebSocket1.OnSocketConnectListener() {
            @Override
            public void onMessage(String playLoad) {
                if (null == getView()) return;
                ((Activity) getView()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handSocketMsg(playLoad);
                    }
                });
            }

            @Override
            public void onReconnect() {
                checkPkType(Integer.parseInt(roomId));
            }
        };
//        if (FWWebSocket1.getInstance() == null) {
        FWWebSocket1.getInstance().init(uid,userProviderService.getToken());
//        }
        FWWebSocket1.getInstance().addOnConnectListener(onSocketConnectListener);
    }

    @Override
    public void attachView(ILivingRoom mvpView) {
        super.attachView(mvpView);
        mHandle = new SafeHandle((Activity) getView()) {
            @Override
            protected void mHandleMessage(Message msg) {
                mHandle.sendEmptyMessageDelayed(1, 3000);
                FWWebSocket1.getInstance().sendSyncMessage(userProviderService.getUserInfo().id + "");
            }
        };
    }

    public void startMsgSync() {
        mHandle.sendEmptyMessage(1);
    }

    public void stopMsgSync() {
        mHandle.removeMessages(1);
    }


    public void setRoomId(String channelId, String nickNameColor, String contentColor, String bgColor,
                          String ssystemColor, String ScreenColor) {
        this.roomId = channelId;
        authNicenameColor = nickNameColor;
        authContentColor = contentColor;
        authBgColor = bgColor;
        systemColor = ssystemColor;
        bulletScreenColor = ScreenColor;
    }

    public void setPkId(String pkId, boolean isPkTime) {
        mPkId = pkId;
        this.isPkTime = isPkTime;
    }

    ////////////////////////////IM处理开始////////////////////////////////////////////
    private void handSocketMsg(String msg) {
        L.e("-----handSocketMsg----", msg);
        try {
            JSONObject object = new JSONObject(msg);
            if (object.has("groupId")) {
                int nowGroupId = object.getInt("groupId");
                if (nowGroupId != groupId) {
                    return;
                }
            }
            int eventId = Integer.parseInt(object.getString("eventId"));
            L.e("lgl", "eventId=========>" + eventId);
            String data;
            JSONObject jsonData;
            int toUid;
            String pkId;
            String actionId = "";
            switch (eventId) {
                case EventConstant.CMD_PACKET_NOTICE://红包飘屏消息
                case EventConstant.CMD_PACKET_DIALOG://红包飘窗公告
                    //消息去重
                    if (MsgHelper.INSTANCE.isRepeatMsg(msg)) break;
                    LiveNoticeHelper.INSTANCE.sendNotice(msg);
                    getView().refreshPacketCount();
                    break;
                case EventConstant.CMD_NOTICE:
                    jsonData = object.getJSONObject("data");
                    String noticeInfo = jsonData.getString("noticeInfo");
                    getView().getAddNotice(noticeInfo, 0, "", "");
                    break;
                case EventConstant.CMD_NOTICEGG://越改越乱
                    jsonData = object.getJSONObject("data");
                    if (jsonData.isNull("noticeContent")) {
                        String userNickname = jsonData.getString("userNickname");
                        String anchorNickname = jsonData.getString("anchorNickname");
                        String wishContent = jsonData.getString("wishContent");
                        int anchorId = jsonData.getInt("anchorId");
                        String contentText = userNickname + " 对 " + anchorNickname + " 许下了心愿：" + wishContent;
                        SpannableStringBuilder builder = CharSequenceExtKt.clearStyle(contentText);
                        builder = CharSequenceExtKt.setSpanColor(builder, userNickname, Color.parseColor("#FFFA84"));
                        builder = CharSequenceExtKt.setSpanColor(builder, anchorNickname, Color.parseColor("#FFFA84"));
                        getView().getAddNotice(builder.toString(), 0, null, userNickname);
                    } else {
                        String context = jsonData.getString("noticeContent");
                        String nickname = jsonData.getString("nickname");
                        int channelIds = jsonData.getInt("channelId");
                        String color = jsonData.getString("colour");
                        String noticeBgImg = jsonData.getString("noticeBgImg");
                        String actmsg = nickname + " " + context + "";
                        getView().normalActivityIng(actmsg, channelIds, color, nickname, noticeBgImg);
                    }


                    break;
                case EventConstant.First_kill:
                    jsonData = object.getJSONObject("data");
                    int isOurSide = jsonData.getInt("isOurSide");
                    try {
                        String effectUrl = jsonData.getString("svgaEffectUrl");
                        getView().getFirstKill(isOurSide, effectUrl);
                        break;
                    } catch (JSONException e) {

                    }
                    String effectUrl = jsonData.getString("effectUrl");
                    getView().getFirstKill(isOurSide, effectUrl);
                    break;
                case EventConstant.pendantview:
                    jsonData = object.getJSONObject("data");

                    String id = jsonData.getString("id");
                    String stickerId = jsonData.getString("stickerId");
                    String stickerUrl = jsonData.getString("stickerUrl");
                    String stickerText = jsonData.getString("stickerText");
                    String stickerLocation = jsonData.getString("stickerLocation");
                    int enentType = jsonData.getInt("eventType");
                    String textColor = jsonData.getString("textColor");

                    setUpsticker(id, stickerId, stickerUrl, stickerText, stickerLocation, enentType, textColor);
//                    String[] strarray = stickerLocation.split("[,]");
//                    getView().moveUpLoactions(stickerUrl, strarray[0], strarray[1], stickerText, enentType);
                    break;
//                case EventConstant.ACTIVITY_BROCAST:
//                    JSONObject scoreJb = object.getJSONObject("data");
//                    EnterLivingRoomActivityDto enterLivingRoomActivityDto = gson.fromJson(scoreJb.toString(), EnterLivingRoomActivityDto.class);
//                    getView().updateActivityScore(enterLivingRoomActivityDto, EventConstant.ACTIVITY_BROCAST);
//                    break;
//                case EventConstant.ACTIVITY_PRIVATE://活动个人消息
//                    JSONObject actPrivate = object.getJSONObject("data");
//                    EnterLivingRoomActivityDto enterPrivateDto = gson.fromJson(actPrivate.toString(), EnterLivingRoomActivityDto.class);
//                    getView().updateActivityScore(enterPrivateDto, EventConstant.ACTIVITY_PRIVATE);
//                    break;
                case EventConstant.levelUp:
                    if (object.has("data")) {
                        JSONObject levelData = object.getJSONObject("data");
                        int level = levelData.getInt("level");
                        String experience = levelData.getLong("experience") + "";
                        userInfo.userLevel = level;
                        userInfo.experience = experience;
                        userProviderService.setUsetInfo(userInfo);
                    }
                    break;
                case EventConstant.BIG_GIFT_PIAOPING:
                    JSONObject jsonObjectData = object.getJSONObject("data");
                    if (jsonObjectData.has("broadCast")) {
                        int broadCast = jsonObjectData.getInt("broadCast");
                        if (broadCast == 3) {
                            LiveNoticeHelper.INSTANCE.sendNotice(msg);
                        }
                    }
                    handGiftBroadCastPiaoping(jsonObjectData);
                    break;
                case EventConstant.update_hot:
                    jsonData = object.getJSONObject("data");
                    int mroomId = jsonData.getInt("toUid");
                    if (mroomId == this.groupId) {
                        if (null == getView()) return;
                        getView().setCalorific(jsonData.getInt("calorific"));
                    }
                    break;
                case EventConstant.saveAMessage:
                    data = object.getJSONArray("data").toString();
                    List<LivingRoomTextMsg> resopnse = gson.fromJson(data, new TypeToken<List<LivingRoomTextMsg>>() {
                    }.getType());
                    handTextMsg(resopnse);
                    break;
                case EventConstant.broadcastMessage:
                    if (!object.getString("toUid").equals(uid) && EventConstant.CMD_OPENSHOUHU.equals(object.getJSONObject("data").getString(EventConstant.ACTIONID))) {
                        LivingRoomGuardMsg msgaaa = gson.fromJson(object.getJSONObject("data").toString(), LivingRoomGuardMsg.class);
                        sendBuyGuardMsgone(msgaaa);
                    }
                case 100602:// 发送弹幕消息EVentId
//                    if (object.has("fromUid")) {
//                        int fromId = object.getInt("fromUid");
//                        int toId = object.getInt("toUid");
//                   //     if (fromId == toId) return;
//                    }


                    handBroadcastMessage(object.getJSONObject("data"));
                    break;
                case EventConstant.leaveGroup:
                    getWatchers(roomId);
                    break;
                case EventConstant.joinGroup:   //进入直播间
                    if (object.has("toUid")) {
                        int myId = object.getInt("toUid");
                        if (myId != userInfo.id) {
                            return;
                        }
                    }
                    if (object.has("data")) {
                        if (object.getJSONObject("data").has("groupId")) {
                            if (!object.getJSONObject("data").getString("groupId").equals(groupId + "")) {
                                return;
                            }
                        }
                    }
                    data = object.getJSONObject("data").toString();
                    LivingRoomJoinMsg joinMsg = gson.fromJson(data, LivingRoomJoinMsg.class);
                    handJoinMsg(joinMsg);
                    break;
                case EventConstant.KAFKA_MSG_ID_SINGLE_RESULT://单人匹配结果
                    jsonData = object.getJSONObject("data");
                    toUid = Integer.parseInt(jsonData.getString("toUid"));
                    mPkId = jsonData.getString("pkId");
                    if (groupId != toUid) return;
                    handMatchSingleResult(object.getJSONObject("data"));
                    break;
                case EventConstant.KAFKA_MSG_ID_PK_RANK://更新pk排行版数据
                    jsonData = object.getJSONObject("data");
                    toUid = Integer.parseInt(jsonData.getString("toUid"));
                    pkId = jsonData.getString("pkId");
                    if (groupId != toUid || !mPkId.equals(pkId)) return;
                    handSinglePkRank(object.getJSONObject("data").toString());
                    break;
                case EventConstant.KAFKA_MSG_ID_PK_SINGLE_RESULT://单人pk结果
                    jsonData = object.getJSONObject("data");
                    toUid = Integer.parseInt(jsonData.getString("toUid"));
                    pkId = jsonData.getString("pkId");
                    if (groupId != toUid || !mPkId.equals(pkId)) return;
//                    handSingleResult(object.getJSONObject("data").toString());
                    break;
                case EventConstant.KAFKA_MSG_ID_PK_END://pk结束
                    jsonData = object.getJSONObject("data");
                    toUid = Integer.parseInt(jsonData.getString("toUid"));
//                    pkId = jsonData.getString("pkId");
                    if (groupId != toUid) return;
                    handPkEnd();
                    mPkId = "";
                    break;
                case EventConstant.KAFKA_MSG_ID_PK_START_TIME: //pk时间校验
                    jsonData = object.getJSONObject("data");
                    toUid = Integer.parseInt(jsonData.getString("toUid"));
//                    pkId = jsonData.getString("pkId");//这条没有加pkId,目前没有用到时间校验，如果启用，则要加pkId
                    if (groupId != toUid) return;
                    handPkUpdateTime(object.getJSONObject("data"));
                    break;
                case EventConstant.KAFKA_MSG_ID_PK_POINT: //pk礼物积分
                    jsonData = object.getJSONObject("data");
                    toUid = Integer.parseInt(jsonData.getString("toUid"));
                    pkId = jsonData.getString("pkId");
                    if (groupId != toUid || (!mPkId.equals(pkId)))
                        return;
                    handPkUpdateScore(object.getJSONObject("data"));
                    break;
                case EventConstant.KAFKA_MSG_ID_PK_ING_ERROR: //单人pk进行中异常
                    jsonData = object.getJSONObject("data");
                    toUid = Integer.parseInt(jsonData.getString("toUid"));
                    pkId = jsonData.getString("pkId");
                    if (groupId != toUid || !mPkId.equals(pkId)) return;
                    String description = object.getString("description");
                    if (null == getView()) return;
                    getView().setPkerror(description);
                    break;
                case EventConstant.forbiden_someone:
                    jsonData = object.getJSONObject("data");
                    String endTime = jsonData.getString("endTime");
                    long date = TimeUtils.instantToDate(endTime);
                    enterLivingRoomDto.isShutUp = 1;
                    enterLivingRoomDto.remainTime = date;
                    L.e("lgl", "time == " + date + "");
                    break;
                case EventConstant.un_forbiden_someone:
                    enterLivingRoomDto.isShutUp = 0;
                    enterLivingRoomDto.remainTime = 0;
                    break;
                case EventConstant.platform_forbiden:
                    JSONObject jsObj = object.getJSONObject("data");
                    long time = jsObj.getLong("time");
                    userProviderService.setClosureTime(time);
                    break;
                case EventConstant.un_platform_forbiden:
                    //   userInfo.setForbiddenWords(0);
                    // userProviderService.setUsetInfo(userInfo);
                    userProviderService.setClosureTime(0);
                    break;
                case EventConstant.banned_login:
//                    ToastUtils.showLong((Activity) getView(), "该账号已被禁用");
                    doLogoout();
                    break;
                case EventConstant.banned_host:
                    if (null == getView()) return;
                    JSONObject hostobj = object.getJSONObject("data");
                    if (null != hostobj.getString("message")) {
                        String title = hostobj.getString("message");
                        getView().bannedHost(title);
                    } else
                        getView().bannedHost("");
                    break;
                case EventConstant.warn_host:
                    String warn_host = object.getString("description");
                    if (null == getView()) return;
                    getView().warnHost(warn_host);
                    break;
                case EventConstant.KAFKA_MSG_ID_PK_SINGLE_INVITE://收到好友pk邀请 （单人）
                    InvitePkMsg inviteMsg = gson.fromJson(object.getJSONObject("data").toString(), InvitePkMsg.class);
                    if (null == getView()) return;
                    getView().showReceivePkMsg(inviteMsg, true);
                    break;
                case EventConstant.KAFKA_MSG_ID_PK_GROUP_INVITE://收到好友pk邀请 （团队）
                    InvitePkMsg teamInviteMsg = gson.fromJson(object.getJSONObject("data").toString(), InvitePkMsg.class);
                    if (null == getView()) return;
                    getView().showReceivePkMsg(teamInviteMsg, false);
                    break;
                case EventConstant.KAFKA_MSG_ID_PK_SINGLE_REFUSE://拒绝好友pk邀请
                    if (null == getView()) return;
                    InvitePkMsg inviteRefuse = gson.fromJson(object.getJSONObject("data").toString(), InvitePkMsg.class);
                    ToastUtils.showLong((Activity) getView(), inviteRefuse.nickname + "拒绝了您的邀请");
                    getView().cancleMatchSinglePK();
                    break;
                case EventConstant.KAFKA_MSG_ID_GROUP_RESULT://团队pk匹配结果
                    jsonData = object.getJSONObject("data");
                    toUid = Integer.parseInt(jsonData.getString("toUid"));
                    mPkId = jsonData.getString("pkId");
                    if (groupId != toUid) return;
                    handMatchTeamResult(object.getJSONObject("data"));
                    break;
                case EventConstant.KAFKA_MSG_ID_PK_GROUP_RESULT://团队PK结果
//                    jsonData = object.getJSONObject("data");
//                    toUid = Integer.parseInt(jsonData.getString("toUid"));
//                    pkId = jsonData.getString("pkId");
//                    if (groupId != toUid || !mPkId.equals(pkId)) return;
//                    handTeamResult(object.getJSONObject("data").toString());
                    break;
                case EventConstant.KAFKA_MSG_ID_GROUP_PK_ING_ERROR://多人PK 进行中/惩罚中异常编号 掉线
                    handTeamError(object.getJSONObject("data"), EventConstant.KAFKA_MSG_ID_GROUP_PK_ING_ERROR);
                    break;
                case EventConstant.KAFKA_MSG_ID_GROUP_PK_LEAVE://多人PK 用户提前结束PK，离开房间通知
                    handTeamError(object.getJSONObject("data"), EventConstant.KAFKA_MSG_ID_GROUP_PK_LEAVE);
                    break;
                case EventConstant.KAFKA_MSG_ID_GROUP_PK_ERROR_TO_OBJECT:
                    handTeamError(object.getJSONObject("data"), EventConstant.KAFKA_MSG_ID_GROUP_PK_ERROR_TO_OBJECT);
                    break;
                case EventConstant.KAFKA_MSG_ID_PK_GROUP_MEMBER_CHANGE://房间成员变化通知
                    handRoomMemberChange(object.getJSONObject("data"), EventConstant.KAFKA_MSG_ID_PK_GROUP_MEMBER_CHANGE);
                    break;
                case EventConstant.KAFKA_MSG_ID_PK_GROUP_CANCEL://有成员取消了匹配
                    if (null == getView()) return;
                    ToastUtils.showLong((Activity) getView(), "很抱歉，有队友取消了PK");
                    getView().cancleMatchSinglePK();
                    break;
                case EventConstant.KAFKA_MSG_ID_PK_GROUP_START://开始匹配通知
                    handRoomMemberChange(object.getJSONObject("data"), EventConstant.KAFKA_MSG_ID_PK_GROUP_START);
                    break;
                case EventConstant.host_end_live:
                    if (!object.has("data"))
                        return;
                    CustomLiveEndDto customLiveEndDto = gson.fromJson(object.getJSONObject("data").toString(), CustomLiveEndDto.class);
                    if (null == getView()) return;
                    getView().liveEnd(customLiveEndDto);
                    break;
                case EventConstant.host_level:
                    if (null == getView()) return;
                    getView().hostLeave();
                    break;
                case EventConstant.host_comeback:
                    if (null == getView()) return;
                    getView().hostComeBack();
                    break;
                case EventConstant.KAFKA_MSG_ID_PK_SURRENDER:
                    if (null == getView()) return;
                    if (isPkTime) getView().pkSurrender();
                    break;

                case EventConstant.attention:
                    String attentionMsg = object.getString("description");
                    if (getView() != null) {
                        LivingRoomTextMsg attentionMsgDto = new LivingRoomTextMsg(attentionMsg, LivingMsgDto.TYPE_SYSTEM);
                        getView().addMsg(attentionMsgDto);
                    }
                    break;
                case EventConstant.add_roomManager:
                    jsonData = object.getJSONObject("data");
                    if (uid == jsonData.getInt("userId")) {
                        isRoomManage = true;
                        getView().setRoomManager(1);
                    }
                    break;
                case EventConstant.remove_roomManager:
                    jsonData = object.getJSONObject("data");
                    if (uid == jsonData.getInt("userId")) {
                        isRoomManage = false;
                        getView().setRoomManager(0);
                    }
                    break;
                case EventConstant.kicked_out:
                    jsonData = object.getJSONObject("data");
                    if (uid == jsonData.getInt("userId") && TextUtils.equals(roomId, jsonData.getString("channelId"))) {
                        getView().kickedOut();
                    }
                    break;
                case EventConstant.KAFKA_MSG_ID_UPDATE_COMBAT:
                    if (null == getView()) return;
                    PkMatchScoreDto pkMatchScoreDto = gson.fromJson(object.getJSONObject("data").toString(), PkMatchScoreDto.class);
                    getView().updateActivityScore(pkMatchScoreDto);
                    break;
                case EventConstant.KAFKA_MSG_ID_FIRST_WIN:
                case EventConstant.KAFKA_MSG_ID_WIN_STREAK:
                case EventConstant.KAFKA_MSG_ID_BEAT_WIN_STREAK:
                case EventConstant.KAFKA_MSG_ID_GAME_CNT:
                case EventConstant.KAFKA_MSG_ID_SHARE_CNT:
                case EventConstant.KAFKA_MSG_ID_TOP_LEVEL:
                    getView().someoneComing(new FloatScreenBean(null, gson.fromJson(object.getJSONObject("data").toString(), PkFloatingScreenBean.class), null));
                    break;
                case EventConstant.CMD_MSGWSJ:
                    if (null == getView()) return;
                    getView().someoneComing(new FloatScreenBean(null, null, gson.fromJson(object.getJSONObject("data").toString(), ActivityDto.class)));
                    break;
                case EventConstant.CMD_ENDWSJ:
                    if (null == getView()) return;
                    jsonData = object.getJSONObject("data");
                    int activityId = jsonData.getInt("activityId");
                    getView().endActivity(activityId);
                    break;
                case EventConstant.CMD_STARTWSJ:
                    getView().startActivity();
                    break;
                case EventConstant.CMD_YEAR_MSG:
                    ActivityPropMessage activityPropMessage = gson.fromJson(object.getJSONObject("data").toString(), ActivityPropMessage.class);
                    RxBus.get().post(new ActivityPropMessage(activityPropMessage.getUserId(), activityPropMessage.getUserNickname(), activityPropMessage.getUserHeadImg()
                            , activityPropMessage.getPropId(), activityPropMessage.getPropName(), activityPropMessage.getPropIcon(), activityPropMessage.getPropQuantity()));
                    break;
//                case EventConstant.CMD_WISH_MSG:
//                    ActivityWishMessage activityWishMessage = gson.fromJson(object.getJSONObject("data").toString(), ActivityWishMessage.class);
//                    RxBus.get().post(new ActivityWishMessage(activityWishMessage.getUserId(), activityWishMessage.getUserNickname(), activityWishMessage.getAnchorId(),
//                            activityWishMessage.getAnchorNickname(), activityWishMessage.getWishContent(), activityWishMessage.getUserHeadImg()));
//                    break;
                case EventConstant.CMD_UP_MSG:
                    jsonData = object.getJSONObject("data");
                    boolean isWish = jsonData.getBoolean("isWish");
                    ActBannerBean newYearBaner = new ActBannerBean();
                    newYearBaner.type = 9;
                    newYearBaner.isChance = isWish;
                    getView().updateBannerData(newYearBaner);
                    break;
                case EventConstant.KAFKA_MSG_ID_PK_WIN_MVP:
                    List<LivingRoomTextMsg> mvpMsg = new ArrayList<>();
                    LivingRoomTextMsg livingRoomTextMsg = new LivingRoomTextMsg();
                    jsonData = object.getJSONObject("data");
                    String nickName = jsonData.getString("nickName");
                    String content = jsonData.getString("content");
                    String mvpEffectUrl = jsonData.getString("effectUrl");
                    livingRoomTextMsg.type = LivingRoomTextMsg.TYPE_PK_MVP;
                    livingRoomTextMsg.nickname = nickName;
                    livingRoomTextMsg.message = content;
                    mvpMsg.add(livingRoomTextMsg);
                    handTextMsg(mvpMsg);
                    try {
                        String effectUrls = jsonData.getString("svgaEffectUrl");
                        getView().getFirstKill(0, effectUrls);
                        break;
                    } catch (JSONException e) {

                    }
                    getView().getFirstKill(0, mvpEffectUrl);
                    break;
                case EventConstant.REFRESH_ACT_INTERGL_RANK://更新普通活动积分排名
//                    EnterLivingRoomActDto enterLivingRoomActDto = gson.fromJson(object.getJSONObject("data").toString(), EnterLivingRoomActDto.class);
//                    ActBannerBean actBannerBean = new ActBannerBean();
//                    if (enterLivingRoomActDto.getUserLives() != null) {
//
//                        actBannerBean.integralInfo = enterLivingRoomActDto.getUserLives().get(0).getIntegralInfo();
//                        actBannerBean.sortInfo = enterLivingRoomActDto.getUserLives().get(0).getSortInfo();
//                        actBannerBean.backgroundImg = enterLivingRoomActDto.getUserLives().get(0).backgroundImg;
//                        actBannerBean.integral = enterLivingRoomActDto.getUserLives().get(0).getIntegral();
//                        actBannerBean.sort = enterLivingRoomActDto.getUserLives().get(0).getSort();
//                        actBannerBean.address = enterLivingRoomActDto.getUserLives().get(0).getAddress();
//                        actBannerBean.type = 7;
//                        getView().updateBannerData(actBannerBean);
//                    }

                    EnterLivingRoomActWsJDto enterLivingRoomActWsJDto = gson.fromJson(object.getJSONObject("data").toString(), EnterLivingRoomActWsJDto.class);
                   // ActBannerBean actBannerBean = new ActBannerBean();
                    ActBannerBean actBean = new ActBannerBean();
                    actBean.sort = enterLivingRoomActWsJDto.getSort();
                    actBean.castleSort = enterLivingRoomActWsJDto.getCastleSort();
                    actBean.isFinish = enterLivingRoomActWsJDto.isIsFinish();
                    actBean.planeOpenSecond = enterLivingRoomActWsJDto.getPlaneOpenSecond();
                    actBean.refreshProcesses = enterLivingRoomActWsJDto.getRefreshProcesses();
                    actBean.address = enterLivingRoomActWsJDto.getAddress();
                    actBean.type = enterLivingRoomActWsJDto.getActivityId();
                    actBean.integral = enterLivingRoomActWsJDto.getIntegral();
                    getView().updateBannerData(actBean);
                    if (enterLivingRoomActWsJDto.getActStatus() == 1) {

                        if (null != enterLivingRoomActWsJDto.userGood && uid == enterLivingRoomActWsJDto.userGood.getUserId()) {
                            getView().updateSdj(enterLivingRoomActWsJDto.userGood);
                        }

                    }

                    break;
                case EventConstant.UPDATE_PLANE_BANNER_DATA://达成开航仓条件 直播间推送
                    MsgPlaneCompleteDto msgPlaneCompleteDto = gson.fromJson(object.getJSONObject("data").toString(), MsgPlaneCompleteDto.class);
                    ActBannerBean planeBaner = new ActBannerBean();
                    planeBaner.type = 4;
                    planeBaner.icon = msgPlaneCompleteDto.treasureBoxIcon;
                    planeBaner.planeOpenSecond = 330;
                    planeBaner.boxId = msgPlaneCompleteDto.treasureBoxId;
                    getView().updateBannerData(planeBaner);
                    break;
                case EventConstant.ALL_ROOM_OPEN_PLANE://达成航仓条件，全服飘屏
                    MsgPlaneCompleteDto msgPlanePiaoping = gson.fromJson(object.getJSONObject("data").toString(), MsgPlaneCompleteDto.class);
                    GiftBoardcastDto dto = new GiftBoardcastDto();
                    dto.msg = msgPlanePiaoping.nickname + "主播直播间告白航班将于5分钟后启航，心动礼盒等你抢";
                    dto.sendUserName = msgPlanePiaoping.nickname;
                    dto.receiveUserName = "";
                    dto.channelId = msgPlanePiaoping.channelId;
                    getView().showGiftBroadcast(dto);
                    break;
                case EventConstant.IS_TIME_TO_ANIMATE:
                    IsTimeToAnimateDto isTimeToAnimateDto = gson.fromJson(object.getJSONObject("data").toString(), IsTimeToAnimateDto.class);
                    GiftBean bean = new GiftBean();
                    bean.swf = isTimeToAnimateDto.specialEffectUrl;
                    bean.bigName = GiftDto.getSwfName(isTimeToAnimateDto.specialEffectUrl);
                    bean.isPlay = true;
                    bean.userid = String.valueOf(uid);
                    bean.isToutiao = true;
                    getView().showGiftAnimation(bean);
                    break;

                case EventConstant.EVENT_TEASE_HIM:  //撩一下
                    if (object.has("data")) {
                        if (object.getJSONObject("data").has("groupId")) {
                            if (!object.getJSONObject("data").getString("groupId").equals(groupId + "")) {
                                return;
                            }
                        }
                    }
                    data = object.getJSONObject("data").toString();
                    LivingRoomTextMsg form = gson.fromJson(data, LivingRoomTextMsg.class);
                    String to = object.getJSONObject("liaoData").toString();
                    LivingRoomTextMsg toMsg = gson.fromJson(to, LivingRoomTextMsg.class);
                    LivingRoomTextMsg joinM = new LivingRoomTextMsg();
                    joinM.headerurl = form.headerurl;
                    if (TextUtils.equals(userInfo.getId() + "", toMsg.userId)) {
                        joinM.nickname = form.nickname;
                        joinM.toNickname = "你";
                    } else if (TextUtils.equals(userInfo.getId() + "", form.userId)) {
                        joinM.nickname = "你";
                        joinM.toNickname = toMsg.nickname;
                    } else {
                        joinM.nickname = form.nickname;
                        joinM.toNickname = toMsg.nickname;
                    }
                    joinM.message = " 抛了一个媚眼";
                    joinM.level = form.level + "";
                    joinM.uid = form.uid + "";
                    joinM.userId = form.userId + "";
                    joinM.messageId = form.uid + System.currentTimeMillis() + "";
                    joinM.userNickColor = form.userNickColor;
                    joinM.toUserNickColor = toMsg.userNickColor;
                    joinM.userMsgColor = form.userMsgColor;
                    joinM.username_bg_color = form.username_bg_color;
                    joinM.bullet_screen = form.bullet_screen;
                    joinM.isRoomManage = form.isRoomManage;
                    joinM.userShouHuLevelIMG = form.userShouHuLevelIMG;
                    joinM.systemColor = form.systemColor;
                    joinM.type = LivingRoomTextMsg.TYPE_TEASE_HIM;
                    getView().addMsg(joinM);
                    break;
                case EventConstant.EVENT_ANCHOR_ATTENTION:   //关注主播
                    if (object.has("data")) {
                        if (object.getJSONObject("data").has("groupId")) {
                            if (!object.getJSONObject("data").getString("groupId").equals(groupId + "")) {
                                return;
                            }
                        }
                    }
                    data = object.getJSONObject("data").toString();
                    LivingRoomTextMsg attention = gson.fromJson(data, LivingRoomTextMsg.class);
                    if (TextUtils.equals(attention.uid, userInfo.getId() + "")) {
                        return;
                    }
                    if(isAttention){
                        return;
                    }
//                    if (!roomId.equals(attention.actionId)) {
//                        return;
//                    }
                    if (userInfo.getId() == Integer.parseInt(roomId)) return;
                    LivingRoomTextMsg attentionAnchorMsg = new LivingRoomTextMsg();
                    attentionAnchorMsg.message = "关注了主播";
                    attentionAnchorMsg.headerurl = attention.headerurl;
                    attentionAnchorMsg.nickname = attention.nickname;
                    attentionAnchorMsg.level = attention.level + "";
                    attentionAnchorMsg.uid = attention.uid + "";
                    attentionAnchorMsg.userId = attention.userId + "";
                    attentionAnchorMsg.messageId = attention.uid + System.currentTimeMillis() + "";
                    attentionAnchorMsg.userNickColor = attention.userNickColor;
                    attentionAnchorMsg.userMsgColor = attention.userMsgColor;
                    attentionAnchorMsg.username_bg_color = attention.username_bg_color;
                    attentionAnchorMsg.bullet_screen = attention.bullet_screen;
                    attentionAnchorMsg.isRoomManage = attention.isRoomManage;
                    attentionAnchorMsg.userShouHuLevelIMG = attention.userShouHuLevelIMG;
                    attentionAnchorMsg.systemColor = attention.systemColor;
                    attentionAnchorMsg.userShouHuLevelIMG = attention.userShouHuLevelIMG;
                    attentionAnchorMsg.type = LivingRoomTextMsg.TYPE_ANCHOR_ATTENTION;
                    attentionAnchorMsg.userVipLevel = attention.userVipLevel;
                    getView().addMsg(attentionAnchorMsg);
                    break;
                case EventConstant.EVENT_ANCHOR_SHARE:  //分享主播
                    if (object.has("data")) {
                        if (object.getJSONObject("data").has("groupId")) {
                            if (!object.getJSONObject("data").getString("groupId").equals(groupId + "")) {
                                return;
                            }
                        }
                    }
                    data = object.getJSONObject("data").toString();
                    LivingRoomTextMsg share = gson.fromJson(data, LivingRoomTextMsg.class);
                    if (TextUtils.equals(share.uid, userInfo.getId() + "")) {
                        return;
                    }
                    LivingRoomTextMsg shareMsg = new LivingRoomTextMsg();
                    shareMsg.message = "分享了主播";
                    shareMsg.headerurl = share.headerurl;
                    shareMsg.nickname = share.nickname;
                    shareMsg.level = share.level + "";
                    shareMsg.uid = share.uid + "";
                    shareMsg.userId = share.userId + "";
                    shareMsg.messageId = share.uid + System.currentTimeMillis() + "";
                    shareMsg.userNickColor = share.userNickColor;
                    shareMsg.userMsgColor = share.userMsgColor;
                    shareMsg.username_bg_color = share.username_bg_color;
                    shareMsg.bullet_screen = share.bullet_screen;
                    shareMsg.isRoomManage = share.isRoomManage;
                    shareMsg.userShouHuLevelIMG = share.userShouHuLevelIMG;
                    shareMsg.systemColor = share.systemColor;
                    shareMsg.userShouHuLevelIMG = share.userShouHuLevelIMG;
                    shareMsg.type = LivingRoomTextMsg.TYPE_ANCHOR_SHARE;
                    shareMsg.userVipLevel = share.userVipLevel;
                    getView().addMsg(shareMsg);
                    break;
                case EventConstant.EVENT_HOUR_RANK:
                    MyHourDto myHourDto = gson.fromJson(object.getJSONObject("data").toString(), MyHourDto.class);
                    getView().getMyHour(myHourDto);
                    break;
                case EventConstant.EVENT_HOUR_XYD:
                    getView().startActivity();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpsticker(String id, String stickerId, String stickerUrl, String stickerText, String stickerLocation, int enentType, String textColor) {
        String[] strarray = stickerLocation.split("[,]");
        getView().moveUpLoactions(id, stickerUrl, strarray[0], strarray[1], stickerText, enentType, textColor);
    }

    //更新pk排行版信息
    private void handSinglePkRank(String data) {
        if (null == getView()) return;
        MatchTeamResult matchTeamResult = gson.fromJson(data, MatchTeamResult.class);
        getView().updatePkRank(matchTeamResult);
    }

    private void handRoomMemberChange(JSONObject data, int eventId) {
        if (null == getView()) return;
        BaseListDto<RoomMemberChangeMsg> baseListDto = gson.fromJson(data.toString(), new TypeToken<BaseListDto<RoomMemberChangeMsg>>() {
        }.getType());
        if (eventId == EventConstant.KAFKA_MSG_ID_PK_GROUP_MEMBER_CHANGE) {
            getView().roomMemberChange(baseListDto.list);
        } else {
            getView().teamRandomMatch(baseListDto.list);
        }
    }

    private void handTeamError(JSONObject data, int msgId) {
        if (null == getView()) return;
        try {
            int errorUserId = 0;
            if (msgId == EventConstant.KAFKA_MSG_ID_GROUP_PK_ING_ERROR) {
                errorUserId = data.getInt("errorUserId");
            } else if (msgId == EventConstant.KAFKA_MSG_ID_GROUP_PK_LEAVE) {
                errorUserId = data.getInt("leaveUserId");
            }
            getView().teamPkError(errorUserId, msgId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doLogoout() {
        if (null == getView()) return;
        userProviderService.setUsetInfo(null);
        FWWebSocket1.getInstance().destroy();
        ArouteUtils.toPathWithId(ArouterApi.LOGIN_ACTIVITY, "banned");
        ((Activity) getView()).finish();
    }

    /**
     * 接收广播消息
     *
     * @param data
     */
    private void handBroadcastMessage(JSONObject data) {
        if (null == getView()) return;
        try {
            String actionId = data.getString(EventConstant.ACTIONID);
            if (EventConstant.CMD_GIFT.equals(actionId)) {
                handGiftMsg(data);
            } else if (EventConstant.CMD_CHAT.equals(actionId)) {
                handTextMsg(data); // 这里隐藏都是不使用socket消息 使用自己发送的数据
            } else if (EventConstant.CMD_GIFT_BOARDCAST.equals(actionId)) {
                handGiftBoardcastMsg(data);
            } else if (EventConstant.CMD_OPENSHOUHU.equals(actionId)) {//啥jb逻辑 我tm服了 接受广播刷新守护列表干jb
                getGuardList(Integer.parseInt(roomId));
                //    handShouhuMsg(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handShouhuMsg(JSONObject data) {
        if (null == getView()) return;
        LivingRoomGuardMsg msg = gson.fromJson(data.toString(), LivingRoomGuardMsg.class);
        getView().buyGuardMsg(msg);
        LivingRoomTextMsg livingRoomTextMsg = new LivingRoomTextMsg();
        livingRoomTextMsg.message = msg.nickname + "开通了守护 ";
        livingRoomTextMsg.userShouHuLevelIMG = msg.userShouHuLevelIMG;
        livingRoomTextMsg.type = LivingRoomTextMsg.TYPE_BUY_GUARD;
        livingRoomTextMsg.userVipLevel = msg.userVipLevel;
        livingRoomTextMsg.isRoomManage = msg.isRoomManage;
        livingRoomTextMsg.level = msg.level;
        getView().addMsg(livingRoomTextMsg);
    }

    //更新礼物积分
    private void handPkUpdateScore(JSONObject data) {
        if (null == getView() || !isPkTime) return;
        PkScoreDto pkScoreDto = gson.fromJson(data.toString(), PkScoreDto.class);
        getView().updatePkGiftScore(pkScoreDto);
    }

    //更新pk时间
    private void handPkUpdateTime(JSONObject data) {
        PkTimeDto pkTimeDto = gson.fromJson(data.toString(), PkTimeDto.class);
//        getView().updatePkTime(pkTimeDto);
    }

    private void handPkEnd() {
        if (null == getView()) return;
        getView().endPk();
        isPkTime = false;
    }


    //单人pk结果
    private void handSingleResult(String stringData) {
        if (null == getView()) return;
        KLog.e("tagpk", "pk结果检测 是否在pk");
        SinglePkResultDto singlePkResultDto = gson.fromJson(stringData, SinglePkResultDto.class);
        PkResultDto pkResultDto = new PkResultDto();
        pkResultDto.setResult(singlePkResultDto.getUserResult());
        pkResultDto.setSinglePkResultDto(singlePkResultDto);
        getView().showPkResult(pkResultDto, true);
        //结果出来最后更新一次分数
        PkScoreDto pkScoreDto = new PkScoreDto();
        pkScoreDto.setTotalPoint((int) singlePkResultDto.getTotalPoint());
        pkScoreDto.setOtherTotalPoint((int) singlePkResultDto.getOtherTotalPoint());
        getView().updatePkGiftScore(pkScoreDto);
        isPkTime = false;
    }

    //团队pk结果
    private void handTeamResult(String stringData) {
        if (null == getView()) return;
        TeamPkResultDto teamPkResultDto = gson.fromJson(stringData, TeamPkResultDto.class);
        PkResultDto pkResultDto = new PkResultDto();
        for (TeamPkItemResultDto itemResultDto : teamPkResultDto.getList()) {
            if (roomId.equals(itemResultDto.getUserId() + "")) {
                pkResultDto.setResult(itemResultDto.getIsWin());
            }
        }
        pkResultDto.setTeamPkResultDto(teamPkResultDto);
        getView().showPkResult(pkResultDto, false);
        //结果出来最后更新一次分数
        PkScoreDto pkScoreDto = new PkScoreDto();
        pkScoreDto.setTotalPoint((int) teamPkResultDto.getTotalPoint());
        pkScoreDto.setOtherTotalPoint((int) teamPkResultDto.getOtherTotalPoint());
        pkScoreDto.setGroupPoint(teamPkResultDto.getTeamPoint());
        pkScoreDto.setOtherGroupPoint(teamPkResultDto.getOtherTeamPoint());
        getView().updatePkGiftScore(pkScoreDto);
        isPkTime = false;
    }

    //单人pk匹配结果
    private void handMatchSingleResult(JSONObject data) {
        if (null == getView()) return;
        L.e("lgl", "data:" + data.toString());
        MatchTeamResult matchTeamResult = gson.fromJson(data.toString(), MatchTeamResult.class);
        getView().setSingleMatchResult(matchTeamResult);
        isPkTime = true;
    }

    //团队pk匹配结果
    private void handMatchTeamResult(JSONObject data) {
        if (null == getView()) return;
        L.e("lgl", "data:" + data.toString());
        MatchTeamResult matchTeamResult = gson.fromJson(data.toString(), MatchTeamResult.class);
        getView().setTeamMatchResult(matchTeamResult);
        isPkTime = true;
    }

    /**
     * 接收文字消息
     *
     * @param array
     */
    private void handTextMsg(List<LivingRoomTextMsg> array) {
        if (null == getView()) return;
        for (int i = 0; i < array.size(); i++) {
            LivingRoomTextMsg msg = array.get(i);
            if (msg.isDanmu()) {
                getView().sendDanmu(msg.message, msg.bullet_screen, userInfo.level == 0 ? 1 : userInfo.level, userInfo.nickname, userInfo.headImg, msg.userNickColor, msg.userMsgColor);
            }
            getView().addMsg(msg);
        }
    }

    /**
     * 接收文字消息
     *
     * @param data
     */
    private void handTextMsg(JSONObject data) throws Exception {
        if (null == getView()) return;
        LivingRoomTextMsg msg = gson.fromJson(data.toString(), LivingRoomTextMsg.class);
        //todo 过滤礼物护送消息 只接受自己
        if (msg.isEachSend == 1) {
            if (msg.userId.equals(userProviderService.getUserInfo().id + "")) {
                msg.type = LivingRoomTextMsg.TYPE_GIFT_BOARDCAST;
                getView().addMsg(msg);
            }
        } else {
            if (msg.isDanmu()) {
                getView().sendDanmu(msg.message, msg.bullet_screen, TextUtils.isEmpty(msg.level) ? 1 : Integer.parseInt(msg.level), msg.nickname, msg.headerurl, msg.userNickColor, msg.userMsgColor);
            }
            if (msg.isEachSend == 1) {//如果是护送礼物 则不添加到聊天列表
                msg.type = LivingRoomTextMsg.TYPE_GIFT_BOARDCAST;
            }
            getView().addMsg(msg);
        }
    }

    /**
     * 赠送礼物 广播 文本消息
     *
     * @param data
     */
    private void handGiftBoardcastMsg(JSONObject data) {
        if (null == getView()) return;
        LivingRoomTextMsg livingRoomTextMsg;
        try {
            String nickname = data.getString("nickname");
            String sendNum = data.getString("sendNum");
            String giftName = data.getString("giftName");
            String anchorNickname = data.getString("anchorNickname");
            String giftIcon = data.getString("giftIcon");
            String uid = data.getString("userId");
            String message = null;
            int allPrice = data.getInt("allPrice");
            if (data.toString().contains("isWishGift")) {
                if (data.getInt("isWishGift") == 1) {//收到心愿礼物信息
                    message = nickname + " 送给" + anchorNickname + " " + sendNum + "个心愿礼物 「" + giftName + "」* 价值" + allPrice + "花钻";
                    getView().refreshWish();
                } else {
                    message = nickname + " 送给" + anchorNickname + " " + sendNum + "个 「" + giftName + "」* 价值" + allPrice + "花钻";
                }
            } else {
                message = nickname + " 送给" + anchorNickname + " " + sendNum + "个 「" + giftName + "」* 价值" + allPrice + "花钻";
            }

            livingRoomTextMsg = new LivingRoomTextMsg(message, LivingRoomTextMsg.TYPE_GIFT_BOARDCAST);
            livingRoomTextMsg.allPrice = allPrice;
            livingRoomTextMsg.nickname = nickname;
            livingRoomTextMsg.giftIcon = giftIcon;
            livingRoomTextMsg.uid = uid;
//            livingRoomTextMsg.systemColor = systemColor;//
            getView().addMsg(livingRoomTextMsg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 礼物飘屏广播
     *
     * @param data
     */
    private void handGiftBroadCastPiaoping(JSONObject data) throws JSONException {
        ILivingRoom view = getView();
        if (null == view) return;
        GiftPiaopingDto giftPiaopingDto = gson.fromJson(data.toString(), GiftPiaopingDto.class);
        int broadCast = giftPiaopingDto.getBroadCast();
        String nickname = giftPiaopingDto.getNickname();
        String sendNum = giftPiaopingDto.getSendNum() + "";
        String giftName = giftPiaopingDto.getGiftName();
        String anchorNickname = giftPiaopingDto.getAnchorNickname();
        int uid = giftPiaopingDto.getUserId();
        if (1 == broadCast) {//普通礼物飘屏
            String message = giftName;
            GiftBoardcastDto dto = new GiftBoardcastDto();
            dto.msg = message;
            dto.sendUserName = nickname;
            dto.sendNum = sendNum;
            dto.receiveUserName = anchorNickname;
            dto.channelId = giftPiaopingDto.getAnchorUserId();
            dto.giftIcon = giftPiaopingDto.getGiftIcon();
            getView().showGiftBroadcast(dto);
        } else if (2 == broadCast) {//头条飘屏
            getView().showToutiao(giftPiaopingDto);
            if (groupId != com.fengwo.module_comment.Constants.GROUP_BASE + data.getInt("anchorUserId"))
                return;
            String message = "|  " + nickname + " 送给 " + anchorNickname + " " + giftName + " | x" + sendNum;
            LivingRoomTextMsg msg = new LivingRoomTextMsg(message, LivingRoomTextMsg.TYPE_TOUTIAO);
            msg.giftIcon = giftPiaopingDto.getGiftIcon();
            msg.nickname = nickname;
            msg.uid = uid + "";
            getView().addMsg(msg);
            GiftBean bean = new GiftBean();
            bean.isToutiao = true;
            bean.giftName = giftPiaopingDto.getGiftName();
            bean.bigName = GiftBean.getSwfName(giftPiaopingDto.getGiftSwf());
            bean.frameRate = giftPiaopingDto.getGiftFrameRate();
            bean.userid = userProviderService.getUserInfo().id + "";
            bean.swf = giftPiaopingDto.getGiftSwf();
            bean.isPlay = true;
            getView().showGiftAnimation(bean);
        } else if (3 == broadCast) {//游戏中奖

        }
    }

    /**
     * 进入直播间消息
     *
     * @param data
     */
    private void handJoinMsg(LivingRoomJoinMsg data) throws Exception {
        if (null == getView()) return;
        String msg = "进入直播间";
        String nickname = data.nickname;
        String headerurl = data.headerurl;
        String leave = data.level;
        String uid = data.uid;
        String consumNums = data.consumNums;
        LivingRoomTextMsg msgDto;
        msgDto = new LivingRoomTextMsg(headerurl, leave, msg, nickname, uid, LivingRoomTextMsg.TYPE_SOMECOMEING);
        msgDto.isRoomManage = data.isRoomManage;
        msgDto.userShouHuLevelIMG = data.userShouHuLevelIMG;
        msgDto.isOfficialUser = data.isOfficialUser;
        msgDto.systemColor = sysColor;
        msgDto.userVipLevel = data.userVipLevel;
        if (!TextUtils.isEmpty(data.cpRank)) {
            msgDto.cpRank = data.cpRank;
        }
        if (!TextUtils.isEmpty(data.guarName))
            msgDto.guardCarName = data.guarName;
        if (!TextUtils.isEmpty(data.carName))
            msgDto.carName = data.carName;
        msgDto.isTourist = data.isTourist;
        if (data.isActivityGuardType <= 0 && data.isCombatCheer <= 0 && data.isShareTalent <= 0) {
            getView().addMsg(msgDto);
        }

        WatcherDto watcherDto = new WatcherDto();
        watcherDto.carSwf = data.carSwf;
        watcherDto.carName = data.carName;
        watcherDto.carFrameRate = data.carFrameRate;
        watcherDto.userShouHuLevelIMG = data.userShouHuLevelIMG;
        watcherDto.isRoomManage = data.isRoomManage;
        watcherDto.guardSwf = data.guardSwf;
        watcherDto.guarFrameRate = data.guarFrameRate;
        watcherDto.guarName = data.guarName;
        if (!TextUtils.isEmpty(data.cpRank)) {
            watcherDto.cpRank = data.cpRank;
        }
        if (null != data.userVipLevel && !TextUtils.equals(data.userVipLevel, "undefined"))
            watcherDto.userVipLevel = Integer.parseInt(data.userVipLevel);
        if (null != data.level && !TextUtils.equals(data.level, "undefined"))
            watcherDto.userLevel = Integer.parseInt(leave);
        watcherDto.headImg = headerurl;
        if (!TextUtils.isEmpty(uid) && !TextUtils.equals(uid, "undefined"))
            watcherDto.userId = Integer.parseInt(uid);
        if (!TextUtils.isEmpty(consumNums) && !TextUtils.equals(consumNums, "undefined")) {
            watcherDto.consumNums = Integer.parseInt(consumNums);
        }
        watcherDto.nickname = nickname;
        watcherDto.medalId = data.medalId;
        watcherDto.isActivityGuardType = data.isActivityGuardType;
        watcherDto.isCombatCheer = data.isCombatCheer;
        watcherDto.isShareTalent = data.isShareTalent;
        if (!TextUtils.isEmpty(data.specialEffectUrl)) {
            watcherDto.specialEffectUrl = data.specialEffectUrl;
        }
//        L.e("========isCombatCheer " + data.isCombatCheer);
//        L.e("========isShareTalent " + data.isShareTalent);
//        watcherDto.isCombatCheer = 1;
//        watcherDto.isShareTalent = 1;
        getView().someoneComing(new FloatScreenBean(watcherDto, null, null));
        if (!TextUtils.isEmpty(this.roomId))
            getWatchers(this.roomId);
    }

    /**
     * 刷礼物消息
     *
     * @param data
     */
    private void handGiftMsg(JSONObject data) {
        if (null == getView()) return;
        try {
            GiftBean bean = new GiftBean();
            String giftName = data.getString("giftName");
            String giftImg = data.getString("giftImg");
            String userName = data.getString("nickname");
            if ("泡泡".equals(giftName)) {
                getView().addQipao();
                return;
            }
            int sendNum = data.getInt("sendNum");
            String swf = data.getString("giftSwf");
            if (data.has("headerurl"))
                bean.hederUrl = data.getString("headerurl");
            bean.setGiftImage(giftImg);
//            bean.frameRate = data.getInt("carFrameRate");
            bean.price = data.getInt("price");
            bean.setGiftName(giftName);
            bean.setGroup(sendNum);
            bean.setUserName(userName);
            bean.swf = swf;
            bean.bigName = GiftDto.getSwfName(swf);
            bean.userid = data.getString("uid");
            bean.isPlay = data.getInt("isPlayer") == 1;
            getView().showGiftAnimation(bean);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    ////////////////////////////IM处理结束////////////////////////////////////////////


    ///////////////////////////////IM消息发送////////////////////////////////////////////////

    /**
     * 发送文字消息
     *
     * @param groupId
     * @param content
     * @param isDanmu
     */
    public void sendMsg(int groupId, String content, boolean isDanmu, String cpRank) {
        if (null == getView()) return;

        if (enterLivingRoomDto != null && enterLivingRoomDto.isBanned(userProviderService.getClosureTime())) {
            ToastUtils.showLong((Activity) getView(), "您已被禁言");
            return;
        }
        SocketRequest<LivingRoomTextMsg> request = new SocketRequest<>();
        request.eventId = EventConstant.broadcastMessage + "";
        request.toUid = groupId + "";
        request.fromUid = uid + "";
        LivingRoomTextMsg msg = new LivingRoomTextMsg();
        msg.actionId = EventConstant.CMD_CHAT;
        msg.headerurl = userInfo.headImg;
        msg.nickname = userInfo.nickname;
        msg.isDanmu = isDanmu ? 1 : 0;
        msg.isOfficialUser = userInfo.isOfficialUser;
        msg.level = userInfo.userLevel + "";
        msg.userVipLevel = userInfo.myVipLevel + "";
        msg.message = content.replaceAll(com.fengwo.module_comment.Constants.REGULAR_EMPTY_LINE, "");
        msg.uid = uid + "";
        msg.messageId = userInfo.id + System.currentTimeMillis() + "";
        msg.userNickColor = authNicenameColor;
        msg.userMsgColor = authContentColor;
        msg.username_bg_color = authBgColor;
        msg.bullet_screen = bulletScreenColor;
        msg.isRoomManage = isRoomManage;
        msg.userShouHuLevelIMG = userShouHuLevelIMG;
        if (!TextUtils.isEmpty(cpRank)) {
            msg.cpRank = cpRank;
        }
        request.data = msg;
        if (!msg.isDanmu()) {
            try {
                //      handTextMsg(new JSONObject(gson.toJson(msg)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            FWWebSocket1.getInstance().sendTextMessage(gson.toJson(request));
        } else {
            if (TextUtils.isEmpty(roomId)) {
                return;
            }
            sendDanmuMsg(Integer.parseInt(roomId), gson.toJson(msg), userInfo.userLevel + "");
        }
    }

    /**
     * 用户护送礼物，弹幕
     *
     * @param groupId
     * @param content
     */
    public void sendGiftMsg(int groupId, String content, int count, String giftName, int allPrice) {
        if (null == getView()) return;
        if (enterLivingRoomDto != null && enterLivingRoomDto.isBanned(userProviderService.getClosureTime())) {
            ToastUtils.showLong((Activity) getView(), "您已被禁言");
            return;
        }
        SocketRequest<LivingRoomTextMsg> request = new SocketRequest<>();
        request.eventId = EventConstant.broadcastMessage + "";
        request.toUid = groupId + "";
        request.fromUid = uid + "";
        LivingRoomTextMsg msg = new LivingRoomTextMsg();
        msg.actionId = EventConstant.CMD_CHAT;
        msg.headerurl = userInfo.headImg;
        msg.nickname = userInfo.nickname;
        msg.level = userInfo.userLevel + "";
        msg.message = content;
        msg.uid = uid + "";
        msg.userId = groupId + "";
        msg.messageId = userInfo.id + System.currentTimeMillis() + "";
        msg.userNickColor = authNicenameColor;
        msg.userMsgColor = authContentColor;
        msg.username_bg_color = authBgColor;
        msg.bullet_screen = bulletScreenColor;
        msg.isRoomManage = isRoomManage;
        msg.userShouHuLevelIMG = userShouHuLevelIMG;
        msg.systemColor = systemColor;
        msg.isDanmu = 0;
        msg.isEachSend = 1;
        msg.giftName = giftName;
        msg.sendNum = count;
        msg.allPrice = allPrice;
        request.data = msg;
        FWWebSocket1.getInstance().sendTextMessage(gson.toJson(request));
    }


    /**
     * 关注主播消息发送
     */
    public void sendAnchorAttentionMsg() {
        if (null == getView()) return;
        SocketRequest<LivingRoomTextMsg> request = new SocketRequest<>();
        request.eventId = EventConstant.EVENT_ANCHOR_ATTENTION + "";
        request.toUid = groupId + "";
        request.fromUid = uid + "";
        LivingRoomTextMsg msg = new LivingRoomTextMsg();
        msg.actionId = EventConstant.EVENT_ANCHOR_ATTENTION + "";
        msg.headerurl = userInfo.headImg;
        msg.nickname = userInfo.nickname;
        msg.level = userInfo.userLevel + "";
        msg.message = "";
        msg.uid = uid + "";
        msg.userVipLevel = userInfo.myVipLevel + "";
        msg.headerurl = userInfo.headImg;
        msg.userId = groupId + "";
        msg.messageId = userInfo.id + System.currentTimeMillis() + "";
        msg.userNickColor = authNicenameColor;
        msg.userMsgColor = authContentColor;
        msg.username_bg_color = authBgColor;
        msg.bullet_screen = bulletScreenColor;
        msg.isRoomManage = isRoomManage;
        msg.userShouHuLevelIMG = userShouHuLevelIMG;
        msg.systemColor = systemColor;
        msg.isDanmu = 0;
        msg.isEachSend = 1;
        request.data = msg;
        FWWebSocket1.getInstance().sendTextMessage(gson.toJson(request));
    }

    /**
     * 关注主播消息发送
     */
    public void sendAnchorAttentionMsgs(String uid) {
        if (null == getView()) return;
        SocketRequest<LivingRoomTextMsg> request = new SocketRequest<>();
        request.eventId = EventConstant.EVENT_ANCHOR_ATTENTION + "";
        request.toUid = groupId + "";
        request.fromUid = uid + "";
        LivingRoomTextMsg msg = new LivingRoomTextMsg();
        msg.actionId = EventConstant.EVENT_ANCHOR_ATTENTION + "";
        msg.headerurl = userInfo.headImg;
        msg.nickname = userInfo.nickname;
        msg.level = userInfo.userLevel + "";
        msg.message = "";
        msg.uid = uid + "";
        msg.userVipLevel = userInfo.myVipLevel + "";
        msg.headerurl = userInfo.headImg;
        msg.userId = groupId + "";
        msg.messageId = userInfo.id + System.currentTimeMillis() + "";
        msg.userNickColor = authNicenameColor;
        msg.userMsgColor = authContentColor;
        msg.username_bg_color = authBgColor;
        msg.bullet_screen = bulletScreenColor;
        msg.isRoomManage = isRoomManage;
        msg.userShouHuLevelIMG = userShouHuLevelIMG;
        msg.systemColor = systemColor;
        msg.isDanmu = 0;
        msg.isEachSend = 1;
        request.data = msg;
        FWWebSocket1.getInstance().sendTextMessage(gson.toJson(request));
    }
    /**
     * 分享主播消息发送
     */
    public void shareAnchorMsg() {
        if (null == getView()) return;
        SocketRequest<LivingRoomTextMsg> request = new SocketRequest<>();
        request.eventId = EventConstant.EVENT_ANCHOR_SHARE + "";
        request.toUid = groupId + "";
        request.fromUid = uid + "";
        LivingRoomTextMsg msg = new LivingRoomTextMsg();
        msg.actionId = EventConstant.EVENT_ANCHOR_SHARE + "";
        msg.headerurl = userInfo.headImg;
        msg.nickname = userInfo.nickname;
        msg.level = userInfo.userLevel + "";
        msg.message = "";
        msg.uid = uid + "";
        msg.userId = groupId + "";
        msg.headerurl = userInfo.headImg;
        msg.userVipLevel = userInfo.myVipLevel + "";
        msg.messageId = userInfo.id + System.currentTimeMillis() + "";
        msg.userNickColor = authNicenameColor;
        msg.userMsgColor = authContentColor;
        msg.username_bg_color = authBgColor;
        msg.bullet_screen = bulletScreenColor;
        msg.isRoomManage = isRoomManage;
        msg.userShouHuLevelIMG = userShouHuLevelIMG;
        msg.systemColor = systemColor;
        msg.isDanmu = 0;
        msg.isEachSend = 1;
        request.data = msg;
        FWWebSocket1.getInstance().sendTextMessage(gson.toJson(request));
    }

    /**
     * 撩他一下消息发送
     */
    public void sendTeaseHimMsg(LivingRoomTextMsg toMsg) {
        if (enterLivingRoomDto != null && enterLivingRoomDto.isBanned(userProviderService.getClosureTime())) {
            ToastUtils.showLong((Activity) getView(), "您已被禁言");
            return;
        }
        LivingRoomTeaseHimMsg request = new LivingRoomTeaseHimMsg();
        request.eventId = EventConstant.EVENT_TEASE_HIM + "";
        request.fromUid = uid + "";
        request.toUid = groupId + "";

        LivingRoomTextMsg to = new LivingRoomTextMsg();
        to.actionId = EventConstant.CMD_TEASE_HIM + "";
        to.headerurl = toMsg.headerurl;
        to.nickname = toMsg.nickname;
        to.level = toMsg.level + "";
        to.uid = groupId + "";
        to.userId = toMsg.uid + "";
        to.messageId = toMsg.uid + System.currentTimeMillis() + "";
        to.userNickColor = toMsg.userNickColor;
        to.userMsgColor = toMsg.userMsgColor;
        to.username_bg_color = toMsg.username_bg_color;
        to.bullet_screen = toMsg.bullet_screen;
        to.isRoomManage = toMsg.isRoomManage;
        to.userShouHuLevelIMG = toMsg.userShouHuLevelIMG;
        to.systemColor = toMsg.systemColor;
        to.userVipLevel = toMsg.getUserVipLevel() + "";
        request.liaoData = to;

        LivingRoomTextMsg from = new LivingRoomTextMsg();
        from.uid = userInfo.getId() + "";
        from.actionId = EventConstant.CMD_TEASE_HIM + "";
        from.headerurl = userInfo.headImg;
        from.nickname = userInfo.nickname;
        from.level = userInfo.userLevel + "";
        from.userId = userInfo.getId() + "";
        from.messageId = uid + System.currentTimeMillis() + "";
        from.userNickColor = authNicenameColor;
        from.userMsgColor = authContentColor;
        from.username_bg_color = authBgColor;
        from.bullet_screen = bulletScreenColor;
        from.isRoomManage = isRoomManage;
        from.userShouHuLevelIMG = userShouHuLevelIMG;
        from.systemColor = systemColor;
        from.userVipLevel = userInfo.vipLevel + "";
        request.data = from;

        FWWebSocket1.getInstance().sendTextMessage(gson.toJson(request));
    }


    /**
     * 发送礼物消息 用于广播给所有用户播放礼物特效
     *
     * @param groupId
     * @param sendNum
     */
    public void sendGiftAnimateMsg(int groupId, GiftDto giftDto, int sendNum, int isPlayer) {
        SocketRequest<LivingRoomGiftMsg> request = new SocketRequest<>();
        request.eventId = EventConstant.broadcastMessage + "";
        request.toUid = groupId + "";
        request.fromUid = uid + "";
        LivingRoomGiftMsg msg = new LivingRoomGiftMsg();
        msg.actionId = EventConstant.CMD_GIFT;
        msg.giftImg = giftDto.giftIcon;
        msg.giftName = giftDto.giftName;
        msg.nickname = userInfo.nickname;
        msg.sendNum = sendNum;
        msg.price = giftDto.giftPrice;
        msg.giftSwf = giftDto.giftSwf;
        msg.carFrameRate = giftDto.frameRate;
        msg.uid = uid + "";
        msg.isPlayer = isPlayer;
        msg.headerurl = userInfo.headImg;
        msg.messageId = userInfo.id + System.currentTimeMillis() + "";
        request.data = msg;
        FWWebSocket1.getInstance().sendTextMessage(gson.toJson(request));
        try {
            handGiftMsg(new JSONObject(gson.toJson(msg)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        creator.sendGiftMsg(uid, groupId, giftImg, giftName, userProviderService.getUserInfo().nickname, userProviderService.getUserInfo().headImg, swf, sendNum);
    }

    /**
     * 发送 加入群组Im消息
     *
     * @param groupId
     */

    public void joinGroupIM(int groupId, int isActivityGuardType, int isCombatCheer, int isShareTalent, boolean hasCar, String cpRank) {
        this.groupId = groupId;
        SocketRequest<LivingRoomJoinMsg> joinGroup = new SocketRequest<>();
        joinGroup.eventId = EventConstant.joinGroup + "";
        joinGroup.fromUid = uid + "";
        joinGroup.toUid = groupId + "";
        LivingRoomJoinMsg data = new LivingRoomJoinMsg();
        data.isActivityGuardType = isActivityGuardType;
        data.isCombatCheer = isCombatCheer;
        data.isShareTalent = isShareTalent;
        data.uid = uid + "";
        data.nickname = userInfo.nickname;
        data.headerurl = userInfo.headImg;
        data.isOfficialUser = userInfo.isOfficialUser;
        data.cpRank = cpRank;
        data.level = userInfo.userLevel + "";
        data.userVipLevel = userInfo.myVipLevel + "";
        if (hasCar) {
            data.carSwf = userInfo.motoringSwf;
        }
        data.carName = userInfo.motoringName;
        data.carFrameRate = userInfo.frameRate;
        data.isRoomManage = isRoomManage;
        data.userShouHuLevelIMG = userShouHuLevelIMG;
        if (hasCar && null != enterLivingRoomDto && null != enterLivingRoomDto.isAnchorGuardVO && null != enterLivingRoomDto.isAnchorGuardVO.privileges) {
            data.guardSwf = enterLivingRoomDto.isAnchorGuardVO.privileges.motoringSwf;
            data.guarFrameRate = enterLivingRoomDto.isAnchorGuardVO.privileges.motoringSwfRate;
            data.guarName = enterLivingRoomDto.isAnchorGuardVO.privileges.motoringName;
        }
        data.groupId = groupId + "";
        data.messageId = userInfo.id + System.currentTimeMillis() + "";
        joinGroup.data = data;
        FWWebSocket1.getInstance().sendTextMessage(gson.toJson(joinGroup));
    }

    /**
     * 发送 加入群组Im消息
     *
     * @param groupId
     */

    public void joinGroupIM(int groupId, String specialEffectUrl) {
        this.groupId = groupId;
        SocketRequest<LivingRoomJoinMsg> joinGroup = new SocketRequest<>();
        joinGroup.eventId = EventConstant.joinGroup + "";
        joinGroup.fromUid = uid + "";
        joinGroup.toUid = groupId + "";
        LivingRoomJoinMsg data = new LivingRoomJoinMsg();
        data.uid = uid + "";
        data.nickname = userInfo.nickname;
        data.headerurl = userInfo.headImg;
        data.level = userInfo.userLevel + "";
        data.userVipLevel = userInfo.myVipLevel + "";
        if (!TextUtils.isEmpty(specialEffectUrl)) {
            data.specialEffectUrl = specialEffectUrl;
        }
        data.carName = userInfo.motoringName;
        data.carFrameRate = userInfo.frameRate;
        data.isRoomManage = isRoomManage;
        data.userShouHuLevelIMG = userShouHuLevelIMG;
        data.groupId = groupId + "";
        data.messageId = userInfo.id + System.currentTimeMillis() + "";
        joinGroup.data = data;
        FWWebSocket1.getInstance().sendTextMessage(gson.toJson(joinGroup));
    }

    //im区发送消息  count 是礼物数量
    private void sendGiftBroadCast(GiftDto gift, int count) {
        if (null == gift) return;
        if (count == 0) count = 1;
        SocketRequest<LivingRoomGiftBoardcastMsg> request = new SocketRequest<>();
        request.eventId = EventConstant.broadcastMessage + "";
        request.toUid = groupId + "";
        request.fromUid = uid + "";
        LivingRoomGiftBoardcastMsg msg = new LivingRoomGiftBoardcastMsg();
        msg.actionId = EventConstant.CMD_GIFT_BOARDCAST;
        msg.giftName = gift.giftName;
        msg.isWishGift = gift.isWishGift;
        msg.sendNum = count + "";
        msg.nickname = userInfo.nickname;
        msg.messageId = userInfo.id + System.currentTimeMillis() + "";
        msg.allPrice = (int) (count * gift.giftPrice);
        if (!TextUtils.isEmpty(gift.giftIcon)) {
            msg.giftIcon = gift.giftIcon;
        }
        msg.anchorNickname = enterLivingRoomDto.nickname;
        msg.userId = uid + "";
        request.data = msg;
        FWWebSocket1.getInstance().sendTextMessage(gson.toJson(request));
        try {
            handGiftBoardcastMsg(new JSONObject(gson.toJson(msg)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendBuyGuardMsg(String name, String icon) {
        SocketRequest<LivingRoomGuardMsg> request = new SocketRequest<>();
        request.eventId = EventConstant.broadcastMessage + "";
        request.toUid = groupId + "";
        request.fromUid = uid + "";
        LivingRoomGuardMsg msg = new LivingRoomGuardMsg();
        msg.actionId = EventConstant.CMD_OPENSHOUHU;
        msg.nickname = userInfo.nickname;
        msg.userShouHuLevel = name;
        msg.userShouHuLevelIMG = icon;
        msg.messageId = userInfo.id + System.currentTimeMillis() + "";
        msg.level = userInfo.userLevel + "";
        msg.userVipLevel = userInfo.myVipLevel + "";
        msg.isRoomManage = isRoomManage;
        request.data = msg;
        FWWebSocket1.getInstance().sendTextMessage(gson.toJson(request));
//        try {
//            handShouhuMsg(new JSONObject(gson.toJson(msg)));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    public void sendBuyGuardMsgone(LivingRoomGuardMsg data) {
        SocketRequest<LivingRoomGuardMsg> request = new SocketRequest<>();
        request.eventId = EventConstant.broadcastMessage + "";
        request.toUid = groupId + "";
        request.fromUid = uid + "";
        LivingRoomGuardMsg msg = new LivingRoomGuardMsg();
        msg.actionId = EventConstant.CMD_OPENSHOUHU;
        msg.nickname = data.nickname;
        msg.userShouHuLevel = data.userShouHuLevel;
        msg.userShouHuLevelIMG = data.userShouHuLevelIMG;
        msg.messageId = data.messageId;
        msg.level = data.level;
        msg.userVipLevel = data.userVipLevel;
        msg.isRoomManage = isRoomManage;
        request.data = msg;
        try {
            handShouhuMsg(new JSONObject(gson.toJson(msg)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 主播关播 发送通知
     *
     * @param channelId
     */
    public void sendEndLiveMsg(int channelId) {
        SocketRequest<LiveEndMsg> request = new SocketRequest<>();
        request.eventId = EventConstant.leaveGroup + "";
        request.toUid = groupId + "";
        request.fromUid = uid + "";
        LiveEndMsg msg = new LiveEndMsg();
        msg.actionId = EventConstant.CMD_TEXT_LEAVEROOM;
        msg.userId = channelId;
        request.data = msg;
        FWWebSocket1.getInstance().sendTextMessage(gson.toJson(request));
    }


    /**
     * 贴图移动view
     *
     * @param
     * @param
     */
    public void sendPendantMsg(String guajian_imgUrl, String topMerge, String leftMerge, String titleMsg, int channelId, String isDelete) {
        SocketRequest<LivePendantMsg> request = new SocketRequest<>();
        request.eventId = EventConstant.pendantview + "";
        request.toUid = groupId + "";
        request.fromUid = uid + "";

        LivePendantMsg msg = new LivePendantMsg();
        msg.guajian_imgUrl = guajian_imgUrl;
        msg.topMerge = topMerge;
        msg.leftMerge = leftMerge;
        msg.titleMsg = titleMsg;
        msg.isDelete = isDelete;
        msg.level = userInfo.userLevel + "";
        msg.carSwf = userInfo.motoringSwf;
        msg.userVipLevel = userInfo.myVipLevel + "";
        msg.userNickColor = authNicenameColor;
        msg.userMsgColor = authContentColor;
        msg.userShouHuLevel = "";
        msg.message = "";
        msg.username_bg_color = authBgColor;
        msg.actionId = EventConstant.CMD_TEXT_LEAVEROOM;
        msg.userId = channelId;
        msg.messageId = userInfo.id + System.currentTimeMillis() + "";
        request.data = msg;
        FWWebSocket1.getInstance().sendTextMessage(gson.toJson(request));

//        creator.sendGiftMsg(uid, groupId, giftImg, giftName, userProviderService.getUserInfo().nickname, userProviderService.getUserInfo().headImg, swf, sendNum);
    }
    ///////////////////////////////接口调用//////////////////////////////////////////

    /**
     * 赠送礼物 接口
     *
     * @param giftDto
     * @param num
     * @param id
     */

    private long firstTime;
    private int count = 0;//送主播礼物统计
    private int preGiftId = -1;
    private GiftDto preGift;
    private Handler giftHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    giftHandler.sendEmptyMessageDelayed(2, 5000);
                    break;
            }
        }
    };
    CountBackUtils countBackUtils = new CountBackUtils();

    private long feizaoFirstTime;
    private int feizaoCount = 0;//送泡泡礼物统计
    CountBackUtils feizaoCountBackUtils = new CountBackUtils();

    private int toUserCount = 1;//用户护送礼物统计
    private long toUserFirstTime = 0;

    AlertDialog noMoneyDialog;

    public void sendFeizaoGift(String channelId) {
        addNet(
                service.sendFeizao(channelId)
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<PopoDto>>() {
                            @Override
                            public void _onNext(HttpResult<PopoDto> data) {
                                if (null == getView()) return;
                                if (data.isSuccess()) {
                                    getView().setUpdateExperience(data.data.getExperience());//更新经验值
                                    RxBus.get().post(new PaySuccessEvent(data.data.getExperience().toString()));//支付成功更新经验值
//                                    feizaoCount++;
//                                    GiftDto gift = new GiftDto();
//                                    gift.giftName = "泡泡";
//                                    gift.giftIcon = "泡泡";
//                                    gift.giftPrice = 5;
//                                    sendGiftAnimateMsg(groupId, gift, feizaoCount, 1);//弹幕消息

//                                    long nowTime = System.currentTimeMillis();
//                                    if (feizaoFirstTime <= 0) {
//                                        feizaoFirstTime = System.currentTimeMillis();
//                                        feizaoCountBackUtils.countBack(3, new CountBackUtils.Callback() {
//                                            @Override
//                                            public void countBacking(long time) {
//
//                                            }
//
//                                            @Override
//                                            public void finish() {
//                                                sendGiftBroadCast(gift, feizaoCount);
//                                                feizaoFirstTime = 0;
//                                                feizaoCount = 0;
//                                            }
//                                        });
//                                    } else {
//                                        if (nowTime - feizaoFirstTime <= 3000) {
//                                            feizaoFirstTime = System.currentTimeMillis();
//                                            feizaoCountBackUtils.updateTime(3);
//                                        } else {
//                                            sendGiftBroadCast(gift, feizaoCount);
//                                            feizaoCount = 0;
//                                        }
//                                    }
                                } else if (data.description.equals("余额不足")) {
                                    if (null == noMoneyDialog || !noMoneyDialog.isShowing())
                                        noMoneyDialog = DialogUtil.showNoMoneyDialog((Activity) getView(), new DialogUtil.AlertDialogBtnClickListener() {
                                            @Override
                                            public void clickPositive() {
                                                RxBus.get().post(new ShowRechargePopEvent());
                                            }

                                            @Override
                                            public void clickNegative() {
                                            }
                                        });
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                                if (null == getView()) return;
//                                getView().toastTip(msg);
                            }
                        }));
    }

    public void sendGiftToUser(int uid, GiftDto gift, int num, String userName, int channelId) {
        //背包礼物互送
        if (gift.id == 0 && gift.goodsType == 1) {
            sendPackGift(gift, num, getView(), true, userName, uid);
            return;
        }
        addNet(
                service.sendGift(new ParamsBuilder()
                        .put("id", gift.id + "")
                        .put("quantity", num + "")
                        .put("targetId", uid + "")
                        .put("isEachSend", channelId == uid ? "0" : "1")//是否互送 默认0：否，1：是
                        .build())
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<EndGiftDto>>() {
                            @Override
                            public void _onNext(HttpResult<EndGiftDto> data) {
                                sendEachOtherMsg(data, gift, uid, userName, num);
                            }

                            @Override
                            public void _onError(String msg) {
//                                if (null == getView()) return;
//                                getView().toastTip(msg);
                            }
                        })
        );
    }

    private void sendEachOtherMsg(HttpResult<EndGiftDto> data, GiftDto gift, int uid, String userName, int num) {
        if (null == getView()) return;
        if (data.isSuccess()) {
            RxBus.get().post(new PaySuccessEvent(data.data.getExperience()));//更新钱包

            String gitName = TextUtils.isEmpty(gift.giftName) ? gift.goodsName : gift.giftName;
            StringBuilder builder = new StringBuilder();
            int price = (int) (gift.giftPrice * toUserCount);
            builder.append(userProviderService.getUserInfo().nickname)
                    .append("送了你")
                    .append(toUserCount + "")
                    .append("个 「")
                    .append(gitName)
                    .append("」 价值")
                    .append(price + "花钻");
            sendGiftMsg(uid, builder.toString(), toUserCount, gitName, price);
            if(LivingRoomPresenter.this.roomId.equals(uid+"")){//判断是否是当前主播
                return;
            }
            LivingRoomTextMsg msg = new LivingRoomTextMsg();
            msg.type = LivingRoomTextMsg.TYPE_GIFT_BOARDCAST;
            StringBuilder mineStrBuild = new StringBuilder();
            mineStrBuild.append("送给")
                    .append(userName + " ")
                    .append(toUserCount + "")
                    .append("个 「")
                    .append(gitName)
                    .append("」 价值")
                    .append(price + "花钻");
            msg.message = mineStrBuild.toString();
            getView().addMsg(msg);//发im消息
        } else {
            getView().toastTip(data.description);
        }
    }

    //活动赠送套餐礼盒
    public void sendActivityGift(GiftDto giftDto, int num, int id) {
        if (null == getView()) return;
        MvpView v = TextUtils.isEmpty(giftDto.giftSwf) ? null : getView();
        addNet(
                service.sendActivityGift(new ParamsBuilder()
                        .put("giftId", giftDto.id + "")
                        .put("targetId", roomId + "")
                        .build())
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<EndGiftDto>>(v) {
                            @Override
                            public void _onNext(HttpResult<EndGiftDto> data) {
                                if (data.isSuccess()) {
                                    RxBus.get().post(new PaySuccessEvent(data.data.getExperience()));   //赠送成功 更新用户钱包
//                                    long nowTime = System.currentTimeMillis();
//                                    if (firstTime <= 0) {
//                                        preGift = giftDto;
//                                        firstTime = System.currentTimeMillis();
//                                        countBackUtils.countBack(3, new CountBackUtils.Callback() {
//                                            @Override
//                                            public void countBacking(long time) {
//                                            }
//
//                                            @Override
//                                            public void finish() {
//                                                sendGiftBroadCast(preGift, num);
//                                                firstTime = 0;
//                                                count = 0;
//                                            }
//                                        });
//                                        preGiftId = giftDto.id;
//                                    } else {
//                                        if (nowTime - firstTime <= 3000 && preGift.id == giftDto.id) {
//                                            firstTime = System.currentTimeMillis();
//                                            countBackUtils.updateTime(3);
//                                        } else {
//                                            sendGiftBroadCast(preGift, num);
//                                            count = 0;
//                                            preGift = giftDto;
//                                            if (countBackUtils.isTiming()) {
//                                                firstTime = System.currentTimeMillis();
//                                                countBackUtils.updateTime(3);
//                                            }
//                                        }
//
//                                        preGiftId = giftDto.id;
//                                    }
//                                    giftDto.giftPrice = giftDto.giftDiscountPriceTotal;
//                                    if(!getResult(giftDto.giftName,"折扣套餐")){
//                                        giftDto.giftName = giftDto.giftName + "折扣套餐";
//                                    }
//
//                                    L.e("giftttttCounttttt", giftDto.giftPrice + "-------" + num + "");
//                                    sendGiftAnimateMsg(groupId, giftDto, 1, PrivilegeEffectUtils.getInstance().isShowBigGift(giftDto.giftPrice) ? 1 : 0);
                                } else {
                                    if (null != getView())
                                        getView().toastTip(data.description);
                                }

                            }

                            @Override
                            public void _onError(String msg) {
                                if (null != getView())
                                    getView().toastTip(msg);
                            }
                        })
        );
    }

    public static boolean getResult(String targetStr, String str) {
        if (targetStr.contains(str)) {
            return true;
        } else {
            return false;
        }
    }

    public void sendGift(GiftDto giftDto, int num, int id) {
        if (null == getView()) return;
        MvpView v = TextUtils.isEmpty(giftDto.giftSwf) ? null : getView();
        if (giftDto.id <= 0) {//背包礼物
            sendPackGift(giftDto, num, v, false, null, 0);
            return;
        }
        //普通礼物
        addNet(
                service.sendGift(new ParamsBuilder()
                        .put("id", giftDto.id + "")
                        .put("quantity", num + "")
                        .put("targetId", roomId + "")
                        .build())
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<EndGiftDto>>(v) {
                            @Override
                            public void _onNext(HttpResult<EndGiftDto> data) {
                                if (data.isSuccess()) {
                                    RxBus.get().post(new PaySuccessEvent(data.data.getExperience()));   //赠送成功 更新用户钱包
//                                    long nowTime = System.currentTimeMillis();
//                                    if (firstTime <= 0) {
//                                        preGift = giftDto;
//                                        firstTime = System.currentTimeMillis();
//                                        countBackUtils.countBack(3, new CountBackUtils.Callback() {
//                                            @Override
//                                            public void countBacking(long time) {
//                                            }
//
//                                            @Override
//                                            public void finish() {
//                                                sendGiftBroadCast(preGift, count);
//                                                firstTime = 0;
//                                                count = 0;
//                                            }
//                                        });
//                                        count = count + num;
//                                        preGiftId = giftDto.id;
//                                    } else {
//                                        if (nowTime - firstTime <= 3000 && preGift.id == giftDto.id) {
//                                            firstTime = System.currentTimeMillis();
//                                            countBackUtils.updateTime(3);
//                                        } else {
//                                            sendGiftBroadCast(preGift, count);
//                                            count = 0;
//                                            preGift = giftDto;
//                                            if (countBackUtils.isTiming()) {
//                                                firstTime = System.currentTimeMillis();
//                                                countBackUtils.updateTime(3);
//                                            }
//                                        }
//                                        count = count + num;
//                                        preGiftId = giftDto.id;
//                                    }
//                                    double value = count * giftDto.giftPrice;
//                                    L.e("giftttttCounttttt", value + "-------" + count + "");
//                                    sendGiftAnimateMsg(groupId, giftDto, num, PrivilegeEffectUtils.getInstance().isShowBigGift(value) ? 1 : 0);
                                } else {
                                    if (null != getView())
                                        getView().toastTip(data.description);
                                }

                            }

                            @Override
                            public void _onError(String msg) {
                                if (null != getView())
                                    getView().toastTip(msg);
                            }
                        })
        );
    }

    private void sendPackGift(GiftDto giftDto, int num, MvpView v, boolean isEachSend, String userName, int uid) {
        addNet(service.givingBagGift(new ParamsBuilder()
                        .put("id", giftDto.goodsId + "")
                        .put("isEachSend", isEachSend ? "1" : "0")
                        .put("quantity", num + "")
                        .put("targetId", isEachSend ? (uid + "") : (roomId + ""))
                        .put("validTime", giftDto.validTime + "")
                        .build())
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<EndGiftDto>>(v) {
                            @Override
                            public void _onNext(HttpResult<EndGiftDto> data) {
                                if (isEachSend) {
                                    sendEachOtherMsg(data, giftDto, uid, userName, num);
                                }
                                if (data.isSuccess()) {
                                    getView().requestBackpack(giftDto);
//                            RxBus.get().post(new PaySuccessEvent(data.data.getExperience()));
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                                if (null != getView())
                                    getView().toastTip(msg);
                            }
                        })
        );
    }

    public void getUserInfo(boolean isHost, int userId) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        int id = userId << 3;
        Map map = new HashMap();
        map.put("userId", id);
        compositeDisposable.add(new RetrofitUtils().createApi(LiveApiService.class)
                .getUserinfoByIdNew(createRequestBody(map))
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<UserInfo>>() {
                    @Override
                    public void _onNext(HttpResult<UserInfo> data) {
                        if (data.isSuccess()) {
                            if (getView() == null) return;
                            getView().showShouhu(isHost, userId, data.data);

                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                }));

    }

    private final Map<Integer, String> leaveGroupBufferMap = new HashMap<>();//离开直播间接口调用去重

    /**
     * 调用离开房间接口 并发送离开房间消息
     *
     * @param groupId
     * @param channelId 从channelId改为userId
     */
    public void leaveGroup(int groupId, int channelId) {
        KLog.e("gkh_ggg", "---退出直播间 channelId " + channelId);
        if (leaveGroupBufferMap.containsKey(channelId)) {
            KLog.e("gkh_ggg", "---退出直播间 channelId " + channelId + "重复");
            return;
        }
        leaveGroupBufferMap.put(channelId, "");
        service.leaveLivingRoom(channelId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        KLog.e("gkh_ggg", "---退出直播间-成功 channelId " + channelId);
                        SocketRequest leave = new SocketRequest();
                        leave.toUid = groupId + "";
                        leave.fromUid = uid + "";
                        leave.eventId = EventConstant.leaveGroup + "";
                        LivingRoomLeaveMsg livingRoomLeaveMsg = new LivingRoomLeaveMsg();
                        livingRoomLeaveMsg.groupId = groupId + "";
                        leave.data = livingRoomLeaveMsg;
                        FWWebSocket1.getInstance().sendTextMessage(gson.toJson(leave));
                        leaveGroupBufferMap.remove(channelId);
                    }

                    @Override
                    public void _onError(String msg) {
                        leaveGroupBufferMap.remove(channelId);
                    }
                });
    }


    Subscriber sub;
    private String description = "主播已下播";


    /**
     * 调用进入房间接口 并发送加入房间消息
     *
     * @param groupId
     * @param channelId 从channelId改为userId
     * @param isChange  是否是切换房间
     */
    public void joinGroup(int groupId, int channelId, boolean isChange) {
        if (countBackUtils.isTiming()) {//如果正在送礼物且广播还没发送，切换房间直接发送广播，销毁计时
            sendGiftBroadCast(preGift, count);
            countBackUtils.destory();//销毁礼物延迟广播
            preGift = null;
        }
        getView().showNoCancleLoading();
        Map map = new HashMap();
        map.put("channelId", channelId);
        map.put("timestamp", System.nanoTime());
        Map userMedal = new HashMap();
        userMedal.put("channelId", channelId);
        userMedal.put("userId", uid);
        userMedal.put("timestamp", System.nanoTime());

        KLog.e("gkh_ggg", "加入直播间 channelId " + channelId);
        addNet(service.enterLivingRoom(createRequestBody(map))
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<EnterLivingRoomDto>() {
                    @Override
                    public void _onNext(EnterLivingRoomDto data) {
                        if (getView() == null) return;
                        if (null == data) {
//                            DialogUtil.showAlertDialog((Activity) getView(), "提示", description, "确定", "", false, new DialogUtil.AlertDialogBtnClickListener() {
//                                @Override
//                                public void clickPositive() {
//                                    ((Activity) getView()).finish();
//                                }
//
//                                @Override
//                                public void clickNegative() {
//                                    ((Activity) getView()).finish();
//                                }
//                            });
                            return;
                        }
                        KLog.e("gkh_ggg", "加入直播间-成功 channelId " + channelId);
                        enterLivingRoomDto = data;
                        ////todo 异步请求 加载房间的信息

                        loadRoomInfo(groupId, channelId, false);//获取房间活动信息

                        getView().hideNoCancleLoading();//显示viewpgaer的 右边的布局
                        authNicenameColor = data.colorInfo.getNickname_color();
                        authContentColor = data.colorInfo.getUser_color();
                        authBgColor = data.colorInfo.getNickname_bg_color();
                        systemColor = data.colorInfo.getSystem_notice_color();
                        bulletScreenColor = data.colorInfo.getBullet_screen();
                        sysColor = data.colorInfo.getSystem_message();
                        isRoomManage = data.roomManage == 1;
                        if (data.isAnchorGuardVO != null) {
                            userShouHuLevelIMG = data.isAnchorGuardVO.guardIcon;
                        }
                        LivingRoomPresenter.this.groupId = groupId;
                        getView().setZhuboInfo(data);
                        LivingRoomPresenter.this.roomId = data.channelId + "";
                        getWatchers(data.channelId + "");
                        getView().clearMsg();//im清空
                        for (int i = 0; i < data.notices.size(); i++) {
                            LivingRoomTextMsg msg = new LivingRoomTextMsg(data.notices.get(i), LivingRoomTextMsg.TYPE_SYSTEM);
                            msg.systemColor = systemColor;
                            getView().addMsg(msg);
                        }
//                        if (null != data.calorific)
//                            getView().setCalorific(data.calorific);//热度 被干掉了
                        if (data.pkStatus > 0 && null != data.channelPkInfo && null != data.channelPkInfo.getEndPunishTime() && data.channelPkInfo.getEndPunishTime() > 0) {
//                            data.channelPkInfo.setEndPunishTime(data.channelPkInfo.getEndPunishTime() - data.channelPkInfo.getEndPkTime());//进入直播间的惩罚时间重新计算(只有这里需要重算)
                            if (data.channelPkInfo.getMembers() != null && data.channelPkInfo.getMembers().size() > 0) {//pk相关
                                getView().setTeamMatchResult(data.channelPkInfo);
                            } else {
                                getView().setSingleMatchResult(data.channelPkInfo);
                            }
                            if (data.channelPkInfo.getPkPoint() != null) {
                                getView().updatePkGiftScore(data.channelPkInfo.getPkPoint());
                                //         getView().toastTip("进入直播间 更新经验值 TotalPoint=" + data.channelPkInfo.getPkPoint().getTotalPoint());
                            }

                            KLog.e("pk", "进入直播 pk剩余时间" + data.channelPkInfo.getPkTime());
                            if (data.channelPkInfo.getPkTime() <= 0) {//惩罚时间进入直播间
                                PkResultDto pkResultDto = new PkResultDto();
                                isPkTime = false;
                                pkResultDto.setResult(data.channelPkInfo.getUserResult());
                                pkResultDto.setSinglePkResultDto(new SinglePkResultDto());
                                KLog.e("pk", "进入直播间检测是否在pk 是否在pk");
                                getView().showPkResult(pkResultDto, true);//如果是惩罚时间进入直播间，暂时客户端只展示当前直播间胜负，不展示团队胜负
                            } else {
                                isPkTime = true;
                            }
                            mPkId = data.channelPkInfo.getPkId();
                        } else {
                            getView().setNewRoomInfo(data);//切换直播间获取挂件 小时榜等数据
                        }
                        getView().changeRoomInfo(data);//替换列表数据为 接口数据
                        getGuardList(data.channelId);
                        getLastActivityInfo(channelId);
                        if (null != data.getActivityCpInfoBean() && !TextUtils.isEmpty(data.getActivityCpInfoBean().getCpRank())) {
                            joinGroupIM(groupId, 0, 0, 0, true, data.getActivityCpInfoBean().getCpRank());

                            //   getView().activityCpIMG(data.getActivityCpInfoBean().getCpRank());

                        } else {
                            joinGroupIM(groupId, 0, 0, 0, true, "");
                        }

                    }

                    @Override
                    public void _onError(String msg) {
                        if (getView() == null) return;
                        //  getView().hideNoCancleLoading();
                        boolean isKickedOut = TextUtils.equals("已被踢出", msg);
                        if (isChange || isKickedOut) {
                            if (isKickedOut) {
                                msg = "您已暂离直播间";
                            }
                            getView().toastTip(msg);
                        }
                        getView().setRoomNextData();
                        //     if (description == null) return;
                        //      toastError();
                    }
                }));

    }

    public void getHour(int channelId) {
        //主播直播间小时榜 当前排名
        addNet(service.getActivityHour(channelId)
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<MyHourDto>() {
                    @Override
                    public void _onNext(MyHourDto data) {
                        getView().getMyHour(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (!TextUtils.isEmpty(msg)) {
                            ToastUtils.showShort((Activity) getView(), msg + "");
                        }
                    }
                }));

    }

    private void getLastActivityInfo(int channelId) {
        //获取活动奖励接口
        addNet(service.getActivityReward(channelId)
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<LastFrameDto>() {
                    @Override
                    public void _onNext(LastFrameDto data) {
                        getView().lastActivityReward(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (!TextUtils.isEmpty(msg)) {
                            ToastUtils.showShort((Activity) getView(), msg + "");
                        }
                    }
                }));
    }
//活动和心愿单更新 升级
    public void UpActivity(int channelId) {

        Map map = new HashMap();
        map.put("channelId", channelId);
        Map userMedal = new HashMap();
        userMedal.put("channelId", channelId);
        userMedal.put("userId", uid);

        addNet(Flowable.zip(service.enterLivingRoomNewActivity(channelId),

                service.getUserWish(channelId),
                service.getLiveMedalInfo(channelId),
                new Function3<HttpResult<NewActivityDto>,
                        HttpResult<List<AnchorWishBean>>,
                        HttpResult<UserMedalBean>,
                        EnterLivingRoomDto>() {
                    @Override
                    public EnterLivingRoomDto apply(HttpResult<NewActivityDto> enterLivingRoomActDto, HttpResult<List<AnchorWishBean>> anchorWishDto, HttpResult<UserMedalBean> userMedalDto) throws Exception {
                        EnterLivingRoomDto livingRoomDto = new EnterLivingRoomDto();
                        if (enterLivingRoomActDto.isSuccess()) {//普通活动
                            livingRoomDto.newActivityDto = enterLivingRoomActDto.data;
                        }

                        if (anchorWishDto.isSuccess()) {//心愿
                            livingRoomDto.wishInfo = anchorWishDto.data;
                        } else {
                            description = anchorWishDto.description;
                        }


                        return livingRoomDto;
                    }
                }).compose(io_main()).subscribeWith(new LoadingObserver<EnterLivingRoomDto>() {
            @Override
            public void _onNext(EnterLivingRoomDto data) {

                LivingRoomBannerDto livingRoomBannerDto = new LivingRoomBannerDto();
                livingRoomBannerDto.activityDto = data.activityDto;
                livingRoomBannerDto.wishInfo = data.wishInfo;
                livingRoomBannerDto.newActivityDto = data.newActivityDto;
                setActivityStatus(livingRoomBannerDto, true);
            }

            @Override
            public void _onError(String msg) {
                if (getView() == null) return;
                getView().hideNoCancleLoading();
                if (description == null) {
                    ToastUtils.showShort((Activity) getView(), msg + "");
                } else {
                    ToastUtils.showShort((Activity) getView(), description);
                }
            }
        }));


//    addNet(
//            service.enterLivingRoomNewActivity(channelId)
//                    .compose(io_main())
//                    .subscribeWith(new LoadingObserver<HttpResult<NewActivityDto>>() {
//                        @Override
//                        public void _onNext(HttpResult<NewActivityDto> data) {
//                            LivingRoomBannerDto livingRoomBannerDto = new LivingRoomBannerDto();
//
//                            livingRoomBannerDto.newActivityDto = data.data;
//                            setActivityStatus(livingRoomBannerDto);
//                        }
//
//                        @Override
//                        public void _onError(String msg) {
//                        }
//                    })
//    );
    }

    /**
     * 用户获取活动  进入房间后 加载房间的展示信息
     *
     * @param groupId
     * @param channelId
     */
    public void loadRoomInfo(int groupId, int channelId, boolean refresh) {
        Map map = new HashMap();
        map.put("channelId", channelId);
        Map userMedal = new HashMap();
        userMedal.put("channelId", channelId);
        userMedal.put("userId", uid);

        addNet(Flowable.zip(service.enterLivingRoomNewActivity(channelId),
                service.enterLivingRoomPKActivity(createRequestBody(map)),
                service.getUserWish(channelId),
                service.getLiveMedalInfo(channelId),
                new Function4<HttpResult<NewActivityDto>,
                        HttpResult<EnterLivingRoomPkActivityDto>,
                        HttpResult<List<AnchorWishBean>>,
                        HttpResult<UserMedalBean>,

                        EnterLivingRoomDto>() {
                    @Override
                    public EnterLivingRoomDto apply(HttpResult<NewActivityDto> enterLivingRoomActDto, HttpResult<EnterLivingRoomPkActivityDto> enterLivingRoomDto, HttpResult<List<AnchorWishBean>> anchorWishDto, HttpResult<UserMedalBean> userMedalDto) throws Exception {
                        EnterLivingRoomDto livingRoomDto = new EnterLivingRoomDto();
                        if (enterLivingRoomActDto.isSuccess()) {//普通活动
                            livingRoomDto.newActivityDto = enterLivingRoomActDto.data;
                        } else {
                            description = enterLivingRoomDto.description;
                        }
                        if (enterLivingRoomDto.isSuccess()) {//pk活动
                            L.e("======" + enterLivingRoomDto.data);
                            livingRoomDto.pkActivityDto = enterLivingRoomDto.data;
                        } else {
                            description = enterLivingRoomDto.description;
                        }

                        if (anchorWishDto.isSuccess()) {//心愿
                            livingRoomDto.wishInfo = anchorWishDto.data;
                        } else {
                            description = anchorWishDto.description;
                        }

                        if (userMedalDto.isSuccess()) {//钦章
                            livingRoomDto.userMedalBean = userMedalDto.data;
                        } else {
                            description = userMedalDto.description;
                        }


                        return livingRoomDto;
                    }
                }).compose(io_main()).subscribeWith(new LoadingObserver<EnterLivingRoomDto>() {
            @Override
            public void _onNext(EnterLivingRoomDto data) {
                if (!refresh) {
                    if (data.userMedalBean.medalOneId > 0)//勋章等级  1 2 3
                        //              data.medalId.add(data.userMedalBean.medalOneId);
                        joinGroupIM(groupId, data.userMedalBean.medalOneId, 0, 0, false, "");
                    if (data.userMedalBean.medalTwoId > 0)
                        //              data.medalId.add(data.userMedalBean.medalTwoId);
                        joinGroupIM(groupId, data.userMedalBean.medalTwoId, 0, 0, false, "");
                    if (data.userMedalBean.medalThreeId > 0)
                        //              data.medalId.add(data.userMedalBean.medalThreeId);
                        joinGroupIM(groupId, data.userMedalBean.medalThreeId, 0, 0, false, "");

                    if (data.userMedalBean.isCombatCheer == 1) {
                        joinGroupIM(groupId, 0, data.userMedalBean.isCombatCheer, 0, false, "");//助力达人分享达人发一条只能一个
                    }
                    if (data.userMedalBean.isShareTalent == 1) {
                        joinGroupIM(groupId, 0, 0, data.userMedalBean.isShareTalent, false, "");//助力达人分享达人发一条只能一个
                    }
                    if (!TextUtils.isEmpty(data.userMedalBean.specialEffectUrl)) {
                        joinGroupIM(groupId, data.userMedalBean.specialEffectUrl);
                    }
                }
                getPlaneListUser(channelId, uid);//获取用户端航仓信息
                //   getUserBox(uid);//获取宝箱信息
                LivingRoomBannerDto livingRoomBannerDto = new LivingRoomBannerDto();
                livingRoomBannerDto.pkActivityDto = data.pkActivityDto;
                livingRoomBannerDto.activityDto = data.activityDto;
                livingRoomBannerDto.wishInfo = data.wishInfo;
                livingRoomBannerDto.newActivityDto = data.newActivityDto;
                setActivityStatus(livingRoomBannerDto, true);
            }

            @Override
            public void _onError(String msg) {
                if (getView() == null) return;
                getView().hideNoCancleLoading();
                if (description == null) {
                    ToastUtils.showShort((Activity) getView(), msg + "");
                } else {
                    ToastUtils.showShort((Activity) getView(), description);
                }
            }
        }));
    }

    private void toastError() {
        DialogUtil.showAlertDialog((Activity) getView(), "提示", description, "确定", "", false, new DialogUtil.AlertDialogBtnClickListener() {
            @Override
            public void clickPositive() {
                ((Activity) getView()).finish();
            }

            @Override
            public void clickNegative() {
                ((Activity) getView()).finish();
            }
        });
    }

    /**
     * 主播获取直播间活动的信息
     * <p>
     * 之前的普通活动被代替
     */
    public void getActivityMsg(int channelId) {
        Map map = new HashMap();
        map.put("channelId", channelId);
        addNet(Flowable.zip(service.enterLivingRoomNewActivity(channelId), service.enterLivingRoomPKActivity(createRequestBody(map)), service.getAnchorWish(),
                (enterLivingRoomActDto, enterLivingRoomPkActivity, anchorWishBean) -> {
                    LivingRoomBannerDto livingRoomBannerDto = new LivingRoomBannerDto();
                    if (enterLivingRoomActDto.isSuccess()) {//普通活动
                        livingRoomBannerDto.newActivityDto = enterLivingRoomActDto.data;
                    } else {
                        description = enterLivingRoomActDto.description;
                    }
                    if (enterLivingRoomPkActivity.isSuccess()) {//Pk活动
                        livingRoomBannerDto.pkActivityDto = enterLivingRoomPkActivity.data;
                    } else {
                        description = enterLivingRoomPkActivity.description;
                    }
                    if (anchorWishBean.isSuccess()) {//心愿
                        livingRoomBannerDto.wishInfo = anchorWishBean.data;
                    } else {
                        description = anchorWishBean.description;
                    }
                    return livingRoomBannerDto;
                })
                .compose(io_main())
                .subscribeWith(new LoadingObserver<LivingRoomBannerDto>() {
                    @Override
                    public void _onNext(LivingRoomBannerDto data) {
                        getPlaneListAnchor(channelId);//获取主播端航仓信息

                        setActivityStatus(data, true);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (description == null) {
                            ToastUtils.showShort((Activity) getView(), msg + "");
                        } else {
                            ToastUtils.showShort((Activity) getView(), description);
                        }
                    }
                })
        );

    }


    /**
     * 0活动未开始，1活动进行中，2奖励进行中
     *
     * @param livingRoomBanner
     * @param isShowTips       是否展示公告信息
     */
    private void setActivityStatus(LivingRoomBannerDto livingRoomBanner, boolean isShowTips) {
        getView().updateActivityScore(livingRoomBanner, 0);
        if (livingRoomBanner.pkActivityDto != null) {//pk活动
            int pkActStatus = livingRoomBanner.pkActivityDto.getActStatus();
            switch (pkActStatus) {//0活动未开始，1活动进行中，2奖励进行中，3奖励结束
                case 1://活动进行中
                    if (isShowTips) {
                        getView().activityIng(livingRoomBanner.pkActivityDto.getNotice(), livingRoomBanner.newActivityDto.getNoticeBgImg());
                    }

                    break;
                case 2://活动奖励阶段
                    getView().activityReward(livingRoomBanner.pkActivityDto);


                    break;
                case 0:
                default:
                    if (livingRoomBanner.activityDto != null) {
                        int normalActStatus = livingRoomBanner.activityDto.getActStatus();
                        if (normalActStatus == 0 || normalActStatus > 2) {//两个活动都不在进行中
                            L.e("tag", "活动调用");
                            //      getView().activityNoStart();
                        }
                    }
                    break;
            }
        }
        if (livingRoomBanner.newActivityDto != null) {//普通活动
            int normalActStatus = livingRoomBanner.newActivityDto.getActStatus();
            switch (normalActStatus) {//0活动未开始，1活动进行中，2奖励进行中，3奖励结束
                case 1://活动进行中
                        if (isShowTips)
                    getView().normalActivityIng(livingRoomBanner.newActivityDto.getNotice(), 0, "", livingRoomBanner.newActivityDto.getNickname(), livingRoomBanner.newActivityDto.getNoticeBgImg());
                    if (livingRoomBanner.newActivityDto.getActivityId() == 8) {
                        getView().updateSdj(livingRoomBanner.newActivityDto.getUserGood());
                    }
                    if (livingRoomBanner.newActivityDto.getActivityId() > 9 && livingRoomBanner.newActivityDto.getActivityId() < 18) {
                        getView().updateSdj(livingRoomBanner.newActivityDto.getUserGood());
                    }
                    if (livingRoomBanner.newActivityDto.getActivityId() == 9) {
                        getView().updateNewYear(livingRoomBanner.newActivityDto.getNewYearCountingDown());

                    }
                    break;
                case 2://活动奖励阶段
                    if (livingRoomBanner.newActivityDto.getSort() > 0 && livingRoomBanner.newActivityDto.getSort() < 10) {
                        getView().activityCpIMG(livingRoomBanner.newActivityDto.getSort() + "");
                    }
                    //     getView().normalActivityReward(livingRoomBanner.activityDto);
                    break;
                case 0:
                default:
                    if (livingRoomBanner.pkActivityDto != null) {
                        int pkActStatus = livingRoomBanner.pkActivityDto.getActStatus();
                        if (pkActStatus == 0 || pkActStatus > 2)//两个活动都不在进行中
                            L.e("tag", "活动调用2");
                        //         getView().activityNoStart();
                    }
                    break;
            }
        }


//            switch (normalActStatus) {//0活动未开始，1活动进行中，2奖励进行中，3奖励结束
//                case 1://活动进行中
//                    getView().normalActivityFestival(livingRoomBanner.festivalDto.getNotice(),livingRoomBanner.festivalDto);
//                    break;
//                case 2://活动奖励阶段
//                    getView().normalActivityReward(livingRoomBanner.festivalDto);
//                    break;
//                case 0:
//                default:
//                    if (livingRoomBanner.pkActivityDto != null) {
//                        int pkActStatus = livingRoomBanner.pkActivityDto.getActStatus();
//                        if (pkActStatus == 0 || pkActStatus > 2)//两个活动都不在进行中
//                            getView().activityNoStart();
//                    }
//                    break;
//            }

    }

    public void attention(int id, boolean isPk) {
        addNet(
                service.addAttention(id + "")
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (data.isSuccess()) {
                                    RxBus.get().post(new AttentionChangeEvent(true, isPk, 1, id + ""));
                                } else {
                                    if (getView() == null) return;
                                    ToastUtils.showShort((Activity) getView(), data.description);
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        })
        );
    }

    private Disposable mDisposableZhuboList;

    @SuppressLint("CheckResult")
    public void getZhuboList(int page, int menuId) {
        String p = page + "," + 10;
        //addNet();//不加入队列,因为新进直播间会取消请求
        mDisposableZhuboList = service.getZhuboList(p, menuId)
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<ZhuboDto>>() {
                    @Override
                    public void _onNext(BaseListDto<ZhuboDto> data) {
                        if (getView() == null) return;
                        //过滤只留下开播状态的
                        ArrayList<ZhuboDto> collect = (ArrayList<ZhuboDto>) StreamSupport.stream(data.records)
                                .filter(e -> e.status == 2)
                                .collect(Collectors.toList());
                        getView().setZhuboList(collect, page);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (getView() == null) return;
                        getView().netError();
                    }
                });
    }

    /**
     * 用于右侧列表
     *
     * @param page
     * @param menuId
     */
    public void getZhuboList1(int page, int menuId) {
        String p = page + "," + 10;
        addNet(service.getZhuboList(p, menuId)
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<ZhuboDto>>() {
                    @Override
                    public void _onNext(BaseListDto<ZhuboDto> data) {
                        if (getView() == null) return;
//                        ArrayList<ZhuboDto> collect = (ArrayList<ZhuboDto>) StreamSupport.stream(data.records)
//                                .filter(e -> e.status == 2)
//                                .collect(Collectors.toList());
                        getView().setZhuboList1(data.records, page);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (getView() == null) return;
                        getView().netError();
                    }
                }));
    }


    /**
     * 用户互相关注状态
     *
     * @param id
     */
    public void getAttentionEach(int id, boolean istype) {
        addNet(
                service.getEachAttention(id)
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<BaseEachAttention>>() {
                            @Override
                            public void _onNext(HttpResult<BaseEachAttention> data) {
                                if (data.isSuccess() && data.data != null) {
                                    getView().attentionEachSuccess(data.data.state, data.data.switchStatus, istype);
                                } else {
                                    if (getView() == null) return;
                                    ToastUtils.showShort((Activity) getView(), data.description);
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        })
        );
    }


    /**
     * 获取当前房间 观众列表  仅第一次进入直播间调用，后续加入直播间的人 通过IM 本地维护观众列表
     *
     * @param roomId
     */
    public void getWatchers(String roomId) {
        addNet(
                service.getRoomWatchers(roomId)
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<List<WatcherDto>>() {
                            @Override
                            public void _onNext(List<WatcherDto> data) {
                                if (getView() == null) return;
                                getView().setWatchers(data);
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        })
        );
    }

    public void sendDanmuMsg(int anchorId, String content, String level) {
        addNet(
                service.sendDanmu(new ParamsBuilder().put("anchorId", anchorId + "")
                        .put("level", level).put("content", content).build())
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<PopoDto>>() {
                            @Override
                            public void _onNext(HttpResult<PopoDto> data) {

                                if (getView() == null) return;
                                if (data.isSuccess()) {
                                    RxBus.get().post(new PaySuccessEvent(data.data.getExperience() + ""));
                                    getView().setUpdateExperience(data.data.getExperience());
//                                    getView().toastTip(data.description);
//                                    LivingRoomTextMsg msg = gson.fromJson(content, LivingRoomTextMsg.class);
//                                    getView().addMsg(msg);
//                                    getView().sendDanmu(msg.message, msg.bullet_screen, TextUtils.isEmpty(msg.level) ? 1 : Integer.parseInt(msg.level), msg.nickname, msg.headerurl, msg.userNickColor, msg.userMsgColor);
                                } else {
                                    getView().toastTip(data.description);
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                                if (null == getView()) return;
                                getView().toRecharge();
//                                getView().toastTip(msg);
                            }
                        })
        );
    }

    /**
     * 开始单人随机pk
     */
    public void startSingleRandomPk() {
        addNet(
                service.singleRandomPk()
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<PunishTimeDto>>() {
                            @Override
                            public void _onNext(HttpResult<PunishTimeDto> data) {
                                try {
                                    if (getView() == null) return;
                                    getView().setSingleMatch(data, true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        })
        );
    }

    /**
     * 调用获取充值列表接口
     */
    public void getRecharge() {
        addNet(
                service.getRechargeList()
                        .compose(io_main())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<List<RechargeDto>>() {
                            @Override
                            public void _onNext(List<RechargeDto> data) {
                                if (getView() == null) return;
                                getView().setRecharge(data);
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        })
        );
    }

    /**
     * 获取挂件列表
     */
    public void getPendantList() {
        addNet(
                service.getPendantList()
                        .compose(io_main())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<PendantListDto>() {
                            @Override
                            public void _onNext(PendantListDto data) {
                                if (getView() == null) return;
                                getView().setPendantList(data);
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        })
        );
    }

    /**
     * 获取礼物列表 接口
     *
     * @param isUpLevelTip : 升级快捷提示进入
     */
    public void getGift(boolean isUpLevelTip, int giftId) {
        addNet(
                service.getGifts()
                        .compose(io_main())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<List<GiftDto>>() {
                            @Override
                            public void _onNext(List<GiftDto> data) {
                                if (getView() == null) return;
                                int type = -1;
                                List<List<GiftDto>> allGifts = new ArrayList<>();
                                List<GiftDto> list = null;
                                for (int i = 0; i < data.size(); i++) {
                                    GiftDto gift = data.get(i);

                                    if (type != gift.giftType) {
                                        if (null != list) {
                                            allGifts.add(list);
                                        }
                                        list = new ArrayList<>();
                                        type = gift.giftType;
                                    }
                                    list.add(gift);
                                }
                                if (list != null) {
                                    allGifts.add(list);
                                }
                                getView().setGifts(allGifts, isUpLevelTip, giftId);
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        })
        );

    }

    /**
     * 关播
     */
    public void closeLivePush(JumpInvtationDataBean jumpInvtationDataBean) {
        addNet(
                service.closeLivePush()
                        .compose(io_main())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<CloseLiveDto>() {
                            @Override
                            public void _onNext(CloseLiveDto data) {
                                if (null == getView()) return;
                                getView().closeLivePush(data, jumpInvtationDataBean);
                            }

                            @Override
                            public void _onError(String msg) {
                                getView().toastTip(msg);
                            }
                        })
        );
    }

    //退出个人随机匹配
    public void cancleMatchSinglePk() {
        addNet(
                service.cancleMatchSinglePk()
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (null == getView()) return;
                                if (data.isSuccess())
                                    getView().cancleMatchSinglePK();
                                else
                                    ToastUtils.showShort((Context) getView(), data.description);
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        })
        );
    }

    public void getGuardList(int uid) {
        addNet(
                service.getGuardList("1,50", uid)
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<GuardListDto>() {
                            @Override
                            public void _onNext(GuardListDto data) {
                                if (null == getView()) return;
                                getView().setGuardWindow(data.total, data.records);
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        })
        );
    }

    /**
     * 组团随机pk
     */
    public void teamRandomPk(String teamId, int type) {
        Map map = new HashMap();
        map.put("teamId", teamId);
        map.put("type", type);
        addNet(
                service.teamRandomPk(createRequestBody(map))
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
//                        if (data.isSuccess())getView().teamRandomMatch(null);
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        })
        );
    }

    /**
     * 邀请好友pk（单人）
     */
    public void inviteFriendSinglePk(int userId) {
        addNet(
                service.inviteFriendSinglePk(userId)
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<PunishTimeDto>>() {
                            @Override
                            public void _onNext(HttpResult<PunishTimeDto> data) {
                                if (null == getView()) return;
                                try {
                                    getView().setSingleMatch(data, false);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        })
        );
    }

    /**
     * 同意好友邀请
     */
    public void responseFriendSingleAgree(int userId) {
        addNet(
                service.responseAgree(userId)
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (null == getView()) return;
                                if (data.isSuccess()) {
                                    try {
                                        getView().setSingleMatch(data, false);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    getView().toastTip(data.description);
                                }

                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        })
        );
    }

    /**
     * 拒绝好友邀请
     */
    public void responseFriendSingleRefulse(int userId) {
        addNet(
                service.responseRefuse(userId)
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
//                        ToastUtils.showLong((Activity) getView(), data.description);
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        })
        );
    }

    /**
     * 创建房间
     */
    public void createRoom(int type) {
        addNet(
                service.createRoom()
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (null == getView()) return;
                                try {
                                    if (data.isSuccess()) {
                                        String teamId = ((JSONObject) JSONObject.wrap(data.data)).getString("teamId");
                                        getView().createRoomSuccess(teamId);
                                    } else {
                                        getView().toastTip(data.description);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        })
        );
    }

    /**
     * 单人进行组团随机匹配
     */
    public void groupRandomSingleAdd(int type) {
        addNet(
                service.groupRandomSingleAdd()
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (null == getView()) return;
                                if (data.isSuccess()) {
                                    getView().teamRandomMatch(null);
                                } else if (Constants.PUNISH.equals(data.status)) {
//                            PunishTimeDto punishTimeDto = gson.fromJson((String) data.data, PunishTimeDto.class);
//                            long time = punishTimeDto.getTime() - System.currentTimeMillis();
//                            L.e("lgl", "punishtime:" + time);
//                            new CustomerDialog.Builder((Activity) getView())
//                                    .setTitle(data.description)
//                                    .setCountDownTimer(time)
//                                    .setNegativeBtnShow(false)
//                                    .create().show();
                                    getView().toastTip(data.description);
                                } else {
                                    getView().toastTip(data.description);
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        })
        );
    }

    /**
     * 组团邀请好友pk
     */
    public void inveteFriendTeamPk(List list, String teamId) {
        Map map = new HashMap();
        map.put("memberList", list);
        map.put("teamId", teamId);
        addNet(
                service.inviteFriendTeamPk(createRequestBody(map))
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (null == getView()) return;
                                getView().toastTip(data.description);
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        })
        );
    }

    /**
     * 单人取消邀请好友pk
     */
    public void cancelInviteFriend() {
        addNet(
                service.cancelInviteFriend()
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (null == getView()) return;
                                if (data.isSuccess()) {
                                    getView().cancleMatchSinglePK();
                                } else {
                                    ToastUtils.showShort((Activity) getView(), data.description);
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        })
        );
    }

    /**
     * 接受组团邀请 进入房间
     */
    public void intoGroupRoom(String teamId) {
        addNet(
                service.intoGroupRoom(teamId)
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (null == getView()) return;
                                if (!data.isSuccess())
                                    ToastUtils.showLong((Activity) getView(), data.description);
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        })
        );
    }

    /**
     * 退出组团匹配
     */
    public void cancelGroupPk() {
        addNet(
                service.cancelGroupPk()
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                if (null == getView()) return;
                                if (data.isSuccess()) {
                                    getView().cancleMatchSinglePK();
                                } else {
                                    ToastUtils.showShort((Activity) getView(), data.description);
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                                if (null == getView()) return;
                                ToastUtils.showLong((Activity) getView(), msg);
                            }
                        })
        );
    }

    /**
     * 检查pk是否开始
     */
    public void checkPkStart(int channelId) {
        addNet(
                service.checkPkStart(channelId)
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<MatchTeamResult>>() {
                            @Override
                            public void _onNext(HttpResult<MatchTeamResult> data) {
                                if (null == getView()) return;
                                if (data.isSuccess()) {
                                    if (data.data != null && data.data.getUserInfo() != null) {
                                        if (data.data.getMembers() != null) {//组团
                                            getView().setTeamMatchResult(data.data);
                                        } else {//单人
                                            getView().setSingleMatchResult(data.data);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        })
        );
    }


    private int reCheckTime = 0;//重试次数

    /**
     * 检查pk结果
     */
    public void checkPkResult(int channelId, boolean isSingle) {
        addNet(
                service.checkPkResult(channelId)
                        .compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
                                reCheckTime = 0;
                                if (data.isSuccess()) {
                                    String stringData = gson.toJson(data.data);
                                    if (isSingle) {
                                        handSingleResult(stringData);
                                    } else {
                                        handTeamResult(stringData);
                                    }
                                }
                            }

                            @Override
                            public void _onError(String msg) {
                                if (reCheckTime < 3) {
                                    reCheckTime++;
                                    checkPkResult(channelId, isSingle);
                                }
                            }
                        })
        );
    }

    /**
     * 查是否关注
     */
    public void isAttention(int uid) {
        addNet(service.isAttention(uid)
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (null == getView()) return;
                        if (data.isSuccess()) {
                            String stringData = gson.toJson(data.data);
                            getView().isAttention(stringData);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                }));
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposableZhuboList != null) {
            mDisposableZhuboList.dispose();
            mDisposableZhuboList = null;
        }
        countBackUtils.destory();
        mHandle.removeCallbacksAndMessages(null);
        try {
            FWWebSocket1.getInstance().removeListener(onSocketConnectListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return userInfo.nickname;
    }

    public void getTouTiaoIfExist() {
        addNet(
                service.getExistTouTiao().compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<GiftPiaopingDto>>() {
                            @Override
                            public void _onNext(HttpResult<GiftPiaopingDto> data) {
                                if (null == getView()) return;
                                if (data.isSuccess()) {
                                    if (data.data != null) getView().showToutiao(data.data);
                                } else getView().toastTip(data.description);

                            }

                            @Override
                            public void _onError(String msg) {
//                                if (null == getView()) return;
//                                getView().toastTip(msg);
                            }
                        })
        );
    }

    //观看用户查询直播间挂件
    public void getstickers(int id) {
        addNet(service.getstickers(id).compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<List<StickersDto>>>() {
                            @Override
                            public void _onNext(HttpResult<List<StickersDto>> data) {
                                if (null == getView()) return;
                                if (data.isSuccess()) {
                                    if (data.data != null) getView().getStickers(data);
                                }

                            }

                            @Override
                            public void _onError(String msg) {
//                                if (null == getView()) return;
//                                getView().toastTip(msg);
                            }
                        })
        );
    }


    //观看用户查询直播间挂件
    public void getActvity(int id) {
        addNet(service.getstickers(id).compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<List<StickersDto>>>() {
                            @Override
                            public void _onNext(HttpResult<List<StickersDto>> data) {
                                if (null == getView()) return;
                                if (data.isSuccess()) {
                                    if (data.data != null) getView().getStickers(data);
                                }

                            }

                            @Override
                            public void _onError(String msg) {
//                                if (null == getView()) return;
//                                getView().toastTip(msg);
                            }
                        })
        );
    }

    //主播查询直播间挂件
    public void getZbStickers() {
        addNet(service.getZbstickers().compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<List<StickersDto>>>() {
                            @Override
                            public void _onNext(HttpResult<List<StickersDto>> data) {
                                if (null == getView()) return;
                                if (data.isSuccess()) {
                                    if (data.data != null) getView().getStickers(data);
                                }

                            }

                            @Override
                            public void _onError(String msg) {
//                                if (null == getView()) return;
//                                getView().toastTip(msg);
                            }
                        })
        );
    }

    public void getRoomInfo(String channelId, SHARE_MEDIA shareMedia) {
        addNet(
                service.getRoomInfo(new ParamsBuilder()
                        .put("channelId", channelId).build())
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<ChannelShareInfoDto>() {
                            @Override
                            public void _onNext(ChannelShareInfoDto data) {
                                if (null == getView()) return;
                                getView().share(data, shareMedia);
                                shareAnchorMsg();
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        })
        );
    }

    //观看用户查询直播公告
    public void getNotice(int id) {
        addNet(service.getstickers(id).compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult<List<StickersDto>>>() {
                            @Override
                            public void _onNext(HttpResult<List<StickersDto>> data) {
                                if (null == getView()) return;
                                if (data.isSuccess()) {
                                    if (data.data != null) getView().getStickers(data);
                                }

                            }

                            @Override
                            public void _onError(String msg) {
//                                if (null == getView()) return;
//                                getView().toastTip(msg);
                            }
                        })
        );
    }

    //发公告
    public void getNoticeAdd(String noticeInfo) {
        addNet(service.getNoticeAdd(new ParamsBuilder()
                .put("noticeInfo", noticeInfo).build())
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (null == getView()) return;
                        if (!data.isSuccess()) {
                            getView().toastTip(data.description);
                            return;
                        }
                        getView().getAddNotice(data.data.toString(), 0, "", "");
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                })
        );
    }

    //删除公告
    public void getNoticeEdl() {
        addNet(service.getNoticeEel()
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (null == getView()) return;
                        getView().getEndNotice();
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                })
        );
    }

    //轮询公告信息
    public void getNoticeInfo() {
        addNet(service.getNoticeInfo()
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (null == getView()) return;

                    }

                    @Override
                    public void _onError(String msg) {
                    }
                })
        );
    }

    public void checkPkType(int channelId) {
        addNet(
                service.checkPkType(channelId)
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<PkTypeDto>() {
                            @Override
                            public void _onNext(PkTypeDto data) {
                                if (null == getView()) return;
                                if (data.getState() == 0)
                                    handPkEnd();
                            }

                            @Override
                            public void _onError(String msg) {
                            }
                        })
        );
    }

    //获取主播活动进度详情
    public void getActivityInfo(int channelId) {
        Map map = new HashMap();
        map.put("anchorId", channelId);
        addNet(service.getActivityInfo(createRequestBody(map))
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<GetActivityInfoDto>(getView()) {
                    @Override
                    public void _onNext(GetActivityInfoDto data) {
                        if (null == getView()) return;
                        getView().setActivityInfo(data);
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                }));
    }

    public void getPlaneListAnchor(int channelId) {
        addNet(
                service.getPlaneBoxListAnchor(channelId)
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<List<PlaneAllDto>>() {
                            @Override
                            public void _onNext(List<PlaneAllDto> data) {
                                calPlaneData(data);
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        })
        );
    }

    public void getPlaneListUser(int channelId, int uid) {
        addNet(
                service.getPlaneBoxListUser(channelId, uid)
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<List<PlaneAllDto>>() {
                            @Override
                            public void _onNext(List<PlaneAllDto> data) {
                                calPlaneData(data);
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        })
        );
    }

    public void getUserBox(int uid) {
        addNet(
                service.getUserBox(uid)
                        .compose(handleResult())
                        .subscribeWith(new LoadingObserver<List<UserBoxDto>>() {
                            @Override
                            public void _onNext(List<UserBoxDto> data) {
                                calBoxData(data);
                            }

                            @Override
                            public void _onError(String msg) {

                            }
                        })
        );
    }

    public void getAddPendant(int id, String stickerLocation, String stickerText) {
        Map map = new HashMap();
        map.put("id", id);
        map.put("stickerLocation", stickerLocation);
        map.put("stickerText", stickerText);
        addNet(service.getActivitySticers(createRequestBody(map))
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {

                    }

                    @Override
                    public void _onError(String msg) {
//                        if (!TextUtils.isEmpty(msg)) {
//                            ToastUtils.showShort((Activity) getView(), msg + "");
//                        }
                    }
                }));
    }


    public void getAddPendantpost(int id, String stickerLocation, String stickerText) {
        if (null == getView()) return;
        MvpView v = getView();
        addNet(service.getActivitySticerspost(new ParamsBuilder()
                .put("stickerId", id + "")
                .put("stickerLocation", stickerLocation + "")
                .put("stickerText", stickerText + "")
                .build())
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult<PendantDto>>(v) {
                    @Override
                    public void _onNext(HttpResult<PendantDto> data) {
                        Log.e("tag", data.toString());
                        getView().getPendant(data);

                    }

                    @Override
                    public void _onError(String msg) {
                        if (null != getView())
                            Log.e("tag", msg.toString());
                    }
                })
        );


    }

    public void getDelPendant(int id) {
        addNet(service.delSticers(id)
                .compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult>(getView()) {
                    @Override
                    public void _onNext(HttpResult data) {

                    }

                    @Override
                    public void _onError(String msg) {
                    }
                }));
    }

    public void getQuickSdMsgList() {
        addNet(new RetrofitUtils().createApi(LiveApiService.class)
                .getCommentWord(1 + "," + 50)
                .compose(RxUtils.applySchedulers2())
                .subscribeWith(new LoadingObserver<HttpResult<BaseListDto<QuickTalkDto>>>() {
                    @Override
                    public void _onNext(HttpResult<BaseListDto<QuickTalkDto>> data) {
                        if (data.isSuccess()) {
                            getView().getQuickSdMsgList(data.data.records);
                        } else {
                            ToastUtils.showShort(getContext(), data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(getContext(), msg);
                    }
                }));
    }

    private void calBoxData(List<UserBoxDto> data) {
        if (data == null || data.size() == 0) return;
        ActBannerBean bannerBean = null;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getBoxStatus() == 1) {//待开启状态
                bannerBean = new ActBannerBean();
                bannerBean.icon = data.get(i).getLevelIcon();
                bannerBean.type = 7;
                bannerBean.boxId = data.get(i).getId();
                bannerBean.planeOpenSecond = data.get(i).getBoxOpenSecond();
                bannerBean.address = data.get(i).getUrl() + "?userId=" + uid;
                bannerBean.userIsOpen = false;
                break;
            }
        }
        if (bannerBean == null) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getBoxStatus() == 0 && data.get(i).getBoxOpenSecond() > 0) {
                    bannerBean = new ActBannerBean();
                    bannerBean.icon = data.get(i).getLevelIcon();
                    bannerBean.type = 7;
                    bannerBean.boxId = data.get(i).getId();
                    bannerBean.planeOpenSecond = data.get(i).getBoxOpenSecond();
                    bannerBean.address = data.get(i).getUrl() + "?userId=" + uid;
                    bannerBean.userIsOpen = false;
                    break;
                }
            }
        }
        if (bannerBean == null) {
            bannerBean = new ActBannerBean();
            bannerBean.icon = data.get(data.size() - 1).getLevelIcon();
            bannerBean.type = 7;
            bannerBean.boxId = data.get(data.size() - 1).getId();
            bannerBean.planeOpenSecond = data.get(data.size() - 1).getBoxOpenSecond();
            bannerBean.address = data.get(data.size() - 1).getUrl() + "?userId=" + uid;
            bannerBean.userIsOpen = true;
        }
        getView().updateBannerData(bannerBean);
    }

    private void calPlaneData(List<PlaneAllDto> data) {
        if (data == null || data.size() == 0) return;
        ActBannerBean bannerBean = null;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getPlaneOpenSecond() > 0) {
                bannerBean = new ActBannerBean();
                bannerBean.icon = data.get(i).getIcon();
                bannerBean.type = 4;
                bannerBean.boxId = data.get(i).getId();
                bannerBean.planeOpenSecond = data.get(i).getPlaneOpenSecond();
                bannerBean.userIsOpen = data.get(i).isUserIsOpen();
                break;
            }
        }
        if (bannerBean == null) {
            for (int i = 0; i < data.size(); i++) {
                if (!data.get(i).isAnchorIsReach()) {
                    bannerBean = new ActBannerBean();
                    bannerBean.icon = data.get(i).getIcon();
                    bannerBean.type = 4;
                    bannerBean.boxId = data.get(i).getId();
                    bannerBean.planeOpenSecond = data.get(i).getPlaneOpenSecond();
                    bannerBean.userIsOpen = data.get(i).isUserIsOpen();
                    break;
                }
            }
        }
        if (bannerBean == null) {
            bannerBean = new ActBannerBean();
            bannerBean.icon = data.get(0).getIcon();
            bannerBean.type = 4;
            bannerBean.boxId = data.get(0).getId();
            bannerBean.planeOpenSecond = data.get(0).getPlaneOpenSecond();
            bannerBean.userIsOpen = data.get(0).isUserIsOpen();
        }
        getView().updateBannerData(bannerBean);

//        ActBannerBean planeBaner = new ActBannerBean();
//        planeBaner.type = 4;
////        planeBaner.icon = msgPlaneCompleteDto.treasureBoxIcon;
//        planeBaner.planeOpenSecond = 130;
//        planeBaner.boxId = 1;
//        getView().updateBannerData(planeBaner);

//        GiftBoardcastDto dto = new GiftBoardcastDto();
//        dto.msg = "米雪儿主播直播间告白航班将于5分钟后启航，心动礼盒等你抢";
//        dto.sendUserName = "米雪儿";
//        dto.receiveUserName = "";
//        dto.channelId = 7008217;
//        getView().showGiftBroadcast(dto);

//        GiftBean bean = new GiftBean();
//        bean.swf = "https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/gift/1597837272000*gift6389175664.mp4";
//        bean.bigName = GiftDto.getSwfName("https://fwres.oss-cn-hangzhou.aliyuncs.com/upload/videos/s-videos/gift/1597837272000*gift6389175664.mp4");
//        bean.isPlay = true;
//        bean.userid = String.valueOf(uid);
//        bean.isToutiao = true;
//        getView().showGiftAnimation(bean);

    }

    /**
     * 主播端 推流三次都失败后 就请求这个接口
     */
    public void requestDropLiveStream(String roomId) {
        addNet(service.requestDropLiveStream(roomId).compose(io_main()).subscribeWith(new ResourceSubscriber<HttpResult>() {
            @Override
            public void onNext(HttpResult httpResult) {
                getView().onDropLiveStreamSuccess();
            }

            @Override
            public void onError(Throwable t) {
                getView().onDropLiveStreamError();
            }

            @Override
            public void onComplete() {

            }
        }));
    }

}