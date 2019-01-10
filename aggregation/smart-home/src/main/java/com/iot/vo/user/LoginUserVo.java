package com.iot.vo.user;

public class LoginUserVo {

    /**
     * 用户名，可以是邮箱或手机
     */
    private String userName = "";

    /**
     * 用户名，可以是邮箱或手机
     */
    private String venderCode = "";

    /**
     * 密码
     */
    private String passWord = "";


    /**
     * 租户
     */
    private String tenantId = "";

    /**
     * 用户状态（0-未激活，1-已激活，2-在线，3-离线，4-已冻结，5-已注销）
     */
    private String userState = "";

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
     * 区域id
     */
    private String locationId = "";
    /**
     * 苹果注册ID
     */
    private String iosToken = "";
    /**
     * 手机类型
     */
    private String phoneType = "";

    /**
     * 手机操作系统
     */
    private String os = "";

    /**
     * 手机操作系统版本
     */
    private String osVersion = "";

    /**
     * 最后一次登录IP
     */
    private String lastIp = "";

    /**
     * 安卓注册ID
     */
    private String registrationId = "";
    private String terminalMark;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVenderCode() {
        return venderCode;
    }

    public void setVenderCode(String venderCode) {
        this.venderCode = venderCode;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

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

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getIosToken() {
        return iosToken;
    }

    public void setIosToken(String iosToken) {
        this.iosToken = iosToken;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLocalLang() {
        return localLang;
    }

    public void setLocalLang(String localLang) {
        this.localLang = localLang;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getAndroidLoginId() {
        return androidLoginId;
    }

    public void setAndroidLoginId(String androidLoginId) {
        this.androidLoginId = androidLoginId;
    }

    public String getDefaultLocationId() {
        return defaultLocationId;
    }

    public void setDefaultLocationId(String defaultLocationId) {
        this.defaultLocationId = defaultLocationId;
    }

    public String getTerminalMark() {
        return terminalMark;
    }

    public void setTerminalMark(String terminalMark) {
        this.terminalMark = terminalMark;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    /**
     * 最后一次登录时间
     */
    private String lastLogin = "";

    /**
     * 本地语言代码
     */
    private String localLang = "";

    /**
     * 时区
     */
    private String timeZone = "";

    /**
     * 安卓登陆ID
     */
    private String androidLoginId = "";

    /**
     * 默认LocationId
     */
    private String defaultLocationId;

    private String verifyCode;

}
