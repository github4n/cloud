package com.iot.user.vo;

/**
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：用户VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/4/8 11:39
 * 修改人： mao2080@sina.com
 * 修改时间：2018/4/8 11:39
 * 修改描述：
 */
public class UserParamVO extends UserVO {

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 密码
     **/
    private String passWord;

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

}
