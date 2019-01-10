package com.iot.building.allocation.vo;

import java.util.Date;

/**
 * @Author: Xieby
 * @Date: 2018/9/4
 * @Description: *
 */
public class ExecuteLogReq {
    private Long id;

    private Long functionId;

    private Integer exeResult;

    private String exeContent;

    private Date createTime;

    // *******
    private Integer pageSize;
    private Integer pageNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public Integer getExeResult() {
        return exeResult;
    }

    public void setExeResult(Integer exeResult) {
        this.exeResult = exeResult;
    }

    public String getExeContent() {
        return exeContent;
    }

    public void setExeContent(String exeContent) {
        this.exeContent = exeContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
