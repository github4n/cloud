package com.iot.device.vo.rsp.device;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 10:03 2018/10/10
 * @Modify by:
 */
@Data
@ToString
public class GetDeviceExtendInfoRespVo {
    private Long id;
    /**
     * 设备id  extends device table  uuid
     * device_id
     */
    private String deviceId;
    /**
     * 批次 extend cust_uuid_manage  table  id
     * batch_num
     */
    private String batchNumId;
    /**
     * 创建时间
     * create_time
     */
    private Date createTime;
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
     * 租户id
     * tenant_id
     */
    private Long tenantId;
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
     * 地区
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
