package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.ReceiveGiftDto;

public interface IReceiveGiftView extends MvpView {
    void receiveGift(ReceiveGiftDto data);
}
