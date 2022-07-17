package com.fengwo.module_comment.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Pair;

import com.fengwo.module_comment.api.CommentService;
import com.fengwo.module_comment.base.BaseActivity;
import com.fengwo.module_comment.bean.ZhuboDto;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

/**
 * @anchor Administrator
 * @date 2021/1/11
 */
public class IntentRoomActivityUrils {
    public static void setRoomActivity(int actId, ArrayList<ZhuboDto> dtoArrayList) {
        setRoomActivity(actId, dtoArrayList, 0);
    }

    @SuppressLint("CheckResult")
    public static void setRoomActivity(int actId, ArrayList<ZhuboDto> dtos, final int position) {
        ArrayList<ZhuboDto> dtoArrayList = new ArrayList<>(dtos);
//        new RetrofitUtils().createApi(CommentService.class).getZhuboList("1,10",-1).compose(RxUtils.applySchedulers())
//                .subscribeWith(new LoadingObserver<HttpResult<BaseListDto<ZhuboDto>>>() {
//                    @Override
//                    public void _onNext(HttpResult<BaseListDto<ZhuboDto>> data) {
//                        if(data.isSuccess()){
//                            for (int i = 0; i <data.data.records.size(); i++) {
//                                if(data.data.records.get(i).channelId==actId){
//                                    ArouteUtils.toLive(data.data.records,i);
//                                    return;
//                                }
//                            }
//                            ArrayList<ZhuboDto> list  = new ArrayList<>();
//                            list.addAll(dtoArrayList);
//                            list.addAll(data.data.records);
//                           // dtoArrayList.addAll(data.data.records);
//                            ArouteUtils.toLive(list,position);
//                        }
//                    }
//
//                    @Override
//                    public void _onError(String msg) {
//
//                    }
//                });
        Activity activity = ActivitysManager.getInstance().currentActivity();
        if (activity instanceof BaseActivity) {
            ((BaseActivity) activity).showLoadingDialog();
        }
        new RetrofitUtils().createApi(CommentService.class).getZhuboList("1,10", -1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(data -> {
                            int currentPosition = position;
                            if (currentPosition < dtoArrayList.size()) {
                                ZhuboDto zhuboDto = dtoArrayList.get(currentPosition);
                                //如果从其他页面只有channelId信息的页面过来，需要设置直播状态，否则会被过滤掉
                                if (zhuboDto.status != 2) {
                                    zhuboDto.status = 2;
                                }
                            }
                            //去除未开播状态的数据
                            ArrayList<ZhuboDto> tempDto = new ArrayList<>();
                            if (data.isSuccess()) {
                                //去重处理
                                for (ZhuboDto newDto : data.data.records) {
                                    boolean key = true;
                                    for (ZhuboDto oldDto : dtoArrayList) {
                                        //相同的 和 不开播的不添加
                                        if ((oldDto.channelId == newDto.channelId)) {
                                            oldDto.update(newDto);
                                            key = false;
                                            break;
                                        }
                                    }
                                    if (key) {
                                        //没有重复
                                        tempDto.add(newDto);
                                    }
                                }
                            }
                            dtoArrayList.addAll(tempDto);
                            //去除未开播状态的数据
                            ArrayList<ZhuboDto> collect = (ArrayList<ZhuboDto>) StreamSupport.stream(dtoArrayList)
                                    .filter(e -> e.status == 2)
                                    .collect(Collectors.toList());
                            //查询主播下标位置
                            for (int i = 0; i < collect.size(); i++) {
                                if (collect.get(i).channelId == actId) {
                                    currentPosition = i;
                                    break;
                                }
                            }
                            return Pair.create(collect, currentPosition);
                        }
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pair -> {
                    ArouteUtils.toLive(pair.first, pair.second);
                }, throwable -> {
                    throwable.fillInStackTrace();
                    KLog.e("tage", throwable.getMessage());
                    if (activity instanceof BaseActivity) {
                        ((BaseActivity) activity).hideLoadingDialog();
                    }
                }, () -> {
                    if (activity instanceof BaseActivity) {
                        ((BaseActivity) activity).hideLoadingDialog();
                    }
                });

    }

}

