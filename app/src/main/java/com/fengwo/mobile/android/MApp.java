package com.fengwo.mobile.android;

import android.content.Context;

import com.fengwo.module_chat.utils.chat_new.GreenDaoManager;
import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_live_vedio.helper.LiveNoticeHelper;
import com.scwang.smart.refresh.footer.ClassicsFooter;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;

import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;

import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;;

import com.scwang.smart.refresh.header.ClassicsHeader;

public class MApp extends BaseApplication {

//yw_1222_0670 阿里的图片识别签名
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

    @Override
    public void onCreate() {
        super.onCreate();
        GreenDaoManager.initGreenDaoManager(this);
        LiveNoticeHelper.INSTANCE.initHelper(this);
    }
}
