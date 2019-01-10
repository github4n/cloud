package com.iot.user.vo;

import java.util.Date;

/**
 * 描述：更新用户请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/12 11:28
 */
public class UpdateUserReq {
    private Long id;

    private Long tenantId;

    private String userName;

    private String nickname;

    private Byte state;

    private String password;

    private String mqttPassword;

    private String tel;

    private String headImg;

    private String email;

    private String address;

    private Integer adminStatus;

    private String company;

    private Date updateTime;

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
        this.userName = userName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
        this.password = password;
    }

    public String getMqttPassword() {
        return mqttPassword;
    }

    public void setMqttPassword(String mqttPassword) {
        this.mqttPassword = mqttPassword;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        this.company = company;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "UpdateUserReq{" +
                "id=" + id +
                ", tenantId=" + tenantId +
                ", userName='" + userName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", state=" + state +
                ", password='" + password + '\'' +
                ", mqttPassword='" + mqttPassword + '\'' +
                ", tel='" + tel + '\'' +
                ", headImg='" + headImg + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", adminStatus=" + adminStatus +
                ", company='" + company + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
