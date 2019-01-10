package com.iot.report.dto.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：报表服务
 * 功能描述：设备激活活跃对象
 * 创建人： mao2080@sina.com
 * 创建时间：2019/1/5 15:31
 * 修改人： mao2080@sina.com
 * 修改时间：2019/1/5 15:31
 * 修改描述：
 */
@ApiModel(value = "设备激活活跃对象",description = "设备激活活跃对象")
@Data
public class DevActivateResp {

    @ApiModelProperty(value="昨日设备活跃数",name="yesDevcieActivate")
    private Long yesDevcieActivate = 0L;

    @ApiModelProperty(value="昨日设备激活数",name="yesDevcieActivate")
    private Long yesDevcieActivated = 0L;

    @ApiModelProperty(value="今日设备活跃数",name="todDevcieActivate")
    private Long todDevcieActivate = 0L;

    @ApiModelProperty(value="今日设备激活数",name="todDevcieActivate")
    private Long todDevcieActivated = 0L;

    @ApiModelProperty(value="最近7日设备活跃数",name="lastWeekDevcieActivate")
    private Long lastWeekDevcieActivate = 0L;

    @ApiModelProperty(value="最近7日设备激活数",name="lastWeekDevcieActivate")
    private Long lastWeekDevcieActivated = 0L;

    @ApiModelProperty(value="最近7日的前7日设备活跃数",name="beforeLastWeekDevcieActivate")
    private Long beforeLastWeekDevcieActivate = 0L;

    @ApiModelProperty(value="最近7日的前7日设备激活数",name="beforeLastWeekDevcieActivate")
    private Long beforeLastWeekDevcieActivated = 0L;

    public DevActivateResp() {
    }

    public DevActivateResp(Long yesDevcieActivate, Long yesDevcieActivated, Long todDevcieActivate, Long todDevcieActivated, Long lastWeekDevcieActivate, Long lastWeekDevcieActivated, Long beforeLastWeekDevcieActivate, Long beforeLastWeekDevcieActivated) {
        this.yesDevcieActivate = yesDevcieActivate;
        this.yesDevcieActivated = yesDevcieActivated;
        this.todDevcieActivate = todDevcieActivate;
        this.todDevcieActivated = todDevcieActivated;
        this.lastWeekDevcieActivate = lastWeekDevcieActivate;
        this.lastWeekDevcieActivated = lastWeekDevcieActivated;
        this.beforeLastWeekDevcieActivate = beforeLastWeekDevcieActivate;
        this.beforeLastWeekDevcieActivated = beforeLastWeekDevcieActivated;
    }
}
