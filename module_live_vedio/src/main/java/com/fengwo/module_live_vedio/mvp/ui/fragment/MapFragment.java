package com.fengwo.module_live_vedio.mvp.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.fengwo.module_comment.base.BaseMvpFragment;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.R2;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.MapPoiDto;
import com.fengwo.module_live_vedio.mvp.ui.df.GuildMsgDF;
import com.fengwo.module_live_vedio.mvp.ui.df.MapDialogF;
import com.fengwo.module_live_vedio.widget.mapview.MapView;

import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import butterknife.BindView;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/19
 */
public class MapFragment extends BaseMvpFragment implements MapView.OnPointClickLisenter {

    @BindView(R2.id.mapview)
    MapView mapview;
    GuildMsgDF guildMsgDF;

    public static Fragment newInstance(){
        MapFragment mapFragment = new MapFragment();
        return mapFragment;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_map;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {

        new RetrofitUtils().createApi(LiveApiService.class).getMapPoi()
                .compose(handleResult())
                .subscribe(new LoadingObserver<List<MapPoiDto>>(this) {
                    @Override
                    public void _onNext(List<MapPoiDto> data) {
//                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mapimages);
//                        mapview.initImageWH(bmp,MapFragment.this);
//                        mapview.setMapPoiEntityList(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
        mapview.post(new Runnable() {
            @Override
            public void run() {
            }
        });
    }


    @Override
    public void onPointClick(MapPoiDto mapPoiDto) {
//        if (guildMsgDF==null) {
//            guildMsgDF = new GuildMsgDF(mapPoiDto);
//        }
//        guildMsgDF.showDF(mapPoiDto,getChildFragmentManager(),GuildMsgDF.class.getName());
    }
}
