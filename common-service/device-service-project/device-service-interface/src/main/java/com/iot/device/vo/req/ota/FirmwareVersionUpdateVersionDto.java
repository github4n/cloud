package com.iot.device.vo.req.ota;

import java.util.Date;
import java.util.Set;

/**
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： nongchongwei
 * 创建时间：2018年09月11日 15:50
 * 修改人： nongchongwei
 * 修改时间：2018年09月11日 15:50
 */
public class FirmwareVersionUpdateVersionDto {
    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 版本上线时间
     */
    private Date versionOnlineTime;
    /**
     * 产品id
     */
    private Long productId;

    /**
     * ota版本
     */
    private Set<String> otaVersion;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getVersionOnlineTime() {
        return versionOnlineTime;
    }

    public void setVersionOnlineTime(Date versionOnlineTime) {
        this.versionOnlineTime = versionOnlineTime;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Set<String> getOtaVersion() {
        return otaVersion;
    }

    public void setOtaVersion(Set<String> otaVersion) {
        this.otaVersion = otaVersion;
    }

}
