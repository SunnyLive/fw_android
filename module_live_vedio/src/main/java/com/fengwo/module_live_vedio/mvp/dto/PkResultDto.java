package com.fengwo.module_live_vedio.mvp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GuiLianL
 * @intro
 * @date 2019/11/7
 */
public class PkResultDto implements Serializable {
    private int result = 0;//1、胜，0、平，-1、负 (本场）
    private TeamPkResultDto teamPkResultDto; // （团队数据)
    private SinglePkResultDto singlePkResultDto; // （单人数据)

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public TeamPkResultDto getTeamPkResultDto() {
        return teamPkResultDto;
    }

    public void setTeamPkResultDto(TeamPkResultDto teamPkResultDto) {
        this.teamPkResultDto = teamPkResultDto;
    }

    public SinglePkResultDto getSinglePkResultDto() {
        return singlePkResultDto;
    }

    public void setSinglePkResultDto(SinglePkResultDto singlePkResultDto) {
        this.singlePkResultDto = singlePkResultDto;
    }
}
