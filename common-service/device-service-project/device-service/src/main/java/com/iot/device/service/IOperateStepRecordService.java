package com.iot.device.service;

import com.iot.device.model.OperateStepRecord;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：IOperateStepRecordService
 * 创建人： yeshiyuan
 * 创建时间：2018/9/11 15:20
 * 修改人： yeshiyuan
 * 修改时间：2018/9/11 15:20
 * 修改描述：
 */
public interface IOperateStepRecordService {

    /**
     * @despriction：保存操作步骤记录
     * @author  yeshiyuan
     * @created 2018/9/11 15:31
     * @param null
     * @return
     */
    int insert(OperateStepRecord operateStepRecord);

    /**
      * @despriction：查询当前所处步骤
      * @author  yeshiyuan
      * @created 2018/9/11 16:25
      * @return
      */
    OperateStepRecord currentStep(Long operateId, String operateType, Long tenantId);

    /**
      * @despriction：查询某一步的操作记录是否存在
      * @author  yeshiyuan
      * @created 2018/9/11 20:29
      * @return
      */
    Long queryExists(Long operateId, String operateType, Long tenantId, Integer stepIndex);

    /**
      * @despriction：修改操作步骤记录
      * @author  yeshiyuan
      * @created 2018/9/11 20:36
      * @return
      */
    int update(Long id, Long userId, Date updateTime);
}
