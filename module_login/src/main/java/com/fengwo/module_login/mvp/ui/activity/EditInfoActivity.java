package com.fengwo.module_login.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
import com.fengwo.module_comment.base.BaseMvpActivity;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.UserInfo;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.HttpUtils;
import com.fengwo.module_comment.utils.LocationUtils;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxBus;
import com.fengwo.module_comment.utils.TimeUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_comment.utils.location.JsonBean;
import com.fengwo.module_comment.widget.ClearEditText;
import com.fengwo.module_comment.widget.GradientTextView;
import com.fengwo.module_login.R;
import com.fengwo.module_login.R2;
import com.fengwo.module_login.api.LoginApiService;
import com.fengwo.module_login.eventbus.UpdateUserinfo;
import com.fengwo.module_login.mvp.dto.NickNameRuleBean;
import com.fengwo.module_login.mvp.ui.pop.NobilityDialog;
import com.fengwo.module_login.utils.UserManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditInfoActivity extends BaseMvpActivity {
    private final static int TYPE_NAME = 1;
    private final static int TYPE_DES = 2;
    private final static int TYPE_SEX = 3;
    private final static int TYPE_ADDRESS = 4;
    private final static int TYPE_BIRTHDAY = 5;
    @BindView(R2.id.tv_to_recharge)
    TextView tvToRecharge;
    @BindView(R2.id.tv_rule_description)
    TextView tvRuleDescription;
    @BindView(R2.id.btn_confirm)
    GradientTextView btnConfirm;
    @BindView(R2.id.tv_error_tips)
    TextView tvErrorTips;

    public static void startForName(Activity context, int requestCode) {
        Intent i = new Intent(context, EditInfoActivity.class);
        i.putExtra("type", TYPE_NAME);
        context.startActivityForResult(i, requestCode);
    }

    public static void startForDes(Activity context, int requestCode) {
        Intent i = new Intent(context, EditInfoActivity.class);
        i.putExtra("type", TYPE_DES);
        context.startActivityForResult(i, requestCode);
    }

    public static void startForSex(Activity context, int requestCode) {
        Intent i = new Intent(context, EditInfoActivity.class);
        i.putExtra("type", TYPE_SEX);
        context.startActivityForResult(i, requestCode);
    }

    public static void startForAddress(Activity context, int requestCode) {
        Intent i = new Intent(context, EditInfoActivity.class);
        i.putExtra("type", TYPE_ADDRESS);
        context.startActivityForResult(i, requestCode);
    }

    public static void startForBirthday(Activity context, int requestCode) {
        Intent i = new Intent(context, EditInfoActivity.class);
        i.putExtra("type", TYPE_BIRTHDAY);
        context.startActivityForResult(i, requestCode);
    }

    @BindView(R2.id.root)
    View rootView;
    @BindView(R2.id.view_nickname)
    View viewnickname;
    @BindView(R2.id.et_name)
    ClearEditText etName;

    @BindView(R2.id.view_desc)
    View viewDesc;
    @BindView(R2.id.et_des)
    EditText etDes;
    @BindView(R2.id.tv_desc_count)
    TextView tvCount;

    @BindView(R2.id.ll_sex)
    View llSex;
    @BindView(R2.id.btn_boy)
    ImageView btnBoy;
    @BindView(R2.id.btn_girl)
    ImageView btnGirl;
    @BindView(R2.id.btn_secret)
    ImageView btnSecret;

    @BindView(R2.id.viewAddress)
    View addressView;
    @BindView(R2.id.wheelProvince)
    WheelView province;
    @BindView(R2.id.wheelCity)
    WheelView city;

    @BindView(R2.id.viewBirthday)
    View birthdayView;
    @BindView(R2.id.wheelYear)
    WheelView year;
    @BindView(R2.id.wheelMonth)
    WheelView month;
    @BindView(R2.id.wheelDay)
    WheelView day;

    private boolean isChangename = false;
    private int type;
    private UserInfo userInfo;
    private int padding;

    private List<String> provinceList;
    private List<List<JsonBean.CityBean>> cityList;

    private List<String> daysList = new ArrayList<>();
    private long nickNameCost = 0;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfo = UserManager.getInstance().getUser();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        userInfo = UserManager.getInstance().getUser();
    }

    @Override
    protected void initView() {
        padding = DensityUtils.dp2px(getApplicationContext(), 16);
        userInfo = UserManager.getInstance().getUser();
        type = getIntent().getIntExtra("type", -1);
//        setTitleBackground(getResources().getColor(R.color.white));
        String title = "";
        switch (type) {
            case TYPE_NAME:
                title = "昵称";
                viewnickname.setVisibility(View.VISIBLE);
                etName.setText(userInfo.nickname);
                btnConfirm.setVisibility(View.GONE);
                getNickNameRule();
                break;
            case TYPE_DES:
                title = "个人简介";
                viewDesc.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(userInfo.signature) && !TextUtils.equals(userInfo.signature, "这个人很懒，什么都没留下~")) {
                    etDes.setText(userInfo.signature);
                }
                break;
            case TYPE_SEX:
                title = "性别";
                setSexStatus(userInfo.sex);
                llSex.setVisibility(View.VISIBLE);
                break;
            case TYPE_ADDRESS:
                title = "所在地";
                addressView.setVisibility(View.VISIBLE);
                rootView.setBackgroundColor(getResources().getColor(R.color.normal_bg));
//                setTitleBackground(getResources().getColor(R.color.normal_bg));
                initAddress();
                break;
            case TYPE_BIRTHDAY:
                title = "生日/星座";
                birthdayView.setVisibility(View.VISIBLE);
                rootView.setBackgroundColor(getResources().getColor(R.color.normal_bg));
//                setTitleBackground(getResources().getColor(R.color.normal_bg));
                initBirthDay();
                break;
        }

        new ToolBarBuilder()
                .setTitle(title)
                .setTitleColor(R.color.text_33)
                .setBackIcon(R.drawable.ic_back_black)
                .showBack(true).build();

        etDes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvCount.setText(String.format("%d/50", s.length()));
                tvCount.setTextColor(s.length() == 50 ?
                        getResources().getColor(R.color.red_ff3333) :
                        getResources().getColor(R.color.text_66));
            }
        });
        etName.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            if (source.equals("\n")) return "";
            return null;
        }, new InputFilter.LengthFilter(10)});
    }

    @Override
    protected int getContentView() {
        return R.layout.login_activity_edituserinfo;
    }

    @OnClick({R2.id.btn_sex_secret, R2.id.btn_sex_women, R2.id.btn_sex_man, R2.id.btn_confirm, R2.id.tv_to_recharge, R2.id.btn_name_confirm})
    public void onViewClick(View view) {
        if (isFastClick()) return;
        int id = view.getId();
        if (id == R.id.btn_sex_secret) {
            userInfo.sex = 0;
            setSexStatus(0);
        } else if (id == R.id.btn_sex_women) {
            userInfo.sex = 2;
            setSexStatus(2);
        } else if (id == R.id.btn_sex_man) {
            userInfo.sex = 1;
            setSexStatus(1);
        } else if (id == R.id.btn_confirm) {
            if (type == TYPE_NAME) {
                changeNickCheck();
            } else if (type == TYPE_DES) {
                String des = etDes.getText().toString();
                if (TextUtils.isEmpty(des)) {
                    toastTip("请输入简介");
                    return;
                }
                userInfo.signature = des;
            } else if (type == TYPE_ADDRESS) {
                userInfo.location = String.format("%s/%s", provinceList.get(province.getCurrentItem()),
                        cityList.get(province.getCurrentItem()).get(city.getCurrentItem()).getName());
            } else if (type == TYPE_BIRTHDAY) {
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentDay = calendar.get(Calendar.DATE);
                if (year.getCurrentItem() + 1900 > currentYear ||
                        year.getCurrentItem() + 1900 == currentYear && month.getCurrentItem() > currentMonth ||
                        year.getCurrentItem() + 1900 == currentYear && month.getCurrentItem() == currentMonth && day.getCurrentItem() + 1 > currentDay) {
                    toastTip("生日不能晚于今天");
                    return;
                }
                calendar.set(Calendar.YEAR, year.getCurrentItem() + 1900);
                calendar.set(Calendar.MONTH, month.getCurrentItem());
                calendar.set(Calendar.DATE, day.getCurrentItem() + 1);
                userInfo.birthday = TimeUtils.dateToInstant(calendar.getTime());
                userInfo.constellation = TimeUtils.getAstro(month.getCurrentItem() + 1, day.getCurrentItem() + 1);
            }
            changeUserInfo();
        } else if (id == R.id.tv_to_recharge) {
            startActivity(ChongzhiActivity.class);
        } else if (id == R.id.btn_name_confirm) {
            if (changeNickCheck()) {
                modifyNickName();
            }
        }
    }

    private boolean changeNickCheck() {
        String nick = etName.getText().toString().trim();
        if (TextUtils.isEmpty(nick)) {
            toastTip("请输入昵称");
            return false;
        }
        if (userInfo.balance < nickNameCost) {
            showTypeDialog(NobilityDialog.TYPE_PAY_FAILURE);
            return false;
        }
//                if (nick.equals(userInfo.nickname)) {
//                    finish();
//                    return;
//                }
        isChangename = true;
        userInfo.nickname = nick;
        return true;
    }

    private void setSexStatus(int sex) {
        btnSecret.setSelected(sex == 0);
        int secretPadding = sex == 0 ? 0 : padding;
        btnSecret.setPadding(secretPadding, secretPadding, secretPadding, secretPadding);
        btnBoy.setSelected(sex == 1);
        int boyPadding = sex == 1 ? 0 : padding;
        btnBoy.setPadding(boyPadding, boyPadding, boyPadding, boyPadding);
        btnGirl.setSelected(sex == 2);
        int girlPadding = sex == 2 ? 0 : padding;
        btnGirl.setPadding(girlPadding, girlPadding, girlPadding, girlPadding);
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

    private void getNickNameRule() {
        new RetrofitUtils().createApi(LoginApiService.class)
                .getNickNameRule()
                .compose(io_main())
                .compose(bindToLifecycle())
                .subscribe(new LoadingObserver<HttpResult<NickNameRuleBean>>() {
                    @Override
                    public void _onNext(HttpResult<NickNameRuleBean> data) {
                        nickNameCost = Long.parseLong(data.data.getModifyNameCost());
                        tvToRecharge.setText(data.data.getModifyNameCost() + "花钻" + "    去充值>");
                        StringBuilder builder = new StringBuilder();
                        for (String s : data.data.getRules()) {
                            builder.append(s + "\n");
                        }
                        tvRuleDescription.setText(builder.toString());
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    private void modifyNickName() {
        new RetrofitUtils().createApi(LoginApiService.class)
                .modifyNickName(userInfo.nickname)
                .compose(io_main())
                .compose(bindToLifecycle())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            userInfo.balance -= nickNameCost;//跟新余额
                            UserManager.getInstance().setUserInfo(userInfo);
                            RxBus.get().post(new UpdateUserinfo());
                            setResult(RESULT_OK);
                            finish();
                        } else if (data.isServiceErr()) {
                            ToastUtils.showShort(EditInfoActivity.this, data.description);
                            tvErrorTips.setText(data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    private void changeUserInfo() {
        HttpUtils.ParamsBuilder builder;
        if (type == TYPE_DES) {//拆开提交  signature被后台限制 一起提交会导致其他更新失败
            builder = new HttpUtils.ParamsBuilder()
                    .put("signature", userInfo.signature);
        } else {
            builder = new HttpUtils.ParamsBuilder()
                    .put("sex", userInfo.sex + "")
                    .put("location", userInfo.location)
                    .put("headImg", userInfo.headImg)
                    .put("birthday", userInfo.birthday);
        }
        if (isChangename&&type != TYPE_DES) {
            builder.put("nickname", userInfo.nickname);
            isChangename = false;
        }
        new RetrofitUtils().createApi(LoginApiService.class)
                .updateUserinfo(builder.build())
                .compose(io_main())
                .compose(bindToLifecycle())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        if (data.isSuccess()) {
                            UserManager.getInstance().setUserInfo(userInfo);
                            RxBus.get().post(new UpdateUserinfo());
                            setResult(RESULT_OK);
                            finish();
                        } else if (data.isServiceErr()) {
                            ToastUtils.showShort(EditInfoActivity.this, data.description);
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                    }
                });
    }

    private void initAddress() {
        provinceList = new ArrayList<>();
        cityList = new ArrayList<>();
        List<JsonBean> list = LocationUtils.getLocationList(this);



        for (int i = 0; i < list.size(); i++) {
            if(!TextUtils.equals("全部",list.get(i).getName())){
                provinceList.add(list.get(i).getName());
                cityList.add(list.get(i).getCityList());
            }
        }

        province.setAdapter(new ArrayWheelAdapter(provinceList));
        province.setCyclic(false);
        province.setOnItemSelectedListener(index -> {
            int cityItem = city.getCurrentItem();
            ArrayWheelAdapter adapter = new ArrayWheelAdapter(cityList.get(index));
            city.setAdapter(adapter);
            city.setCurrentItem(cityItem >= cityList.get(index).size() ? cityList.get(index).size() : cityItem);
        });
        province.setCurrentItem(0);
        city.setCyclic(false);
        city.setAdapter(new ArrayWheelAdapter(cityList.get(0)));
    }

    private void initBirthDay() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DATE);

        ArrayList<String> yearsList = new ArrayList<>();
        ArrayList<String> monthList = new ArrayList<>();
        for (int i = 1900; i <= 2100; i++) {
            yearsList.add(String.format("%d年", i));
        }
        for (int i = 0; i < 12; i++) {
            monthList.add(String.format("%d月", i + 1));
        }
        year.setCyclic(true);
        year.setAdapter(new ArrayWheelAdapter(yearsList));
        year.setOnItemSelectedListener(index -> {
            getDays(calendar, 1900 + index, month.getCurrentItem());
            day.setAdapter(new ArrayWheelAdapter(daysList));
            int oldPosition = day.getCurrentItem();
            day.setCurrentItem(oldPosition < daysList.size() ? oldPosition : daysList.size() - 1);
        });
        month.setCyclic(true);
        month.setAdapter(new ArrayWheelAdapter(monthList));
        month.setOnItemSelectedListener(index -> {
            getDays(calendar, 1900 + year.getCurrentItem(), index);
            day.setAdapter(new ArrayWheelAdapter(daysList));
            int oldPosition = day.getCurrentItem();
            day.setCurrentItem(oldPosition < daysList.size() ? oldPosition : daysList.size() - 1);
        });
        getDays(calendar, currentYear, currentMonth);
        day.setCyclic(true);
        day.setAdapter(new ArrayWheelAdapter(daysList));
        year.setCurrentItem(currentYear - 1900);
        month.setCurrentItem(currentMonth);
        day.setCurrentItem(currentDay - 1);
    }

    private void getDays(Calendar calendar, int year, int month) {
        daysList.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.roll(Calendar.DAY_OF_MONTH, -1);
        int days = calendar.get(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < days; i++) {
            daysList.add(String.format("%d日", i + 1));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}