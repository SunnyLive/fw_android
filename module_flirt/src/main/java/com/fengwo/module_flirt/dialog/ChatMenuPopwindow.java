package com.fengwo.module_flirt.dialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_chat.mvp.model.bean.chat_new.VoiceInfoBean;
import com.fengwo.module_chat.widgets.chat_new.AudioLayout;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.emojiTextview.GifTextView;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.activity.ChatRoomActivity;
import com.fengwo.module_flirt.manager.ChatHistroySQLHelper;
import com.fengwo.module_flirt.manager.WenboMsgManager;
import com.fengwo.module_websocket.bean.MsgType;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;
import com.google.gson.Gson;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import razerdp.basepopup.BasePopupWindow;

/**
 * @Author BLCS
 * @Time 2020/6/19 11:33
 */
public class ChatMenuPopwindow extends BasePopupWindow {
    private static int REVOKE_EFFECTIVE_TIME = 3 * 60 * 1000;//允许撤回时间

    @BindView(R2.id.tv_revoke)
    TextView tvRevoke;
    @BindView(R2.id.view_line1)
    View viewLine1;
    @BindView(R2.id.tv_copy)
    TextView tvCopy;
    @BindView(R2.id.view_line2)
    View viewLine2;
    @BindView(R2.id.tv_remove)
    TextView tvRemove;

    private int position;
    private SocketRequest<WenboWsChatDataBean> chatData;
    private OnClickChatMenuListener onClickChatMenuListener;

    public ChatMenuPopwindow(Context context) {
        super(context);
        ButterKnife.bind(this, getContentView());
        ARouter.getInstance().inject(this);
        setPopupGravity(Gravity.BOTTOM);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_chat_menu);
        return v;
    }

    @OnClick({R2.id.tv_cancel, R2.id.tv_copy, R2.id.tv_revoke, R2.id.tv_remove})
    public void onClick(View view) {
        if (view.getId() == R.id.tv_cancel) {
        } else if (view.getId() == R.id.tv_revoke) {//撤回
            onRevoke();
        } else if (view.getId() == R.id.tv_copy) {//复制
            onCopy();
        } else if (view.getId() == R.id.tv_remove) {//删除
            onRemove();
        }
        dismiss();
    }

    public void showPopupWindow(SocketRequest<WenboWsChatDataBean> chatData, int position) {
        this.chatData = chatData;
        this.position = position;
        if (initMenu()) {
            super.showPopupWindow();
        }
    }

    /**
     * 根据不同的消息类型展示不同的menu选项
     * return 是否打开menu
     */
    private boolean initMenu() {
        switch (chatData.getItemType()) {
            case MsgType.toText://发送文本消息:
            case MsgType.toVoice://发送语音
                showRevokeMenu(true);
                showCopyMenu(true);
                break;
            case MsgType.comeText://收到文本消息:
                showRevokeMenu(false);
                showCopyMenu(true);
                break;
            case MsgType.comeVoice://收到语音
            case MsgType.fromGameMsg://划拳 骰子游戏
                showRevokeMenu(false);
                showCopyMenu(false);
                break;
            case MsgType.systemText://系统消息
            case MsgType.splashMeg://欢迎进入
            case MsgType.giftMeg://赠送礼物
            case MsgType.fromGiftMsg:
            case MsgType.toGiftMsg:
                return false;
            case MsgType.toGameMsg://发送划拳 骰子游戏
                showRevokeMenu(true);
                showCopyMenu(false);
                break;
            case MsgType.toRevocation:
            case MsgType.comeRevocation:

                return false;
        }
        return true;
    }


    /**
     * 是否显示撤回menu
     *
     * @param isShow
     */
    private void showRevokeMenu(boolean isShow) {
        if (isShow &&
                (System.currentTimeMillis() - Long.parseLong(chatData.timestamp)) <= REVOKE_EFFECTIVE_TIME) {
            tvRevoke.setVisibility(View.VISIBLE);
            viewLine1.setVisibility(View.VISIBLE);
        } else {
            tvRevoke.setVisibility(View.GONE);
            viewLine1.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示复制nemu
     *
     * @param isShow
     */
    private void showCopyMenu(boolean isShow) {
        if (isShow) {
            tvCopy.setVisibility(View.VISIBLE);
            viewLine2.setVisibility(View.VISIBLE);
        } else {
            tvCopy.setVisibility(View.GONE);
            viewLine2.setVisibility(View.GONE);
        }
    }

    /**
     * 撤回
     * 撤回需要判断是否在时间范围内
     */
    private void onRevoke() {
        if ((System.currentTimeMillis() - Long.parseLong(chatData.timestamp)) <= REVOKE_EFFECTIVE_TIME) {
            if (onClickChatMenuListener != null) {
                onClickChatMenuListener.onRevoke(chatData, position);
            }
        } else {
            ToastUtils.showShort(getContext(), "已超过3分钟,无法撤回");
        }
    }

    /**
     * 删除
     */
    private void onRemove() {
        if (onClickChatMenuListener != null) {
            onClickChatMenuListener.onRemove(chatData, position);
        }
    }

    /**
     * 复制
     */
    private void onCopy() {
        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", chatData.data.getContent().getValue());
        cm.setPrimaryClip(mClipData);
        ToastUtils.showShort(getContext(),"已复制到剪贴板");
    }

    public void setOnClickChatMenuListener(OnClickChatMenuListener onClickChatMenuListener) {
        this.onClickChatMenuListener = onClickChatMenuListener;
    }

    public interface OnClickChatMenuListener {
        void onRevoke(SocketRequest<WenboWsChatDataBean> chatData, int position);

        void onRemove(SocketRequest<WenboWsChatDataBean> chatData, int position);
    }
}
