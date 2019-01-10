package com.iot.video.mongo.vo;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：自定义的mongo分页返回参数
 * 创建人： yeshiyuan
 * 创建时间：2018/8/1 17:47
 * 修改人： yeshiyuan
 * 修改时间：2018/8/1 17:47
 * 修改描述：
 */
public class MongoPageInfo<T> {

    private Long totalCount;

    private Integer pageSize;

    private Integer currentPage;

    private List<T> list;

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public MongoPageInfo(Long totalCount, Integer pageSize, Integer currentPage, List<T> list) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.list = list;
    }

    public MongoPageInfo() {
    }
}
