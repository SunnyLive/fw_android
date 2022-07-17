package com.fengwo.module_live_vedio.mvp.dto;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class WishingWallDto {

    private List<HourProcessesBean> hourProcesses;
    private List<ResidueProcessesBean> residueProcesses;

    public  List<HourProcessesBean> getHourProcesses() {
        return hourProcesses;
    }

    public void setHourProcesses(List<HourProcessesBean> hourProcesses) {
        this.hourProcesses = hourProcesses;
    }

    public List<ResidueProcessesBean> getResidueProcesses() {
        return residueProcesses;
    }

    public void setResidueProcesses(List<ResidueProcessesBean> residueProcesses) {
        this.residueProcesses = residueProcesses;
    }

    public static class HourProcessesBean {
        /**
         * anchroHeadImg : string
         * anchroName : string
         * begin : string
         * content : string
         * dateHour : string
         * end : string
         * endTime : string
         * icon : string
         * integral : 0
         * status : true
         * userHeadImg : string
         * userName : string
         */

        private String anchorHeadImg;
        private String anchroHeadImg;
        private String anchorName;
        private String anchroName;
        private String anchorId;
        private String begin;
        private String content;
        private String dateHour;
        private String end;
        private String endTime;
        private String icon;
        private int integral;
        private boolean status;
        private String userHeadImg;
        private String userName;
        private List<ResidueProcessesBean> dateList;

        public List<ResidueProcessesBean> getDateList() {
            return dateList == null ? dateList = new ArrayList<>() : dateList;
        }


        public String getAnchorHeadImg() {
            return TextUtils.isEmpty(anchorHeadImg) ? anchroHeadImg : anchorHeadImg;
        }

        public void setAnchorHeadImg(String anchorHeadImg) {
            this.anchorHeadImg = anchorHeadImg;
        }

        public String getAnchorName() {
            return TextUtils.isEmpty(anchorName) ? anchroName : anchorName;
        }

        public void setAnchorName(String anchorName) {
            this.anchorName = anchorName;
        }

        public String getAnchorId() {
            return anchorId;
        }

        public void setAnchorId(String anchorId) {
            this.anchorId = anchorId;
        }

        public String getBegin() {
            return begin;
        }

        public void setBegin(String begin) {
            this.begin = begin;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDateHour() {
            return dateHour;
        }

        public void setDateHour(String dateHour) {
            this.dateHour = dateHour;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getUserHeadImg() {
            return userHeadImg;
        }

        public void setUserHeadImg(String userHeadImg) {
            this.userHeadImg = userHeadImg;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

    public static class ResidueProcessesBean {
        private String end;
        private String begin;

        public ResidueProcessesBean() {
        }

        public ResidueProcessesBean(String begin, String end) {
            this.end = end;
            this.begin = begin;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getBegin() {
            return begin;
        }

        public void setBegin(String begin) {
            this.begin = begin;
        }
    }
}
