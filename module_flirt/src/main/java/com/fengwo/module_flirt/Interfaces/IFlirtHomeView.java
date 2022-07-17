package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_flirt.bean.CerTagBean;
import com.fengwo.module_flirt.bean.LabelTalentDto;
import com.fengwo.module_flirt.bean.ZipLabelDto;

import java.util.List;

public interface IFlirtHomeView extends MvpView {
    void getZipLabel(List<ZipLabelDto> data);
}
