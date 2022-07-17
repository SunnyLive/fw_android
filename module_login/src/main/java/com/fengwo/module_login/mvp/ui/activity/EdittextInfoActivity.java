package com.fengwo.module_login.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.donkingliang.labels.LabelsView;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.FileUtils;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.LocationUtils;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.utils.UCropUtils;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_comment.widget.TakePicPopwindow;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.eventbus.UpdatePhotoEvent;
import com.fengwo.module_login.eventbus.UpdateUserinfo;
import com.fengwo.module_login.mvp.dto.TagsDto;
import com.fengwo.module_login.mvp.ui.adapter.PhotoWallAdapter;
import com.fengwo.module_login.mvp.ui.pop.HeightWeightWindow;
import com.fengwo.module_login.mvp.ui.pop.IdealTypePopWindow;
import com.fengwo.module_login.utils.UserManager;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class EdittextInfoActivity extends BaseMvpActivity {
    @Autowired
    UserProviderService userProviderService;
    private final static int REQUEST_CODE = 10001;
    private final static int REQUEST_NAME_CODE = 10002;
    private final static int REQUEST_DES_CODE = 10003;
    private final static int REQUEST_SEX_CODE = 10004;
    private final static int REQUEST_ADDRESS_CODE = 10005;
    private final static int REQUEST_BIRTHDAY_CODE = 10006;
    private final static int REQUEST_TAKE_PHOTO = 10007;
    public final static int REQUEST_DELETE_PHOTO = 10008;
    @BindView(R2.id.tv_username)
    TextView tvUsername;
    @BindView(R2.id.iv_sex)
    ImageView ivSex;
    @BindView(R2.id.tv_id)
    TextView tvId;
    @BindView(R2.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R2.id.tv_location)
    TextView tvLocation;
    @BindView(R2.id.tv_des)
    TextView tvDes;
    @BindView(R2.id.tv_sex)
    TextView tvSex;
    @BindView(R2.id.tv_hw)
    TextView mTvHW;
    @BindView(R2.id.tv_ideal_type)
    TextView mTvIdealType;  //理想型
    @BindView(R2.id.rv_photo_wall)
    RecyclerView mRvPhotoWall;   //照片墙
    @BindView(R2.id.tv_add_sign)
    TextView mRvAddSign;
    private TakePicPopwindow takePicPopwindow;
    private PhotoWallAdapter mAdapter;
    private List<UserInfo.UserPhotosList> mUserPhotos;
    private RxPermissions rxPermissions;
    /*@BindView(R2.id.iv_header)
    ImageView ivHeader;*/
    @BindView(R2.id.lv_label)
    LabelsView mLvLabel;
    TimePickerView timePickerView;
    private String biethdayStr;
    UserInfo userInfo;
    private String astro;
    private Uri photoUri;

    private int mPhotoType = 1;   //上传的是照片墙还是头像   1：头像  2：照片墙
    private String mIdealType;   //理想型

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    @Override
    protected void initView() {
        new ToolBarBuilder().setTitle("编辑资料").setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black).showBack(true).build();
        rxPermissions = new RxPermissions(this);
        mAdapter = new PhotoWallAdapter(this);
        mAdapter.setListener(new PhotoWallAdapter.OnItemClickListener() {
            @Override
            public void itemAdd() {
                mPhotoType = 2;
                showTakePhotoPoo();
            }

            @Override
            public void itemClick(List<UserInfo.UserPhotosList> data, int position) {
                mPhotoType = 2;
                BigPhotoWallActivity.start(EdittextInfoActivity.this, data.get(position).id, data.get(position).photoUrl, position
                        , data.get(position).photoStatus);
            }


        });
        mRvPhotoWall.setLayoutManager(new GridLayoutManager(this, 3));
        mRvPhotoWall.setAdapter(mAdapter);
        setUserInfo();

        netManager.add(RxBus.get().toObservable(UpdateUserinfo.class).compose(bindToLifecycle())
                .subscribe((UpdateUserinfo updateUserinfo) -> {
                    setUserInfo();
                }));

        netManager.add(RxBus.get().toObservable(UpdatePhotoEvent.class).compose(bindToLifecycle())
                .subscribe((UpdatePhotoEvent update) -> {
                    mUserPhotos.get(update.position).photoUrl = update.url;
                    mUserPhotos.get(update.position).photoStatus = update.status;
                    mAdapter.setNewData(mUserPhotos);
                }));


        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                int dragFrlg = 0;
                //如果是add  就不拖拽
                int position = viewHolder.getAdapterPosition();
                if (TextUtils.equals(mAdapter.getData().get(position).photoUrl, "ADD")) {
                    return makeMovementFlags(0, 0);
                }

                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    dragFrlg = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    dragFrlg = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                }
                return makeMovementFlags(dragFrlg, 0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                if (TextUtils.equals(mAdapter.getData().get(fromPosition).photoUrl, "ADD")) {
                    return false;
                }
                if (TextUtils.equals(mAdapter.getData().get(toPosition).photoUrl, "ADD")) {
                    return false;
                }
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(userInfo.userPhotos, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        if (userInfo.userPhotos.size() > i + 1) {
                            Collections.swap(userInfo.userPhotos, i, i - 1);
                        }
                    }
                }
                mAdapter.notifyItemMoved(fromPosition, toPosition);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < userInfo.userPhotos.size(); i++) {
                    if (!TextUtils.equals("ADD", userInfo.userPhotos.get(i).photoUrl)) {
                        sb.append(userInfo.userPhotos.get(i).id);
                        sb.append(",");
                    }
                }
                if (!TextUtils.isEmpty(sb.toString())) {
                    useUpdatePhotosOrder(sb.toString().substring(0, sb.toString().length() - 1));
                }
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) { }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            /**
             * 长按选中Item的时候开始调用
             * 长按高亮
             * @param viewHolder
             * @param actionState
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    //获取系统震动服务//震动70毫秒
                    Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                    vib.vibrate(70);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            /**
             * 手指松开的时候还原高亮
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                mAdapter.notifyDataSetChanged(); //完成拖动后刷新适配器，这样拖动后删除就不会错乱
            }
        });
        helper.attachToRecyclerView(mRvPhotoWall);


    }

    private void showBirthdaySelector() {
        timePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                astro = TimeUtils.getAstro(month, day);
                String str = TimeUtils.dateToInstant(date);
                userInfo.birthday = str;
                biethdayStr = (TimeUtils.dateToYYYYMM(date));
                tvBirthday.setText(biethdayStr + " " + astro);
                changeUserInfo();
            }
        })
                .setType(new boolean[]{true, true, false, false, false, false})
                .isDialog(false) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .build();
        timePickerView.show();
    }

    private void setUserInfo() {
        userInfo = userProviderService.getUserInfo();
        astro = userInfo.constellation;
        tvUsername.setText(userInfo.nickname);


        if (userInfo != null && userInfo.userPhotos != null) {
            mUserPhotos = userInfo.userPhotos;
            if (mUserPhotos.size() < 6) {
                UserInfo.UserPhotosList userPhotosList = new UserInfo.UserPhotosList("ADD");
                mUserPhotos.add(userPhotosList);
            }
            mAdapter.setNewData(mUserPhotos);
        }
        if (TextUtils.isEmpty(userInfo.birthday)) {
            tvBirthday.setText("未设置");
        } else
            tvBirthday.setText(TimeUtils.dealInstanToYYYYMMDD(userInfo.birthday) + "  " + userInfo.constellation);
        tvId.setText(userInfo.fwId);
        tvLocation.setText(userInfo.location);
        tvDes.setText(userInfo.signature);
        if (TextUtils.isEmpty(userInfo.signature) || TextUtils.equals(userInfo.signature, "这个人很懒，什么都没留下~")) {
            mRvAddSign.setVisibility(View.VISIBLE);
            tvDes.setVisibility(View.GONE);
        } else {
            mRvAddSign.setVisibility(View.GONE);
            tvDes.setVisibility(View.VISIBLE);
        }
        if (userInfo.sex == 1) {
            ivSex.setImageResource(R.drawable.ic_boy_sex);
            tvSex.setText("男");
        } else if (userInfo.sex == 2) {
            ivSex.setImageResource(R.drawable.ic_girl_sex);
            tvSex.setText("女");
        } else {
            ivSex.setVisibility(View.GONE);
            tvSex.setText("保密");
        }
        mTvHW.setText(TextUtils.isEmpty(userInfo.height) ? "" : userInfo.height + "cm" + (TextUtils.isEmpty(userInfo.weight) ? "  " : "/" + userInfo.weight + "kg  "));
        if (!TextUtils.isEmpty(userInfo.idealTypeTag)) {
            mTvIdealType.setText("#" + userInfo.idealTypeTag.replace(",", " #"));  //理想型
        } else {
            mTvIdealType.setText(getResources().getString(R.string.ideal_type_tip));
        }
        mIdealType = userInfo.idealTypeTag;
        List<String> liveLabel = new ArrayList<>();
        if (!TextUtils.isEmpty(userInfo.getLiveLabel())) {
            String[] labels = userInfo.getLiveLabel().split(",");
            liveLabel.addAll(Arrays.asList(labels));
        }
        mLvLabel.setLabels(liveLabel);
        mLvLabel.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                if (TextUtils.equals(label.getText().toString(), "添加我的标签")) {


                }
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_editinfo;
    }


    @SuppressLint("CheckResult")
    @OnClick({R2.id.btn_takepic, R2.id.btn_to_setname, R2.id.btn_to_setsex, R2.id.btn_to_setbirthday, R2.id.btn_to_setlocation, R2.id.btn_to_setdes,/* R2.id.iv_header, */R2.id.ll_id
            , R2.id.ll_ideal_type, R2.id.ll_hw})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_takepic) {
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            takePicPopwindow.showPopupWindow();
                        } else {
                            toastTip("您关闭了权限，请去设置页面开启");
                        }
                    });
        } else if (id == R.id.iv_header) {
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            mPhotoType = 1;
                            showTakePhotoPoo();
                        } else {
                            toastTip("您关闭了权限，请去设置页面开启");
                        }
                    });
        } else if (id == R.id.btn_to_setname) {
            EditInfoActivity.startForName(this, REQUEST_NAME_CODE);
        } else if (id == R.id.btn_to_setsex) {
            EditInfoActivity.startForSex(this, REQUEST_SEX_CODE);
        } else if (id == R.id.btn_to_setbirthday) {
            if (true) {
                EditInfoActivity.startForBirthday(this, REQUEST_BIRTHDAY_CODE);
                return;
            }
            showBirthdaySelector();
        } else if (id == R.id.btn_to_setlocation) {
            if (true) {
                EditInfoActivity.startForAddress(this, REQUEST_ADDRESS_CODE);
                return;
            }
            LocationUtils.showLocationDialog(this, true, location -> {
                userInfo.location = location;
                tvLocation.setText(location);
                changeUserInfo();
            });
        } else if (id == R.id.btn_to_setdes) {
            EditInfoActivity.startForDes(this, REQUEST_DES_CODE);
        } else if (id == R.id.ll_id) {
            startActivity(MyCodeActivity.class);
        } else if (id == R.id.ll_hw) {   //身高体重

            HeightWeightWindow heightWeightWindow = new HeightWeightWindow(this);
            heightWeightWindow.setOnClick(new HeightWeightWindow.OnClickListener() {
                @Override
                public void onChoose(int height, int weight) {
                    userInfo.weight = weight + "";
                    userInfo.height = height + "";
                    changeUserInfo();

                }
            });
            heightWeightWindow.showPopupWindow();

        } else if (id == R.id.ll_ideal_type) {   //理想型
            if (userInfo == null)
                return;
            IdealTypePopWindow idealTypePopWindow = new IdealTypePopWindow(this, userInfo.idealTypeTag);
            idealTypePopWindow.setOnClick(new IdealTypePopWindow.OnClickListener() {
                @Override
                public void onChoose(HashMap<String, TagsDto> map) {
                    if (map != null) {
                        StringBuilder stringBuffer = new StringBuilder();
                        int i = 0;
                        for (String key : map.keySet()) {
                            if (i != 0) stringBuffer.append(",");
                            stringBuffer.append(key);
                            i++;
                        }
                        userInfo.idealTypeTag = stringBuffer.toString();
                        mTvIdealType.setText(stringBuffer.toString());
                        mIdealType = stringBuffer.toString();
                        if (!TextUtils.isEmpty(stringBuffer.toString())) {
                            mTvIdealType.setText("#" + stringBuffer.toString().replace(",", " #"));  //理想型
                        } else {
                            mTvIdealType.setText(getResources().getString(R.string.ideal_type_tip));
                        }
                        changeUserInfo();
                    }

                }
            });
            idealTypePopWindow.showPopupWindow();
        }
    }


    /**
     * 显示弹窗window
     */
    private void showTakePhotoPoo() {
        if (null == takePicPopwindow) {
            takePicPopwindow = new TakePicPopwindow(EdittextInfoActivity.this, 0);
        }
        takePicPopwindow.setOnTakePopwindowClickListener(new TakePicPopwindow.OnTakePopwindowClickListener() {
            @Override
            public void onTakeClick() {
                takePhoto();
                takePicPopwindow.dismiss();
            }

            @Override
            public void onChooseClick() {
                MImagePicker.openImagePicker(EdittextInfoActivity.this, MImagePicker.TYPE_IMG, REQUEST_CODE);
                takePicPopwindow.dismiss();
            }

            @Override
            public void onDeleteClick() {

            }
        });
        takePicPopwindow.showPopupWindow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String url = MImagePicker.getImagePath(this, data);
            if (TextUtils.isEmpty(url)) return;
            File file = new File(url);
            UCropUtils.startCrop(this, Uri.fromFile(file));
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {//拍照处理
            if (photoUri != null) {
                UCropUtils.startCrop(this, photoUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            showLoadingDialog();
            Uri outputUri = UCrop.getOutput(data);
            File outputFile = new File(outputUri.getPath());
            UploadHelper.getInstance(getApplicationContext()).doUpload(UploadHelper.TYPE_IMAGE,
                    outputFile, new UploadHelper.OnUploadListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onLoading(long cur, long total) {
                        }

                        @Override
                        public void onSuccess(String url) {
                            hideLoadingDialog();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mPhotoType == 1) {
                                        userInfo.headImg = url;
                                        changeUserInfo();
                                    } else
                                        addUserPhotos(url);
                                }
                            });
                        }

                        @Override
                        public void onError() {
                            hideLoadingDialog();
                            toastTip("上传失败");
                        }
                    });

        } else if (requestCode == REQUEST_DELETE_PHOTO && resultCode == RESULT_OK) {
            if (mUserPhotos != null && mPhotoType == 2) {
                int position = data.getIntExtra("position", 0);
                mUserPhotos.remove(position);
                boolean hasAdd = false;
                for (int i = 0; i < mUserPhotos.size(); i++) {
                    if (TextUtils.equals("ADD", mUserPhotos.get(i).photoUrl)) {
                        hasAdd = true;
                    }
                }
                if (!hasAdd) {
                    UserInfo.UserPhotosList item = new UserInfo.UserPhotosList("ADD");
                    mUserPhotos.add(item);
                }
                mAdapter.setNewData(mUserPhotos);
            }

        } else if (resultCode == RESULT_OK) {
            setUserInfo();
        }
    }

    private void handleResultUri(Uri uri) {


    }

    public String saveImage(String name, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath());
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 跟新用户信息
     */
    private void changeUserInfo() {
        new RetrofitUtils().createApi(LoginApiService.class)
                .updateUserinfo(new HttpUtils.ParamsBuilder()
                        .put("sex", userInfo.sex + "")
                        .put("location", userInfo.location)
                        .put("headImg", userInfo.headImg)
                        .put("birthday", userInfo.birthday)
                        .put("idealTypeTag", userInfo.idealTypeTag)
                        .put("height", userInfo.height)
                        .put("weight", userInfo.weight)
                        .build()
                )
                .compose(io_main())
                .compose(bindToLifecycle())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            userInfo.constellation = astro;
                            UserManager.getInstance().setUserInfo(userInfo);
                            RxBus.get().post(new UpdateUserinfo());
                        } else if (data.isServiceErr()) {
                            ToastUtils.showShort(EdittextInfoActivity.this, data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                });
    }


    /**
     * 更新照片墙排序
     *
     * @param photoIds
     */
    private void useUpdatePhotosOrder(String photoIds) {
        new RetrofitUtils().createApi(LoginApiService.class)
                .useUpdatePhotosOrder(new HttpUtils.ParamsBuilder()
                        .put("photoIds", photoIds)
                        .build()
                )
                .compose(io_main())
                .compose(bindToLifecycle())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            ToastUtils.showShort(EdittextInfoActivity.this, "更新成功");
                        } else if (data.isServiceErr()) {
                            ToastUtils.showShort(EdittextInfoActivity.this, data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                });
    }


    /**
     * 添加用户照片墙
     */
    private void addUserPhotos(String url) {
        new RetrofitUtils().createApi(LoginApiService.class)
                .userAddPhotos(new HttpUtils.ParamsBuilder()
                        .put("photoUrl", url)
                        .build()
                )
                .compose(io_main())
                .compose(bindToLifecycle())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
//                            UserManager.getInstance().setUserInfo(userInfo);
//                            RxBus.get().post(new UpdateUserinfo());
                            UserInfo.UserPhotosList item = new UserInfo.UserPhotosList(url);
                            mUserPhotos.add(item);
                            for (int i = 0; i < mUserPhotos.size(); i++) {
                                if (TextUtils.equals("ADD", mUserPhotos.get(i).photoUrl)) {
                                    mUserPhotos.remove(mUserPhotos.get(i));
                                }
                            }
                            if (mUserPhotos.size() < 6) {
                                UserInfo.UserPhotosList item1 = new UserInfo.UserPhotosList("ADD");
                                mUserPhotos.add(item1);
                            }
                            mAdapter.setNewData(mUserPhotos);
                        } else if (data.isServiceErr()) {
                            ToastUtils.showShort(EdittextInfoActivity.this, data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                });
    }


    @SuppressLint("CheckResult")
    private void takePhoto() {
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    try {
                        if (granted) {
                            String take_cam_fileName = TimeUtils.getCurrentTimeInLong() + ".jpg";
                            // 步骤一：创建存储照片的文件
                            String path = FileUtils.SD_PATH;
                            File file = new File(path, take_cam_fileName);
                            if (!file.getParentFile().exists())
                                file.getParentFile().mkdirs();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                //步骤二：Android 7.0及以上获取文件 Uri
                                photoUri = FileProvider.getUriForFile(EdittextInfoActivity.this, "fengwoImg", file);
                            } else {
                                //步骤三：获取文件Uri
                                photoUri = Uri.fromFile(file);
                            }
                            //步骤四：调取系统拍照
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                        } else {

                            Toast.makeText(this, "您关闭了权限，请去设置页面开启", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                    }

                });
    }

    // 获取图片信息
    private BitmapFactory.Options getPictureOption(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options;
    }
}
