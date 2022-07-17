package com.fengwo.mobile.android.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.utils.KLog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler{
	
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	
	private Button gotoBtn, regBtn, launchBtn, checkBtn, scanBtn;

    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.entry);

		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
		//注意：
		//第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
		try {
			if (!api.handleIntent(getIntent(), this)) {
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	// ΢�ŷ������󵽵�����Ӧ��ʱ����ص����÷���
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:

			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:

			break;
		default:
			break;
		}
	}

	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
			WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
			String extraData = launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
			KLog.i("wx_mini","小程序==" + extraData);
//			RxBus.get().post(new RechargeSuccessEvent());
			finish();
		}
//		Toast.makeText(this, "baseresp.getType = " + resp.getType(), Toast.LENGTH_SHORT).show();
//		startActivity(new Intent(this, ChongzhiActivity.class));
		
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}

}