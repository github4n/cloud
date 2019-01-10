package com.iot.device.vo.rsp.servicereview;

import com.iot.device.enums.product.ProductAuditStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：产品审核列表出参
 * 创建人： yeshiyuan
 * 创建时间：2018/10/24 14:57
 * 修改人： yeshiyuan
 * 修改时间：2018/10/24 14:57
 * 修改描述：
 */
@ApiModel(description = "产品审核列表出参")
public class ServiceAuditListResp {
    @ApiModelProperty(name = "productId", value = "产品id", dataType = "Long")
    private Long productId;
    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    @ApiModelProperty(value = "产品名称")
    private String productName;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "设备类型名称")
    private String deviceTypeName;
    @ApiModelProperty(value = "审核状态")
    private Integer auditStatus;
    @ApiModelProperty(value = "审核状态描述")
    private String auditStatusStr;
    @ApiModelProperty(value = "操作人用户id")
    private Long operateUserId;
    @ApiModelProperty(name = "applyTime", value = "操作时间", dataType = "Date")
    private Date applyTime;
    @ApiModelProperty(value = "服务描述")
    private String serviceDesc;
    @ApiModelProperty(value = "订单号")
    private String orderId;
    @ApiModelProperty(name = "serviceId", value = "服务id")
    private Long serviceId;

    public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatusStr() {
        return ProductAuditStatusEnum.getDesc(auditStatus);
    }

    public void setAuditStatusStr(String auditStatusStr) {
        this.auditStatusStr = auditStatusStr;
    }

    public Long getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(Long operateUserId) {
        this.operateUserId = operateUserId;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
}
