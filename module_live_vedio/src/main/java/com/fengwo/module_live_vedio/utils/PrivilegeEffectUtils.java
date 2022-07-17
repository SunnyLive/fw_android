package com.fengwo.module_live_vedio.utils;

import android.graphics.Color;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.ComingEffectDto;
import com.fengwo.module_live_vedio.mvp.dto.GiftEffectDto;

import java.util.ArrayList;
import java.util.List;

public class PrivilegeEffectUtils {

    private final static String MSG_NO_ENOUGH = "3级才可以发言哦~";

    public static PrivilegeEffectUtils INSTANCE;
    private static List<GiftEffectDto> giftEffectDtos;
    private static List<ComingEffectDto> comingEffectDtos;

    public static PrivilegeEffectUtils getInstance() {
        if (INSTANCE == null) {
            synchronized (PrivilegeEffectUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PrivilegeEffectUtils();
                }
            }
        }
        return INSTANCE;
    }

    private PrivilegeEffectUtils() {
//        getEffects();
        initGiftEffects();
        initComingEffects();
    }

    public boolean isShowBigGift(double price) {
        if (price > 1000) {
            return true;
        } else return false;

    }

    public boolean isMsgEnable(int userLevel) {
        return userLevel >= 3;
    }

    public int getVip3Color() {
        return Color.parseColor("#64990000");
    }

    public String msgLevelNoEnough() {
        return MSG_NO_ENOUGH;
    }

    public GiftEffectDto getEffectsWithPrice(int price) {
        for (int i = giftEffectDtos.size() - 1; i >= 0; i--) {
            if (price > giftEffectDtos.get(i).getGiftValue()) {
                return giftEffectDtos.get(i);
            }
        }
        return giftEffectDtos.get(0);
//        if (price > 0 && price < 10000) {
//            return giftEffectDtos.get(0);
//        } else if (price >= 10000 && price <= 100000) {
//            return giftEffectDtos.get(1);
//        } else if (price > 100000 && price < 500000) {
//            return giftEffectDtos.get(2);
//        } else
//            return giftEffectDtos.get(3);
    }

    public ComingEffectDto getEffectsWithVip(int vip) {
        return comingEffectDtos.get(vip);
    }

    private void initGiftEffects() {
        giftEffectDtos = new ArrayList<>();
        GiftEffectDto dto = new GiftEffectDto();
        dto.setGiftValue(0);
        dto.setComboRes(R.drawable.bg_gift_effect1);
        dto.setBorderColor("#60000000");
        dto.setBorderColor("#00000000");
        giftEffectDtos.add(dto);
        dto = new GiftEffectDto();
        dto.setGiftValue(10000);
        dto.setComboRes(R.drawable.bg_gift_effect2);
        dto.setBubbleColor("#644595E5");
        dto.setBorderColor("#00000000");
        giftEffectDtos.add(dto);
        dto = new GiftEffectDto();
        dto.setGiftValue(100000);
        dto.setComboRes(R.drawable.bg_gift_effect3);
        dto.setBubbleColor("#E56045");
        dto.setBorderColor("#00000000");
        giftEffectDtos.add(dto);
        dto = new GiftEffectDto();
        dto.setGiftValue(1000000);
        dto.setComboRes(R.drawable.bg_gift_effect4);
        dto.setBubbleColor("#C72EE5");
        dto.setBorderColor("#FFE566");
        giftEffectDtos.add(dto);
    }

    private void initComingEffects() {
        comingEffectDtos = new ArrayList<>();
        ComingEffectDto dto = new ComingEffectDto();
        dto.setCombo(R.drawable.bg_gift_effect1);
        dto.setBubbleColor("#60000000");
        dto.setBorderColor("#00000000");
        dto.setComingTxtColor("#FFE566");
        comingEffectDtos.add(dto);
        dto = new ComingEffectDto();
        dto.setCombo(R.drawable.bg_gift_effect2);
        dto.setBubbleColor("#4595E5");
        dto.setBorderColor("#00000000");
        dto.setComingTxtColor("#FFE566");
        comingEffectDtos.add(dto);
        dto = new ComingEffectDto();
        dto.setCombo(R.drawable.bg_gift_effect3);
        dto.setBubbleColor("#E56045");
        dto.setBorderColor("#00000000");
        dto.setComingTxtColor("#FFE566");
        comingEffectDtos.add(dto);
        dto = new ComingEffectDto();
        dto.setCombo(R.drawable.bg_gift_effect4);
        dto.setBubbleColor("#C72EE5");
        dto.setBorderColor("#FFE566");
        dto.setComingTxtColor("#FFE566");
        comingEffectDtos.add(dto);
    }


    private void getEffects() {
        new RetrofitUtils().createApi(LiveApiService.class)
                .getGiftEffect()
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult<List<GiftEffectDto>>>() {
                    @Override
                    public void _onNext(HttpResult<List<GiftEffectDto>> data) {
                        if (data.isSuccess()) {
                            giftEffectDtos = data.data;
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }


    public boolean hasEnterHeader(int userVipLevel) {
        return userVipLevel == 3;
    }
}
