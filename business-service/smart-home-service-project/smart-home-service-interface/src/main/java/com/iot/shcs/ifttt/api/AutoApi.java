package com.iot.shcs.ifttt.api;

import com.iot.shcs.ifttt.vo.req.AddAutoRuleReq;
import com.iot.shcs.ifttt.vo.req.AutoListReq;
import com.iot.shcs.ifttt.vo.req.SaveAutoReq;
import com.iot.shcs.ifttt.vo.res.AutoListResp;
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

@Api(tags = "联动接口")
@FeignClient(value = "smart-home-service")
@RequestMapping(value = "/auto")
public interface AutoApi {

    @ApiOperation("获取联动列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<AutoListResp> list(@RequestBody AutoListReq req);

    @ApiOperation("保存联动信息")
    @RequestMapping(value = "/saveAuto", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveAuto(@RequestBody SaveAutoReq req);

    @ApiOperation("根据设备uuid获取关联情景列表")
    @RequestMapping(value = "/getSceneListByDevId", method = RequestMethod.GET)
    List<Map<String, Object>> getSceneListByDevId(@RequestParam("devId") String devId);

    @ApiOperation("迁移数据")
    @RequestMapping(value = "/moveData", method = RequestMethod.GET)
    void moveData(@RequestParam("key") String key);

    @ApiOperation("清脏数据")
    @RequestMapping(value = "/clearDirtyData", method = RequestMethod.GET)
    void clearDirtyData(@RequestParam("tenantId") Long tenantId, @RequestParam("key") String key);

    @ApiOperation("删除联动信息")
    @RequestMapping(value = "/delAuto", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void delBleAuto(@RequestBody SaveAutoReq req);

    @ApiOperation("获取automation规则")
    @RequestMapping(value = "/getAutoDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> getAutoDetail(@RequestBody SaveAutoReq saveAutoReq);

    @ApiOperation("添加automation规则")
    @RequestMapping(value = "/addAutoRule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addAutoRule(@RequestBody AddAutoRuleReq req);

    @ApiOperation("编辑automation规则")
    @RequestMapping(value = "/editAutoRule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void editAutoRule(@RequestBody AddAutoRuleReq req);

    @ApiOperation("设置automation使能")
    @RequestMapping(value = "/setAutoEnable", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void setAutoEnable(@RequestBody AddAutoRuleReq req);
}
