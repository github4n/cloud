package com.iot.device.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author chq
 * @since 2018-06-27
 */
@Data
@ToString
public class LicenseUsageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;
    /**
     * 批次号
     */
    private String batchNum;
    /**
     * 设备id
     */
    private String licenseUuid;
    /**
     * mqtt秘钥
     */
    private String secretKey;
    /**
     * 设备的mac
     */
    private String mac;
    /**
     * 产品型号
     */
    private String modeId;
    /**
     * 上传者
     */
    private Long createBy;
    /**
     * 生成时间
     */
    private Date createTime;
    /**
     * 租户ID
     */
    private Long tenantId;

}
