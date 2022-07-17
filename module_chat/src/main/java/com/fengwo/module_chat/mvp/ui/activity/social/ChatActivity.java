package com.fengwo.module_chat.mvp.ui.activity.social;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buildware.widget.indeterm.IndeterminateCheckBox;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.ChatContent;
import com.fengwo.module_chat.mvp.model.bean.uimsg.BaseMessageView;
import com.fengwo.module_chat.mvp.model.bean.uimsg.IMessageView;
import com.fengwo.module_chat.mvp.model.bean.uimsg.MessageViewFractory;
import com.fengwo.module_chat.mvp.presenter.ChatPresenter;
import com.fengwo.module_chat.mvp.ui.contract.IChatView;
import com.fengwo.module_chat.mvp.ui.adapter.ChatAdapter;
import com.fengwo.module_comment.utils.AudioRecoderUtils;
import com.fengwo.module_comment.utils.FileUtils;
import com.fengwo.module_chat.utils.Logger;
import com.fengwo.module_chat.utils.PopupWindowFactory;
import com.fengwo.module_chat.widgets.chat.ChatBottomPanelView;
import com.fengwo.module_chat.widgets.chat.PanelCallback;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_websocket.EventConstant;
import com.fengwo.module_websocket.FWWebSocket;
import com.fengwo.module_websocket.bean.ChatMessage;
import com.fengwo.module_websocket.eventtag.EventChatLogin;
import com.fengwo.module_websocket.eventtag.EventChatMessage;
import com.fengwo.module_websocket.eventtag.EventWsConnected;
import com.fengwo.module_websocket.security.DataSecurityUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.OnKeyboardListener;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;
import com.scwang.smart.refresh.footer.ClassicsFooter;

import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.tapadoo.alerter.Alerter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ChatActivity extends BaseMvpActivity<IChatView, ChatPresenter> implements PanelCallback {


    private final static int REQUEST_CODE = 10001;

    public static void start(Context context, int fromUid, String toUid) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("fromUid", fromUid);
        intent.putExtra("toUid", toUid);
        context.startActivity(intent);
    }

    public String g_uids = "";

    public int fromUid = -1;
    public String fromUserName = "";
    public String fromUserImg = "";
    public String fromUserImgIp = "";

    public String toUid = "";
    public String toUserName = "";
    public String toUserImg = "";
    public String toUserImgIp = "";

    public RelativeLayout chat_list_bg, mult_select_bgview;

    public EditText input_box;
    ImageView emos_btn;
    ImageView mImageView;
    TextView mTextView;
    TextView start_record_btn;


    IndeterminateCheckBox mult_checkbox;
    ImageView delete_img_btn, fowrd_img_btn, cancel_img_btn;


    public RelativeLayout chat_view_ac;
    public LinearLayout input_bottom_view;
    private List<ChatContent> personChats = new ArrayList<ChatContent>();


    DataSecurityUtil desUtils = new DataSecurityUtil();

    private static final String wsurl = "ws://192.168.2.182:8085/hkim";
//    public WebSocketConnection mConnect = new WebSocketConnection();


    //录音需要
    private AudioRecoderUtils mAudioRecoderUtils;
    private PopupWindowFactory mPop;


    RecyclerView recyclerView;
    ChatBottomPanelView panelView;

    private ChatAdapter mAdapter;


    private static final String TAG = "ChatActivity";

    @SuppressLint("CheckResult")
    private void initRxBusEvent() {

        RxBus.get().toObservable(EventWsConnected.class)
                .compose(bindToLifecycle())
                .subscribe(eventWsConnected -> {
//                    FWWebSocket.getInstance().sendLoginChatMessage(fromUid);
                });
        RxBus.get().toObservable(EventChatLogin.class)
                .compose(bindToLifecycle())
                .subscribe(eventChatLogin -> {
                    firstComeinMessage();
                });
        RxBus.get().toObservable(EventChatMessage.class)
                .compose(bindToLifecycle())
                .subscribe(new Observer<EventChatMessage>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EventChatMessage eventChatMessage) {
                        dealWithChatMessage(eventChatMessage.chatMessage);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private boolean isAllowScrollToBottom = true;//用户拖拽判断屏幕最后一个Item position 是不是最后一个


    private void dealWithChatMessage(List<ChatMessage> chatMessage) {
        List<BaseMessageView> messageViews = new ArrayList<>();
        for (int i = chatMessage.size() - 1; i > 0; i--) {
            BaseMessageView baseMessageView = MessageViewFractory.createMessageView(chatMessage.get(i));
            if (Integer.parseInt(baseMessageView.getChatMessage().getFromUid()) == fromUid) {
                baseMessageView.setItemType(1);
            } else {
                baseMessageView.setItemType(2);
            }
            messageViews.add(baseMessageView);
        }
        mAdapter.addData(0, messageViews);

//        mAdapter.addData(baseMessageView);
        if (isAllowScrollToBottom)
            scrollToBottom();
    }


    private LinearLayoutManager layoutManager;

    private void initChatView() {
        panelView.setPanelCallback(this);
        recyclerView = findViewById(R.id.listView);
        ClassicsFooter footer = findViewById(R.id.footer);
        View arrow = footer.findViewById(ClassicsFooter.ID_IMAGE_ARROW);
        arrow.setScaleY(-1);//必须设置

        mAdapter = new ChatAdapter(mData);
        recyclerView.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setScaleY(-1);//必须设置
        scrollToBottom();

        final RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);//必须关闭
        refreshLayout.setEnableAutoLoadMore(true);//必须关闭
        refreshLayout.setEnableNestedScroll(false);//必须关闭
        refreshLayout.setEnableScrollContentWhenLoaded(true);//必须关闭
        refreshLayout.getLayout().setScaleY(-1);//必须设置
//        refreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDeciderAdapter() {
//            @Override
//            public boolean canLoadMore(View content) {
//                return super.canRefresh(content);//必须替换
//            }
//        });
        //监听加载，而不是监听 刷新
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        scrollToBottom();
                        refreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isAllowScrollToBottom = layoutManager.findLastVisibleItemPosition() == (mAdapter.getItemCount() - 1);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

        });

    }

    ArrayList<IMessageView> mData = new ArrayList<>();


    //获得第一次20条消息
    public void firstComeinMessage() {
        try {
            JSONObject clientpara = new JSONObject();
            clientpara.put("eventId", EventConstant.firstComeinMessage);
            clientpara.put("fromUid", "" + fromUid);
            clientpara.put("toUid", "" + toUid);
            clientpara.put("index_num", "0");

            if (FWWebSocket.getInstance().isConnect()) {
                FWWebSocket.getInstance().sendTextMessage(clientpara.toString());
            } else {
                Toast.makeText(ChatActivity.this, "链接服务器失败", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        panelView = findViewById(R.id.chatBottomPanelView);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .setOnKeyboardListener(new OnKeyboardListener() {
                    @Override
                    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                        panelView.onKeyboardChanged(isPopup, keyboardHeight);
                        if (isPopup) scrollToBottom();
                    }
                }).init();
        initChatView();

        //参数
        Intent intent = getIntent();

        g_uids = intent.getStringExtra("g_uids");

        fromUid = intent.getIntExtra("fromUid", -1);
        fromUserName = intent.getStringExtra("fromUserName");
        fromUserImg = intent.getStringExtra("fromHeadImg");
        fromUserImgIp = intent.getStringExtra("fromUserImgIp");

        toUid = intent.getStringExtra("toUid");
        toUserName = intent.getStringExtra("toUserName");
        toUserImg = intent.getStringExtra("toUserImg");//名称
        toUserImgIp = intent.getStringExtra("toUserImgIp");//名称
        chat_view_ac = (RelativeLayout) findViewById(R.id.chat_view_ac);
        p.setFromUid(fromUid + "");
        p.setToUid(toUid);

        //PopupWindow的布局文件
        final View view = View.inflate(this, R.layout.layout_microphone, null);
        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);

        start_record_btn = (TextView) findViewById(R.id.start_record_btn);
        input_bottom_view = (LinearLayout) findViewById(R.id.input_bottom_view);

        mult_select_bgview = (RelativeLayout) findViewById(R.id.mult_select_bgview);

        mult_checkbox = (IndeterminateCheckBox) findViewById(R.id.mult_checkbox);
        delete_img_btn = (ImageView) findViewById(R.id.delete_img_btn);
        fowrd_img_btn = (ImageView) findViewById(R.id.fowrd_img_btn);
        cancel_img_btn = (ImageView) findViewById(R.id.cancel_img_btn);

        mult_checkbox.setState(true);
        mult_checkbox.setOnStateChangedListener(new IndeterminateCheckBox.OnStateChangedListener() {
            @Override
            public void onStateChanged(IndeterminateCheckBox checkBox, @Nullable Boolean newState) {

                if (newState) {
                    for (int i = 0; i < personChats.size(); i++) {
                        ChatContent chatContent = personChats.get(i);
                        chatContent.setCheckbox_is_true(1);
                        personChats.remove(i);
                        personChats.add(i, chatContent);

                    }
                } else {
                    for (int i = 0; i < personChats.size(); i++) {
                        ChatContent chatContent = personChats.get(i);
                        chatContent.setCheckbox_is_true(0);
                        personChats.remove(i);
                        personChats.add(i, chatContent);

                    }
                }
            }
        });

        delete_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e("personChats.size()===" + personChats.size());
                String wait_delete_guids = "";
                for (int i = 0; i < personChats.size(); i++) {
                    ChatContent chatContent = personChats.get(i);
                    Logger.e("chatContent.getCheckbox_is_true=====[" + i + "]    " + chatContent.getCheckbox_is_true());
                    if (chatContent.getCheckbox_is_true() == 1) {
                        wait_delete_guids = chatContent.getGuid() + "," + wait_delete_guids;
                    }

                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                if (wait_delete_guids.length() > 5) {
                    wait_delete_guids = wait_delete_guids.substring(0, wait_delete_guids.length() - 1);
                    deleteAChatMessage(wait_delete_guids);
                } else {
                    Alerter.create(ChatActivity.this)
                            .setTitle("提示")
                            .setText("并未选择任何要删除的消息")
                            .setBackgroundColorRes(R.color.red_ff3333) // or setBackgroundColorInt(Color.CYAN)
                            .show();
                }
            }
        });

        fowrd_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] ge_b = new byte[1];
                ge_b[0] = 0x05;
                String ge_str = new String(ge_b);

                String message_list = "";
                String message_type_list = "";
                for (int i = 0; i < personChats.size(); i++) {
                    ChatContent chatContent = personChats.get(i);
                    Logger.e("chatContent.getCheckbox_is_true=====[" + i + "]    " + chatContent.getCheckbox_is_true());
                    if (chatContent.getCheckbox_is_true() == 1) {
                        message_list = chatContent.getMessage() + ge_str + message_list;
                        message_type_list = chatContent.getMessage_type() + ge_str + message_type_list;
                    }

                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                if (message_list.length() > 0) {

                } else {
                    Alerter.create(ChatActivity.this)
                            .setTitle("提示")
                            .setText("并未选择任何需转发的消息")
                            .setBackgroundColorRes(R.color.red_ff3333) // or setBackgroundColorInt(Color.CYAN)
                            .show();
                }


            }
        });

        cancel_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < personChats.size(); i++) {
                    ChatContent chatContent = personChats.get(i);
                    chatContent.setCheckbox_is_true(0);
                    chatContent.setCheckbox_show(0);
                    personChats.remove(i);
                    personChats.add(i, chatContent);
                }
                mult_select_bgview.setVisibility(View.GONE);
                input_bottom_view.setVisibility(View.VISIBLE);
            }
        });


        mAudioRecoderUtils = new AudioRecoderUtils();
        mPop = new PopupWindowFactory(this, view);
        final Date[] date1 = new Date[1];
        final Date[] date2 = new Date[1];

        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.getTime(time));

            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                //Toast.makeText(ChatActivity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                mTextView.setText(TimeUtils.getTime(0));

                date2[0] = new Date();
                sound_length = TimeUtils.timeCalculateSeconds(date1[0], date2[0]);
                Logger.e("录音时间:" + sound_length + "秒");
                File f = new File(filePath);
                p.sendAudioMessage(f, sound_length);

            }
        });


        initRxBusEvent();
        connect();
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_chat;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {

//                List<String> mSelected = Matisse.obtainPathResult(data);
//                for (String a_path : mSelected) {
//                    //获取Options对象
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    //仅做解码处理，不加载到内存
//                    options.inJustDecodeBounds = true;
//                    //解析文件
//                    BitmapFactory.decodeFile(a_path, options);
//                    //获取宽高
//                    int imgWidth = options.outWidth;
//                    int imgHeight = options.outHeight;
//
//                    String imgFileName = fromUid + "_" + TimeUtils.getCurrentTimeInLong() + ".jpg";
//                    p.sendImageMessage(new File(a_path), imgWidth, imgHeight);
//                }
            } else if (requestCode == FLAG_TAKE_PHOTO) {
                if (photoUri != null) {
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

                    File f = new File(a_path);
                    p.sendImageMessage(f, imgWidth, imgHeight);
                }
            }
        }
    }


    int FLAG_TAKE_PHOTO = 1;
    String take_cam_fileName = "";
    Uri photoUri;
    ArrayList<String> imgArray = new ArrayList<String>();


    int audioIsShow = 0;

    public void setAudioBtnVisible() {
        if (audioIsShow == 0) {
            input_box.setVisibility(View.GONE);
            start_record_btn.setVisibility(View.VISIBLE);
            audioIsShow = 1;
        } else {
            input_box.setVisibility(View.VISIBLE);
            start_record_btn.setVisibility(View.GONE);
            audioIsShow = 0;
        }
    }


    //上传录音文件
    String audio_file_name = "";
    double sound_length = 0;


    public void deleteAChatMessage(String guidList) {

        try {
            JSONObject clientpara = new JSONObject();
            clientpara.put("eventId", EventConstant.deleteAChatMessage);
            clientpara.put("fromUid", "" + fromUid);
            clientpara.put("toUid", "" + toUid);
            clientpara.put("guidList", guidList);

            String msg = desUtils.encrypt(clientpara.toString());
//            if (mConnect.isConnected()) {
//                mConnect.sendTextMessage(msg);
//            } else {
//                Toast.makeText(ChatActivity.this, "链接服务器失败", Toast.LENGTH_LONG).show();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public ChatPresenter initPresenter() {
        return new ChatPresenter();
    }

    /**
     * websocket连接，接收服务器消息
     */
    private void connect() {
        FWWebSocket.getInstance().connect();
    }


    private void scrollToBottom() {
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void sendText(String str) {
        p.sendTextMessage(str);
    }

    @SuppressLint("CheckResult")
    @Override
    public void sendImage() {
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
//                        Matisse.from(this)
//                                .choose(MimeType.ofAll())
//                                .theme(R.style.Matisse_Zhihu)
//                                .countable(true)
//                                .maxSelectable(1)
//                                .capture(true)  //是否可以拍照
//                                .captureStrategy(//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
//                                        new CaptureStrategy(true, "fengwoImg"))
//
////                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
//                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                                .thumbnailScale(0.85f)
//                                .imageEngine(new MyGlideEngine())
//                                .forResult(REQUEST_CODE);
                    } else {
                        Toast.makeText(this, "您关闭了权限，请去设置页面开启", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @SuppressLint("CheckResult")
    @Override
    public void sendCamera() {
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    try {
                        if (granted) {
                            take_cam_fileName = TimeUtils.getCurrentTimeInLong() + ".jpg";
                            // 步骤一：创建存储照片的文件
                            String path = FileUtils.SD_PATH;
                            File file = new File(path, take_cam_fileName);
                            if (!file.getParentFile().exists())
                                file.getParentFile().mkdirs();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                //步骤二：Android 7.0及以上获取文件 Uri
                                photoUri = FileProvider.getUriForFile(ChatActivity.this, "fengwoImg", file);
                            } else {
                                //步骤三：获取文件Uri
                                photoUri = Uri.fromFile(file);
                            }
                            //步骤四：调取系统拍照
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(intent, FLAG_TAKE_PHOTO);
                        } else {

                            Toast.makeText(this, "您关闭了权限，请去设置页面开启", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){

                    }

                });

    }

    @Override
    public void sendAudio(String filePath, int sound_length) {
        p.sendAudioMessage(new File(filePath), sound_length);

    }

    @Override
    public void bottomShow() {

    }


}
