package com.iot.portal.web.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：保存操作步骤记录入参
 * 创建人： yeshiyuan
 * 创建时间：2018/9/12 10:27
 * 修改人： yeshiyuan
 * 修改时间：2018/9/12 10:27
 * 修改描述：
 */
@ApiModel(value = "保存操作步骤记录入参")
public class SaveOperateStepRecordReq {

    @ApiModelProperty(name = "operateId", value = "操作对象id", dataType = "Long")
    private Long operateId;
    @ApiModelProperty(name = "stepIndex", value = "步骤下标", dataType = "Integer")
    private Integer stepIndex;
    @ApiModelProperty(name = "operateType", value = "操作类型", dataType = "String")
    private String operateType;

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
}
