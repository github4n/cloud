package com.iot.device.vo.req;

import com.iot.common.exception.BusinessException;
import com.iot.device.enums.OperateStepRecordEnum;
import com.iot.device.exception.StepRecordExecptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：操作步骤记录入参
 * 创建人： yeshiyuan
 * 创建时间：2018/9/11 16:04
 * 修改人： yeshiyuan
 * 修改时间：2018/9/11 16:04
 * 修改描述：
 */
@ApiModel(value = "操作步骤记录入参")
public class OperateStepRecordReq {

    @ApiModelProperty(name = "operateId", value = "操作对象id", dataType = "Long")
    private Long operateId;
    @ApiModelProperty(name = "stepIndex", value = "步骤下标", dataType = "Integer")
    private Integer stepIndex;
    @ApiModelProperty(name = "operateType", value = "操作类型", dataType = "String")
    private String operateType;
    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;
    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Long")
    private Long userId;

    public Long getOperateId() {
        return operateId;
    }

    public void setOperateId(Long operateId) {
        this.operateId = operateId;
    }

    public Integer getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(Integer stepIndex) {
        this.stepIndex = stepIndex;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
      * @despriction：参数校验
      * @author  yeshiyuan
      * @created 2018/9/11 16:42
      * @return
      */
    public static boolean checkParam(OperateStepRecordReq recordReq) {
        if (recordReq == null){
            throw new BusinessException(StepRecordExecptionEnum.PARAM_ERROR, "OperateStepRecordReq is null");
        }
        if (recordReq.getOperateId() == null) {
            throw new BusinessException(StepRecordExecptionEnum.PARAM_ERROR, "operateId is null");
        }
        if (recordReq.getUserId() == null) {
            throw new BusinessException(StepRecordExecptionEnum.PARAM_ERROR, "userId is null");
        }
        if (recordReq.getStepIndex() == null || recordReq.getStepIndex().compareTo(1)==-1) {
            throw new BusinessException(StepRecordExecptionEnum.PARAM_ERROR, "stepIndex must more than 0");
        }
        if (!OperateStepRecordEnum.check(recordReq.getOperateType())) {
            throw new BusinessException(StepRecordExecptionEnum.PARAM_ERROR, "operateType error");
        }
        return true;
    }
}
