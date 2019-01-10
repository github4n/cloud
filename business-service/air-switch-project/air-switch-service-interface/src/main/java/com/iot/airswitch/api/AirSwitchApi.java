package com.iot.airswitch.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/10/26
 * @Description: *
 */
@Api("空开接口")
@FeignClient(value = "air-switch-service")
@RequestMapping("/air/switch")
public interface AirSwitchApi {

    @ApiOperation("事件推送")
    @RequestMapping(value = "/eventPush", method = RequestMethod.POST)
    public boolean eventPush(@RequestParam("info") String info);

    @ApiOperation("设备改名")
    @RequestMapping(value = "/modifyDeviceName", method = RequestMethod.POST)
    public boolean modifyDeviceName(@RequestParam("deviceId") String deviceId, @RequestParam("name") String name);

    @ApiOperation("启动UDP服务")
    @RequestMapping(value = "/startup", method = RequestMethod.POST)
    public boolean startup(@RequestParam("port") Integer port);

    @ApiOperation("空开注册")
    @RequestMapping(value = "registerAirSwitch", method = RequestMethod.POST)
    public String registerAirSwitch(@RequestParam("info") String info, @RequestParam("tenantId") Long tenantId,
                                    @RequestParam("ip") String ip, @RequestParam("locationId") Long locationId);

    @ApiOperation("同步网络和节点信息")
    @RequestMapping(value = "syncAirSwitch", method = RequestMethod.POST)
    public void syncDevice(@RequestParam("netInfo") String netInfo, @RequestParam("swInfo") String swInfo);

    @ApiOperation("同步空开")
    @RequestMapping(value = "synAirSwitch", method = RequestMethod.POST)
    public void synAirSwitch(@RequestParam("ip") String ip);

    @ApiOperation("上报电量(按分钟)")
    @RequestMapping(value = "saveElectricity", method = RequestMethod.POST)
    public void saveElectricity(@RequestParam("info") String info);

    @ApiOperation("设置服务器指向地址")
    @RequestMapping(value = "setServerAddress", method = RequestMethod.POST)
    void setServerAddress(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceId") String deviceId, @RequestParam("ip") String ip, @RequestParam("port") String port);

    @ApiOperation("对设备进行漏电测试")
    @RequestMapping(value = "leakageTest", method = RequestMethod.POST)
    void leakageTest(@RequestParam("deviceId") String deviceId, @RequestParam("tenantId") Long tenantId);

    @ApiOperation("设备开闸")
    @RequestMapping(value = "switchOn", method = RequestMethod.POST)
    void switchOn(@RequestParam("deviceId") String deviceId, @RequestParam("tenantId") Long tenantId);

    @ApiOperation("设备合闸")
    @RequestMapping(value = "switchOff", method = RequestMethod.POST)
    void switchOff(@RequestParam("deviceId") String deviceId, @RequestParam("tenantId") Long tenantId);

    @ApiOperation("设置上传时间间隔")
    @RequestMapping(value = "setRTVI", method = RequestMethod.POST)
    void setRTVI(@RequestParam("deviceId") String deviceId, @RequestParam("interval") Integer interval,@RequestParam("tenantId") Long tenantId);

    @ApiOperation("自检")
    @RequestMapping(value = "selfCheck", method = RequestMethod.POST)
    void selfCheck(@RequestParam("deviceId") String deviceId, @RequestParam("tenantId") Long tenantId);

    @ApiOperation("批量自检")
    @RequestMapping(value = "batchLeakageTest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void batchLeakageTest(@RequestBody List<String> deviceIdList);

}
