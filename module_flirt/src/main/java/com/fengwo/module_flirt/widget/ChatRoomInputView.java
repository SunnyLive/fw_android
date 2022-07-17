package com.fengwo.module_flirt.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fengwo.module_chat.utils.chat_new.VoicePlayHelper;
import com.fengwo.module_chat.widgets.chat_new.MsgEditText;
import com.fengwo.module_chat.widgets.chat_new.VoiceRecorderView;
import com.fengwo.module_comment.utils.EPSoftKeyBoardListener;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.chat.ChatEmotionFragment;
import com.fengwo.module_comment.utils.chat.ChatGameFragment;
import com.fengwo.module_comment.utils.chat.GlobalOnItemClickManagerUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.dialog.KeyMapDailog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.exceptions.OnErrorNotImplementedException;

/**
 * 聊天室 输入框
 *
 * @Author BLCS
 * @Time 2020/4/2 16:56
 */
public class ChatRoomInputView extends LinearLayoutCompat implements VoiceRecorderView.OnFinishedRecordListener {

    @BindView(R2.id.iv_present)
    ImageView ivPresent;
    @BindView(R2.id.et_input)
    TextView etInput;
    @BindView(R2.id.iv_emoticon)
    ImageView ivEmoticon;
    @BindView(R2.id.iv_game)
    ImageView ivGame;
    @BindView(R2.id.iv_flirt_room_voice)
    ImageView ivRoomVoice;
    @BindView(R2.id.tv_send)
    TextView tvSend;
    @BindView(R2.id.rb_press_recode)
    VoiceRecorderView rbPressRecode;
    @BindView(R2.id.fl_emoticon)
    FrameLayout flEmoticon;
    @BindView(R2.id.view_keyboard)
    View viewKeyboard;
    @BindView(R2.id.ll_input)
    LinearLayout llInput;
    @BindView(R2.id.iv_closes)
    ImageView iv_closes;
    @BindView(R2.id.iv_gd)
    ImageView iv_gd;
    @BindView(R2.id.iv_small)
    ImageView iv_small;
    @BindView(R2.id.iv_gift)
    ImageView iv_gift;


    ChatEmotionFragment chatEmotionFragment;
    ChatGameFragment chatGameFragment;
    Fragment currentFragment;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    KeyMapDailog dialogs;
    private boolean isGame = false;
    private int heights;
    private boolean isHost;
//    private boolean isFirst;
//    private PopupWindowFactory mPop;
//    private Date audioStart,audioStop;

    /*当前键盘是否显示*/
    private boolean keyboardShow;

    public EditText getInputEdit() {
        if (null != dialogs && dialogs.getShowsDialog()) {
            return dialogs.setClone();
        } else {
            return null;
        }

    }


    public ChatRoomInputView(Context context) {
        super(context);
        init();
    }

    public ChatRoomInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        init();
    }

    public void init() {
        setOrientation(LinearLayoutCompat.VERTICAL);
        initUI();
    }

    private void initUI() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.include_chat_room_input, this, true);
        ButterKnife.bind(this, inflate);
        initEditInput();
        initRecoder();
        ivRoomVoice.setSelected(true);
    }

    private void initRecoder() {
        rbPressRecode.setOnFinishedRecordListener(this);
        rbPressRecode.setBackgroundResource(R.drawable.chat_right_text_bg);
        rbPressRecode.setTextColor(getResources().getColor(R.color.white));
        rbPressRecode.setBackgroundResourceChat(R.drawable.shape_chat_boy);
    }

    /**
     * 键盘监听
     */
    private void initEditInput() {

//        etInput.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//              //  tvSend.setVisibility(s.length() > 0 ? VISIBLE : GONE);
//            }
//        });
        EPSoftKeyBoardListener.setListener((Activity) getContext(), new EPSoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
                int size = (int) getContext().getResources().getDimension(R.dimen.dp_120);
                dialogs.setHeigth(height);
                params.bottomMargin = height + size;
                setLayoutParams(params);
                llInput.setVisibility(GONE);
                if (dialogs.isAdded()) {
                    dialogs.setViewGsonType(1);
                }
//                showKeyBoardHeight(height);
                if (flEmoticon.getVisibility() == View.VISIBLE) {
                    flEmoticon.setVisibility(GONE);
                }
                if (onClickListener != null) onClickListener.showKeyboard(true, 1);
                keyboardShow = true;
                if (onClickListener != null) {
                    onClickListener.onShowOpenBtn(true);
                }
            }

            @Override
            public void keyBoardHide(int height) {
                etInput.setText(dialogs.getInputDlg().getText().toString());
                isSqJp(false);
                keyboardShow = false;
                if (onClickListener != null) {
                    onClickListener.onShowOpenBtn(false);
                }
//                hideKeyBoardHeight();
            }
        });
    }

    private void isSqJp(boolean isForce) {
        if (isForce) {
            MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
            params.bottomMargin = 0;
            llInput.setVisibility(VISIBLE);
            setLayoutParams(params);

            if (onClickListener != null) onClickListener.showKeyboard(false, 10);
        } else if (!dialogs.getRjp()) {
            MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
            params.bottomMargin = 0;
            llInput.setVisibility(VISIBLE);
            setLayoutParams(params);

            if (onClickListener != null) onClickListener.showKeyboard(false, 10);
        } else {
            if (!dialogs.isAdded()) {
                MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
                params.bottomMargin = 0;
                llInput.setVisibility(VISIBLE);
                setLayoutParams(params);

                if (onClickListener != null) onClickListener.showKeyboard(false, 10);
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    public void setVider() {
        ivRoomVoice.setVisibility(VISIBLE);
    }

    /**
     * 显示键盘高度
     */
    private void showKeyBoardHeight(int height) {
        int supportSoftInputHeight = KeyBoardUtils.getSupportSoftInputHeight((Activity) getContext());
//        int bottomHeight = getRootView().getHeight() - ScreenUtils.getScreenHeight(getContext());
        if (supportSoftInputHeight > 0) {
            ViewGroup.LayoutParams layoutParams = viewKeyboard.getLayoutParams();
//            layoutParams.height = supportSoftInputHeight + bottomHeight;
            layoutParams.height = height;
            viewKeyboard.setLayoutParams(layoutParams);
            viewKeyboard.setVisibility(VISIBLE);
        }
    }

    /**
     * 隐藏键盘高度
     */
    private void hideKeyBoardHeight() {
        int supportSoftInputHeight = KeyBoardUtils.getSupportSoftInputHeight((Activity) getContext());
        if (supportSoftInputHeight < 0) {
            viewKeyboard.setVisibility(GONE);
            dialogs.setClone();
            //    KeyBoardUtils.closeKeybord(etInput, getContext());
        }
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyBoard() {
        if (null != dialogs && dialogs.getShowsDialog()) {
            try {
                dialogs.setClone();
            } catch (OnErrorNotImplementedException e) {

            }
        }
    }

    /**
     * 隐藏表情
     */
    public void hideEmoticon() {
        flEmoticon.setVisibility(GONE);
    }

    /**
     * 添加表情UI
     */
    private void initEmoticon() {
        if (null == chatEmotionFragment) {
            chatEmotionFragment = new ChatEmotionFragment();
        }
        isGame = false;
        showFragment(chatEmotionFragment);

        //绑定输入框
        //   GlobalOnItemClickManagerUtils.getInstance().attachToEditText(etInput, 100);
    }

    private void showFragment(Fragment fragment) {
        if (currentFragment != fragment) {//  判断传入的fragment是不是当前的currentFragmentgit
            fragmentTransaction = fragmentManager.beginTransaction();
            if (null != currentFragment)
                fragmentTransaction.hide(currentFragment);//  不是则隐藏
            currentFragment = fragment;  //  然后将传入的fragment赋值给currentFragment
            if (!fragment.isAdded()) { //  判断传入的fragment是否已经被add()过
                fragmentTransaction.add(R.id.fl_emoticon, fragment).show(fragment).commit();
            } else {
                fragmentTransaction.show(fragment).commit();
            }
        }
    }

    /**
     * 添加表情UI  游戏
     */
    private void initGames() {
        if (null == chatGameFragment) {
            chatGameFragment = new ChatGameFragment();
        }
        showFragment(chatGameFragment);
        isGame = true;
        //绑定输入框
        //      GlobalOnItemClickManagerUtils.getInstance().attachToEditText(etInput, 100);
    }

    @OnClick({R2.id.iv_present, R2.id.et_input, R2.id.iv_emoticon, R2.id.tv_send, R2.id.iv_game, R2.id.iv_flirt_room_voice, R2.id.iv_closes, R2.id.iv_more, R2.id.iv_small, R2.id.iv_gd, R2.id.iv_gift})
    public void onClick(View view) {
        if(isFastClick()){
            return;
        }
        int id = view.getId();
        if (id == R.id.iv_present) {// 礼物
            if (null != dialogs && dialogs.getShowsDialog()) {
                hideKeyBoard();
            }
            if (onClickListener != null) onClickListener.clickPresent();
        } else if (id == R.id.iv_flirt_room_voice) {//  点击语音
            if (onClickListener != null && onClickListener.clickVoice(false)) {
                return;
            }
            if (ivRoomVoice.isSelected()) {
                ivRoomVoice.setSelected(false);
            } else {
                ivRoomVoice.setSelected(true);
            }
        } else if (id == R.id.et_input) {//  输入
            ivEmoticon.setSelected(false);
            showDialog();
        } else if (id == R.id.iv_emoticon) {//  表情
            ivGame.setSelected(false);
            if (flEmoticon.getVisibility() == View.VISIBLE && !isGame) {
                //  KeyBoardUtils.openKeybord(etInput, getContext());
                flEmoticon.setVisibility(GONE);
                ivEmoticon.setSelected(false);
            } else {
                //    KeyBoardUtils.closeKeybord(etInput, getContext());
                ivRoomVoice.setSelected(false);//显示语音
                rbPressRecode.setVisibility(GONE);
                ivEmoticon.setSelected(true);
                etInput.setVisibility(VISIBLE);
                initEmoticon();
                flEmoticon.setVisibility(VISIBLE);
            }
            if (onClickListener != null) onClickListener.clickEmoticon();
        } else if (id == R.id.tv_send) {//  发送
            if (onClickListener != null)
                onClickListener.clickSend(etInput.getText().toString().trim());
            etInput.setText("");
        } else if (id == R.id.iv_game) {
            ivEmoticon.setSelected(false);
            if (flEmoticon.getVisibility() == View.VISIBLE && isGame) {
                flEmoticon.setVisibility(GONE);
                ivGame.setSelected(false);
            } else {
                flEmoticon.setVisibility(VISIBLE);
                ivGame.setSelected(true);
                initGames();
            }
        } else if (id == R.id.iv_closes) {
            if (onClickListener != null) onClickListener.clickfinish();
        } else if (id == R.id.iv_more) {
            if (onClickListener != null)
                if (isHost) {
                    onClickListener.clickMuch();
                } else {
                    onClickListener.showMoer();

                }
        } else if (id == R.id.iv_small) {

            if (onClickListener != null) {
                onClickListener.clickNarrowWindow();
            }
        } else if (id == R.id.iv_gd) {
            onClickListener.showMoer();
        } else if (id == R.id.iv_gift) {
            if (onClickListener != null)
                onClickListener.clickPresentRecord();
        }
    }

    private void showDialog() {
        if (onClickListener != null && onClickListener.clickInput()) {
            return;
        }
        ivGame.setSelected(false);
        if (null == dialogs) {
            dialogs = new KeyMapDailog(new KeyMapDailog.SendBackListener() {
                @Override
                public void sendBack(String inputText) {
                    onClickListener.clickSend(inputText.trim());
                }

                @Override
                public void setOnDismiss(boolean isForce) {
                    isSqJp(isForce);
                }

                @Override
                public void setVideoSwitch() {
                    if (ivRoomVoice.getVisibility() == View.VISIBLE) {
                        onClickListener.clickVoice(true);
                        hideKeyBoard();
                        isSqJp(false);
                        keyboardShow = false;
                        ivRoomVoice.setSelected(false);
                    }
                }
            });

        }
        if (ivRoomVoice.getVisibility() == View.GONE) {
            dialogs.setType(true);
        }
        dialogs.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "dialog");
    }

    public void setVoice() {
        if (!ivRoomVoice.isSelected()) {
            //显示语音icon 打开文字输入
            rbPressRecode.setVisibility(GONE);
            etInput.setVisibility(VISIBLE);
        } else {
            //显示键盘icon 打开语音
            hideKeyBoard();
            ivEmoticon.setSelected(false);//显示表情图标
            flEmoticon.setVisibility(GONE);
            rbPressRecode.setVisibility(VISIBLE);
            etInput.setVisibility(GONE);
        }
    }

    public void setChoice() {
        rbPressRecode.setVisibility(GONE);
        etInput.setVisibility(VISIBLE);
        ivRoomVoice.setVisibility(GONE);
        ivRoomVoice.setSelected(true);
    }

    @Override
    public void onFinishedRecord(String audioPath, int time) {
        onRecordSuccess(audioPath, time);
    }

    private RecodeListener recodeListener;

    public void setFirst(boolean isFirst) {
        //  ivRoomVoice.setImageResource(!isFirst ? R.drawable.ic_flirt_room_keyboard : R.drawable.ic_chat_voice);
//        if (isFirst){
////            ImageLoader.loadGif(ivPresent,R.drawable.ic_gift_gif);
//        }else{
//            ivPresent.setImageResource(R.mipmap.ic_present);
//        }
    }


    public void setHost(boolean isHost) {
        this.isHost = isHost;
        ivPresent.setVisibility(isHost ? GONE : VISIBLE);
        iv_small.setVisibility(isHost ? GONE : VISIBLE);

        iv_gd.setVisibility(isHost ? VISIBLE : GONE);
        iv_gift.setVisibility(isHost ? VISIBLE : GONE);
        ivRoomVoice.setVisibility(isHost ? VISIBLE : GONE);

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
     * 录制成功回调
     *
     * @param path     文件路径
     * @param duration 录音时长
     */
    private void onRecordSuccess(String path, int duration) {
        if (recodeListener != null) recodeListener.onRecordSuccess(path, duration);
    }

    public void setVoicePlayerWrapper(VoicePlayHelper voicePlayerWrapper) {
        rbPressRecode.setVoicePlayerWrapper(voicePlayerWrapper);
    }

    public void setRecodeListener(RecodeListener recodeListener) {
        this.recodeListener = recodeListener;
    }

    /**
     * 键盘是否显示
     *
     * @return
     */
    public boolean isKeyboardShow() {
        return keyboardShow;
    }

    public interface OnInputClickListener {
        void clickPresent();

        /**
         * @return 是否拦截事件
         */
        boolean clickInput();

        void clickEmoticon();

        void clickSend(String sendContent);

        boolean clickVoice(boolean istype);

        void showKeyboard(boolean show, int we);

        void clickfinish();

        void showMoer();

        void clickNarrowWindow();

        void clickMuch();

        void clickPresentRecord();

        void onShowOpenBtn(boolean isShow);

    }

    public OnInputClickListener onClickListener;

    public void addOnClickListener(OnInputClickListener listener) {
        onClickListener = listener;
    }

    private static final int MIN_CLICK_DELAY_TIME = 300;
    private static long lastClickTime;

    public static boolean isFastClick() {
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) <= MIN_CLICK_DELAY_TIME) {
            return true;
        }else{
            lastClickTime = curClickTime;
            return false;
        }

    }
}
