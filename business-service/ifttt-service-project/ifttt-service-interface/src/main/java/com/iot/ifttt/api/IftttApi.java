package com.iot.ifttt.api;

import com.iot.ifttt.vo.AppletVo;
import com.iot.ifttt.vo.CheckAppletReq;
import com.iot.ifttt.vo.SetEnableReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：联动接口
 * 创建人： LaiGuiMing
 * 创建时间： 2018/10/16 14:39
 */

@Api(tags = "联动接口")
@FeignClient(value = "ifttt-service", fallbackFactory = IftttApiFallbackFactory.class)
@RequestMapping("/ifttt")
public interface IftttApi {

    @ApiOperation("触发校验")
    @RequestMapping(value = "/check", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void check(@RequestBody CheckAppletReq req);

    @ApiOperation("保存联动规则")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long save(@RequestBody AppletVo req);

    @ApiOperation("启用或禁用联动")
    @RequestMapping(value = "/setEnable", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void setEnable(@RequestBody SetEnableReq req);

    @ApiOperation("获取联动详细信息")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    AppletVo get(@RequestParam("id") Long id);

    @ApiOperation("删除联动")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    Boolean delete(@RequestParam("id") Long id);

    @ApiOperation("统计天文定时")
    @RequestMapping(value = "/countAstroClockJob", method = RequestMethod.GET)
    void countAstroClockJob();

    @ApiOperation("删除关联子项")
    @RequestMapping(value = "/delItem", method = RequestMethod.GET)
    void delItem(@RequestParam("itemId") Long itemId);

}
