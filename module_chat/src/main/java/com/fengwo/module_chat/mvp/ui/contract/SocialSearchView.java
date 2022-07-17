package com.fengwo.module_chat.mvp.ui.contract;

import com.fengwo.module_chat.mvp.model.bean.SearchCardBean;
import com.fengwo.module_comment.base.MvpView;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/26
 */
public interface SocialSearchView extends MvpView {

    void returnSearchHot(List<String> list);
    void returnSearchCard(SearchCardBean list);
}
