package com.iot.device.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.vo.DevOtaVo;
import com.iot.device.vo.rsp.ota.UpgradePlanResp;
import com.iot.file.api.FileApi;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.device.api.SmartHomeDeviceCoreApi;
import com.iot.shcs.device.vo.DevInfoReq;
import com.iot.shcs.device.vo.SetDevAttrNotifReq;
import com.iot.shcs.device.vo.SubDevReq;
import com.iot.shcs.ota.OtaSmartHomeApi;
import com.iot.util.AssertUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton: 设备服务接口
 * @Date: 9:58 2018/5/21
 * @Modify by:
 */
@RestController
@RequestMapping("/device")
public class DeviceCoreController {

    @Autowired
    private SmartHomeDeviceCoreApi deviceCoreApi;

    @Autowired
    private OtaSmartHomeApi otaServiceApi;

    @Autowired
    private FileApi fileApi;
    /**
     *  获取用户和设备的绑定消息通知数据
     *
     * @param userId    用户的uuid
     * @param devId     设备id
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取用户和设备的绑定消息通知数据")
    @RequestMapping(value = "/getDevBindNotif", method = RequestMethod.GET)
    public CommonResponse getDevBindNotif(@RequestParam("userId") String userId, @RequestParam("devId") String devId) {
        return ResultMsg.SUCCESS.info(deviceCoreApi.getDevBindNotifMqttMsg(userId, devId, SaaSContextHolder.currentTenantId()));
    }

    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "针对BLE设备OTA升级-获取产品对应的最新版本", notes = "针对BLE设备OTA升级-获取产品对应的最新版本")
    @RequestMapping(value = "/getDevVersion", method = RequestMethod.GET)
    public CommonResponse getDevVersion(@RequestParam("productId") String productId) {
        UpgradePlanResp upgradePlanResp = otaServiceApi.getUpgradePlanByProductModel(productId);
        Map<String, Object> returnData = new HashMap<>();
        if (upgradePlanResp != null) {
            returnData.put("version", upgradePlanResp.getTargetVersion());
        }
        return ResultMsg.SUCCESS.info(returnData);
    }

    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "针对BLE设备OTA升级-获取下载地址", notes = "针对BLE设备OTA升级-获取下载地址")
    @RequestMapping(value = "/getDevOtaUrl", method = RequestMethod.POST)
    public CommonResponse getDevOtaUrl(@RequestBody DevOtaVo devOtaVo) {
        AssertUtils.notNull(devOtaVo, "ota.notnull");
        if (StringUtils.isEmpty(devOtaVo.getProductId())) {
            throw new BusinessException(ProductExceptionEnum.PRODUCT_ID_NOTNULL);
        }
        Map<String, String> returnData = otaServiceApi.getFirmwareUrlByModel(devOtaVo.getProductId(),devOtaVo.getVersion());
        return ResultMsg.SUCCESS.info(returnData);
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "针对BLE设备添加子设备")
    @RequestMapping(value = "/addSubDev", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addSubDev(@RequestBody SubDevReq subDev) {
        AssertUtils.notNull(subDev, "subDev.notnull");
        subDev.setTenantId(SaaSContextHolder.currentTenantId());
        subDev.setUserUuid(SaaSContextHolder.getCurrentUserUuid());
        return ResultMsg.SUCCESS.info(deviceCoreApi.addBleSubDev(subDev));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取设备信息")
    @RequestMapping(value = "/getDevInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getDevInfo(@RequestBody DevInfoReq req) {
        AssertUtils.notNull(req.getDevId(), "devId.notnull");
        req.setTenantId(SaaSContextHolder.currentTenantId());
        return ResultMsg.SUCCESS.info(deviceCoreApi.getDevInfo(req));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "设置设备信息")
    @RequestMapping(value = "/setDevInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse setDevInfo(@RequestBody DevInfoReq req) {
        AssertUtils.notNull(req, "DevInfo.notnull");
        AssertUtils.notNull(req.getDevId(), "devId.notnull");
        req.setTenantId(SaaSContextHolder.currentTenantId());
        req.setUserId(SaaSContextHolder.getCurrentUserId());
        deviceCoreApi.setDevInfo(req);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "针对BLE设备删除子设备")
    @RequestMapping(value = "/delSubDev", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse delSubDev(@RequestBody SubDevReq subDev) {
        AssertUtils.notNull(subDev, "subDev.notnull");
        subDev.setTenantId(SaaSContextHolder.currentTenantId());
        subDev.setUserUuid(SaaSContextHolder.getCurrentUserUuid());
        deviceCoreApi.delBleSubDev(subDev);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "上报设备属性")
    @RequestMapping(value = "/setDevAttr", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse setDevAttr(@RequestBody SetDevAttrNotifReq devAttr) {
        AssertUtils.notNull(devAttr, "devAttr.notnull");
        AssertUtils.notNull(devAttr.getDevId(), "devId.notnull");
        deviceCoreApi.dealDevAttr(devAttr);
        return ResultMsg.SUCCESS.info();
    }
}
