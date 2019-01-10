package com.iot.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 描述：登录请求类
 * 创建人： nongchongwei
 * 创建时间： 2018/4/9 14:23
 */
@ApiModel
public class reviewedReq {

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称", dataType = "string", required = true)
    private String userName;
    /**
     * 登录密码
     */
    @ApiModelProperty(value = "登录密码", dataType = "string", required = true)
    private String pwd;
    /**
     * 最后登录ip（手机端登录才需要）
     */
    private String lastIp;

    private Date lastLoginTime;
    /**
     * app系统类型（1，iOS 2，Android）（手机端登录才需要）
     */
    private String os;
    /**
     * 手机唯一tokenId，用于推送（手机端登录才需要）
     */
    private String phoneId;
    /**
     * 登录终端类型（app|web），默认手机
     */
    @ApiModelProperty(value = "登录终端类型（APP|WEB），默认手机", dataType = "string", required = true)
    private String terminalMark = "APP";

    private String verifyCode; //登录失败3次需要

    private Long locationId;

    private Long tenantId;

    private Integer adminStatus;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public String getTerminalMark() {
        return terminalMark;
    }

    public void setTerminalMark(String terminalMark) {
        this.terminalMark = terminalMark;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Integer getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(Integer adminStatus) {
        this.adminStatus = adminStatus;
    }

    @Override
    public String toString() {
        return "LoginReq{" +
                "userName='" + userName + '\'' +
                ", pwd='" + pwd + '\'' +
                ", lastIp='" + lastIp + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", os='" + os + '\'' +
                ", phoneId='" + phoneId + '\'' +
                ", terminalMark='" + terminalMark + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", locationId='" + locationId + '\'' + '}';
    }
}
