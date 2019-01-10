package com.iot.portal.report.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：设备活跃对象
 * 创建人： maochengyuan
 * 创建时间：2019/1/5 15:31
 * 修改人： maochengyuan
 * 修改时间：2019/1/5 15:31
 * 修改描述：
 */
@ApiModel(value = "设备活跃对象",description = "设备活跃对象")
public class DevcieActivateResp {

    @ApiModelProperty(value="昨日设备活跃数",name="yesDevcieActivate")
    private Long yesDevcieActivate = 0L;

    @ApiModelProperty(value="今日设备活跃数",name="todDevcieActivate")
    private Long todDevcieActivate = 0L;

    @ApiModelProperty(value="最近7日设备活跃数",name="lastWeekDevcieActivate")
    private Long lastWeekDevcieActivate = 0L;

    @ApiModelProperty(value="最近7日的前7日设备活跃数",name="beforeLastWeekDevcieActivate")
    private Long beforeLastWeekDevcieActivate = 0L;

    public DevcieActivateResp() {

    }

    public DevcieActivateResp(Long yesDevcieActivate, Long lastWeekDevcieActivate) {
        this.yesDevcieActivate = yesDevcieActivate;
        this.lastWeekDevcieActivate = lastWeekDevcieActivate;
    }

    public Long getYesDevcieActivate() {
        return yesDevcieActivate;
    }

    public void setYesDevcieActivate(Long yesDevcieActivate) {
        this.yesDevcieActivate = yesDevcieActivate;
    }

    public Long getTodDevcieActivate() {
        return todDevcieActivate;
    }

    public void setTodDevcieActivate(Long todDevcieActivate) {
        this.todDevcieActivate = todDevcieActivate;
    }

    public Long getLastWeekDevcieActivate() {
        return lastWeekDevcieActivate;
    }

    public void setLastWeekDevcieActivate(Long lastWeekDevcieActivate) {
        this.lastWeekDevcieActivate = lastWeekDevcieActivate;
    }

    public Long getBeforeLastWeekDevcieActivate() {
        return beforeLastWeekDevcieActivate;
    }

    public void setBeforeLastWeekDevcieActivate(Long beforeLastWeekDevcieActivate) {
        this.beforeLastWeekDevcieActivate = beforeLastWeekDevcieActivate;
    }

}
