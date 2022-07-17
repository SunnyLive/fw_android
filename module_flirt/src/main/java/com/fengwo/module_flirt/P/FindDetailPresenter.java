package com.fengwo.module_flirt.P;

import android.graphics.pdf.PdfDocument;
import android.text.TextUtils;

import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_chat.mvp.model.bean.CommentModel;
import com.fengwo.module_chat.mvp.ui.event.CommentRefreshEvent;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.KeyBoardUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.Interfaces.ICerTagView;
import com.fengwo.module_flirt.Interfaces.IFindDetailView;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.CerTagBean;
import com.fengwo.module_flirt.bean.FindDetailBean;
import com.fengwo.module_flirt.bean.ShareUrlDto;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class FindDetailPresenter extends BaseFlirtPresenter<IFindDetailView> {

    public void getDetailData(int id) {
        addNet(new RetrofitUtils().createApi(FlirtApiService.class).getFindDetail(id).compose(handleResult())
                .subscribeWith(new LoadingObserver<FindDetailBean>() {

                    @Override
                    public void _onNext(FindDetailBean data) {
                        getView().setData(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }
    public void sendComment(int cardId,String content) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cardId", cardId+"");
        map.put("content", content);
        map.put("parentId", 0);
        map.put("secondType", 0);
        map.put("secondUserId", 0);
        map.put("type", 1);
        String json = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        addNet(new RetrofitUtils().createApi(ChatService.class).comment(requestBody).compose(RxUtils.applySchedulers())
                .subscribeWith(new LoadingObserver<HttpResult<CommentModel>>() {
                    @Override
                    public void _onNext(HttpResult<CommentModel> data) {
                        if (data.isSuccess()) {
                            getView().setCommentSuccess(data.data);
                        } else {
                            getView().toastTip(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                })
        );
    }

    public void getComments(String id, String pageParams) {
        addNet(new RetrofitUtils().createApi(ChatService.class).getCommentList(Integer.parseInt(id), pageParams).compose(RxUtils.applySchedulers2())
                .subscribeWith(new LoadingObserver<HttpResult<BaseListDto<CommentModel>>>() {
                    @Override
                    public void _onNext(HttpResult<BaseListDto<CommentModel>> data) {
                        if (data.isSuccess()) {
                            getView().setComments(data.data);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                }));
    }

    public void likeComment(int parentIndex, int index, int id) {
        addNet(new RetrofitUtils().createApi(ChatService.class).likeComment(id).compose(RxUtils.applySchedulers2())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            getView().commentLike(parentIndex);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                       getView().toastTip(msg);
                    }
                }));
    }

    /**
     * 点赞
     * @param id
     *
     */
    public void cardLike(int id){
        ChatService service = new RetrofitUtils().createApi(ChatService.class);
        addNet(service.cardLike(""+id).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                if (data.isSuccess()) getView().cardLikeSuccess(id);
                else getView().toastTip(data.description);
            }

            @Override
            public void _onError(String msg) {
                getView().toastTip(msg);
            }
        }));
    }

    /**
     * 获取分享信息
     * @param id
     * @param type
     * @param imgUrl
     * @param content
     */
    public void getShareInfo(int id,int type,String imgUrl,String content){
        addNet( new RetrofitUtils().createApi(FlirtApiService.class).getShareInfo(id).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult<ShareUrlDto>>() {
            @Override
            public void _onNext(HttpResult<ShareUrlDto> data) {
                getView().getShareUrlSuccess(data.data.getUrl(),type,imgUrl,content);
            }

            @Override
            public void _onError(String msg) {
                if (!TextUtils.isEmpty(msg)){
                    getView().toastTip(msg);
                }
            }
        }));
    }
}
