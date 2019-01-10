package com.iot.tenant.vo.req.network;

import com.iot.common.exception.BusinessException;
import com.iot.tenant.exception.TenantExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：拷贝配网步骤入参
 * 创建人： yeshiyuan
 * 创建时间：2018/10/9 11:33
 * 修改人： yeshiyuan
 * 修改时间：2018/10/9 11:33
 * 修改描述：
 */
@ApiModel(description = "拷贝配网步骤入参")
public class CopyNetworkStepReq {

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Long")
    private Long userId;

    @ApiModelProperty(name = "appId", value = "appId", dataType = "Long")
    private Long appId;

    @ApiModelProperty(name = "productId", value = "产品id", dataType = "Long")
    private Long productId;

    @ApiModelProperty(name = "deviceTypeId", value = "设备类型id", dataType = "Long")
    private Long deviceTypeId;

    @ApiModelProperty(name = "networkTypeIds", value = "配网类型ids", dataType = "List")
    private List<Long> networkTypeIds;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public List<Long> getNetworkTypeIds() {
        return networkTypeIds;
    }

    public void setNetworkTypeIds(List<Long> networkTypeIds) {
        this.networkTypeIds = networkTypeIds;
    }

    public CopyNetworkStepReq() {
    }

    public CopyNetworkStepReq(Long tenantId, Long userId, Long appId, Long productId, Long deviceTypeId, List<Long> networkTypeIds) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.appId = appId;
        this.productId = productId;
        this.deviceTypeId = deviceTypeId;
        this.networkTypeIds = networkTypeIds;
    }

    public static void checkParam(CopyNetworkStepReq req) {
        if (req == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_IS_NULL);
        }
        if (req.getDeviceTypeId() == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "device type id is null");
        }
        if (req.getAppId() == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "app id is null");
        }
        if (req.getProductId() == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "product id is null");
        }
        if (req.getTenantId() == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "tenant id is null");
        }
        if (req.getNetworkTypeIds() == null || req.getNetworkTypeIds().isEmpty()) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "network type id is null");
        }
    }
}
