package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.bean.PersonNearBy;

import java.util.ArrayList;

/**
 * @Author BLCS
 * @Time 2020/4/24 18:15
 */
public interface INearbyView extends MvpView {
    void setNearByData(ArrayList<CityHost> records);
}
