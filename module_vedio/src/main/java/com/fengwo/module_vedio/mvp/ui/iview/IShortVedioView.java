package com.fengwo.module_vedio.mvp.ui.iview;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.bean.VideoHomeShortModel;

import java.util.List;

public interface IShortVedioView extends IBaseVideoView {

    void setVideoList(BaseListDto<VideoHomeShortModel> baseListDt);

    void setAlbumList(BaseListDto<VideoHomeShortModel> baseListDt);

    void setAddLike(HttpResult httpResult);

    void addPlayNum(HttpResult httpResult);
}
