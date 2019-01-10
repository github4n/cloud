package com.iot.device.controller;

import com.iot.device.api.GatewaySubDevRelationApi;
import com.iot.device.service.GatewaySubDevRelationService;
import com.iot.device.vo.req.gatewaysubdev.GatewaySubDevRelationReq;
import com.iot.device.vo.rsp.gatewaysubdev.GatewaySubDevRelationResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class GatewaySubDevRelationController implements GatewaySubDevRelationApi{

    @Autowired
    private GatewaySubDevRelationService gatewaySubDevRelationService;

    @Override
    public int batchInsert(@RequestBody List<GatewaySubDevRelationReq> gatewaySubDevRelationReq) {
        return gatewaySubDevRelationService.batchInsert(gatewaySubDevRelationReq);
    }

    @Override
    public int deleteById(@RequestParam("ids") List<Long> ids) {
        return gatewaySubDevRelationService.deleteById(ids);
    }

    @Override
    public List<GatewaySubDevRelationResp> getGatewaySubDevByParDevId(@RequestParam("parDevId") Long parDevId,@RequestParam("tenantId") Long tenantId) {
        return gatewaySubDevRelationService.getGatewaySubDevByParDevId(parDevId, tenantId);
    }

    @Override
    public List<GatewaySubDevRelationResp> getGatewaySubDevByParDevIds(@RequestParam("parDevIds") List parDevIds,@RequestParam("tenantId") Long tenantId) {
        return gatewaySubDevRelationService.getGatewaySubDevByParDevIds(parDevIds, tenantId);
    }

    @Override
    public List<Long> parentProductIds(@RequestParam("productId") Long productId, @RequestParam("tenantId") Long tenantId) {
        return gatewaySubDevRelationService.parentProductIds(productId, tenantId);
    }
}
