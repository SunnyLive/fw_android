package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.bean.PostCardItem;
import com.fengwo.module_flirt.bean.CerMsgBean;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/28
 */
public interface IFlirtCerView extends MvpView {

    void returnSaveCerMsg(HttpResult result);

    void checkCerMsg(CerMsgBean cerMsgBean);
}
