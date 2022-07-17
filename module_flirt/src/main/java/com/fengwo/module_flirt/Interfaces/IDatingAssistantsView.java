package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_flirt.bean.AppointmentListBean;

import java.util.ArrayList;

/**
 * @Author BLCS
 * @Time 2020/5/6 10:12
 */
public interface IDatingAssistantsView extends MvpView {
    void setAppointList(ArrayList<AppointmentListBean> records);

    void acceptAppointment(HttpResult data);

    void remindUserSuccess(HttpResult data);

    void setClearList(HttpResult data);
}
