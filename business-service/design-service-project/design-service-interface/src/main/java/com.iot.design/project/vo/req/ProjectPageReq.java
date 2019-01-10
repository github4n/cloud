package com.iot.design.project.vo.req;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/10
 */
public class ProjectPageReq {
    private Integer pageNum =1;

    private Integer pageSize =10;

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
}
