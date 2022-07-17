package com.fengwo.module_live_vedio.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_live_vedio.mvp.dto.AgentInviteDto;

import java.util.ArrayList;

/**
 * @Author BLCS
 * @Time 2020/4/20 18:02
 */
public interface IAgentInvite extends MvpView {
   void setData(ArrayList<AgentInviteDto> records,String total);
}
