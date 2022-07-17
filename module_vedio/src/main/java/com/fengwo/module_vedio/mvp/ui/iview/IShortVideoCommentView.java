package com.fengwo.module_vedio.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_vedio.mvp.dto.ShortVideoCommentDto;

import java.util.ArrayList;

/**
 * @author Zachary
 * @date 2019/12/30
 */
public interface IShortVideoCommentView extends IBaseVideoView {
    void setComment(ArrayList<ShortVideoCommentDto> records);

    void setSecondComment(int parentIndex, ArrayList<ShortVideoCommentDto> records);

    void likeComment(int parentIndex, int secondPosition);
}
