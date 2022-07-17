package com.fengwo.module_live_vedio.mvp.dto;

import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/20
 */
public class CitySelectDto {
    /**
     * citys : [{"cityId":0,"cityName":"string"}]
     * num : 0
     */

    private int num;
    private List<CitysBean> citys;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<CitysBean> getCitys() {
        return citys;
    }

    public void setCitys(List<CitysBean> citys) {
        this.citys = citys;
    }

    public static class CitysBean {
        /**
         * cityId : 0
         * cityName : string
         */

        private int cityId;
        private String cityName;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }
    }
}
