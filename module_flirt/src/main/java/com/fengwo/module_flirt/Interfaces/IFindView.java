package com.fengwo.module_flirt.Interfaces;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.bean.FindHeaderDto;
import com.fengwo.module_flirt.bean.FindListDto;
import com.fengwo.module_flirt.bean.ShareUrlDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author BLCS
 * @Time 2020/4/24 18:15
 */
public interface IFindView extends MvpView {

    void getRequesHeadersSuccess(List<FindHeaderDto> data);

    void getRequestFindListSuccess(BaseListDto<FindListDto> data);

    void cardLikeSuccess(int id, int position);

    void getShareUrlSuccess(String url,int type,String imgUrl,String content);
}
