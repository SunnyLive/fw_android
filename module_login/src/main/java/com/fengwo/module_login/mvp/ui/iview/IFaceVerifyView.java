package com.fengwo.module_login.mvp.ui.iview;

import com.fengwo.module_comment.base.MvpView;
import com.fengwo.module_login.mvp.dto.VerifyResultDto;
import com.fengwo.module_login.mvp.dto.VerifyTokenDto;

public interface IFaceVerifyView extends MvpView {

   /**
    * 获取人脸识别token成功
    * @param verifyTokenDto
    */
   void onGetVerifyTokenSuccess(VerifyTokenDto verifyTokenDto);

   /**
    * 获取人脸识别token失败
    * @param msg
    */
   void onGetVerifyTokenError(String msg);

   /**
    * 获取人脸识别结果成功
    * @param verifyResultDto
    */
   void onGetVerifyResultSuccess(VerifyResultDto verifyResultDto);

   /**
    * 获取人脸识别结果失败
    * @param msg
    */
   void onGetVerifyResultError(String msg);


   /**
    * 人工审核提交成功
    */
   void onFaceReViewSuccess();


}
