package com.fengwo.module_chat.widgets.chat_new;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.utils.chat_new.VoicePlayHelper;
import com.fengwo.module_comment.utils.EventIntervalUtil;

import java.util.Observer;

/**
 * ================================================
 * 作    者：Fuzp
 * 版    本：1.0
 * 创建日期：2018/11/2.
 * 描    述：聊天输入组件
 * 修订历史：
 * ================================================
 */
public class ChatInput extends LinearLayout implements View.OnClickListener,
        TextWatcher, View.OnFocusChangeListener, VoiceRecorderView.OnFinishedRecordListener {
    private static final String TAG = ChatInput.class.getSimpleName();
    private static final int DEFAULT_DELAY_TYPING_STATUS = 1500;



    protected ImageView ivRecode;
    protected ImageView ivExpression;
    protected ImageView ivMore;
    protected ImageView ivAdd;
    protected VoiceRecorderView rbPressRecode;
    protected TextView tvSend;
    protected MsgEditText etInput;
    protected TextView tvBan;

    private boolean isTyping;
    private int delayTypingStatusMillis;
    private boolean lastFocus;
    private CharSequence input;
    private InputListener inputListener;
    private AttachmentsListener attachmentsListener;
    private RecodeListener recodeListener;
    private TypingListener typingListener;
    private boolean isRecordMode = false;

    /**
     * 作为群聊控件时被踢出要控制不允许输入
     */
    private boolean isInGroup = true;
    /**
     * 作为群聊控件时被禁言要做出处理
     */
    private boolean isBan = false;
    /**
     * 作为群聊控件时全体禁言要做出处理
     */
    private boolean isTotalBan = false;

    private boolean moreVisible = false;

    private boolean textChangeAble = true;

    private final static String MASK_STR = "@";

    private Runnable typingTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isTyping) {
                isTyping = false;
                if (typingListener != null) typingListener.onStopTyping();
            }
        }
    };

    private TextChangeListener changeListener;

    public ChatInput(Context context) {
        super(context);
        init(context);
    }

    public ChatInput(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatInput(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * @param isIn 是否在群聊的群中
     */
    public void setIsInGroup(boolean isIn) {
        isInGroup = isIn;
    }

    /**
     * @param ban 是否被禁言
     */
    public void setIsBan(boolean ban) {
        isBan = ban;
        refreshInputView();
    }

    public boolean isTextChangeAble() {
        return textChangeAble;
    }

    public void setTextChangeAble(boolean textChangeAble) {
        this.textChangeAble = textChangeAble;
    }

    /**
     * @param totalBan 是否全体禁言
     */
    public void setIsTotalBan(boolean totalBan) {
        isTotalBan = totalBan;
        refreshInputView();
    }

    @Override
    public void onClick(View view) {
        if (EventIntervalUtil.canOperate()) {
            if (!isInGroup) {
                Toast.makeText(getContext(), "您已不在该群！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isTotalBan) {
                return;
            }
            if (isBan) {
                return;
            }
            int id = view.getId();
            if (id == R.id.iv_input_recode) {//语音
                onRecodeAttachments();
            } else if (id == R.id.iv_input_expression) {//表情
                rbPressRecode.setVisibility(GONE);
                etInput.setVisibility(VISIBLE);
                isRecordMode = false;
                refreshInputView();
                onExpressionAttachments();
            } else if (id == R.id.iv_input_more) {//更多
                onMoreAttachments();
            } else if (id == R.id.iv_input_add) {//添加
                moreVisible = !moreVisible;
                onAddAttachments(moreVisible);
            } else if (id == R.id.tv_send) {//发送
                if (TextUtils.isEmpty(etInput.getText().toString().trim())) {
                    Toast.makeText(getContext(), "不能发送空白信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                onSubmit((o, arg) -> {
                });
                etInput.setText("");
                if (changeListener != null)
                    changeListener.onAtSuccess();
                removeCallbacks(typingTimerRunnable);
                post(typingTimerRunnable);
            }
        }
    }

    public void refreshInputView() {
        etInput.setVisibility(isRecordMode ? GONE : VISIBLE);
        rbPressRecode.setVisibility(isRecordMode ? VISIBLE : GONE);
        ivRecode.setImageResource(isRecordMode ?
                R.drawable.ic_input_keyboard : R.drawable.ic_chat_single_voice);
        if (isRecordMode) {
            changeSendView(false);
        } else {
            changeSendView(input != null && input.length() > 0);
        }

        if (isTotalBan) {
            tvBan.setVisibility(VISIBLE);
            tvBan.setText(R.string.total_ban);
            etInput.setVisibility(GONE);
            rbPressRecode.setVisibility(GONE);
            return;
        }

        if (isBan) {
            tvBan.setVisibility(VISIBLE);
            tvBan.setText(R.string.forbidden_words);
            etInput.setVisibility(GONE);
            rbPressRecode.setVisibility(GONE);
        }

        tvBan.setVisibility(isTotalBan || isBan ? VISIBLE : GONE);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int len) {
        input = charSequence;
        boolean canSend = input.length() > 0;
        changeSendView(canSend);
        if (canSend) {
            if (!isTyping) {
                isTyping = true;
                if (typingListener != null) typingListener.onStartTyping();
            }
            removeCallbacks(typingTimerRunnable);
            postDelayed(typingTimerRunnable, delayTypingStatusMillis);
            if (textChangeAble) {
                if (changeListener != null) {
                    if (len == 1 && charSequence.charAt(charSequence.length() - 1) == MASK_STR.charAt(0)) { //添加一个字
                        //跳转到@界面
//                        etInput.addAtSpan(null, str[aaa], 2000);
                        changeListener.onTextChange(start > 0 ? charSequence.toString().substring(start - 1, start) : charSequence.toString());
                    }
                }
            }
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        int id = view.getId();
        if (id == R.id.et_input_text) {
            if (hasFocus) {
                onStartInput();
            } else {
                onFinishInput();
            }
        }
        if (lastFocus && !hasFocus && typingListener != null) {
            typingListener.onStopTyping();
        }
        lastFocus = hasFocus;
    }

    @SuppressLint({"HandlerLeak", "ClickableViewAccessibility"})
    private void init(Context context) {

        delayTypingStatusMillis = DEFAULT_DELAY_TYPING_STATUS;
        inflate(context, R.layout.layout_chat_input, this);

        etInput = findViewById(R.id.et_input_text);
        tvBan = findViewById(R.id.tv_input_ban);
        ivRecode = findViewById(R.id.iv_input_recode);
        ivExpression = findViewById(R.id.iv_input_expression);
        ivMore = findViewById(R.id.iv_input_more);
        ivAdd = findViewById(R.id.iv_input_add);
        tvSend = findViewById(R.id.tv_send);
        rbPressRecode = findViewById(R.id.rb_press_recode);
        etInput.addTextChangedListener(this);
        etInput.setOnFocusChangeListener(this);
        ivRecode.setOnClickListener(this);
        ivExpression.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        rbPressRecode.setOnFinishedRecordListener(this);
        rbPressRecode.setBackgroundResource(R.drawable.shape_edittext_bg_gray_stroke);
        rbPressRecode.setTextColor(getResources().getColor(R.color.black));
        refreshInputView();
    }

    private void changeSendView(boolean canSend) {
//        ivMore.setVisibility(canSend ? View.GONE : View.VISIBLE);
        ivMore.setVisibility(View.GONE);
        ivAdd.setVisibility(canSend ? View.GONE : View.VISIBLE);
        tvSend.setVisibility(canSend ? View.VISIBLE : View.GONE);
    }

    /*------------------------------------------- WIDGET EVENT STATR ---------------------------------------------*/

    private boolean onSubmit(Observer observer) {
        return inputListener != null && inputListener.onSubmit(observer, input);
    }

    private void onStartInput() {
        if (inputListener != null) inputListener.onFocusInput();
    }

    private void onFinishInput() {
        if (inputListener != null) inputListener.unFocusInput();
    }

    private void onRecodeAttachments() {
        if (attachmentsListener != null) attachmentsListener.onRecodeAttachments(isRecordMode);
    }

    private void onExpressionAttachments() {
        if (attachmentsListener != null) attachmentsListener.onExpressionAttachments();
    }

    private void onMoreAttachments() {
        if (attachmentsListener != null) attachmentsListener.onMoreAttachments();
    }

    private void onAddAttachments(boolean moreVisible) {
        if (attachmentsListener != null) attachmentsListener.onAddAttachments(moreVisible);
    }

    /**
     * 录制成功回调
     *
     * @param path     文件路径
     * @param duration 录音时长
     */
    private void onRecordSuccess(String path, int duration) {
        if (recodeListener != null) recodeListener.onRecordSuccess(path, duration);
    }

    /**
     * 设置录音文件存储标识（用于创建存储文件夹名称）
     */
    public void setRecodeId(String uId) {
//        rbPressRecode.setId(uId);
    }

    public void setInputListener(InputListener inputListener) {
        this.inputListener = inputListener;
    }

    public void setAttachmentsListener(AttachmentsListener attachmentsListener) {
        this.attachmentsListener = attachmentsListener;
    }

    public void setRecodeListener(RecodeListener recodeListener) {
        this.recodeListener = recodeListener;
    }

    public MsgEditText getInputEditText() {
        return etInput;
    }

    public TextView getButton() {
        return tvSend;
    }

    public ImageView getIvRecode() {
        return ivRecode;
    }

    public ImageView getIvExpression() {
        return ivExpression;
    }

    public ImageView getIvMore() {
        return ivMore;
    }

    public ImageView getIvAdd() {
        return ivAdd;
    }

    public void setRecordMode(boolean recordMode) {
        isRecordMode = recordMode;
    }

    public boolean isRecordMode() {
        return isRecordMode;
    }

    @Override
    public void onFinishedRecord(String audioPath, int time) {
        Log.i(TAG + "onFinishedRecord", "VOICE PATH == " + audioPath + "VOICE DURATION ==" + time);
        onRecordSuccess(audioPath, time);
    }

    public void setVoicePlayerWrapper(VoicePlayHelper voicePlayerWrapper) {
        rbPressRecode.setVoicePlayerWrapper(voicePlayerWrapper);
    }

    public void setChangeListener(TextChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    /*------------------------------------------- WIDGET EVENT STATR ---------------------------------------------*/


    /*---------------------------------------- INTERFACE START --------------------------------------*/

    public interface InputListener {

        /**
         * 点击发送按钮时调用
         *
         * @param observer 输入响应监听
         * @param input    输入内容
         * @return 如果输入文本有效，则必须返回{@代码TRUE }，输入将被清除，否则返回false。
         */
        boolean onSubmit(Observer observer, CharSequence input);

        /**
         * 点击输入框准备开始输入
         */
        void onFocusInput();

        /**
         * 点击输入框准备开始输入
         */
        void unFocusInput();
    }

    public interface AttachmentsListener {

        /**
         * 点击录音按钮时调用
         */
        void onRecodeAttachments(boolean ifRecordMode);

        /**
         * 点击表情按钮时调用
         */
        void onExpressionAttachments();

        /**
         * 点击更多按钮时调用
         */
        void onMoreAttachments();

        /**
         * 点击添加按钮时调用
         */
        void onAddAttachments(boolean visible);

    }

    /**
     * 录音时回调的接口定义
     */
    public interface RecodeListener {

        /**
         * 完成录音时调用
         *
         * @param path     文件路径
         * @param duration 时长
         */
        void onRecordSuccess(String path, int duration);
    }

    /**
     * 用户键入时调用的回调的接口定义
     */
    public interface TypingListener {

        /**
         * 用户点击开始输入时调用
         */
        void onStartTyping();

        /**
         * 用户点击结束输入时调用
         */
        void onStopTyping();

    }

    /*---------------------------------------- INTERFACE END --------------------------------------*/
    public interface TextChangeListener {
        void onTextChange(CharSequence charSequence);

        void onAtSuccess();

    }

}
