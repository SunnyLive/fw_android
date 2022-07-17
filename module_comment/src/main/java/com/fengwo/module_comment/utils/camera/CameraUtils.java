package com.fengwo.module_comment.utils.camera;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by xieyi on 2017/banner_03/12.
 */

public class CameraUtils implements ICameraUtils {
    private final String LOG_TAG = "CameraUtils";
    private Camera mCamera;
    private Object mThreadLock = new Object();
    private int mCurrentCameraType = 1;//Camera.CameraInfo.CAMERA_FACING_FRONT
    private Camera.Size mCameraPreviewFrameSize;
    private int mOritation;
    Camera.PreviewCallback mPreviewCallbakck;
    SurfaceTexture mSurfaceTexture;


    public CameraUtils(Camera.PreviewCallback callback) {
        mPreviewCallbakck = callback;

    }

    public void switchCamera() {
        if (mSurfaceTexture == null) return;
        stopCamera();
        mCurrentCameraType = mCurrentCameraType == 0 ? 1 : 0;
        startCamera();
        startCameraPreview(mSurfaceTexture);

    }

    public int getCurrentOrientation() {
        return mOritation;
    }

    @Override
    public void stopCamera() {
        if (mCamera == null)
            return;
        synchronized (mThreadLock) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
        Log.i(LOG_TAG, "stopped camera");
    }

    @Override
    public void startCamera() {
        Log.i(LOG_TAG, "starting camera");
        if (mCamera != null) return;
        synchronized (mThreadLock) {
            Camera.CameraInfo camInfo = new Camera.CameraInfo();

            int numCameras = Camera.getNumberOfCameras();
            for (int i = 0; i < numCameras; i++) {
                Camera.getCameraInfo(i, camInfo);
                if (camInfo.facing == mCurrentCameraType) {
                    mCamera = Camera.open(i);
                    mOritation = camInfo.orientation;
                    break;
                }
            }

            if (mCamera == null) {
                Log.i(LOG_TAG, "No back-facing camera found; opening default");
                mCamera = Camera.open();
            }
            if (mCamera == null) {
                throw new RuntimeException("Unable to open camera");
            }

            Camera.Parameters camParams = mCamera.getParameters();

            mCameraPreviewFrameSize = getProperPreviewSize(camParams);
            if (mCameraPreviewFrameSize == null)
                mCameraPreviewFrameSize = camParams.getPreviewSize();


            if (mCameraPreviewFrameSize != null) {
                camParams.setPreviewSize(mCameraPreviewFrameSize.width, mCameraPreviewFrameSize.height);
            }
            camParams.setPreviewFormat(ImageFormat.NV21);//YV12时才用
            camParams.setRecordingHint(true);


            mCamera.setParameters(camParams);
            mCamera.setPreviewCallback(mPreviewCallbakck);
           /* mCamera.addCallbackBuffer(new byte[((mCameraPreviewFrameSize.width * mCameraPreviewFrameSize.height)
                    * ImageFormat.getBitsPerPixel(ImageFormat.NV21)) / 8]);

            mCamera.setPreviewCallbackWithBuffer(mPreviewCallbakck);*/


            int[] fps = new int[2];
            camParams.getPreviewFpsRange(fps);

            Log.i(LOG_TAG, "camera preview frame size is " + mCameraPreviewFrameSize.width + "x" + mCameraPreviewFrameSize.height);
            Log.i(LOG_TAG, "camera preview fps range is " + fps[0] / 1000.0f + " - " + fps[1] / 1000.0f);
        }
    }

/*

    public void setPreviewCallback(Camera.PreviewCallback callback){
        mCamera.setPreviewCallbackWithBuffer(callback);
    }
*/

    @Override
    public void startCameraPreview(SurfaceTexture surfaceTexture) {

        try {
            mSurfaceTexture = surfaceTexture;
            mCamera.setPreviewTexture(mSurfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // start the camera preview output
        Log.i(LOG_TAG, "starting camera preview");
        mCamera.startPreview();

    }

    public void addCallbackBuffer(byte[] data) {
        mCamera.addCallbackBuffer(data);
    }


    private Camera.Size getProperPreviewSize(Camera.Parameters parameters) {
        int min = 640 * 480;
        int max = 720 * 1280;
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();

        Collections.sort(sizeList, new Comparator<Camera.Size>() {
            public int compare(Camera.Size o1, Camera.Size o2) {
                if (o1.width > o2.width) {
                    return -1;
                }
                if (o1.width == o2.width) {
                    if (o1.height > o2.height) return -1;
                    if (o1.height == o2.height) return 0;
                    return 1;
                }
                return 1;
            }
        });

        Camera.Size previewSize = null;
        for (int i = 0; i < sizeList.size(); i++) {
            Camera.Size size = sizeList.get(i);
            int value = size.width * size.height;

            if (value >= min && value <= max) {
                previewSize = size;
                break;
            }
        }

        return previewSize;
    }

    @Override
    public int getCameraWidth() {
        return mCameraPreviewFrameSize.width;
    }

    @Override
    public int getCameraHeight() {
        return mCameraPreviewFrameSize.height;
    }

    @Override
    public int getCameraID() {
        return mCurrentCameraType;
    }
}
