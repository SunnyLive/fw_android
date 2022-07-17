package com.fengwo.module_chat.mvp.presenter;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.fengwo.module_chat.mvp.model.bean.CardDetailBean;
import com.fengwo.module_chat.mvp.model.bean.PostCardBean;
import com.fengwo.module_chat.mvp.model.bean.PostCardDraftModel;
import com.fengwo.module_comment.bean.CardTagModel;
import com.fengwo.module_comment.bean.PostCardItem;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_chat.mvp.ui.contract.IPostCardView;
import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.CacheDiskUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.UploadHelper;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostCardPresenter extends BaseChatPresenter<IPostCardView> {
    private static final String TAG = "PostCardPresenter";
    private int mUpType = 1; // 1 表示图片 2 表示视频 3 表示文本
    public void postCard(int cardId, String circleId, String desc, String tagIds, AMapLocation location, List<PostCardItem> items, boolean isDraft) {
/*        if (items.get(0).isVideo) {//视频上传 增加封面上传
            PostCardItem postCardItem = items.get(0);
            items.add(0, new PostCardItem(postCardItem.filePath, postCardItem.width, postCardItem.height, false));
        }*/
        PostCardItem pi = items.get(0);
        if (pi.isVideo) {//视频上传 增加封面上传
            mUpType = 2;
        }else {
            mUpType = 1;
        }
        items.add(0, new PostCardItem(pi.filePath, pi.width, pi.height, false));
        uploadFile(cardId, items, 0, circleId, desc, tagIds, location, isDraft);
    }

    private void uploadFile(int cardId, List<PostCardItem> items, final int index, String circleId, String desc, String tagIds, AMapLocation location, boolean isDraft) {
        getView().showLoadingDialog();
        if (items.size() <= index) {
            doPost(cardId, items, circleId, desc, tagIds, location, isDraft);
        } else {
            String path = TextUtils.isEmpty(items.get(index).videoPath) ? items.get(index).filePath : items.get(index).videoPath;
            File file = new File(path);
            String type;
            if (items.get(index).isVideo) type = UploadHelper.TYPE_S_VIDEOS;
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
                    items.get(index).fileUrl = url;
                    uploadFile(cardId, items, index + 1, circleId, desc, tagIds, location, isDraft);
                }

                @Override
                public void onError() {
                    getView().hideLoadingDialog();
                }
            });
        }
    }

    private void doPost(int cardId, List<PostCardItem> items, String circleId, String desc, String tagIds, AMapLocation location, boolean isDraft) {
        ArrayList<PostCardBean> list = new ArrayList<>();

/*        for (int i = 0; i < items.size(); i++) {
            PostCardItem item = items.get(i);
            if (item.isVideo) type = 2;
            list.add(new PostCardBean(item.height, item.fileUrl, item.fileUrl, i == 0 ? 1 : 0,i == 0 ? 1 : 0, item.width));
        }
        if (type == 1) {
            PostCardBean bean = list.get(0);
            PostCardBean newBean = new PostCardBean(bean.high, bean.imageUrl, bean.imageUrl, 0,0, bean.width);
            list.add(1, newBean);
        }*/
        String imgUrl = "";
        for (int i = 0; i < items.size(); i++) {
            PostCardItem pi = items.get(i);
            //typeNew	integer($int32) 类型：0图片，1封面 2视频
            //   map.put("type", String.valueOf(type));   type  2表示视频 1表示图片
            //设置封面
            if (i == 0) {
                list.add(new PostCardBean(pi.height, imgUrl = pi.fileUrl, "", 1, 1, pi.width));
            } else {
                if (pi.isVideo) {
                    list.add(new PostCardBean(pi.height, pi.fileUrl, imgUrl, 2, 0, pi.width));
                } else {
                    list.add(new PostCardBean(pi.height, pi.fileUrl, "", 0, 0, pi.width));
                }
            }
        }


        HashMap<String, Object> map = new HashMap<>();
        map.put("circleId", circleId);
        map.put("excerpt", desc);
        if (location != null) {
            map.put("latitude", String.valueOf(location.getLatitude()));
            map.put("longitude", String.valueOf(location.getLongitude()));
            map.put("position", location.getCity());
        }
        map.put("tagIds", tagIds);
        map.put("cover", list);
        if (cardId > 0) {
            map.put("id", cardId);
        }
        map.put("type", mUpType);
        map.put("formType", 0);
        map.put("isDraft", isDraft ? 1 : 0);//是否存草稿 0 直接发布 1 存草稿 默认 0
        service.postCard(createRequestBody(map)).compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        getView().hideLoadingDialog();
                        if (data.isSuccess()) {
                            getView().postCardSuccess(isDraft);
                            clearCache();
                        } else {
                            getView().toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().hideLoadingDialog();
                        getView().toastTip(msg);
                    }
                });
    }

    // 保存数据到草稿箱
    public void saveToCache(List<PostCardItem> items, RecommendCircleBean category, String content, String tagName, String tagIds) {
        PostCardDraftModel draftModel = new PostCardDraftModel();
        draftModel.cardCategory = category;
        draftModel.content = content;
        draftModel.items = items;
        draftModel.tagName = tagName;
        draftModel.tagIds = tagIds;

        CacheDiskUtils draft = CacheDiskUtils.getInstance("draft");
        draft.remove("draft");
        draft.put("draft", draftModel);
    }

    // 从cache中读取对象
    public PostCardDraftModel getDraftFromCache() {
        return (PostCardDraftModel) CacheDiskUtils.getInstance("draft").getSerializable("draft");
    }

    // 清除草稿箱
    public void clearCache() {
        CacheDiskUtils.getInstance("draft").remove("draft");
    }

    public void getCardDetail(int id) {
        service.getCardDetail(id)
                .compose(handleResult())
                .subscribeWith(new LoadingObserver<CardDetailBean>() {
                    @Override
                    public void _onNext(CardDetailBean data) {
                        getView().getCardDetail(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                });

    }

    public void doPost(int cardId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", cardId);
        service.cardDraftPost(createRequestBody(map)).compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        getView().hideLoadingDialog();
                        if (data.isSuccess()) {
                            getView().postCardSuccess(false);
                        } else {
                            getView().toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().hideLoadingDialog();
                        getView().toastTip(msg);
                    }
                });
    }

    public void getTagList(int circleId) {
        addNet(service.getTagList(circleId).compose(io_main()).compose(handleResult())
                .subscribeWith(new LoadingObserver<List<CardTagModel>>() {
                    @Override
                    public void _onNext(List<CardTagModel> data) {
                        getView().setAllTag(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }

}