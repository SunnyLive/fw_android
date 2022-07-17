//package com.fengwo.module_vedio.widget;
//
//import android.content.Context;
//import android.util.AttributeSet;
//
//import com.fengwo.module_vedio.R;
//import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
//
//public class EmptyGsyVideoPlayer extends StandardGSYVideoPlayer {
//
//    public EmptyGsyVideoPlayer(Context context, Boolean fullFlag) {
//        super(context, fullFlag);
//    }
//
//    public EmptyGsyVideoPlayer(Context context) {
//        super(context);
//    }
//
//    public EmptyGsyVideoPlayer(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.vedio_empty_control_vedio;
//    }
//
//    @Override
//    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
//        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
//        //不给触摸快进，如果需要，屏蔽下方代码即可
//        mChangePosition = false;
//
//        //不给触摸音量，如果需要，屏蔽下方代码即可
//        mChangeVolume = false;
//
//        //不给触摸亮度，如果需要，屏蔽下方代码即可
//        mBrightness = false;
//    }
//
//    @Override
//    protected void touchDoubleUp() {
//        //super.touchDoubleUp();
//        //不需要双击暂停
//    }
//}
