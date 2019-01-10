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
public class UserReq {
    /**
     * 主键
     **/
    private Long id;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户名称
     */
    private String nickname;

    /**
     * 用户状态（0未激活，1已激活，2在线，3离线，4已冻结，5已注销）
     */
    private String state;

    /**
     * 用户email
     */
    private String email;

    /**
     * 联系方式
     */
    private String tel;

    /**
     * 用户头像
     */
    private String headImg;

    /**
     * 地址
     **/
    private String address;

    /**
     * 租户id
     **/
    private Long tenantId;

    /**
     * 公司
     **/
    private String company;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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


    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
