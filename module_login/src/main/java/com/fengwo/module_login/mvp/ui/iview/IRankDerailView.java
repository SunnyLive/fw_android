package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.RankLevelDto;

public interface IRankDerailView extends MvpView {
    void setRankLevelData(RankLevelDto data);
}
