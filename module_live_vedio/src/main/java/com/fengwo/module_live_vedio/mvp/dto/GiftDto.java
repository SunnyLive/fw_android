package com.fengwo.module_live_vedio.mvp.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class GiftDto implements Parcelable {
    public int id;
    public String giftName;
    public double giftPrice;
    public int giftType;
    public String giftTypeText;
    public String giftIcon25;
    public String giftIcon;
    public String giftSwf;
    public String swfTime;
    public int swfPlay;
    public int isWishGift;//是否心願禮物  1是
    public int frameRate;
    public int giftLevel;
    public int continuous;//礼物连送，默认1是，0否
    public int status;
    public int broadcast;//全服飘屏，默认0否，1是
    public String quantityGrad; // 该礼物的送礼数量
    public double giftOriginalPrice;
    public int giftNumber;
    public int giftDiscountPriceTotal;
    /**
     *  活动折扣套餐数量
     */
    public int giftQuantity;

    public static String getSwfName(String giftSwf) {
        int index = giftSwf.lastIndexOf("/");
        int length = giftSwf.length();
        if (index > 0) {
            String swfTemp = giftSwf.substring(index + 1, length);
            return swfTemp.split("\\.")[0];
        }
        return "";
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////



    public String activityId;
    public int goodsCount;
    public String goodsDuration;
    public String goodsIcon;
    public String goodsId;
    public String goodsName;
    public int goodsType;
    public int goodsStatus;
    public int remainCount;
    public int remainValidDays;
    public int validType;
    public long validTime;
    public int remainDays;
    public String remark;
    public boolean isOpened;
    public boolean isMaxLevel;






    @Override
    public String toString() {
        return "GiftDto{" +
                "id=" + id +
                ", giftName='" + giftName + '\'' +
                ", giftPrice=" + giftPrice +
                ", giftType=" + giftType +
                ", giftTypeText='" + giftTypeText + '\'' +
                ", giftIcon25='" + giftIcon25 + '\'' +
                ", giftIcon='" + giftIcon + '\'' +
                ", giftSwf='" + giftSwf + '\'' +
                ", swfTime='" + swfTime + '\'' +
                ", swfPlay=" + swfPlay +
                ", frameRate=" + frameRate +
                ", giftLevel=" + giftLevel +
                ", continuous=" + continuous +
                ", status=" + status +
                ", broadcast=" + broadcast +
                ", quantityGrad='" + quantityGrad + '\'' +
                ", giftOriginalPrice=" + giftOriginalPrice +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.giftName);
        dest.writeDouble(this.giftPrice);
        dest.writeInt(this.giftType);
        dest.writeString(this.giftTypeText);
        dest.writeString(this.giftIcon25);
        dest.writeString(this.giftIcon);
        dest.writeString(this.giftSwf);
        dest.writeString(this.swfTime);
        dest.writeInt(this.swfPlay);
        dest.writeInt(this.isWishGift);
        dest.writeInt(this.frameRate);
        dest.writeInt(this.giftLevel);
        dest.writeInt(this.continuous);
        dest.writeInt(this.status);
        dest.writeInt(this.broadcast);
        dest.writeString(this.quantityGrad);
        dest.writeDouble(this.giftOriginalPrice);
        dest.writeInt(this.giftNumber);
    }

    public GiftDto() {
    }

    protected GiftDto(Parcel in) {
        this.id = in.readInt();
        this.giftName = in.readString();
        this.giftPrice = in.readDouble();
        this.giftType = in.readInt();
        this.giftTypeText = in.readString();
        this.giftIcon25 = in.readString();
        this.giftIcon = in.readString();
        this.giftSwf = in.readString();
        this.swfTime = in.readString();
        this.swfPlay = in.readInt();
        this.isWishGift = in.readInt();
        this.frameRate = in.readInt();
        this.giftLevel = in.readInt();
        this.continuous = in.readInt();
        this.status = in.readInt();
        this.broadcast = in.readInt();
        this.quantityGrad = in.readString();
        this.giftOriginalPrice = in.readDouble();
        this.giftNumber = in.readInt();
    }

    public static final Creator<GiftDto> CREATOR = new Creator<GiftDto>() {
        @Override
        public GiftDto createFromParcel(Parcel source) {
            return new GiftDto(source);
        }

        @Override
        public GiftDto[] newArray(int size) {
            return new GiftDto[size];
        }
    };
}
