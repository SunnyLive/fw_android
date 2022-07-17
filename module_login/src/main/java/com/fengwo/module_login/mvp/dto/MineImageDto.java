package com.fengwo.module_login.mvp.dto;

import com.fengwo.module_comment.iservice.UserInfo;

public class MineImageDto {

    public String imageUrl;
    public WatchHistoryDto mAnchorHistory;
    public MyCarDto.RecordsBean mCar;
    public GiftWallDto.PageList.GiftWall mGift;
    public UserInfo.UserGuardList mGuard;

    public boolean isLiving() {
        if (mAnchorHistory != null) {
            return mAnchorHistory.getStatus() == 2;
        }
        return false;
    }
}
