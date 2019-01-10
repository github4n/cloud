package com.iot.portal.corpuser.vo;
/**
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：用户VO
 * 创建人： nongchongwei
 * 创建时间：2018年07月04日 10:09
 * 修改人： nongchongwei
 * 修改时间：2018年07月04日 10:09
 */
public class SendEmailReq {

    private String email;

    private Byte type;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
