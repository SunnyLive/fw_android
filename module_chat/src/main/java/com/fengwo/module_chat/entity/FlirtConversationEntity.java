package com.fengwo.module_chat.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FlirtConversationEntity {

    @Id
    private Long id;

    /**
     * 未讀消息數量
     */
    private int unReadMsgCount;

    /**
     * 對方id
     */
    private String otherUserId;

    @Generated(hash = 804683534)
    public FlirtConversationEntity(Long id, int unReadMsgCount,
            String otherUserId) {
        this.id = id;
        this.unReadMsgCount = unReadMsgCount;
        this.otherUserId = otherUserId;
    }

    @Generated(hash = 149130003)
    public FlirtConversationEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUnReadMsgCount() {
        return this.unReadMsgCount;
    }

    public void setUnReadMsgCount(int unReadMsgCount) {
        this.unReadMsgCount = unReadMsgCount;
    }

    public String getOtherUserId() {
        return this.otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }
    
}
