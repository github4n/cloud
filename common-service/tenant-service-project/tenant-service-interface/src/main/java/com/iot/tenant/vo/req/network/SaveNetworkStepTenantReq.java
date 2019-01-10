package com.iot.tenant.vo.req.network;

import com.iot.common.exception.BusinessException;
import com.iot.tenant.exception.TenantExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：保存设备配网步骤入参（portal使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 15:12
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 15:12
 * 修改描述：
 */
@ApiModel(description = "保存设备配网步骤入参（portal使用）")
public class SaveNetworkStepTenantReq {

    @ApiModelProperty(name = "appId", value = "appId", dataType = "Long")
    private Long appId;

    @ApiModelProperty(name = "productId", value = "产品id", dataType = "Long")
    private Long productId;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Long")
    private Long userId;

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "networkInfos", value = "配网信息", dataType = "List")
    private List<NetworkInfoReq> networkInfos;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public List<NetworkInfoReq> getNetworkInfos() {
        return networkInfos;
    }

    public void setNetworkInfos(List<NetworkInfoReq> networkInfos) {
        this.networkInfos = networkInfos;
    }

    public static void checkParam(SaveNetworkStepTenantReq req){
        if (req == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_IS_NULL);
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
        if (req.getNetworkInfos()==null || req.getNetworkInfos().isEmpty()) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "network infos is empty");
        } else {
            for (NetworkInfoReq info : req.getNetworkInfos()) {
                SaveNetworkStepBaseReq.checkNetworkInfoReq(info,"tenant");
            }
        }
    }



}
