package com.fengwo.module_live_vedio.mvp.ui.iview;

import com.fengwo.module_comment.base.BannerBean;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.bean.ZhuboDto;

import java.util.List;

public interface ILivingHome extends MvpView {
    void initHeaderRv(List<ZhuboDto> records);

    void setBanner(List<BannerBean> result);

    void setZhuboList(List<ZhuboDto> data, int page);

    void setLoadmoreEnable(boolean b);
}
