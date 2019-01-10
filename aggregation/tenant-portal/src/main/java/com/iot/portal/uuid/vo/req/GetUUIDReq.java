package com.iot.portal.uuid.vo.req;

import com.iot.common.beans.SearchParam;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 查询UUID请求，可以查询某个批次的UUID，也可以查询具体的UUID
 */
public class GetUUIDReq extends SearchParam {

    @ApiModelProperty(value="批次号",name="batchNumId")
    private Long batchNumId;

    @ApiModelProperty(value="UUID",name="uuid")
    private String uuid;

    @ApiModelProperty(value="激活状态",name="activeStatus")
    private Integer activeStatus;

    @ApiModelProperty(value="查询时间条件类型，1-首次激活时间,2-最近上线时间",name="timeType")
    private Integer timeType;

    @ApiModelProperty(value="开始时间",name="timeStart")
    private Date timeStart;

    @ApiModelProperty(value="结束时间",name="timeEnd")
    private Date timeEnd;

    public Long getBatchNumId() {
        return batchNumId;
    }

    public void setBatchNumId(Long batchNumId) {
        this.batchNumId = batchNumId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public String toString() {
        return "GetUUIDReq{" +
                "batchNumId=" + batchNumId +
                ", uuid='" + uuid + '\'' +
                ", activeStatus=" + activeStatus +
                ", timeType=" + timeType +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                '}';
    }
}
