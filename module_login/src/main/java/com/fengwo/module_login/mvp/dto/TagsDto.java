package com.fengwo.module_login.mvp.dto;

public class TagsDto {
    private String tagSet;   // 标签集
    private String tagTypeCode;   //标签类型标识
    private String tagTypeName;   //标签类型名称
    private boolean mIsSelect=false;

    public String getTagSet() {
        return tagSet;
    }

    public void setTagSet(String tagSet) {
        this.tagSet = tagSet;
    }

    public String getTagTypeCode() {
        return tagTypeCode;
    }

    public void setTagTypeCode(String tagTypeCode) {
        this.tagTypeCode = tagTypeCode;
    }

    public String getTagTypeName() {
        return tagTypeName;
    }

    public void setTagTypeName(String tagTypeName) {
        this.tagTypeName = tagTypeName;
    }

    public boolean ismIsSelect() {
        return mIsSelect;
    }

    public void setmIsSelect(boolean mIsSelect) {
        this.mIsSelect = mIsSelect;
    }
}
