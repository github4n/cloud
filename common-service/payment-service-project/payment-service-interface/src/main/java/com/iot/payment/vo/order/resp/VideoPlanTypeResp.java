package com.iot.payment.vo.order.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：视频计划类型、容量
 * 创建人： yeshiyuan
 * 创建时间：2018/7/17 15:32
 * 修改人： yeshiyuan
 * 修改时间：2018/7/17 15:32
 * 修改描述：
 */
@ApiModel(description = "视频计划类型、容量")
public class VideoPlanTypeResp {

    @ApiModelProperty(name = "packageType", value = "套餐类型", dataType = "int")
    private Integer packageType;

    @ApiModelProperty(name = "eventOrFulltimeAmount", value = "视频容量", dataType = "String")
    private String eventOrFulltimeAmount;

    @ApiModelProperty(name = "standardUnit", value = "容量单位", dataType = "integer")
    private Integer standardUnit;

    public Integer getPackageType() {
        return packageType;
    }

    public void setPackageType(Integer packageType) {
        this.packageType = packageType;
    }

    public String getEventOrFulltimeAmount() {
        return eventOrFulltimeAmount;
    }

    public void setEventOrFulltimeAmount(String eventOrFulltimeAmount) {
        this.eventOrFulltimeAmount = eventOrFulltimeAmount;
    }

    public Integer getStandardUnit() {
        return standardUnit;
    }

    public void setStandardUnit(Integer standardUnit) {
        this.standardUnit = standardUnit;
    }
}
