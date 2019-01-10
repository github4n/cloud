package com.iot.user.vo;

/**
 * 描述：重置密码请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/11 10:10
 */
public class ResetPwdReq {
    private String userName;
    private Long tenantId;
    private String newPwd;
    private String verifyCode;

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "ResetPwdReq{" +
                "userName='" + userName + '\'' +
                ", tenantId=" + tenantId +
                ", newPwd='" + newPwd + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                '}';
    }
}
