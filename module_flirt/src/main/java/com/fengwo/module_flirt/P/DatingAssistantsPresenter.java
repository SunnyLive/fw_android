package com.fengwo.module_flirt.P;

import android.text.TextUtils;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_flirt.Interfaces.IDatingAssistantsView;
import com.fengwo.module_flirt.bean.AppointmentListBean;

import okhttp3.RequestBody;

/**
 * @Author BLCS
 * @Time 2020/5/6 10:11
 */
public class DatingAssistantsPresenter extends BaseFlirtPresenter<IDatingAssistantsView> {

    /**
     * 清空列表
     */
    public void clearList(){
        addNet(service.getDatingAssistantsClearList().compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                getView().setClearList(data);
            }
            @Override
            public void _onError(String msg) {
                if (!TextUtils.isEmpty(msg)){
                    getView().toastTip(msg);
                }
            }
        }));
    }
    /**
     * 获取列表
     */
    public void getList(int current){
        RequestBody build = new WenboParamsBuilder()
                .put("current", String.valueOf(current))
                .put("size", 20)
                .build();
        addNet(service.getAppointList(build).compose(handleResult()).subscribeWith(new LoadingObserver<BaseListDto<AppointmentListBean>>() {
            @Override
            public void _onNext(BaseListDto<AppointmentListBean> data) {
                getView().setAppointList(data.records);
            }

            @Override
            public void _onError(String msg) {
                if (!TextUtils.isEmpty(msg)){
                    getView().toastTip(msg);
                }
            }
        }));
    }

    /**
     * 主播接受/拒绝邀约
     * @param appointTimeId 预约记录ID
     * @param status 0：拒绝，1：接受
     */
    public void acceptAppointment(int appointTimeId,int messageId,int status){
        RequestBody build = new WenboParamsBuilder()
                .put("appointTimeId", String.valueOf(appointTimeId))
                .put("messageId",  String.valueOf(messageId))
                .put("status",  String.valueOf(status))
                .build();
        addNet(service.acceptAppointment(build).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                getView().acceptAppointment(data);
            }
            @Override
            public void _onError(String msg) {
                L.e("======"+msg);
                if (!TextUtils.isEmpty(msg)){
                    getView().toastTip(msg);
                }
            }
        }));
    }

    /**
     * 主播提醒/邀约
     */
    public void remindUser(int appointmentId,int messageId){
        RequestBody build = new WenboParamsBuilder()
                .put("appointmentId", appointmentId)
                .put("messageId", messageId)
                .build();
        addNet(service.remindUser(build).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                getView().remindUserSuccess(data);
            }
            @Override
            public void _onError(String msg) {
                L.e("======"+msg);
                if (!TextUtils.isEmpty(msg)){
                    getView().toastTip(msg);
                }
            }
        }));
    }

}
