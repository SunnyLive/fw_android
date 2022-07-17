package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.bean.CardTagModel;
import com.fengwo.module_flirt.bean.CerTagBean;

import java.util.List;

public interface ICerTagView extends MvpView {

    void setAllTag(BaseListDto<CerTagBean> data);
}
