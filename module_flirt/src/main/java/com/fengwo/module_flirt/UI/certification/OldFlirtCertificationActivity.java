package com.fengwo.module_flirt.UI.certification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_chat.mvp.ui.adapter.ChatCardPostAdapter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.PostCardItem;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.pop.ChooseMediaTypePopwindow;
import com.fengwo.module_comment.utils.AudioRecoderUtils;
import com.fengwo.module_comment.utils.BitmapUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.chat.VoicePlayer;
import com.fengwo.module_comment.widget.GridItemDecoration;
import com.fengwo.module_comment.widget.MenuItem;
import com.fengwo.module_flirt.Interfaces.IFlirtCerView;
import com.fengwo.module_flirt.P.FlirtCerPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.certification.adapter.FlirtCerAdapter;
import com.fengwo.module_flirt.adapter.CerMediaAdapter;
import com.fengwo.module_flirt.bean.CerMsgBean;
import com.fengwo.module_flirt.bean.CerTagBean;
import com.fengwo.module_flirt.dialog.CerIdentityPop;
import com.fengwo.module_login.utils.UserManager;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/3/27
 */
@Route(path = ArouterApi.FLIRT_CERTIFICATION)
public class OldFlirtCertificationActivity extends BaseMvpActivity<IFlirtCerView, FlirtCerPresenter> implements IFlirtCerView {

    private final int REQUEST_GET_GALLERY = 0;
    private final int REQUEST_CHANGE_POST_IMAGE = 1;
    private final int REQUEST_CHANGE_POST_VIDEO = 2;
    private final int REQUEST_SET_CATEGORY = 3;
    private final int REQUEST_SET_TAG = 4;

    @BindView(R2.id.tv_record_tips)
    TextView tvRecordTips;
    @BindView(R2.id.iv_record_start)
    ImageView ivRecordStart;
    @BindView(R2.id.tv_record_duration)
    TextView tvRecordDuration;
    @BindView(R2.id.iv_record_del)
    ImageView ivRecordDel;
    @BindView(R2.id.rv_upload)
    RecyclerView rvUpload;
    @BindView(R2.id.menu_tag)
    MenuItem menuTag;
    @BindView(R2.id.menu_shenfen)
    MenuItem menuShenfen;
    @BindView(R2.id.iv_voice_wava)
    ImageView ivVoiceWava;
    @BindView(R2.id.ll_voice_record_finish)
    LinearLayout llVoiceRecordFinish;

    AudioRecoderUtils recordUtils;
    VoicePlayer voicePlayer;

    private CerMediaAdapter adapter;
    private ChooseMediaTypePopwindow chooseMediaTypePopwindow;

    @Autowired
    UserProviderService userProviderService;

    private int duration = 0;
    private String voiceFilePath;
    private String tagIds;
    private String tagName;
    private String occaName;

    enum VoiceType {
        EMPTY,
        FINISH,
        PLAYING;
    }

    @Override
    public FlirtCerPresenter initPresenter() {
        return new FlirtCerPresenter();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true).setTitle("达人资料设置")
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black).build();
        addRecordListener();
        showVoiceViewByType(VoiceType.EMPTY);
        p.getCerMsg();
        ivRecordStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ivRecordStart.setPressed(true);
                    recordUtils.startRecord();
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
                    ivRecordStart.setPressed(false);
                    recordUtils.stopRecord();
                }
                return true;
            }
        });
        voicePlayer = new VoicePlayer(this, new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                showVoiceViewByType(VoiceType.FINISH);
            }
        });

        // add model
        adapter = new CerMediaAdapter(this);
        adapter.setListener(new CerMediaAdapter.OnItemClickListener() {
            @Override
            public void itemAdd(boolean isAdd) {
                L.e("okh", "itemAdd: " + isAdd);
                int maxCount = isAdd ? ChatCardPostAdapter.MAX_COUNT_IMAGE - adapter.getData().size() : 3;
                chooseMediaTypePopwindow.showPopupWindow();
            }

            @Override
            public void itemChangePost(boolean isVideo) {
                L.e("okh", "itemChangePost: " + isVideo);
                MImagePicker.openImagePicker(OldFlirtCertificationActivity.this, MImagePicker.TYPE_ALL, isVideo ? REQUEST_CHANGE_POST_VIDEO : REQUEST_CHANGE_POST_IMAGE);
//                openImageSelector(isVideo ? REQUEST_CHANGE_POST_VIDEO : REQUEST_CHANGE_POST_IMAGE, 1, true);
            }

            @Override
            public void itemDel() {

            }
        });
        rvUpload.setLayoutManager(new GridLayoutManager(this, FlirtCerAdapter.MAX_COUNT_IMAGE));
        rvUpload.addItemDecoration(new GridItemDecoration(this, DensityUtils.dp2px(this, 20)));
        rvUpload.setAdapter(adapter);

        chooseMediaTypePopwindow = new ChooseMediaTypePopwindow(this);
        chooseMediaTypePopwindow.setOnTakePopwindowClickListener(new ChooseMediaTypePopwindow.OnChooseClickListener() {
            @Override
            public void onImgClick() {
                MImagePicker.openImagePicker(OldFlirtCertificationActivity.this, MImagePicker.TYPE_IMG, REQUEST_GET_GALLERY);
            }

            @Override
            public void onVideoClick() {
//                if (adapter.getData().size() > 0) {
//                    ToastUtils.showShort(FlirtCertificationActivity.this, "视频只能单传！");
//                    return;
//                }
                MImagePicker.openImagePicker(OldFlirtCertificationActivity.this, MImagePicker.TYPE_VEDIO, REQUEST_GET_GALLERY);
            }
        });
    }

    private void addRecordListener() {
        recordUtils = new AudioRecoderUtils();
        recordUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                duration = (int) (time / 1000);
                tvRecordTips.setVisibility(View.VISIBLE);
                tvRecordTips.setText(duration + " 秒");
                tvRecordTips.setTextColor(getResources().getColor(R.color.purple_9966ff));
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                if (duration < 3 || duration > 30) {
                    tvRecordTips.setVisibility(View.VISIBLE);
                    tvRecordTips.setText("录音时长必须在3-30之间");
                    tvRecordTips.setTextColor(getResources().getColor(R.color.red_f46060));
                } else {
//                    showRecordCompleteView();
                    showVoiceViewByType(VoiceType.FINISH);
                    voiceFilePath = filePath;
                }
            }
        });
    }

    private void showVoiceViewByType(VoiceType type) {
        llVoiceRecordFinish.setVisibility(View.GONE);
        ivRecordStart.setVisibility(View.GONE);
        tvRecordTips.setVisibility(View.INVISIBLE);
        ivRecordDel.setVisibility(View.GONE);
        switch (type) {
            case EMPTY:
                isPlay = false;
                try {
                    voicePlayer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ivRecordStart.setVisibility(View.VISIBLE);
                break;
            case FINISH:
                isPlay = false;
                llVoiceRecordFinish.setVisibility(View.VISIBLE);
                ivRecordDel.setVisibility(View.VISIBLE);
                ivVoiceWava.setImageResource(R.mipmap.ic_voice_card);
                tvRecordDuration.setText(TimeUtils.long2String(duration * 1000));
                break;
            case PLAYING:
                isPlay = true;
                llVoiceRecordFinish.setVisibility(View.VISIBLE);
                ivRecordDel.setVisibility(View.VISIBLE);
                ImageLoader.loadGif(ivVoiceWava, R.drawable.voice_playing);
                tvRecordDuration.setText(TimeUtils.long2String(duration * 1000));
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_flirt_cer;
    }

    private boolean isPlay = false;

    @OnClick({R2.id.iv_record_del, R2.id.ll_voice_record_finish, R2.id.menu_tag, R2.id.tv_flirt_cer_save, R2.id.menu_shenfen})
    void onClick(View v) {
        if (v.getId() == R.id.iv_record_del) {
            showVoiceViewByType(VoiceType.EMPTY);
        } else if (v.getId() == R.id.ll_voice_record_finish) {
            if (isPlay) {
                try {
                    voicePlayer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showVoiceViewByType(VoiceType.FINISH);
            } else {
                try {
                    voicePlayer.play(voiceFilePath);
                    showVoiceViewByType(VoiceType.PLAYING);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (v.getId() == R.id.menu_tag) {
            FlirtCerTagActivity.start(this, 4, "", REQUEST_SET_TAG);
        } else if (v.getId() == R.id.tv_flirt_cer_save) {
            if (TextUtils.isEmpty(voiceFilePath) || duration < 3) {
                toastTip("请上传录音");
                return;
            }
            if (adapter.getData().size() <= 0) {
                toastTip("请上传图片或者视频");
                return;
            }
            if (TextUtils.isEmpty(menuShenfen.getRightText())) {
                toastTip("请选择身份");
                return;
            }
            if (TextUtils.isEmpty(menuTag.getRightText())) {
                toastTip("请选择标签");
                return;
            }
            KLog.e("size", adapter.getData().toString() + "size:" + adapter.getData().size());
            p.upLoadAudio("",userProviderService.getUserInfo().id, duration, voiceFilePath, menuShenfen.getRightText(), menuTag.getRightText(), adapter.getData());
        } else if (v.getId() == R.id.menu_shenfen) {
            CerIdentityPop cerIdentityPop = new CerIdentityPop(this, new CerIdentityPop.OnIdentityListener() {
                @Override
                public void onIdentitySelect(String identity) {
                    menuShenfen.setRightText(identity);
                }
            });
            cerIdentityPop.showPopupWindow();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (data != null && requestCode == REQUEST_GET_GALLERY) { // 添加卡片数据
            String url = MImagePicker.getImagePath(this, data);
            if (!TextUtils.isEmpty(url)) {
                String substring = url.substring(url.lastIndexOf('.') + 1);
                String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(substring);
                if (MimeType.isVideo(type)) {// 选择视频
                    Flowable.create((FlowableOnSubscribe<PostCardItem>) emitter -> {
                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        mmr.setDataSource(url);
                        String width = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                        String height = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                        Bitmap bitmap = mmr.getFrameAtTime();
                        File file = BitmapUtils.saveBitmap(this, bitmap, String.valueOf(System.currentTimeMillis()));
                        PostCardItem post = new PostCardItem(file.getPath(), Integer.parseInt(width), Integer.parseInt(height), true);
                        post.rType = 2;
                        emitter.onNext(post);
                    }, BackpressureStrategy.ERROR).compose(bindToLifecycle()).compose(io_main()).subscribe(new Consumer<PostCardItem>() {
                        @Override
                        public void accept(PostCardItem postCardItem) throws Exception {
                            // 设置视频第一张图片
//                            adapter.setPost(postCardItem);
                            // 视频路径
//                                PostCardItem item = new PostCardItem(postCardItem.filePath, postCardItem.width, postCardItem.height, true);
//                                item.videoPath = filePath;
                            postCardItem.videoPath = url;
                            adapter.addData(postCardItem);
                        }
                    });
                } else {
                    // 获取图片宽高
                    BitmapFactory.Options options = getPictureOption(url);
                    int width = options.outWidth;
                    int height = options.outHeight;
                    // 构造数据
                    PostCardItem item = new PostCardItem(url, width, height, false);
                    item.rType = 1;
//                        L.e(TAG, "onActivityResult: "+ filePath);
//                        if (adapter.isDataEmpty()) adapter.setPost(item);
                    if (adapter.isDataEmpty()) {
                        List<PostCardItem> items = new ArrayList<>();
                        items.add(item);
                        adapter.setData(items);
                    } else adapter.addData(item);
                }
            }
//        } else if (data != null && (requestCode == REQUEST_CHANGE_POST_IMAGE || requestCode == REQUEST_CHANGE_POST_VIDEO)) { // 设置封面
//            String url = MImagePicker.getImagePath(this, data);
////            List<String> list = Matisse.obtainPathResult(data);
//            BitmapFactory.Options options = getPictureOption(url);
//            int width = options.outWidth;
//            int height = options.outHeight;
            // 构造数据
//            PostCardItem item = new PostCardItem(url, width, height, requestCode == REQUEST_CHANGE_POST_VIDEO);
//            adapter.setPost(item);
        } else if (requestCode == REQUEST_SET_TAG) {// 设置标签
            List<CerTagBean> tagList = (List<CerTagBean>) data.getSerializableExtra("data");
            if (tagList != null) {
                StringBuilder nameBuilder = new StringBuilder();
                StringBuilder tagBuilder = new StringBuilder();
                for (CerTagBean model : tagList) {
                    nameBuilder.append(model.getTagName()).append(",");
                    tagBuilder.append(model.getId()).append(",");
                }
                menuTag.setRightText(nameBuilder.substring(0, nameBuilder.length() - 1));
                tagIds = tagBuilder.substring(0, tagBuilder.length() - 1);
            }
        }
    }

    // 获取图片信息
    private BitmapFactory.Options getPictureOption(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        voicePlayer.release();
        recordUtils.stopRecord();
    }

    @Override
    public void returnSaveCerMsg(HttpResult result) {
        toastTip("保存成功");
        UserManager.getInstance().setWenboCer(true);
        finish();
    }

    @Override
    public void checkCerMsg(CerMsgBean cerMsgBean) {
        menuShenfen.setRightText(cerMsgBean.getOccuName());
        menuTag.setRightText(cerMsgBean.getTagName());
        adapter.setData(cerMsgBean.getResList());
        duration = cerMsgBean.getAudioLength();
        voiceFilePath = cerMsgBean.getAudioPath();
        if (duration >= 3) {
            showVoiceViewByType(VoiceType.FINISH);
        }
    }
}
