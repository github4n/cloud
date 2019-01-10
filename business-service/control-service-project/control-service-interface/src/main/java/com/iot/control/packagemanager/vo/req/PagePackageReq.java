package com.iot.control.packagemanager.vo.req;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

public class PagePackageReq {
    private Integer pageNum = 1;
    private Integer pageSize = 10;


    @ApiModelProperty(name = "name" ,value = "名称")
    private String name;

    @ApiModelProperty(name = "description", value = "描述")
    private String description;

    @ApiModelProperty(name = "packageType", value = "套包类型")
    private Integer packageType;

    @ApiModelProperty(name = "tenantId", value = "租户id")
    private Long tenantId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPackageType(Integer packageType) {
        this.packageType = packageType;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPackageType() {
        return packageType;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
