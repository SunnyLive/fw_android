package com.fengwo.module_vedio.mvp.presenter;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_vedio.mvp.dto.SmallVideoMenuDto;
import com.fengwo.module_vedio.mvp.ui.iview.ISmallVedioMenuView;
import com.fengwo.module_vedio.mvp.ui.iview.ISmallVideoMenuView;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/1/9
 */
public class SmallVideoMenuPresenter extends BaseVideoPresenter<ISmallVideoMenuView> {

    public void getSmallVideoMenu(){
        addNet(
            service.getSmallVideoMenu("1,100")
                    .compose(io_main())
                    .compose(handleResult())
                    .subscribeWith(new LoadingObserver<BaseListDto<SmallVideoMenuDto>>() {
                        @Override
                        public void _onNext(BaseListDto<SmallVideoMenuDto> data) {
                            getView().setSmallVideoMenu(data);
                        }

                        @Override
                        public void _onError(String msg) {
                            getView().toastTip(msg);
                        }
                    })
        );
    }
}
