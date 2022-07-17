package com.fengwo.module_live_vedio.mvp.ui.df;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.CitySelectDto;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/20
 */
public class CitySelectDF extends BaseDialogFragment {

    private TextView tvTimes,tvSure;
    private RecyclerView rvCity;
    private CityAdapter cityAdapter;

    public int otherCityId,otherGuildId;
    private int cityId;

    @Autowired
    UserProviderService providerService;

    public static DialogFragment getInstance(int otherCityId, int otherGuildId){
        CitySelectDF citySelectDF = new CitySelectDF();
        Bundle bundle  = new Bundle();
        bundle.putInt("otherCityId",otherCityId);
        bundle.putInt("otherGuildId",otherGuildId);
        citySelectDF.setArguments(bundle);
        return citySelectDF;
    }

    @Override
    protected void initView() {
        otherCityId = getArguments().getInt("otherCityId");
        otherGuildId = getArguments().getInt("otherGuildId");
         tvTimes = findViewById(R.id.tv_remind_times);
         tvSure = findViewById(R.id.tv_sure);
         rvCity = findViewById(R.id.recyclerview);
        rvCity.setLayoutManager(new LinearLayoutManager(getActivity()));
        getData();

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guildChallenge();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.dialog_city_select;
    }

    private void getData(){
        new RetrofitUtils().createApi(LiveApiService.class)
                .getGuildCityList(String.valueOf(providerService.getUserInfo().familyId))
                .compose(RxUtils.handleResult2())
                .subscribe(new LoadingObserver<CitySelectDto>() {
                    @Override
                    public void _onNext(CitySelectDto data) {
                       setData(data.getCitys());
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    private void guildChallenge(){
        Map map = new HashMap();
        map.put("cityId",cityId);
        map.put("guildId",providerService.getUserInfo().familyId);
        map.put("otherCityId",otherCityId);
        map.put("otherGuildId",otherGuildId);
        new RetrofitUtils().createApi(LiveApiService.class)
                .guildChallenge(createRequestBody(map))
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        ToastUtils.showShort(getActivity(),data.description);
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showShort(getActivity(),msg);
                    }
                });
    }

    private void setData(List<CitySelectDto.CitysBean> data) {
        cityAdapter = new CityAdapter(data);
        rvCity.setAdapter(cityAdapter);
        cityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0;i<cityAdapter.getData().size();i++){
                    if (cityAdapter.getData().get(i).isSelected()){
                        cityAdapter.getData().get(i).setSelected(false);
                        break;
                    }
                }
                cityAdapter.getData().get(position).setSelected(!cityAdapter.getData().get(position).isSelected());
                cityId = cityAdapter.getData().get(position).getCityId();
                cityAdapter.notifyDataSetChanged();
            }
        });
    }

    private class CityAdapter extends BaseQuickAdapter<CitySelectDto.CitysBean,BaseViewHolder>{

        public CityAdapter(@Nullable List<CitySelectDto.CitysBean> data) {
            super(R.layout.item_city_select,data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, CitySelectDto.CitysBean item) {
            helper.setText(R.id.tv_item_city_name,item.getCityName());
//            ImageLoader.loadImg(helper.getView(R.id.iv_city_icon),item.getCityIcon());
            helper.getView(R.id.iv_item_city_select).setSelected(item.isSelected());
        }
    }

    public RequestBody createRequestBody(Map map) {
        String json = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        return requestBody;
    }
}
