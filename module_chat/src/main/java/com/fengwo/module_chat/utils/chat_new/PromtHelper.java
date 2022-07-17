package com.fengwo.module_chat.utils.chat_new;

import android.content.Context;

import com.fengwo.module_chat.R;

public class PromtHelper {
    public static void voiceStopedPromt(Context context) {
        MediaPlayerHelper.getInstance(context).play(R.raw.audio_voice_stoped);
    }
}
