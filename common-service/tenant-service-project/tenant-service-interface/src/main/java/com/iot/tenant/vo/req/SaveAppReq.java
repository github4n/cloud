package com.iot.tenant.vo.req;

import java.util.Date;

/**
 * 描述：保存app配置请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/5 17:41
 */
public class SaveAppReq {
    private Long id;
    /**
     * app名称
     */
    private String appName;
    private String enName;
    private String packName;
    private String appCode;
    /**
     * app皮肤 0：小清新 1：科技范 2：中性
     */
    private Integer theme;
    /**
     * 版本 0：在线 1：本地
     */
    private Integer verFlag;
    private Long tenantId;
    /**
     * 安卓版本号
     */
    private String androidVer;
    /**
     * 苹果版本号
     */
    private String iosVer;
    /**
     * 文件id
     */
    private String fileId;
    /**
     * logo图片文件ID
     */
    private String logoFileId;
    /**
     * 加载页图片文件ID
     */
    private String loadingImgFileId;
    /**
     * 是否支持安防 0否 1是
     */
    private Integer hasSecurity;
    /**
     * 是否支持隐私政策 0否 1是
     */
    private Integer hasPrivacyPolicy;
    /**
     * 英文版权声明
     */
    private String enCopyright;
    /**
     * 中文版权声明
     */
    private String znCopyright;
    /**
     * 安卓安装路径
     */
    private String androidInstallUrl;
    /**
     * ios安装路径
     */
    private String iosInstallUrl;
    /**
     * 多语言中 已选语言 1,2,3
     */
    private String chooseLang;

    private String mfile;
    private String pfile;
    private String filePassword;

    private Integer androidPack;
    private Integer iosPack;

    private String androidPackUrl;
    private String iosPackUrl;
    private String desc;
    private Date packTime;

    private String googleUrl;
    private String googleKey;

    private Integer status;
    private String fcmFileId;
    private String iosFileId;
    private String keystoreId;

    private Date confirmTime;

    private Integer confirmStatus;

    private Integer displayIdentification;

    private String backgroundColor;

    private String imageId;
    /**
     * 创建者
     */
    private Long createBy;

    private String privacyCnLinkId;
    private String privacyEnLinkId;

    private String iosSendKey;

    private String keypass;

    private String storepass;

    private String isFavorite;

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getKeypass() {
        return keypass;
    }

    public void setKeypass(String keypass) {
        this.keypass = keypass;
    }

    public String getStorepass() {
        return storepass;
    }

    public void setStorepass(String storepass) {
        this.storepass = storepass;
    }

    public String getIosSendKey() {
        return iosSendKey;
    }

    public void setIosSendKey(String iosSendKey) {
        this.iosSendKey = iosSendKey;
    }

    public String getGoogleUrl() {
        return googleUrl;
    }

    public void setGoogleUrl(String googleUrl) {
        this.googleUrl = googleUrl;
    }

    public String getGoogleKey() {
        return googleKey;
    }

    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    public String getFcmFileId() {
        return fcmFileId;
    }

    public void setFcmFileId(String fcmFileId) {
        this.fcmFileId = fcmFileId;
    }

    public Date getPackTime() {
        return packTime;
    }

    public void setPackTime(Date packTime) {
        this.packTime = packTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public Integer getTheme() {
        return theme;
    }

    public void setTheme(Integer theme) {
        this.theme = theme;
    }

    public Integer getVerFlag() {
        return verFlag;
    }

    public void setVerFlag(Integer verFlag) {
        this.verFlag = verFlag;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getAndroidVer() {
        return androidVer;
    }

    public void setAndroidVer(String androidVer) {
        this.androidVer = androidVer;
    }

    public String getIosVer() {
        return iosVer;
    }

    public void setIosVer(String iosVer) {
        this.iosVer = iosVer;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getLogoFileId() {
        return logoFileId;
    }

    public void setLogoFileId(String logoFileId) {
        this.logoFileId = logoFileId;
    }

    public String getLoadingImgFileId() {
        return loadingImgFileId;
    }

    public void setLoadingImgFileId(String loadingImgFileId) {
        this.loadingImgFileId = loadingImgFileId;
    }

    public Integer getHasSecurity() {
        return hasSecurity;
    }

    public void setHasSecurity(Integer hasSecurity) {
        this.hasSecurity = hasSecurity;
    }

    public Integer getHasPrivacyPolicy() {
        return hasPrivacyPolicy;
    }

    public void setHasPrivacyPolicy(Integer hasPrivacyPolicy) {
        this.hasPrivacyPolicy = hasPrivacyPolicy;
    }

    public String getEnCopyright() {
        return enCopyright;
    }

    public void setEnCopyright(String enCopyright) {
        this.enCopyright = enCopyright;
    }

    public String getZnCopyright() {
        return znCopyright;
    }

    public void setZnCopyright(String znCopyright) {
        this.znCopyright = znCopyright;
    }

    public String getAndroidInstallUrl() {
        return androidInstallUrl;
    }

    public void setAndroidInstallUrl(String androidInstallUrl) {
        this.androidInstallUrl = androidInstallUrl;
    }

    public String getIosInstallUrl() {
        return iosInstallUrl;
    }

    public void setIosInstallUrl(String iosInstallUrl) {
        this.iosInstallUrl = iosInstallUrl;
    }

    public String getChooseLang() {
        return chooseLang;
    }

    public void setChooseLang(String chooseLang) {
        this.chooseLang = chooseLang;
    }

    public String getMfile() {
        return mfile;
    }

    public void setMfile(String mfile) {
        this.mfile = mfile;
    }

    public String getPfile() {
        return pfile;
    }

    public void setPfile(String pfile) {
        this.pfile = pfile;
    }

    public String getFilePassword() {
        return filePassword;
    }

    public void setFilePassword(String filePassword) {
        this.filePassword = filePassword;
    }

    public Integer getAndroidPack() {
        return androidPack;
    }

    public void setAndroidPack(Integer androidPack) {
        this.androidPack = androidPack;
    }

    public Integer getIosPack() {
        return iosPack;
    }

    public void setIosPack(Integer iosPack) {
        this.iosPack = iosPack;
    }

    public String getAndroidPackUrl() {
        return androidPackUrl;
    }

    public void setAndroidPackUrl(String androidPackUrl) {
        this.androidPackUrl = androidPackUrl;
    }

    public String getIosPackUrl() {
        return iosPackUrl;
    }

    public void setIosPackUrl(String iosPackUrl) {
        this.iosPackUrl = iosPackUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getKeystoreId() {
        return keystoreId;
    }

    public void setKeystoreId(String keystoreId) {
        this.keystoreId = keystoreId;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Integer getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(Integer confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public Integer getDisplayIdentification() {
        return displayIdentification;
    }

    public void setDisplayIdentification(Integer displayIdentification) {
        this.displayIdentification = displayIdentification;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getPrivacyCnLinkId() {
        return privacyCnLinkId;
    }

    public void setPrivacyCnLinkId(String privacyCnLinkId) {
        this.privacyCnLinkId = privacyCnLinkId;
    }

    public String getPrivacyEnLinkId() {
        return privacyEnLinkId;
    }

    public void setPrivacyEnLinkId(String privacyEnLinkId) {
        this.privacyEnLinkId = privacyEnLinkId;
    }

    public String getIosFileId() {
        return iosFileId;
    }

    public void setIosFileId(String iosFileId) {
        this.iosFileId = iosFileId;
    }
}
