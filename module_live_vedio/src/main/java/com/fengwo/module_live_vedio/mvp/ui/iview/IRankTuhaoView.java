package com.fengwo.module_live_vedio.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_live_vedio.mvp.dto.RankTuhaoDto;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/19
 */
public interface IRankTuhaoView extends MvpView {

    void rankTuhaoDate(List<RankTuhaoDto> rankTuhaoDtos);
}
