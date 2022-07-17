package com.fengwo.module_vedio.mvp.presenter;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.VideoHomeShortModel;
import com.fengwo.module_vedio.mvp.ui.iview.IShortVedioView;

import java.util.Map;

public class ShortVideoPresenter extends BaseVideoPresenter<IShortVedioView> {

    public void getShortVideoList(String pageParams, String content) {
        service.getSearchShortVideoList(pageParams,content)
                .compose(handleResult())
                .subscribe(new LoadingObserver<BaseListDto<VideoHomeShortModel>>() {
                    @Override
                    public void _onNext(BaseListDto<VideoHomeShortModel> data) {
                        getView().setVideoList(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }
    public void getAlbumList(String pageParams, int albumId,int uid) {
        if (uid>0) {
            service.getAlbumListById(pageParams, albumId, 4, uid)//排序：0-点赞量 1-浏览量，2-评论量，3-时间倒序，4-时间升序
                    .compose(handleResult())
                    .subscribe(new LoadingObserver<BaseListDto<VideoHomeShortModel>>(getView()) {
                        @Override
                        public void _onNext(BaseListDto<VideoHomeShortModel> data) {
                            getView().setAlbumList(data);
                        }

                        @Override
                        public void _onError(String msg) {

                        }
                    });
        }else {
            service.getAlbumListById(pageParams, albumId, 4)//排序：0-点赞量 1-浏览量，2-评论量，3-时间倒序，4-时间升序
                    .compose(handleResult())
                    .subscribe(new LoadingObserver<BaseListDto<VideoHomeShortModel>>(getView()) {
                        @Override
                        public void _onNext(BaseListDto<VideoHomeShortModel> data) {
                            getView().setAlbumList(data);
                        }

                        @Override
                        public void _onError(String msg) {

                        }
                    });
        }
    }
    public void addLike(Map map) {
        addNet(service.addShortVideoLike(createRequestBody(map))
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        getView().setAddLike(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                })
        );
    }

    public void addPlayNum(int movieId) {
        addNet(service.addShortVideoPlayNem(movieId)
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        getView().addPlayNum(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                })
        );
    }


}
