package com.iot.portal.corpuser.vo;

import com.iot.user.vo.LoginReq;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：用户VO
 * 创建人： nongchongwei
 * 创建时间：2018年07月04日 10:09
 * 修改人： nongchongwei
 * 修改时间：2018年07月04日 10:09
 */
public class UserLoginReq extends LoginReq {

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
