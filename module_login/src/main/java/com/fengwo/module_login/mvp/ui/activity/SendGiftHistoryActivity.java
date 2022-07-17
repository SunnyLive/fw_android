package com.fengwo.module_login.mvp.ui.activity;

import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.widget.AppTitleBar;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.mvp.dto.SendGiftHistoryDto;
import com.fengwo.module_login.mvp.ui.pop.SendHistoryPop;

import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public class SendGiftHistoryActivity extends BaseListActivity<SendGiftHistoryDto> implements SendHistoryPop.OnClearCallback {

    private final static String PAGE_SIZE = ",10";

    @BindView(R2.id.title)
    AppTitleBar titleBar;

    @Override
    protected int getContentView() {
        return R.layout.login_activity_send_gift_history;
    }

    @Override
    protected void initView() {
        super.initView();
//        titleBar.setMoreClickListener(v -> showSelectDialog());
    }

    @Override
    public Flowable setNetObservable() {
        String p = page + PAGE_SIZE;
        return new RetrofitUtils().createApi(LoginApiService.class).getSendGiftHistory(p);
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.login_item_send_gift;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, SendGiftHistoryDto item, int position) {
        ImageView header = helper.setText(R.id.tv_title_name, item.getUserNickname())
                .setText(R.id.tv_title_gift, ""+item.getQuantity()+item.getGiftName())
                .setText(R.id.tv_gift_date, TimeUtils.dealDateFormatToRecord(item.getCreateTime()))
                .getView(R.id.iv_header);
        ImageLoader.loadImg(header, item.getGiftIcon());
    }

    @Override
    public void onClear() {
        DialogUtil.showAlertDialog(this, "删除充值记录", "确定要删除充值记录？",
                "确定", "取消", false, new DialogUtil.AlertDialogBtnClickListener() {
                    @Override
                    public void clickPositive() {
                    }

                    @Override
                    public void clickNegative() {
                    }
                });
    }

    private void showSelectDialog() {
        SendHistoryPop pop = new SendHistoryPop(this, this);
        pop.showPopupWindow();
    }
}
