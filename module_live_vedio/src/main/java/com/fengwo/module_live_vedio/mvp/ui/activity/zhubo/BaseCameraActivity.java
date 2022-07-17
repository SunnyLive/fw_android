package com.fengwo.module_live_vedio.mvp.ui.activity.zhubo;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import androidx.annotation.Nullable;


import com.faceunity.FURenderer;
import com.faceunity.renderer.CameraRenderer;
import com.faceunity.utils.CameraUtils;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_comment.base.BeautyDto;
import com.google.gson.Gson;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

public abstract class BaseCameraActivity<V extends MvpView, P extends BasePresenter<V>> extends BaseMvpActivity<V, P> implements
         CameraRenderer.OnRendererStatusListener {

    SurfaceView mSurfaceView;
    protected TXCloudVideoView mPusherView;                    // SDK 推流本地预览类
    protected TXLivePushConfig mLivePushConfig;                // SDK 推流 config
    protected TXLivePusher mLivePusher;                    // SDK 推流类
    //相芯美颜相关
    protected FURenderer mFURenderer;
    boolean mOnFirstCreate = true;
    private int mCurrentCamFacingIndex;//摄像头索引
    private boolean mFrontCamera = true;

    //    private OrientationEventListener mOrientationListener;
    private boolean mFirstFrame = true;
    public boolean mClear = false;
//    protected PreviewUtils mPreviewUtils;
//    protected MeiyanPopwindow meiyanPopwindow;

    protected Gson gson = new Gson();
    protected BeautyDto beautyDto;


    private boolean zipSuccess = false;
    private boolean isSticker = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(this);
        beautyDto = gson.fromJson((String) SPUtils1.get(this, "beauty", gson.toJson(new BeautyDto())), BeautyDto.class);
        mSurfaceView = findViewById(R.id.surface_view);
        mPusherView = getPushView();

        initPusher(beautyDto.isFront);
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
//            mLivePusher.setPushListener(this);
            initXiangxin();
            mLivePusher.startCameraPreview(mPusherView);
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
                if(null!=mFURenderer){
                    mFURenderer.onSurfaceDestroyed();
                }

                mOnFirstCreate = true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPusherView != null) {
            mPusherView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPusherView != null) {
            mPusherView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 停止本地预览
    }

    protected void stopCamera() {
        mLivePusher.stopCameraPreview(true);
//        if (beautyControlView != null) {
//            beautyControlView.onPause();
//            beautyControlView = null;
//        }
        if (mFURenderer != null) {
//            mFURenderer.destroyItems();
            mFURenderer = null;
        }
        mLivePusher.setVideoProcessListener(null);
        if (mPusherView != null) {
            mPusherView.onDestroy(); // 销毁 View
        }
    }

    protected abstract TXCloudVideoView getPushView();


    protected void switchCamera(boolean isFront) {
//        mPreviewUtils.switchCamera();
        beautyDto.isFront = !isFront;
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

    protected void getResult() {
//        mPreviewUtils.getSkinSoftenByte();
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


}
