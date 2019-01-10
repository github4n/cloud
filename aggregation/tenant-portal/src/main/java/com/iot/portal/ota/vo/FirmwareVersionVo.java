package com.iot.portal.ota.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：OTA版本响应对象
 * 创建人： nongchongwei
 * 创建时间：2018/7/24 19:23
 * 修改人： nongchongwei
 * 修改时间：2018/7/24 19:23
 * 修改描述：
 */
@ApiModel(value="FirmwareVersionVo", description="OTA版本响应对象")
public class FirmwareVersionVo {

    @ApiModelProperty(value="主键",name="firmwareId")
    private String firmwareId;

    @ApiModelProperty(value="版本号",name="otaVersion")
    private String otaVersion;

    @ApiModelProperty(value="升级包类型",name="otaType")
    private String otaType;

    @ApiModelProperty(value="MD5值",name="otaMd5")
    private String otaMd5;

    @ApiModelProperty(value="分位的类型",name="fwType")
    private Integer fwType;

    @ApiModelProperty(value="创建时间",name="createTime")
    private Date createTime;

    @ApiModelProperty(value="上线时间",name="versionOnlineTime")
    private Date versionOnlineTime;

    public FirmwareVersionVo() {

    }

    public String getOtaVersion() {
        return otaVersion;
    }

    public void setOtaVersion(String otaVersion) {
        this.otaVersion = otaVersion;
    }

    public String getOtaType() {
        return otaType;
    }

    public void setOtaType(String otaType) {
        this.otaType = otaType;
    }

    public String getOtaMd5() {
        return otaMd5;
    }

    public void setOtaMd5(String otaMd5) {
        this.otaMd5 = otaMd5;
    }

    public Integer getFwType() {
        return fwType;
    }

    public void setFwType(Integer fwType) {
        this.fwType = fwType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFirmwareId() {
        return firmwareId;
    }

    public void setFirmwareId(String firmwareId) {
        this.firmwareId = firmwareId;
    }

    public Date getVersionOnlineTime() {
        return versionOnlineTime;
    }

    public void setVersionOnlineTime(Date versionOnlineTime) {
        this.versionOnlineTime = versionOnlineTime;
    }
}
