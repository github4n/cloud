package com.iot.vo.user;

import com.iot.vo.AuthenticationVo;

public class UpdateUserVo extends AuthenticationVo {
    /**
     * 国家
     */
    private String country = "";

    /**
     * 省，州
     */
    private String province = "";

    /**
     * 城市
     */
    private String city = "";
    /**
     * 昵称
     */
    private String nickName = "";

    /**
     * 公司
     */
    private String company = "";

    /**
     * 地址
     */
    private String address = "";
    /**
     * 手机号
     */
    private String cellPhone = "";

    /**
     * 头像地址
     */
    private String photoUrl = "";

    /**
     * 性别
     */
    private String sex = "";

    /**
     * 年龄
     */
    private String age = "";

    /**
     * 职业
     */
    private String vocation = "";

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getVocation() {
        return vocation;
    }

    public void setVocation(String vocation) {
        this.vocation = vocation;
    }
}
