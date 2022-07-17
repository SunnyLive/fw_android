package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_flirt.bean.ReconWbBean;
import com.fengwo.module_flirt.bean.StartWBBean;
import com.fengwo.module_flirt.bean.TopicTagBean;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/27
 */
public interface IPreStartWbView extends MvpView {

    void startWB(StartWBBean startWBBean);

    void setTagList(BaseListDto<TopicTagBean> list);


    void reconnec(ReconWbBean data);
}
