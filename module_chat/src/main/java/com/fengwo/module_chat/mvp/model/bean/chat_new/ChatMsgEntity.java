
package com.fengwo.module_chat.mvp.model.bean.chat_new;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fengwo.module_comment.utils.chat.ChatTimeUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 聊天消息列表中的每个单元数据封装对象（本对像仅用于各聊天界面中ListView的UI显示时，不会用作别的地方）。
 */
public class ChatMsgEntity implements MultiItemEntity, Serializable {
    //======================================================== 核心数据字段 START
    /**
     * 昵称（用于显示）
     */
    private String name;
    public int userId;
    public int groupId;
    /**
     * 消息时间戳
     */
    private long date;
    private String headerImg;
    /**
     * 消息内容（注意：此消息内容可能并非文本，以不同消息定义的封装对象为准）。
     * 当前除了文件消息外（文件消息放的是{@link FileMeta对象}），其它消息类型存放的都是文本内容。
     */
    private String text;
    /**
     * 消息类型（因历史原因命名成了现在的样子，请勿被误导）
     */
    private int isComMeg = MsgType.toText;
    /**
     * 消息所对应的原始协议包指纹，目前只在发出的消息对象中有意义
     */
    private String fingerPrintOfProtocal = null;

    private boolean isGroup = false;
    //======================================================== 核心数据字段 END


    //======================================================== 专用于BBS/群聊消息的核心数据字段 START
    /**
     * 目前本字段仅用于记录BBS消息发送者的uid.且此uid主要用于获取该用户头像、查看该人员人息等之用.
     *
     * @since 2.4
     */
    private String uidForBBSCome = null;
    /**
     * 目前本字段仅用于记录BBS消息发送者的头像存放于服务端的文件名.此文件名将用于本地缓存时使用.
     *
     * @since 2.4
     */
    private String userAvatarFileNameForBBSCome;
    //======================================================== 专用于BBS/群聊消息的核心数据字段 START


    //======================================================== 辅助UI显示字段 START
    /**
     * 文字消息从网络发送的当前状态. 本字段仅针对发送的消息（而非收到的消息哦）
     */
    private int sendStatus = SendStatus.beReceived;

    /**
     * 辅助处理状态. 本字段仅针对发送的消息（而非收到的消息哦）.
     * <p>
     * 此变量通常用于发送图片、语音留言场合，因为图片文件上传到服务端的过程是
     * 一个独立的处理过程，需要和文字消息分开处理.
     */
    private int sendStatusSecondary = SendStatusSecondary.none;
    /**
     * 辅助处理状态下的进度值（0~100整数）. 本字段仅针对发送的消息（而非收到的消息哦）.
     * <p>
     * 本字段当前仅用于大文件消息的文件数据传输时（当然，你也可以用于其它消息类型，
     * 这样的值仅辅助UI显示，并非关键数据）。
     *
     * @since 4.3
     */
    private int sendStatusSecondaryProgress = 0;

    /**
     * 辅助下载状态常量.
     * <p>
     * 此常量目前仅用于语音留言的声音文件下载时提示下载进度之用（当然以后也可用作它用）.
     */
    private DownloadStatus downloadStatus = new DownloadStatus();
    //======================================================== 辅助UI显示字段 END

    public ChatMsgEntity(String name, String headerImg, long date, String text, int isComMsg,boolean isGroup) {
        this(name, headerImg, date, text, isComMsg, SendStatusSecondary.none,isGroup);
    }

    public ChatMsgEntity(String name, String headerImage, long date, String text, int isComMsg, int sendStatusSecondary,boolean isGroup) {
        super();

        this.name = name;
        this.date = date;
        this.text = text;
        this.headerImg = headerImage;
        this.isComMeg = isComMsg;
        this.isGroup = isGroup;
        this.sendStatusSecondary = sendStatusSecondary;
    }

    public ChatMsgEntity(String name, String headerImg, long date, String text, int isComMsg, String fingerPrint, Boolean isGroup, int sendStatus,int sendStatusSecondary) {
        this.name = name;
        this.date = date;
        this.text = text;
        this.headerImg = headerImg;
        this.isComMeg = isComMsg;
        this.fingerPrintOfProtocal = fingerPrint;
        this.isGroup = isGroup;
        this.sendStatus = sendStatus;
        this.sendStatusSecondary = sendStatusSecondary;
    }

    public String getName() {

        return TextUtils.isEmpty(name) ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    /**
     * date字段存放的是GMT+0时区的时间戳，UI显示时要转换成人类友好的形式哦.
     *
     * @return 转换成功则返回日期时间字串，否则返回空字符串""
     */
    public String getDateHuman() {
        return ChatTimeUtils.getTimeStringAutoShort(new Date(date));
    }

    public void setDate(long date) {
        this.date = date;
    }

    /**
     * 返回消息体（对于文本消息而言，它就是消息字串，而对于复杂的消息而言，本字段值可能是一个对象的JSON序列化字串）。
     *
     * @return
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMsgType() {
        return isComMeg;
    }

    public void setMsgType(int isComMsg) {
        isComMeg = isComMsg;
    }

    public String getFingerPrintOfProtocal() {
        return fingerPrintOfProtocal;
    }

    public ChatMsgEntity setFingerPrintOfProtocal(String fingerPrintOfProtocal) {
        this.fingerPrintOfProtocal = fingerPrintOfProtocal;
        return this;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public int getSendStatusSecondary() {
        return sendStatusSecondary;
    }

    public void setSendStatusSecondary(int sendStatusSecondary) {
        this.sendStatusSecondary = sendStatusSecondary;
    }

    public DownloadStatus getDownloadStatus() {
        return downloadStatus;
    }

    public int getSendStatusSecondaryProgress() {
        return sendStatusSecondaryProgress;
    }

    public void setSendStatusSecondaryProgress(int sendStatusSecondaryProgress) {
        this.sendStatusSecondaryProgress = sendStatusSecondaryProgress;
    }

    /**
     * 是否"我"发出的消息
     */
    public boolean isOutgoing() {
        return this.isComMeg == MsgType.toText
                || this.isComMeg == MsgType.toFile
                || this.isComMeg == MsgType.toImage
                || this.isComMeg == MsgType.toForwardMerge
                || this.isComMeg == MsgType.toVoice;
    }

    @Override
    public int getItemType() {
        return isComMeg;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }
//------------------------------------------------------------------------------------------- 实用方法 END


    //------------------------------------------------------------------------------------------- 内部类 START
    public static class MsgType implements Serializable {
        /**
         * 　发出的消息：普通文本
         */
        public final static int toText = 0;
        /**
         * 　收到的消息：普通文本
         */
        public final static int comeText = 1;

        /**
         * 　发出的消息：图片
         */
        public final static int toImage = 2;
        /**
         * 　收到的消息：图片
         */
        public final static int comeImage = 3;

        /**
         * 　发出的消息：语音留言
         */
        public final static int toVoice = 4;
        /**
         * 　收到的消息：语音留言
         */
        public final static int comeVoice = 5;

        /**
         * 　发出的消息：文件
         */
        public final static int toFile = 6;
        /**
         * 　收到的消息：文件
         */
        public final static int comeFile = 7;

        /**
         * 　收到的系统级消息(比如一些群通知、系统通知等)
         */
        public final static int systemText = 14;

        /**
         * 　发出的消息：视频
         */
        public final static int toVideo = 8;
        /**
         * 　收到的消息：视频
         */
        public final static int comeVideo = 9;

        /**
         *  撤回的消息
         */
        public final static int revocation = 12;

        /**
         * 收到合并转发消息
         */
        public final static int toForwardMerge = 10;
        public final static int comeForwardMerge = 11;

        /**
         * 　被邀请入群
         */
        public final static int inviteIntoGroup = 46;

        /**
         * 提示未关注的系统消息
         */
        public final static int systemTxtAttention = 47;

//        /**
//         * 提示未关注的系统消息
//         */
//        public final static int systemTxtOfficial = 48;
    }

    /**
     * 文字消息的发送状态常量.
     */
    public interface SendStatus {
        /**
         * 消息发送中
         */
        int sending = 1;
        /**
         * 消息已被对方收到（我方已收到应答包）
         */
        int beReceived = 0;
        /**
         * 消息发送失败（在超时重传的时间内未收到应答包）
         */
        int sendFaild = 2;
    }

    /**
     * 辅助发送状态常量.
     * <p>
     * 此常量通常用于发送图片、语音留言场合，因为图片文件上传到服务端的过程是
     * 一个独立的处理过程，需要和文字消息分开处理.
     */
    public interface SendStatusSecondary {
        /**
         * 无需处理
         */
        int none = 0;
        /**
         * 等待处理
         */
        int pending = 1;
        /**
         * 处理中
         */
        int processing = 2;
        /**
         * 成功处理完成
         */
        int processOk = 3;
        /**
         * 处理失败
         */
        int processFaild = 4;
    }

    /**
     * 辅助下载状态常量.
     * <p>
     * 此常量目前语音留言的声音文件下载时提示下载进度之用.
     */
    public static class DownloadStatus implements Serializable {
        /**
         * 无需处理
         */
        public final static int NONE = 0;
        /**
         * 处理中
         */
        public final static int PROCESSING = 1;
        /**
         * 成功处理完成
         */
        public final static int PROCESS_OK = 2;
        /**
         * 处理失败
         */
        public final static int PROCESS_FAILD = 3;

        private int status = NONE;
        private int progress = 0;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }
    }

    /**
     * 辅助发送状态的处理结果观察者统一接口.
     */
    public interface SendStatusSecondaryResult {
        /**
         * 状态变迁到“处理中”状态时要调用的方法
         */
        void processing();

        /**
         * 状态变迁到“处理失败”时要调用的方法
         */
        void processFaild();

        /**
         * 状态变迁到“处理成功”时要调用的方法
         */
        void processOk();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj ==null) return false;
        ChatMsgEntity entity = (ChatMsgEntity) obj;
        return this.fingerPrintOfProtocal.equals(entity.fingerPrintOfProtocal);
    }

    //------------------------------------------------------------------------------------------- 内部类 END

    @Override
    public String toString() {
        return "ChatMsgEntity{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", isComMeg=" + isComMeg +
                ", fingerPrintOfProtocal='" + fingerPrintOfProtocal + '\'' +
                ", uidForBBSCome='" + uidForBBSCome + '\'' +
                ", userAvatarFileNameForBBSCome='" + userAvatarFileNameForBBSCome + '\'' +
                ", sendStatus=" + sendStatus +
                ", sendStatusSecondary=" + sendStatusSecondary +
                ", sendStatusSecondaryProgress=" + sendStatusSecondaryProgress +
                ", downloadStatus=" + downloadStatus +
                '}';
    }
}
