package com.iot.report.dto.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "获取用户激活、活跃，设备激活、活跃")
@Data
public class UserDevActiveAndActivatedResp {

    @ApiModelProperty(value="今日设备活跃数",name="todDevcieActivate")
    private Long todDevcieActivate = 0L;

    @ApiModelProperty(value="今日设备激活数",name="todDevcieActivate")
    private Long todDevcieActivated = 0L;

    @ApiModelProperty(value="昨日设备活跃数",name="yesDevcieActivate")
    private Long yesDevcieActivate = 0L;

    @ApiModelProperty(value="昨日设备激活数",name="yesDevcieActivate")
    private Long yesDevcieActivated = 0L;

    @ApiModelProperty(value="今日用户活跃数",name="todUserActivate")
    private Long todUserActivate = 0L;

    @ApiModelProperty(value="今日用户激活数",name="todUserActivated")
    private Long todUserActivated = 0L;

    @ApiModelProperty(value="昨日用户活跃数",name="yesUserActivate")
    private Long yesUserActivate = 0L;

    @ApiModelProperty(value="昨日用户激活数",name="yesUserActivated")
    private Long yesUserActivated = 0L;

    public UserDevActiveAndActivatedResp(Long todDevcieActivate, Long todDevcieActivated, Long yesDevcieActivate, Long yesDevcieActivated, Long todUserActivate, Long todUserActivated, Long yesUserActivate, Long yesUserActivated) {
        this.todDevcieActivate = todDevcieActivate;
        this.todDevcieActivated = todDevcieActivated;
        this.yesDevcieActivate = yesDevcieActivate;
        this.yesDevcieActivated = yesDevcieActivated;
        this.todUserActivate = todUserActivate;
        this.todUserActivated = todUserActivated;
        this.yesUserActivate = yesUserActivate;
        this.yesUserActivated = yesUserActivated;
    }
}
