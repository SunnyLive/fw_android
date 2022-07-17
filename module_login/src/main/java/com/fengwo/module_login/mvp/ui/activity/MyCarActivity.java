package com.fengwo.module_login.mvp.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_live_vedio.mvp.dto.PopoDto;
import com.fengwo.module_live_vedio.mvp.dto.RefreshBackpack;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.mvp.dto.AllCarDto;
import com.fengwo.module_login.mvp.dto.MyCarDto;
import com.fengwo.module_login.mvp.presenter.MyCarPresenter;
import com.fengwo.module_login.mvp.ui.adapter.AllCarAdapter;
import com.fengwo.module_login.mvp.ui.adapter.MyCarAdapter;
import com.fengwo.module_login.mvp.ui.iview.IMyCarView;
import com.fengwo.module_login.mvp.ui.pop.BuyCarPopwindow;
import com.fengwo.module_login.mvp.ui.pop.NobilityDialog;
import com.fengwo.module_login.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
@Route(path = ArouterApi.CAR_ACTION)
public class MyCarActivity extends BaseMvpActivity<IMyCarView, MyCarPresenter> implements IMyCarView {

    @BindView(R2.id.tv_my)
    TextView tvMy;
    @BindView(R2.id.rv_my_car)
    RecyclerView rvMyCar;
    @BindView(R2.id.tv_all)
    TextView tvAll;
    @BindView(R2.id.rv_all_car)
    RecyclerView rvAllCar;
    @BindView(R2.id.scroll_view)
    NestedScrollView scroll_view;

    private AllCarAdapter allCarAdapter;
    private MyCarAdapter myCarAdapter;
    private List<MyCarDto.RecordsBean> myCarList = new ArrayList<>();
    private List<AllCarDto> allCarList = new ArrayList<>();
    private int choosePosition = -1;

    @Override
    protected void initView() {
//        setTitleBackground(Color.WHITE);
        new ToolBarBuilder().showBack(true).setTitle("我的座驾")
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black).build();
        rvAllCar.setLayoutManager(new GridLayoutManager(this, 2));
        rvMyCar.setLayoutManager(new LinearLayoutManager(this));
        rvMyCar.setNestedScrollingEnabled(false);
        rvAllCar.setNestedScrollingEnabled(false);
        initAllCarList();
        initMyCarAdapter();

        p.getAllCarList();
        p.getMyCarList();
    }

    private void initMyCarAdapter() {
        myCarAdapter = new MyCarAdapter(myCarList);
        rvMyCar.setAdapter(myCarAdapter);
        myCarAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (isFastClick()) {
                    return;
                }
                if (myCarList.isEmpty() || allCarList.isEmpty()) {
                    return;
                }
                if (1 == myCarList.get(position).getIsOpened()) {//启用状态
                    for (int i = 0; i < allCarList.size(); i++) {
                        if (allCarList.get(i).getId() == myCarList.get(position).getMotoringId()) {
                            confirmBuy(allCarList.get(i).getMotoringName(), allCarList.get(i).getId(), allCarList.get(i).getMotoringImg(),
                                    allCarList.get(i).getDayNum(), allCarList.get(i).getMotoringPrice());
                        }
                    }
                } else {
                    choosePosition = position;
                    p.openCar(myCarList.get(position).getMotoringId());//使用此座驾
                }
            }
        });
    }

    private void initAllCarList() {
        allCarAdapter = new AllCarAdapter(allCarList);
        rvAllCar.setAdapter(allCarAdapter);
        allCarAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {//未购买调用购买
                confirmBuy(allCarList.get(position).getMotoringName(), allCarList.get(position).getId(), allCarList.get(position).getMotoringImg(),
                        allCarList.get(position).getDayNum(), allCarList.get(position).getMotoringPrice());
            }
        });
    }

    private void confirmBuy(String name, int id, String imgUrl, int dayNum, int price) {
        BuyCarPopwindow popwindow = new BuyCarPopwindow(this, name, imgUrl, dayNum, price);
        popwindow.setOnBuyCarListener(new BuyCarPopwindow.OnBuyCarListener() {
            @Override
            public void onBuyCar() {
                p.buyCar(String.valueOf(id));
                popwindow.dismiss();
            }
        });
        popwindow.showPopupWindow();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_my_car;
    }

    @Override
    public MyCarPresenter initPresenter() {
        return new MyCarPresenter();
    }

    @Override
    public void setMyCarData(MyCarDto myCarData) {
        if (myCarData.getRecords() == null || myCarData.getRecords().size() == 0) {
            tvMy.setVisibility(View.GONE);
            return;
        } else {
            tvMy.setVisibility(View.VISIBLE);
        }
        myCarList.addAll(myCarData.getRecords());
        myCarAdapter.notifyDataSetChanged();
    }

    @Override
    public void setAllCarData(List<AllCarDto> allCarData) {
        allCarList.addAll(allCarData);
        allCarAdapter.notifyDataSetChanged();
    }

    @Override
    public void buyCarReturn(HttpResult<PopoDto> httpResult) {
        if (httpResult.isSuccess()) {
            toastTip("购买座驾成功");
            UserInfo userInfo = UserManager.getInstance().getUser();
            userInfo.experience = httpResult.data.getExperience().toString();
            UserManager.getInstance().setUserInfo(userInfo);
            reFreshData();
            //告诉直播室 这边购买成功啦 可以刷新数据啦
            RxBus.get().post(new RefreshBackpack());
        } else {
            if (httpResult.description.contains("余额不足")) {
                showTypeDialog(NobilityDialog.TYPE_PAY_FAILURE);
            }else {
                toastTip(httpResult.description);
            }
        }
    }

    private void showTypeDialog(int type) {
        NobilityDialog dialog = new NobilityDialog();
        dialog.setListener(new NobilityDialog.OnPositiveItemClickListener() {
            @Override
            public void onItemConfirm() {
            }

            @Override
            public void onItemPay() {
                startActivity(ChongzhiActivity.class);
            }
        });
        dialog.setType(type);
        dialog.show(getSupportFragmentManager(), NobilityDialog.class.getSimpleName());
    }
    @Override
    public void openCarReturn(HttpResult httpResult) {
        if (httpResult.isSuccess()) {
            toastTip("开启座驾成功");
            UserInfo userInfo = UserManager.getInstance().getUser();
            userInfo.motoringName = myCarList.get(choosePosition).getMotoringName();
            userInfo.motoringSwf = myCarList.get(choosePosition).getMotoringSwf();
            UserManager.getInstance().setUserInfo(userInfo);
            reFreshData();
        } else {
            toastTip(httpResult.description);
        }
    }

    @Override
    public void getcarDetail(AllCarDto allCarDto) {
        confirmBuy(allCarDto.getMotoringName(), allCarDto.getId(), allCarDto.getMotoringThumb(),
                allCarDto.getDayNum(), allCarDto.getMotoringPrice());
    }

    private void reFreshData() {
        myCarList.clear();
        p.getMyCarList();
    }

}
