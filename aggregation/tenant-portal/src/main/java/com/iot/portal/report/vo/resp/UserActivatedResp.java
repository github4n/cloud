package com.iot.portal.report.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：用户激活对象
 * 创建人： maochengyuan
 * 创建时间：2019/1/5 15:31
 * 修改人： maochengyuan
 * 修改时间：2019/1/5 15:31
 * 修改描述：
 */
@ApiModel(value = "用户激活对象",description = "用户激活对象")
public class UserActivatedResp {

    @ApiModelProperty(value="昨日用户激活数",name="yesUserActivated")
    private Long yesUserActivated = 0L;

    @ApiModelProperty(value="今日用户激活数",name="todUserActivated")
    private Long todUserActivated = 0L;

    @ApiModelProperty(value="最近7日用户激活数",name="lastWeekUserActivated")
    private Long lastWeekUserActivated = 0L;

    @ApiModelProperty(value="最近7日的前7日用户激活数",name="beforeLastWeekUserActivated")
    private Long beforeLastWeekUserActivated = 0L;

    @ApiModelProperty(value="总激活数",name="totalUser")
    private Long totalUser = 0L;

    public UserActivatedResp() {

    }

    public UserActivatedResp(Long totalUser) {
        this.totalUser = totalUser;
    }

    public UserActivatedResp(Long yesUserActivated, Long lastWeekUserActivated, Long totalUser) {
        this.yesUserActivated = yesUserActivated;
        this.lastWeekUserActivated = lastWeekUserActivated;
        this.totalUser = totalUser;
    }

    public Long getYesUserActivated() {
        return yesUserActivated;
    }

    public void setYesUserActivated(Long yesUserActivated) {
        this.yesUserActivated = yesUserActivated;
    }

    public Long getTodUserActivated() {
        return todUserActivated;
    }

    public void setTodUserActivated(Long todUserActivated) {
        this.todUserActivated = todUserActivated;
    }

    public Long getLastWeekUserActivated() {
        return lastWeekUserActivated;
    }

    public void setLastWeekUserActivated(Long lastWeekUserActivated) {
        this.lastWeekUserActivated = lastWeekUserActivated;
    }

    public Long getBeforeLastWeekUserActivated() {
        return beforeLastWeekUserActivated;
    }

    public void setBeforeLastWeekUserActivated(Long beforeLastWeekUserActivated) {
        this.beforeLastWeekUserActivated = beforeLastWeekUserActivated;
    }

    public Long getTotalUser() {
        return totalUser;
    }

    public void setTotalUser(Long totalUser) {
        this.totalUser = totalUser;
    }

}
