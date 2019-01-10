package com.iot.device.vo.rsp.ota;

/**
 * 项目名称：IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
public class UpgradeInfoResp {

    /**
     * 计划id
     */
    private Long id;
    /**
     * 产品id
     */
    private Long productId;

    /**
     * 启动Start 暂停Pause
     */
    private String planStatus;

    /**
     * 升级方式  推送升级Push(默认) 强制升级Force
     */
    private String upgradeType;

    /**
     * 升级版本号
     */
    private String targetVersion;


    /**
     * 下一个版本
     */
    private String nextVersion;

    /**
     * 整包升级WholePackage  差分升级Difference
     */
    private String otaType;

    /**
     * 升级文件id
     */
    private String otaFileId;

    /**
     * MD5值
     */
    private String otaMd5;

    /**
     * 分位的类型 0:所有的模块在一个分位里面(默认) 1:wifi 模块的分位 2: zigbee模块的分位 3: z-wave模块的分位 4：ble模块的分位
     */
    private Integer fwType;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(String upgradeType) {
        this.upgradeType = upgradeType;
    }

    public String getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersion(String targetVersion) {
        this.targetVersion = targetVersion;
    }

    public String getNextVersion() {
        return nextVersion;
    }

    public void setNextVersion(String nextVersion) {
        this.nextVersion = nextVersion;
    }

    public String getOtaType() {
        return otaType;
    }

    public void setOtaType(String otaType) {
        this.otaType = otaType;
    }

    public String getOtaFileId() {
        return otaFileId;
    }

    public void setOtaFileId(String otaFileId) {
        this.otaFileId = otaFileId;
    }

    public String getOtaMd5() {
        return otaMd5;
    }

    public void setOtaMd5(String otaMd5) {
        this.otaMd5 = otaMd5;
    }

    public Integer getFwType() {
        return fwType;
    }

    public void setFwType(Integer fwType) {
        this.fwType = fwType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UpgradeInfoResp{" +
                "id=" + id +
                ", productId=" + productId +
                ", planStatus='" + planStatus + '\'' +
                ", upgradeType='" + upgradeType + '\'' +
                ", targetVersion='" + targetVersion + '\'' +
                ", nextVersion='" + nextVersion + '\'' +
                ", otaType='" + otaType + '\'' +
                ", otaFileId='" + otaFileId + '\'' +
                ", otaMd5='" + otaMd5 + '\'' +
                ", fwType=" + fwType +
                '}';
    }
}