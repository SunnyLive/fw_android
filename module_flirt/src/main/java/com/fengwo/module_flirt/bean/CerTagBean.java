package com.fengwo.module_flirt.bean;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/4/27
 */
public class CerTagBean implements Serializable {

    private String tagName;
    private int id;
    // 自定义属性
    public boolean selected = false; // 是否选中
    public int originPosition; // 在原本的数据源中的位置

    private String parentId;
    private List<ChildrenBean> children;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBean> children) {
        this.children = children;
    }


    public static class ChildrenBean implements Serializable{

        @SerializedName("id")
        private int idX;
        private String parentId;
        @SerializedName("tagName")
        private String tagNameX;
        private Object children;

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        private boolean checked;
        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getTagNameX() {
            return tagNameX;
        }

        public void setTagNameX(String tagNameX) {
            this.tagNameX = tagNameX;
        }

        public Object getChildren() {
            return children;
        }

        public void setChildren(Object children) {
            this.children = children;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            ChildrenBean obj1 = (ChildrenBean) obj;
            return idX == obj1.idX;
        }
    }
}

