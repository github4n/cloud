package com.iot.device.vo.rsp.ota;

import com.iot.common.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：OTA版本响应对象
 * 创建人： maochengyuan
 * 创建时间：2018/7/24 19:23
 * 修改人： maochengyuan
 * 修改时间：2018/7/24 19:23
 * 修改描述：
 */
@ApiModel(value="FirmwareVersionPageResp", description="OTA版本响应对象")
public class FirmwareVersionPageResp {

    @ApiModelProperty(value="版本号",name="otaVersion")
    private String otaVersion;

    @ApiModelProperty(value="版本设备数量",name="versionQuantity")
    private Integer versionQuantity = 0;

    @ApiModelProperty(value="版本设备占有率",name="occupancyRate")
    private String occupancyRate;

    @ApiModelProperty(value="现网设备使用数量",name="deviceQuantity")
    private Integer deviceQuantity = 0;

    @ApiModelProperty(value="版本上线时间",name="versionOnlineTime")
    private Date versionOnlineTime;

    @ApiModelProperty(value="是否在升级计划中",name="inPlan")
    private Boolean inPlan;

    public FirmwareVersionPageResp() {

    }

    public String getOtaVersion() {
        return otaVersion;
    }

    public void setOtaVersion(String otaVersion) {
        this.otaVersion = otaVersion;
    }

    public Integer getVersionQuantity() {
        return versionQuantity;
    }

    public void setVersionQuantity(Integer versionQuantity) {
        this.versionQuantity = versionQuantity;
    }

    public String getOccupancyRate() {
        return NumberUtil.calcPercentage((double) this.getVersionQuantity(), (double) this.getDeviceQuantity(), "0.00");
    }

    public void setOccupancyRate(String occupancyRate) {
        this.occupancyRate = occupancyRate;
    }

    public Integer getDeviceQuantity() {
        return deviceQuantity;
    }

    public void setDeviceQuantity(Integer deviceQuantity) {
        this.deviceQuantity = deviceQuantity;
    }

    public Date getVersionOnlineTime() {
        return versionOnlineTime;
    }

    public void setVersionOnlineTime(Date versionOnlineTime) {
        this.versionOnlineTime = versionOnlineTime;
    }

    public Boolean getInPlan() {
        return inPlan;
    }

    public void setInPlan(Boolean inPlan) {
        this.inPlan = inPlan;
    }

}
