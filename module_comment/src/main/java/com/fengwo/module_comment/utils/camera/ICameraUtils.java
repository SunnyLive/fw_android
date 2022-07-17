package com.fengwo.module_comment.utils.camera;

import android.graphics.SurfaceTexture;

/**
 * Created by PC-Xu on 2017/3/16.
 */

public interface ICameraUtils
{
    public  int getCameraWidth();
    public  int getCameraHeight();
    public  int getCameraID();
    public  int getCurrentOrientation();
    public  void switchCamera();
    public  void startCamera();
    public  void stopCamera();
    public  void startCameraPreview(SurfaceTexture surfaceTexture);
}
