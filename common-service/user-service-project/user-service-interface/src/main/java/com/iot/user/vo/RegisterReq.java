package com.iot.user.vo;

import java.io.Serializable;

/**
 * 描述：注册请求类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/8 16:33
 */
public class RegisterReq implements Serializable{
    private Long operatorId;
    private String userName;
    private String nickname;
    private String passWord;
    private String tel;
    private String email;
    private String headImg;
    private String background;
    private String verifyCode;
    private Long tenantId;
    private Integer adminStatus;
    private Integer userLevel;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(Integer adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    @Override
    public String toString() {
        return "RegisterReq{" +
                "operatorId=" + operatorId +
                ", userName='" + userName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", passWord='" + passWord + '\'' +
                ", tel='" + tel + '\'' +
                ", email='" + email + '\'' +
                ", headImg='" + headImg + '\'' +
                ", background='" + background + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", tenantId=" + tenantId +
                ", adminStatus=" + adminStatus +
                ", userLevel=" + userLevel +
                '}';
    }
}
