package com.iot.device.vo.rsp.product;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：查询套包产品
 * 功能描述：查询套包产品
 * 创建人： 李帅
 * 创建时间：2018年12月10日 下午3:42:45
 * 修改人：李帅
 * 修改时间：2018年12月10日 下午3:42:45
 */
public class PackageProductResp implements Serializable{

    /***/
	private static final long serialVersionUID = -7902757318858887767L;

	/**
     * 产品id
     */
    private Long id;

    /**
     * 设备类型id
     */
    private Long deviceTypeId;

    /**
     * 产品名称
     */
    private String productName;

	/**
	 * 租户id
	 */
    private Long tenantId;

    /**
     * 技术实现方案:0 WIFI 1 蓝牙 2 网关 3 IPC
     */
    private Integer communicationMode;
    
    /**
     * 数据传输方式
     */
    private Integer transmissionMode;

    private Date createTime;

    private Date updateTime;
    
    /**
     * 产品唯一标示 model
     */
    private String model;
    
    /**
     * 备注
     */
    private String remark;

    /**
     * 配置网络方式
     */
    private String configNetMode;
    
    /**
     * 是否直连设备
     */
    private Integer isDirectDevice;

    /**
     * 是否套包产品
     */
    private Integer isKit;

    /**
     * 这边返回的是url
     */
    private String icon;

    /**
     * 0 未审核 1 审核失败  2 审核成功
     */
    private Integer developStatus;
    
    /**
     * 0 未审核 1 审核失败  2 审核成功
     */
    private Long enterpriseDevelopId;
    
    /**
     * 0 未审核 1 审核失败  2 审核成功
     */
    private Integer auditStatus;
    
    /**
     * Google语音服务审核状态，0:未审核 1:审核未通过 2:审核通过
     */
    private Integer serviceGooAuditStatus;
    
    /**
     * Aleax语音服务审核状态，0:未审核 1:审核未通过 2:审核通过
     */
    private Integer serviceAlxAuditStatus;
    
    /**
     * 申请审核时间
     */
    private Date applyAuditTime;
    
    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 是否是网关
     */
    private boolean isGateway;
    
	public boolean isGateway() {
		return isGateway;
	}

	public void setGateway(boolean isGateway) {
		this.isGateway = isGateway;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Integer getCommunicationMode() {
		return communicationMode;
	}

	public void setCommunicationMode(Integer communicationMode) {
		this.communicationMode = communicationMode;
	}

	public Integer getTransmissionMode() {
		return transmissionMode;
	}

	public void setTransmissionMode(Integer transmissionMode) {
		this.transmissionMode = transmissionMode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getConfigNetMode() {
		return configNetMode;
	}

	public void setConfigNetMode(String configNetMode) {
		this.configNetMode = configNetMode;
	}

	public Integer getIsDirectDevice() {
		return isDirectDevice;
	}

	public void setIsDirectDevice(Integer isDirectDevice) {
		this.isDirectDevice = isDirectDevice;
	}

	public Integer getIsKit() {
		return isKit;
	}

	public void setIsKit(Integer isKit) {
		this.isKit = isKit;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getDevelopStatus() {
		return developStatus;
	}

	public void setDevelopStatus(Integer developStatus) {
		this.developStatus = developStatus;
	}

	public Long getEnterpriseDevelopId() {
		return enterpriseDevelopId;
	}

	public void setEnterpriseDevelopId(Long enterpriseDevelopId) {
		this.enterpriseDevelopId = enterpriseDevelopId;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Integer getServiceGooAuditStatus() {
		return serviceGooAuditStatus;
	}

	public void setServiceGooAuditStatus(Integer serviceGooAuditStatus) {
		this.serviceGooAuditStatus = serviceGooAuditStatus;
	}

	public Integer getServiceAlxAuditStatus() {
		return serviceAlxAuditStatus;
	}

	public void setServiceAlxAuditStatus(Integer serviceAlxAuditStatus) {
		this.serviceAlxAuditStatus = serviceAlxAuditStatus;
	}

	public Date getApplyAuditTime() {
		return applyAuditTime;
	}

	public void setApplyAuditTime(Date applyAuditTime) {
		this.applyAuditTime = applyAuditTime;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
}