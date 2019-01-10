package com.iot.device.controller;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.device.api.OperateStepRecordApi;
import com.iot.device.exception.StepRecordExecptionEnum;
import com.iot.device.model.OperateStepRecord;
import com.iot.device.service.IOperateStepRecordService;
import com.iot.device.vo.req.OperateStepRecordReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/9/11 16:22
 * 修改人： yeshiyuan
 * 修改时间：2018/9/11 16:22
 * 修改描述：
 */
@RestController
public class OperateStepRecordController implements OperateStepRecordApi {

    @Autowired
    private IOperateStepRecordService recordService;

    /**
     * @despriction：查询当前所处步骤
     * @author  yeshiyuan
     * @created 2018/9/11 16:17
     * @return
     */
    @Override
    public int currentStep(@RequestParam("operateId") Long operateId, @RequestParam("tenantId") Long tenantId, @RequestParam("operateType") String operateType) {
        if (operateId == null || tenantId==null || StringUtil.isBlank(operateType)){
            throw new BusinessException(StepRecordExecptionEnum.PARAM_ERROR);
        }
        OperateStepRecord record = recordService.currentStep(operateId, operateType, tenantId);
        if (record==null){
            return 1;
        }
        return record.getStepIndex()+1;
    }

    /**
     * @despriction：保存操作步骤记录
     * @author  yeshiyuan
     * @created 2018/9/11 16:21
     * @return
     */
    @Override
    public void save(@RequestBody OperateStepRecordReq recordReq) {
        //参数校验
        OperateStepRecordReq.checkParam(recordReq);
        //查询上一步骤下标
        OperateStepRecord previousPecord = recordService.currentStep(recordReq.getOperateId(), recordReq.getOperateType(), recordReq.getTenantId());
        if (previousPecord!=null){
            int previousStep = previousPecord.getStepIndex();
            //防止跳过步骤，必须 previousStep + 1 == 提交的步骤
            if (recordReq.getStepIndex().equals(previousStep + 1)){
                OperateStepRecord operateStepRecord = new OperateStepRecord();
                operateStepRecord.setTenantId(recordReq.getTenantId());
                operateStepRecord.setOperateId(recordReq.getOperateId());
                operateStepRecord.setOperateType(recordReq.getOperateType());
                operateStepRecord.setStepIndex(recordReq.getStepIndex());
                operateStepRecord.setCreateTime(new Date());
                operateStepRecord.setCreateBy(recordReq.getUserId());
                recordService.insert(operateStepRecord);
            }else{
                //校验是否是修改之前的步骤，其stepIndex在数据库有记录
                Long id = recordService.queryExists(recordReq.getOperateId(), recordReq.getOperateType(), recordReq.getTenantId(), recordReq.getStepIndex());
                if (id == null) {
                    throw new BusinessException(StepRecordExecptionEnum.PARAM_ERROR, "you skipped the previous steps");
                }
                recordService.update(id, recordReq.getUserId(), new Date());
            }
        }else{
            //未存在记录，校验传过来的stepIndex是否为1
            if (!recordReq.getStepIndex().equals(1)){
                throw new BusinessException(StepRecordExecptionEnum.PARAM_ERROR, "operate start, stepIndex must be 1");
            }
            //插入新的记录
            OperateStepRecord operateStepRecord = new OperateStepRecord();
            operateStepRecord.setTenantId(recordReq.getTenantId());
            operateStepRecord.setOperateId(recordReq.getOperateId());
            operateStepRecord.setOperateType(recordReq.getOperateType());
            operateStepRecord.setStepIndex(recordReq.getStepIndex());
            operateStepRecord.setCreateTime(new Date());
            operateStepRecord.setCreateBy(recordReq.getUserId());
            recordService.insert(operateStepRecord);
        }
    }
}
