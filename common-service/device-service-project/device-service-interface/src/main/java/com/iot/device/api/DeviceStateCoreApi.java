package com.iot.device.api;

import com.iot.device.api.fallback.DeviceStateCoreApiFallbackFactory;
import com.iot.device.vo.req.device.ListDeviceStateReq;
import com.iot.device.vo.req.device.UpdateDeviceStateReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "device-service", fallbackFactory = DeviceStateCoreApiFallbackFactory.class)
@RequestMapping("/deviceStateCore")
public interface DeviceStateCoreApi {

    @ApiOperation("获取设备状态属性集合")
    @RequestMapping(value = "/listStates", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Map<String, Object>> listStates(@RequestBody @Validated ListDeviceStateReq params);

    @ApiOperation("获取deviceId状态属性")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    Map<String, Object> get(@RequestParam(value = "tenantId", required = true) Long tenantId
            , @RequestParam(value = "deviceId", required = true) String deviceId);

    @ApiOperation("保存设备默认属性值")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveOrUpdate(@RequestBody UpdateDeviceStateReq params);

    @ApiOperation("批量保存设备默认属性值")
    @RequestMapping(value = "/saveOrUpdateBatch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveOrUpdateBatch(@RequestBody List<UpdateDeviceStateReq> params);

    @ApiOperation("恢复设备默认属性值")
    @RequestMapping(value = "/recoveryDefaultState", method = RequestMethod.GET)
    void recoveryDefaultState(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceId") String deviceId);

    @ApiOperation("被执行回调保存state缓存到数据库")
    @RequestMapping(value = "/runSaveStateCacheTask", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void runSaveStateCacheTask();

    @ApiOperation("test保存到队列的数据")
    @RequestMapping(value = "/testSaveStateTask", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void testSaveStateTask();
}
