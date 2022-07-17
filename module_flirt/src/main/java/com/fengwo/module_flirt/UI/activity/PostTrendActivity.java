/*
 * 发布动态页面
 *
 * */

package com.fengwo.module_flirt.UI.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
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
import com.fengwo.module_chat.mvp.model.bean.CardDetailBean;
import com.fengwo.module_chat.mvp.model.bean.PostCardDraftModel;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_chat.mvp.ui.activity.publish.PostCardTagActivity;
import com.fengwo.module_chat.mvp.ui.dialog.SelectCardCategoryPopupWindow;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.MapLocationUtil;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.bean.CardTagModel;
import com.fengwo.module_comment.bean.PostCardItem;
import com.fengwo.module_comment.event.RefreshCardList;
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
import com.fengwo.module_comment.widget.AppTitleBar;
import com.fengwo.module_comment.widget.LoadingDialog;
import com.fengwo.module_flirt.Interfaces.IPostTrendView;
import com.fengwo.module_flirt.P.PostTrendPresenter;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.adapter.TrendPostAdapter;
import com.fengwo.module_flirt.dialog.PostTrendTipPop;
import com.google.gson.Gson;
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
import razerdp.util.KeyboardUtils;

@Route(path = ArouterApi.CHAT_POST_TREND)
public class PostTrendActivity extends BaseMvpActivity<IPostTrendView, PostTrendPresenter> implements IPostTrendView {

    private final int REQUEST_GET_GALLERY = 0;
    private final int REQUEST_CHANGE_POST_IMAGE = 1;
    private final int REQUEST_CHANGE_POST_VIDEO = 2;
    private final int REQUEST_SET_CATEGORY = 3;
    private final int REQUEST_SET_TAG = 4;

    @BindView(R2.id.title)
    AppTitleBar titleBar;
    @BindView(R2.id.rv_chat)
    RecyclerView recyclerView;
    //@BindView(R2.id.tv_chat_category_value)
    //TextView tvCategory;
    //@BindView(R2.id.view_category)
    //View viewCategory;
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

    private TrendPostAdapter adapter;
    private RecommendCircleBean categoryBean;
    private AMapLocation locationModel;
    private String tagIds;
    private ChooseMediaTypePopwindow chooseMediaTypePopwindow;

    private int cardId = -1;//卡片id，编辑草稿的时候获取详情
    private boolean isSecondPost = false;//是否草稿发布

    @Override
    public PostTrendPresenter initPresenter() {
        return new PostTrendPresenter();
    }

    private static final String TAG = "PostCardActivity";

    @Override
    protected void initView() {
        cardId = getIntent().getIntExtra("id", -1);
        //viewCategory.setEnabled(false);
        titleBar.setMoreClickListener(v -> {
            PostTrendTipPop pop = new PostTrendTipPop(this);
            pop.showPopupWindow();
        });
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvContentNum.setText(String.format("%d/200", s == null ? 0 : s.length()));
                checkPostButtonStatus();
            }
        });
        rootView.setMinimumHeight(ScreenUtils.getScreenHeight(this) - DensityUtils.dp2px(this, 72));

        RxPermissions rxPermissions = new RxPermissions(this);
        // add model
        adapter = new TrendPostAdapter(this);
        adapter.setListener(new TrendPostAdapter.OnItemClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void itemAdd(boolean isAdd) {
                KeyboardUtils.close(PostTrendActivity.this);
                rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {
                                boolean empty = adapter.getData().isEmpty();
                                chooseMediaTypePopwindow.mLlVideoColumns.setVisibility(empty ? View.VISIBLE : View.GONE);
                                chooseMediaTypePopwindow.showPopupWindow();
                            } else {
                                toastTip("您关闭了权限，请去设置页面开启");
                            }
                        });
            }

            @SuppressLint("CheckResult")
            @Override
            public void itemChangePost(boolean isVideo) {
                rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {
                                MImagePicker.openImagePicker(PostTrendActivity.this, MImagePicker.TYPE_IMG, isVideo ? REQUEST_CHANGE_POST_VIDEO : REQUEST_CHANGE_POST_IMAGE);
                            } else {
                                toastTip("您关闭了权限，请去设置页面开启");
                            }
                        });
                L.e(TAG, "itemChangePost: " + isVideo);
            }

            @Override
            public void itemDel(int position) {
                checkPostButtonStatus();
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, TrendPostAdapter.MAX_COUNT_IMAGE));
        recyclerView.setAdapter(adapter);
        tvPost.setEnabled(false);
        switchLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed() && isChecked) {
                if (isLocServiceEnable(this)) {
                    requestLocationPermission();
                } else {
                    switchLocation.setChecked(false);
                    Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(locationIntent, 10);
                }

            } else tvLocation.setText("");
        });

        initDataFromCache();
        chooseMediaTypePopwindow = new ChooseMediaTypePopwindow(this);
        chooseMediaTypePopwindow.setOnTakePopwindowClickListener(new ChooseMediaTypePopwindow.OnChooseClickListener() {
            @Override
            public void onImgClick() {
                MImagePicker.openImagePicker(PostTrendActivity.this, MImagePicker.TYPE_IMG, REQUEST_GET_GALLERY);
            }

            @Override
            public void onVideoClick() {
                MImagePicker.openImagePicker(PostTrendActivity.this, MImagePicker.TYPE_VEDIO, REQUEST_GET_GALLERY);
            }
        });

        if (cardId > 0) {
            titleBar.setTitle("编辑动态");
            p.getCardDetail(cardId);
        }

        initScrollHandler();
    }

    /**
     * 解决etContent与外层ScrollView滑动冲突
     */
    private void initScrollHandler() {
        etContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //canScrollVertically()方法为判断指定方向上是否可以滚动,参数为正数或负数,负数检查向上是否可以滚动,正数为检查向下是否可以滚动
                if (etContent.canScrollVertically(1) || etContent.canScrollVertically(-1)) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);//requestDisallowInterceptTouchEvent();要求父类布局不在拦截触摸事件
                    if (event.getAction() == MotionEvent.ACTION_UP) { //判断是否松开
                        v.getParent().requestDisallowInterceptTouchEvent(false); //requestDisallowInterceptTouchEvent();让父类布局继续拦截触摸事件
                    }
                }
                return false;
            }
        });

    }

    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_post_trend;
    }

    @SuppressLint("CheckResult")
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
                        emitter.onNext(post);
                    }, BackpressureStrategy.ERROR).compose(bindToLifecycle()).compose(io_main()).subscribe(postCardItem -> {
                        // 设置视频第一张图片
                        postCardItem.videoPath = url;
                        adapter.addData(postCardItem, true);
                        checkPostButtonStatus();
                    });
                } else {
                    // 获取图片宽高
                    BitmapFactory.Options options = getPictureOption(url);
                    int width = options.outWidth;
                    int height = options.outHeight;
                    // 构造数据
                    PostCardItem item = new PostCardItem(url, width, height, false);
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
                    //tvCategory.setText(categoryBean.name);
                    tvTag.setText("");
                    tagIds = "";
                }
            }
            //checkPostButtonStatus();
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

    @OnClick({R2.id.view_chat_card_location, /*R2.id.view_category,*/ R2.id.view_chat_card_tag, R2.id.tv_chat_post,
            R2.id.tv_chat_save})
    public void onViewClick(View view) {
        String cateId;
        if (categoryBean != null) {
            cateId = String.valueOf(categoryBean.id);
        } else {
            cateId = "";
        }
        int id = view.getId();
        //关联卡片属性
        if (id == R.id.view_category) {
//            Intent intent = new Intent(this, PostCardCateActivity.class);
//            startActivityForResult(intent, REQUEST_SET_CATEGORY);
            SelectCardCategoryPopupWindow categoryPopupWindow = new SelectCardCategoryPopupWindow(this);
            categoryPopupWindow.setPopupGravity(Gravity.TOP);
            categoryPopupWindow.setOutSideDismiss(true);
            categoryPopupWindow.setOnItemSelectListener(model -> {
                categoryPopupWindow.dismiss();
                categoryBean = model;
                //tvCategory.setText(categoryBean.name);
                tvTag.setText("");
                tagIds = "";
                //checkPostButtonStatus();
            });
            categoryPopupWindow.showPopupWindow();
        } else if (id == R.id.view_chat_card_tag) {
            if (categoryBean == null) {
                toastTip("请先选择圈子类别");
                return;
            }

            PostCardTagActivity.start(this, categoryBean.id, tagIds, REQUEST_SET_TAG);
        }
        //触发事件
        //发布动态到服务器
        else if (id == R.id.tv_chat_post) {
            if (TextUtils.isEmpty(etContent.getText())) {
                toastTip("动态内容不能为空");
                return;
            }
            if (isFastClick()) return;
            if (cardId > 0) { //第二次发布要先存草稿 再调发布接口
                isSecondPost = true;
                //p.postCard(cardId, viewCategory.isEnabled() ? cateId : "", etContent.getText().toString(), tagIds, locationModel, adapter.getData(), true);
                p.postCard(cardId, "", etContent.getText().toString(), tagIds, locationModel, adapter.getData(), true);
            } else {
                //p.postCard(cardId, viewCategory.isEnabled() ? cateId : "", etContent.getText().toString(), tagIds, locationModel, adapter.getData(), false);
                p.postCard(cardId, "", etContent.getText().toString(), tagIds, locationModel, adapter.getData(), false);
            }
        }
        //触发事件
        //存为草稿
        else if (id == R.id.tv_chat_save) {
            if (TextUtils.isEmpty(etContent.getText())) {
                toastTip("动态内容不能为空");
                return;
            }
            //p.postCard(cardId, viewCategory.isEnabled() ? cateId : "", etContent.getText().toString(), tagIds, locationModel, adapter.getData(), true);
            p.postCard(cardId, "", etContent.getText().toString(), tagIds, locationModel, adapter.getData(), true);
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
        if (TextUtils.isEmpty(etContent.getText())) {
            buttonCanPost = false;
        }
        tvPost.setEnabled(buttonCanPost);
        boolean isCardState = !adapter.getData().isEmpty()
                && !TextUtils.isEmpty(etContent.getText());

        //viewCategory.setEnabled(isCardState);

        //卡片能否选择
        String cartValue = !isCardState ? getString(R.string.char_card_value_1)
                : getString(R.string.char_card_value_2);
        //tvCategory.setText(cartValue);
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
            /*categoryBean = draft.cardCategory;
            if (categoryBean != null) {
                tvCategory.setText(categoryBean.name);
            }*/
            tvTag.setText(draft.tagName);
            tagIds = draft.tagIds;
            etContent.setText(draft.content);
            tvContentNum.setText(String.format("%d/200", draft.content == null ? 0 : draft.content.length()));
            //checkPostButtonStatus();
        }
    }

    /**
     * 发布成功后接口的回调
     *
     * @param isDraft 是否存草稿 0 直接发布 1 存草稿 默认 0
     */
    @Override
    public void postCardSuccess(boolean isDraft, CardDetailBean bean) {
        if (!isDraft) {
            toastTip("发布成功!");
            Intent intent = new Intent();
            if (null != bean) {
                intent.putExtra("CardDetail", new Gson().toJson(bean));
            }
            setResult(RESULT_OK, intent);
        } else if (isDraft && isSecondPost) {
            p.doPost(cardId);
        } else {
            toastTip("保存成功!");
        }
        RxBus.get().post(new RefreshEvent());
        RxBus.get().post(new RefreshCardList());
        finish();
    }

    private LoadingDialog mLoadingDialog;

    @Override
    public void getCardDetail(CardDetailBean bean) {
        List<PostCardItem> list = new ArrayList<>();
        for (int i = 0; i < bean.getCover().size(); i++) {
            String imageUrl;
            CardDetailBean.CoverBean coverBean = bean.getCover().get(i);
            if (bean.getCover().get(i).getTypeNew() == 2) {
                imageUrl = bean.getCover().get(i).getVideoImgUrl();
            } else {
                if (bean.getCover().get(i).getType() == 1 && bean.getType() == 1) {//图片 过滤封面 拿资源
                    continue;
                } else if (bean.getCover().get(i).getType() == 0 && bean.getType() == 2) {//视频 拿封面 过滤资源
                    continue;
                }
                imageUrl = coverBean.getImageUrl();
            }

            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(this);
            }
            mLoadingDialog.show();
            mLoadingDialog.setProgressPercent("");
            ImageLoader.saveImage(PostTrendActivity.this, imageUrl, coverBean.getWidth(), coverBean.getHigh(), new ImageLoader.SaveImageSuccessListener() {
                @Override
                public void success(String locUrl) {
                    if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                        mLoadingDialog.setProgressPercent("");
                        mLoadingDialog.dismiss();
                    }
                    PostCardItem postCardItem = new PostCardItem(locUrl, coverBean.getWidth(), coverBean.getHigh(), coverBean.getTypeNew() == 2);
                    if (coverBean.getTypeNew() == 2) {
                        postCardItem.videoPath = coverBean.getImageUrl();
                    }
                    list.add(postCardItem);
                    adapter.addData(postCardItem, bean.getType() == 2);
                    if (bean.getType() == 2) {
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
        //tvCategory.setText(bean.getCircleName());
        categoryBean = new RecommendCircleBean();
        categoryBean.id = bean.getCircleId();
        etContent.setText(bean.getExcerpt());
        tagIds = bean.getTagIds();
        if (!TextUtils.isEmpty(bean.getPosition())) {
            startLocation();
        }
        p.getTagList(bean.getCircleId());
        //checkPostButtonStatus();
    }

    @Override
    public void setAllTag(List<CardTagModel> cardTagModelList) {
        if (TextUtils.isEmpty(tagIds)) return;
        String[] tagId = tagIds.split(",");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cardTagModelList.size(); i++) {
            for (int k = 0; k < tagId.length; k++) {
                if (String.valueOf(cardTagModelList.get(i).id).equals(tagId[k])) {
                    builder.append(cardTagModelList.get(i).name);
                    builder.append(",");
                }
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        tvTag.setText(builder.toString());
    }
}