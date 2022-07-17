package com.fengwo.module_chat.mvp.model.bean;

import com.fengwo.module_comment.bean.PostCardItem;

import java.io.Serializable;
import java.util.List;

public class PostCardDraftModel implements Serializable {
    private static final long serialVersionUID = -5161331041135408066L;
    public List<PostCardItem> items;
    public RecommendCircleBean cardCategory;
    public String content;
    public String tagName;
    public String tagIds;
}
