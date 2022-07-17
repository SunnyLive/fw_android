package com.fengwo.module_vedio.mvp.presenter;

import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_vedio.mvp.dto.ShortVedioListDto;
import com.fengwo.module_vedio.mvp.ui.iview.IShortVedioMenuView;

import java.util.HashMap;
import java.util.Map;

public class ShortVedioMenuPresenter extends BaseVideoMenuPresenter<IShortVedioMenuView> {

    public static final int SHORT_TYPE_HOT = 0;
    public static final int SHORT_TYPE_COMMENT = 1;
    public static final int SHORT_TYPE_TIME = 2;



    public void getShortVedio(int type, int page) {
        Map<String, String> m = new HashMap<>();
        m.put("approvalStatus", 5 + "");//审核状态:1平台审核中，2平台审核通过（主题人审核中），3平台退回，4主题审核失败，5主题审核通过
        m.put("isPrivacy", "0");
        m.put("order", type + "");
        m.put("status", 0 + "");
        m.put("current", page + "");
        m.put("size", 12 + "");
        service.getShortVedioList(createRequestBody(m))
                .compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<ShortVedioListDto>() {
                    @Override
                    public void _onNext(ShortVedioListDto data) {
                        switch (type) {
                            case SHORT_TYPE_HOT:
                                getView().setHotShortVedio(data.records);
                                break;
                            case SHORT_TYPE_COMMENT:
                                getView().setLikeShortVedio(data.records);
                                break;
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });

    }
}
