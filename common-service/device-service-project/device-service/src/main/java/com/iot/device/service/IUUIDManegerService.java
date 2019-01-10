package com.iot.device.service;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.device.dto.UUIDInfoDto;
import com.iot.device.dto.UUIDOrderDto;
import com.iot.device.vo.req.LicenseUsageReq;
import com.iot.device.vo.req.uuid.GenerateUUID;
import com.iot.device.vo.req.uuid.GetUUIDOrderReq;
import com.iot.device.vo.req.uuid.GetUUIDReq;
import com.iot.device.vo.req.uuid.UUIDRefundOperateReq;
import com.iot.device.vo.req.uuid.UUIDRefundReq;
import com.iot.device.vo.req.uuid.UUidApplyRecordInfo;

public interface IUUIDManegerService {

    /**
     *
     * 描述：UUID生成
     * @author 李帅
     * @created 2018年4月12日 下午2:49:10
     * @since
     * @param generateUUID
     * @throws BusinessException
     */
    public Long generateUUID(Long batchNum) throws BusinessException;

    /**
     *
     * 描述：通过批次号获取证书下载URL
     * @author 李帅
     * @created 2018年6月8日 下午1:59:27
     * @since
     * @param tenantId
     * @param userId
     * @param batchNum
     * @return
     * @throws BusinessException
     */
    public String getDownloadUUID(Long tenantId, String userId, Long batchNum) throws BusinessException;

    /**
     * 描述：查询UUID申请订单
     * @author shenxiang
     * @created 2018年6月29日 下午1:59:27
     * @param uuidOrderReq
     * @return
     * @throws BusinessException
     */
    Page<UUIDOrderDto> getUUIDOrder(GetUUIDOrderReq uuidOrderReq) throws BusinessException;

    /**
     * 描述：查询UUID信息，可以查询某批次或某个UUID,也可以根据状态查询，
     * @author shenxiang
     * @created 2018年6月29日 下午1:59:27
     * @param uuidReq
     * @return
     * @throws BusinessException
     */
    Page<UUIDInfoDto> getUUIDInfo(GetUUIDReq uuidReq) throws BusinessException;
    
    /**
     * 描述：上报总装测试数据
     *
     * @param licenseUsageReq
     * @return
     * @throws BusinessException
     * @author chq
     * @created 2018年6月27日 下午2:59:27
     * @since
     */
    public void licenseUsage(LicenseUsageReq licenseUsageReq) throws BusinessException;

    /**
     * 
     * 描述：证书生成补偿
     * @author 李帅
     * @created 2018年8月24日 上午9:53:15
     * @since 
     * @throws BusinessException
     */
    public void generateCompensate() throws BusinessException;

    public GenerateUUID getGenerateUUIDInfo(Long batchNum);
    
    /**
     * 
     * 描述：UUID订单退款前操作
     * @author 李帅
     * @created 2018年11月14日 下午4:50:38
     * @since 
     * @param uuidRefundReq
     */
    UUidApplyRecordInfo beforeUUIDRefund(UUIDRefundReq uuidRefundReq);
    
    /**
     * 
     * 描述：UUID订单退款成功操作
     * @author 李帅
     * @created 2018年11月15日 下午4:00:42
     * @since 
     * @param refundOperateReq
     */
    void refundSuccess(UUIDRefundOperateReq refundOperateReq);
    
    /**
     * 
     * 描述：UUID订单退款失败操作
     * @author 李帅
     * @created 2018年11月15日 下午3:59:51
     * @since 
     * @param refundOperateReq
     */
    void refundFail(UUIDRefundOperateReq refundOperateReq);
}
