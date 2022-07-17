package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_chat.mvp.model.bean.AdvertiseBean;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_flirt.bean.CerTagBean;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_flirt.bean.TopicTagBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author BLCS
 * @Time 2020/4/24 12:27
 */
public interface IFlirtView  extends MvpView {
    void setBannerData(ArrayList<AdvertiseBean> records);

    void setMyOrderList(List<MyOrderDto> data);
    void getLabelSuccess(List<CerTagBean> data);
}
