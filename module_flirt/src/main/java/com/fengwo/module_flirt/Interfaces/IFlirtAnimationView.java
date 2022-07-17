package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_flirt.bean.LabelTalentDto;
import com.fengwo.module_flirt.bean.ZipLabelDto;
import com.fengwo.module_flirt.bean.ZipLabelParentDto;

import java.util.List;

public interface IFlirtAnimationView extends MvpView {
    void onReceiveLoadMore(List<LabelTalentDto> data);

    void getZipLabel(ZipLabelParentDto data);
}
