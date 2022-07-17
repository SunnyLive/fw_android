package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.MineCardDto;
import com.fengwo.module_login.mvp.dto.ShareUrlDto;

public interface IMineCardView extends MvpView {

    void success(MineCardDto data);
    void fail(String message);
    void error(String error);



    void resultShareInfo(int cardId,ShareUrlDto i);
    void failShareInfo(String f);
    void errorShareInfo(String e);

    void resultCardLike(int cardId);
    void failCardLike(String f);
    void errorCardLike(String e);

    /**
     * 置顶操作
     * @param isStick isStick
     */
    void resultStickSuccess(boolean isStick);
    void resultStickFail(String f);

    /**
     * 权限操作
     * @param isAuthor isAuthor
     */
    void resultAuthoritySuccess(boolean isAuthor);
    void resultAuthorityFail(String f);

}
