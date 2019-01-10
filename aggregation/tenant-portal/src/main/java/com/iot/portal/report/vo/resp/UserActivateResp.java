package com.iot.portal.report.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：用户活跃对象
 * 创建人： maochengyuan
 * 创建时间：2019/1/5 15:31
 * 修改人： maochengyuan
 * 修改时间：2019/1/5 15:31
 * 修改描述：
 */
@ApiModel(value = "用户活跃对象",description = "用户活跃对象")
public class UserActivateResp {

    @ApiModelProperty(value="昨日用户活跃数",name="yesUserActivate")
    private Long yesUserActivate = 0L;

    @ApiModelProperty(value="今日用户活跃数",name="todUserActivate")
    private Long todUserActivate = 0L;

    @ApiModelProperty(value="最近7日用户活跃数",name="lastWeekUserActivate")
    private Long lastWeekUserActivate = 0L;

    @ApiModelProperty(value="最近7日的前7日用户活跃数",name="beforeLastWeekUserActivate")
    private Long beforeLastWeekUserActivate = 0L;

    public UserActivateResp() {

    }

    public UserActivateResp(Long yesUserActivate, Long lastWeekUserActivate) {
        this.yesUserActivate = yesUserActivate;
        this.lastWeekUserActivate = lastWeekUserActivate;
    }

    public Long getYesUserActivate() {
        return yesUserActivate;
    }

    public void setYesUserActivate(Long yesUserActivate) {
        this.yesUserActivate = yesUserActivate;
    }

    public Long getTodUserActivate() {
        return todUserActivate;
    }

    public void setTodUserActivate(Long todUserActivate) {
        this.todUserActivate = todUserActivate;
    }

    public Long getLastWeekUserActivate() {
        return lastWeekUserActivate;
    }

    public void setLastWeekUserActivate(Long lastWeekUserActivate) {
        this.lastWeekUserActivate = lastWeekUserActivate;
    }

    public Long getBeforeLastWeekUserActivate() {
        return beforeLastWeekUserActivate;
    }

    public void setBeforeLastWeekUserActivate(Long beforeLastWeekUserActivate) {
        this.beforeLastWeekUserActivate = beforeLastWeekUserActivate;
    }

}
