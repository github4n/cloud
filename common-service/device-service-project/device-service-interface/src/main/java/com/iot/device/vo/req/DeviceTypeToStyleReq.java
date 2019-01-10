package com.iot.device.vo.req;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
public class DeviceTypeToStyleReq {

    public List styleTemplateIds;
    private Long id;
    private Long deviceTypeId;
    private Long styleTemplateId;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private String description;
    /**
     * 租户ID
     */
    private Long tenantId;


}
