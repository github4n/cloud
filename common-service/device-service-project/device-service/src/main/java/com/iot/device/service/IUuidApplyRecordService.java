package com.iot.device.service;

import com.iot.device.model.UUidApplyRecord;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：uuid申请记录service
 * 创建人： yeshiyuan
 * 创建时间：2018/6/29 14:39
 * 修改人： yeshiyuan
 * 修改时间：2018/6/29 14:39
 * 修改描述：
 */
public interface IUuidApplyRecordService {
    
    /**
      * @despriction：添加uuid申请记录
      * @author  yeshiyuan
      * @created 2018/6/29 14:40
      * @param null
      * @return 
      */
	Long createUuidApplyRecord(UUidApplyRecord uUidApplyRecord);

    /**
      * @despriction：删除
      * @author  yeshiyuan
      * @created 2018/6/29 17:25
      * @param id uuid申请id
      * @return
      */
    int delete(Long id);

    /**
     * 描述：编辑UUID订单-修改数量
     * @author maochengyuan
     * @created 2018/7/3 19:13
     * @param applyId 申请记录id
     * @param createNum UUID数量
     * @return void
     */
    int editOrderCreateNum(Long applyId, Integer createNum);

    /**
     * 描述：依据订单id查询申请记录
     * @author maochengyuan
     * @created 2018/7/4 10:40
     * @param tenantId 租户id（可为空）
     * @param orderId orderId 订单id
     * @return com.iot.device.model.UUidApplyRecord
     */
    UUidApplyRecord getUUidApplyRecordByOrderId(Long tenantId, String orderId);


    /**
     * @despriction：修改uuid申请记录的支付状态
     * @author  yeshiyuan
     * @created 2018/7/4 16:59
     * @param orderId 订单id
     * @param 租户id 租户id
     * @param payStatus 支付状态
     * @return
     */
    int updatePayStatus(String orderId, Long tenantId, Integer payStatus, Integer oldPayStatus, Date updateTime) ;
}
