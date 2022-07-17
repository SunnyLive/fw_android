package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_live_vedio.mvp.dto.GuardListDto;
import com.fengwo.module_live_vedio.mvp.dto.LiveProfitDto;
import com.fengwo.module_login.mvp.dto.GiftWallDto;
import com.fengwo.module_login.mvp.dto.ReceiveGiftDto;

import java.util.ArrayList;
import java.util.List;

public interface IMineInfoView extends MvpView {
    void getGiftWall(GiftWallDto data);

    void setGuardWindow(int total, ArrayList<GuardListDto.Guard> records);//设置守护列表

    void setTodayReceive(int total, List<LiveProfitDto.RecordsBean> records);//设置今日贡献榜
    void showShouhu(Boolean isHost , int id, UserInfo data);
}
