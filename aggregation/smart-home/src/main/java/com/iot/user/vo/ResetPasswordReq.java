package com.iot.user.vo;

public class ResetPasswordReq {

    private String userName;

    private String verifyCode;

    private String newPassWord;

    private String passWord2;

    private Long tenantId;

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

    public String getNewPassWord() {
        return newPassWord;
    }

    public void setNewPassWord(String newPassWord) {
        this.newPassWord = newPassWord;
    }

    public String getPassWord2() {
        return passWord2;
    }

    public void setPassWord2(String passWord2) {
        this.passWord2 = passWord2;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "ResetPasswordReq{" +
                "userName='" + userName + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", newPassWord='" + newPassWord + '\'' +
                ", passWord2='" + passWord2 + '\'' +
                ", tenantId=" + tenantId +
                '}';
    }
}
