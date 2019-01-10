package com.iot.boss.vo.review.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：Boss聚合层
 * 功能描述： 租户审核请求对象
 * 创建人： mao2080@sina.com
 * 创建时间：2018/10/23 16:27
 * 修改人： mao2080@sina.com
 * 修改时间：2018/10/23 16:27
 * 修改描述：
 */
@ApiModel(value="TenantReviewReq", description="租户审核请求对象")
public class TenantReviewReq {

    @ApiModelProperty(name="租户id",value="tenantId")
    private Long tenantId;

    @ApiModelProperty(name="审核状态（1:审核未通过 2:审核通过）",value="processStatus")
    private Byte processStatus;

    @ApiModelProperty(name="操作描述（提交审核，审核通过，审核不通过原因）",value="operateDesc")
    private String operateDesc;

    public TenantReviewReq() {

    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
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

}
