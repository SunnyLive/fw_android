package com.fengwo.module_flirt.P;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_flirt.Interfaces.IFlirtHomeView;
import com.fengwo.module_flirt.bean.CerTagBean;
import com.fengwo.module_flirt.bean.LabelTalentDto;
import com.fengwo.module_flirt.bean.ZipLabelDto;
import com.fengwo.module_login.api.LoginApiService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import okhttp3.RequestBody;


public class FlirtHomePresenter  extends BaseFlirtPresenter<IFlirtHomeView> {

    /**
     * 获取全部标签
     */
    public void getLabel() {
        addNet(service.getNewHomeTagList().compose(handleResult())
                .subscribeWith(new LoadingObserver<List<CerTagBean>>() {
                    @Override
                    public void _onNext(List<CerTagBean> data) {
//                        getView().getLabelSuccess(data);
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }
    /**
     * 获取在线推荐主播
     */
    public void getLabelTalent(String maxAge,String minAge,String sex) {
        RequestBody build = new WenboParamsBuilder()
                .put("maxAge", maxAge)
                .put("minAge", minAge)
                .put("sex", sex)
                .build();
        addNet(service.getLabelTalent(build).compose(handleResult())
                .subscribeWith(new LoadingObserver<BaseListDto<LabelTalentDto>>() {
                    @Override
                    public void _onNext(BaseListDto<LabelTalentDto> data) {
//                        getView().setLabelTalent(data);
                    }
                    @Override
                    public void _onError(String msg) {
                        getView().toastTip(msg);
                    }
                })
        );
    }

    /**
     * 获取数据
     * @param maxAge
     * @param minAge
     * @param sex
     */
    public void getData(String maxAge,String minAge,String sex){
        RequestBody build = new WenboParamsBuilder()
                .put("maxAge", maxAge)
                .put("minAge", minAge)
                .put("sex", sex)
                .build();
        addNet(Flowable.zip(service.getNewHomeTagList(), service.getLabelTalent(build),
                (BiFunction<HttpResult<List<CerTagBean>>, HttpResult<BaseListDto<LabelTalentDto>>, List<ZipLabelDto>>) (cerTagBeans, labelTalentDto) -> {
                    /*组合两个接口 成一个列表*/
                    ArrayList<ZipLabelDto> zipLabelDtos = new ArrayList<>();
                    List<CerTagBean> data = cerTagBeans.data;//标签列表
                    L.e("======1 "+data.size());
                    if (data!=null){
                        for (CerTagBean cerTagBean:data){
                            ZipLabelDto zipLabelDto = new ZipLabelDto(cerTagBean.getTagName());
                            zipLabelDtos.add(zipLabelDto);
                        }
                    }
                    ArrayList<LabelTalentDto> records = labelTalentDto.data.records;//主播列表
                    L.e("======2 "+records.size());
                    if (records!=null){
                        for (LabelTalentDto labelTalent: records){
                            ZipLabelDto zipLabelDto = new ZipLabelDto(labelTalent.getAge(),labelTalent.getAnchorId(),labelTalent.getHeadImg(),labelTalent.getNickname(),labelTalent.getSex(),labelTalent.getBstatus());
                            zipLabelDtos.add(zipLabelDto);
                        }
                    }
                    return zipLabelDtos;
                }).compose(io_main()).subscribeWith(new LoadingObserver<List<ZipLabelDto>>() {
            @Override
            public void _onNext(List<ZipLabelDto> data) {
                L.e("======3 "+data.size());
                getView().getZipLabel(data);
            }

            @Override
            public void _onError(String msg) {
                getView().toastTip(msg);
            }
        }));
    }

    /**
     * 上传经纬度
     */
    public void upLoading(String latitude, String longitude, String city) {

        new RetrofitUtils().createApi(LoginApiService.class)
                .updateUserinfo(new HttpUtils.ParamsBuilder()
//                        .put("signature", userInfo.signature)
//                        .put("sex", userInfo.sex + "")
                                .put("location", city)
                                .put("longitude", longitude)
                                .put("latitude", latitude)
//                        .put("headImg", userInfo.headImg)
//                        .put("birthday", userInfo.birthday)
                                .build()
                )
                .compose(io_main())
                .compose(io_main())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        L.e("更新地址成功");
                    }

                    @Override
                    public void _onError(String msg) {
                        L.e("更新地址失败 " + msg);
                    }
                });
    }
}
