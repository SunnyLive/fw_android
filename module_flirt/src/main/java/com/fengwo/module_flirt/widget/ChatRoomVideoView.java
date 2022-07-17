package com.fengwo.module_flirt.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.utils.Common;
import com.fengwo.module_comment.utils.CountBackUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_comment.widget.FadingRecyclerView;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.activity.BaseChatRoomButtonActivity;
import com.fengwo.module_flirt.adapter.ChatRoomAdapter;
import com.fengwo.module_flirt.adapter.ChatUserAdapter;
import com.fengwo.module_flirt.bean.MessageListVO;
import com.fengwo.module_flirt.dialog.ChatMenuPopwindow;
import com.fengwo.module_flirt.manager.ChatHistroySQLHelper;
import com.fengwo.module_flirt.manager.WenboMsgManager;
import com.fengwo.module_websocket.bean.MsgType;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.fengwo.module_websocket.bean.StatFateMsg;
import com.fengwo.module_websocket.bean.WenboGiftMsg;
import com.fengwo.module_websocket.bean.WenboWsChatDataBean;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.util.KeyboardUtils;

;

/**
 * 聊天室 内层  直播操作/ 聊天
 *
 * @Author BLCS
 * @Time 2020/4/2 16:56
 */
public class ChatRoomVideoView extends LinearLayoutCompat {

    @BindView(R2.id.video_play)
    TXCloudVideoView videoPlay;
//    @BindView(R2.id.video_play)
//    FrameLayout videoPlay;      // 视频播放父控件

    @BindView(R2.id.sr_refresh)
    SmartRefreshLayout srRefresh;

    @BindView(R2.id.rv_chat_room)
    RecyclerView rvChatRoom;

    @BindView(R2.id.iv_chat_visible)
    ImageView ivChatVisible;
//    @BindView(R2.id.iv_gift_show)
//    ImageView ivGiftShow;
//    @BindView(R2.id.iv_close_gift)
//    ImageView ivCloseGift;

    @BindView(R2.id.cl_chat_room_bottom)
    ConstraintLayout cl_chat_room_bottom;
//    @BindView(R2.id.ll_gift_show)
//    FrameLayout llGiftShow;


    @BindView(R2.id.fl_chat_room)
    FrameLayout fl_chat_room;
    @BindView(R2.id.view_weight)
    View viewWeight;
    //    @BindView(R2.id.iv_bg_gift)
//    ImageView ivBgGift;
    @BindView(R2.id.ll_show_gift_btn)
    RelativeLayout llShowGiftBtn;
    @BindView(R2.id.tv_gift_des)
    TextView tvGiftdes;
    @BindView(R2.id.tv_gift_btn)
    TextView tvGiftbtn;
    @BindView(R2.id.iv_free_bg)
    ImageView ivFreeBg;
    @BindView(R2.id.cri_input)
    ChatRoomInputView criInput; //输入框
    /*观看用户列表*/
    @BindView(R2.id.rv_users)
    FadingRecyclerView rvUsers;
    @BindView(R2.id.tv_users_count)
    TextView tvUsersCount;
    TextView tvUsersCountFooter;
    boolean host;
    /*聊天列表adapter*/
    private ChatRoomAdapter mRoomAdapter;
    /*右侧聊天用户列表*/
    private ChatUserAdapter chatUserAdapter;

    private Context mContext;
    private int pos;
    private ChatUserAdapter.OnChatUserChangeListener OnChatUserChangeListener;
    private ChatMenuPopwindow chatMenu;//聊天消息长按弹窗
    private ChatUserAdapter.OnNotificationListener onNotificationListener;

    public ChatRoomVideoView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ChatRoomVideoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public void init() {
        setOrientation(LinearLayoutCompat.VERTICAL);
        initUI();
    }

    private void initUI() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.include_chat_room_video, this, true);
        ButterKnife.bind(this, inflate);
        initVideo();
        initRvChat();
        videoPlay.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyBroad();
                return false;
            }
        });
//
//        ivGiftShow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onClickListener != null) onClickListener.clickAddTime();
//            }
//        });
//
//        ivCloseGift.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ivGiftShow.setVisibility(GONE);
//                ivCloseGift.setVisibility(GONE);
//            }
//        });

        llShowGiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*开启缘分*/
                if (onClickListener != null) onClickListener.showAddGift();
            }
        });
    }


    private void hideKeyBroad() {
        if (null != criInput.getInputEdit()) {
            if (KeyBoardUtils.isSHowKeyboard(mContext, criInput.getInputEdit()))
                KeyBoardUtils.closeKeybord(criInput.getInputEdit(), mContext);
        }
    }

    public ChatRoomInputView getInputView() {
        return criInput;
    }

    /**
     * 显示未付费背景
     *
     * @param show
     * @param url
     */
    public void showFreeBg(boolean show, String url) {
        ivFreeBg.setVisibility(show ? VISIBLE : GONE);
        if (!show) return;
        ImageLoader.loadImgGaussian(ivFreeBg, url);
    }

    private CountBackUtils activityCb;

    /**
     * 初始化直播
     */
    private void initVideo() {

    }

    /**
     * 展示右侧的聊天用户列表
     *
     * @param users
     */
    public void showChatUsers(List<MessageListVO> users) {
        if (users != null && users.size() > 0) {
            //有用户聊天时显示聊天相关控件
            srRefresh.setVisibility(View.VISIBLE);
            rvUsers.setVisibility(View.VISIBLE);
            tvUsersCount.setVisibility(View.VISIBLE);
            if (chatUserAdapter == null) {
                chatUserAdapter = new ChatUserAdapter(getContext());
                //添加一个数量view，与滚动事件联动决定显示隐藏
                View footerView = LayoutInflater.from(getContext()).inflate(R.layout.footer_chat_user, null);
                tvUsersCountFooter = footerView.findViewById(R.id.tv_users_count);
                chatUserAdapter.addFooterView(footerView);
                rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
                rvUsers.setAdapter(chatUserAdapter);
                chatUserAdapter.bindToRecyclerView(rvUsers);
                rvUsers.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        refreshUsersCountState();
                    }
                });
                chatUserAdapter.setOnChatUserChangeListener(OnChatUserChangeListener);
                chatUserAdapter.setOnNotificationListener(onNotificationListener);
            }
            tvUsersCount.setText(String.format("%d人", users.size()));
            tvUsersCountFooter.setText(String.format("%d人", users.size()));
            //触发滚动事件，显示出底部的人数view
            refreshUsersCountState();

        } else {
            //没有用户聊天时隐藏聊天相关控件
            srRefresh.setVisibility(View.GONE);
            rvUsers.setVisibility(View.GONE);
            tvUsersCount.setVisibility(View.GONE);

        }
        if (chatUserAdapter != null) {
            //这里重复加载，导致chatUserAdapter.notifyChatUserChanged两次调用，所以注释掉
//            if(users != null && users.size() > 0){
//                OnChatUserChangeListener.onChatUserChange(chatUserAdapter.refreshUsers(users));
//            }else {
//                chatUserAdapter.refreshUsers(users);
//            }
            chatUserAdapter.refreshUsers(users);
            refreshUsersCountState();
        }
    }

    private void refreshUsersCountState() {

        //如果两个都在显示状态就显示一个
        if (tvUsersCount.getVisibility() == View.VISIBLE &&
                tvUsersCountFooter.getVisibility() == View.VISIBLE) {
            tvUsersCount.setVisibility(View.GONE);
        }

        //如果当前键盘是展开状态则不改变tag的显示隐藏
        if (criInput.isKeyboardShow()) {
            return;
        }
        tvUsersCount.post(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager manager = (LinearLayoutManager) rvUsers.getLayoutManager();
                //获取最后一个完全显示的ItemPosition
                int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = manager.getItemCount();

                // 判断是否滚动到底部,滑动到底部时将悬浮View隐藏
                tvUsersCount.setVisibility(lastVisibleItem >= (totalItemCount - 1) ? View.GONE : View.VISIBLE);
                tvUsersCountFooter.setVisibility(lastVisibleItem >= (totalItemCount - 1) ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * 初始化聊天
     */
    private void initRvChat() {
        //加载历史消息
        srRefresh.setEnableLoadMore(false);
        srRefresh.setOnRefreshListener(refreshLayout -> {
            if (onClickListener != null) onClickListener.addLoadHistory();
            srRefresh.finishRefresh();
        });
        mRoomAdapter = new ChatRoomAdapter(new ChatRoomAdapter.IStatueListener() {
            @Override
            public void onSendFaild() {
                CustomerDialog mcd = new CustomerDialog.Builder(getContext())
                        .setTitle("")
                        .setMsg("您的语音内容违规，发送失败")
                        .setPositiveButton("我知道了", () -> {

                        }).create();
                mcd.show();
                mcd.hideCancle();

            }

            @Override
            public void onComeBack(String msgid, SocketRequest<WenboWsChatDataBean> data) {
                if (onClickListener != null) onClickListener.onItemMsgFor(msgid, data);
                //  WenboMsgManager.getInstant().setItemMsgFor(msgid);
                mRoomAdapter.updateMsgDing(msgid);
            }
        });

        rvChatRoom.setAdapter(mRoomAdapter);
        rvChatRoom.setLayoutManager(new LinearLayoutManager(getContext()));
        mRoomAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                KeyboardUtils.close(criInput);
            }
        });
        mRoomAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            if (chatMenu == null) {
                chatMenu = new ChatMenuPopwindow(mContext);
                chatMenu.setOnClickChatMenuListener(new ChatMenuPopwindow.OnClickChatMenuListener() {
                    @Override
                    public void onRevoke(SocketRequest<WenboWsChatDataBean> chatData, int position) {
                        ChatHistroySQLHelper.getInstance().revokeChatMsg(true, chatData.msgId);
                        mRoomAdapter.getData().get(position).msgType = MsgType.toRevocation;
                        mRoomAdapter.refreshNotifyItemChanged(position);
                        if (onClickListener != null) {
                            onClickListener.onRevoke(chatData.msgId);
                        }
                    }

                    @Override
                    public void onRemove(SocketRequest<WenboWsChatDataBean> chatData, int position) {
                        ChatHistroySQLHelper.getInstance().deleteSingChatMsg(chatData.msgId);
                        mRoomAdapter.remove(position);
                    }
                });
            }
            chatMenu.showPopupWindow(mRoomAdapter.getData().get(position), position);
            return false;
        });

    }

    /**
     * 接受到撤回消息
     *
     * @param msgId
     */
    public void comeRevoke(String msgId) {
        if (TextUtils.isEmpty(msgId)) return;
        for (int i = 0; i < mRoomAdapter.getData().size(); i++) {
            if (msgId.equals(mRoomAdapter.getData().get(i).msgId)) {
                mRoomAdapter.getData().get(i).msgType = MsgType.comeRevocation;
                mRoomAdapter.refreshNotifyItemChanged(i);
                return;
            }
        }
    }

    public void setScreen() {
        mRoomAdapter.clearData();
    }

    public String getGiftBtn() {
        return tvGiftbtn.getText().toString();
    }

    /**
     * 获取输入EditText
     *
     * @return
     */
    public ChatRoomInputView getEditInput() {
        return criInput;
    }

    /**
     * 设置是否是主播
     */
    public void setHost(boolean isHost) {
        this.host = host;
        if (mRoomAdapter != null) {
            mRoomAdapter.setAnchor(isHost);
        }

        criInput.setHost(isHost);
    }


    public void showGiftTime(int res, boolean show, int pos) {
        this.pos = pos;
//        ImageLoader.loadGif(ivGiftShow,res);
//        llGiftShow.setVisibility(show?VISIBLE:GONE);

    }

    /**
     * 显示开启缘分按钮
     *
     * @param show
     */
    public void showOpenGiftBtn(boolean show, String content, String btn) {
        llShowGiftBtn.setVisibility(show ? VISIBLE : GONE);
        if (!TextUtils.isEmpty(content)) {
            switch (content) {
                case BaseChatRoomButtonActivity.MSG_KQYF:
                    llShowGiftBtn.setBackgroundResource(R.drawable.shape_gift_btn);
                    break;
                case BaseChatRoomButtonActivity.MSG_ZXQY:
                    llShowGiftBtn.setBackgroundResource(R.drawable.shape_gift_btn2);
                    break;
                case BaseChatRoomButtonActivity.MSG_ZDSS:
                    llShowGiftBtn.setBackgroundResource(R.drawable.shape_gift_btn3);
                    break;
            }
        }


        if (!TextUtils.isEmpty(content)) tvGiftdes.setText(content);
        if (!TextUtils.isEmpty(btn)) tvGiftbtn.setText(btn);
    }

    public void showOpenInt(boolean isShow) {
        if (isShow) {
            if(llShowGiftBtn.getVisibility() == VISIBLE){
                llShowGiftBtn.setVisibility(INVISIBLE);
            }
        } else {
            if(llShowGiftBtn.getVisibility() == INVISIBLE){
                llShowGiftBtn.setVisibility(VISIBLE);
            }
        }
    }

    public ImageView getBgGift() {
        return ivFreeBg;
    }

//    public void showAddTimeGIF(boolean show) {
//        llGiftShow.setVisibility(show?VISIBLE:GONE);
//    }

    /**
     * 用于判断 加时 是走 加时 还是走点单
     * @return
     */
//    public boolean getOrderGiftVisibility(){
//       return llGiftShow.getVisibility() ==VISIBLE;
//    }

    /**
     * 改变空白背景图的权重
     *
     * @return
     */
    public void setSpaceweight(int weight) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) viewWeight.getLayoutParams();
        layoutParams.verticalWeight = weight;
        viewWeight.setLayoutParams(layoutParams);
    }

    public TXCloudVideoView getVideoView() {
        return videoPlay;
    }

//    public FrameLayout getVideoPlay() {
//        return videoPlay;
//    }

    public void setVider() {
        criInput.setVider();
    }

    /**
     * 隐藏直播界面
     */
    public void hideVideoView() {
        //    videoPlay.setVisibility(GONE);
        //      ivChatVisible.setVisibility(GONE);
//        rvChatRoom.setPadding(0, DensityUtils.dp2px(getContext(), getResources().getDimension(R.dimen.dp_30)), 0, DensityUtils.dp2px(getContext(), getResources().getDimension(R.dimen.dp_8)));
    }

    /**
     * 隐藏聊天界面
     */
    public void hideChatView(boolean isHide) {
//        srRefresh.setVisibility(isHide == true ? GONE : VISIBLE);
//        criInput.setVisibility(isHide == true ? GONE : VISIBLE);
//        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if (!isHost) {
//                    cl_chat_room_bottom.setVisibility(isHide == true ? GONE : VISIBLE);
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        };
//        if (isHide) { //隐藏
//            Animation translateVerticalAnimation = getTranslateVerticalAnimation(0, 1f, 300);
//            translateVerticalAnimation.setAnimationListener(animationListener);
//            cl_chat_room_bottom.startAnimation(translateVerticalAnimation);
//            KeyboardUtils.close(this);
//        } else { //显示
//            if (cl_chat_room_bottom.getVisibility() == GONE) {
//                Animation translateVerticalAnimation = getTranslateVerticalAnimation(1f, 0, 300);
//                translateVerticalAnimation.setAnimationListener(animationListener);
//                cl_chat_room_bottom.startAnimation(translateVerticalAnimation);
//            }
//        }
    }

    public void setShowChat(boolean isShow) {
        cl_chat_room_bottom.setVisibility(isShow ? VISIBLE : GONE);
    }

    /**
     * 显示直播界面
     */
    public void showVideoView() {
        videoPlay.setVisibility(VISIBLE);
        //   ivChatVisible.setVisibility(VISIBLE);
        rvChatRoom.setPadding(0, 0, 0, DensityUtils.dp2px(getContext(), getResources().getDimension(R.dimen.dp_8)));
    }

    public RecyclerView getChatRcv() {
        return rvChatRoom;
    }

    /**
     * 显示  隐藏聊天按钮
     *
     * @param show
     */
    public void showChatVisible(boolean show) {
        //       ivChatVisible.setVisibility(show?VISIBLE:GONE);
    }

    /**
     * 添加输入框点击事件
     */
    public void addInputClickListener(ChatRoomInputView.OnInputClickListener listener) {
        criInput.addOnClickListener(listener);
    }

    public ChatRoomAdapter getChatAdapter() {
        return mRoomAdapter;
    }

    /**
     * 滚动到最新一行
     */
    public void scrollLast(int we) {
        int size = mRoomAdapter.getData().size();
        if (size > 0) {
            rvChatRoom.scrollToPosition(size - 1);
            if (we != 0) {
                setSpaceweight(we);
            }

        }
    }

    /**
     * 是否从下面开始显示
     */
    public void showLast(boolean showLast) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rvChatRoom.getLayoutManager();
        layoutManager.setStackFromEnd(showLast);
    }

    @OnClick({R2.id.iv_chat_visible})
    public void onClick(View view) {
        if (view.getId() == R.id.iv_chat_visible) {
            if (onClickListener != null) onClickListener.clickDown();
        }
    }


    public interface OnClickListener {
        void addLoadHistory();

        void clickDown();

        void showAddGift();

        void clickAddTime();

        void onRevoke(String msgId);

        void onItemMsgFor(String msgId, SocketRequest<WenboWsChatDataBean> data);

    }

    public OnClickListener onClickListener;

    public void addOnClickListener(OnClickListener listener) {
        onClickListener = listener;
    }

    float y = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y1 = event.getY();
                if (y - y1 > 200) {
                    hideChatView(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    /**
     * 切换当前聊天对象
     */
    public void setOnChatUserChangeListener(ChatUserAdapter.OnChatUserChangeListener OnChatUserChangeListener) {
        this.OnChatUserChangeListener = OnChatUserChangeListener;
    }

    /**
     * 设置180s或300s通知回调
     *
     * @param onNotificationListener
     */
    public void setOnNotificationListener(ChatUserAdapter.OnNotificationListener onNotificationListener) {
        this.onNotificationListener = onNotificationListener;
    }

    /**
     * 获取当前的聊天对象
     *
     * @return
     */
    public MessageListVO getCurrentChatUser() {
        if (chatUserAdapter != null) {
            return chatUserAdapter.getCurrentChatUser();
        }
        return null;
    }

    /**
     * 更新指定观众的消息未读数
     *
     * @param audienceId 观众id
     */
    public void refreshMsgUnReadCount(String audienceId) {
        if (chatUserAdapter != null) {
            chatUserAdapter.refreshMsgUnReadCount(audienceId);
        }
    }

    /**
     * 切换当前聊天对象
     *
     * @param audienceId 对方id
     * @return 是否存在该用户
     */
    public boolean changeCurrentChatUser(String audienceId) {
        if (chatUserAdapter != null) {
            return chatUserAdapter.changeCurrentChatUser(audienceId);
        }
        return false;
    }

    /**
     * 接受到缘分礼物
     *
     * @param statFate
     */
    public void onStatFate(StatFateMsg statFate) {
        if (chatUserAdapter != null) {
            chatUserAdapter.onStatFate(statFate);
        }
    }


    /**
     * 接受到礼物
     *
     * @param gift
     */
    public void onReceivedGift(WenboGiftMsg gift) {
        if (chatUserAdapter != null) {
            chatUserAdapter.onReceivedGift(gift);
        }
    }

    /**
     * 资源释放
     */
    public void release() {
        if (chatUserAdapter != null) {
            chatUserAdapter.release();
        }
    }
}
