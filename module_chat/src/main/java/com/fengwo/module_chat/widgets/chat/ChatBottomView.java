package com.fengwo.module_chat.widgets.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.utils.TimeUtils;
import com.fengwo.module_comment.utils.AudioRecoderUtils;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.chat.FaceConversionUtil;

import java.util.Date;

public class ChatBottomView extends FrameLayout {

    private final int STATUS_NONE = 0;      // 默认状态
    private final int STATUS_TEXT = 1;      // 文字状态
    private final int STATUS_VOICE = 2;     // 录音状态
    private final int STATUS_EXPAND = 3;    // 文件状态
    private final int STATUS_EMOJI = 4;     // 表情状态

    private EditText etInput;
    private TextView btnRecord;
    private View btnMulti;
    private ImageView ivEmoji;
    private ImageView ivMulti;
    private View btnSend;
    private View viewMulti;
    private View btnCamera;
    private View btnGallery;
    private View viewEmoji;
    private ViewPager vpEmoji;
    private View viewMicrophone;
    private View btnStatusRecord;
    private ImageView ivMicrophone;
    private TextView tvRecordTime;


    private int status = STATUS_NONE;
    private PanelCallback callback;
    private AudioRecoderUtils recordUtils;
    private Date audioStart;
    private Date audioStop;

    public ChatBottomView(@NonNull Context context) {
        this(context, null);
    }

    public ChatBottomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatBottomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_chat_bottom, this);

        etInput = findViewById(R.id.et_input);
        btnRecord = findViewById(R.id.tv_record_btn);
        btnMulti = findViewById(R.id.view_expand_holder);
        ivEmoji = findViewById(R.id.iv_emoji);
        ivMulti = findViewById(R.id.iv_expand);
        btnSend = findViewById(R.id.btn_send);
        btnStatusRecord = findViewById(R.id.iv_voice);
        viewMulti = findViewById(R.id.view_expand);
        btnCamera = findViewById(R.id.view_camera);
        btnGallery = findViewById(R.id.view_gallery);
        viewEmoji = findViewById(R.id.view_emoji);
        vpEmoji = findViewById(R.id.vp_emoji);
        viewMicrophone = findViewById(R.id.view_microphone);
        ivMicrophone = findViewById(R.id.iv_recording_icon);
        tvRecordTime = findViewById(R.id.tv_recording_time);

        initView();
    }

    private float startRecordPosition;
    private boolean recordCancel = false;
    private boolean recordStart = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (status == STATUS_VOICE) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN && inRangeOfView(btnRecord, event)) {
                if (!recordStart) {
                    recordStart = true;
                    audioStart = new Date();
                    btnRecord.setText("松开保存");
                    startRecordPosition = event.getRawY();
                    viewMicrophone.setVisibility(VISIBLE);
                    recordUtils.startRecord();
                    return true;
                }
            } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP && recordStart) {
                if (recordCancel) recordUtils.cancelRecord();
                else recordUtils.stopRecord();
                viewMicrophone.setVisibility(GONE);
                btnRecord.setText("按住 说话");
                recordCancel = false;
                recordStart = false;
                return true;
            } else if (action == MotionEvent.ACTION_MOVE && recordStart) {
                float y = event.getRawY();
                if (startRecordPosition - y >= 500) {
                    btnRecord.setText("取消录音");
                    recordCancel = true;
                } else {
                    btnRecord.setText("松开保存");
                    recordCancel = false;
                }
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public boolean isExpand() {
        return (status == STATUS_EXPAND || status == STATUS_EMOJI);
    }

    public boolean close() {
        if (isExpand()) {
            changeLayoutStatus(STATUS_NONE);
            return true;
        }
        return false;
    }

    public void requirFocus(boolean requireFocus) {
        if (requireFocus) etInput.requestFocus();
        else etInput.clearFocus();
    }

    public void setPanelCallback(PanelCallback callback) {
        this.callback = callback;
    }

    private void changeLayoutStatus(int currentStatus) {
        status = currentStatus;
        etInput.setVisibility(currentStatus == STATUS_TEXT || currentStatus == STATUS_EMOJI ||
                currentStatus == STATUS_NONE ? VISIBLE : GONE);
        if (currentStatus == STATUS_TEXT) {
            etInput.requestFocus();
            postDelayed(() -> KeyBoardUtils.openKeybord(etInput, getContext()), 100);
        } else postDelayed(() -> KeyBoardUtils.closeKeybord(etInput, getContext()), 100);
        btnStatusRecord.setSelected(currentStatus == STATUS_VOICE);
        ivEmoji.setSelected(currentStatus == STATUS_EMOJI);
        btnRecord.setVisibility(currentStatus == STATUS_VOICE ? VISIBLE : GONE);
        btnMulti.setVisibility(currentStatus == STATUS_EXPAND ? VISIBLE : GONE);
        postDelayed(() -> viewMulti.setVisibility(currentStatus == STATUS_EXPAND ? VISIBLE : GONE), 200);
        postDelayed(() -> viewEmoji.setVisibility(currentStatus == STATUS_EMOJI ? VISIBLE : GONE), 200);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        btnStatusRecord.setOnClickListener(v -> {
            if (status == STATUS_VOICE)
                changeLayoutStatus(STATUS_TEXT);
            else changeLayoutStatus(STATUS_VOICE);
        });
        btnMulti.setOnClickListener(v -> {
            changeLayoutStatus(STATUS_TEXT);
        });
        ivEmoji.setOnClickListener(v -> {
            if (status == STATUS_EMOJI) changeLayoutStatus(STATUS_TEXT);
            else changeLayoutStatus(STATUS_EMOJI);
        });
        ivMulti.setOnClickListener(v -> {
            if (status == STATUS_EXPAND) changeLayoutStatus(STATUS_TEXT);
            else changeLayoutStatus(STATUS_EXPAND);
        });
        btnSend.setOnClickListener(v -> {
            if (callback != null) {callback.sendText(etInput.getText().toString());
            etInput.setText("");}
        });
        btnCamera.setOnClickListener(v -> {
            if (callback != null) callback.sendCamera();
        });
        btnGallery.setOnClickListener(v -> {
            if (callback != null) callback.sendImage();
        });
        etInput.setOnClickListener(v -> {
            if (status != STATUS_TEXT) changeLayoutStatus(STATUS_TEXT);
        });
        etInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) btnSend.performClick();
            return true;
        });
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.length() == 0) btnSend.setVisibility(GONE);
                else btnSend.setVisibility(VISIBLE);
            }
        });

        EmojiVpAdapterNew emojiAdapter = new EmojiVpAdapterNew();
        emojiAdapter.setOnEmojiClickListener(str -> {
            L.e("emoji-----------", str);
            String inputStr = etInput.getText().toString() + str;
            etInput.setText(FaceConversionUtil.getSmiledText(getContext(), inputStr));
            etInput.setSelection(etInput.getText().length() - 1);
        });
        vpEmoji.setAdapter(emojiAdapter);
//        indicator.setViewPager(vpEmoji);

        recordUtils = new AudioRecoderUtils();
        recordUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                ivMicrophone.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                tvRecordTime.setText(TimeUtils.long2String(time));
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                audioStop = new Date();
                viewMicrophone.setVisibility(GONE);
                tvRecordTime.setText(TimeUtils.long2String(0));
                int recordTime = (int) TimeUtils.timeCalculateSeconds(audioStop, audioStart);
                if (callback != null) callback.sendAudio(filePath, recordTime);
            }
        });
    }

    private boolean inRangeOfView(View view, MotionEvent ev) {
//        int[] location = new int[2];
//        view.getLocationOnScreen(location);
//        int x = location[0];
//        int y = location[1];
//        return !(ev.getX() < x) && !(ev.getX() > (x + view.getWidth())) && !(ev.getY() < y) && !(ev.getY() > (y + view.getHeight()));
        return ev.getX() < view.getRight() && ev.getX() > view.getLeft() &&
                ev.getY() < view.getBottom() && ev.getY() > view.getTop();
    }
}