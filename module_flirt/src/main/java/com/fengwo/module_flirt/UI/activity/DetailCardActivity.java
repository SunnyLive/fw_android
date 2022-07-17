package com.fengwo.module_flirt.UI.activity;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.Interfaces.ICardType;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_comment.widget.DetailCardPopView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 视频图片预览页
 */
@Route(path = ArouterApi.FLIRT_DETAIL_CARD_ACTION)
public class DetailCardActivity extends BaseMvpActivity {

    @BindView(R2.id.detail_view_pop)
    DetailCardPopView detailViewPop;

    public static <T extends ICardType> void start(Context context, ArrayList<T> bannerList, int position) {
        Intent intent = new Intent(context, DetailCardActivity.class);
        intent.putExtra("bannerList", bannerList);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        int position = getIntent().getIntExtra("position", 0);
        ArrayList<ICardType> arrayList = (ArrayList<ICardType>) getIntent().getSerializableExtra("bannerList");
        assert arrayList != null;
        detailViewPop.setBanner(arrayList, position);
//        detailViewPop.addClickPlayListener(new DetailCardPopView.ClickPlayListener() {
//            @Override
//            public void click() {
//
//            }
//        });
    }

    @OnClick(R2.id.iv_back)
    protected void onBack() {
        finish();
    }

    @Override
    protected void onDestroy() {
        detailViewPop.destroy();
        super.onDestroy();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_detail_card_pop;
    }
}
