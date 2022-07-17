package com.fengwo.module_live_vedio.mvp.ui.iview;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_live_vedio.mvp.dto.RankSinglePkDto;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/19
 */
public interface IRankPkView extends MvpView {

    void singlePk(List<RankSinglePkDto> singlePkDto);

    void teamPk(List<RankSinglePkDto> teamPkDtp);

    void guildPk(List<RankSinglePkDto> guildPkDto);

    void addAttention(HttpResult httpResult);

    void removeAttention(HttpResult httpResult);

    void finishRefresh();
}
