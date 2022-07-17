package com.fengwo.module_flirt.P;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.fengwo.module_chat.mvp.model.bean.CardDetailBean;
import com.fengwo.module_chat.mvp.model.bean.PostCardBean;
import com.fengwo.module_chat.mvp.model.bean.PostCardDraftModel;
import com.fengwo.module_chat.mvp.model.bean.RecommendCircleBean;
import com.fengwo.module_chat.mvp.presenter.BaseChatPresenter;
import com.fengwo.module_comment.base.BaseApplication;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.CardTagModel;
import com.fengwo.module_comment.bean.PostCardItem;
import com.fengwo.module_comment.utils.CacheDiskUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_flirt.Interfaces.IPostTrendView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostTrendPresenter extends BaseChatPresenter<IPostTrendView> {
    private static final String TAG = "PostCardPresenter";
    private int mCardId = -1;      //动态id
    private String mCircleId = ""; //卡片id
    private String mDesc = "";    //排序
    private String mTagIds = "";
    private AMapLocation mLocation = null; //位置数据
    private List<PostCardItem> mItems = null;  //资源
    private boolean mIsDraft = false; //是否保存草稿
    private int mIndex = 0;
    private int mUpType = 1; // 1 表示图片 2 表示视频 3 表示文本

    public void postCard(int cardId,      //动态id
                         String circleId, //卡片id
                         String desc,     //排序
                         String tagIds,
                         AMapLocation location, //位置数据
                         List<PostCardItem> items,  //资源
                         boolean isDraft) {

        this.mCardId = cardId;
        this.mCircleId = circleId;
        this.mDesc = desc;
        this.mTagIds = tagIds;
        this.mLocation = location;
        this.mItems = items;
        this.mIsDraft = isDraft;
        this.mIndex = 0;
        //是否需要上传视频或者图片
        if (items != null && items.size() > 0) {
            //首先赋值为1 表示这里默认为有图片
            mUpType = 1;
            PostCardItem pi = items.get(0);
            //如果有视频  就改变type的值 表示上传的为视频
            if (pi.isVideo) {//视频上传 增加封面上传
                mUpType = 2;
            }
            items.add(0, new PostCardItem(pi.filePath, pi.width, pi.height, false));
            uploadFile();
        } else {
            //表示的是纯文字 没有文件 直接请求服务器
            mUpType = 1;
            doPost();
        }
    }

    private void uploadFile() {
        getView().showLoadingDialog();
        if (mItems.size() <= mIndex) {
            doPost();
        } else {
            String path = TextUtils.isEmpty(mItems.get(mIndex).videoPath) ? mItems.get(mIndex).filePath : mItems.get(mIndex).videoPath;
            // 如果已经上传成功，则无需重新上传
            if (path.startsWith("http") || path.startsWith("https")) {
                PostCardItem pi = mItems.get(mIndex);
                pi.fileUrl = path;
                ++mIndex;
                uploadFile();
            } else {
                File file = new File(path);
                String type;
                if (mItems.get(mIndex).isVideo)
                    type = UploadHelper.TYPE_S_VIDEOS;
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
                        PostCardItem pi = mItems.get(mIndex);
                        pi.fileUrl = url;
                        ++mIndex;
                        uploadFile();
                    }

                    @Override
                    public void onError() {
                        getView().hideLoadingDialog();
                    }
                });
            }
        }
    }

    String imgUrl = "";

    private void doPost() {
        ArrayList<PostCardBean> list = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(mCircleId)) {
            map.put("circleId", mCircleId);
        }

        for (int i = 0; i < mItems.size(); i++) {
            PostCardItem pi = mItems.get(i);
            //typeNew	integer($int32)
            //类型：0图片，1封面 2视频
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
        map.put("cover", list);
        map.put("excerpt", mDesc);
        map.put("formType", 1);    // 0 卡片动态 1 发现动态
        if (mLocation != null) {
            map.put("latitude", String.valueOf(mLocation.getLatitude()));
            map.put("longitude", String.valueOf(mLocation.getLongitude()));
            map.put("position", mLocation.getCity());
        }
        if (mCardId > 0) {
            map.put("id", mCardId);
        }
        map.put("type", mUpType);
        map.put("isDraft", mIsDraft ? 1 : 0);//是否存草稿 0 直接发布 1 存草稿 默认 0
        service.postCard(createRequestBody(map)).compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<CardDetailBean>>() {
                    @Override
                    public void _onNext(HttpResult<CardDetailBean> data) {
                        getView().hideLoadingDialog();
                        if (data.isSuccess()) {
                            getView().postCardSuccess(mIsDraft, data.data);
                            clearCache();
                        } else {
                            getView().toastTip(data.description);
                            // 删除掉封面
                            mItems.remove(0);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().hideLoadingDialog();
                        L.d("yang","--------------------------");
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
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult<CardDetailBean>>() {
                    @Override
                    public void _onNext(HttpResult<CardDetailBean> data) {
                        if (data.isSuccess()) {
                            getView().getCardDetail(data.data);
                        }
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
                            getView().postCardSuccess(false,null);
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