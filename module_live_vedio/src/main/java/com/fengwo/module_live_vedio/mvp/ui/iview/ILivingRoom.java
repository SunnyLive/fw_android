package com.fengwo.module_live_vedio.mvp.ui.iview;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.bean.ActBannerBean;
import com.fengwo.module_comment.bean.JumpInvtationDataBean;
import com.fengwo.module_comment.bean.PkMatchScoreDto;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_live_vedio.mvp.UserGoodBean;
import com.fengwo.module_live_vedio.mvp.dto.ChannelShareInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.CloseLiveDto;
import com.fengwo.module_live_vedio.mvp.dto.CustomLiveEndDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomActDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomActWsJDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomPkActivityDto;
import com.fengwo.module_live_vedio.mvp.dto.EnterLivingRoomDto;
import com.fengwo.module_live_vedio.mvp.dto.FloatScreenBean;
import com.fengwo.module_live_vedio.mvp.dto.GetActivityInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.GiftBoardcastDto;
import com.fengwo.module_live_vedio.mvp.dto.GiftDto;
import com.fengwo.module_live_vedio.mvp.dto.GiftPiaopingDto;
import com.fengwo.module_live_vedio.mvp.dto.GuardListDto;
import com.fengwo.module_live_vedio.mvp.dto.LastFrameDto;
import com.fengwo.module_live_vedio.mvp.dto.LivingRoomBannerDto;
import com.fengwo.module_live_vedio.mvp.dto.MatchTeamResult;
import com.fengwo.module_live_vedio.mvp.dto.MyHourDto;
import com.fengwo.module_live_vedio.mvp.dto.PacketCountBean;
import com.fengwo.module_live_vedio.mvp.dto.PendantDto;
import com.fengwo.module_live_vedio.mvp.dto.PendantListDto;
import com.fengwo.module_live_vedio.mvp.dto.PkResultDto;
import com.fengwo.module_live_vedio.mvp.dto.PkScoreDto;
import com.fengwo.module_live_vedio.mvp.dto.PkTimeDto;
import com.fengwo.module_live_vedio.mvp.dto.PunishTimeDto;
import com.fengwo.module_live_vedio.mvp.dto.QuickTalkDto;
import com.fengwo.module_live_vedio.mvp.dto.RechargeDto;
import com.fengwo.module_live_vedio.mvp.dto.RoomMemberChangeMsg;
import com.fengwo.module_live_vedio.mvp.dto.StickersDto;
import com.fengwo.module_live_vedio.mvp.dto.WatcherDto;
import com.fengwo.module_live_vedio.widget.giftlayout.bean.GiftBean;
import com.fengwo.module_websocket.bean.InvitePkMsg;
import com.fengwo.module_websocket.bean.LivingRoomGuardMsg;
import com.fengwo.module_websocket.bean.LivingRoomTextMsg;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public interface ILivingRoom extends MvpView {
    void setWatchers(List<WatcherDto> dtos);

    void setZhuboInfo(EnterLivingRoomDto data);

    void sendDanmu(String content, String color, int userLevel, String nickname, String headUrl, String nickColor, String msgColor);

    void addMsg(LivingRoomTextMsg msgDto);

    void setGifts(List<List<GiftDto>> allGifts, boolean isUpLevelTip,int giftid);//设置礼物

    void setRecharge(List<RechargeDto> data);//设置充值界面

    void setPendantList(PendantListDto data);//获取礼物列表

    void someoneComing(FloatScreenBean floatScreenBean);//进入直播间飘屏

    void showGiftAnimation(GiftBean bean);

    void setSingleMatch(HttpResult<PunishTimeDto> result, boolean isRandom) throws JSONException;

    void closeLivePush(CloseLiveDto closeLiveDto, JumpInvtationDataBean jumpInvtationDataBean);

    void setSingleMatchResult(MatchTeamResult result);//单人匹配结果回调

    void startPk();//开始pk

    void endPk();//结束pk

//    void sendGiftSuccess(GiftDto dto, int num);

    void updatePkTime(PkTimeDto pkTimeDto);//更新pk时间\

    void updatePkGiftScore(PkScoreDto d);//更新礼物分数

    void setPkerror(String error);//pk异常

    void cancleMatchSinglePK();//取消单人pk等待窗口

    void showPkResult(PkResultDto pkResultDto, boolean isSingle);//pk结果

    void setGuardWindow(int total, ArrayList<GuardListDto.Guard> records);//设置守护列表

    void addAttentionSuccess();

    void kickedOut();//踢出直播间

    void buyGuardMsg(LivingRoomGuardMsg buyGuardMsg);

    void liveEnd(CustomLiveEndDto customLiveEndDto);//用户接收到主播退出直播间消息

    void bannedHost(String title);//封禁主播

    void setCalorific(int calorific);//设置热度
    void moveUpLoactions(String id, String url, String l, String r, String title, int isDelete, String textColor);//收到主播挂件动态

    void showReceivePkMsg(InvitePkMsg invitePkMsg, boolean isSingle);//收到邀请pk的信息

    void teamRandomMatch(List<RoomMemberChangeMsg> result);//开启组团pk回调

    void setTeamMatchResult(MatchTeamResult result);//团队匹配结果回调

    void teamPkError(int userId, int msgId);//多人pk掉线异常

    void teamInvitePkAccept(InvitePkMsg invitePkMsg);

    void showGiftBroadcast(GiftBoardcastDto msg);

    void createRoomSuccess(String roomId);

    void roomMemberChange(List<RoomMemberChangeMsg> roomMemberChangeMsgs);

    void showToutiao(GiftPiaopingDto giftPiaopingDto);

    void setNewRoomInfo(EnterLivingRoomDto data);

    void changeRoomInfo(EnterLivingRoomDto data);

    void addQipao();

    void share(ChannelShareInfoDto data, SHARE_MEDIA shareMedia);

    void hostLeave();

    void hostComeBack();

    void isAttention(String attention);

    void pkSurrender();

    void showNoCancleLoading();

    void hideNoCancleLoading();

    void clearMsg();

    void warnHost(String msg);

    void attentionEachSuccess(int status,int switchStatus,boolean istype);


    void getAttentionStatus();

    void setRoomManager(int roomManager);


    void updateActivityScore(LivingRoomBannerDto dto, int msgId);

    void updateActivityScore(PkMatchScoreDto pkMatchScoreDto);

    void updateBannerData(ActBannerBean actBannerBean);

    void updateSdj(UserGoodBean enterLivingRoomActWsJDto);

    void updateNewYear(Long enterLivingRoomActWsJDto);

    void activityNoStart();

    void activityReward(EnterLivingRoomPkActivityDto activityDto);

    void activityIng(String notice,String noticeBack);

    void normalActivityIng(String notice,int id,String color,String name ,String noticeBack);//普通活动公告

    void activityCpIMG(String  cpRank);

    void toRecharge();

    void normalActivityReward(EnterLivingRoomActDto normalActDto);

    void refreshWish();

    void updatePkRank(MatchTeamResult matchTeamResult);//更新pk排行版信息

    void lastActivityReward(LastFrameDto data);

    void getQuickSdMsgList(ArrayList<QuickTalkDto> dtos);

    void setZhuboList(List<ZhuboDto> data, int page);

    void setZhuboList1(List<ZhuboDto> data, int page);

    void setLoadmoreEnable(boolean b);

    void getMyHour(MyHourDto b);

    void getPendant(HttpResult<PendantDto> b);


    void getStickers(HttpResult<List<StickersDto>> b);
    void getFirstKill(int isOurSide, String url);//首杀动画
    void getAddNotice(String msg,int id,String color,String name);//添加公告
    void getEndNotice();//删除公告

    void setUpdateExperience(Long experience);

    void setActivityInfo(GetActivityInfoDto getActivityInfoDto); // 万圣节活动 详情界面


    void showWSJ(String msg,int id);

    void endActivity(int activityId);
    void startActivity();


    void showShouhu(Boolean isHost , int id,UserInfo data);
    void setRoomNextData();

    void requestBackpack( GiftDto sendGift);

    void onDropLiveStreamSuccess();

    void onDropLiveStreamError();
    void refreshPacketCount();
}
