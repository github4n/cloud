package com.iot.shcs.widget.api;

import com.iot.shcs.widget.vo.req.UserWidgetReq;
import com.iot.shcs.widget.vo.resp.UserWidgetResp;
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
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/3 22:20
 * 修改人:
 * 修改时间：
 */

@Api(tags = "用户widget接口")
@FeignClient(value = "smart-home-service")
@RequestMapping("/widget")
public interface UserWidgetApi {

    @ApiOperation("获取用户已选中widget数据")
    @RequestMapping(value = "/getByUserId", method = RequestMethod.GET)
    List<UserWidgetResp> getByUserId(@RequestParam(value = "userId", required = true) Long userId);

    @ApiOperation("生成一个widget记录")
    @RequestMapping(value = "/addUserWidget", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addUserWidget(@RequestBody UserWidgetReq userWidgetReq);

    @ApiOperation("删除一个widget记录")
    @RequestMapping(value = "/deleteUserWidget", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteUserWidget(@RequestBody UserWidgetReq userWidgetReq);
}
