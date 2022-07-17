package com.fengwo.module_live_vedio.mvp.ui.activity.zhubo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.OrientationEventListener;

import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.faceunity.FURenderer;

import com.faceunity.renderer.CameraRenderer.OnRendererStatusListener;
import com.faceunity.ui.entity.BeautyParameterModel;
import com.faceunity.utils.CameraUtils;
import com.fengwo.module_comment.dialog.CommonDialog;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_comment.base.BeautyDto;
import com.fengwo.module_live_vedio.helper.LiveNoticeHelper;
import com.fengwo.module_live_vedio.mvp.ui.activity.BaseLiveingRoomActivity;
import com.google.gson.Gson;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import razerdp.basepopup.BasePopupWindow;

import static com.tencent.rtmp.TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO;
import static com.tencent.rtmp.TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO;

/**
 * @intro
 * @date 2019/10/4
 */
public abstract class BaseLivePushActivity extends BaseLiveingRoomActivity implements ITXLivePushListener,
        FURenderer.OnTrackingStatusChangedListener, FURenderer.OnFUDebugListener,
        OnRendererStatusListener, SensorEventListener {
//    SurfaceView surfaceView;
//    @BindView(R2.id.layout_surface)
//    LinearLayout layoutSurface;
    protected TXLivePushConfig mLivePushConfig;                // SDK 推流 config
    protected TXLivePusher mLivePusher;                    // SDK 推流类
    private TXCloudVideoView mPusherView;                    // SDK 推流本地预览类
    private boolean mIsPushing;                     // 当前是否正在推流
    private int mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960;   // 当前分辨率
    private PhoneStateListener mPhoneListener = null;         // 当前电话监听Listener
    private String mPushUrl;
    TextView tv_monitor;
    protected Gson gson = new Gson();
//    protected PreviewUtils mPreviewUtils;
    protected BeautyDto beautyDto;
    //相芯美颜相关
    protected FURenderer mFURenderer;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    boolean mOnFirstCreate = true;
    private int mCurrentCamFacingIndex;//摄像头索引
    private boolean mFrontCamera = true;

//    protected MeiyanPopwindow meiyanPopwindow;
    private OrientationEventListener mOrientationListener;
    private boolean mFirstFrame = true;
    protected volatile Boolean isPk = false;//是否pk状态
    protected boolean isMirror;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isMirror = (boolean) SPUtils1.get(this,"isMirror",false);
        beautyDto = gson.fromJson((String) SPUtils1.get(this, "beauty", gson.toJson(new BeautyDto())), BeautyDto.class);
        initPremission();
        // 初始化电话监听
        mPusherView = getPushView();
//        mPreviewUtils = new PreviewUtils(getApplicationContext(), this);
//        mPreviewUtils.setUseBigEyeSlimFace(true);
//        mPreviewUtils.setBeauty(beautyDto);//设置美颜
//        meiyanPopwindow = new MeiyanPopwindow(this);
//        meiyanPopwindow.setBeautyProgress(beautyDto);
        //new xx
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        initPusher(beautyDto.isFront);
        initListener();
    }

    private boolean initPremission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this,
                        permissions.toArray(new String[0]),
                        100);
                return false;
            }
        }
        return true;
    }


    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
//        mPreviewUtils.onresume();//启动照相机
//        reStartEngine(isPk);
//        // 注册方向回调，检测屏幕方向改变
//        if (null == mOrientationListener) {
//            mOrientationListener = new OrientationEventListener(this) {
//                @Override
//                public void onOrientationChanged(int orientation) {
//                    mPreviewUtils.onScreenOriChanged(orientation);
//                }
//            };
//            mOrientationListener.enable();
//        }
        // 退出隐私模式
        if (mLivePusher != null)
            mLivePusher.resumePusher();
        if (mPusherView != null) {
            mPusherView.onResume();
        }
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        super.onPause();
//        mPreviewUtils.pause();
//        mFirstFrame = true;
//        if (null != mOrientationListener) {
//            mOrientationListener.disable();
//            mOrientationListener = null;
//        }
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPusherView != null) {
            mPusherView.onPause();
        }
        if (mLivePusher != null)
            mLivePusher.pausePusher();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRTMPPush(); // 停止推流
//        mPreviewUtils.pause();
//        mPreviewUtils.freeRes();
        if (mFURenderer!=null){
//            mFURenderer.destroyItems();
            mFURenderer = null;
        }
        if (mPusherView != null) {
            mPusherView.onDestroy(); // 销毁 View
        }
//        if (surfaceView != null) {
//            surfaceView = null;
//        }
        unInitPhoneListener();
    }

    protected abstract TXCloudVideoView getPushView();


    /**
     * 初始化 SDK 推流器
     */
    protected void initPusher(boolean frontCamera) {
        if (mLivePusher == null) {
            mLivePushConfig = new TXLivePushConfig();
            mLivePushConfig.setFrontCamera(frontCamera);
            mLivePushConfig.enableScreenCaptureAutoRotate(true);// 是否开启屏幕自适应
            mLivePushConfig.setAutoAdjustStrategy(TXLiveConstants.AUTO_ADJUST_BITRATE_STRATEGY_1);//码率自适应算法。
            mLivePusher = new TXLivePusher(this);
            mLivePusher.setConfig(mLivePushConfig);
            mLivePusher.setPushListener(this);
            initXiangxin();
        }
    }
    private void initXiangxin(){
        int frontCameraOrientation = 270;
        frontCameraOrientation = CameraUtils.getFrontCameraOrientation();
        mFURenderer = new FURenderer
                .Builder(this)
                .inputTextureType(0)
                .createEGLContext(false)
                .inputImageOrientation(frontCameraOrientation)
                .setNeedFaceBeauty(true)
                .setOnTrackingStatusChangedListener(this)
                .build();
//        beautyControlView.setOnFaceUnityControlListener((OnFaceUnityControlListener) mFURenderer);
//        FaceBeautyModel.getInstance().setOnFaceUnityControlListener((OnFaceUnityControlListener) mFURenderer);
        mLivePusher.setVideoProcessListener(new TXLivePusher.VideoCustomProcessListener() {
            /**
             * 在OpenGL线程中回调，在这里可以进行采集图像的二次处理
             *
             * @param i  纹理ID
             * @param i1 纹理的宽度
             * @param i2 纹理的高度
             * @return 返回给SDK的纹理
             * 说明：SDK回调出来的纹理类型是GLES20.GL_TEXTURE_2D，接口返回给SDK的纹理类型也必须是GLES20.GL_TEXTURE_2D
             */
            @Override
            public int onTextureCustomProcess(int i, int i1, int i2) {
                if (mOnFirstCreate) {
                    Log.d("lgl", "onTextureCustomProcess: create");
                    mFURenderer.onSurfaceCreated();
                    mOnFirstCreate = false;
                }
                int texId = mFURenderer.onDrawFrame(i, i1, i2);
                return texId;
            }

            /**
             * 增值版回调人脸坐标
             *
             * @param floats 归一化人脸坐标，每两个值表示某点P的X,Y值。值域[0.f, 1.f]
             */
            @Override
            public void onDetectFacePoints(float[] floats) {

            }

            /**
             * 在OpenGL线程中回调，可以在这里释放创建的OpenGL资源
             */
            @Override
            public void onTextureDestoryed() {
                mFURenderer.onSurfaceDestroyed();
                mOnFirstCreate = true;
            }
        });
    }

    protected boolean startRtmpPush(String pushUrl) {
        if (TextUtils.isEmpty(pushUrl) || (!pushUrl.trim().toLowerCase().startsWith("rtmp://"))) {
            Toast.makeText(getApplicationContext(), "推流地址不合法，目前支持rtmp推流!", Toast.LENGTH_SHORT).show();
        }
        // 显示本地预览的View
        mPusherView.setVisibility(View.VISIBLE);
        // 添加播放回调
        mLivePusher.setPushListener(this);
        // 添加后台垫片推流参数
        // bitmap: 用于指定垫片图片，最大尺寸不能超过 1920*1920
        // time：垫片最长持续时间，单位是秒，300即代表最长持续300秒
        // fps：垫片帧率，最小值为 5fps，最大值为 20fps。
        Bitmap bitmap = decodeResource(getResources(), R.drawable.pause_publish);
        mLivePushConfig.setPauseImg(bitmap);
        mLivePushConfig.setPauseImg(300, 5);
        //表示仅暂停视频采集，不暂停音频采集
        //mLivePushConfig.setPauseFlag(PAUSE_FLAG_PAUSE_VIDEO);
        //表示同时暂停视频和音频采集
        mLivePushConfig.setPauseFlag(PAUSE_FLAG_PAUSE_VIDEO | PAUSE_FLAG_PAUSE_AUDIO);
        // 设置推流分辨率
        mLivePushConfig.setVideoResolution(mCurrentVideoResolution);
        // 开启麦克风推流相关
        mLivePusher.setMute(false);
         //是否开启观众端镜像观看
        mLivePusher.setMirror(isMirror);

        // 是否打开调试信息
        mPusherView.showLog(false);
        // 是否打开曝光对焦
        mLivePushConfig.setTouchFocus(false);
        mLivePushConfig.setVideoEncodeGop(2);
        // 是否打开手势放大预览画面
        mLivePushConfig.setEnableZoom(false);
        mLivePushConfig.enableANS(true);
        //客户自己采集视频
//        mLivePushConfig.setCustomModeType(TXLiveConstants.CUSTOM_MODE_VIDEO_CAPTURE);
         //设置推流配置
        mLivePusher.setConfig(mLivePushConfig);
//        initXiangxin();
         //设置场景
//        setPushScene(mPushSettingFragment.getQualityType(), mPushSettingFragment.isEnableAdjustBitrate());
        // 设置本地预览View

        mLivePusher.startCameraPreview(mPusherView);

        // 发起推流
        int ret = mLivePusher.startPusher(pushUrl.trim());
        mPushUrl = pushUrl.trim();
        if (ret == -5) {
            String errInfo = "License 校验失败";
            int start = (errInfo + " 详情请点击[").length();
            int end = (errInfo + " 详情请点击[License 使用指南").length();
            SpannableStringBuilder spannableStrBuidler = new SpannableStringBuilder(errInfo + " 详情请点击[License 使用指南]");
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://cloud.tencent.com/document/product/454/34750");
                    intent.setData(content_url);
                    startActivity(intent);
                }
            };
            spannableStrBuidler.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStrBuidler.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            TextView tv = new TextView(this);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            tv.setText(spannableStrBuidler);
            tv.setPadding(20, 0, 20, 0);
            showPushFailDialog(tv);
            return false;
        }
//        // 设置混响
//        mLivePusher.setReverb(mPushSettingFragment.getReverbIndex());
//        // 设置变声
//        mLivePusher.setVoiceChangerType(mPushSettingFragment.getVoiceChangerIndex());
        mIsPushing = true;
        return true;
    }

    private AgainPushStreamDialog ad;
    @SuppressLint("CheckResult")
    public void showPushFailDialog(TextView tv) {
        if (tv != null) return;
        if (ad == null) {
            ad = new AgainPushStreamDialog(this,30);
            ad.showPopupWindow();
            ad.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    ad = null;
                }
            });
        }
    }

    protected void stopRTMPPush() {
        // 停止本地预览
        mLivePusher.stopCameraPreview(true);
        // 移除监听
        mLivePusher.setPushListener(null);
        // 停止推流
        mLivePusher.stopPusher();
        // 隐藏本地预览的View
        mPusherView.setVisibility(View.GONE);
        // 移除垫片图像
        mLivePushConfig.setPauseImg(null);
        mIsPushing = false;
    }

    /**
     * IsTracking（人脸识别回调相关定义
     *
     * @param status
     */
    @Override
    public void onTrackingStatusChanged(int status) {

    }

    /**
     * 推流器状态回调
     *
     * @param event 事件id.id类型请参考 {@linkplain TXLiveConstants#PLAY_EVT_CONNECT_SUCC 推流事件列表}.
     * @param param
     */
    @Override
    public void onPushEvent(int event, Bundle param) {
        KLog.a("yang","onPushEvent -> event ：" + event + " url : " + param);
        // Toast错误内容
        if (event < 0) {
            Toast.makeText(getApplicationContext(), param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
        }
        if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT
                || event == TXLiveConstants.PUSH_ERR_INVALID_ADDRESS
                || event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL
                || event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
            // 遇到以上错误，则停止推流
            stopRTMPPush();
            showPushFailDialog(null);
        } else if (event == TXLiveConstants.PUSH_WARNING_HW_ACCELERATION_FAIL) {
            // 开启硬件加速失败
            Toast.makeText(getApplicationContext(), param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
            mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
            mLivePusher.setConfig(mLivePushConfig);
        } else if (event == TXLiveConstants.PUSH_EVT_CHANGE_RESOLUTION) {
            L.d("change resolution to " + param.getInt(TXLiveConstants.EVT_PARAM2) + ", bitrate to" + param.getInt(TXLiveConstants.EVT_PARAM1));
        } else if (event == TXLiveConstants.PUSH_EVT_CHANGE_BITRATE) {
            L.d("change bitrate to" + param.getInt(TXLiveConstants.EVT_PARAM1));
        } else if (event == TXLiveConstants.PUSH_WARNING_NET_BUSY) {
            toastTip("您当前的网络环境不佳，请尽快更换网络保证正常直播");
        } else if (event == TXLiveConstants.PUSH_EVT_START_VIDEO_ENCODER) {
//            int encType = param.getInt(TXLiveConstants.EVT_PARAM1);
//            boolean hwAcc = (encType == TXLiveConstants.ENCODE_VIDEO_HARDWARE);
//            Toast.makeText(CameraPusherActivity.this, "是否启动硬编：" + hwAcc, Toast.LENGTH_SHORT).show();
        } else if (event == TXLiveConstants.PUSH_EVT_OPEN_CAMERA_SUCC) {
            // 只有后置摄像头可以打开闪光灯，若默认需要开启闪光灯。 那么在打开摄像头成功后，才可以进行配置。 若果当前是前置，设定无效；若是后置，打开闪光灯。
//            mLivePusher.turnOnFlashLight(mPushMoreFragment.isFlashEnable());
        } else if (event == TXLiveConstants.PUSH_EVT_CONNECT_SUCC) {
//            if (!beautyDto.isFront)mPreviewUtils.switchCamera();
        } else if (event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
            toastTip("打开麦克风失败");
        }
    }


    @Override
    public void onNetStatus(Bundle status) {
        String str = getStatus(status);
        tv_monitor.setText(str);
        L.d("Current status, CPU:" + status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE) +
                ", RES:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT) +
                ", SPD:" + status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps" +
                ", FPS:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS) +
                ", ARA:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps" +
                ", VRA:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps");
//        mPusherTroublieshootingFragment.setLogText(status, null, 0);
//        if (mLivePusher != null){
//            mLivePusher.onLogRecord("[net state]:\n"+str+"\n");
//        }
    }

    /**
     * 获取当前推流状态
     *
     * @param status
     * @return
     */
    public String getStatus(Bundle status) {
        String str = String.format("%-14s %-14s %-12s\n%-8s %-8s %-8s %-8s\n%-14s %-14s %-12s\n%-14s %-14s",
                "CPU:" + status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE),
                "RES:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT),
                "SPD:" + status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps",
                "JIT:" + status.getInt(TXLiveConstants.NET_STATUS_NET_JITTER),
                "FPS:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS),
                "GOP:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_GOP) + "s",
                "ARA:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps",
                "QUE:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_CACHE) + "|" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_CACHE),
                "DRP:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_DROP) + "|" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_DROP),
                "VRA:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps",
                "SVR:" + status.getString(TXLiveConstants.NET_STATUS_SERVER_IP),
                "AUDIO:" + status.getString(TXLiveConstants.NET_STATUS_AUDIO_INFO));

        return str;
    }

    ////////////////////////////////////////监听相关

    /**
     * 初始化电话监听
     */
    private void initListener() {
        mPhoneListener = new TXPhoneStateListener(mLivePusher);
        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 销毁
     */
    private void unInitPhoneListener() {
        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);
    }


    protected void switchCamera(boolean isFronnt) {
        beautyDto.isFront = !isFronnt;
//        mPreviewUtils.switchCamera();
        SPUtils1.put(this, BeautyDto.BEAUTYTAG, gson.toJson(beautyDto));
        mFrontCamera = !mFrontCamera;
        mLivePusher.switchCamera();

        /*设置是否使用前置摄像头。默认使用前置摄像头*/
        TXLivePushConfig config = mLivePusher.getConfig();
        config.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_720_1280);
        config.setAutoAdjustBitrate(false);
        config.setVideoBitrate(1800);
        config.setFrontCamera(mFrontCamera);
//        if (mFrontCamera) {
//            mCurrentCamFacingIndex = -1;
//        } else {
//            mCurrentCamFacingIndex = 0;
//        }
//        FaceBeautyModel.getInstance().setmCurrentCamFacingIndex(mCurrentCamFacingIndex);
//        FaceBeautyModel.getInstance().save();
//        /*切换摄像头*/
//        mFURenderer.onCameraChange(mFrontCamera ? Camera.CameraInfo.CAMERA_FACING_FRONT :
//                Camera.CameraInfo.CAMERA_FACING_BACK, 0);
    }

//    protected byte[] getSkinSoftenGetResult(){
//        return mPreviewUtils.getSkinSoftenByte();
//    }

    /**
     * 电话监听
     */
    private static class TXPhoneStateListener extends PhoneStateListener {
        WeakReference<TXLivePusher> mPusher;

        public TXPhoneStateListener(TXLivePusher pusher) {
            mPusher = new WeakReference<TXLivePusher>(pusher);
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            TXLivePusher pusher = mPusher.get();
            switch (state) {
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (pusher != null) pusher.pausePusher();
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (pusher != null) pusher.resumePusher();
                    break;
            }
        }
    }

    /**
     * 获取资源图片
     *
     * @param resources
     * @param id
     * @return
     */
    private Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }

    @Override
    public void onFpsChange(final double fps, final double renderTime) {
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~FURenderer调用部分~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void onSurfaceCreated() {
        mFURenderer.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(int viewWidth, int viewHeight) {
    }

    @Override
    public int onDrawFrame(byte[] cameraNV21Byte, int cameraTextureId, int cameraWidth, int cameraHeight, float[] mvpMatrix, long timeStamp) {
        int fuTextureId = 0;
//        if (isDoubleInputType) {
//            fuTextureId = mFURenderer.onDrawFrame(cameraNV21Byte, cameraTextureId, cameraWidth, cameraHeight);
//        } else if (cameraNV21Byte != null) {
//            if (mFuNV21Byte == null || mFuNV21Byte.length != cameraNV21Byte.length) {
//                mFuNV21Byte = new byte[cameraNV21Byte.length];
//            }
//            System.arraycopy(cameraNV21Byte, 0, mFuNV21Byte, 0, cameraNV21Byte.length);
//            fuTextureId = mFURenderer.onDrawFrame(mFuNV21Byte, cameraWidth, cameraHeight);
//        }
//        if (CameraRenderer.DRAW_LANDMARK) {
//            mCameraRenderer.setLandmarksData(mFURenderer.getLandmarksData(0));
//        }
//        sendRecordingData(fuTextureId, mvpMatrix, timeStamp / Constant.NANO_IN_ONE_MILLI_SECOND);
//        checkPic(fuTextureId, mvpMatrix, cameraHeight, cameraWidth);
        return fuTextureId;
    }

    @Override
    public void onSurfaceDestroy() {
        mFURenderer.onSurfaceDestroyed();
    }

    @Override
    public void onCameraChange(int cameraType, int cameraOrientation) {
        mFURenderer.onCameraChange(cameraType, cameraOrientation);
//        mVerticalSeekBar.setProgress((int) (100 * mCameraRenderer.getExposureCompensation()));
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Sensor部分~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if (Math.abs(x) > 3 || Math.abs(y) > 3) {
                if (Math.abs(x) > Math.abs(y)) {
                    mFURenderer.setTrackOrientation(x > 0 ? 270 : 90);
                } else {
                    mFURenderer.setTrackOrientation(y > 0 ? 0 : 180);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onDropLiveStreamSuccess() {
        mLivePusher = null;
        initPusher(beautyDto.isFront);
        startRtmpPush(mPushUrl);
    }

    @Override
    public void onDropLiveStreamError() {
        getWindow().getDecorView().postDelayed(() -> {
            if (isDestroyed()) return;
            showPushFailDialog(null);
        }, 3000);
    }


    /**
     * 获取主播端的房间号
     * @return id
     */
    public abstract String getPushRoomId();


    /**
     *
     * 主播端 推流失败重连三次失败后的操作
     *
     * 重连请求接口  重开就去重新开播
     *
     */
    class AgainPushStreamDialog extends BasePopupWindow {


        private Disposable mSubscribe;
        private TextView mTvAgainPush;
        private TextView mTvReStartLive;
        private TextView mBtnReStartLive;
        private boolean isRunTime = true;


        public AgainPushStreamDialog(Context context,int seconds) {
            super(context);
            mTvAgainPush = getContentView().findViewById(R.id.tv_again_push_live);
            mTvReStartLive = getContentView().findViewById(R.id.tv_restart_live);
            mBtnReStartLive = getContentView().findViewById(R.id.tv_restart_live2);
            setPopupGravity(Gravity.CENTER);
            mSubscribe = Observable.interval(0, 1, TimeUnit.SECONDS)
                    .takeWhile(aLong -> isRunTime)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(time -> {
                        if (time == seconds) {
                            mTvReStartLive.setVisibility(View.GONE);
                            mTvAgainPush.setVisibility(View.GONE);
                            mBtnReStartLive.setVisibility(View.VISIBLE);
                            isRunTime = false;
                        }
                    });
            //重新推流
            mTvAgainPush.setOnClickListener(v -> {
                isRunTime = false;
                p.requestDropLiveStream(getPushRoomId());
                dismiss();
            });
            //重新开播
            mTvReStartLive.setOnClickListener(v -> {
                isRunTime = false;
                dismiss();
                requestReStartLive();
            });
            //重新开播
            mBtnReStartLive.setOnClickListener(v -> {
                isRunTime = false;
                dismiss();
                requestReStartLive();
            });

        }

        @Override
        public View onCreateContentView() {
            return createPopupById(R.layout.layout_again_push_stream_dialog);
        }

        @Override
        public void dismiss() {
            if (mSubscribe != null)
                mSubscribe.dispose();
            super.dismiss();
        }
    }

    public abstract void requestReStartLive();
}
