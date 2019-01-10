package com.iot.center.vo;

/**
 * Created Time by sheting on 2018/10/26
 * Created by sheting on ${UAER}
 */
public class InfoList {
    private static final long serialVersionUID = 2025580783894328456L;

    private String fresh;
    private String parameter;
    private int moduleName;
    private int moduleSort;

    public String getFresh() {
        return fresh;
    }

    public void setFresh(String fresh) {
        this.fresh = fresh;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public int getModuleName() {
        return moduleName;
    }

    public void setModuleName(int moduleName) {
        this.moduleName = moduleName;
    }

    public int getModuleSort() {
        return moduleSort;
    }

    public void setModuleSort(int moduleSort) {
        this.moduleSort = moduleSort;
    }
}
