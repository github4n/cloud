package com.iot.portal.report.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：设备激活对象
 * 创建人： maochengyuan
 * 创建时间：2019/1/5 15:31
 * 修改人： maochengyuan
 * 修改时间：2019/1/5 15:31
 * 修改描述：
 */
@ApiModel(value = "设备激活对象",description = "设备激活对象")
public class DevcieActivatedResp {

    @ApiModelProperty(value="昨日设备激活数",name="yesDevcieActivated")
    private Long yesDevcieActivated = 0L;

    @ApiModelProperty(value="今日设备激活数",name="todDevcieActivated")
    private Long todDevcieActivated = 0L;

    @ApiModelProperty(value="最近7日设备激活数",name="lastWeekDevcieActivated")
    private Long lastWeekDevcieActivated = 0L;

    @ApiModelProperty(value="最近7日的前7日设备激活数",name="beforeLastWeekDevcieActivated")
    private Long beforeLastWeekDevcieActivated = 0L;

    @ApiModelProperty(value="总激活数",name="totalDevcie")
    private Long totalDevcie = 0L;

    public DevcieActivatedResp() {

    }

    public DevcieActivatedResp(Long totalDevcie) {
        this.totalDevcie = totalDevcie;
    }

    public DevcieActivatedResp(Long yesDevcieActivated, Long lastWeekDevcieActivated, Long totalDevcie) {
        this.yesDevcieActivated = yesDevcieActivated;
        this.lastWeekDevcieActivated = lastWeekDevcieActivated;
        this.totalDevcie = totalDevcie;
    }

    public Long getYesDevcieActivated() {
        return yesDevcieActivated;
    }

    public void setYesDevcieActivated(Long yesDevcieActivated) {
        this.yesDevcieActivated = yesDevcieActivated;
    }

    public Long getTodDevcieActivated() {
        return todDevcieActivated;
    }

    public void setTodDevcieActivated(Long todDevcieActivated) {
        this.todDevcieActivated = todDevcieActivated;
    }

    public Long getLastWeekDevcieActivated() {
        return lastWeekDevcieActivated;
    }

    public void setLastWeekDevcieActivated(Long lastWeekDevcieActivated) {
        this.lastWeekDevcieActivated = lastWeekDevcieActivated;
    }

    public Long getBeforeLastWeekDevcieActivated() {
        return beforeLastWeekDevcieActivated;
    }

    public void setBeforeLastWeekDevcieActivated(Long beforeLastWeekDevcieActivated) {
        this.beforeLastWeekDevcieActivated = beforeLastWeekDevcieActivated;
    }

    public Long getTotalDevcie() {
        return totalDevcie;
    }

    public void setTotalDevcie(Long totalDevcie) {
        this.totalDevcie = totalDevcie;
    }

}
