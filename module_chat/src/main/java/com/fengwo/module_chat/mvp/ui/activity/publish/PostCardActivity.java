package com.fengwo.module_chat.mvp.ui.activity.publish;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocation;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.R2;
import com.fengwo.module_chat.mvp.model.bean.CardDetailBean;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.bean.CardTagModel;
import com.fengwo.module_chat.mvp.model.bean.PostCardDraftModel;
import com.fengwo.module_comment.bean.PostCardItem;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_chat.mvp.presenter.PostCardPresenter;
import com.fengwo.module_chat.mvp.ui.adapter.ChatCardPostAdapter;
import com.fengwo.module_chat.mvp.ui.contract.IPostCardView;
import com.fengwo.module_chat.mvp.ui.dialog.SelectCardCategoryPopupWindow;
import com.fengwo.module_comment.widget.GridItemDecoration;
import com.fengwo.module_comment.MapLocationUtil;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.event.RefreshEvent;
import com.fengwo.module_comment.pop.ChooseMediaTypePopwindow;
import com.fengwo.module_comment.utils.BitmapUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.MyGlideEngine;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.ScreenUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.widget.AppTitleBar;
import com.fengwo.module_comment.widget.LoadingDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Consumer;
@Route(path = ArouterApi.CHAT_POST_CARD)
public class PostCardActivity extends BaseMvpActivity<IPostCardView, PostCardPresenter> implements IPostCardView {

    private final int REQUEST_GET_GALLERY = 0;
    private final int REQUEST_CHANGE_POST_IMAGE = 1;
    private final int REQUEST_CHANGE_POST_VIDEO = 2;
    private final int REQUEST_SET_CATEGORY = 3;
    private final int REQUEST_SET_TAG = 4;

    @BindView(R2.id.title)
    AppTitleBar titleBar;
    @BindView(R2.id.rv_chat)
    RecyclerView recyclerView;
    @BindView(R2.id.tv_chat_category)
    TextView tvCategory;
    @BindView(R2.id.view_category)
    View viewCategory;
    @BindView(R2.id.et_chat_content)
    EditText etContent;
    @BindView(R2.id.tv_chat_content_num)
    TextView tvContentNum;
    @BindView(R2.id.tv_chat_card_tag)
    TextView tvTag;
    @BindView(R2.id.tv_chat_card_location)
    TextView tvLocation;
    @BindView(R2.id.tv_chat_save)
    TextView tvSave;
    @BindView(R2.id.tv_chat_post)
    TextView tvPost;
    @BindView(R2.id.switch_location)
    Switch switchLocation;
    @BindView(R2.id.root)
    LinearLayout rootView;

    private ChatCardPostAdapter adapter;
    private RecommendCircleBean categoryBean;
    private AMapLocation locationModel;
    private String tagIds;
    private ChooseMediaTypePopwindow chooseMediaTypePopwindow;

    private int cardId = -1;//卡片id，编辑草稿的时候获取详情
    private boolean isSecondPost = false;//是否草稿发布

    @Override
    public PostCardPresenter initPresenter() {
        return new PostCardPresenter();
    }

    private static final String TAG = "PostCardActivity";

    @Override
    protected void initView() {
        cardId = getIntent().getIntExtra("id",-1);

        titleBar.setMoreClickListener(v -> finish());
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvContentNum.setText(String.format("%d/70", s == null ? 0 : s.length()));
            }
        });
        rootView.setMinimumHeight(ScreenUtils.getScreenHeight(this) - DensityUtils.dp2px(this, 72));

        RxPermissions rxPermissions = new RxPermissions(this);
        // add model
        adapter = new ChatCardPostAdapter(this);
        adapter.setListener(new ChatCardPostAdapter.OnItemClickListener() {
            @Override
            public void itemAdd(boolean isAdd) {
                rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {
                                chooseMediaTypePopwindow.showPopupWindow();
                            } else {
                                toastTip("您关闭了权限，请去设置页面开启");
                            }
                        });
                L.e(TAG, "itemAdd: " + isAdd);
//                int maxCount = isAdd ? ChatCardPostAdapter.MAX_COUNT_IMAGE - adapter.getData().size() : 3;
//                openImageSelector(REQUEST_GET_GALLERY, maxCount, adapter.getData().size() > 0 ? false : true);
            }

            @Override
            public void itemChangePost(boolean isVideo) {
                rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {
                                MImagePicker.openImagePicker(PostCardActivity.this, MImagePicker.TYPE_IMG, isVideo ? REQUEST_CHANGE_POST_VIDEO : REQUEST_CHANGE_POST_IMAGE);
                            } else {
                                toastTip("您关闭了权限，请去设置页面开启");
                            }
                        });
                L.e(TAG, "itemChangePost: " + isVideo);

//                openImageSelector(isVideo ? REQUEST_CHANGE_POST_VIDEO : REQUEST_CHANGE_POST_IMAGE, 1, true);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, ChatCardPostAdapter.MAX_COUNT_IMAGE));
        recyclerView.addItemDecoration(new GridItemDecoration(this, DensityUtils.dp2px(this, 20)));
        recyclerView.setAdapter(adapter);

        tvPost.setEnabled(false);
        // location listenter
        switchLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed() && isChecked) requestLocationPermission();
            else tvLocation.setText("");
        });

        initDataFromCache();
        chooseMediaTypePopwindow = new ChooseMediaTypePopwindow(this);
        chooseMediaTypePopwindow.setOnTakePopwindowClickListener(new ChooseMediaTypePopwindow.OnChooseClickListener() {
            @Override
            public void onImgClick() {
                MImagePicker.openImagePicker(PostCardActivity.this, MImagePicker.TYPE_IMG, REQUEST_GET_GALLERY);
            }

            @Override
            public void onVideoClick() {
                if (adapter.getData().size() > 0) {
                    ToastUtils.showShort(PostCardActivity.this, "视频只能单传！");
                    return;
                }
                MImagePicker.openImagePicker(PostCardActivity.this, MImagePicker.TYPE_VEDIO, REQUEST_GET_GALLERY);
            }
        });

        if (cardId>0){
            p.getCardDetail(cardId);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.chat_activity_post_card;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (data != null && requestCode == REQUEST_GET_GALLERY) { // 添加卡片数据
            String url = MImagePicker.getImagePath(this, data);
            if (!TextUtils.isEmpty(url)) {
                boolean isVideo = false;
                String substring = url.substring(url.lastIndexOf('.') + 1);
                String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(substring);
                if (MimeType.isVideo(type)) {// 选择视频
                    isVideo = true;
                    Flowable.create((FlowableOnSubscribe<PostCardItem>) emitter -> {
                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        mmr.setDataSource(url);
                        String width = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                        String height = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                        Bitmap bitmap = mmr.getFrameAtTime();
                        File file = BitmapUtils.saveBitmap(this, bitmap, String.valueOf(System.currentTimeMillis()));
                        PostCardItem post = new PostCardItem(file.getPath(), Integer.parseInt(width), Integer.parseInt(height), true);
                        emitter.onNext(post);
                    }, BackpressureStrategy.ERROR).compose(bindToLifecycle()).compose(io_main()).subscribe(new Consumer<PostCardItem>() {
                        @Override
                        public void accept(PostCardItem postCardItem) throws Exception {
                            // 设置视频第一张图片
                            adapter.setPost(postCardItem);
                            // 视频路径
//                                PostCardItem item = new PostCardItem(postCardItem.filePath, postCardItem.width, postCardItem.height, true);
//                                item.videoPath = filePath;
                            postCardItem.videoPath = url;
                            adapter.addData(postCardItem, true);
                            checkPostButtonStatus();
                        }
                    });
                } else {
                    // 获取图片宽高
                    BitmapFactory.Options options = getPictureOption(url);
                    int width = options.outWidth;
                    int height = options.outHeight;
                    // 构造数据
                    PostCardItem item = new PostCardItem(url, width, height, false);
//                        L.e(TAG, "onActivityResult: "+ filePath);
//                        if (adapter.isDataEmpty()) adapter.setPost(item);
                    if (adapter.isDataEmpty()) {
                        List<PostCardItem> items = new ArrayList<>();
                        items.add(item);
                        adapter.setData(items, false);
                    } else adapter.addData(item, false);
                    checkPostButtonStatus();
                }
            }
        } else if (data != null && (requestCode == REQUEST_CHANGE_POST_IMAGE || requestCode == REQUEST_CHANGE_POST_VIDEO)) { // 设置封面
            String url = MImagePicker.getImagePath(this, data);
//            List<String> list = Matisse.obtainPathResult(data);
            BitmapFactory.Options options = getPictureOption(url);
            int width = options.outWidth;
            int height = options.outHeight;
            // 构造数据
            PostCardItem item = new PostCardItem(url, width, height, requestCode == REQUEST_CHANGE_POST_VIDEO);
            adapter.setPost(item);
        } else if (requestCode == REQUEST_SET_CATEGORY) {// 设置圈子类别
            if (data != null) {
                categoryBean = (RecommendCircleBean) data.getSerializableExtra("data");
                if (categoryBean != null) {
                    tvCategory.setText(categoryBean.name);
                    tvTag.setText("");
                    tagIds = "";
                }
            }
            checkPostButtonStatus();
        } else if (requestCode == REQUEST_SET_TAG) {// 设置标签
            List<CardTagModel> tagList = (List<CardTagModel>) data.getSerializableExtra("data");
            if (tagList != null) {
                StringBuilder nameBuilder = new StringBuilder();
                StringBuilder tagBuilder = new StringBuilder();
                for (CardTagModel model : tagList) {
                    nameBuilder.append(model.name).append(" ");
                    tagBuilder.append(model.id).append(",");
                }
                tvTag.setText(nameBuilder.substring(0, nameBuilder.length() - 1));
                tagIds = tagBuilder.substring(0, tagBuilder.length() - 1);
            }
        }
    }

    @OnClick({R2.id.view_chat_card_location, R2.id.view_category, R2.id.view_chat_card_tag, R2.id.tv_chat_post,
            R2.id.tv_chat_save})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.view_category) {
//            Intent intent = new Intent(this, PostCardCateActivity.class);
//            startActivityForResult(intent, REQUEST_SET_CATEGORY);
            SelectCardCategoryPopupWindow categoryPopupWindow = new SelectCardCategoryPopupWindow(this);
            categoryPopupWindow.setPopupGravity(Gravity.TOP);
            categoryPopupWindow.setOnItemSelectListener(model -> {
                categoryPopupWindow.dismiss();
                categoryBean = model;
                tvCategory.setText(categoryBean.name);
                tvTag.setText("");
                tagIds = "";
                checkPostButtonStatus();
            });
            categoryPopupWindow.showPopupWindow();
        } else if (id == R.id.view_chat_card_tag) {
            if (categoryBean == null) {
                toastTip("请先选择圈子类别");
                return;
            }

            PostCardTagActivity.start(this, categoryBean.id, tagIds, REQUEST_SET_TAG);
        } else if (id == R.id.tv_chat_post) {
            if (adapter.getData().size() < 1) {
                toastTip("请选择要上传的内容");
                return;
            }
            if (categoryBean == null) {
                toastTip("请选择卡片的类别");
                return;
            }
            if (TextUtils.isEmpty(tagIds)) {
                toastTip("请选择标签");
                return;
            }
            if (cardId>0){ //第二次发布要先存草稿 再调发布接口
                isSecondPost = true;
                p.postCard(cardId,String.valueOf(categoryBean.id), etContent.getText().toString(), tagIds, locationModel, adapter.getData(), true);
            }else {
                p.postCard(cardId,String.valueOf(categoryBean.id), etContent.getText().toString(), tagIds, locationModel, adapter.getData(), false);
            }
        } else if (id == R.id.tv_chat_save) {
            if (adapter.getData().size() < 1) {
                toastTip("请选择要上传的内容");
                return;
            }
            if (categoryBean == null) {
                toastTip("请选择卡片的类别");
                return;
            }
            if (TextUtils.isEmpty(tagIds)) {
                toastTip("请选择标签");
                return;
            }
            p.postCard(cardId,String.valueOf(categoryBean.id), etContent.getText().toString(), tagIds, locationModel, adapter.getData(), true);
//            List<PostCardItem> data = adapter.getData();
//            if(data.size()==0&&TextUtils.isEmpty(etContent.getText().toString().trim())){
//                toastTip("请先填写卡片内容");
//                return;
//            }
//            p.saveToCache(data, categoryBean, etContent.getText().toString(),
//                    tvTag.getText().toString(), tagIds);
//            toastTip("保存成功!");
        }
    }

    public void openImageSelector(int requestCode, int maxCount, boolean CanChooseVideo) {
        Matisse.from(this)
                .choose(CanChooseVideo ? MimeType.ofAll() : MimeType.ofImage(),
                        true)
                .maxSelectablePerMediaType(maxCount, 1)
                .showSingleMediaType(true)
                .countable(true)
                .capture(false)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new MyGlideEngine())
                .forResult(requestCode);
    }

    // 获取图片信息
    private BitmapFactory.Options getPictureOption(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options;
    }

    // 判断发布按钮是否能够点击
    private void checkPostButtonStatus() {
        boolean buttonCanPost = true;
        if (categoryBean == null) {
            buttonCanPost = false;
        }
        if (adapter.getData().size() <= 0 && adapter.getPost() == null) {
            buttonCanPost = false;
        }
        tvPost.setEnabled(buttonCanPost);
    }

    @SuppressLint("CheckResult")
    private void requestLocationPermission() {
        new RxPermissions(this).request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .compose(bindToLifecycle()).subscribe(aBoolean -> {
            if (aBoolean) {
                startLocation();
            } else {
                toastTip("定位权限未授权");
                switchLocation.toggle();
            }
        });
    }

    private void startLocation() {
        MapLocationUtil.getInstance().startLocationForOnce(new MapLocationUtil.LocationListener() {
            @Override
            public void onLocationSuccess(AMapLocation location) {
                locationModel = location;
                tvLocation.setText(location.getCity());
            }

            @Override
            public void onLocationFailure(String msg) {
                if (!TextUtils.isEmpty(msg)) toastTip(msg);
            }
        });
    }

    private void initDataFromCache() {
        PostCardDraftModel draft = p.getDraftFromCache();
        if (draft != null) {
            List<PostCardItem> items = draft.items;
            if (items != null && items.size() > 0) {
                boolean isVideo = false;
                for (PostCardItem item : items) {
                    if (item.isVideo) {
                        isVideo = true;
                        break;
                    }
                }
                adapter.setPost(items.get(0));
                adapter.setData(items, isVideo);
            }
            categoryBean = draft.cardCategory;
            if (categoryBean != null) {
                tvCategory.setText(categoryBean.name);
            }
            tvTag.setText(draft.tagName);
            tagIds = draft.tagIds;
            etContent.setText(draft.content);
            tvContentNum.setText(String.format("%d/70", draft.content == null ? 0 : draft.content.length()));
            checkPostButtonStatus();
        }
    }

    @Override
    public void postCardSuccess(boolean isDraft) {
        if (!isDraft){
            toastTip("发布成功!");
            RxBus.get().post(RefreshEvent.class);
        }else if (isDraft&&isSecondPost){
            p.doPost(cardId);
        }else {
            toastTip("保存成功!");
        }
        setResult(RESULT_OK);
        finish();
    }

    private LoadingDialog mLoadingDialog;

    @Override
    public void getCardDetail(CardDetailBean bean) {
        List<PostCardItem> list = new ArrayList<>();
        for (int i = 0;i<bean.getCover().size();i++){
            CardDetailBean.CoverBean coverBean = bean.getCover().get(i);
            if (bean.getCover().get(i).getType()==1&&bean.getType()==1) {//图片 过滤封面 拿资源
                continue;
            }else if (bean.getCover().get(i).getType()==0&&bean.getType() ==2){//视频 拿封面 过滤资源
                continue;
            }

            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(this);
            }
            mLoadingDialog.show();
            mLoadingDialog.setProgressPercent("");
            ImageLoader.saveImage(PostCardActivity.this, coverBean.getImageUrl(), coverBean.getWidth(), coverBean.getHigh(), new ImageLoader.SaveImageSuccessListener() {
                @Override
                public void success(String locUrl) {
                    if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                        mLoadingDialog.setProgressPercent("");
                        mLoadingDialog.dismiss();
                    }
                    PostCardItem postCardItem = new PostCardItem(locUrl,coverBean.getWidth(),coverBean.getHigh(),bean.getType()==2);
                    list.add(postCardItem);
                    adapter.addData(postCardItem, bean.getType() == 2);
                    if (bean.getType() == 2){
                        adapter.setPost(postCardItem);
                    }
                    checkPostButtonStatus();
                }

                @Override
                public void failed() {
                    if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                        mLoadingDialog.setProgressPercent("");
                        mLoadingDialog.dismiss();
                    }
                }
            });
        }
//        adapter.setData(list,bean.getType()==2);
        tvCategory.setText(bean.getCircleName());
        categoryBean = new RecommendCircleBean();
        categoryBean.id = bean.getCircleId();
        etContent.setText(bean.getExcerpt());
        tagIds = bean.getTagIds();
        if (!TextUtils.isEmpty(bean.getPosition())){
            startLocation();
        }
        p.getTagList(bean.getCircleId());
        checkPostButtonStatus();
    }

    @Override
    public void setAllTag(List<CardTagModel> cardTagModelList) {
        String [] tagId = tagIds.split(",");
        StringBuilder builder = new StringBuilder();
        for (int i = 0;i<cardTagModelList.size();i++){
            for (int k = 0;k<tagId.length;k++){
                if (String.valueOf(cardTagModelList.get(i).id).equals(tagId[k])){
                    builder.append(cardTagModelList.get(i).name);
                    builder.append(",");
                }
            }
        }
        builder.deleteCharAt(builder.length()-1);
        tvTag.setText(builder.toString());
    }
}