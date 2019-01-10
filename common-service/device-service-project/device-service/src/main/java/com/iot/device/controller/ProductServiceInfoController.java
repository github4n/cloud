package com.iot.device.controller;

import com.iot.common.helper.Page;
import com.iot.device.api.ProductServiceInfoApi;
import com.iot.device.service.ProductServiceInfoService;
import com.iot.device.vo.req.product.ProductServiceInfoReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.rsp.product.ProductServiceInfoResp;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *@description 产品购买虚拟服务信息controller
 *@author wucheng
 *@create 2018/12/21 16:59
 */
@RestController
public class ProductServiceInfoController implements ProductServiceInfoApi{

    @Autowired
    private ProductServiceInfoService productServiceInfoService;

    @Override
    public int saveProductServiceInfo(@RequestBody  ProductServiceInfoReq req) {
        return productServiceInfoService.saveProductServiceInfo(req);
    }

    @Override
    public int updateAuditStatusIsNull(@RequestParam("tenantId") Long tenantId, @RequestParam("productId") Long productId, @RequestParam("serviceId") Long serviceId) {
        return productServiceInfoService.updateAuditStatusIsNull(tenantId, productId, serviceId);
    }

    @Override
    public int deleteProductServiceInfo(@RequestParam("tenantId") Long tenantId, @RequestParam("productId") Long productId,@RequestParam(name = "serviceId", required=false) Long serviceId) {
        return productServiceInfoService.deleteProductServiceInfo(tenantId, productId, serviceId);
    }

    @Override
    public int updateAuditStatus(@RequestParam("tenantId") Long tenantId, @RequestParam("productId") Long productId, @RequestParam("serviceId") Long serviceId, @RequestParam("auditStatus") Integer auditStatus) {
        return productServiceInfoService.updateAuditStatus(tenantId, productId, serviceId, auditStatus);
    }

    @Override
    public ProductServiceInfoResp getProductServiceInfo(@RequestParam("tenantId") Long tenantId,@RequestParam("productId") Long productId, @RequestParam("serviceId") Long serviceId) {
        return productServiceInfoService.getProductServiceInfo(tenantId, productId, serviceId);
    }

    @Override
    public List<ProductServiceInfoResp> getServiceInfoByProductId(@RequestParam("tenantId") Long tenantId, @RequestParam("productId") Long productId){
        return productServiceInfoService.getServiceInfoByProductId(tenantId, productId);
    }

    @Override
    public Page<ServiceAuditListResp> queryServiceAuditList(@RequestBody  ServiceAuditPageReq pageReq) {
        return productServiceInfoService.queryServiceAuditList(pageReq);
    }
}
