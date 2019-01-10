package com.iot.device.vo.rsp.group;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class GetGroupInfoResp {
    /**
     * id
     */
    private Long id;

    private Long groupId;
    /**
     * 组名称
     */
    private String name;
    /**
     * 空间id
     */
    private Long homeId;
    /**
     * 设备控制组Id
     */
    private Integer devGroupId;
    /**
     * 图标
     */
    private String icon;
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


}
