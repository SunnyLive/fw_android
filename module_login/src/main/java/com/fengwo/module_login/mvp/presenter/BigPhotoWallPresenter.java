package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.SignUtils;
import com.fengwo.module_login.mvp.dto.BankDto;
import com.fengwo.module_login.mvp.ui.iview.IBigPhotoWallView;
import com.fengwo.module_login.mvp.ui.iview.IBindcardView;
import com.fengwo.module_login.utils.UserManager;

import java.util.List;

public class BigPhotoWallPresenter extends BaseLoginPresenter<IBigPhotoWallView> {

    public void userDeletePhotos(int id) {
        service.userDeletePhotos(id)
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>(getView()) {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().deleteSuccess();
                            getView().toastTip("删除成功");
                        } else {
                            getView().toastTip("删除失败");
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip("删除失败");
                    }
                });

    }

    public void userUpdatePhotos(int id, String url) {
        service.useUpdatePhotos(new ParamsBuilder()
                .put("photoUrl", url)
                .put("id", id + "").build())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>(getView()) {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().updateSuccess();
                            getView().toastTip("更新成功");
                        } else {
                            getView().toastTip("更新失败");
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip("更新失败");
                    }
                });

    }

}
