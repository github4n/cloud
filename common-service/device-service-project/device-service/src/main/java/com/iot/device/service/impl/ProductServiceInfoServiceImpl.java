package com.iot.device.service.impl;

import com.iot.common.helper.Page;
import com.iot.device.core.utils.BeanCopyUtils;
import com.iot.device.mapper.ProductMapper;
import com.iot.device.mapper.ProductServiceInfoMapper;
import com.iot.device.mapper.ServiceReviewMapper;
import com.iot.device.model.ServiceReviewRecord;
import com.iot.device.service.ProductServiceInfoService;
import com.iot.device.vo.req.product.ProductServiceInfoReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.rsp.product.ProductServiceInfoResp;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;
import com.iot.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 *@description 产品关联的增值服务service
 *@author wucheng
 *@create 2018/12/28 17:59
 */
@Service
public class ProductServiceInfoServiceImpl implements ProductServiceInfoService{

    @Autowired
    private ProductServiceInfoMapper productServiceInfoMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ServiceReviewMapper serviceReviewMapper;
    @Override
    public int saveProductServiceInfo(ProductServiceInfoReq req) {
        return productServiceInfoMapper.saveProductServiceInfo(req);
    }

    @Override
    public int updateAuditStatusIsNull(Long tenantId, Long productId, Long serviceId) {
        checkParamIsNull(tenantId, productId, serviceId);
        return productServiceInfoMapper.updateAuditStatusIsNull(tenantId, productId, serviceId);
    }

    @Override
    public int deleteProductServiceInfo(Long tenantId, Long productId, Long serviceId) {
        AssertUtils.notNull(tenantId, "tenantId is null");
        AssertUtils.notNull(productId, "productId is null");
        return productServiceInfoMapper.deleteProductServiceInfo(tenantId, productId, serviceId);
    }

    @Override
    public int updateAuditStatus(Long tenantId, Long productId, Long serviceId, Integer auditStatus) {
        checkParamIsNull(tenantId, productId, serviceId);
        return productServiceInfoMapper.updateAuditStatus(tenantId, productId, serviceId, auditStatus);
    }

    @Override
    public ProductServiceInfoResp getProductServiceInfo(Long tenantId, Long productId, Long serviceId) {
        checkParamIsNull(tenantId, productId, serviceId);
        return productServiceInfoMapper.getProductServiceInfo(tenantId,  productId, serviceId);
    }

    @Override
    public List<ProductServiceInfoResp> getServiceInfoByProductId(Long tenantId, Long productId){
        AssertUtils.notNull(tenantId, "tenantId is null");
        AssertUtils.notNull(productId, "productId is null");
        return productServiceInfoMapper.getServiceInfoByProductId(tenantId, productId);
    }

    @Override
    public Page<ServiceAuditListResp> queryServiceAuditList(ServiceAuditPageReq pageReq) {
        com.baomidou.mybatisplus.plugins.Page<ServiceAuditListResp> page = new com.baomidou.mybatisplus.plugins.Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        List<ServiceAuditListResp> productRespList = productServiceInfoMapper.queryServiceAuditList(page, pageReq);
//        if (!productRespList.isEmpty()) {
//            //查询申请时间
//            List<Long> productIds = productRespList.stream().map(ServiceAuditListResp::getProductId).collect(Collectors.toList());
//            List<ServiceReviewRecord> applyTimeList = serviceReviewMapper.queryApplyTimeByProductIds(productIds);
//            Map<Long, Date> applyTimeMap = applyTimeList.stream().collect(Collectors.toMap(ServiceReviewRecord::getProductId, a -> a.getOperateTime()));
//            productRespList.forEach(o -> {
//                o.setApplyTime(applyTimeMap.get(o.getProductId()));
//            });
//            if (pageReq.getType() != 0) {
//                if (!productRespList.isEmpty()) {
//                    List<ServiceReviewRecord> reviewRecords = serviceReviewMapper.queryUserIdByProductIds(productIds);
//                    Map<Long, Long> map = reviewRecords.stream().collect(Collectors.toMap(ServiceReviewRecord::getProductId, a -> a.getCreateBy()));
//                    productRespList.forEach(o -> {
//                        o.setOperateUserId(map.get(o.getProductId()));
//                    });
//                }
//            }
//        }
        page.setRecords(productRespList);
        com.iot.common.helper.Page<ServiceAuditListResp> myPage = new com.iot.common.helper.Page<>();
        BeanCopyUtils.copyMybatisPlusPageToPage(page, myPage);
        return myPage;
    }

    /**
     *@description 检测参数是否为空
     *@author wucheng
     *@params [tenantId, productId, serviceId]
     *@create 2018/12/28 17:57
     *@return void
     */
    private void checkParamIsNull(Long tenantId, Long productId, Long serviceId) {
        AssertUtils.notNull(tenantId, "tenantId is null");
        AssertUtils.notNull(productId, "productId is null");
        AssertUtils.notNull(serviceId, "serviceId is null");
    }
}
