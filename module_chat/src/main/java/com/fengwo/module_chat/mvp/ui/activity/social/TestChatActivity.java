package com.fengwo.module_chat.mvp.ui.activity.social;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buildware.widget.indeterm.IndeterminateCheckBox;
import com.fengbo.module_chatlib.ui.msg.IMessageView;
import com.fengbo.module_chatlib.ui.msg.ImageMessage;
import com.fengbo.module_chatlib.utils.ChatPopwindow;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.base.ShowAllImgEvent;
import com.fengwo.module_chat.mvp.model.bean.EnterGroupModel;
import com.fengwo.module_chat.mvp.ui.activity.chat_new.BaseChatActivity;
import com.fengwo.module_chat.mvp.ui.adapter.ChatAdapter2;
import com.fengwo.module_comment.utils.FileUtils;
import com.fengwo.module_chat.widgets.chat.ChatBottomView;
import com.fengwo.module_chat.widgets.chat.PanelCallback;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.TimeUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;
import razerdp.basepopup.BasePopupWindow;

public class TestChatActivity extends BaseChatActivity implements PanelCallback {

    private final static int REQUEST_CODE = 10001;

    @BindView(R2.id.listView)
    RecyclerView listView;
    @BindView(R2.id.mult_checkbox)
    IndeterminateCheckBox multCheckbox;
    @BindView(R2.id.chatBottomView)
    ChatBottomView chatBottomView;
    @BindView(R2.id.delete_img_btn)
    ImageView deleteImgBtn;
    @BindView(R2.id.fowrd_img_btn)
    ImageView fowrdImgBtn;
    @BindView(R2.id.cancel_img_btn)
    ImageView cancelImgBtn;
    @BindView(R2.id.mult_select_bgview)
    RelativeLayout multSelectBgview;

    ChatPopwindow chatPopwindow;

    private ChatAdapter2 chatAdapter2;
    private List<IMessageView> messageViewList = new ArrayList<>();
    private int longClickPosition = -1;

    public static void start(Context context, String fromUid, String toUid) {
        Intent intent = new Intent(context, TestChatActivity.class);
        intent.putExtra("fromUid", fromUid);
        intent.putExtra("toUid", toUid);
        context.startActivity(intent);
    }

    private void scrollToBottom() {
        runOnUiThread(() -> {
            if (null != chatAdapter2)
                listView.scrollToPosition(chatAdapter2.getItemCount() - 1);
        });

    }

    @Override
    protected void initView() {
        chatPopwindow = new ChatPopwindow(this);
        chatPopwindow.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                longClickPosition = -1;
            }
        });
        chatPopwindow.setOnItemClickListener(new ChatPopwindow.OnItemClickListener() {
            @Override
            public void onReback() {
                if (longClickPosition == -1) {
                    return;
                }
                // TODO: 2019/11/25 获取数据，消息撤回
                String gid = messageViewList.get(longClickPosition).getChatMessage().getGuid();
                p.revocationMsg(null);
            }

            @Override
            public void onDel() {
                if (longClickPosition == -1) {
                    return;
                }
                // TODO: 2019/11/25 获取数据，删除消息
                String gid = messageViewList.get(longClickPosition).getChatMessage().getGuid();
                p.delMsg(gid);
            }

            @Override
            public void onCheckAble() {

            }
        });
        RxBus.get().toObservable(ShowAllImgEvent.class)
                .compose(bindToLifecycle())
                .subscribe(
                        showAllImgEvent -> {
                            showBigImg(showAllImgEvent.position);
                        }
                );
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .setOnKeyboardListener((isPopup, keyboardHeight) -> {
                    if (isPopup) scrollToBottom();
                }).init();
        // 设置底部按钮
        chatBottomView.setPanelCallback(this);
        // 设置RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        chatAdapter2 = new ChatAdapter2();
        chatAdapter2.setOnItemChildLongClickListener((adapter, view, position) -> {
            L.e("Xxxxxxxxxxx", view.getBottom() + "------" + view.getLeft());
            longClickPosition = position;
            chatPopwindow.showPopupWindow(view);
            return false;
        });
        chatAdapter2.setUpFetchEnable(true);

        listView.setLayoutManager(linearLayoutManager);
        listView.setAdapter(chatAdapter2);
        scrollToBottom();
    }

    @Override
    protected int getContentView() {
        return R.layout.test_chat;
    }

    @Override
    public String setFromId() {
        return getIntent().getStringExtra("fromUid");
    }

    @Override
    public String setToId() {
        return getIntent().getStringExtra("toUid");
    }

    @Override
    public void sendText(CharSequence text) {

    }

    @Override
    public void sendImage(String url, int width, int height) {

    }

    private void showBigImg(int position) {
        List<ImageInfo> imageInfos = new ArrayList<>();
        int startIndex = 0;
        for (int i = 0; i < messageViewList.size(); i++) {
            if (messageViewList.get(i) instanceof ImageMessage) {
                String messages = messageViewList.get(i).getMsg();
                ImageInfo info = new ImageInfo();
                info.setOriginUrl(messages);
                info.setThumbnailUrl(messages);
                imageInfos.add(info);
                if (position == i) {
                    startIndex = imageInfos.size() - 1;
                }
            }
        }
        ImagePreview
                .getInstance()
                .setContext(this)
                .setIndex(startIndex)// 默认显示第几个
                .setImageInfoList(imageInfos)// 图片集合
                .start();// 开始跳转
    }

    /**
     * 发送文字消息
     */
    @Override
    public void sendText(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        p.sendTxtMessage(str);
    }

    /**
     * 发送图片
     */
    @SuppressLint("CheckResult")
    @Override
    public void sendImage() {
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(bindToLifecycle())
                .subscribe(granted -> {
                    if (granted) {
                        MImagePicker.openImagePicker(TestChatActivity.this, MImagePicker.TYPE_IMG, REQUEST_CODE_CHOOSE);
                    } else {
                        Toast.makeText(this, "您关闭了权限，请去设置页面开启", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    int FLAG_TAKE_PHOTO = 1;
    Uri photoUri;
    String take_cam_fileName;

    /**
     * 发送照片
     */
    @SuppressLint("CheckResult")
    @Override
    public void sendCamera() {
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
                            if (!file.getParentFile().exists())
                                file.getParentFile().mkdirs();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                //步骤二：Android 7.0及以上获取文件 Uri
                                photoUri = FileProvider.getUriForFile(TestChatActivity.this, "fengwoImg", file);
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

    /**
     * 发送录音
     */
    @Override
    public void sendAudio(String filePath, int sound_length) {
        p.sendAudio(filePath, sound_length);
    }

    @Override
    public void bottomShow() {
        scrollToBottom();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                List<String> mSelected = Matisse.obtainPathResult(data);
                //获取Options对象
                BitmapFactory.Options options = new BitmapFactory.Options();
                //仅做解码处理，不加载到内存
                options.inJustDecodeBounds = true;
                //解析文件
                BitmapFactory.decodeFile(mSelected.get(0), options);
                //获取宽高
                int imgWidth = options.outWidth;
                int imgHeight = options.outHeight;
                p.sendImageMessage(mSelected.get(0), imgWidth, imgHeight);
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
                    p.sendImageMessage(a_path, imgWidth, imgHeight);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!chatBottomView.close())
            super.onBackPressed();

    }

    @Override
    public void enterGroupSuccess(EnterGroupModel data) {

    }

    @Override
    public void enterGroupFail() {

    }

    @Override
    public void hasItemInSession(boolean hasItemInSession) {

    }

    @Override
    public void onAttentionSuccess() {

    }
}