package com.fengwo.module_comment.base;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.faceunity.FURenderer;
import com.fengwo.module_comment.BuildConfig;
import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.R;
import com.fengwo.module_comment.event.UserOnlineEvent;
import com.fengwo.module_comment.helper.GlobalMsgHelper;
import com.fengwo.module_comment.utils.DownloadHelper;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.SPUtils1;
import com.fengwo.module_websocket.Url;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.smtt.sdk.QbSdk;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.plugins.RxJavaPlugins;


public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {
    {
        PlatformConfig.setWeixin("wx0caf01020ad1af24", "ceefd573f88eef319b9cb676bd836078");
    }
//    static {
//        System.loadLibrary("native-lib");
//    }

//    String licenceUrl = "http://license.vod2.myqcloud.com/license/v1/a943266ec227c9ab507bc9e221870337/TXLiveSDK.licence";
//    String licenseKey = "2a4d8e57bd01826327103482f29ff60e";
//    //短视频
//    String ugcLicenceUrl = "http://license.vod2.myqcloud.com/license/v1/a943266ec227c9ab507bc9e221870337/TXUgcSDK.licence";
//    String ugcKey = "2a4d8e57bd01826327103482f29ff60e";

    String licenceUrl = "http://license.vod2.myqcloud.com/license/v1/24a80f4d8d172e2d03f08addd009635b/TXLiveSDK.licence";
    String licenseKey = "0f149857557bf01ba59e27e1d57c3f24";
    //短视频http://license.vod2.myqcloud.com/license/v1/24a80f4d8d172e2d03f08addd009635b/TXLiveSDK.licence
    String ugcLicenceUrl = "http://license.vod2.myqcloud.com/license/v1/24a80f4d8d172e2d03f08addd009635b/TXUgcSDK.licence";
    String ugcKey = "0f149857557bf01ba59e27e1d57c3f24";

    public static Typeface textFace;

    // IWXAPI 是第三方app和微信通信的openApi接口
    private IWXAPI api;

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    public static BaseApplication mApp;


    @Override
    public void onCreate() {
        super.onCreate();
        //前后台切换判断逻辑
        registerActivityLifecycleCallbacks(this);
        mApp = this;
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
//            ARouter.openLog();     // 打印日志
//            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            int key_url = (int) SPUtils1.get(this, "KEY_URL", 0);
            Url.url(key_url);

        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
        GlobalMsgHelper.INSTANCE.initHelper(this);
        TXLiveBase.getInstance().setLicence(this, licenceUrl, licenseKey);
       // TXUGCBase.getInstance().setLicence(this, ugcLicenceUrl, ugcKey);
        closeAndroid10Dialog();
        //log打印（可删除）
//        TXLiveBase.setConsoleEnabled(BuildConfig.DEBUG);
//        TXLiveBase.setLogLevel(TXLiveConstants.LOG_LEVEL_DEBUG);

        ImageLoader.init(this);
//        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        DownloadHelper.init(this);
        //Jpush
//        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
        textFace = Typeface.createFromAsset(getAssets(), "font/iliaostyle.ttf");
        //微信
        regToWx();
        //相芯美颜
        initFaceUnity();
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                L.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
        // RxJava2设置全局错误处理
        RxJavaPlugins.setErrorHandler(Throwable::printStackTrace);


        //设置当前设备为开发设备
        CrashReport.setIsDevelopmentDevice(this, BuildConfig.DEBUG);
        Bugly.init(this, Constants.BUGLY_APP_ID, true);
    }

    /*faceuinity SDK*/
    private void initFaceUnity() {
        FURenderer.initFURenderer(this);
    }

    private void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, true);
        // 将应用的appId注册到微信
        api.registerApp(Constants.WX_APP_ID);
        //建议动态监听微信启动广播进行注册到微信
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 将该app注册到微信
                api.registerApp(Constants.WX_APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));

    }

    /**
     * Detected problems with API compatibility
     * 版本问题 视频播放会有个未知的弹框
     */
    public void closeAndroid10Dialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
//        MultiDex.install(base);
    }


    int i = 0;
    boolean isForeground = false;

    public boolean isForeground() {
        return isForeground;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        isForeground = true;
        if (i == 0) {
            Log.e("前后台切换", "onActivityStarted: 切换到前台");
            RxBus.get().post(new UserOnlineEvent(true));


        }
        i++;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Log.i("BaseApplication", "onActivityResumed: 位置" + activity.getLocalClassName());
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        i--;
        if (i == 0) {
            isForeground = false;
            RxBus.get().post(new UserOnlineEvent(false));
            Log.e("前后台切换", "onActivityStarted: 切换到后台");

        } else {
            isForeground = true;
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }
}
