package com.fengwo.module_flirt.P;

import android.text.TextUtils;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_flirt.Interfaces.IFlirtAnimationView;
import com.fengwo.module_flirt.bean.CerTagBean;
import com.fengwo.module_flirt.bean.LabelTalentDto;
import com.fengwo.module_flirt.bean.ZipLabelParentDto;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import okhttp3.RequestBody;


public class FlirtAnimationPresenter extends BaseFlirtPresenter<IFlirtAnimationView> {


    /**
     * 换一批页码
     */
    private int page = 1;

    public void resetPage() {
        page = 1;
    }

    /**
     * 获取数据
     */
    public void getData() {
        RequestBody tagBody = new WenboParamsBuilder().put("count", 12).build();

        WenboParamsBuilder builder = new WenboParamsBuilder()
                .put("type", "0")
                .put("page", "1,3");
        addNet(Flowable.zip(service.getNewHomeTagList2(tagBody), service.getLabelTalent(builder.build()),
                (BiFunction<HttpResult<List<CerTagBean>>, HttpResult<BaseListDto<LabelTalentDto>>, ZipLabelParentDto>) (cerTagBeans, labelTalentDto) -> {
                    /*组合两个接口 成一个列表*/
                    ZipLabelParentDto zipLabelDto = new ZipLabelParentDto();
                    zipLabelDto.setCerTags(cerTagBeans.data);
                    zipLabelDto.setLabelTalents(labelTalentDto.data.records);
//                    List<CerTagBean> data = cerTagBeans.data;//标签列表
//                    L.e("======1 " + data.size());
//                    if (data != null) {
//                        for (CerTagBean cerTagBean : data) {
//                            ZipLabelDto zipLabelDto = new ZipLabelDto(cerTagBean.getTagName());
//                            zipLabelDtos.add(zipLabelDto);
//                        }
//                    }
//                    ArrayList<LabelTalentDto> records = labelTalentDto.data.records;//主播列表
//                    L.e("======2 " + records.size());
//                    if (records != null) {
//                        for (LabelTalentDto labelTalent : records) {
//                            ZipLabelDto zipLabelDto = new ZipLabelDto(labelTalent.getAge(), labelTalent.getAnchorId(), labelTalent.getHeadImg(), labelTalent.getNickname(), labelTalent.getSex(), labelTalent.getBstatus());
//                            zipLabelDtos.add(zipLabelDto);
//                        }
//                    }
                    return zipLabelDto;
                }).compose(io_main()).subscribeWith(new LoadingObserver<ZipLabelParentDto>() {
            @Override
            public void _onNext(ZipLabelParentDto data) {
                L.e("======3 " + data);
                getView().getZipLabel(data);
            }

            @Override
            public void _onError(String msg) {
                getView().toastTip(msg);
            }
        }));
    }


    /**
     * 获取在线列表
     */
    public void setTagName(String tagName, String maxAge, String minAge, String sex, String city) {
        WenboParamsBuilder builder = new WenboParamsBuilder()
                .put("type", "1")
                .put("page", page + ",7")
                .put("tagName", tagName)
                .put("maxAge", maxAge)
                .put("minAge", minAge)
                .put("sex", sex)
                .put("city", city);
        addNet(service.getLabelTalent(builder.build()).compose(handleResult()).subscribeWith(new LoadingObserver<BaseListDto<LabelTalentDto>>(getView()) {
            @Override
            public void _onNext(BaseListDto<LabelTalentDto> data) {
                if (page < Integer.parseInt(data.total) / 7) {
                    page++;
                } else {
                    page = 1;
                }
                getView().onReceiveLoadMore(data.records);
            }

            @Override
            public void _onError(String msg) {
                if (TextUtils.isEmpty(msg)) return;
                getView().toastTip(msg);
            }
        }));
    }

}
