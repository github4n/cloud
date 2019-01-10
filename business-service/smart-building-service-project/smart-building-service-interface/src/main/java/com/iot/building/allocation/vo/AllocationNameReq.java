package com.iot.building.allocation.vo;

import java.io.Serializable;

/**
 * @Author: Xieby
 * @Date: 2018/9/3
 * @Description: *
 */
public class AllocationNameReq implements Serializable {

    private Long id;

    private String name;

    // *******
    private Integer pageSize;
    private Integer pageNum;

    public AllocationNameReq() {}

    public AllocationNameReq(Integer pageSize, Integer pageNum) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
