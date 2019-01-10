package com.iot.tenant.api;

import com.iot.common.helper.Page;
import com.iot.tenant.api.fallback.AppVersionApiFallbackFactory;
import com.iot.tenant.vo.req.AppVersionListReq;
import com.iot.tenant.vo.req.CheckVersionRequest;
import com.iot.tenant.vo.req.SaveAppVersionReq;
import com.iot.tenant.vo.resp.AppVersionResp;
import com.iot.tenant.vo.resp.CheckVersionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 描述：App版本接口
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/12 11:04
 */
@Api(tags = "App版本接口")
@FeignClient(value = "tenant-service", fallbackFactory = AppVersionApiFallbackFactory.class)
@RequestMapping("/app-version")
public interface AppVersionApi {

    @ApiOperation("获取列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<AppVersionResp> list(@RequestBody AppVersionListReq req);

    @ApiOperation("保存app版本信息")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long save(@RequestBody SaveAppVersionReq req);

    @ApiOperation("获取最新版本")
    @RequestMapping(value = "/getLastVersion", method = RequestMethod.GET)
    AppVersionResp getLastVersion(@RequestParam("appId") Long appId);

    @ApiOperation("检测app版本升级")
    @RequestMapping(value = "/checkVersion", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    CheckVersionResponse checkVersion(@RequestBody CheckVersionRequest req);
}
