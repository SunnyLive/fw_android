package com.fengwo.module_flirt.UI.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocation;
import com.fengwo.module_chat.mvp.api.ChatService;
import com.fengwo.module_comment.ArouterApi;
import com.fengwo.module_comment.MapLocationUtil;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.R2;
import com.fengwo.module_flirt.UI.fragment.ContactFragment;
import com.fengwo.module_flirt.bean.ContactsNumBean;
import com.fengwo.module_flirt.bean.event.ContactsNumRefreshEvent;
import com.fengwo.module_flirt.bean.event.OrderSortEvent;
import com.fengwo.module_flirt.dialog.OrderSortDialogWindow;
import com.google.gson.Gson;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.LayoutBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

@Route(path = ArouterApi.CONTACT_HOME)
public class ContactHomeActivity extends BaseMvpActivity {

    private String[] titles = {"好友 %s", "关注 %s", "粉丝 %s"};

    @BindView(R2.id.tabLayout)
    ScrollIndicatorView tabLayout;
    @BindView(R2.id.vp)
    ViewPager vp;

    private int orderby = 1;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        compositeDisposable = new CompositeDisposable();
        setTitleBackground(getResources().getColor(R.color.white));
        new ToolBarBuilder().showBack(true)
                .setBackIcon(R.drawable.ic_back_black)
                .setTitle("联系人")
                .setTitleColor(R.color.text_33)
                .setRight1Img(R.drawable.ic_ordersort, view -> {
                    OrderSortDialogWindow sortDialogWindow = new OrderSortDialogWindow(ContactHomeActivity.this, true, orderby);
                    sortDialogWindow.addOnClickListener(order -> {
                        orderby = order;
                        RxBus.get().post(new OrderSortEvent(order));
                    });
                    sortDialogWindow.showPopupWindow();
                })
                .build();

        observeContactsNum();

        showLoadingDialog();
        getContactNums();
    }

    @SuppressLint("CheckResult")
    private void observeContactsNum() {
        RxBus.get().toObservable(ContactsNumRefreshEvent.class).subscribe(contactsNumRefreshEvent -> {
            showLoadingDialog();
            ChatService service = new RetrofitUtils().createApi(ChatService.class);
            Flowable<HttpResult> getFriendsNum = service.getMyFriendsNum();
            Flowable<HttpResult> getAttentionNum = service.getMyAttentionNum();
            Flowable<HttpResult> getFans = service.getMyFansNum();
            compositeDisposable.add(Flowable.combineLatest(getFriendsNum, getAttentionNum, getFans, (friendsNumResult, attentionNumResult, fansResult) -> {
                        ContactsNumBean numBean = new ContactsNumBean();
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(friendsNumResult.data));
                        numBean.setFriendsNum((int) Math.round(jsonObject.getDouble("count")));
                        JSONObject jsonObject2 = new JSONObject(new Gson().toJson(attentionNumResult.data));
                        numBean.setAttentionNum((int) Math.round(jsonObject2.getDouble("attention")));
                        JSONObject jsonObject3 = new JSONObject(new Gson().toJson(fansResult.data));
                        numBean.setFansNum((int) Math.round(jsonObject3.getDouble("fans")));
                        return numBean;
                    }
                    ).compose(io_main())
                            .subscribeWith(new LoadingObserver<ContactsNumBean>() {
                                @Override
                                public void _onNext(ContactsNumBean data) {
                                    hideLoadingDialog();
                                    ((TextView) Objects.requireNonNull(tabLayout.getItemView(0))).setText(String.format(titles[0], formatNums(data.getFriendsNum())));//data.getFriendsNum()
                                    ((TextView) Objects.requireNonNull(tabLayout.getItemView(1))).setText(String.format(titles[1], formatNums(data.getAttentionNum())));
                                    ((TextView) Objects.requireNonNull(tabLayout.getItemView(2))).setText(String.format(titles[2], formatNums(data.getFansNum())));
                                }

                                @Override
                                public void _onError(String msg) {
                                    hideLoadingDialog();
                                    ToastUtils.showShort(ContactHomeActivity.this, msg);
                                }
                            })
            );
        });
    }

    @SuppressLint("DefaultLocale")
    private String formatNums(int num) {
        if (num <= 99)
            return num + "";
        return "99+";
    }

    private void setScrollIndicator(ContactsNumBean data) {
        vp.setOffscreenPageLimit(fragmentList.size());
        int selectColor = Color.WHITE;
        int unSelectColor = getResources().getColor(R.color.text_99);
        tabLayout.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(15, 13));
        tabLayout.setSplitAuto(false);
        tabLayout.setScrollBar(new LayoutBar(getApplicationContext(), R.layout.layout_contact_home_slidebar, ScrollBar.Gravity.CENTENT_BACKGROUND));

        IndicatorViewPager pager = new IndicatorViewPager(tabLayout, vp);
        pager.setClickIndicatorAnim(false);
        pager.setAdapter(new IndicatorViewPager.IndicatorFragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public View getViewForTab(int position, View convertView, ViewGroup container) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(ContactHomeActivity.this).inflate(R.layout.chat_item_home_tab, container, false);
                }
                TextView textView = (TextView) convertView;
                switch (position) {
                    case 0:
                        textView.setText(String.format(titles[position], data.getFriendsNum()));
                        break;
                    case 1:
                        textView.setText(String.format(titles[position], data.getAttentionNum()));
                        break;
                    case 2:
                        textView.setText(String.format(titles[position], data.getFansNum()));
                        break;
                }
                int width = getTextWidth(textView);
                int padding = DensityUtils.dp2px(ContactHomeActivity.this, 25);
                //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
                textView.setWidth((int) (width * 1.4f) + padding);
                return convertView;
            }

            @Override
            public Fragment getFragmentForPage(int position) {
                return fragmentList.get(position);
            }

            private int getTextWidth(TextView textView) {
                if (textView == null) {
                    return 0;
                }
                Rect bounds = new Rect();
                String text = textView.getText().toString();
                Paint paint = textView.getPaint();
                paint.getTextBounds(text, 0, text.length(), bounds);
                return bounds.left + bounds.width();
            }
        });
    }

    //    @SuppressLint("CheckResult")
    private void requestLocationPermission(ContactsNumBean data) {
//        new RxPermissions(this).request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
//                .compose(bindToLifecycle()).subscribe(aBoolean -> {
//            //todo 没有权限就申请 申请不管拒绝还是接受 都直接定位处理 在location中onLocationFailure再处理
        startLocation(data);
//        });
    }

    private void startLocation(ContactsNumBean data) {
        MapLocationUtil.getInstance().startLocationForOnce(new MapLocationUtil.LocationListener() {
            @Override
            public void onLocationSuccess(AMapLocation location) {
                fragmentList.add(ContactFragment.newInstance(ContactFragment.TYPE_FRIENDS, location));
                fragmentList.add(ContactFragment.newInstance(ContactFragment.TYPE_ATTENTION, location));
                fragmentList.add(ContactFragment.newInstance(ContactFragment.TYPE_FANS, location));
                setScrollIndicator(data);
            }

            @Override
            public void onLocationFailure(String msg) {
                fragmentList.add(ContactFragment.newInstance(ContactFragment.TYPE_FRIENDS, null));
                fragmentList.add(ContactFragment.newInstance(ContactFragment.TYPE_ATTENTION, null));
                fragmentList.add(ContactFragment.newInstance(ContactFragment.TYPE_FANS, null));
                setScrollIndicator(data);
            }
        });
    }

    private CompositeDisposable compositeDisposable;

    private void getContactNums() {
        ChatService service = new RetrofitUtils().createApi(ChatService.class);
        Flowable<HttpResult> getFriendsNum = service.getMyFriendsNum();
        Flowable<HttpResult> getAttentionNum = service.getMyAttentionNum();
        Flowable<HttpResult> getFans = service.getMyFansNum();
        compositeDisposable.add(Flowable.combineLatest(getFriendsNum, getAttentionNum, getFans, (friendsNumResult, attentionNumResult, fansResult) -> {
                    ContactsNumBean numBean = new ContactsNumBean();
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(friendsNumResult.data));
                    numBean.setFriendsNum((int) Math.round(jsonObject.getDouble("count")));
                    JSONObject jsonObject2 = new JSONObject(new Gson().toJson(attentionNumResult.data));
                    numBean.setAttentionNum((int) Math.round(jsonObject2.getDouble("attention")));
                    JSONObject jsonObject3 = new JSONObject(new Gson().toJson(fansResult.data));
                    numBean.setFansNum((int) Math.round(jsonObject3.getDouble("fans")));
                    return numBean;
                }
                ).compose(io_main())
                        .subscribeWith(new LoadingObserver<ContactsNumBean>() {
                            @Override
                            public void _onNext(ContactsNumBean data) {
                                hideLoadingDialog();
                                requestLocationPermission(data);
                            }

                            @Override
                            public void _onError(String msg) {
                                hideLoadingDialog();
                                ToastUtils.showShort(ContactHomeActivity.this, msg);
                            }
                        })
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable == null) return;
        compositeDisposable.isDisposed();
        compositeDisposable.clear();
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_contact_home;
    }
}
