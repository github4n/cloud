package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.common.helper.Page;
import com.iot.device.model.Product;
import com.iot.device.model.ProductPublishHistory;
import com.iot.device.vo.req.AddProductReq;
import com.iot.device.vo.req.GetProductReq;
import com.iot.device.vo.req.ProductPageReq;
import com.iot.device.vo.req.ProductReq;
import com.iot.device.vo.req.ota.ProductOtaReq;
import com.iot.device.vo.req.product.ProductAuditPageReq;
import com.iot.device.vo.req.product.ProductConfirmReleaseReq;
import com.iot.device.vo.req.product.ReopenAuditReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.rsp.DataPointResp;
import com.iot.device.vo.rsp.ProductPageResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.product.*;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
public interface IProductService extends IService<Product> {

    /**
     * 根据产品id获取功能列表
     * @param productId
     * @return
     */
    List<DataPointResp> findDataPointListByProductId(Long productId);

    ProductResp getProductByDeviceTypeIdAndProperties(Long deviceTypeId, List<Map<String,Object>> propertyList);
    
    List<ProductResp> findProductListByTenantId(ProductReq product);

    /**
     * 
     * 描述：根据租户id获取直连产品列表
     * @author 李帅
     * @created 2018年11月8日 上午11:26:28
     * @since 
     * @param product
     * @return
     */
    List<ProductResp> findDirectProductListByTenantId(ProductReq product);
    
    List<ProductResp> findAllDirectProductList();

	Page<ProductResp> findProductPageByTenantId(GetProductReq req);


    /**
     * 分页获取产品列表
     *
     * @param pageReq
     * @return
     * @author lucky
     * @date 2018/6/27 16:17
     */
    Page<ProductPageResp> findProductPage(ProductPageReq pageReq);


    /**
     * 添加产品信息
     *
     * @param productReq
     * @return
     * @author lucky
     * @date 2018/6/28 15:51
     */
    Long addProduct(AddProductReq productReq);

    /**
     * 
     * 描述：只更新产品信息
     * @author 李帅
     * @created 2018年12月6日 下午3:07:05
     * @since 
     * @param productReq
     * @return
     */
    Long onlyUpdateProduct(AddProductReq productReq);
    
    /**
     * 更新产品信息
     *
     * @param productReq
     * @return
     * @author lucky
     * @date 2018/6/28 15:51
     */
    Long updateProduct(AddProductReq productReq);

    Long updateProductBaseInfo(AddProductReq productReq);

    ProductResp findProductByDeviceTypeId(Long deviceTypeId);

    Page<ProductResp> getProduct(ProductOtaReq productOtaReq);

    /**
     * @despriction：产品确认发布
     * @author  yeshiyuan
     * @created 2018/9/12 14:51
     * @return
     */
    int confirmRelease(ProductConfirmReleaseReq req);
	
	void addProductPublish(ProductPublishHistory req);
    
    List<ProductPublishHistory> getProductPublishHis(Long productId, Long tenantId);


    List<Product> findListByProductIds(List<Long> productIds);

    Product getProductByProductId(Long productId);

    Product getProductByProductModel(String productModel);

    List<Product> findListByProductModels(List<String> productModels);

    /**
      * @despriction：查询产品审核列表（boss使用）
      * @author  yeshiyuan
      * @created 2018/10/24 14:36
      * @return
      */
    Page<ProductAuditListResp> queryProductAuditList(ProductAuditPageReq pageReq);

    /**
     *
     * 描述：查询语音服务审核审核列表（boss使用）
     * @author 李帅
     * @created 2018年10月26日 上午11:32:06
     * @since
     * @param pageReq
     * @return
     */
   Page<ServiceAuditListResp> queryServiceAuditList(ServiceAuditPageReq pageReq);

    /**
      * @despriction：修改审核状态
      * @author  yeshiyuan
      * @created 2018/10/24 17:22
      * @return
      */
    int updateAuditStatus(Long productId, Integer auditStatus, Integer developStatus, Date updateTime);

    /**
     * @despriction：重开审核
     * @author  yeshiyuan
     * @created 2018/10/25 17:06
     * @return
     */
    void reOpenAudit(ReopenAuditReq reopenAuditReq);
    /**
     * @descrpiction: 根据租户tenantId 和 CommunicationMode 获取设备列表
     * @author wucheng
     * @created 2018/11/8 14:49
     * @param
     * @return
     */
    List<ProductMinimumSubsetResp> getProductListByTenantIdAndCommunicationMode(Long tenantId, Long communicationMode);

    /**
     * 根据id更新 gooAuditStatus 和 alxAuditStatus 审核状态
     * @param flag 0: 谷歌 1：alexa
     * @param productId
     * @param gooAuditStatus
     * @param alxAuditStatus
     * @return
     */
    int updateServiceGooAndAlxAuditStatus(Long productId, int flag, Integer gooAuditStatus, Integer alxAuditStatus);
    
    /**
     * 
     * 描述：查询套包产品
     * @author 李帅
     * @created 2018年12月10日 下午3:42:21
     * @since 
     * @param tenantId
     * @return
     */
    List<PackageProductResp> getPackageProducts(Long tenantId);
    
    /**
     * 
     * 描述：查询网管子产品
     * @author 李帅
     * @created 2018年12月10日 下午5:23:12
     * @since 
     * @param tenantId
     * @param productId
     * @return
     */
    List<GatewayChildProductResp> getGatewayChildProducts(Long tenantId, Long productId);
    /**
     *@description 套包根据ids查询产品的名称
     *@author wucheng
     *@params [ids]
     *@create 2018/12/12 16:54
     *@return java.util.List<java.util.Map<java.lang.Long,java.lang.String>>
     */
    List<PackageProductNameResp>  getProductByIds(List<Long> ids);
}
