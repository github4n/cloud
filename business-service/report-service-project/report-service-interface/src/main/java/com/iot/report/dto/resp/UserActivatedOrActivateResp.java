package com.iot.report.dto.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户激活、活跃返回实体类")
public class UserActivatedOrActivateResp {

    @ApiModelProperty(value="昨日用户激活/活跃数",name="yesUserActivated")
    private Long yesUserActivated = 0L;

    @ApiModelProperty(value="今日用户激活/活跃数",name="todUserActivated")
    private Long todUserActivated = 0L;

    @ApiModelProperty(value="最近7日用户激活/活跃数",name="lastWeekUserActivated")
    private Long lastWeekUserActivated = 0L;

    @ApiModelProperty(value="最近7日的前7日用户激活/活跃数",name="beforeLastWeekUserActivated")
    private Long beforeLastWeekUserActivated = 0L;

    public UserActivatedOrActivateResp(Long yesUserActivated, Long todUserActivated, Long lastWeekUserActivated, Long beforeLastWeekUserActivated) {
        this.yesUserActivated = yesUserActivated;
        this.todUserActivated = todUserActivated;
        this.lastWeekUserActivated = lastWeekUserActivated;
        this.beforeLastWeekUserActivated = beforeLastWeekUserActivated;
    }
}
