package com.iot.tenant.vo.req.lang;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：分页查询基础文案入参（BOSS使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/11 18:57
 * 修改人： yeshiyuan
 * 修改时间：2018/10/11 18:57
 * 修改描述：
 */
@ApiModel(description = "分页查询基础文案入参（BOSS使用）")
public class QueryLangInfoBasePageReq extends QueryLangInfoBaseReq{
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
