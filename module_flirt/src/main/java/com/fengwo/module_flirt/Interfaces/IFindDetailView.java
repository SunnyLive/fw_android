package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_chat.mvp.model.bean.CommentModel;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_flirt.bean.FindDetailBean;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/9/24
 */
public interface IFindDetailView extends MvpView {

    void setData(FindDetailBean findDetailBean);

    void setCommentSuccess(CommentModel commentModel);

    void setComments(BaseListDto<CommentModel> data);

    void commentLike(int index);

    void cardLikeSuccess(int id);

    void getShareUrlSuccess(String url, int type, String imgUrl, String content);
}
