package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_flirt.bean.CityHost;

import java.util.ArrayList;

/**
 * @Author BLCS
 * @Time 2020/4/27 12:02
 */
public interface IVideoDatingView extends MvpView {

    void setData(ArrayList<CityHost> records);
}
