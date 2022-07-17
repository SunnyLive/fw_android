package com.fengwo.module_login.mvp.presenter;

import android.content.Context;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_login.mvp.ui.iview.IRealAnchorView;

import java.io.File;

public class RealAnchorPresenter extends BaseLoginPresenter<IRealAnchorView> {



    public void postAnchorIDCard(String bizId,String idCardUrl,String heardUrl){
        service.postAnchorIDCard(new HttpUtils.ParamsBuilder()
                .put("cardImg", idCardUrl)
                .put("bizId", bizId)
                .put("imagePhoto", heardUrl)
                .build())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (getView() == null) return;
                        if (data.status.equals("OK")) {
                            getView().onPostAnchorIDCardSuccess((String) data.data);
                        } else {
                            getView().onPostAnchorIDCardError(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        if (getView() == null) return;
                        getView().onPostAnchorIDCardError(msg);
                    }
                });


    }


    /**
     *
     *
     * 上传图片操作
     *
     * @param context
     * @param f  文件地址
     * @param type  身份证还是形象照片
     */
    public void requestUpLoad(Context context, File f,int type){
        getView().showLoadingDialog();
        UploadHelper.getInstance(context).doUpload(UploadHelper.TYPE_IMAGE, f, new UploadHelper.OnUploadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onLoading(long cur, long total) {
                int p = (int) (1f * cur / total * 100);
                getView().setDialogProgressPercent(p + "%");
            }

            @Override
            public void onSuccess(String url) {
                getView().hideLoadingDialog();
                getView().onUpLoadSuccess(url,type);
            }

            @Override
            public void onError() {
                getView().toastTip("上传失败，请重新上传");
                getView().hideLoadingDialog();
            }
        });


    }

}
