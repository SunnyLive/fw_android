package com.fengwo.module_flirt.P;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_flirt.Interfaces.IPreStartWbView;
import com.fengwo.module_flirt.bean.CerTagBean;
import com.fengwo.module_flirt.bean.ReconWbBean;
import com.fengwo.module_flirt.bean.StartWBBean;
import com.fengwo.module_flirt.bean.TopicTagBean;

import okhttp3.RequestBody;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/27
 */
public class PreWbPresent extends BaseFlirtPresenter<IPreStartWbView>{

    public void startFlirt(String roomTitle){

        RequestBody build = new WenboParamsBuilder()
                .put("roomTitle", roomTitle)
                .build();
        addNet(service.startFlirt(build).compose(handleResult())
                .subscribeWith(new LoadingObserver<StartWBBean>() {
                    @Override
                    public void _onNext(StartWBBean data) {
                        getView().startWB(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }
    public void reconnec(){
        addNet(service.reconnec().compose(handleResult())
                .subscribeWith(new LoadingObserver<ReconWbBean>() {
                    @Override
                    public void _onNext(ReconWbBean data) {
                        getView().reconnec(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }

    public void rejectReconnect(){
        addNet(service.rejectReconnec().compose(io_main())
                        .subscribeWith(new LoadingObserver<HttpResult>() {
                            @Override
                            public void _onNext(HttpResult data) {
//                        getView().reconnec(data);
                            }

                            @Override
                            public void _onError(String msg) {
                                getView().toastTip(msg);
                            }
                        })
        );
    }
    public void getTopicTag(){
        RequestBody build = new WenboParamsBuilder()
                .put("current", 1)
                .put("size", 100)
                .build();
        addNet(service.getTopicTag(build).compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<TopicTagBean>>() {
                    @Override
                    public void _onNext(BaseListDto<TopicTagBean> data) {
                        getView().setTagList(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }



}
