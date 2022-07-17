package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.HuafenLevelDto;
import com.fengwo.module_login.mvp.dto.PrivilegeDto;
import com.fengwo.module_login.mvp.dto.TaskDto;

import java.util.List;

public interface IHuafenView extends MvpView {
    void setLevle(HuafenLevelDto data);

    void setTaskList(List<TaskDto> data);

    void setPrivelege(List<PrivilegeDto> data);
}
