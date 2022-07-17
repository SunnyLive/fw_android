package com.fengwo.module_flirt.bean;

import java.util.List;

public class ZipLabelParentDto {
    private List<CerTagBean> cerTags;
    private List<LabelTalentDto> labelTalents;

    public ZipLabelParentDto() {
    }

    public List<CerTagBean> getCerTags() {
        return cerTags;
    }

    public void setCerTags(List<CerTagBean> cerTags) {
        this.cerTags = cerTags;
    }

    public List<LabelTalentDto> getLabelTalents() {
        return labelTalents;
    }

    public void setLabelTalents(List<LabelTalentDto> labelTalents) {
        this.labelTalents = labelTalents;
    }
}
