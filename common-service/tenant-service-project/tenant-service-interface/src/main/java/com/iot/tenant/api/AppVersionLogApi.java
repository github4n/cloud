package com.iot.tenant.api;

import com.github.pagehelper.PageInfo;
import com.iot.tenant.api.fallback.AppVersionLogApiFallbackFactory;
import com.iot.tenant.vo.req.AppVersionLogReq;
import com.iot.tenant.vo.resp.AppVersionLogResp;
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
 * 描述：App版本记录接口
 * 创建人： zhangyue
 */
@Api(tags = "应用版本记录接口",description = "应用版本记录接口")
@FeignClient(value = "tenant-service", fallbackFactory = AppVersionLogApiFallbackFactory.class)
@RequestMapping("/app/version/log")
public interface AppVersionLogApi {

    @ApiOperation("保存app版本信息")
    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long insertOrUpdate(@RequestBody AppVersionLogReq appVersionLogReq);

    @ApiOperation("获取版本信息")
    @RequestMapping(value = "/versionLogByKey", method = RequestMethod.GET)
    AppVersionLogResp versionLogByKey(@RequestParam("systemInfo") String systemInfo,@RequestParam("appPackage") String appPackage,@RequestParam("version") String version);

    @ApiOperation("分页获取版本记录")
    @RequestMapping(value = "/page", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    PageInfo page(@RequestBody AppVersionLogReq appVersionLogReq);

    @ApiOperation("分页获取版本记录")
    @RequestMapping(value = "/delete", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    void delete(@RequestBody List<Long> ids);


}
