package com.fengwo.module_comment.utils;

import android.app.Activity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.bean.CircleListBean;
import com.fengwo.module_comment.bean.VideoHomeShortModel;
import com.fengwo.module_comment.bean.ZhuboDto;
import com.fengwo.module_comment.iservice.UserInfo;

import java.util.ArrayList;

public class ArouteUtils {

    public static void inject(Object ob) {
        ARouter.getInstance().inject(ob);
    }

    public static void toPathWithId(String path) {
        ARouter.getInstance().build(path).navigation();
    }

    public static void toPathWithId(String path, String param) {
        ARouter.getInstance().build(path)
                .withString("id", param)
                .navigation();
    }

    public static void toPathWithId(String path, long param) {
        ARouter.getInstance().build(path)
                .withLong("id", param)
                .navigation();
    }

    public static void toChatSingleActivity(String uid, String toId, String name, String headerImg) {
        ARouter.getInstance().build(ArouterApi.CHAT_SINGLE)
                .withString("fromUid", uid)
                .withString("headerImg", headerImg)
                .withString("toUid", toId)
                .withString("name", name).navigation();
    }

    public static void toGreetChatSingleActivity(String uid, String toId, String name, String content, String headerImg) {
        ARouter.getInstance().build(ArouterApi.CHAT_SINGLE)
                .withString("fromUid", uid)
                .withString("headerImg", headerImg)
                .withString("toUid", toId)
                .withString("greet", content)
                .withString("name", name).navigation();
    }


    public static void toGreetChatSingleMsgActivity(String uid, String toId, String name, String content, String headerImg) {
        ARouter.getInstance().build(ArouterApi.CHAT_SINGLE)
                .withString("fromUid", uid)
                .withString("headerImg", headerImg)
                .withString("toUid", toId)
                .withString("msg", content)
                .withString("name", name).navigation();
    }

//    public static void toChatRoomActivity(String anchorId, int status) {
//        ARouter.getInstance().build(ArouterApi.CHAT_ROOM_ACTIVITY)
//                .withString("headerImg", anchorId)
//                .withInt("name", status).navigation();
//    }

    public static void toFlirtCardDetailsActivity(int anchorId) {
        ARouter.getInstance().build(ArouterApi.FLIRT_CARD_DETAILS_ACTIVITY)
                .withInt("anchorId", anchorId).navigation();
    }

    public static void toChatGroupActivity(String uid, String toId, String name, String Avatar) {
        ARouter.getInstance().build(ArouterApi.CHAT_GROUP).withString("fromUid", uid)
                .withString("toUid", toId)
                .withString("name", name)
                .withString("avatar", Avatar)
                .navigation();
    }

    public static void toShortVideoActivity(VideoHomeShortModel videoHomeShortModel) {
        ARouter.getInstance().build(ArouterApi.SHORT_VIDEO)
                .withSerializable("model", videoHomeShortModel)
                .navigation();
    }

    public static void toShortVideoActivity(int od, int uid) {
        ARouter.getInstance().build(ArouterApi.SHORT_VIDEO)
                .withInt("albumId", od)
                .withInt("uid", uid)
                .navigation();
    }

    public static void toShortVideoActivity(int od) {
        ARouter.getInstance().build(ArouterApi.SHORT_VIDEO)
                .withInt("albumId", od)
                .navigation();
    }

    public static void toBuildSubjectActivity() {
        ARouter.getInstance().build(ArouterApi.BUILD_SUBJECT)
                .navigation();
    }


    public static void toSmallVedio(int position, ArrayList<VideoHomeShortModel> list) {
        ARouter.getInstance().build(ArouterApi.SMALLVEDIODETAILACTIVITY)
                .withSerializable("list", list)
                .withInt("position", position)
                .navigation();
    }

    public static void toLive(ArrayList<ZhuboDto> list,int position) {
        ARouter.getInstance().build(ArouterApi.LIVE_ROOM)
                .withSerializable("getlist", list)
                .withInt("getindex", position)
                .withBoolean("GET_ONE", false)
                .navigation();
    }

    public static void toCardHome(ArrayList<CircleListBean> data, String cardId, String circleId, int showSame, int uid, int mylike, int tab) {
        ARouter.getInstance().build(ArouterApi.CARD_HOME)
                .withSerializable("data", data)
                .withString("cardId", cardId)
                .withString("circleId", circleId)
                .withInt("showSame", showSame)
                .withInt("uid", uid)
                .withInt("myLike", mylike)
                .withInt("tab", tab)
                .navigation();
    }

    public static void toPostCard(Activity context) {
        ARouter.getInstance().build(ArouterApi.CHAT_POST_CARD)
                .navigation(context,1000);
    }

    /**
     * 跳转到发布动态
     */
    public static void toPostTrend(Activity context) {
        ARouter.getInstance().build(ArouterApi.CHAT_POST_TREND)
                .navigation(context,1000);
    }


    /**
     * 实名认证状态
     * @param verifyStatus
     */
    public static void toRealIdCard(int verifyStatus) {
//        ARouter.getInstance().build(ArouterApi.REALIDCARD_ACTIVITY).navigation();
        ARouter.getInstance().build(ArouterApi.FACE_VERIFY_ACTION)
                .withInt("type", Common.SKIP_USER)
                .withInt("status", verifyStatus)
                .navigation();
    }
}
