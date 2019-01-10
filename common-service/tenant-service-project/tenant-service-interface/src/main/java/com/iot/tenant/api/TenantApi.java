package com.iot.tenant.api;

import com.iot.common.helper.Page;
import com.iot.tenant.api.fallback.TenantApiFallbackFactory;
import com.iot.tenant.vo.req.*;
import com.iot.tenant.vo.resp.AppPackResp;
import com.iot.tenant.vo.resp.TenantInfoResp;
import com.iot.tenant.vo.resp.TenantReviewRecordInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api(tags = "租户服务接口")
@FeignClient(value = "tenant-service", fallbackFactory = TenantApiFallbackFactory.class)
@RequestMapping("/tenant")
public interface TenantApi {

    @ApiOperation("分页获取租户信息")
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<TenantInfoResp> list(@RequestBody GetTenantReq req);

    @ApiOperation("保存租户信息")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long save(@RequestBody SaveTenantReq req);

    @ApiOperation("删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    Boolean delete(@RequestBody List<Long> ids);

    @ApiOperation("根据租户id获取租户信息")
    @RequestMapping(value = "/getTenantById", method = RequestMethod.GET)
    TenantInfoResp getTenantById(@RequestParam("id") Long id);

    @ApiOperation("根据租户id批量获取租户信息")
    @RequestMapping(value = "/getTenantByIds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<TenantInfoResp> getTenantByIds(@RequestBody List<Long> ids);
    
    @ApiOperation("根据租户唯一标示code获取租户信息")
    @RequestMapping(value = "/getTenantByCode", method = RequestMethod.GET)
    TenantInfoResp getTenantByCode(@RequestParam("code") String code);

    @ApiOperation("获取租户信息列表")
    @RequestMapping(value = "/getTenantList", method = RequestMethod.GET)
    List<TenantInfoResp> getTenantList();


    ///////////////////////////////////////App打包//////////////////////////////////////////////////////////
    @ApiOperation("保存app打包配置")
    @RequestMapping(value = "/saveAppPack", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Boolean saveAppPack(@RequestBody SaveAppPackReq req);

    @ApiOperation("根据客户代码获取打包信息")
    @RequestMapping(value = "/getAppPack", method = RequestMethod.GET)
    AppPackResp getAppPack(@RequestParam("code") String code);
    
    @ApiOperation("租户审核信息列表")
    @RequestMapping(value = "/tenantAuditList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<TenantInfoResp> tenantAuditList(@RequestBody GetAuditTenantReq req);
    
    @ApiOperation("保存租户审核记录")
    @RequestMapping(value = "/saveTenantReviewRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveTenantReviewRecord(@RequestBody SaveTenantReviewRecordReq req);
    
    @ApiOperation("获取租户审核记录")
    @RequestMapping(value = "/getTenantReviewRecordByTenantId", method = RequestMethod.GET)
    List<TenantReviewRecordInfoResp> getTenantReviewRecordByTenantId(@RequestParam("tenantId") Long tenantId);

    /**
      * @despriction：通过租户名称查询租户id
      * @author  yeshiyuan
      * @created 2018/10/30 16:55
      * @return
      */
    @ApiOperation("通过租户名称查询租户id")
    @RequestMapping(value = "/searchTenantIdsByName", method = RequestMethod.GET)
    List<Long> searchTenantIdsByName(@RequestParam("name") String name);

    /**
     * 描述：更改租户code
     * @author maochengyuan
     * @created 2018/11/21 20:15
     * @param newCode 新租户code
     * @param oldCode 旧租户code
     * @param tenantId 租户id
     * @return long
     */
    @ApiOperation("更改租户code")
    @RequestMapping(value = "/updateTenantCode", method = RequestMethod.GET)
    void updateTenantCode(@RequestParam("newCode") String newCode, @RequestParam("oldCode") String oldCode, @RequestParam("tenantId") Long tenantId);

}
