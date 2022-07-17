package com.fengwo.module_comment.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/10/5
 */
public class BeautyDto implements Parcelable {

    public static final String BEAUTYTAG = "beauty";

    public float mPinkValue = 0.4f;//粉嫩
    public float mWhitenValue = 0.7f;//baixi
    public float mReddenValue = 0.5f;//红润
    public int mSoftenValue = 70;
    public int mFilterValue = 100;
    public String filterType = "";
    public String stickerType = "";
    public boolean isFront = true;

    public int mEyeValue = 39;
    public int mFaceValue = 46;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.mPinkValue);
        dest.writeFloat(this.mWhitenValue);
        dest.writeFloat(this.mReddenValue);
        dest.writeInt(this.mSoftenValue);
        dest.writeInt(this.mFilterValue);
        dest.writeString(this.filterType);
        dest.writeString(this.stickerType);
        dest.writeInt(this.mEyeValue);
        dest.writeInt(this.mFaceValue);
    }

    public BeautyDto() {
    }

    protected BeautyDto(Parcel in) {
        this.mPinkValue = in.readFloat();
        this.mWhitenValue = in.readFloat();
        this.mReddenValue = in.readFloat();
        this.mSoftenValue = in.readInt();
        this.mFilterValue = in.readInt();
        this.filterType = in.readString();
        this.stickerType = in.readString();
        this.mEyeValue = in.readInt();
        this.mFaceValue = in.readInt();
    }

    public static final Creator<BeautyDto> CREATOR = new Creator<BeautyDto>() {
        @Override
        public BeautyDto createFromParcel(Parcel source) {
            return new BeautyDto(source);
        }

        @Override
        public BeautyDto[] newArray(int size) {
            return new BeautyDto[size];
        }
    };
}
