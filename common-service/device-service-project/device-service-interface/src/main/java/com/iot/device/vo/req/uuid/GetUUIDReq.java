package com.iot.device.vo.req.uuid;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iot.common.beans.SearchParam;

import java.util.Date;

/**
 * 查询UUID请求，可以查询某个批次的UUID，也可以查询具体的UUID
 */
public class GetUUIDReq extends SearchParam {

    private Long tenantId;
    /** 订单Id,批次号 */
    private Long batchNumId;
    /** 指定UUID */
    private String uuid;
    /** 激活状态 */
    private Integer activeStatus;
    /** 查询时间条件类型，1-首次激活时间,2-最近上线时间 */
    private Integer timeType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
