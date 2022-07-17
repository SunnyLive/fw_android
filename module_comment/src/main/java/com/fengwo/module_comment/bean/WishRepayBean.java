package com.fengwo.module_comment.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @Author BLCS
 * @Time 2020/7/10 14:12
 */
public class WishRepayBean {

    /**
     * 1 : 自行协商
     * 2 : 唱歌
     * 3 : 跳舞
     */

    @SerializedName("1")
    private String _$1;
    @SerializedName("2")
    private String _$2;
    @SerializedName("3")
    private String _$3;

    public String get_$1() {
        return _$1;
    }

    public void set_$1(String _$1) {
        this._$1 = _$1;
    }

    public String get_$2() {
        return _$2;
    }

    public void set_$2(String _$2) {
        this._$2 = _$2;
    }

    public String get_$3() {
        return _$3;
    }

    public void set_$3(String _$3) {
        this._$3 = _$3;
    }
}
