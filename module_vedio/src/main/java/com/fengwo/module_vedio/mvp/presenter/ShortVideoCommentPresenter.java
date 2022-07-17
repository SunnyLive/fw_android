package com.fengwo.module_vedio.mvp.presenter;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_vedio.mvp.dto.ShortVideoCommentDto;
import com.fengwo.module_vedio.mvp.ui.iview.IShortVideoCommentView;

import java.util.HashMap;

/**
 * @author Zachary
 * @date 2019/12/30
 */
public class ShortVideoCommentPresenter extends BaseVideoPresenter<IShortVideoCommentView> {

    public void getCommentList(String movieId, String pageParam) {
        service.getShortVideoComment(movieId, pageParam, "", "1")
                .compose(io_main()).compose(handleResult()).subscribe(new LoadingObserver<BaseListDto<ShortVideoCommentDto>>() {
            @Override
            public void _onNext(BaseListDto<ShortVideoCommentDto> data) {
                getView().setComment(data.records);
            }

            @Override
            public void _onError(String msg) {
                getView().toastTip(msg);
            }
        });
    }

    public void getSecondCommentList(String movieId, String pageParam, int parentId, int position) {
        service.getShortVideoComment(movieId, pageParam, String.valueOf(parentId), "2")
                .compose(io_main()).compose(handleResult()).subscribe(new LoadingObserver<BaseListDto<ShortVideoCommentDto>>() {
            @Override
            public void _onNext(BaseListDto<ShortVideoCommentDto> data) {
                getView().setSecondComment(position, data.records);
            }

            @Override
            public void _onError(String msg) {
                getView().toastTip(msg);
            }
        });
    }

    public void likeComment(int parentIndex, int secondPosition, String commentId, String movieId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("commentId", commentId);
        map.put("movieId", movieId);
        map.put("type", "1");
        service.likeVideo(createRequestBody(map)).compose(io_main()).subscribe(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                if (data.isSuccess()) getView().likeComment(parentIndex, secondPosition);
                else getView().toastTip(data.description);
            }

            @Override
            public void _onError(String msg) {
                getView().toastTip(msg);
            }
        });
    }
}
