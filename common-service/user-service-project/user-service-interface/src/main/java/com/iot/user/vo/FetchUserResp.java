package com.iot.user.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述：获取用户应答类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/12 11:23
 */
public class FetchUserResp implements Serializable {
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

    private String email;

    private String address;

    private Integer userLevel;

    private Integer adminStatus;

    private String company;

    private Date createTime;

    private Date updateTime;

    private Long locationId;

    private Long orgId;

    private String background;

    private String userStatus;

    private String roleName;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "FetchUserResp{" +
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
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", userLevel=" + userLevel +
                ", adminStatus=" + adminStatus +
                ", company='" + company + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", locationId=" + locationId +
                ", orgId=" + orgId +
                ", background='" + background + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
