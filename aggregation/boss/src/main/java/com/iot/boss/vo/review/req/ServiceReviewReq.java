package com.iot.boss.vo.review.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：Boss聚合层
 * 功能描述：语音服务审核请求对象
 * 创建人： mao2080@sina.com
 * 创建时间：2018/10/23 16:27
 * 修改人： mao2080@sina.com
 * 修改时间：2018/10/23 16:27
 * 修改描述：
 */
@ApiModel(value="ServiceReviewReq", description="语音服务审核请求对象")
public class ServiceReviewReq{

	@ApiModelProperty(name="租户主键",value="tenantId")
    private Long tenantId;
	
    @ApiModelProperty(name="语音服务id",value="serviceId")
    private Long serviceId;

    @ApiModelProperty(name="产品id",value="productId")
    private Long productId;

    @ApiModelProperty(name="审核状态（1:审核未通过 2:审核通过）",value="processStatus")
    private Byte processStatus;

    @ApiModelProperty(name="操作描述（提交审核，审核通过，审核不通过原因）",value="operateDesc")
    private String operateDesc;

    public ServiceReviewReq() {

    }

    public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Byte getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Byte processStatus) {
        this.processStatus = processStatus;
    }

    public String getOperateDesc() {
        return operateDesc;
    }

    public void setOperateDesc(String operateDesc) {
        this.operateDesc = operateDesc;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

}
