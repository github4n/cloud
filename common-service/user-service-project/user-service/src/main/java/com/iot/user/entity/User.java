package com.iot.user.entity;

import java.util.Date;

public class User {
    private Long id;

    private Long tenantId;

    private String userName;

    private String nickname;

    private Byte state;

    private String password;

    private String uuid;

    private String mqttPassword;

    private String tel;

    private String headImg;

    private String background;

    private String email;

    private Long locationId;

    private String address;

    private Integer userLevel;

    private Integer adminStatus;

    private String company;

    private Date createTime;

    private Date updateTime;

    private String userStatus;

    /**
     * 不做持久化数据库
     */
    private Long orgId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public String getMqttPassword() {
        return mqttPassword;
    }

    public void setMqttPassword(String mqttPassword) {
        this.mqttPassword = mqttPassword == null ? null : mqttPassword.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg == null ? null : headImg.trim();
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(Integer adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
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

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", tenantId=" + tenantId +
                ", userName='" + userName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", state=" + state +
                ", password='" + password + '\'' +
                ", uuid='" + uuid + '\'' +
                ", mqttPassword='" + mqttPassword + '\'' +
                ", tel='" + tel + '\'' +
                ", headImg='" + headImg + '\'' +
                ", background='" + background + '\'' +
                ", email='" + email + '\'' +
                ", locationId=" + locationId +
                ", address='" + address + '\'' +
                ", userLevel=" + userLevel +
                ", adminStatus=" + adminStatus +
                ", company='" + company + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", userStatus='" + userStatus + '\'' +
                ", orgId=" + orgId +
                '}';
    }
}