package com.fengwo.module_flirt.P;

import android.text.TextUtils;

import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_comment.MapLocationUtil;
import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.bean.MyOrderDto;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_flirt.Interfaces.IFlirtTalentView;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.bean.FindDetailBean;
import com.fengwo.module_flirt.bean.FindListDto;
import com.fengwo.module_flirt.bean.ShareUrlDto;

import java.util.List;

import okhttp3.RequestBody;


public class FlirtTalentPresenter extends BaseFlirtPresenter<IFlirtTalentView> {
    private int mPageIndex = 1;

    /**
     * 发现列表
     */
    public void getFindList(int current, String city, String maxAge, String minAge, String sex) {
        RequestBody build = new ParamsBuilder()
                .put("city", TextUtils.isEmpty(city) ? "全部" : city)
                .put("current", String.valueOf(current))
                .put("latitude", MapLocationUtil.getInstance().getLatitude())
                .put("longitude", MapLocationUtil.getInstance().getLongitude())
                .put("size", "20")
                .put("tag", "0")
                .put("maxAge", maxAge)
                .put("minAge", minAge)
                .put("sex", sex)
                .build();
        FlirtApiService service = new RetrofitUtils().createApi(FlirtApiService.class);
        addNet(service.getFindList(build).compose(handleResult()).subscribeWith(new LoadingObserver<BaseListDto<FindListDto>>(getView()) {
            @Override
            public void _onNext(BaseListDto<FindListDto> data) {
                getView().getRequestFindListSuccess(data);
            }

            @Override
            public void _onError(String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    getView().toastTip(msg);
                }
            }
        }));
    }

    /**
     * 我的点单列表
     */
    public void getMyOrder() {
        addNet(service.getMyOrder().compose(handleResult())
                .subscribeWith(new LoadingObserver<List<MyOrderDto>>(getView()) {
                    @Override
                    public void _onNext(List<MyOrderDto> data) {
                        getView().setMyOrderList(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        if (TextUtils.isEmpty(msg)) return;
                        getView().toastTip(msg);
                    }
                }));
    }

    /**
     * 获取在线列表
     * <p>
     * params.put("tagname", tagname);
     * params.put("maxAge", maxAge);
     * params.put("minAge", minAge);
     * params.put("sex", sex);
     * params.put("city", city);
     */
    public void getOnlineList(boolean isRefresh, String city, String maxAge, String minAge, String sex) {
        if (isRefresh) {
            mPageIndex = 1;
        }

        RequestBody build = new WenboParamsBuilder()
                .put("page", String.valueOf(mPageIndex))
                .put("city", city)
                .put("maxAge", maxAge)
                .put("minAge", minAge)
                .put("sex", sex)
                .build();
        addNet(service.getOnLineList(build).compose(handleResult()).subscribeWith(new LoadingObserver<BaseListDto<CityHost>>(getView()) {
            @Override
            public void _onNext(BaseListDto<CityHost> data) {
                getView().onReceiveFlirtData(mPageIndex == 1, data.records);
                mPageIndex++;
            }

            @Override
            public void _onError(String msg) {
                if (TextUtils.isEmpty(msg)) return;
                getView().toastTip(msg);
            }
        }));
    }

    /**
     * 获取在线的TA的页码
     *
     * @return
     */
    public int getOnlinePage() {
        return mPageIndex;
    }

    /**
     * 点赞
     *
     * @param id
     * @param position
     */
    public void cardLike(int id, int position) {
        ChatService service = new RetrofitUtils().createApi(ChatService.class);
        addNet(service.cardLike("" + id).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult>() {
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
     *
     * @param id
     * @param type
     * @param imgUrl
     * @param content
     */
    public void getShareInfo(int id, int type, String imgUrl, String content) {
        addNet(new RetrofitUtils().createApi(FlirtApiService.class).getShareInfo(id).compose(io_main()).subscribeWith(new LoadingObserver<HttpResult<ShareUrlDto>>() {
            @Override
            public void _onNext(HttpResult<ShareUrlDto> data) {
                getView().getShareUrlSuccess(data.data.getUrl(), type, imgUrl, content);
            }

            @Override
            public void _onError(String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    getView().toastTip(msg);
                }
            }
        }));
    }

    /**
     * 获取动态详情，用于图片预览和视频播放
     *
     * @param id
     */
    public void getDetailData(int id, int position) {
        addNet(new RetrofitUtils().createApi(FlirtApiService.class).getFindDetail(id).compose(handleResult())
                .subscribeWith(new LoadingObserver<FindDetailBean>() {

                    @Override
                    public void _onNext(FindDetailBean data) {
                        getView().onGetFindDetail(id, data, position);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }
}
