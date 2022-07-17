package com.fengwo.module_flirt.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;

import butterknife.BindView;
import butterknife.OnClick;


public class VoiceNotifyView extends BaseChatRoomDialog {

    @BindView(R2.id.ci_voice_header)
    ImageView mIvVoiceHeader;
    @BindView(R2.id.tv_voice_nick_name)
    TextView mTvVoiceNickName;
    @BindView(R2.id.tv_voice_sign)
    TextView mTvVoiceSign;
    @BindView(R2.id.tv_voice_tip)
    TextView mTvVoiceTip;
    private OnChangeChatUserListener onChangeChatUserListener;

    @OnClick({R2.id.bt_send_chat_room, R2.id.iv_voice_close})
    public void onViewClick(View v) {
        if (v.getId() == R.id.bt_send_chat_room) {
            if (onChangeChatUserListener != null) {
                if (!onChangeChatUserListener.onChangeChatUser()) {
                    WenboWsChatDataBean.FromUserBean fb = mChatData.data.getFromUser();
                    WenboWsChatDataBean.RoomBean mRoom = mChatData.data.getRoom();
                    if (!mRoom.getRoomId().equals("0")) {
                        quitRoom(mChatData.data.getRoom().getRoomId());
                    }
                    checkAnchorStatus(fb.getUserId(), fb.getHeadImg());
                } else {
                    dismiss();
                }
            } else {
                WenboWsChatDataBean.FromUserBean fb = mChatData.data.getFromUser();
                WenboWsChatDataBean.RoomBean mRoom = mChatData.data.getRoom();
                if (!mRoom.getRoomId().equals("0")) {
                    quitRoom(mChatData.data.getRoom().getRoomId());
                }
                checkAnchorStatus(fb.getUserId(), fb.getHeadImg());
            }
        } else if (v.getId() == R.id.iv_voice_close) {
            dismiss();
        }
    }

    public VoiceNotifyView(Context context, SocketRequest<WenboWsChatDataBean> chatData, String msg) {
        super(context, chatData);
        initView();
        mTvVoiceTip.setText(msg);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.dialog_voice_msg);
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        setPopupGravity(Gravity.CENTER);
        ImageLoader.loadImg(mIvVoiceHeader, mChatData.data.getFromUser().getHeadImg());
        mTvVoiceNickName.setText(mChatData.data.getFromUser().getNickname() + "");
        setTip();
    }


    private void setTip() {
        mTvVoiceSign.setVisibility(View.GONE);
        if (null != mChatData.data.getGears()&&mChatData.data.getIsGears()!=null&&mChatData.data.getIsGears().equals("1")) {
            mTvVoiceSign.setVisibility(View.VISIBLE);
            switch (mChatData.data.getGears()) {
                case "0":
                    mTvVoiceNickName.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_63A5FF));
                    mTvVoiceSign.setBackgroundResource(R.drawable.bg_text_sign2);
                    mTvVoiceSign.setText(getContext().getString(R.string.char_open_fate));
                    break;
                case "2":
                    mTvVoiceNickName.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_4DC6E0));
                    mTvVoiceSign.setBackgroundResource(R.drawable.bg_text_sign3);
                    mTvVoiceSign.setText(getContext().getString(R.string.char_susan_slade));
                    break;
                case "1":
                    mTvVoiceNickName.setTextColor(ContextCompat.getColor(getContext(), R.color.color_FFC147));
                    mTvVoiceSign.setBackgroundResource(R.drawable.bg_text_sign1);
                    mTvVoiceSign.setText(getContext().getString(R.string.char_once_again));
                    break;
            }
        }
    }

    public void setOnChangeChatUserListener(OnChangeChatUserListener onChangeChatUserListener) {
        this.onChangeChatUserListener = onChangeChatUserListener;
    }

    public interface OnChangeChatUserListener {
        boolean onChangeChatUser();
    }
}
