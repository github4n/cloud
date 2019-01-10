package com.iot.tenant.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.tenant.api.fallback.TenantAddresManageApiFallbackFactory;
import com.iot.tenant.vo.req.TenantAddresManageReq;
import com.iot.tenant.vo.resp.TenantAddresManageResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：租户地址服务
 * 功能描述：租户地址服务
 * 创建人： 李帅
 * 创建时间：2018年9月11日 下午2:31:47
 * 修改人：李帅
 * 修改时间：2018年9月11日 下午2:31:47
 */
@Api(tags = "租户地址服务接口")
@FeignClient(value = "tenant-service", fallbackFactory = TenantAddresManageApiFallbackFactory.class)
@RequestMapping("/addres")
public interface TenantAddresManageApi {

    @ApiOperation("保存租户地址信息")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void save(@RequestBody TenantAddresManageReq req);
    
    @ApiOperation("更新租户地址信息")
    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void update(@RequestBody TenantAddresManageReq req);
    
    @ApiOperation("删除租户地址信息")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    void delete(@RequestParam("id") Long id, @RequestParam("tenantId") Long tenantId);
    
    @ApiOperation("通过租户ID获取租户地址信息")
    @RequestMapping(value = "/getAddresByTenantId", method = RequestMethod.GET)
    List<TenantAddresManageResp> getAddresByTenantId(@RequestParam("tenantId") Long tenantId);
    
}
