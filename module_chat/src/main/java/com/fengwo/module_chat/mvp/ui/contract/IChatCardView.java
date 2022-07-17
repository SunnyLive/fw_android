package com.fengwo.module_chat.mvp.ui.contract;

import com.fengwo.module_chat.mvp.model.bean.CardMemberModel;
import com.fengwo.module_chat.mvp.model.bean.ChatCardBean;
import com.fengwo.module_comment.base.MvpView;

import java.util.ArrayList;

public interface IChatCardView extends MvpView {

    void setCardList(ArrayList<ChatCardBean> records);

    void setMemberList(int position, CardMemberModel data);

    void addLeftCardList(ArrayList<ChatCardBean> records);

    void addRightCardList(ArrayList<ChatCardBean> records);

    void attentionSuccess(String id, int position);

    void cardLikeSuccess(String id, int position);

    void cardTopSuccess(String id, int position);

    void cardPowerSuccess(String id, int position, int powerStatus);

    void cardDeleteSuccess(String id, int position);

    void postCardSuccess(boolean isDraft);
}
