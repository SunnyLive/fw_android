package com.fengwo.module_flirt.manager;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.Interfaces.IFlirtSocketManager;

/**
 * 文播socket路由
 *
 * @Author gukaihong
 * @Time 2021/2/1
 */
@Route(path = ArouterApi.FLIRT_SOCKET_MANAGER_ROUTE, name = "文播socket路由")
public class FlirtSocketManagerRoute implements IFlirtSocketManager {
    @Override
    public void connect(int uid) {
        FlirtSocketManager.getInstance().connect(uid);
    }

    @Override
    public void destroy() {
        FlirtSocketManager.getInstance().onDestroy();
    }

    @Override
    public void init(Context context) {

    }
}
