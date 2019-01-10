package com.iot.boss.vo.review.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：Boss聚合层
 * 功能描述：产品审核请求对象
 * 创建人： mao2080@sina.com
 * 创建时间：2018/10/23 16:27
 * 修改人： mao2080@sina.com
 * 修改时间：2018/10/23 16:27
 * 修改描述：
 */
@ApiModel(value="ProductReviewReq", description="产品审核请求对象")
public class ProductReviewReq {

    @ApiModelProperty(value="产品id",name="productId")
    private Long productId;

    @ApiModelProperty(value="审核状态（1:审核未通过 2:审核通过）",name="processStatus")
    private Integer processStatus;

    @ApiModelProperty(value="操作描述（提交审核，审核通过，审核不通过原因）",name="operateDesc")
    private String operateDesc;

    public ProductReviewReq() {

    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

}
