package com.iot.device.vo.rsp.group;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
public class GetGroupDetailResp {
    /**
     * id
     */
    private Long id;

    /**
     * 组名称
     */
    private Long groupId;
    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 创建者id
     */
    private Long createBy;
    /**
     * 更新者id
     */
    private Long updateBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    private List<String> members;


}
