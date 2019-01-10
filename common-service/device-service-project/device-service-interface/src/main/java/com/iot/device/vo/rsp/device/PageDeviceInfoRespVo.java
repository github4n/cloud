package com.iot.device.vo.rsp.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @Author: lucky
 * @Descrpiton: 设备列表分页显示
 * @Date: 14:41 2018/10/16
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class PageDeviceInfoRespVo {

    /**
     * id
     */
    private Long id;
    /**
     * parent_id  extends device_id
     */
    private String parentId;
    /**
     * 设备id
     */
    private String uuid;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备图片
     */
    private String icon;
    /**
     * ip
     */
    private String ip;
    /**
     * mac地址
     */
    private String mac;
    /**
     * 设备序列号
     */
    private String sn;
    /**
     * 租户ID
     * tenant_id
     */
    private Long tenantId;
    /**
     * 是否直连设备0否、1是
     * is_direct_device
     */
    private Integer isDirectDevice;
    /**
     * 业务类型
     * business_type_id
     */
    private Long businessTypeId;
    /**
     * MAC 地址 hue网关用
     * reality_id
     */
    private String realityId;
    /**
     * 扩展名 hue用
     * extra_name
     */
    private String extraName;
    /**
     * 设置类型id  extend device_type table id
     * device_type_id
     */
    private Long deviceTypeId;
    /**
     * 产品id  extend product table id
     * product_id
     */
    private Long productId;
    /**
     * wifi名称
     * ssid
     */
    private String ssid;
    /**
     * 重置标识
     * reset_random
     */
    private String resetRandom;
    /**
     * 设备版本号
     */
    private String version;
    /**
     * 创建时间
     * create_time
     */
    private Date createTime;

    //条件
    private String conditional;
    /**
     * 最后跟新时间
     * location_id
     */
    private Long locationId;

    private String hwVersion;

    private String devModel;

    private String supplier;
}
