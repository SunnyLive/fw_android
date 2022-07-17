//package com.fengwo.mobile.android;
//
//import android.annotation.TargetApi;
//import android.app.Application;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//
//import androidx.multidex.MultiDex;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.faceunity.ui.FURenderer;
//import com.fengwo.module_chat.utils.chat_new.GreenDaoManager;
//import com.fengwo.module_comment.utils.DownloadHelper;
//import com.fengwo.module_comment.utils.ImageLoader;
//import com.fengwo.module_comment.utils.L;
//import com.tencent.bugly.Bugly;
//import com.tencent.bugly.beta.Beta;
//import com.tencent.rtmp.TXLiveBase;
//import com.tencent.smtt.sdk.QbSdk;
//import com.tencent.tinker.entry.DefaultApplicationLike;
//import com.tencent.ugc.TXUGCBase;
//import com.umeng.commonsdk.UMConfigure;
//import com.umeng.socialize.PlatformConfig;
//
//import cn.jpush.android.api.JPushInterface;
//import io.reactivex.plugins.RxJavaPlugins;
//
//public class ApplicationLike extends DefaultApplicationLike {
//    {
//        PlatformConfig.setWeixin("wx0caf01020ad1af24", "ceefd573f88eef319b9cb676bd836078");
//    }
//
//    String licenceUrl = "http://license.vod2.myqcloud.com/license/v1/a943266ec227c9ab507bc9e221870337/TXLiveSDK.licence";
//    String licenseKey = "2a4d8e57bd01826327103482f29ff60e";
//    //短视频
//    String ugcLicenceUrl = "http://license.vod2.myqcloud.com/license/v1/a943266ec227c9ab507bc9e221870337/TXUgcSDK.licence";
//    String ugcKey = "2a4d8e57bd01826327103482f29ff60e";
//
//    public ApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
//        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
//        // 调试时，将第三个参数改为true
//        Bugly.init(getApplication(), "900029763", BuildConfig.DEBUG);
//        GreenDaoManager.initGreenDaoManager(getApplication());
//        if (com.fengwo.module_comment.BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
////            ARouter.openLog();     // 打印日志
////            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
//        }
//        ARouter.init(getApplication()); // 尽可能早，推荐在Application中初始化
//        TXLiveBase.getInstance().setLicence(getApplication(), licenceUrl, licenseKey);
//        TXUGCBase.getInstance().setLicence(getApplication(), ugcLicenceUrl, ugcKey);
//        //log打印（可删除）
////        TXLiveBase.setConsoleEnabled(BuildConfig.DEBUG);
////        TXLiveBase.setLogLevel(TXLiveConstants.LOG_LEVEL_DEBUG);
//
//        ImageLoader.init(getApplication());
////        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
//        UMConfigure.init(getApplication(), UMConfigure.DEVICE_TYPE_PHONE, "");
//        DownloadHelper.init(getApplication());
//        //Jpush
////        JPushInterface.setDebugMode(BuildConfig.DEBUG);
//        JPushInterface.init(getApplication());
//
//        //相芯美颜
//        initFaceUnity();
//        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
//
//            @Override
//            public void onViewInitFinished(boolean arg0) {
//                // TODO Auto-generated method stub
//                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                L.d("app", " onViewInitFinished is " + arg0);
//            }
//
//            @Override
//            public void onCoreInitFinished() {
//                // TODO Auto-generated method stub
//            }
//        };
//        //x5内核初始化接口
//        QbSdk.initX5Environment(getApplication(), cb);
//        // RxJava2设置全局错误处理
//        RxJavaPlugins.setErrorHandler(Throwable::printStackTrace);
//    }
//
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    @Override
//    public void onBaseContextAttached(Context base) {
//        super.onBaseContextAttached(base);
//        // you must install multiDex whatever tinker is installed!
//        MultiDex.install(base);
//
//        // 安装tinker
//        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
//        Beta.installTinker(this);
//
//    }
//
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
//        getApplication().registerActivityLifecycleCallbacks(callbacks);
//    }
//
//    /*faceuinity SDK*/
//    private void initFaceUnity() {
//        FURenderer.initFURenderer(getApplication());
//    }
//}
