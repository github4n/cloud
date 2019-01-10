package com.iot.payment.vo.goods.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：视频套餐入参
 * 创建人： yeshiyuan
 * 创建时间：2018/7/17 10:32
 * 修改人： yeshiyuan
 * 修改时间：2018/7/17 10:32
 * 修改描述：
 */
@ApiModel(description = "视频套餐入参")
public class VideoPackageReq {

    @ApiModelProperty(name = "pageNum", value = "页数", dataType = "int")
    private Integer pageNum = 0;

    @ApiModelProperty(name = "pageSize", value = "每页条数", dataType = "int")
    private Integer pageSize;

    @ApiModelProperty(name = "packageTypeList", value = "计划套餐类型", dataType = "list")
    private List<Integer> packageTypeList;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        if (pageSize == 0 || pageSize == null){
            pageSize = 10;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<Integer> getPackageTypeList() {
        return packageTypeList;
    }

    public void setPackageTypeList(List<Integer> packageTypeList) {
        this.packageTypeList = packageTypeList;
    }

    public VideoPackageReq(Integer pageNum, Integer pageSize, List<Integer> packageTypeList) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.packageTypeList = packageTypeList;
    }

    public VideoPackageReq() {
    }
}
