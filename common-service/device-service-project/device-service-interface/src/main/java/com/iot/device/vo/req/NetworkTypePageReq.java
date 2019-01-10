package com.iot.device.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：配网类型分页入参
 * 创建人： yeshiyuan
 * 创建时间：2018/10/15 17:12
 * 修改人： yeshiyuan
 * 修改时间：2018/10/15 17:12
 * 修改描述：
 */
@ApiModel(description = "配网类型分页入参")
public class NetworkTypePageReq {

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
}
