package com.iot.building.index.vo;

import java.io.Serializable;
import java.util.Date;

public class IndexDetailResp implements Serializable {

    private static final long serialVersionUID = 2025580783894328456L;

    private Long id;

    private Long indexContentId;

    private int moduleSort;

    private String moduleName;

    private String parameter;

    private Long createBy;

    private Long updateBy;

    private Date createTime;

    private Date updateTime;

    private String fresh;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIndexContentId() {
        return indexContentId;
    }

    public void setIndexContentId(Long indexContentId) {
        this.indexContentId = indexContentId;
    }

    public int getModuleSort() {
        return moduleSort;
    }

    public void setModuleSort(int moduleSort) {
        this.moduleSort = moduleSort;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFresh() {
        return fresh;
    }

    public void setFresh(String fresh) {
        this.fresh = fresh;
    }
}
