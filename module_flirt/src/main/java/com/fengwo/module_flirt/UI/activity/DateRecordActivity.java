package com.fengwo.module_flirt.UI.activity;

import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseListActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.base.WenboParamsBuilder;
import com.fengwo.module_comment.utils.DialogUtil;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.L;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;

import com.fengwo.module_comment.utils.SmartRefreshLayoutUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.api.FlirtApiService;
import com.fengwo.module_flirt.bean.DateRecordDto;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

@Route(path = ArouterApi.FLIRT_DATERECORD)
public class DateRecordActivity extends BaseListActivity<DateRecordDto> {

    private CompositeDisposable compositeDisposables;
    private int cancleId;
    private AlertDialog cancleDialog;

    @Override
    protected void initView() {
        super.initView();
        SmartRefreshLayoutUtils.setWhiteBlackText(this, findViewById(R.id.smartrefreshlayout));
        new ToolBarBuilder().setTitle("预约记录").setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black).showBack(true).build();
//        setTitleBackground(getResources().getColor(R.color.white));
        compositeDisposables = new CompositeDisposable();
        cancleDialog = DialogUtil.getWenboAlertDialog(this, "撤销预约", "确定要撤销与主播的视频约会吗", "撤销预约", "取消", true, new DialogUtil.AlertDialogBtnClickListener() {
            @Override
            public void clickPositive() {
                cancleDate(cancleId);
            }

            @Override
            public void clickNegative() {

            }
        });
    }


    @Override
    public Flowable setNetObservable() {
        WenboParamsBuilder builder = new WenboParamsBuilder();
        builder.put("page", page + "");
        builder.put("size", DEFAULT_PAGE_SIZE);
        return new RetrofitUtils().createWenboApi(FlirtApiService.class).getDateRecord(builder.build());
    }

    @Override
    public RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public int setItemLayoutRes() {
        return R.layout.item_date_record;
    }

    @Override
    public void bingViewHolder(BaseViewHolder helper, DateRecordDto item, int position) {
        ImageLoader.loadImg(helper.getView(R.id.iv_header), item.getHeadImg());
        helper.setText(R.id.tv_name, item.getNickname());
        helper.setText(R.id.tv_time, item.getApponitTime());
        helper.setText(R.id.tv_status, item.getStatusTxt());
        helper.setText(R.id.btn_cancle, item.getButtonText());
        helper.setVisible(R.id.btn_cancle, item.isCanCancle());
        helper.getView(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getApponitStatus()==0){
                    cancleId = item.getId();
                    cancleDialog.show();
                }else{
                    FlirtCardDetailsActivity.start(DateRecordActivity.this,item.getAnchorId());
                }
            }
        });
    }

    private void cancleDate(int id) {
        WenboParamsBuilder paramsBuilder = new WenboParamsBuilder();
        paramsBuilder.put("id", id + "");
        compositeDisposables.add(new RetrofitUtils().createWenboApi(FlirtApiService.class)
                .cancleDateRecord(paramsBuilder.build())
                .compose(io_main())
                .subscribeWith(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            refresh();
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                }));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_date_record;
    }
}
