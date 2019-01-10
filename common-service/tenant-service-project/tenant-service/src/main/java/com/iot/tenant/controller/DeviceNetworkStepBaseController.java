package com.iot.tenant.controller;

import com.iot.common.exception.BusinessException;
import com.iot.tenant.api.DeviceNetworkStepBaseApi;
import com.iot.tenant.exception.TenantExceptionEnum;
import com.iot.tenant.service.IDeviceNetworkStepBaseService;
import com.iot.tenant.vo.req.network.SaveNetworkStepBaseReq;
import com.iot.tenant.vo.resp.network.DeviceNetworkStepBaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：设备配网步骤模板管理（BOSS使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 15:38
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 15:38
 * 修改描述：
 */
@RestController
public class DeviceNetworkStepBaseController implements DeviceNetworkStepBaseApi{

    @Autowired
    private IDeviceNetworkStepBaseService deviceNetworkStepBaseService;

    /**
     * @despriction：保存步骤（先删后插）
     * @author  yeshiyuan
     * @created 2018/10/8 14:28
     * @return
     */
    @Override
    public void save(@RequestBody SaveNetworkStepBaseReq req) {
        SaveNetworkStepBaseReq.checkParam(req);
        deviceNetworkStepBaseService.saveStepAndInsertLangInfo(req);
    }

    /**
     * @despriction：查询某设备类型对应的配网步骤
     * @author  yeshiyuan
     * @created 2018/10/8 17:48
     * @return
     */
    @Override
    public DeviceNetworkStepBaseResp queryNetworkStep(@RequestParam("deviceTypeId") Long deviceTypeId,
                                                      @RequestParam(value = "networkTypeId", required = false) Long networkTypeId) {
        if (deviceTypeId == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "device type id is null");
        }
        return deviceNetworkStepBaseService.queryNetworkStep(deviceTypeId, networkTypeId);
    }

    /**
     * @despriction：查询某设备类型支持的配网类型
     * @author  yeshiyuan
     * @created 2018/12/4 13:53
     */
    @Override
    public List<Long> supportNetworkType(@RequestParam("deviceTypeId") Long deviceTypeId) {
        if (deviceTypeId == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "device type id is null");
        }
        return deviceNetworkStepBaseService.supportNetworkType(deviceTypeId);
    }

    /**
     * @despriction：删除设备类型的某种配网类型
     * @author  yeshiyuan
     * @created 2018/12/4 14:04
     */
    @Override
    public void deleteByNetworkTypes(@RequestParam("deviceTypeId") Long deviceTypeId, @RequestParam(value = "networkTypeIds")List<Long> networkTypeIds) {
        if (deviceTypeId == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "device type id is null");
        }
        if (networkTypeIds == null || networkTypeIds.isEmpty()) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "network type id is null");
        }
        deviceNetworkStepBaseService.deleteByNetworkTypes(deviceTypeId, networkTypeIds);
    }
}
