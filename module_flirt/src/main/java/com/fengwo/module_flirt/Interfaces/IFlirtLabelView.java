package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.bean.LabelTalentDto;

import java.util.ArrayList;

public interface IFlirtLabelView extends MvpView {

    void setLabelTalent(BaseListDto<LabelTalentDto> data);
}
