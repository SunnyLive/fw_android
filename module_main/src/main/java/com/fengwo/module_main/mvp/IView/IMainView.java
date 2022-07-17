package com.fengwo.module_main.mvp.IView;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_flirt.bean.ReconWbBean;
import com.fengwo.module_comment.bean.VensionDto;

public interface IMainView extends MvpView {

    void setVension(VensionDto vension);

    void reconnec(ReconWbBean data);
}
