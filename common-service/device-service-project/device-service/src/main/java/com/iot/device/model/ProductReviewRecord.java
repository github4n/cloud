package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：产品审核记录实体类
 * 创建人： yeshiyuan
 * 创建时间：2018/10/24 11:44
 * 修改人： yeshiyuan
 * 修改时间：2018/10/24 11:44
 * 修改描述：
 */
@Data
@ToString
@TableName("product_review_record")
public class ProductReviewRecord extends Model<ProductReviewRecord> implements Serializable {

    @ApiModelProperty(name = "id", value = "id", dataType = "Long")
    private Long id;
    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;
    @ApiModelProperty(name = "productId", value = "产品id", dataType = "Long")
    private Long productId;
    @ApiModelProperty(name = "operateTime", value = "操作时间", dataType = "Date")
    private Date operateTime;
    @ApiModelProperty(name = "operateDesc", value = "操作描述（提交审核，审核通过，审核不通过原因）", dataType = "String")
    private String operateDesc;
    @ApiModelProperty(name = "createTime", value = "创建时间", dataType = "Date")
    private Date createTime;
    @ApiModelProperty(name = "createBy", value = "创建人", dataType = "Long")
    private Long createBy;
    @ApiModelProperty(name = "updateTime", value = "修改时间", dataType = "Date")
    private Date updateTime;
    @ApiModelProperty(name = "updateBy", value = "修改人", dataType = "Long")
    private Long updateBy;
    @ApiModelProperty(name = "isDeleted", value = "数据有效性（invalid;valid(默认)）", dataType = "String")
    private String isDeleted;
    @ApiModelProperty(name = "processStatus", value = "0:未审核 1:审核未通过 2:审核通过", dataType = "Byte")
    private Integer processStatus;


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

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateDesc() {
        return operateDesc;
    }

    public void setOperateDesc(String operateDesc) {
        this.operateDesc = operateDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }


    @Override
    protected Serializable pkVal() {
        return id;
    }

    public ProductReviewRecord() {
    }

    public ProductReviewRecord(Long tenantId, Long productId, Date operateTime, String operateDesc, Date createTime, Long createBy, Integer processStatus) {
        this.tenantId = tenantId;
        this.productId = productId;
        this.operateTime = operateTime;
        this.operateDesc = operateDesc;
        this.createTime = createTime;
        this.createBy = createBy;
        this.processStatus = processStatus;
    }
}
