package com.iot.device.vo.req.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

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
public class ProductAuditPageReq {

    @ApiModelProperty(name = "type", value = "类型（0：待处理，1：处理完成）", dataType = "Integer")
    private Integer type;

    @ApiModelProperty(name = "tenantIds", value = "搜索租户id", dataType = "List")
    private List<Long> tenantIds;

    @ApiModelProperty(name = "productName", value = "搜索产品名称", dataType = "String")
    private String productName;

    @ApiModelProperty(name = "pageNum", value = "页码", dataType = "Integer")
    private Integer pageNum;

    @ApiModelProperty(name = "pageSize", value = "页大小", dataType = "Integer")
    private Integer pageSize;

    @ApiModelProperty(name = "createBy", value = "创建人", dataType = "Long")
    private Long createBy;

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getCreateBy() {
        return createBy;
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

    public List<Long> getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(List<Long> tenantIds) {
        this.tenantIds = tenantIds;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
