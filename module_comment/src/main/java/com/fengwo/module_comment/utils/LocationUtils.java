package com.fengwo.module_comment.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.contrarywind.interfaces.IPickerViewData;
import com.fengwo.module_comment.utils.location.JsonBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LocationUtils {


    public static List<JsonBean> getLocationList(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open("province.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson g = new Gson();
        List<JsonBean> list = g.fromJson(stringBuilder.toString(), new TypeToken<List<JsonBean>>() {
        }.getType());
        return list;
    }

    public static void showLocationDialog(Context mContext, boolean type,OnSelectedListener l) {
        List<String> provinceList = new ArrayList<>();
        List<List<JsonBean.CityBean>> cityList = new ArrayList<>();
//        List<List<List<String>>> areaList = new ArrayList<>();
        List<JsonBean> list = getLocationList(mContext);
        for (int i = 0; i < list.size(); i++) {
            if(type){
                provinceList.add(list.get(i).getName());
                cityList.add(list.get(i).getCityList());
            }else
            if(!list.get(i).getName().equals("全部")){
                provinceList.add(list.get(i).getName());
                cityList.add(list.get(i).getCityList());
            }

        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx;
                if(type){
                     tx = provinceList.get(options1) + "/" + cityList.get(options1).get(option2).getName();
                }else {
                     tx = provinceList.get(options1) + "." + cityList.get(options1).get(option2).getName();
                }

                l.onSelected(tx);
            }
        }).build();
        pvOptions.setPicker(provinceList, cityList);
        pvOptions.show();
    }

    /**
     * 获取市
     * @param mContext
     * @param l
     */
    public static void showLocationCity(Context mContext, OnSelectedListener l) {
        List<String> provinceList = new ArrayList<>();
        List<List<JsonBean.CityBean>> cityList = new ArrayList<>();
//        List<List<List<String>>> areaList = new ArrayList<>();
        List<JsonBean> list = getLocationList(mContext);
        for (int i = 0; i < list.size(); i++) {
            provinceList.add(list.get(i).getName());
            cityList.add(list.get(i).getCityList());
        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = cityList.get(options1).get(option2).getName();

                l.onSelected(tx);
            }
        }).build();
        pvOptions.setPicker(provinceList, cityList);
        pvOptions.show();
    }
    public interface OnSelectedListener {
        void onSelected(String location);
    }
}
