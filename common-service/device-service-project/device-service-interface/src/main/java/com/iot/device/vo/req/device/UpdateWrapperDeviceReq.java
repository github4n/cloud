package com.iot.device.vo.req.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 10:25 2018/10/11
 * @Modify by:
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UpdateWrapperDeviceReq {
    /**
     * 设备id
     */
    private String uuid;

    /**
     * 租户ID
     * tenant_id
     */
    @NotNull(message = "tenantId.notnull")
    private Long tenantId;

    /**
     * parent_id  extends device_id   如果要把parentId 设置为空 则设置为 ""
     */
    private String parentId;
    /**
     * id
     */
    private Long id;

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
     * 创建人id
     * create_by
     */
    private Long createBy;
    /**
     * 更新人
     * update_by
     */
    private Long updateBy;
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
