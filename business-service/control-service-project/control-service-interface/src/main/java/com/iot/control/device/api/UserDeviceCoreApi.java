package com.iot.control.device.api;

import com.iot.common.helper.Page;
import com.iot.control.device.api.fallback.UserDeviceCoreApiFallbackFactory;
import com.iot.control.device.vo.req.DelUserDeviceInfoReq;
import com.iot.control.device.vo.req.GetUserDeviceInfoReq;
import com.iot.control.device.vo.req.ListUserDeviceInfoReq;
import com.iot.control.device.vo.req.PageUserDeviceInfoReq;
import com.iot.control.device.vo.req.UpdateUserDeviceInfoReq;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.control.device.vo.resp.PageUserDeviceInfoRespVo;
import com.iot.control.device.vo.resp.UpdateUserDeviceInfoResp;
import io.swagger.annotations.Api;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:43 2018/10/12
 * @Modify by:
 */
@Api("用户设备接口")
@FeignClient(value = "control-service", fallbackFactory = UserDeviceCoreApiFallbackFactory.class)
@RequestMapping("/control/userDeviceCore")
public interface UserDeviceCoreApi {

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    UpdateUserDeviceInfoResp saveOrUpdate(@RequestBody UpdateUserDeviceInfoReq params);

    @RequestMapping(value = "/saveOrUpdateBatch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<UpdateUserDeviceInfoResp> saveOrUpdateBatch(@RequestBody List<UpdateUserDeviceInfoReq> paramsList);

    @RequestMapping(value = "/listUserDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ListUserDeviceInfoRespVo> listUserDevice(@RequestBody ListUserDeviceInfoReq params);

    @RequestMapping(value = "/listBatchUserDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ListUserDeviceInfoRespVo> listBatchUserDevice(@RequestBody GetUserDeviceInfoReq params);

    @RequestMapping(value = "/delUserDevice", method = RequestMethod.GET)
    void delUserDevice(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId
            , @RequestParam("deviceId") String deviceId);

    @RequestMapping(value = "/delBatchUserDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void delBatchUserDevice(@RequestBody DelUserDeviceInfoReq params);


    @RequestMapping(value = "/pageUserDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<PageUserDeviceInfoRespVo> pageUserDevice(@RequestBody PageUserDeviceInfoReq params);
}
