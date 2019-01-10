package com.iot.tenant.api;

import com.iot.tenant.vo.req.network.CopyNetworkStepReq;
import com.iot.tenant.vo.req.network.SaveNetworkStepTenantReq;
import com.iot.tenant.vo.resp.network.DeviceNetworkStepTenantResp;
import com.iot.tenant.vo.resp.network.NetworkFileFormat;
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

import java.util.Map;

/**
 * 项目名称：cloud
 * 模块名称：设备配网步骤管理（portal使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/9 11:28
 * 修改人： yeshiyuan
 * 修改时间：2018/10/9 11:28
 * 修改描述：
 */
@Api(description = "设备配网步骤管理（portal使用）", tags = "设备配网步骤管理（portal使用）")
@RequestMapping(value = "/api/deviceNetworkStepTenant")
@FeignClient(value = "tenant-service")
public interface DeviceNetworkStepTenantApi {

    /**
      * @despriction：拷贝配网步骤文案
      * @author  yeshiyuan
      * @created 2018/10/9 11:31
      * @return
      */
    @ApiOperation(value = "拷贝配网步骤文案", notes = "拷贝配网步骤文案")
    @RequestMapping(value = "/copyNetworkStep", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void copyNetworkStep(@RequestBody CopyNetworkStepReq copyNetworkStepReq);

    /**
      * @despriction：保存配网步骤文案（先删后插）
      * @author  yeshiyuan
      * @created 2018/10/9 14:02
      * @return
      */
    @ApiOperation(value = "保存配网步骤文案（先删后插）", notes = "拷贝配网步骤文案（先删后插）")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void save(@RequestBody SaveNetworkStepTenantReq req);

    /**
      * @despriction：查询配网步骤文案
      * @author  yeshiyuan
      * @created 2018/10/9 16:13
      * @return
      */
    @ApiOperation(value = "查询配网步骤文案", notes = "查询配网步骤文案")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "appId", value = "appId", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "productId", value = "产品id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/queryNetworkStep", method = RequestMethod.GET)
    DeviceNetworkStepTenantResp queryNetworkStep(@RequestParam("tenantId") Long tenantId, @RequestParam("appId") Long appId, @RequestParam("productId") Long productId);

    /**
      * @despriction：配网步骤文件格式
      * @author  yeshiyuan
      * @created 2018/10/18 15:47
      * @return
      */
    @ApiOperation(value = "配网步骤文件格式", notes = "配网步骤文件格式")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "appId", value = "appId", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "productId", value = "产品id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getNetworkFileFormat", method = RequestMethod.GET)
    Map<String, NetworkFileFormat> getNetworkFileFormat(@RequestParam("tenantId") Long tenantId, @RequestParam("appId") Long appId, @RequestParam("productId") Long productId);
}
