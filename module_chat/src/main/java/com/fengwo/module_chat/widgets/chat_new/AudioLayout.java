package com.fengwo.module_chat.widgets.chat_new;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_chat.mvp.model.bean.chat_new.VoiceInfoBean;
import com.fengwo.module_chat.utils.chat_new.VoicePlayHelper;
import com.fengwo.module_comment.utils.DensityUtils;
import com.google.gson.Gson;

/**
 * ================================================
 * 作    者：Fuzp
 * 版    本：1.0
 * 创建日期：2018/12/24.
 * 描    述：聊天语音控件
 * 修订历史：
 * ================================================
 */
public class AudioLayout extends LinearLayout implements View.OnClickListener {

    private static final String TAG = AudioLayout.class.getSimpleName();

    LinearLayout mMain;
    TextView mTvDuration;
    TextView mTvMessageText;
    LinearLayout mLayoutVoicePrint;
    AudioPrintView mAudioPrintView;
    /**
     * 是否发出消息
     */
    private boolean mIsTo;
    private VoicePlayHelper mVoicePlayerWrapper;

    /**
     * 当前正在播放的文件
     */
    protected String mCurrentPlayFileName;

    protected VoiceInfoBean mVoiceInfoBean;

    protected boolean isNeedDump;

    public AudioLayout(Context context) {
        this(context, null);
    }

    public AudioLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public AudioLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initByChatMsgEntity(boolean isTo, String content, VoicePlayHelper voicePlayerWrapper) {
        mVoiceInfoBean = new Gson().fromJson(content, VoiceInfoBean.class);
        isNeedDump = true;
        init(isTo, voicePlayerWrapper);
    }

    public void initByVoiceBean(boolean isTo, VoiceInfoBean voiceInfoBean, VoicePlayHelper voicePlayerWrapper) {
        //来自历史消息不用转存
        isNeedDump = false;

        this.mVoiceInfoBean = voiceInfoBean;
        init(isTo, voicePlayerWrapper);

        mAudioPrintView.setPaintColor(R.color.voice_print);
        mTvDuration.setTextColor(getResources().getColor(R.color.voice_print));
    }


    protected void init(boolean isTo, VoicePlayHelper voicePlayerWrapper) {
        removeAllViews();
        View contentView = inflate(getContext(), (isTo && isNeedDump) ? R.layout.layout_audio_to : R.layout.layout_audio_come, this);
        mMain = contentView.findViewById(R.id.audio_main);
        mTvDuration = contentView.findViewById(R.id.tv_voice_duration);
        mLayoutVoicePrint = contentView.findViewById(R.id.layout_voice_print);
        mTvMessageText = contentView.findViewById(R.id.messageText);
        mTvMessageText.setVisibility(GONE);//TODO 暂时不处理语音翻译

        mMain.setOnClickListener(this);

        mIsTo = isTo;
        mVoicePlayerWrapper = voicePlayerWrapper;

        showAudioView();
    }

    /**
     * 显示消息信息布局
     */
    private void showAudioView() {
        int duration = mVoiceInfoBean.getDuration();
        String durationStr;
        int voicePrintCount = 0;//需显示的声纹数量
        if (duration >= 60) {
            durationStr = "60\"";
            voicePrintCount = 6;
        } else {
            durationStr = duration + "\"";
            if (duration <= 10) {
                voicePrintCount = 1;
            } else if (duration <= 20) {
                voicePrintCount = 2;
            } else if (duration <= 30) {
                voicePrintCount = 3;
            } else if (duration <= 40) {
                voicePrintCount = 4;
            } else if (duration <= 50) {
                voicePrintCount = 5;
            } else {
                voicePrintCount = 6;
            }
        }
        mTvDuration.setText(durationStr);

        mLayoutVoicePrint.removeAllViews();
        mAudioPrintView = new AudioPrintView(getContext());
        mLayoutVoicePrint.addView(mAudioPrintView, DensityUtils.dp2px(getContext(), 17 * voicePrintCount), DensityUtils.dp2px(getContext(), 15));

        mAudioPrintView.init(duration, mIsTo ? R.color.voice_print : R.color.white);
    }

    /**
     * 播放/停止动画
     */
    public void playVoice() {
        if (mVoicePlayerWrapper == null) {
            Log.w(TAG, "语音播放助手为空！");
            return;
        }
        // 播放动画处理
        if (mAudioPrintView.isStart()) {
            mAudioPrintView.stop();
        } else {
            mAudioPrintView.start(mIsTo);
        }
        //设置关联动画组件
        mVoicePlayerWrapper.setAudioPrintView(mAudioPrintView, mIsTo);

        // 音频逻辑处理
        if (mVoicePlayerWrapper.isEntityVoicePlaying(mVoiceInfoBean)) {
            mVoicePlayerWrapper.stopVoice();
        } else {
            mVoicePlayerWrapper.stopVoice();
            boolean playSuccess = mVoicePlayerWrapper.playByVoiceInfo(mVoiceInfoBean);// 播放声音
            if (!playSuccess) {
                // 播放失败则清空播放状态
                mVoicePlayerWrapper.clearPlayingStatus();
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.audio_main) {
            playVoice();
        }
    }
}
