package com.iot.tenant.api;

import com.iot.tenant.api.fallback.AppReviewApiFallbackFactory;
import com.iot.tenant.vo.req.review.AppReviewRecordReq;
import com.iot.tenant.vo.resp.review.AppReviewRecordResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：租户
 * 功能描述：App审核
 * 创建人： maochengyuan
 * 创建时间：2018/10/23 14:35
 * 修改人： maochengyuan
 * 修改时间：2018/10/23 14:35
 * 修改描述：
 */
@Api(tags = "App审核接口")
@FeignClient(value = "tenant-service", fallbackFactory = AppReviewApiFallbackFactory.class)
@RequestMapping("/appReviewApi")
public interface AppReviewApi {

    @ApiOperation("App提交审核")
    @RequestMapping(value = "/submitAudit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void submitAudit(@RequestBody AppReviewRecordReq req);

    @ApiOperation("App审核操作")
    @RequestMapping(value = "/review", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void review(@RequestBody AppReviewRecordReq req);

    @ApiOperation("获取App审核记录")
    @RequestMapping(value = "/getAppReviewRecord", method = RequestMethod.GET)
    List<AppReviewRecordResp> getAppReviewRecord(@RequestParam("appId") Long appId);

    @ApiOperation(value = "获取租户id", notes = "获取租户id")
    @RequestMapping(value = "/getTenantIdById", method = RequestMethod.GET)
    Long getTenantIdById(@RequestParam("id") Long id);

    @ApiOperation("添加App审核记录")
    @RequestMapping(value = "/addRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long addRecord(@RequestBody AppReviewRecordReq req);

    /**
      * @despriction：记录置为失效
      * @author  yeshiyuan
      * @created 2018/11/14 13:46
      * @return
      */
    @ApiOperation(value = "记录置为失效", notes = "记录置为失效")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "appId", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/invalidRecord", method = RequestMethod.POST)
    void invalidRecord(@RequestParam("appId")Long appId, @RequestParam("tenantId") Long tenantId);
}
