package com.iot.center.vo;

import java.util.Date;

public class IndexDetailVo {

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

    private int type;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public IndexDetailVo(int moduleSort, String moduleName, String parameter, String fresh,int type,String title) {
        this.moduleSort = moduleSort;
        this.moduleName = moduleName;
        this.parameter = parameter;
        this.fresh = fresh;
        this.type = type;
        this.title = title;
    }

    public IndexDetailVo(Long id,int type,String title) {
        this.id = id;
        this.type = type;
        this.title = title;
    }

    public IndexDetailVo(int type,String title) {
        this.type = type;
        this.title = title;
    }

    public IndexDetailVo(Long id,int moduleSort, String moduleName, String parameter, String fresh,int type,String title) {
        this.id = id;
        this.moduleSort = moduleSort;
        this.moduleName = moduleName;
        this.parameter = parameter;
        this.fresh = fresh;
        this.type = type;
        this.title = title;
    }

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
