package com.iot.tenant.controller;

import com.iot.common.exception.BusinessException;
import com.iot.tenant.api.DeviceNetworkStepTenantApi;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.tenant.service.IDeviceNetworkStepTenantService;
import com.iot.tenant.vo.req.network.CopyNetworkStepReq;
import com.iot.tenant.vo.req.network.SaveNetworkStepTenantReq;
import com.iot.tenant.vo.resp.network.DeviceNetworkStepTenantResp;
import com.iot.tenant.vo.resp.network.NetworkFileFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：设备配网步骤管理（portal使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/9 11:42
 * 修改人： yeshiyuan
 * 修改时间：2018/10/9 11:42
 * 修改描述：
 */
@RestController
public class DeviceNetworkStepTenantController implements DeviceNetworkStepTenantApi {

    @Autowired
    private IDeviceNetworkStepTenantService deviceNetworkStepTenantService;

    /**
     * @despriction：拷贝配网步骤文案
     * @author  yeshiyuan
     * @created 2018/10/9 11:31
     * @return
     */
    @Override
    public void copyNetworkStep(@RequestBody CopyNetworkStepReq copyNetworkStepReq) {
        CopyNetworkStepReq.checkParam(copyNetworkStepReq);
        deviceNetworkStepTenantService.copyNetworkStep(copyNetworkStepReq);
    }

    /**
     * @despriction：保存配网步骤文案（先删后插）
     * @author  yeshiyuan
     * @created 2018/10/9 14:02
     * @return
     */
    @Override
    public void save(@RequestBody SaveNetworkStepTenantReq req) {
        SaveNetworkStepTenantReq.checkParam(req);
        deviceNetworkStepTenantService.save(req);
    }

    /**
     * @despriction：查询配网步骤文案
     * @author  yeshiyuan
     * @created 2018/10/9 16:13
     * @return
     */
    @Override
    public DeviceNetworkStepTenantResp queryNetworkStep(@RequestParam("tenantId") Long tenantId, @RequestParam("appId") Long appId, @RequestParam("productId") Long productId) {
        if (tenantId == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "tenant id is null");
        }
        if (appId == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "app id is null");
        }
        if (productId == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "product id is null");
        }
        return deviceNetworkStepTenantService.queryNetworkStep(tenantId, appId, productId);
    }

    /**
     * @despriction：配网步骤文件格式
     * @author  yeshiyuan
     * @created 2018/10/18 15:47
     * @return
     */
    @Override
    public Map<String, NetworkFileFormat> getNetworkFileFormat(@RequestParam("tenantId") Long tenantId, @RequestParam("appId") Long appId, @RequestParam("productId") Long productId) {
        return deviceNetworkStepTenantService.getNetworkFileFormat(tenantId, appId, productId);
    }
}
