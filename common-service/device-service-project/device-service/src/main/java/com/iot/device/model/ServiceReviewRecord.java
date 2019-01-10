package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：语音服务审核
 * 功能描述：语音服务审核
 * 创建人： 李帅
 * 创建时间：2018年10月25日 上午11:24:54
 * 修改人：李帅
 * 修改时间：2018年10月25日 上午11:24:54
 */
@TableName("service_review_record")
public class ServiceReviewRecord extends Model<ServiceReviewRecord> {

	/***/
	private static final long serialVersionUID = 1618828981514412600L;

	public static final String TABLE_NAME = "service_review_record";

    /**
     * 主键id
     */
    private Long id;

    /**
     * 租户主键
     */
    @TableField("tenant_id")
    private Long tenantId;
    
    /**
     * 产品Id
     */
    @TableField("product_id")
    private Long productId;
    
    /**
     * 语音服务Id，goods表中对应的Id
     */
    @TableField("service_id")
    private Long serviceId;
    
    /**
     * 操作时间（申请时间，审核时间）
     */
    @TableField("operate_time")
    private Date operateTime;

    /**
     * 0:未审核 1:审核未通过 2:审核通过
     */
    @TableField("process_status")
    private Integer processStatus;
    
    /**
     * 操作描述（提交审核，审核通过，审核不通过原因） 
     */
    @TableField("operate_desc")
    private String operateDesc;
    
    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    
    /**
     * 更新人
     */
    @TableField("update_by")
    private Long updateBy;
    
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
    
    /**
     * 数据有效性
     */
    @TableField("is_deleted")
    private String isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public Integer getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}

	public String getOperateDesc() {
		return operateDesc;
	}

	public void setOperateDesc(String operateDesc) {
		this.operateDesc = operateDesc;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
