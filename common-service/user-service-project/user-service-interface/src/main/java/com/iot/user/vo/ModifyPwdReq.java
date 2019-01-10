package com.iot.user.vo;

/**
 * 描述：修改密码请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/11 10:06
 */
public class ModifyPwdReq {
    private Long userId;
    private String oldPwd;
    private String newPwd;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    @Override
    public String toString() {
        return "ModifyPwdReq{" +
                "userId=" + userId +
                ", oldPwd='" + oldPwd + '\'' +
                ", newPwd='" + newPwd + '\'' +
                '}';
    }
}
