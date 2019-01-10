package com.iot.device.controller;

import com.iot.common.exception.BusinessException;
import com.iot.device.api.ServiceReviewApi;
import com.iot.device.exception.DeviceExceptionEnum;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.exception.ServiceReviewExceptionEnum;
import com.iot.device.model.Product;
import com.iot.device.model.ServiceReviewRecord;
import com.iot.device.service.IProductService;
import com.iot.device.service.IServiceReviewService;
import com.iot.device.service.ProductServiceInfoService;
import com.iot.device.vo.req.AddProductReq;
import com.iot.device.vo.req.product.ProductServiceInfoReq;
import com.iot.device.vo.req.servicereview.GetServiceReviewReq;
import com.iot.device.vo.req.servicereview.SetServiceReviewReq;
import com.iot.device.vo.rsp.product.ProductServiceInfoResp;
import com.iot.device.vo.rsp.servicereview.ServiceReviewRecordResp;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.enums.AuditStatusEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class ServiceReviewController implements ServiceReviewApi {

	private static final String google = "0010";
	
	private static final String amazon = "0011";
	
	@Autowired
	IServiceReviewService serviceReviewService;

	@Autowired
	IProductService productService;
	
	@Autowired
	GoodsServiceApi goodsServiceApi;

	@Autowired
	private ProductServiceInfoService productServiceInfoService; // 产品关联的第三方服务
	/**
	 * 
	 * 描述：提交语音服务审核
	 * @author 李帅
	 * @created 2018年10月25日 上午11:27:32
	 * @since 
	 * @param setServiceReviewReq
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public void submitServiceReview(@RequestBody SetServiceReviewReq setServiceReviewReq) throws BusinessException {
		if (setServiceReviewReq.getTenantId() == null) {
			throw new BusinessException(DeviceExceptionEnum.TENANTID_IS_NULL);
		}
		if(setServiceReviewReq.getProductId() == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_ID_NOTNULL);
		}
		Product product = productService.getProductByProductId(setServiceReviewReq.getProductId());
		if(product == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
		}
		if(product.getTenantId() != setServiceReviewReq.getTenantId()) {
			throw new BusinessException(ServiceReviewExceptionEnum.PRODUCT_NOT_TENANT);
		}
		//只有当状态为待审核才允许进行审核操作
//        if(!AuditStatusEnum.Pending.getAuditStatus().equals(product.getAuditStatus())){
//            throw new BusinessException(ServiceReviewExceptionEnum.SERVICE_REVIEW_STATUS_ERROR);
//        }
        //获取商品信息
        if(setServiceReviewReq.getServiceId() == null) {
        	throw new BusinessException(ServiceReviewExceptionEnum.SERVICEID_IS_NULL);
        }
        GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(setServiceReviewReq.getServiceId());
        if(goodsInfo == null) {
        	throw new BusinessException(ServiceReviewExceptionEnum.GOODSINFO_NOT_EXIST);
        }
        //修改产品名称
//        AddProductReq addProductReq = new AddProductReq();
//        addProductReq.setId(setServiceReviewReq.getProductId());
//        addProductReq.setTenantId(setServiceReviewReq.getTenantId());
//        if(google.equals(goodsInfo.getGoodsCode())) {
//        	addProductReq.setServiceGooAuditStatus(Integer.parseInt(AuditStatusEnum.Pending.getAuditStatus().toString()));
//        }else if(amazon.equals(goodsInfo.getGoodsCode())){
//        	addProductReq.setServiceAlxAuditStatus(Integer.parseInt(AuditStatusEnum.Pending.getAuditStatus().toString()));
//        }
//        productService.onlyUpdateProduct(addProductReq);
		// 保存产品第三方增值服务到关联表中去
		ProductServiceInfoReq productServiceInfoReq = new ProductServiceInfoReq();
		productServiceInfoReq.setProductId(setServiceReviewReq.getProductId());
		productServiceInfoReq.setServiceId(setServiceReviewReq.getServiceId());
		productServiceInfoReq.setTenantId(setServiceReviewReq.getTenantId());
		productServiceInfoReq.setAuditStatus(Integer.parseInt(AuditStatusEnum.Pending.getAuditStatus().toString()));
		productServiceInfoReq.setCreateBy(setServiceReviewReq.getUserId());
		productServiceInfoReq.setCreateTime(new Date());
		// 查询该服务是否已经关联第三方信息，存在就删除，再去插入
		ProductServiceInfoResp productServiceInfoResp = productServiceInfoService.getProductServiceInfo(setServiceReviewReq.getTenantId(),setServiceReviewReq.getProductId(),setServiceReviewReq.getServiceId());
        if (productServiceInfoResp != null) {
			productServiceInfoService.deleteProductServiceInfo(setServiceReviewReq.getTenantId(),setServiceReviewReq.getProductId(),setServiceReviewReq.getServiceId());
        }
		productServiceInfoService.saveProductServiceInfo(productServiceInfoReq);

        //保存审核记录
        ServiceReviewRecord serviceReviewRecord = new ServiceReviewRecord();
        serviceReviewRecord.setTenantId(setServiceReviewReq.getTenantId());
        serviceReviewRecord.setProductId(setServiceReviewReq.getProductId());
        serviceReviewRecord.setServiceId(setServiceReviewReq.getServiceId());
        serviceReviewRecord.setProcessStatus(Integer.parseInt(AuditStatusEnum.Pending.getAuditStatus().toString()));
        serviceReviewRecord.setOperateTime(new Date());
        serviceReviewRecord.setOperateDesc(setServiceReviewReq.getOperateDesc());
        serviceReviewRecord.setCreateBy(setServiceReviewReq.getUserId());
        serviceReviewRecord.setCreateTime(new Date());
        serviceReviewRecord.setIsDeleted("valid");
		serviceReviewService.saveServiceReviewRecord(serviceReviewRecord);
	}
	
	/**
	 * 
	 * 描述：语音服务审核操作
	 * @author 李帅
	 * @created 2018年10月25日 上午11:27:32
	 * @since 
	 * @param setServiceReviewReq
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public void setServiceReview(@RequestBody SetServiceReviewReq setServiceReviewReq) throws BusinessException {
		if (setServiceReviewReq.getTenantId() == null) {
			throw new BusinessException(DeviceExceptionEnum.TENANTID_IS_NULL);
		}
		//判断目标状态是否为"审核通过"或为"审核不通过"
        if(!AuditStatusEnum.checkAuditStatus(setServiceReviewReq.getProcessStatus())){
            throw new BusinessException(ServiceReviewExceptionEnum.SERVICE_REVIEW_TARGET_STATUS_ERROR);
        }
		if(setServiceReviewReq.getProductId() == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_ID_NOTNULL);
		}
		Product product = productService.getProductByProductId(setServiceReviewReq.getProductId());
		if(product == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
		}
		if(product.getTenantId() != setServiceReviewReq.getTenantId()) {
			throw new BusinessException(ServiceReviewExceptionEnum.PRODUCT_NOT_TENANT);
		}
		
        //获取商品信息
        if(setServiceReviewReq.getServiceId() == null) {
        	throw new BusinessException(ServiceReviewExceptionEnum.SERVICEID_IS_NULL);
        }
        GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(setServiceReviewReq.getServiceId());
        if(goodsInfo == null) {
        	throw new BusinessException(ServiceReviewExceptionEnum.GOODSINFO_NOT_EXIST);
        }

		// 获取产品关联的第三方增值服务，并更新其审核信息
		ProductServiceInfoResp productServiceInfoResp = productServiceInfoService.getProductServiceInfo(setServiceReviewReq.getTenantId(), product.getId(), setServiceReviewReq.getServiceId());
        if (productServiceInfoResp != null) {
			if(productServiceInfoResp.getAuditStatus() == null || !(AuditStatusEnum.Pending.getAuditStatus().intValue() == productServiceInfoResp.getAuditStatus())){
				throw new BusinessException(ServiceReviewExceptionEnum.SERVICE_REVIEW_STATUS_ERROR);
			}
			productServiceInfoService.updateAuditStatus(setServiceReviewReq.getTenantId(),setServiceReviewReq.getProductId(), setServiceReviewReq.getServiceId(), Integer.parseInt(setServiceReviewReq.getProcessStatus().toString()));
		}
//		if(google.equals(goodsInfo.getGoodsCode())) {
//        	//只有当状态为待审核才允许进行审核操作
//            if(product.getServiceGooAuditStatus() == null || !(AuditStatusEnum.Pending.getAuditStatus().intValue() == product.getServiceGooAuditStatus())){
//                throw new BusinessException(ServiceReviewExceptionEnum.SERVICE_REVIEW_STATUS_ERROR);
//            }
//        }else if(amazon.equals(goodsInfo.getGoodsCode())){
//        	//只有当状态为待审核才允许进行审核操作
//            if(product.getServiceAlxAuditStatus() == null || !(AuditStatusEnum.Pending.getAuditStatus().intValue() == product.getServiceAlxAuditStatus())){
//                throw new BusinessException(ServiceReviewExceptionEnum.SERVICE_REVIEW_STATUS_ERROR);
//            }
//        }

        //修改产品名称
//        AddProductReq addProductReq = new AddProductReq();
//        addProductReq.setId(setServiceReviewReq.getProductId());
//        addProductReq.setTenantId(setServiceReviewReq.getTenantId());
//        if(google.equals(goodsInfo.getGoodsCode())) {
//        	addProductReq.setServiceGooAuditStatus(Integer.parseInt(setServiceReviewReq.getProcessStatus().toString()));
//        }else if(amazon.equals(goodsInfo.getGoodsCode())){
//        	addProductReq.setServiceAlxAuditStatus(Integer.parseInt(setServiceReviewReq.getProcessStatus().toString()));
//        }
//        productService.onlyUpdateProduct(addProductReq);
        //保存审核记录
        ServiceReviewRecord serviceReviewRecord = new ServiceReviewRecord();
        serviceReviewRecord.setTenantId(setServiceReviewReq.getTenantId());
        serviceReviewRecord.setProductId(setServiceReviewReq.getProductId());
        serviceReviewRecord.setServiceId(setServiceReviewReq.getServiceId());
        serviceReviewRecord.setProcessStatus(Integer.parseInt(setServiceReviewReq.getProcessStatus().toString()));
        serviceReviewRecord.setOperateTime(new Date());
        serviceReviewRecord.setOperateDesc(setServiceReviewReq.getOperateDesc());
        serviceReviewRecord.setCreateBy(setServiceReviewReq.getUserId());
        serviceReviewRecord.setCreateTime(new Date());
        serviceReviewRecord.setIsDeleted("valid");
		serviceReviewService.saveServiceReviewRecord(serviceReviewRecord);
	}

	/**
	 * 
	 * 描述：语音服务重开操作
	 * @author 李帅
	 * @created 2018年10月25日 上午11:27:32
	 * @since 
	 * @param setServiceReviewReq
	 * @return
	 * @throws BusinessException
	 */
	@Override
//	@RequestMapping(value = "/setServiceReview", method = RequestMethod.POST)
	public void reSetServiceReview(@RequestBody SetServiceReviewReq setServiceReviewReq) throws BusinessException {
		if (setServiceReviewReq.getTenantId() == null) {
			throw new BusinessException(DeviceExceptionEnum.TENANTID_IS_NULL);
		}
		//判断目标状态是否为"审核通过"或为"审核不通过"
        if(!AuditStatusEnum.checkAuditStatus(setServiceReviewReq.getProcessStatus())){
            throw new BusinessException(ServiceReviewExceptionEnum.SERVICE_REVIEW_TARGET_STATUS_ERROR);
        }
		if(setServiceReviewReq.getProductId() == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_ID_NOTNULL);
		}
		Product product = productService.getProductByProductId(setServiceReviewReq.getProductId());
		if(product == null) {
			throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
		}
		if(product.getTenantId() != setServiceReviewReq.getTenantId()) {
			throw new BusinessException(ServiceReviewExceptionEnum.PRODUCT_NOT_TENANT);
		}
		
        //获取商品信息
        if(setServiceReviewReq.getServiceId() == null) {
        	throw new BusinessException(ServiceReviewExceptionEnum.SERVICEID_IS_NULL);
        }
        GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsId(setServiceReviewReq.getServiceId());
        if(goodsInfo == null) {
        	throw new BusinessException(ServiceReviewExceptionEnum.GOODSINFO_NOT_EXIST);
        }

		// 获取产品关联的第三方增值服务，并更新其审核信息
		ProductServiceInfoResp productServiceInfoResp = productServiceInfoService.getProductServiceInfo(SaaSContextHolder.currentTenantId(), product.getId(), setServiceReviewReq.getServiceId());
		if (productServiceInfoResp != null) {
			productServiceInfoService.updateAuditStatus(setServiceReviewReq.getTenantId(),setServiceReviewReq.getProductId(), setServiceReviewReq.getServiceId(), Integer.parseInt(setServiceReviewReq.getProcessStatus().toString()));
		}
        //修改产品名称
//        AddProductReq addProductReq = new AddProductReq();
//        addProductReq.setId(setServiceReviewReq.getProductId());
//        addProductReq.setTenantId(setServiceReviewReq.getTenantId());
//        if(google.equals(goodsInfo.getGoodsCode())) {
//        	addProductReq.setServiceGooAuditStatus(Integer.parseInt(setServiceReviewReq.getProcessStatus().toString()));
//        }else if(amazon.equals(goodsInfo.getGoodsCode())){
//        	addProductReq.setServiceAlxAuditStatus(Integer.parseInt(setServiceReviewReq.getProcessStatus().toString()));
//        }
//        productService.onlyUpdateProduct(addProductReq);
        //保存审核记录
        ServiceReviewRecord serviceReviewRecord = new ServiceReviewRecord();
        serviceReviewRecord.setTenantId(setServiceReviewReq.getTenantId());
        serviceReviewRecord.setProductId(setServiceReviewReq.getProductId());
        serviceReviewRecord.setServiceId(setServiceReviewReq.getServiceId());
        serviceReviewRecord.setProcessStatus(Integer.parseInt(setServiceReviewReq.getProcessStatus().toString()));
        serviceReviewRecord.setOperateTime(new Date());
        serviceReviewRecord.setOperateDesc(setServiceReviewReq.getOperateDesc());
        serviceReviewRecord.setCreateBy(setServiceReviewReq.getUserId());
        serviceReviewRecord.setCreateTime(new Date());
        serviceReviewRecord.setIsDeleted("valid");
		serviceReviewService.saveServiceReviewRecord(serviceReviewRecord);
	}
	
	/**
	 * 
	 * 描述：获取语音服务审核记录
	 * @author 李帅
	 * @created 2018年10月25日 下午5:30:42
	 * @since 
	 * @param getServiceReviewReq
	 * @return
	 */
    @Override
    public List<ServiceReviewRecordResp> getServiceReviewRecord(@RequestBody GetServiceReviewReq getServiceReviewReq) {
    	List<ServiceReviewRecord> serviceReviewRecords = this.serviceReviewService.getServiceReviewRecord(getServiceReviewReq);
    	List<ServiceReviewRecordResp> serviceReviewRecordResps = new ArrayList<ServiceReviewRecordResp>();
    	ServiceReviewRecordResp serviceReviewRecordResp = null;
    	for(ServiceReviewRecord serviceReviewRecord : serviceReviewRecords) {
    		serviceReviewRecordResp = new ServiceReviewRecordResp();
    		BeanUtils.copyProperties(serviceReviewRecord, serviceReviewRecordResp);
    		serviceReviewRecordResp.setProcessStatus((byte) serviceReviewRecord.getProcessStatus().byteValue());
    		serviceReviewRecordResps.add(serviceReviewRecordResp);
    	}
        return serviceReviewRecordResps;
    }

	/**
	 * @despriction：获取租户id
	 * @author  yeshiyuan
	 * @created 2018/11/3 14:18
	 * @return
	 */
	@Override
	public Long getTenantIdById(Long id) {
		return serviceReviewService.getTenantIdById(id);
	}

	/**
	 * @despriction：添加记录
	 * @author  yeshiyuan
	 * @created 2018/11/3 14:49
	 * @return
	 */
	@Override
	public Long addRecord(@RequestBody SetServiceReviewReq setServiceReviewReq) {
		//保存审核记录
		ServiceReviewRecord serviceReviewRecord = new ServiceReviewRecord();
		serviceReviewRecord.setTenantId(setServiceReviewReq.getTenantId());
		serviceReviewRecord.setProductId(setServiceReviewReq.getProductId());
		serviceReviewRecord.setServiceId(setServiceReviewReq.getServiceId());
		serviceReviewRecord.setProcessStatus(Integer.parseInt(setServiceReviewReq.getProcessStatus().toString()));
		serviceReviewRecord.setOperateTime(new Date());
		serviceReviewRecord.setOperateDesc(setServiceReviewReq.getOperateDesc());
		serviceReviewRecord.setCreateBy(setServiceReviewReq.getUserId());
		serviceReviewRecord.setCreateTime(new Date());
		serviceReviewRecord.setIsDeleted("valid");
		return serviceReviewService.saveServiceReviewRecord(serviceReviewRecord);
	}
    /**
     *@description 设置退款服务失效
     *@author wucheng
     *@params [productId, tenantId, serviceId]
     *@create 2018/12/26 18:29
     *@return void
     */
	@Override
	public void invalidRecord(@RequestParam("productId") Long productId, @RequestParam("tenantId") Long tenantId, @RequestParam("serviceId") Long serviceId) {
		serviceReviewService.invalidRecord(productId, tenantId, serviceId);
	}
}
