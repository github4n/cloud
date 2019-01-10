package com.iot.report.dto.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "用户活跃与用户返回实体类")
@Data
public class UserActiveAndActivatedResp {

    @ApiModelProperty(value="昨日用户活跃数",name="yesUserActivate")
    private Long yesUserActivate = 0L;

    @ApiModelProperty(value="昨日用户激活数",name="yesUserActivated")
    private Long yesUserActivated = 0L;

    @ApiModelProperty(value="今日用户活跃数",name="todUserActivate")
    private Long todUserActivate = 0L;

    @ApiModelProperty(value="今日用户激活数",name="todUserActivated")
    private Long todUserActivated = 0L;

    @ApiModelProperty(value="最近7日用户活跃数",name="lastWeekUserActivate")
    private Long lastWeekUserActivate = 0L;

    @ApiModelProperty(value="最近7日用户激活数",name="lastWeekUserActivated")
    private Long lastWeekUserActivated = 0L;

    @ApiModelProperty(value="最近7日的前7日用户活跃数",name="beforeLastWeekUserActivate")
    private Long beforeLastWeekUserActivate = 0L;

    @ApiModelProperty(value="最近7日的前7日用户激活数",name="beforeLastWeekUserActivated")
    private Long beforeLastWeekUserActivated = 0L;

    public UserActiveAndActivatedResp(Long yesUserActivate, Long yesUserActivated, Long todUserActivate, Long todUserActivated, Long lastWeekUserActivate, Long lastWeekUserActivated, Long beforeLastWeekUserActivate, Long beforeLastWeekUserActivated) {
        this.yesUserActivate = yesUserActivate;
        this.yesUserActivated = yesUserActivated;
        this.todUserActivate = todUserActivate;
        this.todUserActivated = todUserActivated;
        this.lastWeekUserActivate = lastWeekUserActivate;
        this.lastWeekUserActivated = lastWeekUserActivated;
        this.beforeLastWeekUserActivate = beforeLastWeekUserActivate;
        this.beforeLastWeekUserActivated = beforeLastWeekUserActivated;
    }
}
