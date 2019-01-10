package com.iot.device.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iot.device.vo.rsp.product.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.common.helper.Page;
import com.iot.device.api.fallback.ProductApiFallbackFactory;
import com.iot.device.vo.req.AddProductReq;
import com.iot.device.vo.req.DataPointReq;
import com.iot.device.vo.req.GetProductReq;
import com.iot.device.vo.req.ProductInfoResp;
import com.iot.device.vo.req.ProductPageReq;
import com.iot.device.vo.req.ProductPublishHistoryReq;
import com.iot.device.vo.req.ProductReq;
import com.iot.device.vo.req.ProductStepVoReq;
import com.iot.device.vo.req.ota.ProductOtaReq;
import com.iot.device.vo.req.product.ProductAuditPageReq;
import com.iot.device.vo.req.product.ProductConfirmReleaseReq;
import com.iot.device.vo.req.product.ReopenAuditReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.rsp.DataPointResp;
import com.iot.device.vo.rsp.ProductPageResp;
import com.iot.device.vo.rsp.ProductPublishHistoryResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "产品接口")
@FeignClient(value = "device-service", fallbackFactory = ProductApiFallbackFactory.class)
@RequestMapping("/product")
public interface ProductApi {

    @ApiOperation(value = "保存产品", notes = "保存产品数据")
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long saveProduct(@RequestBody ProductReq product);

    @ApiOperation(value = "根据产品id删除产品", notes = "删除产品数据")
    @RequestMapping(value = "/deleteProductById", method = RequestMethod.DELETE)
    boolean deleteProductById(@RequestParam("id") Long id);

    @ApiOperation(value = "根据产品id查询产品明细", notes = "查询产品数据")
    @RequestMapping(value = "/getProductById", method = RequestMethod.GET)
    ProductResp getProductById(@RequestParam("id") Long id);

    /**
     *@description 批量获取产品数据
     *@author wucheng
     *@params [ids]
     *@create 2018/12/12 16:38
     *@return java.util.List<com.iot.device.vo.rsp.ProductResp>
     */
    @ApiOperation(value = "根据产品ids获取产品明细", notes = "批量获取产品数据")
    @RequestMapping(value = "/getProductByIds", method = RequestMethod.POST)
    List<PackageProductNameResp> getProductByIds(@RequestParam("ids") List<Long> ids);

    @ApiOperation(value = "根据产品id查询产品明细[包括功能组]", notes = "查询产品数据[包括功能组]")
    @RequestMapping(value = "/getProductInfoByProductId", method = RequestMethod.GET)
    ProductInfoResp getProductInfoByProductId(@RequestParam("productId") Long productId);

    @ApiOperation(value = "根据产品id查询部分产品信息", notes = "查询产品数据")
    @RequestMapping(value = "/getSomeProductPropertyById", method = RequestMethod.GET)
    ProductResp getSomeProductPropertyById(@RequestParam("id") Long id);

    @ApiOperation(value = "根据产品model查询产品明细", notes = "查询产品数据")
    @RequestMapping(value = "/getProductByModel", method = RequestMethod.GET)
    ProductResp getProductByModel(@RequestParam(value = "model") String model);

    @ApiOperation(value = "更新产品", notes = "更新产品数据")
    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean updateProduct(@RequestBody ProductReq product);

    @ApiOperation(value = "根据设备类型id查询产品列表", notes = "根据设备类型id查询产品列表")
    @RequestMapping(value = "/findProductListByDeviceTypeId", method = RequestMethod.GET)
    List<ProductResp> findProductListByDeviceTypeId(@RequestParam("deviceTypeId") @ApiParam(value = "deviceTypeId", required = true) Long deviceTypeId);


    @ApiOperation(value = "根据设备类型id获取产品详情")
    @RequestMapping(value = "/getProductIdByDeviceTypeId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    ProductResp getProductByDeviceTypeIdAndProperties(@RequestParam("deviceTypeId") Long deviceTypeId, @RequestBody List<Map<String, Object>> propertyList);

    @ApiOperation(value = "根据产品id获取产品功能点列表", notes = "根据产品id获取产品功能点列表")
    @RequestMapping(value = "/findDataPointListByProductId", method = RequestMethod.GET)
    List<DataPointResp> findDataPointListByProductId(@RequestParam("productId") @ApiParam(value = "productId", required = true) Long productId);

    @ApiOperation(value = "根据租户id获取产品列表", notes = "根据租户id获取产品列表")
    @RequestMapping(value = "/findProductListByTenantId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ProductResp> findProductListByTenantId(@RequestBody ProductReq product);

    @ApiOperation(value = "根据租户id获取主要的产品列表", notes = "根据租户id获取主要的产品列表")
    @RequestMapping(value = "/findPrimaryProductListByTenantId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ProductResp> findPrimaryProductListByTenantId(@RequestBody ProductReq product);


    @ApiOperation(value = "根据租户id获取直连产品列表", notes = "根据租户id获取直连产品列表")
    @RequestMapping(value = "/findDirectProductListByTenantId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ProductResp> findDirectProductListByTenantId(@RequestBody ProductReq product);
    
    @ApiOperation(value = "Boss获取所有直连产品列表", notes = "Boss获取产品列表")
    @RequestMapping(value = "/findAllDirectProductList", method = RequestMethod.GET)
    List<ProductResp> findAllDirectProductList();

    @ApiOperation(value = "根据model和tenantId获取产品详细", notes = "根据model和tenantId获取产品详细")
    @RequestMapping(value = "/getProductByModelAndTenantId", method = RequestMethod.GET)
    ProductResp getProductByModelAndTenantId(@RequestParam("productModel") String productModel, @RequestParam("tenantId") Long tenantId);

    @ApiOperation("保存自定义功能点")
    @RequestMapping(value = "saveDataPoint/{productId}", method = RequestMethod.POST)
    boolean saveDataPoint(@PathVariable("productId") Long productId, @RequestBody ArrayList<DataPointReq> reqs);

    @ApiOperation(value = "根据租户id获取产品列表分页", notes = "根据租户id获取产品列表")
    @RequestMapping(value = "/findProductPageByTenantId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<ProductResp> findProductPageByTenantId(@RequestBody GetProductReq req);

    /***********************************************portal***************************************************************************************/

    /**
     * 分页获取产品列表
     *
     * @param pageReq
     * @return
     * @author lucky
     * @date 2018/6/27 16:20
     */
    @ApiOperation(value = "分页获取产品列表", notes = "分页获取产品列表")
    @RequestMapping(value = "/findProductPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<ProductPageResp> findProductPage(@RequestBody ProductPageReq pageReq);


    /**
     * add 添加产品信息
     *
     * @param productReq
     * @return
     * @author lucky
     * @date 2018/6/29 14:10
     */
    @ApiOperation(value = "添加产品", notes = "添加产品")
    @RequestMapping(value = "/addProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long addProduct(@RequestBody AddProductReq productReq);

    @ApiOperation(value = "更新产品", notes = "更新产品")
    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long updateProduct(@RequestBody AddProductReq productReq);

    @ApiOperation(value = "更新产品基本信息", notes = "更新产品基本信息")
    @RequestMapping(value = "/updateProductBaseInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long updateProductBaseInfo(@RequestBody AddProductReq productReq);

    @ApiOperation(value = "添加产品对应的功能组", notes = "添加产品对应的功能组")
    @RequestMapping(value = "/updateDevelopInfoByProductId", method = RequestMethod.GET)
    void updateDevelopInfoByProductId(@RequestParam("enterpriseDevelopId") Long enterpriseDevelopId, @RequestParam("productId") Long productId);

    /**
     * 通过设备类型id获取产品
     *
     * @param deviceTypeId
     * @return
     */
    @ApiOperation(value = "通过设备类型id获取产品", notes = "通过设备类型id获取产品")
    @RequestMapping(value = "/findProductByDeviceId", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    ProductResp findProductByDeviceTypeId(@RequestParam("deviceTypeId") Long deviceTypeId);

    @ApiOperation(value = "删除产品", notes = "删除产品")
    @RequestMapping(value = "/delProductByProductId", method = RequestMethod.GET)
    void delProductByProductId(@RequestParam("productId") Long productId);

    @ApiOperation(value = "拷贝产品", notes = "拷贝产品")
    @RequestMapping(value = "/copyProductByProductId", method = RequestMethod.GET)
    Long copyProductByProductId(@RequestParam("productId") Long productId);

    @ApiOperation(value = "产品分页查询")
    @RequestMapping(value = "/getProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<ProductResp> getProduct(@RequestBody ProductOtaReq productOtaReq);

    @ApiOperation(value = "获取产品步骤", notes = "获取产品步骤")
    @RequestMapping(value = "/getStepByProductId", method = RequestMethod.GET)
    Integer getStepByProductId(@RequestParam("productId") Long productId);

    @ApiOperation(value = "添加产品步骤")
    @RequestMapping(value = "/addProductStep", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addProductStep(@RequestBody ProductStepVoReq stepVoReq);

    /**
     * @return
     * @despriction：产品确认发布
     * @author yeshiyuan
     * @created 2018/9/12 14:51
     */
    @ApiOperation(value = "产品确认发布", notes = "产品确认发布")
    @RequestMapping(value = "confirmRelease", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void confirmRelease(@RequestBody ProductConfirmReleaseReq req);

    @ApiOperation(value = "添加发布历史记录")
    @RequestMapping(value = "/addProductPublish", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addProductPublish(@RequestBody ProductPublishHistoryReq req);

    @ApiOperation(value = "查询发布历史记录")
    @RequestMapping(value = "/getProductPublishHis", method = RequestMethod.GET)
    List<ProductPublishHistoryResp> getProductPublishHis(@RequestParam("productId") Long productId, @RequestParam("tenantId") Long tenantId);

    /**
     * @return
     * @despriction：查询产品审核列表（boss使用）
     * @author yeshiyuan
     * @created 2018/10/24 14:36
     */
    @ApiOperation(value = "查询产品审核列表（boss使用）", notes = "查询产品审核列表（boss使用）")
    @RequestMapping(value = "/queryProductAuditList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<ProductAuditListResp> queryProductAuditList(@RequestBody ProductAuditPageReq pageReq);

    /**
     * 描述：查询语音服务审核审核列表（boss使用）
     *
     * @param pageReq
     * @return
     * @author 李帅
     * @created 2018年10月26日 上午11:31:25
     * @since
     */
    @ApiOperation(value = "查询语音服务审核审核列表（boss使用）", notes = "查询语音服务审核审核列表（boss使用）")
    @RequestMapping(value = "/queryServiceAuditList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<ServiceAuditListResp> queryServiceAuditList(@RequestBody ServiceAuditPageReq pageReq);

    /**
     * @return
     * @despriction：重开审核
     * @author yeshiyuan
     * @created 2018/10/25 17:06
     */
    @ApiOperation(value = "重开审核", notes = "重开审核")
    @RequestMapping(value = "/reOpenAudit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void reOpenAudit(@RequestBody ReopenAuditReq reopenAuditReq);

    /**
     * @descrpiction:
     * @author wucheng
     * @created 2018/11/9 14:11
     * @param 
     * @return
     */
    @ApiOperation(value = "根据tentantId和通信类型communicationMode获取设备列表", notes = "根据tentantId和通信类型communicationMode获取设备列表")
    @RequestMapping(value = "/getProductListByTenantIdAndCommunicationMode", method = RequestMethod.POST)
    List<ProductMinimumSubsetResp> getProductListByTenantIdAndCommunicationMode(@RequestParam("tenantId") Long tenantId, @RequestParam("communicationMode") Long communicationMode);

    /**
     * @descrpiction: 根据id更新 gooAuditStatus 和 alxAuditStatus 审核状态
     * @author wucheng
     * @created 2018/11/15 14:47
     * @param
     * @return
     */
    @ApiOperation(value = "根据id更新 gooAuditStatus 和 alxAuditStatus 审核状态", notes = "根据id更新 gooAuditStatus 和 alxAuditStatus 审核状态")
    @RequestMapping(value = "/updateServiceGooAndAlxAuditStatus", method = RequestMethod.POST)
    int updateServiceGooAndAlxAuditStatus(@RequestParam("productId")Long productId, @RequestParam("flag") int flag,@RequestParam("gooAuditStatus")Integer gooAuditStatus, @RequestParam("alxAuditStatus")Integer alxAuditStatus);
    
    /***********************************************套包***********************************************/
    /**
     * 
     * 描述：查询套包产品
     * @author 李帅
     * @created 2018年12月10日 下午3:41:26
     * @since 
     * @param tenantId
     * @return
     */
    @ApiOperation(value = "查询套包产品", notes = "查询套包产品")
    @RequestMapping(value = "/getPackageProducts", method = RequestMethod.GET)
    List<PackageProductResp> getPackageProducts(@RequestParam("tenantId") Long tenantId);

    /**
     * 
     * 描述：查询网管子产品
     * @author 李帅
     * @created 2018年12月10日 下午3:41:26
     * @since 
     * @param tenantId
     * @return
     */
    @ApiOperation(value = "查询网管子产品", notes = "查询网管子产品")
    @RequestMapping(value = "/getGatewayChildProducts", method = RequestMethod.GET)
    List<GatewayChildProductResp> getGatewayChildProducts(@RequestParam("tenantId") Long tenantId, @RequestParam("productId") Long productId);
    /***********************************************套包***********************************************/
}
