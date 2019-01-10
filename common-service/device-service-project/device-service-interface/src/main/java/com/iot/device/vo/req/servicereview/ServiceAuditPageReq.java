package com.iot.device.vo.req.servicereview;

import java.util.List;

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
public class ServiceAuditPageReq {

    @ApiModelProperty(name = "type", value = "类型（0：待处理，1：处理完成）", dataType = "Integer")
    private Integer type;

    @ApiModelProperty(name = "pageNum", value = "页码", dataType = "Integer")
    private Integer pageNum;

    @ApiModelProperty(name = "pageSize", value = "页大小", dataType = "Integer")
    private Integer pageSize;
    
    @ApiModelProperty(name = "tenantIds", value = "企业查询条件", dataType = "Long")
    private List<Long> tenantIds;
    
    @ApiModelProperty(name = "productParam", value = "产品查询条件", dataType = "String")
    private String productParam;
    
//    @ApiModelProperty(name = "googleAssistantId", value = "Google语音产品ID", dataType = "Long")
//    private Long googleAssistantId;
//
//    @ApiModelProperty(name = "amazonAlexaId", value = "Amazon语音产品ID", dataType = "Long")
//    private Long amazonAlexaId;

    @ApiModelProperty(name = "createBy", value = "创建人id", dataType = "Long")
    private Long createBy;

    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "String")
    private String orderId;

    @ApiModelProperty(name = "serviceId", value = "服务类型id", dataType = "Long")
    private Long serviceType;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

//    public Long getGoogleAssistantId() {
//		return googleAssistantId;
//	}
//
//	public void setGoogleAssistantId(Long googleAssistantId) {
//		this.googleAssistantId = googleAssistantId;
//	}
//
//	public Long getAmazonAlexaId() {
//		return amazonAlexaId;
//	}
//
//	public void setAmazonAlexaId(Long amazonAlexaId) {
//		this.amazonAlexaId = amazonAlexaId;
//	}
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

	public List<Long> getTenantIds() {
		return tenantIds;
	}

	public void setTenantIds(List<Long> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public String getProductParam() {
		return productParam;
	}

	public void setProductParam(String productParam) {
		this.productParam = productParam;
	}

    public Long getServiceType() {
        return serviceType;
    }

    public void setServiceType(Long serviceType) {
        this.serviceType = serviceType;
    }
}
