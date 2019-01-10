package com.iot.shcs.security.domain;

import java.util.Date;

public class Security {
    private Long id;

    // 空间表id
    private Long spaceId;

    // 布置安防模式,off:撤防,stay:在家布防,away:离家布防,紧急呼叫:panic
    private String armMode;

    // 安防密码
    private String password;

    // 创建时间
    private Date createTime;

    // 更新时间
    private Date updateTime;

    // 创建者id
    private Long createBy;

    // 更新者id
    private Long updateBy;

    // 租户id
    private Long tenantId;

    // 组织机构id
    private Long orgId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public String getArmMode() {
        return armMode;
    }

    public void setArmMode(String armMode) {
        this.armMode = armMode == null ? null : armMode.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
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

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return "Security{" +
                "id=" + id +
                ", spaceId=" + spaceId +
                ", armMode='" + armMode + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", tenantId=" + tenantId +
                ", orgId=" + orgId +
                '}';
    }
}