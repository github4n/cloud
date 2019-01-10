package com.iot.device.vo.req.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:04 2018/9/25
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class PageDeviceInfoReq {

    //租户id
    @NotNull(message = "tenant.id.not.null")
    private Long tenantId;

    /**
     * 根据parent deviceId 获取
     */
    private String parentId;

    /**
     * 检索 是否非直连设备
     */
    private Integer isDirectDevice;

    /**
     * 检索指定产品的所有设备信息
     */
    private Long productId;

    /**
     * 检索指定的设备类型下所有的设备
     */
    private Long deviceTypeId;

    /**
     * 检索 locationId 下的所有设备
     */
    private Long locationId;

    /**
     * 检索模糊设备名称
     */
    private String deviceName;

    /**
     * 根据产品ids集合检索 productId 就可以不用
     */
    private List<Long> productIds;

    private Integer pageSize = 10;

    private Integer pageNumber = 1;
    
    private Long orgId;


}
