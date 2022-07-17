package com.fengwo.module_comment;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.fengwo.module_comment.base.BaseApplication;

public class MapLocationUtil {

    private volatile static MapLocationUtil INSTANCE;
    private final AMapLocationClient client;
    public AMapLocation aMapLocation;
    private LocationListener mListener;

    private MapLocationUtil() {
        client = new AMapLocationClient(BaseApplication.mApp);
    }

    public static MapLocationUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (MapLocationUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MapLocationUtil();
                }
            }
        }
        return INSTANCE;
    }

    public AMapLocation getLocation() {
        return aMapLocation;
    }

    public String getLongitude() {
        return aMapLocation != null ? String.valueOf(aMapLocation.getLongitude()) : "0";
    }

    public String getLatitude() {
        return aMapLocation != null ? String.valueOf(aMapLocation.getLatitude()) : "0";
    }

    /**
     * 单次定位
     */
    public void startLocationForOnce(LocationListener listener) {
        if (aMapLocation == null || aMapLocation.getLatitude() == 0.0 || aMapLocation.getLongitude() == 0.0) {
            mListener = listener;
            AMapLocationClientOption option = new AMapLocationClientOption();
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            option.setOnceLocation(true);
            option.setNeedAddress(true);
            client.setLocationOption(option);
            client.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    MapLocationUtil.this.aMapLocation = aMapLocation;
                    if (aMapLocation.getErrorCode() == AMapLocation.LOCATION_SUCCESS) {
                        if (mListener != null) mListener.onLocationSuccess(aMapLocation);
                    } else {
                        if (mListener != null) mListener.onLocationFailure(aMapLocation.getDescription());
                    }
                    client.stopLocation();
                    client.unRegisterLocationListener(this);
                    mListener = null;
                }
            });
            client.startLocation();
        } else {
            if(listener!=null) listener.onLocationSuccess(aMapLocation);
        }
    }

    public void saveAMapLocation(AMapLocation location) {

    }

    public interface LocationListener {
        void onLocationSuccess(AMapLocation location);

        void onLocationFailure(String msg);
    }

}