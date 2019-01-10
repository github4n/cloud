package com.iot.tenant.vo.req;

import com.iot.common.beans.SearchParam;

import java.util.ArrayList;
import java.util.Date;

public class AppVersionLogReq extends SearchParam {

    private Long id;

    private String systemInfo;

    private String appPackage;

    private String key;

    private String appName;

    private String version;

    private String discribes;

    private String downLocation;

    private Date createTime;

    private Date updateTime;

    private Long createBy;

    private Long updateBy;

    private Long tenantId;

    private ArrayList ids;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(String systemInfo) {
        this.systemInfo = systemInfo;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDiscribes() {
        return discribes;
    }

    public void setDiscribes(String discribes) {
        this.discribes = discribes;
    }

    public String getDownLocation() {
        return downLocation;
    }

    public void setDownLocation(String downLocation) {
        this.downLocation = downLocation;
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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public ArrayList getIds() {
        return ids;
    }

    public void setIds(ArrayList ids) {
        this.ids = ids;
    }
}
