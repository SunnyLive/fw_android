package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.bean.JumpInvtationDataBean;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_comment.bean.ReceiveSocketBean;
import com.fengwo.module_flirt.bean.EnterRoomBean;
import com.fengwo.module_flirt.bean.GetAnchorRoomInfo;
import com.fengwo.module_flirt.bean.ImpressDTO;
import com.fengwo.module_flirt.bean.MessageListVO;
import com.fengwo.module_flirt.bean.ShareInfoBean;
import com.fengwo.module_websocket.bean.ReceiveCommentMsg;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.StatFateMsg;
import com.fengwo.module_websocket.bean.WebboBulletin;
import com.fengwo.module_websocket.bean.WenboGiftMsg;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author BLCS
 * @Time 2020/4/6 11:50
 */
public interface IChatRoomView extends MvpView {

    void enterRoomSuccess(EnterRoomBean data);

    void enterRoomFailure(String meg);

    void quitRoomSuccess(HttpResult data, boolean isShow);

    void quitRoomSuccessOut();

    void quitRoomFailure(String msg);

    void hostEnd(HttpResult result, JumpInvtationDataBean jumpInvtationDataBean);

    void orderReceiveSuccess(HttpResult data);

    void updatetime(long time);

    void updateMsgStatus(String msgId);

    void updateMsgFaild(String msgId);

    void addGift(WenboGiftMsg gift, boolean istype);

    void anchorClose(ReceiveSocketBean gift);

    void acceptOrder(ReceiveSocketBean bean);

    void isAttention(String stringData);

    void addHistoryMsg(boolean isRefresh, ArrayList<SocketRequest<WenboWsChatDataBean>> records);

    void toastWarn(ReceiveSocketBean bean);

    void toastBanned(ReceiveSocketBean bean);

    void refuseOrder(String value);


    void receiveCancelOrder(ReceiveSocketBean cancelOrder);

    void setMyOrderList(List<MyOrderDto> data);

    void receiveMsg(SocketRequest<WenboWsChatDataBean> chatBean);

    void addTimeTip(String msg);

    void getAnchorRoomInfoSuccess(GetAnchorRoomInfo data);

    void getAnchorRoomInfoFailure(String description);


    void isWaitStatus(String msg);

    void getOrderNum(int orderNum, boolean clickAdd);

    void notifyBulletinMsg(WebboBulletin webboBulletin);//????????????


    void getShareInfoSuccess(ShareInfoBean data);

    /**
     * 获取到观看的用户列表
     *
     * @param data
     */
    void onGetChatUsers(List<MessageListVO> data);


    /**
     * 当有进入直播间
     *
     * @param bulletin
     */
    void onUserEnterRoom(WebboBulletin bulletin);

    /**
     * 当有退出直播间
     *
     * @param bulletin
     */
    void onUserExitRoom(WebboBulletin bulletin);

    /**
     * 当接受到缘分礼物
     *
     * @param statFate
     */
    void onStatFate(StatFateMsg statFate);

    /**
     * 获取到缘分信息
     * @param impressDTO
     */
    void onGetImpress(ImpressDTO impressDTO);

    /**
     * 接收到评价消息
     */
    void onReceiveComment(ReceiveCommentMsg receiveCommentMsg);

    /**
     * 语音检测失败
     * @param msg
     */
    void onVoiceFailed(String msg);

}
