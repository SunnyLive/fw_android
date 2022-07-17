package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.bean.FindDetailBean;
import com.fengwo.module_flirt.bean.FindListDto;

import java.util.ArrayList;
import java.util.List;

public interface IFlirtTalentView extends MvpView {
    void setOnLineList(ArrayList<CityHost> records);

    void getRequestFindListSuccess(BaseListDto<FindListDto> data);

    void cardLikeSuccess(int id, int position);

    void getShareUrlSuccess(String url, int type, String imgUrl, String content);

    void onReceiveFlirtData(boolean isRefresh, List<CityHost> onlineFlirtData);

    void setMyOrderList(List<MyOrderDto> data);

    void onGetFindDetail(int id, FindDetailBean detail, int position);
}
