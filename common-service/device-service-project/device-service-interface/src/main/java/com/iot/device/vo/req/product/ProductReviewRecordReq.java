package com.iot.device.vo.req.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：云平台
 * 功能描述：产品审核记录Req
 * 创建人： maochengyuan
 * 创建时间：2018/10/23 13:52
 * 修改人： maochengyuan
 * 修改时间：2018/10/23 13:52
 * 修改描述：
 */
@ApiModel(description = "产品审核记录Req")
public class ProductReviewRecordReq {

    @ApiModelProperty(name = "productId", value = "productId", dataType = "Long")
    private Long productId;

    @ApiModelProperty(name = "operateTime", value = "操作时间", dataType = "Date")
    private Date operateTime;

    @ApiModelProperty(name = "processStatus", value = "流程名称(1:审核未通过 2:审核通过)", dataType = "String")
    private Integer processStatus;

    @ApiModelProperty(name = "operateDesc", value = "操作描述", dataType = "String")
    private String operateDesc;

    @ApiModelProperty(name = "createBy", value = "创建人", dataType = "Long")
    private Long createBy;


    public ProductReviewRecordReq() {

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

}
