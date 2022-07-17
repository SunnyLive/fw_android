package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_live_vedio.mvp.dto.PopoDto;
import com.fengwo.module_login.mvp.dto.AllCarDto;
import com.fengwo.module_login.mvp.dto.MyCarDto;

import java.util.List;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/16
 */
public interface IMyCarView extends MvpView {

    void setMyCarData(MyCarDto myCarData);

    void setAllCarData(List<AllCarDto> allCarData);

    void buyCarReturn(HttpResult<PopoDto> httpResult);

    void openCarReturn(HttpResult httpResult);

    void getcarDetail(AllCarDto allCarDto);
}
