package com.fengwo.module_live_vedio.mvp.dto;

import java.util.List;

public class PacketResultBean {


    public int id;
    public Integer refRedpacketId;
    public Integer refUserId;
    public String refUserName;
    public Integer refMasterUserId;
    public String refMasterUserName;
    public Integer amount;
    public Integer redpacketType;
    public Integer redpacketChannel;
    public String createDatetime;
    public Integer nums;
    public List<RecordList> recordList;

    public static class RecordList {
        public int id;
        public Integer refRedpacketId;
        public Integer refUserId;
        public String refUserName;
        public Integer refMasterUserId;
        public String refMasterUserName;
        public Integer amount;
        public Integer redpacketType;
        public Integer redpacketChannel;
        public String createDatetime;
    }
}
