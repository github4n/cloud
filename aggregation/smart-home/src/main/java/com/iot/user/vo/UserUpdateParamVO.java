package com.iot.user.vo;

/**
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：用户更新VO
 * 创建人： mao2080@sina.com
 * 创建时间：2018/4/8 11:39
 * 修改人： mao2080@sina.com
 * 修改时间：2018/4/8 11:39
 * 修改描述：
 */
public class UserUpdateParamVO {

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
