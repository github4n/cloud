package com.iot.user.vo;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：用户登录VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/4/8 11:39
 * 修改人： mao2080@sina.com
 * 修改时间：2018/4/8 11:39
 * 修改描述：
 */
public class UserLoginParamVO extends LoginReq {

    private String passWord;

    /**
     * 最后登录时间
     */
    private String lastLogin;

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

}
