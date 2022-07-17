package com.fengwo.module_flirt.P;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.amap.api.location.AMapLocation;
import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.PostCardItem;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_flirt.Interfaces.IFlirtCerView;
import com.fengwo.module_flirt.bean.CerMsgBean;
import com.fengwo.module_flirt.bean.CerTagBean;
import com.fengwo.module_flirt.bean.MineTypeModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/28
 */
public class FlirtCerPresenter extends BaseFlirtPresenter<IFlirtCerView> {

    public void getCerMsg() {
        addNet(service.getCerMsg().compose(handleResult())
                .subscribeWith(new LoadingObserver<CerMsgBean>() {
                    @Override
                    public void _onNext(CerMsgBean data) {
                        getView().checkCerMsg(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }

    private void saveCerMsg(String bizId,int anchorId, long audioLength, String audioPath, String occuName, String tagName, List<PostCardItem> list) {
        RequestBody build = new WenboParamsBuilder()
                .put("anchorId", anchorId + "")
                .put("audioLength", audioLength + "")
                .put("audioPath", audioPath)
                .put("occuName", occuName)
                .put("tagName", tagName)
                .put("bizId", bizId)
                .put("resList", JSONArray.parseArray(JSON.toJSONString(list)))
                .build();
        addNet(service.saveCerMsg(build).compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        getView().hideLoadingDialog();
                        if (data.isSuccess()) {
                            getView().returnSaveCerMsg(data);
                        } else {
                            getView().toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().hideLoadingDialog();
                        getView().toastTip(msg);
                    }
                })
        );
    }

    public void upLoadAudio(String bizId,int anchorId, long audioLength, String audioPath, String occuName, String tagName, List<PostCardItem> items) {
        getView().showLoadingDialog();
        if (audioPath.contains("http")) {
            postCard(bizId,anchorId, audioLength, audioPath, occuName, tagName, items);
        } else {
            UploadHelper.getInstance(BaseApplication.mApp).doUpload(UploadHelper.TYPE_AUDIOS, new File(audioPath), new UploadHelper.OnUploadListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onLoading(long cur, long total) {
                    int p = (int) (1f * cur / total * 100);
                    L.e("=====p: " + p);
//                    getView().setDialogProgressPercent(p + "%");
                }

                @Override
                public void onSuccess(String url) {
                    postCard(bizId,anchorId, audioLength, url, occuName, tagName, items);
                }

                @Override
                public void onError() {
                    getView().hideLoadingDialog();
                }
            });
        }
    }

    public void postCard(String bizId,int anchorId, long audioLength, String audioPath, String occuName, String tagName, List<PostCardItem> items) {
        List<PostCardItem> newItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            PostCardItem postCardItem = items.get(i);
            postCardItem.anchorId = anchorId;
            postCardItem.coverImg = postCardItem.filePath;
//            postCardItem.rPath = TextUtils.isEmpty(items.get(i).videoPath) ? items.get(i).filePath : items.get(i).videoPath;
//            postCardItem.rType = postCardItem.isVideo ? 2 : 1;
            newItems.add(i, postCardItem);
        }
        uploadFile(bizId,anchorId, audioLength, audioPath, occuName, tagName, newItems, 0);
    }

    private void uploadFile(String bizId,int anchorId, long audioLength, String audioPath, String occuName, String tagName, List<PostCardItem> items, int index) {

        if (items.size() <= index) {
            saveCerMsg(bizId,anchorId, audioLength, audioPath, occuName, tagName, items);
        } else if (items.get(index).rPath.contains("http")) {
            uploadFile(bizId,anchorId, audioLength, audioPath, occuName, tagName, items, index + 1);
        } else {
            String path = TextUtils.isEmpty(items.get(index).videoPath) ? items.get(index).filePath : items.get(index).videoPath;
            File file = new File(path);
            String type;
            if (items.get(index).rType == 2) type = UploadHelper.TYPE_S_VIDEOS;
            else type = UploadHelper.TYPE_IMAGE;
            UploadHelper.getInstance(BaseApplication.mApp).doUpload(type, file, new UploadHelper.OnUploadListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onLoading(long cur, long total) {
                    int p = (int) (1f * cur / total * 100);
                    L.e("=====p: " + p);
//                    getView().setDialogProgressPercent(p + "%");
                }

                @Override
                public void onSuccess(String url) {
                    items.get(index).rPath = url;
                    uploadFile(bizId,anchorId, audioLength, audioPath, occuName, tagName, items, index + 1);
                }

                @Override
                public void onError() {
                    getView().hideLoadingDialog();
                }
            });
        }

    }
}
