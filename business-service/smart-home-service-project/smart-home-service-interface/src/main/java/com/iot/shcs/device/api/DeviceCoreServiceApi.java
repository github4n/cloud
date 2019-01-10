package com.iot.shcs.device.api;

import com.iot.common.helper.Page;
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
import io.swagger.annotations.Api;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 10:03 2018/10/19
 * @Modify by:
 */
@Api("设备聚合接口")
@FeignClient(value = "smart-home-service")
@RequestMapping("/smartHome/deviceCoreService")
public interface DeviceCoreServiceApi {
    @RequestMapping(value = "/getDevList", method = RequestMethod.GET)
    Long getRootUserIdByDeviceId(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceId") String deviceId);

    @RequestMapping(value = "/listUserDevicesByUserId", method = RequestMethod.GET)
    List<ListUserDeviceInfoRespVo> listUserDevicesByUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId);

    @RequestMapping(value = "/listUserDevicesByDeviceId", method = RequestMethod.GET)
    List<ListUserDeviceInfoRespVo> listUserDevicesByDeviceId(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceId") String deviceId);

    @RequestMapping(value = "/getUserDevices", method = RequestMethod.GET)
    List<ListUserDeviceInfoRespVo> getUserDevices(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId
            , @RequestParam("deviceId") String deviceId);

    @RequestMapping(value = "/getUserDevice", method = RequestMethod.GET)
    ListUserDeviceInfoRespVo getUserDevice(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId
            , @RequestParam("deviceId") String deviceId);

    @RequestMapping(value = "/listBatchUserDevices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ListUserDeviceInfoRespVo> listBatchUserDevices(@RequestParam("tenantId") Long tenantId
            , @RequestParam("userId") Long userId
            , @RequestBody List<String> deviceIds);

    @RequestMapping(value = "/saveOrUpdateUserDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    UpdateUserDeviceInfoResp saveOrUpdateUserDevice(@RequestBody UpdateUserDeviceInfoReq updateUserDeviceParam);

    @RequestMapping(value = "/checkUserOnlyDirectDevice", method = RequestMethod.GET)
    void checkUserOnlyDirectDevice(@RequestParam("alreadyDirectDeviceId") String alreadyDirectDeviceId
            , @RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId);

    @RequestMapping(value = "/delUserDeviceParams", method = RequestMethod.GET)
    void delUserDeviceParams(@RequestParam("tenantId") Long tenantId
            , @RequestParam("userId") Long userId, @RequestParam("deviceId") String deviceId);

    @RequestMapping(value = "/getProductByDeviceId", method = RequestMethod.GET)
    GetProductInfoRespVo getProductByDeviceId(@RequestParam("deviceId") String deviceId);

    @RequestMapping(value = "/getDeviceTypeByDeviceId", method = RequestMethod.GET)
    GetDeviceTypeInfoRespVo getDeviceTypeByDeviceId(@RequestParam("deviceId") String deviceId);

    @RequestMapping(value = "/getProductById", method = RequestMethod.GET)
    GetProductInfoRespVo getProductById(@RequestParam("productId") Long productId);

    @RequestMapping(value = "/getDeviceTypeById", method = RequestMethod.GET)
    GetDeviceTypeInfoRespVo getDeviceTypeById(@RequestParam("deviceTypeId") Long deviceTypeId);

    @RequestMapping(value = "/listDevicesByParentId", method = RequestMethod.GET)
    List<ListDeviceInfoRespVo> listDevicesByParentId(@RequestParam("parentDeviceId") String parentDeviceId);

    @RequestMapping(value = "/getByProductModel", method = RequestMethod.GET)
    GetProductInfoRespVo getByProductModel(@RequestParam("productModel") String productModel);

    @RequestMapping(value = "/getDeviceInfoByDeviceId", method = RequestMethod.GET)
    GetDeviceInfoRespVo getDeviceInfoByDeviceId(@RequestParam("deviceId") String deviceId);

    @RequestMapping(value = "/getDeviceStatusByDeviceId", method = RequestMethod.GET)
    GetDeviceStatusInfoRespVo getDeviceStatusByDeviceId(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceId") String deviceId);

    @RequestMapping(value = "/saveOrUpdateDeviceInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    GetDeviceInfoRespVo saveOrUpdateDeviceInfo(@RequestBody UpdateDeviceInfoReq deviceInfoParam);

    @RequestMapping(value = "/saveOrUpdateDeviceStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveOrUpdateDeviceStatus(@RequestBody UpdateDeviceStatusReq deviceStatusParam);

    @RequestMapping(value = "/getDeviceExtendByDeviceId", method = RequestMethod.GET)
    GetDeviceExtendInfoRespVo getDeviceExtendByDeviceId(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceId") String deviceId);

    @RequestMapping(value = "/saveOrUpdateExtend", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveOrUpdateExtend(@RequestBody UpdateDeviceExtendReq updateDeviceExtendReq);

    @RequestMapping(value = "/delChildDeviceByDeviceId", method = RequestMethod.GET)
    void delChildDeviceByDeviceId(@RequestParam("tenantId") Long tenantId, @RequestParam("subDevId") String subDevId, @RequestParam("userId") Long userId);

    @RequestMapping(value = "/listUserDevicesByDeviceIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ListUserDeviceInfoRespVo> listUserDevicesByDeviceIds(@RequestParam("tenantId") Long tenantId
            , @RequestParam("userId") Long userId, @RequestBody List<String> deviceIdList);

    @RequestMapping(value = "/listDevicesByDeviceIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ListDeviceInfoRespVo> listDevicesByDeviceIds(@RequestBody List<String> deviceIdList);

    @RequestMapping(value = "/listDeviceStatusByDeviceIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ListDeviceStatusRespVo> listDeviceStatusByDeviceIds(@RequestParam("tenantId") Long tenantId,
                                                             @RequestBody List<String> deviceIdList);

    @RequestMapping(value = "/listDeviceStateByDeviceIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Map<String, Object>> listDeviceStateByDeviceIds(@RequestParam("tenantId") Long tenantId
            , @RequestBody List<String> deviceIdList);

    @RequestMapping(value = "/listProductByProductIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ListProductRespVo> listProductByProductIds(@RequestBody List<Long> productIds);

    @RequestMapping(value = "/listDeviceTypeByDeviceTypeIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ListDeviceTypeRespVo> listDeviceTypeByDeviceTypeIds(@RequestBody List<Long> deviceTypeIds);

    @RequestMapping(value = "/listDeviceExtendByDeviceIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ListDeviceExtendRespVo> listDeviceExtendByDeviceIds(@RequestParam("tenantId") Long tenantId, @RequestBody List<String> deviceIdList);

    @RequestMapping(value = "/findDeviceListByUserIdAndDeviceIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<DeviceResp> findDeviceListByUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId, @RequestBody List<String> deviceIdList);

    @RequestMapping(value = "/findDirectDeviceListByUserId", method = RequestMethod.GET)
    List<DeviceResp> findDirectDeviceListByUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId);

    @RequestMapping(value = "/findDeviceListByUserId", method = RequestMethod.GET)
    List<DeviceResp> findDeviceListByUserId(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId);

    @RequestMapping(value = "/findDeviceListByDeviceIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<DeviceResp> findDeviceListByDeviceIds(@RequestParam("tenantId") Long tenantId, @RequestBody List<String> deviceIdList);

    @RequestMapping(value = "/saveOrUpdateDeviceStates", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveOrUpdateDeviceStates(@RequestBody List<UpdateDeviceStateReq> deviceStateList);

    @RequestMapping(value = "/findPageUserDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<PageUserDeviceInfoRespVo> findPageUserDevice(@RequestBody PageUserDeviceInfoReq params);

    @RequestMapping(value = "/cancelUser", method = RequestMethod.GET)
    public void cancelUser(@RequestParam("userId")Long userId,@RequestParam("userUuid")String userUuid,@RequestParam("tenantId")Long tenantId) ;

}
