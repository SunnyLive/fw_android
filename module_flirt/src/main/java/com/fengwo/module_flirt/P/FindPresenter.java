package com.fengwo.module_flirt.P;

import android.text.TextUtils;

import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_comment.MapLocationUtil;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_flirt.Interfaces.IFindView;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.FindHeaderDto;
import com.fengwo.module_flirt.bean.FindListDto;
import com.fengwo.module_flirt.bean.ShareUrlDto;

import java.util.List;

import okhttp3.RequestBody;

/**
 * @Author BLCS
 * @Time 2020/8/12 11:29
 */
public class FindPresenter extends BaseFlirtPresenter<IFindView> {
    /**
     *
     * 附近3个人头像
     */
    public void getHeaderInfo(){
        RequestBody build = new WenboParamsBuilder()
                .put("latitude", MapLocationUtil.getInstance().getLatitude())
                .put("longitude",MapLocationUtil.getInstance().getLongitude())
                .build();
        addNet(service.getHeaderInfo(build).compose(handleResult()).subscribeWith(new LoadingObserver<List<FindHeaderDto>>() {
            @Override
            public void _onNext(List<FindHeaderDto> data) {
                getView().getRequesHeadersSuccess(data);
            }

            @Override
            public void _onError(String msg) {
                if (!TextUtils.isEmpty(msg)){
                    getView().toastTip(msg);
                }
            }
        }));
    }

    /**
     * 发现列表
     */
    public void getFindList(int current,String city){
        RequestBody build = new ParamsBuilder()
                .put("city", city)
                .put("current", String.valueOf(current))
                .put("latitude",MapLocationUtil.getInstance().getLatitude())
                .put("longitude", MapLocationUtil.getInstance().getLongitude())
                .put("size","20")
                .build();
        FlirtApiService service = new RetrofitUtils().createApi(FlirtApiService.class);
        addNet(service.getFindList(build).compose(handleResult()).subscribeWith(new LoadingObserver<BaseListDto<FindListDto>>(getView()) {
            @Override
            public void _onNext(BaseListDto<FindListDto> data) {
                getView().getRequestFindListSuccess(data);
            }

            @Override
            public void _onError(String msg) {
                if (!TextUtils.isEmpty(msg)){
                    getView().toastTip(msg);
                }
            }
        }));
    }

    /**
     * 点赞
     * @param id
     * @param position
     */
    public void cardLike(int id,int position){
        ChatService service = new RetrofitUtils().createApi(ChatService.class);
        addNet(service.cardLike(""+id).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
            @Override
            public void _onNext(HttpResult data) {
                if (data.isSuccess()) getView().cardLikeSuccess(id, position);
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
