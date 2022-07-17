package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.MCD_CommentDto;
import com.fengwo.module_login.mvp.dto.MineCardDetailDto;
import com.fengwo.module_login.mvp.dto.ShareUrlDto;

public interface IMineCardDetailView extends MvpView {

    /**
     * 获取动态信息
     * @param d d
     */
    void resultDataSuccess(MineCardDetailDto d);
    void resultDataFail(String f);


    /**
     * 获取评论信息
     * @param d d
     */
    void resultCommentsSuccess(BaseListDto<MCD_CommentDto> d);
    void resultCommentsFail(String f);

    /**
     * 发送评论
     * @param d d
     */
    void sendCommentResultSuccess(MCD_CommentDto d);
    void  sendCommentResultFail(String f);

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

    /**
     * 分享操作
     * @param i
     */
    void resultShareSuccess(ShareUrlDto i);
    void resultShareFail(String f);

    /**
     * 点赞操作
     */
    void resultCardLikeSuccess();
    void resultCardLikeFail(String f);


    /**
     *
     * 服务器返回异常
     * @param e error
     */
    void requestError(String e);

    /**
     * 评论点赞骚操作
     * @param position position
     */
    void resultCommentLike(int position);
}
