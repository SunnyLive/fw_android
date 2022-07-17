//package com.fengwo.module_chat.mvp.ui.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.fengwo.module_chat.R;
//import com.fengwo.module_chat.R2;
//import com.fengwo.module_chat.mvp.model.bean.BeanHotCircleBanner;
//import com.fengwo.module_chat.mvp.ui.adapter.ChatHotCircleAdapter;
//import com.fengwo.module_chat.mvp.ui.adapter.ChatTopCircleAdapter;
//import com.fengwo.module_chat.widgets.xbanner.XBanner;
//import com.fengwo.module_comment.base.BaseActivity;
//import com.fengwo.module_comment.base.BaseMvpActivity;
//import com.fengwo.module_comment.base.BasePresenter;
//import com.fengwo.module_comment.utils.ScreenUtils;
//import com.scwang.smart.refresh.layout.SmartRefreshLayout;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * 热点圈
// *
// * @author chenshanghui
// * @intro
// * @date 2019/9/6
// */
//public class HotCircleActivity extends BaseMvpActivity {
//
//
//    public static void start(Context context) {
//        Intent intent = new Intent(context, HotCircleActivity.class);
//        context.startActivity(intent);
//    }
//
//
//    @BindView(R2.id.rv_hot_circle)
//    RecyclerView rvHotCircle;
//    @BindView(R2.id.smartRefreshLayout)
//    SmartRefreshLayout smartRefreshLayout;
////    @BindView(R2.id.banner)
//    XBanner banner;//太难用了找到合适的换
//
//
//    private ArrayList<String> mData = new ArrayList<>();
//
//    private ChatHotCircleAdapter mAdapter;
//
//
//    @Override
//    protected void initView() {
//        mData.add("");
//        mData.add("");
//        mData.add("");
//        mData.add("");
//        mData.add("");
//        mAdapter = new ChatHotCircleAdapter(mData);
//        rvHotCircle.setLayoutManager(new LinearLayoutManager(this));
//        rvHotCircle.setAdapter(mAdapter);
//        initHeaderBanner();
//
//    }
//
//    private void initHeaderBanner() {
//        View headerBanner = LayoutInflater.from(this).inflate(R.layout.chat_header_banner_hot_circle,null);
//        banner = headerBanner.findViewById(R.id.banner);
//        banner.setIsClipChildrenMode(true);
//        List<BeanHotCircleBanner> data = new ArrayList<>();
//        data.add(new BeanHotCircleBanner());
//        data.add(new BeanHotCircleBanner());
//        data.add(new BeanHotCircleBanner());
//        data.add(new BeanHotCircleBanner());
//        banner.setBannerData(R.layout.chat_item_banner_hot_circle,data);
//        banner.loadImage(new XBanner.XBannerAdapter() {
//            @Override
//            public void loadBanner(XBanner banner, Object model, View view, int position) {
//                ImageView iv = view.findViewById(R.id.iv_circle_icon);
//            }
//        });
//        banner.setAutoPlayAble(true);
//        mAdapter.addHeaderView(headerBanner);
//    }
//
//    @Override
//    protected int getContentView() {
//        return R.layout.chat_activity_hot_circle;
//    }
//
//
//    @Override
//    public BasePresenter initPresenter() {
//        return null;
//    }
//}
