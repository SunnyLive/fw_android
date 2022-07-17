package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.widget.CustomerDialog;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.BannedDto;
import com.fengwo.module_live_vedio.mvp.ui.adapter.ManagerVpAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/10/22
 */
public class LiveManagerPopwindow extends BasePopupWindow implements TabLayout.OnTabSelectedListener {

    public static final int room_manager = 0;
    public static final int banned = 1;
    public static final int kick_out = 2;
    private int mCurrenType = room_manager;

    private Context context;
    private Gson gson = new Gson();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private ManagerVpAdapter pagerAdapter;
    private int channelId;
    private TextView tvDay, tvWeek, tvMonth;


    public LiveManagerPopwindow(Context context, int channelId) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        this.context = context;
        this.channelId = channelId;
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);
        titleList.add("房管");
        titleList.add("禁言");
        titleList.add("踢出");
        tvDay = findViewById(R.id.tv_day);
        tvWeek = findViewById(R.id.tv_week);
        tvMonth = findViewById(R.id.tv_month);
//        for (int i = 0; i < titleList.size(); i++) {
//            TabLayout.Tab tab = tabLayout.newTab();
//            GradientTextView textView = new GradientTextView(context);
//            textView.setText(titleList.get(i));
//            textView.setGravity(Gravity.CENTER);
//            if (i == 0) {
//                textView.setTextColor(context.getResources().getColor(R.color.white));
//                textView.setColors(Color.parseColor("#D375FF"), Color.parseColor("#9372ED"));
//            } else {
//                textView.setTextColor(context.getResources().getColor(R.color.gray_666666));
//                int colorTrans = context.getResources().getColor(android.R.color.transparent);
//                textView.setColors(colorTrans, colorTrans);
//            }
//            int dp10 = (int) context.getResources().getDimension(R.dimen.dp_10);
//            int dp22 = (int) context.getResources().getDimension(R.dimen.dp_22);
//            textView.setPadding(dp22, dp10, dp22, dp10);
//            tab.setCustomView(textView);
//            tabLayout.addTab(tab);
//        }
        pagerAdapter = new ManagerVpAdapter(context, new ArrayList<>(), titleList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrenType = position;
                tabLayout.getTabAt(position).select();
                if (position == banned) {
                    getStickData(banned);
                } else if (position == kick_out) {
                    getStickData(kick_out);
                } else {
                    getRoomManager(room_manager);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagerAdapter.setOnItemClickLisenter(new ManagerVpAdapter.OnChildItemClickListener() {
            @Override
            public void onChildClick(int channelId, int userId) {
                new CustomerDialog.Builder(context)
                        .setTitle("温馨提示")
                        .setMsg("是否移除该用户")
                        .setNegativeBtnShow(true)
                        .setPositiveButton(new CustomerDialog.onPositiveInterface() {
                            @Override
                            public void onPositive() {
                                if (mCurrenType == room_manager) {
                                    removeRoomManager(userId);
                                } else {
                                    removeStick(channelId, userId);
                                }
                            }
                        }).create().show();
            }
        });
        getRoomManager(room_manager);
    }

    private void getStickData(int type) {
        new RetrofitUtils().createApi(LiveApiService.class).getStickList(createRequestBody(type))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingObserver<HttpResult<BaseListDto<BannedDto>>>() {
                    @Override
                    public void _onNext(HttpResult<BaseListDto<BannedDto>> data) {
                        if (data.isSuccess()) {
                            pagerAdapter.reFreshStickData(data.data.records, type);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    private void getRoomManager(int type) {
        new RetrofitUtils().createApi(LiveApiService.class).getRoomManager()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingObserver<HttpResult<List<BannedDto>>>() {

                    @Override
                    public void _onNext(HttpResult<List<BannedDto>> data) {
                        if (data.isSuccess()) {
                            pagerAdapter.reFreshStickData(data.data, type);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    public RequestBody createRequestBody(int type) {
        Map map = new HashMap();
        map.put("channelId", channelId);
        map.put("type", type);
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        return requestBody;
    }

    public RequestBody createRemoveStickRequestBody(int channelId, int userId) {
        Map map = new HashMap();
        map.put("channelId", channelId);
        map.put("userId", userId);
        map.put("type", mCurrenType);
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        return requestBody;
    }

    private void removeStick(int channelId, int userId) {
        new RetrofitUtils().createApi(LiveApiService.class).removeStrick(createRemoveStickRequestBody(channelId, userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingObserver<HttpResult>() {

                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess())
                            getStickData(mCurrenType);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    private void removeRoomManager(int userId) {
        new RetrofitUtils().createApi(LiveApiService.class).removeRoomManager(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadingObserver<HttpResult>() {

                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess())
                            getRoomManager(mCurrenType);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_pop_manager);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    private void reSetView(int position) {
        if (position == 0) {
            tvDay.setVisibility(View.VISIBLE);
            tvWeek.setVisibility(View.INVISIBLE);
            tvMonth.setVisibility(View.INVISIBLE);
        } else if (position == 1) {
            tvDay.setVisibility(View.INVISIBLE);
            tvWeek.setVisibility(View.VISIBLE);
            tvMonth.setVisibility(View.INVISIBLE);
        } else {
            tvDay.setVisibility(View.INVISIBLE);
            tvWeek.setVisibility(View.INVISIBLE);
            tvMonth.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        reSetView(tab.getPosition());
        viewPager.setCurrentItem(tab.getPosition());
//        GradientTextView textView = (GradientTextView) tab.getCustomView();
//        textView.setTextColor(context.getResources().getColor(R.color.white));
//        textView.setColors(Color.parseColor("#D375FF"), Color.parseColor("#9372ED"));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
//        GradientTextView tv = (GradientTextView) tab.getCustomView();
//        tv.setTextColor(context.getResources().getColor(R.color.gray_666666));
//        int colorTrans = context.getResources().getColor(android.R.color.transparent);
//        tv.setColors(colorTrans, colorTrans);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
