package com.iot.shcs.device.controller;

import com.iot.common.helper.Page;
import com.iot.control.device.api.UserDeviceCoreApi;
import com.iot.control.device.vo.req.PageUserDeviceInfoReq;
import com.iot.control.device.vo.req.UpdateUserDeviceInfoReq;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.control.device.vo.resp.PageUserDeviceInfoRespVo;
import com.iot.control.device.vo.resp.UpdateUserDeviceInfoResp;
import com.iot.device.vo.req.device.UpdateDeviceExtendReq;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceStateReq;
import com.iot.device.vo.req.device.UpdateDeviceStatusReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceExtendInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceExtendRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceStatusRespVo;
import com.iot.device.vo.rsp.device.ListDeviceTypeRespVo;
import com.iot.device.vo.rsp.device.ListProductRespVo;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 10:16 2018/10/19
 * @Modify by:
 */
@RestController
public class DeviceCoreController implements DeviceCoreServiceApi {

    @Autowired
    private DeviceCoreService deviceCoreService;

    @Autowired
    private UserDeviceCoreApi userDeviceCoreApi;

    public Long getRootUserIdByDeviceId(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceId") String deviceId) {
        return deviceCoreService.getRootUserIdByDeviceId(tenantId, deviceId);
    }

    public List<ListUserDeviceInfoRespVo> listUserDevicesByUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId) {
        return deviceCoreService.listUserDevices(tenantId, userId);
    }

    public List<ListUserDeviceInfoRespVo> listUserDevicesByDeviceId(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceId") String deviceId) {
        return deviceCoreService.listUserDevices(tenantId, deviceId);
    }

    public List<ListUserDeviceInfoRespVo> getUserDevices(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId
            , @RequestParam("deviceId") String deviceId) {
        return deviceCoreService.listUserDevices(tenantId, userId, deviceId);
    }

    public ListUserDeviceInfoRespVo getUserDevice(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId
            , @RequestParam("deviceId") String deviceId) {
        return deviceCoreService.getUserDevice(tenantId, userId, deviceId);
    }

    public List<ListUserDeviceInfoRespVo> listBatchUserDevices(@RequestParam("tenantId") Long tenantId
            , @RequestParam("userId") Long userId
            , @RequestBody List<String> deviceIds) {
        return deviceCoreService.listBatchUserDevices(tenantId, userId, deviceIds);
    }

    public UpdateUserDeviceInfoResp saveOrUpdateUserDevice(@RequestBody UpdateUserDeviceInfoReq updateUserDeviceParam) {
        return deviceCoreService.saveOrUpdateUserDevice(updateUserDeviceParam);
    }

    public void checkUserOnlyDirectDevice(@RequestParam("alreadyDirectDeviceId") String alreadyDirectDeviceId
            , @RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId) {
        deviceCoreService.checkUserOnlyDirectDevice(alreadyDirectDeviceId, tenantId, userId);
    }

    public void delUserDeviceParams(@RequestParam("tenantId") Long tenantId
            , @RequestParam("userId") Long userId, @RequestParam("deviceId") String deviceId) {
        delUserDeviceParams(tenantId, userId, deviceId);
    }

    public GetProductInfoRespVo getProductByDeviceId(@RequestParam("deviceId") String deviceId) {
        return getProductByDeviceId(deviceId);
    }

    public GetDeviceTypeInfoRespVo getDeviceTypeByDeviceId(@RequestParam("deviceId") String deviceId) {
        return deviceCoreService.getDeviceTypeByDeviceId(deviceId);
    }

    public GetProductInfoRespVo getProductById(@RequestParam("productId") Long productId) {
        return deviceCoreService.getProductById(productId);
    }

    public GetDeviceTypeInfoRespVo getDeviceTypeById(@RequestParam("deviceTypeId") Long deviceTypeId) {
        return deviceCoreService.getDeviceTypeById(deviceTypeId);
    }

    public List<ListDeviceInfoRespVo> listDevicesByParentId(@RequestParam("parentDeviceId") String parentDeviceId) {
        return deviceCoreService.listDevicesByParentId(parentDeviceId);
    }

    public GetProductInfoRespVo getByProductModel(@RequestParam("productModel") String productModel) {
        return deviceCoreService.getByProductModel(productModel);
    }

    public GetDeviceInfoRespVo getDeviceInfoByDeviceId(@RequestParam("deviceId") String deviceId) {
        return deviceCoreService.getDeviceInfoByDeviceId(deviceId);
    }

    public GetDeviceStatusInfoRespVo getDeviceStatusByDeviceId(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceId") String deviceId) {
        return deviceCoreService.getDeviceStatusByDeviceId(tenantId, deviceId);
    }

    public GetDeviceInfoRespVo saveOrUpdateDeviceInfo(@RequestBody UpdateDeviceInfoReq deviceInfoParam) {
        return deviceCoreService.saveOrUpdateDeviceInfo(deviceInfoParam);
    }

    public void saveOrUpdateDeviceStatus(@RequestBody UpdateDeviceStatusReq deviceStatusParam) {
        deviceCoreService.saveOrUpdateDeviceStatus(deviceStatusParam);
    }

    public GetDeviceExtendInfoRespVo getDeviceExtendByDeviceId(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceId") String deviceId) {
        return deviceCoreService.getDeviceExtendByDeviceId(tenantId, deviceId);
    }

    public void saveOrUpdateExtend(@RequestBody UpdateDeviceExtendReq updateDeviceExtendReq) {
        deviceCoreService.saveOrUpdateExtend(updateDeviceExtendReq);
    }

    public void delChildDeviceByDeviceId(@RequestParam("tenantId") Long tenantId, @RequestParam("subDevId") String subDevId, @RequestParam("userId") Long userId) {
        deviceCoreService.delChildDeviceByDeviceId(tenantId, subDevId, userId);
    }

    public List<ListUserDeviceInfoRespVo> listUserDevicesByDeviceIds(@RequestParam("tenantId") Long tenantId
            , @RequestParam("userId") Long userId, @RequestBody List<String> deviceIdList) {
        return deviceCoreService.listUserDevicesByDeviceIds(tenantId, userId, deviceIdList);
    }

    public List<ListDeviceInfoRespVo> listDevicesByDeviceIds(@RequestBody List<String> deviceIdList) {
        return deviceCoreService.listDevicesByDeviceIds(deviceIdList);
    }

    public List<ListDeviceStatusRespVo> listDeviceStatusByDeviceIds(@RequestParam("tenantId") Long tenantId,
                                                                    @RequestBody List<String> deviceIdList) {
        return deviceCoreService.listDeviceStatusByDeviceIds(tenantId, deviceIdList);
    }

    public Map<String, Map<String, Object>> listDeviceStateByDeviceIds(@RequestParam("tenantId") Long tenantId
            , @RequestBody List<String> deviceIdList) {
        return deviceCoreService.listDeviceStateByDeviceIds(tenantId, deviceIdList);
    }

    public List<ListProductRespVo> listProductByProductIds(@RequestBody List<Long> productIds) {
        return deviceCoreService.listProductByProductIds(productIds);
    }

    public List<ListDeviceTypeRespVo> listDeviceTypeByDeviceTypeIds(@RequestBody List<Long> deviceTypeIds) {
        return deviceCoreService.listDeviceTypeByDeviceTypeIds(deviceTypeIds);
    }

    public List<ListDeviceExtendRespVo> listDeviceExtendByDeviceIds(@RequestParam("tenantId") Long tenantId, @RequestBody List<String> deviceIdList) {
        return deviceCoreService.listDeviceExtendByDeviceIds(tenantId, deviceIdList);
    }

    public List<DeviceResp> findDeviceListByUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId, @RequestBody List<String> deviceIdList) {
        return deviceCoreService.findDeviceListByUserId(tenantId, userId, deviceIdList);
    }

    public List<DeviceResp> findDirectDeviceListByUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId) {
        return deviceCoreService.findDirectDeviceListByUserId(tenantId, userId);
    }

    @Override
    public List<DeviceResp> findDeviceListByUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId) {
        return deviceCoreService.findDeviceListByUserId(tenantId, userId);
    }

    public List<DeviceResp> findDeviceListByDeviceIds(@RequestParam("tenantId") Long tenantId, @RequestBody List<String> deviceIdList) {
        return deviceCoreService.findDeviceListByDeviceIds(tenantId, deviceIdList);
    }

    public void saveOrUpdateDeviceStates(@RequestBody List<UpdateDeviceStateReq> deviceStateList) {
        deviceCoreService.saveOrUpdateDeviceStates(deviceStateList);
    }

    @Override
    public Page<PageUserDeviceInfoRespVo> findPageUserDevice(@RequestBody PageUserDeviceInfoReq params) {

        return userDeviceCoreApi.pageUserDevice(params);
    }

    @Override
    public void cancelUser(@RequestParam("userId")Long userId,@RequestParam("userUuid")String userUuid,@RequestParam("tenantId")Long tenantId) {
        deviceCoreService.cancelUser(userId,userUuid,tenantId);
    }
}
