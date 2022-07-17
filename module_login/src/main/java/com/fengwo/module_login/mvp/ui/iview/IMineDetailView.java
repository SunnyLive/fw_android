package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.LiveStatusDto;

import java.util.List;

public interface IMineDetailView extends MvpView {

    void greet(String content);

    void setGreetTips(List<String> dataList, int pages);

    void getLiveStatus(LiveStatusDto d);
}
