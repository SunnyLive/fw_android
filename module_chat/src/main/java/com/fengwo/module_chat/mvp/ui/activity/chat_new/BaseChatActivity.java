package com.fengwo.module_chat.mvp.ui.activity.chat_new;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faceunity.ui.dialog.BaseDialogFragment;
import com.faceunity.ui.dialog.ConfirmDialogFragment;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.base.RefreshChatMessageEvent;
import com.fengwo.module_chat.entity.ChatListItemEntity;
import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_chat.mvp.model.bean.chat_new.ChatMsgEntity;
import com.fengwo.module_chat.mvp.presenter.BaseChatSocketPresenter;
import com.fengwo.module_chat.mvp.ui.adapter.chat_new.ChatAdapter;
import com.fengwo.module_chat.mvp.ui.dialog.LongClickDialog;
import com.fengwo.module_chat.mvp.ui.event.ChatMessageErrorEvent;
import com.fengwo.module_chat.mvp.ui.fragment.ChatFunctionFragment;
import com.fengwo.module_chat.mvp.ui.fragment.FunctionFirstFragment;
import com.fengwo.module_comment.utils.CopyUtils;
import com.fengwo.module_chat.utils.chat_new.ChatGreenDaoHelper;
import com.fengwo.module_chat.utils.chat_new.UIUtil;
import com.fengwo.module_chat.utils.chat_new.VoicePlayHelper;
import com.fengwo.module_chat.widgets.chat_new.ChatInput;
import com.fengwo.module_chat.widgets.chat_new.MsgEditText;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.Constant;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.FileUtils;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.chat.ChatEmotionFragment;
import com.fengwo.module_comment.utils.chat.CommonFragmentPageAdapter;
import com.fengwo.module_comment.utils.chat.GlobalOnItemClickManagerUtils;
import com.fengwo.module_comment.utils.chat.NoScrollViewPager;
import com.fengwo.module_comment.widget.AppTitleBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;

import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.Observer;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * 聊天基类 根据不同场景 选择重写IBaseChatView里面的方法 在界面实现
 */
public abstract class BaseChatActivity extends BaseMvpActivity<IBaseChatView,
        BaseChatSocketPresenter<IBaseChatView>> implements IBaseChatView,
        FunctionFirstFragment.FunctionFirstListener, ChatInput.RecodeListener,
        ChatInput.InputListener, ChatInput.AttachmentsListener {

    public final int REQUEST_CODE_CHOOSE = 101;
    public final int REQUEST_CODE_CAMERA = 102;
    public final int REQUEST_CODE_TRANSPOND = 103;

    @BindView(R2.id.lay_input)
    ChatInput mInput;
    @BindView(R2.id.emotion_layout)
    View mEmotionLayout;
    @BindView(R2.id.recyclerview)
    RecyclerView mChatList;
    @BindView(R2.id.sr_rv)
    SmartRefreshLayout sr_rv;
    @BindView(R2.id.vp_bottom_more)
    NoScrollViewPager mVpBottomMore;
    @BindView(R2.id.layout_more_bottom_view)
    View bottomView;
    @BindView(R2.id.titleBar)
    AppTitleBar titleBar;

    public MsgEditText mEditText;
    protected VoicePlayHelper mVoicePlayerWrapper;
    protected ChatAdapter mChatAdapter;
    protected ArrayList<Fragment> mFragments;
    protected boolean moreSelectType = false;
    /**
     * 0：关闭；1：表情；2：常用语；3：功能菜单
     */

    private boolean isEmotionViewShown = false;

    private int mBottomType = 0;
    private String take_cam_fileName;

    protected ArrayList<ChatMsgEntity> transponds = new ArrayList<>();

    @Override
    public BaseChatSocketPresenter<IBaseChatView> initPresenter() {
        return new BaseChatSocketPresenter();
    }

    public abstract String setFromId();

    public abstract String setToId();

    public void setTalkName(String talkName) {
        p.setTalkName(talkName);
    }

    public void setTalkAvatar(String avatar) {
        p.setTalkAvatar(avatar);
    }

    public abstract void sendText(CharSequence text);

    public abstract void sendImage(String url, int width, int height);

    public abstract void sendAudio(String path, int duration);

    private int page = 0;

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        Constants.CURRENT_CHAT_TARGET_ID = setToId();//当前正在聊天的目前id 用于接收socket标记为已读
        //清空已读消息
        ChatGreenDaoHelper daoHelper = ChatGreenDaoHelper.getInstance();
        daoHelper.cleanUnreadCount(setFromId(), setToId());

        initListView();
        initBottomMore();
        mInput.setRecodeListener(this);
        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance();
        mEditText = mInput.getInputEditText();
        globalOnItemClickListener.attachToEditText(mEditText, 400);
        mVoicePlayerWrapper = new VoicePlayHelper(this, setToId());
        mInput.setVoicePlayerWrapper(mVoicePlayerWrapper);
        initListener();
        titleBar.setBackClickListener(v -> {
            if (moreSelectType) {
                moreSelectType = false;
                refreshMoreTypeMode();
            } else finish();
        });

        initData();
        /*显示消息列表*/
        p.messages.observe(this, chatMsgEntities -> {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mChatList.getLayoutManager();
            layoutManager.setStackFromEnd(chatMsgEntities.size() > 20 ? true : false);
            if (page > 0) {
                mChatAdapter.addData(0, chatMsgEntities);
            } else {
                mChatAdapter.setNewData(chatMsgEntities);
                mChatAdapter.showLastItem(true);
            }
        });

        /*显示自己接收的数据*/
        p.myMessage.observe(this, chatMsgEntity -> {
            mChatAdapter.addData(chatMsgEntity);
            //显示接收数据
            mChatAdapter.showLastItem(true);
        });

        // 消息发送失败
        RxBus.get().toObservable(ChatMessageErrorEvent.class).compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (TextUtils.equals(event.talkerUserId, setToId())) {
                        p.getHistoryList(0);
                        if (isSoftInputShown()) {
                            KeyBoardUtils.closeKeybord(mEditText, BaseChatActivity.this);
                        }
                    }
                });
        /**
         * @new 刷新接收消息
         */
        RxBus.get().toObservable(RefreshChatMessageEvent.class)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    refreshData(event);
                }, Throwable::printStackTrace);
    }


    private void refreshData(RefreshChatMessageEvent event) {
        switch (event.type) {
            case RefreshChatMessageEvent.TYPE_RECEIVE:
                //接收到单聊与群聊消息  判断是否是该界面 是则显示
                int id = event.bean.isGroup() ? event.bean.groupId : event.bean.userId;
                if (id == Integer.parseInt(setToId())) {
                    mChatAdapter.addData(event.bean);
                }
                break;
            case RefreshChatMessageEvent.TYPE_SEND:
                //接收到发送消息成功回调 刷新状态
                int pos = mChatAdapter.getData().indexOf(event.bean);
                mChatAdapter.getData().remove(event.bean);
                if(pos<0){
                    mChatAdapter.getData().add( event.bean);
                }else {
                    mChatAdapter.getData().add(pos, event.bean);
                }

                mChatAdapter.notifyItemChanged(pos);
                break;
        }
        //显示接收数据
        mChatAdapter.showLastItem(true);
    }


    protected void initData() {
        p.setFromId(setFromId());
        p.setToId(setToId());
        p.getHistoryList(page);
    }

    private void initListView() {
        //加载历史消息
        sr_rv.setEnableLoadMore(false);
        sr_rv.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                p.getHistoryList(++page);
                sr_rv.finishRefresh();
            }
        });
        mChatAdapter = new ChatAdapter(mChatList);
        mChatList.setAdapter(mChatAdapter);
        mChatList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.CURRENT_CHAT_TARGET_ID = "";
        GlobalOnItemClickManagerUtils.getInstance().attachToEditText(null,400);
        if (mVoicePlayerWrapper != null)
            mVoicePlayerWrapper.release();
        System.gc();
    }

    private void initBottomMore() {
        mFragments = new ArrayList<>();
        ChatEmotionFragment chatEmotionFragment = new ChatEmotionFragment();
        mFragments.add(chatEmotionFragment);
        ChatFunctionFragment chatFunctionFragment = new ChatFunctionFragment();
        chatFunctionFragment.setListener(this);
        mFragments.add(chatFunctionFragment);
        CommonFragmentPageAdapter mAdapter = new CommonFragmentPageAdapter(getSupportFragmentManager(), mFragments);
        mVpBottomMore.setOffscreenPageLimit(2);//添加缓冲页数，避免被销毁
        mVpBottomMore.setAdapter(mAdapter);
        mVpBottomMore.setCurrentItem(0);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onChoosePress() {
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(bindToLifecycle())
                .subscribe(granted -> {
                    if (granted) {
                        MImagePicker.openImagePicker(BaseChatActivity.this, MImagePicker.TYPE_IMG, REQUEST_CODE_CHOOSE);
                    } else {
                        Toast.makeText(this, "您关闭了权限，请去设置页面开启", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R2.id.layout_bottom_forward)
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.layout_bottom_forward) {
            if (mChatAdapter.getSelection().size() > 0) {
                //Todo 添加不能转发语音提示：仿微信：你选择的消息中，语音、特殊消息不能转发给朋友，是否继续。
                boolean hasVoice = false;
                for (ChatMsgEntity entity : mChatAdapter.getSelection()) {
                    if (entity.getMsgType() == ChatMsgEntity.MsgType.comeVoice || entity.getMsgType() == ChatMsgEntity.MsgType.toVoice) {
                        hasVoice = true;
                    }
                }
                if (hasVoice) {
                    ConfirmDialogFragment confirmDialogFragment = ConfirmDialogFragment.newInstance("你选择的消息中，语音、特殊消息不能转发给朋友，是否继续？", new BaseDialogFragment.OnClickListener() {
                        @Override
                        public void onConfirm() {
                            transpond();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    confirmDialogFragment.show(this.getSupportFragmentManager(), "BaseChat");
                } else {
                    transpond();
                }
            }
        }
    }

    /**
     * 转发
     */
    private void transpond() {
        transponds.clear();
        transponds.addAll(mChatAdapter.getSelection());
        Intent intent = new Intent(BaseChatActivity.this, MessageListActivity.class);
        intent.putExtra("uid", setFromId());
        intent.putExtra("type", MessageListActivity.TYPE_TRANSPOND);
        startActivityForResult(intent, REQUEST_CODE_TRANSPOND);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onShootPress() {
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(bindToLifecycle())
                .subscribe(granted -> {
                    try {
                        if (granted) {
                            take_cam_fileName = TimeUtils.getCurrentTimeInLong() + ".jpg";
                            // 步骤一：创建存储照片的文件
                            String path = FileUtils.SD_PATH;
                            File file = new File(path, take_cam_fileName);
                            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                            Uri photoUri;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                //步骤二：Android 7.0及以上获取文件 Uri
                                photoUri = FileProvider.getUriForFile(this, "fengwoImg", file);
                            } else {
                                //步骤三：获取文件Uri
                                photoUri = Uri.fromFile(file);
                            }
                            //步骤四：调取系统拍照
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(intent, REQUEST_CODE_CAMERA);
                        } else {
                            Toast.makeText(this, "您关闭了权限，请去设置页面开启", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE && data != null) {
                String url = MImagePicker.getImagePath(this, data);
//                List<String> mSelected = Matisse.obtainPathResult(data);
                //获取Options对象
                BitmapFactory.Options options = new BitmapFactory.Options();
                //仅做解码处理，不加载到内存
                options.inJustDecodeBounds = true;
                //解析文件
                BitmapFactory.decodeFile(url, options);
                //获取宽高
                int imgWidth = options.outWidth;
                int imgHeight = options.outHeight;
                sendImage(url, imgWidth, imgHeight);
            } else if (requestCode == REQUEST_CODE_CAMERA) {
                String a_path = FileUtils.SD_PATH + take_cam_fileName;
                //获取Options对象
                BitmapFactory.Options options = new BitmapFactory.Options();
                //仅做解码处理，不加载到内存
                options.inJustDecodeBounds = true;
                //解析文件
                BitmapFactory.decodeFile(a_path, options);
                //获取宽高
                int imgWidth = options.outWidth;
                int imgHeight = options.outHeight;
                sendImage(a_path, imgWidth, imgHeight);
            } else if (requestCode == REQUEST_CODE_TRANSPOND) {
                assert data != null;
                ChatListItemEntity item = (ChatListItemEntity) data.getSerializableExtra("data");
                assert item != null;
                if (item.getTalkUserId() == Integer.parseInt(setToId())) {
                    toastTip("消息来自该聊天对象，不能重复转发");
                    return;
                }
                for (ChatMsgEntity entity : transponds) {
                    p.transponds(entity, item);
                }
                moreSelectType = false;
                refreshMoreTypeMode();
            }
        }
    }

    @Override
    public void finish() {
        KeyBoardUtils.closeKeybord(mEditText, this);
        super.finish();
    }

    @Override
    public void onRecordSuccess(String path, int duration) {
        sendAudio(path, duration);
    }


    /**
     * 发送
     *
     * @param observer 输入响应监听
     * @param input    输入内容
     * @return
     */
    @Override
    public boolean onSubmit(Observer observer, CharSequence input) {
        sendText(input);
        return true;
    }

    @Override
    public void onFocusInput() {
        scrollToLastDelayed();
    }

    @Override
    public void unFocusInput() {
        KeyBoardUtils.closeKeybord(mEditText, this);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onRecodeAttachments(boolean ifRecordMode) {
        new RxPermissions(this)
                .request(Manifest.permission.RECORD_AUDIO)
                .compose(bindToLifecycle())
                .subscribe(grant -> {
                    if (grant) {
                        mInput.setRecordMode(!mInput.isRecordMode());
                        mInput.refreshInputView();
                        hideEmotionLayout(false);
                    } else {
                        Toast.makeText(this, "您关闭了权限，请去设置页面开启", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 表情列表
     */
    @Override
    public void onExpressionAttachments() {
        if (isEmotionViewShown) {
            if (mBottomType != 1) {
                mVpBottomMore.setCurrentItem(0);
                mBottomType = 1;
            } else {
                lockChatListHeight();
                hideEmotionLayout(true);
                mBottomType = 0;
                unlockChatListHeightDelayed();
            }
        } else {
            scrollToLastDelayed();
            if (isSoftInputShown()) {
                lockChatListHeight();
                showEmotionLayout();
                unlockChatListHeightDelayed();
            } else {
                showEmotionLayout();
            }
            mVpBottomMore.setCurrentItem(0);
            mBottomType = 1;
        }
    }

    @Override
    public void onMoreAttachments() {
    }

    @Override
    public void onAddAttachments(boolean visible) {
        if (isEmotionViewShown) {
            if (mBottomType != 3) {
                mVpBottomMore.setCurrentItem(1);
                mBottomType = 3;
            } else {
                lockChatListHeight();
                hideEmotionLayout(true);
                mBottomType = 0;
                unlockChatListHeightDelayed();
            }
        } else {
            scrollToLastDelayed();
            if (isSoftInputShown()) {
                lockChatListHeight();
                showEmotionLayout();
                unlockChatListHeightDelayed();
            } else {
                showEmotionLayout();
            }
            mVpBottomMore.setCurrentItem(1);
            mBottomType = 3;
        }
    }

    @SuppressLint({"ClickableViewAccessibility", "CheckResult"})
    private void initListener() {
        mChatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //当前RecyclerView显示出来的最后一个的item的position
            int lastPosition = -1;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://当前状态为停止滑动状态SCROLL_STATE_IDLE时
                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        break;
                    default:
                        break;
                }
            }
        });
        mChatList.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (isEmotionViewShown) {
                    mEditText.postDelayed(() -> {
                        isEmotionViewShown = false;
                        mEmotionLayout.getLayoutParams().height = 1;
                        mEmotionLayout.setVisibility(View.GONE);
                        ((LinearLayout.LayoutParams) sr_rv.getLayoutParams()).weight = 1.0F;
                        ((LinearLayout.LayoutParams) sr_rv.getLayoutParams()).height = 0;
                    }, 200L);
                }
                if (!mChatList.hasFocus()) mChatList.requestFocus();
            }
            return false;
        });
        mInput.setInputListener(this);
        mInput.setAttachmentsListener(this);
        mEditText.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP && isEmotionViewShown) {
                lockChatListHeight();
                hideEmotionLayout(true);
                mEditText.postDelayed(this::unlockChatListHeightDelayed, 200L);
            } else {
                scrollToLastDelayed();
                mEmotionLayout.getLayoutParams().height = 1;
                mEmotionLayout.setVisibility(View.INVISIBLE);
            }
            return false;
        });
        mChatAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            hideEmotionLayout(false);
            view.requestFocus();
            int id = view.getId();
            if (id == R.id.image || id == R.id.image_other) {
                if (moreSelectType) return;
                ChatMsgEntity entity = mChatAdapter.getData().get(position);
                L.e(TAG, "initListener: " + entity.getText());
                if (TextUtils.isEmpty(entity.getText())) return;
                LargerImageActivity.start(this, entity.getText());
                // TODO: 2019/11/27 显示大图
            } else if (id == R.id.messageUserAvatar) {
                if (moreSelectType) return;
                ChatMsgEntity entity = mChatAdapter.getData().get(position);
                System.out.println(entity.userId+"-=--=---");
                ArouteUtils.toPathWithId(ArouterApi.USER_DETAIL, entity.userId);
//                ARouter.getInstance().build(ArouterApi.USER_DETAIL).withInt("id", entity.userId).navigation();
            } else if (id == R.id.view_select) {
                ChatMsgEntity item = mChatAdapter.getItem(position);
                if (mChatAdapter.isItemSelected(item)) {
                    mChatAdapter.removeSelection(item);
                } else {
                    mChatAdapter.addSelection(item);
                }
            }
        });
        mChatAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
            if (moreSelectType) return false;
            hideEmotionLayout(false);
            view.requestFocus();
            int horOffSet = 0;
            int verOffSet = 0;
            boolean isRight = false;
            int id = view.getId();
            if (id == R.id.bubble_me) {
                horOffSet = DensityUtils.dp2px(this, 20);
                verOffSet = DensityUtils.sp2px(this, 4);
                isRight = true;
            } else if (id == R.id.bubble_other) {
                horOffSet = DensityUtils.dp2px(this, 20);
                verOffSet = DensityUtils.dp2px(this, 4);
                isRight = false;
            } else if (id == R.id.image) {
                horOffSet = DensityUtils.dp2px(this, 60);
                verOffSet = DensityUtils.dp2px(this, 24);
                isRight = true;
            } else if (id == R.id.image_other) {
                horOffSet = DensityUtils.dp2px(this, 60);
                verOffSet = DensityUtils.dp2px(this, 24);
                isRight = false;
            }
            showOptionPop(view, horOffSet, verOffSet, isRight, mChatAdapter.getItem(position));
            return true;
        });
        mChatAdapter.setOnItemAttentionClickListener(() -> {
            new RetrofitUtils().createApi(ChatService.class).addAttention(setToId()).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
                @Override
                public void _onNext(HttpResult data) {
                    if (data.isSuccess()) {
                        p.attentionSuccess();
                    }
                }

                @Override
                public void _onError(String msg) {

                }
            });
        });
    }

    private static final String TAG = "BaseChatActivity";

    protected void hideEmotionLayout(boolean showSoftInput) {
        isEmotionViewShown = false;
        mEmotionLayout.getLayoutParams().height = 1;
        mEmotionLayout.setVisibility(View.GONE);
        if (showSoftInput) {
            KeyBoardUtils.openKeybord(mEditText, this);
        }
    }

    protected void lockChatListHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) sr_rv.getLayoutParams();
        params.height = sr_rv.getHeight();
        params.weight = 0.0F;
    }

    protected void unlockChatListHeightDelayed() {
        mEditText.postDelayed(() -> {
            ((LinearLayout.LayoutParams) sr_rv.getLayoutParams()).weight = 1.0F;
            ((LinearLayout.LayoutParams) sr_rv.getLayoutParams()).height = 0;
        }, 200L);
    }

    protected void scrollToLastDelayed() {
        mEditText.postDelayed(() -> {
            // 延时保证键盘隐藏后再滚动至最新消息
            mChatAdapter.showLastItem(false);
        }, 200L);
    }

    protected boolean isSoftInputShown() {
        return KeyBoardUtils.getSupportSoftInputHeight(this) > 0;
    }

    protected void showEmotionLayout() {
        isEmotionViewShown = true;
//        int softInputHeight = KeyBoardUtils.getSupportSoftInputHeight(this);
        int softInputHeight = (int) SPUtils1.get(this, Constant.PRE_KEYBOARD_HEIGHT,
                DensityUtils.dp2px(this, 280));
//        if (softInputHeight <= 0) {
//        }
        KeyBoardUtils.closeKeybord(mEditText, this);
        mEmotionLayout.getLayoutParams().height = softInputHeight;
        mEmotionLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 显示标题栏菜单
     */
    protected void showOptionPop(View view, int horizontalOffset, int verticalOffset, boolean isRight, final ChatMsgEntity chatMsgEntity) {
        String[] menus;

        //1分钟内消息可以撤回
        if (chatMsgEntity.isOutgoing() && (System.currentTimeMillis() - chatMsgEntity.getDate() * 1000) < (1000 * 60)) {
            if (chatMsgEntity.getMsgType() == ChatMsgEntity.MsgType.comeText || chatMsgEntity.getMsgType() == ChatMsgEntity.MsgType.toText) {
//                menus = new String[]{"复制", "转发", "撤回", "删除", "多选"};
                menus = new String[]{"转发", "撤回", "复制", "删除"};
            } else if (chatMsgEntity.getMsgType() == ChatMsgEntity.MsgType.comeVoice || chatMsgEntity.getMsgType() == ChatMsgEntity.MsgType.toVoice) {
                menus = new String[]{"撤回", "删除"};
            } else if (chatMsgEntity.getMsgType() == ChatMsgEntity.MsgType.comeImage || chatMsgEntity.getMsgType() == ChatMsgEntity.MsgType.toImage) {
                menus = new String[]{"转发", "撤回", "删除"};
            } else menus = new String[]{"撤回", "删除"};
        } else {
            if (chatMsgEntity.getMsgType() == ChatMsgEntity.MsgType.comeText || chatMsgEntity.getMsgType() == ChatMsgEntity.MsgType.toText) {
//                menus = new String[]{"复制", "转发", "删除", "多选"};
                menus = new String[]{"转发", "复制", "删除"};
            } else if (chatMsgEntity.getMsgType() == ChatMsgEntity.MsgType.comeVoice || chatMsgEntity.getMsgType() == ChatMsgEntity.MsgType.toVoice) {
                menus = new String[]{"删除"};
//                menus = new String[]{"删除", "多选"};
            } else if (chatMsgEntity.getMsgType() == ChatMsgEntity.MsgType.comeImage || chatMsgEntity.getMsgType() == ChatMsgEntity.MsgType.toImage) {
                menus = new String[]{"转发", "删除"};
            } else menus = new String[]{"删除"};
        }
        int windowPos[] = UIUtil.calculatePopWindowPos(view, LongClickDialog.getShowDialogHeight(menus.length), verticalOffset, isRight);
        windowPos[0] -= horizontalOffset;//水平方向调整
        LongClickDialog.showLongClickDialogByPosition(this, getWindow().getDecorView(), null, menus, Gravity.TOP | Gravity.START, windowPos[0],
                windowPos[1], item -> {
                    switch (item) {
                        case "复制":
                            if (CopyUtils.copy2Board(this, chatMsgEntity.getText())) {
                                toastTip("复制成功");
                            } else toastTip("复制失败");
                            break;
                        case "撤回":
                            p.revocationMsg(chatMsgEntity);
                            break;
                        case "删除":
                            if (chatMsgEntity.getMsgType() == ChatMsgEntity.MsgType.comeVoice || chatMsgEntity.getMsgType() == ChatMsgEntity.MsgType.toVoice) {
                                mVoicePlayerWrapper.stopVoice();
                            }
                            p.deleteItems(chatMsgEntity);
                            break;
                        case "转发":
                            transponds.clear();
                            transponds.add(chatMsgEntity);
                            Intent intent = new Intent(BaseChatActivity.this, MessageListActivity.class);
                            intent.putExtra("uid", setFromId());
                            intent.putExtra("type", MessageListActivity.TYPE_TRANSPOND);
                            intent.putExtra("data", chatMsgEntity);
                            startActivityForResult(intent, REQUEST_CODE_TRANSPOND);
                            break;
                        case "多选":
                            // 设置多选模式
                            moreSelectType = true;
                            refreshMoreTypeMode();
                            break;
                    }
                });
    }

    public void refreshMoreTypeMode() {
        if (moreSelectType) {
            hideEmotionLayout(false);
            if (isSoftInputShown()) {
                KeyBoardUtils.closeKeybord(mEditText, this);
            }
        }
        mInput.setVisibility(moreSelectType ? View.GONE : View.VISIBLE);
        bottomView.setVisibility(moreSelectType ? View.VISIBLE : View.GONE);
        mChatAdapter.setMoreType(moreSelectType);
    }

    @Override
    public void sendRandomContent(String title) {

    }
    public VoicePlayHelper getVoicePlayerWrapper() {
        return mVoicePlayerWrapper;
    }
}