package com.iot.device.api;

import com.iot.common.helper.Page;
import com.iot.device.api.fallback.ProductServiceInfoApiFallbackFactory;
import com.iot.device.vo.req.product.ProductServiceInfoReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.rsp.product.ProductServiceInfoResp;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api(tags = "产品增值服务接口")
@FeignClient(value = "device-service", fallbackFactory = ProductServiceInfoApiFallbackFactory.class)
@RequestMapping("/productServiceInfo")
public interface ProductServiceInfoApi {

    /**
     *@description 新增
     *@author wucheng
     *@params [req]
     *@create 2018/12/21 18:25
     *@return int
     */
    @ApiOperation("保存产品关联增值服务信息")
    @RequestMapping(value = "/saveProductServiceInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int saveProductServiceInfo(@RequestBody ProductServiceInfoReq req);

    /**
     *@description 根据tenantId, productId, serviceId 更新审核状态为null
     *@author wucheng
     *@params [tenantId, productId, serviceId]
     *@create 2018/12/21 18:25
     *@return int
     */
    @ApiOperation("修改产品关联服务为初始状态")
    @RequestMapping(value = "/updateAuditStatusIsNull", method = RequestMethod.POST)
    int updateAuditStatusIsNull(@RequestParam("tenantId") Long tenantId, @RequestParam("productId") Long productId, @RequestParam("serviceId") Long serviceId);

    /**
     *@description 根据tenantId, productId, serviceId, auditStatus更新关联服务审核信息
     *@author wucheng
     *@params [tenantId, productId, serviceId, auditStatus]
     *@create 2018/12/21 18:46
     *@return int
     */
    @ApiOperation("修改产品关联第三方服务审核状态")
    @RequestMapping(value = "/updateAuditStatus", method = RequestMethod.POST)
    int updateAuditStatus(@RequestParam("tenantId") Long tenantId, @RequestParam("productId") Long productId, @RequestParam("serviceId") Long serviceId, @RequestParam("auditStatus") Integer auditStatus);
    /**
     *@description 根据tenantId, productId 删除产品关联增值服务
     *@author wucheng
     *@params [tenantId, productId, serviceId]
     *@create 2018/12/21 18:25
     *@return int
     */
    @ApiOperation("删除产品关联第三方服务")
    @RequestMapping(value = "/deleteProductServiceInfo", method = RequestMethod.POST)
    int deleteProductServiceInfo(@RequestParam("tenantId") Long tenantId, @RequestParam("productId") Long productId, @RequestParam(name = "serviceId" ,required=false) Long serviceId);

    /**
     *@description 根据tenantId, productId, serviceId 获取产品关联的服务信息
     *@author wucheng
     *@params [tenantId, productId, serviceId]
     *@create 2018/12/21 18:25
     *@return int
     */
    @ApiOperation("获取产品关联的服务信息")
    @RequestMapping(value = "/getProductServiceInfo", method = RequestMethod.GET)
    ProductServiceInfoResp getProductServiceInfo(@RequestParam("tenantId") Long tenantId, @RequestParam("productId") Long productId, @RequestParam("serviceId") Long serviceId);

    @ApiOperation("通过productId获取产品关联的服务信息")
    @RequestMapping(value = "/getServiceInfoByProductId", method = RequestMethod.GET)
    List<ProductServiceInfoResp> getServiceInfoByProductId(@RequestParam("tenantId") Long tenantId, @RequestParam("productId") Long productId);

    /**
     *@description
     *@author wucheng
     *@params [pageReq]
     *@create 2018/12/24 13:49
     *@return com.iot.common.helper.Page<com.iot.device.vo.rsp.servicereview.ServiceAuditListResp>
     */
    @ApiOperation(value = "查询第三方服务审核审核列表（boss使用）", notes = "查询第三方服务审核审核列表（boss使用）")
    @RequestMapping(value = "/queryServiceAuditList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<ServiceAuditListResp> queryServiceAuditList(@RequestBody ServiceAuditPageReq pageReq);
}
