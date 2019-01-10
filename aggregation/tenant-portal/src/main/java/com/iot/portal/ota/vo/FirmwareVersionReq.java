package com.iot.portal.ota.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：OTA版本请求对象
 * 创建人： maochengyuan
 * 创建时间：2018/7/24 19:23
 * 修改人： maochengyuan
 * 修改时间：2018/7/24 19:23
 * 修改描述：
 */
@ApiModel(value="firmwareVersionReq", description="OTA版本请求对象")
public class FirmwareVersionReq {

    @ApiModelProperty(value="产品id",name="prodId")
    private String prodId;

    @ApiModelProperty(value="版本号",name="otaVersion")
    private String otaVersion;

    @ApiModelProperty(value="升级包类型",name="otaType")
    private String otaType;

    @ApiModelProperty(value="升级文件id",name="otaFileId")
    private String otaFileId;

    @ApiModelProperty(value="MD5值",name="otaMd5")
    private String otaMd5;

    @ApiModelProperty(value="分位的类型",name="fwType")
    private Integer fwType;

    public FirmwareVersionReq() {

    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
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

    public String getOtaFileId() {
        return otaFileId;
    }

    public void setOtaFileId(String otaFileId) {
        this.otaFileId = otaFileId;
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

    @Override
    public String toString() {
        return "FirmwareVersionReq{" +
                "prodId='" + prodId + '\'' +
                ", otaVersion='" + otaVersion + '\'' +
                ", otaType='" + otaType + '\'' +
                ", otaFileId='" + otaFileId + '\'' +
                ", otaMd5='" + otaMd5 + '\'' +
                ", fwType=" + fwType +
                '}';
    }
}
