package com.iot.boss.vo.servicereview;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：产品审核列表分页入参（boss使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/24 14:26
 * 修改人： yeshiyuan
 * 修改时间：2018/10/24 14:26
 * 修改描述：
 */
@ApiModel(description = "产品审核列表分页入参（boss使用）")
public class BossServiceAuditListReq {

    @ApiModelProperty(name = "type", value = "类型（0：待处理，1：处理完成）", dataType = "Integer")
    private Integer type;

    @ApiModelProperty(name = "pageNum", value = "页码", dataType = "Integer")
    private Integer pageNum;

    @ApiModelProperty(name = "pageSize", value = "页大小", dataType = "Integer")
    private Integer pageSize;

    @ApiModelProperty(name = "accountName", value = "账号", dataType = "String")
    private String accountName;

    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "String")
    private String orderId;

    @ApiModelProperty(name = "tenantParam", value = "企业名称", dataType = "String")
    private String tenantParam;
    
    @ApiModelProperty(name = "productParam", value = "产品名称", dataType = "String")
    private String productParam;

    @ApiModelProperty(name = "serviceType", value = "服务类型", dataType = "Long")
    private Long serviceType;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setServiceType(Long serviceType) {
        this.serviceType = serviceType;
    }

    public Long getServiceType() {
        return serviceType;
    }

    public Integer getPageNum() {
        if (pageNum == null || pageNum==0) {
            pageNum = 1;
        }
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize==0) {
            pageSize = 20;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getType() {
        if (type == null) {
            type = 0;
        }
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

	public String getTenantParam() {
		return tenantParam;
	}

	public void setTenantParam(String tenantParam) {
		this.tenantParam = tenantParam;
	}

	public String getProductParam() {
		return productParam;
	}

	public void setProductParam(String productParam) {
		this.productParam = productParam;
	}
    
}
