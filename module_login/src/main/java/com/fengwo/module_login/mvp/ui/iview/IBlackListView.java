package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.BlackDto;

public interface IBlackListView extends MvpView {
   void  getBalckList(BaseListDto<BlackDto> data);
   void  deleteSuccess(String  data,int pos);
}
