package com.fengwo.module_chat.widgets.chat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.utils.PopupWindowFactory;
import com.fengwo.module_chat.utils.TimeUtils;
import com.fengwo.module_comment.utils.AudioRecoderUtils;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.chat.FaceConversionUtil;
import com.fengwo.module_comment.widget.ViewPagerIndicator;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Date;

/**
 * 聊天下半区
 *
 * @author sunhee
 * @intro
 * @date 2019/9/17
 */
public class ChatBottomPanelView extends FrameLayout {

    String[] emoticon_array_txt = {"[招呼]", "[害羞]", "[调皮]", "[委屈]", "[流汗]", "[敲你]", "[微笑]", "[坏笑]", "[抠鼻]", "[发火]", "[疑问]", "[惊讶]", "[低级]", "[耍酷]"
            , "[尴尬]", "[流泪]", "[流汗]", "[晕倒]", "[偷笑]", "[鼓掌]", "[鼻滴]", "[好色]", "[傻笑]", "[亲亲]"};
    int[] emoji = {
            R.drawable.e00,
            R.drawable.e01,
            R.drawable.e02,
            R.drawable.e03,
            R.drawable.e04,
            R.drawable.e05,
            R.drawable.e06,
            R.drawable.e07,
            R.drawable.e08,
            R.drawable.e09,
            R.drawable.e10,
            R.drawable.e11,
            R.drawable.e12,
            R.drawable.e13,
            R.drawable.e14,
            R.drawable.e15,
            R.drawable.e16,
            R.drawable.e17,
            R.drawable.e18,
            R.drawable.e19,
            R.drawable.e20,
            R.drawable.e21,
            R.drawable.e22,
            R.drawable.e23,
    };


    public static final int PANEL_STUTAS_NONE = 0;
    public static final int PANEL_STUTAS_TEXT = 1;
    public static final int PANEL_STUTAS_VOICE = 2;
    public static final int PANEL_STUTAS_EMOJI = 3;


    EditText etInput;
    TextView btnSend;
    ImageView ivAblum;
    ImageView ivCamera;
    ImageView ivEmoji;
    ImageView ivVoice;
    FrameLayout flPanel;
    TextView btnStartTalk;
    private Context mContext;
    private PopupWindowFactory mPop;
    private ViewPager vpEmoji;
    private ViewPagerIndicator indicator;
    private EmojiVpAdapter emojiVpAdapter;
    PanelCallback panelCallback;

    int mPanelStatus = PANEL_STUTAS_NONE;

    //录音需要
    private AudioRecoderUtils mAudioRecoderUtils;
    //麦克风控件
    ImageView mImageView;
    TextView mTextView;
    private Date audioStart, audioStop;

    private boolean isTalk = false;

    public ChatBottomPanelView(Context context) {
        this(context, null);
    }

    public ChatBottomPanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatBottomPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {

        final View microphone = View.inflate(mContext, R.layout.layout_microphone, null);

        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) microphone.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) microphone.findViewById(R.id.tv_recording_time);
        mPop = new PopupWindowFactory(mContext, microphone);
        mAudioRecoderUtils = new AudioRecoderUtils();
        LayoutInflater.from(getContext()).inflate(R.layout.chat_view_bottom_panel, this);
        etInput = findViewById(R.id.et_input);
        btnSend = findViewById(R.id.btn_send);
        ivAblum = findViewById(R.id.iv_album);
        ivCamera = findViewById(R.id.iv_camera);
        ivEmoji = findViewById(R.id.iv_emoji);
        ivVoice = findViewById(R.id.iv_voice);
        flPanel = findViewById(R.id.fl_panel);
        vpEmoji = findViewById(R.id.vp_emoji);
        btnStartTalk = findViewById(R.id.start_record_btn);
        indicator = findViewById(R.id.indicator_circle_line);
        btnSend.setOnClickListener(view -> {
            String text = etInput.getText().toString();
//                if (!TextUtils.isEmpty(text)) {
            if (panelCallback != null) {
                panelCallback.sendText(text);
                etInput.setText("");
            }
        });
        ivVoice.setOnClickListener(view -> {
            new RxPermissions((FragmentActivity) mContext).request(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO)
                    .subscribe(
                            grant -> {
                                if (grant) {
                                    isTalk = !isTalk;
                                    if (isTalk) {
                                        btnStartTalk.setVisibility(VISIBLE);
                                        etInput.setVisibility(GONE);
                                    } else {
                                        btnStartTalk.setVisibility(GONE);
                                        etInput.setVisibility(VISIBLE);
                                    }
                                }
                            }

                    );

        });
        btnStartTalk.setOnTouchListener((view, motionEvent) -> {
            int action = motionEvent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    audioStart = new Date();
                    mPop.showAtLocation(((Activity) mContext).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                    btnStartTalk.setText("松开保存");
                    mAudioRecoderUtils.startRecord();
                    break;
                case MotionEvent.ACTION_UP:
                    mAudioRecoderUtils.stopRecord();  //结束录音（保存录音文件）
                    mPop.dismiss();
                    btnStartTalk.setText("按住说话");
                    break;
            }
            return true;
        });
        ivAblum.setOnClickListener(view -> {
            if (panelCallback != null) {
                panelCallback.sendImage();
            }
        });
        ivCamera.setOnClickListener(view -> {
            if (panelCallback != null) {
                panelCallback.sendCamera();
            }
        });

        ivEmoji.setOnClickListener(view -> setPanelStatus(PANEL_STUTAS_EMOJI));

        etInput.setOnFocusChangeListener((view, b) -> {
            if (b) {
                setPanelStatus(PANEL_STUTAS_TEXT);
            }
        });
        emojiVpAdapter = new EmojiVpAdapter(getContext(), emoji, emoticon_array_txt);
        emojiVpAdapter.setOnEmojiClickListener(str -> {
            L.e("emoji-----------", str);
            String inputStr = etInput.getText().toString();
            inputStr = inputStr + str;
            etInput.setText(FaceConversionUtil.getSmiledText(mContext, inputStr));
        });
        vpEmoji.setAdapter(emojiVpAdapter);
        indicator.setViewPager(vpEmoji, false);
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));

            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                audioStop = new Date();
                mPop.dismiss();
                //Toast.makeText(ChatViewActivity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                mTextView.setText(TimeUtils.long2String(0));
//                date2[0] = new Date();
                int sound_length = (int) TimeUtils.timeCalculateSeconds(audioStop, audioStart);
//                Logger.e("录音时间:" + sound_length + "秒");
                if (null != panelCallback) {
                    panelCallback.sendAudio(filePath, sound_length);
                }
            }
        });

    }

    private static final String TAG = "ChatBottomPanelView";

    public void hideKeyboard() {
        KeyBoardUtils.closeKeybord(etInput, getContext());

    }

    public void showKeyboard() {
        KeyBoardUtils.openKeybord(etInput, getContext());

    }

    public void setPanelStatus(int status) {
        if (mPanelStatus != status) {
            if (mPanelStatus == PANEL_STUTAS_TEXT) {
                mPanelStatus = status;
                hideKeyboard();
            } else {
                mPanelStatus = status;
                changePanel();
            }
        }
    }


    public void changePanel() {
        if (mPanelStatus == PANEL_STUTAS_NONE) {
            hideKeyboard();
            flPanel.setVisibility(View.GONE);
        } else if (mPanelStatus == PANEL_STUTAS_TEXT) {
            flPanel.setVisibility(View.GONE);
            showKeyboard();
//            etInput.requestFocus();
        } else if (mPanelStatus == PANEL_STUTAS_EMOJI) {
            hideKeyboard();
//            etInput.clearFocus();
            flPanel.setVisibility(View.VISIBLE);
            if(null!=panelCallback){
                panelCallback.bottomShow();
            }
        } else if (mPanelStatus == PANEL_STUTAS_VOICE) {
            hideKeyboard();
            etInput.clearFocus();
            flPanel.setVisibility(View.GONE);
        }
    }

    public boolean close() {
        if (flPanel.getVisibility() != GONE) {
            setPanelStatus(PANEL_STUTAS_NONE);
            return true;
        }
        return false;
    }


    public void setPanelCallback(PanelCallback panelCallback) {
        this.panelCallback = panelCallback;
    }

    private int keyboardHeight = 0;

    public void onKeyboardChanged(boolean isOpen, int keyboardHeight) {
        Log.d(TAG, "onKeyboardChanged: " + isOpen + " " + keyboardHeight);
        if (isOpen) {
            this.keyboardHeight = keyboardHeight;
            Context context = getContext();
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                Rect rect = new Rect();
                View decorView = activity.getWindow().getDecorView();
                decorView.getWindowVisibleDisplayFrame(rect);//弹出键盘时可见高度变小 decorview - 键盘高度
                ViewGroup.LayoutParams layoutParams = decorView.getLayoutParams();
                layoutParams.height = rect.bottom - keyboardHeight;
            }
            if (mPanelStatus != PANEL_STUTAS_TEXT) {
                mPanelStatus = PANEL_STUTAS_TEXT;
                changePanel();
            }
        } else {
            Context context = getContext();
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                Rect rect = new Rect();
                View decorView = activity.getWindow().getDecorView();
                decorView.getWindowVisibleDisplayFrame(rect);//键盘隐藏高度变回正常，设置为正常
                ViewGroup.LayoutParams layoutParams = decorView.getLayoutParams();
                layoutParams.height = rect.bottom;
            }
            if (mPanelStatus != PANEL_STUTAS_TEXT) {
                changePanel();
            } else {
                mPanelStatus = PANEL_STUTAS_NONE;
                changePanel();
            }
        }
    }

}
