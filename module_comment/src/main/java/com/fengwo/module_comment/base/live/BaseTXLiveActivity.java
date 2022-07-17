package com.fengwo.module_comment.base.live;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.faceunity.FURenderer;
import com.faceunity.renderer.CameraRenderer;
import com.faceunity.utils.CameraUtils;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.BeautyDto;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_comment.widget.floatingview.FloatingView;
import com.google.gson.Gson;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.tencent.rtmp.TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO;
import static com.tencent.rtmp.TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/27
 */
public abstract class BaseTXLiveActivity<V extends MvpView, P extends BasePresenter<V>> extends BaseMvpActivity<V, P> implements ITXLivePushListener,
        FURenderer.OnTrackingStatusChangedListener, FURenderer.OnFUDebugListener,
        CameraRenderer.OnRendererStatusListener, SensorEventListener {


    //拉流相关
//    private TXLivePlayer mLivePlayer = null;
//    public static final int ACTIVITY_TYPE_LINK_MIC = 4;
//    public static final int ACTIVITY_TYPE_REALTIME_PLAY = 5;
//    private boolean mHWDecode = false;
//    private int mCurrentRenderMode;
//    private int mCurrentRenderRotation;
//
//    private ITXLivePlayListener mainListener, pkMineListener, pkOtherListener;
//    protected int mActivityType = 2;
//    protected int mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
//    public static final int ACTIVITY_TYPE_PUBLISH = 1;
//    public static final int ACTIVITY_TYPE_LIVE_PLAY = 2;
//    public static final int ACTIVITY_TYPE_VOD_PLAY = 3;
//    private long mStartPlayTS = 0;

    //推流相关
    protected TXLivePushConfig mLivePushConfig;                // SDK 推流 config
    protected TXLivePusher mLivePusher;                    // SDK 推流类
    private TXCloudVideoView mPusherView;                    // SDK 推流本地预览类
    private boolean mIsPushing;                     // 当前是否正在推流
    private int mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960;   // 当前分辨率
    private PhoneStateListener mPhoneListener = null;         // 当前电话监听Listener

    protected Gson gson = new Gson();
    protected BeautyDto beautyDto;
    //相芯美颜相关
    protected FURenderer mFURenderer;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    boolean mOnFirstCreate = true;
    private boolean mFrontCamera = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
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

    protected abstract TXCloudVideoView getPushView();//获取推流画布

    protected abstract BeautyDto getBeautyDto();//获取推流画布

    @Override
    protected void initView() {
        initPremission();
//        mActivityType = 2;
        mPusherView = getPushView();
        beautyDto = getBeautyDto();
        //new xx
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        initPusher(beautyDto.isFront);
        initListener();
//        mCurrentRenderMode = TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;
//        mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;
//        mainListener = new ITXLivePlayListener() {
//            @Override
//            public void onPlayEvent(int event, Bundle param) {
//                String playEventLog = "score event: " + event + ", " + param.getString(TXLiveConstants.EVT_DESCRIPTION);
//                if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
////                    onPlayBegin();
////                    L.d("AutoMonitor", "PlayFirstRender,cost=" + (System.currentTimeMillis() - mStartPlayTS));
//                } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_EVT_PLAY_END) {
//                    L.d("txplayevent", "PLAY_ERR_NET_DISCONNECT");
////                    onPlayEnd();
//                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
//                    L.d("txplayevent", "PLAY_EVT_PLAY_LOADING");
//                } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
//                    L.d("txplayevent", "PLAY_EVT_RCV_FIRST_I_FRAME");
//                } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
//                    L.d("txplayevent", "PLAY_EVT_CHANGE_RESOLUTION");
//                } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_ROTATION) {
//                    L.d("txplayevent", "PLAY_EVT_CHANGE_ROTATION");
//                    return;
//                }
//            }
//
//            @Override
//            public void onNetStatus(Bundle bundle) {
//
//            }
//        };
    }

    protected void setRenderMode(int mode) {
//        mCurrentRenderMode = mode;
        PlayerManager.getInstance(this).setRenderMode(mode);
    }

//    protected boolean checkPlayUrl(final String playUrl) {
//        if (TextUtils.isEmpty(playUrl) || (!playUrl.startsWith("http://") && !playUrl.startsWith("https://") && !playUrl.startsWith("rtmp://") && !playUrl.startsWith("/"))) {
////            Toast.makeText(getApplicationContext(), "播放地址不合法，直播目前仅支持rtmp,flv播放方式!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        switch (mActivityType) {
//            case ACTIVITY_TYPE_LIVE_PLAY: {
//                if (playUrl.startsWith("rtmp://")) {
//                    mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
//                } else if ((playUrl.startsWith("http://") || playUrl.startsWith("https://")) && playUrl.contains(".flv")) {
//                    mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;
//                } else {
////                    Toast.makeText(getApplicationContext(), "播放地址不合法，直播目前仅支持rtmp,flv播放方式!", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            }
//            break;
//            case ACTIVITY_TYPE_REALTIME_PLAY:
//                mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP_ACC;
//                break;
//            default:
////                Toast.makeText(getApplicationContext(), "播放地址不合法，目前仅支持rtmp,flv播放方式!",Toast.LENGTH_SHORT).show();
//                return false;
//        }
//        return true;
//    }


//    public boolean startPlay(String url, TXCloudVideoView txCloudVideoView, ITXLivePlayListener listener) {
//
//        stopPlay();
//
//        if (!checkPlayUrl(url)) {
//            return false;
//        }
//
//        mLivePlayer = new TXLivePlayer(this);
//        mLivePlayer.setPlayListener(listener == null ? mainListener : listener);
//        // 硬件加速在1080p解码场景下效果显著，但细节之处并不如想象的那么美好：
//        // (1) 只有 4.3 以上android系统才支持
//        // (2) 兼容性我们目前还仅过了小米华为等常见机型，故这里的返回值您先不要太当真
//        mLivePlayer.enableHardwareDecode(mHWDecode);
//        mLivePlayer.setRenderRotation(mCurrentRenderRotation);
//        mLivePlayer.setRenderMode(mCurrentRenderMode);
//        //设置播放器缓存策略
//        //这里将播放器的策略设置为自动调整，调整的范围设定为1到4s，您也可以通过setCacheTime将播放器策略设置为采用
//        //固定缓存时间。如果您什么都不调用，播放器将采用默认的策略（默认策略为自动调整，调整范围为1到4s）
//        mLivePlayer.setPlayerView(txCloudVideoView);
//        int result = mLivePlayer.startPlay(url, mPlayType); // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
//        mStartPlayTS = System.currentTimeMillis();
//        if (result != 0) {
//            return false;
//        }
//        return true;
//    }

    public void startPlay(String url, TXCloudVideoView videoView, ITXLivePlayListener listener) {
        FloatingView.getInstance().setPlayUrl(url);
        PlayerManager.getInstance(this).startPlay(url, videoView, listener);
    }

    protected void stopPlay() {
//        if (mLivePlayer != null) {
//            mLivePlayer.stopRecord();
//            mLivePlayer.setPlayListener(null);
//            mLivePlayer.stopPlay(true);
//            mLivePlayer = null;
//        }
        PlayerManager.getInstance(this).stopPlay();
    }


    @Override
    protected void onDestroy() {
//        stopPlay();
//        mainListener = null;
//        pkOtherListener = null;
//        pkMineListener = null;
        super.onDestroy();

        stopRTMPPush(); // 停止推流
        if (mFURenderer != null) {
            mFURenderer = null;
        }

        if (mPusherView != null) {
            mPusherView.onDestroy(); // 销毁 View
        }
        unInitPhoneListener();
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (null != mLivePlayer)
//            mLivePlayer.pause();
        PlayerManager.getInstance(getApplicationContext()).onPause();
        if (mSensorManager != null)
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
    public void onResume() {
        super.onResume();
//        if (null != mLivePlayer)
//            mLivePlayer.resume();
        PlayerManager.getInstance(getApplicationContext()).onResume();


        // 退出隐私模式
        if (mLivePusher != null)
            mLivePusher.resumePusher();
        if (mPusherView != null) {
            mPusherView.onResume();
        }
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


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

    private void initXiangxin() {
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
        mLivePusher.setMute(true);
        //是否开启观众端镜像观看
        mLivePusher.setMirror(getMirrir());

        // 是否打开调试信息
        mPusherView.showLog(false);
        // 是否打开曝光对焦
        mLivePushConfig.setTouchFocus(false);
        mLivePushConfig.setVideoEncodeGop(2);
        // 是否打开手势放大预览画面
        mLivePushConfig.setEnableZoom(false);
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

    protected abstract boolean getMirrir();//是否镜像推流

    private void showPushFailDialog(TextView tv) {
        AlertDialog.Builder dialogBuidler = new AlertDialog.Builder(this);
//        dialogBuidler.setTitle("网络异常").setView(tv).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                stopRTMPPush();
//                finish();
//            }
//        });
//        dialogBuidler.show();
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
        String msg = param.getString(TXLiveConstants.EVT_DESCRIPTION);
        String pushEventLog = "score event: " + event + ", " + msg;
        L.d(pushEventLog);
        // Toast错误内容
        if (event < 0) {
            Toast.makeText(getApplicationContext(), param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
        }
        if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT
                || event == TXLiveConstants.PUSH_ERR_INVALID_ADDRESS
                || event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL
                || event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
            // 遇到以上错误，则停止推流
            TextView tv = new TextView(this);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            tv.setText("网络异常，请重试");
            tv.setPadding(20, 0, 20, 0);
            showPushFailDialog(tv);
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
//            toastTip("当前网络环境较差");
            L.d("当前网络环境较差");
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
    private String getStatus(Bundle status) {
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
    }


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
                    if (pusher != null) pusher.pausePusher();
                    break;
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

}
