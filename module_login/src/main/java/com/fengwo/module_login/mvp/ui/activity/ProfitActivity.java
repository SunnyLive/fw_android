package com.fengwo.module_login.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.faceunity.ui.dialog.BaseDialogFragment;
import com.fengwo.module_comment.BuildConfig;
import com.fengwo.module_comment.Constants;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BrowserActivity;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WebViewActivity;
import com.fengwo.module_comment.dialog.ConfirmWithDrawFragment;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_live_vedio.mvp.ui.pop.BrokenSharePop;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.ProfitDto;
import com.fengwo.module_login.mvp.presenter.ProfitPresenter;
import com.fengwo.module_login.mvp.ui.iview.IProfitView;
import com.fengwo.module_login.utils.UserManager;
import com.fengwo.module_websocket.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.fengwo.module_comment.utils.StringFormatUtils.formatDouble;

public class ProfitActivity extends BaseMvpActivity<IProfitView, ProfitPresenter> implements IProfitView {
    @BindView(R2.id.tv_honey_total)
    TextView tvHoneyTotal;
    @BindView(R2.id.btn_tocashout)
    TextView btnTocashout;
    @BindView(R2.id.btn_toduihuan)
    TextView btnToduihuan;
    @BindView(R2.id.tv_invite_people)
    TextView tvInvitePeople;
    @BindView(R2.id.tv_share_broker)
    TextView tvShareBroker;
    @BindView(R2.id.tv_profit_down)
    TextView tvProfitDown;
    @BindView(R2.id.tv_gift_all)
    TextView tvGiftAll;
    @BindView(R2.id.tv_share)
    TextView tvShare;

    private String H5_WITHDRAW = "H5_WITHDRAW";
    private boolean isH5 = false;

    public static void start(Context context) {
        Intent intent = new Intent(context, ProfitActivity.class);
        context.startActivity(intent);
    }

    private final DecimalFormat df = new DecimalFormat("###.##");

    @Override
    public ProfitPresenter initPresenter() {
        return new ProfitPresenter();
    }

    @Override
    protected void initView() {
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("收益")
                .setRightText("明细", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProfitActivity.this, ProfitDetailActivity.class);
                        startActivity(intent);
                    }
                })
                .setRightTextColor(R.color.text_33)
                .setTitleColor(R.color.text_33)
                .build();
        p.getProfit();
        getServiceKey(H5_WITHDRAW);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tvHoneyTotal != null)
            tvHoneyTotal.setText(formatDouble(UserManager.getInstance().getUser().profit));
        //刷新总额
        UserManager.getInstance().updateUser(new LoadingObserver<UserInfo>() {
            @Override
            public void _onNext(UserInfo data) {
                if (tvHoneyTotal != null) tvHoneyTotal.setText(formatDouble(data.profit));
                UserManager.getInstance().setUserInfo(data);
            }

            @Override
            public void _onError(String msg) {
                if (!TextUtils.isEmpty(msg) && msg.contains("重新登录")) {
                    tokenIInvalid();
                }
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_profit;
    }


    @OnClick({R2.id.btn_tocashout, R2.id.btn_toduihuan, R2.id.tv_share})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_tocashout) {
            if (UserManager.getInstance().getUser().profit == 0) {
                toastTip("暂无可提现金额");
            } else {
                if (isH5) {
                    BrowserActivity.start(this, "", BuildConfig.DEBUG?Url.TEST_BASE_PAY_URL:Url.BASE_PAY_URL + "CashWithdrawal?token=" + UserManager.getInstance().getToken());
                } else {
                    startActivity(CashOutActivity.class);
                }
            }
        } else if (id == R.id.btn_toduihuan) {
        } else if (id == R.id.tv_share) {
            new BrokenSharePop(this, getSupportFragmentManager()).showPopupWindow();
        }
    }

    @Override
    public void setProfitData(ProfitDto profitData) {
        tvInvitePeople.setText(profitData.getInviteCount() + "人");
        tvShareBroker.setText(df.format(profitData.getSuperiorDivide()));
        tvProfitDown.setText(profitData.getInviteProfitCount() + "人");
        tvGiftAll.setText(profitData.getSuperiorGiftTotal() + "");
    }


    private void getServiceKey(String key) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BuildConfig.DEBUG?Url.TEST_BASE_URL: Url.BASE_URL + "api/base/sys_configs/" + key)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject resultJSON = new JSONObject(result);
                    isH5 = resultJSON.getJSONObject("data").getString(key).equals("1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
