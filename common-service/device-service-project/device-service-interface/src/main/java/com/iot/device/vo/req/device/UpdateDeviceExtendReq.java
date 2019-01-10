package com.iot.device.vo.req.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 16:19 2018/10/10
 * @Modify by:
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UpdateDeviceExtendReq {

    @NotBlank(message = "deviceId.notnull")
    private String deviceId;
    /**
     * 租户ID
     */
    @NotNull(message = "tenantId.notnull")
    private Long tenantId;
    /**
     * 批次 extend cust_uuid_manage  table  id
     * batch_num
     */
    private String batchNumId;
    /**
     * p2pid  extend custP2pid  table  id
     * p2p_id
     */
    private String p2pId;
    /**
     * 有效时长
     * uuid_validity_days
     */
    private BigDecimal uuidValidityDays;
    /**
     * 设备密码
     * device_cipher
     */
    private String deviceCipher;

    /**
     * 是否首次上传子设备,1是,0否(带套包的网关需要)
     * first_upload_sub_dev
     */
    private Integer firstUploadSubDev;

    private Integer unbindFlag;

    private Integer resetFlag;
    /**
     * 批次 extend cust_uuid_manage  table  id
     * batch_num
     */
    private Long batchNum;

    /**
     * 地区 为 "" 则清空
     */
    private String area;

    /**
     * 产品类型
     */
    private String CommType;

    /**
     * 时区
     */
    private String timezone;

    /**
     * 服务器IP
     */
    private String serverIp;

    /**
     * 服务器端口
     */
    private Long serverPort;

    /**
     * 上传间隔
     */
    private Long reportInterval;

    private Long address;

}
