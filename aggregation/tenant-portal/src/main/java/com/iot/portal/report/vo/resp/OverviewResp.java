package com.iot.portal.report.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Collection;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：报表概览
 * 创建人： maochengyuan
 * 创建时间：2019/1/5 14:34
 * 修改人： maochengyuan
 * 修改时间：2019/1/5 14:34
 * 修改描述：
 */
@ApiModel(value = "报表概览",description = "报表概览")
public class OverviewResp {

    @ApiModelProperty(value="昨日用户激活数",name="yesUserActivated")
    private Long yesUserActivated = 0L;

    @ApiModelProperty(value="今日用户激活数",name="todUserActivated")
    private Long todUserActivated = 0L;

    @ApiModelProperty(value="昨日用户活跃数",name="yesUserActive")
    private Long yesUserActive = 0L;

    @ApiModelProperty(value="今日用户活跃数",name="todUserActive")
    private Long todUserActive = 0L;

    @ApiModelProperty(value="昨日设备激活数",name="yesDeviceActivated")
    private Long yesDeviceActivated = 0L;

    @ApiModelProperty(value="今日设备激活数",name="todDeviceActivated")
    private Long todDeviceActivated = 0L;

    @ApiModelProperty(value="昨日设备活跃数",name="yesDeviceActive")
    private Long yesDeviceActive = 0L;

    @ApiModelProperty(value="今日设备活跃数",name="todDeviceActive")
    private Long todDeviceActive = 0L;

    @ApiModelProperty(value="设备激活明细",name="deviceActivatedDetail")
    private Collection<BaseBean> deviceActivatedDetail;

    @ApiModelProperty(value="设备活跃明细",name="deviceActiveDetail")
    private Collection<BaseBean> deviceActiveDetail;

    @ApiModelProperty(value="用户激活明细",name="userActivatedDetail")
    private Collection<BaseBean> userActivatedDetail;

    @ApiModelProperty(value="用户活跃明细",name="userActiveDetail")
    private Collection<BaseBean> userActiveDetail;

    public OverviewResp() {

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

    public Long getYesUserActive() {
        return yesUserActive;
    }

    public void setYesUserActive(Long yesUserActive) {
        this.yesUserActive = yesUserActive;
    }

    public Long getTodUserActive() {
        return todUserActive;
    }

    public void setTodUserActive(Long todUserActive) {
        this.todUserActive = todUserActive;
    }

    public Long getYesDeviceActivated() {
        return yesDeviceActivated;
    }

    public void setYesDeviceActivated(Long yesDeviceActivated) {
        this.yesDeviceActivated = yesDeviceActivated;
    }

    public Long getTodDeviceActivated() {
        return todDeviceActivated;
    }

    public void setTodDeviceActivated(Long todDeviceActivated) {
        this.todDeviceActivated = todDeviceActivated;
    }

    public Long getYesDeviceActive() {
        return yesDeviceActive;
    }

    public void setYesDeviceActive(Long yesDeviceActive) {
        this.yesDeviceActive = yesDeviceActive;
    }

    public Long getTodDeviceActive() {
        return todDeviceActive;
    }

    public void setTodDeviceActive(Long todDeviceActive) {
        this.todDeviceActive = todDeviceActive;
    }

    public Collection<BaseBean> getDeviceActivatedDetail() {
        return deviceActivatedDetail;
    }

    public void setDeviceActivatedDetail(Collection<BaseBean> deviceActivatedDetail) {
        this.deviceActivatedDetail = deviceActivatedDetail;
    }

    public Collection<BaseBean> getDeviceActiveDetail() {
        return deviceActiveDetail;
    }

    public void setDeviceActiveDetail(Collection<BaseBean> deviceActiveDetail) {
        this.deviceActiveDetail = deviceActiveDetail;
    }

    public Collection<BaseBean> getUserActivatedDetail() {
        return userActivatedDetail;
    }

    public void setUserActivatedDetail(Collection<BaseBean> userActivatedDetail) {
        this.userActivatedDetail = userActivatedDetail;
    }

    public Collection<BaseBean> getUserActiveDetail() {
        return userActiveDetail;
    }

    public void setUserActiveDetail(Collection<BaseBean> userActiveDetail) {
        this.userActiveDetail = userActiveDetail;
    }

}
