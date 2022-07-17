package com.fengwo.module_live_vedio.mvp.ui.iview;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_live_vedio.mvp.dto.ChannelShareInfoDto;
import com.fengwo.module_live_vedio.mvp.dto.StartLivePushDto;
import com.fengwo.module_live_vedio.mvp.dto.ZhuboMenuDto;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

public interface IStartView extends MvpView {
    void setMenu(List<ZhuboMenuDto> records);
    void startLivePush(StartLivePushDto startLivePushDto);
    void share(ChannelShareInfoDto data, SHARE_MEDIA shareMedia);
    void connectionLine(HttpResult<StartLivePushDto> startLivePushDto);
    void onLiveConnection();
    void onLiveReStart();
}
