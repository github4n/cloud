package com.iot.report.dto.req;

import com.iot.common.beans.SearchParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "设备分页查询参数")
public class DevPageReq extends SearchParam {

    @ApiModelProperty(name = "tenantId", value = "租户ID")
    private Long tenantId;

    public DevPageReq() {

    }

    public DevPageReq(Long tenantId) {
        this.tenantId = tenantId;
    }

    public DevPageReq(SearchParam param, Long tenantId) {
        this.setPageNum(param.getPageNum());
        this.setPageSize(param.getPageSize());
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

}
