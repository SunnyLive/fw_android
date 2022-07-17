package com.fengwo.module_comment.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @anchor Administrator
 * @date 2020/10/30
 */
public class JumpInvtationDataBean implements Parcelable {
    private String pic;
    private String roomId;
    private String nickname;
    private String text;
    public JumpInvtationDataBean(String pic, String roomId, String nickname, String text) {
        this.pic = pic;
        this.roomId = roomId;
        this.nickname = nickname;
        this.text = text;
    }

    protected JumpInvtationDataBean(Parcel in) {
        pic = in.readString();
        roomId = in.readString();
        nickname = in.readString();
        text = in.readString();
    }

    public static final Creator<JumpInvtationDataBean> CREATOR = new Creator<JumpInvtationDataBean>() {
        @Override
        public JumpInvtationDataBean createFromParcel(Parcel in) {
            return new JumpInvtationDataBean(in);
        }

        @Override
        public JumpInvtationDataBean[] newArray(int size) {
            return new JumpInvtationDataBean[size];
        }
    };

    public String getPic() {
        return pic;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getText() {
        return text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pic);
        dest.writeString(roomId);
        dest.writeString(nickname);
        dest.writeString(text);
    }
}
