package com.iot.boss.vo.review.req;

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
public class BossProductAuditPageReq {

    @ApiModelProperty(name = "type", value = "类型（0：待处理，1：处理完成）", dataType = "Integer")
    private Integer type;

    @ApiModelProperty(name = "accountName", value = "搜索账号名称", dataType = "String")
    private String accountName;

    @ApiModelProperty(name = "tenantName", value = "搜索租户名称", dataType = "String")
    private String tenantName;
    @ApiModelProperty(name = "productName", value = "搜索产品名称", dataType = "String")
    private String productName;

    @ApiModelProperty(name = "pageNum", value = "页码", dataType = "Integer")
    private Integer pageNum;

    @ApiModelProperty(name = "pageSize", value = "页大小", dataType = "Integer")
    private Integer pageSize;

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

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
