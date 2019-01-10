package com.iot.device.service;

import com.iot.common.helper.Page;
import com.iot.device.vo.req.product.ProductServiceInfoReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.rsp.product.ProductServiceInfoResp;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *@description 产品关联第三方服务接口
 *@author wucheng
 *@create 2018/12/21 18:18
 */
public interface ProductServiceInfoService {
    /**
     *@description 新增
     *@author wucheng
     *@params [req]
     *@create 2018/12/21 18:25
     *@return int
     */
    int saveProductServiceInfo(ProductServiceInfoReq req);

    /**
     *@description 根据tenantId, productId, serviceId 更新审核状态为null
     *@author wucheng
     *@params [tenantId, productId, serviceId]
     *@create 2018/12/21 18:25
     *@return int
     */
    int updateAuditStatusIsNull(Long tenantId, Long productId,  Long serviceId);
    /**
     *@description
     *@author wucheng 根据tenantId, productId, serviceId, auditStatus 更新AuditStatus
     *@params [tenantId, productId, serviceId, auditStatus]
     *@create 2018/12/21 18:49
     *@return int
     */
    int updateAuditStatus(Long tenantId, Long productId,  Long serviceId, Integer auditStatus);
    /**
     *@description 根据tenantId, productId删除产品关联增值服务
     *@author wucheng
     *@params [tenantId, productId, serviceId]
     *@create 2018/12/21 18:25
     *@return int
     */
    int deleteProductServiceInfo(Long tenantId, Long productId, Long serviceId);

    /**
     *@description 根据tenantId, productId, serviceId获取产品关联的第三方服务信息
     *@author wucheng
     *@params [tenantId, productId, serviceId]
     *@create 2018/12/24 9:06
     *@return com.iot.device.vo.rsp.product.ProductServiceInfoResp
     */
    ProductServiceInfoResp getProductServiceInfo(Long tenantId, Long productId, Long serviceId);

    /**
     *@description 根据tenantId, productId获取产品关联的第三方服务信息
     *@author chq
     *@params [tenantId, productId]
     *@create 2019/1/4 9:06
     *@return com.iot.device.vo.rsp.product.ProductServiceInfoResp
     */
    List<ProductServiceInfoResp> getServiceInfoByProductId(Long tenantId, Long productId);

    /**
     *@description
     *@author wucheng
     *@params [page, pageReq]
     *@create 2018/12/24 13:43
     *@return java.util.List<com.iot.device.vo.rsp.servicereview.ServiceAuditListResp>
     */
    Page<ServiceAuditListResp> queryServiceAuditList(ServiceAuditPageReq pageReq);
}
