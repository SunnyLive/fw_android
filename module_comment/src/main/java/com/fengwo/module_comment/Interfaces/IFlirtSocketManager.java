package com.fengwo.module_comment.Interfaces;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * 获取到WenboSock
 *
 * @Author gukaihong
 * @Time 2021/2/1
 */
public interface IFlirtSocketManager extends IProvider {

    public void connect(int uid);
    public void destroy();
}
