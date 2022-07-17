package com.fengwo.module_comment.event;

public class PaySuccessEvent {
    String experience;

    public PaySuccessEvent(String experience) {
        this.experience = experience;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
