package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.LiveLengthDto;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/30
 */
public interface ILiveTimeView extends MvpView {

    void returnLiveTimeDate(LiveLengthDto liveLengthDto);

    void returnAllTime(HttpResult httpResult);
}
