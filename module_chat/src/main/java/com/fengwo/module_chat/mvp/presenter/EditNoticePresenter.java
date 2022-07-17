package com.fengwo.module_chat.mvp.presenter;

import com.fengwo.module_chat.mvp.ui.contract.IEditNoticeView;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;

import java.util.HashMap;

public class EditNoticePresenter extends BaseChatPresenter<IEditNoticeView> {

    public void editNotice(int groupId, String content) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", content);
        map.put("id", groupId);
        addNet(service.editNotice(createRequestBody(map)).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().editNoticeSuccess(content);
                        } else getView().toastTip(data.description);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }
}
