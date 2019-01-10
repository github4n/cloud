package com.iot.device.controller;

import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.device.api.UuidApplyRecordApi;
import com.iot.device.core.service.ProductServiceCoreUtils;
import com.iot.device.enums.uuid.UuidApplyStatusEnum;
import com.iot.device.enums.uuid.UuidPayStatusEnum;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.exception.UuidExceptionEnum;
import com.iot.device.model.Product;
import com.iot.device.model.UUidApplyRecord;
import com.iot.device.service.IUUIDManegerService;
import com.iot.device.service.IUuidApplyRecordService;
import com.iot.device.vo.req.uuid.UuidApplyForBoss;
import com.iot.device.vo.req.uuid.UuidApplyRecordReq;
import com.iot.device.vo.rsp.uuid.UuidApplyRecordResp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：uuid申请
 * 创建人： yeshiyuan
 * 创建时间：2018/6/29 14:36
 * 修改人： yeshiyuan
 * 修改时间：2018/6/29 14:36
 * 修改描述：
 */
@RestController
public class UuidApplyRecordController implements UuidApplyRecordApi {

    private static Logger LOGGER = LoggerFactory.getLogger(UuidApplyRecordController.class);
    @Autowired
    private IUuidApplyRecordService uuidApplyRecordService;
    @Autowired
    private IUUIDManegerService uuidManegerService;

    /**
     * @despriction：创建uuid申请记录
     * @author  yeshiyuan
     * @created 2018/6/29 14:16
     * @param uuidApplyReq
     * @return
     */
    @Override
    public void createUuidApplyRecord(@RequestBody UuidApplyRecordReq uuidApplyReq) {
        if (uuidApplyReq == null || StringUtil.isBlank(uuidApplyReq.getOrderId()) || uuidApplyReq.getTenantId()==null ){
            throw new BusinessException(UuidExceptionEnum.UUID_PARAM_ERROR);
        }
        UUidApplyRecord uUidApplyRecord = new UUidApplyRecord();
        try{
            BeanUtil.copyProperties(uuidApplyReq,uUidApplyRecord);
            uUidApplyRecord.setCreateTime(new Date());
            uUidApplyRecord.setPayStatus(UuidPayStatusEnum.WAIT_PAY.getValue());
            uUidApplyRecord.setUuidApplyStatus(UuidApplyStatusEnum.WAIT_DEAL.getValue());
//            uUidApplyRecord.setId(RedisCacheUtil.incr(CacheKeyUtils.getTableOfUuidApplyRecordKey(),0L));
            uUidApplyRecord.setUuidValidityYear(8); //默认8年
            uuidApplyRecordService.createUuidApplyRecord(uUidApplyRecord);
        }catch (Exception e){
            LOGGER.error("createUuidApplyRecord error",e);
            throw new BusinessException(UuidExceptionEnum.UUID_APPLY_FAIL);
        }
    }

    /**
     * 描述：编辑UUID订单-修改数量
     * @author maochengyuan
     * @created 2018/7/3 19:13
     * @param orderId 订单id
     * @param createNum UUID数量
     * @return void
     */
    @Override
    public void editOrderCreateNum(@RequestParam("orderId") String orderId, @RequestParam("createNum") Integer createNum, @RequestParam("tenantId")Long tenantId){
        if (StringUtil.isEmpty(orderId)){
            throw new BusinessException(UuidExceptionEnum.UUID_APPLY_ORDER_ISNULL);
        }
        if (createNum == null || createNum< 1){
            throw new BusinessException(UuidExceptionEnum.UUID_APPLY_CREATE_NUM_ERROR);
        }
        UUidApplyRecord record = this.uuidApplyRecordService.getUUidApplyRecordByOrderId(tenantId, orderId);
        if(record == null){
            throw new BusinessException(UuidExceptionEnum.UUID_APPLY_NOT_EXIST);
        }
        this.uuidApplyRecordService.editOrderCreateNum(record.getId(), createNum);
    }

    @Override
    public UuidApplyRecordResp getUuidApplyRecordInfo(@RequestParam("orderId")String orderId, @RequestParam("tenantId") Long tenantId) {
        if (StringUtil.isEmpty(orderId)){
            throw new BusinessException(UuidExceptionEnum.UUID_APPLY_ORDER_ISNULL);
        }
        if (tenantId == null){
            throw new BusinessException(UuidExceptionEnum.UUID_PARAM_ERROR, "tenantId is null");
        }
        UUidApplyRecord record = this.uuidApplyRecordService.getUUidApplyRecordByOrderId(tenantId, orderId);
        UuidApplyRecordResp resp = new UuidApplyRecordResp();
        BeanUtil.copyProperties(record,resp);
        return resp;
    }

    /**
     * @despriction：修改uuid申请记录的支付状态
     * @author  yeshiyuan
     * @created 2018/7/4 16:59
     * @param orderId 订单id
     * @param tenantId 租户id
     * @param payStatus 支付状态
     * @return
     */
    @Override
    public int updatePayStatus(@RequestParam("orderId") String orderId, @RequestParam("tenantId") Long tenantId, @RequestParam("payStatus") Integer payStatus, @RequestParam("oldPayStatus")Integer oldPayStatus) {
        if (StringUtil.isEmpty(orderId)){
            throw new BusinessException(UuidExceptionEnum.UUID_APPLY_ORDER_ISNULL);
        }
        if (tenantId == null){
            throw new BusinessException(UuidExceptionEnum.UUID_PARAM_ERROR, "tenantId is null");
        }
        if (!UuidPayStatusEnum.checkIsValid(payStatus) || !UuidPayStatusEnum.checkIsValid(oldPayStatus)){
            throw new BusinessException(UuidExceptionEnum.UUID_PARAM_ERROR, "pay status is error");
        }
        return uuidApplyRecordService.updatePayStatus(orderId, tenantId, payStatus, oldPayStatus, new Date());
    }

    /**
     * @despriction：专门给boss系统用的uuid申请
     * @author  yeshiyuan
     * @created 2018/6/29 14:16
     * @param uuidApplyForBoss
     * @return 批次号
     */
    @Override
    public Long uuidApplyForBoss(@RequestBody UuidApplyForBoss uuidApplyForBoss) {
        //1、校验租户是否是lds的
//        if (uuidApplyForBoss.getTenantId() == null){
//            throw new BusinessException(UuidExceptionEnum.UUID_APPLY_FAIL);
//        }
        //2、校验产品是否存在
        Product product = ProductServiceCoreUtils.getProductByProductId(uuidApplyForBoss.getProductId());
        if (product == null){
            throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
        }
        if(product.getCommunicationMode() == null){
            throw new BusinessException(ProductExceptionEnum.GOODS_ID_NOTNULL);
          }
        Long goodsId = Integer.valueOf(product.getCommunicationMode()).longValue();
        //3、产品是否属于当前租户
//        if (product.getTenantId().compareTo(uuidApplyForBoss.getTenantId())!=0){
//            throw new BusinessException(UuidExceptionEnum.UUID_PARAM_ERROR,"this product doesn't belong to current tenant");
//        }
        //4、数量校验
        if (uuidApplyForBoss.getCreateNum()==null || uuidApplyForBoss.getCreateNum().intValue()<1){
            throw new BusinessException(UuidExceptionEnum.UUID_PARAM_ERROR,"uuid create num must more than 1");
        }
        //5、生成uuid申请记录（特殊处理的）
        String orderId = "leedarson-" + Calendar.getInstance().getTimeInMillis();
        UUidApplyRecord uUidApplyRecord = new UUidApplyRecord();
        BeanUtil.copyProperties(uuidApplyForBoss,uUidApplyRecord);
        //兼容生产采用同一管理账户登录申请不同租户下的uuid
        uUidApplyRecord.setTenantId(product.getTenantId());
        uUidApplyRecord.setCreateTime(new Date());
        uUidApplyRecord.setPayStatus(UuidPayStatusEnum.PAYED.getValue());
        uUidApplyRecord.setUuidApplyStatus(UuidApplyStatusEnum.WAIT_DEAL.getValue());
        uUidApplyRecord.setGoodsId(goodsId);
//        Long batchId = RedisCacheUtil.incr(CacheKeyUtils.getTableOfUuidApplyRecordKey(),0L);
//        uUidApplyRecord.setId(batchId);
        uUidApplyRecord.setUuidValidityYear(8); //默认8年
        uUidApplyRecord.setOrderId(orderId);
        Long batchId = null;
		try {
			batchId = uuidApplyRecordService.createUuidApplyRecord(uUidApplyRecord);
		} catch (Exception e) {
			LOGGER.info("createUuidApplyRecord fail.");
		}
//        UUidApplyRecord uuidApplyRecord = uuidApplyRecordService.getUUidApplyRecordByOrderId(uuidApplyForBoss.getTenantId(), orderId);
        //生成uuid
        try {
			uuidManegerService.generateUUID(batchId);
		} catch (BusinessException e) {
			LOGGER.info("generateUUID fail.");
		}
        return batchId;
    }
}
