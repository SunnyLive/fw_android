package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.GiftWallDto;

public interface IGiftWallView extends MvpView {
    void getGiftWall(GiftWallDto data);
}
