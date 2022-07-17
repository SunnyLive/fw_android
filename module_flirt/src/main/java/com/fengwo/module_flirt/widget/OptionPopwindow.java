package com.fengwo.module_flirt.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.LocationUtils;
import com.fengwo.module_comment.utils.location.JsonBean;
import com.fengwo.module_flirt.R;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * 筛选
 */
public class OptionPopwindow extends BasePopupWindow {


    @Autowired
    UserProviderService userProviderService;
    Context context;


    private List<String> provinceList;
    private List<List<JsonBean.CityBean>> cityList;

    WheelView province;
    WheelView city;
    private int sex = 3, age = -1; //0：保密，1：男；2：女 3：不限

    public OptionPopwindow(Context context, int sexA) {
        super(context);
        this.context = context;
        setPopupGravity(Gravity.BOTTOM);
        province = findViewById(R.id.wheelProvince);
        city = findViewById(R.id.wheelCity);
        TextView tv_confirm = findViewById(R.id.tv_confirm);
        ImageView im_clone = findViewById(R.id.im_clone);
        RadioGroup rg_sex = findViewById(R.id.rg_sex);
        RadioGroup rg_age = findViewById(R.id.rg_age);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provinceList.get(province.getCurrentItem());
                if (null != onClickListener) {
                    //int sex, int minAge, int maxAge, String city
                    int minAge = 0, maxAge = 100;
                    if (age == -1) {
                        minAge = 0;
                        maxAge = 100;
                    } else if (age == 0) {
                        minAge = 18;
                        maxAge = 25;
                    } else if (age == 1) {
                        minAge = 25;
                        maxAge = 30;
                    } else if (age == 2) {
                        minAge = 30;
                        maxAge = 35;
                    } else if (age == 3) {
                        minAge = 35;
                        maxAge = 100;
                    }
                    if ("不限".equals(provinceList.get(province.getCurrentItem()))) {
                        onClickListener.complete(sex, minAge, maxAge, "");
                    } else {
                        String cityStr = cityList.get(province.getCurrentItem()).get(city.getCurrentItem()).getName();
//                        String cityStr = provinceList.get(province.getCurrentItem()) + " " + cityList.get(province.getCurrentItem()).get(city.getCurrentItem()).getName();
                        onClickListener.complete(sex, minAge, maxAge, cityStr);
                    }
                }
            }
        });
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_sex1) {
                    sex = 1;
                } else if (i == R.id.rb_sex2) {
                    sex = 2;
                } else if (i == R.id.rb_sex3) {
                    sex = 3;
                }
            }
        });
        rg_age.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_age1) {
                    age = 0;
                } else if (i == R.id.rb_age2) {
                    age = 1;
                } else if (i == R.id.rb_age3) {
                    age = 2;
                } else if (i == R.id.rb_age4) {
                    age = 3;
                }
            }
        });
        im_clone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        initAddress();


    }

    private void initAddress() {
        provinceList = new ArrayList<>();
        cityList = new ArrayList<>();
        List<JsonBean> list = LocationUtils.getLocationList(context);

        //添加不限城市选项
        provinceList.add("不限");
        List<JsonBean.CityBean> unlimitedCitys = new ArrayList<>(1);
        JsonBean.CityBean unlimitedCity = new JsonBean.CityBean();
        unlimitedCity.setName("不限");
        unlimitedCitys.add(unlimitedCity);
        cityList.add(unlimitedCitys);

        for (int i = 0; i < list.size(); i++) {
            if (!TextUtils.equals("全部", list.get(i).getName())) {
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

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popwindow_option);
        return v;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }

    public OnClickListener onClickListener;

    public interface OnClickListener {
        void complete(int sex, int minAge, int maxAge, String city);
    }

    public void addOnClickListener(OnClickListener listener) {
        onClickListener = listener;
    }

}
