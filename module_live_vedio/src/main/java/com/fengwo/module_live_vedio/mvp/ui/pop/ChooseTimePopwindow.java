package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.LitmitDto;
import com.fengwo.module_websocket.EventConstant;
import com.fengwo.module_websocket.FWWebSocket1;
import com.fengwo.module_websocket.bean.SocketRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/24
 */
public class ChooseTimePopwindow extends BasePopupWindow implements View.OnClickListener {

    private TextView tvHours, tvDay, tvWeek, tvMonth, tvCancel;
    private Gson gson = new Gson();
    private int limitTime = 0;
    private Context context;
    private int type = 1;//类型 1:发言 2 :进入
    private int channelId;//主播id
    private int userId;//用户id


    public ChooseTimePopwindow(Context context, int type, int channelId, int userId) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        this.context = context;
        this.type = type;
        this.channelId = channelId;

        L.e("---", "channelId:" + channelId);
        this.userId = userId;
        tvHours = findViewById(R.id.tv_hours);
        tvDay = findViewById(R.id.tv_day);
        tvWeek = findViewById(R.id.tv_week);
        tvMonth = findViewById(R.id.tv_month);
        tvCancel = findViewById(R.id.tv_cancel);
        tvHours.setOnClickListener(this);
        tvDay.setOnClickListener(this);
        tvWeek.setOnClickListener(this);
        tvMonth.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    private void setLimitTime() {
        new RetrofitUtils().createApi(LiveApiService.class).AddRestrict(createRequestBody())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingObserver<HttpResult>() {

                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            if (type == 2) {//踢出，发送IM消息
//                                sendKickedOutMsg();
                            }
                            ToastUtils.showShort(context, data.description);
                            dismiss();
                        } else {
                            ToastUtils.showShort(context, data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(context, msg);
                    }
                });
    }

    private void sendKickedOutMsg() {
        SocketRequest<LitmitDto> request = new SocketRequest<>();
        request.eventId = EventConstant.broadcastMessage + "";
        request.toUid = Constants.GROUP_BASE + channelId + "";
        request.fromUid = "-1";
        LitmitDto litmitDto = new LitmitDto();
        litmitDto.actionId = EventConstant.CMD_KICKEDOUT;
        litmitDto.userId = userId;
        litmitDto.msgId = userId + System.currentTimeMillis() + "";
        request.data = litmitDto;
        FWWebSocket1.getInstance().sendTextMessage(gson.toJson(request));
    }

    private void sendShutUpMsg() {
        SocketRequest<LitmitDto> request = new SocketRequest<>();
        request.eventId = EventConstant.broadcastMessage + "";
        request.toUid = Constants.GROUP_BASE + channelId + "";
        request.fromUid = "-1";
        LitmitDto litmitDto = new LitmitDto();
        litmitDto.actionId = EventConstant.CMD_KICKEDOUT;
        litmitDto.userId = userId;
        litmitDto.msgId = userId + System.currentTimeMillis() + "";
        request.data = litmitDto;
        FWWebSocket1.getInstance().sendTextMessage(gson.toJson(request));
    }

    public RequestBody createRequestBody() {
        Map map = new HashMap();
        map.put("channelId", channelId);
        map.put("type", type);
        map.put("effectTime", limitTime);
        map.put("userId", userId);
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        return requestBody;
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_choose_time);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_hours) {
            limitTime = 3600;
            setLimitTime();
        } else if (i == R.id.tv_day) {
            limitTime = 3600 * 24;
            setLimitTime();
        } else if (i == R.id.tv_week) {
            limitTime = 3600 * 24 * 7;
            setLimitTime();
        } else if (i == R.id.tv_month) {
            limitTime = 3600 * 24 * 30;
            setLimitTime();
        } else if (i == R.id.tv_cancel) {
            dismiss();
        }
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }
}
