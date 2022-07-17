package com.fengwo.module_live_vedio.mvp.ui.activity.zhubo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocation;
import com.faceunity.ui.control.BeautyControlView;
import com.faceunity.ui.dialog.BaseDialogFragment;
import com.faceunity.ui.entity.BeautyParameterModel;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.MapLocationUtil;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.dialog.ExitDialog;
import com.fengwo.module_comment.iservice.GetUserInfoByIdService;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.AndPermission;
import com.fengwo.module_comment.utils.ArouteUtils;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.LocationUtils;
import com.fengwo.module_comment.utils.MImagePicker;
import com.fengwo.module_comment.utils.PermissionUtil;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.utils.UCropUtils;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.mvp.dto.ChannelShareInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.StartLivePushDto;
import com.fengwo.module_live_vedio.mvp.dto.ZhuboMenuDto;
import com.fengwo.module_live_vedio.mvp.presenter.StartLivePresenter;
import com.fengwo.module_live_vedio.mvp.ui.activity.BaseLiveingRoomActivity;
import com.fengwo.module_live_vedio.mvp.ui.iview.IStartView;
import com.fengwo.module_live_vedio.mvp.ui.pop.AnchorWishPop;
import com.fengwo.module_live_vedio.mvp.ui.pop.ChooseMenuPopwindow;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yalantis.ucrop.UCrop;



import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@Route(path = ArouterApi.START_LIVE_ACTION)
public class StartLiveActivity extends BaseCameraActivity<IStartView, StartLivePresenter> implements IStartView, UMShareListener {

    private static final int IMG_REQUESTCODE = 10001;

    @BindView(R2.id.btn_close)
    ImageView btnClose;

    @BindView(R2.id.btn_upload)
    ImageView ivUpload;
    @BindView(R2.id.et_title)
    EditText etTitle;
    @BindView(R2.id.tv_label)
    TextView tvLabel;
    @BindView(R2.id.tv_location)
    TextView tvLocation;
    @BindView(R2.id.tv_xz)
    TextView tvXz;
    @BindView(R2.id.tv_type)
    TextView tv_type;


    @BindView(R2.id.root)
    RelativeLayout rootView;

    //美颜view
    @BindView(R2.id.fl_faceunity)
    FrameLayout flFaceUnity;
    @BindView(R2.id.pusher_tx_cloud_view)
    TXCloudVideoView pushView;

    @Autowired
    UserProviderService service;
    @Autowired
    GetUserInfoByIdService getUserInfoByIdService;
    private String imgUrl;
    private ChooseMenuPopwindow popwindow;

    private List<ZhuboMenuDto> menuList;
    Disposable disposable;

    protected BeautyControlView mBeautyControlView; //美颜view
    protected ViewStub mBottomViewStub;//美颜 占位view

    private double latitude = 24.51689285906131;
    private double longitude = 118.1159969289877;

    @Override
    public StartLivePresenter initPresenter() {
        return new StartLivePresenter();
    }

    @Override
    protected void initView() {
        initPremission(null);
        p.getZhuboMenu();
        p.hostConnecLine();

        //定位当前位置
        MapLocationUtil.getInstance().startLocationForOnce(new MapLocationUtil.LocationListener() {
            @Override
            public void onLocationSuccess(AMapLocation location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                tvLocation.setText(location.getProvider() + "." + location.getCity() + "");
                tvLocation.setText(location.getProvince()+"."+location.getCity()+"");
            }

            @Override
            public void onLocationFailure(String msg) {

            }
        });

        getUserInfoByIdService.getUserInfoById(service.getUserInfo().getId() + "", new LoadingObserver<HttpResult<UserInfo>>(this) {
            @Override
            public void _onNext(HttpResult<UserInfo> data) {
                UserInfo userInfo = service.getUserInfo();
                userInfo.userPhotos = data.data.userPhotos;
                userInfo.liveLevel = data.data.liveLevel;
                service.setUsetInfo(userInfo);
                if (userInfo.userPhotos.size() > 0) {
                    for(int i=0;i<userInfo.userPhotos.size();i++){
                        if(userInfo.userPhotos.get(i).photoStatus == 1){
                            setThumb(userInfo.userPhotos.get(i).photoUrl);
                            break;
                        }else{
                            setThumb(userInfo.thumb);
                        }
                    }
                }else{
                    setThumb(userInfo.thumb);
                }
            }

            @Override
            public void _onError(String msg) {

            }
        });

    }

    /**
     * 设置直播封面
     */
    private void setThumb(String url){
        imgUrl = url;
        ImageLoader.loadImg(ivUpload, imgUrl);
        tv_type.setBackgroundResource(R.drawable.shap_gd_dialog_bg);
        tv_type.setTextColor(Color.parseColor("#FFFFFF"));
        tv_type.setText("更改封面");
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void initBeautiView() {
        BeautyParameterModel.init(this);
        mBottomViewStub = (ViewStub) findViewById(R.id.fu_base_bottom);
        mBottomViewStub.setInflatedId(R.id.fu_base_bottom);
        //美颜
        mBottomViewStub.setLayoutResource(R.layout.layout_fu_beauty);
        mBeautyControlView = (BeautyControlView) mBottomViewStub.inflate();
        mBeautyControlView.setOnFUControlListener(mFURenderer);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        meiyanPopwindow.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                setRootView(false);
//                SPUtils1.put(StartLiveActivity.this, BeautyDto.BEAUTYTAG, gson.toJson(beautyDto));
//            }
//        });
        initBeautiView();

    }
    AlertDialog alertDialog;
    /**
     * Display setting dialog.
     */
    public void showSettingDialog(Context context, String msg, final int requestCode) {
        String message = msg;
        if(null==alertDialog){
            alertDialog  =  new AlertDialog.Builder(context).setCancelable(false)
                    .setTitle(context.getResources().getString(R.string.permissions_title))
                    .setMessage(message)
                    .setPositiveButton(R.string.permissions_setting, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setPermission(requestCode);
                        }
                    })
                    .show();
            return;
        }
        if(null!=alertDialog&&!alertDialog.isShowing()){
            alertDialog  =  new AlertDialog.Builder(context).setCancelable(false)
                    .setTitle(context.getResources().getString(R.string.permissions_title))
                    .setMessage(message)
                    .setPositiveButton(R.string.permissions_setting, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setPermission(requestCode);
                        }
                    })
                    .show();
        }

    }
    private void setPermission(int requestCode) {
        PermissionUtil.gotoPermission(StartLiveActivity.this);
    }
    private boolean initPremission(Map map) {
        if (Build.VERSION.SDK_INT >= 23) {
           new RxPermissions(this).requestEach(Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.RECORD_AUDIO)
                //    .compose(bindToLifecycle())
                    .subscribe(permission  -> {
                        Log.i("cxw","权限名称:"+permission.name+",申请结果:"+permission.granted);
                        if (!lacksPermission(StartLiveActivity.this, Manifest.permission.CAMERA)
                                && !lacksPermission(StartLiveActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                && !lacksPermission(StartLiveActivity.this, Manifest.permission.RECORD_AUDIO)
                        ) {
                            if(null!=map){
                                if (FloatingView.getInstance().isShow()) {
                                    showExitDialog(map);
                                } else {
                                    p.startLivePush(map);
                                    showLoadingDialog();
                                }
                            }

                        }
                        if (lacksPermission(StartLiveActivity.this,  Manifest.permission.RECORD_AUDIO)) {
//                            Toast.makeText(StartLiveActivity.this,
//                                    getResources().getString(R.string.permissions_audio), Toast.LENGTH_SHORT).show();
                            List<String> deniedPermissions = new ArrayList<>();
                            deniedPermissions.add(Manifest.permission.RECORD_AUDIO);
                            if (AndPermission.hasAlwaysDeniedPermission(StartLiveActivity.this, deniedPermissions)) {
                                showSettingDialog(StartLiveActivity.this,
                                        getResources().getString(R.string.permissions_audio),
                                        0);
                            } else {
                                initPremission(map);
                            }
                            return;
                        }
                        if (lacksPermission(StartLiveActivity.this,  Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                            Toast.makeText(StartLiveActivity.this,
//                                    getResources().getString(R.string.permissions_audio), Toast.LENGTH_SHORT).show();
                            List<String> deniedPermissions = new ArrayList<>();
                            deniedPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (AndPermission.hasAlwaysDeniedPermission(StartLiveActivity.this, deniedPermissions)) {
                                showSettingDialog(StartLiveActivity.this,
                                        getResources().getString(R.string.permissions_storage),
                                        0);
                            } else {
                                initPremission(map);
                            }
                            return;
                        }
                        if (lacksPermission(StartLiveActivity.this,  Manifest.permission.CAMERA)) {
//                            Toast.makeText(StartLiveActivity.this,
//                                    getResources().getString(R.string.permissions_audio), Toast.LENGTH_SHORT).show();
                            List<String> deniedPermissions = new ArrayList<>();
                            deniedPermissions.add(Manifest.permission.CAMERA);
                            if (AndPermission.hasAlwaysDeniedPermission(StartLiveActivity.this, deniedPermissions)) {
                                showSettingDialog(StartLiveActivity.this,
                                        getResources().getString(R.string.permissions_camera),
                                        0);
                            } else {
                                initPremission(map);
                            }
                            return;
                        }
                    });
        }
        return true;
    }
    public static boolean lacksPermission(Context mContexts, String permission) {
        if (ContextCompat.checkSelfPermission(mContexts, permission) == PackageManager.PERMISSION_DENIED) {

            return true;
        } else {

            return false;
        }


    }
    @Override
    protected TXCloudVideoView getPushView() {
        return pushView;
    }


    @Override
    protected int getContentView() {
        return R.layout.live_activity_startlive;
    }

    @OnClick({R2.id.btn_close, R2.id.btn_change_camera, R2.id.btn_upload, R2.id.btn_tochoose,
            R2.id.btn_meiyan, R2.id.btn_start, R2.id.tv_location, R2.id.iv_start_wx, R2.id.iv_start_circle, R2.id.fl_faceunity})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_close) {
            finish();
        } else if (id == R.id.btn_change_camera) {
            switchCamera(beautyDto.isFront);
        } else if (id == R.id.btn_upload) {
            MImagePicker.openImagePicker(StartLiveActivity.this, MImagePicker.TYPE_IMG, IMG_REQUESTCODE);
        } else if (id == R.id.btn_tochoose) {
            if (null == popwindow) {
                popwindow = new ChooseMenuPopwindow(this, menuList, tag -> {
                    tvLabel.setText(tag);
                  //  tvXz.setVisibility(View.GONE);
                    tvXz.setText("去修改");
                    popwindow.dismiss();
                });
                // 暂时注释掉这个功能,因为IOS没有这个功能
//                popwindow.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        StringBuilder menuName = new StringBuilder();
//                        List<ZhuboMenuDto> selectDatas = popwindow.getSelected();
//                        if (selectDatas.size() == 0) {
//                            return;
//                        }
//                        for (ZhuboMenuDto menuDto : selectDatas) {
//                            menuName.append(menuDto.name).append(",");
//                        }
//                        menuName.deleteCharAt(menuName.length() - 1);
//                        tvLabel.setText("直播标签：" + menuName);
//                    }
//                });
            }
            KeyBoardUtils.closeKeybord(etTitle, this);
            etTitle.postDelayed(() -> popwindow.showPopupWindow(), 300);
        } else if (id == R.id.btn_meiyan) {
            KeyBoardUtils.closeKeybord(etTitle, this);
            etTitle.postDelayed(() -> {
                setRootView(true);
//                meiyanPopwindow.showPopupWindow();
                if (flFaceUnity.getVisibility() == View.VISIBLE) {
                    flFaceUnity.setVisibility(View.GONE);
                    BeautyParameterModel.save();
                } else {
                    flFaceUnity.setVisibility(View.VISIBLE);
                }
            }, 300);
        } else if (id == R.id.btn_start) {
            startLive();
//            startLivePush(null);
        } else if (id == R.id.tv_location) {
            LocationUtils.showLocationDialog(this,false, new LocationUtils.OnSelectedListener() {
                @Override
                public void onSelected(String location) {
                    if(location.equals("全部/全部")){
                        tvLocation.setText("全部");
                    }else
                    tvLocation.setText(location);
                }
            });
        } else if (id == R.id.iv_start_circle) {
            p.getRoomInfo(String.valueOf(service.getUserInfo().id), SHARE_MEDIA.WEIXIN_CIRCLE);
        } else if (id == R.id.iv_start_wx) {
            p.getRoomInfo(String.valueOf(service.getUserInfo().id), SHARE_MEDIA.WEIXIN);
        } else if (id == R.id.fl_faceunity) {
            if (flFaceUnity.getVisibility() == View.VISIBLE) {
                setRootView(false);
                flFaceUnity.setVisibility(View.GONE);
                BeautyParameterModel.save();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (flFaceUnity.getVisibility() == View.VISIBLE) {

            BeautyParameterModel.save();
            setRootView(false);
            flFaceUnity.setVisibility(View.GONE);
            return;
        }
        finish();
    }

    private void setRootView(boolean showMeiYan) {
        int count = rootView.getChildCount();
        if (count > 1) {
            for (int i = 1; i < count; i++) {
                if (showMeiYan) rootView.getChildAt(i).setVisibility(View.GONE);
                else rootView.getChildAt(i).setVisibility(View.VISIBLE);
            }
        }
    }

    private void startLive() {
        String title = etTitle.getText().toString().trim();
        StringBuilder menuId = new StringBuilder();
        if (TextUtils.isEmpty(title)) {
            toastTip("请输入标题");
            return;
        }
        if (TextUtils.isEmpty(imgUrl)) {
            toastTip("请上传封面");
            return;
        }
        if (popwindow == null) {
            toastTip("请选择直播类型");
            return;
        } else {
            List<ZhuboMenuDto> selectDatas = popwindow.getSelected();
            if (selectDatas.size() == 0) {
                toastTip("请选择直播类型");
                return;
            }
            for (ZhuboMenuDto menuDto : selectDatas) {
                menuId.append(menuDto.id).append(",");
            }
            menuId.deleteCharAt(menuId.length() - 1);
        }
        Map map = new HashMap();
        map.put("menuIds", menuId);
        map.put("thumb", imgUrl);
        map.put("deviceInfo", getPhoneModel());
        map.put("title", title);
        map.put("latitude", String.valueOf(latitude));
        map.put("longitude", String.valueOf(longitude));


//        map.put("menuIds", "27,28");
//        map.put("thumb", "http://fwres.oss-cn-hangzhou.aliyuncs.com/upload/images/zip.png");
//        map.put("title", "pk测试");
//        map.put("deviceInfo", Build.MODEL);

        initPremission(map);

    }


    /**
     * 关闭悬浮窗弹框提示
     */
    public void showExitDialog(Map map) {
        FloatingView floatingView = FloatingView.getInstance();
        ExitDialog dialog = new ExitDialog();
        dialog.setNegativeButtonText("取消")
                .setPositiveButtonText("确定退出")
                .addDialogClickListener(new BaseDialogFragment.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        p.startLivePush(map);
                        showLoadingDialog();
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .setGear(floatingView.getGear())
                .setNickname(floatingView.getNickname())
                .setExpireTime(floatingView.getExpireTime())
                .setHeadImg(floatingView.getHeadImg())
                .setRoomId(floatingView.getRoomId())
                .setTip("退出达人房间，印象值将归零\n是否要退出")
                .show(getSupportFragmentManager(), "");
    }

    /**
     * 获取机型
     */
    public String getPhoneModel() {
        String brand = android.os.Build.BRAND;//手机品牌
        String model = android.os.Build.MODEL;//手机型号
        return brand + " " + model;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMG_REQUESTCODE) {
                File file = new File(MImagePicker.getImagePath(this, data));
                UCropUtils.startCrop(this, Uri.fromFile(file));
            } else if (requestCode == UCrop.REQUEST_CROP && data != null) {
                showLoadingDialog();
                Uri outputUri = UCrop.getOutput(data);
                File outputFile = new File(outputUri.getPath());
                UploadHelper.getInstance(this).doUpload(UploadHelper.TYPE_IMAGE, outputFile, new UploadHelper.OnUploadListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long cur, long total) {
                    }

                    @Override
                    public void onSuccess(String url) {
                        runOnUiThread(() -> {
                            imgUrl = url;
                            ImageLoader.loadImg(ivUpload, outputFile.getPath());
                            tv_type.setBackgroundResource(R.drawable.shap_gd_dialog_bg);
                            tv_type.setTextColor(Color.parseColor("#FFFFFF"));
                            tv_type.setText("更改封面");
                            hideLoadingDialog();
                        });
                    }

                    @Override
                    public void onError() {
                    }
                });
            }
        }
    }

    @Override
    public void setMenu(List<ZhuboMenuDto> records) {
        menuList = records;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBeautyControlView != null) {
            mBeautyControlView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != disposable && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void startLivePush(StartLivePushDto startLivePushDto) {
        stopCamera();
        hideLoadingDialog();
        setPushActivity(false,startLivePushDto);


    }

    private HttpResult<StartLivePushDto> mStartLivePushDto;
    @Override
    public void connectionLine(HttpResult<StartLivePushDto> startLivePushDto) {
        mStartLivePushDto = startLivePushDto;
        if (startLivePushDto.isSuccess() && startLivePushDto.data != null) {
            new CustomerDialog.Builder(this).setTitle("提示")
                    .setMsg("由于您上次异常断开直播，是否需要继续上次直播")
                    .setPositiveButton("需要", new CustomerDialog.onPositiveInterface() {
                        @Override
                        public void onPositive() {
                            p.requestLiveConnectionStatus();
                        }
                    }).setNegativeButton("不需要", new CustomerDialog.onNegetiveInterface() {
                @Override
                public void onNegetive() {
                    p.rejectReconnect();
                }
            }).setOutSideCancel(false)
                    .create().show();
        }
    }

    @Override
    public void onLiveConnection() {
        stopCamera();
        if (Build.VERSION.SDK_INT >= 23) {
            new RxPermissions(StartLiveActivity.this).request(Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.RECORD_AUDIO)
                    .compose(bindToLifecycle())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                setPushActivity(true,mStartLivePushDto.data);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

    }

    @Override
    public void onLiveReStart() {
        toastTip("您已下播，请重新开播");
    }

    private void setPushActivity(boolean isPush,StartLivePushDto startLivePushDto){
        if(isPush){
            Intent i = new Intent(StartLiveActivity.this, LivePushActivity.class);
            i.setExtrasClassLoader(getClass().getClassLoader());
            i.putExtra(LivePushActivity.LIVE_PUSH_DATA, startLivePushDto);
            i.putExtra(LivePushActivity.LIVE_PUSH_PK_DATA, startLivePushDto.getChannelPkInfo());
            startActivity(i);
            finish();
        }else {
            Intent i = new Intent(StartLiveActivity.this, LivePushActivity.class);
            i.putExtra(LivePushActivity.LIVE_PUSH_DATA, startLivePushDto);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void share(ChannelShareInfoDto data, SHARE_MEDIA shareMedia) {
        if (!CommentUtils.isWeixinAvilible(this)) {
            ToastUtils.showShort(this, "您没有安装微信！！！");
            return;
        }
        UMWeb web = new UMWeb(data.shareUrl);
        web.setTitle(data.shareTitle);
        web.setDescription(data.shareContent);
        if (!TextUtils.isEmpty(data.shareImg))
            web.setThumb(new UMImage(this, data.shareImg));
        else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            web.setThumb(new UMImage(this, bitmap));
//            bitmap.recycle();
        }
        new ShareAction(this)
                .setPlatform(shareMedia)//传入平台
                .withMedia(web)
                .setCallback(this)//回调监听器
                .share();
    }

    @Override
    public void toastTip(CharSequence msg) {
        super.toastTip(msg);
        hideLoadingDialog();
    }

    @Override
    public void jump() {
        ArouteUtils.toPathWithId(ArouterApi.REALNAME_ACTIVITY);
        ArouteUtils.toRealIdCard(service.getUserInfo().getMyIdCardWithdraw());
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        ToastUtils.showShort(this, "分享成功");
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        ToastUtils.showShort(this, "分享失败");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        ToastUtils.showShort(this, "取消分享");
    }
}
