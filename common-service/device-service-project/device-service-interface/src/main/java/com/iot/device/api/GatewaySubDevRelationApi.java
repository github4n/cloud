package com.iot.device.api;

import com.iot.device.api.fallback.GatewaySubDevApiFallbackFactory;
import com.iot.device.vo.req.gatewaysubdev.GatewaySubDevRelationReq;
import com.iot.device.vo.rsp.gatewaysubdev.GatewaySubDevRelationResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Api(tags = "网关子设备关联接口")
@FeignClient(value="device-service", fallbackFactory = GatewaySubDevApiFallbackFactory.class)
@RequestMapping("/gatewaySubDevRelation")
public interface GatewaySubDevRelationApi {

    @ApiOperation("批量新增网关子设备关联信息")
    @RequestMapping(value = "/batchInsert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int batchInsert(@RequestBody List<GatewaySubDevRelationReq> gatewaySubDevRelationReq);

    @ApiOperation("根据id批量删除网关子设备信息")
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    int deleteById(@RequestParam("ids") List<Long> ids);

    @ApiOperation("根据网关产品id，获取该网关关联的子设备")
    @RequestMapping(value = "/getGatewaySubDevByParDevId", method = RequestMethod.POST)
    List<GatewaySubDevRelationResp> getGatewaySubDevByParDevId(@RequestParam("parDevId") Long parDevId,@RequestParam("tenantId") Long tenantId);

    @ApiOperation("根据网关产品id，获取该网关关联的子设备")
    @RequestMapping(value = "/getGatewaySubDevByParDevIds", method = RequestMethod.GET)
    List<GatewaySubDevRelationResp> getGatewaySubDevByParDevIds(@RequestParam("parDevIds") List parDevIds,@RequestParam("tenantId") Long tenantId);

    @ApiOperation(value = "父产品id列表", notes = "父产品id列表")
    @RequestMapping(value = "/parentProductIds", method = RequestMethod.GET)
    List<Long> parentProductIds(@RequestParam("productId") Long productId, @RequestParam("tenantId") Long tenantId);
}
