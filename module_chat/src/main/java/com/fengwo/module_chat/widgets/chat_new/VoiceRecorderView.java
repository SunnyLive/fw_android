package com.fengwo.module_chat.widgets.chat_new;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.utils.chat_new.VoicePlayHelper;
import com.fengwo.module_chat.utils.chat_new.VoiceRecorder;
import com.fengwo.module_comment.utils.DeviceUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @author ZhouZhBao on 2018/12/14
 */
public class VoiceRecorderView extends RelativeLayout {

    private static final String TAG = "VoiceRecorderView";

    private TextView tvHoldTalk;

    private LinearLayout llVoice;

    private VoiceRecorder mVoiceRecorder;
    private Handler mDialogHandler;
    private static final int CANCEL_RECORD = -300;
    private static final int MIN_TIME = 1000;
    private final static int MAX_TIME = 60000;
    private boolean isTooShort = true;


    private View ChatDialogView;
    private ImageView ivDialogShow;
    private TextView tvDialogHint;
    private Dialog recordDialog;
    private long mStartTime;
    private boolean isFirst;

    private float y;
    /*允许手指上滑距离*/
    private final int FINGER_MOVEMENT = -80;

    //private AnimationDrawable anim;

    private Context mContext;
    /* 完成录音回调*/
    private OnFinishedRecordListener finishedListener;
    /**
     * 语音播放工具
     */
    private VoicePlayHelper mVoicePlayerWrapper;
    private Disposable countDownTimer;

    private int bgColor = R.drawable.shape_edittext_bg_gray_stroke;
    private int textColor = getResources().getColor(R.color.black);
    private int currentRecodingGif = 0;

    public VoiceRecorderView(Context context) {
        super(context);
        init(context);
    }

    public VoiceRecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoiceRecorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_voice_recorder, this);

        llVoice = findViewById(R.id.ll_voice);

        tvHoldTalk = findViewById(R.id.tv_hold_talk);
        // llVoice.setBackgroundResource(bgColor);
        tvHoldTalk.setTextColor(textColor);
        llVoice.setOnTouchListener(new VoiceRecorderViewTouchListenner());/*设置按钮触摸事件*/


        initHandler();
        mVoiceRecorder = new VoiceRecorder(mDialogHandler);
    }

    public void setBackgroundResource(int res) {
        bgColor = res;
        llVoice.setBackgroundResource(res);
    }

    int newbgColor = 0;

    public void setBackgroundResourceChat(int res) {
        newbgColor = res;

    }

    public void setTextColor(int color) {
        textColor = color;
        tvHoldTalk.setTextColor(color);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (countDownTimer != null && !countDownTimer.isDisposed()) {
            countDownTimer.dispose();
        }
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mDialogHandler = new Handler() {
            private int lastWhat = -1;

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == CANCEL_RECORD) {
                    onCancelRecord();
                    if (recordDialog != null) {
                        recordDialog.dismiss();
                    }
                } else if (msg.what != -1) {
                    if (!isTooShort && lastWhat != msg.what) {
                        lastWhat = msg.what;
                        Log.i(TAG, "AUDIO AMPLITUDE ==== " + lastWhat);
                        if (msg.what > 5) {
                            lastWhat = 5;
                        }
                        //   ImageLoader.loadGif(ivDialogShow,R.drawable.icon_recoding_gif);
                        //    ivDialogShow.setBackgroundResource(VOICE_IMAGE_RES[lastWhat]);
                    }
                }
            }
        };
    }

    private void initDialogAndStartRecord() {
        currentRecodingGif = 0;
        mStartTime = System.currentTimeMillis();
        recordDialog = new Dialog(mContext, R.style.chat_recode_dialog_style);
        ChatDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_chat_recode, null);
        ivDialogShow = ChatDialogView.findViewById(R.id.iv_chat_recode);
        // ivDialogShow.setImageResource(R.drawable.icon_recoding_01);
        tvDialogHint = ChatDialogView.findViewById(R.id.tv_chat_recode_hint);
        tvDialogHint.setText(mContext.getString(R.string.finger_slide_cancel_send));
        recordDialog.setContentView(ChatDialogView, new LinearLayout.LayoutParams(
                (int) getResources().getDimension(R.dimen.dp_160),
                (int) getResources().getDimension(R.dimen.dp_160)));
        WindowManager.LayoutParams lp = recordDialog.getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        recordDialog.show();
        isTooShort = false;
        isFirst = true;
        onStartRecord();
    }

    private void onStopRecord() {
        VoiceRecorder.VoiceRecorderBean voiceRecorderBean = null;
        try {
            voiceRecorderBean = mVoiceRecorder.stopRecoding();
        } catch (Exception e) {
            ToastUtils.showLong(getContext().getApplicationContext(), "存储出错，请检测存储功能是否正常");
            L.e("=========" + e.getMessage());
            e.printStackTrace();
        }
        if (null != finishedListener && null != voiceRecorderBean) {
            if (voiceRecorderBean.getRecord_state() == VoiceRecorder.RECORDER_SUCCESS) {
//                LogUtil.e(TAG,"RECORD STATE = " + voiceRecorderBean.getRecord_state() +"VOICE PATH = " + voiceRecorderBean.getRecord_path() + "VOICE TIME LENGTH = " + voiceRecorderBean.getRecord_time());
                finishedListener.onFinishedRecord(voiceRecorderBean.getRecord_path(), voiceRecorderBean.getRecord_time());
            }
        }
    }


    private void onStartRecord() {
        startCountdown();
        mVoiceRecorder.startRecording(mContext);
    }

    private void startCountdown() {
        countDownTimer = Observable.timer(60 * 1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    onFinishRecord(60 * 1000);
                    tvHoldTalk.setText(mContext.getString(R.string.hold_talk));

                    llVoice.setBackgroundResource(bgColor);
                }, Throwable::printStackTrace);
    }

    private void stopCountdown() {
        if (countDownTimer != null && !countDownTimer.isDisposed()) {
            countDownTimer.dispose();
            countDownTimer = null;
        }
    }

    public void setVoicePlayerWrapper(VoicePlayHelper voicePlayerWrapper) {
        mVoicePlayerWrapper = voicePlayerWrapper;
    }

    /**
     * 音频录制视图触摸事件监听
     */
    private class VoiceRecorderViewTouchListenner implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            llVoice.requestDisallowInterceptTouchEvent(true);/*设置父View不在拦截当前View事件*/

            int action = event.getAction();
            y = event.getY();

            if (!isTooShort) {
                if (ivDialogShow != null && !(y > 0 && y < getHeight())) {
                    setDialogShowUI(R.drawable.icon_recoding_gif, R.string.finger_loosening_cancel_send);
                } else if (ivDialogShow != null) {
                    setDialogShowUI(R.drawable.icon_recoding_gif_start, R.string.finger_slide_cancel_send);
                }
            }

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (mVoicePlayerWrapper != null) {//存在正在播放的语音则停止播放
                        mVoicePlayerWrapper.stopVoice();
                    }

                    if (!DeviceUtils.validateMicAvailability()) {
                        KLog.e(TAG, "麦克风已被占用");
                        ToastUtils.showLong(getContext().getApplicationContext(), "麦克风已被占用");
                        return false;
                    }

                    tvHoldTalk.setText(mContext.getString(R.string.release_end));
                    if (newbgColor != 0) {
                        llVoice.setBackgroundResource(newbgColor);
                    } else
                        llVoice.setBackgroundResource(bgColor);
                    initDialogAndStartRecord();
                    setDialogShowUI(R.drawable.icon_recoding_gif_start, R.string.finger_slide_cancel_send);
                    break;
                case MotionEvent.ACTION_UP:
                    tvHoldTalk.setText(mContext.getString(R.string.hold_talk));
                    llVoice.setBackgroundResource(bgColor);
                    int diffTime = (int) (System.currentTimeMillis() - mStartTime);
                    if (y >= FINGER_MOVEMENT && (diffTime <= MAX_TIME)) {
                        onFinishRecord(diffTime);
                    } else if (y < FINGER_MOVEMENT) {  //当手指向上滑，会cancel
                        onCancelRecord();
                        if (recordDialog != null) {
                            recordDialog.dismiss();
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL: // 取消
                    tvHoldTalk.setText(mContext.getString(R.string.hold_talk));
                    llVoice.setBackgroundResource(bgColor);
                    onCancelRecord();
                    if (recordDialog != null) {
                        recordDialog.dismiss();
                    }
                    break;
            }
            return true;
        }
    }

    private void setDialogShowUI(int p, int p2) {
        if (currentRecodingGif != p) {
            currentRecodingGif = p;
            if (ivDialogShow != null) {
                ImageLoader.loadGif(ivDialogShow, p);
            }
            if (tvDialogHint != null) {
                tvDialogHint.setText(mContext.getString(p2));
            }
        }
    }

    private void onCancelRecord() {
        stopCountdown();
        if (mVoiceRecorder != null) {
            mVoiceRecorder.discardRecording();
        }
    }

    /**
     * 完成录音
     */
    @SuppressLint("CheckResult")
    private void onFinishRecord(int diffTime) {
        if (diffTime < MIN_TIME) {
            mDialogHandler.sendEmptyMessageDelayed(CANCEL_RECORD, 1000);
            if (ivDialogShow != null) {
                ivDialogShow.setImageResource(R.drawable.icon_error_recode);
            }
            if (tvDialogHint != null) {
                tvDialogHint.setText(mContext.getString(R.string.Recording_time_is_too_short));
                tvDialogHint.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
            }
//            anim.stop();
            isTooShort = true;
            Observable.timer(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        if (recordDialog != null) {
                            recordDialog.dismiss();
                        }
                    }, Throwable::printStackTrace);
            return;
        } else {
            onStopRecord();
            if (recordDialog != null) {
                recordDialog.dismiss();
            }
        }
        stopCountdown();
    }

    public interface OnFinishedRecordListener {
        public void onFinishedRecord(String audioPath, int time);
    }

    public void setOnFinishedRecordListener(OnFinishedRecordListener listener) {
        finishedListener = listener;
    }
}
