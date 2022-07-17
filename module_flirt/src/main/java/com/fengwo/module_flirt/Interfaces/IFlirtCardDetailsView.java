package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_comment.bean.CheckAnchorStatus;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.bean.ShareInfoBean;

import java.util.List;

/**
 * @Author BLCS
 * @Time 2020/4/27 18:12
 */
public interface IFlirtCardDetailsView extends MvpView {

    void onReceiveFlirtData(boolean isRefresh, List<CityHost> onlineFlirtData);

//    void setNearByData(ArrayList<CityHost> records);

//    void setPeriodPrice(AppointTimes records);

//    void setAppointmentSuccess(SureAppointmentBean data);

//    void setAppointmentFailure(String msg);

//    void setOrderListPrice(ArrayList<OrderListBean> records);
//
//    void getSurePayInfo(OrderIdBean records);
//
//    void getSurePayInfoFailure(String msg);

    void checkAnchorStatus(CheckAnchorStatus data, boolean isClick);

    void getAnchorInfoSuccess(int position, CityHost data);

    void getShareInfoSuccess(ShareInfoBean data);

    void setMyOrderList(List<MyOrderDto> data);

    void greet(String content);

    void setGreetTips(List<String> dataList, int pages);

    void checkAnchorStatusint(CheckAnchorStatus data, boolean isClick);
}
