package com.iot.device.service.impl;

import com.iot.device.mapper.OperateStepRecordMapper;
import com.iot.device.model.OperateStepRecord;
import com.iot.device.service.IOperateStepRecordService;
import com.iot.device.vo.req.OperateStepRecordReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：OperateStepRecordServiceImpl
 * 创建人： yeshiyuan
 * 创建时间：2018/9/11 15:20
 * 修改人： yeshiyuan
 * 修改时间：2018/9/11 15:20
 * 修改描述：
 */
@Service
public class OperateStepRecordServiceImpl implements IOperateStepRecordService {

    @Autowired
    private OperateStepRecordMapper recordMapper;

    /**
     * @despriction：保存操作步骤记录
     * @author  yeshiyuan
     * @created 2018/9/11 15:31
     * @param null
     * @return
     */
    @Override
    public int insert(OperateStepRecord operateStepRecord) {
        return recordMapper.insert(operateStepRecord);
    }

    /**
     * @despriction：查询当前所处步骤
     * @author  yeshiyuan
     * @created 2018/9/11 16:25
     * @return
     */
    @Override
    public OperateStepRecord currentStep(Long operateId, String operateType, Long tenantId) {
        return recordMapper.queryCurrentStep(operateId, operateType, tenantId);
    }

    /**
     * @despriction：查询某一步的操作记录是否存在
     * @author  yeshiyuan
     * @created 2018/9/11 20:29
     * @return
     */
    @Override
    public Long queryExists(Long operateId, String operateType, Long tenantId, Integer stepIndex) {
        return recordMapper.queryExists(operateId, operateType, tenantId, stepIndex);
    }

    /**
     * @despriction：修改操作步骤记录
     * @author  yeshiyuan
     * @created 2018/9/11 20:36
     * @return
     */
    @Override
    public int update(Long id, Long userId, Date updateTime) {
        return recordMapper.update(id, userId, updateTime);
    }
}
