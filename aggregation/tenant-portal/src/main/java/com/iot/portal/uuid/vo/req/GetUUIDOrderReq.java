package com.iot.portal.uuid.vo.req;

import com.iot.common.beans.SearchParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 查询UUID订单列表请求
 */
@ApiModel(value = "查询UUID订单列表请求", description = "查询UUID订单列表请求")
public class GetUUIDOrderReq extends SearchParam {

    @ApiModelProperty(value="查询条件condition",name="condition")
    private String condition;

    @ApiModelProperty(value="批次号",name="batchNumId")
    private Long batchNumId;

    @ApiModelProperty(value="产品ID",name="strProductId")
    private String strProductId;

    /** 订单状态 */
    @ApiModelProperty(value="订单状态",name="applyStatus")
    private Integer applyStatus;

    @ApiModelProperty(value="开始时间",name="start")
    private Date start;

    @ApiModelProperty(value="结束时间",name="end")
    private Date end;

    public Long getBatchNumId() {
        return batchNumId;
    }

    public void setBatchNumId(Long batchNumId) {
        this.batchNumId = batchNumId;
    }

    public String getStrProductId() {
        return strProductId;
    }

    public void setStrProductId(String strProductId) {
        this.strProductId = strProductId;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "GetUUIDOrderReq{" +
                "condition='" + condition + '\'' +
                ", batchNumId=" + batchNumId +
                ", strProductId='" + strProductId + '\'' +
                ", applyStatus=" + applyStatus +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
