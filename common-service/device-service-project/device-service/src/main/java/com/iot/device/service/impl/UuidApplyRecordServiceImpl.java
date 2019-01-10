package com.iot.device.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.device.mapper.UuidApplyRecordMapper;
import com.iot.device.model.UUidApplyRecord;
import com.iot.device.service.IUuidApplyRecordService;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/6/29 14:44
 * 修改人： yeshiyuan
 * 修改时间：2018/6/29 14:44
 * 修改描述：
 */
@Service
public class UuidApplyRecordServiceImpl implements IUuidApplyRecordService {

    @Autowired
    private UuidApplyRecordMapper uuidApplyRecordMapper;

    @Override
    public Long createUuidApplyRecord(UUidApplyRecord uUidApplyRecord) {
        uuidApplyRecordMapper.add(uUidApplyRecord);
        return uUidApplyRecord.getId();
    }

    @Override
    public int delete(Long id) {
        return uuidApplyRecordMapper.delete(id);
    }

    /**
     * 描述：编辑UUID订单-修改数量
     * @author maochengyuan
     * @created 2018/7/3 19:13
     * @param applyId 申请记录id
     * @param createNum UUID数量
     * @return void
     */
    @Override
    public int editOrderCreateNum(Long applyId, Integer createNum){
        return this.uuidApplyRecordMapper.editOrderCreateNum(applyId, createNum, new Date());
    }

    /**
     * 描述：依据订单id查询申请记录
     * @author maochengyuan
     * @created 2018/7/4 10:40
     * @param tenantId 租户id（可为空）
     * @param orderId orderId 订单id
     * @return com.iot.device.model.UUidApplyRecord
     */
    @Override
    public UUidApplyRecord getUUidApplyRecordByOrderId(Long tenantId, String orderId){
        return this.uuidApplyRecordMapper.getUUidApplyRecordByOrderId(tenantId, orderId);
    }

    /**
     * @despriction：修改uuid申请记录的支付状态
     * @author  yeshiyuan
     * @created 2018/7/4 16:59
     * @param orderId 订单id
     * @param 租户id 租户id
     * @param payStatus 支付状态
     * @param oldPayStatus 旧支付状态
     * @return
     */
    @Override
    public int updatePayStatus(String orderId, Long tenantId, Integer payStatus, Integer oldPayStatus, Date updateTime) {
        return this.uuidApplyRecordMapper.updatePayStatus(orderId, tenantId, payStatus, oldPayStatus, updateTime);
    }
}
