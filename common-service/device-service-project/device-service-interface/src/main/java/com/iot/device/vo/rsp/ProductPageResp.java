package com.iot.device.vo.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 13:54 2018/6/28
 * @Modify by:
 */

@Data
@ToString
@ApiModel("产品详细")
public class ProductPageResp implements Serializable {
    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id")
    private Long id;
    //设备类型id
    @ApiModelProperty(value = "设备类型id")
    private Long deviceTypeId;
    @ApiModelProperty(value = "开发状态,0:未开发,1:开发中,2:已上线")
    //开发状态,0:未开发,1:开发中,2:已上线
    private Integer developStatus;
    //设备类型名称
    @ApiModelProperty(value = "设备类型名称")
    private String deviceTypeName;
    //产品名称
    @ApiModelProperty(value = "产品名称")
    private String productName;
    //产品icon
    @ApiModelProperty(value = "产品icon")
    private String productIcon;
    //创建时间
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    //产品唯一标识
    @ApiModelProperty(value = "产品唯一标识")
    private String model;

    //是否为免开发产品
    @ApiModelProperty(value = "0非免开发产品，1免开发产品")
    private Integer whetherSoc;

    //技术实现方案 0 WIFI 1 蓝牙 2 网关 3 IPC
    @ApiModelProperty(value = "技术实现方案 ", allowableValues = "0 WIFI 1 蓝牙 2 网关 3 IPC")
    private Integer communicationMode;

    private Integer auditStatus;

	/**
	 * service_goo_audit_status
	 */
	private Integer serviceGooAuditStatus;
	
	/**
	 * service_alx_audit_status
	 */
	private Integer serviceAlxAuditStatus;
	

}
