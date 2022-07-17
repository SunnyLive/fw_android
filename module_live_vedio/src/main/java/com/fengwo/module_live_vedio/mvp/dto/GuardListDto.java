package com.fengwo.module_live_vedio.mvp.dto;

import java.util.ArrayList;
import java.util.List;

public class GuardListDto {

    public ArrayList<Guard> records;
    public int total;


    public class Guard {
        public int level;
        public int guardId;
        public String guardName;
        public String guardIconp;
        public int guardUserId;
        public String guardUserNickname;
        public String guardUserHeadImg;
        public String guardDeadline;
        public String levelIcon;
        public int guardUserLevel;
        public int guardUserSex;
    }
}


