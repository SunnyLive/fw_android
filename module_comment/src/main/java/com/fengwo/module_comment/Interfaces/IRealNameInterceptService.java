package com.fengwo.module_comment.Interfaces;


import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface IRealNameInterceptService extends IProvider {


    /**
     *
     * 达人认证  主播认证的逻辑代码
     * 因为这个是跨模块的  需要这样调用
     * 逻辑代码在 commonUtils i撩模块里面
     *
     * @param c     上下文  里面会有弹框需要
     * @param type  目前 type == 1 表示是主播申请 2表示是达人申请
     * @param isSetting 是否是从setting进入的  目前同城资料设置需要 isSetting = true 其他的要看产品需求怎么跳转
     *
     */
    void showRealName(Context c,int type,boolean isSetting);

}
