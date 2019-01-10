package com.iot.shcs.device.api;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.device.vo.req.ota.OtaFileInfoReq;
import com.iot.shcs.device.vo.AttrGetReq;
import com.iot.shcs.device.vo.ControlReq;
import com.iot.shcs.device.vo.DevBindNotifVo;
import com.iot.shcs.device.vo.DevInfoReq;
import com.iot.shcs.device.vo.DevInfoResp;
import com.iot.shcs.device.vo.DeviceVoReq;
import com.iot.shcs.device.vo.SetDevAttrNotifReq;
import com.iot.shcs.device.vo.SubDevReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Api("设备接口")
@FeignClient(value = "smart-home-service")
@RequestMapping("/smartHome/deviceCore")
public interface SmartHomeDeviceCoreApi {

    @ApiOperation("获取设备")
    @RequestMapping(value = "/getDevList", method = RequestMethod.GET)
    Map<String, Object> getDevList(@RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId, @RequestParam("homeId") Long homeId);

    @RequestMapping(value = "/synchronousRemoteControl", method = RequestMethod.POST)
    void synchronousRemoteControl(@RequestParam("spaceId") Long spaceId);

    @ApiOperation("alexa设备控制")
    @RequestMapping(value = "/singleDeviceControlForAlexa", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse singleDeviceControlForAlexa(@RequestBody ControlReq controlVo) throws BusinessException;

    @ApiOperation("设备单控")
    @RequestMapping(value = "/singleControl", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    CommonResponse singleControl(@RequestBody ControlReq controlVo) throws BusinessException;

    @ApiOperation("报警设备sos模式取消")
    @RequestMapping(value = "/cancelSos", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Boolean cancelSos(@RequestBody ControlReq controlVo) throws BusinessException;

    @ApiOperation("获取设备属性值")
    @RequestMapping(value = "/getDeviceAttr", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Object getDeviceAttr(@RequestBody AttrGetReq req) throws BusinessException;

    @ApiOperation(value = "获取网关下的所有子设备")
    @RequestMapping(value = "/getDeviceList", method = RequestMethod.GET)
    void getDeviceList(@RequestParam("deviceId") String deviceId) throws Exception;

    @ApiOperation(value = "添加设备记录")
    @RequestMapping(value = "/saveDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDevice(@RequestBody DeviceVoReq deviceReq) throws BusinessException;

    @ApiOperation(value = "获取设备内存属性")
    @RequestMapping(value = "/getDeviceProerty", method = RequestMethod.GET)
    Map<String, Object> getDeviceProerty(@RequestParam("deviceId") String deviceId) throws BusinessException;

    @ApiOperation(value = "挂载初始化数据")
    @RequestMapping(value = "/initMountInfo", method = RequestMethod.GET)
    void initMountInfo(@RequestParam("tenantId") Long tenantId) throws BusinessException;

    @ApiOperation("获取设备属性值")
    @RequestMapping(value = "/getDeviceAttrs", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Object getDeviceAttrs(@RequestBody AttrGetReq req) throws BusinessException;

    @ApiOperation("添加用户和设备的策略")
    @RequestMapping(value = "/addAcls", method = RequestMethod.GET)
    int addAcls(@RequestParam("deviceId") String deviceId);

    @ApiOperation("设备排序")
    @RequestMapping(value = "/sortDev", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Boolean sortDev(@RequestParam("tenantId") Long tenantId, @RequestBody List<String> deviceIds);

    @ApiOperation("获取传感器设备属性值")
    @RequestMapping(value = "/getSensorDevsAttrs", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    JSONObject getSensorDevsAttrs(@RequestBody AttrGetReq req);


    @ApiOperation("获取用户和设备的绑定消息通知数据")
    @RequestMapping(value = "/getDevBindNotifMqttMsg", method = RequestMethod.GET)
    DevBindNotifVo getDevBindNotifMqttMsg(@RequestParam("userId") String userId, @RequestParam("devId") String devId, @RequestParam("tenantId") Long tenantId);

    @ApiOperation("ToB添加设备的策略")
    @RequestMapping(value = "/addAclsToB", method = RequestMethod.GET)
    int addAclsToB(@RequestParam("deviceId") String deviceId);

    @ApiOperation("是否有子设备")
    @RequestMapping(value = "/deviceId", method = RequestMethod.GET)
    Boolean hasChlid(@RequestParam("deviceId") String deviceId);

    @ApiOperation(value = "更新ota包")
    @RequestMapping(value = "/updateOtaVersion", method = RequestMethod.GET)
    void updateOtaVersion(@RequestParam("deviceId") String deviceId, @RequestParam("fileName") String fileName, @RequestParam("tenantId") Long tenantId, @RequestParam("locationId") Long locationId);

    @ApiOperation("下载云端OTA文件")
    @RequestMapping(value = "/downLoadOtaFile", method = RequestMethod.POST)
    void downLoadOtaFile(@RequestBody OtaFileInfoReq otaFileInfoReq);

    @ApiOperation("针对BLE设备添加子设备")
    @RequestMapping(value = "/addBleSubDev", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    String addBleSubDev(@RequestBody SubDevReq subDev);

    @ApiOperation("获取设备信息")
    @RequestMapping(value = "/getDevInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    DevInfoResp getDevInfo(@RequestBody DevInfoReq req);

    @ApiOperation("设置设备信息")
    @RequestMapping(value = "/setDevInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void setDevInfo(@RequestBody DevInfoReq req);

    @ApiOperation("针对BLE设备删除子设备")
    @RequestMapping(value = "/delBleSubDev", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void delBleSubDev(@RequestBody SubDevReq subDev);

    @ApiOperation("上报设备属性")
    @RequestMapping(value = "/dealDevAttr", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void dealDevAttr(@RequestBody SetDevAttrNotifReq devAttr);
}
