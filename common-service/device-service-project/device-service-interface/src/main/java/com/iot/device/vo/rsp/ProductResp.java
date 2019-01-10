package com.iot.device.vo.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("产品详细")
public class ProductResp implements Comparable<ProductResp>, Serializable {

    /**
     * 产品id
     */

    @ApiModelProperty(value = "产品id")
    private Long id;

    /**
     * 设备类型id
     */
    @ApiModelProperty(value = "设备类型id")
    private Long deviceTypeId;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    private String tenantName;

    private String tenantCode;

    @ApiModelProperty(value = "技术实现方案 ", allowableValues = "0 WIFI 1 蓝牙 2 网关 3 IPC")
    private Integer communicationMode;
    @ApiModelProperty(value = "数据传输方式")
    private Integer transmissionMode;

    private Date createTime;

    private Date updateTime;
    @ApiModelProperty(value = "产品唯一标示 model")
    private String model;
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "配置网络方式")
    private List configNetModes;
    /**
     * 是否直连设备
     */
    private Integer isDirectDevice;

    /**
     * 是否套包产品
     */
    private Integer isKit;

    /**
     * 设备类型名称
     */
    private String deviceTypeName;

    private String deviceType;

    private String deviceTypeIcon;

    /**
     * 设备类型名称
     */
    private String catalogName;

    private Long catalogId;

    /**
     * 分类排序
     */
    private Integer catalogOrder;

    /**
     * 这边返回的是url
     */
    @ApiModelProperty(value = "icon")
    private String icon;

    /**
     * 这边返回的是file id
     */
    private String fileId;

    /**
     * 默认图片
     */
    private String defaultIcon;

    /**
     * 是否为免开发产品
     */

    @ApiModelProperty(value = "0非免开发产品，1免开发产品")
    private Integer whetherSoc;

    @ApiModelProperty(value = "智能类型")
    private List<SmartDeviceTypeResp> smartDeviceTypeResps;


    //0 未审核 1 审核失败  2 审核成功
    private Integer auditStatus;


    @Override
    public int compareTo(ProductResp o) {
        Long temp = o.getId() - this.id;
        return temp.intValue();
    }
}